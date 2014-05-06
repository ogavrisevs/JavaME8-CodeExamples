/*
 * Java Embedded MOOC
 * 
 * February 2014
 *
 * Copyright © 2014, Oracle and/or its affiliates. All rights reserved.
 */
package gpsdata.sensor;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import mooc.data.Messages;
import static mooc.data.Messages.ERROR;
import static mooc.data.Messages.INFO;
import mooc.data.gps.Position;
import mooc.data.gps.Velocity;
import mooc.sensor.GPSSensor;

/**
 * Common code for the AdaFruit Ultimate GPS sensor.  This is an abstract 
 * class so the implementation of the readDataLine method can be implemented
 * in subclasses that can use different methods to communicate with the 
 * GPS receiver.
 * 
 * @author Simon Ritter
 */
public abstract class AdaFruitGPSSensor implements GPSSensor, Messages {
  private static final String POSITION_TAG = "GPGGA";
  private static final String VELOCITY_TAG = "GPVTG";
  
  private final ArrayList<String> fields = new ArrayList<>();
  protected BufferedReader serialBufferedReader;
  private boolean verbose = false;
  private int messageLevel = 1;

  /**
   * Get a line of raw data from the GPS sensor
   *
   * @return The complete line of data
   * @throws IOException If there is an IO error
   */
  protected String readDataLine() throws IOException {
    String dataLine = null;

    /**
     * All data lines start with a '$' character so keep reading until we find a
     * valid line of data
     */
    do {
      dataLine = serialBufferedReader.readLine();

      /* Got a valid line, so break out of the loop */
      if (dataLine.startsWith("$")) {
        break;
      }
    } while (true);

    /* Return what we got */
    return dataLine;
  }

  /**
   * Get a string of raw data from the GPS receiver.  How this happens is 
   * sub-class dependent.
   * 
   * @param type The type of data to be retrieved
   * @return A line of data for that type
   * @throws IOException If there is an IO error 
   */
  @Override
  public String getRawData(String type) throws IOException {
    boolean foundGGAData = false;
    String dataLine = null;

    do {
      /**
       * Retrieve a line with the appropriate tag. Return null in the case of 
       * an error
       */
      try {
        dataLine = readDataLine();
      } catch (IOException ex) {
        return null;
      }

      /* Extract the type of the data */
      String dataType = dataLine.substring(1, 6);

      /* If this is the type we're looking break out of the loop */
      if (dataType.compareTo(type) == 0) {
        break;
      }
    } while (!foundGGAData);

    return dataLine.substring(7);
  }

  /**
   * Get the current position
   * 
   * @return The position data
   * @throws IOException If there is an IO error
   */
  @Override
  public Position getPosition() throws IOException {
    String rawData;
    long timeStamp = 0;
    double latitude = 0;
    double longitude = 0;
    double altitude = 0;
    char latitudeDirection = 0;
    char longitudeDirection = 0;

    /* Read data repeatedly, until we have valid data */
    while (true) {
      rawData = getRawData(POSITION_TAG);

      /* Handle situation where we didn't get data */
      if (rawData == null) {
        printMessage("NULL position data received", ERROR);
        continue;
      }

      if (rawData.contains("$GP")) {
        printMessage("Corrupt position data", ERROR);
        continue;
      }

      int fieldCount = splitCSVString(rawData);

      /**
       * The position data must have 10 fields to it to be valid, so reject the
       * data if we don't have the correct number
       */
      if (fieldCount < 10) {
        printMessage("Incorrect position field count", ERROR);
        continue;
      }

      printMessage("position data = " + rawData, INFO);

      /* Record a time stamp for the reading */
      Date now = new Date();
      timeStamp = now.getTime() / 1000;

      /**
       * Parse the relevant fields into values that we can use to create a new
       * Position object.
       */
      try {
        latitude = Double.parseDouble(fields.get(1)) / 100;
        latitudeDirection = fields.get(2).toCharArray()[0];
      } catch (NumberFormatException nfe) {
        printMessage("Badly formatted latitude number", ERROR);
        continue;
      }

      try {
        longitude = Double.parseDouble(fields.get(3)) / 100;
        longitudeDirection = fields.get(4).toCharArray()[0];
      } catch (NumberFormatException nfe) {
        printMessage("Badly formatted longitude number", ERROR);
        continue;
      }

      try {
        altitude = Double.parseDouble(fields.get(8));
      } catch (NumberFormatException nfe) {
        printMessage("Badly formatted altitude number", ERROR);
        continue;
      }

      /* Passed all the tests so we have valid data */
      break;
    }

    /* Return the encapsulated data */
    return new Position(timeStamp, latitude, latitudeDirection,
        longitude, longitudeDirection, altitude);
  }

  /**
   * Get the current velocity
   * 
   * @return The velocity data
   * @throws IOException If there is an IO error
   */
  @Override
  public Velocity getVelocity() throws IOException {
    String rawData = getRawData(VELOCITY_TAG);
    double track = 0;
    double speed = 0;

    while (true) {
      /* Handle the situation where we didn't get valid data */
      if (rawData == null) {
        printMessage("NULL velocity data received", ERROR);
        continue;
      }

      int fieldCount = splitCSVString(rawData);

      if (fieldCount < 8) {
        printMessage("Incorrect velocity field count", ERROR);
        continue;
      }

      /* Extract the track and velocity of the GPS receiver */
      try {
        track = Double.parseDouble(fields.get(0));
      } catch (NumberFormatException nfe) {
        printMessage("Badly formatted track number", ERROR);
        continue;
      }

      try {
        speed = Double.parseDouble(fields.get(6));
      } catch (NumberFormatException nfe) {
        printMessage("Badly formatted speed number", ERROR);
        continue;
      }

      break;
    }

    printMessage("velocity data = " + rawData, INFO);

    /* Record a time stanp for the reading */
    Date now = new Date();
    long timeStamp = now.getTime() / 1000;

    printMessage("Bearing = " + fields.get(0), DATA);
    printMessage("speed = " + fields.get(6), DATA);

    /* Return the Velocity object */
    return new Velocity(timeStamp, track, speed);
  }
  
  /**
   * Turn on or off verbose messaging
   *
   * @param verbose Whether to enable verbose messages
   */
  public void setVerbose(boolean verbose) {
    this.verbose = verbose;
  }
  
  /**
   * Set the level of messages to display, 1 = ERROR, 2 = INFO
   * 
   * @param level The level for messages
   */
  public void setMessageLevel(int level) {
    messageLevel = level;
  }
  
  /**
   * Break a comma separated value string into its individual fields. We need to
   * have this as explicit code because Java ME does not support String.split or
   * java.util.regex and StringTokenizer has a bug that affects empty fields.
   *
   * @param input The CSV input string
   * @return The number of fields extracted
   */
  private int splitCSVString(String input) {
    /* Clear the list of data fields */
    fields.clear();
    int start = 0;
    int end;

    while ((end = input.indexOf(",", start)) != -1) {
      fields.add(input.substring(start, end));
      start = end + 1;
    }

    return fields.size();
  }

  /**
   * Print a message if verbose messaging is turned on
   *
   * @param message The message to print
   * @param level What level to log these messages at
   */
  protected void printMessage(String message, int level) {
    if (verbose && level <= messageLevel)
      System.out.println("AdaFruit GPS Sensor: " + message);
  }
}