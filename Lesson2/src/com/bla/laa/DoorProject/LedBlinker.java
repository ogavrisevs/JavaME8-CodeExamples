/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bla.laa.DoorProject;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author oskarsg
 */
public class LedBlinker implements Runnable{
    private GpioLed led; 
    private Boolean isThreadRunning = false;
            
    LedBlinker(GpioLed aLed){
        System.out.println("New blinker [ "+ getTid() + " ] ");
        this.led = aLed;
    }
    private long getTid(){
        return Thread.currentThread().getId();
    }
    
    public Boolean isLedBlinking(){
        return this.isThreadRunning;
    }
    
    public void stopBlinking(){
        this.isThreadRunning = false;    
    }
    
    @Override
    public void run() {
        this.isThreadRunning = true;
        while (this.isThreadRunning) {
            try {
                Boolean ledState = !led.getValue();
                led.setValue(ledState);
                System.out.println("Turn led [ " + led.getLedId() + " ] ["+ getTid() +"] in : " + ledState.toString());
                Thread.sleep(1000);
            } catch (IOException | InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println("Blink loop end [ " + led.getLedId() + " ] ["+ getTid() +"] ");
        try {
            led.setValue(false);
        } catch (IOException ex) {
            Logger.getLogger(LedBlinker.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.isThreadRunning = false;
        this.led = null;
    }

}
