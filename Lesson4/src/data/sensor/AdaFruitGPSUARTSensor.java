/* Copyright © 2014, Oracle and/or its affiliates. All rights reserved. 
 * AdaFruitGPSUARTSensor.java
 */
package data.sensor;

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
 * @author Simon Ritter @speakjava
 */
public class AdaFruitGPSUARTSensor extends AdaFruitGPSSensor implements AutoCloseable {

    private static final int UART_DEVICE_ID = 40;

    private UART uart;

    /**
     * Constructor
     *
     * @throws IOException If there is an IO error
     */
    public AdaFruitGPSUARTSensor() throws IOException {
        try {
            uart = PeripheralManager.open(UART_DEVICE_ID);
            uart.setBaudRate(9600);
            InputStream serialInputStream = Channels.newInputStream(uart);
            serialBufferedReader
                    = new BufferedReader(new InputStreamReader(serialInputStream));
        } catch (IOException ioe) {
            printMessage("Unable to open the UART", ERROR);
            printMessage("Exception = " + ioe.getMessage(), ERROR);
            throw ioe;
        }

        printMessage("AdaFruit GPS Sensor: DIO API UART opened", INFO);
    }

    /**
     * Close the connection to the GPS receiver via the UART
     *
     * @throws IOException If there is an IO error
     */
    @Override
    public void close() throws IOException {
        serialBufferedReader.close();
        uart.close();
    }
}
