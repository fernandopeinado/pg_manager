package br.com.cas10.pgman.worker;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.cas10.pgman.service.PGManagerDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class DatabaseSizeWorker extends Worker {

  private static final Logger log = LoggerFactory.getLogger(DatabaseSizeWorker.class);

  @Autowired
  private PGManagerDAO pgmanagerDAO;

  public DatabaseSizeWorker() {
    super("databaseSize", "0 0 0 * * ?");
  }

  @Override
  public void run() {
    log.info("Database Size Worker - Running");
    pgmanagerDAO.updateDbSizes();
  }

  @Override
  public void clean() {
    pgmanagerDAO.cleanDbSizes(30);
  }

  public void scheduled() {
    pgmanagerDAO.updateDbSizes();
  }
}
