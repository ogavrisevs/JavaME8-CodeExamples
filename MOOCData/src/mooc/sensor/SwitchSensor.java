/* Copyright © 2014, Oracle and/or its affiliates. All rights reserved. 
 * Java Embedded MOOC
 *
 * January 2014
 */
package mooc.sensor;

import java.io.IOException;
import mooc.data.SwitchData;

/**
 * Interface for a switch sensor
 * 
 * @author simonri
 */
public interface SwitchSensor {
  /**
   * Get the state of the switch (open or closed)
   * 
   * @return Current state of the switch (true - closed, false - open)
   * @throws java.io.IOException If there is an IO error reading the pin state
   */
  public boolean getState() throws IOException;
  
  /**
   * Get the switch data (timestamp and state)
   * 
   * @return The current switch data
   */
  public SwitchData getSwitchData(); 
}