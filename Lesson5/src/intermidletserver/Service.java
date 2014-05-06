/*
 * Java Embedded MOOC
 * 
 * March 2014
 *
 * Copyright © 2014, Oracle and/or its affiliates. All rights reserved.
 */
package intermidletserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.IMCConnection;
import javax.microedition.io.IMCServerConnection;
import mooc.data.Messages;

/**
 * Generic service to be used by other MIDlets via the Inter-Midlet
 * Communications (IMC) facility
 *
 * @author simonri
 */
public abstract class Service implements Messages {
  private final static String RESPONSE_OK = "OK";
  
  private final IMCServerConnection imcServerConnection;
  private boolean running = true;
  protected DataInputStream input;
  protected DataOutputStream output;
  private final boolean verbose = true;
  private final int messageLevel = INFO;

  /**
   * Constructor
   *
   * @param name The name to use for the service
   * @throws IOException If there is an IO error
   */
  public Service(String name) throws IOException {
    String imcURL = "imc://:" + name;
    printMessage("Starting IMC server: " + imcURL, INFO);
    imcServerConnection
        = (IMCServerConnection) Connector.open(imcURL);
  }

  /**
   * Start the service. Listen for an incoming connection and then process it.
   *
   * @throws IOException If there is an IO error
   */
  public void startService() throws IOException {
    byte[] message = new byte[1024];
    
    while (running) {
      IMCConnection imc
          = (IMCConnection) imcServerConnection.acceptAndOpen();
      printMessage("got IMC connection", INFO);
      input = imc.openDataInputStream();
      output = imc.openDataOutputStream();
      printMessage("got IMC streams", INFO);
      
      while (input.read(message) != 0) {
        printMessage("got message via IMC, saving", INFO);
        saveData(new String(message));
        output.write(RESPONSE_OK.getBytes());
      }
    }
  }

  /**
   * Stop the service from handling new connections
   */
  public void stopService() {
    running = false;
  }
  
  /**
   * Print a message if verbose messaging is turned on
   *
   * @param message The message to print
   */
  private void printMessage(String message, int level) {
    if (verbose && level <= messageLevel)
      System.out.println("Service: " + message);
  }

  /**
   * Save data in a way specific to the concrete sub-class.
   *
   * @param data The data to save
   * @throws IOException If there is an IO error
   */
  public abstract void saveData(String data) throws IOException;
}