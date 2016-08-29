package org.bksport.annotation.core;

import com.ontotext.kim.client.KIMService;
import com.ontotext.kim.client.corpora.KIMAnnotation;
import com.ontotext.kim.client.corpora.KIMAnnotationImpl;
import com.ontotext.kim.client.corpora.KIMAnnotationSet;
import com.ontotext.kim.client.corpora.KIMCorporaException;
import com.ontotext.kim.client.corpora.KIMDocument;
import com.ontotext.kim.client.corpora.KIMFeatureMap;
import com.ontotext.kim.client.corpora.KIMFeatureMapImpl;
import com.ontotext.kim.client.model.FeatureConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bksport.annotation.connector.KIMConnector;
import org.bksport.annotation.mvc.AppFacade;
import org.bksport.annotation.mvc.model.ConnectorProxy;
import org.bksport.annotation.util.Constants;
import org.bksport.annotation.util.NSUtil;
import org.bksport.annotation.util.StringUtil;

/**
 * 
 * @author congnh
 * 
 */
public class DocumentProcessor {

  private KIMService service;

  public DocumentProcessor(KIMService service) {
    this.service = service;
  }

  public void setService(KIMService service) {
    this.service = service;
  }

  public KIMService getService() {
    return service;
  }

  public static HashMap<String, String> labelClass = new HashMap<String, String>();

  public static HashMap<String, String> labelProperty = new HashMap<String, String>();

  public static long reificationId = 0;

  public static void init() {
    initLabelClass();
    initLabelProperty();
  }

  public static void initLabelClass() {
    labelClass = Constants.getLabelOfClass();
  }

  public static void initLabelProperty() {
    labelProperty = Constants.getLabelOfProperty();
  }

  public static void annotate(KIMDocument doc) {
    for (String lb : labelClass.keySet()) {
      int[] indexes;
      try {
        indexes = StringUtil.indexesOfIgnoreCase(doc.getContent(), lb);
        if (indexes.length >= 0) {
          for (int i = 0; i < indexes.length; i++) {
            int anId = doc.getAnnotations().size();
            String type = "SportClass";
            KIMFeatureMap kimFeatureMap = new KIMFeatureMapImpl();
            kimFeatureMap.put("originName", lb);
            kimFeatureMap.put("class", labelClass.get(lb));
            KIMAnnotationImpl an = new KIMAnnotationImpl(anId, indexes[i],
                indexes[i] + lb.length(), type, kimFeatureMap);
            doc.getAnnotations().add(an);
          }
        }
      } catch (KIMCorporaException ex) {
        ex.printStackTrace();
      }
    }
    for (String lb : labelProperty.keySet()) {
      int[] indexes;
      try {
        indexes = StringUtil.indexesOfIgnoreCase(doc.getContent(), lb);
        if (indexes.length >= 0) {
          for (int i = 0; i < indexes.length; i++) {
            int anId = doc.getAnnotations().size();
            String type = "SportProperty";
            KIMFeatureMap kimFeatureMap = new KIMFeatureMapImpl();
            kimFeatureMap.put("originName", lb);
            kimFeatureMap.put("class", labelClass.get(lb));
            KIMAnnotationImpl an = new KIMAnnotationImpl(anId, indexes[i],
                indexes[i] + lb.length(), type, kimFeatureMap);
            doc.getAnnotations().add(an);
          }
        }
      } catch (KIMCorporaException ex) {
        ex.printStackTrace();
      }
    }
    try {
      KIMAnnotationSet personAnnSet = doc.getAnnotations("Person");
      Iterator personIterator = personAnnSet.iterator();
      KIMAnnotationSet sportPersonAnnSet = doc.getAnnotations("SportPerson");
      while (personIterator.hasNext()) {
        Iterator sportPersonIterator = sportPersonAnnSet.iterator();
        KIMAnnotation personAnn = (KIMAnnotation) personIterator.next();
        String personName = personAnn.getFeatures().get("originalName")
            .toString();
        while (sportPersonIterator.hasNext()) {
          KIMAnnotation sportPersonAnn = (KIMAnnotation) sportPersonIterator
              .next();
          String sportPersonName = sportPersonAnn.getFeatures()
              .get("originalName").toString();
          String sportPersonUri = sportPersonAnn.getFeatures().get("inst")
              .toString();
          String sportPersonClass = sportPersonAnn.getFeatures().get("class")
              .toString();
          if (sportPersonName.contains(personName)) {
            int annId = doc.getAnnotations().size();
            KIMFeatureMap kimFeatureMap = new KIMFeatureMapImpl();
            kimFeatureMap.put("originName", personName);
            kimFeatureMap.put("inst", sportPersonUri);
            kimFeatureMap.put("class", sportPersonClass);
            KIMAnnotationImpl ann = new KIMAnnotationImpl(annId,
                personAnn.getStartOffset(), personAnn.getEndOffset(),
                personName, kimFeatureMap);
            doc.getAnnotations().add(ann);
          }
        }
      }
    } catch (KIMCorporaException ex) {
      Logger.getLogger(DocumentProcessor.class.getName()).log(Level.SEVERE,
          null, ex);
    }
  }

  // <editor-fold defaultstate="collapsed"
  // desc="unused extractSemanticsAnnotation">
  public static String extractSemanticsAnnotation(KIMDocument doc) {
    if (doc == null)
      return null;
    KIMConnector connector = ((ConnectorProxy) AppFacade.getInstance()
        .retrieveProxy(AppFacade.CONNECTOR_PROXY)).getKIMConnector();

    String extract = "";
    String url = null;
    KIMAnnotationSet docAnnSet = null;
    try {
      docAnnSet = doc.getAnnotations();
      url = doc.getSourceUrl().toString();
    } catch (KIMCorporaException ex) {
      ex.printStackTrace();
    }
    KIMFeatureMap featureMap = doc.getFeatures();
    // extract title
    Object title = doc.getFeatures().get(KIMDocument.DOCUMENT_FEATURE_TITLE);
    KIMAnnotationSet titleAnnSet = null;
    if (title != null) {
        titleAnnSet = connector.annotate(title.toString());
        System.out.println(titleAnnSet.size());
        java.util.Iterator iterator = titleAnnSet.iterator();
        while (iterator.hasNext()) {
          KIMAnnotation ann = (KIMAnnotation) iterator.next();
          Object inst = ann.getFeatures().get(FeatureConstants.INSTANCE);
          if (inst.toString().contains("http://bk.sport.owl#")) {
            extract += "<" + url + ">" + " <http://bk.sport.owl#about>" + " <"
                + inst.toString() + ">\n";
          }
          System.out.println("Extract title: " + extract);
        }
    }
    if (docAnnSet != null) {
      // statistic annotations for each sentence
      KIMAnnotationSet senSet = docAnnSet.get("Sentence");
      Iterator senIterator = senSet.iterator();
      // store {sentenceStartOffset, sentenceEndOffset} for each sentence
      ArrayList<int[]> senOffsetList = new ArrayList<int[]>();
      // store {sentenceStartOffset, sentenceAnnotation} for each sentence
      HashMap<Integer, KIMAnnotation> senOffsetSenAnn = new HashMap<Integer, KIMAnnotation>();
      // store {sentenceStartOffset, annotations} for each sentence
      HashMap<Integer, ArrayList<KIMAnnotation>> senOffsetAnnList = new HashMap<Integer, ArrayList<KIMAnnotation>>();
      while (senIterator.hasNext()) {
        KIMAnnotation ann = (KIMAnnotation) senIterator.next();
        senOffsetSenAnn.put(ann.getStartOffset(), ann);
        senOffsetList
            .add(new int[] { ann.getStartOffset(), ann.getEndOffset() });
        senOffsetAnnList.put(ann.getStartOffset(),
            new ArrayList<KIMAnnotation>());
      }
      //
      Iterator annIterator = docAnnSet.iterator();
      while (annIterator.hasNext()) {
        KIMAnnotation ann = (KIMAnnotation) annIterator.next();
        for (int j = 0; j < senOffsetList.size(); j++) {
          // find sentence which ann belong to.
          if (senOffsetList.get(j)[0] <= ann.getStartOffset()
              && senOffsetList.get(j)[1] >= ann.getEndOffset()
              && ann.getFeatures().get(FeatureConstants.INSTANCE) != null
              && ann.getFeatures().get(FeatureConstants.INSTANCE).toString()
                  .contains("http://bk.sport.owl#")) {
            senOffsetAnnList.get(senOffsetList.get(j)[0]).add(ann);
            break;
          }
        }
      }
      // extract semantic with each sentence
      Set<Integer> senOffsetSet = senOffsetAnnList.keySet();
      for (int startOffset : senOffsetSet) {
        ArrayList<KIMAnnotation> annList = senOffsetAnnList.get(startOffset);
        if (annList.isEmpty())
          continue;
        KIMAnnotation senAnn = senOffsetSenAnn.get(startOffset);

        ArrayList<KIMAnnotation> sortAnnList = new ArrayList<KIMAnnotation>();
        // sort
        for (int i = 0; i < annList.size(); i++) {
          if (i == 0) {
            sortAnnList.add(annList.get(i));
          }
          for (int j = 0; j < sortAnnList.size(); j++) {
            if (sortAnnList.get(j).getStartOffset() >= annList.get(i)
                .getStartOffset()) {
              // two ann with the same inst can not be in the same position
              if (!annList
                  .get(i)
                  .getFeatures()
                  .get(FeatureConstants.INSTANCE)
                  .toString()
                  .equals(
                      sortAnnList.get(j).getFeatures()
                          .get(FeatureConstants.INSTANCE).toString()))
                sortAnnList.add(j, annList.get(i));
              break;
            }
          }
        }
        annList = sortAnnList;

        // Alex Ferguson said that "Hello world" .
        // "Hello world" said Alex Ferguson.
        String senText = senAnn.getFeatures().get("originalName").toString();
        int[] saidIndexes = StringUtil.indexesOfIgnoreCase(senText, "said");
        if (saidIndexes.length > 0) {
          int saidIndex = saidIndexes[0];// accept sentence with one said
          int dquote1 = senText.indexOf("\"", saidIndex);
          if (dquote1 > 0) {
            int dquote2 = senText.indexOf("\"", dquote1 + 1);
            if (dquote2 > 0) {
              KIMAnnotation ann = null;
              for (int i = 0; i < annList.size(); i++) {
                if (annList.get(i).getEndOffset() - senAnn.getStartOffset() < saidIndex)
                  ann = annList.get(i);
                else
                  break;
              }
              if (ann != null) {
                extract += "<"
                    + ann.getFeatures().get(FeatureConstants.INSTANCE) + "> "
                    + "<http://bk.sport.owl#said> " + "\""
                    + senText.substring(dquote1 + 1, dquote2) + "\".\n";
              }
            }
          } else {
            dquote1 = senText.substring(0, saidIndex + 1).lastIndexOf("\"");
            if (dquote1 > 0) {
              int dquote2 = senText.substring(0, dquote1 - 1).lastIndexOf("\"");
              if (dquote2 >= 0) {
                KIMAnnotation ann = null;
                for (int i = 0; i < annList.size(); i++) {
                  if (annList.get(i).getEndOffset() - senAnn.getStartOffset() > saidIndex) {
                    ann = annList.get(i);
                    break;
                  }
                }
                if (ann != null) {
                  extract += "<"
                      + ann.getFeatures().get(FeatureConstants.INSTANCE) + "> "
                      + "<http://bk.sport.owl#said> " + "\""
                      + senText.substring(dquote2 + 1, dquote1) + "\".\n";
                }
              }
            }
          }
        }
        HashMap<String, String> labelRelations = Constants.getLabelOfRelation();
        for (int i = 0; i < annList.size(); i++) {
          for (int j = i + 1; j < annList.size(); j++) {
            String text = textBetween(annList.get(i), annList.get(j), senAnn);
            for (String labelRel : labelRelations.keySet()) {
              if (text != null && text.contains(labelRel)) {
                extract += "<"
                    + annList.get(i).getFeatures()
                        .get(FeatureConstants.INSTANCE)
                    + "> "
                    + "<"
                    + labelRelations.get(labelRel)
                    + "> "
                    + "<"
                    + annList.get(j).getFeatures()
                        .get(FeatureConstants.INSTANCE) + ">.\n";
              }
            }
          }
        }
      }
    }
    return extract;
  }

  // </editor-fold>
  /**
   * return text between ann1 and ann2 in sentence
   * 
   * @param ann1
   * @param ann2
   * @param sentence
   * @return empty if ann1 overlap ann2, null if one of anns is not belong to
   *         sentence
   */
  private static String textBetween(KIMAnnotation ann1, KIMAnnotation ann2,
      KIMAnnotation sentence) {
    String senText = sentence.getFeatures().get("originalName").toString();
    int startOffset1 = ann1.getStartOffset();
    int endOffset1 = ann1.getEndOffset();
    int startOffset2 = ann2.getStartOffset();
    int endOffset2 = ann2.getEndOffset();
    int startSenOffset = sentence.getStartOffset();
    int endSenOffset = sentence.getEndOffset();
    if (startOffset1 >= startSenOffset && startOffset2 >= startSenOffset
        && endOffset1 <= endSenOffset && endOffset2 <= endSenOffset) {
      if (startOffset1 < startOffset2) {
        if (endOffset1 > startOffset2)
          return "";
        else {
          return senText.substring(endOffset1 - startSenOffset, startOffset2
              - startSenOffset);
        }
      } else {
        if (endOffset2 > startOffset1)
          return "";
        else {
          return senText.substring(endOffset2 - startSenOffset, startOffset1
              - startSenOffset);
        }
      }
    } else
      return null;
  }

  public static String extractSemantics(KIMDocument doc) {
    if (doc == null)
      return null;
    KIMConnector connector = ((ConnectorProxy) AppFacade.getInstance()
        .retrieveProxy(AppFacade.CONNECTOR_PROXY)).getKIMConnector();

    String extract = "";

    String urlDoc = null;
    KIMAnnotationSet docAnnSet = null;
    try {
      docAnnSet = doc.getAnnotations();
      urlDoc = "http://bk.sport.owl#document" + doc.getDocumentId();
      String url = doc.getSourceUrl().toString();
      extract += "<" + urlDoc + "> "
          + "<http://www.w3.org/1999/02/22-rdf-syntax-ns#type> "
          + "<http://bk.sport.owl#Document>.\n";
      extract += "<" + urlDoc + "> " + "<http://bk.sport.owl#hasURL> " + "\""
          + url + "\".\n";
    } catch (KIMCorporaException ex) {
      ex.printStackTrace();
    }

    if (docAnnSet != null) {
      double totalWeight = 0;
      double meanWeight = 0;
      int titleEndOffset = 0;
      // store weight of inst
      HashMap<String, Double> instWeight = new HashMap<String, Double>();

      // comput weight of inst base on occurence of inst
      Iterator annIter = docAnnSet.iterator();
      // avoid two annotation have the same offset and inst by store
      // (inst uri - start offset) to checking whether inst exist at that
      // position
      HashSet<String> instOffset = new HashSet<String>();
      while (annIter.hasNext()) {
        KIMAnnotation ann = (KIMAnnotation) annIter.next();
        if (ann.getType().contains("Sentence")) {
          if (ann.getStartOffset() == 0) {// title sentence have start offset 0
            titleEndOffset = ann.getEndOffset();
            extract += toTripleDeprecated(urlDoc, "hasTitle", ann.getFeatures()
                .get("originalName").toString())
                + "\n";
          }
          continue;
        }
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
          KIMFeatureMap featureMap = ann.getFeatures();
          String info = featureMap.get("info").toString();
          if (info != null && featureMap.containsKey("triple")) {
            extract += featureMap.get("triple").toString() + "\n";
          }
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
          extract += toTripleDeprecated(urlDoc, "about", inst) + "\n";
        } else {
          extract += toTripleDeprecated(urlDoc, "contain", inst) + "\n";
        }
      }
    }
    return extract;
  }

  public static String extractSemanticFromKIM(KIMDocument doc) {
    String extract = "";
    KIMAnnotationSet docAnnSet = null;
    try {
      docAnnSet = doc.getAnnotations();
      String docUri = "http://bk.sport.owl#document" + doc.getDocumentId();
      KIMAnnotationSet semanticAnnSet = docAnnSet.get("Semantic");
      Iterator<KIMAnnotation> annIter = semanticAnnSet.iterator();
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
          String triple = NSUtil.toTriple(subject, predicate, object);
          if (info != null && !info.contains("debug")) {
            /**
             * v1.0.0
             */
            // extract+=triple+"\n";
            /**
             * End v1.0.0
             */
            /**
             * v2.0.0
             */
            reificationId++;
            String statementUri = NSUtil.rdf + "statement" + reificationId;
            extract += NSUtil.toTriple(docUri, NSUtil.bksport + "saidThat",
                statementUri) + "\n";
            extract += NSUtil.toTriple(statementUri, NSUtil.rdf + "type",
                NSUtil.rdf + "Statement") + "\n";
            extract += NSUtil.toTriple(statementUri, NSUtil.rdf + "subject",
                subject) + "\n";
            extract += NSUtil.toTriple(statementUri, NSUtil.rdf + "predicate",
                predicate) + "\n";
            extract += NSUtil.toTriple(statementUri, NSUtil.rdf + "object",
                object) + "\n";
            /**
             * End v2.0.0
             */
          }
        }
      }
    } catch (KIMCorporaException ex) {
      Logger.getLogger(DocumentProcessor.class.getName()).log(Level.SEVERE,
          null, ex);
    }
    return extract;
  }

  public static String extractSemanticFromWeight(KIMDocument doc) {
    String extract = "";
    KIMAnnotationSet docAnnSet = null;
    try {
      docAnnSet = doc.getAnnotations();
      String docUri = "http://bk.sport.owl#document" + doc.getDocumentId();
      double totalWeight = 0;
      double meanWeight = 0;
      int titleEndOffset = 0;
      // store weight of inst
      HashMap<String, Double> instWeight = new HashMap<String, Double>();
      Iterator<KIMAnnotation> annIter = docAnnSet.iterator();
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
          extract += toTripleDeprecated(docUri, "about", inst) + "\n";
        } else {
          extract += toTripleDeprecated(docUri, "contain", inst) + "\n";
        }
      }
    } catch (KIMCorporaException ex) {
      Logger.getLogger(DocumentProcessor.class.getName()).log(Level.SEVERE,
          null, ex);
    }
    return extract;
  }

  public static String extractSemanticFromCustomRule(KIMDocument doc) {
    String extract = "";
    String docUri = "http://bk.sport.owl#document" + doc.getDocumentId();
    String url = null;
    KIMAnnotationSet docAnnSet = null;
    try {
      url = doc.getSourceUrl().toString();
      docAnnSet = doc.getAnnotations();

      // statistic annotations for each sentence
      KIMAnnotationSet senSet = docAnnSet.get("Sentence");
      Iterator senIterator = senSet.iterator();
      // store {sentenceStartOffset, sentenceEndOffset} for each sentence
      ArrayList<int[]> senOffsetList = new ArrayList<int[]>();
      // store {sentenceStartOffset, sentenceAnnotation} for each sentence
      HashMap<Integer, KIMAnnotation> senOffsetSenAnn = new HashMap<Integer, KIMAnnotation>();
      // store {sentenceStartOffset, annotations} for each sentence
      HashMap<Integer, ArrayList<KIMAnnotation>> senOffsetAnnList = new HashMap<Integer, ArrayList<KIMAnnotation>>();
      while (senIterator.hasNext()) {
        KIMAnnotation ann = (KIMAnnotation) senIterator.next();
        senOffsetSenAnn.put(ann.getStartOffset(), ann);
        senOffsetList
            .add(new int[] { ann.getStartOffset(), ann.getEndOffset() });
        senOffsetAnnList.put(ann.getStartOffset(),
            new ArrayList<KIMAnnotation>());
      }
      //
      Iterator annIterator = docAnnSet.iterator();
      while (annIterator.hasNext()) {
        KIMAnnotation ann = (KIMAnnotation) annIterator.next();
        for (int j = 0; j < senOffsetList.size(); j++) {
          // find sentence which ann belong to.
          if (senOffsetList.get(j)[0] <= ann.getStartOffset()
              && senOffsetList.get(j)[1] >= ann.getEndOffset()
              && ann.getFeatures().get(FeatureConstants.INSTANCE) != null
              && ann.getFeatures().get(FeatureConstants.INSTANCE).toString()
                  .contains("http://bk.sport.owl#")) {
            senOffsetAnnList.get(senOffsetList.get(j)[0]).add(ann);
            break;
          }
        }
      }
      // extract semantic with each sentence
      Set<Integer> senOffsetSet = senOffsetAnnList.keySet();
      for (int startOffset : senOffsetSet) {
        ArrayList<KIMAnnotation> annList = senOffsetAnnList.get(startOffset);
        if (annList.isEmpty())
          continue;
        KIMAnnotation senAnn = senOffsetSenAnn.get(startOffset);

        ArrayList<KIMAnnotation> sortAnnList = new ArrayList<KIMAnnotation>();
        // sort
        for (int i = 0; i < annList.size(); i++) {
          if (i == 0) {
            sortAnnList.add(annList.get(i));
          }
          for (int j = 0; j < sortAnnList.size(); j++) {
            if (sortAnnList.get(j).getStartOffset() >= annList.get(i)
                .getStartOffset()) {
              // two ann with the same inst can not be in the same position
              if (!annList
                  .get(i)
                  .getFeatures()
                  .get(FeatureConstants.INSTANCE)
                  .toString()
                  .equals(
                      sortAnnList.get(j).getFeatures()
                          .get(FeatureConstants.INSTANCE).toString()))
                sortAnnList.add(j, annList.get(i));
              break;
            }
          }
        }
        annList = sortAnnList;
        HashMap<String, String> labelRelations = Constants.getLabelOfRelation();
        for (int i = 0; i < annList.size(); i++) {
          for (int j = i + 1; j < annList.size(); j++) {
            String text = textBetween(annList.get(i), annList.get(j), senAnn);
            for (String labelRel : labelRelations.keySet()) {
              if (text != null && text.contains(labelRel)) {
                extract += "<"
                    + annList.get(i).getFeatures()
                        .get(FeatureConstants.INSTANCE)
                    + "> "
                    + "<"
                    + labelRelations.get(labelRel)
                    + "> "
                    + "<"
                    + annList.get(j).getFeatures()
                        .get(FeatureConstants.INSTANCE) + ">.\n";
              }
            }
          }
        }
      }
    } catch (KIMCorporaException ex) {
      Logger.getLogger(DocumentProcessor.class.getName()).log(Level.SEVERE,
          null, ex);
    }
    extract += "<" + docUri + "> "
        + "<http://www.w3.org/1999/02/22-rdf-syntax-ns#type> "
        + "<http://bk.sport.owl#Document>.\n";
    extract += "<" + docUri + "> " + "<http://bk.sport.owl#hasURL> " + "\""
        + url + "\".\n";
    return extract;
  }

  public static String extractReificationFromKIM(KIMDocument doc) {
    String extract = "";
    String docUri = NSUtil.bksport + "document" + doc.getDocumentId();
    String url = null;
    KIMAnnotationSet docAnnSet = null;
    try {
      url = doc.getSourceUrl().toString();
      docAnnSet = doc.getAnnotations();
      KIMAnnotationSet semanticAnnSet = docAnnSet.get("Semantic");
      Iterator<KIMAnnotation> annIter = semanticAnnSet.iterator();
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
          String triple = NSUtil.toTriple(subject, predicate, object);
          if (info != null && info.contains("statement") && triple != null) {
            KIMConnector connector = ((ConnectorProxy) AppFacade.getInstance()
                .retrieveProxy(AppFacade.CONNECTOR_PROXY)).getKIMConnector();
            String statement = object.substring(1, object
                .lastIndexOf("\"^^<http://www.w3.org/2001/XMLSchema#string>"));
            KIMAnnotationSet reifiAnnSet = connector.annotate(statement);
            Iterator<KIMAnnotation> reifiIter = reifiAnnSet.iterator();
            while (reifiIter.hasNext()) {
              KIMAnnotation reifiAnn = reifiIter.next();
              KIMFeatureMap reifiFeatureMap = reifiAnn.getFeatures();
              if (reifiFeatureMap.containsKey("info")) {
                String reifiInfo = reifiFeatureMap.get("info").toString();
                String reifiSubject = reifiFeatureMap.get("subject").toString();
                String reifiPredicate = reifiFeatureMap.get("predicate")
                    .toString();
                String reifiObject = reifiFeatureMap.get("object").toString();
                if (!reifiInfo.equals("debug") && reifiInfo.equals("statement")) {
                  /**
                   * v1.0.0
                   */
                  // reificationId++;
                  // String statementUri = NS.rdf+"statement"+reificationId;
                  // extract+= NS.toTriple(subject, NS.bksport+"saidThat",
                  // statementUri) + "\n";
                  // extract+= NS.toTriple(statementUri, NS.rdf+"type",
                  // NS.rdf+"Statement") + "\n";
                  // extract+= NS.toTriple(statementUri, NS.rdf+"subject",
                  // reifiSubject) + "\n";
                  // extract+= NS.toTriple(statementUri, NS.rdf+"predicate",
                  // reifiPredicate) + "\n";
                  // extract+= NS.toTriple(statementUri, NS.rdf+"object",
                  // reifiObject) + "\n";
                  /**
                   * End v1.0.0
                   */
                  /**
                   * v2.0.0
                   */
                  reificationId++;
                  String statementUri = NSUtil.rdf + "statement"
                      + reificationId;
                  reificationId++;
                  String statementUri1 = NSUtil.rdf + "statement"
                      + reificationId;
                  extract += NSUtil.toTriple(docUri, NSUtil.bksport
                      + "saidThat", statementUri)
                      + "\n";
                  extract += NSUtil.toTriple(statementUri, NSUtil.rdf + "type",
                      NSUtil.rdf + "Statement") + "\n";
                  extract += NSUtil.toTriple(statementUri, NSUtil.rdf
                      + "subject", subject)
                      + "\n";
                  extract += NSUtil.toTriple(statementUri, NSUtil.rdf
                      + "predicate", NSUtil.rdf + "saidThat")
                      + "\n";
                  extract += NSUtil.toTriple(statementUri, NSUtil.rdf
                      + "object", statementUri1)
                      + "\n";
                  extract += NSUtil.toTriple(statementUri1,
                      NSUtil.rdf + "type", NSUtil.rdf + "Statement") + "\n";
                  extract += NSUtil.toTriple(statementUri1, NSUtil.rdf
                      + "subject", reifiSubject)
                      + "\n";
                  extract += NSUtil.toTriple(statementUri1, NSUtil.rdf
                      + "predicate", reifiPredicate)
                      + "\n";
                  extract += NSUtil.toTriple(statementUri1, NSUtil.rdf
                      + "object", reifiObject)
                      + "\n";
                  /**
                   * End v2.0.0
                   */
                }
              }
            }
          }
        }
      }
    } catch (KIMCorporaException ex) {
      Logger.getLogger(DocumentProcessor.class.getName()).log(Level.SEVERE,
          null, ex);
    }
    return extract;
  }

  private static String toTripleDeprecated(String subject, String predicate,
      String object) {
    String rt = "";
    if (subject == null || predicate == null || object == null
        | subject.isEmpty() || predicate.isEmpty() || object.isEmpty()) {
      return null;
    } else {
      return "<"
          + subject
          + ">"
          + " <http://bk.sport.owl#"
          + predicate
          + "> "
          + (object.contains("http://bk.sport.owl#") ? ("<" + object + ">")
              : ("\"" + object + "\"")) + ".";
    }
  }

}
