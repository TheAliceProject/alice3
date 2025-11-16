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

import edu.cmu.cs.dennisc.math.rigidbody.TranslationDerivative;
import edu.cmu.cs.dennisc.math.rigidbody.TranslationFunction;
import org.alice.math.immutable.*;

import java.awt.event.KeyEvent;

/**
 * @author Dennis Cosgrove
 * @deprecated This is only used by the IK program, and should be revisited if we ever resurrect that project.
 */
@Deprecated
class CameraNavigationDerivative extends TranslationDerivative {
}

/**
 * @author Dennis Cosgrove
 * @deprecated This is only used by the IK program, and should be revisited if we ever resurrect that project.
 */
@Deprecated
public class CameraNavigationFunction extends TranslationFunction<CameraNavigationDerivative> {
  private static final double DISTANCE_MINIMUM = 4.0;
  private static final double DISTANCE_MAXIMUM = 100.0;

  private static final double FORCE_FOR_ACCELERATION = +4.0;
  private static final double FORCE_FOR_DECELERATION = -8.0;

  private Vector3 m_velocityRequested = Vector3.ZERO;

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

  public void setKeyPressed(int keyCode, boolean isKeyPressed) {
    switch (keyCode) {
    case KeyEvent.VK_UP:
      m_isForwardKeyPressed = isKeyPressed;
      break;
    case KeyEvent.VK_DOWN:
      m_isBackwardKeyPressed = isKeyPressed;
      break;
    case KeyEvent.VK_LEFT:
      m_isLeftKeyPressed = isKeyPressed;
      break;
    case KeyEvent.VK_RIGHT:
      m_isRightKeyPressed = isKeyPressed;
      break;
    }
  }

  @Override
  protected CameraNavigationDerivative newDerivative() {
    return new CameraNavigationDerivative();
  }

  public void requestVelocity(Vector3 velocityRequested) {
    m_velocityRequested = velocityRequested;
  }

  public void requestVelocity(double x, double y, double z) {
    m_velocityRequested = new Vector3(x, y, z);
  }

  public void stopImmediately() {
    requestVelocity(Vector3.ZERO);
    setVelocity(Vector3.ZERO);
  }

  public void requestDistance(double distance) {
    m_distanceRequested = Math.max(Math.min(distance, DISTANCE_MAXIMUM), DISTANCE_MINIMUM);
  }

  public void requestYaw(Angle yaw) {
    m_yawRequested = yaw.getAsRadians();
  }

  public void requestDistanceChange(double delta) {
    requestDistance(m_distanceRequested + delta);
  }

  public void requestTarget(Point3 target) {
    setTranslation(target);
  }

  public void requestOrbit(double yawDelta, double pitchDelta) {
    m_yawRequested += yawDelta;
    m_pitchRequested += pitchDelta;
  }

  public Angle getYawRequested() {
    return new AngleInRadians(m_yawRequested);
  }

  public Angle getPitchRequested() {
    return new AngleInRadians(m_pitchRequested);
  }

  public double getDistanceRequested() {
    return m_distanceRequested;
  }

  public Point3 accessTargetRequested() {
    return getTranslation();
  }

  public Point3 getTargetRequested() {
    return getTranslation();
  }

  private static double getHeight(double distance) {
    double d = distance * 0.1;
    return d * d;
  }

  private static double getPitchMinimum(double height, double distance) {
    return Math.atan2(height, distance);
  }

  @Override
  protected Vector3 getForce(double t) {
    final double LENGTH_SQUARED_THRESHOLD = 0.25;
    if (m_velocityRequested.isZero() && (getVelocity().magnitudeSquared() < LENGTH_SQUARED_THRESHOLD)) {
      setMomentum(Vector3.ZERO);
      return Vector3.ZERO;
    }
    return new Vector3(
        requestDirection(m_velocityRequested.x(), getVelocity().x()),
        requestDirection(m_velocityRequested.y(), getVelocity().y()),
        requestDirection(m_velocityRequested.z(), getVelocity().z()));
  }

  private static double requestDirection(double requested, double current) {
    if (requested > 0) {
      return requested > current ? +FORCE_FOR_ACCELERATION : +FORCE_FOR_DECELERATION;
    } else {
      return requested > current ? -FORCE_FOR_DECELERATION : -FORCE_FOR_ACCELERATION;
    }
  }

  @Override
  protected CameraNavigationDerivative evaluate(CameraNavigationDerivative rv, double t) {
    return super.evaluate(rv, t);
  }

  @Override
  protected CameraNavigationDerivative evaluate(CameraNavigationDerivative rv, double t, double dt, CameraNavigationDerivative derivative) {
    return super.evaluate(rv, t, dt, derivative);
  }

  @Override
  public void update(CameraNavigationDerivative a, CameraNavigationDerivative b, CameraNavigationDerivative c, CameraNavigationDerivative d, double dt) {
    super.update(a, b, c, d, dt);

    double delta = 2.0 * dt;

    Point3 translation = getTranslation();
    double y = Math.max(translation.y(), 0);
    double z = translation.z();
    if (m_isForwardKeyPressed) {
      z -= delta;
    }
    if (m_isBackwardKeyPressed) {
      z += delta;
    }
    double x = translation.x();
    if (m_isLeftKeyPressed) {
      x -= delta;
    }
    if (m_isRightKeyPressed) {
      x += delta;
    }
    setTranslation(new Point3(x, y, z));
    //edu.cmu.cs.dennisc.print.PrintUtilities.println( "update:", a );
  }

  public AffineMatrix4x4 getTransformation() {
    double height = getHeight(m_distanceRequested);
    double pitch = Math.max(m_pitchRequested, getPitchMinimum(height, m_distanceRequested));

    return AffineMatrix4x4.IDENTITY
        .rotateAboutYAxis(new AngleInRadians(m_yawRequested))
        .withTranslation(new Point3(0, height, m_distanceRequested))
        .withTranslation(getTranslation())
        .rotateAboutXAxis(new AngleInRadians(-pitch));
  }
}
