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
package org.lgna.story.implementation.sims2;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.Dimension3;
import edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.UnitQuaternion;
import edu.cmu.cs.dennisc.nebulous.NebulousJoint;
import edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound;
import org.lgna.story.implementation.JointImp;
import org.lgna.story.implementation.JointedModelImp;
import org.lgna.story.resources.JointId;

public class JointImplementation extends JointImp {
  private NebulousJoint sgJoint;

  public JointImplementation(JointedModelImp<?, ?> jointedModelImplementation, NebulousJoint sgJoint) {
    super(jointedModelImplementation);
    this.sgJoint = sgJoint;
    putInstance(this.sgJoint);
  }

  @Override
  public JointId getJointId() {
    return this.sgJoint.getJointId();
  }

  @Override
  public NebulousJoint getSgComposite() {
    return this.sgJoint;
  }

  @Override
  public boolean isFreeInX() {
    //todo
    return true;
  }

  @Override
  public boolean isFreeInY() {
    //todo
    return true;
  }

  @Override
  public boolean isFreeInZ() {
    //todo
    return true;
  }

  @Override
  public UnitQuaternion getOriginalOrientation() {
    return this.sgJoint.getScaledOriginalLocalTransformation().orientation.createUnitQuaternion();
  }

  @Override
  public AffineMatrix4x4 getScaledOriginalTransformation() {
    return this.sgJoint.getScaledOriginalLocalTransformation();
  }

  @Override
  public void setScale(Dimension3 scale) {
    sgJoint.setScale(scale);
  }

  @Override
  public boolean isReoriented() {
    return !getLocalTransformation().orientation.isWithinReasonableEpsilonOf(getScaledOriginalTransformation().orientation);
  }

  @Override
  public boolean isRelocated() {
    return !getLocalTransformation().translation.isWithinReasonableEpsilonOf(getScaledOriginalTransformation().translation);
  }

  @Override
  protected void copyOnto(JointImp newJoint) {
    newJoint.setScale(sgJoint.getScale());
    Point3 position = isRelocated() ? getLocalPosition() : newJoint.getLocalPosition();
    OrthogonalMatrix3x3 orientation = isReoriented() ? getLocalOrientation() : newJoint.getLocalOrientation();
    newJoint.setLocalTransformation(new AffineMatrix4x4(orientation, position));
  }

  @Override
  protected void updateCumulativeBound(CumulativeBound rv, AffineMatrix4x4 trans) {
    rv.addBoundingBox(sgJoint.getAxisAlignedBoundingBox(), trans);
  }

}
