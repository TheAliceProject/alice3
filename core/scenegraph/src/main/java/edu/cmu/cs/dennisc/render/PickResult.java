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

package edu.cmu.cs.dennisc.render;

import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.scenegraph.Component;
import edu.cmu.cs.dennisc.scenegraph.Geometry;
import edu.cmu.cs.dennisc.scenegraph.Visual;

/**
 * @author Dennis Cosgrove
 */
public class PickResult {
  private Component m_sgSource;
  private Visual m_sgVisual;
  private boolean m_isFrontFacing;
  private Geometry m_sgGeometry;
  private int m_subElement;
  private Point3 m_xyzInSource = new Point3();
  private Point3 m_xyzInVisual = new Point3();

  public PickResult() {
    setNaN();
  }

  public PickResult(Component sgSource) {
    set(sgSource);
  }

  public PickResult(Component sgSource, Visual sgVisual, boolean isFrontFacing, Geometry sgGeometry, int subElement, Point3 xyzInSource) {
    set(sgSource, sgVisual, isFrontFacing, sgGeometry, subElement, xyzInSource);
  }

  public void set(Component sgSource) {
    setNaN();
    m_sgSource = sgSource;
  }

  public void set(Component sgSource, Visual sgVisual, boolean isFrontFacing, Geometry sgGeometry, int subElement, Point3 xyzInSource) {
    m_sgSource = sgSource;
    m_sgVisual = sgVisual;
    m_isFrontFacing = isFrontFacing;
    m_sgGeometry = sgGeometry;
    m_subElement = subElement;
    if (xyzInSource != null) {
      m_xyzInSource.set(xyzInSource);
    } else {
      m_xyzInSource.setNaN();
    }
    m_xyzInVisual.setNaN();
  }

  public void setNaN() {
    set(null, null, false, null, -1, null);
  }

  public Component getSource() {
    return m_sgSource;
  }

  public Visual getVisual() {
    return m_sgVisual;
  }

  public Geometry getGeometry() {
    return m_sgGeometry;
  }

  public boolean isFrontFacing() {
    return m_isFrontFacing;
  }

  public int getSubElement() {
    return m_subElement;
  }

  public Point3 accessPositionInSource() {
    return m_xyzInSource;
  }

  public Point3 getPositionInSource(Point3 rv) {
    rv.set(accessPositionInSource());
    return rv;
  }

  public Point3 getPositionInSource() {
    return getPositionInSource(new Point3());
  }

  public Point3 accessPositionInVisual() {
    if (m_xyzInSource.isNaN()) {
      if (m_xyzInVisual.isNaN()) {
        //pass
      } else {
        assert m_sgVisual != null;
        m_sgVisual.transformFrom_AffectReturnValuePassedIn(m_xyzInVisual, m_sgSource);
      }
    }
    return m_xyzInVisual;
  }

  public Point3 getPositionInVisual(Point3 rv) {
    rv.set(accessPositionInVisual());
    return rv;
  }

  public Point3 getPositionInVisual() {
    return getPositionInVisual(new Point3());
  }

  @Override
  public String toString() {
    return getClass().getName() + "[visual=" + m_sgVisual + "]";
    // ",geometry=" + m_sgGeometry
  }
}
