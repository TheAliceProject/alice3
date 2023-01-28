/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
 */

package org.lgna.story.resourceutilities;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrSkeletonVisual;
import edu.cmu.cs.dennisc.scenegraph.InverseAbsoluteTransformationWeightsPair;
import edu.cmu.cs.dennisc.scenegraph.Joint;

class UtilityWeightedMeshControl extends GlrSkeletonVisual.WeightedMeshControl {
  AxisAlignedBox getAbsoluteBoundingBox() {
    AxisAlignedBox box = new AxisAlignedBox();
    this.indexBuffer.rewind();
    while (this.indexBuffer.hasRemaining()) {
      int index = this.indexBuffer.get() * 3;
      Point3 vertex = new Point3(this.vertexBuffer.get(index), this.vertexBuffer.get(index + 1), this.vertexBuffer.get(index + 2));
      box.union(vertex);
    }
    return box;
  }

  private static final float WEIGHT_THRESHOLD = .2f;

  AxisAlignedBox getBoundingBoxForJoint(Joint joint) {
    InverseAbsoluteTransformationWeightsPair iatwp = this.weightedMesh.weightInfo.getValue().getMap().get(joint.jointID.getValue());
    AxisAlignedBox box = new AxisAlignedBox();
    if (iatwp != null) {
      AffineMatrix4x4 inverseJoint = iatwp.getInverseAbsoluteTransformation();
      AffineMatrix4x4 projectedJoint = joint.getAbsoluteTransformation();
      InverseAbsoluteTransformationWeightsPair.WeightIterator weightIterator = iatwp.getIterator();
      while (weightIterator.hasNext()) {
        int vertexIndex = weightIterator.getIndex() * 3;
        float weight = weightIterator.next();
        if (weight > WEIGHT_THRESHOLD) {
          Point3 vertex = new Point3(this.vertexBuffer.get(vertexIndex), this.vertexBuffer.get(vertexIndex + 1), this.vertexBuffer.get(vertexIndex + 2));
          Point3 localVertex = inverseJoint.createTransformed(vertex);
          box.union(localVertex);
        }
      }
      box.scale(projectedJoint.orientation);
    }
    return box;
  }
}
