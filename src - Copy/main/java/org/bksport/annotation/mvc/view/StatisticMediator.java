package org.bksport.annotation.mvc.view;

import java.awt.Frame;
import java.util.HashMap;

import org.bksport.annotation.mvc.AppFacade;
import org.bksport.annotation.mvc.view.ui.StatisticDialog;
import org.puremvc.java.interfaces.INotification;
import org.puremvc.java.patterns.mediator.Mediator;

/**
 * 
 * Mediator of {@link StatisticDialog}, show information of every annotations
 * 
 * @author congnh
 * 
 */
public class StatisticMediator extends Mediator {

  public StatisticMediator() {
    super(StatisticMediator.class.getName(), null);
  }

  @Override
  public String[] listNotificationInterests() {
    return new String[] { AppFacade.STATISTIC_LOADED };
  }

  @SuppressWarnings("unchecked")
  public void handleNotification(INotification notification) {
    String name = notification.getName();
    if (name.equals(AppFacade.STATISTIC_LOADED)) {
      StatisticDialog dialog = new StatisticDialog((Frame) facade
          .retrieveMediator(AppFacade.CONTAINER_MED).getViewComponent(), true);
      dialog
          .setStatistic((HashMap<String, HashMap<String, Integer>>) notification
              .getBody());
      dialog.setVisible(true);
    }
  }

}
