/* Copyright © 2014, Oracle and/or its affiliates. All rights reserved. 
 * Java Embedded MOOC
 * 
 * January 2014
 */
package mooc.data.gps;

import java.util.Formatter;

/**
 * The current position of the GPS sensor
 * 
 * @author Simon
 */
public class Position {
  private final long timeStamp;
  private final double latitude;
  private final char latitudeDirection;
  private final double longitude;
  private final char longitudeDirection;
  private final double altitude;
  
  /**
   * Constructor
   * 
   * @param time The current time
   * @param latitude The latitude of this position
   * @param latitudeDirection Direction of longitude
   * @param longitude The longitude of this position
   * @param longitudeDirection Direction of latitude
   * @param altitude The altitude of this positioon
   */
  public Position(long time, double latitude, char latitudeDirection, 
      double longitude, char longitudeDirection, double altitude) {
    this.timeStamp = time;
    this.latitude = latitude;
    this.latitudeDirection = latitudeDirection;
    this.longitude = longitude;
    this.longitudeDirection = longitudeDirection;
    this.altitude = altitude;
  }
  
  /**
   * Get the time this position was recorded
   * 
   * @return The time this position was recorded in seconds from the epoch
   */
  public long getTime() {
    return timeStamp;
  }

  /**
   * Get the current latitude
   * 
   * @return The latitude
   */
  public double getLatitude() {
    return latitude;
  }
  
  /**
   * Get the direction of latitude (N or S)
   * 
   * @return The latitude direction as a single character
   */
  public char getLatitudeDirection() {
    return latitudeDirection;
  }

  /**
   * Get the longitude
   * 
   * @return The longitude
   */
  public double getLongitude() {
    return longitude;
  }
  
  /**
   * Get the longitude direction (E or W)
   * 
   * @return The longitude direction as a single character
   */
  public char getLongitudeDirection() {
    return longitudeDirection;
  }

  /**
   * Get the altitude (in metres above sea level)
   * 
   * @return The altitude in metres above sea level
   */
  public double getAltitude() {
    return altitude;
  }
  
  /**
   * Get the data in the right format for the client
   * 
   * @return A formatted data string
   */
  @Override
  public String toString() {
    Formatter f = new Formatter();
    return f.format("%d,%.3f,%c,%.3f,%c,%.2f", 
        timeStamp,
        latitude,
        latitudeDirection,
        longitude,
        longitudeDirection,
        altitude).toString();
  }
}