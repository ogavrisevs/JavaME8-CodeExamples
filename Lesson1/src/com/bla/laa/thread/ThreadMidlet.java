/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bla.laa.thread;

import javax.microedition.midlet.MIDlet;

/**
 *
 * @author oskarsg
 */
public class ThreadMidlet extends MIDlet {
    private  SingleThread th1;
    private  SingleThread th2;
    
    @Override
    public void startApp() {
        System.out.println("Starting midlet . . . ");
        
        th1 = new SingleThread();
        th1.start();
        
        th2 = new SingleThread();
        th2.start();
    }
    
    @Override
    public void pauseApp() {
    }
    
    @Override
    public void destroyApp(boolean unconditional) {
        System.out.println("Stoping midlet . . . ");
        th1.destroy();
        th2.destroy();
        
    }
}
