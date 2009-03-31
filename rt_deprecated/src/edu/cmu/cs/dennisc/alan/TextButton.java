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

package edu.cmu.cs.dennisc.alan;

import edu.cmu.cs.dennisc.awt.ShapeUtilties;

/**
 * @author Dennis Cosgrove
 */
public class TextButton extends Button {
	public TextButton( String text ) {
		add( new TextLabel( text ) );
	}
	private float getBorderSize() {
		return Look.getBaseFontSize() * 0.4f;
	}
	@Override
	protected void updateLocalBounds() {
		super.updateLocalBounds();
		float borderSize = getBorderSize();
		m_localBounds.x -= borderSize;
		m_localBounds.y -= borderSize;
		m_localBounds.width += borderSize + borderSize;
		m_localBounds.height += borderSize + borderSize;
	}
	@Override
	protected void paintBackground( java.awt.Graphics2D g2, float x, float y, float width, float height ) {
		super.paintBackground( g2, x, y, width, height );
		
		float borderSize = getBorderSize();
		float xButton = x + borderSize;
		float yButton = y + borderSize;
		float widthButton = width - ( borderSize + borderSize );
		float heightButton = height - ( borderSize + borderSize );
		
		float roundButton = Math.min( widthButton, heightButton );
		
		java.awt.Shape shape = new java.awt.geom.RoundRectangle2D.Float( xButton, yButton, widthButton, heightButton, roundButton, roundButton );

		java.awt.Color clearColor = getClearColor();
		if( clearColor != null ) {
			//pass
		} else {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "WARNING: clearColor is null; using java.awt.Color.RED", this );
			clearColor = java.awt.Color.RED;
		}
		ShapeUtilties.paintBorder( g2, shape, clearColor, java.awt.Color.DARK_GRAY, (int)borderSize );
		//edu.cmu.cs.dennisc.toolkit.ShapeUtilties.paintBorder( g2, shape, new java.awt.Color( 63, 63, 63, 0 ), new java.awt.Color( 63, 63, 63, 255 ), (int)borderSize );

		final java.awt.Color BASE = new java.awt.Color( 201, 213, 230 );
		java.awt.Paint paint;
		if( isPressed() ) {
			//paint = edu.cmu.cs.dennisc.color.ColorUtilities.shiftHSB( BASE, 0, 0, -0.25 );
			paint = new java.awt.GradientPaint( xButton, yButton, edu.cmu.cs.dennisc.color.ColorUtilities.shiftHSB( BASE, 0, 0, -0.5 ), xButton, yButton + heightButton, edu.cmu.cs.dennisc.color.ColorUtilities.shiftHSB( BASE, 0, 0, -0.25 ) );
		} else {
			if( isHighlighted() ) {
				paint = new java.awt.GradientPaint( xButton, yButton, new java.awt.Color( 255, 255, 191 ), xButton, yButton + heightButton, BASE );
			} else {
				paint = BASE;
			}
		}
		g2.setPaint( paint );
		g2.fill( shape );
	}
}
