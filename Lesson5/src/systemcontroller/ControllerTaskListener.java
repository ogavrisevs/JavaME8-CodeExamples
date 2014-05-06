/* Copyright © 2014, Oracle and/or its affiliates. All rights reserved. 
 * Java Embedded MOOC
 * 
 * April 2014
 */
package systemcontroller;

import javax.microedition.swm.ManagerFactory;
import javax.microedition.swm.Suite;
import javax.microedition.swm.Task;
import javax.microedition.swm.TaskListener;
import javax.microedition.swm.TaskStatus;

/**
 *
 * @author Simon Ritter
 */
public class ControllerTaskListener implements TaskListener {

    private static final String AUTOSTART = "Auto-Start";

    /**
     * Handle a change in status
     *
     * @param task The task the change in status is associated with
     * @param newStatus The new status of that task
     */
    @Override
    public void notifyStatusUpdate(Task task, TaskStatus newStatus) {
        if (newStatus == TaskStatus.EXITED_TERMINATED
                || newStatus == TaskStatus.EXITED_REGULAR) {
            /* Figure out which suite this is and whether it is configured for
             * autostart
             */
            Suite suite = task.getSuite();
            String autoStart = suite.getAttributeValue(AUTOSTART);

            /* If the suite is configured for autostart attempt to restart it */
            if (autoStart != null && autoStart.equals("true")) {
                try {
                    ManagerFactory.getTaskManager().startTask(suite, task.getName());
                } catch (IllegalStateException |IllegalArgumentException | SecurityException ex) {
                    System.out.println("ControllerTaskListener: " + ex.getMessage());
                }
            }
        }
    }
}
