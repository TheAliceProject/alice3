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
public abstract class Tuple2f implements edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable {
	public float x = 0.0f;
	public float y = 0.0f;

	public Tuple2f() {
	}
	public Tuple2f( Tuple2f other ) {
		set( other );
	}
	public Tuple2f( float x, float y ) {
		set( x, y );
	}
		
	public void decode(edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder) {
		x = binaryDecoder.decodeFloat();
		y = binaryDecoder.decodeFloat();
	}
	public void encode(edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder) {
		binaryEncoder.encode( x );
		binaryEncoder.encode( y );
	}

	@Override
	public boolean equals( Object o ) {
		if( o == this ) {
			return true;
		} else {
			if( o instanceof Tuple2f ) {
				Tuple2f t = (Tuple2f) o;
				return this.x == t.x && this.y == t.y;
			} else {
				return false;
			}
		}
	}
	public boolean isWithinEpsilonOf( Tuple2f other, float epsilon ) {
		return EpsilonUtilities.isWithinEpsilon( this.x, other.x, epsilon ) && EpsilonUtilities.isWithinEpsilon( this.y, other.y, epsilon );
	}
	public boolean isWithinReasonableEpsilonOf( Tuple2f other ) {
		return isWithinEpsilonOf( other, EpsilonUtilities.REASONABLE_EPSILON_FLOAT );
	}

	public boolean isWithinEpsilonOfUnitLengthSquared( float epsilon ) {
		return EpsilonUtilities.isWithinEpsilon( calculateMagnitudeSquared(), 1.0f, epsilon );
	}
	public boolean isWithinReasonableEpsilonOfUnitLengthSquared() {
		return isWithinEpsilonOfUnitLengthSquared( EpsilonUtilities.REASONABLE_EPSILON_FOR_SQUARED_VALUES_FLOAT );
	}

	public void set( Tuple2f other ) {
		if( other != null ) {
			this.x = other.x;
			this.y = other.y;
		} else {
			setNaN();
		}
	}
	public void set( float x, float y ) {
		this.x = x;
		this.y = y;
	}

	//Zero
	public static Tuple2f setReturnValueToZero( Tuple2f rv ) {
		rv.x = rv.y = 0.0f;
		return rv;
	}
	public void setZero() {
		setReturnValueToZero( this );
	}
	public boolean isZero() {
		return x==0.0f && y==0.0f;
	}

	//NaN
	public static Tuple2f setReturnValueToNaN( Tuple2f rv ) {
		rv.x = rv.y = Float.NaN;
		return rv;
	}
	public void setNaN() {
		setReturnValueToNaN( this );
	}
	public boolean isNaN() {
		return Float.isNaN( x ) && Float.isNaN( y );
	}
	

	//Add
	public static Tuple2f setReturnValueToAddition( Tuple2f rv, Tuple2f a, Tuple2f b ) {
		rv.x = a.x + b.x;
		rv.y = a.y + b.y;
		return rv;
	}
	public void setToAddition( Tuple2f a, Tuple2f b ) {
		setReturnValueToAddition( this, a, b );
	}
	public void add( Tuple2f b ) {
		setToAddition( this, b );
	}
	
	//Subtract
	public static Tuple2f setReturnValueToSubtraction( Tuple2f rv, Tuple2f a, Tuple2f b ) {
		rv.x = a.x - b.x;
		rv.y = a.y - b.y;
		return rv;
	}
	public void setToSubtraction( Tuple2f a, Tuple2f b ) {
		setReturnValueToSubtraction( this, a, b );
	}
	public void subtract( Tuple2f b ) {
		setToSubtraction( this, b );
	}

	//Negate
	public static Tuple2f setReturnValueToNegation( Tuple2f rv, Tuple2f a ) {
		rv.x = -a.x;
		rv.y = -a.y;
		return rv;
	}
	public void setToNegation( Tuple2f a ) {
		setReturnValueToNegation( this, a );
	}
	public void negate() {
		setToNegation( this );
	}

	//Multiply
	public static Tuple2f setReturnValueToMultiplication( Tuple2f rv, Tuple2f a, Tuple2f b ) {
		rv.x = a.x * b.x;
		rv.y = a.y * b.y;
		return rv;
	}
	public void setToMultiplication( Tuple2f a, Tuple2f b ) {
		setReturnValueToMultiplication( this, a, b );
	}
	public void multiply( Tuple2f b ) {
		setToMultiplication( this, b );
	}
	public static Tuple2f setReturnValueToMultiplication( Tuple2f rv, Tuple2f a, float b ) {
		rv.x = a.x * b;
		rv.y = a.y * b;
		return rv;
	}
	public void setToMultiplication( Tuple2f a, float b ) {
		setReturnValueToMultiplication( this, a, b );
	}
	public void multiply( float b ) {
		setToMultiplication( this, b );
	}


	//Divide
	public static Tuple2f setReturnValueToDivision( Tuple2f rv, Tuple2f a, Tuple2f b ) {
		rv.x = a.x / b.x;
		rv.y = a.y / b.y;
		return rv;
	}
	public void setToDivision( Tuple2f a, Tuple2f b ) {
		setReturnValueToDivision( this, a, b );
	}
	public void divide( Tuple2f b ) {
		setToDivision( this, b );
	}
	public static Tuple2f setReturnValueToDivision( Tuple2f rv, Tuple2f a, float b ) {
		rv.x = a.x / b;
		rv.y = a.y / b;
		return rv;
	}
	public void setToDivision( Tuple2f a, float b ) {
		setReturnValueToDivision( this, a, b );
	}
	public void divide( float b ) {
		setToDivision( this, b );
	}


	//Interpolate
	public static Tuple2f setReturnValueToInterpolation( Tuple2f rv, Tuple2f a, Tuple2f b, float portion ) {
		rv.x = a.x + ( b.x - a.x ) * portion;
		rv.y = a.y + ( b.y - a.y ) * portion;
		return rv;
	}
	public void setToInterpolation( Tuple2f a, Tuple2f b, float portion ) {
		setReturnValueToInterpolation( this, a, b, portion );
	}
	
	//Magnitude
	public static float calculateMagnitudeSquared( float x, float y ) {
		return x*x + y*y;
	}
	public static float calculateMagnitude( float x, float y ) {
		float magnitudeSquared = calculateMagnitudeSquared( x, y );
		if( magnitudeSquared == 1.0f ) {
			return 1.0f;
		} else {
			return (float)Math.sqrt( magnitudeSquared );
		}
	}

	public float calculateMagnitudeSquared() {
		return Tuple2f.calculateMagnitudeSquared( x, y );
	}
	public float calculateMagnitude() {
		return Tuple2f.calculateMagnitude( x, y );
	}

	//Normalize
	public static Tuple2f setReturnValueToNormalized( Tuple2f rv, Tuple2f a ) {
		rv.set( a );
		float magnitudeSquared = rv.calculateMagnitudeSquared();
		if( magnitudeSquared != 1.0 ) {
			rv.divide( (float)Math.sqrt( magnitudeSquared ) );
		}
		return rv;
	}
	public void setToNormalized( Tuple2f a ) {
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
		sb.append( "]" );
		return sb.toString();
	}
}
