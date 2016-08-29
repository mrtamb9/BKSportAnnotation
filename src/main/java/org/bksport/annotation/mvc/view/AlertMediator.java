package org.bksport.annotation.mvc.view;

import java.awt.Frame;

import org.bksport.annotation.mvc.AppFacade;
import org.bksport.annotation.mvc.view.ui.AlertDialog;
import org.puremvc.java.interfaces.INotification;
import org.puremvc.java.patterns.mediator.Mediator;

/**
 * Mediator of {@link AlertDialog}, show importance information of the
 * application
 * 
 * @author congnh
 * 
 */
public class AlertMediator extends Mediator {

  public AlertMediator(String name) {
    super(name, null);
  }

  @Override
  public String[] listNotificationInterests() {
    return new String[] { AppFacade.ALERT_CMD };
  }

  @Override
  public void handleNotification(INotification notification) {
    // JOptionPane.showMessageDialog((Frame) AppFacade.getInstance()
    // .retrieveMediator(AppFacade.CONTAINER_MED).getViewComponent(),
    // notification.getBody(), "Alert", JOptionPane.INFORMATION_MESSAGE);
    AlertDialog dialog = new AlertDialog((Frame) AppFacade.getInstance()
        .retrieveMediator(AppFacade.CONTAINER_MED).getViewComponent(), true);
    dialog.setAlertMessage(notification.getBody().toString());
    dialog.setVisible(true);

  }

}
