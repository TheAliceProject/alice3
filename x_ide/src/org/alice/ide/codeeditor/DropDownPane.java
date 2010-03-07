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
package org.alice.ide.codeeditor;

/**
 * @author Dennis Cosgrove
 */
public abstract class DropDownPane extends org.alice.ide.common.AbstractDropDownPane {
	private java.awt.Component mainComponent;
	public DropDownPane( java.awt.Component prefixPane, java.awt.Component mainComponent, java.awt.Component postfixPane ) {
		this.setLayout( new javax.swing.BoxLayout( this, javax.swing.BoxLayout.LINE_AXIS ) );
		if( prefixPane != null ) {
			this.add( prefixPane );
		}
		this.mainComponent = mainComponent;
		this.add( this.mainComponent );
		if( postfixPane != null ) {
			this.add( postfixPane );
		}
	}
	
	public java.awt.Component getMainComponent() {
		return this.mainComponent;
	}
	

//	@Override
//	protected edu.cmu.cs.dennisc.awt.BeveledShape createBoundsShape() {
//		return new edu.cmu.cs.dennisc.awt.BeveledRectangle( new java.awt.geom.Rectangle2D.Float( 0, 0, getWidth()-1.0f, getHeight()-1.0f ) );
//	}
//	@Override
//	protected void paintBorder( java.awt.Graphics g ) {
//		super.paintBorder( g );
//		java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
//		java.awt.Color prev = g.getColor();
//		try {
//			final int INSET = 4;
//			int size = AFFORDANCE_SIZE;
//
//			int x0 = getWidth() - INSET/2 - AFFORDANCE_SIZE;
//			int x1 = x0 + size;
//			int xC = (x0 + x1) / 2;
//
//			
//			int y0 = INSET + 2;
//			int y1 = y0 + size;
//
//			java.awt.Color triangleFill;
//			java.awt.Color triangleOutline;
//			if( this.isActive() ) {
//				triangleFill = java.awt.Color.YELLOW;
//				triangleOutline = java.awt.Color.BLACK;
//			} else {
//				triangleFill = edu.cmu.cs.dennisc.awt.ColorUtilities.createGray( 192 );
//				triangleOutline = null;
//			}
//
//			g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
//
//			int[] xs = { x0, xC, x1 };
//			int[] ys = { y0, y1, y0 };
//			g.setColor( triangleFill );
//			g.fillPolygon( xs, ys, 3 );
//			if( triangleOutline != null ) {
//				g.setColor( triangleOutline );
//				g.drawPolygon( xs, ys, 3 );
//			}
//		} finally {
//			g.setColor( prev );
//		}
//	}
	
//	@Override
//	protected void paintActiveBorder( java.awt.Graphics2D g2 ) {
//		//super.paintActiveBorder( g2 );
//		g2.setStroke( new java.awt.BasicStroke( 3.0f ) );
//		g2.setColor( java.awt.Color.BLUE );
//		g2.draw( new java.awt.geom.Rectangle2D.Float( 1.5f, 1.5f, getWidth()-3.0f, getHeight()-3.0f ) );
//	}
}
