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
import edu.cmu.cs.dennisc.math.immutable.MRectangleI;
import edu.cmu.cs.dennisc.pattern.AbstractReleasable;
import edu.cmu.cs.dennisc.render.*;
import edu.cmu.cs.dennisc.render.event.RenderTargetListener;
import edu.cmu.cs.dennisc.render.gl.imp.RenderTargetImp;
import edu.cmu.cs.dennisc.render.gl.imp.adapters.AdapterFactory;
import edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrAbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.AbstractNearPlaneAndFarPlaneCamera;
import org.alice.math.immutable.Matrix4x4;
import org.alice.math.immutable.Point3;
import org.alice.math.immutable.Ray;
import org.alice.math.immutable.Vector4;

import java.awt.*;
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

  @Override
  public final int getSurfaceWidth() {
    return getSurfaceSize().width;
  }

  @Override
  public final int getSurfaceHeight() {
    return getSurfaceSize().height;
  }

  @Override
  public final MRectangleI getActualViewport(AbstractCamera camera) {
    GlrAbstractCamera<?> cameraAdapter = AdapterFactory.getAdapterFor(camera);
    return RectangleUtilities.toMRectangleI(getActualViewportFromAdapter(cameraAdapter));
  }

  private Rectangle getActualViewportFromAdapter(GlrAbstractCamera<? extends AbstractCamera> cameraAdapter) {
    Dimension surfaceSize = this.getSurfaceSize();
    return cameraAdapter.getActualViewport(surfaceSize.width, surfaceSize.height);
  }

  @Override
  public final Rectangle getActualViewportAsAwtRectangle(AbstractCamera camera) {
    GlrAbstractCamera<?> cameraAdapter = AdapterFactory.getAdapterFor(camera);
    return getActualViewportFromAdapter(cameraAdapter);
  }

  @Override
  public Matrix4x4 getActualProjectionMatrix(AbstractCamera camera) {
    GlrAbstractCamera<? extends AbstractCamera> cameraAdapter = AdapterFactory.getAdapterFor(camera);
    final Rectangle viewport = getActualViewportFromAdapter(cameraAdapter);
    return cameraAdapter.getActualProjectionMatrix(viewport);
  }

  @Override
  public final Ray getRayAtPixel(int xPixel, int yPixel, AbstractCamera sgCamera) {
    if (sgCamera != null) {
      GlrAbstractCamera<? extends AbstractCamera> cameraAdapter = AdapterFactory.getAdapterFor(sgCamera);
      final Rectangle viewport = getActualViewportFromAdapter(cameraAdapter);
      return cameraAdapter.getRayAtPixel(xPixel, yPixel, viewport);
    }
    return Ray.NaN;
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


  /**
   * These are functions related to transformations between different types of spaces that we have, eg. 2d <-> 3d
   * AWT has 0,0 in the upper left, and is what our ui mouse clicks, etc are using
   * the 'viewport' has it in the lower left
   *
   */

  private Vector4 transformFromAWTToViewport(Point p, double z, Rectangle actualViewport) {
    final int y = actualViewport.height - p.y;
    return new Vector4(p.x, y, z, 1);
  }

  private  Point transformFromViewportToAWT(Vector4 xyzw, Rectangle actualViewport) {
    final int x = (int) (xyzw.x() / xyzw.w());
    final int y = actualViewport.height - (int) (xyzw.y() / xyzw.w());
    return new Point(x, y);
  }

  private  double getNear(AbstractCamera sgCamera) {
    if (sgCamera instanceof AbstractNearPlaneAndFarPlaneCamera) {
      return ((AbstractNearPlaneAndFarPlaneCamera) sgCamera).nearClippingPlaneDistance.getValue();
    } else {
      //todo?
      return Double.NaN;
    }
  }

  private  double getFar(AbstractCamera sgCamera) {
    if (sgCamera instanceof AbstractNearPlaneAndFarPlaneCamera) {
      return ((AbstractNearPlaneAndFarPlaneCamera) sgCamera).farClippingPlaneDistance.getValue();
    } else {
      //todo?
      return Double.NaN;
    }
  }

  private Vector4 transformFromViewportToProjection_AffectReturnValuePassedIn(Vector4 viewportCoord, AbstractCamera sgCamera, Rectangle actualViewport) {
    Vector4 scaled = viewportCoord.dividedBy(viewportCoord.w());

    double x = (scaled.x() - actualViewport.x) / actualViewport.width;
    double y = (scaled.y() - actualViewport.y) / actualViewport.height;

    double zNear = getNear(sgCamera);
    double zFar = getFar(sgCamera);
    double z = (scaled.z() - zNear) / (zFar - zNear);

    return new Vector4((x * 2.0) - 1.0, (y * 2.0) - 1.0, (z * 2.0) - 1.0, 1.0);
  }

  private Vector4 transformFromProjectionToViewport_AffectReturnValuePassedIn(Vector4 projectionCoord, AbstractCamera sgCamera, Rectangle actualViewport) {
    Vector4 scaled = projectionCoord.dividedBy(projectionCoord.w());

    double x = (scaled.x() * 0.5) + 0.5;
    double y = (scaled.y() * 0.5) + 0.5;
    double z = (scaled.z() * 0.5) + 0.5;

    x = (x * actualViewport.width) + actualViewport.x;
    y = (y * actualViewport.height) + actualViewport.y;
    double zNear = getNear(sgCamera);
    double zFar = getFar(sgCamera);
    z = (z * (zFar - zNear)) + zNear;

    return new Vector4(x, y, z, 1.0);
  }

  private  Vector4 transformFromProjectionToCamera_AffectReturnValuePassedIn(Vector4 projectionCoord, AbstractCamera sgCamera) {
    Vector4 scaled = projectionCoord.dividedBy(projectionCoord.w());
    return getActualProjectionMatrix(sgCamera).invert().transform(scaled);
  }

  private  Vector4 transformFromCameraToProjection_AffectReturnValuePassedIn(Vector4 camCoord, AbstractCamera sgCamera) {
    Vector4 scaled = camCoord.dividedBy(camCoord.w());
    return getActualProjectionMatrix(sgCamera).transform(scaled);
  }

  private  Vector4 transformFromViewportToCamera_AffectReturnValuePassedIn(Vector4 viewportCoord, AbstractCamera sgCamera, Rectangle actualViewport) {
    Vector4 projectionCoord = transformFromViewportToProjection_AffectReturnValuePassedIn(viewportCoord, sgCamera, actualViewport);
    return transformFromProjectionToCamera_AffectReturnValuePassedIn(projectionCoord, sgCamera);
  }

  private  Vector4 transformFromCameraToViewport_AffectReturnValuePassedIn(Vector4 camCoord, AbstractCamera sgCamera, Rectangle actualViewport) {
    Vector4 projectionCoord = transformFromCameraToProjection_AffectReturnValuePassedIn(camCoord, sgCamera);
    return transformFromProjectionToViewport_AffectReturnValuePassedIn(projectionCoord, sgCamera, actualViewport);
  }

  public Vector4 transformFromViewportToCamera(Vector4 xyzw, AbstractCamera sgCamera) {
    final Rectangle actualViewport = getActualViewportAsAwtRectangle(sgCamera);
    return transformFromViewportToCamera_AffectReturnValuePassedIn(xyzw,  sgCamera, actualViewport);
  }

  public Vector4 transformFromCameraToViewport(Vector4 xyzw, AbstractCamera sgCamera) {
    final Rectangle actualViewport = getActualViewportAsAwtRectangle(sgCamera);
    return transformFromCameraToViewport_AffectReturnValuePassedIn(xyzw,  sgCamera, actualViewport);
  }

  public Point transformFromCameraToAWT(Vector4 xyzw, AbstractCamera sgCamera) {
    final Rectangle actualViewport = getActualViewportAsAwtRectangle(sgCamera);
    Vector4 viewportCoord = transformFromCameraToViewport_AffectReturnValuePassedIn(xyzw,  sgCamera, actualViewport);
    return transformFromViewportToAWT(viewportCoord, actualViewport);
  }

  public  Point transformFromCameraToAWT(Point3 xyzw, AbstractCamera sgCamera) {
    return transformFromCameraToAWT(new Vector4(xyzw.x(), xyzw.y(), xyzw.z(), 1.0), sgCamera);
  }
  private final GlrRenderFactory glrRenderer;
  private final RenderCapabilities requestedCapabilities;
  private final RenderCapabilities actualCapabilities;

  protected final RenderTargetImp imp = new RenderTargetImp(this);

  private String m_description = new String();

  public abstract GLAutoDrawable getGLAutoDrawable();

  private boolean m_isRenderingEnabled = true;

}
