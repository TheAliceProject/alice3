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
public class UnitQuaternion implements Orientation, edu.cmu.cs.dennisc.print.Printable {
	public double x = Double.NaN;
	public double y = Double.NaN;
	public double z = Double.NaN;
	public double w = Double.NaN;

	private UnitQuaternion() {
	}

	public UnitQuaternion( double x, double y, double z, double w ) {
		set( x, y, z, w );
	}

	public UnitQuaternion( OrthogonalMatrix3x3 other ) {
		setValue( other );
	}

	public UnitQuaternion( UnitQuaternion other ) {
		setValue( other );
	}

	public UnitQuaternion( AxisRotation other ) {
		setValue( other );
	}

	public UnitQuaternion( EulerAngles other ) {
		setValue( other );
	}

	public UnitQuaternion( ForwardAndUpGuide other ) {
		setValue( other );
	}

	@Override
	public OrthogonalMatrix3x3 createOrthogonalMatrix3x3() {
		return new OrthogonalMatrix3x3( this );
	}

	@Override
	public UnitQuaternion createUnitQuaternion() {
		return new UnitQuaternion( this );
	}

	@Override
	public AxisRotation createAxisRotation() {
		return new AxisRotation( this );
	}

	@Override
	public EulerAngles createEulerAngles() {
		return new EulerAngles( this );
	}

	@Override
	public ForwardAndUpGuide createForwardAndUpGuide() {
		return new ForwardAndUpGuide( this );
	}

	@Override
	public OrthogonalMatrix3x3 getValue( OrthogonalMatrix3x3 rv ) {
		rv.setValue( this );
		return rv;
	}

	@Override
	public UnitQuaternion getValue( UnitQuaternion rv ) {
		rv.setValue( this );
		return rv;
	}

	@Override
	public AxisRotation getValue( AxisRotation rv ) {
		rv.setValue( this );
		return rv;
	}

	@Override
	public EulerAngles getValue( EulerAngles rv ) {
		rv.setValue( this );
		return rv;
	}

	@Override
	public ForwardAndUpGuide getValue( ForwardAndUpGuide rv ) {
		rv.setValue( this );
		return rv;
	}

	public void decode( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		x = binaryDecoder.decodeDouble();
		y = binaryDecoder.decodeDouble();
		z = binaryDecoder.decodeDouble();
		w = binaryDecoder.decodeDouble();
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
			rv.append( "+-       -+q\n" );
		}
		return rv;
	}

	//NaN
	public static UnitQuaternion setReturnValueToNaN( UnitQuaternion rv ) {
		rv.x = rv.y = rv.z = rv.w = Double.NaN;
		return rv;
	}

	public static UnitQuaternion createNaN() {
		return setReturnValueToNaN( new UnitQuaternion() );
	}

	@Override
	public void setNaN() {
		setReturnValueToNaN( this );
	}

	@Override
	public boolean isNaN() {
		return Double.isNaN( x ) || Double.isNaN( y ) || Double.isNaN( z ) || Double.isNaN( w );
	}

	//Identity
	private static final UnitQuaternion IDENTITY = UnitQuaternion.createNaN();

	public static UnitQuaternion accessIdentity() {
		IDENTITY.setIdentity();
		return IDENTITY;
	}

	public static UnitQuaternion setReturnValueToIdentity( UnitQuaternion rv ) {
		rv.x = rv.y = rv.z = 0.0;
		rv.w = 1.0;
		return rv;
	}

	public static UnitQuaternion createIdentity() {
		return setReturnValueToIdentity( UnitQuaternion.createNaN() );
	}

	@Override
	public void setIdentity() {
		setReturnValueToIdentity( this );
	}

	@Override
	public boolean isIdentity() {
		return ( x == 0.0 ) && ( y == 0.0 ) && ( z == 0.0 ) && ( w == 1.0 );
	}

	@Override
	public boolean equals( Object obj ) {
		if( super.equals( obj ) ) {
			return true;
		} else {
			if( obj instanceof UnitQuaternion ) {
				UnitQuaternion q = (UnitQuaternion)obj;
				return ( x == q.x ) && ( y == q.y ) && ( z == q.z ) && ( w == q.w );
			} else {
				return false;
			}
		}
	}

	private static boolean isWithinEpsilon( double x, double y, double z, double w, UnitQuaternion q, double epsilon ) {
		return ( Math.abs( x - q.x ) < epsilon ) && ( Math.abs( y - q.y ) < epsilon ) && ( Math.abs( z - q.z ) < epsilon ) && ( Math.abs( w - q.w ) < epsilon );
	}

	public boolean isWithinEpsilonOrIsNegativeWithinEpsilon( UnitQuaternion q, double epsilon ) {
		return UnitQuaternion.isWithinEpsilon( this.x, this.y, this.z, this.w, q, epsilon ) || UnitQuaternion.isWithinEpsilon( -this.x, -this.y, -this.z, -this.w, q, epsilon );
	}

	public boolean isWithinReasonableEpsilonOrIsNegativeWithinReasonableEpsilon( UnitQuaternion q ) {
		return UnitQuaternion.isWithinEpsilon( this.x, this.y, this.z, this.w, q, EpsilonUtilities.REASONABLE_EPSILON ) || UnitQuaternion.isWithinEpsilon( -this.x, -this.y, -this.z, -this.w, q, EpsilonUtilities.REASONABLE_EPSILON );
	}

	public void set( double x, double y, double z, double w ) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	@Override
	public void setValue( OrthogonalMatrix3x3 m ) {
		//todo: convert directly
		if( m.isWithinReasonableEpsilonOfUnitLengthSquared() ) {
			//pass
		} else {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "WARNING: UnitQuaternion set to non-unit Matrix3x3" );
			edu.cmu.cs.dennisc.print.PrintUtilities.printlns( m );
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "right magnitude:   ", m.right.calculateMagnitude() );
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "up magnitude:      ", m.up.calculateMagnitude() );
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "backward magnitude:", m.backward.calculateMagnitude() );
			edu.cmu.cs.dennisc.print.PrintUtilities.println();
		}
		setValue( new AxisRotation( m ) );
	}

	@Override
	public void setValue( UnitQuaternion other ) {
		this.x = other.x;
		this.y = other.y;
		this.z = other.z;
		this.w = other.w;
	}

	@Override
	public void setValue( AxisRotation aa ) {
		double halfThetaInRadians = aa.angle.getAsRadians() * 0.5;
		double c = Math.cos( halfThetaInRadians );
		double s = Math.sin( halfThetaInRadians );
		w = c;
		x = aa.axis.x * s;
		y = aa.axis.y * s;
		z = aa.axis.z * s;
	}

	@Override
	public void setValue( EulerAngles ea ) {
		//todo: convert directly
		setValue( new AxisRotation( ea ) );
	}

	@Override
	public void setValue( ForwardAndUpGuide faug ) {
		//todo: convert directly
		setValue( new AxisRotation( faug ) );
	}

	//Add
	public static UnitQuaternion setReturnValueToAddition( UnitQuaternion rv, UnitQuaternion a, UnitQuaternion b ) {
		rv.x = a.x + b.x;
		rv.y = a.y + b.y;
		rv.z = a.z + b.z;
		rv.w = a.w + b.w;
		return rv;
	}

	public static UnitQuaternion createAddition( UnitQuaternion a, UnitQuaternion b ) {
		return setReturnValueToAddition( UnitQuaternion.createNaN(), a, b );
	}

	public void setToAddition( UnitQuaternion a, UnitQuaternion b ) {
		setReturnValueToAddition( this, a, b );
	}

	public void add( UnitQuaternion b ) {
		setToAddition( this, b );
	}

	//Subtract
	public static UnitQuaternion setReturnValueToSubtraction( UnitQuaternion rv, UnitQuaternion a, UnitQuaternion b ) {
		rv.x = a.x - b.x;
		rv.y = a.y - b.y;
		rv.z = a.z - b.z;
		rv.w = a.w - b.w;
		return rv;
	}

	public static UnitQuaternion createSubtraction( UnitQuaternion a, UnitQuaternion b ) {
		return setReturnValueToSubtraction( UnitQuaternion.createNaN(), a, b );
	}

	public void setToSubtraction( UnitQuaternion a, UnitQuaternion b ) {
		setReturnValueToSubtraction( this, a, b );
	}

	public void subtract( UnitQuaternion b ) {
		setToSubtraction( this, b );
	}

	//Negate
	public static UnitQuaternion setReturnValueToNegation( UnitQuaternion rv, UnitQuaternion a ) {
		rv.x = -a.x;
		rv.y = -a.y;
		rv.z = -a.z;
		rv.w = -a.w;
		return rv;
	}

	public static UnitQuaternion createNegation( UnitQuaternion a ) {
		return setReturnValueToNegation( UnitQuaternion.createNaN(), a );
	}

	public void setToNegation( UnitQuaternion a ) {
		setReturnValueToNegation( this, a );
	}

	public void negate() {
		setToNegation( this );
	}

	//Multiply
	public static UnitQuaternion setReturnValueToMultiplication( UnitQuaternion rv, UnitQuaternion a, UnitQuaternion b ) {
		throw new RuntimeException( "TODO" );

		//		double w = a.w * b.w - a.x * b.x - a.y * b.y - a.z * b.z;
		//		double x = a.w * b.x + a.x * b.w + a.y * b.z - a.z * b.y;
		//		double y = a.w * b.y - a.x * b.z + a.y * b.w + a.z * b.x;
		//		double z = a.w * b.z + a.x * b.y - a.y * b.x + a.z * b.w;
		//		rv.x = x;
		//		rv.y = y;
		//		rv.z = z;
		//		rv.w = w;

		//or

		//		rv.x = a.x * b.x;
		//		rv.y = a.y * b.y;
		//		rv.z = a.z * b.z;
		//		rv.w = a.w * b.w;
		//		return rv;
	}

	public static UnitQuaternion createMultiplication( UnitQuaternion a, UnitQuaternion b ) {
		return setReturnValueToMultiplication( UnitQuaternion.createNaN(), a, b );
	}

	public void setToMultiplication( UnitQuaternion a, UnitQuaternion b ) {
		setReturnValueToMultiplication( this, a, b );
	}

	public void multiply( UnitQuaternion b ) {
		setToMultiplication( this, b );
	}

	public static UnitQuaternion setReturnValueToMultiplication( UnitQuaternion rv, UnitQuaternion a, double b ) {
		rv.x = a.x * b;
		rv.y = a.y * b;
		rv.z = a.z * b;
		rv.w = a.w * b;
		return rv;
	}

	public static UnitQuaternion createMultiplication( UnitQuaternion a, double b ) {
		return setReturnValueToMultiplication( UnitQuaternion.createNaN(), a, b );
	}

	public void setToMultiplication( UnitQuaternion a, double b ) {
		setReturnValueToMultiplication( this, a, b );
	}

	public void multiply( double b ) {
		setToMultiplication( this, b );
	}

	//Divide
	//	public static UnitQuaternionD setReturnValueToDivision( UnitQuaternionD rv, UnitQuaternionD a, UnitQuaternionD b ) {
	//		rv.x = a.x / b.x;
	//		rv.y = a.y / b.y;
	//		rv.z = a.z / b.z;
	//		rv.w = a.w / b.w;
	//		return rv;
	//	}
	//	public static UnitQuaternionD createDivision( UnitQuaternionD a, UnitQuaternionD b ) {
	//		return setReturnValueToDivision( UnitQuaternionD.createNaN(), a, b );
	//	}
	//	public void setToDivision( UnitQuaternionD a, UnitQuaternionD b ) {
	//		setReturnValueToDivision( this, a, b );
	//	}
	//	public void divide( UnitQuaternionD b ) {
	//		setToDivision( this, b );
	//	}
	public static UnitQuaternion setReturnValueToDivision( UnitQuaternion rv, UnitQuaternion a, double b ) {
		rv.x = a.x / b;
		rv.y = a.y / b;
		rv.z = a.z / b;
		rv.w = a.w / b;
		return rv;
	}

	public static UnitQuaternion createDivision( UnitQuaternion a, double b ) {
		return setReturnValueToDivision( UnitQuaternion.createNaN(), a, b );
	}

	public void setToDivision( UnitQuaternion a, double b ) {
		setReturnValueToDivision( this, a, b );
	}

	public void divide( double b ) {
		setToDivision( this, b );
	}

	//Interpolate
	public static UnitQuaternion setReturnValueToInterpolation( UnitQuaternion rv, UnitQuaternion a, UnitQuaternion b, double portion ) {
		final double EPSILON = 0.0001;
		assert a.isNaN() == false;
		assert b.isNaN() == false;
		if( portion == 0.0 ) {
			rv.setValue( a );
		} else if( portion == 1.0 ) {
			rv.setValue( b );
		} else if( a.isWithinEpsilonOrIsNegativeWithinEpsilon( b, EPSILON ) ) {
			rv.setValue( b );
		} else {
			double dotProduct = ( a.x * b.x ) + ( a.y * b.y ) + ( a.z * b.z ) + ( a.w * b.w );
			double b_x;
			double b_y;
			double b_z;
			double b_w;
			if( dotProduct < 0.0 ) {
				b_x = -b.x;
				b_y = -b.y;
				b_z = -b.z;
				b_w = -b.w;
				dotProduct = -dotProduct;
			} else {
				b_x = b.x;
				b_y = b.y;
				b_z = b.z;
				b_w = b.w;
			}
			double aPortion;
			double bPortion;
			final double THRESHOLD_TO_PERFORM_SIMPLE_LINEAR_INTERPOLATION = 0.05;
			if( ( 1 - dotProduct ) < THRESHOLD_TO_PERFORM_SIMPLE_LINEAR_INTERPOLATION ) {
				aPortion = 1 - portion;
				bPortion = portion;
			} else {
				double halfAngle = Math.acos( dotProduct );
				//				double sineHalfAngle = Math.sin( halfAngle );
				double sineHalfAngle = Math.sqrt( 1.0 - ( dotProduct * dotProduct ) );
				aPortion = Math.sin( ( 1 - portion ) * halfAngle ) / sineHalfAngle;
				bPortion = Math.sin( portion * halfAngle ) / sineHalfAngle;
			}
			rv.x = ( a.x * aPortion ) + ( b_x * bPortion );
			rv.y = ( a.y * aPortion ) + ( b_y * bPortion );
			rv.z = ( a.z * aPortion ) + ( b_z * bPortion );
			rv.w = ( a.w * aPortion ) + ( b_w * bPortion );
			//normalizeIfNecessary( 0.001 );
		}
		assert rv.isNaN() == false;
		return rv;
	}

	public static UnitQuaternion createInterpolation( UnitQuaternion a, UnitQuaternion b, double portion ) {
		return setReturnValueToInterpolation( new UnitQuaternion(), a, b, portion );
	}

	public void setToInterpolation( UnitQuaternion a, UnitQuaternion b, double portion ) {
		setReturnValueToInterpolation( this, a, b, portion );
	}

	//Dot Product
	public static double calculateDotProduct( UnitQuaternion a, UnitQuaternion b ) {
		return ( a.x * b.x ) + ( a.y * b.y ) + ( a.z * b.z ) + ( a.w * b.w );
	}

	//Magnitude
	public static double calculateMagnitudeSquared( double x, double y, double z, double w ) {
		return ( x * x ) + ( y * y ) + ( z * z ) + ( w * w );
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
		return UnitQuaternion.calculateMagnitudeSquared( x, y, z, w );
	}

	public double calculateMagnitude() {
		return UnitQuaternion.calculateMagnitude( x, y, z, w );
	}

	//Normalize
	public static UnitQuaternion setReturnValueToNormalized( UnitQuaternion rv, UnitQuaternion a ) {
		double magnitudeSquared = a.calculateMagnitudeSquared();
		if( magnitudeSquared != 1.0 ) {
			rv.divide( Math.sqrt( magnitudeSquared ) );
		}
		return rv;
	}

	public static UnitQuaternion createNormalized( UnitQuaternion a ) {
		return setReturnValueToNormalized( UnitQuaternion.createNaN(), a );
	}

	public void setToNormalized( UnitQuaternion a ) {
		setReturnValueToNormalized( this, a );
	}

	public void normalize() {
		setToNormalized( this );
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append( UnitQuaternion.class.getName() );
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
