/* Copyright © 2014, Oracle and/or its affiliates. All rights reserved. */
package data.sensor;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 *
 * @author Angela
 */
public class BMP180Sensor extends I2CSensor {

    /**
     * Device address BMP180 address is 0x77
     */
    private static final int BMP180_ADDR = 0x77;

    // EEPROM registers - these represent calibration data
    protected short AC1;
    protected short AC2;
    protected short AC3;
    protected int AC4;
    protected int AC5;
    protected int AC6;
    protected short B1;
    protected short B2;
    protected short MB;
    protected short MC;
    protected short MD;

    //Variable common between temperature & pressure calculations
    protected int B5;

    //Total of bytes use for callibration
    protected final int callibrationBytes = 22;

    // Address byte length
    protected final int subAddressSize = 1;                // Size of each address (in bytes)
    // EEPROM address data
    protected final int EEPROM_start = 0xAA;
    //BMP180 control registry
    protected final int controlRegister = 0xF4;

    /**
     * BMP180 constructor invokes the parent constructor to initialize the
     * connection with the I2C device. Them we proceed with some initialization
     * steps(reading calibration data)
     */
    public BMP180Sensor() {
        super(BMP180_ADDR);
        initDevice();
    }

    /**
     * BMP180 constructor invokes the parent constructor to initialize the
     * connection with the I2C device. Them we proceed with some initialization
     * steps(reading calibration data)
     *
     * @param i2cBus I2C Bus. For Raspberry Pi it's normally 1
     * @param address Device Address
     * @param addressSizeBits I2C uses 7bits address size
     * @param serialClock Clock speed
     */
    public BMP180Sensor(int i2cBus, int address, int addressSizeBits, int serialClock) {
        super(i2cBus, address, addressSizeBits, serialClock);
        initDevice();
    }

    /**
     * This method read the calibration data common for the Temperature sensor
     * and Barometer sensor included in the BMP180
     */
    private void initDevice() {
        try {
            //Small delay before starting
            Thread.sleep(500);
            //Getting calibration data
            gettingCalibration();
        } catch (IOException e) {
            printMessage("Exception: " + e.getMessage(), ERROR);
        } catch (InterruptedException e) {
            printMessage("Interrupted Exception: " + e.getMessage(), ERROR);
        }
    }

    /**
     * Method for reading the calibration data. Do not worry too much about this
     * method. Normally this information is given in the device information
     * sheet.
     *
     * @throws IOException
     */
    public void gettingCalibration() throws IOException {
        // Read all of the calibration data into a byte array
        ByteBuffer calibData = ByteBuffer.allocateDirect(callibrationBytes);
        int result = myDevice.read(EEPROM_start, subAddressSize, calibData);
        if (result < callibrationBytes) {
            printMessage("Not all the callibration bytes were read", ERROR);
            return;
        }
        // Read each of the pairs of data as a signed short
        calibData.rewind();
        AC1 = calibData.getShort();
        AC2 = calibData.getShort();
        AC3 = calibData.getShort();

        // Unsigned short values
        byte[] data = new byte[2];
        calibData.get(data);
        AC4 = (((data[0] << 8) & 0xFF00) + (data[1] & 0xFF));
        calibData.get(data);
        AC5 = (((data[0] << 8) & 0xFF00) + (data[1] & 0xFF));
        calibData.get(data);
        AC6 = (((data[0] << 8) & 0xFF00) + (data[1] & 0xFF));

        // Signed sort values
        B1 = calibData.getShort();
        B2 = calibData.getShort();
        MB = calibData.getShort();
        MC = calibData.getShort();
        MD = calibData.getShort();
    }
}
