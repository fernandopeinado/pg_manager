package br.com.cas10.pgman.ash;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class WaitTime implements Serializable {

    private String waitClass;
    private Double activeSessions;

    public WaitTime(String waitClass, Double activeSessions) {
        this.waitClass = waitClass;
        this.activeSessions = activeSessions;
    }

    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<>();
        json.put("waitClass", waitClass);
        json.put("activeSessions", activeSessions);
        return json;
    }
}
