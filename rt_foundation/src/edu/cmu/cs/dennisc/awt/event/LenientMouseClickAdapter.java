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
package edu.cmu.cs.dennisc.awt.event;

/**
 * @author Dennis Cosgrove
 */
public abstract class LenientMouseClickAdapter implements java.awt.event.MouseListener, java.awt.event.MouseMotionListener {
	private static final long CLICK_THRESHOLD_MILLIS = 1000; //todo: query windowing system 
	private static final long CLICK_THRESHOLD_PIXELS_SQUARED = 25; //todo: query windowing system 
	private boolean isStillClick = false;
	private boolean isStillUnclick = false;
	private java.awt.event.MouseEvent ePressed = null;
	private java.awt.event.MouseEvent eReleased = null;
	private int count = 0;
	
	protected abstract void mouseQuoteClickedUnquote( java.awt.event.MouseEvent e, int quoteClickCountUnquote );
	
	private boolean isWithinThreshold( java.awt.event.MouseEvent eThen, java.awt.event.MouseEvent eNow ) {
		if( eThen != null ) {
			long whenDelta = eNow.getWhen() - eThen.getWhen();
			if( whenDelta < CLICK_THRESHOLD_MILLIS ) {
				int xDelta = eNow.getX() - eThen.getX();
				int yDelta = eNow.getY() - eThen.getY();
				int distanceSquared = xDelta*xDelta + yDelta*yDelta;
				return distanceSquared <= CLICK_THRESHOLD_PIXELS_SQUARED;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	private void updateStillClick( java.awt.event.MouseEvent eNow ) {
		if( this.isStillClick ) {
			this.isStillClick = this.isWithinThreshold( this.ePressed, eNow );
		}
	}
	private void updateStillUnclick( java.awt.event.MouseEvent eNow ) {
		if( this.isStillUnclick ) {
			this.isStillUnclick = this.isWithinThreshold( this.eReleased, eNow );
		}
	}
	public final void mouseEntered( java.awt.event.MouseEvent e ) {
	}
	public final void mouseExited( java.awt.event.MouseEvent e ) {
	}
	public final void mousePressed( java.awt.event.MouseEvent e ) {
		this.updateStillUnclick( e );
		if( this.isStillUnclick ) {
			//pass
		} else {
			this.count = 0; 
		}
		this.isStillClick = true;
		this.ePressed = e;
	}
	public final void mouseReleased( java.awt.event.MouseEvent e ) {
		this.updateStillClick( e );
		if( this.isStillClick ) {
			this.count++;
			this.mouseQuoteClickedUnquote( e, this.count );
		}
		this.isStillUnclick = true;
		this.eReleased = e;
	}
	public final void mouseClicked( java.awt.event.MouseEvent e ) {
	}
	public final void mouseMoved( java.awt.event.MouseEvent e ) {
		this.updateStillUnclick( e );
	}
	public final void mouseDragged( java.awt.event.MouseEvent e ) {
		this.updateStillClick( e );
	}
}
