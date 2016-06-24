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
package edu.cmu.cs.dennisc.java.awt.geom.animation;

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
				v0.getX() + ( ( v1.getX() - v0.getX() ) * portion ),
				v0.getY() + ( ( v1.getY() - v0.getY() ) * portion )
				);
		return rv;
	}
}
