/* Copyright © 2014, Oracle and/or its affiliates. All rights reserved. 
 * Java Embedded MOOC
 * 
 * January 2014
 */
package data.persist;

import java.io.IOException;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import mooc.data.Messages;

/**
 * Persistent store using the Java ME Record Management System (RMS)
 *
 * @author Simon Ritter
 */
public class RMSPerisistentStore implements Messages, PersistentStore {

    private RecordStore store;
    private int recordCount;
    private boolean verbose = false;
    private int messageLevel = ERROR;

    /**
     * Constructor
     *
     * @param name Name to use to reference the RMS
     */
    public RMSPerisistentStore(String name) {
        try {
            store = RecordStore.openRecordStore(name, true);
        } catch (RecordStoreException rse) {
            printMessage("RMSPersistentStore: Unable to open record store: " + name, ERROR);
            System.out.println(rse.getMessage());
        }
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public void setMessageLevel(int messageLevel) {
        this.messageLevel = messageLevel;
    }

    /**
     * Save GPS data: position and velocity
     *
     * @return The number of the record saved
     * @throws IOException If there is an IO error
     */
    @Override
    public int saveData(String data) throws IOException {
        byte[] dataBytes = data.getBytes();

        try {
            int recordNumber = store.addRecord(dataBytes, 0, dataBytes.length);
            printMessage("RMSPersistentStore: saved record number " + recordNumber, INFO);
        } catch (RecordStoreException ex) {
            throw new IOException(ex.getMessage());
        }

        return ++recordCount;
    }

    /**
     * Get the record count
     *
     * @return The number of records in the persistent store
     */
    @Override
    public int getRecordCount() {
        return recordCount;
    }

    /**
     * Close the persistent store to ensure data is in a consistent state
     */
    @Override
    public void close() {
        try {
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
        if (verbose && level <= messageLevel) {
            printMessage("FilePersistentStore: " + message, ERROR);
        }
    }
}
