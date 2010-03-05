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

package edu.cmu.cs.dennisc.lookingglass.opengl;

/**
 * @author Dennis Cosgrove
 */
abstract class Animator implements Runnable {
	public static final long DEFAULT_SLEEP_MILLIS = 5;
	private boolean isActive = false;
	private long tStart;
	private int frameCount;
	private long sleepMillis = DEFAULT_SLEEP_MILLIS;

	public enum ThreadDeferenceAction {
		DO_NOTHING, SLEEP, YIELD
	}

	public void start() {
		this.isActive = true;
		this.frameCount = 0;
		this.tStart = System.currentTimeMillis();
		//		javax.swing.SwingUtilities.invokeLater( this );
		new Thread( this ).start();
	}
	public void stop() {
		this.isActive = false;
		//long tDelta = System.currentTimeMillis() - this.tStart;
		//edu.cmu.cs.dennisc.print.PrintUtilities.println( this.frameCount, tDelta, this.frameCount/(tDelta*0.001) );
	}
	public int getFrameCount() {
		return this.frameCount;
	}
	public long getStartTimeMillis() {
		return this.tStart;
	}
	public long getSleepMillis() {
		return this.sleepMillis;
	}
	public void setSleepMillis( long sleepMillis ) {
		this.sleepMillis = sleepMillis;
	}

	protected abstract ThreadDeferenceAction step();
	public void run() {
		final long THRESHOLD = 5;
		long tPrev = System.currentTimeMillis() - THRESHOLD;
		while( this.isActive ) {
//			int i = 0;
			while( true ) {
				long tCurrent = System.currentTimeMillis();
				if( (tCurrent-tPrev) < THRESHOLD ) {
					edu.cmu.cs.dennisc.lang.ThreadUtilities.sleep( 5 );
//					i++;
				} else {
					tPrev = tCurrent;
					break;
				}
			}
//			if( i>3 ) {
//				edu.cmu.cs.dennisc.print.PrintUtilities.println( "sleep count:", i );
//			}
			//		if( this.isActive ) {
			//			try {
			ThreadDeferenceAction threadAction = this.step();
			if( threadAction == ThreadDeferenceAction.SLEEP ) {
				//edu.cmu.cs.dennisc.print.PrintUtilities.println( "sleep", this.sleepMillis );
				edu.cmu.cs.dennisc.lang.ThreadUtilities.sleep( this.sleepMillis );
			} else if( threadAction == ThreadDeferenceAction.YIELD ) {
				//edu.cmu.cs.dennisc.print.PrintUtilities.println( "yield" );
				Thread.yield();
			} else {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "threadAction", threadAction );
			}
			this.frameCount++;
			//			} finally {
			//				javax.swing.SwingUtilities.invokeLater( this );
			//			}
		}
	}
}
