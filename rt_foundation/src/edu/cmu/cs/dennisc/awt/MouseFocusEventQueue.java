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
public class MouseFocusEventQueue extends java.awt.EventQueue {
	private java.awt.Component componentWithMouseFocus = null;
	private static MouseFocusEventQueue singleton;
	public static MouseFocusEventQueue getSingleton() {
		if( singleton != null ) {
			//pass
		} else {
			singleton = new MouseFocusEventQueue();
			//todo
			java.awt.Toolkit.getDefaultToolkit().getSystemEventQueue().push( singleton );
		}
		return singleton;
	}
	public java.awt.Component getComponentWithMouseFocus() {
		return this.componentWithMouseFocus;
	}
	public void setComponentWithMouseFocus( java.awt.Component componentWithMouseFocus ) {
		this.componentWithMouseFocus = componentWithMouseFocus;
	}
	@Override
	protected void dispatchEvent( java.awt.AWTEvent e ) {
		if( this.componentWithMouseFocus != null ) {
			if( e instanceof java.awt.event.MouseEvent ) {
				java.awt.event.MouseEvent me = (java.awt.event.MouseEvent)e;
				java.awt.Component curr = me.getComponent();
				if( curr == this.componentWithMouseFocus ) {
					//pass
				} else {
					e = edu.cmu.cs.dennisc.swing.SwingUtilities.convertMouseEvent( curr, me, this.componentWithMouseFocus );
				}
			}
		}
//		edu.cmu.cs.dennisc.print.PrintUtilities.println( "dispatchEvent", e );
		super.dispatchEvent( e );
	}
}
