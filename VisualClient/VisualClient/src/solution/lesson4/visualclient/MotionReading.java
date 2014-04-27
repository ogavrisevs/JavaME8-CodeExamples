/* Copyright © 2014, Oracle and/or its affiliates. All rights reserved. 
 * MotionReading.java
*/

package solution.lesson4.visualclient;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Represents a motion reading
 * 
 * @author Jim Weaver @JavaFXpert
 */
public class MotionReading {
  private LocalDateTime _dateTime;
  private float _speed;
  private float _heading;

  public MotionReading(String dateTimeStr, 
                        String headingStr,
                        String speedStr) {
    try {
      long dateTimeLong = Long.parseLong(dateTimeStr);
      _dateTime = LocalDateTime.ofEpochSecond(dateTimeLong, 0, ZoneOffset.UTC);
    }
    catch (NumberFormatException nfe) {
      System.out.println("Problem parsing Motion dateTimeStr: " + nfe);
    }
    
    try {
      _speed = Float.parseFloat(speedStr);
    }
    catch (NumberFormatException nfe) {
      System.out.println("Problem parsing Motion speedStr: " + nfe);
    }
    
    try {
      _heading = Float.parseFloat(headingStr);
    }
    catch (NumberFormatException nfe) {
      System.out.println("Problem parsing Motion headingStr: " + nfe);
    }
  }
  
  public LocalDateTime getDateTime() {
    return _dateTime;
  }
  
  public float getVelocity() {
    return _speed;
  }  
  
  public float getHeading() {
    return _heading;
  }  
  
  public String toString() {
    return "_dateTime: " + _dateTime + 
           ", _heading: " + _heading +
           ", _speed: " + _speed;
  }
}
