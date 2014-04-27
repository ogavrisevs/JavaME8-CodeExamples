/* Copyright © 2014, Oracle and/or its affiliates. All rights reserved. */
package doorproject;

import com.oracle.deviceaccess.PeripheralConfigInvalidException;
import com.oracle.deviceaccess.PeripheralExistsException;
import com.oracle.deviceaccess.PeripheralManager;
import com.oracle.deviceaccess.PeripheralNotFoundException;
import com.oracle.deviceaccess.PeripheralTypeNotSupportedException;
import com.oracle.deviceaccess.gpio.GPIOPin;
import java.io.IOException;
import mooc.data.Messages;

/**
 *
 * @author Angela
 */
public class GPIOLED implements Messages {

    //Pin where the LED is connected. Be default we are using Pin 23.
    private int ledID = 23;

    private GPIOPin greenLED;   //Green LED
    public boolean stopBlink = false;
    public boolean iAmBlinking = false;
    private boolean verbose = true;
    private int messageLevel = ERROR;

    /**
     * GPIOLED Constructor
     *
     * @param pinNumber Pin where the LED is connected
     */
    public GPIOLED(int pinNumber) {
        ledID = pinNumber;
    }

    /**
     * This method start the connection to the LED, and turns it on.
     *
     * @throws IOException, PeripheralNotFoundException,
     * PeripheralTypeNotSupportedException, PeripheralConfigInvalidException,
     * PeripheralExistsException
     */
    public void start() throws IOException,
            PeripheralNotFoundException, PeripheralTypeNotSupportedException,
            PeripheralConfigInvalidException, PeripheralExistsException {

        // Open the LED pin (Output)
        greenLED = (GPIOPin) PeripheralManager.open(ledID);

        //Set LED initial value
        greenLED.setValue(false);

        //Small pause is introduce
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
        }

    }

    /**
     * This method set the value for the LED
     *
     * @param value new value for te LED
     * @throws IOException
     */
    public void setValue(boolean value) throws IOException {
        greenLED.setValue(value);
    }

    /**
     * This method set the value for the LED
     *
     * @return It returns the value of the LED
     * @throws IOException
     */
    public boolean getValue() throws IOException {
        return greenLED.getValue();
    }

    /**
     * This method allows to stop the LED blinking process
     *
     * @param status Status for the stopBlinking variable. If true, this will
     * cause an active blinking to stop.
     */
    public void setStopBlink(boolean status) {
        stopBlink = status;
    }

    /**
     * This method will blink the LED. We used a separate thread so the program
     * won't get paused for other actions.
     *
     * @param times Number of LED's blinks.
     */
    public void blink(final int times) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!iAmBlinking) {  //If I'm already blinking dont blink again!
                    iAmBlinking = true;  //LED blinking, don't start another blinking process again
                    for (int i = 0; (i < (times * 2)) && !stopBlink; i++) {
                        try {
                            setValue(!getValue());  //Blink
                            Thread.sleep(1000);
                        } catch (IOException | InterruptedException e) {
                            printMessage(e.getMessage(), ERROR);
                        }
                    }
                    iAmBlinking = false;  //blinking done, it's free for a new blink
                }
            }
        }).start();
    }

    /**
     * Method to turn off the LED before ending the application nd closing the
     * connection with the LED.
     *
     * @throws IOException
     */
    public void stop() throws IOException {
        if (greenLED != null) {
            greenLED.setValue(false);
            greenLED.close();
        }
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
     * Print a message if verbose messaging is turned on
     *
     * @param message The message to print
     * @param level
     */
    protected void printMessage(String message, int level) {
        if (verbose && messageLevel >= level) {
            System.out.println("GPIOLED: " + message);
        }
    }
}
