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
package edu.cmu.cs.dennisc.swing;

/**
 * @author Dennis Cosgrove
 */
public class PaddedBoxLayout extends javax.swing.BoxLayout {
	private int pad;
	private int axis;
	public PaddedBoxLayout( java.awt.Container target, int axis, int pad ) {
		super( target, axis );
		this.axis = axis;
		this.pad = pad;
	}
	@Override
	public void layoutContainer( java.awt.Container target ) {
		super.layoutContainer( target );
		for( int i=0; i<target.getComponentCount(); i++ ) {
			java.awt.Component componentI = target.getComponent( i );
			java.awt.Point p = componentI.getLocation();
			//todo: handle right to left
			switch( this.axis ) {
			case Y_AXIS:
			case PAGE_AXIS:
				p.y += i*this.pad;
				break;
			case X_AXIS:
			case LINE_AXIS:
				p.x += i*this.pad;
				break;
			}
			componentI.setLocation( p );
		}
	}
}
