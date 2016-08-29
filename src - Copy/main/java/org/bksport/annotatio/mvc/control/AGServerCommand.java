package org.bksport.annotation.mvc.control;

import org.bksport.annotation.connector.AGConnector;
import org.bksport.annotation.mvc.AppFacade;
import org.bksport.annotation.mvc.model.ConnectorProxy;
import org.bksport.annotation.mvc.model.data.Server;
import org.puremvc.java.interfaces.INotification;
import org.puremvc.java.patterns.command.SimpleCommand;

/**
 * 
 * Handling every tasks related to AllegroGraph
 * 
 * @author congnh
 * 
 */
public class AGServerCommand extends SimpleCommand {

  @Override
  public void execute(INotification notification) {
    if (notification.getName().equals(AppFacade.CONNECT_AG_SERVER_CMD)) {
      Server server = (Server) facade.retrieveProxy(AppFacade.AG_SERVER_PROXY)
          .getData();
      ConnectorProxy cProxy = (ConnectorProxy) facade
          .retrieveProxy(AppFacade.CONNECTOR_PROXY);
      AGConnector connector = new AGConnector(server.getHost(),
          server.getPort(), server.getUsername(), server.getPassword());
      cProxy.setAGConnector(connector);
    }
    if (notification.getName().equals(AppFacade.DISCONNECT_AG_SERVER_CMD)) {

    }
  }

}
