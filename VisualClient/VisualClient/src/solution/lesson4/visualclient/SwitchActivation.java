/* Copyright © 2014, Oracle and/or its affiliates. All rights reserved. 
 * SwitchActivation.java
 */

package solution.lesson4.visualclient;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Represents a switch activation
 * 
 * @author Jim Weaver @JavaFXpert
 */
public class SwitchActivation {
  private LocalDateTime _dateTime;
  private boolean _switchedOn;

  public SwitchActivation(String dateTimeStr, String switchedOnStr) {
    try {
      long dateTimeLong = Long.parseLong(dateTimeStr);
      _dateTime = LocalDateTime.ofEpochSecond(dateTimeLong, 0, ZoneOffset.UTC);
    }
    catch (NumberFormatException nfe) {
      System.out.println("Problem parsing SwitchActivation dateTimeStr: " + nfe);
    }
    
    try {
      _switchedOn = Integer.parseInt(switchedOnStr) == 1;
    }
    catch (NumberFormatException nfe) {
      System.out.println("Problem parsing SwitchActivation switchedOnStr: " + nfe);
    }
  }
  
  public LocalDateTime getDateTime() {
    return _dateTime;
  }
  
  public boolean getSwitchedOn() {
    return _switchedOn;
  }  
  
  public String toString() {
    return "_dateTime: " + _dateTime + ", _switchedOn: " + _switchedOn;
  }
}
