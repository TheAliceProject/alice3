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


import org.alice.math.immutable.AffineMatrix4x4;
import org.alice.math.immutable.UnitQuaternion;
import org.alice.math.immutable.Vector3;

/**
 * @author Dennis Cosgrove
 */
public abstract class TranslationAndOrientationFunction extends TranslationFunction<TranslationAndOrientationDerivative> {
  private UnitQuaternion m_orientation = UnitQuaternion.NaN;
  private Vector3 m_angularMomentum = Vector3.ZERO;

  private UnitQuaternion m_spin = UnitQuaternion.NaN;
  private Vector3 m_angularVelocity = Vector3.ZERO;

  //todo: use Matrix
  private double m_inertiaTensor = 1 / 6.0;
  private double m_inverseInertiaTensor = 6.0;

  //  @Override
  //  protected Object clone() throws CloneNotSupportedException {
  //    TranslationAndOrientationFunction pof = (TranslationAndOrientationFunction)super.clone();
  //    pof.m_orientation = new edu.cmu.cs.dennisc.math.UnitQuaternion( m_orientation );
  //    pof.m_angularMomentum = new edu.cmu.cs.dennisc.math.Vector3( m_angularMomentum );
  //    pof.m_spin = new edu.cmu.cs.dennisc.math.UnitQuaternion( m_spin );
  //    pof.m_angularVelocity = new edu.cmu.cs.dennisc.math.Vector3( m_angularVelocity );
  //    return pof;
  //  }

  public UnitQuaternion accessOrientation() {
    return m_orientation;
  }

  public UnitQuaternion getOrientation() {
    return m_orientation;
  }

  public void setOrientation(UnitQuaternion orientation) {
    m_orientation = orientation;
  }

  public Vector3 getAngularMomentum() {
    return m_angularMomentum;
  }

  public void setAngularMomentum(Vector3 angularMomentum) {
    m_angularMomentum = angularMomentum;
  }

  public UnitQuaternion getSpin() {
    return m_spin;
  }

  public void setSpin(UnitQuaternion spin) {
    m_spin = spin;
  }

  public Vector3 getAngularVelocity() {
    return m_angularVelocity;
  }

  public void setAngularVelocity(Vector3 angularVelocity) {
    m_angularVelocity = angularVelocity;
  }

  public double getInertiaTensor() {
    return m_inertiaTensor;
  }

  public void setInertiaTensor(double inertiaTensor) {
    m_inertiaTensor = inertiaTensor;
    m_inverseInertiaTensor = 1 / m_inertiaTensor;
  }

  public AffineMatrix4x4 getTransformation() {
    return new AffineMatrix4x4(m_orientation.asMatrix3x3(), getTranslation());
  }

  public void setTransformation(AffineMatrix4x4 transformation) {
    setTranslation(transformation.translation());
    setOrientation(transformation.orientation().asUnitQuaternion());
  }

  protected abstract Vector3 getTorque(Vector3 rv, double t);

  @Override
  protected TranslationAndOrientationDerivative newDerivative() {
    return new TranslationAndOrientationDerivative();
  }

  @Override
  protected TranslationAndOrientationDerivative evaluate(TranslationAndOrientationDerivative rv, double t) {
    rv.spin = m_spin;
    getTorque(rv.torque, t);
    return super.evaluate(rv, t);
  }

  @Override
  protected void update(double t, double dt, TranslationAndOrientationDerivative derivative) {
    super.update(t, dt, derivative);
    m_orientation = m_orientation.plus(derivative.spin.times(dt)).normalized();
    m_angularMomentum = m_angularMomentum.plus(derivative.torque.times(dt));
  }

  @Override
  protected TranslationAndOrientationDerivative evaluate(TranslationAndOrientationDerivative rv, double t, double dt, TranslationAndOrientationDerivative positionDerivative) {
    return super.evaluate(rv, t, dt, positionDerivative);
  }

  @Override
  public void update(TranslationAndOrientationDerivative a, TranslationAndOrientationDerivative b, TranslationAndOrientationDerivative c, TranslationAndOrientationDerivative d, double dt) {
    super.update(a, b, c, d, dt);
    m_orientation = m_orientation.plus(a.spin.plus((b.spin.plus(c.spin)).times(2.0).plus(d.spin)).times(dt / 6)).normalized();
    m_angularMomentum = m_angularMomentum.plus(a.torque.plus((b.torque.plus(c.torque).times(2.0).plus(d.torque))).times(dt / 6));
  }

  @Override
  public void update() {
    super.update();
    m_angularVelocity = m_angularMomentum.times(m_inverseInertiaTensor);
    m_spin = (new UnitQuaternion(0, m_angularVelocity.x(), m_angularVelocity.y(), m_angularVelocity.z()))
        .times(m_orientation).times(0.5);
  }
}
