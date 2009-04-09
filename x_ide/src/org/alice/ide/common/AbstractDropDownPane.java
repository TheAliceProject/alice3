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
package org.alice.ide.common;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractDropDownPane extends org.alice.ide.AbstractControl {
	public AbstractDropDownPane() {
		this.setBackground( edu.cmu.cs.dennisc.awt.ColorUtilities.createGray( 230 ) );
		zoot.event.ControlAdapter controlAdapter = new zoot.event.ControlAdapter( this );
		this.addMouseListener( controlAdapter );
		this.addMouseMotionListener( controlAdapter );
	}
	
	private static final int INSET = 1;
	private static final int AFFORDANCE_SIZE = 9;
	@Override
	protected int getInsetTop() {
		return AbstractDropDownPane.INSET;
	}
	@Override
	protected int getInsetLeft() {
		return AbstractDropDownPane.INSET;
	}
	@Override
	protected int getInsetBottom() {
		return AbstractDropDownPane.INSET;
	}
	@Override
	protected int getInsetRight() {
		return AbstractDropDownPane.INSET + AbstractDropDownPane.AFFORDANCE_SIZE;
	}
	@Override
	protected void fillBounds( java.awt.Graphics2D g2, int x, int y, int width, int height ) {
		g2.fillRect( x, y, width - 1, height - 1 );
	}
	@Override
	protected void paintPrologue( java.awt.Graphics2D g2, int x, int y, int width, int height ) {
		g2.setColor( java.awt.Color.WHITE );
		fillBounds( g2, x, y, width, height );
		
		final int INSET = 4;
		int size = AbstractDropDownPane.AFFORDANCE_SIZE;

		int x0 = width - INSET / 2 - AbstractDropDownPane.AFFORDANCE_SIZE;
		int x1 = x0 + size;
		int xC = (x0 + x1) / 2;

		int y0 = INSET + 2;
		int y1 = y0 + size;

		java.awt.Color triangleFill;
		java.awt.Color triangleOutline;
		if( this.isActive() ) {
			triangleFill = java.awt.Color.YELLOW;
			triangleOutline = java.awt.Color.BLACK;
		} else {
			triangleFill = edu.cmu.cs.dennisc.awt.ColorUtilities.createGray( 192 );
			triangleOutline = null;
		}

		g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );

		int[] xs = { x0, xC, x1 };
		int[] ys = { y0, y1, y0 };
		g2.setColor( triangleFill );
		g2.fillPolygon( xs, ys, 3 );
		if( triangleOutline != null ) {
			g2.setColor( triangleOutline );
			g2.drawPolygon( xs, ys, 3 );
		}
	}
	@Override
	protected void paintEpilogue( java.awt.Graphics2D g2, int x, int y, int width, int height ) {
		if( this.isActive() ) {
			g2.setStroke( new java.awt.BasicStroke( 3.0f ) );
			g2.setColor( java.awt.Color.BLUE );
			g2.draw( new java.awt.geom.Rectangle2D.Float( 1.5f, 1.5f, width-3.0f, height-3.0f ) );
		} else {
			g2.setColor( java.awt.Color.LIGHT_GRAY );
			g2.drawRect( x, y, width-1, height-1 );
		}
	}
}
