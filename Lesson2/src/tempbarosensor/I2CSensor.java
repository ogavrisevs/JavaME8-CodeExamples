/* Copyright © 2014, Oracle and/or its affiliates. All rights reserved. */
package tempbarosensor;

import com.oracle.deviceaccess.PeripheralManager;
import com.oracle.deviceaccess.i2cbus.I2CDevice;
import com.oracle.deviceaccess.i2cbus.I2CDeviceConfig;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 *
 * @author Angela
 */
public abstract class I2CSensor {

    protected I2CDevice myDevice = null;             // I2C device

    private int i2cBus = 1;                        //Default I2C bus

    private int serialClock = 3400000;             // default clock 3.4MHz Max clock  
    private int addressSizeBits = 7;               // default address
    private int address = 0x00;                    //I2C device address
    private final int registrySize = 1;            //Registry size in bytes

    private ByteBuffer command;
    private ByteBuffer byteToRead;

    /**
     * Constructor for the I2C sensor. This method update the device address and
     * try to connect to the device
     *
     * @param address Device's address
     */
    public I2CSensor(int address) {
        this.address = address;
        connectToDevice();
    }

    /**
     * Constructor for the I2C sensor.
     *
     * @param i2cBus Device bus. For Raspberry Pi usually it's 1
     * @param address Device address
     * @param addressSizeBits I2C normally uses 7 bits addresses
     * @param serialClock Clock speed
     */
    public I2CSensor(int i2cBus, int address, int addressSizeBits, int serialClock) {
        this.address = address;
        this.addressSizeBits = addressSizeBits;
        this.serialClock = serialClock;
        this.i2cBus = i2cBus;
        connectToDevice();
    }

    /**
     * This method tries to connect to the I2C device, initializing myDevice
     * variable
     */
    private void connectToDevice() {
        // Construct the shared ByteBuffers
        // Reusing the buffers rather than allocating new space each time is
        // good practice with embedded devices to reduce garbage collection.
        command = ByteBuffer.allocateDirect(registrySize);
        byteToRead = ByteBuffer.allocateDirect(1);
        try {
            I2CDeviceConfig config = new I2CDeviceConfig(i2cBus, address, addressSizeBits, serialClock);
            myDevice = PeripheralManager.open(config);
            System.out.println("Connected to the device OK!!!");
        } catch (IOException e) {
            System.out.println("Error connecting to device");
        }
    }

    /**
     * This method tries to write one single byte to particular registry
     *
     * @param registry Registry to write
     * @param byteToWrite Byte to be written
     */
    public void writeOneByte(int registry, byte byteToWrite) {
        command.clear();
        command.put(byteToWrite);
        command.rewind();
        try {
            myDevice.write(registry, registrySize, command);
        } catch (IOException e) {
            System.out.println("Error writing registry " + registry);
        }

    }

    /**
     * This method reads one byte from a specified registry address. The method
     * checks that the byte is actually read, otherwise it'll show some messages
     * in the output
     *
     * @param registry Registry to be read
     * @return Byte read from the registry
     */
    public byte readOneByte(int registry) {
        byteToRead.clear();
        int result = -1;
        try {
            result = myDevice.read(registry, registrySize, byteToRead);
        } catch (IOException e) {
            System.out.println("Error reading byte");
        }
        if (result < 1) {
            System.out.println("Byte could not be read");
        } else {
            byteToRead.rewind();
            return byteToRead.get();
        }
        return 0;
    }

}
