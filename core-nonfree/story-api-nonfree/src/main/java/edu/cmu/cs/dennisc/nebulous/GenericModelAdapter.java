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

package edu.cmu.cs.dennisc.nebulous;

import edu.cmu.cs.dennisc.render.gl.imp.PickContext;
import edu.cmu.cs.dennisc.render.gl.imp.RenderContext;
import edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrGeometry;
import edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrVisual;
import org.alice.math.immutable.Matrix4x4;
import org.alice.math.immutable.Point3;
import org.alice.math.immutable.Ray;
import org.alice.math.immutable.Vector3;
import org.alice.math.immutable.Vector4;

/**
 * @author Dennis Cosgrove
 */
public class GenericModelAdapter<T extends Model> extends GlrGeometry<T> {
  @Override
  protected boolean isDisplayListDesired() {
    return false;
  }

  @Override
  public boolean hasOpaque() {
    return owner.synchronizedHasOpaque();
  }

  @Override
  public boolean isAlphaBlended() {
    return owner.synchronizedIsAlphaBlended();
  }

  @Override
  protected void pickGeometry(PickContext pc, boolean isSubElementRequired) {
    owner.synchronizedPick();
  }

  @Override
  protected void renderGeometry(RenderContext rc, GlrVisual.RenderType renderType) {
    float globalBrightness = rc.getGlobalBrightness();
    boolean renderAlpha = (renderType == GlrVisual.RenderType.ALPHA_BLENDED) || (renderType == GlrVisual.RenderType.ALL);
    boolean renderOpaque = (renderType == GlrVisual.RenderType.OPAQUE) || (renderType == GlrVisual.RenderType.ALL);
    rc.clearDiffuseColorTextureAdapter();
    owner.synchronizedRender(rc.gl, globalBrightness, renderAlpha, renderOpaque);
  }

  @Override
  public Point3 getIntersectionInSource(Ray ray, Matrix4x4 m, int subElement) {
    Vector4 direction = m.columnTranslation();
    Vector3 flatDirection = new Vector3(direction.x(), 0, direction.z());
    if (flatDirection.magnitudeSquared() == 0.0) {
      return Point3.NaN;
    } else {
      Vector3 normalized = flatDirection.normalized();
      return GlrGeometry.getIntersectionInSourceFromPlaneInLocal(ray, m, 0, 0, 0, normalized.x(), 0, normalized.z());
    }
  }
}
