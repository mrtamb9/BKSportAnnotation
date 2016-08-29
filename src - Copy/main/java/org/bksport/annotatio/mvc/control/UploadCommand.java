package org.bksport.annotation.mvc.control;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.impl.PropertyImpl;
import com.hp.hpl.jena.rdf.model.impl.ResourceImpl;
import com.hp.hpl.jena.rdf.model.impl.StatementImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import com.ontotext.kim.client.corpora.KIMAnnotation;
import com.ontotext.kim.client.corpora.KIMAnnotationSet;
import com.ontotext.kim.client.corpora.KIMCorporaException;
import com.ontotext.kim.client.corpora.KIMDocument;
import com.ontotext.kim.client.corpora.KIMFeatureMap;
import com.ontotext.kim.client.model.FeatureConstants;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.bksport.annotation.connector.AGConnector;
import org.bksport.annotation.connector.KIMConnector;
import org.bksport.annotation.mvc.AppFacade;
import org.bksport.annotation.mvc.model.ConnectorProxy;
import org.bksport.annotation.mvc.model.DocumentProxy;
import org.bksport.annotation.util.Constants;
import org.bksport.annotation.util.NSUtil;
import org.puremvc.java.interfaces.INotification;
import org.puremvc.java.patterns.command.SimpleCommand;

/**
 * 
 * upload semantic information to AG
 * 
 * @author congnh
 * 
 */
public class UploadCommand extends SimpleCommand implements Runnable {

  private Model model = ModelFactory.createDefaultModel();
  private ArrayList<Statement> statementList = new ArrayList<Statement>();
  private static long reificationId = 0;

  @Override
  public void execute(INotification notification) {
    Thread thread = new Thread(this);
    thread.start();
  }

  @Override
  public void run() {
    sendNotification(AppFacade.UPLOADING_SI);
    sendNotification(AppFacade.UPDATE_STATUS_CMD,
        "Upload semantic information to server");
    sendNotification(AppFacade.UPDATE_STATUS_PROGRESS, -1);
    DocumentProxy docProxy = (DocumentProxy) facade
        .retrieveProxy(AppFacade.DOC_PROXY);
    for (int i = 0; i < docProxy.documents().size(); i++) {
      KIMDocument doc = docProxy.documents().get(i).getKIMDoc();
      String docUri = "http://bk.sport.owl#document" + doc.getDocumentId();
      statementList.add(new StatementImpl((Resource) new ResourceImpl(docUri),
          (Property) new PropertyImpl(NSUtil.rdf, "type"),
          (RDFNode) new ResourceImpl(NSUtil.bksport, "News")));
      try {
        statementList.add(new StatementImpl(
            (Resource) new ResourceImpl(docUri), (Property) new PropertyImpl(
                NSUtil.bksport, "hasURL"), (RDFNode) model
                .createTypedLiteral(doc.getSourceUrl().toString())));
      } catch (KIMCorporaException e) {
        Logger.getLogger(getClass()).error(ExceptionUtils.getStackTrace(e));
      }
      extractSemanticFromKIM(doc);
      extractSemanticFromWeight(doc);
      // reification
      extractReificationFromKIM(doc);
    }
    for (int i = 0; i < statementList.size(); i++) {
      System.out.println(statementList.get(i).toString());
    }
    ConnectorProxy cProxy = (ConnectorProxy) facade
        .retrieveProxy(AppFacade.CONNECTOR_PROXY);

    AGConnector connector = cProxy.getAGConnector();
    connector.connect();
    connector.setCatalogID("bksport");
    connector.setRepositoryID("main-repository");
    connector.openCatalog();
    connector.openRepository();
    connector.addTripleList(statementList);
    connector.closeRepository();
    connector.disconnect();

    statementList.clear();
    sendNotification(AppFacade.SI_UPLOADED);
    sendNotification(AppFacade.UPDATE_STATUS_CMD, "Semantic information uploaded");
    sendNotification(AppFacade.UPDATE_STATUS_PROGRESS, 0);
  }

  private void extractSemanticFromKIM(KIMDocument doc) {
    KIMAnnotationSet docAnnSet = null;
    try {
      docAnnSet = doc.getAnnotations();
      String docUri = "http://bk.sport.owl#document" + doc.getDocumentId();
      KIMAnnotationSet semanticAnnSet = docAnnSet.get("Semantic");
      Iterator<?> annIter = semanticAnnSet.iterator();
      while (annIter.hasNext()) {
        KIMAnnotation ann = (KIMAnnotation) annIter.next();
        KIMFeatureMap featureMap = ann.getFeatures();
        String info = featureMap.get("info").toString();
        if (featureMap.get("subject") != null
            && featureMap.get("predicate") != null
            && featureMap.get("object") != null) {
          String subject = featureMap.get("subject").toString();
          String predicate = featureMap.get("predicate").toString();
          String object = featureMap.get("object").toString();
          if (info != null && !info.contains("debug")) {
            reificationId++;
            String statementUri = NSUtil.rdf + "statement" + reificationId;
            statementList.add(new StatementImpl((Resource) new ResourceImpl(
                docUri),
                (Property) new PropertyImpl(NSUtil.bksport, "saidThat"),
                (RDFNode) new ResourceImpl(statementUri)));
            statementList.add(new StatementImpl((Resource) new ResourceImpl(
                statementUri), (Property) new PropertyImpl(NSUtil.rdf, "type"),
                (RDFNode) new ResourceImpl(NSUtil.rdf, "Statement")));
            statementList.add(new StatementImpl((Resource) new ResourceImpl(
                statementUri), (Property) new PropertyImpl(NSUtil.rdf,
                "subject"), (RDFNode) new ResourceImpl(subject)));
            statementList.add(new StatementImpl((Resource) new ResourceImpl(
                statementUri), (Property) new PropertyImpl(NSUtil.rdf,
                "predicate"), (RDFNode) new ResourceImpl(predicate)));
            if (object.contains("^^")) {
              object = object.substring(1, object.indexOf("^^") - 1);
              statementList.add(new StatementImpl((Resource) new ResourceImpl(
                  statementUri), (Property) new PropertyImpl(NSUtil.rdf,
                  "object"), (RDFNode) model.createTypedLiteral(object)));
            } else {
              statementList.add(new StatementImpl((Resource) new ResourceImpl(
                  statementUri), (Property) new PropertyImpl(NSUtil.rdf,
                  "object"), (RDFNode) new ResourceImpl(object)));
            }
          }
        }
      }
    } catch (KIMCorporaException ex) {
      Logger.getLogger(getClass()).error(ExceptionUtils.getStackTrace(ex));
    }
  }

  private void extractSemanticFromWeight(KIMDocument doc) {
    KIMAnnotationSet docAnnSet = null;
    try {
      docAnnSet = doc.getAnnotations();
      String docUri = "http://bk.sport.owl#document" + doc.getDocumentId();
      double totalWeight = 0;
      double meanWeight = 0;
      int titleEndOffset = 0;
      // store weight of inst
      HashMap<String, Double> instWeight = new HashMap<String, Double>();
      Iterator<?> annIter = docAnnSet.iterator();
      // avoid two annotation have the same offset and inst by store
      // (inst uri - start offset) to checking whether inst exist at that
      // position
      HashSet<String> instOffset = new HashSet<String>();
      while (annIter.hasNext()) {
        KIMAnnotation ann = (KIMAnnotation) annIter.next();
        Object inst = ann.getFeatures().get(FeatureConstants.INSTANCE);
        if (inst != null) {
          String instUri = inst.toString();
          if (!instUri.contains("http://bk.sport.owl#"))
            continue;
          if (!instOffset.contains(instUri)) {
            instOffset.add(instUri + "==" + ann.getStartOffset());
            Double previousWeight = instWeight.get(instUri);
            if (previousWeight != null)
              instWeight.put(instUri, previousWeight
                  + Constants.getRuleWeight().get("occurence"));
            else
              instWeight.put(instUri, Constants.getRuleWeight()
                  .get("occurence"));
          }
        }
      }
      // compute weight of inst base on occurence in title, rule
      // output semantic from rule
      annIter = docAnnSet.iterator();// reset iterator
      while (annIter.hasNext()) {
        KIMAnnotation ann = (KIMAnnotation) annIter.next();
        if (ann.getType().contains("Sentence")) {
          continue;
        }
        if (ann.getType().equals("Semantic")) {
          continue;
        } else if (ann.getEndOffset() <= titleEndOffset) {// annotation in title
          Object inst = ann.getFeatures().get(FeatureConstants.INSTANCE);
          if (inst != null) {
            double titleWeight = Constants.getRuleWeight().get("title");
            String instUri = inst.toString();
            if (!instUri.contains("http://bk.sport.owl#"))
              continue;
            if (!instOffset.contains(instUri)) {
              instWeight.put(instUri, titleWeight);
            } else {
              instWeight.put(instUri, instWeight.get(instUri) + titleWeight);
            }
          }
        }
      }
      // compute sum of weight
      for (String inst : instWeight.keySet()) {
        totalWeight += instWeight.get(inst);
      }
      if (!instWeight.isEmpty()) {
        meanWeight = totalWeight / instWeight.size();
      }
      // output &bk;about and &bk;contain
      for (String inst : instWeight.keySet()) {
        if (meanWeight < instWeight.get(inst)) {
          statementList.add(new StatementImpl((Resource) new ResourceImpl(
              docUri), (Property) new PropertyImpl(NSUtil.bksport, "about"),
              (RDFNode) new ResourceImpl(inst)));
        } else {
          statementList.add(new StatementImpl((Resource) new ResourceImpl(
              docUri), (Property) new PropertyImpl(NSUtil.bksport, "contain"),
              (RDFNode) new ResourceImpl(inst)));
        }
      }
    } catch (KIMCorporaException ex) {
      Logger.getLogger(UploadCommand.class).error(
          ExceptionUtils.getStackTrace(ex));
    }
  }

  private void extractReificationFromKIM(KIMDocument doc) {
    String docUri = NSUtil.bksport + "news" + doc.getDocumentId();
    KIMAnnotationSet docAnnSet = null;
    try {
      docAnnSet = doc.getAnnotations();
      KIMAnnotationSet semanticAnnSet = docAnnSet.get("Semantic");
      Iterator<?> annIter = semanticAnnSet.iterator();
      while (annIter.hasNext()) {
        KIMAnnotation ann = (KIMAnnotation) annIter.next();
        KIMFeatureMap featureMap = ann.getFeatures();
        String info = featureMap.get("info").toString();
        if (featureMap.get("subject") != null
            && featureMap.get("predicate") != null
            && featureMap.get("object") != null) {
          String subject = featureMap.get("subject").toString();
          String predicate = featureMap.get("predicate").toString();
          String object = featureMap.get("object").toString();
          if (object.contains("^^")) {
            object = object.substring(1, object.indexOf("^^") - 1);
          }
          String triple = NSUtil.toTriple(subject, predicate, object);
          if (info != null && info.contains("statement") && triple != null) {
            KIMConnector connector = ((ConnectorProxy) facade
                .retrieveProxy(AppFacade.CONNECTOR_PROXY)).getKIMConnector();
            KIMAnnotationSet reifiAnnSet = connector.annotate(object);
            Iterator<?> reifiIter = reifiAnnSet.iterator();
            while (reifiIter.hasNext()) {
              KIMAnnotation reifiAnn = (KIMAnnotation) reifiIter.next();
              KIMFeatureMap reifiFeatureMap = reifiAnn.getFeatures();
              if (reifiFeatureMap.containsKey("info")) {
                String reifiInfo = reifiFeatureMap.get("info").toString();
                String reifiSubject = reifiFeatureMap.get("subject").toString();
                String reifiPredicate = reifiFeatureMap.get("predicate")
                    .toString();
                String reifiObject = reifiFeatureMap.get("object").toString();
                if (reifiObject.contains("^^")) {
                  reifiObject = object.substring(1, object.indexOf("^^") - 1);
                }
                if (!reifiInfo.equals("debug") && reifiInfo.equals("statement")) {
                  reificationId++;
                  String statementUri = NSUtil.rdf + "statement"
                      + reificationId;
                  reificationId++;
                  String statementUri1 = NSUtil.rdf + "statement"
                      + reificationId;
                  statementList.add(new StatementImpl(
                      (Resource) new ResourceImpl(docUri),
                      (Property) new PropertyImpl(NSUtil.bksport, "saidThat"),
                      (RDFNode) new ResourceImpl(statementUri)));
                  statementList.add(new StatementImpl(
                      (Resource) new ResourceImpl(statementUri),
                      (Property) new PropertyImpl(NSUtil.rdf, "type"),
                      (RDFNode) new ResourceImpl(NSUtil.rdf, "Statement")));
                  statementList.add(new StatementImpl(
                      (Resource) new ResourceImpl(statementUri),
                      (Property) new PropertyImpl(NSUtil.rdf, "subject"),
                      (RDFNode) new ResourceImpl(subject)));
                  statementList.add(new StatementImpl(
                      (Resource) new ResourceImpl(statementUri),
                      (Property) new PropertyImpl(NSUtil.rdf, "predicate"),
                      (RDFNode) new ResourceImpl(NSUtil.rdf, "saidThat")));
                  statementList.add(new StatementImpl(
                      (Resource) new ResourceImpl(statementUri),
                      (Property) new PropertyImpl(NSUtil.rdf, "object"),
                      (RDFNode) new ResourceImpl(statementUri1)));
                  statementList.add(new StatementImpl(
                      (Resource) new ResourceImpl(statementUri1),
                      (Property) new PropertyImpl(NSUtil.rdf, "type"),
                      (RDFNode) new ResourceImpl(NSUtil.rdf, "Statement")));
                  statementList.add(new StatementImpl(
                      (Resource) new ResourceImpl(statementUri1),
                      (Property) new PropertyImpl(NSUtil.rdf, "subject"),
                      (RDFNode) new ResourceImpl(reifiSubject)));
                  statementList.add(new StatementImpl(
                      (Resource) new ResourceImpl(statementUri1),
                      (Property) new PropertyImpl(NSUtil.rdf, "predicate"),
                      (RDFNode) new ResourceImpl(reifiPredicate)));

                  if (reifiObject.contains("^^")) {
                    reifiObject = reifiObject.substring(1,
                        reifiObject.indexOf("^^") - 1);
                    statementList.add(new StatementImpl(
                        (Resource) new ResourceImpl(statementUri),
                        (Property) new PropertyImpl(NSUtil.rdf, "object"),
                        (RDFNode) model.createTypedLiteral(reifiObject)));
                  } else {
                    statementList.add(new StatementImpl(
                        (Resource) new ResourceImpl(statementUri1),
                        (Property) new PropertyImpl(NSUtil.rdf, "object"),
                        (RDFNode) new ResourceImpl(reifiObject)));
                  }
                }
              }
            }
          }
        }
      }
    } catch (KIMCorporaException ex) {
      Logger.getLogger(getClass()).error(ExceptionUtils.getStackTrace(ex));
    }
  }
}
