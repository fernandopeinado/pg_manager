package br.com.cas10.pgman.index;

import java.util.Map;

public interface IndexedContent {

    long getTimestamp();

    String getIndexNamePrefix();

    Map<String, Object> toJson();

}
