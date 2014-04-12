/* Copyright © 2014, Oracle and/or its affiliates. All rights reserved. */
package com.bla.laa.DoorProject;

import gpioledtest.*;
import com.oracle.deviceaccess.PeripheralConfigInvalidException;
import com.oracle.deviceaccess.PeripheralExistsException;
import com.oracle.deviceaccess.PeripheralManager;
import com.oracle.deviceaccess.PeripheralNotFoundException;
import com.oracle.deviceaccess.PeripheralTypeNotSupportedException;
import com.oracle.deviceaccess.gpio.GPIOPin;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GpioLed {
    private int ledID = 0;
    private GPIOPin led;

    public GpioLed(int pinNumber) {
        ledID = pinNumber;
        System.out.println("Led [ " + ledID + " ] constructor");
    }

    public void start() throws IOException,
            PeripheralNotFoundException, PeripheralTypeNotSupportedException,
            PeripheralConfigInvalidException, PeripheralExistsException {
        System.out.println("Led [ " + ledID + " ] starting");

        led = PeripheralManager.open(ledID);
        this.setValue(false);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            System.out.println(ex.toString());
        }
    }

    public void setValue(boolean value) throws IOException {
        led.setValue(value);
    }

    public boolean getValue() throws IOException {
        return led.getValue();
    }



    public void stop() throws IOException {
        if (led != null) {
            led.setValue(false);
            led.close();
        }
    }

    public int getLedId() {
        return this.ledID;
    }
}
