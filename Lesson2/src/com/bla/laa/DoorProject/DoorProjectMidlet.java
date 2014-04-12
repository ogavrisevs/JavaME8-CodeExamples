/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bla.laa.DoorProject;

import com.oracle.deviceaccess.gpio.GPIOPin;
import com.oracle.deviceaccess.gpio.PinEvent;
import com.oracle.deviceaccess.gpio.PinListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.microedition.midlet.MIDlet;

/**
 *
 * @author oskarsg
 */
public class DoorProjectMidlet extends MIDlet implements PinListener {

    private GpioLed redLed;
    private GpioLed greenLed;
    private GpioSwitch dswitch;
    private LedBlinker blinker;

    private final int switchPin = 17;
    private final int gledPin = 23;
    private final int rledPin = 24;

    
    @Override
    public void startApp() {
        System.out.println("Startin app . . . ");

        redLed = new GpioLed(rledPin);
        greenLed = new GpioLed(gledPin);
        dswitch = new GpioSwitch(switchPin);

        try {
            redLed.start();
            greenLed.start();
            dswitch.start(this);
            
            blinker = new LedBlinker(redLed);
            new Thread(blinker).start();

        } catch (IOException ex) {
            Logger.getLogger(DoorProjectMidlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        
    }

    @Override
    public void valueChanged(PinEvent event) {
        GPIOPin pin = (GPIOPin) event.getPeripheral();
        try {
            System.out.println("Switch pressed [ "+ dswitch.getPin() +" ] : "+ pin.getValue());
            if (pin == dswitch.getGpioPin()) {
                if (pin.getValue() == true) {
                    
                    blinker.stopBlinking();
                    blinker = new LedBlinker(greenLed);
                    new Thread(blinker).start();
                } else {
                    blinker.stopBlinking();
                    blinker = new LedBlinker(redLed);
                    new Thread(blinker).start();
                }
            }
        } catch (IOException ex) {
            System.out.println("IOException: " + ex);
        }
    }

    @Override
    public void pauseApp() {
    }

    @Override
    public void destroyApp(boolean unconditional) {
        System.out.println("Destroying app . . . ");
        try {
            redLed.stop();
            greenLed.stop();
            dswitch.stop();

        } catch (IOException ex) {
            Logger.getLogger(DoorProjectMidlet.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
