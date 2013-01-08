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

import java.util.List;

import org.lgna.ik.IkConstants;
import org.lgna.ik.enforcer.JointedModelIkEnforcer;
import org.lgna.story.ImplementationAccessor;
import org.lgna.story.MoveDirection;
import org.lgna.story.SBiped;
import org.lgna.story.SThing;
import org.lgna.story.implementation.AsSeenBy;
import org.lgna.story.implementation.BipedImp;
import org.lgna.story.implementation.JointImp;
import org.lgna.story.implementation.JointedModelImp;
import org.lgna.story.resources.JointId;

import edu.cmu.cs.dennisc.java.util.Collections;
import edu.cmu.cs.dennisc.math.Point3;

/**
 * @author Matt May
 */
public class IKMagicWand {

	public enum Limb {
		RIGHT_ARM,
		LEFT_ARM,
		RIGHT_LEG,
		LEFT_LEG
	}

	private static double legLength;
	private static final SBiped ogre = new SBiped( org.lgna.story.resources.biped.OgreResource.GREEN );
	private static final boolean USING_OLD = true;
	private static List<JointId> defaultAnchors = Collections.newArrayList(
			( (JointImp)ImplementationAccessor.getImplementation( ogre.getRightClavicle() ) ).getJointId(),
			( (JointImp)ImplementationAccessor.getImplementation( ogre.getLeftClavicle() ) ).getJointId(),
			//			( (JointImp)ImplementationAccessor.getImplementation( ogre.getRightHip() ) ).getJointId(),
			//			( (JointImp)ImplementationAccessor.getImplementation( ogre.getLeftHip() ) ).getJointId()
			( (JointImp)ImplementationAccessor.getImplementation( ogre.getPelvis() ) ).getJointId()
			);

	//	JointId rightWrist = ;

	public static void moveChainToPointInSceneSpace( JointImp anchor, JointImp end, Point3 target ) {
		if( USING_OLD ) {
			moveUsingOldJMIKEnforcer( anchor, end, target );
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

			java.util.Map<org.lgna.ik.solver.Bone.Axis, Double> desiredSpeedForAxis = new java.util.HashMap<org.lgna.ik.solver.Bone.Axis, Double>();

			//not bad concurrent programming practice
			boolean isLinearEnabled = test.ik.croquet.IsLinearEnabledState.getInstance().getValue();
			boolean isAngularEnabled = test.ik.croquet.IsAngularEnabledState.getInstance().getValue();

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

	public static void moveChainToPointSelfSpace( JointImp anchor, JointImp end, Point3 target ) {

		JointedModelImp<?, ?> jointedParent = anchor.getJointedModelImplementation();
		Point3 targetPrime = jointedParent.getTransformation( AsSeenBy.SCENE ).createTransformed( target );
		moveChainToPointInSceneSpace( anchor, end, targetPrime );
	}

	/**
	 * bipedStrides
	 * 
	 * @return
	 */
	public static double stride( BipedImp bipedImp, Limb limb, boolean shouldBackStep ) {
		final JointImp anchor;
		final JointImp rightEnd;
		final JointImp leftEnd;
		if( limb == Limb.RIGHT_LEG ) {
			anchor = ImplementationAccessor.getImplementation( bipedImp.getAbstraction().getPelvis() );
			rightEnd = ImplementationAccessor.getImplementation( bipedImp.getAbstraction().getRightAnkle() );
			leftEnd = ImplementationAccessor.getImplementation( bipedImp.getAbstraction().getLeftAnkle() );
			double legLength = anchor.getDistanceTo( rightEnd );
			Thread backThread = new Thread() {
				@Override
				public void run() {
					moveChainToPointSelfSpace( anchor, leftEnd, new Point3( 0, 0, 0.5 ) );
				};
			};
			Thread leftStep = new Thread() {
				@Override
				public void run() {
					moveChainToPointSelfSpace( anchor, rightEnd, new Point3( 0, 0, -0.5 ) );
				};
			};
			leftStep.start();
			if( shouldBackStep ) {
				backThread.start();
				bipedImp.getAbstraction().move( MoveDirection.FORWARD, 1 );
			}
			return .5;
		}
		if( limb == Limb.LEFT_LEG ) {
			anchor = ImplementationAccessor.getImplementation( bipedImp.getAbstraction().getPelvis() );
			rightEnd = ImplementationAccessor.getImplementation( bipedImp.getAbstraction().getRightAnkle() );
			leftEnd = ImplementationAccessor.getImplementation( bipedImp.getAbstraction().getLeftAnkle() );
			double legLength = anchor.getDistanceTo( rightEnd );
			Thread backThread = new Thread() {
				@Override
				public void run() {
					moveChainToPointSelfSpace( anchor, rightEnd, new Point3( 0, 0, 0.5 ) );
				};
			};
			Thread leftStep = new Thread() {
				@Override
				public void run() {
					moveChainToPointSelfSpace( anchor, leftEnd, new Point3( 0, 0, -0.5 ) );
				};
			};
			leftStep.start();
			if( shouldBackStep ) {
				backThread.start();
				bipedImp.getAbstraction().move( MoveDirection.FORWARD, 1 );
			}
			return .5;
		}
		return 0;
	}

	private static double calcLength( JointImp anchor, JointImp end ) {
		if( end.equals( anchor ) ) {
			return 0;
		}
		Point3 endPoint = end.getAbsoluteTransformation().translation;
		Point3 parentPoint = end.getVehicle().getAbsoluteTransformation().translation;
		double dist = Point3.calculateDistanceBetween( endPoint, parentPoint );
		return dist + calcLength( anchor, (JointImp)end.getVehicle() );
	}

	public static void walkTo( final BipedImp walker, SThing target ) {
		legLength = legLength == 0 ? legLength = getLegLength( walker ) : legLength;
		IKBipedPose walkPose1 = new IKBipedPose( walker,
				new JointPositionPair( ( (JointImp)ImplementationAccessor.getImplementation( walker.getAbstraction().getRightAnkle() ) ).getJointId(), calcLegPoint( walker, 30, 1, true ) ),
				new JointPositionPair( ( (JointImp)ImplementationAccessor.getImplementation( walker.getAbstraction().getLeftAnkle() ) ).getJointId(), calcLegPoint( walker, -30, 1, false ) ) );
		IKBipedPose walkPose2 = new IKBipedPose( walker,
				new JointPositionPair( ( (JointImp)ImplementationAccessor.getImplementation( walker.getAbstraction().getRightAnkle() ) ).getJointId(), calcLegPoint( walker, 15, .95, true ) ),
				new JointPositionPair( ( (JointImp)ImplementationAccessor.getImplementation( walker.getAbstraction().getLeftAnkle() ) ).getJointId(), calcLegPoint( walker, -20, .95, false ) ) );
		IKBipedPose walkPose3 = new IKBipedPose( walker,
				new JointPositionPair( ( (JointImp)ImplementationAccessor.getImplementation( walker.getAbstraction().getRightAnkle() ) ).getJointId(), calcLegPoint( walker, 5, 1, true ) ),
				new JointPositionPair( ( (JointImp)ImplementationAccessor.getImplementation( walker.getAbstraction().getLeftAnkle() ) ).getJointId(), calcLegPoint( walker, -10, .9, false ) ) );
		IKBipedPose walkPose4 = new IKBipedPose( walker,
				new JointPositionPair( ( (JointImp)ImplementationAccessor.getImplementation( walker.getAbstraction().getRightAnkle() ) ).getJointId(), calcLegPoint( walker, 0, 1, true ) ),
				new JointPositionPair( ( (JointImp)ImplementationAccessor.getImplementation( walker.getAbstraction().getLeftAnkle() ) ).getJointId(), calcLegPoint( walker, -5, .85, false ) ) );
		IKBipedPose walkPose5 = new IKBipedPose( walker,
				new JointPositionPair( ( (JointImp)ImplementationAccessor.getImplementation( walker.getAbstraction().getRightAnkle() ) ).getJointId(), calcLegPoint( walker, -5, 1, true ) ),
				new JointPositionPair( ( (JointImp)ImplementationAccessor.getImplementation( walker.getAbstraction().getLeftAnkle() ) ).getJointId(), calcLegPoint( walker, 5, .925, false ) ) );
		IKBipedPose walkPose6 = new IKBipedPose( walker,
				new JointPositionPair( ( (JointImp)ImplementationAccessor.getImplementation( walker.getAbstraction().getRightAnkle() ) ).getJointId(), calcLegPoint( walker, -10, .95, true ) ),
				new JointPositionPair( ( (JointImp)ImplementationAccessor.getImplementation( walker.getAbstraction().getLeftAnkle() ) ).getJointId(), calcLegPoint( walker, 20, 1, false ) ) );
		IKBipedPose walkPose7 = new IKBipedPose( walker,
				new JointPositionPair( ( (JointImp)ImplementationAccessor.getImplementation( walker.getAbstraction().getRightAnkle() ) ).getJointId(), calcLegPoint( walker, -25, .9, true ) ),
				new JointPositionPair( ( (JointImp)ImplementationAccessor.getImplementation( walker.getAbstraction().getLeftAnkle() ) ).getJointId(), calcLegPoint( walker, 25, .9, false ) ) );
		IKBipedPose walkPose8 = new IKBipedPose( walker,
				new JointPositionPair( ( (JointImp)ImplementationAccessor.getImplementation( walker.getAbstraction().getRightAnkle() ) ).getJointId(), calcLegPoint( walker, -15, .85, true ) ),
				new JointPositionPair( ( (JointImp)ImplementationAccessor.getImplementation( walker.getAbstraction().getLeftAnkle() ) ).getJointId(), calcLegPoint( walker, 15, .95, false ) ) );
		IKBipedPose walkPose9 = new IKBipedPose( walker,
				new JointPositionPair( ( (JointImp)ImplementationAccessor.getImplementation( walker.getAbstraction().getRightAnkle() ) ).getJointId(), calcLegPoint( walker, -5, .825, true ) ),
				new JointPositionPair( ( (JointImp)ImplementationAccessor.getImplementation( walker.getAbstraction().getLeftAnkle() ) ).getJointId(), calcLegPoint( walker, 0, 1, false ) ) );
		IKBipedPose walkPose10 = new IKBipedPose( walker,
				new JointPositionPair( ( (JointImp)ImplementationAccessor.getImplementation( walker.getAbstraction().getRightAnkle() ) ).getJointId(), calcLegPoint( walker, 5, 0.9, true ) ),
				new JointPositionPair( ( (JointImp)ImplementationAccessor.getImplementation( walker.getAbstraction().getLeftAnkle() ) ).getJointId(), calcLegPoint( walker, -5, 1, false ) ) );
		IKBipedPose walkPose11 = new IKBipedPose( walker,
				new JointPositionPair( ( (JointImp)ImplementationAccessor.getImplementation( walker.getAbstraction().getRightAnkle() ) ).getJointId(), calcLegPoint( walker, 5, 0.9, true ) ),
				new JointPositionPair( ( (JointImp)ImplementationAccessor.getImplementation( walker.getAbstraction().getLeftAnkle() ) ).getJointId(), calcLegPoint( walker, -15, 1, false ) ) );
		for( int i = 0; i != 10; ++i ) {
			assumePose( walkPose4, walker );
			assumePose( walkPose5, walker );
			assumePose( walkPose6, walker );
			assumePose( walkPose7, walker );
			assumePose( walkPose8, walker );
			assumePose( walkPose9, walker );
			assumePose( walkPose10, walker );
			assumePose( walkPose11, walker );
			assumePose( walkPose1, walker );
			assumePose( walkPose2, walker );
			assumePose( walkPose3, walker );
		}
	}

	/**
	 * Used for Walk
	 */
	private static Point3 calcLegPoint( BipedImp walker, double theta, double length, boolean isRight ) {
		assert Math.abs( theta ) < 180;
		assert length <= 1;
		assert length > 0;
		double x = isRight ? .1 : -.1;
		double z = Math.sin( ( -theta * Math.PI ) / 180 ) * length * legLength;
		double y = z == 0 ? 0 : legLength - Math.abs( z / Math.tan( ( theta * Math.PI ) / 180 ) );
		return new Point3( x, y, z );
	}

	private static double getLegLength( BipedImp walker ) {
		Point3 pointA = ImplementationAccessor.getImplementation( walker.getAbstraction().getPelvis() ).getAbsoluteTransformation().translation;
		Point3 pointB = ImplementationAccessor.getImplementation( walker.getAbstraction().getRightAnkle() ).getAbsoluteTransformation().translation;
		return Point3.calculateDistanceBetween( pointA, pointB );
	}

	public static void assumePose( final IKBipedPose pose, final BipedImp biped ) {
		Thread rightArmThread = new Thread() {
			@Override
			public void run() {
				if( pose.getPairForLimb( Limb.RIGHT_ARM ) != org.lgna.ik.walkandtouch.IKBipedPose.DEFAULT ) {
					moveChainToPointSelfSpace( (JointImp)ImplementationAccessor.getImplementation( biped.getAbstraction().getRightClavicle() ), biped.getJointImplementation( pose.getPairForLimb( Limb.RIGHT_ARM ).joint ), pose.getPairForLimb( Limb.RIGHT_ARM ).point );
				}
			};
		};
		Thread leftArmThread = new Thread() {
			@Override
			public void run() {
				if( pose.getPairForLimb( Limb.LEFT_ARM ) != org.lgna.ik.walkandtouch.IKBipedPose.DEFAULT ) {
					moveChainToPointSelfSpace( (JointImp)ImplementationAccessor.getImplementation( pose.getModel().getAbstraction().getLeftClavicle() ), biped.getJointImplementation( pose.getPairForLimb( Limb.LEFT_ARM ).joint ), pose.getPairForLimb( Limb.LEFT_ARM ).point );
				}
			};
		};
		Thread rightLegThread = new Thread() {
			@Override
			public void run() {
				if( pose.getPairForLimb( Limb.RIGHT_LEG ) != org.lgna.ik.walkandtouch.IKBipedPose.DEFAULT ) {
					moveChainToPointSelfSpace( (JointImp)ImplementationAccessor.getImplementation( pose.getModel().getAbstraction().getPelvis() ), biped.getJointImplementation( pose.getPairForLimb( Limb.RIGHT_LEG ).joint ), pose.getPairForLimb( Limb.RIGHT_LEG ).point );
				}
			};
		};
		Thread leftLegThread = new Thread() {
			@Override
			public void run() {
				if( pose.getPairForLimb( Limb.LEFT_LEG ) != org.lgna.ik.walkandtouch.IKBipedPose.DEFAULT ) {
					moveChainToPointSelfSpace( (JointImp)ImplementationAccessor.getImplementation( pose.getModel().getAbstraction().getPelvis() ), biped.getJointImplementation( pose.getPairForLimb( Limb.LEFT_LEG ).joint ), pose.getPairForLimb( Limb.LEFT_LEG ).point );
				}
			};
		};
		rightArmThread.start();
		leftArmThread.start();
		rightLegThread.start();
		leftLegThread.start();
		while( rightArmThread.isAlive() || leftArmThread.isAlive() || rightLegThread.isAlive() || leftLegThread.isAlive() ) {
			try {
				Thread.sleep( 50 );
			} catch( InterruptedException e ) {
				e.printStackTrace();
			}
		}
	}

	public static JointId getDefaultAnchorForBipedEndJoint( JointId jointId ) {
		while( ( jointId != null ) && !defaultAnchors.contains( jointId ) ) {
			jointId = jointId.getParent();
		}
		return jointId;
	}

}
