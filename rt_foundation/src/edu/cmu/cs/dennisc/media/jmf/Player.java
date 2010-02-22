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
package edu.cmu.cs.dennisc.media.jmf;

abstract class BarrierControllerListener implements javax.media.ControllerListener {
	private java.util.concurrent.CyclicBarrier barrier = new java.util.concurrent.CyclicBarrier( 2 );
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

//class StateControllerListener extends BarrierControllerListener {
//	private int targetState;
//	public StateControllerListener( int targetState ) {
//		this.targetState = targetState;
//	}
//	public void controllerUpdate( javax.media.ControllerEvent e ) {
//		edu.cmu.cs.dennisc.print.PrintUtilities.println( e );
//		if( e instanceof javax.media.TransitionEvent ) {
//			javax.media.TransitionEvent transitionEvent = (javax.media.TransitionEvent)e;
//			int currentState = transitionEvent.getCurrentState();
//			if( currentState >= this.targetState ) {
//				this.await();
//			}
//		}
//	}
//	@Override
//	public void await() {
//		edu.cmu.cs.dennisc.print.PrintUtilities.println( "awaiting:", this.targetState );
//		super.await();
//	}
//}

class PrefetchControllerListener extends BarrierControllerListener {
	public void controllerUpdate( javax.media.ControllerEvent e ) {
		if( e instanceof javax.media.PrefetchCompleteEvent ) {
			this.await();
		}
	}
}
class RealizeControllerListener extends BarrierControllerListener {
	public void controllerUpdate( javax.media.ControllerEvent e ) {
		if( e instanceof javax.media.RealizeCompleteEvent ) {
			this.await();
		}
	}
}
class StopControllerListener extends BarrierControllerListener {
	public void controllerUpdate( javax.media.ControllerEvent e ) {
		//todo?
		if( e instanceof javax.media.StopEvent ) {
			this.await();
		}
	}
}

//class ScaledTimeBase implements javax.media.TimeBase {
//	private javax.media.TimeBase originalTimeBase;
//	private long scale;
//	public ScaledTimeBase( javax.media.TimeBase originalTimeBase ) {
//		this.originalTimeBase = originalTimeBase;
//	}
//	public long getNanoseconds() {
//		edu.cmu.cs.dennisc.print.PrintUtilities.println( "getNanoseconds" );
//		return this.scale * this.originalTimeBase.getNanoseconds();
//	}
//	public javax.media.Time getTime() {
//		//todo?
//		return new javax.media.Time( this.getNanoseconds() );
//	}
//}

/**
 * @author Dennis Cosgrove
 */
public class Player extends edu.cmu.cs.dennisc.media.Player {
	private javax.media.Player player;
	private double volumeLevel;
	private double startTime;
	private double stopTime;
	/*package private*/ Player( javax.media.Player player, double volumeLevel, double startTime, double stopTime ) {
		//assert player.getState() >= javax.media.Controller.Realized;
		this.player = player;
		this.volumeLevel = volumeLevel;
		this.startTime = startTime;
		this.stopTime = stopTime;
	}
	@Override
	public void realize() {
		if( this.player.getState() < javax.media.Controller.Realized ) {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "realize: ", this.player.getState() );
			RealizeControllerListener controllerListener = new RealizeControllerListener();
			this.player.addControllerListener( controllerListener );
			this.player.realize();
			controllerListener.await();
			this.player.removeControllerListener( controllerListener );
		}
	}
	@Override
	public void prefetch() {
		if( this.player.getState() < javax.media.Controller.Prefetched ) {
			PrefetchControllerListener controllerListener = new PrefetchControllerListener();
			this.player.addControllerListener( controllerListener );
			this.player.prefetch();
			controllerListener.await();
			this.player.removeControllerListener( controllerListener );
		}
	}
	@Override
	public void start() {
		this.realize();
		if( Double.isNaN( this.startTime ) ) {
			//pass
		} else {
			this.player.setMediaTime( new javax.media.Time( this.startTime ) );
		}
		if( Double.isNaN( this.stopTime ) ) {
			//pass
		} else {
			this.player.setStopTime( new javax.media.Time( this.stopTime ) );
		}
		if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( this.volumeLevel, 1.0 ) ) {
			//pass
		} else {
			javax.media.GainControl gainControl = this.player.getGainControl();
			float defaultVolumeLevel = gainControl.getLevel();
			
			float v = (float)( this.volumeLevel * defaultVolumeLevel );
			v = Math.max( v, 0.0f );
			v = Math.min( v, 1.0f );
			gainControl.setLevel( v );
		}
		this.player.start();
	}
	@Override
	public void stop() {
		this.player.stop();
	}
	
	@Override
	public double getDuration() {
		return this.player.getDuration().getSeconds();
	}
	@Override
	public double getTimeRemaining() {
		javax.media.Time duration = this.player.getDuration();
		javax.media.Time stop = this.player.getStopTime();
		javax.media.Time curr = this.player.getMediaTime();
		return Math.min( duration.getSeconds(), stop.getSeconds() ) - curr.getSeconds();
	}
	@Override
	public void playUntilStop() {
		StopControllerListener controllerListener = new StopControllerListener();
		this.player.addControllerListener( controllerListener );
		this.start();
		controllerListener.await();
		this.player.removeControllerListener( controllerListener );
	}

	@Override
	public java.awt.Component getControlPanelComponent() {
		this.realize();
		return this.player.getControlPanelComponent();
	}
	@Override
	public java.awt.Component getVisualComponent() {
		this.realize();
		return this.player.getVisualComponent();
	}
	
	@Override
	public void test( java.awt.Component owner ) {
		edu.cmu.cs.dennisc.croquet.swing.BorderPane content = new edu.cmu.cs.dennisc.croquet.swing.BorderPane() {
			@Override
			public java.awt.Dimension getPreferredSize() {
				return edu.cmu.cs.dennisc.awt.DimensionUtilties.constrainToMinimumWidth( super.getPreferredSize(), 320 );
			}
		};
		
		final javax.swing.JDialog dialog = edu.cmu.cs.dennisc.swing.JDialogUtilities.createJDialog( owner, "test", true );
		dialog.getContentPane().add( content, java.awt.BorderLayout.CENTER );
		
		java.awt.Component controlPanelComponent = this.getControlPanelComponent();
		if( controlPanelComponent != null ) {
			content.add( controlPanelComponent, java.awt.BorderLayout.SOUTH );
		}
		java.awt.Component visualComponent = this.getVisualComponent();
		if( visualComponent != null ) {
			content.add( visualComponent, java.awt.BorderLayout.CENTER );
		}
		dialog.pack();

		edu.cmu.cs.dennisc.awt.WindowUtilties.setLocationOnScreenToCenteredWithin( dialog, owner );

		new Thread() {
			@Override
			public void run() {
				playUntilStop();
				dialog.setVisible( false );
			}
		}.start();
		dialog.setVisible( true );
		this.stop();
	}
	
}
