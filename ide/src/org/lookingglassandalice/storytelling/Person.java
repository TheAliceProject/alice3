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

package org.lookingglassandalice.storytelling;

/**
 * @author Dennis Cosgrove
 */
public abstract class Person extends Model implements Walker, Toucher {
	private final org.lookingglassandalice.storytelling.implementation.PersonImplementation implementation;
	@Override
	/*package-private*/ org.lookingglassandalice.storytelling.implementation.PersonImplementation getImplementation() {
		return this.implementation;
	}
	public Person( org.lookingglassandalice.storytelling.resources.PersonResource resource ) {
		this.implementation = resource.createPersonImplementation( this );
	}
	
	public void walkTo( Entity entity ) {
	}
	public void touch( Entity entity ) {
	}
	
	private java.util.Map< org.lookingglassandalice.storytelling.resources.JointId, Joint > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private Joint getJoint( org.lookingglassandalice.storytelling.resources.JointId jointId ) {
		synchronized( this.map ) {
			Joint rv = this.map.get( jointId );
			if( rv != null ) {
				//pass
			} else {
				rv = org.lookingglassandalice.storytelling.Joint.getInstance( this.implementation, jointId );
			}
			return rv;
		}
	}

	public Joint getPelvisForLowerBody() {
		return this.getJoint( org.lookingglassandalice.storytelling.resources.PersonResource.PersonJointId.PELVIS_LOWER_BODY );
	}
	public Joint getPelvisForUpperBody() {
		return this.getJoint( org.lookingglassandalice.storytelling.resources.PersonResource.PersonJointId.PELVIS_LOWER_BODY );
	}

	public Joint getSpineMiddle() {
		return this.getJoint( org.lookingglassandalice.storytelling.resources.PersonResource.PersonJointId.SPINE_MIDDLE );
	}
	public Joint getSpineUpper() {
		return this.getJoint( org.lookingglassandalice.storytelling.resources.PersonResource.PersonJointId.SPINE_UPPER );
	}
	public Joint getNeck() {
		return this.getJoint( org.lookingglassandalice.storytelling.resources.PersonResource.PersonJointId.NECK );
	}
	public Joint getHead() {
		return this.getJoint( org.lookingglassandalice.storytelling.resources.PersonResource.PersonJointId.HEAD );
	}
	
	public Joint getRightHip() {
		return this.getJoint( org.lookingglassandalice.storytelling.resources.PersonResource.PersonJointId.RIGHT_HIP );
	}
	public Joint getRightKnee() {
		return this.getJoint( org.lookingglassandalice.storytelling.resources.PersonResource.PersonJointId.RIGHT_KNEE );
	}
	public Joint getRightAnkle() {
		return this.getJoint( org.lookingglassandalice.storytelling.resources.PersonResource.PersonJointId.RIGHT_ANKLE );
	}
	public Joint getLeftHip() {
		return this.getJoint( org.lookingglassandalice.storytelling.resources.PersonResource.PersonJointId.LEFT_HIP );
	}
	public Joint getLeftKnee() {
		return this.getJoint( org.lookingglassandalice.storytelling.resources.PersonResource.PersonJointId.LEFT_KNEE );
	}
	public Joint getLeftAnkle() {
		return this.getJoint( org.lookingglassandalice.storytelling.resources.PersonResource.PersonJointId.LEFT_ANKLE );
	}

	
	public Joint getRightClavicle() {
		return this.getJoint( org.lookingglassandalice.storytelling.resources.PersonResource.PersonJointId.RIGHT_CLAVICLE );
	}
	public Joint getRightShoulder() {
		return this.getJoint( org.lookingglassandalice.storytelling.resources.PersonResource.PersonJointId.RIGHT_SHOULDER );
	}
	public Joint getRightElbow() {
		return this.getJoint( org.lookingglassandalice.storytelling.resources.PersonResource.PersonJointId.RIGHT_ELBOW );
	}
	public Joint getRightWrist() {
		return this.getJoint( org.lookingglassandalice.storytelling.resources.PersonResource.PersonJointId.RIGHT_WRIST );
	}
	public Joint getLeftClavicle() {
		return this.getJoint( org.lookingglassandalice.storytelling.resources.PersonResource.PersonJointId.LEFT_CLAVICLE );
	}
	public Joint getLeftShoulder() {
		return this.getJoint( org.lookingglassandalice.storytelling.resources.PersonResource.PersonJointId.LEFT_SHOULDER );
	}
	public Joint getLeftElbow() {
		return this.getJoint( org.lookingglassandalice.storytelling.resources.PersonResource.PersonJointId.LEFT_ELBOW );
	}
	public Joint getLeftWrist() {
		return this.getJoint( org.lookingglassandalice.storytelling.resources.PersonResource.PersonJointId.LEFT_WRIST );
	}
}
