package br.com.cas10.pgman.service

import br.com.cas10.pgman.domain.Database
import br.com.cas10.pgman.domain.PgExtension
import org.mapdb.BTreeMap
import org.mapdb.DB
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import javax.sql.DataSource
import java.sql.ResultSet

@Service
@Transactional(readOnly = true)
class PgExtensionDAO {

    private NamedParameterJdbcTemplate jdbc

    @Autowired
    private DatabaseService databaseService

    public List<PgExtension> getExtensions(String database) {
        NamedParameterJdbcTemplate tmpl = databaseService.getTemplateForDb(database)
        Map params = new HashMap<String, Object>()
        String query = """
            select e.name,
                e.default_version,
                e.installed_version,
                e.comment
            from pg_available_extensions e
			ORDER BY e.name
			"""
        List<PgExtension> result = tmpl.query(query, params, { ResultSet row, int i ->
            new PgExtension(name: row.getString(1),
                    defaultVersion: row.getString(2),
                    installedVersion: row.getString(3),
                    comment: row.getString(4))
        } as RowMapper<PgExtension>)
        return result
    }

    @Transactional
    public void createExtension(String database, String extName) {
        NamedParameterJdbcTemplate tmpl = databaseService.getTemplateForDb(database)
        Map params = new HashMap<String, Object>()
        tmpl.update("CREATE EXTENSION ${extName}", params)
    }
}
