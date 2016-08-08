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
public final class Vector3f extends Tuple3f {
	public Vector3f() {
	}

	public Vector3f( Tuple3f other ) {
		super( other );
	}

	public Vector3f( float x, float y, float z ) {
		super( x, y, z );
	}

	public static Vector3f createZero() {
		return (Vector3f)setReturnValueToZero( new Vector3f() );
	}

	public static Vector3f createNaN() {
		return (Vector3f)setReturnValueToNaN( new Vector3f() );
	}

	public static Vector3f createAddition( Tuple3f a, Tuple3f b ) {
		return (Vector3f)setReturnValueToAddition( new Vector3f(), a, b );
	}

	public static Vector3f createSubtraction( Tuple3f a, Tuple3f b ) {
		return (Vector3f)setReturnValueToSubtraction( new Vector3f(), a, b );
	}

	public static Vector3f createNegation( Tuple3f a ) {
		return (Vector3f)setReturnValueToNegation( new Vector3f(), a );
	}

	public static Vector3f createMultiplication( Tuple3f a, Tuple3f b ) {
		return (Vector3f)setReturnValueToMultiplication( new Vector3f(), a, b );
	}

	public static Vector3f createMultiplication( Tuple3f a, float b ) {
		return (Vector3f)setReturnValueToMultiplication( new Vector3f(), a, b );
	}

	public static Vector3f createDivision( Tuple3f a, Tuple3f b ) {
		return (Vector3f)setReturnValueToDivision( new Vector3f(), a, b );
	}

	public static Vector3f createDivision( Tuple3f a, float b ) {
		return (Vector3f)setReturnValueToDivision( new Vector3f(), a, b );
	}

	public static Vector3f createInterpolation( Tuple3f a, Tuple3f b, float portion ) {
		return (Vector3f)setReturnValueToInterpolation( new Vector3f(), a, b, portion );
	}

	public static Vector3f createNormalized( Tuple3f a ) {
		return (Vector3f)setReturnValueToNormalized( new Vector3f(), a );
	}

	public static float calculateDotProduct( Vector3f a, Vector3f b ) {
		return ( a.x * b.x ) + ( a.y * b.y ) + ( a.z * b.z );
	}

	@Deprecated
	public static float calculateCrossProductX( float ax, float ay, float az, float bx, float by, float bz ) {
		return ( ay * bz ) - ( az * by );
	}

	@Deprecated
	public static float calculateCrossProductY( float ax, float ay, float az, float bx, float by, float bz ) {
		return ( bx * az ) - ( bz * ax );
	}

	@Deprecated
	public static float calculateCrossProductZ( float ax, float ay, float az, float bx, float by, float bz ) {
		return ( ax * by ) - ( ay * bx );
	}

	public static Vector3f setReturnValueToCrossProduct( Vector3f rv, Vector3f a, Vector3f b ) {
		float x = ( a.y * b.z ) - ( a.z * b.y );
		float y = ( b.x * a.z ) - ( b.z * a.x );
		float z = ( a.x * b.y ) - ( a.y * b.x );
		rv.x = x;
		rv.y = y;
		rv.z = z;
		return rv;
	}

	public void setToCrossProduct( Vector3f a, Vector3f b ) {
		setReturnValueToCrossProduct( this, a, b );
	}

	public static Vector3f createFromCrossProduct( Vector3f a, Vector3f b ) {
		return setReturnValueToCrossProduct( new Vector3f(), a, b );
	}

	@Deprecated
	public static boolean isWithinEpsilonOfPositiveOrNegativeYAxis( Vector3f v, float epsilon ) {
		return edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinEpsilon( Math.abs( v.y ), 1.0, epsilon ) && edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinEpsilon( v.x, 0.0, epsilon )
				&& edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinEpsilon( v.z, 0.0, epsilon );
	}

	private static final Vector3f ORIGIN = new Vector3f( 0, 0, 0 );
	private static final Vector3f POSITIVE_X_AXIS = Vector3f.createPositiveXAxis();
	private static final Vector3f POSITIVE_Y_AXIS = Vector3f.createPositiveYAxis();
	private static final Vector3f POSITIVE_Z_AXIS = Vector3f.createPositiveZAxis();
	private static final Vector3f NEGATIVE_X_AXIS = Vector3f.createNegativeXAxis();
	private static final Vector3f NEGATIVE_Y_AXIS = Vector3f.createNegativeYAxis();
	private static final Vector3f NEGATIVE_Z_AXIS = Vector3f.createNegativeZAxis();

	public static Vector3f accessOrigin() {
		ORIGIN.x = ORIGIN.y = ORIGIN.z = 0.0f;
		return ORIGIN;
	}

	public static Vector3f accessPositiveXAxis() {
		POSITIVE_X_AXIS.set( 1, 0, 0 );
		return POSITIVE_X_AXIS;
	}

	public static Vector3f accessPositiveYAxis() {
		POSITIVE_Y_AXIS.set( 0, 1, 0 );
		return POSITIVE_Y_AXIS;
	}

	public static Vector3f accessPositiveZAxis() {
		POSITIVE_Z_AXIS.set( 0, 0, 1 );
		return POSITIVE_Z_AXIS;
	}

	public static Vector3f accessNegativeXAxis() {
		NEGATIVE_X_AXIS.set( -1, 0, 0 );
		return NEGATIVE_X_AXIS;
	}

	public static Vector3f accessNegativeYAxis() {
		NEGATIVE_Y_AXIS.set( 0, -1, 0 );
		return NEGATIVE_Y_AXIS;
	}

	public static Vector3f accessNegativeZAxis() {
		NEGATIVE_Z_AXIS.set( 0, 0, -1 );
		return NEGATIVE_Z_AXIS;
	}

	public static Vector3f createPositiveXAxis() {
		return new Vector3f( 1, 0, 0 );
	}

	public static Vector3f createPositiveYAxis() {
		return new Vector3f( 0, 1, 0 );
	}

	public static Vector3f createPositiveZAxis() {
		return new Vector3f( 0, 0, 1 );
	}

	public static Vector3f createNegativeXAxis() {
		return new Vector3f( -1, 0, 0 );
	}

	public static Vector3f createNegativeYAxis() {
		return new Vector3f( 0, -1, 0 );
	}

	public static Vector3f createNegativeZAxis() {
		return new Vector3f( 0, 0, -1 );
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
}
