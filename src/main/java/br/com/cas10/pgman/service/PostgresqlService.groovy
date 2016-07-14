package br.com.cas10.pgman.service

import br.com.cas10.pgman.domain.Parameter
import br.com.cas10.pgman.domain.ParameterCategory

import javax.sql.DataSource

import org.mapdb.BTreeMap;
import org.mapdb.DB
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import br.com.cas10.pgman.domain.Database
import br.com.cas10.pgman.utils.SizeUtils;

@Service
@Transactional(readOnly = true)
class PostgresqlService {

    private NamedParameterJdbcTemplate jdbc

    @Autowired
    private DatabaseService databaseService

    @Autowired
    private DB mapDB;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbc = new NamedParameterJdbcTemplate(dataSource);
    }

    public List<Map<String,Object>> getTopRelationSizes(Integer top, String database, Long threshouldSizeBytes) {
        NamedParameterJdbcTemplate tmpl = jdbc
        if (database != null) {
            tmpl = databaseService.getTemplateForDb(database);
        }

        Map params = new HashMap<String, Object>();
        if (threshouldSizeBytes == null) {
            threshouldSizeBytes = 0;
        }
        params["threshouldSizeBytes"] = threshouldSizeBytes

        String topClause = "";
        if (top != null && top > 0) {
            topClause = "LIMIT :top"
            params["top"] = top;
        };

        String query = """
			SELECT C.oid,
				nspname || '.' || relname AS "relation",
				pg_size_pretty(pg_total_relation_size(C.oid)) AS "size",
				pg_total_relation_size(C.oid) AS sizebytes,
				pg_size_pretty(pg_indexes_size(C.oid)) AS "indexsize",
				pg_indexes_size(C.oid) AS indexsizebytes
			FROM pg_class C
				LEFT JOIN pg_namespace N ON (N.oid = C.relnamespace)
			WHERE nspname NOT IN ('information_schema')
				AND C.relkind <> 'i'
				AND nspname !~ '^pg_toast'
				AND pg_total_relation_size(C.oid) > :threshouldSizeBytes
			ORDER BY pg_total_relation_size(C.oid) DESC
			${topClause}
			"""
        List<Map<String,Object>> result = tmpl.queryForList(query, params);
        return result;
    }

    Map<String,Object> getTableDetails(Long oid, String database) {
        Map<String,Object> result = [:]
        NamedParameterJdbcTemplate tmpl = jdbc
        if (database != null) {
            tmpl = databaseService.getTemplateForDb(database);
        }

        Map params = new HashMap<String, Object>();
        params["oid"] = oid

        String query = """
			SELECT c.relname AS index, 
				pg_size_pretty(pg_relation_size(i.indexrelid)) AS size, 
				i.indisunique as un, 
				i.indisprimary as pk
			FROM pg_index i
				INNER JOIN pg_class c ON i.indexrelid = c.oid
			WHERE indrelid = :oid
			ORDER BY relname
			"""
        result['indexes'] = []
        tmpl.queryForList(query, params).each { row ->
            result['indexes'] << ['name' : row.index, 'size' : row.size, 'unique' : row.un, 'primaryKey' : row.pk]
        }

        query = """
			SELECT t.relname AS toast, pg_size_pretty(pg_relation_size(t.oid)) AS toastSize, 
				ti.relname AS toastIndex, pg_size_pretty(pg_relation_size(ti.oid)) AS toastIndexSize
			FROM pg_class c
					INNER JOIN pg_class t on c.reltoastrelid = t.oid
					LEFT JOIN pg_class ti on t.reltoastidxid = ti.oid
			WHERE c.oid = :oid
			"""
        result['toasts'] = []
        tmpl.queryForList(query, params).each { row ->
            result['toasts'] << ['toast' : row.toast, 'toastSize' : row.toastSize, 'toastIndex' : row.toastIndex, 'toastIndexSize' : row.toastIndexSize]
        }

        return result;
    }

    public List<Map<String,Object>> getTopDatabaseSizes(Integer top) {
        List<Map<String,Object>> result = new ArrayList<>();
        BTreeMap<String, Database> databases = mapDB.getTreeMap("databases");
        databases.values().each { Database database ->
            result.add(["dbname": database.name, "size": database.prettyPrintSize, "_sizeBytes": database.size]);
        }
        Collections.sort(result, { Map<String,Object> o1, Map<String,Object> o2 ->
                return -o1._sizeBytes.compareTo(o2._sizeBytes);
            } as Comparator<Map<String,Object>>)
        return result;
    }

    public List<Map<String,Object>> getDatabaseStats() {
        String query = """
            SELECT datname as database, numbackends, xact_commit, xact_rollback, blks_read, blks_hit, 
                    tup_returned, tup_fetched, tup_inserted, tup_updated, tup_deleted 
            FROM pg_stat_database
            WHERE datname not like 'template%'
            ORDER BY datname
            """
        List<Map<String,Object>> result = jdbc.queryForList(query, new HashMap<String, Object>());
        return result;
    }

    public List<Map<String,Object>> getCacheStats() {
        String query = """
			SELECT datname as database, 
				round(CAST(((xact_rollback::float * 100) / (xact_commit + xact_rollback))AS numeric),4) AS rollback_rate, 
				round(CAST(((blks_hit::float * 100) / (blks_read + blks_hit))AS numeric),4) AS cache_rate 
			FROM pg_stat_database 
			WHERE (blks_read + blks_hit) > 0 AND (xact_commit + xact_rollback) > 0 
			ORDER BY datname
			"""
        List<Map<String,Object>> result = jdbc.queryForList(query, new HashMap<String, Object>());
        return result;
    }

    public List<Map<String,Object>> getTopStatements(Integer top) {
        if (!top) top = 20;
        String query = """
			SELECT DATNAME as DATABASE, 
				CALLS,
				round(TOTAL_TIME::numeric, 4) AS TOTAL_TIME,
				round((TOTAL_TIME / CALLS)::numeric, 4) AS AVG_TIME, 
				ROWS, QUERY, BLK_READ_TIME, BLK_WRITE_TIME 
			FROM PG_STAT_STATEMENTS S 
				INNER JOIN PG_DATABASE D ON S.DBID = D.OID 
			ORDER BY TOTAL_TIME DESC
			LIMIT :top
			"""
        List<Map<String,Object>> result = jdbc.queryForList(query, ["top":top]);
        return result;
    }

    public List<Map<String, Object>> getMissingIndexesForForeignKeys(String database) {
        NamedParameterJdbcTemplate tmpl = jdbc
        if (database != null) {
            tmpl = databaseService.getTemplateForDb(database);
        }
        String query = """select 'create index ' || relname || '_' ||
			         array_to_string(column_name_list, '_') || '_idx on ' || conrelid ||
			         ' (' || array_to_string(column_name_list, ',') || ')' as command
			from (select distinct
			       conrelid,
			       array_agg(attname) column_name_list,
			       array_agg(attnum) as column_list
			     from pg_attribute
			          join (select conrelid::regclass,
			                 conname,
			                 unnest(conkey) as column_index
			                from (select distinct
			                        conrelid, conname, conkey
			                      from pg_constraint
			                        join pg_class on pg_class.oid = pg_constraint.conrelid
			                        join pg_namespace on pg_namespace.oid = pg_class.relnamespace
			                      where nspname !~ '^pg_' and nspname <> 'information_schema'
			                      ) fkey
			               ) fkey
			               on fkey.conrelid = pg_attribute.attrelid
			                  and fkey.column_index = pg_attribute.attnum
			     group by conrelid, conname
			     ) candidate_index
			join pg_class on pg_class.oid = candidate_index.conrelid
			left join pg_index on pg_index.indrelid = conrelid
			                      and indkey::text = array_to_string(column_list, ' ')
			where indexrelid is null
			order by 1"""
        List<Map<String,Object>> result = tmpl.queryForList(query, new HashMap<String, Object>());
        return result;
    }

    public String getVersion() {
        String query = """SELECT version() AS version"""
        List<Map<String,Object>> result = jdbc.queryForList(query, [:]);
        return result[0].version;
    }

    public List<Map<String,Object>> getActivity() {
        String query = 	"""
			SELECT
				pg_stat_activity.pid AS pid,
				CASE WHEN LENGTH(pg_stat_activity.datname) > 16
					THEN SUBSTRING(pg_stat_activity.datname FROM 0 FOR 6)||'...'||SUBSTRING(pg_stat_activity.datname FROM '........\$')
					ELSE pg_stat_activity.datname
					END
				AS database,
				pg_stat_activity.client_addr AS client,
				EXTRACT(epoch FROM (NOW() - pg_stat_activity.query_start)) AS duration,
				pg_stat_activity.waiting AS wait,
				pg_stat_activity.usename AS user,
				pg_stat_activity.query AS query
			FROM
				pg_stat_activity
			WHERE
				state <> 'idle'
				AND pid <> pg_backend_pid()
			ORDER BY
				EXTRACT(epoch FROM (NOW() - pg_stat_activity.query_start)) DESC"""

        List<Map<String,Object>> result = jdbc.queryForList(query, [:]);
        return result;
    }

    public List<ParameterCategory> getSettings(boolean nonDefaultOnly) {
        String clause = ""
        if (nonDefaultOnly) {
            clause = "WHERE setting <> boot_val"
        }
        String query = 	"""
			SELECT
				name, setting, unit, category, short_desc, 
				extra_desc, context, vartype, source, 
				min_val, max_val, enumvals, boot_val, 
				reset_val, sourcefile, sourceline
			FROM pg_settings ${clause}
			ORDER BY category, name"""

        List<Map<String,Object>> result = jdbc.queryForList(query, [:]);
        List<ParameterCategory> list = new ArrayList<>()
        ParameterCategory category = new ParameterCategory("")
        result.each { item ->
            if (category.name != item.category) {
                category = new ParameterCategory(item.category)
                list << category
            }
            String val = item.setting
            if (item.unit) {
                val += " (${item.unit})"
            }
            category.parameters << new Parameter(item.name, val, item.boot_val, item.extra_desc, item.source)
        };
        return list;
    }

    @Transactional(readOnly = false)
    public void resetStatementsStats() {
        jdbc.queryForList("SELECT pg_stat_statements_reset()", new HashMap<String, Object>())
    }
}
