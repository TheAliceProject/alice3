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
public class ShapeUtilities {
	private static java.awt.geom.GeneralPath s_generalPath = new java.awt.geom.GeneralPath( java.awt.geom.GeneralPath.WIND_NON_ZERO, 2 );

	public static void paintBorder( java.awt.Graphics2D g2, java.awt.Shape shape, java.awt.Color a, java.awt.Color b, int width ) {
		g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
		java.awt.Stroke stroke = g2.getStroke();
		int width2 = width * 2;
		for( int i = width2; i >= 2; i -= 2 ) {
			float portion = (float)( width2 - i ) / ( width2 );
			g2.setColor( edu.cmu.cs.dennisc.java.awt.ColorUtilities.interpolate( a, b, portion ) );
			g2.setStroke( new java.awt.BasicStroke( i ) );
			g2.draw( shape );
		}
		g2.setStroke( stroke );
	}

	public static void paintBorder( java.awt.Graphics2D g2, java.awt.Shape shape, java.awt.Color a, java.awt.Color b, int width, int alphaRule ) {
		g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
		java.awt.Stroke stroke = g2.getStroke();
		int width2 = width * 2;
		for( int i = width2; i >= 2; i -= 2 ) {
			float portion = (float)( width2 - i ) / ( width2 );
			g2.setColor( edu.cmu.cs.dennisc.java.awt.ColorUtilities.interpolate( a, b, portion ) );
			g2.setComposite( java.awt.AlphaComposite.getInstance( alphaRule, portion ) );
			g2.setStroke( new java.awt.BasicStroke( i ) );
			g2.draw( shape );
		}
		g2.setStroke( stroke );
	}

	private static void drawShadedLine( java.awt.Graphics2D g2, float x0, float y0, float x1, float y1, java.awt.Paint topLeftPaint, java.awt.Paint otherPaint, java.awt.Paint bottomRightPaint, java.awt.geom.GeneralPath buffer ) {
		//		float xDelta = x1-x0;
		//		float yDelta = y1-y0;
		float xDelta = x0 - x1;
		float yDelta = y0 - y1;
		if( xDelta > 0 ) {
			if( yDelta > 0 ) {
				g2.setPaint( otherPaint );
			} else if( yDelta < 0 ) {
				g2.setPaint( topLeftPaint );
			} else {
				g2.setPaint( topLeftPaint );
			}
		} else if( xDelta < 0 ) {
			if( yDelta > 0 ) {
				g2.setPaint( bottomRightPaint );
			} else if( yDelta < 0 ) {
				g2.setPaint( otherPaint );
			} else {
				g2.setPaint( bottomRightPaint );
			}
		} else {
			if( yDelta > 0 ) {
				g2.setPaint( bottomRightPaint );
			} else if( yDelta < 0 ) {
				g2.setPaint( topLeftPaint );
			} else {
				return;
			}
		}
		buffer.reset();
		buffer.moveTo( x0, y0 );
		buffer.lineTo( x1, y1 );
		g2.draw( buffer );
		//g2.drawLine( (int)( x0+0.5f ), (int)( y0+0.5f ), (int)( x1+0.5f ), (int)( y1+0.5f ) );
	}

	public static void paint( java.awt.Graphics2D g2, java.awt.Shape shape, BevelState bevelState ) {
		g2.fill( shape );
		if( bevelState != null ) {
			java.awt.Paint paintPrev = g2.getPaint();
			if( bevelState == BevelState.FLUSH ) {
				g2.setPaint( java.awt.Color.GRAY );
				g2.draw( shape );
			} else {
				java.awt.Stroke strokePrev = g2.getStroke();
				g2.setStroke( new java.awt.BasicStroke( 1, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND ) );

				//g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );

				final float FLATNESS = 0.01f;
				float[] segment = new float[ 6 ];
				float x0 = Float.NaN;
				float y0 = Float.NaN;
				float xPrev = Float.NaN;
				float yPrev = Float.NaN;
				float xCurr;
				float yCurr;

				java.awt.Paint highlightPaint = new java.awt.Color( 255, 255, 255, 255 );
				java.awt.Paint shadowPaint = new java.awt.Color( 0, 0, 0, 255 );
				java.awt.Paint topLeftPaint;
				java.awt.Paint bottomRightPaint;
				if( bevelState == BevelState.RAISED ) {
					topLeftPaint = highlightPaint;
					bottomRightPaint = shadowPaint;
				} else {
					topLeftPaint = shadowPaint;
					bottomRightPaint = highlightPaint;
				}
				java.awt.Paint inBetweenPaint = new java.awt.Color( 128, 128, 128, 255 );

				synchronized( s_generalPath ) {
					java.awt.geom.PathIterator pathIterator = shape.getPathIterator( null, FLATNESS );
					while( pathIterator.isDone() == false ) {
						switch( pathIterator.currentSegment( segment ) ) {
						case java.awt.geom.PathIterator.SEG_MOVETO:
							x0 = xPrev = segment[ 0 ];
							y0 = yPrev = segment[ 1 ];
							break;
						case java.awt.geom.PathIterator.SEG_LINETO:
							xCurr = segment[ 0 ];
							yCurr = segment[ 1 ];
							assert Float.isNaN( xPrev ) == false;
							assert Float.isNaN( yPrev ) == false;
							drawShadedLine( g2, xPrev, yPrev, xCurr, yCurr, topLeftPaint, inBetweenPaint, bottomRightPaint, s_generalPath );
							xPrev = xCurr;
							yPrev = yCurr;
							break;
						case java.awt.geom.PathIterator.SEG_CLOSE:
							drawShadedLine( g2, xPrev, yPrev, x0, y0, topLeftPaint, inBetweenPaint, bottomRightPaint, s_generalPath );
							x0 = xPrev = Float.NaN;
							y0 = yPrev = Float.NaN;
							break;

						case java.awt.geom.PathIterator.SEG_QUADTO:
							throw new RuntimeException( "SEG_QUADTO: should not occur when shape.getPathIterator is passed a flatness argument" );
						case java.awt.geom.PathIterator.SEG_CUBICTO:
							throw new RuntimeException( "SEG_CUBICTO: should not occur when shape.getPathIterator is passed a flatness argument" );
						default:
							throw new RuntimeException( "unhandled segment: should not occur" );
						}
						pathIterator.next();
					}
				}
				g2.setStroke( strokePrev );
			}
			g2.setPaint( paintPrev );
		}
	}
}
