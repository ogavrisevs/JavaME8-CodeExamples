/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bla.laa.runnable;

import javax.microedition.midlet.MIDlet;

/**
 *
 * @author oskarsg
 */
public class RunnableMidlet extends MIDlet {
    
    private Thread th1;
    private Thread th2;
    
    @Override
    public void startApp() {
        System.out.println("Starting midlet . . . ");
        
        Task tk1 = new Task();
        th1 = new Thread ( tk1, "myTask1");
        th1.start();
        
        Task tk2 = new Task();
        th2 = new Thread ( tk2, "myTask2");
        th2.start();
        
    }
    
    @Override
    public void pauseApp() {
    }
    
    @Override
    public void destroyApp(boolean unconditional) {
        System.out.println("Stoping midlet . . . ");
    }
}
