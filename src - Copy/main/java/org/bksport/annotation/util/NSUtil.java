package org.bksport.annotation.util;

/**
 * 
 * @author congnh
 * 
 */
public class NSUtil {

  public static final String bksport = "http://bk.sport.owl#";
  public static final String rdf = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
  public static final String rdfs = "http://www.w3.org/2000/01/rdf-schema#";
  public static final String owl = "http://www.w3.org/2002/07/owl#";
  public static final String dc = "http://purl.org/dc/elements/1.1/";
  public static final String dcterms = "http://purl.org/dc/terms/";
  public static final String foaf = "http://xmlns.com/foaf/0.1/";
  public static final String fti = "http://franz.com/ns/allegrograph/2.2/textindex/";
  public static final String skos = "http://www.w3.org/2004/02/skos/core#";
  public static final String xsd = "http://www.w3.org/2001/XMLSchema#";
  public static final String prefix = "prefix bksport: <" + bksport + ">\n"
      + "prefix rdf: <" + rdf + ">\n" + "prefix rdfs: <" + rdfs + ">\n"
      + "prefix owl: <" + owl + ">\n" + "prefix xsd: <" + xsd + ">\n";

  public static boolean isUri(String str) {
    return str.startsWith(bksport) || str.startsWith(rdf)
        || str.startsWith(rdfs) || str.startsWith(owl) || str.startsWith(xsd);
  }

  public static String bksport(String qname) {
    return bksport + qname;
  }

  public static String rdf(String qname) {
    return rdf + qname;
  }

  public static String rdfs(String qname) {
    return rdfs + qname;
  }

  public static String owl(String qname) {
    return owl + qname;
  }

  public static String xsd(String qname) {
    return xsd + qname;
  }

  public static String toTriple(String subject, String predicate, String object) {
    if (subject == null || predicate == null || object == null
        | subject.isEmpty() || predicate.isEmpty() || object.isEmpty()) {
      return null;
    } else {
      return "<" + subject + "> " + "<" + predicate + "> "
          + (NSUtil.isUri(object) ? ("<" + object + "> ") : (object)) + ".";
    }
  }

}
