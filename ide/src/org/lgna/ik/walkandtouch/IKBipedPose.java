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
package org.lgna.ik.walkandtouch;

import java.util.ArrayList;
import java.util.Map;

import org.lgna.ik.walkandtouch.IKMagicWand.Limb;
import org.lgna.story.ImplementationAccessor;
import org.lgna.story.SBiped;
import org.lgna.story.implementation.BipedImp;
import org.lgna.story.implementation.JointImp;
import org.lgna.story.resources.JointId;

import edu.cmu.cs.dennisc.java.util.Collections;

/**
 * @author Matt May
 */
public class IKBipedPose {

	public static final JointPositionPair DEFAULT = new JointPositionPair( null, null );
	//	private InitializingIfAbsentListHashMap<IKMagicWand.Limb, JointPositionPair> map = new InitializingIfAbsentListHashMap<IKMagicWand.Limb, JointPositionPair>();
	private Map<IKMagicWand.Limb, JointPositionPair> limbToJointPosition = Collections.newHashMap();
	private BipedImp model;
	private static ArrayList<JointId> rightArmJoints;
	private static ArrayList<JointId> leftArmJoints;
	private static ArrayList<JointId> rightLegJoints;
	private static ArrayList<JointId> leftLegJoints;

	public IKBipedPose( BipedImp model, JointPositionPair... limbPositionPairs ) {
		assert limbPositionPairs.length < 5;
		this.model = model;
		if( leftLegJoints == null ) {
			initJointArr( model );
		}
		for( JointPositionPair joint : limbPositionPairs ) {
			if( rightArmJoints.contains( joint.joint ) ) {
				assert limbToJointPosition.get( Limb.RIGHT_ARM ) == null;
				limbToJointPosition.put( Limb.RIGHT_ARM, joint );
			}
			if( rightLegJoints.contains( joint.joint ) ) {
				assert limbToJointPosition.get( Limb.RIGHT_LEG ) == null;
				limbToJointPosition.put( Limb.RIGHT_LEG, joint );
			}
			if( leftArmJoints.contains( joint.joint ) ) {
				assert limbToJointPosition.get( Limb.LEFT_ARM ) == null;
				limbToJointPosition.put( Limb.LEFT_ARM, joint );
			}
			if( leftLegJoints.contains( joint.joint ) ) {
				assert limbToJointPosition.get( Limb.LEFT_LEG ) == null;
				limbToJointPosition.put( Limb.LEFT_LEG, joint );
			}
		}
		if( limbToJointPosition.get( Limb.RIGHT_ARM ) == null ) {
			limbToJointPosition.put( Limb.RIGHT_ARM, DEFAULT );
		}
		if( limbToJointPosition.get( Limb.RIGHT_LEG ) == null ) {
			limbToJointPosition.put( Limb.RIGHT_LEG, DEFAULT );
		}
		if( limbToJointPosition.get( Limb.LEFT_ARM ) == null ) {
			limbToJointPosition.put( Limb.LEFT_ARM, DEFAULT );
		}
		if( limbToJointPosition.get( Limb.LEFT_LEG ) == null ) {
			limbToJointPosition.put( Limb.LEFT_LEG, DEFAULT );
		}
	}

	private void initJointArr( BipedImp model ) {
		SBiped abstraction = model.getAbstraction();
		rightArmJoints = new ArrayList<JointId>();
		leftArmJoints = new ArrayList<JointId>();
		rightLegJoints = new ArrayList<JointId>();
		leftLegJoints = new ArrayList<JointId>();
		rightArmJoints.add( ( (JointImp)ImplementationAccessor.getImplementation( abstraction.getRightClavicle() ) ).getJointId() );
		rightArmJoints.add( ( (JointImp)ImplementationAccessor.getImplementation( abstraction.getRightShoulder() ) ).getJointId() );
		rightArmJoints.add( ( (JointImp)ImplementationAccessor.getImplementation( abstraction.getRightShoulder() ) ).getJointId() );
		rightArmJoints.add( ( (JointImp)ImplementationAccessor.getImplementation( abstraction.getRightWrist() ) ).getJointId() );
		rightArmJoints.add( ( (JointImp)ImplementationAccessor.getImplementation( abstraction.getRightHand() ) ).getJointId() );
		leftArmJoints.add( ( (JointImp)ImplementationAccessor.getImplementation( abstraction.getLeftClavicle() ) ).getJointId() );
		leftArmJoints.add( ( (JointImp)ImplementationAccessor.getImplementation( abstraction.getLeftShoulder() ) ).getJointId() );
		leftArmJoints.add( ( (JointImp)ImplementationAccessor.getImplementation( abstraction.getLeftShoulder() ) ).getJointId() );
		leftArmJoints.add( ( (JointImp)ImplementationAccessor.getImplementation( abstraction.getLeftWrist() ) ).getJointId() );
		leftArmJoints.add( ( (JointImp)ImplementationAccessor.getImplementation( abstraction.getLeftHand() ) ).getJointId() );
		rightLegJoints.add( ( (JointImp)ImplementationAccessor.getImplementation( abstraction.getRightHip() ) ).getJointId() );
		rightLegJoints.add( ( (JointImp)ImplementationAccessor.getImplementation( abstraction.getRightKnee() ) ).getJointId() );
		rightLegJoints.add( ( (JointImp)ImplementationAccessor.getImplementation( abstraction.getRightAnkle() ) ).getJointId() );
		rightLegJoints.add( ( (JointImp)ImplementationAccessor.getImplementation( abstraction.getRightFoot() ) ).getJointId() );
		leftLegJoints.add( ( (JointImp)ImplementationAccessor.getImplementation( abstraction.getLeftHip() ) ).getJointId() );
		leftLegJoints.add( ( (JointImp)ImplementationAccessor.getImplementation( abstraction.getLeftKnee() ) ).getJointId() );
		leftLegJoints.add( ( (JointImp)ImplementationAccessor.getImplementation( abstraction.getLeftAnkle() ) ).getJointId() );
		leftLegJoints.add( ( (JointImp)ImplementationAccessor.getImplementation( abstraction.getLeftFoot() ) ).getJointId() );
	}

	public JointPositionPair getPairForLimb( Limb limb ) {
		JointPositionPair rv = limbToJointPosition.get( limb );
		assert rv != null;
		return rv;
	}

	public BipedImp getModel() {
		return this.model;
	}
}
