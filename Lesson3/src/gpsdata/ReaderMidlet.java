

package gpsdata;

import gpsdata.persist.RMSPersistentStore;
import javax.microedition.midlet.MIDlet;

public class ReaderMidlet extends MIDlet {
    RMSPersistentStore store;
    private final String STORE_NAME = "gps-store";
    @Override
    public void startApp() {
        System.out.println("ReaderMidlet starting reading");

        store = new RMSPersistentStore(STORE_NAME, false);
        for (int i = 0 ; i < store.getRecordCount(); i++){
            System.out.println(store.readData(i));
        }

        System.out.println("ReaderMidlet end reading");
    }
    
    @Override
    public void pauseApp() {
    }
    
    @Override
    public void destroyApp(boolean unconditional) {
        System.out.println("ReaderMidlet destroy");
    }
}
