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

package org.lgna.story;

import org.lgna.common.LgnaIllegalArgumentException;
import org.lgna.project.annotations.MethodTemplate;
import org.lgna.project.annotations.Visibility;
import org.lgna.story.implementation.JointedModelImp;
import org.lgna.story.resources.JointArrayId;
import org.lgna.story.resources.JointId;
import org.lgna.story.resources.JointedModelResource;

/**
 * @author Dennis Cosgrove
 */
public abstract class SJointedModel extends SModel {
  @Override
  @MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
  public abstract JointedModelImp getImplementation();

  //todo: make protected
  @MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
  public SJoint getJoint(JointId jointId) {
    return SJoint.getJoint(this, jointId);
  }

  @MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
  public SJoint getJoint(String jointName) {
    return SJoint.getJoint(this, jointName);
  }

  @MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
  public SJoint[] getJointArray(JointId[] jointIdArray) {
    return SJoint.getJointArray(this, jointIdArray);
  }

  @MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
  public SJoint[] getJointArray(JointArrayId jointArrayId) {
    return SJoint.getJointArray(this, jointArrayId);
  }

  @MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
  public void strikePose(Pose<? extends SJointedModel> pose, StrikePose.Detail... details) {
    double duration = Duration.getValue(details);
    edu.cmu.cs.dennisc.animation.Style style = AnimationStyle.getValue(details).getInternal();
    this.getImplementation().strikePose(pose, duration, style);
  }

  public void straightenOutJoints(StraightenOutJoints.Detail... details) {
    this.getImplementation().animateStraightenOutJoints(Duration.getValue(details), AnimationStyle.getValue(details).getInternal());
  }

  @MethodTemplate(visibility = Visibility.TUCKED_AWAY)
  public JointedModelResource getJointedModelResource() {
    return this.getImplementation().getResource();
  }

  @MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
  protected void setJointedModelResource(JointedModelResource resource) {
    LgnaIllegalArgumentException.checkArgumentIsNotNull(resource, 0);
    this.getImplementation().setNewResource(resource);
  }
}
