/* Copyright © 2014, Oracle and/or its affiliates. All rights reserved. 
 * PositionReading.java
 */

package solution.lesson4.visualclient;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * Represents a position reading
 * 
 * @author Jim Weaver @JavaFXpert
 */
public class PositionReading {
  private static final String SOUTH = "S";
  private static final String WEST = "W";
  private LocalDateTime _dateTime;
  private float _latitude;
  private float _longitude;
  private float _altitude;

  public PositionReading(String dateTimeStr, 
                        String latitudeStr,
                        String latitudeDirection,
                        String longitudeStr,
                        String longitudeDirection,
                        String altitudeStr) {
    
    try {
      long dateTimeLong = Long.parseLong(dateTimeStr);
      _dateTime = LocalDateTime.ofEpochSecond(dateTimeLong, 0, ZoneOffset.UTC);
    }
    catch (NumberFormatException nfe) {
      System.out.println("Problem parsing Position dateTimeStr: " + nfe);
    }
    
    try {
      _latitude = Float.parseFloat(latitudeStr);
      if (latitudeDirection.equalsIgnoreCase(SOUTH)) _latitude *= -1;
    }
    catch (NumberFormatException nfe) {
      System.out.println("Problem parsing Position latitudeStr: " + nfe);
    }
    
    try {
      _longitude = Float.parseFloat(longitudeStr);
      if (longitudeDirection.equalsIgnoreCase(WEST)) _longitude *= -1;
    }
    catch (NumberFormatException nfe) {
      System.out.println("Problem parsing Position longitudeStr: " + nfe);
    }

    try {
      _altitude = Float.parseFloat(altitudeStr);
    }
    catch (NumberFormatException nfe) {
      System.out.println("Problem parsing Position altitudeStr: " + nfe);
    }
  }
  
  public LocalDateTime getDateTime() {
    return _dateTime;
  }
  
  public String getDateTimeFormatted() {
    return _dateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM));
  }
  
  public float getLatitude() {
    return _latitude;
  }  
  
  public float getLongitude() {
    return _longitude;
  }  
  
  public float getAltitude() {
    return _altitude;
  }  
  
  public String toString() {
    return "_dateTime: " + _dateTime + 
           ", _latitude: " + _latitude +
           ", _longitude: " + _longitude +
           ", _altitude: " + _altitude;
  }
}
