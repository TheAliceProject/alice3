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
package edu.cmu.cs.dennisc.animation.affine;

import edu.cmu.cs.dennisc.scenegraph.AbstractTransformable;
import edu.cmu.cs.dennisc.scenegraph.ReferenceFrame;
import org.alice.math.immutable.AffineMatrix4x4;
import org.alice.math.immutable.Point3;
import org.alice.math.immutable.UnitQuaternion;

import java.util.Objects;

/**
 * @author Dennis Cosgrove
 */
public class PointOfViewAnimation extends AffineAnimation {
  public static final AffineMatrix4x4 USE_EXISTING_VALUE_AT_RUN_TIME = null;

  private AffineMatrix4x4 m_povBegin = AffineMatrix4x4.NaN;
  private AffineMatrix4x4 m_povEnd = AffineMatrix4x4.NaN;

  private AffineMatrix4x4 m_pov0Runtime = AffineMatrix4x4.NaN;
  private AffineMatrix4x4 m_povRuntime = AffineMatrix4x4.NaN;

  private UnitQuaternion m_q0 = UnitQuaternion.NaN;
  private UnitQuaternion m_q1 = UnitQuaternion.NaN;
  private UnitQuaternion m_q = UnitQuaternion.NaN;

  private Point3 m_t0 = Point3.ORIGIN;
  private Point3 m_t1 = Point3.ORIGIN;
  private Point3 m_t = Point3.ORIGIN;

  public PointOfViewAnimation(AbstractTransformable sgSubject, ReferenceFrame sgAsSeenBy, AffineMatrix4x4 povBegin, AffineMatrix4x4 povEnd) {
    super(sgSubject, sgAsSeenBy);
    setPointOfViewBegin(povBegin);
    setPointOfViewEnd(povEnd);
  }

  public void setPointOfViewBegin(AffineMatrix4x4 povBegin) {
    if (povBegin != USE_EXISTING_VALUE_AT_RUN_TIME) {
      m_povBegin = povBegin;
    } else {
      m_povBegin = AffineMatrix4x4.NaN;
    }
  }

  public void setPointOfViewEnd(AffineMatrix4x4 povEnd) {
    m_povEnd = Objects.requireNonNullElse(povEnd, AffineMatrix4x4.NaN);
  }

  @Override
  protected void prologue() {
    if (m_povBegin.isNaN()) {
      m_pov0Runtime = getSubject().getTransformation(getAsSeenBy());
    } else {
      m_pov0Runtime = m_povBegin;
    }

    m_q0 = m_pov0Runtime.orientation().asUnitQuaternion();
    m_q1 = m_povEnd.orientation().asUnitQuaternion();

    m_t0 = m_pov0Runtime.translation();
    m_t1 = m_povEnd.translation();

    m_povRuntime = m_pov0Runtime;
    m_q = m_q0;
    m_t = m_t0;
  }

  @Override
  protected void setPortion(double portion) {
    m_q = m_q0.interpolate(m_q1, portion);
    m_t = m_t0.interpolate(m_t1, portion);

    m_povRuntime = new AffineMatrix4x4(m_q.asMatrix3x3(), m_t);

    getSubject().setTransformation(m_povRuntime, getAsSeenBy());
    getSubject().notifyTransformationListeners();
  }

  @Override
  protected void epilogue() {
    getSubject().setTransformation(m_povEnd, getAsSeenBy());
    getSubject().notifyTransformationListeners();
    m_pov0Runtime = AffineMatrix4x4.NaN;
    m_povRuntime = AffineMatrix4x4.NaN;
  }
}
