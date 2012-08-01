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

import org.lgna.project.annotations.*;

/**
 * @author Dennis Cosgrove
 */
public class SBiped extends SJointedModel implements Articulable {
	private final org.lgna.story.implementation.BipedImp implementation;
	public SBiped( org.lgna.story.resources.BipedResource resource ) {
		this.implementation = resource.createImplementation( this );
	}
	@Override
	/*package-private*/ org.lgna.story.implementation.BipedImp getImplementation() {
		return this.implementation;
	}
	
	@MethodTemplate(visibility=Visibility.TUCKED_AWAY)
	public void walkTo( SThing entity ) {
		javax.swing.JOptionPane.showMessageDialog( null, "todo: walkTo" );
	}
	@MethodTemplate(visibility=Visibility.TUCKED_AWAY)
	public void touch( SThing entity ) {
		javax.swing.JOptionPane.showMessageDialog( null, "todo: touch" );
	}
	
	@MethodTemplate(visibility=Visibility.TUCKED_AWAY)
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
	
	public SJoint getRightHip() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.BipedResource.RIGHT_HIP );
	}
	public SJoint getRightKnee() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.BipedResource.RIGHT_KNEE );
	}
	public SJoint getRightAnkle() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.BipedResource.RIGHT_ANKLE );
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
}
