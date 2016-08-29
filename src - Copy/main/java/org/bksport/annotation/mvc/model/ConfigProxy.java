package org.bksport.annotation.mvc.model;

import java.util.ArrayList;

import org.puremvc.java.patterns.proxy.Proxy;

/**
 * Configuration of program
 * 
 * @author congnh
 * 
 */
@SuppressWarnings("unchecked")
public class ConfigProxy extends Proxy {

  public ConfigProxy(String name) {
    super(name, new ArrayList<String>());
  }

  public ArrayList<String> getCorpusList() {
    return (ArrayList<String>) getData();
  }

  public void addCorpus(String corpus) {
    ((ArrayList<String>) getData()).add(corpus);
  }

  public void removeCorpus(String corpus) {
    ((ArrayList<String>) getData()).remove(corpus);
  }

}
