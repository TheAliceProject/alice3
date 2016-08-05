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

/**
 * @author Dennis Cosgrove
 */
public abstract class TranslationFunction<E extends TranslationDerivative> implements edu.cmu.cs.dennisc.math.rungekutta.Function<E> {
	private edu.cmu.cs.dennisc.math.Point3 m_translation = new edu.cmu.cs.dennisc.math.Point3();
	private edu.cmu.cs.dennisc.math.Vector3 m_momentum = new edu.cmu.cs.dennisc.math.Vector3();

	private edu.cmu.cs.dennisc.math.Vector3 m_velocity = new edu.cmu.cs.dennisc.math.Vector3();

	private double m_mass = 1.0;
	private double m_inverseMass = 1.0;

	@Override
	protected Object clone() throws CloneNotSupportedException {
		TranslationFunction<E> pf = (TranslationFunction<E>)super.clone();
		pf.m_translation = new edu.cmu.cs.dennisc.math.Point3( m_translation );
		pf.m_momentum = new edu.cmu.cs.dennisc.math.Vector3( m_momentum );
		pf.m_velocity = new edu.cmu.cs.dennisc.math.Vector3( m_velocity );
		return pf;
	}

	public edu.cmu.cs.dennisc.math.Point3 accessTranslation() {
		return m_translation;
	}

	public edu.cmu.cs.dennisc.math.Point3 getTranslation( edu.cmu.cs.dennisc.math.Point3 rv ) {
		rv.set( m_translation );
		return rv;
	}

	public edu.cmu.cs.dennisc.math.Point3 getTranslation() {
		return getTranslation( new edu.cmu.cs.dennisc.math.Point3() );
	}

	public void setTranslation( edu.cmu.cs.dennisc.math.Point3 translation ) {
		m_translation.set( translation );
	}

	public void setTranslation( double x, double y, double z ) {
		m_translation.set( x, y, z );
	}

	public edu.cmu.cs.dennisc.math.Vector3 accessMomentum() {
		return m_momentum;
	}

	public edu.cmu.cs.dennisc.math.Vector3 getMomentum( edu.cmu.cs.dennisc.math.Vector3 rv ) {
		rv.set( m_momentum );
		return rv;
	}

	public edu.cmu.cs.dennisc.math.Vector3 getMomentum() {
		return getMomentum( new edu.cmu.cs.dennisc.math.Vector3() );
	}

	public void setMomentum( edu.cmu.cs.dennisc.math.Vector3 momentum ) {
		m_momentum.set( momentum );
	}

	public void setMomentum( double x, double y, double z ) {
		m_momentum.set( x, y, z );
	}

	public edu.cmu.cs.dennisc.math.Vector3 accessVelocity() {
		return m_velocity;
	}

	public edu.cmu.cs.dennisc.math.Vector3 getVelocity( edu.cmu.cs.dennisc.math.Vector3 rv ) {
		rv.set( m_velocity );
		return rv;
	}

	public edu.cmu.cs.dennisc.math.Vector3 getVelocity() {
		return getVelocity( new edu.cmu.cs.dennisc.math.Vector3() );
	}

	public void setVelocity( edu.cmu.cs.dennisc.math.Vector3 velocity ) {
		m_velocity.set( velocity );
	}

	public void setVelocity( double x, double y, double z ) {
		m_velocity.set( x, y, z );
	}

	public double getMass() {
		return m_mass;
	}

	public void setMass( double mass ) {
		m_mass = mass;
		m_inverseMass = 1 / m_mass;
	}

	protected abstract edu.cmu.cs.dennisc.math.Vector3 getForce( edu.cmu.cs.dennisc.math.Vector3 rv, double t );

	protected E newDerivative() {
		return (E)new TranslationDerivative();
	}

	protected E evaluate( E rv, double t ) {
		rv.velocity.set( m_velocity );
		getForce( rv.force, t );
		return rv;
	}

	//todo: better name
	protected void update( double t, double dt, E derivative ) {
		//todo?
		//m_translation.add( edu.cmu.cs.dennisc.math.PointD3.createFromProduct( dt, derivative.velocity ) );
		m_translation.add( edu.cmu.cs.dennisc.math.Vector3.createMultiplication( derivative.velocity, dt ) );

		m_momentum.add( edu.cmu.cs.dennisc.math.Vector3.createMultiplication( derivative.force, dt ) );
	}

	protected E evaluate( E rv, double t, double dt, E derivative ) {
		try {
			TranslationFunction<E> pf = (TranslationFunction<E>)this.clone();
			pf.update( t, dt, derivative );
			pf.update();
			return pf.evaluate( t + dt );
		} catch( CloneNotSupportedException cnse ) {
			throw new RuntimeException( cnse );
		}
	}

	@Override
	public final E evaluate( double t ) {
		return evaluate( newDerivative(), t );
	}

	@Override
	public final E evaluate( double t, double dt, E derivative ) {
		return evaluate( newDerivative(), t, dt, derivative );
	}

	@Override
	public void update( E a, E b, E c, E d, double dt ) {
		//todo?
		//m_translation.add( edu.cmu.cs.dennisc.math.PointD3.createFromProduct( dt / 6, edu.cmu.cs.dennisc.math.PointD3.createFromAdd( a.velocity, edu.cmu.cs.dennisc.math.PointD3.createFromAdd( edu.cmu.cs.dennisc.math.PointD3.createFromProduct( 2.0, edu.cmu.cs.dennisc.math.PointD3.createFromAdd( b.velocity, c.velocity ) ), d.velocity ) ) ) );
		m_translation.add( edu.cmu.cs.dennisc.math.Vector3.createMultiplication(
				edu.cmu.cs.dennisc.math.Vector3.createAddition(
						a.velocity,
						edu.cmu.cs.dennisc.math.Vector3.createAddition(
								edu.cmu.cs.dennisc.math.Vector3.createMultiplication(
										edu.cmu.cs.dennisc.math.Vector3.createAddition(
												b.velocity,
												c.velocity
												),
										2.0
										),
								d.velocity
								)
						),
				dt / 6
				) );
		m_momentum.add( edu.cmu.cs.dennisc.math.Vector3.createMultiplication(
				edu.cmu.cs.dennisc.math.Vector3.createAddition(
						a.force,
						edu.cmu.cs.dennisc.math.Vector3.createAddition(
								edu.cmu.cs.dennisc.math.Vector3.createMultiplication(
										edu.cmu.cs.dennisc.math.Vector3.createAddition(
												b.force,
												c.force
												),
										2.0
										),
								d.force
								)
						),
				dt / 6
				) );
	}

	@Override
	public void update() {
		edu.cmu.cs.dennisc.math.Vector3.setReturnValueToMultiplication( m_velocity, m_momentum, m_inverseMass );
	}
}
