/* Copyright © 2014, Oracle and/or its affiliates. All rights reserved. 
 * Java Embedded MOOC
 *
 * January 2014
 */
package mooc.data;

/**
 * Interface to define the basic format of data obtiained from the embedded
 * device
 * 
 * @author simonri
 */
public interface SensorData {
  /**
   * All device data has a timestamp associated with when the data was 
   * collected.
   * 
   * @return The time in seconds since the epoch when this data was collected
   */
  public long getTimeStamp();
}