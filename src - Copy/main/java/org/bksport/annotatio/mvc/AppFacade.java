package org.bksport.annotation.mvc;

import org.bksport.annotation.mvc.control.ShutdownCommand;
import org.bksport.annotation.mvc.control.StartupCommand;
import org.puremvc.java.patterns.facade.Facade;

/**
 * 
 * @author congnh
 * 
 */
public class AppFacade extends Facade {

  public static final String EXPORT_MED = "export mediator";
  public static final String DOC_MED = "document mediator";
  public static final String ANN_MED = "annotation mediator";
  public static final String CONTAINER_MED = "container mediator";
  public static final String ALERT_MED = "alert mediator";
  public static final String WAIT_MED = "wait mediator";

  public static final String CONNECTOR_PROXY = "connector proxy";
  public static final String KIM_SERVER_PROXY = "kim server proxy";
  public static final String AG_SERVER_PROXY = "ag server proxy";
  public static final String DOC_PROXY = "document proxy";
  public static final String CONFIG_PROXY = "config proxy";

  public static final String STARTUP_CMD = "startup";
  public static final String SHUTDOWN_CMD = "shutdown";

  public static final String ALERT_CMD = "alert";
  public static final String WAIT_CMD = "wait";
  public static final String UNWAIT_CMD = "unwait";

  public static final String CONNECT_KIM_SERVER_CMD = "connect kim server";
  public static final String CONNECTING_KIM_SERVER_FAILED = "connecting kim server failed";
  public static final String CONNECTING_KIM_SERVER = "connecting kim server";
  public static final String KIM_SERVER_CONNECTED = "kim server connected";
  public static final String DISCONNECT_KIM_SERVER_CMD = "disconnect kim server";

  public static final String CONNECT_AG_SERVER_CMD = "connect ag server";
  public static final String CONNECTING_AG_SERVER = "connecting ag server";
  public static final String AG_SERVER_CONNECTED = "ag server connected";
  public static final String DISCONNECT_AG_SERVER_CMD = "disconnect ag server";

  public static final String UPDATE_STATUS_CMD = "update status";
  public static final String UPDATE_STATUS_PROGRESS = "update status progress";

  public static final String ADD_DOCS_CMD = "add documents";
  public static final String DOCS_ADDED = "documents added";
  public static final String DOC_ADDED = "document added";

  public static final String REMOVE_DOCS_CMD = "remove documents";
  public static final String DOCS_REMOVED = "documents removed";
  public static final String DOC_REMOVED = "document removed";

  public static final String ANNOTATE_DOCS_CMD = "annotate documents";
  public static final String DOCS_ANNOTATED = "documents annotated";
  public static final String DOC_ANNOTATED = "document annotated";

  public static final String LOAD_DOCS_CMD = "load documents";
  public static final String DOCS_LOADED = "documents loaded";
  public static final String DOC_LOADED = "document loaded";

  public static final String LOAD_DOCS_INFO_CMD = "load documents info";
  public static final String DOCS_INFO_LOADED = "documents info loaded";
  public static final String DOC_INFO_LOADED = "document info loaded";

  public static final String LOAD_ANN_CMD = "load annotation";
  public static final String ANN_LOADED = "annotation loaded";

  public static final String LOAD_STATISTIC_CMD = "load statistic";
  public static final String STATISTIC_LOADED = "statistic loaded";

  public static final String EXPORT_SI_CMD = "export SI";
  public static final String EXPORTING_SI = "exporting SI";
  public static final String SI_EXPORTED = "SI exported";

  public static final String UPLOAD_SI_CMD = "upload semantic information";
  public static final String UPLOADING_SI = "uploading semantoc information";
  public static final String SI_UPLOADED = "semantic information exported";

  public AppFacade() {
    super();
    registerCommand(STARTUP_CMD, new StartupCommand());
    registerCommand(SHUTDOWN_CMD, new ShutdownCommand());
  }

  public static AppFacade getInstance() {
    if (instance == null) {
      instance = new AppFacade();
    }
    return (AppFacade) instance;
  }

  public final void startup() {
    sendNotification(STARTUP_CMD);
  }

  public final void shutdown() {
    sendNotification(SHUTDOWN_CMD);
  }
}
