/* Copyright © 2014, Oracle and/or its affiliates. All rights reserved. 
 * DataProtocol.java
 */
package mooc.data.server;

/**
 * Definition of the commands to send to the server
 * 
 * @author simonri
 */
public interface DataProtocol {
  public static final String READ_DATA = "READ";
  public static final String DISCONNECT = "DISCONNECT";
}