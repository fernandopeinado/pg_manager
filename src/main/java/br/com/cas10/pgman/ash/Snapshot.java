package br.com.cas10.pgman.ash;

import com.google.common.collect.ImmutableMap;

import java.io.Serializable;
import java.util.Map;

public class Snapshot<T> implements Serializable {

  private static final long serialVersionUID = 9126429038235792286L;

  private final long timestamp;
  private final Map<String, T> values;

  public Snapshot(long timestamp, Map<String, ? extends T> values) {
    this.timestamp = timestamp;
    this.values = ImmutableMap.copyOf(values);
  }

  public long getTimestamp() {
    return timestamp;
  }

  public Map<String, T> getValues() {
    return values;
  }

}
