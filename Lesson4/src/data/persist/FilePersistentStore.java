/* Copyright © 2014, Oracle and/or its affiliates. All rights reserved.
 * Java Embedded MOOC
 * 
 * January 2014
 */
package data.persist;

import mooc.data.gps.Position;
import mooc.data.gps.Velocity;
import java.io.IOException;
import java.io.PrintStream;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import mooc.data.Messages;

/**
 * Persistent store using a file
 *
 * @author Simmon Ritter
 */
public class FilePersistentStore implements Messages, PersistentStore {

    private FileConnection connection;
    private PrintStream fileWriter;
    private boolean verbose = false;
    private int messageLevel = ERROR;
    private int recordCount;

    /**
     * Constructor
     *
     * @param fileName The name of the file to use for saving data
     * @throws java.io.IOException
     */
    public FilePersistentStore(String fileName)
            throws IOException {

        try {
            /* Open the file for writing */
            String connectorName = "file://" + fileName;
            printMessage("constructor opening file: [" + connectorName + "]", INFO);
            connection = (FileConnection) Connector.open(connectorName,
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
        printMessage("saveData called", INFO);
        fileWriter.println(data);
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
        if (verbose && level <= messageLevel) {
            System.out.println("FilePersistentStore: " + message);
        }
    }
}
