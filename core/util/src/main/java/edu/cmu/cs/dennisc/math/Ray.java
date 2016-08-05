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
public final class Ray {
	private final Point3 origin = new Point3( 0, 0, 0 );
	private final Vector3 direction = new Vector3( 0, 0, 1 );

	public Ray() {
	}

	public Ray( Point3 origin, Vector3 direction ) {
		this.origin.set( origin );
		this.direction.set( direction );
	}

	public Ray( Ray other ) {
		this( other.origin, other.direction );
	}

	@Override
	public boolean equals( Object o ) {
		if( o == this ) {
			return true;
		}
		if( o instanceof Ray ) {
			Ray ray = (Ray)o;
			return this.origin.equals( ray.origin ) && this.direction.equals( ray.direction );
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		int rv = 17;
		if( this.origin != null ) {
			rv = ( 37 * rv ) + this.origin.hashCode();
		}
		if( this.direction != null ) {
			rv = ( 37 * rv ) + this.direction.hashCode();
		}
		return rv;
	}

	public boolean isNaN() {
		return this.origin.isNaN() || this.direction.isNaN();
	}

	public void setNaN() {
		this.origin.setNaN();
		this.direction.setNaN();
	}

	public Point3 accessOrigin() {
		return this.origin;
	}

	public Point3 getOrigin( Point3 rv ) {
		rv.set( this.origin );
		return rv;
	}

	public Point3 getOrigin() {
		return getOrigin( new Point3() );
	}

	public void setOrigin( Point3 origin ) {
		if( origin != null ) {
			this.setOrigin( origin.x, origin.y, origin.z );
		} else {
			this.setOrigin( Double.NaN, Double.NaN, Double.NaN );
		}
	}

	public void setOrigin( double x, double y, double z ) {
		this.origin.set( x, y, z );
	}

	public Vector3 accessDirection() {
		return this.direction;
	}

	public Vector3 getDirection( Vector3 rv ) {
		rv.set( this.direction );
		return rv;
	}

	public Vector3 getDirection() {
		return getDirection( new Vector3() );
	}

	public void setDirection( Vector3 direction ) {
		if( direction != null ) {
			this.setDirection( direction.x, direction.y, direction.z );
		} else {
			this.setDirection( Double.NaN, Double.NaN, Double.NaN );
		}
	}

	public void setDirection( double x, double y, double z ) {
		this.direction.set( x, y, z );
	}

	public Point3 getPointAlong( Point3 rv, double t ) {
		rv.set( this.direction );
		rv.multiply( t );
		rv.add( this.origin );
		return rv;
	}

	public Point3 getPointAlong( double t ) {
		return getPointAlong( new Point3(), t );
	}

	public double getProjectedPointT( Point3 p ) {
		Vector3 toPoint = Vector3.createSubtraction( p, this.origin );
		double dot = Vector3.calculateDotProduct( toPoint, this.direction );
		return dot;
	}

	public Point3 getProjectedPoint( Point3 p ) {
		Vector3 toPoint = Vector3.createSubtraction( p, this.origin );
		double dot = Vector3.calculateDotProduct( toPoint, this.direction );
		return getPointAlong( dot );
	}

	public void transform( AffineMatrix4x4 m ) {
		m.transform( this.origin );
		m.transform( this.direction );
		//		Vector4d transformedOrigin = LinearAlgebra.multiply( this.origin.x, this.origin.y, this.origin.z, 1, m );
		//		this.origin = new Point3d( transformedOrigin.x/transformedOrigin.w, transformedOrigin.y/transformedOrigin.w, transformedOrigin.z/transformedOrigin.w );
		//		Vector4d transformedDirection = LinearAlgebra.multiply( this.direction, 0, m );
		//		transformedDirection.w = 1;
		//		this.direction = LinearAlgebra.newVector3d( transformedDirection );
	}

	public edu.cmu.cs.dennisc.math.immutable.MRay createImmutable() {
		return new edu.cmu.cs.dennisc.math.immutable.MRay( this.origin.createImmutable(), this.direction.createImmutable() );
	}

	@Override
	public String toString() {
		return "Ray[origin=" + this.origin + ",direction=" + this.direction + "]";
	}
}
