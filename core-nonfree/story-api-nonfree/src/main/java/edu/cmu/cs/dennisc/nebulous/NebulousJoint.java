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

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.Dimension3;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.render.gl.imp.adapters.AdapterFactory;
import edu.cmu.cs.dennisc.scenegraph.AbstractTransformable;
import edu.cmu.cs.dennisc.scenegraph.Composite;
import edu.cmu.cs.dennisc.scenegraph.ModelJoint;
import org.lgna.story.resources.JointId;

/**
 * @author Dennis Cosgrove
 */
public class NebulousJoint extends AbstractTransformable implements ModelJoint {
  static {
    AdapterFactory.register(NebulousJoint.class, GlrNebulousJoint.class);
  }

  public NebulousJoint(Model nebModel, JointId jointId) {
    this.nebModel = nebModel;
    this.jointId = jointId;
    this.setName(this.jointId.toString());
  }

  public JointId getJointId() {
    return this.jointId;
  }

  public Dimension3 getScale() {
    return scale;
  }

  public void setScale(Dimension3 scale) {
    this.scale = scale;
  }

  public AffineMatrix4x4 getScaledOriginalLocalTransformation() {
    AffineMatrix4x4 aliceTransform = nebModel.getOriginalTransformationForJoint(this.jointId);
    aliceTransform.translation.setToMultiplication(aliceTransform.translation, scale);
    return aliceTransform;
  }

  @Override
  protected AffineMatrix4x4 accessLocalTransformation() {
    AffineMatrix4x4 aliceTransform = this.nebModel.getLocalTransformationForJoint(this.jointId);
    if (this.actualTranslation != null) {
      aliceTransform.translation.set(this.actualTranslation);
    }
    aliceTransform.translation.setToMultiplication(aliceTransform.translation, scale);
    return aliceTransform;
  }

  @Override
  protected void touchLocalTransformation(AffineMatrix4x4 m) {
    AffineMatrix4x4 current = this.nebModel.getLocalTransformationForJoint(this.jointId);
    current.orientation.setValue(m.orientation);
    final Point3 unscaledTranslation = m.translation;
    // Remove scale before sending to native library
    unscaledTranslation.divide(scale);
    current.translation.set(unscaledTranslation);
    if (this.actualTranslation == null) {
      this.actualTranslation = new Point3();
    }
    this.actualTranslation.set(unscaledTranslation);
    this.nebModel.setLocalTransformationForJoint(this.jointId, current);
    notifyTransformationListeners();
  }

  @Override
  protected Composite getVehicle() {
    return this.getParent();
  }

  public AxisAlignedBox getAxisAlignedBoundingBox() {
    return this.nebModel.getAxisAlignedBoundingBoxForJoint(this.jointId);
  }

  private final Model nebModel;
  private final JointId jointId;
  private Point3 actualTranslation;
  // Alice side scaling that will not be sent to native library
  private Dimension3 scale = new Dimension3(1, 1, 1);
}
