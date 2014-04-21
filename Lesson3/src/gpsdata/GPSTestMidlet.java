/* Copyright © 2014, Oracle and/or its affiliates. All rights reserved. 
 */
package gpsdata;

import gpsdata.sensor.AdaFruitGPSCommSensor;
import gpsdata.sensor.AdaFruitGPSUARTSensor;
import java.io.IOException;
import javax.microedition.midlet.MIDlet;
import mooc.data.Messages;
import static mooc.data.Messages.DATA;
import static mooc.data.Messages.ERROR;
import mooc.data.gps.Position;
import mooc.data.gps.Velocity;

/**
 *
 * @author tmcginn
 */
public class GPSTestMidlet extends MIDlet {

    private static final String SERIAL_PORT = "/dev/ttyAMA0";

    @Override
    public void startApp() {
        System.out.println("GPS recording Imlet starting");
        // Experiment with both types of accessing the GPS device
        try (AdaFruitGPSUARTSensor gps = new AdaFruitGPSUARTSensor()) {
        //try (AdaFruitGPSCommSensor gps = new AdaFruitGPSCommSensor(SERIAL_PORT)) {

            gps.setVerbose(true);
            gps.setMessageLevel(ERROR);

            for (int i = 0; i < 5; i++) {
                Position p = gps.getPosition();
                Velocity v = gps.getVelocity();
                System.out.println("Position and velocity: " + "^^" + p + "^"  + v );
                Thread.sleep(1000);
            }
        } catch (IOException ioe) {
            System.out.println("GPSTestMidlet: IOException " + ioe.getMessage());
            ioe.printStackTrace();
        } catch (InterruptedException ex) {
            // Ignore
        }

        /* Terminate the Imlet correctly */
        System.out.println("GPSTestMidlet finished");
        notifyDestroyed();
    }

    @Override
    public void pauseApp() {
    }

    @Override
    public void destroyApp(boolean unconditional) {
        // Nothing to do here, since we are using AutoCloseable to gracefully close connections
    }
}
