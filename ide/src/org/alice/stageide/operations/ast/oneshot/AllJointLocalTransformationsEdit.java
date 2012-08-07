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
package org.alice.stageide.operations.ast.oneshot;

/**
 * @author Dennis Cosgrove
 */
public class AllJointLocalTransformationsEdit extends MethodInvocationEdit {
	private static class JointUndoRunnable implements Runnable {
		private final org.lgna.story.implementation.JointImp joint;
		private final edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 orientation;
		public JointUndoRunnable( org.lgna.story.implementation.JointImp joint ) {
			this.joint = joint;
			this.orientation = this.joint.getLocalTransformation().orientation;
		}
		public boolean isUndoNecessary() {
			return this.joint.getOriginalOrientation().isWithinReasonableEpsilonOrIsNegativeWithinReasonableEpsilon( this.orientation.createUnitQuaternion() ) == false;
		}
		public void run() {
			this.joint.animateLocalOrientationOnly( this.orientation, 1.0, edu.cmu.cs.dennisc.animation.TraditionalStyle.BEGIN_AND_END_GENTLY );
		}
	}
	private transient JointUndoRunnable[] jointUndoRunnables;
	public AllJointLocalTransformationsEdit( org.lgna.croquet.history.CompletionStep completionStep, org.alice.ide.instancefactory.InstanceFactory instanceFactory, org.lgna.project.ast.AbstractMethod method, org.lgna.project.ast.Expression[] argumentExpressions ) {
		super( completionStep, instanceFactory, method, argumentExpressions );
	}
	public AllJointLocalTransformationsEdit( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder, Object step ) {
		super( binaryDecoder, step );
	}
	@Override
	protected void preserveUndoInfo( Object instance, boolean isDo ) {
		if( instance instanceof org.lgna.story.SJointedModel ) {
			org.lgna.story.SJointedModel jointedModel = (org.lgna.story.SJointedModel)instance;
			org.lgna.story.implementation.JointedModelImp< ?, ? > jointedModelImp = org.lgna.story.ImplementationAccessor.getImplementation( jointedModel );
			Iterable< org.lgna.story.implementation.JointImp > joints = jointedModelImp.getJoints();
			java.util.List< JointUndoRunnable > list = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
			for( org.lgna.story.implementation.JointImp joint : joints ) {
				JointUndoRunnable jointUndoRunnable = new JointUndoRunnable( joint );
				if( jointUndoRunnable.isUndoNecessary() ) {
					list.add( jointUndoRunnable );
				}
			}
			this.jointUndoRunnables = edu.cmu.cs.dennisc.java.lang.ArrayUtilities.createArray( list, JointUndoRunnable.class );
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
					org.lgna.common.ThreadUtilities.doTogether( jointUndoRunnables );
				}
			}.start();
		}
	}
}
