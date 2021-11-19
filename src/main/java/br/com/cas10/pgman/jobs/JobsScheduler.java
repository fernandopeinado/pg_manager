package br.com.cas10.pgman.jobs;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class JobsScheduler {

    private StatementsJob statementsJob;
    private AshJob ashJob;

    public JobsScheduler(StatementsJob statementsJob, AshJob ashJob) {
        this.statementsJob = statementsJob;
        this.ashJob = ashJob;
    }

    @Scheduled(
            initialDelayString = "${pgman.collector.statements.initialDelay:5000}",
            fixedRateString = "${pgman.collector.statements.rate:15000}")
    public void statmentsJob() {
        statementsJob.collect();
    }

    @Scheduled(
            initialDelayString = "${pgman.collector.active_sessions.initialDelay:5000}",
            fixedRateString = "${pgman.collector.active_sessions.rate:1000}")
    public void ashJob() {
        ashJob.collect();
    }

}
