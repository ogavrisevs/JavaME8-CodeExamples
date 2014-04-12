/* Copyright © 2014, Oracle and/or its affiliates. All rights reserved. */
package gpioledtest;

import com.oracle.deviceaccess.PeripheralConfigInvalidException;
import com.oracle.deviceaccess.PeripheralExistsException;
import com.oracle.deviceaccess.PeripheralNotFoundException;
import com.oracle.deviceaccess.PeripheralTypeNotSupportedException;
import java.io.IOException;
import javax.microedition.midlet.MIDlet;

/**
 *
 * @author Angela Caicedo
 */
public class GPIOLEDTest extends MIDlet {

    GPIOLED pinTest; 
    
    
    /**
     * Imlet lifecycle start method
     * 
     * This method creates a GPIOLED, and invoked the blink method, to blink the
     * LED X number of times, in our case 8 times. 
     */
    @Override
    public void startApp() {
        pinTest = new GPIOLED(23);
        try {
            pinTest.start();
        } catch (PeripheralTypeNotSupportedException | PeripheralNotFoundException | PeripheralConfigInvalidException | PeripheralExistsException ex) {
            System.out.println("PeripheralException: " + ex.getMessage());
            notifyDestroyed();
        } catch (IOException ex) {
            System.out.println("IOException: " + ex);
            notifyDestroyed();
        }
        pinTest.blink(8);
    }

    
   /**
   * Imlet lifecycle termination method
   * 
   * @param unconditional If the imlet should be terminated whatever
   */
    @Override
    public void destroyApp(boolean unconditional) {
        try {
            pinTest.stop();
        } catch (IOException ex) {
            System.out.println("IOException: " + ex);
        }
    }

    /**
     * Imlet lifecycle pause method
     */
    @Override
    public void pauseApp() {
    }

}
