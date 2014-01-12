package com.stubborneagle.AudioVideoFromWebcam;

import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Player;
import javax.media.Processor;
import javax.media.TransitionEvent;

public class RTPReceiver implements ControllerListener {
	Player player = null;
	
	@SuppressWarnings("unused")
	public static void main(String args[]){
		RTPReceiver rtprec =new RTPReceiver();
		try {
			Thread.sleep(60000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		rtprec.player.stop();
		System.exit(0);
	}
	
	public RTPReceiver() {
	    MediaLocator mrl= new MediaLocator("rtp://192.168.120.1:52040/audio/1");	  

	    // Create a player for this rtp session
	    try {
	        player = Manager.createPlayer(mrl);
	    } catch (Exception e) {
	        System.err.println("Error:" + e);
	        return;
	    }

	    if (player != null) {
	        player.addControllerListener(this);
	        player.realize();
	    }
	}

	public synchronized void controllerUpdate(ControllerEvent ce) {
	    System.out.println(ce);
	    if(ce instanceof TransitionEvent) {
	        if (((TransitionEvent)ce).getCurrentState() == Processor.Realized) {
	            player.start();
	            System.out.println("starting player now");
	        }
	    }
	}
}
