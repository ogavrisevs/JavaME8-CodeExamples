/*
 * Java Embedded MOOC
 * 
 * March 2014
 *
 * Copyright © 2014, Oracle and/or its affiliates. All rights reserved.
 */
package gpsdata;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.IMCConnection;
import mooc.data.Messages;
import mooc.data.gps.Position;
import mooc.data.gps.Velocity;

/**
 * Client connection to a service provided by another MIDlet. Interaction
 * handled by Inter-MIDlet Communication
 *
 * @author simonri
 */
public class ServiceClient implements Messages {
  private final DataInputStream input;
  private final DataOutputStream output;
  private final boolean verbose = true;
  private final int messageLevel = INFO;

  /**
   * Constructor
   * 
   * @param serviceName The name of the service to connect to
   * @throws IOException If there is an IO error
   */
  public ServiceClient(String serviceName) throws IOException {
    String imcURL = "imc://*:" + serviceName;
    printMessage("constructor: " + imcURL, INFO);
    IMCConnection imcConnection
        = (IMCConnection) Connector.open(imcURL);
    output = imcConnection.openDataOutputStream();
    input = imcConnection.openDataInputStream();
    printMessage("connection open", INFO);
  }
  
  /**
   * Persist data using the remote service
   * 
   * @param position The position data
   * @param velocity The velocity data
   * @throws java.io.IOException If there is an IO error
   */
  public void persistData(Position position, Velocity velocity) 
      throws IOException {
    printMessage("persistData", INFO);
    String message = position.toString() + "^" + velocity.toString();
    output.write(message.getBytes());
    byte[] response = new byte[16];
    input.read(response);
    String responseString = new String(response);
    
    /* Check that the response is correct */
    if (!responseString.startsWith("OK"))
      throw new IOException("Incorrect response code received");
  }
  
  /**
   * Print a message if verbose messaging is turned on
   *
   * @param message The message to print
   */
  private void printMessage(String message, int level) {
    if (verbose && level <= messageLevel)
      System.out.println("ServiceClient: " + message);
  }
}