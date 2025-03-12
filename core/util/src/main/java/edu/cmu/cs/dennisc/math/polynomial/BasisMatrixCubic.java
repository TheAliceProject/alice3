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

package edu.cmu.cs.dennisc.math.polynomial;

import org.alice.math.immutable.Matrix4x4;
import org.alice.math.immutable.Vector4;

/**
 * @author Dennis Cosgrove
 */
public abstract class BasisMatrixCubic implements Cubic {
  private Matrix4x4 m_h;
  private Vector4 m_g;

  protected BasisMatrixCubic(Matrix4x4 h, Vector4 g) {
    m_h = h;
    m_g = g;
  }

  @Override
  public boolean isNaN() {
    return (m_h == null) || m_h.isNaN() || (m_g == null) || m_g.isNaN();
  }

  @Override
  public double evaluate(double t) {
    double ttt = t * t * t;
    double tt = t * t;
    return (((ttt * m_h.columnRight().x()) + (tt * m_h.columnRight().y()) + (t * m_h.columnRight().z()) + m_h.columnRight().w()) * m_g.x()) + (((ttt * m_h.columnUp().x()) + (tt * m_h.columnUp().y()) + (t * m_h.columnUp().z()) + m_h.columnUp().w()) * m_g.y()) + (((ttt * m_h.columnBackward().x()) + (tt * m_h.columnBackward().y()) + (t * m_h.columnBackward().z()) + m_h.columnBackward().w()) * m_g.z()) + (((ttt * m_h.columnTranslation().x()) + (tt * m_h.columnTranslation().y()) + (t * m_h.columnTranslation().z()) + m_h.columnTranslation().w()) * m_g.w());
  }

  @Override
  public double evaluateDerivative(double t) {
    double tt3 = t * t * 3;
    double t2 = t * 2;
    return (((tt3 * m_h.columnRight().x()) + (t2 * m_h.columnRight().y()) + m_h.columnRight().z()) * m_g.x()) + (((tt3 * m_h.columnUp().x()) + (t2 * m_h.columnUp().y()) + m_h.columnUp().z()) * m_g.y()) + (((tt3 * m_h.columnBackward().x()) + (t2 * m_h.columnBackward().y()) + m_h.columnBackward().z()) * m_g.z()) + (((tt3 * m_h.columnTranslation().x()) + (t2 * m_h.columnTranslation().y()) + m_h.columnTranslation().z()) * m_g.w());
  }
}
