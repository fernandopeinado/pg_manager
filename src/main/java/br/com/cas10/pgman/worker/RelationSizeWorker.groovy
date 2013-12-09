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
class RelationSizeWorker extends Worker {

	private static final Pattern PATTERN = Pattern.compile("\\s+(\\d+)\\s(.*)")

	@Autowired
	private PGManagerDAO pgmanagerDAO;

	RelationSizeWorker() {
		super("relationSize", "0 0 0 * * ?")
	}

	@Override
	public void run() {
		log.info("Relation Size Worker - Running")
		pgmanagerDAO.updateRelationSizes()
	}
	
	@Override
	public void clean() {
		pgmanagerDAO.cleanRelationSizes(30)
	}
}