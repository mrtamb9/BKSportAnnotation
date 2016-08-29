package org.bksport.annotation.mvc.model.data;

/**
 * 
 * Wrapper of most server information, including host, port, username, password
 * 
 * @author congnh
 * 
 */
public class Server {

  private String host;
  private int port;
  private String username;
  private String password;

  /**
   * initialize default server information: {host = localhost, port = 80,
   * username = null, password = null}
   */
  public Server() {
    host = "localhost";
    port = 80;
    username = null;
    password = null;
  }

  /**
   * initialize server information
   * 
   * @param host
   *          host where server is located
   * @param port
   *          port which server is accessed
   * @param strings
   *          possible username and password
   */
  public Server(String host, int port, String... strings) {
    setHost(host);
    setPort(port);
    if (strings.length > 0) {
      setUsername(strings[0]);
    }
    if (strings.length > 1) {
      setPassword(strings[1]);
    }
  }

  public String getHost() {
    return host;
  }

  public int getPort() {
    return port;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String toString() {
    return "{host = " + host + ", port = " + port + ", username = " + username
        + ", password = " + password + "}";
  }

}
