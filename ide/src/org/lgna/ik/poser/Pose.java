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
package org.lgna.ik.poser;

import org.lgna.story.ImplementationAccessor;
import org.lgna.story.SBiped;
import org.lgna.story.SJoint;
import org.lgna.story.implementation.JointImp;
import org.lgna.story.resources.JointId;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.UnitQuaternion;

/**
 * @author Matt May
 */
public class Pose {
	private static JointId getIDFor( SJoint sJoint ) {
		return ( (JointImp)ImplementationAccessor.getImplementation( sJoint ) ).getJointId();
	}

	private static AffineMatrix4x4 getOrientation( SJoint sJoint ) {
		return ( (JointImp)ImplementationAccessor.getImplementation( sJoint ) ).getLocalTransformation();
	}

	private static UnitQuaternion getUnitQuaternion( SJoint sJoint ) {
		return new UnitQuaternion( ( (JointImp)ImplementationAccessor.getImplementation( sJoint ) ).getLocalTransformation().orientation );
	}

	private static void setOrientationOnly( SJoint sJoint, OrthogonalMatrix3x3 matrix ) {
		( (JointImp)ImplementationAccessor.getImplementation( sJoint ) ).setLocalOrientation( matrix );
	}

	private static void setOrientationOnly( SJoint sJoint, UnitQuaternion unitQuaternion ) {
		( (JointImp)ImplementationAccessor.getImplementation( sJoint ) ).setLocalOrientation( new OrthogonalMatrix3x3( unitQuaternion ) );
	}

	/* package-private */static Pose createPoseFromBiped( SBiped biped ) {
		JointQPair rightArmBase = new JointQPair( getIDFor( biped.getRightClavicle() ), getUnitQuaternion( biped.getRightClavicle() ),
				new JointQPair( getIDFor( biped.getRightShoulder() ), getUnitQuaternion( biped.getRightShoulder() ),
						new JointQPair( getIDFor( biped.getRightElbow() ), getUnitQuaternion( biped.getRightElbow() ),
								new JointQPair( getIDFor( biped.getRightWrist() ), getUnitQuaternion( biped.getRightWrist() ) ) ) ) );

		JointQPair leftArmBase = new JointQPair( getIDFor( biped.getLeftClavicle() ), getUnitQuaternion( biped.getLeftClavicle() ),
				new JointQPair( getIDFor( biped.getLeftShoulder() ), getUnitQuaternion( biped.getLeftShoulder() ),
						new JointQPair( getIDFor( biped.getLeftElbow() ), getUnitQuaternion( biped.getLeftElbow() ),
								new JointQPair( getIDFor( biped.getLeftWrist() ), getUnitQuaternion( biped.getLeftWrist() ) ) ) ) );

		JointQPair rightLegBase = new JointQPair( getIDFor( biped.getPelvis() ), getUnitQuaternion( biped.getPelvis() ),
				new JointQPair( getIDFor( biped.getRightHip() ), getUnitQuaternion( biped.getRightHip() ),
						new JointQPair( getIDFor( biped.getRightKnee() ), getUnitQuaternion( biped.getRightKnee() ),
								new JointQPair( getIDFor( biped.getRightAnkle() ), getUnitQuaternion( biped.getRightAnkle() ) ) ) ) );

		JointQPair leftLegBase = new JointQPair( getIDFor( biped.getPelvis() ), getUnitQuaternion( biped.getPelvis() ),
				new JointQPair( getIDFor( biped.getLeftHip() ), getUnitQuaternion( biped.getLeftHip() ),
						new JointQPair( getIDFor( biped.getLeftKnee() ), getUnitQuaternion( biped.getLeftKnee() ),
								new JointQPair( getIDFor( biped.getLeftAnkle() ), getUnitQuaternion( biped.getLeftAnkle() ) ) ) ) );

		return new Pose( rightArmBase, rightLegBase, leftArmBase, leftLegBase );
	}

	//	private static AffineMatrix4x4 TODO_DELETE_AND_USE_QUATERNION( edu.cmu.cs.dennisc.math.UnitQuaternion q ) {
	//		return new AffineMatrix4x4( q, new edu.cmu.cs.dennisc.math.Point3() );
	//	}

	private static edu.cmu.cs.dennisc.math.UnitQuaternion getQuaternion( org.lgna.story.Orientation orientation ) {
		//todo: org.lgna.story.Orientation should store the UnitQuaternion?
		return ImplementationAccessor.getOrthogonalMatrix3x3( orientation ).createUnitQuaternion();
	}

	public static class BuilderWithQuaternions {
		private JointQPair rightArmBase;
		private JointQPair leftArmBase;
		private JointQPair rightLegBase;
		private JointQPair leftLegBase;

		public BuilderWithQuaternions rightArm( UnitQuaternion rightClavicleUnitQuaternion, UnitQuaternion rightShoulderUnitQuaternion, UnitQuaternion rightElbowUnitQuaternion, UnitQuaternion rightWristUnitQuaternion ) {
			assert this.rightArmBase == null : this;
			assert rightClavicleUnitQuaternion != null : this;
			assert rightShoulderUnitQuaternion != null : this;
			assert rightElbowUnitQuaternion != null : this;
			assert rightWristUnitQuaternion != null : this;
			this.rightArmBase =
					new JointQPair( org.lgna.story.resources.BipedResource.RIGHT_CLAVICLE, rightClavicleUnitQuaternion,
							new JointQPair( org.lgna.story.resources.BipedResource.RIGHT_SHOULDER, rightShoulderUnitQuaternion,
									new JointQPair( org.lgna.story.resources.BipedResource.RIGHT_ELBOW, rightElbowUnitQuaternion,
											new JointQPair( org.lgna.story.resources.BipedResource.RIGHT_WRIST, rightWristUnitQuaternion ) ) ) );
			return this;
		}

		public BuilderWithQuaternions leftArm( UnitQuaternion leftClavicleUnitQuaternion, UnitQuaternion leftShoulderUnitQuaternion, UnitQuaternion leftElbowUnitQuaternion, UnitQuaternion leftWristUnitQuaternion ) {
			assert this.leftArmBase == null : this;
			assert leftClavicleUnitQuaternion != null : this;
			assert leftShoulderUnitQuaternion != null : this;
			assert leftElbowUnitQuaternion != null : this;
			assert leftWristUnitQuaternion != null : this;
			this.leftArmBase =
					new JointQPair( org.lgna.story.resources.BipedResource.LEFT_CLAVICLE, leftClavicleUnitQuaternion,
							new JointQPair( org.lgna.story.resources.BipedResource.LEFT_SHOULDER, leftShoulderUnitQuaternion,
									new JointQPair( org.lgna.story.resources.BipedResource.LEFT_ELBOW, leftElbowUnitQuaternion,
											new JointQPair( org.lgna.story.resources.BipedResource.LEFT_WRIST, leftWristUnitQuaternion ) ) ) );
			return this;
		}

		public BuilderWithQuaternions rightLeg( UnitQuaternion pelvisUnitQuaternion, UnitQuaternion rightHipUnitQuaternion, UnitQuaternion rightKneeUnitQuaternion, UnitQuaternion rightAnkleUnitQuaternion ) {
			assert this.rightLegBase == null : this;
			assert pelvisUnitQuaternion != null : this;
			assert rightHipUnitQuaternion != null : this;
			assert rightKneeUnitQuaternion != null : this;
			assert rightAnkleUnitQuaternion != null : this;
			this.rightLegBase =
					new JointQPair( org.lgna.story.resources.BipedResource.PELVIS_LOWER_BODY, pelvisUnitQuaternion,
							new JointQPair( org.lgna.story.resources.BipedResource.RIGHT_HIP, rightHipUnitQuaternion,
									new JointQPair( org.lgna.story.resources.BipedResource.RIGHT_KNEE, rightKneeUnitQuaternion,
											new JointQPair( org.lgna.story.resources.BipedResource.RIGHT_ANKLE, rightAnkleUnitQuaternion ) ) ) );
			return this;
		}

		public BuilderWithQuaternions leftLeg( UnitQuaternion pelvisUnitQuaternion, UnitQuaternion leftHipUnitQuaternion, UnitQuaternion leftKneeUnitQuaternion, UnitQuaternion leftAnkleUnitQuaternion ) {
			assert this.leftLegBase == null : this;
			assert pelvisUnitQuaternion != null : this;
			assert leftHipUnitQuaternion != null : this;
			assert leftKneeUnitQuaternion != null : this;
			assert leftAnkleUnitQuaternion != null : this;
			this.leftLegBase =
					new JointQPair( org.lgna.story.resources.BipedResource.PELVIS_LOWER_BODY, pelvisUnitQuaternion,
							new JointQPair( org.lgna.story.resources.BipedResource.LEFT_HIP, leftHipUnitQuaternion,
									new JointQPair( org.lgna.story.resources.BipedResource.LEFT_KNEE, leftKneeUnitQuaternion,
											new JointQPair( org.lgna.story.resources.BipedResource.LEFT_ANKLE, leftAnkleUnitQuaternion ) ) ) );
			return this;
		}

		public Pose build() {
			return new Pose( this.rightArmBase, this.rightLegBase, this.leftArmBase, this.leftLegBase );
		}
	}

	public static class Builder {
		private JointQPair rightArmBase;
		private JointQPair leftArmBase;
		private JointQPair rightLegBase;
		private JointQPair leftLegBase;

		public Builder rightArm( org.lgna.story.Orientation rightClavicleOrientation, org.lgna.story.Orientation rightShoulderOrientation, org.lgna.story.Orientation rightElbowOrientation, org.lgna.story.Orientation rightWristOrientation ) {
			assert this.rightArmBase == null : this;
			assert rightClavicleOrientation != null : this;
			assert rightShoulderOrientation != null : this;
			assert rightElbowOrientation != null : this;
			assert rightWristOrientation != null : this;
			this.rightArmBase =
					new JointQPair( org.lgna.story.resources.BipedResource.RIGHT_CLAVICLE, getQuaternion( rightClavicleOrientation ),
							new JointQPair( org.lgna.story.resources.BipedResource.RIGHT_SHOULDER, getQuaternion( rightShoulderOrientation ),
									new JointQPair( org.lgna.story.resources.BipedResource.RIGHT_ELBOW, getQuaternion( rightElbowOrientation ),
											new JointQPair( org.lgna.story.resources.BipedResource.RIGHT_WRIST, getQuaternion( rightWristOrientation ) ) ) ) );
			return this;
		}

		public Builder leftArm( org.lgna.story.Orientation leftClavicleOrientation, org.lgna.story.Orientation leftShoulderOrientation, org.lgna.story.Orientation leftElbowOrientation, org.lgna.story.Orientation leftWristOrientation ) {
			assert this.leftArmBase == null : this;
			assert leftClavicleOrientation != null : this;
			assert leftShoulderOrientation != null : this;
			assert leftElbowOrientation != null : this;
			assert leftWristOrientation != null : this;
			this.leftArmBase =
					new JointQPair( org.lgna.story.resources.BipedResource.LEFT_CLAVICLE, getQuaternion( leftClavicleOrientation ),
							new JointQPair( org.lgna.story.resources.BipedResource.LEFT_SHOULDER, getQuaternion( leftShoulderOrientation ),
									new JointQPair( org.lgna.story.resources.BipedResource.LEFT_ELBOW, getQuaternion( leftElbowOrientation ),
											new JointQPair( org.lgna.story.resources.BipedResource.LEFT_WRIST, getQuaternion( leftWristOrientation ) ) ) ) );
			return this;
		}

		public Builder rightLeg( org.lgna.story.Orientation pelvisOrientation, org.lgna.story.Orientation rightHipOrientation, org.lgna.story.Orientation rightKneeOrientation, org.lgna.story.Orientation rightAnkleOrientation ) {
			assert this.rightLegBase == null : this;
			assert pelvisOrientation != null : this;
			assert rightHipOrientation != null : this;
			assert rightKneeOrientation != null : this;
			assert rightAnkleOrientation != null : this;
			this.rightLegBase =
					new JointQPair( org.lgna.story.resources.BipedResource.PELVIS_LOWER_BODY, getQuaternion( pelvisOrientation ),
							new JointQPair( org.lgna.story.resources.BipedResource.RIGHT_HIP, getQuaternion( rightHipOrientation ),
									new JointQPair( org.lgna.story.resources.BipedResource.RIGHT_KNEE, getQuaternion( rightKneeOrientation ),
											new JointQPair( org.lgna.story.resources.BipedResource.RIGHT_ANKLE, getQuaternion( rightAnkleOrientation ) ) ) ) );
			return this;
		}

		public Builder leftLeg( org.lgna.story.Orientation pelvisOrientation, org.lgna.story.Orientation leftHipOrientation, org.lgna.story.Orientation leftKneeOrientation, org.lgna.story.Orientation leftAnkleOrientation ) {
			assert this.leftLegBase == null : this;
			assert pelvisOrientation != null : this;
			assert leftHipOrientation != null : this;
			assert leftKneeOrientation != null : this;
			assert leftAnkleOrientation != null : this;
			this.leftLegBase =
					new JointQPair( org.lgna.story.resources.BipedResource.PELVIS_LOWER_BODY, getQuaternion( pelvisOrientation ),
							new JointQPair( org.lgna.story.resources.BipedResource.LEFT_HIP, getQuaternion( leftHipOrientation ),
									new JointQPair( org.lgna.story.resources.BipedResource.LEFT_KNEE, getQuaternion( leftKneeOrientation ),
											new JointQPair( org.lgna.story.resources.BipedResource.LEFT_ANKLE, getQuaternion( leftAnkleOrientation ) ) ) ) );
			return this;
		}

		public Pose build() {
			return new Pose( this.rightArmBase, this.rightLegBase, this.leftArmBase, this.leftLegBase );
		}
	}

	private final JointQPair rightArmBase;
	private final JointQPair leftArmBase;
	private final JointQPair rightLegBase;
	private final JointQPair leftLegBase;

	public Pose( JointQPair raBase, JointQPair rlBase, JointQPair laBase, JointQPair llBase ) {
		this.rightArmBase = raBase;
		this.rightLegBase = rlBase;
		this.leftArmBase = laBase;
		this.leftLegBase = llBase;
	}

	public void applyToBiped( SBiped biped ) {
		setOrientationOnly( biped.getRightClavicle(), getRightClavicle().getUnitQuaternion() );
		setOrientationOnly( biped.getRightShoulder(), getRightShoulder().getUnitQuaternion() );
		setOrientationOnly( biped.getRightElbow(), getRightElbow().getUnitQuaternion() );
		setOrientationOnly( biped.getRightWrist(), getRightWrist().getUnitQuaternion() );
		setOrientationOnly( biped.getLeftClavicle(), getLeftClavicle().getUnitQuaternion() );
		setOrientationOnly( biped.getLeftShoulder(), getLeftShoulder().getUnitQuaternion() );
		setOrientationOnly( biped.getLeftElbow(), getLeftElbow().getUnitQuaternion() );
		setOrientationOnly( biped.getLeftWrist(), getLeftWrist().getUnitQuaternion() );
		setOrientationOnly( biped.getPelvis(), getPelvis().getUnitQuaternion() );
		setOrientationOnly( biped.getRightHip(), getRightHip().getUnitQuaternion() );
		setOrientationOnly( biped.getRightKnee(), getRightKnee().getUnitQuaternion() );
		setOrientationOnly( biped.getRightAnkle(), getRightAnkle().getUnitQuaternion() );
		setOrientationOnly( biped.getLeftHip(), getLeftHip().getUnitQuaternion() );
		setOrientationOnly( biped.getLeftKnee(), getLeftKnee().getUnitQuaternion() );
		setOrientationOnly( biped.getLeftAnkle(), getLeftAnkle().getUnitQuaternion() );
	}

	private static org.lgna.story.Orientation createOrientation( edu.cmu.cs.dennisc.math.UnitQuaternion q ) {
		return new org.lgna.story.Orientation( q.x, q.y, q.z, q.w );
	}

	public org.lgna.story.Orientation getRightClavicleOrientation() {
		return createOrientation( this.rightArmBase.getUnitQuaternion() );
	}

	public org.lgna.story.Orientation getRightShoulderOrientation() {
		return createOrientation( this.rightArmBase.getChild().getUnitQuaternion() );
	}

	public org.lgna.story.Orientation getRightElbowOrientation() {
		return createOrientation( this.rightArmBase.getChild().getChild().getUnitQuaternion() );
	}

	public org.lgna.story.Orientation getRightWristOrientation() {
		return createOrientation( this.rightArmBase.getChild().getChild().getChild().getUnitQuaternion() );
	}

	public org.lgna.story.Orientation getLeftClavicleOrientation() {
		return createOrientation( this.leftArmBase.getUnitQuaternion() );
	}

	public org.lgna.story.Orientation getLeftShoulderOrientation() {
		return createOrientation( this.leftArmBase.getChild().getUnitQuaternion() );
	}

	public org.lgna.story.Orientation getLeftElbowOrientation() {
		return createOrientation( this.leftArmBase.getChild().getChild().getUnitQuaternion() );
	}

	public org.lgna.story.Orientation getLeftWristOrientation() {
		return createOrientation( this.leftArmBase.getChild().getChild().getChild().getUnitQuaternion() );
	}

	public org.lgna.story.Orientation getPelvisOrientation() {
		return createOrientation( this.rightLegBase.getUnitQuaternion() );
	}

	public org.lgna.story.Orientation getRightHipOrientation() {
		return createOrientation( this.rightLegBase.getChild().getUnitQuaternion() );
	}

	public org.lgna.story.Orientation getRightKneeOrientation() {
		return createOrientation( this.rightLegBase.getChild().getChild().getUnitQuaternion() );
	}

	public org.lgna.story.Orientation getRightAnkleOrientation() {
		return createOrientation( this.rightLegBase.getChild().getChild().getChild().getUnitQuaternion() );
	}

	public org.lgna.story.Orientation getLeftHipOrientation() {
		return createOrientation( this.leftLegBase.getChild().getUnitQuaternion() );
	}

	public org.lgna.story.Orientation getLeftKneeOrientation() {
		return createOrientation( this.leftLegBase.getChild().getChild().getUnitQuaternion() );
	}

	public org.lgna.story.Orientation getLeftAnkleOrientation() {
		return createOrientation( this.leftLegBase.getChild().getChild().getChild().getUnitQuaternion() );
	}

	public JointQPair getRightClavicle() {
		return this.rightArmBase;
	}

	public JointQPair getRightShoulder() {
		return this.rightArmBase.getChild();
	}

	public JointQPair getRightElbow() {
		return this.rightArmBase.getChild().getChild();
	}

	public JointQPair getRightWrist() {
		return this.rightArmBase.getChild().getChild().getChild();
	}

	public JointQPair getLeftClavicle() {
		return this.leftArmBase;
	}

	public JointQPair getLeftShoulder() {
		return this.leftArmBase.getChild();
	}

	public JointQPair getLeftElbow() {
		return this.leftArmBase.getChild().getChild();
	}

	public JointQPair getLeftWrist() {
		return this.leftArmBase.getChild().getChild().getChild();
	}

	public JointQPair getPelvis() {
		return this.rightLegBase;
	}

	public JointQPair getRightHip() {
		return this.rightLegBase.getChild();
	}

	public JointQPair getRightKnee() {
		return this.rightLegBase.getChild().getChild();
	}

	public JointQPair getRightAnkle() {
		return this.rightLegBase.getChild().getChild().getChild();
	}

	public JointQPair getLeftHip() {
		return this.leftLegBase.getChild();
	}

	public JointQPair getLeftKnee() {
		return this.leftLegBase.getChild().getChild();
	}

	public JointQPair getLeftAnkle() {
		return this.leftLegBase.getChild().getChild().getChild();
	}

	/* package-private */JointQPair[] getChains() {
		return new JointQPair[] { this.rightArmBase, this.leftArmBase, this.rightLegBase, this.leftLegBase };
	}

	public Point3 getFakeLeftHandPosition() {
		return getFakeEffectorPosition( leftArmBase );
	}

	public Point3 getFakeRightHandPosition() {
		return getFakeEffectorPosition( rightArmBase );
	}

	private Point3 getFakeEffectorPosition( JointQPair startingJointQPair ) {
		AffineMatrix4x4 currentMatrix = AffineMatrix4x4.createIdentity();
		currentMatrix.translation.set( 1, 1, 1 );
		for( JointQPair jointQPair = startingJointQPair; jointQPair != null; jointQPair = jointQPair.getChild() ) {
			AffineMatrix4x4 mat = new AffineMatrix4x4( jointQPair.getUnitQuaternion(), new Point3( 1.0, 1.0, 1.0 ) );
			currentMatrix.multiply( mat );
		}
		return currentMatrix.translation;
	}

	@Override
	public String toString() {
		String rv = "";
		rv += "\n";
		rv += "Right Arm: ";
		rv += stringForChain( rightArmBase );
		rv += "\n";
		rv += "Left Arm: ";
		rv += stringForChain( leftArmBase );
		rv += "\n";
		rv += "Right Leg: ";
		rv += stringForChain( rightLegBase );
		rv += "\n";
		rv += "Left Leg: ";
		rv += stringForChain( leftLegBase );
		return rv;
	}

	private String stringForChain( JointQPair base ) {
		JointQPair jqpPointer = base;
		String rv = "";
		boolean comma = false;
		while( jqpPointer != null ) {
			if( comma ) {
				rv += ", ";
			}
			comma = true;
			rv += jqpPointer.toString();
			jqpPointer = jqpPointer.getChild();
		}
		return rv;
	}

	public boolean equals( Pose other ) {
		return ( rightArmBase.equals( other.rightArmBase ) && leftArmBase.equals( other.leftArmBase ) && rightLegBase.equals( other.rightLegBase ) && leftLegBase.equals( other.leftLegBase ) );
	}

	//	public MethodInvocation createAliceMethod( AnimateToPose.Detail[] details ) {
	//		Expression[] exArr = new Expression[ details.length + 1 ];
	//		try {
	//			exArr[ 0 ] = blah.createExpression( this );
	//		} catch( CannotCreateExpressionException e1 ) {
	//			e1.printStackTrace();
	//		}
	//		int i = 1;
	//		for( AnimateToPose.Detail detail : details ) {
	//			try {
	//				exArr[ i ] = blah.createExpression( detail );
	//			} catch( CannotCreateExpressionException e ) {
	//				e.printStackTrace();
	//			}
	//			++i;
	//		}
	//		//		Constructor constructor = this.getClass().getConstructor( JointQPair.class, JointQPair.class, JointQPair.class, JointQPair.class );
	//		JavaConstructor blah = JavaConstructor.getInstance( this.getClass(), JointQPair.class, JointQPair.class, JointQPair.class, JointQPair.class );
	//		Expression[] constArgs = this.createExpressionArrForBases();
	//		AstUtilities.createInstanceCreation( blah, constArgs );//getRightArmBase(), getRightLegBase(), getLeftArmBase(), getLeftLegBase() );
	//		//newThisExpresson
	//		//		AddProcedureComposite
	//		Builder builder = new Builder();
	//		Pose pose = builder.build();
	//		//		AstUtilities.createMethodInvocation( new ThisExpression(), ADD_POSE_ANIMATION, pose,  );
	//		//		MethodInvocation rv = new MethodInvocation( null, ADD_POSE_ANIMATION, this, details, details );
	//		return null;
	//	}
	//
	//	private Expression[] createExpressionArrForBases() {
	//		Expression[] rv = new Expression[ 4 ];
	//		Expression bleh = JointQPair.createInstance( getRightArmBase() );
	//		//		rv[ 0 ] = ;
	//		return null;
	//	}
}
