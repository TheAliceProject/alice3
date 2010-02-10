/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package edu.cmu.cs.dennisc.media;

/**
 * @author Dennis Cosgrove
 */
public class Player {
	private javax.media.Player player;
	public Player( javax.media.Player player ) {
		this.player = player;
	}
	public void prefetch() {
		final java.util.concurrent.CyclicBarrier barrier = new java.util.concurrent.CyclicBarrier( 2 );
		javax.media.ControllerListener controllerListener = new javax.media.ControllerListener() {
			public void controllerUpdate( javax.media.ControllerEvent e ) {
				if( e instanceof javax.media.TransitionEvent ) {
					javax.media.TransitionEvent transitionEvent = (javax.media.TransitionEvent)e;
					int currentState = transitionEvent.getCurrentState();
					int targetState = transitionEvent.getTargetState();
					if( currentState == targetState ) {
            			try {
        					barrier.await();
            			} catch( InterruptedException ie ) {
            				throw new RuntimeException( ie );
            			} catch( java.util.concurrent.BrokenBarrierException bbe ) {
            				throw new RuntimeException( bbe );
            			}
					}
				}
			}
		};
		this.player.addControllerListener( controllerListener );
		this.player.prefetch();
		try {
			barrier.await();
		} catch( InterruptedException ie ) {
			throw new RuntimeException( ie );
		} catch( java.util.concurrent.BrokenBarrierException bbe ) {
			throw new RuntimeException( bbe );
		}
		this.player.removeControllerListener( controllerListener );
	}
	public void start() {
		this.player.start();
	}
	public void stop() {
		this.player.stop();
	}
	
	public double getTimeRemaining() {
		javax.media.Time duration = this.player.getDuration();
		javax.media.Time time = this.player.getMediaTime();
		return duration.getSeconds() - time.getSeconds();
	}
}
