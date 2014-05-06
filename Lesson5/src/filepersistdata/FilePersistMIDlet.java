/*
 * Java Embedded MOOC
 * 
 * March 2014
 *
 * Copyright © 2014, Oracle and/or its affiliates. All rights reserved.
 */
package filepersistdata;

import java.io.IOException;
import javax.microedition.midlet.MIDlet;
import mooc.data.Messages;

/**
 * MIDlet providing a service to persist data using a file via IMC
 * 
 * @author simonri
 */
public class FilePersistMIDlet extends MIDlet {
  /**
   * MIDlet lifecycle start method
   */
  @Override
  public void startApp() {
    System.out.println("FilePersistMIDlet: starting");
    
    try {
      FilePersistService service = 
          new FilePersistService("mooc.filepersist:1.0;", 
              "/rootfs/tmp/gps-data.txt", true, Messages.INFO);
      service.startService();
    } catch (IOException ioe) {
      System.out.println("ERROR starting file persist service");
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