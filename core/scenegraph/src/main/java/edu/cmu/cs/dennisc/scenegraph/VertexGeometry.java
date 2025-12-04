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

package edu.cmu.cs.dennisc.scenegraph;

import edu.cmu.cs.dennisc.property.CopyableArrayProperty;
import edu.cmu.cs.dennisc.property.InstancePropertyOwner;
import edu.cmu.cs.dennisc.scenegraph.bound.BoundUtilities;
import org.alice.math.immutable.*;

/**
 * @author Dennis Cosgrove
 */
public abstract class VertexGeometry extends Geometry {
  public static class VerticesProperty extends CopyableArrayProperty<Vertex> {
    public VerticesProperty(InstancePropertyOwner owner, Vertex... vertices) {
      super(owner, vertices);
    }

    @Override
    protected Vertex[] createArray(int length) {
      return new Vertex[length];
    }

    @Override
    protected Vertex createCopy(Vertex src) {
      return new Vertex(src);
    }

    @Deprecated
    public void touch() {
      setValue(getValue());
    }
  }

  @Override
  protected AxisAlignedBox updateBoundingBox() {
    return BoundUtilities.getBoundingBox(vertices.getValue());
  }

  @Override
  public AffineMatrix4x4 getPlane() {
    Vertex[] vertices = this.vertices.getValue();
    assert vertices.length >= 2;

    Point3 translation = vertices[0].position;
    Point3 point1 = vertices[1].position;
    Vector3f normal = vertices[0].normal;

    Vector3 forward = (new Vector3(normal.x(), normal.y(), normal.z())).normalized().negate();
    Vector3 upGuide = translation.minus(point1).normalized();

    return new AffineMatrix4x4(new ForwardAndUpGuide(forward, upGuide).asMatrix3x3(), translation);
  }

  @Override
  public void transform(Matrix4x4 trans) {
    //todo: does not seem to work
    for (Vertex vertex : vertices.getValue()) {
      vertex.transform(trans);
    }
    vertices.touch();
  }

  public final VerticesProperty vertices = new VerticesProperty(this);
}
