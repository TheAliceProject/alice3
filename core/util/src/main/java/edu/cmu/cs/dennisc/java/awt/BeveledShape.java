/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may
 *    "Alice" appear in their name, without prior written permission of
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is
 *    contributed by Electronic Arts Inc. and may be used for personal,
 *    non-commercial, and academic use only. Redistributions of any program
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in
 *    The Alice 3.0 Art Gallery License.
 *
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *******************************************************************************/
package edu.cmu.cs.dennisc.java.awt;

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
		GraphicsContext gc = GraphicsContext.getInstanceAndPushGraphics( g2 );
		try {
			gc.pushStroke();
			gc.pushPaint();
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
			gc.popAll();
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
