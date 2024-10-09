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
package edu.cmu.cs.dennisc.render;

import edu.cmu.cs.dennisc.math.immutable.MRectangleI;
import edu.cmu.cs.dennisc.render.event.RenderTargetListener;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import org.alice.math.immutable.Point3;
import org.alice.math.immutable.Ray;
import org.alice.math.immutable.Vector4;

import java.awt.*;
import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public interface RenderTarget {
  RenderFactory getRenderFactory();

  RenderCapabilities getActualCapabilities();

  int getSurfaceWidth();

  int getSurfaceHeight();

  Dimension getSurfaceSize();

  void addSgCamera(AbstractCamera sgCamera);

  void removeSgCamera(AbstractCamera sgCamera);

  void clearSgCameras();

  List<AbstractCamera> getSgCameras();

  AbstractCamera getSgCameraAt(int index);

  int getSgCameraCount();

  void addRenderTargetListener(RenderTargetListener listener);

  void removeRenderTargetListener(RenderTargetListener listener);

  AbstractCamera getCameraAtPixel(int xPixel, int yPixel);

  Ray getRayAtPixel(int xPixel, int yPixel, AbstractCamera sgCamera);

  boolean isLetterboxed(AbstractCamera sgCamera);

  void setLetterboxed(AbstractCamera sgCamera, boolean isLetterboxed);

  boolean isRenderingEnabled();

  void setRenderingEnabled(boolean isRenderingEnabled);

  SynchronousPicker getSynchronousPicker();

  AsynchronousPicker getAsynchronousPicker();

  SynchronousImageCapturer getSynchronousImageCapturer();

  AsynchronousImageCapturer getAsynchronousImageCapturer();

  void forgetAllCachedItems();

  void clearUnusedTextures();

  //todo: remove?
  org.alice.math.immutable.Matrix4x4 getActualProjectionMatrix(AbstractCamera sgCamera);

  MRectangleI getActualViewport(AbstractCamera sgCamera);

  Rectangle getActualViewportAsAwtRectangle(AbstractCamera sgCamera);

  Vector4 transformFromViewportToCamera(Vector4 xyzw, AbstractCamera sgCamera);
  Vector4 transformFromCameraToViewport(Vector4 xyzw, AbstractCamera sgCamera);


  // AWT (yes, java's ancient ui code) transformations are used by aabb collision, isInView, and speech/thought bubbles
  // where we need to know what's actually showing in the ui
  Point transformFromCameraToAWT(Vector4 xyzw, AbstractCamera sgCamera);
  Point transformFromCameraToAWT(Point3 xyzw, AbstractCamera sgCamera);
}
