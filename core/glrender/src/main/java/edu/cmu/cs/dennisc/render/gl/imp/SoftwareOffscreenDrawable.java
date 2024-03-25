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

import com.jogamp.nativewindow.AbstractGraphicsDevice;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLCapabilitiesChooser;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLDrawable;
import com.jogamp.opengl.GLException;
import edu.cmu.cs.dennisc.render.gl.GlDrawableUtils;
import jogamp.opengl.GLContextImpl;
import jogamp.opengl.GLDrawableHelper;
import jogamp.opengl.GLDrawableImpl;

/**
 * @author Dennis Cosgrove
 */
public final class SoftwareOffscreenDrawable extends OffscreenDrawable {
  private GLDrawableImpl glDrawable;
  private GLContextImpl glContext;

  private final Runnable displayAdapter = new Runnable() {
    @Override
    public void run() {
      fireDisplay(glContext.getGL().getGL2());
    }
  };
  private final Runnable initAdapter = new Runnable() {
    @Override
    public void run() {
    }
  };
  private GLDrawableHelper drawableHelper;

  public SoftwareOffscreenDrawable(DisplayCallback callback) {
    super(callback);
  }

  @Override
  protected GLDrawable getGlDrawable() {
    return this.glDrawable;
  }

  @Override
  public void initialize(GLCapabilities glRequestedCapabilities, GLCapabilitiesChooser glCapabilitiesChooser, GLContext glShareContext, int width, int height) {
    assert this.glDrawable == null : this;
    GLCapabilities glCapabilities;
    if (IS_HARDWARE_ACCELERATION_DESIRED) {
      glCapabilities = glRequestedCapabilities;
    } else {
      glCapabilities = (GLCapabilities) glRequestedCapabilities.clone();
      glCapabilities.setHardwareAccelerated(false);
    }
    this.glDrawable = GlDrawableUtils.createOffscreenDrawable(glCapabilities, glCapabilitiesChooser, width, height);
    this.glDrawable.setRealized(true);
    this.glContext = (GLContextImpl) this.glDrawable.createContext(glShareContext);
    //this.glContext.setSynchronized( true );
  }

  @Override
  public void destroy() {
    assert false;
    if (this.glContext != null) {
      this.glContext.destroy();
      this.glContext = null;
    }
    if (this.glDrawable != null) {
      AbstractGraphicsDevice graphicsDevice = this.glDrawable.getNativeSurface().getGraphicsConfiguration().getScreen().getDevice();
      this.glDrawable.setRealized(false);
      this.glDrawable = null;
      if (graphicsDevice != null) {
        graphicsDevice.close();
      }
    }
  }

  @Override
  public void display() {
    if (this.drawableHelper == null) {
      this.drawableHelper = new GLDrawableHelper();
    }
    if ((this.glDrawable != null) && (this.glContext != null) && (this.displayAdapter != null) && (this.initAdapter != null)) {
      //edu.cmu.cs.dennisc.java.util.logging.Logger.outln( this.glContext );
      this.drawableHelper.invokeGL(this.glDrawable, this.glContext, this.displayAdapter, this.initAdapter);
    } else {
      StringBuilder sb = new StringBuilder();
      if (this.glDrawable == null) {
        sb.append("glDrawable is null;");
      }
      if (this.glContext == null) {
        sb.append("glContext is null;");
      }
      if (this.displayAdapter == null) {
        sb.append("displayAdapter is null;");
      }
      if (this.initAdapter == null) {
        sb.append("initAdapter is null;");
      }
      throw new GLException(sb.toString());
    }
  }

  @Override
  public boolean isHardwareAccelerated() {
    return false;
  }
}
