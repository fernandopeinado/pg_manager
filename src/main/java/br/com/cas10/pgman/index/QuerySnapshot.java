package br.com.cas10.pgman.index;

import java.util.LinkedHashMap;
import java.util.Map;

public class QuerySnapshot implements IndexedContent {

    private Long queryId;
    private String database;
    private String query;
    private Long calls;
    private Double totalTime;
    private Double minTime;
    private Double maxTime;
    private Double meanTime;
    private Long rows;
    private Double blockReadTime;
    private Double blockWriteTime;

    public Long getQueryId() {
        return queryId;
    }

    public void setQueryId(Long queryId) {
        this.queryId = queryId;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Long getCalls() {
        return calls;
    }

    public void setCalls(Long calls) {
        this.calls = calls;
    }

    public Double getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Double totalTime) {
        this.totalTime = totalTime;
    }

    public Double getMinTime() {
        return minTime;
    }

    public void setMinTime(Double minTime) {
        this.minTime = minTime;
    }

    public Double getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(Double maxTime) {
        this.maxTime = maxTime;
    }

    public Double getMeanTime() {
        return meanTime;
    }

    public void setMeanTime(Double meanTime) {
        this.meanTime = meanTime;
    }

    public Long getRows() {
        return rows;
    }

    public void setRows(Long rows) {
        this.rows = rows;
    }

    public Double getBlockReadTime() {
        return blockReadTime;
    }

    public void setBlockReadTime(Double blockReadTime) {
        this.blockReadTime = blockReadTime;
    }

    public Double getBlockWriteTime() {
        return blockWriteTime;
    }

    public void setBlockWriteTime(Double blockWriteTime) {
        this.blockWriteTime = blockWriteTime;
    }

    @Override
    public Map<String, Object> toJson() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("queryId", queryId);
        map.put("database", database);
        map.put("query", query);
        map.put("calls", calls);
        map.put("totalTime", totalTime);
        map.put("minTime", minTime);
        map.put("maxTime", maxTime);
        map.put("meanTime", meanTime);
        map.put("rows", rows);
        map.put("rowsAvg", (calls != null && calls != 0) ? (Long) (rows / calls) : 0L);
        map.put("blockReadTime", blockReadTime);
        map.put("blockWriteTime", blockWriteTime);
        return map;
    }

    @Override
    public String toString() {
        return "QuerySnapshot{" +
                "queryId=" + queryId +
                ", database='" + database + '\'' +
                ", query='" + query + '\'' +
                ", calls=" + calls +
                ", totalTime=" + totalTime +
                ", minTime=" + minTime +
                ", maxTime=" + maxTime +
                ", meanTime=" + meanTime +
                ", rows=" + rows +
                ", blockReadTime=" + blockReadTime +
                ", blockWriteTime=" + blockWriteTime +
                '}';
    }
}
