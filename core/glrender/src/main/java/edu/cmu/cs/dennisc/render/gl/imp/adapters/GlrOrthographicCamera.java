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

import edu.cmu.cs.dennisc.java.awt.RectangleUtilities;
import edu.cmu.cs.dennisc.math.ClippedZPlane;
import edu.cmu.cs.dennisc.math.Matrix4x4;
import edu.cmu.cs.dennisc.math.Ray;
import edu.cmu.cs.dennisc.property.InstanceProperty;
import edu.cmu.cs.dennisc.render.gl.imp.Context;
import edu.cmu.cs.dennisc.scenegraph.OrthographicCamera;

import java.awt.Rectangle;

/**
 * @author Dennis Cosgrove
 */
public class GlrOrthographicCamera extends GlrAbstractNearPlaneAndFarPlaneCamera<OrthographicCamera> {

  @Override
  public Ray getRayAtPixel(int xPixel, int yPixel, Rectangle actualViewport) {
    ClippedZPlane pp = getActualPicturePlane(actualViewport);
    double left = pp.getXMinimum();
    double right = pp.getXMaximum();
    double bottom = pp.getYMinimum();
    double top = pp.getYMaximum();
    double near = owner.nearClippingPlaneDistance.getValue();

    //Pixels are relative to the top of the screen, but the "up" vector is bottom relative. Make the yPixel value bottom relative
    yPixel = actualViewport.height - yPixel;

    double xPortion = (xPixel - actualViewport.x) / (double) actualViewport.width;
    double yPortion = (yPixel - actualViewport.y) / (double) actualViewport.height;

    double x = left + ((right - left) * xPortion);
    double y = bottom + ((top - bottom) * yPortion);
    double z = near;

    Ray rv = new Ray();
    rv.setOrigin(x, y, z);
    rv.setDirection(0, 0, -1);
    return rv;
  }

  @Override
  public Matrix4x4 getActualProjectionMatrix(Rectangle actualViewport) {
    ClippedZPlane pp = getActualPicturePlane(actualViewport);
    double left = pp.getXMinimum();
    double right = pp.getXMaximum();
    double bottom = pp.getYMinimum();
    double top = pp.getYMaximum();
    double near = owner.nearClippingPlaneDistance.getValue();
    double far = owner.farClippingPlaneDistance.getValue();

    Matrix4x4 rv = new Matrix4x4();
    rv.setIdentity();

    rv.right.x = 2 / (right - left);
    rv.up.y = 2 / (top - bottom);
    rv.backward.z = -2 / (far - near);

    rv.translation.x = -(right + left) / (right - left);
    rv.translation.y = -(top + bottom) / (top - bottom);
    rv.translation.z = -(far + near) / (far - near);

    return rv;
  }

  @Override
  protected Rectangle performLetterboxing(Rectangle rect) {
    //todo: handle NaN
    return rect;
  }

  public final ClippedZPlane getActualPicturePlane(Rectangle actualViewport) {
    return new ClippedZPlane(owner.picturePlane.getValue(), RectangleUtilities.toMRectangleI(actualViewport));
  }

  @Override
  protected void setupProjection(Context context, Rectangle actualViewport, float near, float far) {
    ClippedZPlane pp = getActualPicturePlane(actualViewport);
    context.gl.glOrtho(pp.getXMinimum(), pp.getXMaximum(), pp.getYMinimum(), pp.getYMaximum(), near, far);
  }

  @Override
  protected void propertyChanged(InstanceProperty<?> property) {
    if (property != owner.picturePlane) {
      super.propertyChanged(property);
    }
  }
}
