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
  @MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
  public BipedImp getImplementation() {
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
    return getJoint(BipedResource.ROOT);
  }

  public SJoint getPelvis() {
    return getJoint(BipedResource.PELVIS_LOWER_BODY);
  }

  public SJoint getSpineBase() {
    return getJoint(BipedResource.SPINE_BASE);
  }

  public SJoint getSpineMiddle() {
    return getJoint(BipedResource.SPINE_MIDDLE);
  }

  public SJoint getSpineUpper() {
    return getJoint(BipedResource.SPINE_UPPER);
  }

  public SJoint getNeck() {
    return getJoint(BipedResource.NECK);
  }

  public SJoint getHead() {
    return getJoint(BipedResource.HEAD);
  }

  public SJoint getMouth() {
    return getJoint(BipedResource.MOUTH);
  }

  public SJoint getRightEye() {
    return getJoint(BipedResource.RIGHT_EYE);
  }

  public SJoint getLeftEye() {
    return getJoint(BipedResource.LEFT_EYE);
  }

  public SJoint getLeftEyelid() {
    return getJoint(BipedResource.LEFT_EYELID);
  }

  public SJoint getRightEyelid() {
    return getJoint(BipedResource.RIGHT_EYELID);
  }

  public SJoint getRightHip() {
    return getJoint(BipedResource.RIGHT_HIP);
  }

  public SJoint getRightKnee() {
    return getJoint(BipedResource.RIGHT_KNEE);
  }

  public SJoint getRightAnkle() {
    return getJoint(BipedResource.RIGHT_ANKLE);
  }

  public SJoint getRightFoot() {
    return getJoint(BipedResource.RIGHT_FOOT);
  }

  public SJoint getLeftHip() {
    return getJoint(BipedResource.LEFT_HIP);
  }

  public SJoint getLeftKnee() {
    return getJoint(BipedResource.LEFT_KNEE);
  }

  public SJoint getLeftAnkle() {
    return getJoint(BipedResource.LEFT_ANKLE);
  }

  public SJoint getLeftFoot() {
    return getJoint(BipedResource.LEFT_FOOT);
  }

  public SJoint getRightClavicle() {
    return getJoint(BipedResource.RIGHT_CLAVICLE);
  }

  public SJoint getRightShoulder() {
    return getJoint(BipedResource.RIGHT_SHOULDER);
  }

  public SJoint getRightElbow() {
    return getJoint(BipedResource.RIGHT_ELBOW);
  }

  public SJoint getRightWrist() {
    return getJoint(BipedResource.RIGHT_WRIST);
  }

  public SJoint getRightHand() {
    return getJoint(BipedResource.RIGHT_HAND);
  }

  public SJoint getRightThumb() {
    return getJoint(BipedResource.RIGHT_THUMB);
  }

  public SJoint getRightThumbKnuckle() {
    return getJoint(BipedResource.RIGHT_THUMB_KNUCKLE);
  }

  public SJoint getRightIndexFinger() {
    return getJoint(BipedResource.RIGHT_INDEX_FINGER);
  }

  public SJoint getRightIndexFingerKnuckle() {
    return getJoint(BipedResource.RIGHT_INDEX_FINGER_KNUCKLE);
  }

  public SJoint getRightMiddleFinger() {
    return getJoint(BipedResource.RIGHT_MIDDLE_FINGER);
  }

  public SJoint getRightMiddleFingerKnuckle() {
    return getJoint(BipedResource.RIGHT_MIDDLE_FINGER_KNUCKLE);
  }

  public SJoint getRightPinkyFinger() {
    return getJoint(BipedResource.RIGHT_PINKY_FINGER);
  }

  public SJoint getRightPinkyFingerKnuckle() {
    return getJoint(BipedResource.RIGHT_PINKY_FINGER_KNUCKLE);
  }

  public SJoint getLeftClavicle() {
    return getJoint(BipedResource.LEFT_CLAVICLE);
  }

  public SJoint getLeftShoulder() {
    return getJoint(BipedResource.LEFT_SHOULDER);
  }

  public SJoint getLeftElbow() {
    return getJoint(BipedResource.LEFT_ELBOW);
  }

  public SJoint getLeftWrist() {
    return getJoint(BipedResource.LEFT_WRIST);
  }

  public SJoint getLeftHand() {
    return getJoint(BipedResource.LEFT_HAND);
  }

  public SJoint getLeftThumb() {
    return getJoint(BipedResource.LEFT_THUMB);
  }

  public SJoint getLeftThumbKnuckle() {
    return getJoint(BipedResource.LEFT_THUMB_KNUCKLE);
  }

  public SJoint getLeftIndexFinger() {
    return getJoint(BipedResource.LEFT_INDEX_FINGER);
  }

  public SJoint getLeftIndexFingerKnuckle() {
    return getJoint(BipedResource.LEFT_INDEX_FINGER_KNUCKLE);
  }

  public SJoint getLeftMiddleFinger() {
    return getJoint(BipedResource.LEFT_MIDDLE_FINGER);
  }

  public SJoint getLeftMiddleFingerKnuckle() {
    return getJoint(BipedResource.LEFT_MIDDLE_FINGER_KNUCKLE);
  }

  public SJoint getLeftPinkyFinger() {
    return getJoint(BipedResource.LEFT_PINKY_FINGER);
  }

  public SJoint getLeftPinkyFingerKnuckle() {
    return getJoint(BipedResource.LEFT_PINKY_FINGER_KNUCKLE);
  }

  @MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
  public void strikePose(BipedPose pose, StrikePose.Detail... details) {
    super.strikePose(pose, details);
  }
}
