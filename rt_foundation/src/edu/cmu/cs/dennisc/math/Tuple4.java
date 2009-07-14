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
public abstract class Tuple4 implements edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable, edu.cmu.cs.dennisc.print.Printable {
	public double x = 0.0;
	public double y = 0.0;
	public double z = 0.0;
	public double w = 0.0;

	public Tuple4() {
	}
	public Tuple4( Tuple4 other ) {
		set( other );
	}
	public Tuple4( double x, double y, double z, double w ) {
		set( x, y, z, w );
	}
		
	public void decode(edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder) {
		x = binaryDecoder.decodeDouble();
		y = binaryDecoder.decodeDouble();
		z = binaryDecoder.decodeDouble();
		w = binaryDecoder.decodeDouble();
	}
	public void encode(edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder) {
		binaryEncoder.encode( x );
		binaryEncoder.encode( y );
		binaryEncoder.encode( z );
		binaryEncoder.encode( w );
	}

	public StringBuffer append( StringBuffer rv, java.text.DecimalFormat decimalFormat, boolean isLines ) {
		if( isLines ) {
			rv.append( "+-       -+\n" );
			rv.append( "| " );
		}
		rv.append( decimalFormat.format( this.x ) );
		if( isLines ) {
			rv.append( " |\n" );
			rv.append( "| " );
		} else {
			rv.append( ' ' );
		}
		rv.append( decimalFormat.format( this.y ) );
		if( isLines ) {
			rv.append( " |\n" );
			rv.append( "| " );
		} else {
			rv.append( ' ' );
		}
		rv.append( decimalFormat.format( this.z ) );
		if( isLines ) {
			rv.append( " |\n" );
			rv.append( "| " );
		} else {
			rv.append( ' ' );
		}
		rv.append( decimalFormat.format( this.w ) );
		if( isLines ) {
			rv.append( " |\n" );
			rv.append( "+-       -+\n" );
		}
		return rv;
	}

	@Override
	public boolean equals( Object o ) {
		if( o == this ) {
			return true;
		} else {
			if( o instanceof Tuple4 ) {
				Tuple4 t = (Tuple4) o;
				return this.x == t.x && this.y == t.y && this.z == t.z && this.w == t.w;
			} else {
				return false;
			}
		}
	}
	public boolean isWithinEpsilonOf( Tuple4 other, double epsilon ) {
		return EpsilonUtilities.isWithinEpsilon( this.x, other.x, epsilon ) && EpsilonUtilities.isWithinEpsilon( this.y, other.y, epsilon ) && EpsilonUtilities.isWithinEpsilon( this.z, other.z, epsilon ) && EpsilonUtilities.isWithinEpsilon( this.w, other.w, epsilon );
	}
	public boolean isWithinReasonableEpsilonOf( Tuple4 other ) {
		return isWithinEpsilonOf( other, EpsilonUtilities.REASONABLE_EPSILON );
	}

	public boolean isWithinEpsilonOfUnitLengthSquared( double epsilon ) {
		return EpsilonUtilities.isWithinEpsilon( calculateMagnitudeSquared(), 1.0, epsilon );
	}
	public boolean isWithinReasonableEpsilonOfUnitLengthSquared() {
		return isWithinEpsilonOfUnitLengthSquared( EpsilonUtilities.REASONABLE_EPSILON_FOR_SQUARED_VALUES );
	}

	public void set( Tuple4 other ) {
		if( other != null ) {
			this.x = other.x;
			this.y = other.y;
			this.z = other.z;
			this.w = other.w;
		} else {
			setNaN();
		}
	}
	public void set( double x, double y, double z, double w ) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	//Zero
	public static Tuple4 setReturnValueToZero( Tuple4 rv ) {
		rv.x = rv.y = rv.z = rv.w = 0.0;
		return rv;
	}
	public void setZero() {
		setReturnValueToZero( this );
	}
	public boolean isZero() {
		return x==0.0 && y==0.0 && z==0.0 && w==0.0;
	}

	//NaN
	public static Tuple4 setReturnValueToNaN( Tuple4 rv ) {
		rv.x = rv.y = rv.z = rv.w = Double.NaN;
		return rv;
	}
	public void setNaN() {
		setReturnValueToNaN( this );
	}
	public boolean isNaN() {
		return Double.isNaN( x ) && Double.isNaN( y ) && Double.isNaN( z ) && Double.isNaN( w );
	}
	

	//Add
	public static Tuple4 setReturnValueToAddition( Tuple4 rv, Tuple4 a, Tuple4 b ) {
		rv.x = a.x + b.x;
		rv.y = a.y + b.y;
		rv.z = a.z + b.z;
		rv.w = a.w + b.w;
		return rv;
	}
	public void setToAddition( Tuple4 a, Tuple4 b ) {
		setReturnValueToAddition( this, a, b );
	}
	public void add( Tuple4 b ) {
		setToAddition( this, b );
	}
	
	//Subtract
	public static Tuple4 setReturnValueToSubtraction( Tuple4 rv, Tuple4 a, Tuple4 b ) {
		rv.x = a.x - b.x;
		rv.y = a.y - b.y;
		rv.z = a.z - b.z;
		rv.w = a.w - b.w;
		return rv;
	}
	public void setToSubtraction( Tuple4 a, Tuple4 b ) {
		setReturnValueToSubtraction( this, a, b );
	}
	public void subtract( Tuple4 b ) {
		setToSubtraction( this, b );
	}

	//Negate
	public static Tuple4 setReturnValueToNegation( Tuple4 rv, Tuple4 a ) {
		rv.x = -a.x;
		rv.y = -a.y;
		rv.z = -a.z;
		rv.w = -a.w;
		return rv;
	}
	public void setToNegation( Tuple4 a ) {
		setReturnValueToNegation( this, a );
	}
	public void negate() {
		setToNegation( this );
	}

	//Multiply
	public static Tuple4 setReturnValueToMultiplication( Tuple4 rv, Tuple4 a, Tuple4 b ) {
		rv.x = a.x * b.x;
		rv.y = a.y * b.y;
		rv.z = a.z * b.z;
		rv.w = a.w * b.w;
		return rv;
	}
	public void setToMultiplication( Tuple4 a, Tuple4 b ) {
		setReturnValueToMultiplication( this, a, b );
	}
	public void multiply( Tuple4 b ) {
		setToMultiplication( this, b );
	}
	public static Tuple4 setReturnValueToMultiplication( Tuple4 rv, Tuple4 a, double b ) {
		rv.x = a.x * b;
		rv.y = a.y * b;
		rv.z = a.z * b;
		rv.w = a.w * b;
		return rv;
	}
	public void setToMultiplication( Tuple4 a, double b ) {
		setReturnValueToMultiplication( this, a, b );
	}
	public void multiply( double b ) {
		setToMultiplication( this, b );
	}


	//Divide
	public static Tuple4 setReturnValueToDivision( Tuple4 rv, Tuple4 a, Tuple4 b ) {
		rv.x = a.x / b.x;
		rv.y = a.y / b.y;
		rv.z = a.z / b.z;
		rv.w = a.w / b.w;
		return rv;
	}
	public void setToDivision( Tuple4 a, Tuple4 b ) {
		setReturnValueToDivision( this, a, b );
	}
	public void divide( Tuple4 b ) {
		setToDivision( this, b );
	}
	public static Tuple4 setReturnValueToDivision( Tuple4 rv, Tuple4 a, double b ) {
		rv.x = a.x / b;
		rv.y = a.y / b;
		rv.z = a.z / b;
		rv.w = a.w / b;
		return rv;
	}
	public void setToDivision( Tuple4 a, double b ) {
		setReturnValueToDivision( this, a, b );
	}
	public void divide( double b ) {
		setToDivision( this, b );
	}


	//Interpolate
	public static Tuple4 setReturnValueToInterpolation( Tuple4 rv, Tuple4 a, Tuple4 b, double portion ) {
		rv.x = a.x + ( b.x - a.x ) * portion;
		rv.y = a.y + ( b.y - a.y ) * portion;
		rv.z = a.z + ( b.z - a.z ) * portion;
		rv.w = a.w + ( b.w - a.w ) * portion;
		return rv;
	}
	public void setToInterpolation( Tuple4 a, Tuple4 b, double portion ) {
		setReturnValueToInterpolation( this, a, b, portion );
	}

	//Magnitude
	public static double calculateMagnitudeSquared( double x, double y, double z, double w ) {
		return x*x + y*y + z*z + w*w;
	}
	public static double calculateMagnitude( double x, double y, double z, double w ) {
		double magnitudeSquared = calculateMagnitudeSquared( x, y, z, w );
		if( magnitudeSquared == 1.0 ) {
			return 1.0;
		} else {
			return Math.sqrt( magnitudeSquared );
		}
	}

	public double calculateMagnitudeSquared() {
		return Tuple4.calculateMagnitudeSquared( x, y, z, w );
	}
	public double calculateMagnitude() {
		return Tuple4.calculateMagnitude( x, y, z, w );
	}

	//Normalize
	public static Tuple4 setReturnValueToNormalized( Tuple4 rv, Tuple4 a ) {
		rv.set( a );
		double magnitudeSquared = rv.calculateMagnitudeSquared();
		if( magnitudeSquared != 1.0 ) {
			rv.divide( Math.sqrt( magnitudeSquared ) );
		}
		return rv;
	}
	public void setToNormalized( Tuple4 a ) {
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
		sb.append( ";w=" );
		sb.append( w );
		sb.append( "]" );
		return sb.toString();
	}
}
