/* Copyright © 2014, Oracle and/or its affiliates. All rights reserved. 
 * Java Embedded MOOC
 * 
 * January 2014
 */
package mooc.data.gps;

import java.util.Formatter;

/**
 * Current velocity of the GPS sensor
 * 
 * @author Simon
 */
public class Velocity {
  private final long timeStamp;
  private final double trueTrack;
  private final double groundSpeed;
  
  /**
   * Constructor
   * 
   * @param timeStamp Time when this data was recorded
   * @param trueTrack The true track bearing
   * @param groundSpeed The speed over the ground in km/h
   */
  public Velocity(long timeStamp, double trueTrack, double groundSpeed) {
    this.timeStamp = timeStamp;
    this.trueTrack = trueTrack;
    this.groundSpeed = groundSpeed;
  }

  /**
   * Get the track for the GPS sensor (i.e. a bearing)
   * 
   * @return the trueTrack
   */
  public double getTrueTrack() {
    return trueTrack;
  }

  /**
   * Get the velocity of the GPS sensor (in Km/h)
   * 
   * @return the speed over the ground
   */
  public double getGroundSpeed() {
    return groundSpeed;
  }
  
  /**
   * Get the time stamp for when this data was recorded
   * 
   * @return The number of seconds from the epoch when this data was collected
   */
  public long getTimeStamp() {
    return timeStamp;
  }
  
  /**
   * Get the data in the right format for the client
   * 
   * @return A fomatted data string
   */
  @Override
  public String toString() {
    Formatter f = new Formatter();
    return f.format("%d,%.2f,%.2f", 
        timeStamp, 
        trueTrack, 
        groundSpeed).toString();
  }
}