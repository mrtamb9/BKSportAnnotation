package org.bksport.annotation.mvc.view;

import com.ontotext.kim.client.corpora.KIMDocument;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.bksport.annotation.mvc.AppFacade;
import org.bksport.annotation.mvc.model.ConfigProxy;
import org.bksport.annotation.mvc.view.ui.ContainerFrame;
import org.bksport.annotation.mvc.view.ui.OpenDialog;
import org.bksport.annotation.mvc.view.ui.FileListDialog;
import org.puremvc.java.interfaces.INotification;
import org.puremvc.java.patterns.mediator.Mediator;

/**
 * 
 * Mediator of {@link ContainerFrame}, show GUI of the application
 * 
 * @author congnh
 * 
 */
public class ContainerMediator extends Mediator {

  public ContainerMediator(String name) {
    super(name, null);
    try {
      SwingUtilities.invokeAndWait(new Runnable() {
        @Override
        public void run() {
          ContainerFrame containerFrame = new ContainerFrame();
          containerFrame.setSize(800, 600);
          containerFrame.setLocationRelativeTo(null);
          ContainerMediator.this.setViewComponent(containerFrame);
          containerFrame.addExitListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
              sendNotification(AppFacade.SHUTDOWN_CMD);
            }
          });
          containerFrame.addBrowseListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
              OpenDialog browseDialog = new OpenDialog(
                  (Frame) getViewComponent(), true, ((ConfigProxy) facade
                      .retrieveProxy(AppFacade.CONFIG_PROXY)).getCorpusList()
                      .get(0));
              browseDialog.setVisible(true);
              File[] selectedFiles = browseDialog.getSelectedFiles();
              FileListDialog uriListDialog = new FileListDialog(
                  (Frame) getViewComponent(), true);
              if (selectedFiles.length != 0) {
                uriListDialog.viewURIList(selectedFiles);
                ArrayList<File> acceptFiles = uriListDialog.showURIListDialog();
                if (!acceptFiles.isEmpty()) {
                  sendNotification(AppFacade.ADD_DOCS_CMD, acceptFiles);
                  sendNotification(AppFacade.LOAD_DOCS_INFO_CMD);
                }
              }
            }
          });
          containerFrame.addConnectListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
              sendNotification(AppFacade.CONNECT_KIM_SERVER_CMD);
            }
          });
          containerFrame.addExportListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
              sendNotification(AppFacade.EXPORT_SI_CMD);
            }
          });
          containerFrame.addUploadListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
              sendNotification(AppFacade.UPLOAD_SI_CMD);
            }
          });
          containerFrame.addDocumentListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
              sendNotification(AppFacade.LOAD_DOCS_INFO_CMD);
            }
          });
          containerFrame.addStatisticListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
              sendNotification(AppFacade.LOAD_STATISTIC_CMD);
            }
          });
          containerFrame.setVisible(true);
        }
      });
    } catch (InterruptedException e) {
      Logger.getLogger(getClass()).error(ExceptionUtils.getStackTrace(e));
    } catch (InvocationTargetException e) {
      Logger.getLogger(getClass()).error(ExceptionUtils.getStackTrace(e));
    }
  }

  @Override
  public String[] listNotificationInterests() {
    return new String[] { AppFacade.DOC_ANNOTATED, AppFacade.DOC_LOADED,
        AppFacade.DOCS_LOADED, AppFacade.UPDATE_STATUS_CMD,
        AppFacade.UPDATE_STATUS_PROGRESS };
  }

  @Override
  public void handleNotification(final INotification notification) {
    String name = notification.getName();
    if (name.equals(AppFacade.UPDATE_STATUS_CMD)) {
      ((ContainerFrame) (getViewComponent()))
          .updateStatus((String) notification.getBody());
    } else if (name.equals(AppFacade.UPDATE_STATUS_PROGRESS)) {
      ((ContainerFrame) (getViewComponent()))
          .updateStatusProgress((Integer) notification.getBody());
    } else if (name.equals(AppFacade.DOCS_LOADED)) {
      ArrayList<?> docs = (ArrayList<?>) notification.getBody();
      for (int i = 0; i < docs.size(); i++) {
        ((ContainerFrame) (getViewComponent())).viewDocument((KIMDocument) docs
            .get(i));
      }
    } else if (name.equals(AppFacade.DOC_ANNOTATED)) {
      // containerFrame.viewDocument((KIMDocument)notification.getBody());
    }
  }

}
