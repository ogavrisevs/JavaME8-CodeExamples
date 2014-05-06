/*
 * Java Embedded MOOC
 * 
 * March 2014
 *
 * Copyright © 2014, Oracle and/or its affiliates. All rights reserved.
 */
package rmspersistdata;

import java.io.IOException;
import javax.microedition.midlet.MIDlet;
import mooc.data.Messages;

/**
 * MIDlet providing a service to persist data using the RMS via IMC
 * 
 * @author simonri
 */
public class RMSPersistMIDlet extends MIDlet {
  /**
   * MIDlet lifecycle start method
   */
  @Override
  public void startApp() {
    try {
      RMSPersistService service = 
          new RMSPersistService("mooc.rmspersist:1.0;", "gps-data", 
              true, Messages.INFO);
      service.startService();
    } catch (IOException ioe) {
      System.out.println("ERROR Unable to start RMS persist service");
      System.out.println(ioe.getMessage());
    }
  }
  
  /**
   * MIDlet lifecycle pause method
   */
  @Override
  public void pauseApp() {
  }
  
  /**
   * MIDlet lifecycle termination method
   * 
   * @param unconditional If the imlet should be terminated whatever
   */
  @Override
  public void destroyApp(boolean unconditional) {
  }
}