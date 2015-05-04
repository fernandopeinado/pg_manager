/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cas10.pgman.domain;

import br.com.cas10.pgman.utils.SizeUtils;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 *
 * @author kasten
 */
public class Database implements Serializable {

  private String name;
  private Long size;
  private Map<String, Table> tables = new TreeMap<String, Table>();
  private SortedMap<Date, HistSize> hist = new TreeMap<Date, HistSize>();

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getSize() {
    return size;
  }

  public void setSize(Long size) {
    this.size = size;
  }

  public Map<String, Table> getTables() {
    return tables;
  }

  public void setTables(Map<String, Table> tables) {
    this.tables = tables;
  }

  public SortedMap<Date, HistSize> getHist() {
    return hist;
  }

  public void setHist(SortedMap<Date, HistSize> hist) {
    this.hist = hist;
  }

  public String getPrettyPrintSize() {
    return SizeUtils.prettyPrintSize(size);
  }

}
