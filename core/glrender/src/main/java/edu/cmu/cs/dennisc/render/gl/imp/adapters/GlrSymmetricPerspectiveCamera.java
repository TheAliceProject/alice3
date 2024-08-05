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

import edu.cmu.cs.dennisc.math.Angle;
import edu.cmu.cs.dennisc.math.AngleInDegrees;
import edu.cmu.cs.dennisc.math.AngleInRadians;
import edu.cmu.cs.dennisc.math.Matrix4x4;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Ray;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.property.InstanceProperty;
import edu.cmu.cs.dennisc.render.gl.imp.Context;
import edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera;

import java.awt.Rectangle;
import java.nio.DoubleBuffer;

/**
 * @author Dennis Cosgrove
 */
public class GlrSymmetricPerspectiveCamera extends GlrAbstractPerspectiveCamera<SymmetricPerspectiveCamera> {
  @Override
  public Ray getRayAtPixel(int xPixel, int yPixel, Rectangle actualViewport) {
    final double near = owner.nearClippingPlaneDistance.getValue();
    final double far = owner.farClippingPlaneDistance.getValue();

    // xPixel and yPixel are given to us with 0, 0 in the upper left of the viewport, so flip y.
    // actualViewport.x & y are set > 0 when letterboxing
    final double xRatio = (xPixel - actualViewport.x) / (actualViewport.width * 0.5);
    final double yRatio = (actualViewport.height - yPixel - actualViewport.y) / (actualViewport.height * 0.5);
    final double tanHalfVertical = Math.tan(getActualVerticalViewingAngle(actualViewport).getAsRadians() * 0.5);
    final double dx = (1.0 - xRatio) * tanHalfVertical * getAspectRatio(actualViewport);
    final double dy = (1.0 - yRatio) * tanHalfVertical;

    //todo: optimize?
    Point3 pNear = new Point3(dx * near, dy * near, near);
    Point3 pFar = new Point3(dx * far, dy * far, far);

    Vector3 direction = Vector3.createSubtraction(pNear, pFar);
    //todo: remove?
    direction.normalize();

    return new Ray(pNear, direction);
  }

  @Override
  public Matrix4x4 getActualProjectionMatrix(Rectangle actualViewport) {
    double zNear = owner.nearClippingPlaneDistance.getValue();
    double zFar = owner.farClippingPlaneDistance.getValue();
    double fovx = getActualHorizontalViewingAngle(actualViewport).getAsRadians();
    double fovy = getActualVerticalViewingAngle(actualViewport).getAsRadians();
    double aspect = fovx / fovy;
    double f = 1 / Math.tan(fovy / 2);
    owner.setEffectiveHorizontalViewingAngle(new AngleInRadians(fovx));
    owner.setEffectiveVerticalViewingAngle(new AngleInRadians(fovy));

    Matrix4x4 rv = new Matrix4x4();
    rv.right.set(f / aspect, 0, 0, 0);
    rv.up.set(0, f, 0, 0);
    rv.backward.set(0, 0, (zFar + zNear) / (zNear - zFar), -1);
    rv.translation.set(0, 0, (2 * zFar * zNear) / (zNear - zFar), 0);
    return rv;
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
    if (Double.isNaN(this.horizontalInDegrees) || Double.isNaN(this.verticalInDegrees)) {
      if (this.isLetterboxed()) {
        return SymmetricPerspectiveCamera.DEFAULT_WIDTH_TO_HEIGHT_RATIO;
      } else {
        return rect.width / (double) rect.height;
      }
    } else {
      return this.horizontalInDegrees / this.verticalInDegrees;
    }
  }

  public Angle getActualHorizontalViewingAngle(Rectangle actualViewport) {
    double angle;
    if (Double.isNaN(this.horizontalInDegrees)) {
      double aspect = getAspectRatio(actualViewport);
      if (Double.isNaN(this.verticalInDegrees)) {
        angle = SymmetricPerspectiveCamera.DEFAULT_VERTICAL_VIEW_ANGLE.getAsDegrees() * aspect;
      } else {
        angle = this.verticalInDegrees * aspect;
      }
    } else {
      angle = this.horizontalInDegrees;
    }
    return new AngleInDegrees(angle);
  }

  public Angle getActualVerticalViewingAngle(Rectangle actualViewport) {
    double angle;
    if (Double.isNaN(this.verticalInDegrees)) {
      double aspect = getAspectRatio(actualViewport);
      if (Double.isNaN(this.horizontalInDegrees)) {
        angle = SymmetricPerspectiveCamera.DEFAULT_VERTICAL_VIEW_ANGLE.getAsDegrees();
      } else {
        angle = this.horizontalInDegrees / aspect;
      }
    } else {
      angle = this.verticalInDegrees;
    }
    return new AngleInDegrees(angle);
  }

  //  @Override
  //  protected void setupViewportAndProjection( PickContext pc, int x, int y, java.awt.Rectangle actualViewport, float zNear, float zFar ) {
  //    int yFlipped = actualViewport.height - y;
  //
  ////    double vertical = getActualVerticalViewingAngle( actualViewport, edu.cmu.cs.dennisc.math.UnitOfAngle.RADIANS );
  ////
  ////    double xInWindow = x;
  ////    //todo: account for actualViewport.x
  ////    xInWindow /= actualViewport.width;
  ////    xInWindow *= 2;
  ////    xInWindow -= 1;
  ////
  ////    double yInWindow = actualViewport.height - y;
  ////    //todo: account for actualViewport.y
  ////    yInWindow /= actualViewport.height;
  ////    yInWindow *= 2;
  ////    yInWindow -= 1;
  ////
  //////    xInWindow = 0.0;
  //////    yInWindow = 0.0;
  ////
  ////    edu.cmu.cs.dennisc.math.Matrix4d actualProjection = new edu.cmu.cs.dennisc.math.Matrix4d();
  ////    getActualProjectionMatrix( actualProjection, actualViewport );
  ////
  ////    edu.cmu.cs.dennisc.math.Vector4d xyzwNear = new edu.cmu.cs.dennisc.math.Vector4d();
  ////    xyzwNear.x = xInWindow;
  ////    xyzwNear.y = yInWindow;
  ////    xyzwNear.z = 0.0;
  ////    xyzwNear.w = 1.0;
  ////
  ////    actualProjection.invert();
  ////    actualProjection.transform( xyzwNear );
  ////
  //////
  //////    actualProjection.transform( xyzwNear );
  //////    xyzwNear.scale( 1/xyzwNear.w );
  ////
  //////    edu.cmu.cs.dennisc.math.Vector4d xyzwFar = new edu.cmu.cs.dennisc.math.Vector4d();
  //////    xyzwFar.x = xInWindow;
  //////    xyzwFar.y = yInWindow;
  //////    xyzwFar.z = zFar;
  //////    xyzwFar.w = 1.0;
  //////
  //////    actualProjection.transform( xyzwFar );
  ////
  ////    double aspect = actualViewport.width / (double)actualViewport.height;
  ////
  ////    double tanHalfVertical = Math.tan( vertical * 0.5 );
  ////    double halfHeightNear = tanHalfVertical * zNear;
  ////    double halfHeightPixelNear = halfHeightNear / actualViewport.height;
  ////
  ////    //double halfWidthNear = halfHeightNear * aspect;
  ////    double halfWidthPixelNear = halfHeightPixelNear * aspect;
  ////
  ////    double _x = xyzwNear.x / xyzwNear.w;
  ////    double _y = xyzwNear.y / xyzwNear.w;
  ////
  ////    System.err.println( _x + ", " + _y );
  ////    System.err.println( halfWidthPixelNear + ", " + halfHeightPixelNear );
  ////    System.err.println( zNear + " " + ( xyzwNear.z / xyzwNear.w ) );
  ////
  ////
  //
  //    double halfVertical = getActualVerticalViewingAngle( actualViewport, edu.cmu.cs.dennisc.math.UnitOfAngle.RADIANS ) * 0.5;
  //    double aspect = actualViewport.width / (double)actualViewport.height;
  //
  //    double halfVerticalTangent = Math.tan( halfVertical );
  //    double halfHeightPlaneNear = zNear * halfVerticalTangent;
  //    double halfHeightPixelNear = halfHeightPlaneNear / actualViewport.height;
  //
  //    double halfWidthPlaneNear = halfHeightPlaneNear * aspect;
  //    double halfWidthPixelNear = halfHeightPixelNear * aspect;
  //
  //    double left = -halfWidthPlaneNear  + ( x        * halfWidthPixelNear );
  //    double top  = -halfHeightPlaneNear + ( yFlipped * halfHeightPixelNear );
  //
  //    double right  = left + halfWidthPixelNear  + halfWidthPixelNear;
  //    double bottom = top  + halfHeightPixelNear + halfHeightPixelNear;
  //
  //    pc.gl.glFrustum( left, right, top, bottom, zNear, zFar );
  //    pc.gl.glViewport( x, yFlipped, 1, 1 );
  //  }

  @Override
  protected void setupProjection(Context context, Rectangle actualViewport, float zNear, float zFar) {

    double[] projectionArray = new double[16];
    DoubleBuffer projectionBuffer = DoubleBuffer.wrap(projectionArray);
    Matrix4x4 projection = getActualProjectionMatrix(actualViewport);
    projection.getAsColumnMajorArray16(projectionArray);
    context.gl.glMultMatrixd(projectionBuffer);
  }

  @Override
  protected void propertyChanged(InstanceProperty<?> property) {
    if (property == owner.verticalViewingAngle) {
      this.verticalInDegrees = owner.verticalViewingAngle.getValue().getAsDegrees();
    } else if (property == owner.horizontalViewingAngle) {
      this.horizontalInDegrees = owner.horizontalViewingAngle.getValue().getAsDegrees();
    } else {
      super.propertyChanged(property);
    }
  }

  private double verticalInDegrees;
  private double horizontalInDegrees;
}
