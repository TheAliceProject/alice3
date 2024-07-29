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

import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLCapabilitiesChooser;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLOffscreenAutoDrawable;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.render.gl.GlDrawableUtils;

/**
 * @author Dennis Cosgrove
 */
public final class PixelBufferOffscreenDrawable extends OffscreenDrawable {
  private final GLEventListener glEventListener = new GLEventListener() {
    @Override
    public void init(GLAutoDrawable drawable) {
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    }

    @Override
    public void display(GLAutoDrawable drawable) {
      Throwable throwable = null;
      try {
        drawable.getGL();
        drawable.getContext().makeCurrent();
      } catch (Throwable t) {
        throwable = t;
      }
      if (throwable != null) {
        if (throwable instanceof NullPointerException) {
          NullPointerException nullPointerException = (NullPointerException) throwable;
          Logger.info(nullPointerException);
        } else {
          Logger.throwable(throwable);
        }
      } else {
        fireDisplay(drawable.getGL().getGL2());
      }
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }
  };

  private GLOffscreenAutoDrawable glPixelBuffer;

  public PixelBufferOffscreenDrawable(DisplayCallback callback) {
    super(callback);
  }

  @Override
  protected GLOffscreenAutoDrawable getGlDrawable() {
    return this.glPixelBuffer;
  }

  @Override
  public void initialize(GLCapabilities glRequestedCapabilities, GLCapabilitiesChooser glCapabilitiesChooser, GLContext glShareContext, int width, int height) {
    if (this.glPixelBuffer != null) {
      Logger.severe(this);
    } else {
      this.glPixelBuffer = GlDrawableUtils.createOffscreenAutoDrawable(glRequestedCapabilities, glCapabilitiesChooser, width, height);
      if (this.getCallback() != null) {
        this.glPixelBuffer.addGLEventListener(glEventListener);
      }
    }
  }

  @Override
  public void destroy() {
    if (this.glPixelBuffer != null) {
      this.glPixelBuffer.destroy();
      this.glPixelBuffer = null;
    }
  }

  @Override
  public void display() {
    this.glPixelBuffer.display();
  }

  @Override
  public boolean isHardwareAccelerated() {
    return true;
  }
}
