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

import com.jogamp.nativewindow.CapabilitiesImmutable;
import com.jogamp.opengl.DefaultGLCapabilitiesChooser;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLCapabilitiesChooser;
import com.jogamp.opengl.GLCapabilitiesImmutable;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLDrawable;
import com.jogamp.opengl.GLDrawableFactory;
import com.jogamp.opengl.GLOffscreenAutoDrawable;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.awt.GLJPanel;
import edu.cmu.cs.dennisc.java.lang.SystemUtilities;
import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.render.RenderCapabilities;
import jogamp.opengl.GLDrawableImpl;

import java.awt.Dimension;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Dennis Cosgrove
 */
public class GlDrawableUtils {
  private static final Map<GLOffscreenAutoDrawable, Dimension> mapPixelBufferToDimension;

  private static boolean areEquivalentIgnoringMultisample(GLCapabilitiesImmutable a, GLCapabilitiesImmutable b) {
    return a.getAccumAlphaBits() == b.getAccumAlphaBits()
        && a.getAccumBlueBits() == b.getAccumBlueBits()
        && a.getAccumGreenBits() == b.getAccumGreenBits()
        && a.getAccumRedBits() == b.getAccumRedBits()
        && a.getDepthBits() == b.getDepthBits()
        && a.getDoubleBuffered() == b.getDoubleBuffered()
        && a.getHardwareAccelerated() == b.getHardwareAccelerated()
        && a.getStencilBits() == b.getStencilBits()
        && a.getStereo() == b.getStereo()
        && a.isPBuffer() == b.isPBuffer()
        && a.isFBO() == b.isFBO();
  }

  private static class GlMultisampledCapabilitiesChooser extends DefaultGLCapabilitiesChooser {
    private final int maximumMultisampleCount;

    public GlMultisampledCapabilitiesChooser(int maximumMultisampleCount) {
      this.maximumMultisampleCount = maximumMultisampleCount;
    }

    @Override
    public int chooseCapabilities(CapabilitiesImmutable desired, List<? extends CapabilitiesImmutable> available, int windowSystemRecommendedChoice) {
      int original = super.chooseCapabilities(desired, available, windowSystemRecommendedChoice);
      int rv = original;
      if ((0 <= rv) && (rv < available.size())) {
        CapabilitiesImmutable selected = available.get(rv);
        if (selected instanceof GLCapabilitiesImmutable) {
          GLCapabilitiesImmutable glSelected = (GLCapabilitiesImmutable) selected;
          int i = 0;
          int indexOfMax = -1;
          int max = -1;
          for (CapabilitiesImmutable c : available) {
            if (c instanceof GLCapabilitiesImmutable) {
              GLCapabilitiesImmutable glCandidate = (GLCapabilitiesImmutable) c;
              //edu.cmu.cs.dennisc.java.util.logging.Logger.errln( "consider", i, glCandidate );
              if (glCandidate.getSampleBuffers()) {
                if (areEquivalentIgnoringMultisample(glSelected, glCandidate)) {
                  int numSamples = glCandidate.getNumSamples();
                  if (numSamples <= this.maximumMultisampleCount) {
                    if (numSamples == this.maximumMultisampleCount) {
                      indexOfMax = i;
                      break;
                    } else {
                      if (numSamples > max) {
                        indexOfMax = i;
                        max = numSamples;
                      }
                    }
                  }
                }
              }
            }
            i++;
          }
          if (indexOfMax != -1) {
            rv = indexOfMax;
          }
        }
        //        if( rv != original ) {
        //          edu.cmu.cs.dennisc.java.util.logging.Logger.errln( "original", original, available.get( original ) );
        //          edu.cmu.cs.dennisc.java.util.logging.Logger.errln( "replacement", rv, available.get( rv ) );
        //        }
      }
      return rv;
    }
  }

  private static final GLCapabilitiesChooser glDefaultCapabilitiesChooser = new DefaultGLCapabilitiesChooser();
  private static final GLCapabilitiesChooser glMultisampleCapabilitiesChooser;

  static {
    if (SystemUtilities.isLinux()) {
      mapPixelBufferToDimension = Maps.newHashMap();
    } else {
      mapPixelBufferToDimension = null;
    }

    final int MAXIMUM_MULTISAMPLE_COUNT = 0; //4;
    if (MAXIMUM_MULTISAMPLE_COUNT > 1) {
      glMultisampleCapabilitiesChooser = new GlMultisampledCapabilitiesChooser(MAXIMUM_MULTISAMPLE_COUNT);
    } else {
      glMultisampleCapabilitiesChooser = null;
    }
  }

  private GlDrawableUtils() {
    throw new AssertionError();
  }

  static GLCapabilities createGlCapabilities(RenderCapabilities requestedCapabilities) {
    GLCapabilities capabilities = new GLCapabilities(GLProfile.getDefault());
    capabilities.setStencilBits(requestedCapabilities.getStencilBits());
    if (!SystemUtilities.isWindows()) {
      capabilities.setDepthBits(32);
    }
    return capabilities;
  }

  public static GLCapabilitiesChooser getPerhapsMultisampledGlCapabilitiesChooser() {
    return Objects.requireNonNullElse(glMultisampleCapabilitiesChooser, glDefaultCapabilitiesChooser);
  }

  public static GLCanvas createGLCanvas(RenderCapabilities renderCapabilities) {
    return new GLCanvas(createGlCapabilities(renderCapabilities), getPerhapsMultisampledGlCapabilitiesChooser(), null);
  }

  public static boolean canCreateGlPixelBuffer() {
    GLProfile glProfile = GLProfile.getDefault();
    GLDrawableFactory glDrawableFactory = GLDrawableFactory.getFactory(glProfile);
    return glDrawableFactory.canCreateGLPbuffer(glDrawableFactory.getDefaultDevice(), glProfile);
  }

  public static GLOffscreenAutoDrawable createOffscreenAutoDrawable(GLCapabilities glCapabilities, GLCapabilitiesChooser glCapabilitiesChooser, int width, int height) {
    try {
      GLDrawableFactory factory = getGlDrawableFactory();
      GLOffscreenAutoDrawable drawable = factory.createOffscreenAutoDrawable(factory.getDefaultDevice(), glCapabilities, glCapabilitiesChooser, width, height);

      // This is a work around for Linux users.
      // Because of a bug in mesa (https://bugs.freedesktop.org/show_bug.cgi?id=24320) sometimes on Linux the method glXQueryDrawable() will
      // return 0 for information about a drawable, include getWidth and getHeight even though the drawable is the correct size.
      if (mapPixelBufferToDimension != null) {
        mapPixelBufferToDimension.put(drawable, new Dimension(width, height));
      }

      return drawable;
    } catch (Exception e) {
    Logger.warning("GLDrawableFactory failed to create offscreen drawable. This may cause errors in rendering.");
    return null;
    }
  }

  public static GLDrawableImpl createOffscreenDrawable(GLCapabilities glCapabilities, GLCapabilitiesChooser glCapabilitiesChooser, int width, int height) {
      return (GLDrawableImpl) getGlDrawableFactory().createOffscreenDrawable(null, glCapabilities, glCapabilitiesChooser, width, height);
  }

  private static GLDrawableFactory getGlDrawableFactory() {
    return GLDrawableFactory.getFactory(GLProfile.getDefault());
  }

  public static int getGlDrawableWidth(GLDrawable drawable) {
    // Bug in linux opengl, getWidth ALWAYS returns 0
    int width = drawable.getSurfaceWidth();
    if (width == 0) {
      if (mapPixelBufferToDimension != null) {
        if (drawable instanceof GLOffscreenAutoDrawable) {
          GLOffscreenAutoDrawable glPixelBuffer = (GLOffscreenAutoDrawable) drawable;
          Dimension size = mapPixelBufferToDimension.get(glPixelBuffer);
          if (size != null) {
            width = size.width;
          }
        }
      }
    }
    return width;
  }

  public static int getGlDrawableHeight(GLDrawable drawable) {
    // Bug in linux opengl, getHeight ALWAYS returns 0
    int height = drawable.getSurfaceHeight();
    if (height == 0) {
      if (mapPixelBufferToDimension != null) {
        if (drawable instanceof GLOffscreenAutoDrawable) {
          GLOffscreenAutoDrawable glPixelBuffer = (GLOffscreenAutoDrawable) drawable;
          Dimension size = mapPixelBufferToDimension.get(glPixelBuffer);
          if (size != null) {
            height = size.height;
          }
        }
      }
    }
    return height;
  }

  public static int getGLJPanelHeight(GLDrawable drawable) {
    if (drawable instanceof GLJPanel) {
      GLJPanel glPanel = (GLJPanel) drawable;
      return glPanel.getHeight();
    } else if (drawable instanceof GLCanvas) {
      GLCanvas glCanvas = (GLCanvas) drawable;
      return glCanvas.getHeight();
    } else {
      return getGlDrawableHeight(drawable);
    }
  }

  public static int getGLJPanelWidth(GLDrawable drawable) {
    if (drawable instanceof GLJPanel) {
      GLJPanel glPanel = (GLJPanel) drawable;
      return glPanel.getWidth();
    } else if (drawable instanceof GLCanvas) {
      GLCanvas glCanvas = (GLCanvas) drawable;
      return glCanvas.getWidth();
    } else {
      return getGlDrawableWidth(drawable);
    }
  }

  public static GLContext getGlContextToShare(GlrRenderTarget glrRenderTarget) {
    GLContext share;
    if (glrRenderTarget != null) {
      share = glrRenderTarget.getGLAutoDrawable().getContext();
    } else {
      share = null;
    }
    return share;
  }

}
