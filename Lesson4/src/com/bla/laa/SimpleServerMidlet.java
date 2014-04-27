/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bla.laa;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.microedition.midlet.MIDlet;

/**
 *
 * @author oskarsg
 */
public class SimpleServerMidlet extends MIDlet {
    
    @Override
    public void startApp() {
        System.out.println("Starting . . .");
        
        DataServer server;
        try {
            server = new DataServer(8080);
            server.setVerbose(true);
            server.setMessageLevel(3);
            new Thread(server).start();
            
        } catch (IOException ex) {
            Logger.getLogger(SimpleServerMidlet.class.getName()).log(Level.SEVERE, null, ex);
        }      
    }
    
    @Override
    public void pauseApp() {
    }
    
    @Override
    public void destroyApp(boolean unconditional) {
        System.out.println("Ending . . . ");
    }
}
