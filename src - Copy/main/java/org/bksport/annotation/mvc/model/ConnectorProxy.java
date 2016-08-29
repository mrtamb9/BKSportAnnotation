package org.bksport.annotation.mvc.model;

import org.bksport.annotation.connector.AGConnector;
import org.bksport.annotation.connector.KIMConnector;
import org.puremvc.java.patterns.proxy.Proxy;

public class ConnectorProxy extends Proxy {

  private KIMConnector kimConnector;
  private AGConnector agConnector;

  public ConnectorProxy(String name) {
    super(name);
  }

  public KIMConnector getKIMConnector() {
    return kimConnector;
  }

  public AGConnector getAGConnector() {
    return agConnector;
  }

  public void setKIMConnector(KIMConnector connector) {
    this.kimConnector = connector;
  }

  public void setAGConnector(AGConnector connector) {
    this.agConnector = connector;
  }

  public boolean isKIMConnected() {
    return kimConnector != null;
  }

  public boolean isAGConnected() {
    return agConnector != null;
  }

}
