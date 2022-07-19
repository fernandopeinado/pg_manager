package br.com.cas10.pgman.ash;

import br.com.cas10.pgman.index.IndexService;
import br.com.cas10.pgman.index.IndexedContent;

import java.util.HashMap;
import java.util.Map;

public class ActiveSession implements IndexedContent {

    public long timestamp;
    public String pid;
    public String database;
    public String username;
    public String program;
    public String query;
    public String event;
    public String waitClass;
    public String backendType;
    public Double value;
    public String instanceName;

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String getIndexNamePrefix() {
        return IndexService.INDEX_ASH_NAME_PREFIX;
    }

    @Override
    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<>();
        json.put("pid", pid);
        json.put("database", database);
        json.put("username", username);
        json.put("program", program);
        json.put("query", query);
        json.put("event", event);
        json.put("waitClass", waitClass);
        json.put("backendType", backendType);
        json.put("instanceName", instanceName);
        json.put("value", value);
        return json;
    }
}
