/* Copyright © 2014, Oracle and/or its affiliates. All rights reserved. 
 * RMSReader.java
 */
package rmsreader;

import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import server.DataSource;

/**
 * Read records from an RMS
 *
 * @author Simon Ritter @speakjava
 * @author Jim Weaver @JavaFXpert
 */
public class RMSReader implements DataSource {

    private final RecordStore store;
    private final int recordCount;

    /**
     * Constructor
     *
     * @param name The name of the record store to access
     * @throws RecordStoreException If the record store cannot be accessed
     */
    public RMSReader(String name) throws RecordStoreException {
        store = RecordStore.openRecordStore(name, false);
        recordCount = store.getNumRecords();
    }

    /**
     * Get the count of the number of records in the store
     *
     * @return The number of records in the store
     */
    @Override
    public int getRecordCount() {
        return recordCount;
    }

    /**
     * Print all records in the store
     */
    public void printRecords() {
        for (int i = 1; i <= recordCount; i++) {
            try {
                System.out.println("Record " + i + " = "
                        + new String(store.getRecord(i)));
            } catch (RecordStoreException ex) {
                System.out.println("Bad record: " + i);
            }
        }
    }

    /**
     * Get all the records in the store as Strings
     *
     * @return 
     */
    @Override
    public String[] getRecords() {
        String[] result = new String[getRecordCount()];
        for (int i = 1; i <= recordCount; i++) {
            try {
                result[i - 1] = new String(store.getRecord(i));
            } catch (RecordStoreException ex) {
                System.out.println("Bad record: " + i);
            }
        }
        return result;
    }

    /**
     * Concatenate all of the records into a String
     *
     * @return String concatenation all of the records in the store (newline
     * terminated)
     */
    @Override
    public String toString() {
        String retVal = "";
        for (int i = 1; i <= recordCount; i++) {
            try {
                retVal += new String(store.getRecord(i));
                retVal += "\n";
            } catch (RecordStoreException ex) {
                System.out.println("RMSReader#getDelimitedString: Bad record: " + i);
            }
        }
        return retVal;
    }
}
