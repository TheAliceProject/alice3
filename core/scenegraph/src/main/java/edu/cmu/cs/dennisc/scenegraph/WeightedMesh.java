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

import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.property.InstanceProperty;

import java.util.Map;

public class WeightedMesh extends Mesh {
  public final InstanceProperty<WeightInfo> weightInfo = new InstanceProperty<WeightInfo>(this, null);
  public final InstanceProperty<Joint> skeleton = new InstanceProperty<Joint>(this, null);

  public WeightedMesh() {
    super();
  }

  public WeightedMesh(WeightedMesh m) {
    super(m);
    if (m != null) {
      skeleton.setValue(m.skeleton.getValue());
      weightInfo.setValue(m.weightInfo.getValue().createCopy());
    }
  }

  @Override
  public WeightedMesh createCopy() {
    return new WeightedMesh(this);
  }

  public void normalizeWeights() {
    int nVertexCount = this.vertexBuffer.getValue().limit() / 3;

    float[] weights = new float[nVertexCount];
    for (Map.Entry<String, InverseAbsoluteTransformationWeightsPair> entry : weightInfo.getValue().getMap().entrySet()) {
      InverseAbsoluteTransformationWeightsPair iatwp = entry.getValue();
      if (iatwp != null) {
        InverseAbsoluteTransformationWeightsPair.WeightIterator weightIterator = iatwp.getIterator();
        while (weightIterator.hasNext()) {
          int vertexIndex = weightIterator.getIndex();
          weights[vertexIndex] += weightIterator.next();
        }
      }
    }
    for (Map.Entry<String, InverseAbsoluteTransformationWeightsPair> entry : weightInfo.getValue().getMap().entrySet()) {
      InverseAbsoluteTransformationWeightsPair iatwp = entry.getValue();
      if (iatwp != null) {
        InverseAbsoluteTransformationWeightsPair.WeightIterator weightIterator = iatwp.getIterator();
        float[] newWeights = new float[nVertexCount];
        while (weightIterator.hasNext()) {
          int vertexIndex = weightIterator.getIndex();
          float weight = weightIterator.next();
          if (weights[vertexIndex] != 0) {
            newWeights[vertexIndex] = weight / weights[vertexIndex];
          }
        }
        iatwp.setWeights(newWeights);
      }
    }
  }

  @Override
  public void scale(Vector3 scale) {
    super.scale(scale);
    weightInfo.getValue().scale(scale);
  }

}
