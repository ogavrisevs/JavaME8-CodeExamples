/* Copyright © 2014, Oracle and/or its affiliates. All rights reserved. */
package gpioswitchtest;

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

/**
 *
 * @author Angela This class is use to create a pin for input (a switch in our
 * case) and to listen to the in changes of the pin value
 */
public class GPIOSwitch implements PinListener {

    //Switch GPIO Port.  Default value 0
    private int switchPortID = 0;
    //Switch GPIO Pin. Default value 17
    private int switchPinID = 17;

    private boolean filteringPreviousState = true;

    private GPIOPin switchPin;

    /**
     * Constructor for GPIOSwitch
     *
     * @param portID port to be use
     * @param pinID pin where the switch is connected
     */
    public GPIOSwitch(int portID, int pinID) {
        switchPortID = portID;
        switchPinID = pinID;
    }

    /**
     * Method to create the GPIOPin and register listening
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
        switchPin = (GPIOPin) PeripheralManager.open(config1);

        // Add this class as a pin listener to the buttons
        switchPin.setInputListener(this);

    }

    /**
     * Method to stop connection to the pin
     *
     * @throws IOException
     */
    public void stop() throws IOException {
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
        // Simple one button = one LED
        try {
            //Verify the event come from the switch
            if (pin == switchPin) {
                if (pin.getValue() == true) {
                    if (filteringPreviousState != true) { //filtering multiple consecutive trues
                        System.out.println(" true");
                        filteringPreviousState = true;
                    }
                } else {
                    if (filteringPreviousState != false) { //filtering multiple consecutive false
                        System.out.println(" false");
                        filteringPreviousState= false;
                    }
                }

            }
        } catch (IOException ex) {
            System.out.println("IOException: " + ex);
        }
    }
}
