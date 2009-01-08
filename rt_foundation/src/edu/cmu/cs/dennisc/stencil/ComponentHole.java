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
package edu.cmu.cs.dennisc.stencil;

/**
 * @author Dennis Cosgrove
 */
public class ComponentHole implements Hole {
	private static java.awt.Rectangle s_buffer = new java.awt.Rectangle();

	private java.awt.Component component = null;
	private java.awt.geom.Area area = null;
	
	public ComponentHole( java.awt.Component component ) {
		this.component = component;
		this.component.addComponentListener( new java.awt.event.ComponentListener() {
			public void componentHidden( java.awt.event.ComponentEvent e ) {
				ComponentHole.this.area = null;
			}
			public void componentMoved( java.awt.event.ComponentEvent e ) {
				ComponentHole.this.area = null;
			}
			public void componentResized( java.awt.event.ComponentEvent e ) {
				ComponentHole.this.area = null;
			}
			public void componentShown( java.awt.event.ComponentEvent e ) {
				ComponentHole.this.area = null;
			}
		} );
	}

	public java.awt.Component getComponent() {
		return this.component;
	}
	
	//todo: handle root
	public java.awt.geom.Area getArea( java.awt.Container root ) {
		if( this.area != null ) {
			//pass
		} else {
			if( this.component != null && this.component.isShowing() ) {
				synchronized( s_buffer ) {
					this.area = new java.awt.geom.Area( this.component.getBounds( s_buffer ) );
				}
			} else {
				//pass
			}
		}
		return this.area;
	}
}
