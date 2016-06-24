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
public abstract class Tuple4f implements edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable, edu.cmu.cs.dennisc.print.Printable {
	public float x = 0.0f;
	public float y = 0.0f;
	public float z = 0.0f;
	public float w = 0.0f;

	public Tuple4f() {
	}

	public Tuple4f( Tuple4f other ) {
		set( other );
	}

	public Tuple4f( float x, float y, float z, float w ) {
		set( x, y, z, w );
	}

	public void decode( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		x = binaryDecoder.decodeFloat();
		y = binaryDecoder.decodeFloat();
		z = binaryDecoder.decodeFloat();
		w = binaryDecoder.decodeFloat();
	}

	@Override
	public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		binaryEncoder.encode( x );
		binaryEncoder.encode( y );
		binaryEncoder.encode( z );
		binaryEncoder.encode( w );
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

	public boolean isWithinEpsilonOf( float x, float y, float z, float w, float epsilon ) {
		return EpsilonUtilities.isWithinEpsilon( this.x, x, epsilon ) && EpsilonUtilities.isWithinEpsilon( this.y, y, epsilon ) && EpsilonUtilities.isWithinEpsilon( this.z, z, epsilon ) && EpsilonUtilities.isWithinEpsilon( this.w, w, epsilon );
	}

	public boolean isWithinReasonableEpsilonOf( float x, float y, float z, float w ) {
		return isWithinEpsilonOf( x, y, z, w, EpsilonUtilities.REASONABLE_EPSILON_FLOAT );
	}

	public boolean isWithinEpsilonOf( Tuple4f other, float epsilon ) {
		return isWithinEpsilonOf( other.x, other.y, other.z, other.w, epsilon );
	}

	public boolean isWithinReasonableEpsilonOf( Tuple4f other ) {
		return isWithinEpsilonOf( other, EpsilonUtilities.REASONABLE_EPSILON_FLOAT );
	}

	public boolean isWithinEpsilonOfUnitLengthSquared( float epsilon ) {
		return EpsilonUtilities.isWithinEpsilonOf1InSquaredSpace( calculateMagnitudeSquared(), epsilon );
	}

	public boolean isWithinReasonableEpsilonOfUnitLengthSquared() {
		return isWithinEpsilonOfUnitLengthSquared( EpsilonUtilities.REASONABLE_EPSILON_FLOAT );
	}

	public void set( Tuple4f other ) {
		if( other != null ) {
			this.x = other.x;
			this.y = other.y;
			this.z = other.z;
			this.w = other.w;
		} else {
			setNaN();
		}
	}

	public void set( float x, float y, float z, float w ) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	//Zero
	public static Tuple4f setReturnValueToZero( Tuple4f rv ) {
		rv.x = rv.y = rv.z = rv.w = 0.0f;
		return rv;
	}

	public void setZero() {
		setReturnValueToZero( this );
	}

	public boolean isZero() {
		return ( x == 0.0f ) && ( y == 0.0f ) && ( z == 0.0f ) && ( w == 0.0f );
	}

	//NaN
	public static Tuple4f setReturnValueToNaN( Tuple4f rv ) {
		rv.x = rv.y = rv.z = rv.w = Float.NaN;
		return rv;
	}

	public void setNaN() {
		setReturnValueToNaN( this );
	}

	public boolean isNaN() {
		return Float.isNaN( x ) || Float.isNaN( y ) || Float.isNaN( z ) || Float.isNaN( w );
	}

	//Add
	public static Tuple4f setReturnValueToAddition( Tuple4f rv, Tuple4f a, Tuple4f b ) {
		rv.x = a.x + b.x;
		rv.y = a.y + b.y;
		rv.z = a.z + b.z;
		rv.w = a.w + b.w;
		return rv;
	}

	public void setToAddition( Tuple4f a, Tuple4f b ) {
		setReturnValueToAddition( this, a, b );
	}

	public void add( Tuple4f b ) {
		setToAddition( this, b );
	}

	//Subtract
	public static Tuple4f setReturnValueToSubtraction( Tuple4f rv, Tuple4f a, Tuple4f b ) {
		rv.x = a.x - b.x;
		rv.y = a.y - b.y;
		rv.z = a.z - b.z;
		rv.w = a.w - b.w;
		return rv;
	}

	public void setToSubtraction( Tuple4f a, Tuple4f b ) {
		setReturnValueToSubtraction( this, a, b );
	}

	public void subtract( Tuple4f b ) {
		setToSubtraction( this, b );
	}

	//Negate
	public static Tuple4f setReturnValueToNegation( Tuple4f rv, Tuple4f a ) {
		rv.x = -a.x;
		rv.y = -a.y;
		rv.z = -a.z;
		rv.w = -a.w;
		return rv;
	}

	public void setToNegation( Tuple4f a ) {
		setReturnValueToNegation( this, a );
	}

	public void negate() {
		setToNegation( this );
	}

	//Multiply
	public static Tuple4f setReturnValueToMultiplication( Tuple4f rv, Tuple4f a, Tuple4f b ) {
		rv.x = a.x * b.x;
		rv.y = a.y * b.y;
		rv.z = a.z * b.z;
		rv.w = a.w * b.w;
		return rv;
	}

	public void setToMultiplication( Tuple4f a, Tuple4f b ) {
		setReturnValueToMultiplication( this, a, b );
	}

	public void multiply( Tuple4f b ) {
		setToMultiplication( this, b );
	}

	public static Tuple4f setReturnValueToMultiplication( Tuple4f rv, Tuple4f a, float b ) {
		rv.x = a.x * b;
		rv.y = a.y * b;
		rv.z = a.z * b;
		rv.w = a.w * b;
		return rv;
	}

	public void setToMultiplication( Tuple4f a, float b ) {
		setReturnValueToMultiplication( this, a, b );
	}

	public void multiply( float b ) {
		setToMultiplication( this, b );
	}

	//Divide
	public static Tuple4f setReturnValueToDivision( Tuple4f rv, Tuple4f a, Tuple4f b ) {
		rv.x = a.x / b.x;
		rv.y = a.y / b.y;
		rv.z = a.z / b.z;
		rv.w = a.w / b.w;
		return rv;
	}

	public void setToDivision( Tuple4f a, Tuple4f b ) {
		setReturnValueToDivision( this, a, b );
	}

	public void divide( Tuple4f b ) {
		setToDivision( this, b );
	}

	public static Tuple4f setReturnValueToDivision( Tuple4f rv, Tuple4f a, float b ) {
		rv.x = a.x / b;
		rv.y = a.y / b;
		rv.z = a.z / b;
		rv.w = a.w / b;
		return rv;
	}

	public void setToDivision( Tuple4f a, float b ) {
		setReturnValueToDivision( this, a, b );
	}

	public void divide( float b ) {
		setToDivision( this, b );
	}

	//Interpolate
	public static Tuple4f setReturnValueToInterpolation( Tuple4f rv, Tuple4f a, Tuple4f b, float portion ) {
		rv.x = a.x + ( ( b.x - a.x ) * portion );
		rv.y = a.y + ( ( b.y - a.y ) * portion );
		rv.z = a.z + ( ( b.z - a.z ) * portion );
		rv.w = a.w + ( ( b.w - a.w ) * portion );
		return rv;
	}

	public void setToInterpolation( Tuple4f a, Tuple4f b, float portion ) {
		setReturnValueToInterpolation( this, a, b, portion );
	}

	//Magnitude
	public static float calculateMagnitudeSquared( float x, float y, float z, float w ) {
		return ( x * x ) + ( y * y ) + ( z * z ) + ( w * w );
	}

	public static float calculateMagnitude( float x, float y, float z, float w ) {
		float magnitudeSquared = calculateMagnitudeSquared( x, y, z, w );
		if( magnitudeSquared == 1.0 ) {
			return 1.0f;
		} else {
			return (float)Math.sqrt( magnitudeSquared );
		}
	}

	public float calculateMagnitudeSquared() {
		return Tuple4f.calculateMagnitudeSquared( x, y, z, w );
	}

	public float calculateMagnitude() {
		return Tuple4f.calculateMagnitude( x, y, z, w );
	}

	//Normalize
	public static Tuple4f setReturnValueToNormalized( Tuple4f rv, Tuple4f a ) {
		rv.set( a );
		float magnitudeSquared = rv.calculateMagnitudeSquared();
		if( magnitudeSquared != 1.0 ) {
			rv.divide( (float)Math.sqrt( magnitudeSquared ) );
		}
		return rv;
	}

	public void setToNormalized( Tuple4f a ) {
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
					Tuple4f other = (Tuple4f)o;
					return ( Float.compare( this.x, other.x ) == 0 ) && ( Float.compare( this.y, other.y ) == 0 ) && ( Float.compare( this.z, other.z ) == 0 ) && ( Float.compare( this.w, other.w ) == 0 );
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
		rv = ( 37 * rv ) + this.getClass().hashCode();
		rv = ( 37 * rv ) + Float.floatToIntBits( this.x );
		rv = ( 37 * rv ) + Float.floatToIntBits( this.y );
		rv = ( 37 * rv ) + Float.floatToIntBits( this.z );
		rv = ( 37 * rv ) + Float.floatToIntBits( this.w );
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
		sb.append( ";w=" );
		sb.append( w );
		sb.append( "]" );
		return sb.toString();
	}
}
