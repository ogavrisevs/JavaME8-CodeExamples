/* Copyright © 2014, Oracle and/or its affiliates. All rights reserved. 
 * ServerMidlet.java
 */
package server;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.microedition.io.PushRegistry;
import javax.microedition.midlet.MIDlet;
import javax.microedition.rms.RecordStoreException;
import rmsreader.RMSReader;

/**
 * MIDlet for the MOOC data server example
 * 
 * @author Simon Ritter @speakjava
 * @author Jim Weaver @JavaFXpert
 */
public class ServerMidlet extends MIDlet {
  /**
   * MIDlet lifecycle start method
   */
  @Override
  public void startApp() {
      try {
          PushRegistry.registerConnection(null, null, null);
      } catch (IOException ex) {
          Logger.getLogger(ServerMidlet.class.getName()).log(Level.SEVERE, null, ex);
      } catch (ClassNotFoundException ex) {
          Logger.getLogger(ServerMidlet.class.getName()).log(Level.SEVERE, null, ex);
      }
    try {
      RMSReader dataSource = new RMSReader("event-data");
      DataServer server = new DataServer(8080, dataSource);
      server.setVerbose(true);
      server.setMessageLevel(3);      
      new Thread(server).start();
    } 
    catch (RecordStoreException rse) {
      System.out.println("Unable to access RMS");
      System.out.println(rse.getMessage());
    }
    catch (IOException ioe) {
      System.out.println("IO Error");
      System.out.println(ioe.getMessage());
    }
  }
  
  /**
   * MIDlet lifecycle pause method
   */
  @Override
  public void pauseApp() {}
  
  /**
   * MIDlet lifecycle termination method
   * 
   * @param unconditional How to terminate the MIDlet
   */
  @Override
  public void destroyApp(boolean unconditional) {}
}
