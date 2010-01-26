package edu.cmu.cs.dennisc.lookingglass.opengl.graphics;


/**
 * @author Dennis Cosgrove
 */
class BumpyBubble extends edu.cmu.cs.dennisc.awt.geom.Composite {
//	class EllipseWrappedMultilineText extends edu.cmu.cs.dennisc.awt.geom.MultilineText {
//		public EllipseWrappedMultilineText( String text, java.awt.Font font, edu.cmu.cs.dennisc.awt.TextAlignment alignment, java.awt.Paint paint ) {
//			super( text, font, alignment, paint );
//		}
//	}
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
		edu.cmu.cs.dennisc.awt.geom.MultilineText multilineText = new edu.cmu.cs.dennisc.awt.geom.MultilineText( m_message, m_font, edu.cmu.cs.dennisc.awt.TextAlignment.LEADING, g2.getPaint() );
		multilineText.setWrapWidth( 400.0f );
		java.awt.geom.Rectangle2D bound = multilineText.getBounds( g2 );
		
		java.awt.geom.Ellipse2D ellipse = new java.awt.geom.Ellipse2D.Double();
		ellipse.setFrame( bound );
		
		double xHalfBoxLength = ellipse.getWidth() / 2;
		double yHalfBoxLength = ellipse.getHeight() / 2;

		java.util.List< Bump > bumps = new java.util.LinkedList< Bump >();
		
		double thetaCurr = 0;
		
		double xUnit = 30;//xHalfBoxLength * 0.25;
		double yUnit = 30;//yHalfBoxLength * 0.25;
		while( thetaCurr < PI_TIMES_2 - 0.05 ) {
			double xHalfEllipseLength = edu.cmu.cs.dennisc.random.RandomUtilities.nextDoubleInRange( xUnit * 1.2, xUnit * 1.6 );
			double yHalfEllipseLength = edu.cmu.cs.dennisc.random.RandomUtilities.nextDoubleInRange( yUnit * 0.8, yUnit * 1.2 );

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

		
		edu.cmu.cs.dennisc.awt.geom.Ellipse background = new edu.cmu.cs.dennisc.awt.geom.Ellipse( xHalfBoxLength, yHalfBoxLength );
		background.setDrawn( false );
		background.setFilled( true );
		add( background );

		
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
		add( multilineText );
	}
}

public class ThoughtBubbleAdapter extends BubbleAdapter< edu.cmu.cs.dennisc.scenegraph.graphics.ThoughtBubble > {
	private static final java.awt.Stroke STROKE = new java.awt.BasicStroke( 1 );

	private edu.cmu.cs.dennisc.awt.geom.Ellipse[] m_tailEllipses = null;
	private BumpyBubble m_bumpyBubble;

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
	protected void render( 
			edu.cmu.cs.dennisc.lookingglass.Graphics2D g2, 
			edu.cmu.cs.dennisc.lookingglass.LookingGlass lookingGlass, 
			java.awt.Rectangle actualViewport, 
			edu.cmu.cs.dennisc.scenegraph.AbstractCamera camera, 
			edu.cmu.cs.dennisc.awt.MultilineText multilineText, 
			java.awt.Font font, 
			java.awt.Color textColor, 
			float wrapWidth,
			java.awt.Color fillColor, 
			java.awt.Color outlineColor,
			java.awt.geom.Point2D.Float originOfTail,
			java.awt.geom.Point2D.Float bodyConnectionLocationOfTail,
			java.awt.geom.Point2D.Float textBoundsOffset,
			double portion ) {
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
		if( portion < 1.0 ) {
			paintEllipses( gc, portion );
		} else {
			if( m_bumpyBubble != null ) {
				//pass
			} else {
				m_bumpyBubble = new BumpyBubble();
				m_bumpyBubble.setMessage( multilineText.getText() );
				m_bumpyBubble.setFont( font );
				m_bumpyBubble.initialize( g2 );
				m_bumpyBubble.applyTranslation( bodyConnectionLocationOfTail.getX()+textBoundsOffset.getX(), bodyConnectionLocationOfTail.getY()+textBoundsOffset.getY() );
			}
			m_bumpyBubble.paint( gc );
			for( edu.cmu.cs.dennisc.awt.geom.Ellipse tailEllipse : m_tailEllipses ) {
				tailEllipse.paint( gc );
			}
		}
		
		g2.setStroke( stroke );
	}	
}
