/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bla.laa.homework;

import java.util.Date;
import java.util.TimerTask;

/**
 *
 * @author oskarsg
 */
public class FirstThread extends TimerTask  {
    Utils util = new Utils();

    @Override
    public void run() {
        System.out.println("  Thread : "+ Thread.currentThread().getId() +" [ "+ util.dateFormatter(new Date()) + " ] ");
    }
}
