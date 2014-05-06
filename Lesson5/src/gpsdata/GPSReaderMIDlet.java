                       /*
 * Java Embedded MOOC
 * 
 * March 2014
 *
 * Copyright © 2014, Oracle and/or its affiliates. All rights reserved.
 */
package gpsdata;

import gpsdata.sensor.AdaFruitGPSUARTSensor;
import java.io.IOException;
import javax.microedition.midlet.MIDlet;
import static mooc.data.Messages.ERROR;
import mooc.data.gps.Position;
import mooc.data.gps.Velocity;

/**
 * MIDlet to read data from the GPS and store it using a file and RMS. How
 * the persisting of data is achieved is handled by separate MIDLets via
 * inter-MIDlet communication.
 *
 * @author simonri
 */
public class GPSReaderMIDlet extends MIDlet {
  /**
   * MIDlet lifecycle start method
   */
  @Override
  public void startApp() {
    /* Establish connection to services */
    ServiceClient filePersistClient = null;
    ServiceClient rmsPersistClient = null;

    /* Establish connection to services provided by other MIDlets */
    try {
      System.out.println("GPSReaderMIDlet: Establishing connection to file service");
      filePersistClient = new ServiceClient("mooc.filepersist:*;");
      System.out.println("GPSReaderMIDlet: Establishing connection to RMS service");
      rmsPersistClient = new ServiceClient("mooc.rmspersist:1.0;");
    } catch (IOException ioe) {
      System.out.println("GPSReaderMIDlet: Error connecting to persist service");
      System.out.println(ioe.getMessage());
      notifyDestroyed();
    }

    /* The notifyDetroyed in the catch above does not seem to actually
     * terminate the MIDlet before we get here, s we need an explicit check
     */
    if (filePersistClient != null && rmsPersistClient != null) {
      System.out.println("Done.");

      try (AdaFruitGPSUARTSensor gps = new AdaFruitGPSUARTSensor();) {
        gps.setVerbose(true);
        gps.setMessageLevel(ERROR);

        /* As an example we'll take one reading every second and store three 
         * sets of data
         */
        for (int i = 0; i < 5; i++) {
          Position p = gps.getPosition();
          Velocity v = gps.getVelocity();

          filePersistClient.persistData(p, v);
          rmsPersistClient.persistData(p, v);
          Thread.sleep(1000);
        }
      } catch (IOException ioe) {
        System.out.println("RecorderMidlet: IOException " + ioe.getMessage());
        ioe.printStackTrace();
      } catch (InterruptedException ex) {
        // Ignore
      }
    }
  }

  /**
   * MIDlet lifecycle pause method
   */
  @Override
  public void pauseApp() {
  }

  /**
   * MIDlet lifecycle termination method
   *
   * @param unconditional If the imlet should be terminated whatever
   */
  @Override
  public void destroyApp(boolean unconditional) {
  }
}