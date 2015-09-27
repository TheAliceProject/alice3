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

//todo: rename?
/**
 * @author Dennis Cosgrove
 */
public class AffineMatrix4x4 extends AbstractMatrix4x4 implements edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable {
	public final OrthogonalMatrix3x3 orientation = OrthogonalMatrix3x3.createIdentity();
	public final Point3 translation = new Point3();

	public static AffineMatrix4x4 createFromColumnMajorArray12( double[] columnMajorArray ) {
		assert columnMajorArray.length == 12;
		AffineMatrix4x4 rv = AffineMatrix4x4.createNaN();
		rv.orientation.right.x = columnMajorArray[ 0 ];
		rv.orientation.right.y = columnMajorArray[ 1 ];
		rv.orientation.right.z = columnMajorArray[ 2 ];
		rv.orientation.up.x = columnMajorArray[ 3 ];
		rv.orientation.up.y = columnMajorArray[ 4 ];
		rv.orientation.up.z = columnMajorArray[ 5 ];
		rv.orientation.backward.x = columnMajorArray[ 6 ];
		rv.orientation.backward.y = columnMajorArray[ 7 ];
		rv.orientation.backward.z = columnMajorArray[ 8 ];
		rv.translation.x = columnMajorArray[ 9 ];
		rv.translation.y = columnMajorArray[ 10 ];
		rv.translation.z = columnMajorArray[ 11 ];
		return rv;
	}

	public static AffineMatrix4x4 createFromColumnMajorArray16( double[] columnMajorArray ) {
		assert columnMajorArray.length == 16;
		AffineMatrix4x4 rv = AffineMatrix4x4.createNaN();
		rv.orientation.right.x = columnMajorArray[ 0 ];
		rv.orientation.right.y = columnMajorArray[ 1 ];
		rv.orientation.right.z = columnMajorArray[ 2 ];
		assert columnMajorArray[ 3 ] == 0.0;
		rv.orientation.up.x = columnMajorArray[ 4 ];
		rv.orientation.up.y = columnMajorArray[ 5 ];
		rv.orientation.up.z = columnMajorArray[ 6 ];
		assert columnMajorArray[ 7 ] == 0.0;
		rv.orientation.backward.x = columnMajorArray[ 8 ];
		rv.orientation.backward.y = columnMajorArray[ 9 ];
		rv.orientation.backward.z = columnMajorArray[ 10 ];
		assert columnMajorArray[ 11 ] == 0.0;
		rv.translation.x = columnMajorArray[ 12 ];
		rv.translation.y = columnMajorArray[ 13 ];
		rv.translation.z = columnMajorArray[ 14 ];
		assert columnMajorArray[ 15 ] == 1.0;
		return rv;
	}

	//todo: reduce visibility to private
	public AffineMatrix4x4() {
	}

	public AffineMatrix4x4( AffineMatrix4x4 other ) {
		set( other );
	}

	public AffineMatrix4x4( Orientation orientation, Point3 translation ) {
		set( orientation, translation );
	}

	@Deprecated
	public AffineMatrix4x4( double m00, double m01, double m02, double m03, double m10, double m11, double m12, double m13, double m20, double m21, double m22, double m23, double m30, double m31, double m32, double m33 ) {
		set( new Matrix4x4( m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33 ) );
	}

	public AffineMatrix4x4( Matrix4x4 other ) {
		set( other );
	}

	public AffineMatrix4x4( edu.cmu.cs.dennisc.math.immutable.MAffineMatrix4x4 other ) {
		this( new OrthogonalMatrix3x3( other.orientation ), new Point3( other.translation ) );
	}

	public void decode( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		orientation.decode( binaryDecoder );
		translation.decode( binaryDecoder );
	}

	@Override
	public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		orientation.encode( binaryEncoder );
		translation.encode( binaryEncoder );
	}

	@Override
	public double[] getAsColumnMajorArray16( double[] rv ) {
		assert rv.length == 16;
		rv[ 0 ] = orientation.right.x;
		rv[ 1 ] = orientation.right.y;
		rv[ 2 ] = orientation.right.z;
		rv[ 3 ] = 0.0;
		rv[ 4 ] = orientation.up.x;
		rv[ 5 ] = orientation.up.y;
		rv[ 6 ] = orientation.up.z;
		rv[ 7 ] = 0.0;
		rv[ 8 ] = orientation.backward.x;
		rv[ 9 ] = orientation.backward.y;
		rv[ 10 ] = orientation.backward.z;
		rv[ 11 ] = 0.0;
		rv[ 12 ] = translation.x;
		rv[ 13 ] = translation.y;
		rv[ 14 ] = translation.z;
		rv[ 15 ] = 1.0;
		return rv;
	}

	public double[] getAsColumnMajorArray12( double[] rv ) {
		assert rv.length == 12;
		rv[ 0 ] = orientation.right.x;
		rv[ 1 ] = orientation.right.y;
		rv[ 2 ] = orientation.right.z;
		rv[ 3 ] = orientation.up.x;
		rv[ 4 ] = orientation.up.y;
		rv[ 5 ] = orientation.up.z;
		rv[ 6 ] = orientation.backward.x;
		rv[ 7 ] = orientation.backward.y;
		rv[ 8 ] = orientation.backward.z;
		rv[ 9 ] = translation.x;
		rv[ 10 ] = translation.y;
		rv[ 11 ] = translation.z;
		return rv;
	}

	public final double[] getAsColumnMajorArray12() {
		return getAsColumnMajorArray12( new double[ 12 ] );
	}

	@Override
	public boolean isAffine() {
		return true;
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
			rv.append( decimalFormat.format( this.orientation.right.x ) );
			rv.append( ' ' );
			rv.append( decimalFormat.format( this.orientation.up.x ) );
			rv.append( ' ' );
			rv.append( decimalFormat.format( this.orientation.backward.x ) );
			rv.append( ' ' );
			rv.append( decimalFormat.format( this.translation.x ) );
			rv.append( "  |\n" );

			rv.append( "| " );
			rv.append( decimalFormat.format( this.orientation.right.y ) );
			rv.append( ' ' );
			rv.append( decimalFormat.format( this.orientation.up.y ) );
			rv.append( ' ' );
			rv.append( decimalFormat.format( this.orientation.backward.y ) );
			rv.append( ' ' );
			rv.append( decimalFormat.format( this.translation.y ) );
			rv.append( "  |\n" );

			rv.append( "| " );
			rv.append( decimalFormat.format( this.orientation.right.z ) );
			rv.append( ' ' );
			rv.append( decimalFormat.format( this.orientation.up.z ) );
			rv.append( ' ' );
			rv.append( decimalFormat.format( this.orientation.backward.z ) );
			rv.append( ' ' );
			rv.append( decimalFormat.format( this.translation.z ) );
			rv.append( "  |\n" );

			rv.append( "| " );
			rv.append( decimalFormat.format( 0 ) );
			rv.append( ' ' );
			rv.append( decimalFormat.format( 0 ) );
			rv.append( ' ' );
			rv.append( decimalFormat.format( 0 ) );
			rv.append( ' ' );
			rv.append( decimalFormat.format( 1 ) );
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
			rv.append( decimalFormat.format( this.orientation.right.x ) );
			rv.append( ' ' );
			rv.append( decimalFormat.format( this.orientation.up.x ) );
			rv.append( ' ' );
			rv.append( decimalFormat.format( this.orientation.backward.x ) );
			rv.append( ' ' );
			rv.append( decimalFormat.format( this.translation.x ) );
			rv.append( "  ] " );

			rv.append( "[ " );
			rv.append( decimalFormat.format( this.orientation.right.y ) );
			rv.append( ' ' );
			rv.append( decimalFormat.format( this.orientation.up.y ) );
			rv.append( ' ' );
			rv.append( decimalFormat.format( this.orientation.backward.y ) );
			rv.append( ' ' );
			rv.append( decimalFormat.format( this.translation.y ) );
			rv.append( "  ] " );

			rv.append( "[ " );
			rv.append( decimalFormat.format( this.orientation.right.z ) );
			rv.append( ' ' );
			rv.append( decimalFormat.format( this.orientation.up.z ) );
			rv.append( ' ' );
			rv.append( decimalFormat.format( this.orientation.backward.z ) );
			rv.append( ' ' );
			rv.append( decimalFormat.format( this.translation.z ) );
			rv.append( "  ] " );

			rv.append( "[ " );
			rv.append( decimalFormat.format( 0 ) );
			rv.append( ' ' );
			rv.append( decimalFormat.format( 0 ) );
			rv.append( ' ' );
			rv.append( decimalFormat.format( 0 ) );
			rv.append( ' ' );
			rv.append( decimalFormat.format( 1 ) );
			rv.append( "  ] " );
		}
		return rv;
	}

	//NaN
	public static AffineMatrix4x4 setReturnValueToNaN( AffineMatrix4x4 rv ) {
		rv.orientation.setNaN();
		rv.translation.setNaN();
		return rv;
	}

	public static AffineMatrix4x4 createNaN() {
		return setReturnValueToNaN( new AffineMatrix4x4() );
	}

	@Override
	public void setNaN() {
		setReturnValueToNaN( this );
	}

	@Override
	public boolean isNaN() {
		return orientation.isNaN() || translation.isNaN();
	}

	//Identity
	private static final AffineMatrix4x4 IDENTITY = AffineMatrix4x4.createNaN();

	public static AffineMatrix4x4 accessIdentity() {
		IDENTITY.setIdentity();
		return IDENTITY;
	}

	public static AffineMatrix4x4 setReturnValueToIdentity( AffineMatrix4x4 rv ) {
		rv.orientation.setIdentity();
		rv.translation.setZero();
		return rv;
	}

	public static AffineMatrix4x4 createIdentity() {
		return setReturnValueToIdentity( AffineMatrix4x4.createNaN() );
	}

	@Override
	public void setIdentity() {
		setReturnValueToIdentity( this );
	}

	@Override
	public boolean isIdentity() {
		return orientation.isIdentity() && translation.isZero();
	}

	//Translation
	private static AffineMatrix4x4 setReturnValueToTranslation( AffineMatrix4x4 rv, double x, double y, double z ) {
		rv.translation.set( x, y, z );
		rv.orientation.setIdentity();
		return rv;
	}

	public static AffineMatrix4x4 createTranslation( double x, double y, double z ) {
		return setReturnValueToTranslation( AffineMatrix4x4.createNaN(), x, y, z );
	}

	public static AffineMatrix4x4 createTranslation( Point3 translation ) {
		return createTranslation( translation.x, translation.y, translation.z );
	}

	public static AffineMatrix4x4 createTranslationAlongXAxis( double x ) {
		return setReturnValueToTranslation( AffineMatrix4x4.createNaN(), x, 0, 0 );
	}

	public static AffineMatrix4x4 createTranslationAlongYAxis( double y ) {
		return setReturnValueToTranslation( AffineMatrix4x4.createNaN(), 0, y, 0 );
	}

	public static AffineMatrix4x4 createTranslationAlongZAxis( double z ) {
		return setReturnValueToTranslation( AffineMatrix4x4.createNaN(), 0, 0, z );
	}

	public void applyTranslationAlongXAxis( double x ) {
		//todo: optimize
		multiply( AffineMatrix4x4.createTranslationAlongXAxis( x ) );
	}

	public void applyTranslationAlongYAxis( double y ) {
		//todo: optimize
		multiply( AffineMatrix4x4.createTranslationAlongYAxis( y ) );
	}

	public void applyTranslationAlongZAxis( double z ) {
		//todo: optimize
		multiply( AffineMatrix4x4.createTranslationAlongZAxis( z ) );
	}

	public void applyTranslation( double x, double y, double z ) {
		//todo: optimize
		multiply( AffineMatrix4x4.createTranslation( x, y, z ) );
		//		if( x != 0.0 ) {
		//			applyTranslationAlongXAxis( x );
		//		}
		//		if( y != 0.0 ) {
		//			applyTranslationAlongYAxis( y );
		//		}
		//		if( z != 0.0 ) {
		//			applyTranslationAlongZAxis( z );
		//		}
	}

	public void applyTranslation( Point3 translation ) {
		applyTranslation( translation.x, translation.y, translation.z );
	}

	//Orientation
	public static AffineMatrix4x4 setReturnValueToOrientation( AffineMatrix4x4 rv, Orientation orientation ) {
		rv.translation.setZero();
		orientation.getValue( rv.orientation );
		return rv;
	}

	public static AffineMatrix4x4 createOrientation( Orientation orientation ) {
		return setReturnValueToOrientation( AffineMatrix4x4.createNaN(), orientation );
	}

	public static AffineMatrix4x4 createRotationAboutXAxis( Angle angle ) {
		return setReturnValueToOrientation( AffineMatrix4x4.createNaN(), AxisRotation.createXAxisRotation( angle ) );
	}

	public static AffineMatrix4x4 createRotationAboutYAxis( Angle angle ) {
		return setReturnValueToOrientation( AffineMatrix4x4.createNaN(), AxisRotation.createYAxisRotation( angle ) );
	}

	public static AffineMatrix4x4 createRotationAboutZAxis( Angle angle ) {
		return setReturnValueToOrientation( AffineMatrix4x4.createNaN(), AxisRotation.createZAxisRotation( angle ) );
	}

	public void applyOrientation( Orientation orientation ) {
		//todo: optimize
		multiply( AffineMatrix4x4.createOrientation( orientation ) );
	}

	public void applyRotationAboutXAxis( Angle angle ) {
		//todo: optimize
		multiply( createRotationAboutXAxis( angle ) );
	}

	public void applyRotationAboutYAxis( Angle angle ) {
		//todo: optimize
		multiply( createRotationAboutYAxis( angle ) );
	}

	public void applyRotationAboutZAxis( Angle angle ) {
		//todo: optimize
		multiply( createRotationAboutZAxis( angle ) );
	}

	public void set( AffineMatrix4x4 other ) {
		this.orientation.setValue( other.orientation );
		this.translation.set( other.translation );
	}

	public void set( Orientation orientation, Point3 translation ) {
		orientation.getValue( this.orientation );
		this.translation.set( translation );
	}

	public void set( Matrix4x4 other ) {
		assert other.isWithinReasonableEpsilonOfAffine();
		//edu.cmu.cs.dennisc.print.PrintUtilities.printlns(  other );
		//assert other.isAffine();

		this.orientation.right.set( other.right.x, other.right.y, other.right.z );
		this.orientation.up.set( other.up.x, other.up.y, other.up.z );
		this.orientation.backward.set( other.backward.x, other.backward.y, other.backward.z );
		this.translation.set( other.translation.x, other.translation.y, other.translation.z );
	}

	private void set( double m00, double m01, double m02, double m03, double m10, double m11, double m12, double m13, double m20, double m21, double m22, double m23 ) {
		orientation.right.x = m00;
		orientation.right.y = m10;
		orientation.right.z = m20;
		orientation.up.x = m01;
		orientation.up.y = m11;
		orientation.up.z = m21;
		orientation.backward.x = m02;
		orientation.backward.y = m12;
		orientation.backward.z = m22;
		translation.x = m03;
		translation.y = m13;
		translation.z = m23;
	}

	//Multiply
	public static AffineMatrix4x4 setReturnValueToMultiplication( AffineMatrix4x4 rv, AffineMatrix4x4 a, AffineMatrix4x4 b ) {
		double m00 = ( a.orientation.right.x * b.orientation.right.x ) + ( a.orientation.up.x * b.orientation.right.y ) + ( a.orientation.backward.x * b.orientation.right.z );
		double m01 = ( a.orientation.right.x * b.orientation.up.x ) + ( a.orientation.up.x * b.orientation.up.y ) + ( a.orientation.backward.x * b.orientation.up.z );
		double m02 = ( a.orientation.right.x * b.orientation.backward.x ) + ( a.orientation.up.x * b.orientation.backward.y ) + ( a.orientation.backward.x * b.orientation.backward.z );
		double m03 = ( a.orientation.right.x * b.translation.x ) + ( a.orientation.up.x * b.translation.y ) + ( a.orientation.backward.x * b.translation.z ) + a.translation.x;

		double m10 = ( a.orientation.right.y * b.orientation.right.x ) + ( a.orientation.up.y * b.orientation.right.y ) + ( a.orientation.backward.y * b.orientation.right.z );
		double m11 = ( a.orientation.right.y * b.orientation.up.x ) + ( a.orientation.up.y * b.orientation.up.y ) + ( a.orientation.backward.y * b.orientation.up.z );
		double m12 = ( a.orientation.right.y * b.orientation.backward.x ) + ( a.orientation.up.y * b.orientation.backward.y ) + ( a.orientation.backward.y * b.orientation.backward.z );
		double m13 = ( a.orientation.right.y * b.translation.x ) + ( a.orientation.up.y * b.translation.y ) + ( a.orientation.backward.y * b.translation.z ) + a.translation.y;

		double m20 = ( a.orientation.right.z * b.orientation.right.x ) + ( a.orientation.up.z * b.orientation.right.y ) + ( a.orientation.backward.z * b.orientation.right.z );
		double m21 = ( a.orientation.right.z * b.orientation.up.x ) + ( a.orientation.up.z * b.orientation.up.y ) + ( a.orientation.backward.z * b.orientation.up.z );
		double m22 = ( a.orientation.right.z * b.orientation.backward.x ) + ( a.orientation.up.z * b.orientation.backward.y ) + ( a.orientation.backward.z * b.orientation.backward.z );
		double m23 = ( a.orientation.right.z * b.translation.x ) + ( a.orientation.up.z * b.translation.y ) + ( a.orientation.backward.z * b.translation.z ) + a.translation.z;

		rv.orientation.right.x = m00;
		rv.orientation.right.y = m10;
		rv.orientation.right.z = m20;

		rv.orientation.up.x = m01;
		rv.orientation.up.y = m11;
		rv.orientation.up.z = m21;

		rv.orientation.backward.x = m02;
		rv.orientation.backward.y = m12;
		rv.orientation.backward.z = m22;

		rv.translation.x = m03;
		rv.translation.y = m13;
		rv.translation.z = m23;

		return rv;
	}

	public static AffineMatrix4x4 createMultiplication( AffineMatrix4x4 a, AffineMatrix4x4 b ) {
		return setReturnValueToMultiplication( new AffineMatrix4x4(), a, b );
	}

	public void setToMultiplication( AffineMatrix4x4 a, AffineMatrix4x4 b ) {
		setReturnValueToMultiplication( this, a, b );
	}

	public void multiply( AffineMatrix4x4 b ) {
		setToMultiplication( this, b );
	}

	//Multiply
	public static AffineMatrix4x4 setReturnValueToMultiplication( AffineMatrix4x4 rv, AffineMatrix4x4 a, double scale ) {
		rv.orientation.right.x = a.orientation.right.x * scale;
		rv.orientation.right.y = a.orientation.right.y * scale;
		rv.orientation.right.z = a.orientation.right.z * scale;

		rv.orientation.up.x = a.orientation.up.x * scale;
		rv.orientation.up.y = a.orientation.up.y * scale;
		rv.orientation.up.z = a.orientation.up.z * scale;

		rv.orientation.backward.x = a.orientation.backward.x * scale;
		rv.orientation.backward.y = a.orientation.backward.y * scale;
		rv.orientation.backward.z = a.orientation.backward.z * scale;

		rv.translation.x = a.translation.x * scale;
		rv.translation.y = a.translation.y * scale;
		rv.translation.z = a.translation.z * scale;

		return rv;
	}

	public static AffineMatrix4x4 createMultiplication( AffineMatrix4x4 a, double scale ) {
		return setReturnValueToMultiplication( new AffineMatrix4x4(), a, scale );
	}

	public void setToMultiplication( AffineMatrix4x4 a, double scale ) {
		setReturnValueToMultiplication( this, a, scale );
	}

	public void multiply( double scale ) {
		setToMultiplication( this, scale );
	}

	//Addition
	public static AffineMatrix4x4 setReturnValueToAddition( AffineMatrix4x4 rv, AffineMatrix4x4 a, AffineMatrix4x4 b ) {
		rv.orientation.right.x = a.orientation.right.x + b.orientation.right.x;
		rv.orientation.right.y = a.orientation.right.y + b.orientation.right.y;
		rv.orientation.right.z = a.orientation.right.z + b.orientation.right.z;

		rv.orientation.up.x = a.orientation.up.x + b.orientation.up.x;
		rv.orientation.up.y = a.orientation.up.y + b.orientation.up.y;
		rv.orientation.up.z = a.orientation.up.z + b.orientation.up.z;

		rv.orientation.backward.x = a.orientation.backward.x + b.orientation.backward.x;
		rv.orientation.backward.y = a.orientation.backward.y + b.orientation.backward.y;
		rv.orientation.backward.z = a.orientation.backward.z + b.orientation.backward.z;

		rv.translation.x = a.translation.x + b.translation.x;
		rv.translation.y = a.translation.y + b.translation.y;
		rv.translation.z = a.translation.z + b.translation.z;

		return rv;
	}

	public static AffineMatrix4x4 createAddition( AffineMatrix4x4 a, AffineMatrix4x4 b ) {
		return setReturnValueToAddition( new AffineMatrix4x4(), a, b );
	}

	public void setToAddition( AffineMatrix4x4 a, AffineMatrix4x4 b ) {
		setReturnValueToAddition( this, a, b );
	}

	public void add( AffineMatrix4x4 b ) {
		setToAddition( this, b );
	}

	public void setZero()
	{
		this.orientation.right.set( 0, 0, 0 );
		this.orientation.up.set( 0, 0, 0 );
		this.orientation.backward.set( 0, 0, 0 );
		this.translation.set( 0, 0, 0 );
	}

	//Determinate
	public double calculateDeterminate() {
		double m00 = orientation.right.x;
		double m10 = orientation.right.y;
		double m20 = orientation.right.z;

		double m01 = orientation.up.x;
		double m11 = orientation.up.y;
		double m21 = orientation.up.z;

		double m02 = orientation.backward.x;
		double m12 = orientation.backward.y;
		double m22 = orientation.backward.z;
		return ( ( ( m02 * m11 * m20 ) + ( m01 * m12 * m20 ) +
				( m02 * m10 * m21 ) ) - ( m00 * m12 * m21 ) -
				( m01 * m10 * m22 ) ) + ( m00 * m11 * m22 );
	}

	//Inverse
	public static AffineMatrix4x4 setReturnValueToInverse( AffineMatrix4x4 rv, AffineMatrix4x4 a ) {
		//		double m00 = a.orientation.right.x;
		//		double m01 = a.orientation.up.x;
		//		double m02 = a.orientation.backward.x;
		//		double m10 = a.orientation.right.y;
		//		double m11 = a.orientation.up.y;
		//		double m12 = a.orientation.backward.y;
		//		double m20 = a.orientation.right.z;
		//		double m21 = a.orientation.up.z;
		//		double m22 = a.orientation.backward.z;
		//		double m03 = a.translation.x;
		//		double m13 = a.translation.y;
		//		double m23 = a.translation.z;
		//		
		//		double determinate = a.calculateDeterminate();
		//		rv.set( 
		//				- m12 * m21 + m11 * m22,
		//				+ m02 * m21 - m01 * m22, 
		//				- m02 * m11 + m01 * m12, 
		//				m03 * m12 * m21 - m02 * m13 * m21 - m03 * m11 * m22 + m01 * m13 * m22 + m02 * m11 * m23 - m01 * m12 * m23,
		//				+ m12 * m20 - m10 * m22, 
		//				- m02 * m20 + m00 * m22, 
		//				+ m02 * m10 - m00 * m12, 
		//				m02 * m13 * m20 - m03 * m12 * m20 + m03 * m10 * m22 - m00 * m13 * m22 - m02 * m10 * m23 + m00 * m12 * m23, 
		//				- m11 * m20 + m10 * m21, 
		//				+ m01 * m20 - m00 * m21, 
		//				- m01 * m10 + m00 * m11, 
		//				m03 * m11 * m20 - m01 * m13 * m20 - m03 * m10 * m21 + m00 * m13 * m21 + m01 * m10 * m23 - m00 * m11 * m23
		//		);
		//		rv.orientation.divide( determinate );
		//		rv.translation.divide( determinate );

		Matrix4x4 m = new Matrix4x4( a );
		m.invert();
		//rv.set( new AffineMatrixD4x4( m ) );
		rv.set( m );
		return rv;
	}

	public static AffineMatrix4x4 createInverse( AffineMatrix4x4 a ) {
		return setReturnValueToInverse( new AffineMatrix4x4(), a );
	}

	public void setToInverse( AffineMatrix4x4 a ) {
		setReturnValueToInverse( this, a );
	}

	public void invert() {
		setToInverse( this );
	}

	public boolean isZero()
	{
		return this.orientation.right.isZero() && this.orientation.up.isZero() && this.orientation.backward.isZero() && this.translation.isZero();
	}

	@Override
	public Vector4 setReturnValueToTransformed( Vector4 rv, Vector4 b ) {
		//todo
		return new Matrix4x4( this ).setReturnValueToTransformed( rv, b );
	}

	@Override
	public Vector3 setReturnValueToTransformed( Vector3 rv, Vector3 b ) {
		//todo
		return new Matrix4x4( this ).setReturnValueToTransformed( rv, b );
	}

	@Override
	public Point3 setReturnValueToTransformed( Point3 rv, Point3 b ) {
		//todo
		return new Matrix4x4( this ).setReturnValueToTransformed( rv, b );
	}

	@Override
	public Vector4f setReturnValueToTransformed( Vector4f rv, Vector4f b ) {
		//todo
		return new Matrix4x4( this ).setReturnValueToTransformed( rv, b );
	}

	@Override
	public Vector3f setReturnValueToTransformed( Vector3f rv, Vector3f b ) {
		//todo
		return new Matrix4x4( this ).setReturnValueToTransformed( rv, b );
	}

	@Override
	public Point3f setReturnValueToTransformed( Point3f rv, Point3f b ) {
		//todo
		return new Matrix4x4( this ).setReturnValueToTransformed( rv, b );
	}

	public double[] transformVertex( double[] afRV, int offsetDest, double[] afSrc, int offsetSrc )
	{
		if( afRV == null )
		{
			afRV = new double[ 3 ];
			offsetDest = 0;
		}
		transformVector( afRV, offsetDest, afSrc, offsetSrc );
		afRV[ offsetDest ] += this.translation.x;
		afRV[ offsetDest + 1 ] += this.translation.y;
		afRV[ offsetDest + 2 ] += this.translation.z;
		return afRV;
	}

	public float[] transformVertex( float[] afRV, int offsetDest, float[] afSrc, int offsetSrc )
	{
		if( afRV == null )
		{
			afRV = new float[ 3 ];
			offsetDest = 0;
		}
		transformVector( afRV, offsetDest, afSrc, offsetSrc );
		afRV[ offsetDest ] += this.translation.x;
		afRV[ offsetDest + 1 ] += this.translation.y;
		afRV[ offsetDest + 2 ] += this.translation.z;
		return afRV;
	}

	public float[] transformNormal( float[] afRV, float[] afSrc )
	{
		return transformNormal( afRV, 0, afSrc, 0 );
	}

	public float[] transformNormal( float[] afRV, int offsetDest, float[] afSrc, int offsetSrc )
	{
		return transformVector( afRV, offsetDest, afSrc, offsetSrc );
	}

	private double[] transformVector( double[] afRV, int offsetDest, double[] afSrc, int offsetSrc )
	{
		if( afRV == null )
		{
			afRV = new double[ 3 ];
			offsetDest = 0;
		}

		afRV[ offsetDest ] = ( this.orientation.right.x * afSrc[ offsetSrc ] ) + ( this.orientation.up.x * afSrc[ offsetSrc + 1 ] ) + ( this.orientation.backward.x * afSrc[ offsetSrc + 2 ] );
		afRV[ offsetDest + 1 ] = ( this.orientation.right.y * afSrc[ offsetSrc ] ) + ( this.orientation.up.y * afSrc[ offsetSrc + 1 ] ) + ( this.orientation.backward.y * afSrc[ offsetSrc + 2 ] );
		afRV[ offsetDest + 2 ] = ( this.orientation.right.z * afSrc[ offsetSrc ] ) + ( this.orientation.up.z * afSrc[ offsetSrc + 1 ] ) + ( this.orientation.backward.z * afSrc[ offsetSrc + 2 ] );
		return afRV;
	}

	private float[] transformVector( float[] afRV, int offsetDest, float[] afSrc, int offsetSrc )
	{
		if( afRV == null )
		{
			afRV = new float[ 3 ];
			offsetDest = 0;
		}

		afRV[ offsetDest ] = (float)( ( this.orientation.right.x * afSrc[ offsetSrc ] ) + ( this.orientation.up.x * afSrc[ offsetSrc + 1 ] ) + ( this.orientation.backward.x * afSrc[ offsetSrc + 2 ] ) );
		afRV[ offsetDest + 1 ] = (float)( ( this.orientation.right.y * afSrc[ offsetSrc ] ) + ( this.orientation.up.y * afSrc[ offsetSrc + 1 ] ) + ( this.orientation.backward.y * afSrc[ offsetSrc + 2 ] ) );
		afRV[ offsetDest + 2 ] = (float)( ( this.orientation.right.z * afSrc[ offsetSrc ] ) + ( this.orientation.up.z * afSrc[ offsetSrc + 1 ] ) + ( this.orientation.backward.z * afSrc[ offsetSrc + 2 ] ) );
		return afRV;
	}

	@Override
	public boolean equals( Object o ) {
		if( this == o ) {
			return true;
		} else {
			if( o instanceof AffineMatrix4x4 ) {
				AffineMatrix4x4 other = (AffineMatrix4x4)o;
				return edu.cmu.cs.dennisc.java.util.Objects.equals( this.orientation, other.orientation )
						&& edu.cmu.cs.dennisc.java.util.Objects.equals( this.translation, other.translation );
			} else {
				return false;
			}
		}
	}

	@Override
	public int hashCode() {
		int rv = 17;
		if( this.orientation != null ) {
			rv = ( 37 * rv ) + this.orientation.hashCode();
		}
		if( this.orientation != null ) {
			rv = ( 37 * rv ) + this.translation.hashCode();
		}
		return rv;
	}

	@Override
	public boolean isWithinEpsilonOfIdentity( double epsilon ) {
		return this.orientation.isWithinEpsilonOfIdentity( epsilon ) && this.translation.isWithinEpsilonOfZero( epsilon );
	}

	public edu.cmu.cs.dennisc.math.immutable.MAffineMatrix4x4 createImmutable() {
		return new edu.cmu.cs.dennisc.math.immutable.MAffineMatrix4x4( this.orientation.createImmutable(), this.translation.createImmutable() );
	}
}
