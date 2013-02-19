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

import org.alice.ide.ast.ExpressionCreator.CannotCreateExpressionException;
import org.alice.stageide.ast.ExpressionCreator;
import org.lgna.project.ast.AstUtilities;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.JavaConstructor;
import org.lgna.project.ast.JavaMethod;
import org.lgna.project.ast.MethodInvocation;
import org.lgna.project.ast.ThisExpression;
import org.lgna.story.AnimateToPose;
import org.lgna.story.ImplementationAccessor;
import org.lgna.story.SBiped;
import org.lgna.story.SJoint;
import org.lgna.story.implementation.JointImp;
import org.lgna.story.resources.JointId;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;

/**
 * @author Matt May
 */
public class Pose {

	public class Builder {
		private JointQPair rightArmBase;
		private JointQPair leftArmBase;
		private JointQPair rightLegBase;
		private JointQPair leftLegBase;

		public Builder rightArmBase( JointQPair rightArmBase ) {
			assert this.rightArmBase == null;
			this.rightArmBase = rightArmBase;
			return this;
		}

		public Builder leftArmBase( JointQPair leftArmBase ) {
			assert this.leftArmBase == null;
			this.leftArmBase = rightArmBase;
			return this;
		}

		public Builder rightLegBase( JointQPair rightLegBase ) {
			assert this.rightLegBase == null;
			this.rightLegBase = rightLegBase;
			return this;
		}

		public Builder leftLegBase( JointQPair leftLegBase ) {
			assert this.leftLegBase == null;
			this.leftLegBase = leftLegBase;
			return this;
		}

		public Pose build() {
			return new Pose( rightArmBase, rightLegBase, leftArmBase, leftLegBase );
		}

		public Pose buildFromBiped( SBiped biped ) {
			rightArmBase = new JointQPair( getIDFor( biped.getRightClavicle() ), getOrientation( biped.getRightClavicle() ),
					new JointQPair( getIDFor( biped.getRightShoulder() ), getOrientation( biped.getRightShoulder() ),
							new JointQPair( getIDFor( biped.getRightElbow() ), getOrientation( biped.getRightElbow() ),
									new JointQPair( getIDFor( biped.getRightWrist() ), getOrientation( biped.getRightWrist() ) ) ) ) );

			leftArmBase = new JointQPair( getIDFor( biped.getLeftClavicle() ), getOrientation( biped.getLeftClavicle() ),
					new JointQPair( getIDFor( biped.getLeftShoulder() ), getOrientation( biped.getLeftShoulder() ),
							new JointQPair( getIDFor( biped.getLeftElbow() ), getOrientation( biped.getLeftElbow() ),
									new JointQPair( getIDFor( biped.getLeftWrist() ), getOrientation( biped.getLeftWrist() ) ) ) ) );

			rightLegBase = new JointQPair( getIDFor( biped.getPelvis() ), getOrientation( biped.getPelvis() ),
					new JointQPair( getIDFor( biped.getRightHip() ), getOrientation( biped.getRightHip() ),
							new JointQPair( getIDFor( biped.getRightKnee() ), getOrientation( biped.getRightKnee() ),
									new JointQPair( getIDFor( biped.getRightAnkle() ), getOrientation( biped.getRightAnkle() ) ) ) ) );

			leftLegBase = new JointQPair( getIDFor( biped.getPelvis() ), getOrientation( biped.getPelvis() ),
					new JointQPair( getIDFor( biped.getLeftHip() ), getOrientation( biped.getLeftHip() ),
							new JointQPair( getIDFor( biped.getLeftKnee() ), getOrientation( biped.getLeftKnee() ),
									new JointQPair( getIDFor( biped.getLeftAnkle() ), getOrientation( biped.getLeftAnkle() ) ) ) ) );

			return this.rightArmBase( rightArmBase )
					.leftArmBase( leftArmBase )
					.rightLegBase( rightLegBase )
					.leftLegBase( leftLegBase )
					.build();
		}

		private AffineMatrix4x4 getOrientation( SJoint sJoint ) {
			return ( (JointImp)ImplementationAccessor.getImplementation( sJoint ) ).getLocalTransformation();
		}

		private JointId getIDFor( SJoint sJoint ) {
			return ( (JointImp)ImplementationAccessor.getImplementation( sJoint ) ).getJointId();
		}
	}

	private final JointQPair rightArmBase;
	private final JointQPair leftArmBase;
	private final JointQPair rightLegBase;
	private final JointQPair leftLegBase;
	public static final JavaMethod ADD_POSE_ANIMATION = JavaMethod.getInstance( SBiped.class, "animateToPose", PoseAnimation.class, AnimateToPose.Detail[].class );
	private static final ExpressionCreator blah = new ExpressionCreator();

	//	public Pose( Pose other ) {
	//		this.rightArmBase = other.rightArmBase;
	//		this.leftArmBase = other.leftArmBase;
	//		this.rightLegBase = other.rightLegBase;
	//		this.leftLegBase = other.leftLegBase;
	//	}

	private Pose( JointQPair raBase, JointQPair rlBase, JointQPair laBase, JointQPair llBase ) {
		this.rightArmBase = raBase;
		this.rightLegBase = rlBase;
		this.leftArmBase = laBase;
		this.leftLegBase = llBase;
	}

	//	public JointQPair createJQP( SJoint joint ) {
	//		return createJQP( joint, null );
	//	}

	public JointQPair getRightArmBase() {
		return this.rightArmBase;
	}

	public JointQPair getLeftArmBase() {
		return this.leftArmBase;
	}

	public JointQPair getRightLegBase() {
		return this.rightLegBase;
	}

	public JointQPair getLeftLegBase() {
		return this.leftLegBase;
	}

	//	private JointQPair createJQP( SJoint joint, JointQPair parent ) {
	//		JointImp jointImp = ImplementationAccessor.getImplementation( joint );
	//		JointQPair rv = new JointQPair( parent, jointImp.getJointId(), jointImp.getLocalTransformation() );
	//		if( parent != null ) {
	//			parent.setChild( rv );
	//		}
	//		return rv;
	//	}

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

	public MethodInvocation createAliceMethod( AnimateToPose.Detail[] details ) {
		Expression[] exArr = new Expression[ details.length + 1 ];
		try {
			exArr[ 0 ] = blah.createExpression( this );
		} catch( CannotCreateExpressionException e1 ) {
			e1.printStackTrace();
		}
		int i = 1;
		for( AnimateToPose.Detail detail : details ) {
			try {
				exArr[ i ] = blah.createExpression( detail );
			} catch( CannotCreateExpressionException e ) {
				e.printStackTrace();
			}
			++i;
		}
		//		Constructor constructor = this.getClass().getConstructor( JointQPair.class, JointQPair.class, JointQPair.class, JointQPair.class );
		JavaConstructor blah = JavaConstructor.getInstance( this.getClass(), JointQPair.class, JointQPair.class, JointQPair.class, JointQPair.class );
		Expression[] constArgs = this.createExpressionArrForBases();
		AstUtilities.createInstanceCreation( blah, constArgs );//getRightArmBase(), getRightLegBase(), getLeftArmBase(), getLeftLegBase() );
		//newThisExpresson
		//		AddProcedureComposite
		AstUtilities.createMethodInvocation( new ThisExpression(), ADD_POSE_ANIMATION, exArr );
		//		MethodInvocation rv = new MethodInvocation( null, ADD_POSE_ANIMATION, this, details, details );
		return null;
	}

	private Expression[] createExpressionArrForBases() {
		Expression[] rv = new Expression[ 4 ];
		Expression bleh = JointQPair.createInstance( getRightArmBase() );
		//		rv[ 0 ] = ;
		return null;
	}
}
