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
package edu.cmu.cs.dennisc.awt.geom;

/**
 * @author Dennis Cosgrove
 */
public class Text extends Transformable {
	private String m_text;
	private java.awt.Font m_font;
	private boolean m_isFiguredIntoAreaCalculation = false;

	public Text( String text, java.awt.Font font ) {
		m_text = text;
		m_font = font;
	}
	public boolean isFiguredIntoAreaCalculation() {
		return m_isFiguredIntoAreaCalculation;
	}
	public void setFiguredIntoAreaCalculation( boolean isFiguredIntoAreaCalculation ) {
		m_isFiguredIntoAreaCalculation = isFiguredIntoAreaCalculation;
	}	

	@Override
	protected void paintComponent( edu.cmu.cs.dennisc.awt.geom.GraphicsContext gc ) {
		java.awt.Graphics2D g2 = gc.getAWTGraphics2D();
		g2.setFont( m_font );

		//todo:
		java.awt.geom.Rectangle2D bounds = g2.getFontMetrics().getStringBounds( m_text, g2 );
		
		g2.setPaint( java.awt.Color.BLACK );
		g2.drawString( m_text, (float)-bounds.getCenterX(), (float)-bounds.getCenterY() );
	}

	@Override
	protected java.awt.geom.Area update( java.awt.geom.Area rv, edu.cmu.cs.dennisc.awt.geom.TransformContext tc ) {
		if( m_isFiguredIntoAreaCalculation ) {
			throw new RuntimeException( "TODO" );
		} else {
			return rv;
		}
	}
	
}
