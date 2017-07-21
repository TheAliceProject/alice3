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
public class EulerAngles implements Orientation {
	private enum CardinalRotation {
		PITCH,
		YAW,
		ROLL
	}

	public enum Order {
		PITCH_YAW_ROLL( CardinalRotation.PITCH, CardinalRotation.YAW, CardinalRotation.ROLL ),
		YAW_ROLL_PITCH( CardinalRotation.YAW, CardinalRotation.ROLL, CardinalRotation.PITCH ),
		ROLL_PITCH_YAW( CardinalRotation.ROLL, CardinalRotation.PITCH, CardinalRotation.YAW ),
		PITCH_ROLL_YAW( CardinalRotation.PITCH, CardinalRotation.ROLL, CardinalRotation.YAW ),
		YAW_PITCH_ROLL( CardinalRotation.YAW, CardinalRotation.PITCH, CardinalRotation.ROLL ),
		ROLL_YAW_PITCH( CardinalRotation.ROLL, CardinalRotation.YAW, CardinalRotation.PITCH ),
		NOT_APPLICABLE();
		private int m_pitchIndex;
		private int m_yawIndex;
		private int m_rollIndex;

		Order( CardinalRotation... angles ) {
			m_pitchIndex = -1;
			m_yawIndex = -1;
			m_rollIndex = -1;
			if( angles.length == 3 ) {
				int index = 0;
				for( CardinalRotation angle : angles ) {
					if( angle == CardinalRotation.PITCH ) {
						assert m_pitchIndex == -1;
						m_pitchIndex = index;
					} else if( angle == CardinalRotation.YAW ) {
						assert m_yawIndex == -1;
						m_yawIndex = index;
					} else if( angle == CardinalRotation.ROLL ) {
						assert m_rollIndex == -1;
						m_rollIndex = index;
					}
					index++;
				}
				assert m_pitchIndex != -1;
				assert m_yawIndex != -1;
				assert m_rollIndex != -1;
			} else {
				assert angles.length == 0;
			}
		}

		//		PITCH_YAW_ROLL( 0, 1, 2 ),
		//		YAW_ROLL_PITCH( 2, 0, 1 ),
		//		ROLL_PITCH_YAW( 1, 2, 0 ),
		//		PITCH_ROLL_YAW( 0, 2, 1 ),
		//		YAW_PITCH_ROLL( 1, 0, 2 ),
		//		ROLL_YAW_PITCH( 2, 1, 0 ),
		//		NOT_APPLICABLE( -1, -1, -1 );
		//		private int m_pitchIndex;
		//		private int m_yawIndex;
		//		private int m_rollIndex;
		//		Order( int pitchIndex, int yawIndex, int rollIndex ) {
		//			m_pitchIndex = pitchIndex;
		//			m_yawIndex = yawIndex;
		//			m_rollIndex = rollIndex;
		//		}

		private double getValue( int index, double a, double b, double c ) {
			switch( index ) {
			case 0:
				return a;
			case 1:
				return b;
			case 2:
				return c;
			default:
				throw new Error();
			}
		}

		private double getValue( int index, EulerNumbers numbers ) {
			return getValue( index, numbers.pitch, numbers.yaw, numbers.roll );
		}

		private Vector3 accessAxis( int index ) {
			if( index == m_pitchIndex ) {
				return Vector3.accessPositiveXAxis();
			} else if( index == m_yawIndex ) {
				return Vector3.accessPositiveYAxis();
			} else if( index == m_rollIndex ) {
				return Vector3.accessPositiveZAxis();
			} else {
				assert false;
				return null;
			}
		}

		public OrthogonalMatrix3x3 setReturnValueToPitchYawRoll( OrthogonalMatrix3x3 rv, Angle pitch, Angle yaw, Angle roll ) {
			//todo
			EulerNumbers numbers = new EulerNumbers();
			numbers.pitch = pitch.getAsRadians();
			numbers.yaw = yaw.getAsRadians();
			numbers.roll = roll.getAsRadians();
			rv.setIdentity();
			for( int i = 0; i < 3; i++ ) {
				rv.applyRotationAboutArbitraryAxis( accessAxis( i ), new AngleInRadians( getValue( i, numbers ) ) );
			}
			return rv;
		}

		public double getPitch( double a, double b, double c ) {
			return getValue( m_pitchIndex, a, b, c );
		}

		public double getYaw( double a, double b, double c ) {
			return getValue( m_yawIndex, a, b, c );
		}

		public double getRoll( double a, double b, double c ) {
			return getValue( m_rollIndex, a, b, c );
		}
	}

	public final Angle yaw = new AngleInRadians( Double.NaN );
	public final Angle pitch = new AngleInRadians( Double.NaN );
	public final Angle roll = new AngleInRadians( Double.NaN );
	public Order order = Order.NOT_APPLICABLE;

	public EulerAngles() {
	}

	public EulerAngles( OrthogonalMatrix3x3 other ) {
		setValue( other );
	}

	public EulerAngles( UnitQuaternion other ) {
		setValue( other );
	}

	public EulerAngles( AxisRotation other ) {
		setValue( other );
	}

	public EulerAngles( EulerAngles other ) {
		setValue( other );
	}

	public EulerAngles( ForwardAndUpGuide other ) {
		setValue( other );
	}

	public EulerAngles( Angle pitch, Angle yaw, Angle roll, Order order ) {
		setPitchYawRollOrder( pitch, yaw, roll, order );
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
		yaw.decode( binaryDecoder );
		pitch.decode( binaryDecoder );
		roll.decode( binaryDecoder );
		order = binaryDecoder.decodeEnum(/* Order.class */);
	}

	@Override
	public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		yaw.encode( binaryEncoder );
		pitch.encode( binaryEncoder );
		roll.encode( binaryEncoder );
		binaryEncoder.encode( order );
	}

	//NaN
	public static EulerAngles setReturnValueToNaN( EulerAngles rv ) {
		rv.yaw.setNaN();
		rv.pitch.setNaN();
		rv.roll.setNaN();
		rv.order = Order.NOT_APPLICABLE;
		return rv;
	}

	public static EulerAngles createNaN() {
		return setReturnValueToNaN( new EulerAngles() );
	}

	@Override
	public void setNaN() {
		setReturnValueToNaN( this );
	}

	@Override
	public boolean isNaN() {
		return this.yaw.isNaN() || this.pitch.isNaN() || this.roll.isNaN();
	}

	//Identity
	private static final EulerAngles IDENTITY = EulerAngles.createNaN();

	public static EulerAngles accessIdentity() {
		IDENTITY.setIdentity();
		return IDENTITY;
	}

	public static EulerAngles setReturnValueToIdentity( EulerAngles rv ) {
		rv.yaw.setAsRadians( 0.0 );
		rv.pitch.setAsRadians( 0.0 );
		rv.roll.setAsRadians( 0.0 );
		if( rv.order == Order.NOT_APPLICABLE ) {
			rv.order = Order.YAW_PITCH_ROLL;
		}
		return rv;
	}

	public static EulerAngles createIdentity() {
		return setReturnValueToIdentity( EulerAngles.createNaN() );
	}

	@Override
	public void setIdentity() {
		setReturnValueToIdentity( this );
	}

	@Override
	public boolean isIdentity() {
		return ( this.yaw.getAsRadians() == 0.0 ) && ( this.pitch.getAsRadians() == 0.0 ) && ( this.roll.getAsRadians() == 0.0 ) && ( this.order != Order.NOT_APPLICABLE );
	}

	public void setPitchYawRollOrder( Angle pitch, Angle yaw, Angle roll, Order order ) {
		this.pitch.set( pitch );
		this.yaw.set( yaw );
		this.roll.set( roll );
		this.order = order;
	}

	private void set( double m00, double m01, double m02, double m10, double m11, double m12, double m20, double m21, double m22 ) {
		m20 = Math.max( m20, -1 );
		m20 = Math.min( m20, 1 );

		this.yaw.setAsRadians( Math.asin( -m20 ) );

		this.pitch.setAsRadians( Math.atan2( m21, m22 ) );
		this.roll.setAsRadians( Math.atan2( m10, m00 ) );

		this.order = Order.YAW_PITCH_ROLL;

		assert isNaN() == false;
	}

	@Override
	public void setValue( OrthogonalMatrix3x3 m ) {
		assert m != null;
		assert m.isNaN() == false;
		set( m.right.x, m.up.x, m.backward.x, m.right.y, m.up.y, m.backward.y, m.right.z, m.up.z, m.backward.z );
	}

	@Override
	public void setValue( UnitQuaternion q ) {
		//todo: convert directly
		setValue( new OrthogonalMatrix3x3( q ) );
	}

	@Override
	public void setValue( AxisRotation aa ) {
		//todo: convert directly
		setValue( new OrthogonalMatrix3x3( aa ) );
	}

	@Override
	public void setValue( EulerAngles other ) {
		if( other != null ) {
			setPitchYawRollOrder( other.pitch, other.yaw, other.roll, other.order );
		} else {
			setNaN();
		}
	}

	@Override
	public void setValue( ForwardAndUpGuide faug ) {
		//todo: convert directly
		setValue( new OrthogonalMatrix3x3( faug ) );
	}

	public void setToOrientationComponentOf( Matrix4x4 m ) {
		assert m != null;
		assert m.isNaN() == false;
		set( m.right.x, m.up.x, m.backward.x, m.right.y, m.up.y, m.backward.y, m.right.z, m.up.z, m.backward.z );
	}

	public void interpolate( EulerAngles ea0, EulerAngles ea1, double portion ) {
		//todo: remove?
		assert ea0.order == ea1.order;

		//todo: convert to Quat4d?
		double yaw0 = ea0.yaw.getAsRadians();
		double pitch0 = ea0.pitch.getAsRadians();
		double roll0 = ea0.roll.getAsRadians();

		double yaw1 = ea1.yaw.getAsRadians();
		double pitch1 = ea1.pitch.getAsRadians();
		double roll1 = ea1.roll.getAsRadians();

		this.yaw.setAsRadians( yaw0 + ( ( yaw1 - yaw0 ) * portion ) );
		this.pitch.setAsRadians( pitch0 + ( ( pitch1 - pitch0 ) * portion ) );
		this.roll.setAsRadians( roll0 + ( ( roll1 - roll0 ) * portion ) );
	}

	@Override
	public String toString() {
		return EulerAngles.class.getName() + "[pitch=" + this.pitch + ",yaw=" + this.yaw + ",roll=" + this.roll + ",order=" + this.order + "]";
	}
	//todo
	//	public static EulerAnglesD valueOf( String s ) {
	//		String[] markers = { EulerAnglesD.class.getName()+"[pitch=", ",yaw=", ",roll=", ",order=", "]" };
	//		EulerAnglesD rv = new EulerAnglesD();		
	//		for( int i = 0; i < (markers.length-1); i++ ) {
	//			int begin = s.indexOf( markers[ i ] ) + markers[ i ].length();
	//			int end = s.indexOf( markers[ i + 1 ] );
	//			String v = s.substring( begin, end );
	//			switch( i ) {
	//			case 0:
	//				rv.this.pitch = Double.parseDouble( v );
	//				break;
	//			case 1:
	//				rv.this.yaw = Double.parseDouble( v );
	//				break;
	//			case 2:
	//				rv.this.roll = Double.parseDouble( v );
	//				break;
	//			case 3:
	//				rv.this.order = EulerOrder.valueOf( v );
	//				break;
	//			default:
	//				throw new Error();
	//			}
	//		}
	//		return rv;
	//	}

}
