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
public class RectangleUtilties {
	public static java.awt.Rectangle createCenteredRectangle( java.awt.Rectangle bound, int width, int height ) {
		int x0 = bound.x;
		int x1 = bound.x + bound.width-1;
		int xC = ( x0 + x1 ) / 2;

		int y0 = bound.y;
		int y1 = bound.y + bound.height-1;
		int yC = ( y0 + y1 ) / 2;

		return new java.awt.Rectangle( xC-width/2, yC-height/2, width, height );
	}
	public static java.awt.Rectangle createCenteredRectangle( java.awt.Rectangle bound, java.awt.Dimension size ) {
		return createCenteredRectangle( bound, size.width, size.height );
	}
}
