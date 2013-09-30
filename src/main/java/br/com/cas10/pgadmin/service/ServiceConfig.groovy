package br.com.cas10.pgadmin.service

import java.beans.beancontext.BeanContext;

import javax.sql.DataSource

import org.hibernate.ejb.HibernatePersistence
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jndi.JndiTemplate
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.transaction.annotation.EnableTransactionManagement

import br.com.cas10.pgadmin.agent.AgentConfig;
import br.com.cas10.pgadmin.analitics.Snapshots;

@Configuration
@ComponentScan(basePackages=["br.com.cas10.pgadmin.service"])
@Import(AgentConfig.class)
@EnableTransactionManagement(proxyTargetClass = true)
class ServiceConfig {

	@Bean
	DataSource dataSource() {
		JndiTemplate jndi = new JndiTemplate();
		return jndi.lookup("java:comp/env/jdbc/pgadmin", DataSource.class)
	}

	@Bean
	DataSourceTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource())
	}
	
	@Bean
	ThreadPoolTaskScheduler taskScheduler() {
		ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler()
		int threads = Math.ceil(Runtime.runtime.availableProcessors() / 2.0d)
		taskScheduler.setPoolSize(threads)
		return taskScheduler
	}
	
	@Bean
	Snapshots snapshots() {
		return new Snapshots()
	}
}
