/* Copyright © 2014, Oracle and/or its affiliates. All rights reserved. 
 * AdaFruitGPSCommSensor.java
 */
package data.sensor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.microedition.io.CommConnection;
import javax.microedition.io.Connector;

/**
 * Adafruit Ultimate GPS sensor connected to the Raspberry Pi via a serial port
 *
 * @author Simon Ritter @speakjava
 */
public class AdaFruitGPSCommSensor extends AdaFruitGPSSensor 
    implements AutoCloseable {
  /**
   * Constructor
   *
   * @param serialPort The serial port to use
   * @throws IOException If there is an IO error
   */
  public AdaFruitGPSCommSensor(String serialPort) throws IOException {
    CommConnection serialConnection = (CommConnection) Connector.open(
        "comm:" + serialPort + ";baudrate=9600");
    InputStream serialInputStream = serialConnection.openInputStream();
    serialBufferedReader
        = new BufferedReader(new InputStreamReader(serialInputStream));

    System.out.println("AdaFruit GPS Sensor: READY");
  }

  /**
   * Close the serial port
   *
   * @throws IOException If there is an IO error
   */
  @Override
  public void close() throws IOException {
    serialBufferedReader.close();
  }
}