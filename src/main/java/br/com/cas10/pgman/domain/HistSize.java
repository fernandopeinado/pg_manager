package br.com.cas10.pgman.domain;

import br.com.cas10.pgman.utils.SizeUtils;
import java.io.Serializable;
import java.util.Date;

public class HistSize implements Serializable, Comparable<HistSize> {

  private Date date = new Date();
  private Long size;

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
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

  public int compareTo(HistSize h) {
    return this.date.compareTo(h.date);
  }

}
