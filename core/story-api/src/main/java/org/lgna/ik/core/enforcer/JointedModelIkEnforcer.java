package org.lgna.ik.core.enforcer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.lgna.ik.core.IkConstants;
import org.lgna.ik.core.solver.Bone;
import org.lgna.ik.core.solver.Chain;
import org.lgna.ik.core.solver.Bone.Axis;
import org.lgna.ik.core.solver.Solver.JacobianAndInverse;
import org.lgna.story.implementation.JointImp;
import org.lgna.story.resources.JointId;

import edu.cmu.cs.dennisc.math.EpsilonUtilities;
import edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector3;

public class JointedModelIkEnforcer extends IkEnforcer {

	org.lgna.ik.core.solver.Solver solver = new org.lgna.ik.core.solver.Solver();

	public JointedModelIkEnforcer( org.lgna.story.implementation.JointedModelImp<?, ?> jointedModelImp ) {
		super( jointedModelImp );
		solver.setJointWeights( weights );
	}

	Map<JointId, Map<JointId, Chain>> anchors = new HashMap<JointId, Map<JointId, Chain>>();
	Map<JointId, Map<JointId, Chain>> chainsForEes = new HashMap<JointId, Map<JointId, Chain>>();

	//this could also be by axis. actually should be... later. 
	Weights weights = new Weights();

	class DesiredPositionParameters {
		private final JointId jointId;
		private final Point3 position;
		private final double maxSpeed;

		public DesiredPositionParameters( JointId jointId, Point3 position, double maxSpeed ) {
			this.jointId = jointId;
			this.position = position;
			this.maxSpeed = maxSpeed;
		}
	}

	class DesiredOrientationParameters {
		private final JointId jointId;
		private final OrthogonalMatrix3x3 orientation;
		private final double maxSpeed;

		public DesiredOrientationParameters( JointId jointId, OrthogonalMatrix3x3 orientation, double maxSpeed ) {
			this.jointId = jointId;
			this.orientation = orientation;
			this.maxSpeed = maxSpeed;
		}
	}

	class DesiredVelocityParameters {
		private final JointId jointId;
		private final Vector3 velocity;

		public DesiredVelocityParameters( JointId jointId, Vector3 velocity ) {
			this.jointId = jointId;
			this.velocity = velocity;
		}
	}

	List<DesiredPositionParameters> currentDesiredPositions = new ArrayList<DesiredPositionParameters>();
	List<DesiredOrientationParameters> currentDesiredOrientations = new ArrayList<DesiredOrientationParameters>();
	List<DesiredVelocityParameters> currentDesiredLinearVelocities = new ArrayList<DesiredVelocityParameters>();
	List<DesiredVelocityParameters> currentDesiredAngularVelocities = new ArrayList<DesiredVelocityParameters>();

	public boolean hasActiveChain() {
		return !chainsForEes.isEmpty();
	}

	//this ignores current value of the chain end effector local point
	public void setChainBetween( org.lgna.story.resources.JointId baseId, org.lgna.story.resources.JointId eeId ) {
		Map<org.lgna.story.resources.JointId, org.lgna.ik.core.solver.Chain> ees = anchors.get( baseId );
		if( ees == null ) {
			ees = new HashMap<org.lgna.story.resources.JointId, org.lgna.ik.core.solver.Chain>();
			anchors.put( baseId, ees );
		}
		Chain chain = ees.get( eeId );
		if( chain == null ) {
			//create and use chain
			chain = createNewChain( baseId, eeId );
			ees.put( eeId, chain );

			//add to ee's chains
			Map<org.lgna.story.resources.JointId, org.lgna.ik.core.solver.Chain> chainsForEe = chainsForEes.get( eeId );
			if( chainsForEe == null ) {
				chainsForEe = new HashMap<org.lgna.story.resources.JointId, org.lgna.ik.core.solver.Chain>();
				chainsForEes.put( eeId, chainsForEe );
			}
			chainsForEe.put( baseId, chain );
			solver.addChain( chain );
		}
	}

	//can also set weights per chain, axis, etc. 
	public void setDefaultJointWeight( double weight ) {
		weights.defaultJointWeight = weight;
	}

	public void setJointWeight( JointId jointId, double weight ) {
		weights.setJointWeight( jointId, weight );
	}

	private org.lgna.ik.core.solver.Chain createNewChain( org.lgna.story.resources.JointId baseId, org.lgna.story.resources.JointId eeId ) {
		return org.lgna.ik.core.solver.Chain.createInstance( jointedModelImp, baseId, eeId );
	}

	public void clearChainBetween( JointId baseId, JointId eeId ) {
		Chain chain = null;
		Map<org.lgna.story.resources.JointId, org.lgna.ik.core.solver.Chain> ees = anchors.get( baseId );
		if( ees != null ) {
			chain = ees.get( eeId );
			ees.remove( eeId );
			if( ees.isEmpty() ) {
				anchors.remove( baseId );
			}
		}
		Map<org.lgna.story.resources.JointId, org.lgna.ik.core.solver.Chain> chainsForEe = chainsForEes.get( eeId );
		if( chainsForEe != null ) {
			chainsForEe.remove( baseId );
			if( chainsForEe.isEmpty() ) {
				chainsForEes.remove( eeId );
			}
		}
		solver.removeChain( chain );
	}

	public void setEeLocalPosition( org.lgna.story.resources.JointId eeId, edu.cmu.cs.dennisc.math.Point3 localPosition ) {
		//find the chain(s)
		//set their local positions
		Map<org.lgna.story.resources.JointId, org.lgna.ik.core.solver.Chain> chainsForEe = chainsForEes.get( eeId );
		for( Map.Entry<org.lgna.story.resources.JointId, org.lgna.ik.core.solver.Chain> e : chainsForEe.entrySet() ) {
			org.lgna.ik.core.solver.Chain chain = e.getValue();
			chain.setEndEffectorLocalPosition( localPosition );
		}
	}

	public void setEePosition( org.lgna.story.resources.JointId eeId, edu.cmu.cs.dennisc.math.Point3 position ) {
		//find the chain(s)
		//set their local positions
		Map<org.lgna.story.resources.JointId, org.lgna.ik.core.solver.Chain> chainsForEe = chainsForEes.get( eeId );
		for( Map.Entry<org.lgna.story.resources.JointId, org.lgna.ik.core.solver.Chain> e : chainsForEe.entrySet() ) {
			org.lgna.ik.core.solver.Chain chain = e.getValue();
			chain.setEndEffectorPosition( position );
		}
	}

	public void setEeDesiredLinearVelocity( org.lgna.story.resources.JointId eeId, edu.cmu.cs.dennisc.math.Vector3 linearVelocity ) {
		currentDesiredLinearVelocities.add( new DesiredVelocityParameters( eeId, linearVelocity ) );
		setEeDesiredLinearVelocityWithoutRecordingIt( eeId, linearVelocity );
	}

	public void setEeDesiredLinearVelocityWithoutRecordingIt( org.lgna.story.resources.JointId eeId, edu.cmu.cs.dennisc.math.Vector3 linearVelocity ) {
		Map<org.lgna.story.resources.JointId, org.lgna.ik.core.solver.Chain> chainsForEe = chainsForEes.get( eeId );
		for( Map.Entry<org.lgna.story.resources.JointId, org.lgna.ik.core.solver.Chain> e : chainsForEe.entrySet() ) {
			org.lgna.ik.core.solver.Chain chain = e.getValue();
			solver.setDesiredEndEffectorLinearVelocity( chain, linearVelocity );
		}
	}

	public void setEeDesiredAngularVelocity( org.lgna.story.resources.JointId eeId, edu.cmu.cs.dennisc.math.Vector3 angularVelocity ) {
		currentDesiredAngularVelocities.add( new DesiredVelocityParameters( eeId, angularVelocity ) );
		setEeDesiredAngularVelocityWithoutRecordingIt( eeId, angularVelocity );
	}

	public void setEeDesiredAngularVelocityWithoutRecordingIt( org.lgna.story.resources.JointId eeId, edu.cmu.cs.dennisc.math.Vector3 angularVelocity ) {
		Map<org.lgna.story.resources.JointId, org.lgna.ik.core.solver.Chain> chainsForEe = chainsForEes.get( eeId );
		for( Map.Entry<org.lgna.story.resources.JointId, org.lgna.ik.core.solver.Chain> e : chainsForEe.entrySet() ) {
			org.lgna.ik.core.solver.Chain chain = e.getValue();
			solver.setDesiredEndEffectorAngularVelocity( chain, angularVelocity );
		}
	}

	public Point3 getEndEffectorPosition( JointId eeId ) {
		Map<org.lgna.story.resources.JointId, org.lgna.ik.core.solver.Chain> chainsForEe = chainsForEes.get( eeId );
		for( Chain chain : chainsForEe.values() ) {
			return chain.getEndEffectorPosition();
		}
		return null;
	}

	public Point3 getAnchorPosition( JointId anchorId ) {
		Map<JointId, Chain> chainsForBase = anchors.get( anchorId );
		for( Chain chain : chainsForBase.values() ) {
			return chain.getAnchorPosition();
		}
		return null;
	}

	public void setEeDesiredPosition( org.lgna.story.resources.JointId eeId, edu.cmu.cs.dennisc.math.Point3 desiredPosition, double maxLinearSpeedForEe ) {
		currentDesiredPositions.add( new DesiredPositionParameters( eeId, desiredPosition, maxLinearSpeedForEe ) );

		setEeDesiredPositionWithoutRecordingIt( eeId, desiredPosition, maxLinearSpeedForEe );
	}

	private void setEeDesiredPositionWithoutRecordingIt( org.lgna.story.resources.JointId eeId, edu.cmu.cs.dennisc.math.Point3 desiredPosition, double maxLinearSpeedForEe ) {
		//for each chain for this ee
		Map<org.lgna.story.resources.JointId, org.lgna.ik.core.solver.Chain> chainsForEe = chainsForEes.get( eeId );
		edu.cmu.cs.dennisc.math.Vector3 eeLinearVelocityToUse = null;
		for( Map.Entry<org.lgna.story.resources.JointId, org.lgna.ik.core.solver.Chain> e : chainsForEe.entrySet() ) {
			org.lgna.ik.core.solver.Chain chain = e.getValue();

			if( eeLinearVelocityToUse == null ) {
				//get the current ee reference position in world (these all should be the same for each loop iteration)
				edu.cmu.cs.dennisc.math.Point3 eePosition = chain.getEndEffectorPosition();

				//calculate the error vector
				edu.cmu.cs.dennisc.math.Vector3 errorVector = edu.cmu.cs.dennisc.math.Vector3.createSubtraction( desiredPosition, eePosition );

				//calculate the desired velocity
				if( errorVector.calculateMagnitudeSquared() > ( maxLinearSpeedForEe * maxLinearSpeedForEe ) ) {
					errorVector.normalize();
					errorVector.multiply( maxLinearSpeedForEe );
				}
				eeLinearVelocityToUse = errorVector;
			}

			//set the desired ee velocity
			setEeDesiredLinearVelocityWithoutRecordingIt( eeId, eeLinearVelocityToUse );
		}
	}

	public void setEeDesiredOrientation( org.lgna.story.resources.JointId eeId, edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 desiredOrientation, double maxAngularSpeedForEe ) {
		currentDesiredOrientations.add( new DesiredOrientationParameters( eeId, desiredOrientation, maxAngularSpeedForEe ) );

		setEeDesiredOrientationWithoutRecordingIt( eeId, desiredOrientation, maxAngularSpeedForEe );
	}

	private void setEeDesiredOrientationWithoutRecordingIt( org.lgna.story.resources.JointId eeId, edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 desiredOrientation, double maxAngularSpeedForEe ) {
		//for each chain for this ee
		Map<org.lgna.story.resources.JointId, org.lgna.ik.core.solver.Chain> chainsForEe = chainsForEes.get( eeId );
		edu.cmu.cs.dennisc.math.Vector3 eeAngularVelocityToUse = null;
		for( Map.Entry<org.lgna.story.resources.JointId, org.lgna.ik.core.solver.Chain> e : chainsForEe.entrySet() ) {
			org.lgna.ik.core.solver.Chain chain = e.getValue();

			if( eeAngularVelocityToUse == null ) {
				//get the current ee reference position in world (these all should be the same for each loop iteration)
				edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 endEffectorOrientation = chain.getEndEffectorOrientation();

				edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 inverseCurrent = new edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3( endEffectorOrientation );
				inverseCurrent.invert();

				edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 diff = new edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3();
				diff.setToMultiplication( desiredOrientation, inverseCurrent );

				edu.cmu.cs.dennisc.math.AxisRotation diffAxisRotation = new edu.cmu.cs.dennisc.math.AxisRotation( diff );

				edu.cmu.cs.dennisc.math.Vector3 errorAngularDistance = edu.cmu.cs.dennisc.math.Vector3.createMultiplication( diffAxisRotation.axis, diffAxisRotation.angle.getAsRadians() );

				//not going to use it directly because is likely to be too fast (linear is bad approximation for large steps)

				if( errorAngularDistance.calculateMagnitude() > maxAngularSpeedForEe ) {
					errorAngularDistance.normalize();
					eeAngularVelocityToUse = edu.cmu.cs.dennisc.math.Vector3.createMultiplication( errorAngularDistance, maxAngularSpeedForEe );
				} else {
					eeAngularVelocityToUse = errorAngularDistance;
				}
			}

			//set the desired ee velocity
			setEeDesiredAngularVelocityWithoutRecordingIt( eeId, eeAngularVelocityToUse );
		}
	}

	public void advanceTimeAdaptivelyForFixedDuration( double deltaTime, double maxPseudoInverseErrorBeforeHalvingDeltaTime ) {
		double totalDeltaTimeToFill = deltaTime;
		double deltaTimePassedSoFar = 0;
		double deltaTimeAttemptingToAdvance = deltaTime;

		double minDeltaTime = deltaTime * IkConstants.ADAPTIVE_TIME_MIN_DELTA_TIME_IS_FRACTION_OF_DESIRED_DELTA_TIME;

		boolean firstTry = true;

		while( !EpsilonUtilities.isWithinReasonableEpsilon( totalDeltaTimeToFill - deltaTimePassedSoFar, 0.0 ) ) {

			if( firstTry ) {
				firstTry = false;
			} else {
				//reapply all outside commands
				reactivateDesiredPositionAndOrientations();
			}

			//should calculate the jacobian
			JacobianAndInverse jacobianAndInverse = solver.prepareAndCalculateJacobianAndInverse();

			//should calculate the error of the jacobian for the time left
			double error = solver.calculatePseudoInverseErrorForTime( jacobianAndInverse, deltaTimeAttemptingToAdvance );
			//while the error is too much, half the time and recalculate
			//			System.out.println("initerror: " + error);
			while( ( Math.abs( error ) > maxPseudoInverseErrorBeforeHalvingDeltaTime ) && ( ( deltaTimeAttemptingToAdvance * .5 ) > minDeltaTime ) ) {
				//				System.out.println("error: " + error + " abserror: "+ Math.abs(error) + " maxerror: " + maxPseudoInverseErrorBeforeHalvingDeltaTime);
				//				System.out.println("scaled down " + (Math.abs(error) > maxPseudoInverseErrorBeforeHalvingDeltaTime) + " " + (Math.abs(error) - maxPseudoInverseErrorBeforeHalvingDeltaTime));
				deltaTimeAttemptingToAdvance *= .5;
				error = solver.calculatePseudoInverseErrorForTime( jacobianAndInverse, deltaTimeAttemptingToAdvance );
			}

			//move the joints for that time
			Map<Bone, Map<Axis, Double>> jointSpeedsToUse = solver.calculateAngleSpeeds( jacobianAndInverse );

			if( IkConstants.USE_NULLSPACE_TO_MOVE_CLOSE_TO_DEFAULT_POSE && ( currentFullBodyDefaultPose != null ) ) {
				solver.addAngleSpeedsTowardsDefaultPoseInNullSpace( currentFullBodyDefaultPose, jointSpeedsToUse, jacobianAndInverse );
			}
			moveJointsWithSpeedsForTime( jointSpeedsToUse, deltaTimeAttemptingToAdvance );

			if( !EpsilonUtilities.isWithinReasonableEpsilon( deltaTimeAttemptingToAdvance, totalDeltaTimeToFill ) ) {
				//				System.out.printf("advanced %f.1 only\n", deltaTimeAttemptingToAdvance / totalDeltaTimeToFill);
				//				System.out.println("error: " + error + " abserror: "+ Math.abs(error) + " maxerror: " + maxError);
			}

			deltaTimePassedSoFar += deltaTimeAttemptingToAdvance;

			//if time is not complete, recalculate jacobian and try again
		}

		clearDesiredPositionAndOrientations();
	}

	private void clearDesiredPositionAndOrientations() {
		currentDesiredPositions.clear();
		currentDesiredOrientations.clear();
		currentDesiredLinearVelocities.clear();
		currentDesiredAngularVelocities.clear();
	}

	private void reactivateDesiredPositionAndOrientations() {
		for( DesiredPositionParameters p : currentDesiredPositions.toArray( new DesiredPositionParameters[ 0 ] ) ) {
			setEeDesiredPositionWithoutRecordingIt( p.jointId, p.position, p.maxSpeed );
		}

		for( DesiredOrientationParameters p : currentDesiredOrientations.toArray( new DesiredOrientationParameters[ 0 ] ) ) {
			setEeDesiredOrientationWithoutRecordingIt( p.jointId, p.orientation, p.maxSpeed );
		}

		for( DesiredVelocityParameters p : currentDesiredLinearVelocities.toArray( new DesiredVelocityParameters[ 0 ] ) ) {
			setEeDesiredLinearVelocityWithoutRecordingIt( p.jointId, p.velocity );
		}

		for( DesiredVelocityParameters p : currentDesiredAngularVelocities.toArray( new DesiredVelocityParameters[ 0 ] ) ) {
			setEeDesiredAngularVelocityWithoutRecordingIt( p.jointId, p.velocity );
		}
	}

	public void advanceTimeStaticallyForFixedDuration( double deltaTime ) {
		Map<Bone, Map<Axis, Double>> jointSpeedsToUse = solver.solve();

		moveJointsWithSpeedsForTime( jointSpeedsToUse, deltaTime );

		clearDesiredPositionAndOrientations();
	}

	private void moveJointsWithSpeedsForTime( Map<Bone, Map<Axis, Double>> jointSpeedsToUse, double deltaTime ) {
		if( jointSpeedsToUse == null ) {
			System.err.println( "Joint speeds were null. Could not advance time." );
			return;
		}

		boolean doThreeSeparateRotationsWhichIsNotDesired = false;
		for( Entry<Bone, Map<Axis, Double>> eb : jointSpeedsToUse.entrySet() ) {
			Bone bone = eb.getKey();
			Map<Axis, Double> jointSpeedsForBone = eb.getValue();

			JointId jointId = bone.getA().getJointId();

			double weight = getEffectiveWeightForJoint( jointId );

			if( doThreeSeparateRotationsWhichIsNotDesired ) {
				for( Entry<Axis, Double> ea : jointSpeedsForBone.entrySet() ) {
					Axis axis = ea.getKey();
					Double speed = ea.getValue();
					axis.applyRotation( deltaTime * speed * weight );
				}
			} else {
				Vector3 cumulativeAxisAngle = Vector3.createZero();
				//				System.out.println( "=========" );
				for( Entry<Axis, Double> ea : jointSpeedsForBone.entrySet() ) {
					Axis axis = ea.getKey();
					Double speed = ea.getValue();

					//					System.out.println( "ea: " + ea );
					//					System.out.println( "ea.key: " + ea.getKey() );
					//					System.out.println( "speed: " + speed );
					Vector3 contribution = Vector3.createMultiplication( axis.getLocalAxis(), deltaTime * speed * weight );
					//					System.out.println( "cumulativeAxisAngle: " + cumulativeAxisAngle );
					//					System.out.println( "contribution: " + contribution );
					cumulativeAxisAngle.add( contribution );
				}
				//				System.out.println( "=========" );

				//				System.out.println( "cumulativeAxisAngle: " + cumulativeAxisAngle );
				double angle = cumulativeAxisAngle.calculateMagnitude();
				if( !EpsilonUtilities.isWithinReasonableEpsilon( 0, angle ) ) {
					Vector3 axis = Vector3.createDivision( cumulativeAxisAngle, angle );
					bone.applyLocalRotation( axis, angle );
				}

			}
		}
	}

	private double getEffectiveWeightForJoint( JointId jointId ) {
		return weights.getEffectiveJointWeight( jointId );
	}

	public Chain getChainForPrinting( JointId anchorId, JointId eeId ) {
		return anchors.get( anchorId ).get( eeId );
	}

	public void advanceTime( double deltaTime ) {
		if( IkConstants.USE_ADAPTIVE_TIME ) {
			advanceTimeAdaptivelyForFixedDuration( deltaTime, IkConstants.MAX_PSEUDO_INVERSE_ERROR_BEFORE_HALVING_DELTA_TIME );
		} else {
			advanceTimeStaticallyForFixedDuration( deltaTime );
		}
	}

	Map<JointImp, OrthogonalMatrix3x3> currentFullBodyDefaultPose;

	//TODO there should be options to add default poses for specific chains as well
	public void addFullBodyDefaultPoseUsingCurrentPose() {
		//normally, chains tell their bones to update their state from their implementations
		//what we want to do is to go to the jointedmodelimp and get all the values that can later be recalled using chains by solver
		//first implement the part where you pull defaults for chains. that will determine how I get it. 
		HashMap<JointImp, OrthogonalMatrix3x3> fullBodyDefaultPose = new HashMap<JointImp, OrthogonalMatrix3x3>();

		Iterable<JointImp> joints = jointedModelImp.getJoints();
		for( JointImp jointImp : joints ) {
			fullBodyDefaultPose.put( jointImp, new OrthogonalMatrix3x3( jointImp.getLocalOrientation() ) );
		}
		currentFullBodyDefaultPose = fullBodyDefaultPose;
	}

}
