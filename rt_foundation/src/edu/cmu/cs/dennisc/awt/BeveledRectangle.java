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
public class BeveledRectangle extends BeveledShape {
	public BeveledRectangle() {
	}
	public BeveledRectangle( java.awt.geom.Rectangle2D.Float rect ) {
		initialize( rect );
	}
	public BeveledRectangle( float x0, float y0, float width, float height ) {
		initialize( x0, y0, width, height );
	}
	public void initialize( float x, float y, float width, float height ) {
		java.awt.geom.Rectangle2D.Float base = new java.awt.geom.Rectangle2D.Float( x, y, width, height );
		java.awt.geom.GeneralPath high = new java.awt.geom.GeneralPath();
		java.awt.geom.GeneralPath neut = null;
		java.awt.geom.GeneralPath shad = new java.awt.geom.GeneralPath();

		float x1 = x + width;
		float y1 = y + height;
		
		high.moveTo( x1, y );
		high.lineTo( x, y );
		high.lineTo( x, y1 );

		shad.moveTo( x, y1 );
		shad.lineTo( x1, y1 );
		shad.lineTo( x1, y );
		
		initialize( base, high, neut, shad );
	}
	public void initialize( java.awt.geom.Rectangle2D.Float rect ) {
		initialize( rect.x, rect.y, rect.width, rect.height );
	}
}
