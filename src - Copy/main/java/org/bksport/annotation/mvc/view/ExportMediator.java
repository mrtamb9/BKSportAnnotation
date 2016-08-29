package org.bksport.annotation.mvc.view;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;

import org.bksport.annotation.mvc.AppFacade;
import org.bksport.annotation.mvc.view.ui.ExportDialog;
import org.bksport.annotation.mvc.view.ui.SaveDialog;
import org.puremvc.java.interfaces.INotification;
import org.puremvc.java.patterns.mediator.Mediator;

/**
 * 
 * Mediator of {@link ExportDialog}, show exported semantic information
 * 
 * @author congnh
 * 
 */
public class ExportMediator extends Mediator {

  public ExportMediator(String name) {
    super(name, null);
  }

  public String[] listNotificationInterests() {
    return new String[] { AppFacade.SI_EXPORTED };
  }

  public void handleNotification(INotification notification) {
    String name = notification.getName();
    if (name.equals(AppFacade.SI_EXPORTED)) {
      if (notification.getBody().toString().isEmpty()) {
        sendNotification(AppFacade.ALERT_CMD, "Exported content is empty!!!");
      } else {
        ExportDialog exportDialog = new ExportDialog((Frame) AppFacade
            .getInstance().retrieveMediator(AppFacade.CONTAINER_MED)
            .getViewComponent(), true);
        exportDialog.setSize(600, 400);
        exportDialog.setLocationRelativeTo(null);
        exportDialog.addCancelListener(new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent e) {
            ((JDialog) getViewComponent()).dispose();
          }
        });
        exportDialog.addAcceptListener(new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent e) {
            SaveDialog saveDialog = new SaveDialog(null, true);
            saveDialog.showSaveDialog(((ExportDialog) getViewComponent())
                .getExportedContent());
            ((JDialog) getViewComponent()).dispose();
          }
        });
        exportDialog.setExportedContent(notification.getBody().toString());
        setViewComponent(exportDialog);
        exportDialog.setVisible(true);
      }
    }
  }
}
