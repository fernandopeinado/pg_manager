package br.com.cas10.pgman.worker;

import org.slf4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.cas10.pgman.service.PGManagerDAO;
import org.slf4j.LoggerFactory;

@Component
public class RelationSizeWorker extends Worker {

  private static final Logger log = LoggerFactory.getLogger(RelationSizeWorker.class);

  @Autowired
  private PGManagerDAO pgmanagerDAO;

  public RelationSizeWorker() {
    super("relationSize", "0 0 0 * * ?");
  }

  @Override
  public void run() {
    log.info("Relation Size Worker - Running");
    pgmanagerDAO.updateRelationSizes();
  }

  @Override
  public void clean() {
    pgmanagerDAO.cleanRelationSizes(30);
  }
}
