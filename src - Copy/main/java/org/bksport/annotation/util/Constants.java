package org.bksport.annotation.util;

import java.util.HashMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * 
 * @author congnh
 * 
 */
public class Constants {

  /**
   * XML constants file
   */
  private static Document docConst;

  private static final String pathConst = "./conf/constants.xml";

  private static HashMap<String, String> prefix_uriHashMap;

  private static HashMap<String, String> label_classHashMap = null;

  private static HashMap<String, String> label_propertyHashMap = null;

  private static HashMap<String, String> label_relationHashMap = null;

  private static HashMap<String, Double> rule_weightHashMap = null;

  private static void initConstants() {
    prefix_uriHashMap = new HashMap<String, String>();
    if (docConst == null)
      docConst = XMLUtil.loadDomDoc(pathConst);
    NodeList nsTagList = docConst.getElementsByTagName("namespace-list");
    // checking whether there is more than one "namespace" element in pathConst
    // file
    if (nsTagList.getLength() > 1)
      System.out.println("Error!!! There are more than one \"namespace-list\""
          + " element in " + pathConst);
    else if (nsTagList.getLength() == 1) {
      // retrieve namespace list
      NodeList nsList = ((Element) nsTagList.item(0))
          .getElementsByTagName("namespace");

      for (int index = 0; index < nsList.getLength(); index++) {

        // retrieve each ns with prefix and uri
        Element nsElement = (Element) nsList.item(index);
        String prefix = nsElement.getAttribute("prefix");
        String uri = nsElement.getAttribute("uri");

        // add to prefix_uriHashMap
        if (prefix != null && uri != null)
          prefix_uriHashMap.put(prefix, uri);
      }
    }
  }

  /**
   * Get URI of a prefix.
   * 
   * @param prefix
   * @return null if <code>prefix</code> does not exist in
   *         "./config/constants.xml"
   */
  public static String uri(String prefix) {
    if (prefix_uriHashMap == null) {
      initConstants();
    }
    return prefix_uriHashMap.get(prefix);
  }

  /**
   * Get prefix of a URI
   * 
   * @param uri
   * @return null if <code>uri</code> does not exist in "./config/constants.xml"
   */
  public static String prefix(String uri) {
    if (prefix_uriHashMap == null)
      initConstants();
    for (String prefix : prefix_uriHashMap.keySet()) {
      if (uri.equals(prefix_uriHashMap.get(prefix)))
        return prefix;
    }
    return null;
  }

  public static HashMap<String, Double> getRuleWeight() {
    if (rule_weightHashMap == null) {
      rule_weightHashMap = new HashMap<String, Double>();
      docConst = XMLUtil.loadDomDoc(pathConst);
      NodeList wTagList = docConst.getElementsByTagName("weight-list");
      if (wTagList.getLength() > 1)
        System.out.println("Error!!! There are more than one \"weight-list\""
            + " element in " + pathConst);
      else if (wTagList.getLength() == 1) {
        // retrieve weight list
        NodeList wList = ((Element) wTagList.item(0))
            .getElementsByTagName("weight");

        for (int index = 0; index < wList.getLength(); index++) {

          // retrieve each weight with rule and value
          Element wElement = (Element) wList.item(index);
          String rule = wElement.getAttribute("rule");
          String value = wElement.getAttribute("value");

          // add to rule_weightHashMap
          if (rule != null && value != null)
            rule_weightHashMap.put(rule, Double.parseDouble(value));
        }
      }
      return rule_weightHashMap;
    } else
      return rule_weightHashMap;
  }

  public static HashMap<String, String> getLabelOfClass() {
    if (label_classHashMap == null) {
      label_classHashMap = new HashMap<String, String>();
      docConst = XMLUtil.loadDomDoc(pathConst);
      NodeList luTagList = docConst.getElementsByTagName("label-uri-list");
      if (luTagList.getLength() > 1)
        System.out
            .println("Error!!! There are more than one \"label-uri-list\""
                + " element in " + pathConst);
      else if (luTagList.getLength() == 1) {
        // retrieve namespace list
        NodeList luList = ((Element) luTagList.item(0))
            .getElementsByTagName("label-class");

        for (int index = 0; index < luList.getLength(); index++) {

          // retrieve each ns with prefix and uri
          Element luElement = (Element) luList.item(index);
          String label = luElement.getAttribute("label");
          String uri = luElement.getAttribute("uri");

          // add to label_classHashMap
          if (label != null && uri != null)
            label_classHashMap.put(label, uri);
        }
      }
      return label_classHashMap;
    } else
      return label_classHashMap;
  }

  public static HashMap<String, String> getLabelOfProperty() {
    if (label_propertyHashMap == null) {
      label_propertyHashMap = new HashMap<String, String>();
      docConst = XMLUtil.loadDomDoc(pathConst);
      NodeList luTagList = docConst.getElementsByTagName("label-uri-list");
      if (luTagList.getLength() > 1)
        System.out
            .println("Error!!! There are more than one \"label-uri-list\""
                + " element in " + pathConst);
      else if (luTagList.getLength() == 1) {
        // retrieve namespace list
        NodeList luList = ((Element) luTagList.item(0))
            .getElementsByTagName("label-property");

        for (int index = 0; index < luList.getLength(); index++) {

          // retrieve each ns with prefix and uri
          Element luElement = (Element) luList.item(index);
          String label = luElement.getAttribute("label");
          String uri = luElement.getAttribute("uri");

          // add to prefix_uriHashMap
          if (label != null && uri != null)
            label_propertyHashMap.put(label, uri);
        }
      }
      return label_propertyHashMap;
    } else
      return label_propertyHashMap;
  }

  public static HashMap<String, String> getLabelOfRelation() {
    if (label_relationHashMap == null) {
      label_relationHashMap = new HashMap<String, String>();
      docConst = XMLUtil.loadDomDoc(pathConst);
      NodeList lrTagList = docConst.getElementsByTagName("label-relation-list");
      if (lrTagList.getLength() > 1)
        System.out
            .println("Error!!! There are more than one \"label-relation-list\""
                + " element in " + pathConst);
      else if (lrTagList.getLength() == 1) {
        // retrieve namespace list
        NodeList lrList = ((Element) lrTagList.item(0))
            .getElementsByTagName("relation");

        for (int index = 0; index < lrList.getLength(); index++) {

          // retrieve each ns with prefix and uri
          Element rElement = (Element) lrList.item(index);
          String label = rElement.getAttribute("label");
          String uri = rElement.getAttribute("uri");

          // add to prefix_uriHashMap
          if (label != null && uri != null)
            label_relationHashMap.put(label, uri);
        }
      }
      return label_relationHashMap;
    } else
      return label_relationHashMap;
  }
}