package br.com.cas10.pgman.jobs;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class JobsScheduler {

    private StatementsJob statementsJob;
    private AshJob ashJob;
    private ConnectionsJob connectionsJob;
    private DatabasesJob databasesJob;

    public JobsScheduler(StatementsJob statementsJob, AshJob ashJob, ConnectionsJob connectionsJob, DatabasesJob databasesJob) {
        this.statementsJob = statementsJob;
        this.ashJob = ashJob;
        this.connectionsJob = connectionsJob;
        this.databasesJob = databasesJob;
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

    @Scheduled(
            initialDelayString = "${pgman.collector.connections.initialDelay:10000}",
            fixedRateString = "${pgman.collector.connections.rate:60000}")
    public void connectionsJob() {
        connectionsJob.collect();
    }

    @Scheduled(
            initialDelayString = "${pgman.collector.databases.initialDelay:10000}",
            fixedRateString = "${pgman.collector.databases.rate:60000}")
    public void databasesJob() {
        databasesJob.collect();
    }
}
