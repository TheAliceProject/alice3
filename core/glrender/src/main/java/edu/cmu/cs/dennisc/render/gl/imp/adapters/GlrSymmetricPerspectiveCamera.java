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

package edu.cmu.cs.dennisc.render.gl.imp.adapters;

import edu.cmu.cs.dennisc.property.InstanceProperty;
import edu.cmu.cs.dennisc.render.gl.imp.Context;
import edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera;
import org.alice.math.immutable.Angle;
import org.alice.math.immutable.FullMatrix4x4;
import org.alice.math.immutable.Matrix4x4;
import org.alice.math.immutable.Point3;
import org.alice.math.immutable.Ray;
import org.alice.math.immutable.Vector4;

import java.awt.Rectangle;
import java.nio.DoubleBuffer;

/**
 * @author Dennis Cosgrove
 */
public class GlrSymmetricPerspectiveCamera extends GlrAbstractPerspectiveCamera<SymmetricPerspectiveCamera> {
  @Override
  public Ray getRayAtViewportPixel(int xPixel, int yPixel, Rectangle actualViewport) {
    // Camera forward is negative z, so these values are negative, in the camera's frame of reference.
    final double near = -owner.nearClippingPlaneDistance.getValue();
    final double far = -owner.farClippingPlaneDistance.getValue();

    // actualViewport.x or y are set > 0 when letterboxing

    // Proportional offset from center of viewport.
    // -1, -1 is the lower left corner
    // 0,0 is the center
    // 1, 1 is the upper right
    final double xOffset = 1.0 - ((2.0 * (xPixel - actualViewport.x)) / actualViewport.width);
    final double yOffset = 1.0 - ((2.0 * (yPixel - actualViewport.y)) / actualViewport.height);

    final double tanHalfVertical = Math.tan(getActualVerticalViewingAngle(actualViewport).getAsRadians() * 0.5);
    final double dx = xOffset * tanHalfVertical * getAspectRatio(actualViewport);
    final double dy = yOffset * tanHalfVertical;

    Point3 pNear = new Point3(dx * near, dy * near, near);
    Point3 pFar = new Point3(dx * far, dy * far, far);
    return Ray.fromAtoB(pNear, pFar);
  }

  @Override
  public Matrix4x4 getActualProjectionMatrix(Rectangle actualViewport) {
    double zNear = owner.nearClippingPlaneDistance.getValue();
    double zFar = owner.farClippingPlaneDistance.getValue();
    Angle fovX = getActualHorizontalViewingAngle(actualViewport);
    Angle fovY = getActualVerticalViewingAngle(actualViewport);
    double aspect = fovX.getAsRadians() / fovY.getAsRadians();
    double f = 1 / Math.tan(fovY.getAsRadians() / 2);
    owner.setEffectiveHorizontalViewingAngle(fovX);
    owner.setEffectiveVerticalViewingAngle(fovY);

    return new FullMatrix4x4(
        new Vector4(f / aspect, 0, 0, 0),
        new Vector4(0, f, 0, 0),
        new Vector4(0, 0, (zFar + zNear) / (zNear - zFar), -1),
        new Vector4(0, 0, (2 * zFar * zNear) / (zNear - zFar), 0));
  }

  @Override
  protected Rectangle performLetterboxing(Rectangle rect) {
    Rectangle rv = new Rectangle(rect);
    final double viewAspect = getAspectRatio(rv);
    double surfaceAspect = rv.width / (double) rv.height;
    if (viewAspect > surfaceAspect) {
      int letterBoxedHeight = (int) ((rv.width / viewAspect) + 0.5);
      rv.setBounds(0, (rv.height - letterBoxedHeight) / 2, rv.width, letterBoxedHeight);
    } else if (viewAspect < surfaceAspect) {
      int letterBoxedWidth = (int) ((rv.height * viewAspect) + 0.5);
      rv.setBounds((rv.width - letterBoxedWidth) / 2, 0, letterBoxedWidth, rv.height);
    } else {
      // aspect is the same, we don't have to change it
    }
    return rv;
  }

  private double getAspectRatio(Rectangle rect) {
    if (!horizontalView.isNaN() && !verticalView.isNaN()) {
      return horizontalView.getAsRadians() / verticalView.getAsRadians();
    }
    if (this.isLetterboxed()) {
      return SymmetricPerspectiveCamera.DEFAULT_WIDTH_TO_HEIGHT_RATIO;
    }
    return rect.width / (double) rect.height;
  }

  private Angle getActualHorizontalViewingAngle(Rectangle actualViewport) {
    if (!horizontalView.isNaN()) {
      return horizontalView;
    }
    Angle vertical = verticalView.isNaN() ? SymmetricPerspectiveCamera.DEFAULT_VERTICAL_VIEW_ANGLE : verticalView;
    return vertical.times(getAspectRatio(actualViewport));
  }

  private Angle getActualVerticalViewingAngle(Rectangle actualViewport) {
    if (!verticalView.isNaN()) {
      return verticalView;
    }
    if (horizontalView.isNaN()) {
      return SymmetricPerspectiveCamera.DEFAULT_VERTICAL_VIEW_ANGLE;
    }
    return horizontalView.times(1 / getAspectRatio(actualViewport));
  }

  @Override
  protected void setupProjection(Context context, Rectangle actualViewport, float zNear, float zFar) {
    Matrix4x4 projection = getActualProjectionMatrix(actualViewport);
    double[] projectionArray = projection.asColumnMajorArray16();
    context.gl.glMultMatrixd(DoubleBuffer.wrap(projectionArray));
  }

  @Override
  protected void propertyChanged(InstanceProperty<?> property) {
    if (property == owner.verticalViewingAngle) {
      this.verticalView = owner.verticalViewingAngle.getValue();
    } else if (property == owner.horizontalViewingAngle) {
      this.horizontalView = owner.horizontalViewingAngle.getValue();
    } else {
      super.propertyChanged(property);
    }
  }

  private Angle verticalView;
  private Angle horizontalView;
}
