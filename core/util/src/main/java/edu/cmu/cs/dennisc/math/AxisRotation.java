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

import edu.cmu.cs.dennisc.codec.BinaryDecoder;
import edu.cmu.cs.dennisc.codec.BinaryEncoder;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.random.RandomUtilities;

/**
 * @author Dennis Cosgrove
 */
public class AxisRotation implements Orientation {
	public final Vector3 axis = Vector3.createNaN();
	public final Angle angle = new AngleInRadians( Double.NaN );

	private AxisRotation() {
	}

	public AxisRotation( Vector3 axis, Angle angle ) {
		set( axis, angle );
	}

	public AxisRotation( OrthogonalMatrix3x3 other ) {
		setValue( other );
	}

	public AxisRotation( UnitQuaternion other ) {
		setValue( other );
	}

	public AxisRotation( AxisRotation other ) {
		setValue( other );
	}

	public AxisRotation( EulerAngles other ) {
		setValue( other );
	}

	public AxisRotation( ForwardAndUpGuide other ) {
		setValue( other );
	}

	public void decode( BinaryDecoder binaryDecoder ) {
		axis.decode( binaryDecoder );
		angle.decode( binaryDecoder );
	}

	@Override
	public void encode( BinaryEncoder binaryEncoder ) {
		axis.encode( binaryEncoder );
		angle.encode( binaryEncoder );
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

	//	public void decode(edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder) {
	//		axis.decode( binaryDecoder );
	//		angle.decode( binaryDecoder );
	//	}
	//	public void encode(edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder) {
	//		axis.encode( binaryEncoder );
	//		angle.encode( binaryEncoder );
	//	}

	public static AxisRotation createXAxisRotation( Angle angle ) {
		return new AxisRotation( Vector3.accessPositiveXAxis(), angle );
	}

	public static AxisRotation createYAxisRotation( Angle angle ) {
		return new AxisRotation( Vector3.accessPositiveYAxis(), angle );
	}

	public static AxisRotation createZAxisRotation( Angle angle ) {
		return new AxisRotation( Vector3.accessPositiveZAxis(), angle );
	}

	public AxisRotation createCopy() {
		return createAxisRotation();
	}

	//NaN
	public static AxisRotation setReturnValueToNaN( AxisRotation rv ) {
		rv.axis.setNaN();
		rv.angle.setNaN();
		return rv;
	}

	public static AxisRotation createNaN() {
		return setReturnValueToNaN( new AxisRotation() );
	}

	@Override
	public void setNaN() {
		setReturnValueToNaN( this );
	}

	@Override
	public boolean isNaN() {
		return this.axis.isNaN() || this.angle.isNaN();
	}

	//Identity
	public static AxisRotation setReturnValueToIdentity( AxisRotation rv ) {
		rv.axis.set( 0, 0, 1 );
		rv.angle.setAsRadians( 0.0 );
		return rv;
	}

	public static AxisRotation createIdentity() {
		return setReturnValueToIdentity( AxisRotation.createNaN() );
	}

	@Override
	public void setIdentity() {
		setReturnValueToIdentity( this );
	}

	@Override
	public boolean isIdentity() {
		return this.axis.isPositiveXAxis() && ( this.angle.getAsRadians() == 0.0 );
	}

	private boolean isWithinReasonableEpsilonOfZero( double d ) {
		return Math.abs( d ) < EpsilonUtilities.REASONABLE_EPSILON;
	}

	private void set( double m00, double m01, double m02, double m10, double m11, double m12, double m20, double m21, double m22 ) {
		//todo: assert orthogonal
		double thetaInRadians;
		if( isWithinReasonableEpsilonOfZero( m01 - m10 ) && isWithinReasonableEpsilonOfZero( m02 - m20 ) && isWithinReasonableEpsilonOfZero( m12 - m21 ) ) {
			//singularity
			if( isWithinReasonableEpsilonOfZero( m01 + m10 ) && isWithinReasonableEpsilonOfZero( m02 + m20 ) && isWithinReasonableEpsilonOfZero( m12 + m21 ) && isWithinReasonableEpsilonOfZero( m12 + m21 ) && isWithinReasonableEpsilonOfZero( ( m00 + m11 + m22 ) - 3 ) ) {
				thetaInRadians = 0;
				this.axis.set( 0, 1, 0 ); //any axis will do
			} else {
				thetaInRadians = Math.PI;
				double x = ( m00 + 1 ) * 0.5;
				double y = ( m11 + 1 ) * 0.5;
				double z = ( m22 + 1 ) * 0.5;
				if( x > 0 ) {
					this.axis.x = Math.sqrt( x );
				} else {
					this.axis.x = 0.0;
				}
				if( y > 0 ) {
					this.axis.y = Math.sqrt( y );
				} else {
					this.axis.y = 0.0;
				}
				if( z > 0 ) {
					this.axis.z = Math.sqrt( z );
				} else {
					this.axis.z = 0.0;
				}
				assert this.axis.isNaN() == false;

				boolean isXZero = isWithinReasonableEpsilonOfZero( this.axis.x );
				boolean isYZero = isWithinReasonableEpsilonOfZero( this.axis.y );
				boolean isZZero = isWithinReasonableEpsilonOfZero( this.axis.z );

				boolean isXYNegativeOrZero = m01 <= 0;
				boolean isXZNegativeOrZero = m02 <= 0;
				boolean isYZNegativeOrZero = m12 <= 0;

				if( ( isXZero == true ) && ( isYZero == false ) && ( isZZero == false ) ) {
					if( isYZNegativeOrZero ) {
						this.axis.y = -this.axis.y;
					}
				} else if( ( isYZero == true ) && ( isZZero == false ) ) {
					if( isXZNegativeOrZero ) {
						this.axis.z = -this.axis.z;
					}
				} else if( isZZero == true ) {
					if( isXYNegativeOrZero ) {
						this.axis.x = -this.axis.x;
					}
				}

				//todo: remove?
				this.axis.normalize();
			}
		} else {
			double c = ( ( m00 + m11 + m22 ) - 1 ) * 0.5;
			assert Math.abs( c ) <= ( 1.0 + EpsilonUtilities.REASONABLE_EPSILON );
			c = Math.max( c, -1.0 );
			c = Math.min( c, 1.0 );
			thetaInRadians = Math.acos( c );
			if( Double.isNaN( thetaInRadians ) ) {
				Logger.severe( "AxisRotationD isNaN" );
			}
			assert Double.isNaN( thetaInRadians ) == false;

			//	        double x = m21 - m12;
			//	        double y = m02 - m20;
			//	        double z = m10 - m01;
			//	        double magnitudeSquared = x*x + y*y + z*z;
			//			double s = 0.5 * Math.sqrt( magnitudeSquared );
			//			this.angle = Math.atan2( s, c );

			this.axis.x = m21 - m12;
			this.axis.y = m02 - m20;
			this.axis.z = m10 - m01;

			//todo: remove?
			this.axis.normalize();
		}
		this.angle.setAsRadians( thetaInRadians );
		assert isNaN() == false;
	}

	@Deprecated
	public void setToOrientationComponentOf( Matrix4x4 m ) {
		if( m.isNaN() ) {
			setNaN();
		} else {
			set( m.right.x, m.up.x, m.backward.x, m.right.y, m.up.y, m.backward.y, m.right.z, m.up.z, m.backward.z );
		}
	}

	@Override
	public void setValue( OrthogonalMatrix3x3 m ) {
		if( m.isNaN() ) {
			setNaN();
		} else {
			//assert m.isWithinReasonableEpsilonOfUnitLengthSquared();
			set( m.right.x, m.up.x, m.backward.x, m.right.y, m.up.y, m.backward.y, m.right.z, m.up.z, m.backward.z );
		}
	}

	@Override
	public void setValue( AxisRotation other ) {
		this.axis.set( other.axis );
		this.angle.set( other.angle );
	}

	@Override
	public void setValue( UnitQuaternion q ) {
		//assert q.isNormalized();
		assert Math.abs( q.w ) <= 1;

		this.angle.setAsRadians( 2 * Math.acos( q.w ) );

		double s = Math.sqrt( 1 - ( q.w * q.w ) );

		final double THESHOLD = 0.0001;
		if( s < THESHOLD ) {
			this.axis.set( 1, 0, 0 );
		} else {
			this.axis.set( q.x / s, q.y / s, q.z / s );
		}
	}

	@Override
	public void setValue( EulerAngles ea ) {
		//todo: convert directly
		setValue( new OrthogonalMatrix3x3( ea ) );
	}

	@Override
	public void setValue( ForwardAndUpGuide faug ) {
		//todo: convert directly
		setValue( new OrthogonalMatrix3x3( faug ) );
	}

	public void set( Vector3 axis, Angle angle ) {
		this.axis.set( axis );
		this.angle.set( angle );
	}

	@Override
	public boolean equals( Object o ) {
		if( o == this ) {
			return true;
		} else {
			if( o instanceof AxisRotation ) {
				AxisRotation aa = (AxisRotation)o;
				return this.axis.equals( aa.axis ) && this.angle.equals( aa.angle );
			} else {
				return false;
			}
		}
	}

	//Random
	public static AxisRotation setReturnValueToRandom( AxisRotation rv ) {
		double magnitudeSquared;
		do {
			rv.axis.x = RandomUtilities.nextDoubleInRange( -1, 1 );
			rv.axis.y = RandomUtilities.nextDoubleInRange( -1, 1 );
			rv.axis.z = RandomUtilities.nextDoubleInRange( -1, 1 );
			magnitudeSquared = rv.axis.calculateMagnitudeSquared();
		} while( magnitudeSquared == 0.0 );
		rv.axis.divide( Math.sqrt( magnitudeSquared ) );
		rv.angle.setAsRadians( RandomUtilities.nextDoubleInRange( -Math.PI, Math.PI ) );
		return rv;
	}

	public static AxisRotation createRandom() {
		return setReturnValueToRandom( new AxisRotation() );
	}

	public void setRandom() {
		setReturnValueToRandom( this );
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append( AxisRotation.class.getName() );
		sb.append( "[axis=" );
		sb.append( this.axis );
		sb.append( ";angle=" );
		sb.append( this.angle );
		sb.append( "]" );
		return sb.toString();
	}
}
