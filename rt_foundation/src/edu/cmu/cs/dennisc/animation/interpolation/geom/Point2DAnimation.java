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
package edu.cmu.cs.dennisc.animation.interpolation.geom;

import edu.cmu.cs.dennisc.animation.interpolation.InterpolationAnimation;

//todo: rename?
//todo: support float
/**
 * @author Dennis Cosgrove
 */
public abstract class Point2DAnimation extends InterpolationAnimation<java.awt.geom.Point2D> {
	public Point2DAnimation( Number duration, edu.cmu.cs.dennisc.animation.Style style, java.awt.geom.Point2D p0, java.awt.geom.Point2D p1 ) {
		super( duration, style, p0, p1 );
	}
	@Override
	protected java.awt.geom.Point2D newE( java.awt.geom.Point2D other ) {
		double x;
		double y;
		if( other != null ) {
			x = other.getX();
			y = other.getY();
		} else {
			x = Double.NaN;
			y = Double.NaN;
		}
		return new java.awt.geom.Point2D.Double( x, y );
	}
	@Override
	protected java.awt.geom.Point2D interpolate( java.awt.geom.Point2D rv, java.awt.geom.Point2D v0, java.awt.geom.Point2D v1, double portion ) {
		rv.setLocation( 
				v0.getX() + (v1.getX()-v0.getX()) * portion,
				v0.getY() + (v1.getY()-v0.getY()) * portion
		);
		return rv;
	}
}

