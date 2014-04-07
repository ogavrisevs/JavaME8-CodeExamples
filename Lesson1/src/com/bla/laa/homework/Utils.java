/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bla.laa.homework;

import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;

/**
 *
 * @author oskarsg
 */
public class Utils {

    Calendar cal = Calendar.getInstance();
    int TIME_PREIOD = 2; //in min
        
    public Date addMins(Date date, int mins){
        cal.setTime(date);
        cal.add(Calendar.MINUTE, mins);
        return cal.getTime();
    }
    
    
    public Date getNextPeriod(Date date){
        cal.setTime(date);
        long mins = cal.get(Calendar.MINUTE);
        int periods=  (int) (mins / TIME_PREIOD); 
        cal.set(Calendar.MINUTE, ((periods + 1) * TIME_PREIOD) );
        cal.set(Calendar.SECOND, 0 );
        cal.set(Calendar.MILLISECOND, 0 );

        return cal.getTime();
    }

    public String dateFormatter(Date date) {
        cal.clear();
        cal.setTime(date);

        long hour = cal.get(Calendar.HOUR_OF_DAY);
        long mins = cal.get(Calendar.MINUTE);
        long second = cal.get(Calendar.SECOND);
        long millisecond = cal.get(Calendar.MILLISECOND);

        Formatter format = new Formatter();
        format.format("%s:", String.valueOf(hour));
        format.format("%s:", String.valueOf(mins));
        format.format("%s:", String.valueOf(second));
        format.format("%s", String.valueOf(millisecond));

        return format.toString();
    }
}
