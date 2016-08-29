package org.bksport.annotation.mvc.model.data;

import java.io.Serializable;
import java.util.Calendar;

import com.ontotext.kim.client.corpora.KIMDocument;

/**
 * 
 * describe document information
 * 
 * @author congnh
 * 
 */
public class Document implements Serializable {

  private static final long serialVersionUID = -8858508634527908802L;

  private boolean isAnnotated;
  private long docId;
  private String url;
  private String title;
  private String content;
  private Calendar pubdate;
  private KIMDocument kdoc;

  public Document() {
  }

  public String getContent() {
    return content;
  }

  public String getTitle() {
    return title;
  }

  public String getURL() {
    return url;
  }

  public Calendar getPubdate() {
    return pubdate;
  }

  public long getId() {
    return docId;
  }

  public boolean isAnnotated() {
    return isAnnotated;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setPubdate(Calendar pubdate) {
    this.pubdate = pubdate;
  }

  public void setURL(String url) {
    this.url = url;
  }

  public void setKIMDoc(KIMDocument doc) {
    this.kdoc = doc;
  }
  
  public KIMDocument getKIMDoc(){
    return kdoc;
  }
}
