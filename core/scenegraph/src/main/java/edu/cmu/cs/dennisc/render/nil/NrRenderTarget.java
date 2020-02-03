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
package edu.cmu.cs.dennisc.render.nil;

import edu.cmu.cs.dennisc.java.awt.RectangleUtilities;
import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.math.Angle;
import edu.cmu.cs.dennisc.math.ClippedZPlane;
import edu.cmu.cs.dennisc.math.Matrix4x4;
import edu.cmu.cs.dennisc.math.Ray;
import edu.cmu.cs.dennisc.math.immutable.MRectangleI;
import edu.cmu.cs.dennisc.render.AsynchronousImageCapturer;
import edu.cmu.cs.dennisc.render.AsynchronousPicker;
import edu.cmu.cs.dennisc.render.RenderCapabilities;
import edu.cmu.cs.dennisc.render.RenderFactory;
import edu.cmu.cs.dennisc.render.RenderTarget;
import edu.cmu.cs.dennisc.render.SynchronousImageCapturer;
import edu.cmu.cs.dennisc.render.SynchronousPicker;
import edu.cmu.cs.dennisc.render.event.RenderTargetListener;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.FrustumPerspectiveCamera;
import edu.cmu.cs.dennisc.scenegraph.OrthographicCamera;
import edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Dennis Cosgrove
 */
/*package-private*/abstract class NrRenderTarget implements RenderTarget {
  public NrRenderTarget(RenderCapabilities requestedCapabilities) {
    this.requestedCapabilities = requestedCapabilities;
    this.actualCapabilities = new RenderCapabilities.Builder().build();
  }

  @Override
  public RenderCapabilities getRequestedCapabilities() {
    return null;
  }

  @Override
  public RenderCapabilities getActualCapabilities() {
    return null;
  }

  @Override
  public RenderFactory getRenderFactory() {
    return NilRenderFactory.INSTANCE;
  }

  @Override
  public int getSurfaceWidth() {
    return this.getSurfaceSize().width;
  }

  @Override
  public int getSurfaceHeight() {
    return this.getSurfaceSize().height;
  }

  @Override
  public int getDrawableWidth() {
    return this.getDrawableSize().width;
  }

  @Override
  public int getDrawableHeight() {
    return this.getDrawableSize().height;
  }

  @Override
  public String getDescription() {
    return this.description;
  }

  @Override
  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public void addSgCamera(AbstractCamera sgCamera) {
    this.sgCameras.add(sgCamera);
  }

  @Override
  public void removeSgCamera(AbstractCamera sgCamera) {
    this.sgCameras.remove(sgCamera);
  }

  @Override
  public void clearSgCameras() {
    this.sgCameras.clear();
  }

  @Override
  public List<AbstractCamera> getSgCameras() {
    return Collections.unmodifiableList(this.sgCameras);
  }

  @Override
  public AbstractCamera getSgCameraAt(int index) {
    return this.sgCameras.get(index);
  }

  @Override
  public int getSgCameraCount() {
    return this.sgCameras.size();
  }

  @Override
  public void addRenderTargetListener(RenderTargetListener listener) {
    this.renderTargetListeners.add(listener);
  }

  @Override
  public void removeRenderTargetListener(RenderTargetListener listener) {
    this.renderTargetListeners.remove(listener);
  }

  @Override
  public List<RenderTargetListener> getRenderTargetListeners() {
    return Collections.unmodifiableList(this.renderTargetListeners);
  }

  @Override
  public boolean isRenderingEnabled() {
    return this.isRenderingEnabled;
  }

  @Override
  public void setRenderingEnabled(boolean isRenderingEnabled) {
    this.isRenderingEnabled = isRenderingEnabled;
  }

  @Override
  public SynchronousPicker getSynchronousPicker() {
    return this.synchronousPicker;
  }

  @Override
  public AsynchronousPicker getAsynchronousPicker() {
    return this.asynchronousPicker;
  }

  @Override
  public SynchronousImageCapturer getSynchronousImageCapturer() {
    return this.synchronousImageCapturer;
  }

  @Override
  public AsynchronousImageCapturer getAsynchronousImageCapturer() {
    return this.asynchronousImageCapturer;
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
  public Rectangle getActualViewportAsAwtRectangle(Rectangle rv, AbstractCamera sgCamera) {
    Rectangle viewport = this.getSpecifiedViewportAsAwtRectangle(sgCamera);
    if (viewport != null) {
      rv.setBounds(viewport);
    } else {
      Dimension surfaceSize = this.getSurfaceSize();
      //todo: isLetterboxedAsOpposedToDistorted?
      rv.setBounds(0, 0, surfaceSize.width, surfaceSize.height);
    }
    return rv;
  }

  @Override
  public final Rectangle getActualViewportAsAwtRectangle(AbstractCamera sgCamera) {
    return this.getActualViewportAsAwtRectangle(new Rectangle(), sgCamera);
  }

  @Override
  public Rectangle getSpecifiedViewportAsAwtRectangle(AbstractCamera sgCamera) {
    return this.mapCameraToViewport.get(sgCamera);
  }

  @Override
  public void setSpecifiedViewportAsAwtRectangle(AbstractCamera sgCamera, Rectangle viewport) {
    this.mapCameraToViewport.put(sgCamera, viewport);
  }

  @Override
  public boolean isLetterboxedAsOpposedToDistorted(AbstractCamera sgCamera) {
    Boolean rv = this.mapCameraToIsLetterboxed.get(sgCamera);
    if (rv != null) {
      return rv;
    } else {
      return true;
    }
  }

  @Override
  public void setLetterboxedAsOpposedToDistorted(AbstractCamera sgCamera, boolean isLetterboxedAsOpposedToDistorted) {
    this.mapCameraToIsLetterboxed.put(sgCamera, isLetterboxedAsOpposedToDistorted);
  }

  @Override
  public AbstractCamera getCameraAtPixel(int xPixel, int yPixel) {
    //todo
    return null;
  }

  @Override
  public Ray getRayAtPixel(int xPixel, int yPixel, AbstractCamera sgCamera) {
    //todo
    return null;
  }

  @Override
  public final Ray getRayAtPixel(int xPixel, int yPixel) {
    return this.getRayAtPixel(xPixel, yPixel, this.getCameraAtPixel(xPixel, yPixel));
  }

  @Override
  public ClippedZPlane getActualPicturePlane(OrthographicCamera sgOrthographicCamera) {
    //todo
    return null;
  }

  @Override
  public ClippedZPlane getActualPicturePlane(FrustumPerspectiveCamera sgFrustumPerspectiveCamera) {
    //todo
    return null;
  }

  @Override
  public Angle getActualHorizontalViewingAngle(SymmetricPerspectiveCamera sgSymmetricPerspectiveCamera) {
    //todo
    return null;
  }

  @Override
  public Angle getActualVerticalViewingAngle(SymmetricPerspectiveCamera sgSymmetricPerspectiveCamera) {
    //todo
    return null;
  }

  @Override
  public Matrix4x4 getActualProjectionMatrix(Matrix4x4 rv, AbstractCamera sgCamera) {
    //todo
    return rv;
  }

  @Override
  public final Matrix4x4 getActualProjectionMatrix(AbstractCamera sgCamera) {
    return this.getActualProjectionMatrix(Matrix4x4.createNaN(), sgCamera);
  }

  @Override
  public void forgetAllCachedItems() {
  }

  @Override
  public void clearUnusedTextures() {
  }

  @Override
  public void release() {
  }

  private final RenderCapabilities requestedCapabilities;
  private final RenderCapabilities actualCapabilities;

  private final List<AbstractCamera> sgCameras = Lists.newCopyOnWriteArrayList();
  private final List<RenderTargetListener> renderTargetListeners = Lists.newCopyOnWriteArrayList();

  private final Map<AbstractCamera, Boolean> mapCameraToIsLetterboxed = Maps.newHashMap();
  private final Map<AbstractCamera, Rectangle> mapCameraToViewport = Maps.newHashMap();

  private final NrSynchronousPicker synchronousPicker = new NrSynchronousPicker();
  private final NrAsynchronousPicker asynchronousPicker = new NrAsynchronousPicker();
  private final NrSynchronousImageCapturer synchronousImageCapturer = new NrSynchronousImageCapturer();
  private final NrAsynchronousImageCapturer asynchronousImageCapturer = new NrAsynchronousImageCapturer();
  private String description;
  private boolean isRenderingEnabled = true;
}
