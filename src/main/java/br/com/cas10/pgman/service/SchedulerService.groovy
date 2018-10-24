package br.com.cas10.pgman.service

import br.com.cas10.pgman.worker.Worker
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.scheduling.support.CronTrigger
import org.springframework.stereotype.Service

@Service
class SchedulerService {

	private Map<String, Worker> workers
	
	@Autowired
	private ThreadPoolTaskScheduler scheduler

	@Autowired
	void setWorkers(List<Worker> workers) {
		this.workers = new HashMap<String, Worker>()
		for (Worker worker in workers) {
			this.workers.put(worker.type, worker)
			schedule(worker)
		}
	}
	
	List<Worker> getWorkers() {
		new ArrayList<Worker>(workers.values())
	}
	
	private void schedule(Worker worker) {
		scheduler.schedule(worker, new CronTrigger(worker.cron))
		worker.scheduled()
	}
	
	void doWork(String type) {
		Worker worker = workers[type]
		if (worker) {
			worker.run()
		}
	}
}
