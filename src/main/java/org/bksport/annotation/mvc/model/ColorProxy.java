package org.bksport.annotation.mvc.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import org.puremvc.java.patterns.proxy.Proxy;

/**
 * 
 * @author congnh
 * 
 */
public class ColorProxy extends Proxy {

  private static ArrayList<Color> registedColors = new ArrayList<Color>();
  private static ArrayList<String> registedAnnotation = new ArrayList<String>();
  private static Random random = new Random();

  public ColorProxy(String name) {
    super(name);
  }

  public static Color getColor(String annotation) {
    int index = registedAnnotation.indexOf(annotation);
    if (index >= 0) {
      return registedColors.get(index);
    } else {
      int i = random.nextInt(7);
      Color color = getColor(i);
      registedAnnotation.add(annotation);
      registedColors.add(color);
      return color;
    }
  }

  private static Color getColor(int i) {
    switch (i) {
    case 0:
      return new Color(17, 255, 0);
    case 1:
      return Color.CYAN;
    case 2:
      return Color.GREEN;
    case 3:
      return Color.MAGENTA;
    case 4:
      return Color.ORANGE;
    case 5:
      return Color.PINK;
    case 6:
      return Color.RED;
    case 7:
      return Color.YELLOW;
    default:
      return Color.WHITE;
    }
  }
}
