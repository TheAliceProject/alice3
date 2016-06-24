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
package edu.cmu.cs.dennisc.math;

/**
 * @author Dennis Cosgrove
 */
public class ClippedZPlane {
	private static final double DEFAULT_HALF_HEIGHT = 0.1;
	private final Point2 center = new Point2( 0, 0 );
	private double halfWidth = Double.NaN;
	private double halfHeight = DEFAULT_HALF_HEIGHT;

	public ClippedZPlane() {
	}

	public ClippedZPlane( ClippedZPlane other ) {
		set( other );
	}

	public ClippedZPlane( ClippedZPlane other, edu.cmu.cs.dennisc.math.immutable.MRectangleI viewport ) {
		set( other, viewport );
	}

	public static ClippedZPlane createNaN() {
		ClippedZPlane rv = new ClippedZPlane();
		rv.center.setNaN();
		rv.halfWidth = Double.NaN;
		rv.halfHeight = Double.NaN;
		return rv;
	}

	public void set( ClippedZPlane other ) {
		this.center.set( other.center );
		this.halfWidth = other.halfWidth;
		this.halfHeight = other.halfHeight;
	}

	public void set( ClippedZPlane other, edu.cmu.cs.dennisc.math.immutable.MRectangleI viewport ) {
		this.set( other );
		if( Double.isNaN( this.halfWidth ) ) {
			if( Double.isNaN( this.halfHeight ) ) {
				this.halfHeight = DEFAULT_HALF_HEIGHT;
			}
			double factor = viewport.width / (double)viewport.height;
			this.halfWidth = factor * this.halfHeight;
		} else {
			if( Double.isNaN( this.halfHeight ) ) {
				double factor = viewport.height / (double)viewport.width;
				this.halfHeight = factor * this.halfWidth;
			}
		}
	}

	public Point2 getCenter( Point2 rv ) {
		rv.set( this.center );
		return rv;
	}

	public Point2 getCenter() {
		return this.getCenter( Point2.createNaN() );
	}

	public void setCenter( double x, double y ) {
		this.center.set( x, y );
	}

	public void setCenter( Point2 center ) {
		this.center.set( center );
	}

	public double getWidth() {
		return this.halfWidth * 2.0;
	}

	public void setWidth( double width ) {
		this.halfWidth = width * 0.5;
	}

	public double getHeight() {
		return this.halfHeight * 2.0;
	}

	public void setHeight( double height ) {
		this.halfHeight = height * 0.5;
	}

	public double getXMinimum() {
		return this.center.x - this.halfWidth;
	}

	public double getXMaximum() {
		return this.center.x + this.halfWidth;
	}

	public double getYMinimum() {
		return this.center.y - this.halfHeight;
	}

	public double getYMaximum() {
		return this.center.y + this.halfHeight;
	}
}
