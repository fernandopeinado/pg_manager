/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cas10.pgman.domain;

import br.com.cas10.pgman.utils.SizeUtils;
import java.io.Serializable;

/**
 *
 * @author kasten
 */
public class Toast implements Serializable {

  private String name;
  private Long size;

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

  public String getPrettyPrintSize() {
    return SizeUtils.prettyPrintSize(size);
  }

}
