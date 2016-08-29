package org.bksport.annotation.connector;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.openrdf.query.BindingSet;

import com.ontotext.kim.client.GetService;
import com.ontotext.kim.client.KIMService;
import com.ontotext.kim.client.corpora.KIMAnnotationSet;
import com.ontotext.kim.client.corpora.KIMCorporaException;
import com.ontotext.kim.client.corpora.KIMDocument;
import com.ontotext.kim.client.query.KIMQueryException;
import com.ontotext.kim.client.query.SemanticQuery;
import com.ontotext.kim.client.semanticrepository.ClosableIterator;

/**
 * 
 * Connector to KIM's service
 * 
 * @author congnh
 * 
 */
public class KIMConnector {

  private String host;
  private int port;
  private KIMService kimService;
  private boolean isConnected;

  public KIMConnector() {
    this("localhost", 1099);
  }

  public KIMConnector(String host, int port) {
    this.host = host;
    this.port = port;
    isConnected = false;
  }

  public boolean isConnected() {
    return isConnected;
  }

  public boolean connect() {
    if (host != null && port != -1) {
      try {
        kimService = GetService.from(host, port);
        isConnected = true;
        return true;
      } catch (RemoteException e) {
        Logger.getLogger(getClass()).error(ExceptionUtils.getStackTrace(e));
      } catch (NotBoundException e) {
        Logger.getLogger(getClass()).error(ExceptionUtils.getStackTrace(e));
      }
    } else {
      Logger.getLogger(getClass()).warn("Invalid host or port!");
    }
    return false;
  }

  public void disconnect() {
    isConnected = false;
    kimService = null;
  }

  public KIMDocument annotate(KIMDocument doc) {
    try {
      return kimService.getSemanticAnnotationAPI().execute(doc);
    } catch (RemoteException e) {
      Logger.getLogger(getClass()).error(ExceptionUtils.getStackTrace(e));
    }
    return null;
  }

  public KIMAnnotationSet annotate(String text) {
    if (kimService != null && text != null) {
      try {
        KIMAnnotationSet kimAnnSet = kimService.getSemanticAnnotationAPI()
            .execute(text);
        return kimAnnSet;
      } catch (RemoteException e) {
        Logger.getLogger(getClass()).error(ExceptionUtils.getStackTrace(e));
      }
    } else {
      Logger.getLogger(getClass()).warn(
          "Invalid semantic annotation api or text");
    }
    return null;
  }

  public KIMDocument create(String text) {
    if (kimService != null && text != null) {
      try {
        return kimService.getCorporaAPI().createDocument(text, false);
      } catch (KIMCorporaException e) {
        Logger.getLogger(getClass()).error(ExceptionUtils.getStackTrace(e));
      } catch (RemoteException e) {
        Logger.getLogger(getClass()).error(ExceptionUtils.getStackTrace(e));
      }
    } else {

    }
    return null;
  }

  public boolean execAsk(String query) {
    try {
      ClosableIterator<BindingSet> iterator = kimService
          .getSemanticRepositoryAPI().evaluateQuery(query, "SPARQL");
      while (iterator.hasNext()) {
        System.out.println(iterator.next().toString());
      }
      return true;
    } catch (KIMQueryException e) {
      Logger.getLogger(getClass()).error(ExceptionUtils.getStackTrace(e));
    } catch (RemoteException e) {
      Logger.getLogger(getClass()).error(ExceptionUtils.getStackTrace(e));
    }
    return false;
  }

  public ClosableIterator<BindingSet> execSelect(String query) {
    try {
      ClosableIterator<BindingSet> iterator = kimService
          .getSemanticRepositoryAPI().evaluateQuery(query, "SPARQL");
      return iterator;
    } catch (KIMQueryException e) {
      Logger.getLogger(getClass()).error(ExceptionUtils.getStackTrace(e));
    } catch (RemoteException e) {
      Logger.getLogger(getClass()).error(ExceptionUtils.getStackTrace(e));
    }
    return null;
  }

  public ClosableIterator<BindingSet> execSelectInf(String query) {
    try {
      ClosableIterator<BindingSet> iterator = kimService
          .getSemanticRepositoryAPI().evaluateQuery(query, "SPARQL", true);
      return iterator;
    } catch (KIMQueryException e) {
      Logger.getLogger(getClass()).error(ExceptionUtils.getStackTrace(e));
    } catch (RemoteException e) {
      Logger.getLogger(getClass()).error(ExceptionUtils.getStackTrace(e));
    }
    return null;
  }

  public void queryDocument(SemanticQuery semanticQuery) {
    if (kimService != null) {
      try {
        kimService.getQueryAPI().getDocumentIds(semanticQuery);
      } catch (KIMQueryException e) {
        Logger.getLogger(getClass()).error(ExceptionUtils.getStackTrace(e));
      } catch (RemoteException e) {
        Logger.getLogger(getClass()).error(ExceptionUtils.getStackTrace(e));
      }
    }
  }

  public String getHost() {
    return host;
  }

  public int getPort() {
    return port;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public void shutdownServer() {
    if (kimService != null)
      kimService.shutdown();
  }

}
