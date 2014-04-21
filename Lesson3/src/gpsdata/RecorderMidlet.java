package gpsdata;

import gpsdata.persist.PersistentStore;
import gpsdata.persist.RMSPersistentStore;
import gpsdata.sensor.AdaFruitGPSUARTSensor;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.microedition.midlet.MIDlet;
import static mooc.data.Messages.ERROR;
import mooc.data.gps.Position;
import mooc.data.gps.Velocity;

public class RecorderMidlet extends MIDlet {
    RMSPersistentStore store; 
    private final String STORE_NAME = "gps-store";
    

    @Override
    public void startApp() {
        System.out.println("RecorderMidlet starting");
        store = new RMSPersistentStore(STORE_NAME, true);
         
        try (AdaFruitGPSUARTSensor gps = new AdaFruitGPSUARTSensor()) {
        //try (AdaFruitGPSCommSensor gps = new AdaFruitGPSCommSensor(SERIAL_PORT)) {

            gps.setVerbose(true);
            gps.setMessageLevel(ERROR);
            int recId;
            
            for (int i = 0; i < 5; i++) {
                Position p = gps.getPosition();
                Velocity v = gps.getVelocity();
                recId = store.saveData(p, v);
                System.out.println("recId : " + recId);
                
                Thread.sleep(1000);
            }
 
        } catch (IOException ioe) {
            System.out.println("GPSTestMidlet: IOException " + ioe.getMessage());
            ioe.printStackTrace();
        } catch (InterruptedException ex) {
        } catch (Exception ex) {
            Logger.getLogger(RecorderMidlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("RecorderMidlet finished");
    }

    @Override
    public void pauseApp() {
    }

    @Override
    public void destroyApp(boolean unconditional) {
    }
}
