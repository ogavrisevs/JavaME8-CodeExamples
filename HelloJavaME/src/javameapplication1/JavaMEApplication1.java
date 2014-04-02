/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package javameapplication1;

import javax.microedition.midlet.MIDlet;

/**
 *
 * @author oskarsg
 */
public class JavaMEApplication1 extends MIDlet {
    
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
