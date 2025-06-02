package br.com.cas10.pgman.index;

import java.util.LinkedHashMap;
import java.util.Map;


public class ConnectionsSnapshot implements IndexedContent {

    private long timestamp;
    private String instanceName;
    private Long open;
    private Long max;
    private Float usePct;

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

    public Long getOpen() {
        return open;
    }

    public void setOpen(Long open) {
        this.open = open;
    }

    public Long getMax() {
        return max;
    }

    public void setMax(Long max) {
        this.max = max;
    }

    public Float getUsePct() {
        return usePct;
    }

    public void setUsePct(Float usePct) {
        this.usePct = usePct;
    }

    @Override
    public Map<String, Object> toJson() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("instanceName", instanceName);
        map.put("open", open);
        map.put("max", max);
        map.put("usePct", usePct);
        return map;
    }

    @Override
    public String toString() {
        return "ConnectionsSnapshot{" +
                "instanceName=" + instanceName +
                ", open=" + open +
                ", max=" + max +
                ", usePct=" + usePct +
                '}';
    }

    @Override
    public String getIndexNamePrefix() {
        return IndexService.INDEX_CONNECTIONS_NAME_PREFIX;
    }
}
