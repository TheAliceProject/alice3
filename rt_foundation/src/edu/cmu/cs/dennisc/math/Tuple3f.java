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

/**
 * @author Dennis Cosgrove
 */
public abstract class Tuple3f implements edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable {
	public float x = 0.0f;
	public float y = 0.0f;
	public float z = 0.0f;

	public Tuple3f() {
	}
	public Tuple3f( Tuple3f other ) {
		set( other );
	}
	public Tuple3f( float x, float y, float z ) {
		set( x, y, z );
	}
		
	public void decode(edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder) {
		x = binaryDecoder.decodeFloat();
		y = binaryDecoder.decodeFloat();
		z = binaryDecoder.decodeFloat();
	}
	public void encode(edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder) {
		binaryEncoder.encode( x );
		binaryEncoder.encode( y );
		binaryEncoder.encode( z );
	}

	@Override
	public boolean equals( Object o ) {
		if( o == this ) {
			return true;
		} else {
			if( o instanceof Tuple3f ) {
				Tuple3f t = (Tuple3f) o;
				return this.x == t.x && this.y == t.y && this.z == t.z;
			} else {
				return false;
			}
		}
	}
	public boolean isWithinEpsilonOf( Tuple3f other, float epsilon ) {
		return EpsilonUtilities.isWithinEpsilon( this.x, other.x, epsilon ) && EpsilonUtilities.isWithinEpsilon( this.y, other.y, epsilon ) && EpsilonUtilities.isWithinEpsilon( this.z, other.z, epsilon );
	}
	public boolean isWithinReasonableEpsilonOf( Tuple3f other ) {
		return isWithinEpsilonOf( other, EpsilonUtilities.REASONABLE_EPSILON_FLOAT );
	}

	public boolean isWithinEpsilonOfUnitLengthSquared( float epsilon ) {
		return EpsilonUtilities.isWithinEpsilon( calculateMagnitudeSquared(), 1.0f, epsilon );
	}
	public boolean isWithinReasonableEpsilonOfUnitLengthSquared() {
		return isWithinEpsilonOfUnitLengthSquared( EpsilonUtilities.REASONABLE_EPSILON_FOR_SQUARED_VALUES_FLOAT );
	}

	public void set( Tuple3f other ) {
		if( other != null ) {
			this.x = other.x;
			this.y = other.y;
			this.z = other.z;
		} else {
			setNaN();
		}
	}
	public void set( float x, float y, float z ) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	//Zero
	public static Tuple3f setReturnValueToZero( Tuple3f rv ) {
		rv.x = rv.y = rv.z = 0.0f;
		return rv;
	}
	public void setZero() {
		setReturnValueToZero( this );
	}
	public boolean isZero() {
		return x==0.0f && y==0.0f && z==0.0f;
	}

	//NaN
	public static Tuple3f setReturnValueToNaN( Tuple3f rv ) {
		rv.x = rv.y = rv.z = Float.NaN;
		return rv;
	}
	public void setNaN() {
		setReturnValueToNaN( this );
	}
	public boolean isNaN() {
		return Float.isNaN( x ) && Float.isNaN( y ) && Float.isNaN( z );
	}
	

	//Add
	public static Tuple3f setReturnValueToAddition( Tuple3f rv, Tuple3f a, Tuple3f b ) {
		rv.x = a.x + b.x;
		rv.y = a.y + b.y;
		rv.z = a.z + b.z;
		return rv;
	}
	public void setToAddition( Tuple3f a, Tuple3f b ) {
		setReturnValueToAddition( this, a, b );
	}
	public void add( Tuple3f b ) {
		setToAddition( this, b );
	}
	
	//Subtract
	public static Tuple3f setReturnValueToSubtraction( Tuple3f rv, Tuple3f a, Tuple3f b ) {
		rv.x = a.x - b.x;
		rv.y = a.y - b.y;
		rv.z = a.z - b.z;
		return rv;
	}
	public void setToSubtraction( Tuple3f a, Tuple3f b ) {
		setReturnValueToSubtraction( this, a, b );
	}
	public void subtract( Tuple3f b ) {
		setToSubtraction( this, b );
	}

	//Negate
	public static Tuple3f setReturnValueToNegation( Tuple3f rv, Tuple3f a ) {
		rv.x = -a.x;
		rv.y = -a.y;
		rv.z = -a.z;
		return rv;
	}
	public void setToNegation( Tuple3f a ) {
		setReturnValueToNegation( this, a );
	}
	public void negate() {
		setToNegation( this );
	}

	//Multiply
	public static Tuple3f setReturnValueToMultiplication( Tuple3f rv, Tuple3f a, Tuple3f b ) {
		rv.x = a.x * b.x;
		rv.y = a.y * b.y;
		rv.z = a.z * b.z;
		return rv;
	}
	public void setToMultiplication( Tuple3f a, Tuple3f b ) {
		setReturnValueToMultiplication( this, a, b );
	}
	public void multiply( Tuple3f b ) {
		setToMultiplication( this, b );
	}
	public static Tuple3f setReturnValueToMultiplication( Tuple3f rv, Tuple3f a, float b ) {
		rv.x = a.x * b;
		rv.y = a.y * b;
		rv.z = a.z * b;
		return rv;
	}
	public void setToMultiplication( Tuple3f a, float b ) {
		setReturnValueToMultiplication( this, a, b );
	}
	public void multiply( float b ) {
		setToMultiplication( this, b );
	}


	//Divide
	public static Tuple3f setReturnValueToDivision( Tuple3f rv, Tuple3f a, Tuple3f b ) {
		rv.x = a.x / b.x;
		rv.y = a.y / b.y;
		rv.z = a.z / b.z;
		return rv;
	}
	public void setToDivision( Tuple3f a, Tuple3f b ) {
		setReturnValueToDivision( this, a, b );
	}
	public void divide( Tuple3f b ) {
		setToDivision( this, b );
	}
	public static Tuple3f setReturnValueToDivision( Tuple3f rv, Tuple3f a, float b ) {
		rv.x = a.x / b;
		rv.y = a.y / b;
		rv.z = a.z / b;
		return rv;
	}
	public void setToDivision( Tuple3f a, float b ) {
		setReturnValueToDivision( this, a, b );
	}
	public void divide( float b ) {
		setToDivision( this, b );
	}


	//Interpolate
	public static Tuple3f setReturnValueToInterpolation( Tuple3f rv, Tuple3f a, Tuple3f b, float portion ) {
		rv.x = a.x + ( b.x - a.x ) * portion;
		rv.y = a.y + ( b.y - a.y ) * portion;
		rv.z = a.z + ( b.z - a.z ) * portion;
		return rv;
	}
	public void setToInterpolation( Tuple3f a, Tuple3f b, float portion ) {
		setReturnValueToInterpolation( this, a, b, portion );
	}
	

	//Magnitude
	public static float calculateMagnitudeSquared( float x, float y, float z ) {
		return x*x + y*y + z*z;
	}
	public static float calculateMagnitude( float x, float y, float z ) {
		float magnitudeSquared = calculateMagnitudeSquared( x, y, z );
		if( magnitudeSquared == 1.0f ) {
			return 1.0f;
		} else {
			return (float)Math.sqrt( magnitudeSquared );
		}
	}

	public float calculateMagnitudeSquared() {
		return Tuple3f.calculateMagnitudeSquared( x, y, z );
	}
	public float calculateMagnitude() {
		return Tuple3f.calculateMagnitude( x, y, z );
	}
	
	//Normalize
	public static Tuple3f setReturnValueToNormalized( Tuple3f rv, Tuple3f a ) {
		rv.set( a );
		float magnitudeSquared = rv.calculateMagnitudeSquared();
		if( magnitudeSquared != 1.0 ) {
			rv.divide( (float)Math.sqrt( magnitudeSquared ) );
		}
		return rv;
	}
	public void setToNormalized( Tuple3f a ) {
		setReturnValueToNormalized( this, a );
	}
	public void normalize() {
		setToNormalized( this );
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append( getClass().getName() );
		sb.append( "[x=" );
		sb.append( x );
		sb.append( ";y=" );
		sb.append( y );
		sb.append( ";z=" );
		sb.append( z );
		sb.append( "]" );
		return sb.toString();
	}
}
