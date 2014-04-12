/* Copyright © 2014, Oracle and/or its affiliates. All rights reserved. 
 * Java Embedded MOOC
 *
 * January 2014
 */
package mooc.data;

import java.util.Formatter;

/**
 * Data from a temperature sensor
 * 
 * @author simonri
 */
public class TemperatureData implements SensorData {
  private final long _timeStamp;
  private final double _temperatureInCelsius;
  private final double _temperatureInFahrenheit;
  
  /**
   * Constructor
   * 
   * @param timeStamp When this data was recorded
   * @param temperatureInCelsius The recorded temperature (degrees Celsius)
   */
  public TemperatureData(long timeStamp, double temperatureInCelsius) {
    _timeStamp = timeStamp;
    _temperatureInCelsius = temperatureInCelsius;
    _temperatureInFahrenheit = (temperatureInCelsius * 9.0 / 5.0) + 32.0;
  }
  
  /**
   * Get the temperature in degrees Celsius
   * 
   * @return The temperature in degrees Celsius
   */
  public double getTemperatureInCelsius() {
    return _temperatureInCelsius;
  }
  
  /**
   * Get the temperature in degrees Farenheit
   * 
   * @return The temperature in degrees Farenheit 
   */
  public double getTemperatureinFahrenheit() {
    return _temperatureInFahrenheit;
  }
  
  /**
   * Get the timestamp for when this data was collected
   * 
   * @return The number of seconds from the epoch when this data was collected
   */
  @Override
  public long getTimeStamp() {
    return _timeStamp;
  }
  
  /**
   * Override the toString methd to return a data string in the right format
   * for the client
   * 
   * @return A formatted data string
   */
  @Override
  public String toString() {
    Formatter f = new Formatter();
    return f.format("%d,%f", _timeStamp, _temperatureInCelsius).toString();
  }
}