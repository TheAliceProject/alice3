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
package org.lgna.story;

import org.lgna.story.resources.JointId;
import org.lgna.story.resources.QuadrupedResource;

/**
 * @author Matt May
 */
public class QuadrupedPoseBuilder extends PoseBuilder<org.lgna.story.SQuadruped, QuadrupedPose> {
	public QuadrupedPoseBuilder joint( org.lgna.story.resources.JointId jointId, org.lgna.story.Orientation orientation ) {
		this.addJointIdQuaternionPair( jointId, orientation );
		return this;
	}

	public QuadrupedPoseBuilder joint( JointId jointId, Number x, Number y, Number z, Number w ) {
		this.addJointIdQuaternionPair( jointId, new Orientation( x, y, z, w ) );
		return this;
	}

	public QuadrupedPoseBuilder frontRightClavicle( Orientation orientation ) {
		return this.joint( QuadrupedResource.FRONT_RIGHT_CLAVICLE, orientation );
	}

	public QuadrupedPoseBuilder frontRightShoulder( Orientation orientation ) {
		return this.joint( QuadrupedResource.FRONT_RIGHT_SHOULDER, orientation );
	}

	public QuadrupedPoseBuilder frontRightKnee( Orientation orientation ) {
		return this.joint( QuadrupedResource.FRONT_RIGHT_KNEE, orientation );
	}

	public QuadrupedPoseBuilder frontRightAnkle( Orientation orientation ) {
		return this.joint( QuadrupedResource.FRONT_RIGHT_ANKLE, orientation );
	}

	public QuadrupedPoseBuilder backRightHip( Orientation orientation ) {
		return this.joint( QuadrupedResource.BACK_RIGHT_HIP, orientation );
	}

	public QuadrupedPoseBuilder backRightKnee( Orientation orientation ) {
		return this.joint( QuadrupedResource.BACK_RIGHT_KNEE, orientation );
	}

	public QuadrupedPoseBuilder backRightAnkle( Orientation orientation ) {
		return this.joint( QuadrupedResource.BACK_RIGHT_ANKLE, orientation );
	}

	public QuadrupedPoseBuilder frontLeftClavicle( Orientation orientation ) {
		return this.joint( QuadrupedResource.FRONT_LEFT_CLAVICLE, orientation );
	}

	public QuadrupedPoseBuilder frontLeftShoulder( Orientation orientation ) {
		return this.joint( QuadrupedResource.FRONT_LEFT_SHOULDER, orientation );
	}

	public QuadrupedPoseBuilder frontLeftKnee( Orientation orientation ) {
		return this.joint( QuadrupedResource.FRONT_LEFT_KNEE, orientation );
	}

	public QuadrupedPoseBuilder frontLeftAnkle( Orientation orientation ) {
		return this.joint( QuadrupedResource.FRONT_LEFT_ANKLE, orientation );
	}

	public QuadrupedPoseBuilder backLeftHip( Orientation orientation ) {
		return this.joint( QuadrupedResource.BACK_LEFT_HIP, orientation );
	}

	public QuadrupedPoseBuilder backLeftKnee( Orientation orientation ) {
		return this.joint( QuadrupedResource.BACK_LEFT_KNEE, orientation );
	}

	public QuadrupedPoseBuilder backLeftAnkle( Orientation orientation ) {
		return this.joint( QuadrupedResource.BACK_LEFT_ANKLE, orientation );
	}

	@Override
	protected org.lgna.story.QuadrupedPose build( org.lgna.story.implementation.JointIdTransformationPair[] buffer ) {
		return new QuadrupedPose( buffer );
	}
}
