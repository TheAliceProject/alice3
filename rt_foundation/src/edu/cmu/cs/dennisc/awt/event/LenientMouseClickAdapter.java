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
	protected abstract void mouseQuoteClickedUnquote( java.awt.event.MouseEvent e );
	private boolean isWithinThreshold = false;
	private int xPressed = -1;
	private int yPressed = -1;
	private long whenPressed = -1;
	private void updateWithinThreshold( java.awt.event.MouseEvent e ) {
		int xDelta = e.getX() - xPressed;
		int yDelta = e.getY() - yPressed;
		int distanceSquared = xDelta*xDelta + yDelta*yDelta;
		if( distanceSquared >= 25 ) {
			this.isWithinThreshold = false;
		}
	}
	public final void mouseEntered( java.awt.event.MouseEvent e ) {
	}
	public final void mouseExited( java.awt.event.MouseEvent e ) {
	}
	public final void mousePressed( java.awt.event.MouseEvent e ) {
		this.isWithinThreshold = true;
		this.xPressed = e.getX();
		this.yPressed = e.getY();
		this.whenPressed = e.getWhen();
	}
	public final void mouseReleased( java.awt.event.MouseEvent e ) {
		this.updateWithinThreshold( e );
		long whenDelta = e.getWhen() - this.whenPressed;
		if( this.isWithinThreshold && whenDelta < 500 ) {
			this.mouseQuoteClickedUnquote( e );
		}
	}
	public final void mouseClicked( java.awt.event.MouseEvent e ) {
	}
	public final void mouseMoved( java.awt.event.MouseEvent e ) {
	}
	public final void mouseDragged( java.awt.event.MouseEvent e ) {
		this.updateWithinThreshold( e );
	}
}
