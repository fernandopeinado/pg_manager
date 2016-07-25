package br.com.cas10.pgman.service

import br.com.cas10.pgman.worker.Worker
import br.com.cas10.pgman.worker.WorkerJob
import org.quartz.JobDetail
import org.quartz.Scheduler
import org.quartz.Trigger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import static org.quartz.CronScheduleBuilder.cronSchedule
import static org.quartz.JobBuilder.newJob
import static org.quartz.TriggerBuilder.newTrigger

@Service
class SchedulerService {

	private Map<String, Worker> workers
	
	@Autowired
	private Scheduler scheduler

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
		JobDetail job = newJob(WorkerJob.class)
				.withIdentity(worker.type, "workers")
				.usingJobData("workerType", worker.type)
				.build()
		Trigger trigger = newTrigger()
				.withIdentity("${worker.type}Trigger", "workers")
				.startNow()
				.withSchedule(cronSchedule(worker.cron))
				.build()
		scheduler.scheduleJob(job, trigger)
		worker.scheduled();
	}
	
	public void doWork(String type) {
		Worker worker = workers[type]
		if (worker) {
			worker.run()
		}
	}
}
