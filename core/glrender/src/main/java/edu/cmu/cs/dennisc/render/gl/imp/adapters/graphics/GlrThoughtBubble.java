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
package edu.cmu.cs.dennisc.render.gl.imp.adapters.graphics;

import edu.cmu.cs.dennisc.scenegraph.graphics.OnscreenBubble;

/**
 * @author Dennis Cosgrove
 */
public class GlrThoughtBubble extends GlrBubble<edu.cmu.cs.dennisc.scenegraph.graphics.ThoughtBubble> {
	private static final java.awt.Stroke STROKE = new java.awt.BasicStroke( 2 );

	private edu.cmu.cs.dennisc.java.awt.geom.Ellipse[] m_tailEllipses = null;
	//	private BumpyBubble m_bumpyBubble;

	private final int TAIL_ELLIPSE_COUNT = 3;
	private final double PORTION_PER_TAIL_ELLIPSE = 1.0 / TAIL_ELLIPSE_COUNT;

	private void paintEllipses( edu.cmu.cs.dennisc.java.awt.geom.GraphicsContext gc, double portion ) {
		for( int i = 0; i < m_tailEllipses.length; i++ ) {
			double portion0 = i * PORTION_PER_TAIL_ELLIPSE;
			double portion1 = ( i + 1 ) * PORTION_PER_TAIL_ELLIPSE;
			if( portion <= portion0 ) {
				break;
			} else {
				double desiredScale;
				if( portion > portion1 ) {
					desiredScale = 1.0;
				} else {
					desiredScale = ( portion - portion0 ) / PORTION_PER_TAIL_ELLIPSE;
				}
				java.awt.geom.AffineTransform affineTransform = m_tailEllipses[ i ].getAffineTransform();
				double currentScale = affineTransform.getScaleX();

				if( desiredScale > 0 ) {
					double scaleFactor = desiredScale / currentScale;
					m_tailEllipses[ i ].applyScale( scaleFactor, scaleFactor );
					m_tailEllipses[ i ].paint( gc );
				}
			}
		}
	}

	private static java.awt.geom.Ellipse2D createScaledOffsetRectangle2D( java.awt.geom.Rectangle2D.Double r, double offsetPortionX, double offsetPortionY, double scaleX, double scaleY ) {
		return new java.awt.geom.Ellipse2D.Double( r.x + ( offsetPortionX * r.width ), r.y + ( offsetPortionY * r.height ), r.width * scaleX, r.height * scaleY );
	}

	private static java.awt.geom.Area createRotatedAboutCenterArea( java.awt.geom.Ellipse2D e, double theta ) {
		return new java.awt.geom.Area( e ).createTransformedArea( java.awt.geom.AffineTransform.getRotateInstance( theta, e.getCenterX(), e.getCenterY() ) );
	}

	private static java.awt.Shape createBubbleAround( java.awt.geom.Rectangle2D.Double r ) {
		java.awt.geom.Ellipse2D rightEllipse = createScaledOffsetRectangle2D( r, 0.45, -0.05, 0.85, 1.25 );
		java.awt.geom.Ellipse2D topEllipse = createScaledOffsetRectangle2D( r, 0.1, -0.4, 0.8, 1.2 );
		java.awt.geom.Ellipse2D leftEllipse = createScaledOffsetRectangle2D( r, -0.2, -0.1, 0.7, 0.9 );
		java.awt.geom.Ellipse2D bottomLeftEllipse = createScaledOffsetRectangle2D( r, -0.1, 0.15, 0.7, 1.2 );
		java.awt.geom.Ellipse2D bottomRightEllipse = createScaledOffsetRectangle2D( r, 0.15, 0.45, 0.8, 1.0 );

		java.awt.geom.AffineTransform m = java.awt.geom.AffineTransform.getRotateInstance( 0.1, topEllipse.getCenterX(), topEllipse.getCenterY() );

		java.awt.geom.Area rv = new java.awt.geom.Area( r );
		rv.add( createRotatedAboutCenterArea( rightEllipse, -0.1 ) );
		rv.add( createRotatedAboutCenterArea( topEllipse, -0.05 ) );
		rv.add( new java.awt.geom.Area( leftEllipse ) );
		rv.add( createRotatedAboutCenterArea( bottomLeftEllipse, -0.1 ) );
		rv.add( createRotatedAboutCenterArea( bottomRightEllipse, 0.1 ) );
		return rv;
	}

	@Override
	protected void render(
			edu.cmu.cs.dennisc.render.Graphics2D g2,
			edu.cmu.cs.dennisc.render.RenderTarget renderTarget,
			java.awt.Rectangle actualViewport,
			edu.cmu.cs.dennisc.scenegraph.AbstractCamera camera,
			edu.cmu.cs.dennisc.java.awt.MultilineText multilineText,
			java.awt.Font font,
			java.awt.Color textColor,
			float wrapWidth,
			java.awt.Color fillColor,
			java.awt.Color outlineColor,
			OnscreenBubble bubble,
			double portion ) {
		java.awt.Stroke stroke = g2.getStroke();
		g2.setStroke( STROKE );

		if( m_tailEllipses != null ) {
			//pass
		} else {
			m_tailEllipses = new edu.cmu.cs.dennisc.java.awt.geom.Ellipse[ TAIL_ELLIPSE_COUNT ];

			double xDelta = bubble.getEndOfTail().getX() - bubble.getOriginOfTail().getX();
			double yDelta = bubble.getEndOfTail().getY() - bubble.getOriginOfTail().getY();

			for( int i = 0; i < m_tailEllipses.length; i++ ) {
				double factor = i + 1;
				m_tailEllipses[ i ] = new edu.cmu.cs.dennisc.java.awt.geom.Ellipse( 5 * factor, 3 * factor );
				m_tailEllipses[ i ].setDrawPaint( outlineColor );
				m_tailEllipses[ i ].setFillPaint( fillColor );
				m_tailEllipses[ i ].applyTranslation( bubble.getOriginOfTail().getX() + ( i * xDelta * PORTION_PER_TAIL_ELLIPSE ), bubble.getOriginOfTail().getY() + ( i * yDelta * PORTION_PER_TAIL_ELLIPSE ) );
			}
		}

		edu.cmu.cs.dennisc.java.awt.geom.GraphicsContext gc = new edu.cmu.cs.dennisc.java.awt.geom.GraphicsContext();
		gc.initialize( g2 );
		if( portion < 1.0 ) {
			paintEllipses( gc, portion );
		} else {
			java.awt.geom.Rectangle2D.Double textBounds = bubble.getTextBounds();
			final double MINIMUM_WIDTH = 48.0;
			double w;
			double halfWidthDelta;
			if( textBounds.getWidth() < MINIMUM_WIDTH ) {
				w = MINIMUM_WIDTH;
				halfWidthDelta = ( textBounds.getWidth() - w ) * 0.5;
			} else {
				w = textBounds.getWidth();
				halfWidthDelta = 0.0;
			}

			final double MINIMUM_HEIGHT = Math.max( 40.0, w * 0.15 );
			double h;
			double halfHeightDelta;
			if( textBounds.getHeight() < MINIMUM_HEIGHT ) {
				h = MINIMUM_HEIGHT;
				halfHeightDelta = ( textBounds.getHeight() - h ) * 0.5;
			} else {
				h = textBounds.getHeight();
				halfHeightDelta = 0.0;
			}
			java.awt.geom.Rectangle2D.Double bounds = new java.awt.geom.Rectangle2D.Double( textBounds.x + halfWidthDelta, textBounds.y + halfHeightDelta, w, h );

			java.awt.Shape shape = createBubbleAround( bounds );
			g2.setPaint( fillColor );
			g2.fill( shape );
			g2.setPaint( outlineColor );
			g2.draw( shape );

			g2.setPaint( textColor );
			multilineText.paint( g2, wrapWidth, edu.cmu.cs.dennisc.java.awt.TextAlignment.LEADING, textBounds );

			for( edu.cmu.cs.dennisc.java.awt.geom.Ellipse tailEllipse : m_tailEllipses ) {
				tailEllipse.paint( gc );
			}
		}

		g2.setStroke( stroke );
	}
}
