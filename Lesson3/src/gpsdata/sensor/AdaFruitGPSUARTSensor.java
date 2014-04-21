/* Copyright © 2014, Oracle and/or its affiliates. All rights reserved. 
 * Java Embedded MOOC
 * 
 * February 2014
 */
package gpsdata.sensor;

import com.oracle.deviceaccess.Peripheral;
import com.oracle.deviceaccess.PeripheralManager;
import com.oracle.deviceaccess.uart.UART;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.channels.Channels;

/**
 * AdaFruit GPS sensor accessed through the UART interface of the device IO API.
 *
 * @author Simon
 */
public class AdaFruitGPSUARTSensor extends AdaFruitGPSSensor
        implements AutoCloseable {

    private static final int UART_DEVICE_ID = 40;
    private UART uart;
    private InputStream is;

    public AdaFruitGPSUARTSensor() throws IOException {
        uart = PeripheralManager.open(UART_DEVICE_ID);
        uart.setBaudRate(9600);
        is = Channels.newInputStream(uart);
        serialBufferedReader = new BufferedReader(new InputStreamReader(is));
        
        System.out.println("AdaFruit GPS Sensor: DIO API UART opened");
    }

    @Override
    public void close() throws IOException {
        serialBufferedReader.close();
        uart.close();
    }
}
