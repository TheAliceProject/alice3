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
package org.alice.apis.moveandturn.graphic;

/**
 * @author Dennis Cosgrove
 */
class BumpyBubble extends edu.cmu.cs.dennisc.awt.geom.Composite {
	class Bump extends edu.cmu.cs.dennisc.awt.geom.Shape {
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
			return "Bump[xHalfLength="+m_xHalfLength+";yHalfLength="+m_yHalfLength+"]";
		}
	}

	private static double getLengthSquared( double x, double y ) {
		return x*x + y*y;
	}
	private static double getLength( double x, double y ) {
		return Math.sqrt( getLengthSquared( x, y ) );
	}
	private static double getDistanceBetween( double theta0, double theta1, double xHalfLength, double yHalfLength ) {
		double x0 = xHalfLength * Math.cos( theta0 );
		double y0 = yHalfLength * Math.sin( theta0 );
		double x1 = xHalfLength * Math.cos( theta1 );
		double y1 = yHalfLength * Math.sin( theta1 );
		return getLength( x1-x0, y1-y0 );
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
	
	public void initialize( java.awt.Graphics2D g2 ) {
		g2.setFont( m_font );
		java.awt.geom.Rectangle2D bound = g2.getFontMetrics().getStringBounds( m_message, g2 );
		
		//java.awt.geom.Ellipse2D ellipse = getBoundingEllipse( bound );
		java.awt.geom.Ellipse2D ellipse = new java.awt.geom.Ellipse2D.Double();
		ellipse.setFrame( bound );
		
		double xHalfBoxLength = ellipse.getWidth() / 2;
		double yHalfBoxLength = ellipse.getHeight() / 2;


		java.util.List< Bump > bumps = new java.util.LinkedList< Bump >();
		
		double thetaCurr = 0;
		
		while( thetaCurr < PI_TIMES_2 - 0.1 ) {
			double unit = 30;
			double xHalfEllipseLength = edu.cmu.cs.dennisc.random.RandomUtilities.nextDoubleInRange( unit * 1.2, unit * 1.6 );
			double yHalfEllipseLength = edu.cmu.cs.dennisc.random.RandomUtilities.nextDoubleInRange( unit * 0.8, unit * 1.2 );

			final double THETA_STEP = 0.1;
			double s = 0;
			double thetaNext = thetaCurr;
			while( s < (yHalfEllipseLength*2) ) {
				thetaNext += THETA_STEP;
				s += getDistanceBetween( thetaCurr, thetaNext, xHalfBoxLength, yHalfBoxLength );
			}
			
			
			Bump bump = new Bump( xHalfEllipseLength, yHalfEllipseLength );
			double xOnBound = (Math.cos( thetaCurr ) * xHalfBoxLength);
			double yOnBound = (Math.sin( thetaCurr ) * yHalfBoxLength);
			bump.applyTranslation( xOnBound, yOnBound );
			bump.applyRotation( new org.alice.apis.moveandturn.AngleInRadians( thetaCurr ) );
			
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

		
//		edu.cmu.cs.dennisc.toolkit.geom.Ellipse background = new edu.cmu.cs.dennisc.toolkit.geom.Ellipse( xHalfBoxLength, yHalfBoxLength );
//		background.setDrawn( true );
//		background.setFilled( false );
//		add( background );

		
		java.awt.geom.Point2D.Double ptSrc = new java.awt.geom.Point2D.Double();
		java.awt.geom.Point2D.Double ptDst = new java.awt.geom.Point2D.Double();
		
		final int N_BUMPS = bumps.size();
		for( int i=0; i<N_BUMPS; i++ ) {
			Bump prev = bumps.get( i );
			Bump curr = bumps.get( (i+1)%N_BUMPS );
			
			java.awt.geom.AffineTransform m = prev.getInverseAffineTransform();
			m.concatenate( curr.getAffineTransform() );
			
			final int N = s_sineCosineCache.cosines.length;
			for( int j=0; j<N; j++ ) {
				ptSrc.x =  curr.getXHalfLength() * s_sineCosineCache.cosines[ j ];
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
					double angle0 = -Math.PI/2;
					curr.setAnglesInDegrees( angle0, s_sineCosineCache.angles[ j ] );
					add( curr );
					break;
				}
			}
		}
		
		edu.cmu.cs.dennisc.awt.geom.Text text = new edu.cmu.cs.dennisc.awt.geom.Text( m_message, m_font );
		add( text );
	}
}

/**
 * @author Dennis Cosgrove
 */
public class ThoughtBubble extends Bubble {
	private static final java.awt.Stroke STROKE = new java.awt.BasicStroke( 2 );

	private edu.cmu.cs.dennisc.awt.geom.Ellipse[] m_tailEllipses = null;
//	private java.awt.geom.Area m_area;
	
	private BumpyBubble m_bumpyBubble;
	
	public ThoughtBubble( String text, java.awt.Font font, java.awt.Color foregroundColor, java.awt.Color backgroundColor, java.awt.Color outlineColor, Originator originator ) {
		super( text, font, foregroundColor, backgroundColor, outlineColor, originator );
	}
	
	private final int TAIL_ELLIPSE_COUNT = 3;
	private final double PORTION_PER_TAIL_ELLIPSE = 1.0/TAIL_ELLIPSE_COUNT;
	
	private void paintEllipses( edu.cmu.cs.dennisc.awt.geom.GraphicsContext gc, double portion ) {
		for( int i=0; i<m_tailEllipses.length; i++ ) {
			double portion0 = i*PORTION_PER_TAIL_ELLIPSE;
			double portion1 = (i+1)*PORTION_PER_TAIL_ELLIPSE;
			if( portion <= portion0 ) {
				break;
			} else {
				double desiredScale;
				if( portion > portion1 ) {
					desiredScale = 1.0;
				} else {
					desiredScale = (portion-portion0)/PORTION_PER_TAIL_ELLIPSE; 
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
	protected void paint( edu.cmu.cs.dennisc.lookingglass.Graphics2D g2, edu.cmu.cs.dennisc.lookingglass.LookingGlass lookingGlass, edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera, java.lang.String text, java.awt.Font font, java.awt.Color textColor,
			java.awt.geom.Rectangle2D textBounds, org.alice.apis.moveandturn.graphic.Bubble.State state, double portion, java.awt.geom.Point2D originOfTail, java.awt.geom.Point2D bodyConnectionLocationOfTail,
			java.awt.geom.Point2D textBoundsOffset, java.awt.Color fillColor, java.awt.Color outlineColor ) {
		java.awt.Stroke stroke = g2.getStroke();
		g2.setStroke( STROKE );


		if( m_tailEllipses != null ) {
			//pass
		} else {
			m_tailEllipses = new edu.cmu.cs.dennisc.awt.geom.Ellipse[ TAIL_ELLIPSE_COUNT ];
			
			double xDelta = bodyConnectionLocationOfTail.getX() - originOfTail.getX(); 
			double yDelta = bodyConnectionLocationOfTail.getY() - originOfTail.getY(); 
			
			for( int i=0; i<m_tailEllipses.length; i++ ) {
				double factor = i+1;
				m_tailEllipses[ i ] = new edu.cmu.cs.dennisc.awt.geom.Ellipse( 5*factor, 3*factor );
				m_tailEllipses[ i ].applyTranslation( originOfTail.getX() + i*xDelta*PORTION_PER_TAIL_ELLIPSE, originOfTail.getY() + i*yDelta*PORTION_PER_TAIL_ELLIPSE );
			}
		}

		edu.cmu.cs.dennisc.awt.geom.GraphicsContext gc = new edu.cmu.cs.dennisc.awt.geom.GraphicsContext();
		gc.initialize( g2 );
		if( state == State.OPENNING ) {
			paintEllipses( gc, portion );
		} else if( state == State.UPDATING ) {
			if( m_bumpyBubble != null ) {
				//pass
			} else {
				m_bumpyBubble = new BumpyBubble();
				m_bumpyBubble.setMessage( text );
				m_bumpyBubble.setFont( font );
				m_bumpyBubble.initialize( g2 );
				m_bumpyBubble.applyTranslation( bodyConnectionLocationOfTail.getX()+textBoundsOffset.getX(), bodyConnectionLocationOfTail.getY()+textBoundsOffset.getY() );
			}
			m_bumpyBubble.paint( gc );
			for( edu.cmu.cs.dennisc.awt.geom.Ellipse tailEllipse : m_tailEllipses ) {
				tailEllipse.paint( gc );
			}
		} else if( state == State.CLOSING ) {
			paintEllipses( gc, 1.0-portion );
		}
		
		g2.setStroke( stroke );
	}

//	@Override
//	protected void fillAndOutlineBubble( edu.cmu.cs.dennisc.lookingglass.Graphics2D g2, java.awt.geom.Point2D origin, java.awt.geom.Point2D textOffset, java.awt.geom.Point2D anchorOffset, java.awt.geom.Rectangle2D textBounds, java.awt.Color fillColor, java.awt.Color outlineColor ) {
//		g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
//		g2.setRenderingHint( java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
//	
//		if( m_bumpyBubble != null ) {
//			//pass
//		} else {
//			m_bumpyBubble = new BumpyBubble();
//			m_bumpyBubble.setMessage( "I'm tired..." );
//			m_bumpyBubble.applyTranslation( textOffset.getX(), textOffset.getY() );
//		
//		
//			m_bumpyBubble.initialize( g2 );
////			m_area = bumbyBubble.getArea( gc );
//		}
//		edu.cmu.cs.dennisc.toolkit.geom.GraphicsContext gc = new edu.cmu.cs.dennisc.toolkit.geom.GraphicsContext();
//		gc.initialize( g2 );
//		m_bumpyBubble.paint( gc );
//		
//
////		g2.setPaint( java.awt.Color.WHITE );
////		g2.fill( m_area );
////		g2.setPaint( java.awt.Color.BLACK );
////		g2.draw( m_area );
//		//thoughtBubble.paint( gc );
//	}
	
//	private static void fillAndOutlineCenteredOval( edu.cmu.cs.dennisc.lookingglass.Graphics2D g, float xCenter, float yCenter, float width, float height, java.awt.Color fillColor, java.awt.Color outlineColor ) {
//		int x = (int)(xCenter-width*0.5f);
//		int y = (int)(yCenter-height*0.5f);
//		g.setColor( fillColor );
//		g.fillOval( x, y, (int)width, (int)height );
//		g.setColor( outlineColor );
//		g.drawOval( x, y, (int)width, (int)height );
//	}
//	
//	@Override
//	protected void fillAndOutlineBubble( edu.cmu.cs.dennisc.lookingglass.Graphics2D g, java.awt.geom.Point2D.Float origin, java.awt.geom.Point2D.Float textOffset, java.awt.geom.Point2D.Float anchorOffset, java.awt.geom.Rectangle2D.Float textBounds, java.awt.Color fillColor, java.awt.Color outlineColor ) {
//		final int IPAD = 12;
//		int x = (int)( textBounds.x + textOffset.x ) - IPAD;
//		int y = (int)( textBounds.y + textOffset.y ) - IPAD;
//		int width = (int)textBounds.width + IPAD + IPAD;
//		int height = (int)textBounds.height + IPAD + IPAD;
//		g.setColor( fillColor );
//		g.fillRoundRect( x, y, width, height, IPAD, IPAD );
//		g.setColor( outlineColor );
//		g.drawRoundRect( x, y, width, height, IPAD, IPAD );
//		
//		float[] portions = { 0.0f, 0.333f, 0.667f, 1.0f };
//
//		float targetX = textOffset.x + anchorOffset.x;
//		float targetY = textOffset.y + anchorOffset.y;
//
//		final float DELTA_OVAL_WIDTH = 4.0f;
//		final float DELTA_OVAL_HEIGHT = 4.0f;
//
//		float ovalWidth = 8.0f;
//		float ovalHeight = 4.0f;
//
//		float finalOvalHeight = ovalHeight + (portions.length-1)*DELTA_OVAL_HEIGHT;
//
//		float xDelta = targetX - origin.x;
//		float yDelta = targetY - origin.y + finalOvalHeight;
//		
//		for( float portion : portions ) {
//			float xCenter = origin.x + xDelta*portion;
//			float yCenter = origin.y + yDelta*portion;
//			
//			fillAndOutlineCenteredOval( g, xCenter, yCenter, ovalWidth, ovalHeight, fillColor, outlineColor );
//			
//			ovalWidth += DELTA_OVAL_WIDTH;
//			ovalHeight += DELTA_OVAL_HEIGHT;
//		}
//	}
}
