package br.com.cas10.pgman.service

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
class PGManagerDAO {

	private NamedParameterJdbcTemplate jdbc
	
	@Autowired
	void setDataSource(DataSource dataSource) {
		jdbc = new NamedParameterJdbcTemplate(dataSource)
	}

	void updateDbSizes() {
		jdbc.update("""insert into pgman_database_sizes (id, moment, database, size) 
					select nextval('pgman_seq'), now(), datname, pg_database_size(datname) 
						FROM pg_database""", new HashMap<String, Object>());
	}
	
	void cleanDbSizes(int daysToKeep) {
		GregorianCalendar breakPoint = new GregorianCalendar()
		breakPoint.add(Calendar.DAY_OF_YEAR, -daysToKeep)
		jdbc.update("""delete from pgman_database_sizes where moment < :breakPoint""", (Map<String, Object>)["breakPoint" : breakPoint]);
	}
}
