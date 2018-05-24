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
package org.alice.stageide.oneshot.edits;

import edu.cmu.cs.dennisc.java.lang.ArrayUtilities;
import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.Dimension3;
import org.alice.ide.instancefactory.InstanceFactory;
import org.lgna.common.ThreadUtilities;
import org.lgna.croquet.history.CompletionStep;
import org.lgna.project.ast.AbstractMethod;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.FieldAccess;
import org.lgna.project.ast.JavaField;
import org.lgna.story.EmployeesOnly;
import org.lgna.story.Pose;
import org.lgna.story.SJointedModel;
import org.lgna.story.implementation.JointIdTransformationPair;
import org.lgna.story.implementation.JointImp;
import org.lgna.story.implementation.JointedModelImp;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public class StrikePoseEdit extends MethodInvocationEdit {
	private static class JointUndoRunnable implements Runnable {
		private final JointImp joint;
		private final AffineMatrix4x4 transformation;

		public JointUndoRunnable( JointImp joint ) {
			this.joint = joint;
			this.transformation = this.joint.getLocalTransformation();
		}

		//Returns true if the pose will actually change the orientation and position of the joint
		//Scale is passed in because poses that affect the translation of a joint must apply the model's scale to the translation
		public boolean isUndoNecessary( Pose<? extends SJointedModel> pose, Dimension3 scale ) {
			AffineMatrix4x4 poseTransform = null;
			boolean willNotRotateJoint = true;
			boolean willNotTranslateJoint = true;
			boolean affectsTranslation = false;
			for( JointIdTransformationPair idTransformPair : pose.getJointIdTransformationPairs() ) {
				if( idTransformPair.getJointId() == this.joint.getJointId() ) {
					poseTransform = new AffineMatrix4x4( idTransformPair.getTransformation() );
					if( idTransformPair.affectsTranslation() ) {
						//Apply the scale of the model to the translation
						poseTransform.translation.multiply( scale );
						affectsTranslation = true;
					}
					break;
				}
			}
			if( poseTransform != null ) {
				willNotRotateJoint = poseTransform.orientation.createUnitQuaternion().isWithinReasonableEpsilonOrIsNegativeWithinReasonableEpsilon( this.transformation.orientation.createUnitQuaternion() );
				if( affectsTranslation ) {
					willNotTranslateJoint = poseTransform.translation.isWithinReasonableEpsilonOf( this.transformation.translation );
				}
			}
			return !willNotRotateJoint || !willNotTranslateJoint;
		}

		@Override
		public void run() {
			this.joint.animateTransformation( this.joint.getVehicle(), this.transformation );
		}
	}

	private transient JointUndoRunnable[] jointUndoRunnables;

	private transient Pose<? extends SJointedModel> pose;

	private Pose<? extends SJointedModel> getPose( AbstractMethod method, Expression[] argumentExpressions ) {
		if( argumentExpressions.length > 0 ) {
			if( argumentExpressions[ 0 ] instanceof FieldAccess ) {
				FieldAccess fieldAccess = (FieldAccess)argumentExpressions[ 0 ];
				JavaField poseField = (JavaField)fieldAccess.field.getValue();
				if( poseField.isStatic() ) {
					Field fld = poseField.getFieldReflectionProxy().getReification();
					try {
						Object o = fld.get( null );
						if( o != null ) {
							if( o instanceof Pose<?> ) {
								return (Pose<?>)o;
							}
						}
					} catch( IllegalAccessException iae ) {
						iae.printStackTrace();
					}
				}
			}
		} else {
			//TODO: How to get the pose from "spreadWings" and "foldWings" and such.
		}
		return null;
	}

	public StrikePoseEdit( CompletionStep completionStep, InstanceFactory instanceFactory, AbstractMethod method, Expression[] argumentExpressions ) {
		super( completionStep, instanceFactory, method, argumentExpressions );
		this.pose = getPose( method, argumentExpressions );
	}

	@Override
	protected void preserveUndoInfo( Object instance, boolean isDo ) {
		if( instance instanceof SJointedModel ) {
			SJointedModel jointedModel = (SJointedModel)instance;
			JointedModelImp<?, ?> jointedModelImp = EmployeesOnly.getImplementation( jointedModel );
			Iterable<JointImp> joints = jointedModelImp.getJoints();
			List<JointUndoRunnable> list = Lists.newLinkedList();
			//TODO: Possibly use the captured pose to determine if undo is appropriate per joint?
			for( JointImp joint : joints ) {
				JointUndoRunnable jointUndoRunnable = new JointUndoRunnable( joint );
				if( this.pose != null ) {
					if( jointUndoRunnable.isUndoNecessary( this.pose, jointedModelImp.getScale() ) ) {
						list.add( jointUndoRunnable );
					}
				} else {
					list.add( jointUndoRunnable );
				}
			}
			this.jointUndoRunnables = ArrayUtilities.createArray( list, JointUndoRunnable.class );
		} else {
			this.jointUndoRunnables = new JointUndoRunnable[] {};
		}
	}

	@Override
	protected void undoInternal() {
		switch( this.jointUndoRunnables.length ) {
		case 0:
			break;
		case 1:
			this.jointUndoRunnables[ 0 ].run();
			break;
		default:
			new Thread() {
				@Override
				public void run() {
					ThreadUtilities.doTogether( jointUndoRunnables );
				}
			}.start();
		}
	}
}
