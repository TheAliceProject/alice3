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

import edu.cmu.cs.dennisc.math.Matrix4x4;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector4;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.AbstractNearPlaneAndFarPlaneCamera;

import java.awt.Point;
import java.awt.Rectangle;

/**
 * These are functions related to transformations between different types of spaces that we have, eg. 2d <-> 3d
 * AWT has 0,0 in the upper left, and is what our ui mouse clicks, etc are using
 * the 'viewport' has it in the lower left
 *
 * @author Dennis Cosgrove
 */
public class PicturePlaneUtils {
  private static Vector4 transformFromAWTToViewport(Point p, double z, Rectangle actualViewport) {
    final int y = actualViewport.height - p.y;
    return new Vector4(p.x, y, z, 1);
  }

  private static Point transformFromViewportToAWT(Vector4 xyzw, Rectangle actualViewport) {
    final int x = (int) (xyzw.x / xyzw.w);
    final int y = actualViewport.height - (int) (xyzw.y / xyzw.w);
    return new Point(x, y);
  }

  private static double getNear(AbstractCamera sgCamera) {
    if (sgCamera instanceof AbstractNearPlaneAndFarPlaneCamera) {
      return ((AbstractNearPlaneAndFarPlaneCamera) sgCamera).nearClippingPlaneDistance.getValue();
    } else {
      //todo?
      return Double.NaN;
    }
  }

  private static double getFar(AbstractCamera sgCamera) {
    if (sgCamera instanceof AbstractNearPlaneAndFarPlaneCamera) {
      return ((AbstractNearPlaneAndFarPlaneCamera) sgCamera).farClippingPlaneDistance.getValue();
    } else {
      //todo?
      return Double.NaN;
    }
  }

  private static Vector4 transformFromViewportToProjection_AffectReturnValuePassedIn(Vector4 rv, AbstractCamera sgCamera, Rectangle actualViewport) {
    rv.multiply(1.0 / rv.w);

    rv.x = (rv.x - actualViewport.x) / actualViewport.width;
    rv.y = (rv.y - actualViewport.y) / actualViewport.height;

    double zNear = getNear(sgCamera);
    double zFar = getFar(sgCamera);
    rv.z = (rv.z - zNear) / (zFar - zNear);

    rv.x = (rv.x * 2.0) - 1.0;
    rv.y = (rv.y * 2.0) - 1.0;
    rv.z = (rv.z * 2.0) - 1.0;

    return rv;
  }

  private static Vector4 transformFromProjectionToViewport_AffectReturnValuePassedIn(Vector4 rv, AbstractCamera sgCamera, Rectangle actualViewport) {
    rv.multiply(1.0 / rv.w);

    rv.x = (rv.x * 0.5) + 0.5;
    rv.y = (rv.y * 0.5) + 0.5;
    rv.z = (rv.z * 0.5) + 0.5;

    rv.x = (rv.x * actualViewport.width) + actualViewport.x;
    rv.y = (rv.y * actualViewport.height) + actualViewport.y;
    double zNear = getNear(sgCamera);
    double zFar = getFar(sgCamera);
    rv.z = (rv.z * (zFar - zNear)) + zNear;

    return rv;
  }

  private static Matrix4x4 s_actualProjectionBuffer = new Matrix4x4();

  private static Vector4 transformFromProjectionToCamera_AffectReturnValuePassedIn(Vector4 rv, RenderTarget renderTarget, AbstractCamera sgCamera) {
    rv.multiply(1.0 / rv.w);
    synchronized (s_actualProjectionBuffer) {
      renderTarget.getActualProjectionMatrix(s_actualProjectionBuffer, sgCamera);
      s_actualProjectionBuffer.invert();
      s_actualProjectionBuffer.transform(rv);
      return rv;
    }
  }

  private static Vector4 transformFromCameraToProjection_AffectReturnValuePassedIn(Vector4 rv, RenderTarget renderTarget, AbstractCamera sgCamera) {
    rv.multiply(1.0 / rv.w);
    synchronized (s_actualProjectionBuffer) {
      renderTarget.getActualProjectionMatrix(s_actualProjectionBuffer, sgCamera);
      s_actualProjectionBuffer.transform(rv);
      return rv;
    }
  }

  private static Vector4 transformFromViewportToCamera_AffectReturnValuePassedIn(Vector4 rv, RenderTarget renderTarget, AbstractCamera sgCamera, Rectangle actualViewport) {
    transformFromViewportToProjection_AffectReturnValuePassedIn(rv, sgCamera, actualViewport);
    transformFromProjectionToCamera_AffectReturnValuePassedIn(rv, renderTarget, sgCamera);
    return rv;
  }

  private static Vector4 transformFromCameraToViewport_AffectReturnValuePassedIn(Vector4 rv, RenderTarget renderTarget, AbstractCamera sgCamera, Rectangle actualViewport) {
    transformFromCameraToProjection_AffectReturnValuePassedIn(rv, renderTarget, sgCamera);
    transformFromProjectionToViewport_AffectReturnValuePassedIn(rv, sgCamera, actualViewport);
    return rv;
  }

  public static Vector4 transformFromViewportToCamera_AffectReturnValuePassedIn(Vector4 rv, RenderTarget renderTarget, AbstractCamera sgCamera) {
    final Rectangle actualViewport = renderTarget.getActualViewportAsAwtRectangle(sgCamera);
    transformFromViewportToCamera_AffectReturnValuePassedIn(rv, renderTarget, sgCamera, actualViewport);
    return rv;
  }

  public static Vector4 transformFromCameraToViewport_AffectReturnValuePassedIn(Vector4 rv, RenderTarget renderTarget, AbstractCamera sgCamera) {
    final Rectangle actualViewport = renderTarget.getActualViewportAsAwtRectangle(sgCamera);
    transformFromCameraToViewport_AffectReturnValuePassedIn(rv, renderTarget, sgCamera, actualViewport);
    return rv;
  }

  public static Vector4 transformFromViewportToCamera(Vector4 xyzw, RenderTarget renderTarget, AbstractCamera sgCamera) {
    Vector4 rv = new Vector4(xyzw);
    return transformFromViewportToCamera_AffectReturnValuePassedIn(rv, renderTarget, sgCamera);
  }

  public static Vector4 transformFromCameraToViewport(Vector4 xyzw, RenderTarget renderTarget, AbstractCamera sgCamera) {
    Vector4 rv = new Vector4(xyzw);
    return transformFromCameraToViewport_AffectReturnValuePassedIn(rv, renderTarget, sgCamera);
  }

  public static Point transformFromCameraToAWT(Vector4 xyzw, RenderTarget renderTarget, AbstractCamera sgCamera) {
    final Rectangle actualViewport = renderTarget.getActualViewportAsAwtRectangle(sgCamera);
    Vector4 viewportPos = new Vector4(xyzw);
    transformFromCameraToViewport_AffectReturnValuePassedIn(viewportPos, renderTarget, sgCamera, actualViewport);
    return transformFromViewportToAWT(viewportPos, actualViewport);
  }

  public static Point transformFromCameraToAWT(Point3 xyzw, RenderTarget renderTarget, AbstractCamera sgCamera) {
    return transformFromCameraToAWT(new Vector4(xyzw.x, xyzw.y, xyzw.z, 1.0), renderTarget, sgCamera);
  }


}
