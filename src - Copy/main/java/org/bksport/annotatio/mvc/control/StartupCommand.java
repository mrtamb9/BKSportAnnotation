package org.bksport.annotation.mvc.control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.bksport.annotation.mvc.AppFacade;
import org.bksport.annotation.mvc.model.ConfigProxy;
import org.bksport.annotation.mvc.model.ConnectorProxy;
import org.bksport.annotation.mvc.model.DocumentProxy;
import org.bksport.annotation.mvc.model.ServerProxy;
import org.bksport.annotation.mvc.model.data.Server;
import org.bksport.annotation.mvc.view.AlertMediator;
import org.bksport.annotation.mvc.view.AnnotationMediator;
import org.bksport.annotation.mvc.view.ContainerMediator;
import org.bksport.annotation.mvc.view.DocumentMediator;
import org.bksport.annotation.mvc.view.ExportMediator;
import org.bksport.annotation.mvc.view.StatisticMediator;
import org.bksport.annotation.mvc.view.WaitMediator;
import org.puremvc.java.interfaces.INotification;
import org.puremvc.java.patterns.command.SimpleCommand;

/**
 * 
 * handling every tasks when application start
 * 
 * @author congnh
 * 
 */
public class StartupCommand extends SimpleCommand {

  /**
   * Register mediators, proxies and command
   * 
   * @param notification
   */
  @Override
  public void execute(final INotification notification) {
    // load configuration from ./conf/config.properties file
    Properties properties = new Properties();
    try {
      properties
          .load(new FileInputStream(new File("./conf/config.properties")));
    } catch (FileNotFoundException e) {
      Logger.getLogger(getClass()).error(ExceptionUtils.getStackTrace(e));
    } catch (IOException e) {
      Logger.getLogger(getClass()).error(ExceptionUtils.getStackTrace(e));
    }
    // register command
    KIMServerCommand conKIMCmd = new KIMServerCommand();
    facade.registerCommand(AppFacade.CONNECT_KIM_SERVER_CMD, conKIMCmd);
    facade.registerCommand(AppFacade.CONNECT_AG_SERVER_CMD, conKIMCmd);

    facade.registerCommand(AppFacade.ADD_DOCS_CMD, new DocumentCommand());
    facade.registerCommand(AppFacade.REMOVE_DOCS_CMD, new DocumentCommand());
    facade.registerCommand(AppFacade.ANNOTATE_DOCS_CMD, new DocumentCommand());
    facade.registerCommand(AppFacade.LOAD_DOCS_INFO_CMD, new DocumentCommand());

    facade.registerCommand(AppFacade.LOAD_DOCS_CMD, new DocumentCommand());

    AnnotationCommand annCmd = new AnnotationCommand();
    facade.registerCommand(AppFacade.LOAD_ANN_CMD, annCmd);
    facade.registerCommand(AppFacade.LOAD_STATISTIC_CMD, annCmd);

    facade.registerCommand(AppFacade.EXPORT_SI_CMD, new ExportCommand());

    facade.registerCommand(AppFacade.UPLOAD_SI_CMD, new UploadCommand());

    // register mediator
    facade.registerMediator(new DocumentMediator(AppFacade.DOC_MED));
    facade.registerMediator(new AnnotationMediator(AppFacade.ANN_MED));
    facade.registerMediator(new StatisticMediator());
    facade.registerMediator(new ExportMediator(AppFacade.EXPORT_MED));
    facade.registerMediator(new AlertMediator(AppFacade.ALERT_CMD));
    facade.registerMediator(new WaitMediator(AppFacade.WAIT_MED));
    // register proxy
    facade.registerProxy(new DocumentProxy(AppFacade.DOC_PROXY));
    facade.registerProxy(new ConnectorProxy(AppFacade.CONNECTOR_PROXY));
    // initialize KIM's server information
    ServerProxy kimProxy = new ServerProxy(AppFacade.KIM_SERVER_PROXY);
    kimProxy.setServer(new Server(properties.getProperty("KIM_SERVER_HOST",
        "localhost"), Integer.parseInt(properties.getProperty(
        "KIM_SERVER_PORT", "1099"))));
    facade.registerProxy(kimProxy);
    // initialize AG's server information
    ServerProxy agProxy = new ServerProxy(AppFacade.AG_SERVER_PROXY);
    agProxy.setServer(new Server(properties.getProperty("AG_SERVER_HOST",
        "localhost"), Integer.parseInt(properties.getProperty("AG_SERVER_PORT",
        "10035")), properties.getProperty("AG_SERVER_USER"), properties
        .getProperty("AG_SERVER_PASSWORD")));
    ConfigProxy configProxy = new ConfigProxy(AppFacade.CONFIG_PROXY);
    String[] corpuses = properties.getProperty("corpus", "corpus").split(",");
    for (String corpus : corpuses) {
      configProxy.addCorpus(corpus);
    }
    facade.registerProxy(configProxy);

    facade.registerMediator(new ContainerMediator(AppFacade.CONTAINER_MED));

    // remove the command because it never be called more than once
    facade.removeCommand(AppFacade.STARTUP_CMD);
  }
}
