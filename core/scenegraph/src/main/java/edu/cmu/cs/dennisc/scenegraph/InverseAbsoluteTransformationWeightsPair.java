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

import edu.cmu.cs.dennisc.codec.BinaryDecoder;
import edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable;
import edu.cmu.cs.dennisc.codec.BinaryEncoder;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;

import java.util.Arrays;

public abstract class InverseAbsoluteTransformationWeightsPair implements BinaryEncodableAndDecodable {
  protected AffineMatrix4x4 inverseAbsoluteTransformation;
  protected float[] weights;
  protected int index = 0;

  public InverseAbsoluteTransformationWeightsPair() {
  }

  public InverseAbsoluteTransformationWeightsPair(InverseAbsoluteTransformationWeightsPair other) {
    index = other.index;
    inverseAbsoluteTransformation = new AffineMatrix4x4(other.inverseAbsoluteTransformation);
    weights = Arrays.copyOf(other.weights, other.weights.length);
  }

  public abstract void setWeights(float[] weightsIn);

  public float getWeight() {
    return this.weights[this.index];
  }

  public abstract int getIndex();

  public abstract InverseAbsoluteTransformationWeightsPair createCopy();

  public boolean isDone() {
    return this.index == this.weights.length;
  }

  public void advance() {
    this.index++;
  }

  public void reset() {
    this.index = 0;
  }

  public void setInverseAbsoluteTransformation(AffineMatrix4x4 transform) {
    this.inverseAbsoluteTransformation = transform;
  }

  public AffineMatrix4x4 getInverseAbsoluteTransformation() {
    return this.inverseAbsoluteTransformation;
  }

  public void decode(BinaryDecoder binaryDecoder) {
    this.inverseAbsoluteTransformation = binaryDecoder.decodeBinaryEncodableAndDecodable();
    this.weights = binaryDecoder.decodeFloatArray();
  }

  @Override
  public void encode(BinaryEncoder binaryEncoder) {
    binaryEncoder.encode(this.inverseAbsoluteTransformation);
    binaryEncoder.encode(this.weights);
  }

  public static InverseAbsoluteTransformationWeightsPair createInverseAbsoluteTransformationWeightsPair(float[] weights, AffineMatrix4x4 invAbsTransformation) {
    int nonZeroWeights = 0;
    for (float weight : weights) {
      if (weight != 0) {
        nonZeroWeights++;
      }
    }
    if (nonZeroWeights > 0) {
      InverseAbsoluteTransformationWeightsPair iawp;
      double portion = ((double) nonZeroWeights) / weights.length;
      if (portion > .9) {
        iawp = new PlentifulInverseAbsoluteTransformationWeightsPair();
      } else {
        iawp = new SparseInverseAbsoluteTransformationWeightsPair();
      }
      iawp.setWeights(weights);
      iawp.setInverseAbsoluteTransformation(invAbsTransformation);
      return iawp;
    }
    return null;
  }
}
