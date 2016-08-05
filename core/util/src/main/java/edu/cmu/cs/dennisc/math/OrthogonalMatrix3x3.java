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
public final class OrthogonalMatrix3x3 extends AbstractMatrix3x3 implements Orientation {
	private static final OrthogonalMatrix3x3 IDENTITY = new OrthogonalMatrix3x3();

	public static OrthogonalMatrix3x3 accessIdentity() {
		IDENTITY.setIdentity();
		return IDENTITY;
	}

	//todo: reduce visibility to private
	public OrthogonalMatrix3x3() {
		//todo: setNaN();
		setIdentity();
	}

	public OrthogonalMatrix3x3( OrthogonalMatrix3x3 other ) {
		setValue( other );
	}

	public OrthogonalMatrix3x3( UnitQuaternion other ) {
		setValue( other );
	}

	public OrthogonalMatrix3x3( AxisRotation other ) {
		setValue( other );
	}

	public OrthogonalMatrix3x3( EulerAngles other ) {
		setValue( other );
	}

	public OrthogonalMatrix3x3( ForwardAndUpGuide other ) {
		setValue( other );
	}

	public OrthogonalMatrix3x3( Vector3 right, Vector3 up, Vector3 backward ) {
		set( right, up, backward );
	}

	public OrthogonalMatrix3x3( edu.cmu.cs.dennisc.math.immutable.MOrthogonalMatrix3x3 other ) {
		set( new Vector3( other.right ), new Vector3( other.up ), new Vector3( other.backward ) );
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

	@Override
	public void setValue( OrthogonalMatrix3x3 other ) {
		super.setValue( other );
	}

	@Override
	public void setValue( UnitQuaternion q ) {
		//todo: assert unit

		//double ww = q.w * q.w;
		double wx = q.w * q.x;
		double wy = q.w * q.y;
		double wz = q.w * q.z;

		double xx = q.x * q.x;
		double xy = q.x * q.y;
		double xz = q.x * q.z;

		double yy = q.y * q.y;
		double yz = q.y * q.z;

		double zz = q.z * q.z;

		right.x = 1 - ( 2 * ( yy + zz ) );
		up.x = 2 * ( xy - wz );
		backward.x = 2 * ( xz + wy );
		right.y = 2 * ( xy + wz );
		up.y = 1 - ( 2 * ( xx + zz ) );
		backward.y = 2 * ( yz - wx );
		right.z = 2 * ( xz - wy );
		up.z = 2 * ( yz + wx );
		backward.z = 1 - ( 2 * ( xx + yy ) );
	}

	@Override
	public void setValue( AxisRotation aa ) {

		//todo: optimize for special axes

		double thetaInRadians = aa.angle.getAsRadians();
		double c = Math.cos( thetaInRadians );
		double s = Math.sin( thetaInRadians );
		double t = 1 - c;

		right.x = c + ( aa.axis.x * aa.axis.x * t );
		up.y = c + ( aa.axis.y * aa.axis.y * t );
		backward.z = c + ( aa.axis.z * aa.axis.z * t );

		double xyt = aa.axis.x * aa.axis.y * t;
		double zs = aa.axis.z * s;
		right.y = xyt + zs;
		up.x = xyt - zs;

		double xzt = aa.axis.x * aa.axis.z * t;
		double ys = aa.axis.y * s;
		right.z = xzt - ys;
		backward.x = xzt + ys;

		double yzt = aa.axis.y * aa.axis.z * t;
		double xs = aa.axis.x * s;
		up.z = yzt + xs;
		backward.y = yzt - xs;
	}

	@Override
	public void setValue( EulerAngles ea ) {
		assert ea.order != null;
		if( ea.order == EulerAngles.Order.YAW_PITCH_ROLL ) {
			double theta = ea.yaw.getAsRadians();
			double phi = ea.pitch.getAsRadians();
			double psi = ea.roll.getAsRadians();
			double cosTheta = Math.cos( theta );
			double sinTheta = Math.sin( theta );
			double cosPhi = Math.cos( phi );
			double sinPhi = Math.sin( phi );
			double cosPsi = Math.cos( psi );
			double sinPsi = Math.sin( psi );

			right.x = cosPsi * cosTheta;
			up.x = ( cosPsi * sinTheta * sinPhi ) - ( sinPsi * cosPhi );
			backward.x = ( cosPsi * sinTheta * cosPhi ) + ( sinPsi * sinPhi );
			right.y = sinPsi * cosTheta;
			up.y = ( sinPsi * sinTheta * sinPhi ) + ( cosPsi * cosPhi );
			backward.y = ( sinPsi * sinTheta * cosPhi ) - ( cosPsi * sinPhi );
			right.z = -sinTheta;
			up.z = cosTheta * sinPhi;
			backward.z = cosTheta * cosPhi;
		} else {
			ea.order.setReturnValueToPitchYawRoll( this, ea.pitch, ea.yaw, ea.roll );
		}
	}

	@Override
	public void setValue( edu.cmu.cs.dennisc.math.ForwardAndUpGuide faug ) {
		assert faug.forward.isNaN() == false;

		if( faug.forward.calculateMagnitudeSquared() == 0 ) {
			setNaN();
		} else {
			Vector3 upGuide;
			if( faug.upGuide.isNaN() == false ) {
				upGuide = faug.upGuide;
			} else {
				if( ( faug.forward.x == 0 ) && ( faug.forward.z == 0 ) ) {
					upGuide = Vector3.accessPositiveXAxis();
				} else {
					upGuide = Vector3.accessPositiveYAxis();
				}
			}

			assert upGuide.isNaN() == false;

			Vector3 xAxis = new Vector3();
			Vector3 yAxis = new Vector3();
			Vector3 zAxis = new Vector3();

			zAxis.set( faug.forward );
			zAxis.negate();
			zAxis.normalize();

			yAxis.set( upGuide );
			yAxis.normalize();

			Vector3.setReturnValueToCrossProduct( xAxis, yAxis, zAxis );
			Vector3.setReturnValueToCrossProduct( yAxis, zAxis, xAxis );

			xAxis.normalize();
			yAxis.normalize();

			this.right.set( xAxis );
			this.up.set( yAxis );
			this.backward.set( zAxis );

			assert isWithinReasonableEpsilonOfUnitLengthSquared();

			assert isNaN() == false;
		}
	}

	public static OrthogonalMatrix3x3 createNaN() {
		return (OrthogonalMatrix3x3)setReturnValueToNaN( new OrthogonalMatrix3x3() );
	}

	public static OrthogonalMatrix3x3 createIdentity() {
		return (OrthogonalMatrix3x3)setReturnValueToIdentity( OrthogonalMatrix3x3.createNaN() );
	}

	public static OrthogonalMatrix3x3 createFromRotationAboutXAxis( Angle angle ) {
		OrthogonalMatrix3x3 rv = new OrthogonalMatrix3x3();
		rv.setToRotationAboutXAxis( angle );
		return rv;
	}

	public static OrthogonalMatrix3x3 createFromRotationAboutYAxis( Angle angle ) {
		OrthogonalMatrix3x3 rv = new OrthogonalMatrix3x3();
		rv.setToRotationAboutYAxis( angle );
		return rv;
	}

	public static OrthogonalMatrix3x3 createFromRotationAboutZAxis( Angle angle ) {
		OrthogonalMatrix3x3 rv = new OrthogonalMatrix3x3();
		rv.setToRotationAboutZAxis( angle );
		return rv;
	}

	public static OrthogonalMatrix3x3 createFromForwardAndUpGuide( Vector3 forward, Vector3 upGuide ) {
		return new OrthogonalMatrix3x3( new ForwardAndUpGuide( forward, upGuide ) );
	}

	public static OrthogonalMatrix3x3 setReturnValueToRotationAboutArbitraryAxis( OrthogonalMatrix3x3 rv, Vector3 axis, Angle angle ) {
		double angleInRadians = angle.getAsRadians();
		double c = Math.cos( angleInRadians );
		double s = Math.sin( angleInRadians );
		double t = 1 - c;

		double w = axis.calculateMagnitude();

		assert w > 0;

		double x = axis.x / w;
		double y = axis.y / w;
		double z = axis.z / w;

		rv.right.set( ( t * x * x ) + c, ( t * x * y ) + ( s * z ), ( t * x * z ) - ( s * y ) );
		rv.up.set( ( t * x * y ) - ( s * z ), ( t * y * y ) + c, ( t * y * z ) + ( s * x ) );
		rv.backward.set( ( t * x * z ) + ( s * y ), ( t * y * z ) - ( s * x ), ( t * z * z ) + c );

		return rv;
	}

	public void setToRotationAboutArbitraryAxis( Vector3 axis, Angle angle ) {
		setReturnValueToRotationAboutArbitraryAxis( this, axis, angle );
	}

	public static OrthogonalMatrix3x3 createRotationAboutArbitraryAxis( Vector3 axis, Angle angle ) {
		return setReturnValueToRotationAboutArbitraryAxis( new OrthogonalMatrix3x3(), axis, angle );
	}

	public static OrthogonalMatrix3x3 setReturnValueToNormalizedColumns( OrthogonalMatrix3x3 rv, AbstractMatrix3x3 m ) {
		if( rv != m ) {
			rv.right.set( m.right );
			rv.up.set( m.right );
			rv.backward.set( m.right );
		}
		rv.right.normalize();
		rv.up.normalize();
		rv.backward.normalize();
		return rv;
	}

	public static OrthogonalMatrix3x3 createNormalizedColumns( AbstractMatrix3x3 m ) {
		return setReturnValueToNormalizedColumns( new OrthogonalMatrix3x3(), m );
	}

	public void setToNormalizedColumns( AbstractMatrix3x3 m ) {
		setReturnValueToNormalizedColumns( this, m );
	}

	public void normalizeColumns() {
		setToNormalizedColumns( this );
	}

	//	public static OrthogonalMatrix3x3 setReturnValueToStandUp( OrthogonalMatrix3x3 rv, OrthogonalMatrix3x3 m ) {
	//		Vector3 zAxis = new Vector3( m.backward );
	//		if( EpsilonUtilities.isWithinReasonableEpsilon( zAxis.y, 0 ) ) {
	//			rv.setValue( m );
	//			//edu.cmu.cs.dennisc.print.PrintUtilities.println( "do nothing" );
	//		} else {
	//			if( EpsilonUtilities.isWithinReasonableEpsilon( zAxis.x, 0 ) && EpsilonUtilities.isWithinReasonableEpsilon( zAxis.z, 0 ) ) {
	//				rv.setValue( m );
	//				rv.applyRotationAboutXAxis( new AngleInRevolutions( 0.25 ) );
	//				//edu.cmu.cs.dennisc.print.PrintUtilities.println( "rotate about x" );
	//			} else {
	//				zAxis.y = 0;
	//				zAxis.normalize();
	//
	//				Vector3 yAxis = Vector3.accessPositiveYAxis();
	//				Vector3 xAxis = Vector3.createCrossProduct( yAxis, zAxis );
	//				//xAxis.normalize();
	//
	//				rv.right.set( xAxis );
	//				rv.up.set( yAxis );
	//				rv.backward.set( zAxis );
	//			}
	//		}
	//		return rv;
	//	}
	public static OrthogonalMatrix3x3 setReturnValueToStandUp( OrthogonalMatrix3x3 rv, OrthogonalMatrix3x3 m ) {
		Vector3 zAxis = new Vector3( m.backward );
		if( EpsilonUtilities.isWithinReasonableEpsilon( m.up.y, 1.0 ) ) {
			rv.setValue( m );
		} else {
			if( EpsilonUtilities.isWithinReasonableEpsilon( zAxis.x, 0 ) && EpsilonUtilities.isWithinReasonableEpsilon( zAxis.z, 0 ) ) {
				rv.setValue( m );
				double theta;
				if( zAxis.y < 0.0 ) {
					theta = -0.25;
				} else {
					theta = +0.25;
				}
				rv.applyRotationAboutXAxis( new AngleInRevolutions( theta ) );
				//                edu.cmu.cs.dennisc.print.PrintUtilities.println( "rotate about x" );
			} else {
				if( EpsilonUtilities.isWithinReasonableEpsilon( zAxis.y, 0.0 ) ) {
					//pass
				} else {
					zAxis.y = 0;
					zAxis.normalize();
				}

				Vector3 yAxis = Vector3.accessPositiveYAxis();
				Vector3 xAxis = Vector3.createCrossProduct( yAxis, zAxis );
				//xAxis.normalize();

				rv.right.set( xAxis );
				rv.up.set( yAxis );
				rv.backward.set( zAxis );
			}
		}
		return rv;
	}

	public static OrthogonalMatrix3x3 createFromStandUp( OrthogonalMatrix3x3 m ) {
		return setReturnValueToStandUp( new OrthogonalMatrix3x3(), m );
	}

	//	public void setToOrientationComponentOf( Matrix4x4 m ) {
	//		this.right.x = m.right.x;
	//		this.up.x = m.up.x;
	//		this.backward.x = m.backward.x;
	//		this.right.y = m.right.y;
	//		this.up.y = m.up.y;
	//		this.backward.y = m.backward.y;
	//		this.right.z = m.right.z;
	//		this.up.z = m.up.z;
	//		this.backward.z = m.backward.z;
	//	}

	public void setToMultiplication( OrthogonalMatrix3x3 a, OrthogonalMatrix3x3 b ) {
		super.setToMultiplication( a, b );
	}

	public void applyMultiplication( OrthogonalMatrix3x3 b ) {
		super.applyMultiplication( b );
	}

	//todo
	public void applyRotationAboutXAxis( Angle theta ) {
		OrthogonalMatrix3x3 other = new OrthogonalMatrix3x3();
		other.setToRotationAboutXAxis( theta );
		applyMultiplication( other );
	}

	//todo
	public void applyRotationAboutYAxis( Angle theta ) {
		OrthogonalMatrix3x3 other = new OrthogonalMatrix3x3();
		other.setToRotationAboutYAxis( theta );
		applyMultiplication( other );
	}

	//todo
	public void applyRotationAboutZAxis( Angle theta ) {
		OrthogonalMatrix3x3 other = new OrthogonalMatrix3x3();
		other.setToRotationAboutZAxis( theta );
		applyMultiplication( other );
	}

	//todo
	public void applyRotationAboutArbitraryAxis( Vector3 axis, Angle theta ) {
		OrthogonalMatrix3x3 other = new OrthogonalMatrix3x3();
		other.setToRotationAboutArbitraryAxis( axis, theta );
		applyMultiplication( other );
	}

	public void setToRotationAboutXAxis( Angle theta ) {
		double thetaInRadians = theta.getAsRadians();
		double s = Math.sin( thetaInRadians );
		double c = Math.cos( thetaInRadians );
		right.x = 1;
		up.x = 0;
		backward.x = 0;
		right.y = 0;
		up.y = +c;
		backward.y = -s;
		right.z = 0;
		up.z = +s;
		backward.z = +c;
	}

	public void setToRotationAboutYAxis( Angle theta ) {
		double thetaInRadians = theta.getAsRadians();
		double s = Math.sin( thetaInRadians );
		double c = Math.cos( thetaInRadians );
		right.x = +c;
		up.x = 0;
		backward.x = +s;
		right.y = 0;
		up.y = 1;
		backward.y = 0;
		right.z = -s;
		up.z = 0;
		backward.z = +c;
	}

	public void setToRotationAboutZAxis( Angle theta ) {
		double thetaInRadians = theta.getAsRadians();
		double s = Math.sin( thetaInRadians );
		double c = Math.cos( thetaInRadians );
		right.x = +c;
		up.x = -s;
		backward.x = 0;
		right.y = +s;
		up.y = +c;
		backward.y = 0;
		right.z = 0;
		up.z = 0;
		backward.z = 1;
	}

	public edu.cmu.cs.dennisc.math.immutable.MOrthogonalMatrix3x3 createImmutable() {
		return new edu.cmu.cs.dennisc.math.immutable.MOrthogonalMatrix3x3( this.right.createImmutable(), this.up.createImmutable(), this.backward.createImmutable() );
	}
}
