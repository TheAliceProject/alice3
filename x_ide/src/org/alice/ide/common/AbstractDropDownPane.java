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
		this.setCursor( java.awt.Cursor.getPredefinedCursor( java.awt.Cursor.DEFAULT_CURSOR ) );
	}
	
	private static final int AFFORDANCE_WIDTH = 6;
	private static final int AFFORDANCE_HALF_HEIGHT = 5;
	@Override
	protected int getInsetTop() {
//		if( this.isActive() || this.isInactiveFeedbackDesired() ) {
			return 2;
//		} else {
//			return 0;
//		}
	}
	@Override
	protected int getInsetLeft() {
		return 1;
	}
	@Override
	protected int getInsetBottom() {
//		if( this.isActive() || this.isInactiveFeedbackDesired() ) {
			return 2;
//		} else {
//			return 0;
//		}
	}
	@Override
	protected int getInsetRight() {
		return 5 + AbstractDropDownPane.AFFORDANCE_WIDTH;
	}
	
	protected boolean isInactiveFeedbackDesired() {
		return true;
	}
	@Override
	protected void fillBounds( java.awt.Graphics2D g2, int x, int y, int width, int height ) {
		g2.fillRect( x+1, y+1, width - 3, height - 3 );
	}
	@Override
	protected void paintPrologue( java.awt.Graphics2D g2, int x, int y, int width, int height ) {
		java.awt.Color prev = g2.getColor();
		if( this.isActive() || this.isInactiveFeedbackDesired() ) {
//			if( this.isActive() ) {
				g2.setColor( java.awt.Color.WHITE );
//			} else {
//				g2.setColor( new java.awt.Color( 255, 255, 255, 127 ) );
//			}
			this.fillBounds( g2, x, y, width, height );
		}
		
		float x0 = x + width - 4 - AbstractDropDownPane.AFFORDANCE_WIDTH;
		float x1 = x0 + AbstractDropDownPane.AFFORDANCE_WIDTH;
		float xC = (x0 + x1) / 2;

		float yC = (y + height)/2;
		float y0 = yC-AbstractDropDownPane.AFFORDANCE_HALF_HEIGHT;
		float y1 = yC+AbstractDropDownPane.AFFORDANCE_HALF_HEIGHT;

		java.awt.Color triangleFill;
		java.awt.Color triangleOutline;
		if( this.isActive() ) {
			triangleFill = java.awt.Color.YELLOW;
			triangleOutline = java.awt.Color.BLACK;
		} else {
			triangleFill = edu.cmu.cs.dennisc.awt.ColorUtilities.createGray( 191 );
			triangleOutline = null;
		}

		g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );

//		float[] xs = { x0, xC, x1 };
//		float[] ys = { y0, y1, y0 };
		
		java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
		path.moveTo( x0, y0 );
		path.lineTo( xC, y1 );
		path.lineTo( x1, y0 );
		path.closePath();
		
		
		g2.setColor( triangleFill );
		g2.fill( path );
//		g2.fillPolygon( xs, ys, 3 );
		if( triangleOutline != null ) {
			g2.setColor( triangleOutline );
			g2.draw( path );
//			g2.drawPolygon( xs, ys, 3 );
		}
		
		g2.setColor( prev );
	}
	@Override
	protected void paintEpilogue( java.awt.Graphics2D g2, int x, int y, int width, int height ) {
		if( this.isActive() ) {
			g2.setStroke( new java.awt.BasicStroke( 3.0f ) );
			g2.setColor( java.awt.Color.BLUE );
			g2.draw( new java.awt.geom.Rectangle2D.Float( 1.5f, 1.5f, width-3.0f, height-3.0f ) );
		} else {
			if( this.isInactiveFeedbackDesired() ) {
				g2.setColor( java.awt.Color.WHITE );
				//g2.drawRect( x, y, width-1, height-1 );
				g2.drawLine( x+1, y+1, x+width-4, y+1 );
				g2.drawLine( x+1, y+1, x+1, y+height-4 );
			}
		}
	}
}
