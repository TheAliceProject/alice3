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
package edu.cmu.cs.dennisc.animation.rigidbody;

/**
 * @author Dennis Cosgrove
 */
public class RigidBodyAnimation implements edu.cmu.cs.dennisc.animation.Animation {
	class Function extends edu.cmu.cs.dennisc.math.rigidbody.TranslationAndOrientationFunction {
		private static final double TARGET_VELOCITY_MAGINITUDE = +2.0;

		private static final double FORCE_FOR_ACCELERATION = +4.0;
		private static final double FORCE_FOR_DECELERATION = -6.0;
		
		//todo: base on FORCE_FOR_DECELERATION
		private static final double THRESHOLD_AT_WHICH_TO_BEGIN_DECELERATING = 1.0;

		
		private double m_tRemainingEstimate = Double.POSITIVE_INFINITY;
		
		@Override
		protected edu.cmu.cs.dennisc.math.Vector3 getForce( edu.cmu.cs.dennisc.math.Vector3 rv, double t ) {
			edu.cmu.cs.dennisc.math.Point3 translation = accessTranslation();
			edu.cmu.cs.dennisc.math.Vector3 velocity = accessVelocity();
			
			double dx = m_translation1.x - translation.x;
			double dy = m_translation1.y - translation.y;
			double dz = m_translation1.z - translation.z;
			
			double velocityMagnitude = velocity.calculateMagnitude();
			
			double distanceSquared = dx*dx + dy*dy + dz*dz;
			double distance = Math.sqrt( distanceSquared );

			m_tRemainingEstimate = velocityMagnitude / distance;
			
			if( m_tRemainingEstimate < THRESHOLD_AT_WHICH_TO_BEGIN_DECELERATING ) {
				rv.x = FORCE_FOR_DECELERATION * dx / distance; 
				rv.y = FORCE_FOR_DECELERATION * dy / distance; 
				rv.z = FORCE_FOR_DECELERATION * dz / distance; 
			} else {
				if( velocityMagnitude < TARGET_VELOCITY_MAGINITUDE ) {
					rv.x = FORCE_FOR_ACCELERATION * dx / distance; 
					rv.y = FORCE_FOR_ACCELERATION * dy / distance; 
					rv.z = FORCE_FOR_ACCELERATION * dz / distance; 
				} else {
					rv.set( 0,0,0 );
				}
			}
			
			return rv;
		}
		@Override
		protected edu.cmu.cs.dennisc.math.Vector3 getTorque( edu.cmu.cs.dennisc.math.Vector3 rv, double t ) {
			return rv;
		}

		public void initialize( edu.cmu.cs.dennisc.math.AffineMatrix4x4 m ) {
			setTransformation( m );
			setMomentum( 0, 0, 0 );
			setVelocity( 0, 0, 0 );
			setAngularMomentum( 0, 0, 0 );
			setSpin( 0, 0, 0, 0 );
			setAngularVelocity( 0, 0, 0 );
		}
		
		public double getEstimatedTimeRemaining() {
			//return m_tRemainingEstimate;
			return 1.0;
		}
	}

	private Function m_function = new Function();

	private edu.cmu.cs.dennisc.scenegraph.Transformable m_sgTransformable;
	private edu.cmu.cs.dennisc.math.Point3 m_translation1 = edu.cmu.cs.dennisc.math.Point3.createNaN();
	private edu.cmu.cs.dennisc.math.UnitQuaternion m_orientation1 = edu.cmu.cs.dennisc.math.UnitQuaternion.createNaN();

	public RigidBodyAnimation( edu.cmu.cs.dennisc.scenegraph.Transformable sgTransformable, edu.cmu.cs.dennisc.math.AffineMatrix4x4 m1 ) {
		m_sgTransformable = sgTransformable;
		m_translation1.set( m1.translation );
		m_orientation1.setValue( m1.orientation );
	}
	public void initialize() {
		m_function.initialize( m_sgTransformable.getLocalTransformation() );
	}

	private double m_tPrev = Double.NaN;

	public double update( double tCurrent, edu.cmu.cs.dennisc.animation.AnimationObserver animationObserver ) {
		if( Double.isNaN( m_tPrev ) ) {
			//pass
		} else {
			double tDelta = m_tPrev - tCurrent;
			edu.cmu.cs.dennisc.math.rungekutta.RungeKuttaUtilities.rk4( m_function, m_tPrev, tDelta );
			m_sgTransformable.setLocalTransformation( m_function.getTransformation() );
		}
		m_tPrev = tCurrent;
		return m_function.getEstimatedTimeRemaining();
	}
	public void complete( edu.cmu.cs.dennisc.animation.AnimationObserver animationObserver ) {
		m_sgTransformable.setLocalTransformation( new edu.cmu.cs.dennisc.math.AffineMatrix4x4( m_orientation1, m_translation1 ) );
	}
}
