package br.com.cas10.pgman.worker;

import br.com.cas10.pgman.service.PGManagerDAO;
import br.com.cas10.pgman.service.SchedulerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CleanerWorker extends Worker {

  private static final Logger log = LoggerFactory.getLogger(CleanerWorker.class);

  @Autowired
  private SchedulerService schedulerService;

  @Autowired
  private PGManagerDAO dao;

  public CleanerWorker() {
    super("cleaner", "0 0 0 * * ?");
  }

  @Override
  public void run() {
    log.info("Cleaner Worker - Running");
    for (Worker worker : schedulerService.getWorkers()) {
      try {
        worker.clean();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    dao.compactDB();
  }
}
