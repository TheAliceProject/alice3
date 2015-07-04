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
package org.lgna.ik.core;

import java.util.List;

import org.lgna.ik.core.enforcer.JointedModelIkEnforcer;
import org.lgna.ik.core.enforcer.TightPositionalIkEnforcer;
import org.lgna.ik.core.enforcer.TightPositionalIkEnforcer.PositionConstraint;
import org.lgna.story.EmployeesOnly;
import org.lgna.story.SBiped;
import org.lgna.story.implementation.JointImp;
import org.lgna.story.implementation.JointedModelImp;
import org.lgna.story.resources.JointId;

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.math.Point3;

/**
 * @author Matt May
 */
public class IKCore {

	public enum Limb {
		RIGHT_ARM,
		LEFT_ARM,
		RIGHT_LEG,
		LEFT_LEG
	}

	private static final SBiped ogre = new SBiped( org.lgna.story.resources.biped.OgreResource.GREEN );
	private static final boolean USING_OLD = false;
	private static List<JointId> defaultAnchors = Lists.newArrayList(
			( (JointImp)EmployeesOnly.getImplementation( ogre.getRightShoulder() ) ).getJointId(),
			( (JointImp)EmployeesOnly.getImplementation( ogre.getLeftShoulder() ) ).getJointId(),
			( (JointImp)EmployeesOnly.getImplementation( ogre.getRightHip() ) ).getJointId(),
			( (JointImp)EmployeesOnly.getImplementation( ogre.getLeftHip() ) ).getJointId()
			);

	public static void moveChainToPointInSceneSpace( JointImp anchor, JointImp end, Point3 target ) {
		//this because anchor does not behave as expected...
		anchor = anchor.getJointedModelImplementation().getJointImplementation( anchor.getJointId().getParent() );
		if( USING_OLD ) {
			moveUsingOldJMIKEnforcer( anchor, end, target );
		} else {
			moveUsingNewTPIKEnforcer( anchor, end, target );
		}
	}

	private static void moveUsingNewTPIKEnforcer( JointImp anchor, JointImp end, Point3 target ) {
		target = correctTarget( anchor, end, target );
		JointedModelImp<?, ?> jointedParent = anchor.getJointedModelImplementation();
		final TightPositionalIkEnforcer tightIkEnforcer = new TightPositionalIkEnforcer( jointedParent );

		//TODO do what's below to complete it
		// set its chain
		int level = 0;
		org.lgna.story.resources.JointId endId = end.getJointId();
		org.lgna.story.resources.JointId anchorId = anchor.getJointId();
		PositionConstraint myPositionConstraint = tightIkEnforcer.createPositionConstraint( level, anchorId, endId );

		//				System.out.println("will start");
		//				try {
		//					System.in.read();
		//				} catch (IOException e1) {
		//					// TODO Auto-generated catch block
		//					e1.printStackTrace();
		//				}
		//		while( true ) {

		//not bad concurrent programming practice
		//these could be multiple. in this app it is one pair.

		//					edu.cmu.cs.dennisc.math.AffineMatrix4x4 targetTransformation = getTargetImp().getTransformation( org.lgna.story.implementation.AsSeenBy.SCENE );
		myPositionConstraint.setEeDesiredPosition( target );

		//					//this is a little weird. I'd better let the enforcer create and hold the constraint, and I should hold a pointer to it for myself.
		//					for(PositionConstraint positionConstraint: constraints.activePositionConstraints) {
		//						//should it be like this, or should constraints read them automatically?
		//							//IK system reads joint angles automatically anyway
		//							//but these desired position/orientations are not necessarily tied to scenegraph stuff. I should give them myself like this. 
		//						positionConstraint.setEeDesiredPosition(targetTransformation.translation);
		//						
		//						//force bone reprint
		//						//this should be fine even if the chain is not valid anymore.
		//						//this would prevent me from selecting the list
		////						javax.swing.SwingUtilities.invokeLater(new Runnable() {
		////							public void run() {
		////								test.ik.croquet.BonesState.getInstance().setChain( ikEnforcer.getChainForPrinting(anchorId, eeId) );
		////							}
		////						});
		//					}
		//					
		//					for(OrientationConstraint orientationConstraint: constraints.activeOrientationConstraints) {
		//						System.out.println("orientaiton constraint!");
		//						orientationConstraint.setEeDesiredOrientation(targetTransformation.orientation);
		//					} 
		//perhaps better ways of setting constraint values?

		//this enforces the constraints immediately right now. so, there is no talk about deltatime or speed
		//had I had a maximum rotational speed for joints, then having time would make sense
		tightIkEnforcer.enforceConstraints();
		//			break;

		//		}

	}

	private static Point3 correctTarget( JointImp anchor, JointImp end, Point3 target ) {
		double lengthOfLimb = getLengthOfLimb( anchor, end );
		Point3 vec = Point3.createSubtraction( target, anchor.getAbsoluteTransformation().translation );
		if( lengthOfLimb < vec.calculateMagnitude() ) {
			vec.setToDivision( vec, vec.calculateMagnitude() );
			vec.setToMultiplication( vec, lengthOfLimb );
			return Point3.createAddition( anchor.getAbsoluteTransformation().translation, vec );
		}
		return target;
	}

	private static double getLengthOfLimb( JointImp anchor, JointImp end ) {
		JointedModelImp model = end.getJointedModelParent();
		if( !end.getJointId().equals( anchor.getJointId() ) ) {
			JointImp parent = model.getJointImplementation( end.getJointId().getParent() );
			return end.getDistanceTo( parent ) + getLengthOfLimb( anchor, parent );
		} else {
			return 0;
		}
	}

	private static void moveUsingOldJMIKEnforcer( JointImp anchor, JointImp end, Point3 target ) {
		JointedModelImp<?, ?> jointedParent = anchor.getJointedModelImplementation();
		JointedModelIkEnforcer enforcer = new JointedModelIkEnforcer( jointedParent );
		enforcer.addFullBodyDefaultPoseUsingCurrentPose();
		enforcer.setDefaultJointWeight( 1 );
		enforcer.setJointWeight( org.lgna.story.resources.BipedResource.RIGHT_ELBOW, 1 );
		JointId anchorId = anchor.getJointId();
		JointId eeId = end.getJointId();
		enforcer.setChainBetween( anchorId, eeId );
		Point3 currTransformation = end.getTransformation( org.lgna.story.implementation.AsSeenBy.SCENE ).translation;
		Point3 prevTransformation = new Point3( Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE );
		double delta = .001;//arbitrary
		while( Point3.calculateDistanceBetween( currTransformation, prevTransformation ) > delta ) {
			prevTransformation = currTransformation;
			//solver has the chain. can also have multiple chains. 
			//I can tell solver, for this chain this is the linear target, etc. 
			//it actually only needs the velocity, etc. then, I should say for this chain this is the desired velocity. ok. 

			java.util.Map<org.lgna.ik.core.solver.Bone.Axis, Double> desiredSpeedForAxis = new java.util.HashMap<org.lgna.ik.core.solver.Bone.Axis, Double>();

			//not bad concurrent programming practice
			boolean isLinearEnabled = true; //todo: test.ik.croquet.IsLinearEnabledState.getInstance().getValue();
			boolean isAngularEnabled = true; //todo: test.ik.croquet.IsAngularEnabledState.getInstance().getValue();

			//these could be multiple. in this app it is one pair.
			//			final JointId eeId = test.ik.croquet.EndJointIdState.getInstance().getValue();
			//			final JointId anchorId = test.ik.croquet.AnchorJointIdState.getInstance().getValue();

			double maxLinearSpeedForEe = IkConstants.MAX_LINEAR_SPEED_FOR_EE;

			double deltaTime = IkConstants.DESIRED_DELTA_TIME;
			if( enforcer.hasActiveChain() && ( isLinearEnabled || isAngularEnabled ) ) {
				//I could make chain setter not race with this
				//However, racing is fine, as long as the old chain is still valid. It is.  
				//				if( isLinearEnabled ) {
				enforcer.setEeDesiredPosition( eeId, target, maxLinearSpeedForEe );
				//				}

				//				if( isAngularEnabled ) {
				//					enforcer.setEeDesiredOrientation( eeId, targetTransformation.orientation, maxAngularSpeedForEe );
				//				}

				enforcer.advanceTime( deltaTime );

				//this is for display of gazi's sphere's only
				//				Point3 ep = enforcer.getEndEffectorPosition( eeId );
				//				Point3 ap = enforcer.getAnchorPosition( anchorId );
				//				scene.anchor.setPositionRelativeToVehicle( new Position( ap.x, ap.y, ap.z ) );
				//				scene.ee.setPositionRelativeToVehicle( new Position( ep.x, ep.y, ep.z ) );

				//force bone reprint
				//this should be fine even if the chain is not valid anymore.
				//						javax.swing.SwingUtilities.invokeLater(new Runnable() {
				//							public void run() {
				//								//this would prevent me from selecting the list
				////								test.ik.croquet.BonesState.getInstance().setChain( ikEnforcer.getChainForPrinting(anchorId, eeId) );
				//								//this would throw java.lang.IllegalStateException: Attempt to mutate in notification
				////								updateInfo();
				//							}
				//						});
			}

			currTransformation = end.getTransformation( org.lgna.story.implementation.AsSeenBy.SCENE ).translation;
		}
	}

	public static JointId getDefaultAnchorForBipedEndJoint( JointId jointId ) {
		while( ( jointId != null ) && !defaultAnchors.contains( jointId ) ) {
			jointId = jointId.getParent();
		}
		return jointId;
	}

}
