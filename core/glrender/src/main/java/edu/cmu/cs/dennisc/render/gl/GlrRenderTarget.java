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
import edu.cmu.cs.dennisc.java.awt.RectangleUtilities;
import edu.cmu.cs.dennisc.math.Matrix4x4;
import edu.cmu.cs.dennisc.math.Ray;
import edu.cmu.cs.dennisc.math.immutable.MRectangleI;
import edu.cmu.cs.dennisc.pattern.AbstractReleasable;
import edu.cmu.cs.dennisc.render.AsynchronousImageCapturer;
import edu.cmu.cs.dennisc.render.AsynchronousPicker;
import edu.cmu.cs.dennisc.render.RenderCapabilities;
import edu.cmu.cs.dennisc.render.RenderFactory;
import edu.cmu.cs.dennisc.render.RenderTarget;
import edu.cmu.cs.dennisc.render.SynchronousImageCapturer;
import edu.cmu.cs.dennisc.render.SynchronousPicker;
import edu.cmu.cs.dennisc.render.event.RenderTargetListener;
import edu.cmu.cs.dennisc.render.gl.imp.RenderTargetImp;
import edu.cmu.cs.dennisc.render.gl.imp.adapters.AdapterFactory;
import edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrAbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.List;

/**
 * @author Dennis Cosgrove
 */
/*package-private*/abstract class GlrRenderTarget extends AbstractReleasable implements RenderTarget {
  public GlrRenderTarget(GlrRenderFactory glrRenderer, RenderCapabilities requestedCapabilities) {
    this.glrRenderer = glrRenderer;

    this.requestedCapabilities = requestedCapabilities;
    //todo
    this.actualCapabilities = requestedCapabilities;
  }

  @Override
  public RenderCapabilities getActualCapabilities() {
    return this.actualCapabilities;
  }

  @Override
  public RenderFactory getRenderFactory() {
    return this.glrRenderer;
  }

  @Override
  public SynchronousPicker getSynchronousPicker() {
    return this.imp.getSynchronousPicker();
  }

  @Override
  public SynchronousImageCapturer getSynchronousImageCapturer() {
    return this.imp.getSynchronousImageCapturer();
  }

  @Override
  public AsynchronousPicker getAsynchronousPicker() {
    return this.imp.getAsynchronousPicker();
  }

  @Override
  public AsynchronousImageCapturer getAsynchronousImageCapturer() {
    return this.imp.getAsynchronousImageCapturer();
  }

  @Override
  public void addSgCamera(AbstractCamera sgCamera) {
    this.imp.addSgCamera(sgCamera, this.getGLAutoDrawable());
  }

  @Override
  public void removeSgCamera(AbstractCamera sgCamera) {
    this.imp.removeSgCamera(sgCamera, this.getGLAutoDrawable());
  }

  @Override
  public void clearSgCameras() {
    this.imp.clearSgCameras(this.getGLAutoDrawable());
  }

  @Override
  public int getSgCameraCount() {
    return this.imp.getSgCameraCount();
  }

  @Override
  public AbstractCamera getSgCameraAt(int index) {
    return this.imp.getSgCameraAt(index);
  }

  @Override
  public List<AbstractCamera> getSgCameras() {
    return this.imp.getSgCameras();
  }

  @Override
  public AbstractCamera getCameraAtPixel(int xPixel, int yPixel) {
    return this.imp.getCameraAtPixel(xPixel, yPixel);
  }

  @Override
  public void addRenderTargetListener(RenderTargetListener listener) {
    this.imp.addRenderTargetListener(listener);
  }

  @Override
  public void removeRenderTargetListener(RenderTargetListener listener) {
    this.imp.removeRenderTargetListener(listener);
  }

  protected abstract Dimension getSurfaceSize(Dimension rv);

  @Override
  public final Dimension getSurfaceSize() {
    return getSurfaceSize(new Dimension());
  }

  @Override
  public final int getSurfaceWidth() {
    synchronized (s_sizeBufferForReuse) {
      getSurfaceSize(s_sizeBufferForReuse);
      return s_sizeBufferForReuse.width;
    }
  }

  @Override
  public final int getSurfaceHeight() {
    synchronized (s_sizeBufferForReuse) {
      getSurfaceSize(s_sizeBufferForReuse);
      return s_sizeBufferForReuse.height;
    }
  }

  @Override
  public MRectangleI getActualViewport(AbstractCamera sgCamera) {
    return RectangleUtilities.toMRectangleI(this.getActualViewportAsAwtRectangle(sgCamera));
  }


  public Rectangle getActualViewport(Rectangle rv, GlrAbstractCamera<? extends AbstractCamera> cameraAdapter) {
    Dimension surfaceSize = this.getSurfaceSize();
    return cameraAdapter.getActualViewport(rv, surfaceSize.width, surfaceSize.height);
  }

  @Override
  public final Rectangle getActualViewportAsAwtRectangle(AbstractCamera camera) {
    Rectangle rv = new Rectangle();
    GlrAbstractCamera<?> glrCamera = AdapterFactory.getAdapterFor(camera);
    return getActualViewport(rv, glrCamera);
  }

  @Override
  public Matrix4x4 getActualProjectionMatrix(Matrix4x4 rv, AbstractCamera camera) {
    synchronized (s_actualViewportBufferForReuse) {
      GlrAbstractCamera<? extends AbstractCamera> cameraAdapter = AdapterFactory.getAdapterFor(camera);
      getActualViewport(s_actualViewportBufferForReuse, cameraAdapter);
      return cameraAdapter.getActualProjectionMatrix(rv, s_actualViewportBufferForReuse);
    }
  }

  private Ray getRayAtPixel(Ray rv, int xPixel, int yPixel, AbstractCamera sgCamera) {
    if (sgCamera != null) {
      synchronized (s_actualViewportBufferForReuse) {
        GlrAbstractCamera<? extends AbstractCamera> cameraAdapter = AdapterFactory.getAdapterFor(sgCamera);
        getActualViewport(s_actualViewportBufferForReuse, cameraAdapter);
        cameraAdapter.getRayAtPixel(rv, xPixel, yPixel, s_actualViewportBufferForReuse);
      }
    } else {
      rv.setNaN();
    }
    return rv;
  }

  @Override
  public final Ray getRayAtPixel(int xPixel, int yPixel, AbstractCamera sgCamera) {
    return getRayAtPixel(new Ray(), xPixel, yPixel, sgCamera);
  }

  @Override
  public boolean isLetterboxed(AbstractCamera sgCamera) {
    GlrAbstractCamera<? extends AbstractCamera> cameraAdapter = AdapterFactory.getAdapterFor(sgCamera);
    return cameraAdapter.isLetterboxed();
  }

  @Override
  public void setLetterboxed(AbstractCamera sgCamera, boolean isLetterboxed) {
    GlrAbstractCamera<? extends AbstractCamera> cameraAdapter = AdapterFactory.getAdapterFor(sgCamera);
    cameraAdapter.setIsLetterboxed(isLetterboxed);
  }

  @Override
  public boolean isRenderingEnabled() {
    return m_isRenderingEnabled;
  }

  protected abstract void repaintIfAppropriate();

  @Override
  public void setRenderingEnabled(boolean isRenderingEnabled) {
    if (m_isRenderingEnabled != isRenderingEnabled) {
      m_isRenderingEnabled = isRenderingEnabled;
      this.repaintIfAppropriate();
      //      //todo
      //      if( m_isRenderingEnabled ) {
      //        if( m_glEventAdapter.isListening() ) {
      //          //pass
      //        } else {
      //          m_glEventAdapter.startListening( getGLAutoDrawable() );
      //        }
      //      } else {
      //        if( m_glEventAdapter.isListening() ) {
      //          m_glEventAdapter.stopListening( getGLAutoDrawable() );
      //        } else {
      //          //pass
      //        }
      //      }
    }
  }

  @Override
  public void forgetAllCachedItems() {
    this.imp.forgetAllCachedItems();
  }

  @Override
  public void clearUnusedTextures() {
    this.imp.clearUnusedTextures();
  }

  protected RenderTargetImp getRenderTargetImp() {
    return this.imp;
  }

  private final GlrRenderFactory glrRenderer;
  private final RenderCapabilities requestedCapabilities;
  private final RenderCapabilities actualCapabilities;

  protected final RenderTargetImp imp = new RenderTargetImp(this);

  private String m_description = new String();

  public abstract GLAutoDrawable getGLAutoDrawable();

  private boolean m_isRenderingEnabled = true;

  //
  private static Rectangle s_actualViewportBufferForReuse = new Rectangle();
  private static Dimension s_sizeBufferForReuse = new Dimension();
}
