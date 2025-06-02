package br.com.cas10.pgman.index;

import java.util.LinkedHashMap;
import java.util.Map;


public class DatabaseSnapshot implements IndexedContent {

    private long timestamp;
    private String instanceName;
    private String database;
    private Long numBackends;
    private Long xactCommit;
    private Long xactRollback;
    private Long blksHit;
    private Long blksRead;
    private Float cacheHit;
    private Double blksReadTime;
    private Double blksWriteTime;
    private Long conflicts;
    private Long deadlocks;
    private Long tupFeched;
    private Long tupReturned;
    private Long tupInserted;
    private Long tupUpdated;
    private Long tupDeleted;

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public Long getNumBackends() {
        return numBackends;
    }

    public void setNumBackends(Long numBackends) {
        this.numBackends = numBackends;
    }

    public Long getXactCommit() {
        return xactCommit;
    }

    public void setXactCommit(Long xactCommit) {
        this.xactCommit = xactCommit;
    }

    public Long getXactRollback() {
        return xactRollback;
    }

    public void setXactRollback(Long xactRollback) {
        this.xactRollback = xactRollback;
    }

    public Long getBlksHit() {
        return blksHit;
    }

    public void setBlksHit(Long blksHit) {
        this.blksHit = blksHit;
    }

    public Long getBlksRead() {
        return blksRead;
    }

    public void setBlksRead(Long blksRead) {
        this.blksRead = blksRead;
    }

    public Float getCacheHit() {
        return cacheHit;
    }

    public void setCacheHit(Float cacheHit) {
        this.cacheHit = cacheHit;
    }

    public Double getBlksReadTime() {
        return blksReadTime;
    }

    public void setBlksReadTime(Double blksReadTime) {
        this.blksReadTime = blksReadTime;
    }

    public Double getBlksWriteTime() {
        return blksWriteTime;
    }

    public void setBlksWriteTime(Double blksWriteTime) {
        this.blksWriteTime = blksWriteTime;
    }

    public Long getConflicts() {
        return conflicts;
    }

    public void setConflicts(Long conflicts) {
        this.conflicts = conflicts;
    }

    public Long getDeadlocks() {
        return deadlocks;
    }

    public void setDeadlocks(Long deadlocks) {
        this.deadlocks = deadlocks;
    }

    public Long getTupFeched() {
        return tupFeched;
    }

    public void setTupFeched(Long tupFeched) {
        this.tupFeched = tupFeched;
    }

    public Long getTupReturned() {
        return tupReturned;
    }

    public void setTupReturned(Long tupReturned) {
        this.tupReturned = tupReturned;
    }

    public Long getTupInserted() {
        return tupInserted;
    }

    public void setTupInserted(Long tupInserted) {
        this.tupInserted = tupInserted;
    }

    public Long getTupUpdated() {
        return tupUpdated;
    }

    public void setTupUpdated(Long tupUpdated) {
        this.tupUpdated = tupUpdated;
    }

    public Long getTupDeleted() {
        return tupDeleted;
    }

    public void setTupDeleted(Long tupDeleted) {
        this.tupDeleted = tupDeleted;
    }

    @Override
    public Map<String, Object> toJson() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("instanceName", instanceName);
        map.put("database", database);
        map.put("numBackends", numBackends);
        map.put("xactCommit", xactCommit);
        map.put("xactRollback", xactRollback);
        map.put("blksHit", blksHit);
        map.put("blksRead", blksRead);
        map.put("cacheHit", cacheHit);
        map.put("blksReadTime", blksReadTime);
        map.put("blksWriteTime", blksWriteTime);
        map.put("conflicts", conflicts);
        map.put("deadlocks", deadlocks);
        map.put("tupFeched", tupFeched);
        map.put("tupReturned", tupReturned);
        map.put("tupInserted", tupInserted);
        map.put("tupUpdated", tupUpdated);
        map.put("tupDeleted", tupDeleted);
        return map;
    }

    @Override
    public String toString() {
        return "DatabaseSnapshot{" +
                "instanceName=" + instanceName +
                ",database=" + database +
                ",numBackends=" + numBackends +
                ",xactCommit=" + xactCommit +
                ",xactRollback=" + xactRollback +
                ",blksHit=" + blksHit +
                ",blksRead=" + blksRead +
                ",cacheHit=" + cacheHit +
                ",blksReadTime=" + blksReadTime +
                ",blksWriteTime=" + blksWriteTime +
                ",conflicts=" + conflicts +
                ",deadlocks=" + deadlocks +
                ",tupFeched=" + tupFeched +
                ",tupReturned=" + tupReturned +
                ",tupInserted=" + tupInserted +
                ",tupUpdated=" + tupUpdated +
                ",tupDeleted=" + tupDeleted +
                '}';
    }

    @Override
    public String getIndexNamePrefix() {
        return IndexService.INDEX_DATABASE_NAME_PREFIX;
    }
}
