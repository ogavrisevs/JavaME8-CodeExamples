/*
 * TemperatureReading.java
 */

package solution.lesson4.visualclient;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Represents a temperature reading
 * 
 * @author Jim Weaver @JavaFXpert
 */
public class TemperatureReading {
  private LocalDateTime _dateTime;
  private float _temperature;

  public TemperatureReading(String dateTimeStr, String temperatureStr) {
    try {
      long dateTimeLong = Long.parseLong(dateTimeStr);
      _dateTime = LocalDateTime.ofEpochSecond(dateTimeLong, 0, ZoneOffset.UTC);
    }
    catch (NumberFormatException nfe) {
      System.out.println("Problem parsing TemperatureReading dateTimeStr: " + nfe);
    }
    
    try {
      _temperature = Float.parseFloat(temperatureStr);
    }
    catch (NumberFormatException nfe) {
      System.out.println("Problem parsing TemperatureReading temperatureStr: " + nfe);
    }
  }
  
  public LocalDateTime getDateTime() {
    return _dateTime;
  }
  
  public float getTemperature() {
    return _temperature;
  }  
  
  public String toString() {
    return "_dateTime: " + _dateTime + ", _temperature: " + _temperature;
  }
}
