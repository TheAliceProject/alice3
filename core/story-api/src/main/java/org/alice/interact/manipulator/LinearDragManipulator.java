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
package org.alice.interact.manipulator;

import java.awt.Point;

import org.alice.interact.AbstractDragAdapter.CameraView;
import org.alice.interact.InputState;
import org.alice.interact.MovementDirection;
import org.alice.interact.MovementType;
import org.alice.interact.PickHint;
import org.alice.interact.PlaneUtilities;
import org.alice.interact.VectorUtilities;
import org.alice.interact.condition.MovementDescription;
import org.alice.interact.event.ManipulationEvent;
import org.alice.interact.handle.HandleSet;
import org.alice.interact.handle.LinearDragHandle;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.Plane;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Ray;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.AbstractTransformable;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;

/**
 * @author David Culyba
 */
public class LinearDragManipulator extends AbstractManipulator implements CameraInformedManipulator, OnscreenPicturePlaneInformedManipulator {
	@Override
	public AbstractCamera getCamera() {
		return this.camera;
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
		//this can only be ACTIVE_VIEW
	}

	@Override
	public CameraView getDesiredCameraView() {
		return CameraView.PICK_CAMERA;
	}

	@Override
	public edu.cmu.cs.dennisc.render.OnscreenRenderTarget getOnscreenRenderTarget() {
		return this.onscreenRenderTarget;
	}

	@Override
	public void setOnscreenRenderTarget( edu.cmu.cs.dennisc.render.OnscreenRenderTarget onscreenRenderTarget ) {
		this.onscreenRenderTarget = onscreenRenderTarget;
	}

	@Override
	public String getUndoRedoDescription() {
		return "Object Move";
	}

	@Override
	protected void initializeEventMessages() {
		this.setMainManipulationEvent( new ManipulationEvent( ManipulationEvent.EventType.Translate, null, this.manipulatedTransformable ) );
		this.clearManipulationEvents();
		if( this.linearHandle != null ) {
			this.addManipulationEvent( new ManipulationEvent( ManipulationEvent.EventType.Translate, this.linearHandle.getMovementDescription(), this.manipulatedTransformable ) );
			MovementDirection oppositeDirection = this.linearHandle.getMovementDescription().direction.getOpposite();
			if( oppositeDirection != this.linearHandle.getMovementDescription().direction ) {
				this.addManipulationEvent( new ManipulationEvent( ManipulationEvent.EventType.Translate, new MovementDescription( oppositeDirection, this.linearHandle.getMovementDescription().type ), this.manipulatedTransformable ) );
			}
		}
	}

	protected double getDistanceAlongAxisBasedOnMouse( Point mouseLocation ) {
		Ray pickRay = PlaneUtilities.getRayFromPixel( this.onscreenRenderTarget, this.getCamera(), mouseLocation.x, mouseLocation.y );
		if( pickRay != null ) {
			Vector3 cameraBack = this.getCamera().getAbsoluteTransformation().orientation.backward;
			double axisCameraDot = Vector3.calculateDotProduct( this.absoluteDragAxis, cameraBack );
			if( Math.abs( axisCameraDot ) > .98d ) {
				Point3 pointInPlane = PlaneUtilities.getPointInPlane( this.cameraFacingPlane, pickRay );
				Vector3 fromOriginalMouseToCurrentMouse = Vector3.createSubtraction( pointInPlane, this.previousClickPoint );
				this.previousClickPoint.set( pointInPlane );
				Vector3 dragRightAxis = this.getCamera().getAbsoluteTransformation().orientation.right;
				dragRightAxis.normalize();
				Vector3 dragUpAxis = this.getCamera().getAbsoluteTransformation().orientation.up;
				dragUpAxis.normalize();

				double leftRightSign = 1.0d;
				if( Vector3.calculateDotProduct( this.absoluteDragAxis, dragRightAxis ) < 0.0d ) {
					leftRightSign = -1.0d;
				}
				double upDownSign = 1.0d;
				if( Vector3.calculateDotProduct( this.absoluteDragAxis, dragUpAxis ) < 0.0d ) {
					upDownSign = -1.0d;
				}

				double mouseYDistance = upDownSign * Vector3.calculateDotProduct( fromOriginalMouseToCurrentMouse, dragUpAxis );
				double mouseXDistance = leftRightSign * Vector3.calculateDotProduct( fromOriginalMouseToCurrentMouse, dragRightAxis );
				double newDistance = this.currentDistanceAlongAxis + mouseYDistance + mouseXDistance;
				return newDistance;
			} else {
				Point3 pointInPlane = PlaneUtilities.getPointInPlane( this.handleAlignedPlane, pickRay );
				if( pointInPlane != null ) {
					Vector3 pointVector = Vector3.createSubtraction( pointInPlane, this.originalOrigin );
					double dragAmount = Vector3.calculateDotProduct( pointVector, this.absoluteDragAxis );
					return dragAmount;
				}
			}
		}
		return 0;
	}

	protected void updateBasedOnHandlePull( double initialPull, double newPull ) {
		Vector3 translationFromOriginal = Vector3.createMultiplication( this.linearHandle.getDragAxis(), ( newPull - initialPull ) );

		//Translate the translation vector into scene space for snapping
		AffineMatrix4x4 toSceneTransform = this.linearHandle.getReferenceFrame().getAbsoluteTransformation();
		Vector3 sceneSpaceTranslation = new Vector3( translationFromOriginal );
		toSceneTransform.transform( sceneSpaceTranslation );

		//Calculate the new position based on the current mouse position
		Point3 absoluteNewPosition = Point3.createAddition( this.originalOrigin, sceneSpaceTranslation );

		//Apply any snap as necessary
		absoluteNewPosition = SnapUtilities.doMovementSnapping( this.manipulatedTransformable, absoluteNewPosition, this.dragAdapter, this.linearHandle.getSnapReferenceFrame(), this.getCamera() );

		//Calculate handle-relative translation vector
		Vector3 movementVector = Vector3.createSubtraction( absoluteNewPosition, this.manipulatedTransformable.getAbsoluteTransformation().translation );
		this.linearHandle.getReferenceFrame().getAbsoluteTransformation().transform( movementVector );

		Vector3 movementDif = new Vector3( movementVector );
		movementDif.normalize();
		for( ManipulationEvent event : this.getManipulationEvents() ) {
			double dot = Vector3.calculateDotProduct( event.getMovementDescription().direction.getVector(), movementDif );
			if( dot > 0.1d ) {
				this.dragAdapter.triggerManipulationEvent( event, true );
			} else if( dot < -.07d ) {
				this.dragAdapter.triggerManipulationEvent( event, false );
			}
		}
		//		this.manipulatedTransformable.setTranslationOnly(this.originalOrigin, AsSeenBy.SCENE);
		//		this.manipulatedTransformable.applyTranslation(translationFromOriginal, this.linearHandle.getReferenceFrame());
		//		Point3 finalPosition = this.manipulatedTransformable.getAbsoluteTransformation().translation;
		this.manipulatedTransformable.setTranslationOnly( absoluteNewPosition, AsSeenBy.SCENE );
	}

	@Override
	public void doDataUpdateManipulator( InputState currentInput, InputState previousInput ) {
		if( !currentInput.getMouseLocation().equals( previousInput.getMouseLocation() ) ) {
			this.currentDistanceAlongAxis = getDistanceAlongAxisBasedOnMouse( currentInput.getMouseLocation() );
			updateBasedOnHandlePull( this.initialDistanceAlongAxis, this.currentDistanceAlongAxis );
		}

	}

	@Override
	public void doEndManipulator( InputState endInput, InputState previousInput ) {
	}

	@Override
	public boolean doStartManipulator( InputState startInput ) {
		if( startInput.getClickPickHint().intersects( PickHint.PickType.THREE_D_HANDLE.pickHint() ) ) {
			AbstractTransformable clickedHandle = startInput.getClickPickedTransformable( true );
			if( clickedHandle instanceof LinearDragHandle ) {
				this.linearHandle = (LinearDragHandle)clickedHandle;
				this.setManipulatedTransformable( this.linearHandle.getManipulatedObject() );
				this.initializeEventMessages();
				this.absoluteDragAxis = this.linearHandle.getReferenceFrame().getAbsoluteTransformation().createTransformed( this.linearHandle.getDragAxis() );

				startInput.getClickPickResult().getPositionInSource( this.initialClickPoint );
				startInput.getClickPickResult().getSource().transformTo_AffectReturnValuePassedIn( this.initialClickPoint, startInput.getClickPickResult().getSource().getRoot() );

				this.previousClickPoint.set( this.initialClickPoint );

				Vector3 toCamera = Vector3.createSubtraction( this.getCamera().getAbsoluteTransformation().translation, this.manipulatedTransformable.getAbsoluteTransformation().translation );
				toCamera.normalize();
				Vector3 axisAlignedNormal = null;
				if( Math.abs( Vector3.calculateDotProduct( toCamera, this.absoluteDragAxis ) ) > .99d ) {
					axisAlignedNormal = toCamera;
				} else {
					Vector3 axisAlignedCameraVector = VectorUtilities.projectOntoVector( toCamera, this.absoluteDragAxis );
					Vector3 awayFromAxis = Vector3.createSubtraction( toCamera, axisAlignedCameraVector );
					awayFromAxis.normalize();
					axisAlignedNormal = awayFromAxis;
				}
				this.handleAlignedPlane = Plane.createInstance( this.linearHandle.getAbsoluteTransformation().translation, axisAlignedNormal );
				this.cameraFacingPlane = Plane.createInstance( this.initialClickPoint, this.getCamera().getAbsoluteTransformation().orientation.backward );
				this.originalOrigin = this.manipulatedTransformable.getAbsoluteTransformation().translation;
				this.currentDistanceAlongAxis = this.linearHandle.getCurrentHandleLength();
				this.initialDistanceAlongAxis = getDistanceAlongAxisBasedOnMouse( startInput.getMouseLocation() );
				this.currentDistanceAlongAxis = this.initialDistanceAlongAxis;
				return true;
			}
		}
		return false;
	}

	@Override
	public void doClickManipulator( InputState clickInput, InputState previousInput ) {
		//Do nothing
	}

	@Override
	public void doTimeUpdateManipulator( double time, InputState currentInput ) {
	}

	@Override
	protected HandleSet getHandleSetToEnable() {
		HandleSet.HandleGroup translationType;
		if( this.linearHandle.getMovementDescription().type == MovementType.STOOD_UP ) {
			translationType = HandleSet.HandleGroup.STOOD_UP_TRANSLATION;
		} else if( this.linearHandle.getMovementDescription().type == MovementType.ABSOLUTE ) {
			translationType = HandleSet.HandleGroup.ABSOLUTE_TRANSLATION;
		}
		else {
			translationType = HandleSet.HandleGroup.TRANSLATION;
		}
		return new HandleSet( this.linearHandle.getMovementDescription().direction.getHandleGroup(), HandleSet.HandleGroup.VISUALIZATION, translationType );
	}

	protected LinearDragHandle linearHandle;
	private Vector3 absoluteDragAxis;
	private Point3 initialClickPoint = new Point3();
	private Point3 previousClickPoint = new Point3();
	private double initialDistanceAlongAxis;
	private double currentDistanceAlongAxis;
	private Point3 originalOrigin;
	private Plane cameraFacingPlane;
	private Plane handleAlignedPlane;
	private AbstractCamera camera = null;
	private edu.cmu.cs.dennisc.render.OnscreenRenderTarget onscreenRenderTarget;
}
