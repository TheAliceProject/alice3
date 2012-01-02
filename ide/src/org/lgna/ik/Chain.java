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

package org.lgna.ik;

import java.util.List;

import org.lgna.story.implementation.JointImp;

/**
 * @author Dennis Cosgrove
 */
public class Chain {
	
	// Chain includes all the joints that will turn
	
	public static Chain createInstance( org.lgna.story.implementation.JointedModelImp< ?,? > jointedModelImp, org.lgna.story.resources.JointId anchorId, org.lgna.story.resources.JointId endId, boolean isLinearEnabled, boolean isAngularEnabled ) {
		java.util.List< org.lgna.ik.Bone.Direction > directions = new java.util.ArrayList< org.lgna.ik.Bone.Direction >();
		java.util.List< org.lgna.story.implementation.JointImp > jointImps = jointedModelImp.getInclusiveListOfJointsBetween( anchorId, endId, directions );
		return new Chain( jointImps, directions, isLinearEnabled, isAngularEnabled );
	}

	private final java.util.List< org.lgna.story.implementation.JointImp > jointImps;
	private final Bone[] bones;
	private final edu.cmu.cs.dennisc.math.Vector3 desiredEndEffectorLinearVelocity;
	private final edu.cmu.cs.dennisc.math.Vector3 desiredEndEffectorAngularVelocity;
	private final edu.cmu.cs.dennisc.math.Point3 endEffectorLocalPosition;
	private final List< org.lgna.ik.Bone.Direction > directions;
	
	private final java.util.Map< org.lgna.ik.Bone.Axis, edu.cmu.cs.dennisc.math.Vector3 > linearVelocityContributions;
	private final java.util.Map< org.lgna.ik.Bone.Axis, edu.cmu.cs.dennisc.math.Vector3 > angularVelocityContributions;
	
	private Chain( java.util.List< org.lgna.story.implementation.JointImp > jointImps, List< org.lgna.ik.Bone.Direction > directions, boolean isLinearEnabled, boolean isAngularEnabled ) {
		this.jointImps = jointImps;
		this.directions = directions;
		
		if( isLinearEnabled ) {
			this.desiredEndEffectorLinearVelocity = edu.cmu.cs.dennisc.math.Vector3.createZero();
			this.endEffectorLocalPosition = edu.cmu.cs.dennisc.math.Point3.createZero();
			this.linearVelocityContributions = new java.util.HashMap< org.lgna.ik.Bone.Axis, edu.cmu.cs.dennisc.math.Vector3 >();
		} else {
			this.desiredEndEffectorLinearVelocity = null;
			this.endEffectorLocalPosition = null;
			this.linearVelocityContributions = null;
		}
		if( isAngularEnabled ) {
			this.desiredEndEffectorAngularVelocity = edu.cmu.cs.dennisc.math.Vector3.createZero();
			this.angularVelocityContributions = new java.util.HashMap< org.lgna.ik.Bone.Axis, edu.cmu.cs.dennisc.math.Vector3 >();
		} else {
			this.desiredEndEffectorAngularVelocity = null;
			this.angularVelocityContributions = null;
		}
		
		final int N = this.jointImps.size();
		this.bones = new Bone[ N ];
		for( int i=0; i < N; i++ ) {
			Bone newBone = new Bone( this, i, isLinearEnabled, isAngularEnabled );
			this.bones[ i ] = newBone;
			if( isLinearEnabled ) {
				for( org.lgna.ik.Bone.Axis axis: newBone.getAxes() ) {
					linearVelocityContributions.put(axis, edu.cmu.cs.dennisc.math.Vector3.createZero());
				}
			}
			if( isAngularEnabled ) {
				for( org.lgna.ik.Bone.Axis axis: newBone.getAxes() ) {
					angularVelocityContributions.put(axis, edu.cmu.cs.dennisc.math.Vector3.createZero());
				}
			}
		}
	}
	
	//if this is not called, in linear motions, you will be moving the anchor of the last joint
	//this helps you move the tip of the finger
	public void setEndEffectorPosition(edu.cmu.cs.dennisc.math.Point3 eePosition) {
		org.lgna.story.implementation.JointImp eeJointImp = jointImps.get(jointImps.size() - 1);
		
		//get the world eePosition local to eeJointImp
		edu.cmu.cs.dennisc.math.AffineMatrix4x4 eeJointInverse = edu.cmu.cs.dennisc.math.AffineMatrix4x4.createInverse(eeJointImp.getTransformation(org.lgna.story.implementation.AsSeenBy.SCENE));
		
		eeJointInverse.setReturnValueToTransformed(endEffectorLocalPosition, eePosition);
	}
	
	public Bone[] getBones() {
		return this.bones;
	}
	
	public org.lgna.story.implementation.JointImp getJointImpAt( int index ) {
		return this.jointImps.get( index );
	}

	public org.lgna.ik.Bone.Direction getDirectionAt( int index ) {
		return this.directions.get( index );
	}

	public boolean isLinearVelocityEnabled() {
		return this.desiredEndEffectorLinearVelocity != null;
	}
	public boolean isAngularVelocityEnabled() {
		return this.desiredEndEffectorAngularVelocity != null;
	}
	public void setDesiredEndEffectorLinearVelocity( edu.cmu.cs.dennisc.math.Vector3 desiredEndEffectorLinearVelocity ) {
		this.desiredEndEffectorLinearVelocity.set( desiredEndEffectorLinearVelocity );
	}
	public void setDesiredEndEffectorAngularVelocity( edu.cmu.cs.dennisc.math.Vector3 desiredEndEffectorAngularVelocity ) {
		this.desiredEndEffectorAngularVelocity.set( desiredEndEffectorAngularVelocity );
	}
	public edu.cmu.cs.dennisc.math.Vector3 getDesiredEndEffectorLinearVelocity() {
		return desiredEndEffectorLinearVelocity;
	}
	public edu.cmu.cs.dennisc.math.Vector3 getDesiredEndEffectorAngularVelocity() {
		return desiredEndEffectorAngularVelocity;
	}
	
	// these could be local or world. does it matter? it could only matter if you don't move the end effector target with the character.
	// so, these are world
	public edu.cmu.cs.dennisc.math.Point3 getEndEffectorPosition() {
		org.lgna.story.implementation.JointImp eeJointImp = jointImps.get(jointImps.size() - 1);
		return eeJointImp.getTransformation(org.lgna.story.implementation.AsSeenBy.SCENE).setReturnValueToTransformed(new edu.cmu.cs.dennisc.math.Point3(), endEffectorLocalPosition);
	}
	
	public void computeVelocityContributions() {
		edu.cmu.cs.dennisc.math.Point3 endEffectorPos = this.getEndEffectorPosition();
		for( Bone bone : this.bones ) {
			bone.updateStateFromJoint();
			
			if( this.isLinearVelocityEnabled() ) {
				edu.cmu.cs.dennisc.math.Vector3 v = edu.cmu.cs.dennisc.math.Vector3.createSubtraction( 
						endEffectorPos, 
						bone.getAnchorPosition()
				); 
				bone.updateLinearContributions( v );
				
				
				//axis has inverse value. then contrib should also be inversed when merging here?
				// no. it's good like this. 
				
				for( org.lgna.ik.Bone.Axis axis: bone.getAxes() ) {
					linearVelocityContributions.put( axis, axis.getLinearContribution() );
				}
			}
			
			if( this.isAngularVelocityEnabled() ) {
				bone.updateAngularContributions();
				
				for( org.lgna.ik.Bone.Axis axis: bone.getAxes() ) {
					angularVelocityContributions.put( axis, axis.getAngularContribution() );
				}
			}
		}

		//TODO(gazi) updated the unit contributions of each. now need to use them. 
		//all the axes on the bones know their own contributions
		//if there was another chain, they would also know their own contributions
		//therefore, contributions from multiple chains should be taken as a (concrete joint, axis index (axis class has these two))->(contribution) map
		//then, they should be aligned by axes (ordering in bone is good, contribs are independent of that), empty places should be filled with zeros and a matrix should be formed.  
//		throw new RuntimeException("todo first resolve the joint direction issue");
		
		//should be fine now
	}
	
	//these are axis->vel
	//however axis reference is not the unique thing. it's the (joint,axis index) pair
	public java.util.Map< org.lgna.ik.Bone.Axis, edu.cmu.cs.dennisc.math.Vector3 > getLinearVelocityContributions() {
		return linearVelocityContributions;
	}
	
	public java.util.Map< org.lgna.ik.Bone.Axis, edu.cmu.cs.dennisc.math.Vector3 > getAngularVelocityContributions() {
		return angularVelocityContributions;
	}

	public JointImp getLastJointImp() {
		return jointImps.get(jointImps.size() - 1);
	}
}
