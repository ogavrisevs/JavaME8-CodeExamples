/* Copyright © 2014, Oracle and/or its affiliates. All rights reserved. 
 * Embedded Java MOOC
 *
 * January 2014
 */
package mooc.data;

import java.util.Formatter;

/**
 * Data from a switch (boolean state)
 * 
 * @author simonri
 */
public class SwitchData implements SensorData {
  private final long timeStamp;
  private final boolean state;
  
  /**
   * Constructor
   * 
   * @param timestamp When this data was recorded
   * @param state The state of the sitch (closed is true)
   */
  public SwitchData(long timestamp, boolean state) {
    this.timeStamp = timestamp;
    this.state = state;
  }

  /**
   * Get the state of the switch
   * 
   * @return The state (true is closed)
   */
  public boolean getState() {
    return state;
  }
  
  /**
   * Get the state of the switch as a character
   * 
   * @return The state of the switch (0 = open, 1 = closed)
   */
  public char getStateAsChar() {
    return state ? '1' : '0';
  }
  
  /**
   * Get the timestamp for when this data was collected
   * 
   * @return The number of seconds since the epoch when this data was collected
   */
  @Override
  public long getTimeStamp() {
    return timeStamp;
  }
  
  /**
   * Override the toString method to generate a string of the right format for
   * the client
   * 
   * @return A correctly formatted data string
   */
  @Override
  public String toString() {
    Formatter formatter = new Formatter();
    return formatter.format("%d,%c", timeStamp, getStateAsChar()).toString();
  }
}
