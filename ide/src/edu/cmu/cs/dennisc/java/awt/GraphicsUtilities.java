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
package edu.cmu.cs.dennisc.java.awt;

/**
 * @author Dennis Cosgrove
 */
public class GraphicsUtilities {
	public static void setRenderingHint( java.awt.Graphics g, java.awt.RenderingHints.Key key, Object value ) {
		java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
		g2.setRenderingHint( key, value );

	}

	public static void drawCenteredImage( java.awt.Graphics g, java.awt.Image image, java.awt.Component component ) {
		int x = ( component.getWidth() - image.getWidth( component ) ) / 2;
		int y = ( component.getHeight() - image.getHeight( component ) ) / 2;
		g.drawImage( image, x, y, component );
	}

	public static void drawCenteredScaledToFitImage( java.awt.Graphics g, java.awt.Image image, java.awt.Component component ) {
		int imageWidth = image.getWidth( component );
		int imageHeight = image.getHeight( component );
		int componentWidth = component.getWidth();
		int componentHeight = component.getHeight();
		double widthRatio = imageWidth / (double)componentWidth;
		double heightRatio = imageHeight / (double)componentHeight;
		int widthFactor = -1;
		int heightFactor = -1;
		if( ( widthRatio > heightRatio ) && ( widthRatio > 1.0 ) ) {
			widthFactor = componentWidth;
		} else if( ( heightRatio > widthRatio ) && ( heightRatio > 1.0 ) ) {
			heightFactor = componentHeight;
		}
		if( ( widthFactor > 0 ) || ( heightFactor > 0 ) ) {
			image = image.getScaledInstance( widthFactor, heightFactor, java.awt.Image.SCALE_SMOOTH );
		}
		drawCenteredImage( g, image, component );
	}

	public static void drawCenteredText( java.awt.Graphics g, String s, int x, int y, int width, int height ) {
		if( s != null ) {
			java.awt.FontMetrics fm = g.getFontMetrics();
			int messageWidth = fm.stringWidth( s );
			int ascent = fm.getMaxAscent();
			int descent = fm.getMaxDescent();
			g.drawString( s, ( x + ( width / 2 ) ) - ( messageWidth / 2 ), ( y + ( height / 2 ) + ( ascent / 2 ) ) - ( descent / 2 ) );
		}
	}

	public static void drawCenteredText( java.awt.Graphics g, String s, java.awt.Dimension size ) {
		drawCenteredText( g, s, 0, 0, size.width, size.height );
	}

	public static void drawCenteredText( java.awt.Graphics g, String s, java.awt.Rectangle rect ) {
		drawCenteredText( g, s, rect.x, rect.y, rect.width, rect.height );
	}

	public static void drawCenteredText( java.awt.Graphics g, String s, java.awt.geom.Rectangle2D rect ) {
		drawCenteredText( g, s, (int)rect.getX(), (int)rect.getY(), (int)rect.getWidth(), (int)rect.getHeight() );
	}

	private static void renderTriangle( java.awt.Graphics g, Heading heading, int x, int y, int width, int height, boolean isFill ) {
		if( heading != null ) {
			int x0 = x;
			int x1 = ( x + width ) - 1;
			int xC = ( x0 + x1 ) / 2;

			int y0 = y;
			int y1 = ( y + height ) - 1;
			int yC = ( y0 + y1 ) / 2;

			int[] xPoints;
			int[] yPoints;
			if( heading == Heading.NORTH ) {
				xPoints = new int[] { xC, x1, x0 };
				yPoints = new int[] { y0, y1, y1 };
			} else if( heading == Heading.EAST ) {
				xPoints = new int[] { x1, x0, x0 };
				yPoints = new int[] { yC, y1, y0 };
			} else if( heading == Heading.SOUTH ) {
				xPoints = new int[] { xC, x0, x1 };
				yPoints = new int[] { y1, y0, y0 };
			} else if( heading == Heading.WEST ) {
				xPoints = new int[] { x0, x1, x1 };
				yPoints = new int[] { yC, y0, y1 };
			} else {
				throw new IllegalArgumentException();
			}
			if( isFill ) {
				g.fillPolygon( xPoints, yPoints, 3 );
			} else {
				g.drawPolygon( xPoints, yPoints, 3 );
			}
		} else {
			throw new IllegalArgumentException();
		}
	}

	public enum Heading {
		NORTH,
		EAST,
		SOUTH,
		WEST
	}

	public static void drawTriangle( java.awt.Graphics g, Heading heading, int x, int y, int width, int height ) {
		renderTriangle( g, heading, x, y, width, height, false );
	}

	public static void drawTriangle( java.awt.Graphics g, Heading heading, java.awt.Dimension size ) {
		drawTriangle( g, heading, 0, 0, size.width, size.height );
	}

	public static void drawTriangle( java.awt.Graphics g, Heading heading, java.awt.Rectangle rect ) {
		drawTriangle( g, heading, rect.x, rect.y, rect.width, rect.height );
	}

	public static void fillTriangle( java.awt.Graphics g, Heading heading, int x, int y, int width, int height ) {
		renderTriangle( g, heading, x, y, width, height, true );
	}

	public static void fillTriangle( java.awt.Graphics g, Heading heading, java.awt.Dimension size ) {
		fillTriangle( g, heading, 0, 0, size.width, size.height );
	}

	public static void fillTriangle( java.awt.Graphics g, Heading heading, java.awt.Rectangle rect ) {
		fillTriangle( g, heading, rect.x, rect.y, rect.width, rect.height );
	}

	private static java.awt.geom.GeneralPath createPath( float x, float y, float width, float height, boolean isTopLeft ) {
		java.awt.geom.GeneralPath rv = new java.awt.geom.GeneralPath();
		float halfSize = Math.min( width / 2, height / 2 );
		if( isTopLeft ) {
			rv.moveTo( x, y );
		} else {
			rv.moveTo( x + width, y + height );
		}
		rv.lineTo( x + width, y );
		rv.lineTo( ( x + width ) - halfSize, y + halfSize );
		rv.lineTo( x + halfSize, ( y + height ) - halfSize );
		rv.lineTo( x, y + height );
		rv.closePath();
		return rv;
	}

	private static java.awt.Shape createClip( java.awt.Shape prevClip, java.awt.Shape shape, boolean isTopLeft ) {
		java.awt.geom.Rectangle2D bounds = shape.getBounds2D();
		java.awt.geom.Area rv = new java.awt.geom.Area( shape );
		if( prevClip != null ) {
			rv.intersect( new java.awt.geom.Area( prevClip ) );
		}
		rv.subtract( new java.awt.geom.Area( createPath( (float)bounds.getX(), (float)bounds.getY(), (float)bounds.getWidth(), (float)bounds.getHeight(), isTopLeft == false ) ) );
		return rv;
	}

	public static void draw3DRoundRectangle( java.awt.Graphics g, java.awt.geom.RoundRectangle2D rr, java.awt.Paint topLeftPaint, java.awt.Paint bottomRightPaint, java.awt.Stroke stroke ) {
		java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
		java.awt.Paint prevPaint = g2.getPaint();
		java.awt.Stroke prevStroke = g2.getStroke();
		java.awt.Shape prevClip = g2.getClip();
		Object antialiasingValue = g2.getRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING );
		g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );

		g2.setStroke( stroke );
		g2.setClip( createClip( prevClip, rr, true ) );
		g2.setPaint( topLeftPaint );
		g2.draw( rr );

		g2.setClip( createClip( prevClip, rr, false ) );
		g2.setPaint( bottomRightPaint );
		g2.draw( rr );

		g2.setClip( prevClip );
		g2.setStroke( prevStroke );
		g2.setPaint( prevPaint );

		g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, antialiasingValue );
	}

	public static void draw3DishShape( java.awt.Graphics g, java.awt.Shape shape, java.awt.Paint topLeftPaint, java.awt.Paint bottomRightPaint, java.awt.Stroke stroke ) {
		java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
		java.awt.Paint prevPaint = g2.getPaint();
		java.awt.Stroke prevStroke = g2.getStroke();
		java.awt.Shape prevClip = g2.getClip();
		Object antialiasingValue = g2.getRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING );
		g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );

		g2.setStroke( stroke );
		g2.setClip( createClip( prevClip, shape, true ) );
		g2.setPaint( topLeftPaint );
		g2.draw( shape );

		g2.setClip( createClip( prevClip, shape, false ) );
		g2.setPaint( bottomRightPaint );
		g2.draw( shape );

		g2.setClip( prevClip );
		g2.setStroke( prevStroke );
		g2.setPaint( prevPaint );

		g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, antialiasingValue );
	}
}
