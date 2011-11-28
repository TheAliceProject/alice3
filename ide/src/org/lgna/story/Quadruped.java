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
public class Quadruped extends JointedModel implements Articulable {
	private final org.lgna.story.implementation.QuadrupedImp implementation;
	@Override
	/*package-private*/ org.lgna.story.implementation.QuadrupedImp getImplementation() {
		return this.implementation;
	}
	public Quadruped( org.lgna.story.resources.QuadrupedResource resource ) {
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
	
	public Joint getPelvisUpperBody() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.QuadrupedResource.PELVIS_UPPER_BODY);
	}
	public Joint getSpineMiddle() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.QuadrupedResource.SPINE_MIDDLE);
	}
	public Joint getSpineUpper() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.QuadrupedResource.SPINE_UPPER);
	}
	public Joint getNeck() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.QuadrupedResource.NECK);
	}
	public Joint getHead() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.QuadrupedResource.HEAD);
	}
	public Joint getLeftEye() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.QuadrupedResource.LEFT_EYE);
	}
	public Joint getLeftEar() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.QuadrupedResource.LEFT_EAR);
	}
	public Joint getMouth() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.QuadrupedResource.MOUTH);
	}
	public Joint getJawTip() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.QuadrupedResource.JAW_TIP);
	}
	public Joint getRightEar() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.QuadrupedResource.RIGHT_EAR);
	}
	public Joint getRightEye() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.QuadrupedResource.RIGHT_EYE);
	}
	public Joint getFrontLeftScapula() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.QuadrupedResource.FRONT_LEFT_SCAPULA);
	}
	public Joint getFrontLeftShoulder() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.QuadrupedResource.FRONT_LEFT_SHOULDER);
	}
	public Joint getFrontLeftKnee() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.QuadrupedResource.FRONT_LEFT_KNEE);
	}
	public Joint getFrontLeftAnkle() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.QuadrupedResource.FRONT_LEFT_ANKLE);
	}
	public Joint getFrontLeftBall() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.QuadrupedResource.FRONT_LEFT_BALL);
	}
	public Joint getFrontLeftToe() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.QuadrupedResource.FRONT_LEFT_TOE);
	}
	public Joint getFrontRightScapula() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.QuadrupedResource.FRONT_RIGHT_SCAPULA);
	}
	public Joint getFrontRightShoulder() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.QuadrupedResource.FRONT_RIGHT_SHOULDER);
	}
	public Joint getFrontRightKnee() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.QuadrupedResource.FRONT_RIGHT_KNEE);
	}
	public Joint getFrontRightAnkle() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.QuadrupedResource.FRONT_RIGHT_ANKLE);
	}
	public Joint getFrontRightBall() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.QuadrupedResource.FRONT_RIGHT_BALL);
	}
	public Joint getFrontRightToe() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.QuadrupedResource.FRONT_RIGHT_TOE);
	}
	public Joint getPelvisLowerBody() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.QuadrupedResource.PELVIS_LOWER_BODY);
	}
	public Joint getTail1() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.QuadrupedResource.TAIL_1);
	}
	public Joint getTail2() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.QuadrupedResource.TAIL_2);
	}
	public Joint getTail3() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.QuadrupedResource.TAIL_3);
	}
	public Joint getTail4() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.QuadrupedResource.TAIL_4);
	}
	public Joint getBackLeftHip() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.QuadrupedResource.BACK_LEFT_HIP);
	}
	public Joint getBackLeftKnee() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.QuadrupedResource.BACK_LEFT_KNEE);
	}
	public Joint getBackLeftAnkle() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.QuadrupedResource.BACK_LEFT_ANKLE);
	}
	public Joint getBackLeftBall() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.QuadrupedResource.BACK_LEFT_BALL);
	}
	public Joint getBackLeftToe() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.QuadrupedResource.BACK_LEFT_TOE);
	}
	public Joint getBackRightHip() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.QuadrupedResource.BACK_RIGHT_HIP);
	}
	public Joint getBackRightKnee() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.QuadrupedResource.BACK_RIGHT_KNEE);
	}
	public Joint getBackRightAnkle() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.QuadrupedResource.BACK_RIGHT_ANKLE);
	}
	public Joint getBackRightBall() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.QuadrupedResource.BACK_RIGHT_BALL);
	}
	public Joint getBackRightToe() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.QuadrupedResource.BACK_RIGHT_TOE);
	}
}
