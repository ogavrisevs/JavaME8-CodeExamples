/* Copyright © 2014, Oracle and/or its affiliates. All rights reserved. */
package tempbarosensor;

import java.io.IOException;
import javax.microedition.midlet.MIDlet;

/**
 *
 * @author Acaicedo Midlet for testing our sensor
 */
public class TempSensorTest extends MIDlet {

    private BMP180TemperatureSensor myDev;

    @Override
    public void startApp() {
        myDev = new BMP180TemperatureSensor();

        // An example of an anonymous thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        Thread.sleep(500);
                        System.out.format("Temp: %.2fF\n",myDev.getTemperatureInFahrenheit());
                    }
                } catch (IOException | InterruptedException e) {
                }
            }
        }).start();
    }

    @Override
    public void pauseApp() {
    }

    @Override
    public void destroyApp(boolean unconditional) {
    }
}
