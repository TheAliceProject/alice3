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

package org.lgna.story;

import org.lgna.ik.core.IKCore.Limb;
import org.lgna.project.annotations.MethodTemplate;
import org.lgna.project.annotations.Visibility;

/**
 * @author Dennis Cosgrove
 */
public class SBiped extends SJointedModel implements Articulable {
	private final org.lgna.story.implementation.BipedImp implementation;

	public SBiped( org.lgna.story.resources.BipedResource resource ) {
		this.implementation = resource.createImplementation( this );
	}

	@Override
	/* package-private */org.lgna.story.implementation.BipedImp getImplementation() {
		return this.implementation;
	}

	@Override
	@MethodTemplate( visibility = Visibility.TUCKED_AWAY )
	public void walkTo( SThing entity ) {
		//		implementation.walkTo( entity );
	}

	@Override
	@MethodTemplate( visibility = Visibility.TUCKED_AWAY )
	public void touch( SThing entity ) {
		reachFor( Limb.RIGHT_ARM, entity );
	}

	public void reachFor( Limb limb, SThing target ) {
		implementation.reachFor( target, limb );
	}

	@MethodTemplate( visibility = Visibility.TUCKED_AWAY )
	public SJoint getRoot() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.BipedResource.ROOT );
	}

	public SJoint getPelvis() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.BipedResource.PELVIS_LOWER_BODY );
	}

	public SJoint getSpineBase() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.BipedResource.SPINE_BASE );
	}

	public SJoint getSpineMiddle() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.BipedResource.SPINE_MIDDLE );
	}

	public SJoint getSpineUpper() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.BipedResource.SPINE_UPPER );
	}

	public SJoint getNeck() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.BipedResource.NECK );
	}

	public SJoint getHead() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.BipedResource.HEAD );
	}

	public SJoint getMouth() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.BipedResource.MOUTH );
	}

	public SJoint getRightEye() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.BipedResource.RIGHT_EYE );
	}

	public SJoint getLeftEye() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.BipedResource.LEFT_EYE );
	}

	public SJoint getLeftEyelid() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.BipedResource.LEFT_EYELID );
	}

	public SJoint getRightEyelid() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.BipedResource.RIGHT_EYELID );
	}

	public SJoint getRightHip() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.BipedResource.RIGHT_HIP );
	}

	public SJoint getRightKnee() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.BipedResource.RIGHT_KNEE );
	}

	public SJoint getRightAnkle() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.BipedResource.RIGHT_ANKLE );
	}

	public SJoint getRightFoot() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.BipedResource.RIGHT_FOOT );
	}

	public SJoint getLeftHip() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.BipedResource.LEFT_HIP );
	}

	public SJoint getLeftKnee() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.BipedResource.LEFT_KNEE );
	}

	public SJoint getLeftAnkle() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.BipedResource.LEFT_ANKLE );
	}

	public SJoint getLeftFoot() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.BipedResource.LEFT_FOOT );
	}

	public SJoint getRightClavicle() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.BipedResource.RIGHT_CLAVICLE );
	}

	public SJoint getRightShoulder() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.BipedResource.RIGHT_SHOULDER );
	}

	public SJoint getRightElbow() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.BipedResource.RIGHT_ELBOW );
	}

	public SJoint getRightWrist() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.BipedResource.RIGHT_WRIST );
	}

	public SJoint getRightHand() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.BipedResource.RIGHT_HAND );
	}

	public org.lgna.story.SJoint getRightThumb() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.BipedResource.RIGHT_THUMB );
	}

	public org.lgna.story.SJoint getRightThumbKnuckle() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.BipedResource.RIGHT_THUMB_KNUCKLE );
	}

	public org.lgna.story.SJoint getRightIndexFinger() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.BipedResource.RIGHT_INDEX_FINGER );
	}

	public org.lgna.story.SJoint getRightIndexFingerKnuckle() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.BipedResource.RIGHT_INDEX_FINGER_KNUCKLE );
	}

	public org.lgna.story.SJoint getRightMiddleFinger() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.BipedResource.RIGHT_MIDDLE_FINGER );
	}

	public org.lgna.story.SJoint getRightMiddleFingerKnuckle() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.BipedResource.RIGHT_MIDDLE_FINGER_KNUCKLE );
	}

	public org.lgna.story.SJoint getRightPinkyFinger() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.BipedResource.RIGHT_PINKY_FINGER );
	}

	public org.lgna.story.SJoint getRightPinkyFingerKnuckle() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.BipedResource.RIGHT_PINKY_FINGER_KNUCKLE );
	}

	public SJoint getLeftClavicle() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.BipedResource.LEFT_CLAVICLE );
	}

	public SJoint getLeftShoulder() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.BipedResource.LEFT_SHOULDER );
	}

	public SJoint getLeftElbow() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.BipedResource.LEFT_ELBOW );
	}

	public SJoint getLeftWrist() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.BipedResource.LEFT_WRIST );
	}

	public org.lgna.story.SJoint getLeftHand() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.BipedResource.LEFT_HAND );
	}

	public org.lgna.story.SJoint getLeftThumb() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.BipedResource.LEFT_THUMB );
	}

	public org.lgna.story.SJoint getLeftThumbKnuckle() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.BipedResource.LEFT_THUMB_KNUCKLE );
	}

	public org.lgna.story.SJoint getLeftIndexFinger() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.BipedResource.LEFT_INDEX_FINGER );
	}

	public org.lgna.story.SJoint getLeftIndexFingerKnuckle() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.BipedResource.LEFT_INDEX_FINGER_KNUCKLE );
	}

	public org.lgna.story.SJoint getLeftMiddleFinger() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.BipedResource.LEFT_MIDDLE_FINGER );
	}

	public org.lgna.story.SJoint getLeftMiddleFingerKnuckle() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.BipedResource.LEFT_MIDDLE_FINGER_KNUCKLE );
	}

	public org.lgna.story.SJoint getLeftPinkyFinger() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.BipedResource.LEFT_PINKY_FINGER );
	}

	public org.lgna.story.SJoint getLeftPinkyFingerKnuckle() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.BipedResource.LEFT_PINKY_FINGER_KNUCKLE );
	}

	@MethodTemplate( visibility = Visibility.COMPLETELY_HIDDEN )
	public void strikePose( org.lgna.story.BipedPose pose, StrikePose.Detail... details ) {
		super.strikePose( pose, details );
	}
}
