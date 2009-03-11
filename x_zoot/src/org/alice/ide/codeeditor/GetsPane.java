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
public class GetsPane extends edu.cmu.cs.dennisc.moot.ZPane {
	private boolean isTowardLeadingEdge;
	private int length;

	private GetsPane( boolean isTowardLeadingEdge, int length ) {
		//this.setLocale( new java.util.Locale( "ar" ) );
		this.isTowardLeadingEdge = isTowardLeadingEdge;
		this.length = length;
		this.setBackground( java.awt.Color.YELLOW );
		this.setForeground( java.awt.Color.GRAY );
	}
	public GetsPane( boolean isTowardLeadingEdge ) {
		this( isTowardLeadingEdge, 2 );
	}
	private float getArrowHeight() {
		return getFont().getSize2D() * 1.4f;
	}
	@Override
	public java.awt.Dimension getPreferredSize() {
		int height = (int)getArrowHeight();
		int width = height * this.length + 1;
		return new java.awt.Dimension( width, height );
	}

	private boolean isReversalDesired() {
		java.awt.ComponentOrientation componentOrientation = java.awt.ComponentOrientation.getOrientation( this.getLocale() );
		if( componentOrientation.isLeftToRight() ) {
			return isTowardLeadingEdge == false;
		} else {
			return isTowardLeadingEdge;
		}
	}
	private java.awt.Paint createGradientPaint( int width ) {
		java.awt.Color colorStart = org.alice.ide.IDE.getColorForASTClass( edu.cmu.cs.dennisc.alice.ast.ExpressionStatement.class );
		//java.awt.Color colorStart = java.awt.Color.ORANGE;
		java.awt.Color colorEnd = colorStart.darker();
		java.awt.Color color0;
		java.awt.Color color1;
		if( this.isReversalDesired() ) {
			color0 = colorEnd;
			color1 = colorStart;
		} else {
			color0 = colorStart;
			color1 = colorEnd;
		}
		java.awt.Paint rv = new java.awt.GradientPaint( 0.0f, 0.0f, color0, width, 0.0f, color1 );
		return rv;
	}
	private int prevWidth = -1; 
	private java.awt.Paint gradientPaint = null;
	private java.awt.Paint getGradientPaint() {
		int width = this.getWidth();
		if( width != this.prevWidth ) {
			this.gradientPaint = this.createGradientPaint( width );
			this.prevWidth = width;
		}
		return this.gradientPaint;
	}
	
	@Override
	protected void paintComponent( java.awt.Graphics g ) {
		super.paintComponent( g );
		int halfLineSize = getHeight() / 5;
		int yTop = 0;
		int yBottom = getHeight() - 1;
		int yCenter = (yTop + yBottom) / 2;
		int yTopLine = yCenter - halfLineSize;
		int yBottomLine = yCenter + halfLineSize;

		final int INSET = 2;
		int xLeft = INSET;
		int xHeadRight = yBottom;
		int xHeadRightInABit = xHeadRight * 4 / 5;
		int xRight = getWidth() - 1 - INSET * 2;

		int[] xPoints = { xLeft, xHeadRight, xHeadRightInABit, xRight, xRight, xHeadRightInABit, xHeadRight };
		int[] yPoints = { yCenter, yTop, yTopLine, yTopLine, yBottomLine, yBottomLine, yBottom };

		if( this.isReversalDesired() ) {
			for( int i = 0; i < xPoints.length; i++ ) {
				xPoints[ i ] = getWidth() - xPoints[ i ];
			}
		}
		edu.cmu.cs.dennisc.awt.GraphicsUtilties.setRenderingHint( g, java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );

		java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
		g2.setPaint( this.getGradientPaint() );

		g.fillPolygon( xPoints, yPoints, xPoints.length );
		g.setColor( this.getForeground() );
		g.drawPolygon( xPoints, yPoints, xPoints.length );
	}
	public static void main( String[] args ) {
		javax.swing.JComponent.setDefaultLocale( new java.util.Locale( "ar" ) );
		javax.swing.JFrame frame = new javax.swing.JFrame();
		frame.getContentPane().add( new GetsPane( false ) );
		frame.setDefaultCloseOperation( javax.swing.JFrame.EXIT_ON_CLOSE );
		frame.setSize( 640, 480 );
		frame.setVisible( true );
	}
}
