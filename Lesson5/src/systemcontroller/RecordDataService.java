/* Copyright © 2014, Oracle and/or its affiliates. All rights reserved. */
package systemcontroller;

import intermidletserver.Service;
import java.io.IOException;
import mooc.data.Messages;
import persistservice.PersistService;

/**
 * RecordDataService handles writing the data events received to the persistent 
 * storage mechanism.
 * 
 * @author Simon Ritter
 * @author Tom McGinn
 */
public class RecordDataService extends Service implements Messages, AutoCloseable {

    private final PersistService service;

    /**
     * Constructor
     *
     * @param service PeristService instance
     * @param serviceName The name of the IMC service
     * @throws IOException If there is an IO error
     */
    public RecordDataService(PersistService service, String serviceName) throws IOException {
        super(serviceName);
        this.service = service;
    }

    /**
     * Save the event data from the sensor that communicated the data
     *
     * @param data The data to save, as a string
     * @throws java.io.IOException
     */
    @Override
    public void saveData(String data) throws IOException {
        printMessage("RecordDataService: Received event data: " + data, INFO);
        printMessage("RecordDataService: Persisting event data...", INFO);
        service.saveData(data);
    }

    /**
     * Close the file
     *
     * @throws IOException If there is an error
     */
    @Override
    public void close() throws IOException {
    }

}
