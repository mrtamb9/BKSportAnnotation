package org.bksport.annotation.mvc.control;

import com.ontotext.kim.client.corpora.KIMCorporaException;
import com.ontotext.kim.client.corpora.KIMDocument;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.bksport.annotation.core.DocumentProcessor;
import org.bksport.annotation.mvc.AppFacade;
import org.bksport.annotation.mvc.model.DocumentProxy;
import org.bksport.annotation.util.NSUtil;
import org.puremvc.java.interfaces.INotification;
import org.puremvc.java.patterns.command.SimpleCommand;

/**
 * 
 * @author congnh
 * 
 */
public class ExportCommand extends SimpleCommand {

  @Override
  public void execute(INotification notification) {
    DocumentProxy docProxy = (DocumentProxy) facade
        .retrieveProxy(AppFacade.DOC_PROXY);
    String exportStr = "";
    for (String id : docProxy.documents().keySet()) {
      KIMDocument doc = docProxy.documents().get(id).getKIMDoc();
      // <editor-fold defaultstate="collapsed" desc="comment">
      // try{
      // String url = doc.getSourceUrl().toString();
      // KIMAnnotationSet annSet = doc.getAnnotations();
      // Iterator iterator = annSet.iterator();
      // HashSet<String> setInst = new HashSet<String>();
      // while(iterator.hasNext()){
      // KIMAnnotation ann = (KIMAnnotation)iterator.next();
      // Object inst = ann.getFeatures().get(FeatureConstants.INSTANCE);
      // if(inst!= null){
      // if(inst.toString().contains("http://bk.sport.owl#")
      // && setInst.add(inst.toString()))
      // exportStr +="<"+url+">"
      // + " <http://bk.sport.owl#containAnnotation "
      // + "<"+inst.toString()+">.\n";
      // }
      // }
      // }catch(KIMCorporaException ex){
      // ex.printStackTrace();
      // }
      // </editor-fold>
      /**
       * v0.0.0
       */
      // String extract = DocumentUtil.extractSemantics(doc);
      /**
       * End v0.0.0
       */
      /**
       * v1.0.0
       */
      // String extract = DocumentUtil.extractSemanticFromKIM(doc)+
      // DocumentUtil.extractSemanticFromWeight(doc)
      // +DocumentUtil.extractSemanticFromCustomRule(doc);
      /**
       * End v1.0.0
       */
      /**
       * v2.0.0
       */
      String extract = DocumentProcessor.extractSemanticFromKIM(doc)
          + DocumentProcessor.extractSemanticFromWeight(doc);
      String docUri = "http://bk.sport.owl#document" + doc.getDocumentId();
      String url;
      try {
        url = doc.getSourceUrl().toString();
        extract += NSUtil.toTriple(docUri, NSUtil.rdf("type"),
            NSUtil.bksport("Document"))
            + "\n";
        extract += NSUtil.toTriple(docUri, NSUtil.rdf("hasURL"), "\"" + url
            + "\"^^<http://www.w3.org/2001/XMLSchema#string>")
            + "\n";
      } catch (KIMCorporaException ex) {
        Logger.getLogger(getClass()).info(ExceptionUtils.getStackTrace(ex));
      }
      /**
       * End v2.0.0
       */
      if (extract != null && !extract.isEmpty())
        exportStr += extract + "\n";
    }
    /**
     * V2
     */
    // reification
    for (String id: docProxy.documents().keySet()) {
      KIMDocument doc = docProxy.documents().get(id).getKIMDoc();
      String extract = DocumentProcessor.extractReificationFromKIM(doc);
      if (extract != null && !extract.isEmpty())
        exportStr += extract + "\n";
    }
    /**
     * End V2
     */
    sendNotification(AppFacade.SI_EXPORTED, (Object) exportStr);
    exportStr = null;
  }

}
