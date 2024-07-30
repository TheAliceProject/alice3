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

package edu.cmu.cs.dennisc.render.gl;

import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLOffscreenAutoDrawable;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.render.OffscreenRenderTarget;
import edu.cmu.cs.dennisc.render.RenderCapabilities;

import java.awt.Dimension;

/**
 * @author Dennis Cosgrove
 */
class GlrOffscreenRenderTarget extends GlrRenderTarget implements OffscreenRenderTarget {
  private final GLOffscreenAutoDrawable glPbuffer;

  GlrOffscreenRenderTarget(GlrRenderFactory lookingGlassFactory, int width, int height, GlrRenderTarget renderTargetToShareContextWith, RenderCapabilities requestedCapabilities) {
    super(lookingGlassFactory, requestedCapabilities);
    GLContext share;
    if (renderTargetToShareContextWith != null) {
      share = renderTargetToShareContextWith.getGLAutoDrawable().getContext();
    } else {
      share = null;
    }
    this.glPbuffer = GlDrawableUtils.createGlPixelBuffer(GlDrawableUtils.createGlCapabilities(requestedCapabilities), GlDrawableUtils.getPerhapsMultisampledGlCapabilitiesChooser(), width, height, share);
  }

  @Override
  public Dimension getSurfaceSize() {
    Dimension rv = new Dimension();
    //TODO: Should we change this to getGLJPanelHeight and getGLJPanelWidth? This is returning the drawable width and height, not the size of the associated panel
    if (this.glPbuffer != null) {
      rv.setSize(GlDrawableUtils.getGlDrawableWidth(this.glPbuffer), GlDrawableUtils.getGlDrawableHeight(this.glPbuffer));
    } else {
      rv.setSize(0, 0);
    }
    return rv;
  }


  @Override
  public void clearAndRenderOffscreen() {
    getGLAutoDrawable().display();
  }

  @Override
  protected void actuallyRelease() {
    super.actuallyRelease();
    if (this.glPbuffer != null) {
      this.glPbuffer.destroy();
    }
  }

  @Override
  public GLAutoDrawable getGLAutoDrawable() {
    if (glPbuffer == null) {
      Logger.severe("GlrOffscreenRenderTarget has null glPbuffer/GLAutoDrawable. This may be part of the pick bug.");
    }
    return glPbuffer;
  }

  @Override
  protected void repaintIfAppropriate() {
  }
}
