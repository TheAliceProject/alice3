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
package zoot.event;

/**
 * @author Dennis Cosgrove
 */
public class ControlAdapter implements java.awt.event.MouseListener, java.awt.event.MouseMotionListener {
	private zoot.ZControl control;
	public ControlAdapter( zoot.ZControl control ) {
		this.control = control;
	}
	public void mousePressed( java.awt.event.MouseEvent e ) {
		this.control.handleMousePressed( e );
	}
	public void mouseReleased( java.awt.event.MouseEvent e ) {
		this.control.handleMouseReleased( e );
	}
	public void mouseClicked( java.awt.event.MouseEvent e ) {
		this.control.handleMouseClicked( e );
	}
	public void mouseEntered( java.awt.event.MouseEvent e ) {
		this.control.handleMouseEntered( e );
	}
	public void mouseExited( java.awt.event.MouseEvent e ) {
		this.control.handleMouseExited( e );
	}
	public void mouseMoved( java.awt.event.MouseEvent e ) {
		this.control.handleMouseMoved( e );
	}
	public void mouseDragged( java.awt.event.MouseEvent e ) {
		this.control.handleMouseDragged( e );
	}
}
