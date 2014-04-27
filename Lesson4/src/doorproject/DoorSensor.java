/* Copyright © 2014, Oracle and/or its affiliates. All rights reserved. */
package doorproject;

import com.oracle.deviceaccess.PeripheralConfig;
import com.oracle.deviceaccess.PeripheralConfigInvalidException;
import com.oracle.deviceaccess.PeripheralExistsException;
import com.oracle.deviceaccess.PeripheralManager;
import com.oracle.deviceaccess.PeripheralNotFoundException;
import com.oracle.deviceaccess.PeripheralTypeNotSupportedException;
import com.oracle.deviceaccess.gpio.GPIOPin;
import com.oracle.deviceaccess.gpio.GPIOPinConfig;
import com.oracle.deviceaccess.gpio.PinEvent;
import com.oracle.deviceaccess.gpio.PinListener;
import java.io.IOException;
import java.util.Date;
import mooc.data.Messages;
import mooc.data.SwitchData;
import mooc.sensor.SwitchSensor;

/**
 *
 * @author Angela
 */
/**
 * This class contains two LEDs (GPIOLED objects) and one switch. the idea is
 * that when the doors is open, we set a red LED on (or we make it blink), to
 * make sure we alert about the door). When the door is close the greenLED is
 * on, to show everything is fine
 */
public class DoorSensor implements PinListener, SwitchSensor, Messages, AutoCloseable {

    //Switch GPIO Port.  Default value 0
    private int switchPortID = 0;
    //Switch GPIO Pin. Default value 17
    private int switchPinID = 17;

    //
    private boolean filteringPreviousState = true;

    //LED definition
    private final GPIOLED redLED;   // Blinks when the door opens
    private final GPIOLED greenLED;  //On when the door is close

    //Door switch
    private GPIOPin switchPin;
    private boolean verbose = true;
    private int messageLevel = ERROR;

    /**
     * Constructor for DoorSensor
     *
     * @param portID port to be use
     * @param pinID pin where the switch is connected
     * @param redLEDPin pin where the red LED is connected
     * @param greenLEDPin pint where the green LED is connected
     */
    public DoorSensor(int portID, int pinID, int redLEDPin, int greenLEDPin) {
        switchPortID = portID;
        switchPinID = pinID;
        redLED = new GPIOLED(redLEDPin);
        greenLED = new GPIOLED(greenLEDPin);

    }

    /**
     * Method to create the Door sensor: A group of 2 LEDs and one switch
     *
     * @throws IOException
     * @throws PeripheralNotFoundException
     * @throws PeripheralTypeNotSupportedException
     * @throws PeripheralConfigInvalidException
     * @throws PeripheralExistsException
     */
    public void start() throws IOException,
            PeripheralNotFoundException, PeripheralTypeNotSupportedException,
            PeripheralConfigInvalidException, PeripheralExistsException {

        // Config information for the switch
        GPIOPinConfig config1 = new GPIOPinConfig(switchPortID, switchPinID, GPIOPinConfig.DIR_INPUT_ONLY,
                PeripheralConfig.DEFAULT, GPIOPinConfig.TRIGGER_BOTH_EDGES, false);

        //Open pin using the config1 information
        switchPin = PeripheralManager.open(config1);

        //Create the connection to the LEDs
        redLED.start();
        greenLED.start();
        //Door is open by default, as the default value of the switch is false
        redLED.setValue(true);

        // Add this class as a pin listener to the buttons
        switchPin.setInputListener(this);

    }

    /**
     * Method to stop connection to the pin
     *
     * @throws java.io.IOException
     */
    @Override
    public void close() throws IOException {
        //close connections on the LEDs
        redLED.stop();
        greenLED.stop();
        if (switchPin != null) {
            switchPin.close();
        }
    }

    /**
     * Method to be invoked when the value of the pin changed. In our case we
     * are listening to Switch changes
     *
     * @param event
     */
    @Override
    public void valueChanged(PinEvent event) {
        GPIOPin pin = (GPIOPin) event.getPeripheral();

        try {
            //Verify the event come from the switch
            if (pin == switchPin) {
                if (pin.getValue() == true) {
                    if (filteringPreviousState != true) { //filtering multiple consecutive trues
                        greenLED.setValue(true);
                        redLED.setValue(false);
                        filteringPreviousState = true;
                    }
                } else {
                    if (filteringPreviousState != false) { //filtering multiple consecutive false
                        greenLED.setValue(false);
                        redLED.setValue(true);
                        //Turn your LED red or make it blink
                        redLED.blink(3);
                        filteringPreviousState = false;
                    }
                }

            }
        } catch (IOException ex) {
            printMessage("IOException: " + ex, ERROR);
        }
    }

    /**
     * Gets doors state, if the door is open or close
     *
     * @return True if the door is close, or false if the door is open
     */
    @Override
    public boolean getState() {
        boolean state = true;  // Default to closed
        try {
            state = switchPin.getValue();
        } catch (IOException ioe) {
            printMessage("DoorSensor#getState: " + ioe, ERROR);
            printMessage("Defaulting to state: " + state, ERROR);
        }
        return state;
    }

    @Override
    public SwitchData getSwitchData() {
        Date now = new Date();
        long timeStamp = now.getTime() / 1000;
        boolean s = getState();
        printMessage("DoorSensor: state = " + s, INFO);
        return new SwitchData(timeStamp, s);
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
            System.out.println("DoorSensor: " + message);
        }
    }
}
