/* Copyright © 2014, Oracle and/or its affiliates. All rights reserved. 
 * DataSource.java
 */
package server;

/**
 * Access to individual data sources through a single object
 * 
 * @author Simon Ritter @speakjava
 * @author Jim Weaver @JavaFXpert
 */
public interface DataSource {
  // Marker interface
    public int getRecordCount();
    public String[] getRecords();
}