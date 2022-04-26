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
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Ray;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.property.InstanceProperty;
import edu.cmu.cs.dennisc.render.gl.imp.Context;
import edu.cmu.cs.dennisc.render.gl.imp.PickContext;
import edu.cmu.cs.dennisc.render.gl.imp.RenderContext;
import edu.cmu.cs.dennisc.scenegraph.Torus;

import static com.jogamp.opengl.GL2.GL_QUAD_STRIP;

/**
 * @author Dennis Cosgrove
 */
public class GlrTorus extends GlrShape<Torus> {
  final double PI = Math.PI;
  final double TAU = 2 * PI;

  private void drawTorusCustomize(Context c, double majorRadius, double minorRadius, int nsides, int nrings) {
    for (int i = nrings - 1; i >= 0; i--) {
      c.gl.glBegin(GL_QUAD_STRIP);
      for (int j = 0; j < nsides + 1; j++) {
        for (int k = 1; k >= 0; k--) {
          double s, t, x, y, z, u, v;
          double nx, ny, nz;
          double sinRings, cosRings, sinSides, cosSides, dist;

          s = (i + k) % nrings + 0.5;
          t = j % (nsides + 1);
          sinRings = -Math.sin((s * TAU) / nrings);
          cosRings = -Math.cos((s * TAU) / nrings);
          sinSides = -Math.sin((t * TAU) / nsides);
          cosSides = -Math.cos((t * TAU) / nsides);
          dist = majorRadius + minorRadius * cosRings;

          x = dist * sinSides;
          y = minorRadius * -sinRings;
          z = -dist * cosSides;
          u = (i + k) / (float) nrings;
          v = -t / (float) nsides;
          nx = -sinSides * cosRings;
          ny = -sinRings;
          nz = -cosSides * cosRings;

          c.gl.glTexCoord2d(v, u);
          if (c.isLightingEnabled()) {
            normal3d(c.gl, nx, ny, nz);
          }
          c.gl.glVertex3d(x, y, z);
        }
      }
      c.gl.glEnd();
    }
  }

  private void normal3d(final GL gl, double x, double y, double z) {
    double mag = Math.sqrt(x * x + y * y + z * z);
    if (mag > 0.00001F) {
      x /= mag;
      y /= mag;
      z /= mag;
    }
    gl.getGL2().glNormal3d(x, y, z);
  }

  private void glTorus(Context context) {
    double majorRadius = this.owner.majorRadius.getValue();
    double minorRadius = this.owner.minorRadius.getValue();
    int sides = 32;
    int rings = 16;
    drawTorusCustomize(context, majorRadius, minorRadius, sides, rings);
  }

  @Override
  protected void renderGeometry(RenderContext rc, GlrVisual.RenderType renderType) {
    glTorus(rc);
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
    glTorus(pc);
    pc.gl.glPopName();
  }

  @Override
  public Point3 getIntersectionInSource(Point3 rv, Ray ray, AffineMatrix4x4 m, int subElement) {
    //todo: solve for intersection with actual torus as opposed to just the plane
    Vector3 direction = new Vector3(0, 0, 0);
    Torus.CoordinatePlane coordinatePlane = this.owner.coordinatePlane.getValue();
    if (coordinatePlane == Torus.CoordinatePlane.XY) {
      direction.z = 1;
    } else if (coordinatePlane == Torus.CoordinatePlane.YZ) {
      direction.x = 1;
    } else {
      direction.y = 1;
    }
    return GlrGeometry.getIntersectionInSourceFromPlaneInLocal(rv, ray, m, 0, 0, 0, direction.x, direction.y, direction.z);
  }

  @Override
  protected void propertyChanged(InstanceProperty<?> property) {
    if (property == owner.majorRadius) {
      markGeometryAsChanged();
    } else if (property == owner.minorRadius) {
      markGeometryAsChanged();
    } else if (property == owner.coordinatePlane) {
      markGeometryAsChanged();
    } else {
      super.propertyChanged(property);
    }
  }
}
