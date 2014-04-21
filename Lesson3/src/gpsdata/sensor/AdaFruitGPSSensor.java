/* Copyright © 2014, Oracle and/or its affiliates. All rights reserved. 
 * Java Embedded MOOC
 * 
 * February 2014
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
 * Common code for the AdaFruit Ultimate GPS sensor. This is an abstract class
 * so the implementation of the readDataLine method can be implemented in
 * subclasses that can use different methods to communicate with the GPS
 * receiver.
 *
 * @author Simon
 */
public abstract class AdaFruitGPSSensor implements GPSSensor, Messages {

    private static final String POSITION_TAG = "GPGGA";
    private static final String VELOCITY_TAG = "GPVTG";

    private final ArrayList<String> fields = new ArrayList<>();
    protected BufferedReader serialBufferedReader;
    private boolean verbose = false;
    private int messageLevel = 1;

    protected String readDataLine() throws IOException {
        String dataLine = "";        
        while (!dataLine.startsWith("$")){
            dataLine = serialBufferedReader.readLine();
        }        
        return dataLine;
    }

    /**
     * Get a string of raw data from the GPS receiver. How this happens is
     * sub-class dependent.
     *
     * @param type The type of data to be retrieved
     * @return A line of data for that type
     * @throws IOException If there is an IO error
     */
    
    @Override
    public String getRawData(String type) throws IOException {
        boolean foundGGAData = false;
        String dataLine = "";

        while (true) {
            dataLine = serialBufferedReader.readLine();
            if ((dataLine != null) && (dataLine.length() > 6) && (dataLine.startsWith("$"))) {
                if (dataLine.substring(1, 6).equals(type)) {
                    break;
                }
            }
        }
        return dataLine.substring(7);
    }

    @Override
    public Position getPosition() throws IOException {
        String rawData;
        long timeStamp = 0;
        double latitude = 0;
        double longitude = 0;
        double altitude = 0;
        char latitudeDirection = 0;
        char longitudeDirection = 0;

        while (true) {
            rawData = getRawData(POSITION_TAG);

            if (rawData == null) {
                printMessage("NULL position data received", ERROR);
                continue;
            }
            if (rawData.contains("$GP")) {
                printMessage("Corrupt position data", ERROR);
                continue;
            }
            if (splitCSVString(rawData) < 10){
                printMessage("Insufficient fields ", ERROR);
                continue;
            }
            if (!fields.get(2).equals("N") && !fields.get(2).equals("S")){
                printMessage("Cant finde latitude", ERROR);
                continue;
            }
            if (!fields.get(4).equals("E") && !fields.get(4).equals("W")){
                printMessage("Cant finde longitude", ERROR);
                continue;
            }            
            
            break;
        }

        Date now = new Date();
        timeStamp = now.getTime() / 1000;

        latitude = Double.valueOf(fields.get(1));
        latitudeDirection = fields.get(2).charAt(0);
        
        longitude = Double.valueOf(fields.get(3));
        longitudeDirection = fields.get(4).charAt(0);
                        
        altitude = Double.valueOf(fields.get(8));
        
        return new Position(timeStamp, latitude, latitudeDirection,
                longitude, longitudeDirection, altitude);
    }

    @Override
    public Velocity getVelocity() throws IOException {
        String rawData = getRawData(VELOCITY_TAG);
        double track = 0;
        double speed = 0;

        while (true) {
            
            if (rawData == null) {
                printMessage("NULL velocity data received", ERROR);
                continue;
            }
            if (splitCSVString(rawData) < 8){
                printMessage("no valid velocity date", ERROR);
                continue;
            }
            if (fields.get(7).charAt(0) != 'K'){
                printMessage("Cant find speed ", ERROR);
                continue;
            }
            
            break;
        }
        
        Date now = new Date();
        long timeStamp = now.getTime() / 1000;

        speed = Double.valueOf(fields.get(6));
        track = Double.valueOf(fields.get(0));
            
        return new Velocity(timeStamp, track, speed);
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public void setMessageLevel(int level) {
        messageLevel = level;
    }

    private int splitCSVString(String input) {
        fields.clear();
        int start = 0;
        int end;

        printMessage("raw : "+ input, DATA);
        while ((end = input.indexOf(",", start)) != -1) {
            fields.add(input.substring(start, end));
            start = end + 1;
        }
        printMessage("fields : "+ fields.toString(), DATA);
        return fields.size();
    }

    protected void printMessage(String message, int level) {
        if (verbose && level <= messageLevel) {
            System.out.println("AdaFruit GPS Sensor: " + message);
        }
    }
}
