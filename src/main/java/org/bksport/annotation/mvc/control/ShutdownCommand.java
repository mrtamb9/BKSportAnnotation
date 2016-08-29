package org.bksport.annotation.mvc.control;

import org.puremvc.java.interfaces.INotification;
import org.puremvc.java.patterns.command.SimpleCommand;

/**
 * 
 * handling tasks when application shutdown
 * 
 * @author congnh
 * 
 */
public class ShutdownCommand extends SimpleCommand {

  @Override
  public void execute(INotification notification) {
    // doing smth before shutdown
    System.exit(0);
  }

}
