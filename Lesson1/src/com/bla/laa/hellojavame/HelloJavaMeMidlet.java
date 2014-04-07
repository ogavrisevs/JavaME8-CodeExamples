
package com.bla.laa.hellojavame;

import javax.microedition.midlet.MIDlet;

/**
 *
 * @author oskarsg
 */
public class HelloJavaMeMidlet extends MIDlet {
    
    @Override
    public void startApp() {
        System.out.println("Starting . . . ");
    }
    
    @Override
    public void pauseApp() {
    }
    
    @Override
    public void destroyApp(boolean unconditional) {
        System.out.println("Destroying . . . ");
    }
}
