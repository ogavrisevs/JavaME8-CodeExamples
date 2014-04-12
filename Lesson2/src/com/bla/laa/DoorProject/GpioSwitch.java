/* Copyright © 2014, Oracle and/or its affiliates. All rights reserved. */
package com.bla.laa.DoorProject;

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

public class GpioSwitch {
    private int switchPortID = 0;
    private int switchPinID = 17;

    private boolean filteringPreviousState = true;
    private GPIOPin switchPin;

    public GpioSwitch( int pinID) {
        switchPinID = pinID;
    }

    public void start(PinListener pl) throws IOException,
            PeripheralNotFoundException, PeripheralTypeNotSupportedException,
            PeripheralConfigInvalidException, PeripheralExistsException {

        // Config information for the switch
        GPIOPinConfig config1 = new GPIOPinConfig(switchPortID, switchPinID, GPIOPinConfig.DIR_INPUT_ONLY,
                PeripheralConfig.DEFAULT, GPIOPinConfig.TRIGGER_BOTH_EDGES, false);

        //Open pin using the config1 information
        switchPin = (GPIOPin) PeripheralManager.open(config1);

        // Add this class as a pin listener to the buttons
        switchPin.setInputListener(pl);
    }

    /**
     * Method to stop connection to the pin
     */
    public void stop() throws IOException {
        if (switchPin != null) {
            switchPin.close();
        }
    }

    public GPIOPin getGpioPin() {
        return switchPin;
    }
    
    public int getPin(){
        return switchPinID;
    }
            
}
