package org.bksport.annotation.mvc.view;

import java.awt.Frame;
import java.util.ArrayList;

import com.ontotext.kim.client.corpora.KIMAnnotation;

import org.bksport.annotation.mvc.AppFacade;
import org.bksport.annotation.mvc.view.ui.AnnotationDialog;
import org.puremvc.java.interfaces.INotification;
import org.puremvc.java.patterns.mediator.Mediator;

/**
 * 
 * Mediator of {@link AnnotationDialog}, show information of selected
 * annotations
 * 
 * @author congnh
 * 
 */
public class AnnotationMediator extends Mediator {

  public AnnotationMediator(String name) {
    super(name, null);
  }

  @Override
  public String[] listNotificationInterests() {
    return new String[] { AppFacade.ANN_LOADED };
  }

  @SuppressWarnings("unchecked")
  @Override
  public void handleNotification(final INotification notification) {
    String name = notification.getName();
    if (name.equals(AppFacade.ANN_LOADED)) {
      if (getViewComponent() == null)
        setViewComponent(new AnnotationDialog((Frame) facade.retrieveMediator(
            AppFacade.CONTAINER_MED).getViewComponent(), true));
      AnnotationDialog dialog = (AnnotationDialog) getViewComponent();
      ArrayList<KIMAnnotation> annList = (ArrayList<KIMAnnotation>) notification
          .getBody();
      for (KIMAnnotation ann : annList) {
        dialog.addAnnotation(ann);
      }
      dialog.setVisible(true);
    }
  }

}
