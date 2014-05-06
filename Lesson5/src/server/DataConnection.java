/* Copyright © 2014, Oracle and/or its affiliates. All rights reserved. 
 * DataConnection.java
 */
package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import mooc.data.server.DataProtocol;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import javax.microedition.io.StreamConnection;
import mooc.data.Messages;

/**
 * Process requests for data from a client
 *
 * @author Simon Ritter @speakjava
 * @author Jim Weaver @JavaFXpert
 */
public class DataConnection implements DataProtocol, Messages, Runnable {

    private static final byte NEWLINE = 13;

    private final DataSource dataSource;
    private final InputStream input;
    private final BufferedWriter output;
    private boolean running = true;
    private boolean verbose = true;
    private int messageLevel = 1;

    /**
     * Constructor
     *
     * @param dataSource The source of data for the client
     * @param connection The connection to the client
     * @throws IOException If there is an IO error
     */
    public DataConnection(DataSource dataSource, StreamConnection connection)
            throws IOException {
        printMessage("DataConnection: in constructor", INFO);
        this.dataSource = dataSource;
//        InputStream is = connection.openDataInputStream();
//        input = new BufferedReader(new InputStreamReader(is));
        input = connection.openDataInputStream();
        //output = connection.openDataOutputStream();
        OutputStream sos = connection.openDataOutputStream();
        output = new BufferedWriter(new OutputStreamWriter(sos));
    }

    /**
     * Stop the current thread
     */
    public void stopThread() {
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
        byte buffer[] = new byte[128];
        String command;
        
        printMessage("DataConnection: task running", INFO);

        while (running) {
            try {
                /* Read a command from the client */
                int commandLength = input.read(buffer);
                command = new String(buffer, 0, commandLength);
//                command = input.readLine();
                printMessage("command received: " + command, 2);

                /* Process the command */
                if (command.compareTo(READ_DATA) == 0) {
                    /* Send formatted data to the client */
                    printMessage("sending: " + dataSource.toString(), DATA);
                    // Send the number of message strings first
                    output.write(Integer.toString(dataSource.getRecordCount()));
                    output.newLine();
                    // Send all the messages
                    for (String message : dataSource.getRecords()) {
                        output.write(message);
                        output.newLine();
                    }
                    output.flush();
                    break;
                } else if (command.compareTo(DISCONNECT) == 0) {
                    break;
                } else {
                    printMessage("Unrecognized command [" + command + "]", ERROR);
                }
            } catch (IOException ex) {
                printMessage("Client I/O error", ERROR);
                printMessage(ex.getMessage(), ERROR);
                break;
            }
        }
    }

    /**
     * Print a message if verbose messaging is turned on
     *
     * @param message The message to print
     */
    private void printMessage(String message, int level) {
        if (verbose && level <= messageLevel) {
            System.out.println("DataConnection: " + message);
        }
    }
}
