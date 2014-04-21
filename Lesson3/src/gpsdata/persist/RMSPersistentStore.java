/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gpsdata.persist;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotOpenException;
import mooc.data.gps.Position;
import mooc.data.gps.Velocity;

public class RMSPersistentStore implements PersistentStore{
    private RecordStore rs; 
    
    public RMSPersistentStore(String storeName, Boolean create){
        try { 
            rs = RecordStore.openRecordStore(storeName, create, RecordStore.AUTHMODE_ANY, true );
        } catch (RecordStoreException ex) {
            Logger.getLogger(RMSPersistentStore.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public int saveData(Position position, Velocity velocity) throws IOException {
        String dataStr = "Position and velocity: " + "^^" + position + "^"  + velocity ;
        int recordId = -1; 
        try {
            recordId = rs.addRecord(dataStr.getBytes(), 0, dataStr.getBytes().length);     
        } catch (RecordStoreException ex) {
            Logger.getLogger(RMSPersistentStore.class.getName()).log(Level.SEVERE, null, ex);
        }
        return recordId;
    }

    @Override
    public int getRecordCount() {
        try {
            return rs.getSize();
        } catch (RecordStoreNotOpenException ex) {
            Logger.getLogger(RMSPersistentStore.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    @Override
    public void close() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String readData(int id) {
        byte[] dateByt = null;
        String dateStr = "";
        try {
            dateByt = rs.getRecord(id);
            dateStr = new String(dateByt);
            
        } catch (RecordStoreException ex) {
            Logger.getLogger(RMSPersistentStore.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dateStr;
    }
    
}
