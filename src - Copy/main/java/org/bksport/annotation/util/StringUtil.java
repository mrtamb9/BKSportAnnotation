package org.bksport.annotation.util;

import java.util.Arrays;

/**
 * 
 * @author congnh
 * 
 */
public class StringUtil {

  /**
   * Return all positions of pattern in string. Algorithm: 0.p = 0; 1.Find first
   * position of pattern in string from position p of string. 2.If founded, ex.
   * i = 7, p = i+pattern.length(), add i to list then go to step 1. If not, go
   * to 3. 3. return list.
   * 
   */
  public static int[] indexesOf(String string, String pattern) {
    int[] indexes = new int[string.length()];
    int p;
    int index = -pattern.length();
    int count = -1;
    do {
      p = index + pattern.length();// p = 0 in first loop
      index = string.indexOf(pattern, p);
      if (index >= 0) {
        count++;
        indexes[count] = index;
      }
    } while (index >= 0);
    int[] rt = Arrays.copyOf(indexes, count + 1);
    indexes = null;
    return rt;
  }

  /**
   * Return all positions of pattern in string, ignore case of character.
   */
  public static int[] indexesOfIgnoreCase(String string, String pattern) {
    return indexesOf(string.toLowerCase(), pattern.toLowerCase());
  }

}
