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
public final class Matrix4x4 extends AbstractMatrix4x4 implements edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable {
	public final Vector4 right = new Vector4( 1, 0, 0, 0 );
	public final Vector4 up = new Vector4( 0, 1, 0, 0 );
	public final Vector4 backward = new Vector4( 0, 0, 1, 0 );
	public final Vector4 translation = new Vector4( 0, 0, 0, 1 );

	public Matrix4x4() {
	}

	public Matrix4x4( Matrix4x4 other ) {
		set( other );
	}

	public Matrix4x4( AffineMatrix4x4 other ) {
		set( other );
	}

	public Matrix4x4( double m00, double m01, double m02, double m03, double m10, double m11, double m12, double m13, double m20, double m21, double m22, double m23, double m30, double m31, double m32, double m33 ) {
		set( m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33 );
	}

	public Matrix4x4( double[] vals )
	{
		if( vals.length == 12 )
		{
			right.x = vals[ 0 ];
			right.y = vals[ 1 ];
			right.z = vals[ 2 ];
			up.x = vals[ 3 ];
			up.y = vals[ 4 ];
			up.z = vals[ 5 ];
			backward.x = vals[ 6 ];
			backward.y = vals[ 7 ];
			backward.z = vals[ 8 ];
			translation.x = vals[ 9 ];
			translation.y = vals[ 10 ];
			translation.z = vals[ 11 ];
		}
		else if( vals.length == 16 )
		{
			right.x = vals[ 0 ];
			right.y = vals[ 1 ];
			right.z = vals[ 2 ];
			right.w = vals[ 3 ];
			up.x = vals[ 4 ];
			up.y = vals[ 5 ];
			up.z = vals[ 6 ];
			up.w = vals[ 7 ];
			backward.x = vals[ 8 ];
			backward.y = vals[ 9 ];
			backward.z = vals[ 10 ];
			backward.w = vals[ 11 ];
			translation.x = vals[ 12 ];
			translation.y = vals[ 13 ];
			translation.z = vals[ 14 ];
			translation.w = vals[ 15 ];
		}
	}

	public void decode( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		right.decode( binaryDecoder );
		up.decode( binaryDecoder );
		backward.decode( binaryDecoder );
		translation.decode( binaryDecoder );
	}

	@Override
	public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		right.encode( binaryEncoder );
		up.encode( binaryEncoder );
		backward.encode( binaryEncoder );
		translation.encode( binaryEncoder );
	}

	public boolean isWithinEpsilonOf( Matrix4x4 other, double epsilon ) {
		return right.isWithinEpsilonOf( other.right, epsilon ) && up.isWithinEpsilonOf( other.up, epsilon ) && backward.isWithinEpsilonOf( other.backward, epsilon ) && translation.isWithinEpsilonOf( other.translation, epsilon );
	}

	public boolean isWithinReasonableEpsilonOf( Matrix4x4 other ) {
		return isWithinEpsilonOf( other, EpsilonUtilities.REASONABLE_EPSILON );
	}

	@Override
	public boolean isAffine() {
		return ( right.w == 0.0 ) && ( up.w == 0.0 ) && ( backward.w == 0.0 ) && ( translation.w == 1.0 );
	}

	public boolean isWithinEpsilonOfAffine( double epsilon ) {
		return EpsilonUtilities.isWithinEpsilon( right.w, 0.0, epsilon ) && EpsilonUtilities.isWithinEpsilon( up.w, 0.0, epsilon ) && EpsilonUtilities.isWithinEpsilon( backward.w, 0.0, epsilon ) && EpsilonUtilities.isWithinEpsilon( translation.w, 1.0, epsilon );
	}

	public boolean isWithinReasonableEpsilonOfAffine() {
		return isWithinEpsilonOfAffine( EpsilonUtilities.REASONABLE_EPSILON );
	}

	@Override
	public double[] getAsColumnMajorArray16( double[] rv ) {
		assert rv.length == 16;
		rv[ 0 ] = right.x;
		rv[ 1 ] = right.y;
		rv[ 2 ] = right.z;
		rv[ 3 ] = right.w;
		rv[ 4 ] = up.x;
		rv[ 5 ] = up.y;
		rv[ 6 ] = up.z;
		rv[ 7 ] = up.w;
		rv[ 8 ] = backward.x;
		rv[ 9 ] = backward.y;
		rv[ 10 ] = backward.z;
		rv[ 11 ] = backward.w;
		rv[ 12 ] = translation.x;
		rv[ 13 ] = translation.y;
		rv[ 14 ] = translation.z;
		rv[ 15 ] = translation.w;
		return rv;
	}

	//NaN
	public static Matrix4x4 setReturnValueToNaN( Matrix4x4 rv ) {
		rv.right.setNaN();
		rv.up.setNaN();
		rv.backward.setNaN();
		rv.translation.setNaN();
		return rv;
	}

	public static Matrix4x4 createNaN() {
		return setReturnValueToNaN( new Matrix4x4() );
	}

	@Override
	public void setNaN() {
		setReturnValueToNaN( this );
	}

	@Override
	public boolean isNaN() {
		return right.isNaN() || up.isNaN() || backward.isNaN() || translation.isNaN();
	}

	@Override
	public Appendable append( Appendable rv, java.text.DecimalFormat decimalFormat, boolean isLines ) throws java.io.IOException {
		if( isLines ) {
			int n = decimalFormat.format( 0.0 ).length() + 1;
			rv.append( "+-" );
			for( int i = 0; i < 4; i++ ) {
				for( int j = 0; j < n; j++ ) {
					rv.append( ' ' );
				}
			}
			rv.append( "-+\n" );

			rv.append( "| " );
			rv.append( decimalFormat.format( this.right.x ) );
			rv.append( ' ' );
			rv.append( decimalFormat.format( this.up.x ) );
			rv.append( ' ' );
			rv.append( decimalFormat.format( this.backward.x ) );
			rv.append( ' ' );
			rv.append( decimalFormat.format( this.translation.x ) );
			rv.append( "  |\n" );

			rv.append( "| " );
			rv.append( decimalFormat.format( this.right.y ) );
			rv.append( ' ' );
			rv.append( decimalFormat.format( this.up.y ) );
			rv.append( ' ' );
			rv.append( decimalFormat.format( this.backward.y ) );
			rv.append( ' ' );
			rv.append( decimalFormat.format( this.translation.y ) );
			rv.append( "  |\n" );

			rv.append( "| " );
			rv.append( decimalFormat.format( this.right.z ) );
			rv.append( ' ' );
			rv.append( decimalFormat.format( this.up.z ) );
			rv.append( ' ' );
			rv.append( decimalFormat.format( this.backward.z ) );
			rv.append( ' ' );
			rv.append( decimalFormat.format( this.translation.z ) );
			rv.append( "  |\n" );

			rv.append( "| " );
			rv.append( decimalFormat.format( this.right.w ) );
			rv.append( ' ' );
			rv.append( decimalFormat.format( this.up.w ) );
			rv.append( ' ' );
			rv.append( decimalFormat.format( this.backward.w ) );
			rv.append( ' ' );
			rv.append( decimalFormat.format( this.translation.w ) );
			rv.append( "  |\n" );

			rv.append( "+-" );
			for( int i = 0; i < 4; i++ ) {
				for( int j = 0; j < n; j++ ) {
					rv.append( ' ' );
				}
			}
			rv.append( "-+\n" );
		} else {
			rv.append( "[ " );
			rv.append( decimalFormat.format( this.right.x ) );
			rv.append( ' ' );
			rv.append( decimalFormat.format( this.up.x ) );
			rv.append( ' ' );
			rv.append( decimalFormat.format( this.backward.x ) );
			rv.append( ' ' );
			rv.append( decimalFormat.format( this.translation.x ) );
			rv.append( "  ] " );

			rv.append( "[ " );
			rv.append( decimalFormat.format( this.right.y ) );
			rv.append( ' ' );
			rv.append( decimalFormat.format( this.up.y ) );
			rv.append( ' ' );
			rv.append( decimalFormat.format( this.backward.y ) );
			rv.append( ' ' );
			rv.append( decimalFormat.format( this.translation.y ) );
			rv.append( "  ] " );

			rv.append( "[ " );
			rv.append( decimalFormat.format( this.right.z ) );
			rv.append( ' ' );
			rv.append( decimalFormat.format( this.up.z ) );
			rv.append( ' ' );
			rv.append( decimalFormat.format( this.backward.z ) );
			rv.append( ' ' );
			rv.append( decimalFormat.format( this.translation.z ) );
			rv.append( "  ] " );

			rv.append( "[ " );
			rv.append( decimalFormat.format( this.right.w ) );
			rv.append( ' ' );
			rv.append( decimalFormat.format( this.up.w ) );
			rv.append( ' ' );
			rv.append( decimalFormat.format( this.backward.w ) );
			rv.append( ' ' );
			rv.append( decimalFormat.format( this.translation.w ) );
			rv.append( "  ] " );
		}
		return rv;
	}

	//Identity
	private static final Matrix4x4 IDENTITY = Matrix4x4.createNaN();

	public static Matrix4x4 accessIdentity() {
		IDENTITY.right.set( 1, 0, 0, 0 );
		IDENTITY.up.set( 0, 1, 0, 0 );
		IDENTITY.backward.set( 0, 0, 1, 0 );
		IDENTITY.translation.set( 0, 0, 0, 1 );
		return IDENTITY;
	}

	public static Matrix4x4 setReturnValueToIdentity( Matrix4x4 rv ) {
		rv.set( accessIdentity() );
		return rv;
	}

	public static Matrix4x4 createIdentity() {
		return setReturnValueToIdentity( Matrix4x4.createNaN() );
	}

	@Override
	public void setIdentity() {
		setReturnValueToIdentity( this );
	}

	@Override
	public boolean isIdentity() {
		return this.equals( accessIdentity() );
	}

	//Zero
	public static Matrix4x4 setReturnValueToZero( Matrix4x4 rv ) {
		rv.right.setZero();
		rv.up.setZero();
		rv.backward.setZero();
		rv.translation.setZero();
		return rv;
	}

	public static Matrix4x4 createFromZero() {
		return setReturnValueToZero( Matrix4x4.createNaN() );
	}

	public void setZero() {
		setReturnValueToZero( this );
	}

	public boolean isZero() {
		return right.isZero() && up.isZero() && backward.isZero() && translation.isZero();
	}

	public void set( Matrix4x4 other ) {
		this.right.x = other.right.x;
		this.up.x = other.up.x;
		this.backward.x = other.backward.x;
		this.translation.x = other.translation.x;
		this.right.y = other.right.y;
		this.up.y = other.up.y;
		this.backward.y = other.backward.y;
		this.translation.y = other.translation.y;
		this.right.z = other.right.z;
		this.up.z = other.up.z;
		this.backward.z = other.backward.z;
		this.translation.z = other.translation.z;
		this.right.w = other.right.w;
		this.up.w = other.up.w;
		this.backward.w = other.backward.w;
		this.translation.w = other.translation.w;
	}

	public void set( AffineMatrix4x4 other ) {
		this.right.x = other.orientation.right.x;
		this.up.x = other.orientation.up.x;
		this.backward.x = other.orientation.backward.x;
		this.translation.x = other.translation.x;
		this.right.y = other.orientation.right.y;
		this.up.y = other.orientation.up.y;
		this.backward.y = other.orientation.backward.y;
		this.translation.y = other.translation.y;
		this.right.z = other.orientation.right.z;
		this.up.z = other.orientation.up.z;
		this.backward.z = other.orientation.backward.z;
		this.translation.z = other.translation.z;
		this.right.w = 0.0;
		this.up.w = 0.0;
		this.backward.w = 0.0;
		this.translation.w = 1.0;
	}

	public void set( double m00, double m01, double m02, double m03, double m10, double m11, double m12, double m13, double m20, double m21, double m22, double m23, double m30, double m31, double m32, double m33 ) {
		this.right.x = m00;
		this.up.x = m01;
		this.backward.x = m02;
		this.translation.x = m03;
		this.right.y = m10;
		this.up.y = m11;
		this.backward.y = m12;
		this.translation.y = m13;
		this.right.z = m20;
		this.up.z = m21;
		this.backward.z = m22;
		this.translation.z = m23;
		this.right.w = m30;
		this.up.w = m31;
		this.backward.w = m32;
		this.translation.w = m33;
	}

	public double determinate() {
		double m00 = right.x;
		double m10 = right.y;
		double m20 = right.z;
		double m30 = right.w;
		double m01 = up.x;
		double m11 = up.y;
		double m21 = up.z;
		double m31 = up.w;
		double m02 = backward.x;
		double m12 = backward.y;
		double m22 = backward.z;
		double m32 = backward.w;
		double m03 = translation.x;
		double m13 = translation.y;
		double m23 = translation.z;
		double m33 = translation.w;
		return ( ( ( ( ( ( ( ( ( ( ( ( m03 * m12 * m21 * m30 ) - ( m02 * m13 * m21 * m30 ) -
				( m03 * m11 * m22 * m30 ) ) + ( m01 * m13 * m22 * m30 ) +
				( m02 * m11 * m23 * m30 ) ) - ( m01 * m12 * m23 * m30 ) -
				( m03 * m12 * m20 * m31 ) ) + ( m02 * m13 * m20 * m31 ) +
				( m03 * m10 * m22 * m31 ) ) - ( m00 * m13 * m22 * m31 ) -
				( m02 * m10 * m23 * m31 ) ) + ( m00 * m12 * m23 * m31 ) +
				( m03 * m11 * m20 * m32 ) ) - ( m01 * m13 * m20 * m32 ) -
				( m03 * m10 * m21 * m32 ) ) + ( m00 * m13 * m21 * m32 ) +
				( m01 * m10 * m23 * m32 ) ) - ( m00 * m11 * m23 * m32 ) -
				( m02 * m11 * m20 * m33 ) ) + ( m01 * m12 * m20 * m33 ) +
				( m02 * m10 * m21 * m33 ) ) - ( m00 * m12 * m21 * m33 ) -
				( m01 * m10 * m22 * m33 ) ) + ( m00 * m11 * m22 * m33 );
	}

	public void invert() {
		double d = determinate();
		double m00 = right.x;
		double m10 = right.y;
		double m20 = right.z;
		double m30 = right.w;
		double m01 = up.x;
		double m11 = up.y;
		double m21 = up.z;
		double m31 = up.w;
		double m02 = backward.x;
		double m12 = backward.y;
		double m22 = backward.z;
		double m32 = backward.w;
		double m03 = translation.x;
		double m13 = translation.y;
		double m23 = translation.z;
		double m33 = translation.w;
		set(
				( ( ( ( m12 * m23 * m31 ) - ( m13 * m22 * m31 ) ) + ( m13 * m21 * m32 ) ) - ( m11 * m23 * m32 ) - ( m12 * m21 * m33 ) ) + ( m11 * m22 * m33 ),
				( ( ( m03 * m22 * m31 ) - ( m02 * m23 * m31 ) - ( m03 * m21 * m32 ) ) + ( m01 * m23 * m32 ) + ( m02 * m21 * m33 ) ) - ( m01 * m22 * m33 ),
				( ( ( ( m02 * m13 * m31 ) - ( m03 * m12 * m31 ) ) + ( m03 * m11 * m32 ) ) - ( m01 * m13 * m32 ) - ( m02 * m11 * m33 ) ) + ( m01 * m12 * m33 ),
				( ( ( m03 * m12 * m21 ) - ( m02 * m13 * m21 ) - ( m03 * m11 * m22 ) ) + ( m01 * m13 * m22 ) + ( m02 * m11 * m23 ) ) - ( m01 * m12 * m23 ),
				( ( ( m13 * m22 * m30 ) - ( m12 * m23 * m30 ) - ( m13 * m20 * m32 ) ) + ( m10 * m23 * m32 ) + ( m12 * m20 * m33 ) ) - ( m10 * m22 * m33 ),
				( ( ( ( m02 * m23 * m30 ) - ( m03 * m22 * m30 ) ) + ( m03 * m20 * m32 ) ) - ( m00 * m23 * m32 ) - ( m02 * m20 * m33 ) ) + ( m00 * m22 * m33 ),
				( ( ( m03 * m12 * m30 ) - ( m02 * m13 * m30 ) - ( m03 * m10 * m32 ) ) + ( m00 * m13 * m32 ) + ( m02 * m10 * m33 ) ) - ( m00 * m12 * m33 ),
				( ( ( ( m02 * m13 * m20 ) - ( m03 * m12 * m20 ) ) + ( m03 * m10 * m22 ) ) - ( m00 * m13 * m22 ) - ( m02 * m10 * m23 ) ) + ( m00 * m12 * m23 ),
				( ( ( ( m11 * m23 * m30 ) - ( m13 * m21 * m30 ) ) + ( m13 * m20 * m31 ) ) - ( m10 * m23 * m31 ) - ( m11 * m20 * m33 ) ) + ( m10 * m21 * m33 ),
				( ( ( m03 * m21 * m30 ) - ( m01 * m23 * m30 ) - ( m03 * m20 * m31 ) ) + ( m00 * m23 * m31 ) + ( m01 * m20 * m33 ) ) - ( m00 * m21 * m33 ),
				( ( ( ( m01 * m13 * m30 ) - ( m03 * m11 * m30 ) ) + ( m03 * m10 * m31 ) ) - ( m00 * m13 * m31 ) - ( m01 * m10 * m33 ) ) + ( m00 * m11 * m33 ),
				( ( ( m03 * m11 * m20 ) - ( m01 * m13 * m20 ) - ( m03 * m10 * m21 ) ) + ( m00 * m13 * m21 ) + ( m01 * m10 * m23 ) ) - ( m00 * m11 * m23 ),
				( ( ( m12 * m21 * m30 ) - ( m11 * m22 * m30 ) - ( m12 * m20 * m31 ) ) + ( m10 * m22 * m31 ) + ( m11 * m20 * m32 ) ) - ( m10 * m21 * m32 ),
				( ( ( ( m01 * m22 * m30 ) - ( m02 * m21 * m30 ) ) + ( m02 * m20 * m31 ) ) - ( m00 * m22 * m31 ) - ( m01 * m20 * m32 ) ) + ( m00 * m21 * m32 ),
				( ( ( m02 * m11 * m30 ) - ( m01 * m12 * m30 ) - ( m02 * m10 * m31 ) ) + ( m00 * m12 * m31 ) + ( m01 * m10 * m32 ) ) - ( m00 * m11 * m32 ),
				( ( ( ( m01 * m12 * m20 ) - ( m02 * m11 * m20 ) ) + ( m02 * m10 * m21 ) ) - ( m00 * m12 * m21 ) - ( m01 * m10 * m22 ) ) + ( m00 * m11 * m22 ) );
		scale( 1.0 / d );
	}

	public void setToMultiplication( Matrix4x4 a, Matrix4x4 b ) {
		double m00 = ( a.right.x * b.right.x ) + ( a.up.x * b.right.y ) + ( a.backward.x * b.right.z ) + ( a.translation.x * b.right.w );
		double m01 = ( a.right.x * b.up.x ) + ( a.up.x * b.up.y ) + ( a.backward.x * b.up.z ) + ( a.translation.x * b.up.w );
		double m02 = ( a.right.x * b.backward.x ) + ( a.up.x * b.backward.y ) + ( a.backward.x * b.backward.z ) + ( a.translation.x * b.backward.w );
		double m03 = ( a.right.x * b.translation.x ) + ( a.up.x * b.translation.y ) + ( a.backward.x * b.translation.z ) + ( a.translation.x * b.translation.w );

		double m10 = ( a.right.y * b.right.x ) + ( a.up.y * b.right.y ) + ( a.backward.y * b.right.z ) + ( a.translation.y * b.right.w );
		double m11 = ( a.right.y * b.up.x ) + ( a.up.y * b.up.y ) + ( a.backward.y * b.up.z ) + ( a.translation.y * b.up.w );
		double m12 = ( a.right.y * b.backward.x ) + ( a.up.y * b.backward.y ) + ( a.backward.y * b.backward.z ) + ( a.translation.y * b.backward.w );
		double m13 = ( a.right.y * b.translation.x ) + ( a.up.y * b.translation.y ) + ( a.backward.y * b.translation.z ) + ( a.translation.y * b.translation.w );

		double m20 = ( a.right.z * b.right.x ) + ( a.up.z * b.right.y ) + ( a.backward.z * b.right.z ) + ( a.translation.z * b.right.w );
		double m21 = ( a.right.z * b.up.x ) + ( a.up.z * b.up.y ) + ( a.backward.z * b.up.z ) + ( a.translation.z * b.up.w );
		double m22 = ( a.right.z * b.backward.x ) + ( a.up.z * b.backward.y ) + ( a.backward.z * b.backward.z ) + ( a.translation.z * b.backward.w );
		double m23 = ( a.right.z * b.translation.x ) + ( a.up.z * b.translation.y ) + ( a.backward.z * b.translation.z ) + ( a.translation.z * b.translation.w );

		double m30 = ( a.right.w * b.right.x ) + ( a.up.w * b.right.y ) + ( a.backward.w * b.right.z ) + ( a.translation.w * b.right.w );
		double m31 = ( a.right.w * b.up.x ) + ( a.up.w * b.up.y ) + ( a.backward.w * b.up.z ) + ( a.translation.w * b.up.w );
		double m32 = ( a.right.w * b.backward.x ) + ( a.up.w * b.backward.y ) + ( a.backward.w * b.backward.z ) + ( a.translation.w * b.backward.w );
		double m33 = ( a.right.w * b.translation.x ) + ( a.up.w * b.translation.y ) + ( a.backward.w * b.translation.z ) + ( a.translation.w * b.translation.w );

		this.right.set( m00, m10, m20, m30 );
		this.up.set( m01, m11, m21, m31 );
		this.backward.set( m02, m12, m22, m32 );
		this.translation.set( m03, m13, m23, m33 );
	}

	public void applyMultiplication( Matrix4x4 b ) {
		setToMultiplication( this, b );
	}

	public void add( Matrix4x4 b ) {
		right.add( b.right );
		up.add( b.up );
		backward.add( b.backward );
		translation.add( b.translation );
	}

	public void add( Matrix4x4 a, Matrix4x4 b ) {
		right.setToAddition( a.right, b.right );
		up.setToAddition( a.up, b.up );
		backward.setToAddition( a.backward, b.backward );
		translation.setToAddition( a.translation, b.translation );
	}

	public void sub( Matrix4x4 b ) {
		right.subtract( b.right );
		up.subtract( b.up );
		backward.subtract( b.backward );
		translation.subtract( b.translation );
	}

	public void sub( Matrix4x4 a, Matrix4x4 b ) {
		right.setToSubtraction( a.right, b.right );
		up.setToSubtraction( a.up, b.up );
		backward.setToSubtraction( a.backward, b.backward );
		translation.setToSubtraction( a.translation, b.translation );
	}

	public void scale( double s ) {
		right.multiply( s );
		up.multiply( s );
		backward.multiply( s );
		translation.multiply( s );
	}

	public void transpose() {
		double m00 = right.x;
		double m10 = right.y;
		double m20 = right.z;
		double m30 = right.w;
		double m01 = up.x;
		double m11 = up.y;
		double m21 = up.z;
		double m31 = up.w;
		double m02 = backward.x;
		double m12 = backward.y;
		double m22 = backward.z;
		double m32 = backward.w;
		double m03 = translation.x;
		double m13 = translation.y;
		double m23 = translation.z;
		double m33 = translation.w;
		set(
				m00, m10, m20, m30,
				m01, m11, m21, m31,
				m02, m12, m22, m32,
				m03, m13, m23, m33 );
	}

	@Override
	public Vector4 setReturnValueToTransformed( Vector4 rv, Vector4 b ) {
		double m00 = right.x;
		double m10 = right.y;
		double m20 = right.z;
		double m30 = right.w;
		double m01 = up.x;
		double m11 = up.y;
		double m21 = up.z;
		double m31 = up.w;
		double m02 = backward.x;
		double m12 = backward.y;
		double m22 = backward.z;
		double m32 = backward.w;
		double m03 = translation.x;
		double m13 = translation.y;
		double m23 = translation.z;
		double m33 = translation.w;
		double x = ( m00 * b.x ) + ( m01 * b.y ) + ( m02 * b.z ) + ( m03 * b.w );
		double y = ( m10 * b.x ) + ( m11 * b.y ) + ( m12 * b.z ) + ( m13 * b.w );
		double z = ( m20 * b.x ) + ( m21 * b.y ) + ( m22 * b.z ) + ( m23 * b.w );
		double w = ( m30 * b.x ) + ( m31 * b.y ) + ( m32 * b.z ) + ( m33 * b.w );
		rv.x = x;
		rv.y = y;
		rv.z = z;
		rv.w = w;
		return rv;
	}

	@Override
	public Vector3 setReturnValueToTransformed( Vector3 rv, Vector3 b ) {
		double m00 = right.x;
		double m10 = right.y;
		double m20 = right.z;
		double m01 = up.x;
		double m11 = up.y;
		double m21 = up.z;
		double m02 = backward.x;
		double m12 = backward.y;
		double m22 = backward.z;
		double x = ( m00 * b.x ) + ( m01 * b.y ) + ( m02 * b.z );
		double y = ( m10 * b.x ) + ( m11 * b.y ) + ( m12 * b.z );
		double z = ( m20 * b.x ) + ( m21 * b.y ) + ( m22 * b.z );
		rv.x = x;
		rv.y = y;
		rv.z = z;
		return rv;
	}

	@Override
	public Point3 setReturnValueToTransformed( Point3 rv, Point3 b ) {
		double m00 = right.x;
		double m10 = right.y;
		double m20 = right.z;
		double m01 = up.x;
		double m11 = up.y;
		double m21 = up.z;
		double m02 = backward.x;
		double m12 = backward.y;
		double m22 = backward.z;
		double m03 = translation.x;
		double m13 = translation.y;
		double m23 = translation.z;
		double x = ( m00 * b.x ) + ( m01 * b.y ) + ( m02 * b.z ) + m03;
		double y = ( m10 * b.x ) + ( m11 * b.y ) + ( m12 * b.z ) + m13;
		double z = ( m20 * b.x ) + ( m21 * b.y ) + ( m22 * b.z ) + m23;
		rv.x = x;
		rv.y = y;
		rv.z = z;
		return rv;
	}

	@Override
	public Vector4f setReturnValueToTransformed( Vector4f rv, Vector4f b ) {
		float m00 = (float)right.x;
		float m10 = (float)right.y;
		float m20 = (float)right.z;
		float m30 = (float)right.w;
		float m01 = (float)up.x;
		float m11 = (float)up.y;
		float m21 = (float)up.z;
		float m31 = (float)up.w;
		float m02 = (float)backward.x;
		float m12 = (float)backward.y;
		float m22 = (float)backward.z;
		float m32 = (float)backward.w;
		float m03 = (float)translation.x;
		float m13 = (float)translation.y;
		float m23 = (float)translation.z;
		float m33 = (float)translation.w;
		float x = ( m00 * b.x ) + ( m01 * b.y ) + ( m02 * b.z ) + ( m03 * b.w );
		float y = ( m10 * b.x ) + ( m11 * b.y ) + ( m12 * b.z ) + ( m13 * b.w );
		float z = ( m20 * b.x ) + ( m21 * b.y ) + ( m22 * b.z ) + ( m23 * b.w );
		float w = ( m30 * b.x ) + ( m31 * b.y ) + ( m32 * b.z ) + ( m33 * b.w );
		rv.x = x;
		rv.y = y;
		rv.z = z;
		rv.w = w;
		return rv;
	}

	@Override
	public Vector3f setReturnValueToTransformed( Vector3f rv, Vector3f b ) {
		float m00 = (float)right.x;
		float m10 = (float)right.y;
		float m20 = (float)right.z;
		float m01 = (float)up.x;
		float m11 = (float)up.y;
		float m21 = (float)up.z;
		float m02 = (float)backward.x;
		float m12 = (float)backward.y;
		float m22 = (float)backward.z;
		float x = ( m00 * b.x ) + ( m01 * b.y ) + ( m02 * b.z );
		float y = ( m10 * b.x ) + ( m11 * b.y ) + ( m12 * b.z );
		float z = ( m20 * b.x ) + ( m21 * b.y ) + ( m22 * b.z );
		rv.x = x;
		rv.y = y;
		rv.z = z;
		return rv;
	}

	@Override
	public Point3f setReturnValueToTransformed( Point3f rv, Point3f b ) {
		float m00 = (float)right.x;
		float m10 = (float)right.y;
		float m20 = (float)right.z;
		float m01 = (float)up.x;
		float m11 = (float)up.y;
		float m21 = (float)up.z;
		float m02 = (float)backward.x;
		float m12 = (float)backward.y;
		float m22 = (float)backward.z;
		float m03 = (float)translation.x;
		float m13 = (float)translation.y;
		float m23 = (float)translation.z;
		float x = ( m00 * b.x ) + ( m01 * b.y ) + ( m02 * b.z ) + m03;
		float y = ( m10 * b.x ) + ( m11 * b.y ) + ( m12 * b.z ) + m13;
		float z = ( m20 * b.x ) + ( m21 * b.y ) + ( m22 * b.z ) + m23;
		rv.x = x;
		rv.y = y;
		rv.z = z;
		return rv;
	}

	@Override
	public boolean isWithinEpsilonOfIdentity( double epsilon ) {
		return this.right.isWithinEpsilonOf( 1.0, 0.0, 0.0, 0.0, epsilon ) &&
				this.up.isWithinEpsilonOf( 0.0, 1.0, 0.0, 0.0, epsilon ) &&
				this.backward.isWithinEpsilonOf( 0.0, 0.0, 1.0, 0.0, epsilon ) &&
				this.translation.isWithinEpsilonOf( 0.0, 0.0, 0.0, 1.0, epsilon );
	}

	@Override
	public boolean equals( Object o ) {
		if( this == o ) {
			return true;
		} else {
			if( o instanceof Matrix4x4 ) {
				Matrix4x4 other = (Matrix4x4)o;
				return edu.cmu.cs.dennisc.java.util.Objects.equals( this.right, other.right )
						&& edu.cmu.cs.dennisc.java.util.Objects.equals( this.up, other.up )
						&& edu.cmu.cs.dennisc.java.util.Objects.equals( this.backward, other.backward )
						&& edu.cmu.cs.dennisc.java.util.Objects.equals( this.translation, other.translation );
			} else {
				return false;
			}
		}
	}

	@Override
	public int hashCode() {
		int rv = 17;
		if( this.right != null ) {
			rv = ( 37 * rv ) + this.right.hashCode();
		}
		if( this.up != null ) {
			rv = ( 37 * rv ) + this.up.hashCode();
		}
		if( this.backward != null ) {
			rv = ( 37 * rv ) + this.backward.hashCode();
		}
		if( this.translation != null ) {
			rv = ( 37 * rv ) + this.translation.hashCode();
		}
		return rv;
	}

}
