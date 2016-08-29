package org.bksport.annotation.mvc.view;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import org.bksport.annotation.mvc.AppFacade;
import org.bksport.annotation.mvc.model.data.Document;
import org.bksport.annotation.mvc.view.ui.DocumentDialog;
import org.puremvc.java.interfaces.INotification;
import org.puremvc.java.patterns.mediator.Mediator;

/**
 * 
 * Mediator of {@link DocumentDialog}, show documents's information
 * 
 * @author congnh
 */
public class DocumentMediator extends Mediator {

  public DocumentMediator(String name) {
    super(name, null);
  }

  @Override
  public final String[] listNotificationInterests() {
    return new String[] { AppFacade.DOCS_INFO_LOADED };
  }

  @Override
  @SuppressWarnings("unchecked")
  public final void handleNotification(INotification notification) {
    String name = notification.getName();
    if (name.equals(AppFacade.DOCS_INFO_LOADED)) {
      final DocumentDialog dialog = new DocumentDialog((Frame) facade
          .retrieveMediator(AppFacade.CONTAINER_MED).getViewComponent(), true);
      dialog.addAnnotateListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
          facade.sendNotification(AppFacade.ANNOTATE_DOCS_CMD,
              dialog.getSelectedDocuments());
        }
      });
      dialog.addViewListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
          facade.sendNotification(AppFacade.LOAD_DOCS_CMD,
              dialog.getSelectedDocuments());
          dialog.dispose();
        }
      });
      dialog.setDocuments((Collection<Document>) notification.getBody());
      dialog.setVisible(true);
    }
  }
}
