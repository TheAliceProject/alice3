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
package edu.cmu.cs.dennisc.math.rigidbody;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.UnitQuaternion;
import edu.cmu.cs.dennisc.math.Vector3;

/**
 * @author Dennis Cosgrove
 */
public abstract class TranslationAndOrientationFunction extends TranslationFunction<TranslationAndOrientationDerivative> {
	private UnitQuaternion m_orientation = UnitQuaternion.createNaN();
	private Vector3 m_angularMomentum = new Vector3();

	private UnitQuaternion m_spin = UnitQuaternion.createNaN();
	private Vector3 m_angularVelocity = new Vector3();

	//todo: use Matrix
	private double m_inertiaTensor = 1 / 6.0;
	private double m_inverseInertiaTensor = 6.0;

	//	@Override
	//	protected Object clone() throws CloneNotSupportedException {
	//		TranslationAndOrientationFunction pof = (TranslationAndOrientationFunction)super.clone();
	//		pof.m_orientation = new edu.cmu.cs.dennisc.math.UnitQuaternion( m_orientation );
	//		pof.m_angularMomentum = new edu.cmu.cs.dennisc.math.Vector3( m_angularMomentum );
	//		pof.m_spin = new edu.cmu.cs.dennisc.math.UnitQuaternion( m_spin );
	//		pof.m_angularVelocity = new edu.cmu.cs.dennisc.math.Vector3( m_angularVelocity );
	//		return pof;
	//	}

	public UnitQuaternion accessOrientation() {
		return m_orientation;
	}

	public UnitQuaternion getOrientation( UnitQuaternion rv ) {
		rv.setValue( m_orientation );
		return rv;
	}

	public UnitQuaternion getOrientation() {
		return getOrientation( UnitQuaternion.createNaN() );
	}

	public void setOrientation( UnitQuaternion orientation ) {
		m_orientation.setValue( orientation );
	}

	public void setOrientation( double x, double y, double z, double w ) {
		m_orientation.set( x, y, z, w );
	}

	public Vector3 accessAngularMomentum() {
		return m_angularMomentum;
	}

	public Vector3 getAngularMomentum( Vector3 rv ) {
		rv.set( m_angularMomentum );
		return rv;
	}

	public Vector3 getAngularMomentum() {
		return getAngularMomentum( new Vector3() );
	}

	public void setAngularMomentum( Vector3 angularMomentum ) {
		m_angularMomentum.set( angularMomentum );
	}

	public void setAngularMomentum( double x, double y, double z ) {
		m_angularMomentum.set( x, y, z );
	}

	public UnitQuaternion accessSpin() {
		return m_spin;
	}

	public UnitQuaternion getSpin( UnitQuaternion rv ) {
		rv.setValue( m_spin );
		return rv;
	}

	public UnitQuaternion getSpin() {
		return getSpin( UnitQuaternion.createNaN() );
	}

	public void setSpin( UnitQuaternion spin ) {
		m_spin.setValue( spin );
	}

	public void setSpin( double w, double x, double y, double z ) {
		m_spin.set( w, x, y, z );
	}

	public Vector3 accessAngularVelocity() {
		return m_angularVelocity;
	}

	public Vector3 getAngularVelocity( Vector3 rv ) {
		rv.set( m_angularVelocity );
		return rv;
	}

	public Vector3 getAngularVelocity() {
		return getAngularVelocity( new Vector3() );
	}

	public void setAngularVelocity( Vector3 angularVelocity ) {
		m_angularVelocity.set( angularVelocity );
	}

	public void setAngularVelocity( double x, double y, double z ) {
		m_angularVelocity.set( x, y, z );
	}

	public double getInertiaTensor() {
		return m_inertiaTensor;
	}

	public void setInertiaTensor( double inertiaTensor ) {
		m_inertiaTensor = inertiaTensor;
		m_inverseInertiaTensor = 1 / m_inertiaTensor;
	}

	public AffineMatrix4x4 getTransformation( AffineMatrix4x4 rv ) {
		rv.set( m_orientation, accessTranslation() );
		return rv;
	}

	public AffineMatrix4x4 getTransformation() {
		return getTransformation( AffineMatrix4x4.createNaN() );
	}

	public void setTransformation( AffineMatrix4x4 transformation ) {
		accessTranslation().set( transformation.translation );
		m_orientation.setValue( transformation.orientation );
	}

	protected abstract Vector3 getTorque( Vector3 rv, double t );

	@Override
	protected TranslationAndOrientationDerivative newDerivative() {
		return new TranslationAndOrientationDerivative();
	}

	@Override
	protected TranslationAndOrientationDerivative evaluate( TranslationAndOrientationDerivative rv, double t ) {
		rv.spin.setValue( m_spin );
		getTorque( rv.torque, t );
		return super.evaluate( rv, t );
	}

	@Override
	protected void update( double t, double dt, TranslationAndOrientationDerivative derivative ) {
		super.update( t, dt, derivative );
		m_orientation.add( UnitQuaternion.createMultiplication( derivative.spin, dt ) );
		m_orientation.normalize();
		m_angularMomentum.add( Vector3.createMultiplication( derivative.torque, dt ) );
	}

	@Override
	protected TranslationAndOrientationDerivative evaluate( TranslationAndOrientationDerivative rv, double t, double dt, TranslationAndOrientationDerivative positionDerivative ) {
		return super.evaluate( rv, t, dt, positionDerivative );
	}

	@Override
	public void update( TranslationAndOrientationDerivative a, TranslationAndOrientationDerivative b, TranslationAndOrientationDerivative c, TranslationAndOrientationDerivative d, double dt ) {
		super.update( a, b, c, d, dt );
		m_orientation.add(
				UnitQuaternion.createMultiplication(
						UnitQuaternion.createAddition(
								a.spin,
								UnitQuaternion.createAddition(
										UnitQuaternion.createMultiplication(
												UnitQuaternion.createAddition(
														b.spin,
														c.spin
														),
												2.0
												),
										d.spin
										)
								),
						dt / 6
						)
				);
		m_orientation.normalize();
		m_angularMomentum.add(
				Vector3.createMultiplication(
						Vector3.createAddition(
								a.torque,
								Vector3.createAddition(
										Vector3.createMultiplication(
												Vector3.createAddition(
														b.torque,
														c.torque
														),
												2.0
												),
										d.torque
										)
								),
						dt / 6
						)
				);
	}

	@Override
	public void update() {
		super.update();
		m_angularVelocity.setToMultiplication( m_angularMomentum, m_inverseInertiaTensor );
		m_spin.setToMultiplication(
				UnitQuaternion.createMultiplication(
						new UnitQuaternion( 0, m_angularVelocity.x, m_angularVelocity.y, m_angularVelocity.z ),
						m_orientation
						),
				0.5
				);
	}
}
