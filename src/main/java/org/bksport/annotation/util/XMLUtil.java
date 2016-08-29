package org.bksport.annotation.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * 
 * @author congnh
 * 
 */
public class XMLUtil {

  private static DocumentBuilderFactory docBuilderFactory;
  private static DocumentBuilder docBuilder;

  private static void initXMLUtil() {
    try {
      docBuilderFactory = DocumentBuilderFactory.newInstance();
      docBuilder = docBuilderFactory.newDocumentBuilder();
    } catch (ParserConfigurationException ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Create new XML dom Document
   * 
   * @return
   */
  public static Document createDomDoc() {
    if (docBuilder == null)
      initXMLUtil();
    return docBuilder.newDocument();
  }

  /**
   * Load XML document from <code>pathName</code>
   * 
   * @param pathName
   * @return XML document
   */
  public static Document loadDomDoc(String pathName) {
    File file = new File(pathName);
    if (docBuilder == null)
      initXMLUtil();
    Document doc = null;
    try {
      doc = docBuilder.parse(file);
    } catch (IOException ex) {
      ex.printStackTrace();
    } catch (SAXException ex) {
      ex.printStackTrace();
    }
    return doc;
  }

  /***
   * Save a <code>org.w3c.dom.Document</code> to xml file
   * 
   * @see Document
   * @param fileName
   * @param doc
   * @return
   */
  public static boolean saveDomDoc(String fileName, Document doc) {
    try {
      BufferedOutputStream stream = new BufferedOutputStream(
          new FileOutputStream(fileName));
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      DOMSource source = new DOMSource(doc);
      StreamResult result = new StreamResult(stream);
      transformer.transform(source, result);
      return true;
    } catch (TransformerException ex) {
      ex.printStackTrace();
    } catch (FileNotFoundException ex) {
      ex.printStackTrace();
    }
    return false;
  }

}
