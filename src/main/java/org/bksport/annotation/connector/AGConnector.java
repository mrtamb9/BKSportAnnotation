package org.bksport.annotation.connector;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.openrdf.OpenRDFException;
import org.openrdf.repository.RepositoryException;

import com.franz.agraph.http.exception.AGHttpException;
import com.franz.agraph.jena.AGGraphMaker;
import com.franz.agraph.jena.AGInfModel;
import com.franz.agraph.jena.AGModel;
import com.franz.agraph.jena.AGQuery;
import com.franz.agraph.jena.AGQueryExecution;
import com.franz.agraph.jena.AGQueryExecutionFactory;
import com.franz.agraph.jena.AGQueryFactory;
import com.franz.agraph.jena.AGReasoner;
import com.franz.agraph.repository.AGCatalog;
import com.franz.agraph.repository.AGRepository;
import com.franz.agraph.repository.AGServer;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Statement;

/**
 * 
 * Connector to AllegrpGraph's service
 * 
 * @author congnh
 * 
 */
public class AGConnector {

  private String host;
  private int port;
  private String username;
  private String password;
  private String repositoryID;
  private String catalogID;
  private AGServer agServer;
  private AGCatalog agCatalog;
  private AGRepository agRepository;

  public AGConnector() {

  }

  public AGConnector(String host, int port, String username, String password) {
    this.host = host;
    this.password = password;
    this.port = port;
    this.username = username;
  }

  public void addTriple(Statement s) {
    if (agRepository != null || s == null) {
      try {
        AGModel agModel = new AGModel(new AGGraphMaker(
            agRepository.getConnection()).openGraph());
        if (!agModel.contains(s)) {
          agModel.add(s);
        }
        agModel.getGraph().close();
        agModel.close();
      } catch (RepositoryException ex) {
        Logger.getLogger(getClass()).error(ExceptionUtils.getStackTrace(ex));
      }
    } else {
      Logger.getLogger(getClass()).warn("Invalid repository or statement");
    }
  }

  public void addTripleList(List<Statement> sList) {
    if (agRepository != null || sList == null) {
      try {
        AGModel agModel = new AGModel(new AGGraphMaker(
            agRepository.getConnection()).openGraph());
        for (int i = 0; i < sList.size(); i++) {
          if (!agModel.contains(sList.get(i))) {
            agModel.add(sList.get(i));
          }
        }
        agModel.getGraph().close();
        agModel.close();
      } catch (RepositoryException ex) {
        Logger.getLogger(getClass()).error(ExceptionUtils.getStackTrace(ex));
      }
    } else {
      Logger.getLogger(getClass()).warn("Invalid repository or statement");
    }
  }

  public void removeTriple(Statement s) {
    if (agRepository != null || s == null) {
      try {
        AGModel agModel = new AGModel(new AGGraphMaker(
            agRepository.getConnection()).openGraph());
        agModel.remove(s);
        agModel.getGraph().close();
        agModel.close();
      } catch (RepositoryException ex) {
        Logger.getLogger(getClass()).error(ExceptionUtils.getStackTrace(ex));
      }
    } else {
      Logger.getLogger(getClass()).warn("Invalid repository or statement");
    }
  }

  public void removeTripleList(List<Statement> sList) {
    if (agRepository != null || sList == null) {
      try {
        AGModel agModel = new AGModel(new AGGraphMaker(
            agRepository.getConnection()).openGraph());
        agModel.remove(sList);
        agModel.getGraph().close();
        agModel.close();
      } catch (RepositoryException ex) {
        Logger.getLogger(getClass()).error(ExceptionUtils.getStackTrace(ex));
      }
    } else {
      Logger.getLogger(getClass()).warn("Invalid repository or statement");
    }
  }

  public void modifyTriple(Statement oldS, Statement newS) {
    if (agRepository != null || (oldS == null && newS != null)) {
      try {
        AGModel agModel = new AGModel(new AGGraphMaker(
            agRepository.getConnection()).openGraph());
        if (agModel.contains(oldS)) {
          agModel.remove(oldS);
          agModel.add(newS);
        }
        agModel.getGraph().close();
        agModel.close();
      } catch (RepositoryException ex) {
        Logger.getLogger(getClass()).error(ExceptionUtils.getStackTrace(ex));
      }
    } else {
      Logger.getLogger(getClass()).warn("Invalid repository or statement");
    }
  }

  public void modifyTripleList(List<Statement> oldSList,
      List<Statement> newSList) {
    if (agRepository != null
        || (oldSList != null && newSList != null && oldSList.size() != newSList
            .size())) {
      try {
        AGModel agModel = new AGModel(new AGGraphMaker(
            agRepository.getConnection()).openGraph());
        for (int i = 0; i < oldSList.size(); i++) {
          if (agModel.contains(oldSList.get(i))) {
            agModel.remove(oldSList.get(i));
            agModel.add(newSList.get(i));
          }
        }
        agModel.getGraph().close();
        agModel.close();
      } catch (RepositoryException ex) {
        Logger.getLogger(getClass()).error(ExceptionUtils.getStackTrace(ex));
      }
    } else {
      Logger.getLogger(getClass()).warn("Invalid repository or statement");
    }
  }

  public boolean connect() {
    if (host != null && port != -1 && username != null && password != null) {
      agServer = new AGServer("http://" + host + ":" + port, username, password);
      return true;
    } else {
      Logger.getLogger(getClass()).warn(
          "Invalid host, port, username or password!");
    }
    return false;
  }

  public void closeRepository() {
    if (agRepository != null)
      try {
        agRepository.close();
      } catch (RepositoryException ex) {
        Logger.getLogger(getClass()).error(ExceptionUtils.getStackTrace(ex));
      }
  }

  public void disconnect() {
    if (agServer != null)
      agServer.close();
  }

  public boolean execAsk(String query) {
    if (agRepository != null && query != null) {
      AGQuery agQuery = AGQueryFactory.create(query);
      try {
        AGModel agModel = new AGModel(new AGGraphMaker(
            agRepository.getConnection()).openGraph());
        AGQueryExecution agQueryExecution = AGQueryExecutionFactory.create(
            agQuery, agModel);
        boolean rs = agQueryExecution.execAsk();
        agQueryExecution.close();
        agModel.getGraph().close();
        agModel.close();
        return rs;
      } catch (RepositoryException ex) {
        Logger.getLogger(getClass()).error(ExceptionUtils.getStackTrace(ex));
      }
    } else {
      Logger.getLogger(getClass()).warn("Invalid repository or query");
    }
    return false;
  }

  public void execConstruct(String query) {
    throw new UnsupportedOperationException();
  }

  public ResultSet execSelect(String query) {
    if (agRepository != null && query != null) {
      AGQuery agQuery = AGQueryFactory.create(query);
      try {
        AGModel agModel = new AGModel(new AGGraphMaker(
            agRepository.getConnection()).openGraph());
        AGQueryExecution agQueryExecution = AGQueryExecutionFactory.create(
            agQuery, agModel);
        ResultSet rs = agQueryExecution.execSelect();
        agQueryExecution.close();
        agModel.getGraph().close();
        agModel.close();
        return rs;
      } catch (RepositoryException ex) {
        Logger.getLogger(getClass()).error(ExceptionUtils.getStackTrace(ex));
      }
    } else {
      Logger.getLogger(getClass()).warn("Invalid repository or query");
    }
    return null;
  }

  public ResultSet execSelectInf(String query) {
    if (agRepository != null && query != null) {
      AGQuery agQuery = AGQueryFactory.create(query);
      try {
        AGReasoner agReasoner = new AGReasoner();
        AGModel agModel = new AGModel(new AGGraphMaker(
            agRepository.getConnection()).openGraph());
        AGInfModel agInfModel = new AGInfModel(agReasoner, agModel);
        AGQueryExecution agQueryExecution = AGQueryExecutionFactory.create(
            agQuery, agInfModel);
        ResultSet rs = agQueryExecution.execSelect();
        agQueryExecution.close();
        agModel.getGraph().close();
        agModel.close();
        agInfModel.getGraph().close();
        agInfModel.close();
        return rs;
      } catch (RepositoryException ex) {
        Logger.getLogger(getClass()).error(ExceptionUtils.getStackTrace(ex));
      }
    } else {
      Logger.getLogger(getClass()).warn("Invalid repository or query");
    }
    return null;
  }

  public String getCatalogID() {
    return catalogID;
  }

  public String getHost() {
    return host;
  }

  public String getPassword() {
    return password;
  }

  public int getPort() {
    return port;
  }

  public String getRepositoryID() {
    return repositoryID;
  }

  public String getUsername() {
    return username;
  }

  public void openCatalog() {
    if (agServer != null && catalogID != null) {
      try {
        agCatalog = agServer.getCatalog(catalogID);
      } catch (AGHttpException e) {
        Logger.getLogger(getClass()).error(ExceptionUtils.getStackTrace(e));
      }
    }
  }

  public void openRepository() {
    if (agCatalog != null && repositoryID != null) {
      try {
        if (agRepository != null)
          agRepository.close();
        if (agCatalog.hasRepository(repositoryID)) {
          agRepository = agCatalog.openRepository(repositoryID);
        } else {
          Logger.getLogger(getClass()).warn(
              "Catalog " + catalogID + " doen't contains repository "
                  + repositoryID);
        }
      } catch (OpenRDFException e) {
        Logger.getLogger(getClass()).error(ExceptionUtils.getStackTrace(e));
      }
    }
  }

  public void setCatalogID(String catalogID) {
    this.catalogID = catalogID;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public void setRepositoryID(String repositoryID) {
    this.repositoryID = repositoryID;
  }

  public void setUsername(String username) {
    this.username = username;
  }
}
