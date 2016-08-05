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

import java.awt.Point;

import org.alice.interact.InputState;
import org.alice.interact.PlaneUtilities;
import org.lgna.story.implementation.EntityImp;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.ForwardAndUpGuide;
import edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3;
import edu.cmu.cs.dennisc.math.Plane;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Ray;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;
import edu.cmu.cs.dennisc.scenegraph.OrthographicCamera;
import edu.cmu.cs.dennisc.scenegraph.util.ModestAxes;

/**
 * @author user
 */
public class CopyObjectDragManipulator extends OmniDirectionalBoundingBoxManipulator {

	edu.cmu.cs.dennisc.scenegraph.AbstractTransformable objectToCopy = null;

	@Override
	public boolean doStartManipulator( InputState startInput ) {
		this.objectToCopy = startInput.getClickPickTransformable();
		if( this.objectToCopy != null ) {
			assert this.camera != null;
			this.sgBoundingBoxTransformable.setParent( this.camera.getRoot() );
			this.sgBoundingBoxTransformable.setTransformation( this.objectToCopy.getAbsoluteTransformation(), AsSeenBy.SCENE );
			this.sgBoundingBoxDecorator.isShowing.setValue( true );
			this.setManipulatedTransformable( this.sgBoundingBoxTransformable );
			this.initializeEventMessages();
			this.hasMoved = false;
			this.hidCursor = false;
			this.originalPosition = this.objectToCopy.getAbsoluteTransformation().translation;
			AffineMatrix4x4 cameraTransform = this.getCamera().getParent().getAbsoluteTransformation();
			Vector3 toOrigin = Vector3.createSubtraction( this.originalPosition, cameraTransform.translation );
			toOrigin.normalize();
			Vector3 cameraFacingNormal = Vector3.createMultiplication( cameraTransform.orientation.backward, -1 );
			this.orthographicPickPlane = Plane.createInstance( this.originalPosition, cameraFacingNormal );

			Ray orthoPickRay = PlaneUtilities.getRayFromPixel( this.onscreenRenderTarget, this.getCamera(), startInput.getMouseLocation().x, startInput.getMouseLocation().y );
			Point3 orthoPickPoint = PlaneUtilities.getPointInPlane( orthographicPickPlane, orthoPickRay );
			this.orthographicOffsetToOrigin = Point3.createSubtraction( this.originalPosition, orthoPickPoint );

			Point3 initialClickPoint = this.getInitialClickPoint( startInput );

			this.offsetFromOrigin = Point3.createSubtraction( initialClickPoint, this.originalPosition );
			this.mousePlaneOffset = calculateMousePlaneOffset( startInput.getMouseLocation(), this.objectToCopy );

			//We don't need special planes for the orthographic camera
			if( !( this.getCamera() instanceof OrthographicCamera ) ) {
				Point mousePoint = new Point( startInput.getMouseLocation().x + this.mousePlaneOffset.x, startInput.getMouseLocation().y + this.mousePlaneOffset.y );
				setUpPlanes( this.originalPosition, mousePoint );

				Vector3 cameraBackward = this.camera.getAbsoluteTransformation().orientation.backward;
				cameraBackward.y = 0.0;
				cameraBackward.normalize();
				if( cameraBackward.isNaN() ) {
					cameraBackward = this.camera.getAbsoluteTransformation().orientation.up;
					cameraBackward.multiply( -1 );
					cameraBackward.y = 0.0;
					cameraBackward.normalize();
				}
				OrthogonalMatrix3x3 facingCameraOrientation = new OrthogonalMatrix3x3( new ForwardAndUpGuide( cameraBackward, Vector3.accessPositiveYAxis() ) );
				this.sgBoundingBoxTransformable.setTranslationOnly( this.getPerspectivePositionBasedOnInput( startInput ), AsSeenBy.SCENE );
			}
			addPlaneTransitionPointSphereToScene();
			edu.cmu.cs.dennisc.math.AxisAlignedBox box = null;

			EntityImp entityImplementation = EntityImp.getInstance( this.objectToCopy );
			if( entityImplementation != null ) {
				box = entityImplementation.getAxisAlignedMinimumBoundingBox();
			}

			if( box == null ) {
				box = new AxisAlignedBox( new Point3( -.5, 0, -.5 ), new Point3( .5, 1, .5 ) );
			}
			if( this.sgAxes != null ) {
				this.sgAxes.setParent( null );
			}
			if( box.isNaN() ) {
				this.sgBoundingBoxDecorator.isShowing.setValue( false );
				this.sgAxes = new ModestAxes( 1.0 );
			} else {
				this.sgBoundingBoxDecorator.setBox( box );
				this.sgAxes = new ModestAxes( box.getWidth() * .5 );
			}
			this.sgAxes.setParent( this.sgDecoratorOffsetTransformable );
			return true;
		}
		return false;
	}

	@Override
	public void doEndManipulator( InputState endInput, InputState previousInput ) {
		super.doEndManipulator( endInput, previousInput );

		//We need to do this because we launch a dialog box from this method that will grab focus and prevent things like letting go of the alt key from registering
		//This ensures that we don't leave an incomplete state behind
		this.dragAdapter.clearMouseAndKeyboardState();
		this.sgBoundingBoxDecorator.isShowing.setValue( false );
		if( this.sgAxes != null ) {
			this.sgAxes.isShowing.setValue( false );

			if( endInput.getInputEventType() == InputState.InputEventType.MOUSE_UP ) {
				org.lgna.croquet.Model model = null;
				org.alice.stageide.modelresource.ResourceKey resourceKey = null;
				EntityImp entityImplementation = EntityImp.getInstance( this.objectToCopy );
				if( entityImplementation != null ) {

					org.lgna.project.ast.UserField field = org.alice.ide.IDE.getActiveInstance().getSceneEditor().getFieldForInstanceInJavaVM( entityImplementation.getAbstraction() );
					if( field != null ) {

						org.alice.stageide.ast.declaration.AddCopiedManagedFieldComposite addCopiedManagedFieldComposite = org.alice.stageide.ast.declaration.AddCopiedManagedFieldComposite.getInstance();
						addCopiedManagedFieldComposite.setFieldToBeCopied( field );
						model = addCopiedManagedFieldComposite.getLaunchOperation();
						org.lgna.croquet.DropReceptor dropReceptor = ( (org.alice.stageide.sceneeditor.StorytellingSceneEditor)org.alice.ide.IDE.getActiveInstance().getSceneEditor() ).getDropReceptor();
						org.lgna.croquet.views.SwingComponentView<?> component = dropReceptor.getViewController();
						org.lgna.croquet.views.ViewController<?, ?> viewController;
						if( component instanceof org.lgna.croquet.views.ViewController<?, ?> ) {
							viewController = (org.lgna.croquet.views.ViewController<?, ?>)component;
						} else {
							viewController = null;
						}
						org.lgna.croquet.DropSite dropSite = new org.alice.stageide.sceneeditor.draganddrop.SceneDropSite( this.getManipulatedTransformable().getAbsoluteTransformation() );
						try {
							java.awt.event.MouseEvent mouseEvent = endInput.getInputEvent() instanceof java.awt.event.MouseEvent ? (java.awt.event.MouseEvent)endInput.getInputEvent() : null;
							org.lgna.croquet.history.Step<?> step = model.fire( org.lgna.croquet.triggers.DropTrigger.createUserInstance( viewController, mouseEvent, dropSite ) );
						} catch( org.lgna.croquet.CancelException ce ) {
							//Do nothing on cancel
						}
					}
				}
				this.objectToCopy = null;
			}
		}
	}
}
