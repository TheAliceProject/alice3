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

import edu.cmu.cs.dennisc.math.ClippedZPlane;
import org.alice.math.immutable.FixedRectangle;
import edu.cmu.cs.dennisc.property.InstanceProperty;
import edu.cmu.cs.dennisc.render.gl.imp.Context;
import edu.cmu.cs.dennisc.scenegraph.FrustumPerspectiveCamera;
import org.alice.math.immutable.FullMatrix4x4;
import org.alice.math.immutable.Matrix4x4;
import org.alice.math.immutable.Ray;
import org.alice.math.immutable.Vector4;

import java.awt.Rectangle;

/**
 * @author Dennis Cosgrove
 */
public class GlrFrustumPerspectiveCamera extends GlrAbstractPerspectiveCamera<FrustumPerspectiveCamera> {
  @Override
  public Ray getRayAtPixel(int xPixel, int yPixel, Rectangle actualViewport) {
    throw new RuntimeException("TODO");
  }

  @Override
  public Matrix4x4 getActualProjectionMatrix(Rectangle actualViewport) {
    ClippedZPlane pp = getActualPicturePlane(actualViewport);
    double left = pp.getXMinimum();
    double right = pp.getXMaximum();
    double bottom = pp.getYMinimum();
    double top = pp.getYMaximum();

    double zNear = owner.nearClippingPlaneDistance.getValue();
    double zFar = owner.farClippingPlaneDistance.getValue();

    return new FullMatrix4x4(
        new Vector4(2 * zNear, 0, 0, 0),
        new Vector4(0, (2 * zNear) / (top - bottom), 0, 0),
        new Vector4((right + left) / (right - left), (top + bottom) / (top - bottom), -(zFar + zNear) / (zFar + zNear), -1),
        new Vector4(0, 0, -(2 * zFar * zNear) / (zFar - zNear), 0));
  }

  @Override
  protected Rectangle performLetterboxing(Rectangle rect) {
    //todo: handle NaN
    return rect;
  }

  public ClippedZPlane getActualPicturePlane(Rectangle actualViewport) {
    return new ClippedZPlane(owner.picturePlane.getValue(), FixedRectangle.fromRectangle(actualViewport));
  }

  @Override
  protected void setupProjection(Context context, Rectangle actualViewport, float near, float far) {
    ClippedZPlane pp = getActualPicturePlane(actualViewport);
    context.gl.glFrustum(pp.getXMinimum(), pp.getXMaximum(), pp.getYMinimum(), pp.getYMaximum(), near, far);
  }

  @Override
  protected void propertyChanged(InstanceProperty<?> property) {
    if (property == owner.picturePlane) {
      //pass
    } else {
      super.propertyChanged(property);
    }
  }
}
