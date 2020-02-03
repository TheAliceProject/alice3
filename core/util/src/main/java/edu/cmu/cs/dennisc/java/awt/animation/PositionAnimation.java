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
package edu.cmu.cs.dennisc.java.awt.animation;

import edu.cmu.cs.dennisc.math.InterpolationUtilities;
import edu.cmu.cs.dennisc.math.Point2f;

import javax.swing.SwingUtilities;
import java.awt.Component;
import java.awt.Point;

/**
 * @author Dennis Cosgrove
 */
public class PositionAnimation extends SubjectAsSeenByAnimation {
  public static final Point2f USE_EXISTING_VALUE_AT_RUN_TIME = null;

  private Point2f m_posBegin = new Point2f();
  private Point2f m_posEnd = new Point2f();

  private Point2f m_posBeginUsedAtRuntime = new Point2f();

  private Point2f m_posRuntime = new Point2f();

  public PositionAnimation() {
    m_posBeginUsedAtRuntime.setNaN();
    m_posBegin.setNaN();
    m_posEnd.setNaN();
  }

  public PositionAnimation(Component awtSubject, Component awtAsSeenBy, Point2f posBegin, Point2f posEnd) {
    super(awtSubject, awtAsSeenBy);
    m_posBeginUsedAtRuntime.setNaN();
    setPositionBegin(posBegin);
    setPositionEnd(posEnd);
  }

  public Point2f accessPositionBeginUsedAtRuntime() {
    return m_posBeginUsedAtRuntime;
  }

  public Point2f getPositionBeginUsedAtRuntime(Point2f rv) {
    rv.set(m_posBeginUsedAtRuntime);
    return rv;
  }

  public Point2f getPositionBeginUsedAtRuntime() {
    return getPositionBeginUsedAtRuntime(new Point2f());
  }

  public Point2f accessPositionBegin() {
    return m_posBegin;
  }

  public Point2f getPositionBegin(Point2f rv) {
    rv.set(m_posBegin);
    return rv;
  }

  public Point2f getPositionBegin() {
    return getPositionBegin(new Point2f());
  }

  public void setPositionBegin(Point2f posBegin) {
    if (posBegin != USE_EXISTING_VALUE_AT_RUN_TIME) {
      m_posBegin.set(posBegin);
    } else {
      m_posBegin.setNaN();
    }
  }

  public Point2f accessPositionEnd() {
    return m_posEnd;
  }

  public Point2f getPositionEnd(Point2f rv) {
    rv.set(m_posEnd);
    return rv;
  }

  public Point2f getPositionEnd() {
    return getPositionEnd(new Point2f());
  }

  public void setPositionEnd(Point2f posEnd) {
    m_posEnd.set(posEnd);
  }

  @Override
  public void prologue() {
    if (m_posBegin.isNaN()) {
      Point p = SwingUtilities.convertPoint(getSubject().getParent(), getSubject().getX(), getSubject().getY(), getAsSeenBy());
      m_posBeginUsedAtRuntime.set(p.x, p.y);
    } else {
      m_posBeginUsedAtRuntime.set(m_posBegin);
    }
  }

  @Override
  public void setPortion(double portion) {
    InterpolationUtilities.interpolate(m_posRuntime, m_posBeginUsedAtRuntime, m_posEnd, (float) portion);
    Point p = SwingUtilities.convertPoint(getAsSeenBy(), (int) m_posRuntime.x, (int) m_posRuntime.y, getSubject().getParent());
    getSubject().setLocation(p);
  }

  @Override
  public void epilogue() {
    Point p = SwingUtilities.convertPoint(getAsSeenBy(), (int) m_posEnd.x, (int) m_posEnd.y, getSubject().getParent());
    getSubject().setLocation(p);
    m_posBeginUsedAtRuntime.setNaN();
  }
}
