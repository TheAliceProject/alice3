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

/**
 * @author Dennis Cosgrove
 */
public class Biped extends Model implements Articulable {
	private final org.lgna.story.implementation.BipedImplementation implementation;
	private java.util.Map< org.lgna.story.resources.JointId, Joint > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	@Override
	/*package-private*/ org.lgna.story.implementation.BipedImplementation getImplementation() {
		return this.implementation;
	}
	public Biped( org.lgna.story.resources.BipedResource resource ) {
		this.implementation = resource.createImplementation( this );
	}
	
	public void walkTo( Entity entity ) {
	}
	public void touch( Entity entity ) {
	}
	
	public Joint getPelvisForLowerBody() {
		return org.lgna.story.Joint.getJoint( org.lgna.story.resources.BipedResource.BipedJointId.PELVIS_LOWER_BODY, this.implementation, this.map );
	}
	public Joint getPelvisForUpperBody() {
		return org.lgna.story.Joint.getJoint( org.lgna.story.resources.BipedResource.BipedJointId.PELVIS_LOWER_BODY, this.implementation, this.map );
	}

	public Joint getSpineMiddle() {
		return org.lgna.story.Joint.getJoint( org.lgna.story.resources.BipedResource.BipedJointId.SPINE_MIDDLE, this.implementation, this.map );
	}
	public Joint getSpineUpper() {
		return org.lgna.story.Joint.getJoint( org.lgna.story.resources.BipedResource.BipedJointId.SPINE_UPPER, this.implementation, this.map );
	}
	public Joint getNeck() {
		return org.lgna.story.Joint.getJoint( org.lgna.story.resources.BipedResource.BipedJointId.NECK, this.implementation, this.map );
	}
	public Joint getHead() {
		return org.lgna.story.Joint.getJoint( org.lgna.story.resources.BipedResource.BipedJointId.HEAD, this.implementation, this.map );
	}
	
	public Joint getRightHip() {
		return org.lgna.story.Joint.getJoint( org.lgna.story.resources.BipedResource.BipedJointId.RIGHT_HIP, this.implementation, this.map );
	}
	public Joint getRightKnee() {
		return org.lgna.story.Joint.getJoint( org.lgna.story.resources.BipedResource.BipedJointId.RIGHT_KNEE, this.implementation, this.map );
	}
	public Joint getRightAnkle() {
		return org.lgna.story.Joint.getJoint( org.lgna.story.resources.BipedResource.BipedJointId.RIGHT_ANKLE, this.implementation, this.map );
	}
	public Joint getLeftHip() {
		return org.lgna.story.Joint.getJoint( org.lgna.story.resources.BipedResource.BipedJointId.LEFT_HIP, this.implementation, this.map );
	}
	public Joint getLeftKnee() {
		return org.lgna.story.Joint.getJoint( org.lgna.story.resources.BipedResource.BipedJointId.LEFT_KNEE, this.implementation, this.map );
	}
	public Joint getLeftAnkle() {
		return org.lgna.story.Joint.getJoint( org.lgna.story.resources.BipedResource.BipedJointId.LEFT_ANKLE, this.implementation, this.map );
	}

	
	public Joint getRightClavicle() {
		return org.lgna.story.Joint.getJoint( org.lgna.story.resources.BipedResource.BipedJointId.RIGHT_CLAVICLE, this.implementation, this.map );
	}
	public Joint getRightShoulder() {
		return org.lgna.story.Joint.getJoint( org.lgna.story.resources.BipedResource.BipedJointId.RIGHT_SHOULDER, this.implementation, this.map );
	}
	public Joint getRightElbow() {
		return org.lgna.story.Joint.getJoint( org.lgna.story.resources.BipedResource.BipedJointId.RIGHT_ELBOW, this.implementation, this.map );
	}
	public Joint getRightWrist() {
		return org.lgna.story.Joint.getJoint( org.lgna.story.resources.BipedResource.BipedJointId.RIGHT_WRIST, this.implementation, this.map );
	}
	public Joint getLeftClavicle() {
		return org.lgna.story.Joint.getJoint( org.lgna.story.resources.BipedResource.BipedJointId.LEFT_CLAVICLE, this.implementation, this.map );
	}
	public Joint getLeftShoulder() {
		return org.lgna.story.Joint.getJoint( org.lgna.story.resources.BipedResource.BipedJointId.LEFT_SHOULDER, this.implementation, this.map );
	}
	public Joint getLeftElbow() {
		return org.lgna.story.Joint.getJoint( org.lgna.story.resources.BipedResource.BipedJointId.LEFT_ELBOW, this.implementation, this.map );
	}
	public Joint getLeftWrist() {
		return org.lgna.story.Joint.getJoint( org.lgna.story.resources.BipedResource.BipedJointId.LEFT_WRIST, this.implementation, this.map );
	}
}
