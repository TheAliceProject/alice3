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
public final class KnurlUtilities {
	private static void paintPoint( java.awt.Graphics2D g2, int x, int y ) {
		g2.drawLine( x, y, x, y );
	}
	private KnurlUtilities() {
	}
	public static void paintKnurl3( java.awt.Graphics2D g2, float x, float y, float width, float height ) {
		int xPixel0 = (int)x;
		int xPixelA = (int)(x+width*0.5f);
		int xPixel1 = (int)(x+width);
		int yPixel = (int)y;
		float yPixelMax = yPixel + height;
		while( yPixel < yPixelMax ) {
			paintPoint( g2, xPixelA, yPixel );
			if( yPixel + 2 < yPixelMax ) {
				paintPoint( g2, xPixel0, yPixel + 2 );
				paintPoint( g2, xPixel1, yPixel + 2 );
			}
			yPixel += 4;
		}
		if( yPixel + 2 < yPixelMax ) {
			paintPoint( g2, xPixelA, yPixel + 2 );
		}
	}
	public static void paintKnurl5( java.awt.Graphics2D g2, float x, float y, float width, float height ) {
		int xPixel0 = (int)x;
		int xPixelA = (int)(x+width*0.25f);
		int xPixelB = (int)(x+width*0.5f);
		int xPixelC = (int)(x+width*0.75f);
		int xPixel1 = (int)(x+width);
		int yPixel = (int)y;
		float yPixelMax = yPixel + height;
		while( yPixel < yPixelMax ) {
			paintPoint( g2, xPixelA, yPixel );
			paintPoint( g2, xPixelC, yPixel );
			if( yPixel + 2 < yPixelMax ) {
				paintPoint( g2, xPixel0, yPixel + 2 );
				paintPoint( g2, xPixelB, yPixel + 2 );
				paintPoint( g2, xPixel1, yPixel + 2 );
			}
			yPixel += 4;
		}
		if( yPixel + 2 < yPixelMax ) {
			paintPoint( g2, xPixelA, yPixel + 2 );
			paintPoint( g2, xPixelC, yPixel + 2 );
		}
	}
}
