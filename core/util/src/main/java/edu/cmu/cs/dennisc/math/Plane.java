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
public final class Plane {
	public static final Plane NaN = new Plane( Double.NaN, Double.NaN, Double.NaN, Double.NaN );
	public static final Plane XZ_PLANE = new Plane( 0, 1, 0, 0 );

	private final double a;
	private final double b;
	private final double c;
	private final double d;

	private Plane( double a, double b, double c, double d ) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	}

	public static Plane createInstance( double a, double b, double c, double d ) {
		return new Plane( a, b, c, d );
	}

	public static Plane createInstance( double[] array ) {
		return createInstance( array[ 0 ], array[ 1 ], array[ 2 ], array[ 3 ] );
	}

	//Kept private to avoid confusion on order
	private static Plane createInstance( double xPosition, double yPosition, double zPosition, double xNormal, double yNormal, double zNormal ) {
		final double EPSILON = 0.01;
		double magnitudeSquared = Tuple3.calculateMagnitudeSquared( xNormal, yNormal, zNormal );
		if( EpsilonUtilities.isWithinEpsilonOf1InSquaredSpace( magnitudeSquared, EPSILON ) ) {
			//pass
		} else {
			double magnitude = Math.sqrt( magnitudeSquared );
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( magnitude, xNormal, yNormal, zNormal );
			xNormal /= magnitude;
			yNormal /= magnitude;
			zNormal /= magnitude;
		}
		return createInstance( xNormal, yNormal, zNormal, -( ( xNormal * xPosition ) + ( yNormal * yPosition ) + ( zNormal * zPosition ) ) );
	}

	public static Plane createInstance( edu.cmu.cs.dennisc.math.Point3 position, edu.cmu.cs.dennisc.math.Vector3 normal ) {
		assert position.isNaN() == false;
		assert normal.isNaN() == false;
		return createInstance( position.x, position.y, position.z, normal.x, normal.y, normal.z );
	}

	public static Plane createInstance( edu.cmu.cs.dennisc.math.AffineMatrix4x4 m ) {
		assert m.isNaN() == false;
		return createInstance( m.translation.x, m.translation.y, m.translation.z, -m.orientation.backward.x, -m.orientation.backward.y, -m.orientation.backward.z );
	}

	public static Plane createInstance( edu.cmu.cs.dennisc.math.Point3 a, edu.cmu.cs.dennisc.math.Point3 b, edu.cmu.cs.dennisc.math.Point3 c ) {
		assert a.isNaN() == false;
		assert b.isNaN() == false;
		assert c.isNaN() == false;
		edu.cmu.cs.dennisc.math.Vector3 ac = edu.cmu.cs.dennisc.math.Vector3.createSubtraction( c, a );
		edu.cmu.cs.dennisc.math.Vector3 ab = edu.cmu.cs.dennisc.math.Vector3.createSubtraction( b, a );
		ac.normalize();
		ab.normalize();
		edu.cmu.cs.dennisc.math.Vector3 normal = edu.cmu.cs.dennisc.math.Vector3.createCrossProduct( ac, ab );
		return createInstance( a, normal );
	}

	@Override
	public boolean equals( Object o ) {
		if( o == this ) {
			return true;
		}
		if( ( o != null ) && ( o instanceof Plane ) ) {
			Plane plane = (Plane)o;
			return ( this.a == plane.a ) && ( this.b == plane.b ) && ( this.c == plane.c ) && ( this.d == plane.d );
		} else {
			return false;
		}
	}

	@Override
	public final int hashCode() {
		int rv = 17;
		long lng;

		lng = Double.doubleToLongBits( this.a );
		rv = ( 37 * rv ) + (int)( lng ^ ( lng >>> 32 ) );

		lng = Double.doubleToLongBits( this.b );
		rv = ( 37 * rv ) + (int)( lng ^ ( lng >>> 32 ) );

		lng = Double.doubleToLongBits( this.c );
		rv = ( 37 * rv ) + (int)( lng ^ ( lng >>> 32 ) );

		lng = Double.doubleToLongBits( this.d );
		rv = ( 37 * rv ) + (int)( lng ^ ( lng >>> 32 ) );

		return rv;
	}

	public boolean isNaN() {
		return Double.isNaN( this.a ) || Double.isNaN( this.b ) || Double.isNaN( this.c ) || Double.isNaN( this.d );
	}

	public double[] getEquation( double[] rv ) {
		rv[ 0 ] = this.a;
		rv[ 1 ] = this.b;
		rv[ 2 ] = this.c;
		rv[ 3 ] = this.d;
		return rv;
	}

	public double[] getEquation() {
		return getEquation( new double[ 4 ] );
	}

	public double intersect( Ray ray ) {
		edu.cmu.cs.dennisc.math.Point3 p = ray.getOrigin();
		edu.cmu.cs.dennisc.math.Vector3 d = ray.getDirection();
		double denom = ( this.a * d.x ) + ( this.b * d.y ) + ( this.c * d.z );
		if( denom == 0 ) {
			return Double.NaN;
		} else {
			double numer = ( this.a * p.x ) + ( this.b * p.y ) + ( this.c * p.z ) + this.d;
			return -numer / denom;
		}
	}

	public double evaluate( Point3 p ) {
		return ( this.a * p.x ) + ( this.b * p.y ) + ( this.c * p.z ) + this.d;
	}

	//	public LineD intersect( LineD rv, PlaneD other ) {
	//		throw new RuntimException( "todo" );
	//	}
	//	public LineD intersect( PlaneD other ) {
	//		return intersect( new LineD(), other );
	//	}

	//	public edu.cmu.cs.dennisc.math.Matrix4d getReflection( edu.cmu.cs.dennisc.math.Matrix4d rv ) {
	//		rv.setRow( 0, -2 * this.a * this.a + 1,   -2 * this.b * this.a,       -2 * this.c * this.a,       0.0 );
	//		rv.setRow( 1, -2 * this.a * this.b,       -2 * this.b * this.b + 1,   -2 * this.c * this.b,       0.0 );
	//		rv.setRow( 2, -2 * this.a * this.c,       -2 * this.b * this.c,       -2 * this.c * this.c + 1,   0.0 );
	//		rv.setRow( 3, -2 * this.a * this.d,       -2 * this.b * this.d,       -2 * this.c * this.d,       1.0 );
	//		return rv;
	//	}
	//	public edu.cmu.cs.dennisc.math.Matrix4d getReflection() {
	//		return getReflection( new edu.cmu.cs.dennisc.math.Matrix4d() );
	//	}
	@Override
	public String toString() {
		return "edu.cmu.cs.dennisc.math.Plane[a=" + this.a + ",b=" + this.b + ",c=" + this.c + ",d=" + this.d + "]";
	}

	public static Plane valueOf( String s ) {
		String[] markers = { "edu.cmu.cs.dennisc.math.Plane[a=", ",b=", ",c=", ",d=", "]" };
		double[] values = new double[ markers.length - 1 ];
		for( int i = 0; i < values.length; i++ ) {
			int begin = s.indexOf( markers[ i ] ) + markers[ i ].length();
			int end = s.indexOf( markers[ i + 1 ] );
			values[ i ] = Double.valueOf( s.substring( begin, end ) ).doubleValue();
		}
		return createInstance( values );
	}
}
