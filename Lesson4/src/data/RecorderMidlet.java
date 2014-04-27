/* Copyright © 2014, Oracle and/or its affiliates. All rights reserved. 
 * Java Embedded MOOC
 * 
 * April 2014
 */
package data;

import data.persist.FilePersistentStore;
import data.persist.RMSPerisistentStore;
import data.sensor.AdaFruitGPSUARTSensor;
import data.sensor.BMP180TemperatureSensor;
import doorproject.DoorSensor;
import java.io.IOException;
import java.util.ArrayList;
import javax.microedition.midlet.MIDlet;
import mooc.data.Messages;
import mooc.data.SwitchData;
import mooc.data.TemperatureData;
import mooc.data.gps.Position;
import mooc.data.gps.Velocity;

/**
 * MIDlet for getting data from the sensors via the Raspberry Pi ports and
 * storing it in the RMS and a file. Note that writing to the file is just for
 * an example, but that we're getting records only from the RMS when responding
 * to client requests
 *
 * @author Simon Ritter @speakjava
 * @author Jim Weaver @JavaFXpert
 */
public class RecorderMidlet extends MIDlet implements Messages {
    
    private static final String SERIAL_PORT = "/dev/ttyAMA0";
    private static final String RECORD_TYPE_DELIMETER = "^";
    private final ArrayList<SwitchData> _switchStates;
    private final ArrayList<TemperatureData> _temperatures;
    private final ArrayList<Position> _positions;
    private final ArrayList<Velocity> _velocities;
    
    public RecorderMidlet() {
        _switchStates = new ArrayList();
        _temperatures = new ArrayList();
        _positions = new ArrayList();
        _velocities = new ArrayList();
    }

    @Override
    public void startApp() {
        System.out.println("RecorderMIDlet: starting");
        
        try (DoorSensor doorSensor = new DoorSensor(0, 17, 24, 23);
                BMP180TemperatureSensor tempSensor = new BMP180TemperatureSensor();
                AdaFruitGPSUARTSensor gps = new AdaFruitGPSUARTSensor();
                FilePersistentStore fileStore = new FilePersistentStore("/rootfs/tmp/event-data.txt");
                RMSPerisistentStore rmsStore = new RMSPerisistentStore("event-data")
                ) {
            
            // Comment out the setMessageLevel method invocations to reduce messages
            doorSensor.setMessageLevel(INFO);
            tempSensor.setMessageLevel(INFO);
            fileStore.setMessageLevel(INFO);
            rmsStore.setMessageLevel(INFO);
            gps.setMessageLevel(INFO);
            doorSensor.start();

            /* As an example we'll take one reading from each sensor every second, 
             storing them in the RMS and a file
             */
            System.out.println("RecorderMIDlet: reading sensors");
            for (int i = 0; i < 10; i++) {
                SwitchData sd = doorSensor.getSwitchData();
                _switchStates.add(sd);
                
                TemperatureData td = tempSensor.getTemperatureData();
                _temperatures.add(td);
                
                Position p = gps.getPosition();
                _positions.add(p);
                
                Velocity v = gps.getVelocity();
                _velocities.add(v);
                
                Thread.sleep(1000);
            }
            
            System.out.println("RecorderMIDlet: saving data");
            String dataStr = "";
            
            for (int i = 0; i < 10; i++) {
                dataStr = _switchStates.get(i).toString();
                dataStr += RECORD_TYPE_DELIMETER;
                dataStr += _temperatures.get(i).toString();
                dataStr += RECORD_TYPE_DELIMETER;
                dataStr += _positions.get(i).toString();
                dataStr += RECORD_TYPE_DELIMETER;
                dataStr += _velocities.get(i).toString();
                fileStore.saveData(dataStr);
                rmsStore.saveData(dataStr);
            }
            
            System.out.println("RecorderMIDlet finished");
        } catch (IOException ioe) {
            System.out.println("RecorderMidlet: IOException " + ioe.getMessage());
            notifyDestroyed();
        } catch (InterruptedException ex) {
            // Ignore
        }
    }

    /**
     * MIDlet lifecycle termination method
     *
     * @param unconditional If the MIDlet should be terminated whatever
     */
    @Override
    public void destroyApp(boolean unconditional) {
        // No need to clean up, since the resources are closed in the try-with-resources block
    }

    /**
     * NIDlet lifecycle pause method
     */
    @Override
    public void pauseApp() {
    }
}
