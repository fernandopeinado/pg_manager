package br.com.cas10.pgman.service

import java.util.HashMap;

import javax.sql.DataSource

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class PostgresqlService {

	private NamedParameterJdbcTemplate jdbc

	@Autowired
	private DatabaseService databaseService
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		jdbc = new NamedParameterJdbcTemplate(dataSource);
	}

	public List<Map<String,Object>> getTopRelationSizes(Integer top, String database) {
		NamedParameterJdbcTemplate tmpl = jdbc
		if (database != null) {
			tmpl = databaseService.getTemplateForDb(database);
		}
		String topClause = "";
		Map params = new HashMap<String, Object>();
		if (top != null && top > 0) {
			topClause = "LIMIT :top"
			params["top"] = top;
		};
		String query = """
			SELECT nspname || '.' || relname AS "relation"
				, CASE WHEN reltype = 0
					THEN pg_size_pretty(pg_total_relation_size(C.oid))		        
					ELSE pg_size_pretty(pg_relation_size(C.oid))
				  END AS "size"
		  	FROM pg_class C
		  		LEFT JOIN pg_namespace N ON (N.oid = C.relnamespace)
		  	WHERE nspname NOT IN ('pg_catalog', 'information_schema')
				AND pg_relation_size(C.oid) > 0
		  	ORDER BY pg_relation_size(C.oid) DESC
		  	${topClause}
			"""
		List<Map<String,Object>> result = tmpl.queryForList(query, params);
		return result;
	}

	public List<Map<String,Object>> getTopDatabaseSizes(Integer top) {
		String topClause = "";
		Map params = new HashMap<String, Object>();
		if (top != null && top > 0) {
			topClause = "LIMIT :top"
			params["top"] = top;
		};
		String query = """
			SELECT datname as dbname, pg_size_pretty(pg_database_size(datname)) AS size 
			FROM pg_database 
			ORDER BY pg_database_size(datname) DESC
			${topClause}
			"""
		List<Map<String,Object>> result = jdbc.queryForList(query, ["top":top]);
		return result;
	}

	public List<Map<String,Object>> getDatabaseStats() {
		String query = """
			SELECT datname as database, numbackends, xact_commit, xact_rollback, blks_read, blks_hit, 
				tup_returned, tup_fetched, tup_inserted, tup_updated, tup_deleted 
			FROM pg_stat_database
			WHERE xact_commit > 0 OR blks_read > 0
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
				ROWS, QUERY 
			FROM PG_STAT_STATEMENTS S 
				INNER JOIN PG_DATABASE D ON S.DBID = D.OID 
			ORDER BY TOTAL_TIME DESC
			LIMIT :top
			"""
		List<Map<String,Object>> result = jdbc.queryForList(query, ["top":top]);
		return result;
	}
	
	public void resetStatementsStats() {
		jdbc.queryForList("SELECT pg_stat_statements_reset()", new HashMap<String, Object>())
	}
	
	public List<Map<String,Object>> getAuditStats() {
		String query = """
			SELECT ENTITYCLASSNAME AS ENTITY, 
				COUNT(DISTINCT SERIALIZEDPROPERTIESCOMPRESSED) AS TOTAL, 
				SUM(LENGTH(LO.DATA)) AS SIZE,
				SUM(LENGTH(LO.DATA))/COUNT(DISTINCT SERIALIZEDPROPERTIESCOMPRESSED) AS AVG
			FROM AUDI_AUDITENTRY A 
				INNER JOIN PG_LARGEOBJECT LO ON A.SERIALIZEDPROPERTIESCOMPRESSED = LO.LOID
			GROUP BY ENTITYCLASSNAME 
			ORDER BY 3 DESC;
			"""
		List<Map<String,Object>> result = jdbc.queryForList(query, new HashMap<String, Object>());
		return result;
	}
}
