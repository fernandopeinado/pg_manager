package br.com.cas10.pgman.jobs;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class JobsScheduler {

    private StatementsJob statementsJob;

    public JobsScheduler(StatementsJob statementsJob) {
        this.statementsJob = statementsJob;
    }

    @Scheduled(
            initialDelayString = "${pgman.collector.statements.initialDelay:5000}",
            fixedRateString = "${pgman.collector.statements.rate:15000}")
    public void statmentsJob() {
        statementsJob.collect();
    }
}
