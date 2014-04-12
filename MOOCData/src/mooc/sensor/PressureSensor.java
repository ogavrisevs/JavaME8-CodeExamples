/* Copyright © 2014, Oracle and/or its affiliates. All rights reserved. 
 * Java Embedded MOOC
 *
 * January 2014
 */
package mooc.sensor;

import java.io.IOException;

/**
 * Interface for a pressure sensor
 * 
 * @author simonri
 */
public interface PressureSensor {
  /**
   * Get the current pressure in hector Pascal units
   * 
   * @return The current pressure in hPa
   * @throws java.io.IOException
   */
  public double getPressureInHPa() throws IOException;
  
  /**
   * Get the current pressure in inches mercury units
   * 
   * @return The current pressure in inches mercury
   * @throws IOException 
   */
  public double getPressureInInchesMercury() throws IOException;
}
