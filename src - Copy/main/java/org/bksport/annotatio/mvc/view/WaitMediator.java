package org.bksport.annotation.mvc.view;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.SwingUtilities;

import org.bksport.annotation.mvc.AppFacade;
import org.bksport.annotation.mvc.view.ui.WaitDialog;
import org.puremvc.java.interfaces.INotification;
import org.puremvc.java.patterns.mediator.Mediator;

/**
 * 
 * Mediator of {@link WaitDialog}, force user waiting for every long-time tasks
 * 
 * @author congnh
 * 
 */
public class WaitMediator extends Mediator {

  public WaitMediator(String mediatorName) {
    super(mediatorName, null);
  }

  @Override
  public String[] listNotificationInterests() {
    return new String[] { AppFacade.WAIT_CMD, AppFacade.UNWAIT_CMD };
  }

  @Override
  public void handleNotification(final INotification notification) {
    if (AppFacade.WAIT_CMD.equals(notification.getName())) {
      SwingUtilities.invokeLater(new Runnable() {

        @Override
        public void run() {
          final WaitDialog dialog = new WaitDialog((Frame) facade
              .retrieveMediator(AppFacade.CONTAINER_MED).getViewComponent(),
              true);
          dialog.setWaitMessage(notification.getBody().toString());
          dialog.addCancelButtonListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
              dialog.dispose();
            }
          });
          setViewComponent(dialog);
          dialog.setVisible(true);
        }
      });
    } else if (AppFacade.UNWAIT_CMD.equals(notification.getName())) {
      SwingUtilities.invokeLater(new Runnable() {

        @Override
        public void run() {
          ((JDialog) getViewComponent()).dispose();
        }
      });
    }
  }

}
