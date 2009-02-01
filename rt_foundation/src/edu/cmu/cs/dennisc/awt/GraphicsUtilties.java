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
public class GraphicsUtilties {
	public static void drawCenteredText( java.awt.Graphics g, String s, int x, int y, int width, int height ) {
		java.awt.FontMetrics fm = g.getFontMetrics();
		int messageWidth = fm.stringWidth( s );
	    int ascent = fm.getMaxAscent ();
	    int descent= fm.getMaxDescent ();
		g.drawString( s, x+width/2-messageWidth/2, y+height/2+ascent/2-descent/2 );
	}
	public static void drawCenteredText( java.awt.Graphics g, String s, java.awt.Dimension size ) {
		drawCenteredText( g, s, 0, 0, size.width, size.height );
	}
	public static void drawCenteredText( java.awt.Graphics g, String s, java.awt.Rectangle rect ) {
		drawCenteredText( g, s, rect.x, rect.y, rect.width, rect.height );
	}
	
	private static void renderTriangle( java.awt.Graphics g, Heading heading, int x, int y, int width, int height, boolean isFill ) {
		
		if( heading != null ) {
			int x0 = x;
			int x1 = x + width-1;
			int xC = ( x0 + x1 ) / 2;

			int y0 = y;
			int y1 = y + height-1;
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
		NORTH, EAST, SOUTH, WEST
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
}
