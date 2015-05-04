/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cas10.pgman.domain;

import br.com.cas10.pgman.utils.SizeUtils;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author kasten
 */
public class Index implements Serializable {

  private String name;
  private Long size;
  private List<String> columns;

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

  public List<String> getColumns() {
    return columns;
  }

  public void setColumns(List<String> columns) {
    this.columns = columns;
  }

  public String getPrettyPrintSize() {
    return SizeUtils.prettyPrintSize(size);
  }

}
