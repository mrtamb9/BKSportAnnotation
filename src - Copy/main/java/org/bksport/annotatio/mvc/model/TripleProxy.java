package org.bksport.annotation.mvc.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.puremvc.java.patterns.proxy.Proxy;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;

/**
 *  
 * @author congnh
 *
 */
@SuppressWarnings("unchecked")
public class TripleProxy extends Proxy {

  public TripleProxy(String proxyName) {
    super(proxyName, new HashMap<Node, Set<Triple>>());
  }

  public Map<Node, Set<Triple>> getAllTriples() {
    return (Map<Node, Set<Triple>>) getData();
  }

  public void addTriple(Node context, Triple triple) {
    addContext(context);
    getAllTriples().get(context).add(triple);
  }

  public void removeTriple(Node context, Triple triple) {
    if (getAllTriples().containsKey(context))
      getAllTriples().get(context).remove(triple);
  }

  public void addContext(Node context) {
    if (!getAllTriples().containsKey(context)) {
      getAllTriples().put(context, new HashSet<Triple>());
    }
  }

  public void removeContext(Node context) {
    getAllTriples().remove(context);
  }

}
