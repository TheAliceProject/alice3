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

package org.alice.stageide.sceneeditor.interact.manipulators;

import org.alice.interact.AbstractDragAdapter.CameraView;
import org.alice.interact.InputState;
import org.alice.interact.handle.HandleSet;
import org.alice.interact.manipulator.AbstractManipulator;
import org.alice.interact.manipulator.CameraInformedManipulator;
import org.lgna.story.implementation.EntityImp;

import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.AbstractTransformable;

public class GetAGoodLookAtManipulator extends AbstractManipulator implements CameraInformedManipulator {
	@Override
	public AbstractCamera getCamera() {
		return camera;
	}

	@Override
	public CameraView getDesiredCameraView() {
		return CameraView.PICK_CAMERA;
	}

	@Override
	public void setCamera( AbstractCamera camera ) {
		this.camera = camera;
		if( ( this.camera != null ) && ( this.camera.getParent() instanceof AbstractTransformable ) ) {
			this.setManipulatedTransformable( (AbstractTransformable)this.camera.getParent() );
		}
	}

	@Override
	public void setDesiredCameraView( CameraView cameraView ) {
		//this can only be PICK_CAMERA
	}

	@Override
	public void doClickManipulator( InputState endInput, InputState previousInput ) {
		AbstractTransformable toLookAt = endInput.getClickPickTransformable();
		if( ( toLookAt != null ) && ( this.camera != null ) ) {
			org.lgna.story.SThing toLookAtEntity = EntityImp.getAbstractionFromSgElement( toLookAt );
			org.lgna.story.SThing cameraAbstraction = EntityImp.getAbstractionFromSgElement( this.camera );

			if( ( cameraAbstraction instanceof org.lgna.story.SCamera ) && ( this.camera instanceof edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera ) &&
					( toLookAtEntity != cameraAbstraction ) ) {
				org.lgna.story.SCamera storytellingCamera = (org.lgna.story.SCamera)cameraAbstraction;

				if( org.alice.stageide.sceneeditor.interact.croquet.GetAGoodLookAtActionOperation.IsValidOperation( storytellingCamera, toLookAtEntity ) ) {

					//Check to see if the last action we did was a GetAGoodLookAt this object. If so, undo it
					int transactionCount = org.alice.ide.IDE.getActiveInstance().getProjectTransactionHistory().getTransactionCount();
					if( transactionCount > 0 ) {
						org.lgna.croquet.history.Transaction lastTransaction = org.alice.ide.IDE.getActiveInstance().getProjectTransactionHistory().getTransactionAt( transactionCount - 1 );
						org.lgna.croquet.edits.Edit lastEdit = lastTransaction.getEdit();
						if( lastEdit instanceof org.alice.stageide.sceneeditor.interact.croquet.edits.GetAGoodLookAtEdit ) {
							org.alice.stageide.sceneeditor.interact.croquet.edits.GetAGoodLookAtEdit edit = (org.alice.stageide.sceneeditor.interact.croquet.edits.GetAGoodLookAtEdit)lastEdit;
							if( ( edit.getCamera() == storytellingCamera ) && ( edit.getTarget() == toLookAtEntity ) ) {
								org.alice.ide.IDE.getActiveInstance().getDocumentFrame().getUndoOperation().fire();
								return;
							}
						}
					}

					//Check to see if we're already at a "good look" position of the target. If so, don't do anything
					org.lgna.story.implementation.SymmetricPerspectiveCameraImp cameraImp = org.lgna.story.EmployeesOnly.getImplementation( storytellingCamera );
					org.lgna.story.implementation.StandInImp cameraGoal = cameraImp.createGoodVantagePointStandIn( org.lgna.story.EmployeesOnly.getImplementation( toLookAtEntity ) );
					edu.cmu.cs.dennisc.math.AffineMatrix4x4 currentTransform = cameraImp.getAbsoluteTransformation();
					edu.cmu.cs.dennisc.math.AffineMatrix4x4 goalTransform = cameraGoal.getAbsoluteTransformation();
					if( currentTransform.orientation.isWithinReasonableEpsilonOf( goalTransform.orientation ) && currentTransform.translation.isWithinReasonableEpsilonOf( goalTransform.translation ) ) {
						//Do nothing since we're already where we're supposed to be
						return;
					}

					//Actually "get a good look at" the target
					org.alice.stageide.sceneeditor.interact.croquet.GetAGoodLookAtActionOperation lookAtOperation = new org.alice.stageide.sceneeditor.interact.croquet.GetAGoodLookAtActionOperation( org.lgna.croquet.Application.PROJECT_GROUP, storytellingCamera, toLookAtEntity );
					lookAtOperation.fire();
				}
				else {
					edu.cmu.cs.dennisc.java.util.logging.Logger.warning( "Invlalid operation: " + storytellingCamera + ".GetAGoodLookAt( " + toLookAtEntity + " )" );
				}
			}
			else {
				edu.cmu.cs.dennisc.java.util.logging.Logger.todo( "Implement GetAGoodLookAt for orthographic cameras" );
			}
		}
	}

	@Override
	public void doDataUpdateManipulator( InputState currentInput, InputState previousInput ) {
	}

	@Override
	public void doEndManipulator( InputState endInput, InputState previousInput ) {
	}

	@Override
	public boolean doStartManipulator( InputState startInput ) {
		return this.manipulatedTransformable != null;
	}

	@Override
	public void doTimeUpdateManipulator( double dTime, InputState currentInput ) {
	}

	@Override
	protected HandleSet getHandleSetToEnable() {
		return null;
	}

	@Override
	public String getUndoRedoDescription() {
		return "Look At Object";
	}

	private AbstractCamera camera;
}
