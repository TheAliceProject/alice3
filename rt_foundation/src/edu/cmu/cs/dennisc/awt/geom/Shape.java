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
public abstract class Shape extends Transformable {
	private java.awt.Paint m_fillPaint = java.awt.Color.WHITE;
	private java.awt.Paint m_drawPaint = java.awt.Color.BLACK;
	private boolean m_isFilled = true;
	private boolean m_isDrawn = true;

	public boolean isFilled() {
		return m_isFilled;
	}
	public void setFilled( boolean isFilled ) {
		m_isFilled = isFilled;
	}
	public boolean isDrawn() {
		return m_isDrawn;
	}
	public void setDrawn( boolean isDrawn ) {
		m_isDrawn = isDrawn;
	}

	public java.awt.Paint getDrawPaint() {
		return m_drawPaint;
	}
	public void setDrawPaint( java.awt.Paint drawPaint ) {
		m_drawPaint = drawPaint;
	}
	public java.awt.Paint getFillPaint() {
		return m_fillPaint;
	}
	public void setFillPaint( java.awt.Paint fillPaint ) {
		m_fillPaint = fillPaint;
	}

	protected abstract java.awt.Shape getFillShape();
	protected abstract java.awt.Shape getDrawShape();

	@Override
	protected void paintComponent( GraphicsContext gc ) {
		java.awt.Graphics2D g2 = gc.getAWTGraphics2D();

		if( isFilled() ) {
			java.awt.Shape fillShape = getFillShape();
			if( fillShape != null ) {
				g2.setPaint( m_fillPaint );
				g2.fill( fillShape );
			}
		}
		if( isDrawn() ) {
			java.awt.Shape drawShape = getDrawShape();
			if( drawShape != null ) {
				g2.setPaint( m_drawPaint );
				g2.draw( drawShape );
			}
		}
	}

	@Override
	protected java.awt.geom.Area update( java.awt.geom.Area rv, edu.cmu.cs.dennisc.awt.geom.TransformContext tc ) {
		if( isFilled() ) {// TODO Auto-generated method stub
			java.awt.Shape fillShape = getFillShape();
			if( fillShape != null ) {
				java.awt.geom.Area area = new java.awt.geom.Area( fillShape );
				area.transform( tc.getAffineTransform() );
				rv.add( area );
			}
		}
		return rv;
	}	
}
