package org.bksport.annotation.mvc.model;

import org.bksport.annotation.mvc.model.data.Server;
import org.puremvc.java.patterns.proxy.Proxy;

/**
 * 
 * @author congnh
 * 
 */
public class ServerProxy extends Proxy {

  public ServerProxy(String name) {
    super(name, new Server());
  }

  public Server getServer() {
    return (Server) getData();
  }

  public void setServer(Server server) {
    setData(server);
  }

}
