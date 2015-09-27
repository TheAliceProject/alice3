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
@Deprecated
public class InterpolationUtilities {
	public static Vector3 interpolate( Vector3 rv, Vector3 a, Vector3 b, double portion ) {
		rv.setToInterpolation( a, b, portion );
		return rv;
	}

	public static Vector3 interpolate( Vector3 a, Vector3 b, double portion ) {
		return interpolate( new Vector3(), a, b, portion );
	}

	public static Vector4 interpolate( Vector4 rv, Vector4 a, Vector4 b, double portion ) {
		rv.setToInterpolation( a, b, portion );
		return rv;
	}

	public static Vector4 interpolate( Vector4 a, Vector4 b, double portion ) {
		return interpolate( new Vector4(), a, b, portion );
	}

	public static Point2f interpolate( Point2f rv, Point2f a, Point2f b, float portion ) {
		rv.setToInterpolation( a, b, portion );
		return rv;
	}

	public static Point2f interpolate( Point2f a, Point2f b, float portion ) {
		return interpolate( new Point2f(), a, b, portion );
	}

	public static Point2 interpolate( Point2 rv, Point2 a, Point2 b, double portion ) {
		rv.setToInterpolation( a, b, portion );
		return rv;
	}

	public static Point2 interpolate( Point2 a, Point2 b, double portion ) {
		return interpolate( new Point2(), a, b, portion );
	}

	public static Point3 interpolate( Point3 rv, Point3 a, Point3 b, double portion ) {
		rv.setToInterpolation( a, b, portion );
		return rv;
	}

	public static Point3 interpolate( Point3 a, Point3 b, double portion ) {
		return interpolate( new Point3(), a, b, portion );
	}

	public static UnitQuaternion interpolate( UnitQuaternion rv, UnitQuaternion a, UnitQuaternion b, double portion ) {
		rv.setToInterpolation( a, b, portion );
		return rv;
	}

	public static UnitQuaternion interpolate( UnitQuaternion a, UnitQuaternion b, double portion ) {
		return interpolate( UnitQuaternion.createNaN(), a, b, portion );
	}

	public static OrthogonalMatrix3x3 interpolate( OrthogonalMatrix3x3 rv, OrthogonalMatrix3x3 a, OrthogonalMatrix3x3 b, double portion ) {
		UnitQuaternion qa = new UnitQuaternion( a );
		UnitQuaternion qb = new UnitQuaternion( b );
		rv.setValue( interpolate( qa, qb, portion ) );
		return rv;
	}

	public static OrthogonalMatrix3x3 interpolate( OrthogonalMatrix3x3 a, OrthogonalMatrix3x3 b, double portion ) {
		return interpolate( OrthogonalMatrix3x3.createNaN(), a, b, portion );
	}

	public static AffineMatrix4x4 interpolate( AffineMatrix4x4 rv, AffineMatrix4x4 a, AffineMatrix4x4 b, double portion ) {
		UnitQuaternion qa = new UnitQuaternion( a.orientation );
		UnitQuaternion qb = new UnitQuaternion( b.orientation );

		rv.set( interpolate( qa, qb, portion ), interpolate( a.translation, b.translation, portion ) );
		return rv;
	}

	public static AffineMatrix4x4 interpolate( AffineMatrix4x4 a, AffineMatrix4x4 b, double portion ) {
		return interpolate( AffineMatrix4x4.createNaN(), a, b, portion );
	}

	public static edu.cmu.cs.dennisc.color.Color4f interpolate( edu.cmu.cs.dennisc.color.Color4f a, edu.cmu.cs.dennisc.color.Color4f b, double portion ) {
		return edu.cmu.cs.dennisc.color.Color4f.createInterpolation( a, b, (float)portion );
	}

	public static double interpolate( double a, double b, double portion ) {
		return a + ( ( b - a ) * portion );
	}

	public static float interpolate( float a, float b, double portion ) {
		return a + ( ( b - a ) * (float)portion );
	}

	public static int interpolate( int a, int b, double portion ) {
		return (int)( ( a + ( ( b - a ) * portion ) ) + 0.5 );
	}

	public static Object interpolate( Object a, Object b, double portion ) {
		if( a instanceof Vector3 ) {
			return interpolate( (Vector3)a, (Vector3)b, portion );
		} else if( a instanceof UnitQuaternion ) {
			return interpolate( (UnitQuaternion)a, (UnitQuaternion)b, portion );
		} else if( a instanceof edu.cmu.cs.dennisc.color.Color4f ) {
			return interpolate( (edu.cmu.cs.dennisc.color.Color4f)a, (edu.cmu.cs.dennisc.color.Color4f)b, portion );
		} else if( a instanceof Double ) {
			return interpolate( ( (Double)a ).doubleValue(), ( (Double)b ).doubleValue(), portion );
		} else {
			if( portion > 0 ) {
				return b;
			} else {
				return a;
			}
		}
	}

}
