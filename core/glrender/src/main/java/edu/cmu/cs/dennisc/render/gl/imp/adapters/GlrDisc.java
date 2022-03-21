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

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.math.FloatUtil;
import com.jogamp.opengl.util.ImmModeSink;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Ray;
import edu.cmu.cs.dennisc.property.InstanceProperty;
import edu.cmu.cs.dennisc.render.gl.imp.Context;
import edu.cmu.cs.dennisc.render.gl.imp.PickContext;
import edu.cmu.cs.dennisc.render.gl.imp.RenderContext;
import edu.cmu.cs.dennisc.scenegraph.Disc;

/**
 * @author Dennis Cosgrove
 */
public class GlrDisc extends GlrShape<Disc> {
  //todo: add scenegraph hint
  private static final int SLICE_COUNT = 50;
  private static final int LOOP_COUNT = 1;
  private static final float TAU = 2f * FloatUtil.PI;

  private void glDisc(Context c, boolean isTextureEnabled) {
    double innerRadius = owner.innerRadius.getValue();
    double outerRadius = owner.outerRadius.getValue();
    c.gl.glPushMatrix();
    try {
      Disc.Axis axis = owner.axis.getValue();
      if (axis == Disc.Axis.X) {
        c.gl.glRotated(90.0, 0.0, 1.0, 0.0);
      } else if (axis == Disc.Axis.Y) {
        c.gl.glRotated(90.0, 1.0, 0.0, 0.0);
      }
      if (owner.isFrontFaceVisible.getValue()) {
        gluDisk(c.gl, innerRadius, outerRadius, isTextureEnabled);
      }
      if (owner.isBackFaceVisible.getValue()) {
        c.gl.glRotated(180.0, 0.0, 1.0, 0.0);
        gluDisk(c.gl, innerRadius, outerRadius, isTextureEnabled);
      }
    } finally {
      c.gl.glPopMatrix();
    }
  }

  private static void gluDisk(GL2 gl, double innerRadius, double outerRadius, boolean isTextureEnabled) {
    // Use largest disk in texture
    gluDisk(gl, innerRadius, outerRadius, isTextureEnabled, 0.5F, 0.5F, 1.0F);
  }

  // Inspired by GLUquadricImpl.drawDisk() for the limited case of GLU_OUTSIDE, GLU_FILL, and having normals.
  // The texture application has been flipped in T so images appear forward
  // Added the texture center and portion to select a specific part of the texture
  static void gluDisk(GL2 gl, double innerRadius, double outerRadius, boolean isTextureEnabled, float textureCenterS, float textureCenterT, float texturePortion) {
    gl.getGL2().glNormal3f(0.0f, 0.0f, +1.0f);

    float da = TAU / GlrDisc.SLICE_COUNT;
    float dr = (float) ((outerRadius - innerRadius) / GlrDisc.LOOP_COUNT);
    final float dtc = (float) (2.0 * outerRadius) / texturePortion;
    float sa, ca;
    float r1 = (float) innerRadius;
    for (int ring = 0; ring < GlrDisc.LOOP_COUNT; ring++) {
      final float r2 = r1 + dr;
      gl.getGL2().glBegin(ImmModeSink.GL_QUAD_STRIP);
      for (var s = 0; s <= GlrDisc.SLICE_COUNT; s++) {
        float a = s == GlrDisc.SLICE_COUNT ? 0.0f : s * da;
        sa = (float) Math.sin(a);
        ca = (float) Math.cos(a);
        if (isTextureEnabled) {
          gl.getGL2().glTexCoord2f(textureCenterS + sa * r2 / dtc, textureCenterT - ca * r2 / dtc);
        }
        gl.getGL2().glVertex2f(r2 * sa, r2 * ca);
        if (isTextureEnabled) {
          gl.getGL2().glTexCoord2f(textureCenterS + sa * r1 / dtc, textureCenterT - ca * r1 / dtc);
        }
        gl.getGL2().glVertex2f(r1 * sa, r1 * ca);
      }
      gl.getGL2().glEnd();
      r1 = r2;
    }
  }

  @Override
  protected void renderGeometry(RenderContext rc, GlrVisual.RenderType renderType) {
    glDisc(rc, rc.isTextureEnabled());
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
    glDisc(pc, false);
    pc.gl.glPopName();
  }

  @Override
  public Point3 getIntersectionInSource(Point3 rv, Ray ray, AffineMatrix4x4 m, int subElement) {
    return GlrGeometry.getIntersectionInSourceFromPlaneInLocal(rv, ray, m, 0, 0, 0, 0, 1, 0);
  }

  @Override
  protected void propertyChanged(InstanceProperty<?> property) {
    if (property == owner.innerRadius) {
      markGeometryAsChanged();
    } else if (property == owner.outerRadius) {
      markGeometryAsChanged();
    } else if (property == owner.isFrontFaceVisible) {
      markGeometryAsChanged();
    } else if (property == owner.isBackFaceVisible) {
      markGeometryAsChanged();
    } else {
      super.propertyChanged(property);
    }
  }
}
