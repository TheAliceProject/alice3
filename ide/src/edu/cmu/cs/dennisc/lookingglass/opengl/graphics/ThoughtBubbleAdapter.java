/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
 */
package edu.cmu.cs.dennisc.lookingglass.opengl.graphics;

import edu.cmu.cs.dennisc.scenegraph.graphics.OnscreenBubble;

/**
 * @author Dennis Cosgrove
 */
class BumpyBubble extends edu.cmu.cs.dennisc.java.awt.geom.Composite {
	//	class EllipseWrappedMultilineText extends edu.cmu.cs.dennisc.awt.geom.MultilineText {
	//		public EllipseWrappedMultilineText( String text, java.awt.Font font, edu.cmu.cs.dennisc.awt.TextAlignment alignment, java.awt.Paint paint ) {
	//			super( text, font, alignment, paint );
	//		}
	//	}
	class Bump extends edu.cmu.cs.dennisc.java.awt.geom.Shape {
		private double m_xHalfLength;
		private double m_yHalfLength;
		private java.awt.geom.Ellipse2D.Double m_ellipse;
		private java.awt.geom.Arc2D.Double m_arc;

		public Bump( double xHalfLength, double yHalfLength ) {
			m_ellipse = new java.awt.geom.Ellipse2D.Double( -xHalfLength, -yHalfLength, xHalfLength * 2, yHalfLength * 2 );
			m_arc = new java.awt.geom.Arc2D.Double( -xHalfLength, -yHalfLength, xHalfLength * 2, yHalfLength * 2, -90, 180, java.awt.geom.Arc2D.OPEN );
			m_xHalfLength = xHalfLength;
			m_yHalfLength = yHalfLength;
		}

		public void setAngles( edu.cmu.cs.dennisc.math.Angle angle0, edu.cmu.cs.dennisc.math.Angle angle1 ) {
			setAnglesInDegrees( angle0.getAsDegrees(), angle1.getAsDegrees() );
		}

		@Deprecated
		public void setAnglesInDegrees( double angle0, double angle1 ) {
			m_arc.setAngleStart( angle0 );
			//todo: investigate
			//m_arc.setAngleExtent( angle0.getAsDegrees() );
			m_arc.setAngleExtent( -m_arc.getAngleStart() + angle1 );
		}

		public double getXHalfLength() {
			return m_xHalfLength;
		}

		public double getYHalfLength() {
			return m_yHalfLength;
		}

		public boolean contains( java.awt.geom.Point2D.Double p ) {
			return m_ellipse.contains( p );
		}

		@Override
		protected java.awt.Shape getDrawShape() {
			return m_arc;
		}

		@Override
		protected java.awt.Shape getFillShape() {
			return m_ellipse;
		}

		@Override
		public String toString() {
			return "Bump[xHalfLength=" + m_xHalfLength + ";yHalfLength=" + m_yHalfLength + "]";
		}
	}

	private static double getLengthSquared( double x, double y ) {
		return ( x * x ) + ( y * y );
	}

	private static double getLength( double x, double y ) {
		return Math.sqrt( getLengthSquared( x, y ) );
	}

	private static double getDistanceBetween( double theta0, double theta1, double xHalfLength, double yHalfLength ) {
		double x0 = xHalfLength * Math.cos( theta0 );
		double y0 = yHalfLength * Math.sin( theta0 );
		double x1 = xHalfLength * Math.cos( theta1 );
		double y1 = yHalfLength * Math.sin( theta1 );
		return getLength( x1 - x0, y1 - y0 );
	}

	//	private static java.awt.geom.Ellipse2D.Double getBoundingEllipse( java.awt.geom.Ellipse2D.Double rv, java.awt.geom.Rectangle2D rect ) {
	//		double xRectHalfLength = rect.getWidth() / 2;
	//		double yRectHalfLength = rect.getHeight() / 2;
	//
	//		
	//		double xCenter = rect.getX() + xRectHalfLength;
	//		double yCenter = rect.getY() + yRectHalfLength;
	//			
	//		double theta = Math.atan2( yRectHalfLength, xRectHalfLength );
	//		
	//		double ratio = getLength( xRectHalfLength/Math.cos( theta ), yRectHalfLength/Math.sin( theta ) ) / getLength( xRectHalfLength, yRectHalfLength );
	//				
	//		double xEllipseHalfLength = xRectHalfLength * ratio;
	//		double yEllipseHalfLength = yRectHalfLength * ratio;
	//
	//		rv.setFrame( xCenter - xEllipseHalfLength, yCenter - yEllipseHalfLength, xEllipseHalfLength*2, yEllipseHalfLength*2 );
	//
	//		return rv;
	//	}
	//	private static java.awt.geom.Ellipse2D.Double getBoundingEllipse( java.awt.geom.Rectangle2D rect ) {
	//		return getBoundingEllipse( new java.awt.geom.Ellipse2D.Double(), rect );
	//	}

	private String m_message;

	public String getMessage() {
		return m_message;
	}

	public void setMessage( String message ) {
		m_message = message;
	}

	private java.awt.Font m_font;

	public java.awt.Font getFont() {
		return m_font;
	}

	public void setFont( java.awt.Font font ) {
		m_font = font;
	}

	private static final double PI_TIMES_2 = Math.PI * 2;
	private static edu.cmu.cs.dennisc.math.SineCosineCache s_sineCosineCache = new edu.cmu.cs.dennisc.math.SineCosineCache( 8 );

	public void initialize( java.awt.Graphics2D g2, OnscreenBubble bubbleBounds, float wrapWidth ) {
		edu.cmu.cs.dennisc.java.awt.geom.MultilineText multilineText = new edu.cmu.cs.dennisc.java.awt.geom.MultilineText( m_message, m_font, edu.cmu.cs.dennisc.java.awt.TextAlignment.LEADING, g2.getPaint() );
		multilineText.setWrapWidth( wrapWidth );
		//		java.awt.geom.Ellipse2D ellipse = new java.awt.geom.Ellipse2D.Double();
		//		ellipse.setFrame( bubbleBounds.getBubbleRect().getBounds2D() );

		double xHalfBoxLength = bubbleBounds.getTextBounds().getWidth() / 2;
		double yHalfBoxLength = bubbleBounds.getTextBounds().getHeight() / 2;

		java.util.List<Bump> bumps = new java.util.LinkedList<Bump>();

		double thetaCurr = 0;

		double xUnit = bubbleBounds.getHorizontalPadding();//xHalfBoxLength * 0.25;
		double yUnit = bubbleBounds.getVerticalPadding();//yHalfBoxLength * 0.25;
		while( thetaCurr < ( PI_TIMES_2 - 0.05 ) ) {
			double xHalfEllipseLength = edu.cmu.cs.dennisc.random.RandomUtilities.nextDoubleInRange( xUnit * .8, xUnit * 1.0 );
			double yHalfEllipseLength = edu.cmu.cs.dennisc.random.RandomUtilities.nextDoubleInRange( yUnit * .8, yUnit * 1.0 );

			final double THETA_STEP = 0.1;
			double s = 0;
			double thetaNext = thetaCurr;
			while( s < ( xHalfEllipseLength * 5 ) ) {
				thetaNext += THETA_STEP;
				double dist = getDistanceBetween( thetaCurr, thetaNext, xHalfBoxLength, yHalfBoxLength );
				s += dist;
			}

			Bump bump = new Bump( xHalfEllipseLength, yHalfEllipseLength );
			double xOnBound = ( Math.cos( thetaCurr ) * xHalfBoxLength );
			double yOnBound = ( Math.sin( thetaCurr ) * yHalfBoxLength );
			bump.applyTranslation( xOnBound, yOnBound );
			bump.applyRotation( new edu.cmu.cs.dennisc.math.AngleInRadians( thetaCurr ) );

			//bump.setFilled( false );

			//			float portion = (float)( thetaCurr / PI_TIMES_2 );
			//			float hue = 0.0f;
			//			float saturation = portion;
			//			float brightness = 0.5f;
			//			int rgb = java.awt.Color.HSBtoRGB( hue, saturation, brightness );
			//			java.awt.Paint fillPaint = new java.awt.Color( rgb );

			//			java.awt.Paint fillPaint = new java.awt.Color( 192, 192, 255 );

			java.awt.Paint fillPaint = java.awt.Color.WHITE;

			bump.setFillPaint( fillPaint );

			bumps.add( bump );

			thetaCurr = thetaNext;
		}

		edu.cmu.cs.dennisc.java.awt.geom.Ellipse background = new edu.cmu.cs.dennisc.java.awt.geom.Ellipse( xHalfBoxLength, yHalfBoxLength );
		background.setDrawn( false );
		background.setFilled( true );
		add( background );

		java.awt.geom.Point2D.Double ptSrc = new java.awt.geom.Point2D.Double();
		java.awt.geom.Point2D.Double ptDst = new java.awt.geom.Point2D.Double();

		final int N_BUMPS = bumps.size();
		for( int i = 0; i < N_BUMPS; i++ ) {
			Bump prev = bumps.get( i );
			Bump curr = bumps.get( ( i + 1 ) % N_BUMPS );

			java.awt.geom.AffineTransform m = prev.getInverseAffineTransform();
			m.concatenate( curr.getAffineTransform() );

			final int N = s_sineCosineCache.cosines.length;
			for( int j = 0; j < N; j++ ) {
				ptSrc.x = curr.getXHalfLength() * s_sineCosineCache.cosines[ j ];
				ptSrc.y = -curr.getYHalfLength() * s_sineCosineCache.sines[ j ];
				m.transform( ptSrc, ptDst );

				if( prev.contains( ptDst ) ) {
					//					double angle0;
					//					if( i==(N_BUMPS-1) ) {
					//						Bump next = bumps.get( 1 );
					//						java.awt.geom.AffineTransform _m = curr.getInverseAffineTransform();
					//						_m.concatenate( next.getAffineTransform() );
					//						
					//						angle0 = -Math.PI;
					//						for( int k=0; k<N; k++ ) {
					//							ptSrc.x = curr.getXHalfLength() * s_sineCosineCache.cosines[ k ];
					//							ptSrc.y = curr.getYHalfLength() * s_sineCosineCache.sines[ k ];
					//							_m.transform( ptSrc, ptDst );
					//							
					//							edu.cmu.cs.dennisc.toolkit.geom.Ellipse e = new edu.cmu.cs.dennisc.toolkit.geom.Ellipse( 10, 10 );
					//							e.applyTranslation( ptDst.x, ptDst.y );
					//							add( e );
					//							
					//							if( next.contains( ptDst ) ) {
					//								angle0 = s_sineCosineCache.angles[ k ];
					//								break;
					//							}
					//						}
					//					} else {
					//						angle0 = -Math.PI/2;
					//					}
					double angle0 = -Math.PI / 2;
					curr.setAnglesInDegrees( angle0, s_sineCosineCache.angles[ j ] );
					add( curr );
					break;
				}
			}
		}
		add( multilineText );

		this.applyTranslation( bubbleBounds.getBubbleRect().getCenterX(), bubbleBounds.getBubbleRect().getCenterY() );
	}
}

public class ThoughtBubbleAdapter extends BubbleAdapter<edu.cmu.cs.dennisc.scenegraph.graphics.ThoughtBubble> {
	private static final java.awt.Stroke STROKE = new java.awt.BasicStroke( 1 );

	private edu.cmu.cs.dennisc.java.awt.geom.Ellipse[] m_tailEllipses = null;
	private BumpyBubble m_bumpyBubble;

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

	@Override
	protected void render(
			edu.cmu.cs.dennisc.lookingglass.Graphics2D g2,
			edu.cmu.cs.dennisc.lookingglass.LookingGlass lookingGlass,
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
				m_tailEllipses[ i ].applyTranslation( bubble.getOriginOfTail().getX() + ( i * xDelta * PORTION_PER_TAIL_ELLIPSE ), bubble.getOriginOfTail().getY() + ( i * yDelta * PORTION_PER_TAIL_ELLIPSE ) );
			}
		}

		edu.cmu.cs.dennisc.java.awt.geom.GraphicsContext gc = new edu.cmu.cs.dennisc.java.awt.geom.GraphicsContext();
		gc.initialize( g2 );
		if( portion < 1.0 ) {
			paintEllipses( gc, portion );
		} else {
			if( m_bumpyBubble != null ) {
				//pass
			} else {
				m_bumpyBubble = new BumpyBubble();
				m_bumpyBubble.setMessage( multilineText.getText() );
				m_bumpyBubble.setFont( font );
				m_bumpyBubble.initialize( g2, bubble, wrapWidth );
				//m_bumpyBubble.applyTranslation( bubble.getEndOfTail().getX()+textBoundsOffset.getX(), bodyConnectionLocationOfTail.getY()+textBoundsOffset.getY() );
			}
			m_bumpyBubble.paint( gc );
			for( edu.cmu.cs.dennisc.java.awt.geom.Ellipse tailEllipse : m_tailEllipses ) {
				tailEllipse.paint( gc );
			}
		}

		g2.setStroke( stroke );
	}
}
