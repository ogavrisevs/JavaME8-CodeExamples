/*
 * Java Embedded MOOC
 * 
 * March 2014
 *
 * Copyright © 2014, Oracle and/or its affiliates. All rights reserved.
 */
package rmspersistdata;

import intermidletserver.Service;
import java.io.IOException;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import mooc.data.Messages;

/**
 * Persist data received from IMC client to the RMS
 * 
 * @author simonri
 */
public class RMSPersistService extends Service 
    implements Messages, AutoCloseable {
  private RecordStore store;
  private boolean verbose = false;
  private int messageLevel = ERROR;
  
  /**
   * Constructor
   * 
   * @param serviceName The name of the IMC service
   * @param recordStoreName The name of the record store to use
   * @param verbose Whether debug messages are on or off
   * @param messageLevel How verbose debug messages are
   * @throws IOException If there is an IO error
   */
  public RMSPersistService(String serviceName, String recordStoreName,
      boolean verbose, int messageLevel) throws IOException {
    super(serviceName);
    this.verbose = verbose;
    this.messageLevel = messageLevel;
    
    try {
      printMessage("constructor opening record store", INFO);
      store = RecordStore.openRecordStore(recordStoreName, true);
    } catch (RecordStoreException rse) {
      printMessage("Unable to open record store: " + recordStoreName, ERROR);
      System.out.println(rse.getMessage());
    }
  }
  

  /**
   * Save GPS data: position and velocity
   * 
   * @param data The data to save, as a String
   * @throws IOException If there is an IO error
   */
  @Override
  public void saveData(String data) throws IOException {
    byte[] dataBytes = data.getBytes();
    
    try {
      int recordNumber = store.addRecord(dataBytes, 0, dataBytes.length);
      printMessage("saved record number " + recordNumber, INFO);
    } catch (RecordStoreException ex) {
      throw new IOException(ex.getMessage());
    }
  }

  /**
   * Close the persistent store to ensure data is in a consistent state
   */
  @Override
  public void close() {
    try {
      printMessage("closing record store", INFO);
      store.closeRecordStore();
    } catch (RecordStoreException ex) {
      printMessage("Failed to close the record store", ERROR);
    }
  }
  
  /**
   * Print a message if verbose messaging is turned on
   *
   * @param message The message to print
   */
  private void printMessage(String message, int level) {
    if (verbose && level <= messageLevel)
      System.out.println("RMSPersistentStore: " + message);
  }
}