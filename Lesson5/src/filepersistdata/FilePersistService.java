/*
 * Java Embedded MOOC
 * 
 * March 2014
 *
 * Copyright © 2014, Oracle and/or its affiliates. All rights reserved.
 */
package filepersistdata;

import intermidletserver.Service;
import java.io.IOException;
import java.io.PrintStream;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import mooc.data.Messages;
import static mooc.data.Messages.ERROR;
import static mooc.data.Messages.INFO;

/**
 * Persist data received from IMC client to a file
 * 
 * @author simonri
 */
public class FilePersistService extends Service implements Messages, AutoCloseable {
  private FileConnection connection;
  private PrintStream fileWriter;
  private boolean verbose = false;
  private int messageLevel = ERROR;
  
  /**
   * Constructor
   * 
   * @param serviceName The name of the IMC service
   * @param fileName The name of the file to store data in
   * @param verbose Whether debug messages are on or off
   * @param messageLevel How verbose debug messages are
   * @throws IOException If there is an IO error
   */
  public FilePersistService(String serviceName, String fileName, 
      boolean verbose, int messageLevel) throws IOException {
    super(serviceName);
    this.verbose = verbose;
    this.messageLevel = messageLevel;
    
    try {
      /* Open the file for writing */
      String connectorName = "file://" + fileName;
      printMessage("constructor opening file: [" + connectorName + "]", INFO);
      connection = (FileConnection)Connector.open(connectorName,
          Connector.READ_WRITE);
      
      /* If the file does not exist yet, create it */
      if (!connection.exists()) {
        printMessage("File does not exist, creating", INFO);
        connection.create();
      }
      
      fileWriter = new PrintStream(connection.openOutputStream(), true);
      printMessage("Got IO streams", INFO);
    } catch (IOException ioe) {
      printMessage("IOException: " + ioe.getMessage(), ERROR);
      ioe.printStackTrace();
      throw ioe;
    }
  }
  
  /**
   * Save data in the file
   * 
   * @param data The data to save, as a string
   */
  @Override
  public void saveData(String data) {
    fileWriter.println(data);
  }

  /**
   * Close the file
   * 
   * @throws IOException If there is an error
   */
  @Override
  public void close() throws IOException {
    /* Close the file */
    connection.close();
  }
  
  /**
   * Print a message if verbose messaging is turned on
   *
   * @param message The message to print
   */
  private void printMessage(String message, int level) {
    if (verbose && level <= messageLevel)
      System.out.println("FilePersistentStore: " + message);
  }
}