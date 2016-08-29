package org.bksport.annotation.mvc.control;

import com.ontotext.kim.client.corpora.KIMAnnotation;
import com.ontotext.kim.client.corpora.KIMAnnotationSet;
import com.ontotext.kim.client.corpora.KIMCorporaException;
import com.ontotext.kim.client.corpora.KIMDocument;
import com.ontotext.kim.client.model.FeatureConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.bksport.annotation.mvc.AppFacade;
import org.bksport.annotation.mvc.model.DocumentProxy;
import org.puremvc.java.interfaces.INotification;
import org.puremvc.java.patterns.command.SimpleCommand;

/**
 * 
 * Handling every tasks related to annotation 
 * 
 * @author congnh
 * 
 */
public class AnnotationCommand extends SimpleCommand {

  @Override
  public void execute(INotification notification) {
    DocumentProxy docProxy = (DocumentProxy) facade
        .retrieveProxy(AppFacade.DOC_PROXY);
    if (notification.getName().equals(AppFacade.LOAD_ANN_CMD)) {
      Object[] object = (Object[]) notification.getBody();
      int annOffset = (Integer) object[0];
      String docId = object[1].toString();
      KIMDocument doc = docProxy.getDocument(docId).getKIMDoc();
      try {
        KIMAnnotationSet annotationSet = doc.getAnnotations();
        Iterator<?> annotationIterator = annotationSet.iterator();
        ArrayList<KIMAnnotation> annList = new ArrayList<KIMAnnotation>();
        while (annotationIterator.hasNext()) {
          KIMAnnotation annotation = (KIMAnnotation) annotationIterator.next();
          if (annotation.getStartOffset() <= annOffset
              && annotation.getEndOffset() >= annOffset
              && !annotation.getType().equals("Sentence")) {
            annList.add(annotation);
          }
        }
        sendNotification(AppFacade.ANN_LOADED, annList);
      } catch (KIMCorporaException e) {
        sendNotification(AppFacade.ALERT_CMD, "Loading annotation failed.");
        Logger.getLogger(getClass()).error(ExceptionUtils.getStackTrace(e));
      }
    } else if (notification.getName().equals(AppFacade.LOAD_STATISTIC_CMD)) {

      HashMap<String, HashMap<String, Integer>> statistic = new HashMap<String, HashMap<String, Integer>>();
      String all = "All documents";
      statistic.put(all, new HashMap<String, Integer>());
      for (String id : docProxy.documents().keySet()) {
        KIMDocument doc = docProxy.documents().get(id).getKIMDoc();
        try {
          String url = doc.getSourceUrl().toString();
          statistic.put(url, new HashMap<String, Integer>());
          HashMap<Integer, String> offsetInst = new HashMap<Integer, String>();
          KIMAnnotationSet annSet = doc.getAnnotations();
          Iterator<?> iterator = annSet.iterator();
          while (iterator.hasNext()) {
            KIMAnnotation ann = (KIMAnnotation) iterator.next();
            Object cl = ann.getFeatures().get(FeatureConstants.CLASS);
            if (cl != null) {
              if (cl.toString().contains("http://bk.sport.owl#")
                  && offsetInst.get(ann.getStartOffset()) == null) {
                offsetInst.put(ann.getStartOffset(), cl.toString());
                Integer numOfCor = statistic.get(all).get(cl.toString());
                if (numOfCor != null)
                  statistic.get(all).put(cl.toString(), numOfCor + 1);
                else
                  statistic.get(all).put(cl.toString(), 1);
                Integer numOfAnn = statistic.get(url).get(cl.toString());
                if (numOfAnn != null)
                  statistic.get(url).put(cl.toString(), numOfAnn + 1);
                else
                  statistic.get(url).put(cl.toString(), 1);
              }
            }
          }
        } catch (KIMCorporaException e) {
          Logger.getLogger(getClass()).error(ExceptionUtils.getStackTrace(e));
        }
      }
      sendNotification(AppFacade.STATISTIC_LOADED, statistic);
    }
  }
}
