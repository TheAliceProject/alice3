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

import org.lgna.project.annotations.MethodTemplate;
import org.lgna.project.annotations.Visibility;

/**
 * @author dculyba
 */
public class SQuadruped extends SJointedModel implements Articulable {
	private final org.lgna.story.implementation.QuadrupedImp implementation;

	@Override
			/* package-private */org.lgna.story.implementation.QuadrupedImp getImplementation() {
		return this.implementation;
	}

	public SQuadruped( org.lgna.story.resources.QuadrupedResource resource ) {
		this.implementation = resource.createImplementation( this );
	}

	@Override
	@MethodTemplate( visibility = Visibility.TUCKED_AWAY )
	public void walkTo( SThing entity ) {
		javax.swing.JOptionPane.showMessageDialog( null, "todo: walkTo" );
	}

	@Override
	@MethodTemplate( visibility = Visibility.TUCKED_AWAY )
	public void touch( SThing entity ) {
		javax.swing.JOptionPane.showMessageDialog( null, "todo: touch" );
	}

	@MethodTemplate( visibility = Visibility.TUCKED_AWAY )
	public SJoint getRoot() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.QuadrupedResource.ROOT );
	}

	public SJoint getSpineBase() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.QuadrupedResource.SPINE_BASE );
	}

	public SJoint getSpineMiddle() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.QuadrupedResource.SPINE_MIDDLE );
	}

	public SJoint getSpineUpper() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.QuadrupedResource.SPINE_UPPER );
	}

	public SJoint getNeck() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.QuadrupedResource.NECK );
	}

	public SJoint getHead() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.QuadrupedResource.HEAD );
	}

	public SJoint getLeftEye() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.QuadrupedResource.LEFT_EYE );
	}

	public SJoint getLeftEyelid() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.QuadrupedResource.LEFT_EYELID );
	}

	public SJoint getLeftEar() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.QuadrupedResource.LEFT_EAR );
	}

	public SJoint getMouth() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.QuadrupedResource.MOUTH );
	}

	public SJoint getRightEar() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.QuadrupedResource.RIGHT_EAR );
	}

	public SJoint getRightEye() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.QuadrupedResource.RIGHT_EYE );
	}

	public SJoint getRightEyelid() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.QuadrupedResource.RIGHT_EYELID );
	}

	public SJoint getFrontLeftClavicle() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.QuadrupedResource.FRONT_LEFT_CLAVICLE );
	}

	public SJoint getFrontLeftShoulder() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.QuadrupedResource.FRONT_LEFT_SHOULDER );
	}

	public SJoint getFrontLeftKnee() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.QuadrupedResource.FRONT_LEFT_KNEE );
	}

	public SJoint getFrontLeftAnkle() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.QuadrupedResource.FRONT_LEFT_ANKLE );
	}

	public SJoint getFrontLeftFoot() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.QuadrupedResource.FRONT_LEFT_FOOT );
	}

	public SJoint getFrontLeftToe() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.QuadrupedResource.FRONT_LEFT_TOE );
	}

	public SJoint getFrontRightClavicle() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.QuadrupedResource.FRONT_RIGHT_CLAVICLE );
	}

	public SJoint getFrontRightShoulder() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.QuadrupedResource.FRONT_RIGHT_SHOULDER );
	}

	public SJoint getFrontRightKnee() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.QuadrupedResource.FRONT_RIGHT_KNEE );
	}

	public SJoint getFrontRightAnkle() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.QuadrupedResource.FRONT_RIGHT_ANKLE );
	}

	public SJoint getFrontRightFoot() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.QuadrupedResource.FRONT_RIGHT_FOOT );
	}

	public SJoint getFrontRightToe() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.QuadrupedResource.FRONT_RIGHT_TOE );
	}

	public SJoint getPelvisLowerBody() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.QuadrupedResource.PELVIS_LOWER_BODY );
	}

	public SJoint[] getTailArray() {
		return org.lgna.story.SJoint.getJointArray( this, this.getImplementation().getResource().getTailArray() );
	}

	public SJoint getTail() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.QuadrupedResource.TAIL_0 );
	}

	@MethodTemplate( visibility = Visibility.COMPLETELY_HIDDEN )
	@Deprecated
	public SJoint getTail1() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.QuadrupedResource.TAIL_0 );
	}

	@MethodTemplate( visibility = Visibility.COMPLETELY_HIDDEN )
	@Deprecated
	public SJoint getTail2() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.QuadrupedResource.TAIL_1 );
	}

	@MethodTemplate( visibility = Visibility.COMPLETELY_HIDDEN )
	@Deprecated
	public SJoint getTail3() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.QuadrupedResource.TAIL_2 );
	}

	@MethodTemplate( visibility = Visibility.COMPLETELY_HIDDEN )
	@Deprecated
	public SJoint getTail4() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.QuadrupedResource.TAIL_3 );
	}

	public SJoint getBackLeftHip() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.QuadrupedResource.BACK_LEFT_HIP );
	}

	public SJoint getBackLeftKnee() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.QuadrupedResource.BACK_LEFT_KNEE );
	}

	public SJoint getBackLeftHock() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.QuadrupedResource.BACK_LEFT_HOCK );
	}

	public SJoint getBackLeftAnkle() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.QuadrupedResource.BACK_LEFT_ANKLE );
	}

	public SJoint getBackLeftFoot() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.QuadrupedResource.BACK_LEFT_FOOT );
	}

	public SJoint getBackLeftToe() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.QuadrupedResource.BACK_LEFT_TOE );
	}

	public SJoint getBackRightHip() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.QuadrupedResource.BACK_RIGHT_HIP );
	}

	public SJoint getBackRightKnee() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.QuadrupedResource.BACK_RIGHT_KNEE );
	}

	public SJoint getBackRightHock() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.QuadrupedResource.BACK_RIGHT_HOCK );
	}

	public SJoint getBackRightAnkle() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.QuadrupedResource.BACK_RIGHT_ANKLE );
	}

	public SJoint getBackRightFoot() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.QuadrupedResource.BACK_RIGHT_FOOT );
	}

	public SJoint getBackRightToe() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.QuadrupedResource.BACK_RIGHT_TOE );
	}

	@MethodTemplate( visibility = Visibility.COMPLETELY_HIDDEN )
	public void strikePose( org.lgna.story.QuadrupedPose pose, StrikePose.Detail... details ) {
		super.strikePose( pose, details );
	}
}
