package br.com.cas10.pgman.service

import liquibase.Liquibase
import liquibase.database.DatabaseConnection
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import javax.annotation.PostConstruct
import javax.sql.DataSource
import java.sql.Connection

@Service
@Transactional
class InitializationService {

	private DataSource ds;
	private NamedParameterJdbcTemplate jdbc

	@Autowired
	public void setDataSource(DataSource dataSource) {
		ds = dataSource;
		jdbc = new NamedParameterJdbcTemplate(dataSource)
	}

	@PostConstruct
	public void initialize() {
		if (!tableExists("pg_stat_statements")) {
			execute("CREATE EXTENSION PG_STAT_STATEMENTS")
		}
		Connection con = ds.connection
		try {
			DatabaseConnection connection = new JdbcConnection(con)
			Liquibase liquibase = new Liquibase("br/com/cas10/pgman/changelog/db.changelog-master.xml", new ClassLoaderResourceAccessor(), connection)
			liquibase.update("")
		} finally {
			con.close()
		}
	}

	private boolean tableExists(String table) {
		try {
			jdbc.queryForObject("SELECT 1 FROM ${table} LIMIT 1", Collections.emptyMap(), Number.class)
			return true
		} catch (Exception e) {
			return false
		}
	}

	private void execute(String sql) {
		try {
			jdbc.update(sql, Collections.emptyMap())
		} catch (Exception e) {
			e.printStackTrace()
		}
	}
}
