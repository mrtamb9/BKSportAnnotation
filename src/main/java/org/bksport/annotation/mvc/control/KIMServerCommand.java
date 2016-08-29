package org.bksport.annotation.mvc.control;

import org.apache.log4j.Logger;
import org.bksport.annotation.connector.KIMConnector;
import org.bksport.annotation.mvc.AppFacade;
import org.bksport.annotation.mvc.model.ConnectorProxy;
import org.bksport.annotation.mvc.model.ServerProxy;
import org.bksport.annotation.mvc.model.data.Server;
import org.puremvc.java.interfaces.INotification;
import org.puremvc.java.patterns.command.SimpleCommand;

/**
 * 
 * handling every tasks related to KIM Server
 * 
 * @author congnh
 * 
 */
public class KIMServerCommand extends SimpleCommand {

  @Override
  public void execute(INotification notification) {
    if (notification.getName().equals(AppFacade.CONNECT_KIM_SERVER_CMD)) {
      sendNotification(AppFacade.WAIT_CMD, "Connect to KIM server");
      Server server = ((ServerProxy) facade
          .retrieveProxy(AppFacade.KIM_SERVER_PROXY)).getServer();
      ConnectorProxy cProxy = (ConnectorProxy) facade
          .retrieveProxy(AppFacade.CONNECTOR_PROXY);
      KIMConnector connector = new KIMConnector(server.getHost(),
          server.getPort());
      if (!connector.connect()) {
        sendNotification(AppFacade.UNWAIT_CMD);
        sendNotification(AppFacade.ALERT_CMD, "Failed connecting to KIM server");
      } else {
        sendNotification(AppFacade.UNWAIT_CMD);
        cProxy.setKIMConnector(connector);
        Logger.getLogger(getClass()).info("Connect to server: " + server);
      }
    }
    if (notification.getName().equals(AppFacade.DISCONNECT_KIM_SERVER_CMD)) {
      ConnectorProxy cProxy = (ConnectorProxy) facade
          .retrieveProxy(AppFacade.CONNECTOR_PROXY);
      cProxy.getKIMConnector().disconnect();
      cProxy.setKIMConnector(null);
      Logger.getLogger(getClass()).info("Disconnect to server");
    }
  }

}
