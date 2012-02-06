/*
 * Copyright (c) 2006-2011, Carnegie Mellon University. All rights reserved.
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
 * @author dculyba
 */
public class Flyer extends JointedModel implements Articulable {
	private final org.lgna.story.implementation.FlyerImp implementation;
	@Override
	/*package-private*/ org.lgna.story.implementation.FlyerImp getImplementation() {
		return this.implementation;
	}
	public Flyer( org.lgna.story.resources.FlyerResource resource ) {
		this.implementation = resource.createImplementation( this );
	}
	
	@MethodTemplate(visibility=Visibility.TUCKED_AWAY)
	public void walkTo( Entity entity ) {
		javax.swing.JOptionPane.showMessageDialog( null, "todo: walkTo" );
	}
	@MethodTemplate(visibility=Visibility.TUCKED_AWAY)
	public void touch( Entity entity ) {
		javax.swing.JOptionPane.showMessageDialog( null, "todo: touch" );
	}
	@MethodTemplate(visibility=Visibility.TUCKED_AWAY)
	public Joint getRoot() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.FlyerResource.ROOT);
	}
	public Joint getSpineBase() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.FlyerResource.SPINE_BASE);
	}
	public Joint getSpineMiddle() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.FlyerResource.SPINE_MIDDLE);
	}
	public Joint getSpineUpper() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.FlyerResource.SPINE_UPPER);
	}
	public Joint getNeck() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.FlyerResource.NECK);
	}
	public Joint getHead() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.FlyerResource.HEAD);
	}
	public Joint getMouth() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.FlyerResource.MOUTH);
	}
	public Joint getLowerLip() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.FlyerResource.LOWER_LIP);
	}
	public Joint getLeftEye() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.FlyerResource.LEFT_EYE);
	}
	public Joint getRightEye() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.FlyerResource.RIGHT_EYE);
	}
	public Joint getLeftClavicle() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.FlyerResource.LEFT_CLAVICLE);
	}
	public Joint getLeftShoulder() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.FlyerResource.LEFT_SHOULDER);
	}
	public Joint getLeftElbow() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.FlyerResource.LEFT_ELBOW);
	}
	public Joint getLeftWrist() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.FlyerResource.LEFT_WRIST);
	}
	public Joint getRightClavicle() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.FlyerResource.RIGHT_CLAVICLE);
	}
	public Joint getRightShoulder() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.FlyerResource.RIGHT_SHOULDER);
	}
	public Joint getRightElbow() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.FlyerResource.RIGHT_ELBOW);
	}
	public Joint getRightWrist() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.FlyerResource.RIGHT_WRIST);
	}
	public Joint getPelvisLowerBody() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.FlyerResource.PELVIS_LOWER_BODY);
	}
//	public Joint getTail1() {
//		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.FlyerResource.TAIL_1);
//	}
//	public Joint getTail2() {
//		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.FlyerResource.TAIL_2);
//	}
//	public Joint getTail3() {
//		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.FlyerResource.TAIL_3);
//	}
	public Joint getLeftHip() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.FlyerResource.LEFT_HIP);
	}
	public Joint getLeftKnee() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.FlyerResource.LEFT_KNEE);
	}
	public Joint getLeftAnkle() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.FlyerResource.LEFT_ANKLE);
	}
	public Joint getLeftFoot() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.FlyerResource.LEFT_FOOT);
	}
//	public Joint getLeftToe() {
//		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.FlyerResource.LEFT_TOE);
//	}
	public Joint getRightHip() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.FlyerResource.RIGHT_HIP);
	}
	public Joint getRightKnee() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.FlyerResource.RIGHT_KNEE);
	}
	public Joint getRightAnkle() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.FlyerResource.RIGHT_ANKLE);
	}
	public Joint getRightFoot() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.FlyerResource.RIGHT_FOOT);
	}
//	public Joint getRightToe() {
//		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.FlyerResource.RIGHT_TOE);
//	}
}
