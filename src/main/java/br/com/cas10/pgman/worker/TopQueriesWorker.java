package br.com.cas10.pgman.worker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.cas10.pgman.service.PGManagerDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class TopQueriesWorker extends Worker {

  private static final Logger log = LoggerFactory.getLogger(TopQueriesWorker.class);

  @Autowired
  private PGManagerDAO pgmanagerDAO;

  public TopQueriesWorker() {
    super("topQueries", "0 0/1 * * * ?");
  }

  @Override
  public void run() {
    log.info("Top Queries Worker - Running");
    pgmanagerDAO.snapshotQueries();
  }
}
