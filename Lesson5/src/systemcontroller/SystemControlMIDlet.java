/* Copyright © 2014, Oracle and/or its affiliates. All rights reserved. */
package systemcontroller;

import java.io.IOException;
import java.util.List;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.midlet.MIDlet;
import javax.microedition.swm.ManagerFactory;
import javax.microedition.swm.SuiteInstaller;
import javax.microedition.swm.SuiteManager;
import javax.microedition.swm.Task;
import javax.microedition.swm.TaskManager;
import mooc.data.Messages;
import static mooc.data.Messages.INFO;
import persistservice.PersistService;
import rmspersistdata.RMSPersistService;

/**
 * The SystemControlMIDlet is a receiver and processor of events. This
 * application starts threads that listen for asynchronous events from the door
 * sensor, GPS and temperature sensors.
 *
 * @author Tom McGinn
 */
public class SystemControlMIDlet extends MIDlet implements Messages {

    // Each sensor type has its own service to handle recording events
    // to the choosen persistent store
    private RecordDataService switchService, gpsService, tempService;
    private final String switchIMC = "mooc.switch:1.0;";
    private final String GPSIMC = "mooc.gps:1.0;";
    private final String TempIMC = "mooc.temp:1.0;";
    private PersistService persistService;
    private String rmsStorename = "event-data";

    @Override
    public void startApp() {
        System.out.println("SystemControlMIDlet: starting");

        // If the property RMSStorename is defined, use that for the
        // RMS store name
        String rmsStorenameProp = getAppProperty("RMSStorename");
        if (rmsStorenameProp != null) {
            rmsStorename = rmsStorenameProp;
        }

        // Start up the services
        try {
            // Open the persistence service
            persistService = new RMSPersistService(rmsStorename);
            persistService.setMessageLevel(INFO);

            /*
             * Launch threads to handle the communication requests from sensors
             */
            // GPIO Switch events
            switchService = new RecordDataService(persistService, switchIMC);
            switchService.start();

            // GPS Events
            gpsService = new RecordDataService(persistService, GPSIMC);
            gpsService.start();

            // Temp Events
            tempService = new RecordDataService(persistService, TempIMC);
            tempService.start();

            System.out.println("SystemControlMIDlet: waiting for connections");

        } catch (ConnectionNotFoundException ex) {
            System.out.println("SystemControlMIDlet: Missing IMC connection: " + ex);
            System.out.println("Is a service loaded/started?");
        } catch (IOException ex) {
            System.out.println("SystemControlMIDlet: ERROR starting service: " + ex);
            //ex.printStackTrace();
        }

        /**
         * Load and start MIDlet suites
         */
        SuiteManager suiteManager = ManagerFactory.getSuiteManager();
        int midletNumber = 1;
        String jadURL;
        System.out.println("SystemControlMIDlet: Loading MIDlet suites...");

        /* Read the list of MIDlets to control from the application descriptor
         * and install each one
         */
        while (true) {
            if ((jadURL = getAppProperty("InstallSuite-" + midletNumber++)) == null) {
                break;
            }

            //System.out.println("SystemControlMIDlet: Got a url = " + jadURL);

            try {
                System.out.println("SystemControlMIDlet: Installing JAD file: " + jadURL);
                ControllerProgressListener progressListener
                        = new ControllerProgressListener();
                SuiteInstaller installer
                        = suiteManager.getSuiteInstaller(jadURL, false);

                if (installer == null) {
                    System.out.println("SystemControlMIDlet: installer is null");
                    continue;
                }

                installer.addInstallationListener(progressListener);
                //System.out.println("SystemControlMIDlet: Starting install...");
                installer.start();

                /**
                 * EA2 has a bug where installDone is being called too early.
                 * This is a workaround. Wait 2 seconds and try again.
                 */
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {

                }
                System.out.println("SystemControlMIDlet: Starting install...");
                installer.start();

            } catch (SecurityException |
                    IllegalArgumentException ex) {
                System.out.println("SystemControlMIDlet: Error installing suite: " + ex);
            }
        }
    }

    @Override
    public void pauseApp() {
    }

    @Override
    public void destroyApp(boolean unconditional) {
        if (persistService != null) {
            try {
                persistService.close();
            } catch (Exception ex) {
                System.out.println("Unable to close persistService: " + ex);
            }
        }
        if (switchService != null) {
            switchService.stopService();
        }
        if (gpsService != null) {
            gpsService.stopService();
        }
        if (tempService != null) {
            tempService.stopService();
        }

        // Stop the running tasks
        TaskManager taskManager = ManagerFactory.getTaskManager();
        List<Task> tasks = taskManager.getTaskList(false);

        for (Task t : tasks) {
            System.out.println("Stopping task: " + t.getName());
            taskManager.stopTask(t);
        }
    }
}
