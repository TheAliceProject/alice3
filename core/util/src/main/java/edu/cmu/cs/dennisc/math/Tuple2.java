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
public abstract class Tuple2 implements edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable, edu.cmu.cs.dennisc.print.Printable {
	public double x = 0.0;
	public double y = 0.0;

	public Tuple2() {
	}

	public Tuple2( Tuple2 other ) {
		set( other );
	}

	public Tuple2( double x, double y ) {
		set( x, y );
	}

	public void decode( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		x = binaryDecoder.decodeDouble();
		y = binaryDecoder.decodeDouble();
	}

	@Override
	public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		binaryEncoder.encode( x );
		binaryEncoder.encode( y );
	}

	@Override
	public Appendable append( Appendable rv, java.text.DecimalFormat decimalFormat, boolean isLines ) throws java.io.IOException {
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
			rv.append( "+-       -+\n" );
		}
		return rv;
	}

	public boolean isWithinEpsilonOf( double x, double y, double epsilon ) {
		return EpsilonUtilities.isWithinEpsilon( this.x, x, epsilon ) && EpsilonUtilities.isWithinEpsilon( this.y, y, epsilon );
	}

	public boolean isWithinReasonableEpsilonOf( double x, double y ) {
		return isWithinEpsilonOf( x, y, EpsilonUtilities.REASONABLE_EPSILON );
	}

	public boolean isWithinEpsilonOf( Tuple2 other, double epsilon ) {
		return isWithinEpsilonOf( other.x, other.y, epsilon );
	}

	public boolean isWithinReasonableEpsilonOf( Tuple2 other ) {
		return isWithinEpsilonOf( other, EpsilonUtilities.REASONABLE_EPSILON );
	}

	public boolean isWithinEpsilonOfUnitLengthSquared( double epsilon ) {
		return EpsilonUtilities.isWithinEpsilonOf1InSquaredSpace( calculateMagnitudeSquared(), epsilon );
	}

	public boolean isWithinReasonableEpsilonOfUnitLengthSquared() {
		return isWithinEpsilonOfUnitLengthSquared( EpsilonUtilities.REASONABLE_EPSILON );
	}

	public void set( Tuple2 other ) {
		if( other != null ) {
			this.x = other.x;
			this.y = other.y;
		} else {
			setNaN();
		}
	}

	public void set( double x, double y ) {
		this.x = x;
		this.y = y;
	}

	//Zero
	public static Tuple2 setReturnValueToZero( Tuple2 rv ) {
		rv.x = rv.y = 0.0;
		return rv;
	}

	public void setZero() {
		setReturnValueToZero( this );
	}

	public boolean isZero() {
		return ( x == 0.0 ) && ( y == 0.0 );
	}

	//NaN
	public static Tuple2 setReturnValueToNaN( Tuple2 rv ) {
		rv.x = rv.y = Double.NaN;
		return rv;
	}

	public void setNaN() {
		setReturnValueToNaN( this );
	}

	public boolean isNaN() {
		return Double.isNaN( x ) || Double.isNaN( y );
	}

	//Add
	public static Tuple2 setReturnValueToAddition( Tuple2 rv, Tuple2 a, Tuple2 b ) {
		rv.x = a.x + b.x;
		rv.y = a.y + b.y;
		return rv;
	}

	public void setToAddition( Tuple2 a, Tuple2 b ) {
		setReturnValueToAddition( this, a, b );
	}

	public void add( Tuple2 b ) {
		setToAddition( this, b );
	}

	//Subtract
	public static Tuple2 setReturnValueToSubtraction( Tuple2 rv, Tuple2 a, Tuple2 b ) {
		rv.x = a.x - b.x;
		rv.y = a.y - b.y;
		return rv;
	}

	public void setToSubtraction( Tuple2 a, Tuple2 b ) {
		setReturnValueToSubtraction( this, a, b );
	}

	public void subtract( Tuple2 b ) {
		setToSubtraction( this, b );
	}

	//Negate
	public static Tuple2 setReturnValueToNegation( Tuple2 rv, Tuple2 a ) {
		rv.x = -a.x;
		rv.y = -a.y;
		return rv;
	}

	public void setToNegation( Tuple2 a ) {
		setReturnValueToNegation( this, a );
	}

	public void negate() {
		setToNegation( this );
	}

	//Multiply
	public static Tuple2 setReturnValueToMultiplication( Tuple2 rv, Tuple2 a, Tuple2 b ) {
		rv.x = a.x * b.x;
		rv.y = a.y * b.y;
		return rv;
	}

	public void setToMultiplication( Tuple2 a, Tuple2 b ) {
		setReturnValueToMultiplication( this, a, b );
	}

	public void multiply( Tuple2 b ) {
		setToMultiplication( this, b );
	}

	public static Tuple2 setReturnValueToMultiplication( Tuple2 rv, Tuple2 a, double b ) {
		rv.x = a.x * b;
		rv.y = a.y * b;
		return rv;
	}

	public void setToMultiplication( Tuple2 a, double b ) {
		setReturnValueToMultiplication( this, a, b );
	}

	public void multiply( double b ) {
		setToMultiplication( this, b );
	}

	//Divide
	public static Tuple2 setReturnValueToDivision( Tuple2 rv, Tuple2 a, Tuple2 b ) {
		rv.x = a.x / b.x;
		rv.y = a.y / b.y;
		return rv;
	}

	public void setToDivision( Tuple2 a, Tuple2 b ) {
		setReturnValueToDivision( this, a, b );
	}

	public void divide( Tuple2 b ) {
		setToDivision( this, b );
	}

	public static Tuple2 setReturnValueToDivision( Tuple2 rv, Tuple2 a, double b ) {
		rv.x = a.x / b;
		rv.y = a.y / b;
		return rv;
	}

	public void setToDivision( Tuple2 a, double b ) {
		setReturnValueToDivision( this, a, b );
	}

	public void divide( double b ) {
		setToDivision( this, b );
	}

	//Interpolate
	public static Tuple2 setReturnValueToInterpolation( Tuple2 rv, Tuple2 a, Tuple2 b, double portion ) {
		rv.x = a.x + ( ( b.x - a.x ) * portion );
		rv.y = a.y + ( ( b.y - a.y ) * portion );
		return rv;
	}

	public void setToInterpolation( Tuple2 a, Tuple2 b, double portion ) {
		setReturnValueToInterpolation( this, a, b, portion );
	}

	//Magnitude
	public static double calculateMagnitudeSquared( double x, double y ) {
		return ( x * x ) + ( y * y );
	}

	public static double calculateMagnitude( double x, double y ) {
		double magnitudeSquared = calculateMagnitudeSquared( x, y );
		if( magnitudeSquared == 1.0 ) {
			return 1.0;
		} else {
			return Math.sqrt( magnitudeSquared );
		}
	}

	public double calculateMagnitudeSquared() {
		return Tuple2.calculateMagnitudeSquared( x, y );
	}

	public double calculateMagnitude() {
		return Tuple2.calculateMagnitude( x, y );
	}

	//Normalize
	public static Tuple2 setReturnValueToNormalized( Tuple2 rv, Tuple2 a ) {
		rv.set( a );
		double magnitudeSquared = rv.calculateMagnitudeSquared();
		if( magnitudeSquared != 1.0 ) {
			rv.divide( Math.sqrt( magnitudeSquared ) );
		}
		return rv;
	}

	public void setToNormalized( Tuple2 a ) {
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
					Tuple2 other = (Tuple2)o;
					return ( Double.compare( this.x, other.x ) == 0 ) && ( Double.compare( this.y, other.y ) == 0 );
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

		rv = ( 37 * rv ) + this.getClass().hashCode();

		lng = Double.doubleToLongBits( this.x );
		rv = ( 37 * rv ) + (int)( lng ^ ( lng >>> 32 ) );

		lng = Double.doubleToLongBits( this.y );
		rv = ( 37 * rv ) + (int)( lng ^ ( lng >>> 32 ) );

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
		sb.append( "]" );
		return sb.toString();
	}
}
