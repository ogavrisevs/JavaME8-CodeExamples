/* Copyright © 2014, Oracle and/or its affiliates. All rights reserved. 
 * Java Embedded MOOC
 *
 * January 2014
 */
package mooc.sensor;

import mooc.data.gps.Velocity;
import mooc.data.gps.Position;
import java.io.IOException;

/**
 * Interface for a GPS sensor
 * 
 * @author simonri
 */
public interface GPSSensor {
  /**
   * Get the raw data for a specific type of NMEA message
   * 
   * @param type The type of message to get the data for
   * @return The raw string data of this type
   * @throws IOException If there is an IO error
   */
  public String getRawData(String type) throws IOException;
  
  /**
   * Get the current position
   * 
   * @return A Position object with the current values
   * @throws java.io.IOException If there is an IO error
   */
  public Position getPosition() throws IOException;
  
  /**
   * Get the current velocity (bearing and speed)
   * 
   * @return A velocity object with the current values
   * @throws java.io.IOException If there is an IO error
   */
  public Velocity getVelocity() throws IOException;
}