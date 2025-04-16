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

import edu.cmu.cs.dennisc.math.rungekutta.Function;
import org.alice.math.immutable.Point3;
import org.alice.math.immutable.Vector3;

/**
 * @author Dennis Cosgrove
 */
public abstract class TranslationFunction<E extends TranslationDerivative> implements Function<E> {
  private Point3 m_translation = Point3.ORIGIN;
  private Vector3 m_momentum = Vector3.ZERO;

  private Vector3 m_velocity = Vector3.ZERO;

  private double m_mass = 1.0;
  private double m_inverseMass = 1.0;

  @Override
  protected Object clone() throws CloneNotSupportedException {
    TranslationFunction<E> pf = (TranslationFunction<E>) super.clone();
    pf.m_translation = m_translation;
    pf.m_momentum = m_momentum;
    pf.m_velocity = m_velocity;
    return pf;
  }

  public Point3 getTranslation() {
    return m_translation;
  }

  public void setTranslation(Point3 translation) {
    m_translation = translation;
  }

  public Vector3 getMomentum() {
    return m_momentum;
  }

  public void setMomentum(Vector3 momentum) {
    m_momentum = momentum;
  }

  public Vector3 getVelocity() {
    return m_velocity;
  }

  public void setVelocity(Vector3 velocity) {
    m_velocity =  velocity;
  }

  public double getMass() {
    return m_mass;
  }

  public void setMass(double mass) {
    m_mass = mass;
    m_inverseMass = 1 / m_mass;
  }

  protected abstract Vector3 getForce(double t);

  protected E newDerivative() {
    return (E) new TranslationDerivative();
  }

  protected E evaluate(E rv, double t) {
    rv.velocity = m_velocity;
    rv.force = getForce(t);
    return rv;
  }

  //todo: better name
  protected void update(double t, double dt, E derivative) {
    m_translation = m_translation.plus(derivative.velocity.times(dt));
    m_momentum = m_momentum.plus(derivative.force.times(dt));
  }

  protected E evaluate(E rv, double t, double dt, E derivative) {
    try {
      TranslationFunction<E> pf = (TranslationFunction<E>) this.clone();
      pf.update(t, dt, derivative);
      pf.update();
      return pf.evaluate(t + dt);
    } catch (CloneNotSupportedException cnse) {
      throw new RuntimeException(cnse);
    }
  }

  @Override
  public final E evaluate(double t) {
    return evaluate(newDerivative(), t);
  }

  @Override
  public final E evaluate(double t, double dt, E derivative) {
    return evaluate(newDerivative(), t, dt, derivative);
  }

  @Override
  public void update(E a, E b, E c, E d, double dt) {
    m_translation = m_translation.plus(a.velocity.plus(b.velocity.plus(c.velocity).times(2.0).plus(d.velocity)).times(dt / 6));
    m_momentum =  m_momentum.plus(a.force.plus(b.force.plus(c.force).times(2.0).plus(d.force)).times(dt / 6));
  }

  @Override
  public void update() {
    m_velocity = m_momentum.times(m_inverseMass);
  }
}
