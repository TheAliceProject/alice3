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
import edu.cmu.cs.dennisc.math.Angle;
import edu.cmu.cs.dennisc.math.ClippedZPlane;
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
import edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrFrustumPerspectiveCamera;
import edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrOrthographicCamera;
import edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrSymmetricPerspectiveCamera;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.FrustumPerspectiveCamera;
import edu.cmu.cs.dennisc.scenegraph.OrthographicCamera;
import edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera;

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
  public RenderCapabilities getRequestedCapabilities() {
    return this.requestedCapabilities;
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
  public String getDescription() {
    return m_description;
  }

  @Override
  public void setDescription(String description) {
    m_description = description;
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

  @Override
  public List<RenderTargetListener> getRenderTargetListeners() {
    return this.imp.getRenderTargetListeners();
  }

  protected abstract Dimension getSurfaceSize(Dimension rv);

  protected abstract Dimension getDrawableSize(Dimension rv);

  @Override
  public final Dimension getDrawableSize() {
    return getDrawableSize(new Dimension());
  }

  @Override
  public final int getDrawableWidth() {
    synchronized (s_sizeBufferForReuse) {
      getDrawableSize(s_sizeBufferForReuse);
      return s_sizeBufferForReuse.width;
    }
  }

  @Override
  public final int getDrawableHeight() {
    synchronized (s_sizeBufferForReuse) {
      getDrawableSize(s_sizeBufferForReuse);
      return s_sizeBufferForReuse.height;
    }
  }

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

  @Override
  public MRectangleI getSpecifiedViewport(AbstractCamera sgCamera) {
    return RectangleUtilities.toMRectangleI(this.getSpecifiedViewportAsAwtRectangle(sgCamera));
  }

  @Override
  public void setSpecifiedViewport(AbstractCamera sgCamera, MRectangleI viewport) {
    this.setSpecifiedViewportAsAwtRectangle(sgCamera, RectangleUtilities.toAwtRectangle(viewport));
  }

  @Override
  public Rectangle getSpecifiedViewportAsAwtRectangle(AbstractCamera camera) {
    GlrAbstractCamera<? extends AbstractCamera> cameraAdapter = AdapterFactory.getAdapterFor(camera);
    return cameraAdapter.getSpecifiedViewport();
  }

  @Override
  public void setSpecifiedViewportAsAwtRectangle(AbstractCamera camera, Rectangle viewport) {
    GlrAbstractCamera<? extends AbstractCamera> cameraAdapter = AdapterFactory.getAdapterFor(camera);
    cameraAdapter.setSpecifiedViewport(viewport);
  }

  public Rectangle getActualViewport(Rectangle rv, GlrAbstractCamera<? extends AbstractCamera> cameraAdapter) {
    Dimension surfaceSize = this.getSurfaceSize();
    return cameraAdapter.getActualViewport(rv, surfaceSize.width, surfaceSize.height);
  }

  @Override
  public Rectangle getActualViewportAsAwtRectangle(Rectangle rv, AbstractCamera camera) {
    GlrAbstractCamera<?> glrCamera = AdapterFactory.getAdapterFor(camera);
    return getActualViewport(rv, glrCamera);
  }

  @Override
  public final Rectangle getActualViewportAsAwtRectangle(AbstractCamera camera) {
    return getActualViewportAsAwtRectangle(new Rectangle(), camera);
  }

  @Override
  public Matrix4x4 getActualProjectionMatrix(Matrix4x4 rv, AbstractCamera camera) {
    synchronized (s_actualViewportBufferForReuse) {
      GlrAbstractCamera<? extends AbstractCamera> cameraAdapter = AdapterFactory.getAdapterFor(camera);
      getActualViewport(s_actualViewportBufferForReuse, cameraAdapter);
      return cameraAdapter.getActualProjectionMatrix(rv, s_actualViewportBufferForReuse);
    }
  }

  @Override
  public final Matrix4x4 getActualProjectionMatrix(AbstractCamera camera) {
    return getActualProjectionMatrix(new Matrix4x4(), camera);
  }

  private ClippedZPlane getActualPicturePlane(ClippedZPlane rv, OrthographicCamera orthographicCamera) {
    synchronized (s_actualViewportBufferForReuse) {
      GlrOrthographicCamera orthographicCameraAdapter = AdapterFactory.getAdapterFor(orthographicCamera);
      getActualViewport(s_actualViewportBufferForReuse, orthographicCameraAdapter);
      return orthographicCameraAdapter.getActualPicturePlane(rv, s_actualViewportBufferForReuse);
    }
  }

  @Override
  public final ClippedZPlane getActualPicturePlane(OrthographicCamera orthographicCamera) {
    return getActualPicturePlane(new ClippedZPlane(), orthographicCamera);
  }

  private ClippedZPlane getActualPicturePlane(ClippedZPlane rv, FrustumPerspectiveCamera frustumPerspectiveCamera) {
    synchronized (s_actualViewportBufferForReuse) {
      GlrFrustumPerspectiveCamera frustumPerspectiveCameraAdapter = AdapterFactory.getAdapterFor(frustumPerspectiveCamera);
      getActualViewport(s_actualViewportBufferForReuse, frustumPerspectiveCameraAdapter);
      return frustumPerspectiveCameraAdapter.getActualPicturePlane(rv, s_actualViewportBufferForReuse);
    }
  }

  @Override
  public final ClippedZPlane getActualPicturePlane(FrustumPerspectiveCamera frustumPerspectiveCamera) {
    return getActualPicturePlane(new ClippedZPlane(), frustumPerspectiveCamera);
  }

  @Override
  public Angle getActualHorizontalViewingAngle(SymmetricPerspectiveCamera symmetricPerspectiveCamera) {
    synchronized (s_actualViewportBufferForReuse) {
      GlrSymmetricPerspectiveCamera symmetricPerspectiveCameraAdapter = AdapterFactory.getAdapterFor(symmetricPerspectiveCamera);
      getActualViewport(s_actualViewportBufferForReuse, symmetricPerspectiveCameraAdapter);
      return symmetricPerspectiveCameraAdapter.getActualHorizontalViewingAngle(s_actualViewportBufferForReuse);
    }
  }

  @Override
  public Angle getActualVerticalViewingAngle(SymmetricPerspectiveCamera symmetricPerspectiveCamera) {
    synchronized (s_actualViewportBufferForReuse) {
      GlrSymmetricPerspectiveCamera symmetricPerspectiveCameraAdapter = AdapterFactory.getAdapterFor(symmetricPerspectiveCamera);
      getActualViewport(s_actualViewportBufferForReuse, symmetricPerspectiveCameraAdapter);
      return symmetricPerspectiveCameraAdapter.getActualVerticalViewingAngle(s_actualViewportBufferForReuse);
    }
  }

  private Ray getRayAtPixel(Ray rv, int xPixel, int yPixel, AbstractCamera sgCamera) {
    if (sgCamera != null) {
      synchronized (s_actualViewportBufferForReuse) {
        GlrAbstractCamera<? extends AbstractCamera> cameraAdapter = AdapterFactory.getAdapterFor(sgCamera);
        getActualViewport(s_actualViewportBufferForReuse, cameraAdapter);
        //      double halfWidth = s_actualViewportBufferForReuse.width / 2.0;
        //      double halfHeight = s_actualViewportBufferForReuse.height / 2.0;
        //      double xInPlane = (xPixel + 0.5 - halfWidth) / halfWidth;
        //      double yInPlane = -(yPixel + 0.5 - halfHeight) / halfHeight;
        cameraAdapter.getRayAtPixel(rv, xPixel, yPixel, s_actualViewportBufferForReuse);
      }
    } else {
      rv.setNaN();
    }
    //    java.awt.Rectangle viewport = getActualViewport( camera );
    //    double halfWidth = viewport.width / 2.0;
    //    double halfHeight = viewport.height / 2.0;
    //    double x = (xPixel + 0.5 - halfWidth) / halfWidth;
    //    double y = -(yPixel + 0.5 - halfHeight) / halfHeight;
    //
    //    edu.cmu.cs.dennisc.math.Matrix4d inverseProjection = getActualProjectionMatrix( camera );
    //    inverseProjection.invert();
    //
    //    edu.cmu.cs.dennisc.math.Point3d origin = new edu.cmu.cs.dennisc.math.Point3d(
    //        inverseProjection.backward.x / inverseProjection.backward.w,
    //        inverseProjection.backward.y / inverseProjection.backward.w,
    //        inverseProjection.backward.z / inverseProjection.backward.w
    //    );
    //
    //    edu.cmu.cs.dennisc.math.Vector4d qs = new edu.cmu.cs.dennisc.math.Vector4d( x, y, 0, 1 );
    //    edu.cmu.cs.dennisc.math.Vector4d qw = edu.cmu.cs.dennisc.math.LinearAlgebra.multiply( qs, inverseProjection );
    //
    //    edu.cmu.cs.dennisc.math.Vector3d direction = new edu.cmu.cs.dennisc.math.Vector3d(
    //        qw.x * inverseProjection.backward.w - qw.w * inverseProjection.backward.x,
    //        qw.y * inverseProjection.backward.w - qw.w * inverseProjection.backward.y,
    //        qw.z * inverseProjection.backward.w - qw.w * inverseProjection.backward.z
    //    );
    //    direction.normalize();
    //
    //    rv.setOrigin( origin );
    //    rv.setDirection( direction );
    //    return rv;
    return rv;
  }

  @Override
  public final Ray getRayAtPixel(int xPixel, int yPixel, AbstractCamera sgCamera) {
    return getRayAtPixel(new Ray(), xPixel, yPixel, sgCamera);
  }

  private Ray getRayAtPixel(Ray rv, int xPixel, int yPixel) {
    return getRayAtPixel(rv, xPixel, yPixel, getCameraAtPixel(xPixel, yPixel));
  }

  @Override
  public final Ray getRayAtPixel(int xPixel, int yPixel) {
    return getRayAtPixel(new Ray(), xPixel, yPixel);
  }

  @Override
  public boolean isLetterboxedAsOpposedToDistorted(AbstractCamera sgCamera) {
    GlrAbstractCamera<? extends AbstractCamera> cameraAdapter = AdapterFactory.getAdapterFor(sgCamera);
    return cameraAdapter.isLetterboxedAsOpposedToDistorted();
  }

  @Override
  public void setLetterboxedAsOpposedToDistorted(AbstractCamera sgCamera, boolean isLetterboxedAsOpposedToDistorted) {
    GlrAbstractCamera<? extends AbstractCamera> cameraAdapter = AdapterFactory.getAdapterFor(sgCamera);
    cameraAdapter.setIsLetterboxedAsOpposedToDistorted(isLetterboxedAsOpposedToDistorted);
  }

  public double[] getActualPlane(double[] rv, Dimension size, OrthographicCamera orthographicCamera) {
    throw new RuntimeException("todo");
    //    OrthographicCameraAdapter orthographicCameraAdapter = ElementAdapter.getAdapterFor( orthographicCamera );
    //    return orthographicCameraAdapter.getActualPlane( rv, size );
  }

  public double[] getActualPlane(double[] rv, Dimension size, FrustumPerspectiveCamera perspectiveCamera) {
    throw new RuntimeException("todo");
    //    PerspectiveCameraAdapter perspectiveCameraAdapter = ElementAdapter.getAdapterFor( perspectiveCamera );
    //    return perspectiveCameraAdapter.getActualPlane( rv, size );
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
