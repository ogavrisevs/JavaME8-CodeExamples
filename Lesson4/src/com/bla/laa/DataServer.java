/* Copyright © 2014, Oracle and/or its affiliates. All rights reserved. 
 * DataServer.java
 */
package com.bla.laa;

import server.*;
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
  private final ServerSocketConnection server;
  private boolean running = true;
  private boolean verbose = false;
  private int messageLevel = 1;
  
  public DataServer(int port) throws IOException {
    server = (ServerSocketConnection)Connector.open("socket://:" + port);
  }

  public void stop() {
    running = false;
  }
  
  public void setVerbose(boolean verbose) {
    this.verbose = verbose;
  }
  
  public void setMessageLevel(int level) {
    messageLevel = level;
  }
  

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
        DataConnection connection = new DataConnection(clientConnection);
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