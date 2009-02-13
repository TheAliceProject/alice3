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
package edu.cmu.cs.dennisc.alice.ide.editors.code;

/**
 * @author Dennis Cosgrove
 */
public class GetsPane extends edu.cmu.cs.dennisc.zoot.ZPane {
	private boolean isTowardLeadingEdge;
	private int length;
	public GetsPane( boolean isTowardLeadingEdge, int length ) {
		//this.setLocale( new java.util.Locale( "ar" ) );
		this.isTowardLeadingEdge = isTowardLeadingEdge;
		this.length = length;
		this.setBackground( java.awt.Color.YELLOW );
		this.setForeground( java.awt.Color.BLACK );
	}
	private float getArrowHeight() {
		return getFont().getSize2D() * 2.0f;
	}
	@Override
	public java.awt.Dimension getPreferredSize() {
		int height = (int)getArrowHeight();
		int width = height*this.length+1;
		return new java.awt.Dimension( width, height );
	}
	@Override
	protected void paintComponent( java.awt.Graphics g ) {
		super.paintComponent( g );
		int halfLineSize = getHeight()/10;
		int yTop = 0;
		int yBottom = getHeight()-1;
		int yCenter = ( yTop + yBottom ) / 2;
		int yTopLine = yCenter - halfLineSize;
		int yBottomLine = yCenter + halfLineSize;
		
		
		final int INSET = 2;
		int xLeft = INSET;
		int xHeadRight = yBottom;
		int xHeadRightInABit = xHeadRight*9/10;
		int xRight = getWidth()-1-INSET*2;

		int[] xPoints = { xLeft,   xHeadRight, xHeadRightInABit, xRight,   xRight,      xHeadRightInABit,  xHeadRight };
		int[] yPoints = { yCenter, yTop,       yTopLine,         yTopLine, yBottomLine, yBottomLine,       yBottom };
		
		boolean isReverseDesired;
		java.awt.ComponentOrientation componentOrientation = java.awt.ComponentOrientation.getOrientation( this.getLocale() );
		if( componentOrientation.isLeftToRight() ) {
			isReverseDesired = isTowardLeadingEdge == false;
		} else {
			isReverseDesired = isTowardLeadingEdge;
		}
		if( isReverseDesired ) {
			for( int i=0; i<xPoints.length; i++ ) {
				xPoints[ i ] = getWidth()-xPoints[ i ];
			}
		}
		edu.cmu.cs.dennisc.awt.GraphicsUtilties.setRenderingHint( g, java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
		g.setColor( this.getBackground() );
		g.fillPolygon( xPoints, yPoints, xPoints.length );
		g.setColor( this.getForeground() );
		g.drawPolygon( xPoints, yPoints, xPoints.length );
	}
	public static void main( String[] args ) {
		javax.swing.JComponent.setDefaultLocale( new java.util.Locale( "ar" ) );
		javax.swing.JFrame frame = new javax.swing.JFrame();
		frame.getContentPane().add( new GetsPane( true, 5 ) );
		frame.setDefaultCloseOperation( javax.swing.JFrame.EXIT_ON_CLOSE );
		frame.setSize( 640, 480 );
		frame.setVisible( true );
	}
}
