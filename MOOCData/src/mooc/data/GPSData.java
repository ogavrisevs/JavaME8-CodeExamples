/* Copyright © 2014, Oracle and/or its affiliates. All rights reserved. 
 * Java Embedded MOOC
 *
 * January 2014
 */
package mooc.data;

import mooc.data.gps.Position;
import mooc.data.gps.Velocity;

/**
 * Data from a GPS sensor
 * 
 * @author simonri
 */
public class GPSData implements SensorData {
  private final long timeStamp;
  private final Position position;
  private final Velocity velocity;
  
  /**
   * Constructor
   * 
   * @param timeStamp Timestamp of when this 
   * @param position The position of the GPS sensor
   * @param velocity The velocity of the GPS sensor
   */
  public GPSData(long timeStamp, Position position, Velocity velocity) {
    this.timeStamp = timeStamp;
    this.position = position;
    this.velocity = velocity;
  }

  /**
   * Get the position of the sensor as a latitude and logitude
   * 
   * @return The Position cbject with the data
   */
  public Position getPosition() {
    return position;
  }
  
  /**
   * Get the velocity of the sensor (as bearing and speed)
   * 
   * @return The Velocity object with the data
   */
  public Velocity getVelocity() {
    return velocity;
  }
  
  /**
   * Get the time stamp for when this data was collected
   * 
   * @return The number of secods since the epoch when this data was collected
   */
  @Override
  public long getTimeStamp() {
    return timeStamp;
  }  
}