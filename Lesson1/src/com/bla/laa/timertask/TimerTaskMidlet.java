/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bla.laa.timertask;

import java.util.Date;
import java.util.Timer;
import javax.microedition.midlet.MIDlet;

/**
 *
 * @author oskarsg
 */
public class TimerTaskMidlet extends MIDlet {
    
    TimerTaskRunnable ttr1;
    TimerTaskRunnable ttr2;
            
    @Override
    public void startApp() {
        System.out.println("Starting midlet . . . "+ new Date() );
        
        Timer t1 = new Timer();
        t1.schedule(new TimerTaskRunnable(), 3000);
        
        Timer t2 = new Timer();
        t2.schedule(new TimerTaskRunnable(), 6000, 5000);

    }
    
    @Override
    public void pauseApp() {
    }
    
    @Override
    public void destroyApp(boolean unconditional) {
        System.out.println("Destroying . . .");
    }
}
