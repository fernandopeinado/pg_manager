package br.com.cas10.pgman.index;


import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.IndexTemplatesExistRequest;
import org.elasticsearch.client.indices.PutIndexTemplateRequest;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

import static org.apache.commons.lang3.time.DateFormatUtils.ISO_8601_EXTENDED_DATE_FORMAT;

@Service
public class IndexService {

    public static final String INDEX_STATEMENTS_NAME = "postgres-query";
    public static final String INDEX_STATEMENTS_NAME_PREFIX = "postgres-query-";
    public static final String INDEX_ASH_NAME = "postgres-ash";
    public static final String INDEX_ASH_NAME_PREFIX = "postgres-ash-";
    public static final String INDEX_CONNECTIONS_NAME = "postgres-connections";
    public static final String INDEX_CONNECTIONS_NAME_PREFIX = "postgres-connections-";
    public static final String INDEX_DATABASE_NAME = "postgres-databases";
    public static final String INDEX_DATABASE_NAME_PREFIX = "postgres-databases-";

    private boolean initialized = false;
    private RestHighLevelClient elasticsearch;

    public IndexService(RestHighLevelClient elasticsearch) {
        this.elasticsearch = elasticsearch;
    }

    @PostConstruct
    public void initializeIndex() {
        while (!initialized) {
            try {
                initialized = createIndexDatabases() &&
                        createIndexConnections() &&
                        createIndexStatements() &&
                        createIndexAsh();
            } catch (Exception e) {
                System.err.println("Erro ao conectar no elasticsearch");
            }
            if (!initialized) {
                try {
                    System.out.println("Esperando ElasticSearch...");
                    Thread.sleep(5000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean createIndexStatements() throws IOException {
        final String templatePattern = INDEX_STATEMENTS_NAME_PREFIX + "*";
        IndexTemplatesExistRequest request = new IndexTemplatesExistRequest(INDEX_STATEMENTS_NAME);
        boolean exists = elasticsearch.indices().existsTemplate(request, RequestOptions.DEFAULT);
        if (!exists) {
            PutIndexTemplateRequest createRequest = new PutIndexTemplateRequest(INDEX_STATEMENTS_NAME);
            createRequest.patterns(Arrays.asList(templatePattern));
            Map<String, Object> jsonMap = new HashMap<>();
            {
                Map<String, Object> properties = new HashMap<>();
                {
                    Map<String, Object> timestamp = new HashMap<>();
                    timestamp.put("type", "date");
                    timestamp.put("format", "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis");
                    properties.put("@timestamp", timestamp);

                    Map<String, Object> query = new HashMap<>();
                    query.put("type", "text");
                    {
                        Map<String, Object> fields = new HashMap<>();
                        {
                            Map<String, Object> keyword = new HashMap<>();
                            keyword.put("type", "keyword");
                            keyword.put("ignore_above", 10240);
                            fields.put("keyword", keyword);
                        }
                        query.put("fields", fields);
                    }
                    properties.put("query", query);
                }
                jsonMap.put("properties", properties);
            }
            createRequest.mapping(jsonMap);
            elasticsearch.indices().putTemplate(createRequest, RequestOptions.DEFAULT);
            exists = true;
        }
        return exists;
    }

    private boolean createIndexAsh() throws IOException {
        final String templatePattern = INDEX_ASH_NAME_PREFIX + "*";
        IndexTemplatesExistRequest request = new IndexTemplatesExistRequest(INDEX_ASH_NAME);
        boolean exists = elasticsearch.indices().existsTemplate(request, RequestOptions.DEFAULT);
        if (!exists) {
            PutIndexTemplateRequest createRequest = new PutIndexTemplateRequest(INDEX_ASH_NAME);
            createRequest.patterns(Arrays.asList(templatePattern));
            Map<String, Object> jsonMap = new HashMap<>();
            {
                Map<String, Object> properties = new HashMap<>();
                {
                    Map<String, Object> timestamp = new HashMap<>();
                    timestamp.put("type", "date");
                    timestamp.put("format", "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis");
                    properties.put("@timestamp", timestamp);

                    Map<String, Object> query = new HashMap<>();
                    query.put("type", "text");
                    {
                        Map<String, Object> fields = new HashMap<>();
                        {
                            Map<String, Object> keyword = new HashMap<>();
                            keyword.put("type", "keyword");
                            keyword.put("ignore_above", 10240);
                            fields.put("keyword", keyword);
                        }
                        query.put("fields", fields);
                    }
                    properties.put("query", query);
                }
                jsonMap.put("properties", properties);
            }
            createRequest.mapping(jsonMap);
            elasticsearch.indices().putTemplate(createRequest, RequestOptions.DEFAULT);
            exists = true;
        }
        return exists;
    }

    private boolean createIndexConnections() throws IOException {
        final String templatePattern = INDEX_CONNECTIONS_NAME_PREFIX + "*";
        IndexTemplatesExistRequest request = new IndexTemplatesExistRequest(INDEX_CONNECTIONS_NAME);
        boolean exists = elasticsearch.indices().existsTemplate(request, RequestOptions.DEFAULT);
        if (!exists) {
            PutIndexTemplateRequest createRequest = new PutIndexTemplateRequest(INDEX_CONNECTIONS_NAME);
            createRequest.patterns(Arrays.asList(templatePattern));
            Map<String, Object> jsonMap = new HashMap<>();
            {
                Map<String, Object> properties = new HashMap<>();
                {
                    Map<String, Object> timestamp = new HashMap<>();
                    timestamp.put("type", "date");
                    timestamp.put("format", "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis");
                    properties.put("@timestamp", timestamp);
                }
                jsonMap.put("properties", properties);
            }
            createRequest.mapping(jsonMap);
            elasticsearch.indices().putTemplate(createRequest, RequestOptions.DEFAULT);
            exists = true;
        }
        return exists;
    }

    private boolean createIndexDatabases() throws IOException {
        final String templatePattern = INDEX_DATABASE_NAME_PREFIX + "*";
        IndexTemplatesExistRequest request = new IndexTemplatesExistRequest(INDEX_DATABASE_NAME);
        boolean exists = elasticsearch.indices().existsTemplate(request, RequestOptions.DEFAULT);
        if (!exists) {
            PutIndexTemplateRequest createRequest = new PutIndexTemplateRequest(INDEX_DATABASE_NAME);
            createRequest.patterns(Arrays.asList(templatePattern));
            Map<String, Object> jsonMap = new HashMap<>();
            {
                Map<String, Object> properties = new HashMap<>();
                {
                    Map<String, Object> timestamp = new HashMap<>();
                    timestamp.put("type", "date");
                    timestamp.put("format", "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis");
                    properties.put("@timestamp", timestamp);
                }
                jsonMap.put("properties", properties);
            }
            createRequest.mapping(jsonMap);
            elasticsearch.indices().putTemplate(createRequest, RequestOptions.DEFAULT);
            exists = true;
        }
        return exists;
    }

    public void save(IndexedContent... indexedContents) {
        if (indexedContents == null) return;
        try {
            BulkRequest batch = new BulkRequest();
            for (IndexedContent data : indexedContents) {
                String date = ISO_8601_EXTENDED_DATE_FORMAT.format(data.getTimestamp());
                Map<String, Object> content = data.toJson();
                if (content != null) {
                    content.put("@timestamp", data.getTimestamp());
                    String indexName = data.getIndexNamePrefix() + date;
                    IndexRequest indexRequest = new IndexRequest(indexName).source(content);
                    batch.add(indexRequest);
                }
            }
            long timeBefore = System.currentTimeMillis();
            elasticsearch.bulk(batch, RequestOptions.DEFAULT);
            long timeAfter = System.currentTimeMillis();
            System.out.println("Bulk Send: Count " + batch.numberOfActions() + " in " + (timeAfter - timeBefore) + " ms");
        } catch (Exception e) {
            System.out.println("!! Bulk Send Error: " + e.getMessage());
        }
    }
}
