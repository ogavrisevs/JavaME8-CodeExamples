/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bla.laa.thread;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author oskarsg
 */
public class SingleThread extends Thread{
    private int counter = 0 ;
    private Boolean terminateThread = Boolean.FALSE;
    
    @Override
    public void run(){
        while (counter < 10 | !terminateThread ){
            System.out.println("Running thread : "+ this.getName());
            counter ++;
            try {
                sleep(3000);
            } catch (InterruptedException ex) {
                Logger.getLogger(SingleThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }                
    }
    
    public void destroy(){
        System.out.println("Destroying thread : "+ this.getName());
        terminateThread = Boolean.TRUE;
    }
   
}
