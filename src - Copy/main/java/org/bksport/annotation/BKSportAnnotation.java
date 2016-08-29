package org.bksport.annotation;

import org.bksport.annotation.mvc.AppFacade;

/**
 * 
 * @author congnh
 * 
 */
public class BKSportAnnotation {

  public static void main(String args[]) {
    AppFacade facade = AppFacade.getInstance();
    facade.startup(); 
  }
}
