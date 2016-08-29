package org.bksport.annotation.mvc.model;

import java.util.HashMap;

import org.bksport.annotation.mvc.model.data.Document;
import org.puremvc.java.patterns.proxy.Proxy;

/**
 * 
 * @author congnh
 * 
 */
public class DocumentProxy extends Proxy {

  public DocumentProxy(String name) {
    super(name, new HashMap<String, Document>());
  }

  @SuppressWarnings("unchecked")
  public HashMap<String, Document> documents() {
    return (HashMap<String, Document>) getData();
  }

  /**
   * Add new document, if document exist, update it
   * 
   * @param doc
   * @return true if adding successfully, false otherwise
   */
  public boolean addDocument(Document doc) {
    if (doc != null) {
      documents().put(doc.getURL(), doc);
      return true;
    } else {
      return false;
    }
  }

  /**
   * Remove specific document with docId
   * 
   * @param url
   *          URL document
   * @return Removed document if successful, null otherwise
   */
  public Document removeDocument(String url) {
    return documents().remove(url);
  }

  /**
   * Returns document with specific URL
   * 
   * @param url
   * @return Specific document if existed, null otherwise
   */
  public Document getDocument(String url) {
    return documents().get(url);
  }

}
