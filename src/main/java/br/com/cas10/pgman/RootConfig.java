package br.com.cas10.pgman;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@EnableAutoConfiguration(exclude = {ElasticsearchAutoConfiguration.class})
@EnableScheduling
@EnableTransactionManagement(proxyTargetClass = true)
@Configuration
public class RootConfig {

  @Autowired
  private PgmanProperties properties;

  @Value("${pgman.elasticsearch.host:localhost}")
  private String elasticHost;

  @Value("${pgman.elasticsearch.port:9200}")
  private int elasticPort;

  @Value("${pgman.elasticsearch.username:elastic}")
  private String elasticUsername;

  @Value("${pgman.elasticsearch.password:changeme}")
  private String elasticPassword;

  @Value("${pgman.elasticsearch.protocol:http}")
  private String elasticProtocol;

  @Bean
  public DataSource pgmanDataSource() {
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
  public PlatformTransactionManager transactionManager() {
    DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
    dataSourceTransactionManager.setDataSource(pgmanDataSource());
    dataSourceTransactionManager.setDefaultTimeout(60);
    return dataSourceTransactionManager;
  }

  @Bean(destroyMethod = "close")
  public RestHighLevelClient elasticHighLevelClient() {
    final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
    credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(elasticUsername, elasticPassword));
    RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(new HttpHost(elasticHost, elasticPort, elasticProtocol))
            .setHttpClientConfigCallback((HttpAsyncClientBuilder httpClientBuilder) -> {
                return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            }));
    return client;
  }
}
