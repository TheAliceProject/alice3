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
package edu.cmu.cs.dennisc.awt;

/**
 * @author Dennis Cosgrove
 */
public class RedirectingEventQueue extends java.awt.EventQueue {
	private java.awt.Component src;
	private java.awt.Component dst;
	private java.awt.Component last;

	public RedirectingEventQueue( java.awt.Component src, java.awt.Component dst ) {
		this.src = src;
		this.dst = dst;
		this.last = null;
	}
	private static java.awt.Component getDeepestMouseListener( java.awt.Component dst, java.awt.Component descendant ) {
		java.awt.Component rv = descendant;
		while( rv != null ) {
			if( rv.getMouseListeners().length > 0 || rv.getMouseMotionListeners().length > 0 ) {
				break;
			}
			if( rv == dst ) {
				rv = null;
				break;
			}
		}
		return rv;
	}
	@Override
	protected void dispatchEvent( java.awt.AWTEvent e ) {
		if( e instanceof java.awt.event.MouseEvent ) {
			java.awt.event.MouseEvent me = (java.awt.event.MouseEvent)e;
			java.awt.Component curr = me.getComponent();
			int id = me.getID();
			if( curr == this.src ) {
				if( id == java.awt.event.MouseEvent.MOUSE_ENTERED || id == java.awt.event.MouseEvent.MOUSE_EXITED ) {
					e = edu.cmu.cs.dennisc.swing.SwingUtilities.convertMouseEvent( this.src, me, this.dst );
				} else if( id == java.awt.event.MouseEvent.MOUSE_MOVED ) {
					me = edu.cmu.cs.dennisc.swing.SwingUtilities.convertMouseEvent( this.src, me, dst );
					java.awt.Component descendant = javax.swing.SwingUtilities.getDeepestComponentAt( this.dst, me.getX(), me.getY() );
					descendant = RedirectingEventQueue.getDeepestMouseListener( dst, descendant );
					if( this.last != descendant ) {
						java.awt.Component exitComponent;
						if( this.last != null ) {
							exitComponent = this.last;
						} else {
							exitComponent = this.src;
						}
						java.awt.event.MouseEvent exitEvent = new java.awt.event.MouseEvent( exitComponent, java.awt.event.MouseEvent.MOUSE_EXITED, me.getWhen(), me.getModifiers(), me.getX(), me.getY(), me.getClickCount(), me.isPopupTrigger(), me.getButton() );
						exitEvent = edu.cmu.cs.dennisc.swing.SwingUtilities.convertMouseEvent( this.src, exitEvent, exitComponent );
						//edu.cmu.cs.dennisc.print.PrintUtilities.println( exitEvent );
						super.dispatchEvent( exitEvent );
					}
					if( descendant != null ) {
						e = edu.cmu.cs.dennisc.swing.SwingUtilities.convertMouseEvent( dst, me, descendant );
					}
					if( this.last != descendant ) {
						java.awt.Component enterComponent;
						if( descendant != null ) {
							enterComponent = descendant;
						} else {
							enterComponent = this.src;
						}
						java.awt.event.MouseEvent enterEvent = new java.awt.event.MouseEvent( enterComponent, java.awt.event.MouseEvent.MOUSE_ENTERED, me.getWhen(), me.getModifiers(), me.getX(), me.getY(), me.getClickCount(), me.isPopupTrigger(), me.getButton() );
						enterEvent = edu.cmu.cs.dennisc.swing.SwingUtilities.convertMouseEvent( this.src, enterEvent, enterComponent );
						//edu.cmu.cs.dennisc.print.PrintUtilities.println( enterEvent );
						super.dispatchEvent( enterEvent );
						this.last = descendant;
					}
				} else if( id == java.awt.event.MouseEvent.MOUSE_PRESSED || id == java.awt.event.MouseEvent.MOUSE_CLICKED || id == java.awt.event.MouseEvent.MOUSE_RELEASED || id == java.awt.event.MouseEvent.MOUSE_DRAGGED ) {
					if( this.last != null ) {
						e = edu.cmu.cs.dennisc.swing.SwingUtilities.convertMouseEvent( this.src, me, this.last );
					}
				}
			}
		}
		super.dispatchEvent( e );
	}
}
