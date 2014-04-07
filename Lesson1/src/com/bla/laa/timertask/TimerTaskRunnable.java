/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bla.laa.timertask;

import com.bla.laa.runnable.Task;
import java.util.Date;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author oskarsg
 */
class TimerTaskRunnable extends TimerTask {
    private int counnter = 0 ; 
    
    @Override
    public void run() {
        while (5 > counnter) {
            System.out.println("Running task in : " + Thread.currentThread().getId() + " : "+ new Date());
            counnter++;
            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(TimerTaskRunnable.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
