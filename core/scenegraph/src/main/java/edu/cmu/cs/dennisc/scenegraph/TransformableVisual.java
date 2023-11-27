/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
package edu.cmu.cs.dennisc.scenegraph;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector3;

/**
 * @author dculyba
 */
public class TransformableVisual extends Visual {

  public TransformableVisual() {
    super();
    super.setParent(this.sgTransformable);
  }

  @Override
  public void setParent(Composite parent) {
    this.sgTransformable.setParent(parent);
  }

  @Override
  public Composite getParent() {
    return this.sgTransformable.getParent();
  }

  public void setTransform(Transformable transform) {
    this.sgTransformable.setLocalTransformation(transform.accessLocalTransformation());
  }

  public Transformable getTransformable() {
    return this.sgTransformable;
  }

  @Override
  public AxisAlignedBox getAxisAlignedMinimumBoundingBox(AxisAlignedBox rv) {
    AxisAlignedBox transformedRV = super.getAxisAlignedMinimumBoundingBox(rv);

    if (!transformedRV.isNaN()) {
      Point3 maximum = transformedRV.getMaximum();
      this.sgTransformable.accessLocalTransformation().transform(maximum);
      transformedRV.setMaximum(maximum);

      Point3 minimum = transformedRV.getMinimum();
      this.sgTransformable.accessLocalTransformation().transform(minimum);
      transformedRV.setMinimum(minimum);
    }

    return transformedRV;
  }

  @Override
  public edu.cmu.cs.dennisc.math.Sphere getBoundingSphere(edu.cmu.cs.dennisc.math.Sphere rv) {
    edu.cmu.cs.dennisc.math.Sphere transformedRV = super.getBoundingSphere(rv);

    if (!transformedRV.isNaN()) {
      this.sgTransformable.accessLocalTransformation().transform(transformedRV.center);
    }

    return rv;
  }

  private final Transformable sgTransformable = new Transformable();

  public void setTranslation(Vector3 translation) {
    // Update value
    AffineMatrix4x4 currentTransform = sgTransformable.localTransformation.getValue();
    currentTransform.translation.set(translation);

    // Trigger property event
    sgTransformable.localTransformation.setValue(currentTransform);
    // Trigger transformation event
    fireAbsoluteTransformationChange();
  }
}
