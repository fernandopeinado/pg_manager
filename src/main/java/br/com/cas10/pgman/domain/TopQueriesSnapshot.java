/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cas10.pgman.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 *
 * @author kasten
 */
public class TopQueriesSnapshot implements Serializable, Comparable<TopQueriesSnapshot> {

  private Date start;
  private Date end;
  private Long totalTime;
  private Long readTime;
  private Long writeTime;
  private SortedSet<TopQuery> queries = new TreeSet<>();

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
  public Date getStart() {
    return start;
  }

  public void setStart(Date start) {
    this.start = start;
  }

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
  public Date getEnd() {
    return end;
  }

  public void setEnd(Date end) {
    this.end = end;
  }

  public Long getTotalTime() {
    return totalTime;
  }

  public void setTotalTime(Long totalTime) {
    this.totalTime = totalTime;
  }

  public Long getReadTime() {
    return readTime;
  }

  public void setReadTime(Long readTime) {
    this.readTime = readTime;
  }

  public Long getWriteTime() {
    return writeTime;
  }

  public void setWriteTime(Long writeTime) {
    this.writeTime = writeTime;
  }

  public SortedSet<TopQuery> getQueries() {
    return queries;
  }

  public void setQueries(SortedSet<TopQuery> queries) {
    this.queries = queries;
  }

  @Override
  public int compareTo(TopQueriesSnapshot o) {
    return this.end.compareTo(o.end);
  }
}
