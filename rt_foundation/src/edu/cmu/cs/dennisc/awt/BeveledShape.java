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
package edu.cmu.cs.dennisc.awt;

/**
 * @author Dennis Cosgrove
 */
public class BeveledShape {
	//todo: base colors on fill paint
	private final static java.awt.Paint HIGHLIGHT_PAINT = java.awt.Color.LIGHT_GRAY;
	private final static java.awt.Paint NEUTRAL_PAINT = java.awt.Color.GRAY;
	private final static java.awt.Paint SHADOW_PAINT = java.awt.Color.BLACK;
	
	private final static int CAP = java.awt.BasicStroke.CAP_ROUND;
	private final static int JOIN = java.awt.BasicStroke.JOIN_ROUND;

	//todo: reduce visibility to private?
	protected java.awt.Shape m_base;
	protected java.awt.geom.GeneralPath m_highlightForRaised;
	protected java.awt.geom.GeneralPath m_neutralForRaised;
	protected java.awt.geom.GeneralPath m_shadowForRaised;
	protected java.awt.geom.GeneralPath m_highlightForSunken;
	protected java.awt.geom.GeneralPath m_neutralForSunken;
	protected java.awt.geom.GeneralPath m_shadowForSunken;

	public BeveledShape() {
	}
	public BeveledShape( java.awt.Shape base, java.awt.geom.GeneralPath highlightForRaised, java.awt.geom.GeneralPath neutralForRaised, java.awt.geom.GeneralPath shadowForRaised, java.awt.geom.GeneralPath highlightForSunken, java.awt.geom.GeneralPath neutralForSunken, java.awt.geom.GeneralPath shadowForSunken ) {
		initialize( base, highlightForRaised, neutralForRaised, shadowForRaised, highlightForSunken, neutralForSunken, shadowForSunken );
	}
	public BeveledShape( java.awt.Shape base, java.awt.geom.GeneralPath highlightForRaised, java.awt.geom.GeneralPath neutralForRaised, java.awt.geom.GeneralPath shadowForRaised ) {
		initialize( base, highlightForRaised, neutralForRaised, shadowForRaised );
	}
	
	public java.awt.Shape getBaseShape() {
		return m_base;
	}
	protected java.awt.geom.GeneralPath getPathForRaisedHighlight() {
		return m_highlightForRaised;
	}
	protected java.awt.geom.GeneralPath getPathForRaisedNeutral() {
		return m_neutralForRaised;
	}
	protected java.awt.geom.GeneralPath getPathForRaisedShadow() {
		return m_shadowForRaised;
	}
	protected java.awt.geom.GeneralPath getPathForSunkenHighlight() {
		return m_highlightForSunken;
	}
	protected java.awt.geom.GeneralPath getPathForSunkenNeutral() {
		return m_neutralForSunken;
	}
	protected java.awt.geom.GeneralPath getPathForSunkenShadow() {
		return m_shadowForSunken;
	}

	protected void initialize( java.awt.Shape base, java.awt.geom.GeneralPath highlightForRaised, java.awt.geom.GeneralPath neutralForRaised, java.awt.geom.GeneralPath shadowForRaised, java.awt.geom.GeneralPath highlightForSunken, java.awt.geom.GeneralPath neutralForSunken, java.awt.geom.GeneralPath shadowForSunken ) {
		m_base = base;
		m_highlightForRaised = highlightForRaised;
		m_neutralForRaised = neutralForRaised;
		m_shadowForRaised = shadowForRaised;
		m_highlightForSunken = highlightForSunken;
		m_neutralForSunken = neutralForSunken;
		m_shadowForSunken = shadowForSunken;
	}
	protected void initialize( java.awt.Shape base, java.awt.geom.GeneralPath highlightForRaised, java.awt.geom.GeneralPath neutralForRaised, java.awt.geom.GeneralPath shadowForRaised ) {
		initialize( base, highlightForRaised, neutralForRaised, shadowForRaised, shadowForRaised, neutralForRaised, highlightForRaised );
	}

	public void draw( java.awt.Graphics2D g2, BevelState bevelState, float raisedStrokeWidth, float flushStrokeWidth, float sunkenStrokeWidth ) {
		java.awt.Stroke prevStroke = g2.getStroke();
		java.awt.Paint prevPaint = g2.getPaint();
		try {
			if( bevelState == BevelState.FLUSH ) {
				g2.setStroke( new java.awt.BasicStroke( flushStrokeWidth, CAP, JOIN ) );
				g2.setPaint( NEUTRAL_PAINT );
				g2.draw( m_base );
//				if( m_shadowForRaised != null ) {
//					g2.draw( m_shadowForRaised );
//				}
//				if( m_neutralForRaised != null ) {
//					g2.draw( m_neutralForRaised );
//				}
//				if( m_highlightForRaised != null ) {
//					g2.draw( m_highlightForRaised );
//				}
			} else {
				java.awt.Shape highlight;
				java.awt.Shape neutral;
				java.awt.Shape shadow;
				java.awt.Stroke currStroke;
				if( bevelState == BevelState.RAISED ) {
					currStroke = new java.awt.BasicStroke( raisedStrokeWidth, CAP, JOIN );
					highlight = m_highlightForRaised;
					neutral = m_neutralForRaised;
					shadow = m_shadowForRaised;
				} else if( bevelState == BevelState.SUNKEN ) {
					currStroke = new java.awt.BasicStroke( sunkenStrokeWidth, CAP, JOIN );
					highlight = m_highlightForSunken;
					neutral = m_neutralForSunken;
					shadow = m_shadowForSunken;
				} else {
					throw new RuntimeException();
				}
				g2.setStroke( currStroke );
				if( shadow != null ) {
					g2.setPaint( SHADOW_PAINT );
					g2.draw( shadow );
				}
				if( neutral != null ) {
					g2.setPaint( NEUTRAL_PAINT );
					g2.draw( neutral );
				}
				if( highlight != null ) {
					g2.setPaint( HIGHLIGHT_PAINT );
					g2.draw( highlight );
				}
			}
		} finally {
			g2.setPaint( prevPaint );
			g2.setStroke( prevStroke );
		}
	}
	public void fill( java.awt.Graphics2D g2 ) {
		g2.fill( m_base );
	}
	public void paint( java.awt.Graphics2D g2, BevelState bevelState, float raisedStrokeWidth, float flushStrokeWidth, float sunkenStrokeWidth ) {
		this.fill( g2 );
		this.draw( g2, bevelState, raisedStrokeWidth, flushStrokeWidth, sunkenStrokeWidth );
	}
}
