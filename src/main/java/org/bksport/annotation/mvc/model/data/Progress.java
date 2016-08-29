package org.bksport.annotation.mvc.model.data;

import java.util.UUID;

/**
 * Describe progress information
 * 
 * @author congnh
 * 
 */
public class Progress {

  private String id;
  private String name;
  private int percent;
  private String abstractStr;

  public Progress() {
    name = abstractStr = null;
    percent = -1;
    id = UUID.randomUUID().toString();
  }

  public String getID() {
    return id;
  }

  public String getName() {
    return name;
  }

  public int getPercent() {
    return percent;
  }

  public String getAbstract() {
    return abstractStr;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setPercent(int percent) {
    this.percent = percent;
  }

  public void setAbstract(String abstractStr) {
    this.abstractStr = abstractStr;
  }
}
