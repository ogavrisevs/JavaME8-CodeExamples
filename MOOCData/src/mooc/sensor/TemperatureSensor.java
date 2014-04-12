/* Copyright © 2014, Oracle and/or its affiliates. All rights reserved.
 * Java Embedded MOOC
 *
 * January 2014
 */
package mooc.sensor;

import java.io.IOException;
import mooc.data.TemperatureData;

/**
 * Interface for a temperature sensor
 *
 * @author simonri
 */
public interface TemperatureSensor {

    /**
     * Get the current temperature in degrees Celsius
     *
     * @return The current temperature in degrees Celsius
     * @throws java.io.IOException If there is an IO error reading the
     * temperature value
     */
    public double getTemperatureInCelsius() throws IOException;

    /**
     * Get the current temperature in degrees Fahrenheit
     *
     * @return The current temperature in degrees Fahrenheit
     * @throws java.io.IOException If there is an IO error reading the
     * temperature value
     */
    public double getTemperatureInFahrenheit() throws IOException;

    /**
     * Get the temperature data (timestamp and value)
     *
     * @return The current temperature data
     */
    public TemperatureData getTemperatureData();
}
