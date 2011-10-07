package vdiskcore;

import vdiskcore.vdiskcore.vdiskListener;

public class vdiskProgress implements vdiskListener {
    public void vdiskReceived(vdiskEvent event) {
    	
    	//get the events
    	System.out.println(event.progress() + " | " + event.speed());

    }
} 
