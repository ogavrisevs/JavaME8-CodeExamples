/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bla.laa.runnable;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author oskarsg
 */
public class Task implements Runnable {
    private int counnter = 0;
   
    Task(){
        System.out.println("Task constructor ");    
    }
    
    @Override
    public void run() {
        while (10 > counnter){
            System.out.println("Running task in : "+ Thread.currentThread().getName() );
            counnter ++;
            try {
                Thread.sleep(4000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Task.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    

}
