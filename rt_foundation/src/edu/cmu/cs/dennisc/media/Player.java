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

class StateControllerListener implements javax.media.ControllerListener {
	private java.util.concurrent.CyclicBarrier barrier = new java.util.concurrent.CyclicBarrier( 2 );
	public void controllerUpdate( javax.media.ControllerEvent e ) {
		if( e instanceof javax.media.TransitionEvent ) {
			javax.media.TransitionEvent transitionEvent = (javax.media.TransitionEvent)e;
			int currentState = transitionEvent.getCurrentState();
			int targetState = transitionEvent.getTargetState();
			if( currentState == targetState ) {
				this.await();
			}
		}
	}
	public void await() {
		try {
			barrier.await();
		} catch( InterruptedException ie ) {
			throw new RuntimeException( ie );
		} catch( java.util.concurrent.BrokenBarrierException bbe ) {
			throw new RuntimeException( bbe );
		}
	}
}

/**
 * @author Dennis Cosgrove
 */
public class Player {
	private javax.media.Player player;
	private double fromTime;
	private double toTime;
	public Player( javax.media.Player player, double fromTime, double toTime ) {
		this.player = player;
		this.fromTime = fromTime;
		this.toTime = toTime;
	}
	public void prefetch() {
		StateControllerListener controllerListener = new StateControllerListener();
		this.player.addControllerListener( controllerListener );
		this.player.prefetch();
		controllerListener.await();
		this.player.removeControllerListener( controllerListener );
	}
	public void realize() {
		StateControllerListener controllerListener = new StateControllerListener();
		this.player.addControllerListener( controllerListener );
		this.player.realize();
		controllerListener.await();
		this.player.removeControllerListener( controllerListener );
	}
	public void start() {
		this.realize();
		if( Double.isNaN( this.fromTime ) ) {
			//pass
		} else {
			this.player.setMediaTime( new javax.media.Time( this.fromTime ) );
		}
		if( Double.isNaN( this.toTime ) ) {
			//pass
		} else {
			this.player.setStopTime( new javax.media.Time( this.toTime ) );
		}
		this.player.start();
	}
	public void stop() {
		this.player.stop();
	}
	
	public double getTimeRemaining() {
		javax.media.Time duration = this.player.getDuration();
		javax.media.Time stop = this.player.getStopTime();
		javax.media.Time curr = this.player.getMediaTime();
		return Math.min( duration.getSeconds(), stop.getSeconds() ) - curr.getSeconds();
	}
}
