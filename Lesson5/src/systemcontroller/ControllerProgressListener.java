/* Copyright © 2014, Oracle and/or its affiliates. All rights reserved. 
 * Java Embedded MOOC
 * 
 * April 2014
 */
package systemcontroller;

import java.util.StringTokenizer;
import javax.microedition.swm.InstallErrorCodes;
import javax.microedition.swm.ManagerFactory;
import javax.microedition.swm.Suite;
import javax.microedition.swm.SuiteInstallListener;
import javax.microedition.swm.SuiteInstallStage;
import javax.microedition.swm.SuiteManagementTracker;
import javax.microedition.swm.TaskManager;

/**
 * Listener for changes to progress of suite installation
 *
 * @author Simon Ritter
 */
public class ControllerProgressListener implements SuiteInstallListener {

    /**
     * Installation completed callback
     *
     * @param errorCode An error code if the install did not complete correctly
     * @param tracker The tracker for changes to the suite
     */
    @Override
    public void installationDone(InstallErrorCodes errorCode, SuiteManagementTracker tracker) {
        //System.out.println("ControllerProgressListener: Installation done! " + errorCode);

        if (errorCode == InstallErrorCodes.NO_ERROR) {
            /* Suite installed succesfully, so now we can start it */
            Suite suite = tracker.getSuite();

            String midlet = suite.getAttributeValue("MIDlet-1");
            //System.out.println("ControllerProgressListener: Got a MIDlet " + midlet);

            if (midlet != null) {
                System.out.println("ControllerProgressListener: Starting midlet: " + midlet);

                /* Get the MIDlet class name from the attribute */
                StringTokenizer tokens = new StringTokenizer(midlet, ",");

                if (tokens.countTokens() > 2) {
                    tokens.nextToken();   // Skip the Midlet icon name, if necessary
                }
                tokens.nextToken();     // Skip the Midlet name
                String midletClass = tokens.nextToken().trim();  //Midlet class name

                /* Get the task manager reference to start this midlet */
                try {
                    TaskManager taskManager = ManagerFactory.getTaskManager();
                    taskManager.addStatusListener(new ControllerTaskListener());
                    taskManager.startTask(suite, midletClass);
                } catch (SecurityException e) {
                    System.out.println("ControllerProgressListener: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Status update callback
     *
     * @param tracker The tracker for changes to the suite
     * @param status The new status of the installation
     * @param percentage The percentage of installation that is complete
     */
    @Override
    public void updateStatus(SuiteManagementTracker tracker,
            SuiteInstallStage status, int percentage) {
        String statusStr = "No message";

        switch (status) {
            case DOWNLOADING_BODY:
                statusStr = "Downloading body...";
                break;
            case DOWNLOADING_DATA:
                statusStr = "Downloading data...";
                break;
            case DOWNLOADING_DESCRIPTOR:
                statusStr = "Downloading descriptor...";
                break;
            case STORING:
                statusStr = "Storing...";
                break;
            case VERIFYING:
                statusStr = "Verifying...";
                break;
            case DONE:
                statusStr = "Complete!";
                break;
            default:
                break;
        }
        //System.out.println("updateStatus: " + statusStr + " " + percentage + "%");
    }
}
