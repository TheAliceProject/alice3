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
public abstract class Tuple3 implements edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable, edu.cmu.cs.dennisc.print.Printable {
	public double x = 0.0;
	public double y = 0.0;
	public double z = 0.0;

	public Tuple3() {
	}
	public Tuple3( Tuple3 other ) {
		set( other );
	}
	public Tuple3( double x, double y, double z ) {
		set( x, y, z );
	}

	public void decode(edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder) {
		x = binaryDecoder.decodeDouble();
		y = binaryDecoder.decodeDouble();
		z = binaryDecoder.decodeDouble();
	}
	public void encode(edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder) {
		binaryEncoder.encode( x );
		binaryEncoder.encode( y );
		binaryEncoder.encode( z );
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
			rv.append( "+-       -+\n" );
		}
		return rv;
	}

	public boolean isWithinEpsilonOf( Tuple3 other, double epsilon ) {
		return EpsilonUtilities.isWithinEpsilon( this.x, other.x, epsilon ) && EpsilonUtilities.isWithinEpsilon( this.y, other.y, epsilon ) && EpsilonUtilities.isWithinEpsilon( this.z, other.z, epsilon );
	}
	public boolean isWithinReasonableEpsilonOf( Tuple3 other ) {
		return isWithinEpsilonOf( other, EpsilonUtilities.REASONABLE_EPSILON );
	}

	public boolean isWithinEpsilonOfUnitLengthSquared( double epsilon ) {
		return EpsilonUtilities.isWithinEpsilonOf1InSquaredSpace( calculateMagnitudeSquared(), epsilon );
	}
	public boolean isWithinReasonableEpsilonOfUnitLengthSquared() {
		return isWithinEpsilonOfUnitLengthSquared( EpsilonUtilities.REASONABLE_EPSILON );
	}

	public void set( Tuple3 other ) {
		if( other != null ) {
			this.x = other.x;
			this.y = other.y;
			this.z = other.z;
		} else {
			setNaN();
		}
	}
	public void set( double x, double y, double z ) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	//Zero
	public static Tuple3 setReturnValueToZero( Tuple3 rv ) {
		rv.x = rv.y = rv.z = 0.0;
		return rv;
	}
	public void setZero() {
		setReturnValueToZero( this );
	}
	public boolean isZero() {
		return x==0.0 && y==0.0 && z==0.0;
	}

	//NaN
	public static Tuple3 setReturnValueToNaN( Tuple3 rv ) {
		rv.x = rv.y = rv.z = Double.NaN;
		return rv;
	}
	public void setNaN() {
		setReturnValueToNaN( this );
	}
	public boolean isNaN() {
		return Double.isNaN( x ) && Double.isNaN( y ) && Double.isNaN( z );
	}
	

	//Add
	public static Tuple3 setReturnValueToAddition( Tuple3 rv, Tuple3 a, Tuple3 b ) {
		rv.x = a.x + b.x;
		rv.y = a.y + b.y;
		rv.z = a.z + b.z;
		return rv;
	}
	public void setToAddition( Tuple3 a, Tuple3 b ) {
		setReturnValueToAddition( this, a, b );
	}
	public void add( Tuple3 b ) {
		setToAddition( this, b );
	}
	
	//Subtract
	public static Tuple3 setReturnValueToSubtraction( Tuple3 rv, Tuple3 a, Tuple3 b ) {
		rv.x = a.x - b.x;
		rv.y = a.y - b.y;
		rv.z = a.z - b.z;
		return rv;
	}
	public void setToSubtraction( Tuple3 a, Tuple3 b ) {
		setReturnValueToSubtraction( this, a, b );
	}
	public void subtract( Tuple3 b ) {
		setToSubtraction( this, b );
	}

	//Negate
	public static Tuple3 setReturnValueToNegation( Tuple3 rv, Tuple3 a ) {
		rv.x = -a.x;
		rv.y = -a.y;
		rv.z = -a.z;
		return rv;
	}
	public void setToNegation( Tuple3 a ) {
		setReturnValueToNegation( this, a );
	}
	public void negate() {
		setToNegation( this );
	}

	//Multiply
	public static Tuple3 setReturnValueToMultiplication( Tuple3 rv, Tuple3 a, Tuple3 b ) {
		rv.x = a.x * b.x;
		rv.y = a.y * b.y;
		rv.z = a.z * b.z;
		return rv;
	}
	public void setToMultiplication( Tuple3 a, Tuple3 b ) {
		setReturnValueToMultiplication( this, a, b );
	}
	public void multiply( Tuple3 b ) {
		setToMultiplication( this, b );
	}
	public static Tuple3 setReturnValueToMultiplication( Tuple3 rv, Tuple3 a, double b ) {
		rv.x = a.x * b;
		rv.y = a.y * b;
		rv.z = a.z * b;
		return rv;
	}
	public void setToMultiplication( Tuple3 a, double b ) {
		setReturnValueToMultiplication( this, a, b );
	}
	public void multiply( double b ) {
		setToMultiplication( this, b );
	}


	//Divide
	public static Tuple3 setReturnValueToDivision( Tuple3 rv, Tuple3 a, Tuple3 b ) {
		rv.x = a.x / b.x;
		rv.y = a.y / b.y;
		rv.z = a.z / b.z;
		return rv;
	}
	public void setToDivision( Tuple3 a, Tuple3 b ) {
		setReturnValueToDivision( this, a, b );
	}
	public void divide( Tuple3 b ) {
		setToDivision( this, b );
	}
	public static Tuple3 setReturnValueToDivision( Tuple3 rv, Tuple3 a, double b ) {
		rv.x = a.x / b;
		rv.y = a.y / b;
		rv.z = a.z / b;
		return rv;
	}
	public void setToDivision( Tuple3 a, double b ) {
		setReturnValueToDivision( this, a, b );
	}
	public void divide( double b ) {
		setToDivision( this, b );
	}


	//Interpolate
	public static Tuple3 setReturnValueToInterpolation( Tuple3 rv, Tuple3 a, Tuple3 b, double portion ) {
		rv.x = a.x + ( b.x - a.x ) * portion;
		rv.y = a.y + ( b.y - a.y ) * portion;
		rv.z = a.z + ( b.z - a.z ) * portion;
		return rv;
	}
	public void setToInterpolation( Tuple3 a, Tuple3 b, double portion ) {
		setReturnValueToInterpolation( this, a, b, portion );
	}
	

	//Magnitude
	public static double calculateMagnitudeSquared( double x, double y, double z ) {
		return x*x + y*y + z*z;
	}
	public static double calculateMagnitude( double x, double y, double z ) {
		double magnitudeSquared = calculateMagnitudeSquared( x, y, z );
		if( magnitudeSquared == 1.0 ) {
			return 1.0;
		} else {
			return Math.sqrt( magnitudeSquared );
		}
	}

	public double calculateMagnitudeSquared() {
		return Tuple3.calculateMagnitudeSquared( x, y, z );
	}
	public double calculateMagnitude() {
		return Tuple3.calculateMagnitude( x, y, z );
	}
	
	//Normalize
	public static Tuple3 setReturnValueToNormalized( Tuple3 rv, Tuple3 a ) {
		rv.set( a );
		double magnitudeSquared = rv.calculateMagnitudeSquared();
		if( magnitudeSquared != 1.0 ) {
			rv.divide( Math.sqrt( magnitudeSquared ) );
		}
		return rv;
	}
	public void setToNormalized( Tuple3 a ) {
		setReturnValueToNormalized( this, a );
	}
	public void normalize() {
		setToNormalized( this );
	}
	
	@Override
	public final boolean equals( Object o ) {
		if( this == o ) {
			return true;
		} else {
			if( o != null ) {
				if( this.getClass().equals( o.getClass() ) ) {
					Tuple3 other = (Tuple3)o;
					return Double.compare( this.x, other.x ) == 0 && Double.compare( this.y, other.y ) == 0 && Double.compare( this.z, other.z ) == 0;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
	}
	@Override
	public final int hashCode() {
		int rv = 17;
		long lng;
		
		rv = 37*rv + this.getClass().hashCode();

		lng = Double.doubleToLongBits( this.x );
		rv = 37*rv + (int)( lng ^(lng >>>32) );

		lng = Double.doubleToLongBits( this.y );
		rv = 37*rv + (int)( lng ^(lng >>>32) );

		lng = Double.doubleToLongBits( this.z );
		rv = 37*rv + (int)( lng ^(lng >>>32) );

		return rv;
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
