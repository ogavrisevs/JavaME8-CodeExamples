/* Copyright © 2014, Oracle and/or its affiliates. All rights reserved. 
 * Java Embedded MOOC
 * 
 * January 2014
 */
package gpsdata.sensor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.microedition.io.CommConnection;
import javax.microedition.io.Connector;

public class AdaFruitGPSCommSensor extends AdaFruitGPSSensor 
    implements AutoCloseable {
    private static final String SERIAL_PORT = "/dev/ttyAMA0";

    CommConnection con;
    InputStream is;
    
  public AdaFruitGPSCommSensor() throws IOException {
      con = (CommConnection) Connector.open("comm:"+ SERIAL_PORT +";baudrate=9600");      
      is = con.openInputStream();
      serialBufferedReader = new BufferedReader(new InputStreamReader(is));
      
      System.out.println("AdaFruit GPS Sensor: READY");
  }

  @Override
  public void close() throws IOException {
    serialBufferedReader.close();
  }
}