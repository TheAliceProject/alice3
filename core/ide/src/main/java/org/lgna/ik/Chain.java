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

package org.lgna.ik;

/**
 * @author Dennis Cosgrove
 */
public class Chain {
	public static Chain createInstance( org.lgna.story.implementation.JointedModelImp<?, ?> jointedModelImp, org.lgna.story.resources.JointId anchorId, org.lgna.story.resources.JointId endId, boolean isLinearEnabled, boolean isAngularEnabled ) {
		java.util.List<org.lgna.story.implementation.JointImp> jointImps = jointedModelImp.getInclusiveListOfJointsBetween( anchorId, endId );
		return new Chain( jointImps, isLinearEnabled, isAngularEnabled );
	}

	private final java.util.List<org.lgna.story.implementation.JointImp> jointImps;
	private final Bone[] bones;
	private final edu.cmu.cs.dennisc.math.Vector3 desiredEndEffectorLinearVelocity;
	private final edu.cmu.cs.dennisc.math.Vector3 desiredEndEffectorAngularVelocity;

	private Chain( java.util.List<org.lgna.story.implementation.JointImp> jointImps, boolean isLinearEnabled, boolean isAngularEnabled ) {
		this.jointImps = jointImps;
		final int N = this.jointImps.size();
		this.bones = new Bone[ N - 1 ];
		for( int i = 0; i < ( N - 1 ); i++ ) {
			this.bones[ i ] = new Bone( this, i, isLinearEnabled, isAngularEnabled );
		}
		if( isLinearEnabled ) {
			this.desiredEndEffectorLinearVelocity = edu.cmu.cs.dennisc.math.Vector3.createZero();
		} else {
			this.desiredEndEffectorLinearVelocity = null;
		}
		if( isAngularEnabled ) {
			this.desiredEndEffectorAngularVelocity = edu.cmu.cs.dennisc.math.Vector3.createZero();
		} else {
			this.desiredEndEffectorAngularVelocity = null;
		}
	}

	public Bone[] getBones() {
		return this.bones;
	}

	public org.lgna.story.implementation.JointImp getJointImpAt( int index ) {
		return this.jointImps.get( index );
	}

	private boolean isLinearVelocityEnabled() {
		return this.desiredEndEffectorLinearVelocity != null;
	}

	private boolean isAngularVelocityEnabled() {
		return this.desiredEndEffectorAngularVelocity != null;
	}

	public void setDesiredEndEffectorLinearVelocity( edu.cmu.cs.dennisc.math.Vector3 desiredEndEffectorLinearVelocity ) {
		this.desiredEndEffectorLinearVelocity.set( desiredEndEffectorLinearVelocity );
	}

	public void setDesiredEndEffectorAngularVelocity( edu.cmu.cs.dennisc.math.Vector3 desiredEndEffectorAngularVelocity ) {
		this.desiredEndEffectorAngularVelocity.set( desiredEndEffectorAngularVelocity );
	}

	private edu.cmu.cs.dennisc.math.Point3 getAnchorPosition() {
		throw new RuntimeException( "todo" );
	}

	private edu.cmu.cs.dennisc.math.Point3 getEndEffectorPosition() {
		throw new RuntimeException( "todo" );
	}

	private void computeVelocityContributions() {
		edu.cmu.cs.dennisc.math.Point3 endEffectorPos = this.getEndEffectorPosition();
		for( Bone bone : this.bones ) {
			if( this.isLinearVelocityEnabled() ) {
				edu.cmu.cs.dennisc.math.Vector3 v = edu.cmu.cs.dennisc.math.Vector3.createSubtraction(
						endEffectorPos,
						this.getAnchorPosition()
						);
				bone.updateLinearContributions( v );
			}
			if( this.isAngularVelocityEnabled() ) {
				bone.updateAngularContributions();
			}
		}
	}
}
