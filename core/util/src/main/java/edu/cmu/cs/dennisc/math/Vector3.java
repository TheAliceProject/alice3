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

import edu.cmu.cs.dennisc.math.immutable.MVector3;

/**
 * @author Dennis Cosgrove
 */
public class Vector3 extends Tuple3 {
	public Vector3() {
	}

	public Vector3( Tuple3 other ) {
		super( other );
	}

	public Vector3( double x, double y, double z ) {
		super( x, y, z );
	}

	public Vector3( MVector3 other ) {
		this( other.x, other.y, other.z );
	}

	public static Vector3 createZero() {
		return (Vector3)setReturnValueToZero( new Vector3() );
	}

	public static Vector3 createNaN() {
		return (Vector3)setReturnValueToNaN( new Vector3() );
	}

	public static Vector3 createAddition( Tuple3 a, Tuple3 b ) {
		return (Vector3)setReturnValueToAddition( new Vector3(), a, b );
	}

	public static Vector3 createSubtraction( Tuple3 a, Tuple3 b ) {
		return (Vector3)setReturnValueToSubtraction( new Vector3(), a, b );
	}

	public static Vector3 createNegation( Tuple3 a ) {
		return (Vector3)setReturnValueToNegation( new Vector3(), a );
	}

	public static Vector3 createMultiplication( Tuple3 a, Tuple3 b ) {
		return (Vector3)setReturnValueToMultiplication( new Vector3(), a, b );
	}

	public static Vector3 createMultiplication( Tuple3 a, double b ) {
		return (Vector3)setReturnValueToMultiplication( new Vector3(), a, b );
	}

	public static Vector3 createDivision( Tuple3 a, Tuple3 b ) {
		return (Vector3)setReturnValueToDivision( new Vector3(), a, b );
	}

	public static Vector3 createDivision( Tuple3 a, double b ) {
		return (Vector3)setReturnValueToDivision( new Vector3(), a, b );
	}

	public static Vector3 createInterpolation( Tuple3 a, Tuple3 b, double portion ) {
		return (Vector3)setReturnValueToInterpolation( new Vector3(), a, b, portion );
	}

	public static Vector3 createNormalized( Tuple3 a ) {
		return (Vector3)setReturnValueToNormalized( new Vector3(), a );
	}

	public static double calculateDotProduct( Vector3 a, Vector3 b ) {
		return ( a.x * b.x ) + ( a.y * b.y ) + ( a.z * b.z );
	}

	@Deprecated
	public static double calculateCrossProductX( double ax, double ay, double az, double bx, double by, double bz ) {
		return ( ay * bz ) - ( az * by );
	}

	@Deprecated
	public static double calculateCrossProductY( double ax, double ay, double az, double bx, double by, double bz ) {
		return ( bx * az ) - ( bz * ax );
	}

	@Deprecated
	public static double calculateCrossProductZ( double ax, double ay, double az, double bx, double by, double bz ) {
		return ( ax * by ) - ( ay * bx );
	}

	public static Vector3 setReturnValueToCrossProduct( Vector3 rv, Vector3 a, Vector3 b ) {
		double x = ( a.y * b.z ) - ( a.z * b.y );
		double y = ( b.x * a.z ) - ( b.z * a.x );
		double z = ( a.x * b.y ) - ( a.y * b.x );
		rv.x = x;
		rv.y = y;
		rv.z = z;
		return rv;
	}

	public void setToCrossProduct( Vector3 a, Vector3 b ) {
		setReturnValueToCrossProduct( this, a, b );
	}

	public static Vector3 createCrossProduct( Vector3 a, Vector3 b ) {
		return setReturnValueToCrossProduct( new Vector3(), a, b );
	}

	@Deprecated
	public static boolean isWithinEpsilonOfPositiveOrNegativeYAxis( Vector3 v, double epsilon ) {
		return EpsilonUtilities.isWithinEpsilon( Math.abs( v.y ), 1.0, epsilon ) && EpsilonUtilities.isWithinEpsilon( v.x, 0.0, epsilon )
				&& EpsilonUtilities.isWithinEpsilon( v.z, 0.0, epsilon );
	}

	private static final Vector3 ORIGIN = new Vector3( 0, 0, 0 );
	private static final Vector3 POSITIVE_X_AXIS = Vector3.createPositiveXAxis();
	private static final Vector3 POSITIVE_Y_AXIS = Vector3.createPositiveYAxis();
	private static final Vector3 POSITIVE_Z_AXIS = Vector3.createPositiveZAxis();
	private static final Vector3 NEGATIVE_X_AXIS = Vector3.createNegativeXAxis();
	private static final Vector3 NEGATIVE_Y_AXIS = Vector3.createNegativeYAxis();
	private static final Vector3 NEGATIVE_Z_AXIS = Vector3.createNegativeZAxis();

	public static Vector3 accessOrigin() {
		ORIGIN.x = ORIGIN.y = ORIGIN.z = 0.0;
		return ORIGIN;
	}

	public static Vector3 accessPositiveXAxis() {
		POSITIVE_X_AXIS.set( 1, 0, 0 );
		return POSITIVE_X_AXIS;
	}

	public static Vector3 accessPositiveYAxis() {
		POSITIVE_Y_AXIS.set( 0, 1, 0 );
		return POSITIVE_Y_AXIS;
	}

	public static Vector3 accessPositiveZAxis() {
		POSITIVE_Z_AXIS.set( 0, 0, 1 );
		return POSITIVE_Z_AXIS;
	}

	public static Vector3 accessNegativeXAxis() {
		NEGATIVE_X_AXIS.set( -1, 0, 0 );
		return NEGATIVE_X_AXIS;
	}

	public static Vector3 accessNegativeYAxis() {
		NEGATIVE_Y_AXIS.set( 0, -1, 0 );
		return NEGATIVE_Y_AXIS;
	}

	public static Vector3 accessNegativeZAxis() {
		NEGATIVE_Z_AXIS.set( 0, 0, -1 );
		return NEGATIVE_Z_AXIS;
	}

	public static Vector3 createPositiveXAxis() {
		return new Vector3( 1, 0, 0 );
	}

	public static Vector3 createPositiveYAxis() {
		return new Vector3( 0, 1, 0 );
	}

	public static Vector3 createPositiveZAxis() {
		return new Vector3( 0, 0, 1 );
	}

	public static Vector3 createNegativeXAxis() {
		return new Vector3( -1, 0, 0 );
	}

	public static Vector3 createNegativeYAxis() {
		return new Vector3( 0, -1, 0 );
	}

	public static Vector3 createNegativeZAxis() {
		return new Vector3( 0, 0, -1 );
	}

	public boolean isPositiveXAxis() {
		return this.equals( accessPositiveXAxis() );
	}

	public boolean isPositiveYAxis() {
		return this.equals( accessPositiveYAxis() );
	}

	public boolean isPositiveZAxis() {
		return this.equals( accessPositiveZAxis() );
	}

	public boolean isNegativeXAxis() {
		return this.equals( accessNegativeXAxis() );
	}

	public boolean isNegativeYAxis() {
		return this.equals( accessNegativeYAxis() );
	}

	public boolean isNegativeZAxis() {
		return this.equals( accessNegativeZAxis() );
	}

	public MVector3 createImmutable() {
		return new MVector3( this.x, this.y, this.z );
	}

	//	
	//	public VectorD3() {
	//	}
	//	public VectorD3( VectorD3 other ) {
	//		set( other );
	//	}
	//	public VectorD3( PointD3 other ) {
	//		set( other );
	//	}
	//	public VectorD3( double x, double y, double z ) {
	//		set( x, y, z );
	//	}
	//
	//	public static VectorD3 createFromNaN() {
	//		return (VectorD3)setReturnValueToNaN( new VectorD3() );
	//	}
	//	
	//	
	//	public static double calculateDotProduct( VectorD3 a, VectorD3 b ) {
	//		return a.x * b.x + a.y * b.y + a.z * b.z;
	//	}
	//	public void setToCrossProduct( VectorD3 a, VectorD3 b ) {
	//		setReturnValueToCrossProduct( this, a, b );
	//	}
	//	public static VectorD3 setReturnValueToCrossProduct( VectorD3 rv, VectorD3 a, VectorD3 b ) {
	//		double x = a.y*b.z - a.z*b.y;
	//		double y = b.x*a.z - b.z*a.x;
	//		double z = a.x*b.y - a.y*b.x;
	//		rv.x = x;
	//		rv.y = y;
	//		rv.z = z;
	//		return rv;
	//	}
	//	public static VectorD3 createFromCrossProduct( VectorD3 a, VectorD3 b ) {
	//		return setReturnValueToCrossProduct( new VectorD3(), a, b );
	//	}
	//
	//	public static double calculateCrossProductX( double ax, double ay, double az, double bx, double by, double bz ) {
	//		return ay*bz - az*by;
	//	}
	//	public static double calculateCrossProductY( double ax, double ay, double az, double bx, double by, double bz ) {
	//		return bx*az - bz*ax;
	//	}
	//	public static double calculateCrossProductZ( double ax, double ay, double az, double bx, double by, double bz ) {
	//		return ax*by - ay*bx;
	//	}
	//	
	//	public static VectorD3 createFromAdd( VectorD3 rv, TupleD3 a, TupleD3 b ) {
	//		rv.set( a.x + b.x, a.y + b.y, a.z + b.z );
	//		return rv;
	//	}
	//	public static VectorD3 createFromAdd( TupleD3 a, TupleD3 b ) {
	//		return createFromAdd( new VectorD3(), a, b );
	//	}
	//
	//	public static VectorD3 createFromSubtract( VectorD3 rv, TupleD3 a, TupleD3 b ) {
	//		rv.set( a.x - b.x, a.y - b.y, a.z - b.z );
	//		return rv;
	//	}
	//	public static VectorD3 createFromSubtract( TupleD3 a, TupleD3 b ) {
	//		return createFromSubtract( new VectorD3(), a, b );
	//	}
	//	public static VectorD3 createFromProduct( VectorD3 rv, double scalar, TupleD3 v ) {
	//		rv.set( v );
	//		rv.scale( scalar );
	//		return rv;
	//	}
	//	public static VectorD3 createFromProduct( double scalar, TupleD3 v ) {
	//		return createFromProduct( new VectorD3(), scalar, v );
	//	}
	//	public static VectorD3 createFromProduct( VectorD3 rv, TupleD3 a, TupleD3 b ) {
	//		rv.set( a.x * b.x, a.y * b.y, a.z * b.z );
	//		return rv;
	//	}
	//	public static VectorD3 createFromProduct( TupleD3 a, TupleD3 b ) {
	//		return createFromProduct( new VectorD3(), a, b );
	//	}
	//
	//	public static VectorD3 createFromDivide( VectorD3 rv, TupleD3 numerator, TupleD3 divisor ) {
	//		rv.set( numerator.x / divisor.x, numerator.y / divisor.y, numerator.z / divisor.z );
	//		return rv;
	//	}
	//	public static VectorD3 createFromDivide( TupleD3 numerator, TupleD3 divisor ) {
	//		return createFromDivide( new VectorD3(), numerator, divisor );
	//	}
	//
	//	public static VectorD3 createFromDivide( VectorD3 rv, TupleD3 numerator, double divisor ) {
	//		rv.set( numerator.x / divisor, numerator.y, numerator.z );
	//		return rv;
	//	}
	//	public static VectorD3 createFromDivide( TupleD3 numerator, double divisor ) {
	//		return createFromDivide( new VectorD3(), numerator, divisor );
	//	}
	//
	//	@Deprecated
	//	public static boolean isWithinEpsilonOfPositiveOrNegativeYAxis( VectorD3 v, double epsilon ) {
	//		return 
	//			edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinEpsilon( Math.abs( v.y ), 1.0, epsilon ) && 
	//			edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinEpsilon( v.x, 0.0, epsilon ) && 
	//			edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinEpsilon( v.z, 0.0, epsilon ); 
	//	}
	//	
	//	public static VectorD3 setReturnValueToNegative( VectorD3 rv, VectorD3 other ) {
	//		rv.x = -other.x;
	//		rv.y = -other.y;
	//		rv.z = -other.z;
	//		return rv;
	//	}
	//	public static VectorD3 createFromNegative( VectorD3 a ) {
	//		return setReturnValueToNegative( new VectorD3(), a );
	//	}
	//
	//	public void setToNegative( VectorD3 other ) {
	//		setReturnValueToNegative( this, other );
	//	}
	//	public void setToNegative() {
	//		setToNegative( this );
	//	}
	//	public VectorD3 createNegative() {
	//		return createFromNegative( this );
	//	}
}
