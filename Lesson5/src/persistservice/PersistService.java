/* Copyright © 2014, Oracle and/or its affiliates. All rights reserved.*/
package persistservice;

import java.io.IOException;
import mooc.data.Messages;

/**
 * PersistService can be implemented using an RMS or File persistence mechanism.
 * 
 * @author Tom McGinn
 */
public abstract class PersistService implements Messages, AutoCloseable {

    private boolean verbose = true;
    private int messageLevel = ERROR;

    public abstract void saveData(String data) throws IOException;

    public abstract int getRecordCount() throws IOException;

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
     * Print a message if verbose messaging is turned on
     *
     * @param message The message to print
     * @param level
     */
    protected void printMessage(String message, int level) {
        if (verbose && level <= messageLevel) {
            System.out.println("PersistService: " + message);
        }
    }
}
