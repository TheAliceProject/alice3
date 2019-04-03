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

import org.lgna.ik.core.IKCore.Limb;
import org.lgna.project.annotations.MethodTemplate;
import org.lgna.project.annotations.Visibility;
import org.lgna.story.implementation.BipedImp;
import org.lgna.story.resources.BipedResource;

/**
 * @author Dennis Cosgrove
 */
public class SBiped extends SJointedModel implements Articulable {
  private final BipedImp implementation;

  public SBiped(BipedResource resource) {
    this.implementation = resource.createImplementation(this);
  }

  @Override
    /* package-private */BipedImp getImplementation() {
    return this.implementation;
  }

  @Override
  @MethodTemplate(visibility = Visibility.TUCKED_AWAY)
  public void walkTo(SThing entity) {
    //    implementation.walkTo( entity );
  }

  @Override
  @MethodTemplate(visibility = Visibility.TUCKED_AWAY)
  public void touch(SThing entity) {
    reachFor(Limb.RIGHT_ARM, entity);
  }

  @MethodTemplate(visibility = Visibility.TUCKED_AWAY)
  public void reachFor(Limb limb, SThing target) {
    implementation.reachFor(target, limb);
  }

  @MethodTemplate(visibility = Visibility.TUCKED_AWAY)
  public SJoint getRoot() {
    return SJoint.getJoint(this, BipedResource.ROOT);
  }

  public SJoint getPelvis() {
    return SJoint.getJoint(this, BipedResource.PELVIS_LOWER_BODY);
  }

  public SJoint getSpineBase() {
    return SJoint.getJoint(this, BipedResource.SPINE_BASE);
  }

  public SJoint getSpineMiddle() {
    return SJoint.getJoint(this, BipedResource.SPINE_MIDDLE);
  }

  public SJoint getSpineUpper() {
    return SJoint.getJoint(this, BipedResource.SPINE_UPPER);
  }

  public SJoint getNeck() {
    return SJoint.getJoint(this, BipedResource.NECK);
  }

  public SJoint getHead() {
    return SJoint.getJoint(this, BipedResource.HEAD);
  }

  public SJoint getMouth() {
    return SJoint.getJoint(this, BipedResource.MOUTH);
  }

  public SJoint getRightEye() {
    return SJoint.getJoint(this, BipedResource.RIGHT_EYE);
  }

  public SJoint getLeftEye() {
    return SJoint.getJoint(this, BipedResource.LEFT_EYE);
  }

  public SJoint getLeftEyelid() {
    return SJoint.getJoint(this, BipedResource.LEFT_EYELID);
  }

  public SJoint getRightEyelid() {
    return SJoint.getJoint(this, BipedResource.RIGHT_EYELID);
  }

  public SJoint getRightHip() {
    return SJoint.getJoint(this, BipedResource.RIGHT_HIP);
  }

  public SJoint getRightKnee() {
    return SJoint.getJoint(this, BipedResource.RIGHT_KNEE);
  }

  public SJoint getRightAnkle() {
    return SJoint.getJoint(this, BipedResource.RIGHT_ANKLE);
  }

  public SJoint getRightFoot() {
    return SJoint.getJoint(this, BipedResource.RIGHT_FOOT);
  }

  public SJoint getLeftHip() {
    return SJoint.getJoint(this, BipedResource.LEFT_HIP);
  }

  public SJoint getLeftKnee() {
    return SJoint.getJoint(this, BipedResource.LEFT_KNEE);
  }

  public SJoint getLeftAnkle() {
    return SJoint.getJoint(this, BipedResource.LEFT_ANKLE);
  }

  public SJoint getLeftFoot() {
    return SJoint.getJoint(this, BipedResource.LEFT_FOOT);
  }

  public SJoint getRightClavicle() {
    return SJoint.getJoint(this, BipedResource.RIGHT_CLAVICLE);
  }

  public SJoint getRightShoulder() {
    return SJoint.getJoint(this, BipedResource.RIGHT_SHOULDER);
  }

  public SJoint getRightElbow() {
    return SJoint.getJoint(this, BipedResource.RIGHT_ELBOW);
  }

  public SJoint getRightWrist() {
    return SJoint.getJoint(this, BipedResource.RIGHT_WRIST);
  }

  public SJoint getRightHand() {
    return SJoint.getJoint(this, BipedResource.RIGHT_HAND);
  }

  public SJoint getRightThumb() {
    return SJoint.getJoint(this, BipedResource.RIGHT_THUMB);
  }

  public SJoint getRightThumbKnuckle() {
    return SJoint.getJoint(this, BipedResource.RIGHT_THUMB_KNUCKLE);
  }

  public SJoint getRightIndexFinger() {
    return SJoint.getJoint(this, BipedResource.RIGHT_INDEX_FINGER);
  }

  public SJoint getRightIndexFingerKnuckle() {
    return SJoint.getJoint(this, BipedResource.RIGHT_INDEX_FINGER_KNUCKLE);
  }

  public SJoint getRightMiddleFinger() {
    return SJoint.getJoint(this, BipedResource.RIGHT_MIDDLE_FINGER);
  }

  public SJoint getRightMiddleFingerKnuckle() {
    return SJoint.getJoint(this, BipedResource.RIGHT_MIDDLE_FINGER_KNUCKLE);
  }

  public SJoint getRightPinkyFinger() {
    return SJoint.getJoint(this, BipedResource.RIGHT_PINKY_FINGER);
  }

  public SJoint getRightPinkyFingerKnuckle() {
    return SJoint.getJoint(this, BipedResource.RIGHT_PINKY_FINGER_KNUCKLE);
  }

  public SJoint getLeftClavicle() {
    return SJoint.getJoint(this, BipedResource.LEFT_CLAVICLE);
  }

  public SJoint getLeftShoulder() {
    return SJoint.getJoint(this, BipedResource.LEFT_SHOULDER);
  }

  public SJoint getLeftElbow() {
    return SJoint.getJoint(this, BipedResource.LEFT_ELBOW);
  }

  public SJoint getLeftWrist() {
    return SJoint.getJoint(this, BipedResource.LEFT_WRIST);
  }

  public SJoint getLeftHand() {
    return SJoint.getJoint(this, BipedResource.LEFT_HAND);
  }

  public SJoint getLeftThumb() {
    return SJoint.getJoint(this, BipedResource.LEFT_THUMB);
  }

  public SJoint getLeftThumbKnuckle() {
    return SJoint.getJoint(this, BipedResource.LEFT_THUMB_KNUCKLE);
  }

  public SJoint getLeftIndexFinger() {
    return SJoint.getJoint(this, BipedResource.LEFT_INDEX_FINGER);
  }

  public SJoint getLeftIndexFingerKnuckle() {
    return SJoint.getJoint(this, BipedResource.LEFT_INDEX_FINGER_KNUCKLE);
  }

  public SJoint getLeftMiddleFinger() {
    return SJoint.getJoint(this, BipedResource.LEFT_MIDDLE_FINGER);
  }

  public SJoint getLeftMiddleFingerKnuckle() {
    return SJoint.getJoint(this, BipedResource.LEFT_MIDDLE_FINGER_KNUCKLE);
  }

  public SJoint getLeftPinkyFinger() {
    return SJoint.getJoint(this, BipedResource.LEFT_PINKY_FINGER);
  }

  public SJoint getLeftPinkyFingerKnuckle() {
    return SJoint.getJoint(this, BipedResource.LEFT_PINKY_FINGER_KNUCKLE);
  }

  @MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
  public void strikePose(BipedPose pose, StrikePose.Detail... details) {
    super.strikePose(pose, details);
  }
}
