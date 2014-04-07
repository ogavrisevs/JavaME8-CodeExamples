/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bla.laa.homework;

import java.util.Date;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author oskarsg
 */
public class SecondThread extends TimerTask{    
    Utils util = new Utils();
    int counnter = 0;
    
    @Override
    public void run() {
        while (10 > counnter) {
            System.out.println("  Thread : "+ Thread.currentThread().getId() +" [ "+ util.dateFormatter(new Date()) + " ] ");
            counnter++;
            try {
                Thread.currentThread().sleep(30 * 1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(SecondThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
 
}
