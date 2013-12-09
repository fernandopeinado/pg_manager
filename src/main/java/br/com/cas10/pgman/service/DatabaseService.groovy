package br.com.cas10.pgman.service

import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.sql.DataSource

import org.postgresql.ds.PGConnectionPoolDataSource;
import org.postgresql.ds.PGPoolingDataSource;
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class DatabaseService {
	
	private Map<String, PGPoolingDataSource> databaseConnections = new HashMap<String, PGPoolingDataSource>();
	
	private NamedParameterJdbcTemplate jdbc;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		jdbc = new NamedParameterJdbcTemplate(dataSource);
	}

	@PostConstruct
	public void initDatabases() {
		String query = "SELECT datname as dbname FROM pg_database WHERE datistemplate = false"
		List<Map<String,Object>> result = jdbc.queryForList(query, Collections.emptyMap())
		result.each { 
			PGPoolingDataSource ds = new PGPoolingDataSource()
			ds.setDatabaseName(it.dbname)
			ds.setUser("postgres")
			databaseConnections.put(it.dbname, ds)
		}
		println databaseConnections
	}
	
	public NamedParameterJdbcTemplate getTemplateForDb(String name) {
		return new NamedParameterJdbcTemplate(databaseConnections[name])
	}
	
	public Collection<String> getDatabases() {
		this.databaseConnections.keySet()
	}
}
