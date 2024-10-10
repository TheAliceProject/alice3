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
import edu.cmu.cs.dennisc.scenegraph.Disc;
import org.alice.math.immutable.Matrix4x4;
import org.alice.math.immutable.Point3;
import org.alice.math.immutable.Ray;

/**
 * @author Dennis Cosgrove
 */
public class GlrDisc extends GlrShape<Disc> {

  @Override
  protected void shapeOnContext(Context context) {
    glDisc(context);
  }

  private void glDisc(Context c) {
    double innerRadius = owner.innerRadius.getValue();
    double outerRadius = owner.outerRadius.getValue();
    Disc.Axis axis = owner.axis.getValue();
    if (axis == Disc.Axis.X) {
      c.gl.glRotated(90.0, 0.0, 1.0, 0.0);
    } else if (axis == Disc.Axis.Y) {
      c.gl.glRotated(90.0, 1.0, 0.0, 0.0);
    }
    if (owner.isFrontFaceVisible.getValue()) {
      c.glDisk(innerRadius, outerRadius, 0.5F, 0.5F, 1.0F);
    }
    if (owner.isBackFaceVisible.getValue()) {
      c.gl.glRotated(180.0, 0.0, 1.0, 0.0);
      c.glDisk(innerRadius, outerRadius, 0.5F, 0.5F, 1.0F);
    }
  }

  @Override
  public Point3 getIntersectionInSource(Ray ray, Matrix4x4 m, int subElement) {
    return GlrGeometry.getIntersectionInSourceFromPlaneInLocal(ray, m, 0, 0, 0, 0, 1, 0);
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
