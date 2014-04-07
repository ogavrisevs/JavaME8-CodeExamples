/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bla.laa.homework;

import java.util.Date;
import java.util.Timer;
import javax.microedition.midlet.MIDlet;

/**
 *
 * @author oskarsg
 */
public class HomeWorkMidlet extends MIDlet {

    Utils util = new Utils();
    private Timer time1;
    private Timer time2;
    
    @Override
    public void startApp() {
        Date now = new Date();
        System.out.println(" Starting [ "+ util.dateFormatter(now) + " ] ");
        
        Date next = util.getNextPeriod(now);
        FirstThread t1 = new FirstThread();
        time1 = new Timer();
        time1.scheduleAtFixedRate(t1, next, (util.TIME_PREIOD * 60 * 1000));
        
        SecondThread t2 = new SecondThread();
        time2 = new Timer();
        time2.scheduleAtFixedRate(t2, util.addMins(next, util.TIME_PREIOD), (util.TIME_PREIOD * 60 * 1000 * 2));
        
    }
    
    @Override
    public void pauseApp() {
    }
    
    @Override
    public void destroyApp(boolean unconditional) {
        System.out.println(" Destroying [ "+ util.dateFormatter(new Date()) + " ] ");
        time1.cancel();
        time2.cancel();
        
    }    
}
