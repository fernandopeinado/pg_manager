package br.com.cas10.pgman.worker;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Worker implements Runnable {

  protected static final Logger log = LoggerFactory.getLogger(TopQueriesWorker.class);

  protected String type;
  protected String cron;

  public Worker(String type, String cron) {
    this.type = type;
    this.cron = cron;
  }

  @PostConstruct
  public void initialize() {
    log.info("Worker " + this.type + " - " + this.cron + " starting");
  }

  @Override
  public void run() {

  }

  public void clean() {

  }

  public void scheduled() {

  }
}
