package br.com.cas10.pgman;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import javax.sql.DataSource;

@Configuration
class RootConfig {

  @Autowired
  private PgmanProperties properties;

  @Bean
  DataSource oramanDataSource() {
    HikariConfig config = new HikariConfig();
    config.setPoolName("pgman");
    config.setJdbcUrl(properties.getDataSource().getUrl());
    config.setUsername(properties.getDataSource().getUsername());
    config.setPassword(properties.getDataSource().getPassword());
    config.setMinimumIdle(1);
    config.setMaximumPoolSize(5);
    config.addDataSourceProperty("ApplicationName", "pgman");
    return new TransactionAwareDataSourceProxy(new HikariDataSource(config));
  }

  @Bean
  @Primary
  TaskScheduler defaultTaskScheduler() {
    ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
    taskScheduler.setPoolSize(2);
    return taskScheduler;
  }

  @Bean
  @Qualifier("ash")
  TaskScheduler ashTaskScheduler() {
    ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
    taskScheduler.setPoolSize(1);
    return taskScheduler;
  }
}
