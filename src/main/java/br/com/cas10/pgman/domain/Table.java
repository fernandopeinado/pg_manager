/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cas10.pgman.domain;

import br.com.cas10.pgman.utils.SizeUtils;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 *
 * @author kasten
 */
public class Table implements Serializable {

  private String name;
  private Long sizeData;
  private Long sizeIndex;
  private Map<String, Column> columns = new LinkedHashMap<String, Column>();
  private Map<String, Index> indexes = new TreeMap<String, Index>();
  private Map<String, Toast> toasts = new TreeMap<String, Toast>();
  private SortedMap<Date, HistSize> histData = new TreeMap<Date, HistSize>();
  private SortedMap<Date, HistSize> histIndex = new TreeMap<Date, HistSize>();

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getSizeData() {
    return sizeData;
  }

  public void setSizeData(Long sizeData) {
    this.sizeData = sizeData;
  }

  public Long getSizeIndex() {
    return sizeIndex;
  }

  public void setSizeIndex(Long sizeIndex) {
    this.sizeIndex = sizeIndex;
  }

  public Map<String, Column> getColumns() {
    return columns;
  }

  public void setColumns(Map<String, Column> columns) {
    this.columns = columns;
  }

  public Map<String, Index> getIndexes() {
    return indexes;
  }

  public void setIndexes(Map<String, Index> indexes) {
    this.indexes = indexes;
  }

  public Map<String, Toast> getToasts() {
    return toasts;
  }

  public void setToasts(Map<String, Toast> toasts) {
    this.toasts = toasts;
  }

  public SortedMap<Date, HistSize> getHistData() {
    return histData;
  }

  public void setHistData(SortedMap<Date, HistSize> histData) {
    this.histData = histData;
  }

  public SortedMap<Date, HistSize> getHistIndex() {
    return histIndex;
  }

  public void setHistIndex(SortedMap<Date, HistSize> histIndex) {
    this.histIndex = histIndex;
  }

  public String getPrettyPrintSizeData() {
    return SizeUtils.prettyPrintSize(sizeData);
  }

  public String getPrettyPrintSizeIndex() {
    return SizeUtils.prettyPrintSize(sizeIndex);
  }

}
