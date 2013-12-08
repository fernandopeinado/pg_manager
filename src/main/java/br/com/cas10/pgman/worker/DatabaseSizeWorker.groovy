package br.com.cas10.pgman.worker

import groovy.util.logging.Log4j;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.cas10.pgman.service.PGManagerDAO;
import br.com.cas10.pgman.service.PostgresqlService;

@Component
@Log4j
class DatabaseSizeWorker extends Worker {

	private static final Pattern PATTERN = Pattern.compile("\\s+(\\d+)\\s(.*)")

	@Autowired
	private PGManagerDAO pgmanagerDAO;

	DatabaseSizeWorker() {
		super("databaseSize", "0 0 0/6 * * ?")
	}

	@Override
	public void run() {
		log.info("Database Size Worker - Running")
		pgmanagerDAO.updateDbSizes()
	}
	
	@Override
	public void clean() {
		pgmanagerDAO.cleanDbSizes(30)
	}
}