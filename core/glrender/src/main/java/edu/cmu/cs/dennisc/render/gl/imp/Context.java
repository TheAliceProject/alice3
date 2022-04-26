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

package edu.cmu.cs.dennisc.render.gl.imp;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import edu.cmu.cs.dennisc.java.util.DStack;
import edu.cmu.cs.dennisc.java.util.Stacks;

/**
 * @author Dennis Cosgrove
 */
public abstract class Context {
  public GL2 gl;
  public GLU glu;

  private GLUquadric m_quadric;

  public Context() {
    glu = new GLU();
  }

  private int scaledCount = 0;
  private DStack<Integer> scaledCountStack = Stacks.newStack();

  public void initialize() {
    this.scaledCount = 0;
    this.disableNormalize();
  }

  public boolean isScaled() {
    return this.scaledCount > 0;
  }

  public abstract boolean isTextureEnabled();

  public abstract boolean isLightingEnabled();

  protected abstract void enableNormalize();

  protected abstract void disableNormalize();

  public void incrementScaledCount() {
    this.scaledCount++;
    if (this.scaledCount == 1) {
      this.enableNormalize();
    }
  }

  public void decrementScaledCount() {
    if (this.scaledCount == 1) {
      this.disableNormalize();
    }
    this.scaledCount--;
  }

  public void pushScaledCountAndSetToZero() {
    this.scaledCountStack.push(this.scaledCount);
    this.scaledCount = 0;
  }

  public void popAndRestoreScaledCount() {
    this.scaledCount = this.scaledCountStack.pop();
  }

  //todo: synchronize?
  public GLUquadric getQuadric() {
    if (m_quadric == null) {
      m_quadric = glu.gluNewQuadric();
    }
    return m_quadric;
  }

  public void setGL(GL2 gl) {
    if (this.gl != gl) {
      this.gl = gl;
    }
  }
}
