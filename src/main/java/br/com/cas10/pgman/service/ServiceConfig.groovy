package br.com.cas10.pgman.service

import br.com.cas10.pgman.analitics.Snapshots
import br.com.cas10.pgman.worker.WorkerConfig
import org.quartz.Scheduler
import org.quartz.impl.StdSchedulerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.jndi.JndiTemplate
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.transaction.annotation.EnableTransactionManagement

import javax.sql.DataSource

@Configuration
@ComponentScan(basePackages=["br.com.cas10.pgman.postgresService"])
@Import([ WorkerConfig.class ])
@EnableTransactionManagement(proxyTargetClass = true, order = 0)
class ServiceConfig {

    @Bean
    @Qualifier("postgres")
    DataSource dataSource() {
        JndiTemplate jndi = new JndiTemplate();
        return jndi.lookup("java:comp/env/jdbc/pgman", DataSource.class)
    }

    @Bean
    DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource())
    }

    @Bean
    @Qualifier("internal")
    DataSource internalDataSource() {
        JndiTemplate jndi = new JndiTemplate();
        return jndi.lookup("java:comp/env/jdbc/pgman_internal", DataSource.class)
    }

    @Bean
    DataSourceTransactionManager internalTransactionManager() {
        return new DataSourceTransactionManager(internalDataSource())
    }

    @Bean
    ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler()
        int threads = Runtime.runtime.availableProcessors() * 4d
        taskScheduler.setPoolSize(threads)
        return taskScheduler
    }

}
