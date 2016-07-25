package br.com.cas10.pgman.worker

import br.com.cas10.pgman.service.SchedulerService
import org.quartz.DisallowConcurrentExecution
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException
import org.springframework.web.context.ContextLoader

@DisallowConcurrentExecution
class WorkerJob implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		SchedulerService schedService = ContextLoader.getCurrentWebApplicationContext().getBean(SchedulerService.class)
		schedService.doWork(context.jobDetail.jobDataMap.workerType);
	}
	
}