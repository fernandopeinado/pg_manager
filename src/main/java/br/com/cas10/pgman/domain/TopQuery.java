/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cas10.pgman.domain;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author kasten
 */
public class TopQuery implements Serializable, Comparable<TopQuery>, Cloneable {

  private String database;
  private String query;
  private Long calls;
  private Long totalTime;
  private Double avgTime;
  private Long rows;
  private Double blkReadTime;
  private Double blkWriteTime;

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

  public Long getTotalTime() {
    return totalTime;
  }

  public void setTotalTime(Long totalTime) {
    this.totalTime = totalTime;
  }

  public Double getAvgTime() {
    return avgTime;
  }

  public void setAvgTime(Double avgTime) {
    this.avgTime = avgTime;
  }

  public Long getRows() {
    return rows;
  }

  public void setRows(Long rows) {
    this.rows = rows;
  }

  public Double getBlkReadTime() {
    return blkReadTime;
  }

  public void setBlkReadTime(Double blkReadTime) {
    this.blkReadTime = blkReadTime;
  }

  public Double getBlkWriteTime() {
    return blkWriteTime;
  }

  public void setBlkWriteTime(Double blkWriteTime) {
    this.blkWriteTime = blkWriteTime;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 73 * hash + Objects.hashCode(this.database);
    hash = 73 * hash + Objects.hashCode(this.query);
    return hash;
  }

  @Override
  protected Object clone() throws CloneNotSupportedException {
    return super.clone(); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TopQuery other = (TopQuery) obj;
    if (!Objects.equals(this.database, other.database)) {
      return false;
    }
    if (!Objects.equals(this.query, other.query)) {
      return false;
    }
    return true;
  }

  @Override
  public int compareTo(TopQuery o) {
    return -this.totalTime.compareTo(o.totalTime);
  }

}
