/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
 */
package edu.cmu.cs.dennisc.ui.lookingglass;

/**
 * @author Dennis Cosgrove
 */
class CameraNavigationDerivative extends edu.cmu.cs.dennisc.math.rigidbody.TranslationDerivative {
}

/**
 * @author Dennis Cosgrove
 */
public class CameraNavigationFunction extends edu.cmu.cs.dennisc.math.rigidbody.TranslationFunction<CameraNavigationDerivative> {
	private static final double DISTANCE_MINIMUM = 4.0;
	private static final double DISTANCE_MAXIMUM = 100.0;

	private static final double FORCE_FOR_ACCELERATION = +4.0;
	private static final double FORCE_FOR_DECELERATION = -8.0;

	private edu.cmu.cs.dennisc.math.Vector3 m_velocityRequested = new edu.cmu.cs.dennisc.math.Vector3();

	//private double m_distance = Double.NaN;
	private double m_distanceRequested = 16.0;

	//private double m_yaw = Double.NaN;
	private double m_yawRequested = Math.PI;

	//private double m_pitch = Double.NaN;
	private double m_pitchRequested = 0.0;

	private boolean m_isForwardKeyPressed = false;
	private boolean m_isBackwardKeyPressed = false;
	private boolean m_isLeftKeyPressed = false;
	private boolean m_isRightKeyPressed = false;

	public void setKeyPressed( int keyCode, boolean isKeyPressed ) {
		switch( keyCode ) {
		case java.awt.event.KeyEvent.VK_UP:
			m_isForwardKeyPressed = isKeyPressed;
			break;
		case java.awt.event.KeyEvent.VK_DOWN:
			m_isBackwardKeyPressed = isKeyPressed;
			break;
		case java.awt.event.KeyEvent.VK_LEFT:
			m_isLeftKeyPressed = isKeyPressed;
			break;
		case java.awt.event.KeyEvent.VK_RIGHT:
			m_isRightKeyPressed = isKeyPressed;
			break;
		}
	}

	@Override
	protected CameraNavigationDerivative newDerivative() {
		return new CameraNavigationDerivative();
	}

	public void requestVelocity( edu.cmu.cs.dennisc.math.Vector3 velocityRequested ) {
		m_velocityRequested.set( velocityRequested );
	}

	public void requestVelocity( double x, double y, double z ) {
		m_velocityRequested.set( x, y, z );
	}

	public void stopImmediately() {
		requestVelocity( 0, 0, 0 );
		setVelocity( 0, 0, 0 );
	}

	public void requestDistance( double distance ) {
		m_distanceRequested = Math.max( Math.min( distance, DISTANCE_MAXIMUM ), DISTANCE_MINIMUM );
	}

	public void requestYaw( edu.cmu.cs.dennisc.math.Angle yaw ) {
		m_yawRequested = yaw.getAsRadians();
	}

	public void requestDistanceChange( double delta ) {
		requestDistance( m_distanceRequested + delta );
	}

	public void requestTarget( double x, double y, double z ) {
		edu.cmu.cs.dennisc.math.Point3 translation = accessTranslation();
		translation.set( x, y, z );
	}

	public void requestOrbit( double yawDelta, double pitchDelta ) {
		m_yawRequested += yawDelta;
		m_pitchRequested += pitchDelta;
	}

	public edu.cmu.cs.dennisc.math.Angle getYawRequested() {
		return new edu.cmu.cs.dennisc.math.AngleInRadians( m_yawRequested );
	}

	public edu.cmu.cs.dennisc.math.Angle getPitchRequested() {
		return new edu.cmu.cs.dennisc.math.AngleInRadians( m_pitchRequested );
	}

	public double getDistanceRequested() {
		return m_distanceRequested;
	}

	public edu.cmu.cs.dennisc.math.Point3 accessTargetRequested() {
		return accessTranslation();
	}

	public edu.cmu.cs.dennisc.math.Point3 getTargetRequested( edu.cmu.cs.dennisc.math.Point3 rv ) {
		rv.set( accessTargetRequested() );
		return rv;
	}

	public edu.cmu.cs.dennisc.math.Point3 getTargetRequested() {
		return getTargetRequested( new edu.cmu.cs.dennisc.math.Point3() );
	}

	private static double getHeight( double distance ) {
		double d = distance * 0.1;
		return d * d;
	}

	private static double getPitchMinimum( double height, double distance ) {
		return Math.atan2( height, distance );
	}

	@Override
	protected edu.cmu.cs.dennisc.math.Vector3 getForce( edu.cmu.cs.dennisc.math.Vector3 rv, double t ) {
		final double LENGTH_SQUARED_THRESHOLD = 0.25;
		edu.cmu.cs.dennisc.math.Vector3 velocity = accessVelocity();
		if( m_velocityRequested.isZero() && ( velocity.calculateMagnitudeSquared() < LENGTH_SQUARED_THRESHOLD ) ) {
			setMomentum( 0, 0, 0 );
			rv.set( 0, 0, 0 );
		} else {
			if( m_velocityRequested.x > 0 ) {
				if( m_velocityRequested.x > velocity.x ) {
					rv.x = +FORCE_FOR_ACCELERATION;
				} else {
					rv.x = +FORCE_FOR_DECELERATION;
				}
			} else {
				if( m_velocityRequested.x > velocity.x ) {
					rv.x = -FORCE_FOR_DECELERATION;
				} else {
					rv.x = -FORCE_FOR_ACCELERATION;
				}
			}
			if( m_velocityRequested.y > 0 ) {
				if( m_velocityRequested.y > velocity.y ) {
					rv.y = +FORCE_FOR_ACCELERATION;
				} else {
					rv.y = +FORCE_FOR_DECELERATION;
				}
			} else {
				if( m_velocityRequested.y > velocity.y ) {
					rv.y = -FORCE_FOR_DECELERATION;
				} else {
					rv.y = -FORCE_FOR_ACCELERATION;
				}
			}
			if( m_velocityRequested.z > 0 ) {
				if( m_velocityRequested.z > velocity.z ) {
					rv.z = +FORCE_FOR_ACCELERATION;
				} else {
					rv.z = +FORCE_FOR_DECELERATION;
				}
			} else {
				if( m_velocityRequested.z > velocity.z ) {
					rv.z = -FORCE_FOR_DECELERATION;
				} else {
					rv.z = -FORCE_FOR_ACCELERATION;
				}
			}
		}
		return rv;
	}

	@Override
	protected CameraNavigationDerivative evaluate( CameraNavigationDerivative rv, double t ) {
		return super.evaluate( rv, t );
	}

	@Override
	protected CameraNavigationDerivative evaluate( CameraNavigationDerivative rv, double t, double dt, CameraNavigationDerivative derivative ) {
		return super.evaluate( rv, t, dt, derivative );
	}

	@Override
	public void update( CameraNavigationDerivative a, CameraNavigationDerivative b, CameraNavigationDerivative c, CameraNavigationDerivative d, double dt ) {
		super.update( a, b, c, d, dt );

		double delta = 2.0 * dt;

		edu.cmu.cs.dennisc.math.Point3 translation = accessTranslation();
		translation.y = Math.max( translation.y, 0 );
		if( m_isForwardKeyPressed ) {
			translation.z -= delta;
		}
		if( m_isBackwardKeyPressed ) {
			translation.z += delta;
		}
		if( m_isLeftKeyPressed ) {
			translation.x -= delta;
		}
		if( m_isRightKeyPressed ) {
			translation.x += delta;
		}
		//edu.cmu.cs.dennisc.print.PrintUtilities.println( "update:", a );
	}

	public edu.cmu.cs.dennisc.math.AffineMatrix4x4 getTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4 rv ) {
		//		m_distance = m_distanceRequested;
		//		double height = getHeight( m_distance );
		//		m_pitch = Math.max( m_pitchRequested, getPitchMinimum( height, m_distance ) );
		//		m_yaw = m_yawRequested;
		//
		//		rv.setIdentity();
		//		LinearAlgebra.applyRotationAboutYAxis( rv, m_yaw, UnitOfAngle.RADIANS );
		//		LinearAlgebra.applyTranslation( rv, 0, height, m_distance );
		//		LinearAlgebra.applyTranslation( rv, accessTranslation() );
		//		LinearAlgebra.applyRotationAboutXAxis( rv, -m_pitch, UnitOfAngle.RADIANS );
		CameraNavigationFunction.getTransformation( rv, m_yawRequested, m_pitchRequested, m_distanceRequested, accessTranslation() );
		return rv;
	}

	public edu.cmu.cs.dennisc.math.AffineMatrix4x4 getTransformation() {
		return getTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4.createNaN() );
	}

	public static edu.cmu.cs.dennisc.math.AffineMatrix4x4 getTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4 rv, double yaw, double pitch, double distance, double xTranslation, double yTranslation, double zTranslation ) {
		double height = getHeight( distance );
		pitch = Math.max( pitch, getPitchMinimum( height, distance ) );

		rv.setIdentity();
		rv.applyRotationAboutYAxis( new edu.cmu.cs.dennisc.math.AngleInRadians( yaw ) );
		rv.applyTranslation( 0, height, distance );
		rv.applyTranslation( xTranslation, yTranslation, zTranslation );
		rv.applyRotationAboutXAxis( new edu.cmu.cs.dennisc.math.AngleInRadians( -pitch ) );
		return rv;
	}

	public static edu.cmu.cs.dennisc.math.AffineMatrix4x4 getTransformation( double yaw, double pitch, double distance, double xTranslation, double yTranslation, double zTranslation ) {
		return getTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4.createNaN(), yaw, pitch, distance, xTranslation, yTranslation, zTranslation );
	}

	public static edu.cmu.cs.dennisc.math.AffineMatrix4x4 getTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4 rv, double yaw, double pitch, double distance, edu.cmu.cs.dennisc.math.Point3 translation ) {
		return getTransformation( rv, yaw, pitch, distance, translation.x, translation.y, translation.z );
	}

	public static edu.cmu.cs.dennisc.math.AffineMatrix4x4 getTransformation( double yaw, double pitch, double distance, edu.cmu.cs.dennisc.math.Point3 translation ) {
		return getTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4.createNaN(), yaw, pitch, distance, translation );
	}

}
