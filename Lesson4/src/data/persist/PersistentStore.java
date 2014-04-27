/* Copyright © 2014, Oracle and/or its affiliates. All rights reserved.
 * Java Embedded MOOC
 * 
 * January 2014
 */
package data.persist;

import java.io.IOException;

/**
 * Interface for classes that can store persistent GPS data, for example RMS or
 * a file.
 *
 * @author simonri
 */
public interface PersistentStore extends AutoCloseable {

    /**
     * Save GPS data: position and velocity
     *
     * @param data String to persist to the underlying storage
     * @return The number of the record saved
     * @throws IOException If there is an IO error
     */
    public int saveData(String data) throws IOException;

    /**
     * Get the record count
     *
     * @return The number of records in the persistent store
     */
    public int getRecordCount();
}
