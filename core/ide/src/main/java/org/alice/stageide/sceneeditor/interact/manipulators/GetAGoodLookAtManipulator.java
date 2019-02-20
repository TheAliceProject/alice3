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

import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.AbstractTransformable;
import edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera;
import org.alice.ide.IDE;
import org.alice.interact.DragAdapter.CameraView;
import org.alice.interact.InputState;
import org.alice.interact.handle.HandleSet;
import org.alice.interact.manipulator.AbstractManipulator;
import org.alice.interact.manipulator.CameraInformedManipulator;
import org.alice.stageide.sceneeditor.interact.croquet.GetAGoodLookAtActionOperation;
import org.alice.stageide.sceneeditor.interact.croquet.edits.GetAGoodLookAtEdit;
import org.lgna.croquet.Application;
import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.history.UserActivity;
import org.lgna.story.EmployeesOnly;
import org.lgna.story.SCamera;
import org.lgna.story.SThing;
import org.lgna.story.implementation.EntityImp;
import org.lgna.story.implementation.StandInImp;
import org.lgna.story.implementation.SymmetricPerspectiveCameraImp;

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
			SThing toLookAtEntity = EntityImp.getAbstractionFromSgElement( toLookAt );
			SThing cameraAbstraction = EntityImp.getAbstractionFromSgElement( this.camera );

			if( ( cameraAbstraction instanceof SCamera ) && ( this.camera instanceof SymmetricPerspectiveCamera ) &&
					( toLookAtEntity != cameraAbstraction ) ) {
				SCamera storytellingCamera = (SCamera)cameraAbstraction;

				if( GetAGoodLookAtActionOperation.IsValidOperation( storytellingCamera, toLookAtEntity ) ) {

					//Check to see if the last action we did was a GetAGoodLookAt this object. If so, undo it
					final UserActivity projectUserActivity = IDE.getActiveInstance().getProjectUserActivity();
					int activityCount = projectUserActivity.getChildActivities().size();
					if( activityCount > 0 ) {
						UserActivity lastActivity = projectUserActivity.getChildActivities().get( activityCount - 1 );
						Edit lastEdit = lastActivity.getEdit();
						if( lastEdit instanceof GetAGoodLookAtEdit ) {
							GetAGoodLookAtEdit edit = (GetAGoodLookAtEdit)lastEdit;
							if( ( edit.getCamera() == storytellingCamera ) && ( edit.getTarget() == toLookAtEntity ) ) {
								IDE.getActiveInstance().getDocumentFrame().getUndoOperation().fire();
								return;
							}
						}
					}

					//Check to see if we're already at a "good look" position of the target. If so, don't do anything
					SymmetricPerspectiveCameraImp cameraImp = EmployeesOnly.getImplementation( storytellingCamera );
					StandInImp cameraGoal = cameraImp.createGoodVantagePointStandIn( EmployeesOnly.getImplementation( toLookAtEntity ) );
					AffineMatrix4x4 currentTransform = cameraImp.getAbsoluteTransformation();
					AffineMatrix4x4 goalTransform = cameraGoal.getAbsoluteTransformation();
					if( currentTransform.orientation.isWithinReasonableEpsilonOf( goalTransform.orientation ) && currentTransform.translation.isWithinReasonableEpsilonOf( goalTransform.translation ) ) {
						//Do nothing since we're already where we're supposed to be
						return;
					}

					//Actually "get a good look at" the target
					GetAGoodLookAtActionOperation lookAtOperation = new GetAGoodLookAtActionOperation( Application.PROJECT_GROUP, storytellingCamera, toLookAtEntity );
					lookAtOperation.fire();
				}
				else {
					Logger.warning( "Invlalid operation: " + storytellingCamera + ".GetAGoodLookAt( " + toLookAtEntity + " )" );
				}
			}
			else {
				Logger.todo( "Implement GetAGoodLookAt for orthographic cameras" );
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
