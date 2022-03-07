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

import static com.jogamp.opengl.GL2ES1.GL_CLIP_PLANE0;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.Matrix4x4;
import edu.cmu.cs.dennisc.math.Plane;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.property.InstanceProperty;
import edu.cmu.cs.dennisc.render.gl.imp.RenderContext;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.Geometry;
import edu.cmu.cs.dennisc.scenegraph.PlanarReflector;

import java.nio.DoubleBuffer;

/**
 * @author Dennis Cosgrove
 */
public class GlrPlanarReflector extends GlrVisual<PlanarReflector> {
  private void updateGeometryTransformation() {
    Geometry[] sgGeometries = owner.geometries.getValue();
    if ((sgGeometries != null) && (sgGeometries.length > 0)) {
      Geometry sgGeometry = sgGeometries[0];

      sgGeometry.getPlane(this.geometryTransformation);
      //      edu.cmu.cs.dennisc.math.PointD3 point0;
      //      edu.cmu.cs.dennisc.math.PointD3 point1;
      //      edu.cmu.cs.dennisc.math.VectorF3 normal;
      //      if( sgGeometry instanceof edu.cmu.cs.dennisc.scenegraph.Mesh ) {
      //        edu.cmu.cs.dennisc.scenegraph.Mesh sgMesh = (edu.cmu.cs.dennisc.scenegraph.Mesh)sgGeometry;
      //        edu.cmu.cs.dennisc.math.PointD3[] points = sgMesh.points.getValue();
      //        edu.cmu.cs.dennisc.math.VectorF3[] normals = sgMesh.normals.getValue();
      //        assert points.length >= 2;
      //        assert normals.length >= 1;
      //
      //        point0 = points[ 0 ];
      //        point1 = points[ 1 ];
      //        normal = normals[ 0 ];
      //      } else if( sgGeometry instanceof edu.cmu.cs.dennisc.scenegraph.VertexGeometry ) {
      //        edu.cmu.cs.dennisc.scenegraph.VertexGeometry sgVertexGeometry = (edu.cmu.cs.dennisc.scenegraph.VertexGeometry)sgGeometry;
      //        edu.cmu.cs.dennisc.scenegraph.Vertex[] vertices = sgVertexGeometry.vertices.getValue();
      //        assert vertices.length >= 2;
      //        point0 = vertices[ 0 ].position;
      //        point1 = vertices[ 1 ].position;
      //        normal = vertices[ 0 ].normal;
      //      } else {
      //        throw new RuntimeException( "todo" );
      //      }
      //
      //
      //      edu.cmu.cs.dennisc.math.VectorD3 forward = new edu.cmu.cs.dennisc.math.VectorD3();
      //      edu.cmu.cs.dennisc.math.VectorD3 upGuide = new edu.cmu.cs.dennisc.math.VectorD3();
      //      edu.cmu.cs.dennisc.math.PointD3 translation = new edu.cmu.cs.dennisc.math.PointD3();
      //
      //      forward.set( normal.x, normal.y, normal.z );
      //      forward.normalize();
      //      forward.negate();
      //
      //      upGuide.set( point0 );
      //      upGuide.sub( point1 );
      //      upGuide.normalize();
      //
      //      translation.set( point0 );
      //
      //      edu.cmu.cs.dennisc.math.MatrixD3x3 axes = LinearAlgebra.newMatrix3d( forward, upGuide );
      //
      //      this.geometryTransformation.setRow( 0, axes.right.x, axes.up.x, axes.backward.x, translation.x );
      //      this.geometryTransformation.setRow( 1, axes.right.y, axes.up.y, axes.backward.y, translation.y );
      //      this.geometryTransformation.setRow( 2, axes.right.z, axes.up.z, axes.backward.z, translation.z );
      //      this.geometryTransformation.setRow( 3, 0       , 0       , 0       , 1             );
    }
  }

  @Override
  protected void propertyChanged(InstanceProperty<?> property) {
    super.propertyChanged(property);
    if (property == owner.geometries) {
      updateGeometryTransformation();
    }
  }

  @Override
  public void renderAlphaBlended(RenderContext rc) {
    //pass
  }

  @Override
  public void renderOpaque(RenderContext rc) {
    //pass
  }

  //  @Override
  //  public void pick( PickContext pc, PickParameters pickParameters ) {
  //    this.geometryAdapter.pick( pc, pickParameters.isSubElementRequired() );
  //  }

  public boolean isFacing(GlrAbstractCamera<? extends AbstractCamera> cameraAdapter) {
    AffineMatrix4x4 m = owner.getTransformation(cameraAdapter.owner);
    return m.orientation.backward.z > 0;
  }

  public synchronized void applyReflection(RenderContext rc) {
    AffineMatrix4x4 m = owner.getAbsoluteTransformation();
    m.multiply(this.geometryTransformation);

    Plane plane = Plane.createInstance(m);
    plane.getEquation(this.equation);
    rc.gl.glClipPlane(GL_CLIP_PLANE0, this.equationBuffer);

    Point3 p = m.translation;
    Vector3 v = new Vector3(-m.orientation.backward.x, -m.orientation.backward.y, -m.orientation.backward.z);

    Matrix4x4 r = new Matrix4x4();

    double p_dot_v = (p.x * v.x) + (p.y * v.y) + (p.z * v.z);

    r.right.x = 1 - (2 * v.x * v.x);
    r.up.x = 0 - (2 * v.x * v.y);
    r.backward.x = 0 - (2 * v.x * v.z);
    r.translation.x = 2 * v.x * p_dot_v;
    r.right.y = 0 - (2 * v.y * v.x);
    r.up.y = 1 - (2 * v.y * v.y);
    r.backward.y = 0 - (2 * v.y * v.z);
    r.translation.y = 2 * v.y * p_dot_v;
    r.right.z = 0 - (2 * v.z * v.x);
    r.up.z = 0 - (2 * v.z * v.y);
    r.backward.z = 1 - (2 * v.z * v.z);
    r.translation.z = 2 * v.z * p_dot_v;
    r.right.w = 0;
    r.up.w = 0;
    r.backward.w = 0;
    r.translation.w = 1;

    r.getAsColumnMajorArray16(this.reflection);
    rc.gl.glMultMatrixd(this.reflectionBuffer);
  }

  public void renderStencil(RenderContext rc, GlrVisual.RenderType renderType) {
    rc.gl.glPushMatrix();
    try {
      synchronized (this) {
        rc.gl.glMultMatrixd(accessAbsoluteTransformationAsBuffer());
      }
      actuallyRender(rc, renderType);
    } finally {
      rc.gl.glPopMatrix();
    }
  }

  private final AffineMatrix4x4 geometryTransformation = AffineMatrix4x4.createNaN();
  private final double[] reflection = new double[16];
  private final DoubleBuffer reflectionBuffer = DoubleBuffer.wrap(this.reflection);
  private final double[] equation = new double[4];
  private final DoubleBuffer equationBuffer = DoubleBuffer.wrap(this.equation);
}
