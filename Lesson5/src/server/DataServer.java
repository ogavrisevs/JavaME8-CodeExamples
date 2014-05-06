/* Copyright © 2014, Oracle and/or its affiliates. All rights reserved. 
 * DataServer.java
 */
package server;

import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.ServerSocketConnection;
import javax.microedition.io.StreamConnection;
import mooc.data.Messages;

/**
 * Simple network server to provide data from sensors
 * 
 * @author Simon Ritter @speakjava
 * @author Jim Weaver @JavaFXpert
 */
public class DataServer implements Messages, Runnable {
  private final DataSource dataSource;
  private final ServerSocketConnection server;
  private boolean running = true;
  private boolean verbose = false;
  private int messageLevel = 1;
  
  /**
   * Constructor
   * 
   * @param port The port to listen on for client connections
   * @param dataSources
   * @throws IOException If there is an IO error
   */
  public DataServer(int port, DataSource dataSource) throws IOException {
    this.dataSource = dataSource;
    server = (ServerSocketConnection)Connector.open("socket://:" + port);
  }

  /**
   * Terminate the thread gracefully
   */
  public void stop() {
    running = false;
  }
  
  /**
   * Turn on or off verbose messaging
   *
   * @param verbose Whether to enable verbose messages
   */
  public void setVerbose(boolean verbose) {
    this.verbose = verbose;
  }
  
  /**
   * Set the level of messages to display, 1 = ERROR, 2 = INFO
   * 
   * @param level The level for messages
   */
  public void setMessageLevel(int level) {
    messageLevel = level;
  }
  
  /**
   * Code to execute in a separate thread
   */
  @Override
  public void run() {
    System.out.println("DataServer running...");
    
    while (running) {
      try {
        /* Listen for incoming connection and spawn a new thread for
         * each one so we can keep processing more connections
         */
        StreamConnection clientConnection = server.acceptAndOpen();
        printMessage("Connection received", INFO);
        DataConnection connection = new DataConnection(dataSource, clientConnection);
        connection.setVerbose(verbose);
        connection.setMessageLevel(messageLevel);
        new Thread(connection).start();
      } catch (IOException ex) {
        printMessage("IO Error accepting connection from client", ERROR);
        printMessage(ex.getMessage(), ERROR);
      }
    }
  }
  
  /**
   * Print a message if verbose messaging is turned on
   *
   * @param message The message to print
   */
  private void printMessage(String message, int level) {
    if (verbose && level <= messageLevel)
      System.out.println("DataServer: " + message);
  }
}