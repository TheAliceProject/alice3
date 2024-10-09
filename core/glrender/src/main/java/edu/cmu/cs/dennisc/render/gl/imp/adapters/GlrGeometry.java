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

import static com.jogamp.opengl.GL.GL_POLYGON_OFFSET_FILL;
import static com.jogamp.opengl.GL2.GL_COMPILE_AND_EXECUTE;

import com.jogamp.opengl.GL;
import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.render.gl.imp.PickContext;
import edu.cmu.cs.dennisc.render.gl.imp.RenderContext;
import edu.cmu.cs.dennisc.scenegraph.Geometry;
import org.alice.math.immutable.Matrix4x4;
import org.alice.math.immutable.Plane;
import org.alice.math.immutable.Point3;
import org.alice.math.immutable.Ray;
import org.alice.math.immutable.Vector3;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Dennis Cosgrove
 */
public abstract class GlrGeometry<T extends Geometry> extends GlrElement<T> {
  public void addRenderContext(RenderContext rc) {
    this.renderContexts.add(rc);
  }

  public void removeRenderContext(RenderContext rc) {
    this.renderContexts.remove(rc);
  }

  @Override
  public void initialize(T element) {
    super.initialize(element);
    // The jitter factor is used to tweak otherwise coplanar surfaces that might end up interwoven during render.
    // The problem is referred to as "Z fighting".
    // This jitter, in combination with upping the bit depth in GlDrawableUtils, will lessen the problem likelihood
    // and intensity, but not eliminate it.
    // TODO Use a value based on element that would remain consistent across executions to preserve z-ordering seen.
    jitterFactor = ThreadLocalRandom.current().nextFloat();
  }

  @Override
  protected void handleReleased() {
    super.handleReleased();
    if (this.renderContexts.size() > 0) {
      RenderContext[] renderContexts = new RenderContext[this.renderContexts.size()];
      this.renderContexts.toArray(renderContexts);
      for (RenderContext rc : renderContexts) {
        rc.forgetGeometryAdapter(this, true);
      }
    }
  }

  public abstract boolean isAlphaBlended();

  public boolean hasOpaque() {
    return !isAlphaBlended();
  }

  protected boolean isDisplayListDesired() {
    return true;
  }

  protected boolean isDisplayListInNeedOfRefresh(RenderContext rc) {
    return isGeometryChanged || owner.isChanged();
  }

  protected void markGeometryAsChanged() {
    isGeometryChanged = true;
  }

  //todo: better name
  protected abstract void renderGeometry(RenderContext rc, GlrVisual.RenderType renderType);

  protected abstract void pickGeometry(PickContext pc, boolean isSubElementRequired);

  public final void render(RenderContext rc, GlrVisual.RenderType renderType) {
    if (isDisplayListDesired()) {
      Integer id = rc.getDisplayListID(this);
      if (id == null) {
        id = rc.generateDisplayListID(this);
        markGeometryAsChanged();
      }
      if (isDisplayListInNeedOfRefresh(rc) || (!rc.gl.glIsList(id))) {
        rc.gl.glNewList(id, GL_COMPILE_AND_EXECUTE);
        renderGeometry(rc, renderType);
        rc.gl.glEndList();
        int error = rc.gl.glGetError();
        if (error != GL.GL_NO_ERROR) {
          Logger.severe(rc.glu.gluErrorString(error), error, this);
          //throw new com.jogamp.opengl.GLException( rc.glu.gluErrorString( error ) + " " + error + " " + this.toString() );
        }
        isGeometryChanged = false;
        owner.markAsUnchanged();
      } else {
        if (rc.gl.glIsList(id)) {
          rc.gl.glEnable(GL_POLYGON_OFFSET_FILL);
          rc.gl.glPolygonOffset(jitterFactor, 1);
          rc.gl.glCallList(id);
          rc.gl.glDisable(GL_POLYGON_OFFSET_FILL);
        } else {
          Logger.severe(this);
        }
      }
    } else {
      renderGeometry(rc, renderType);
    }
  }

  public final void pick(PickContext pc, boolean isSubElementRequired) {
    //todo: display lists?
    pickGeometry(pc, isSubElementRequired);
  }

  protected static Point3 getIntersectionInSourceFromPlaneInLocal(Ray ray, Matrix4x4 m, double px, double py, double pz, double nx, double ny, double nz) {
    Point3 position = new Point3(px, py, pz);
    Vector3 direction = new Vector3(nx, ny, nz);
    m.transform(position);
    m.transform(direction);
    Plane plane = Plane.createInstance(position, direction);
    if (plane.isNaN()) {
      return Point3.NaN;
    } else {
      double t = plane.intersect(ray);
      return ray.getPointAlong(t);
    }
  }

  protected static Point3 getIntersectionInSourceFromPlaneInLocal(Ray ray, Matrix4x4 m, Point3 planePosition, Vector3 planeDirection) {
    return getIntersectionInSourceFromPlaneInLocal(ray, m, planePosition.x(), planePosition.y(), planePosition.x(), planeDirection.x(), planeDirection.y(), planeDirection.z());
  }

  public abstract Point3 getIntersectionInSource(Ray ray, Matrix4x4 m, int subElement);

  private final List<RenderContext> renderContexts = Lists.newLinkedList();
  private boolean isGeometryChanged;
  private float jitterFactor;
}
