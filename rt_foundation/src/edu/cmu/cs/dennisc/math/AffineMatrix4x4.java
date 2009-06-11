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
package edu.cmu.cs.dennisc.math;

//todo: rename?
/**
 * @author Dennis Cosgrove
 */
public class AffineMatrix4x4 extends AbstractMatrix4x4 implements edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable {
	public final OrthogonalMatrix3x3 orientation = OrthogonalMatrix3x3.createIdentity();
	public final Point3 translation = new Point3();

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
	
	public void decode(edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder) {
		orientation.decode( binaryDecoder );
		translation.decode( binaryDecoder );
	}
	public void encode(edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder) {
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

	@Override
	public boolean isAffine() {
		return true;
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
		double m00 = a.orientation.right.x * b.orientation.right.x + a.orientation.up.x * b.orientation.right.y + a.orientation.backward.x * b.orientation.right.z;
		double m01 = a.orientation.right.x * b.orientation.up.x + a.orientation.up.x * b.orientation.up.y + a.orientation.backward.x * b.orientation.up.z;
		double m02 = a.orientation.right.x * b.orientation.backward.x + a.orientation.up.x * b.orientation.backward.y + a.orientation.backward.x * b.orientation.backward.z;
		double m03 = a.orientation.right.x * b.translation.x   + a.orientation.up.x * b.translation.y   + a.orientation.backward.x * b.translation.z   + a.translation.x;

		double m10 = a.orientation.right.y * b.orientation.right.x + a.orientation.up.y * b.orientation.right.y + a.orientation.backward.y * b.orientation.right.z;
		double m11 = a.orientation.right.y * b.orientation.up.x + a.orientation.up.y * b.orientation.up.y + a.orientation.backward.y * b.orientation.up.z;
		double m12 = a.orientation.right.y * b.orientation.backward.x + a.orientation.up.y * b.orientation.backward.y + a.orientation.backward.y * b.orientation.backward.z;
		double m13 = a.orientation.right.y * b.translation.x   + a.orientation.up.y * b.translation.y   + a.orientation.backward.y * b.translation.z   + a.translation.y;

		double m20 = a.orientation.right.z * b.orientation.right.x + a.orientation.up.z * b.orientation.right.y + a.orientation.backward.z * b.orientation.right.z;
		double m21 = a.orientation.right.z * b.orientation.up.x + a.orientation.up.z * b.orientation.up.y + a.orientation.backward.z * b.orientation.up.z;
		double m22 = a.orientation.right.z * b.orientation.backward.x + a.orientation.up.z * b.orientation.backward.y + a.orientation.backward.z * b.orientation.backward.z;
		double m23 = a.orientation.right.z * b.translation.x   + a.orientation.up.z * b.translation.y   + a.orientation.backward.z * b.translation.z   + a.translation.z;

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
		return 
			m02 * m11 * m20 + m01 * m12 * m20 + 
			m02 * m10 * m21 - m00 * m12 * m21 - 
			m01 * m10 * m22 + m00 * m11 * m22;
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
}
