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

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.ImmModeSink;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Ray;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.property.InstanceProperty;
import edu.cmu.cs.dennisc.render.gl.imp.Context;
import edu.cmu.cs.dennisc.render.gl.imp.PickContext;
import edu.cmu.cs.dennisc.render.gl.imp.RenderContext;
import edu.cmu.cs.dennisc.scenegraph.Sphere;

/**
 * @author Dennis Cosgrove
 */
public class GlrSphere extends GlrShape<Sphere> {
  //todo: add scenegraph hint
  private static final int STACK_COUNT = 50;
  private static final int SLICE_COUNT = 50;

  private void glSphere(Context c, boolean textureFlag) {
    GL2 gl = c.gl;
    drawSphereCustomize(gl, textureFlag);
  }

  /**
   * Customized version of drawSphere with inverting image handling
   * @param gl
   * @param textureFlag
   */
  private void drawSphereCustomize(GL2 gl, boolean textureFlag) {
    double rho, drho, theta, dtheta;
    double x, y, z;
    double s, t, ds, dt;
    int i, j, imin, imax;
    boolean normals = true;
    double nsign = 1.0;

    drho = Math.PI / STACK_COUNT;
    dtheta = (Math.PI * 2) / SLICE_COUNT;

    if (!textureFlag) {
      gl.glBegin(GL.GL_TRIANGLE_FAN);
      gl.glNormal3d(0.0, 0.0, 1.0);
      gl.glVertex3d(0.0, 0.0, nsign * radius);
      for (j = 0; j <= SLICE_COUNT; j++) {
        theta = (j == SLICE_COUNT) ? 0.0 : j * dtheta;
        x = -Math.sin(theta) * Math.sin(drho);
        y = Math.cos(theta) * Math.sin(drho);
        z = nsign * Math.cos(drho);
        if (normals) {
          gl.glNormal3d(x * nsign, y * nsign, z * nsign);
        }
        gl.glVertex3d(x * radius, y * radius, z * radius);
      }
      gl.glEnd();
    }

    ds = 1.0 / SLICE_COUNT;
    dt = 1.0 / STACK_COUNT;
    t = 0.0;
    if (textureFlag) {
      imin = 0;
      imax = STACK_COUNT;
    } else {
      imin = 1;
      imax = STACK_COUNT - 1;
    }

    for (i = imax; i >= imin; i--) {
      rho = i * drho;
      gl.glBegin(ImmModeSink.GL_QUAD_STRIP);
      s = 1.0;
      for (j = SLICE_COUNT; j >= 0; j--) {
        theta = (j == SLICE_COUNT) ? 0.0 : j * dtheta;
        x = Math.sin(theta) * Math.sin(rho);
        y = -Math.cos(rho);
        z = Math.cos(theta) * Math.sin(rho);
        gl.glNormal3d(x, y, z);
        if (textureFlag) {
          gl.glTexCoord2d(s, t);
        }
        gl.glVertex3d(x * radius, y * radius, z * radius);
        x = Math.sin(theta) * Math.sin(rho + drho);
        y = -Math.cos(rho + drho);
        z = Math.cos(theta) * Math.sin(rho + drho);
        gl.glNormal3d(x, y, z);
        if (textureFlag) {
          gl.glTexCoord2d(s, t - dt);
        }
        s -= ds;
        gl.glVertex3d(x * radius, y * radius, z * radius);
      }
      gl.glEnd();
      t += dt;
    }

    if (!textureFlag) {
      gl.glBegin(GL.GL_TRIANGLE_FAN);
      gl.glNormal3d(0.0, 0.0, -1.0);
      gl.glVertex3d(0.0, 0.0, -radius * nsign);
      rho = Math.PI - drho;
      s = 1.0;
      for (j = SLICE_COUNT; j >= 0; j--) {
        theta = (j == SLICE_COUNT) ? 0.0 : j * dtheta;
        x = -Math.sin(theta) * Math.sin(rho);
        y = Math.cos(theta) * Math.sin(rho);
        z = nsign * Math.cos(rho);
        if (normals) {
          gl.glNormal3d(x * nsign, y * nsign, z * nsign);
        }
        s -= ds;
        gl.glVertex3d(x * radius, y * radius, z * radius);
      }
      gl.glEnd();
    }
  }

  @Override
  protected void renderGeometry(RenderContext rc, GlrVisual.RenderType renderType) {
    boolean isTextureEnabled = rc.isTextureEnabled();
    rc.glu.gluQuadricTexture(rc.getQuadric(), isTextureEnabled);
    glSphere(rc, isTextureEnabled);
  }

  @Override
  public Point3 getIntersectionInSource(Point3 rv, Ray ray, AffineMatrix4x4 m, int subElement) {
    //assuming ray unit length
    Point3 origin = new Point3(0, 0, 0);
    Vector3 dst = Vector3.createSubtraction(ray.accessOrigin(), origin);
    double b = Vector3.calculateDotProduct(dst, ray.accessDirection());
    double c = Vector3.calculateDotProduct(dst, dst) - this.radius;
    double d = (b * b) - c;
    if (d > 0) {
      double t = -b - Math.sqrt(d);
      ray.getPointAlong(rv, t);
    } else {
      rv.setNaN();
    }
    return rv;
  }

  @Override
  protected void pickGeometry(PickContext pc, boolean isSubElementRequired) {
    int name;
    if (isSubElementRequired) {
      name = 0;
    } else {
      name = -1;
    }
    pc.gl.glPushName(name);
    glSphere(pc, true);
    pc.gl.glPopName();
  }

  @Override
  protected void propertyChanged(InstanceProperty<?> property) {
    if (property == owner.radius) {
      this.radius = owner.radius.getValue();
      setIsGeometryChanged(true);
    } else {
      super.propertyChanged(property);
    }
  }

  private double radius;
}
