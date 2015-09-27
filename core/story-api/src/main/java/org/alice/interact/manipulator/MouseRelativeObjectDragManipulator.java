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
import org.alice.interact.PlaneUtilities;
import org.alice.interact.condition.MovementDescription;
import org.alice.interact.event.ManipulationEvent;
import org.alice.interact.handle.HandleSet;

import edu.cmu.cs.dennisc.java.awt.CursorUtilities;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.Plane;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Ray;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;
import edu.cmu.cs.dennisc.scenegraph.OrthographicCamera;

public class MouseRelativeObjectDragManipulator extends AbstractManipulator implements CameraInformedManipulator, OnscreenPicturePlaneInformedManipulator {

	private static final double PIXEL_DISTANCE_FACTOR = 200.0d;

	private static final double MAX_DISTANCE_PER_PIXEL = .1d;

	public MouseRelativeObjectDragManipulator() {
		super();
	}

	@Override
	public AbstractCamera getCamera() {
		return this.camera;
	}

	@Override
	public void setCamera( AbstractCamera camera ) {
		this.camera = camera;
	}

	@Override
	public void setDesiredCameraView( CameraView cameraView ) {
		//this can only be PICK_CAMERA
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
	protected HandleSet getHandleSetToEnable() {
		return HandleSet.STOOD_UP_GROUND_TRANSLATION_VISUALIZATION;
	}

	private Vector3 getMouseMovementFromVector( InputState currentInput, InputState previousInput ) {
		int xChange = currentInput.getMouseLocation().x - originalMousePoint.x;
		int yChange = currentInput.getMouseLocation().y - originalMousePoint.y;
		yChange *= -1; //invert X

		Vector3 translationVector = new Vector3();

		//		horizontalPlacementPlane = calculateCameraFacingPlane();
		if( horizontalPlacementPlane != null ) {
			Ray pickRay = PlaneUtilities.getRayFromPixel( this.onscreenRenderTarget, this.getCamera(), currentInput.getMouseLocation().x, currentInput.getMouseLocation().y );
			Point3 pickPoint = PlaneUtilities.getPointInPlane( horizontalPlacementPlane, pickRay );
			pickPoint.subtract( this.offsetFromOrigin );
			pickPoint.y = 0;
			Vector3 translationX = Vector3.createSubtraction( pickPoint, this.originalPosition );
			Vector3 translationY = Vector3.createMultiplication( moveYVector, yChange * worldUnitsPerPixelY );
			translationVector = Vector3.createAddition( translationX, translationY );
		} else {
			Vector3 translationX = Vector3.createMultiplication( moveXVector, xChange * worldUnitsPerPixelX );
			Vector3 translationY = Vector3.createMultiplication( moveYVector, yChange * worldUnitsPerPixelY );
			translationVector = Vector3.createAddition( translationX, translationY );
		}
		return translationVector;
	}

	private Vector3 getOthographicMovementVector( InputState currentInput, InputState previousInput ) {
		Ray pickRay = PlaneUtilities.getRayFromPixel( this.onscreenRenderTarget, this.getCamera(), currentInput.getMouseLocation().x, currentInput.getMouseLocation().y );
		Point3 pickPoint = PlaneUtilities.getPointInPlane( this.orthographicPickPlane, pickRay );
		Point3 newPosition = Point3.createAddition( pickPoint, this.orthographicOffsetToOrigin );

		return Vector3.createSubtraction( newPosition, this.originalPosition );
	}

	private Vector3 getMovementVectorBasedOnCamera( InputState currentInput, InputState previousInput ) {
		if( this.getCamera() instanceof OrthographicCamera ) {
			return getOthographicMovementVector( currentInput, previousInput );
		} else {
			return getMouseMovementFromVector( currentInput, previousInput );
		}
	}

	@Override
	public void doDataUpdateManipulator( InputState currentInput, InputState previousInput ) {
		if( !currentInput.getMouseLocation().equals( previousInput.getMouseLocation() ) && ( this.manipulatedTransformable != null ) ) {
			if( !this.hasMoved ) {
				this.hasMoved = true;
			}

			Vector3 movementVector = getMovementVectorBasedOnCamera( currentInput, previousInput );
			Point3 newPosition = Point3.createAddition( this.originalPosition, movementVector );

			newPosition = SnapUtilities.doMovementSnapping( this.manipulatedTransformable, newPosition, this.dragAdapter, this.manipulatedTransformable.getRoot(), this.getCamera() );

			//Send manipulation events
			Vector3 movementDif = Vector3.createSubtraction( newPosition, this.manipulatedTransformable.getAbsoluteTransformation().translation );
			movementDif.normalize();
			for( ManipulationEvent event : this.getManipulationEvents() ) {
				double dot = Vector3.calculateDotProduct( event.getMovementDescription().direction.getVector(), movementDif );
				if( dot > 0.1d ) {
					this.dragAdapter.triggerManipulationEvent( event, true );
				} else if( dot < -.07d ) {
					this.dragAdapter.triggerManipulationEvent( event, false );
				}
			}
			if( newPosition != null ) {
				this.manipulatedTransformable.setTranslationOnly( newPosition, AsSeenBy.SCENE );
			}
		}
	}

	@Override
	public void doEndManipulator( InputState endInput, InputState previousInput ) {
		if( this.hidCursor ) {
			this.showCursor();
		}
	}

	@Override
	public void doClickManipulator( InputState clickInput, InputState previousInput ) {
		//Do nothing
	}

	@Override
	protected void initializeEventMessages() {
		this.setMainManipulationEvent( new ManipulationEvent( ManipulationEvent.EventType.Translate, null, this.manipulatedTransformable ) );
		this.clearManipulationEvents();
		this.addManipulationEvent( new ManipulationEvent( ManipulationEvent.EventType.Translate, new MovementDescription( MovementDirection.LEFT, MovementType.ABSOLUTE ), this.manipulatedTransformable ) );
		this.addManipulationEvent( new ManipulationEvent( ManipulationEvent.EventType.Translate, new MovementDescription( MovementDirection.RIGHT, MovementType.ABSOLUTE ), this.manipulatedTransformable ) );
		this.addManipulationEvent( new ManipulationEvent( ManipulationEvent.EventType.Translate, new MovementDescription( MovementDirection.FORWARD, MovementType.ABSOLUTE ), this.manipulatedTransformable ) );
		this.addManipulationEvent( new ManipulationEvent( ManipulationEvent.EventType.Translate, new MovementDescription( MovementDirection.BACKWARD, MovementType.ABSOLUTE ), this.manipulatedTransformable ) );
	}

	@Override
	public boolean doStartManipulator( InputState startInput ) {
		this.setManipulatedTransformable( startInput.getClickPickTransformable() );
		hidCursor = false;
		if( this.manipulatedTransformable != null ) {
			this.initializeEventMessages();

			this.hasMoved = false;

			this.originalLocalTransformation = new AffineMatrix4x4( manipulatedTransformable.getLocalTransformation() );
			this.originalMousePoint = new Point( startInput.getMouseLocation() );
			this.originalPosition = this.manipulatedTransformable.getAbsoluteTransformation().translation;
			this.orthographicPickPlane = Plane.createInstance( this.originalPosition, this.getCamera().getAxes( AsSeenBy.SCENE ).backward );

			Ray orthoPickRay = PlaneUtilities.getRayFromPixel( this.onscreenRenderTarget, this.getCamera(), startInput.getMouseLocation().x, startInput.getMouseLocation().y );
			Point3 orthoPickPoint = PlaneUtilities.getPointInPlane( orthographicPickPlane, orthoPickRay );
			this.orthographicOffsetToOrigin = Point3.createSubtraction( this.originalPosition, orthoPickPoint );

			Point3 initialClickPoint = new Point3();
			startInput.getClickPickResult().getPositionInSource( initialClickPoint );
			startInput.getClickPickResult().getSource().transformTo_AffectReturnValuePassedIn( initialClickPoint, startInput.getClickPickResult().getSource().getRoot() );

			Ray pickRay = PlaneUtilities.getRayFromPixel( this.onscreenRenderTarget, this.getCamera(), startInput.getMouseLocation().x, startInput.getMouseLocation().y );
			if( pickRay != null ) {
				this.offsetFromOrigin = Point3.createSubtraction( initialClickPoint, this.originalPosition );
			}

			AffineMatrix4x4 cameraTransform = this.camera.getParent().getAbsoluteTransformation();
			initialCameraDotVertical = Vector3.calculateDotProduct( cameraTransform.orientation.backward, Vector3.accessPositiveYAxis() );
			initialCameraDotVertical = Math.abs( initialCameraDotVertical );
			if( this.camera instanceof OrthographicCamera ) {
				moveXVector = new Vector3( cameraTransform.orientation.right );
				moveYVector = new Vector3( cameraTransform.orientation.up );
			} else {

				if( initialCameraDotVertical > .99999 ) {
					moveYVector = new Vector3( cameraTransform.orientation.up );
				} else {
					moveYVector = new Vector3( cameraTransform.orientation.backward );
					moveYVector.multiply( -1 );
				}
				moveXVector = new Vector3( cameraTransform.orientation.right );
				moveYVector.y = 0;
				moveYVector.normalize();
				moveXVector.y = 0;
				moveXVector.normalize();

				horizontalPlacementPlane = calculateCameraFacingPlane();
			}

			initialDistanceToGround = Math.abs( cameraTransform.translation.y );
			pickDistance = -1;
			Vector3 cameraForward = new Vector3( cameraTransform.orientation.backward );
			cameraForward.multiply( -1.0d );
			Point3 pickPoint = PlaneUtilities.getPointInPlane( Plane.XZ_PLANE, new edu.cmu.cs.dennisc.math.Ray( this.manipulatedTransformable.getAbsoluteTransformation().translation, cameraForward ) );
			if( pickPoint != null ) {
				pickDistance = Point3.calculateDistanceBetween( pickPoint, cameraTransform.translation );
			}

			calculateMovementFactors( startInput.getMouseLocation() );

			this.hideCursor();
			return true;
		}
		return false;

	}

	private void calculateMovementFactors( Point mousePoint ) {
		Ray centerRay = PlaneUtilities.getRayFromPixel( this.onscreenRenderTarget, this.getCamera(), mousePoint.x, mousePoint.y );
		Ray oneUp = PlaneUtilities.getRayFromPixel( this.onscreenRenderTarget, this.getCamera(), mousePoint.x, mousePoint.y - 1 );
		Ray oneDown = PlaneUtilities.getRayFromPixel( this.onscreenRenderTarget, this.getCamera(), mousePoint.x, mousePoint.y + 1 );
		Ray oneRight = PlaneUtilities.getRayFromPixel( this.onscreenRenderTarget, this.getCamera(), mousePoint.x + 1, mousePoint.y );
		Ray oneLeft = PlaneUtilities.getRayFromPixel( this.onscreenRenderTarget, this.getCamera(), mousePoint.x - 1, mousePoint.y );

		double distancePerUpPixel = MAX_DISTANCE_PER_PIXEL;
		double distancePerDownPixel = MAX_DISTANCE_PER_PIXEL;
		double distancePerRightPixel = MAX_DISTANCE_PER_PIXEL;
		double distancePerLeftPixel = MAX_DISTANCE_PER_PIXEL;
		Point3 centerPoint = PlaneUtilities.getPointInPlane( Plane.XZ_PLANE, centerRay );
		if( centerPoint != null ) {
			Point3 offsetPoint = PlaneUtilities.getPointInPlane( Plane.XZ_PLANE, oneUp );
			if( offsetPoint != null ) {
				double pixelDistance = Point3.calculateDistanceBetween( centerPoint, offsetPoint );
				if( pixelDistance < MAX_DISTANCE_PER_PIXEL ) {
					distancePerUpPixel = pixelDistance;
				}
			}
			offsetPoint = PlaneUtilities.getPointInPlane( Plane.XZ_PLANE, oneDown );
			if( offsetPoint != null ) {
				double pixelDistance = Point3.calculateDistanceBetween( centerPoint, offsetPoint );
				if( pixelDistance < MAX_DISTANCE_PER_PIXEL ) {
					distancePerDownPixel = pixelDistance;
				}
			}
			offsetPoint = PlaneUtilities.getPointInPlane( Plane.XZ_PLANE, oneRight );
			if( offsetPoint != null ) {
				double pixelDistance = Point3.calculateDistanceBetween( centerPoint, offsetPoint );
				if( pixelDistance < MAX_DISTANCE_PER_PIXEL ) {
					distancePerRightPixel = pixelDistance;
				}
			}
			offsetPoint = PlaneUtilities.getPointInPlane( Plane.XZ_PLANE, oneLeft );
			if( offsetPoint != null ) {
				double pixelDistance = Point3.calculateDistanceBetween( centerPoint, offsetPoint );
				if( pixelDistance < MAX_DISTANCE_PER_PIXEL ) {
					distancePerLeftPixel = pixelDistance;
				}
			}
		}

		worldUnitsPerPixelX = ( distancePerLeftPixel + distancePerRightPixel ) / 2.0;
		worldUnitsPerPixelY = ( distancePerUpPixel + distancePerDownPixel ) / 2.0;
	}

	private Plane calculateCameraFacingPlane()
	{
		AffineMatrix4x4 cameraTransform = this.camera.getParent().getAbsoluteTransformation();
		Vector3 cameraFacingVector = cameraTransform.orientation.backward;
		cameraFacingVector.y = 0.0;
		cameraFacingVector.normalize();
		if( !cameraFacingVector.isNaN() )
		{
			Point3 planeLocation = Point3.createAddition( this.manipulatedTransformable.getAbsoluteTransformation().translation, this.offsetFromOrigin );
			return Plane.createInstance( planeLocation, cameraFacingVector );
		}
		return null;
	}

	private double calculateWorldUnitsPerPixelX() {
		worldUnitsPerPixelX = initialDistanceToGround / 150.0d;
		return worldUnitsPerPixelX;
	}

	private double calculateWorldUnitsPerPixelY() {
		double parallelToGroundFactor = 30d;
		double perpToGroundFactor = 200d;
		double yFactor = parallelToGroundFactor + ( ( perpToGroundFactor - parallelToGroundFactor ) * initialCameraDotVertical );
		worldUnitsPerPixelY = initialDistanceToGround / yFactor;
		return worldUnitsPerPixelX;
	}

	@Override
	public void doTimeUpdateManipulator( double time, InputState currentInput ) {
	}

	protected void hideCursor() {
		CursorUtilities.pushAndSet( this.onscreenRenderTarget.getAwtComponent(), CursorUtilities.NULL_CURSOR );
		this.hidCursor = true;
	}

	protected void showCursor() {
		try {
			Point3 new3DPoint = Point3.createAddition( this.manipulatedTransformable.getAbsoluteTransformation().translation, this.offsetFromOrigin );

			Point3 pointInCamera = this.camera.transformFrom_New( new3DPoint, this.camera.getRoot() );
			Point awtPoint = edu.cmu.cs.dennisc.render.PicturePlaneUtils.transformFromCameraToAWT_New( pointInCamera, this.onscreenRenderTarget, this.getCamera() );
			edu.cmu.cs.dennisc.java.awt.RobotUtilities.mouseMove( this.onscreenRenderTarget.getAwtComponent(), awtPoint );
		} finally {
			CursorUtilities.popAndSet( this.onscreenRenderTarget.getAwtComponent() );
		}
	}

	private Point originalMousePoint;
	private AffineMatrix4x4 originalLocalTransformation;
	private Vector3 moveXVector;
	private Vector3 moveYVector;
	private double worldUnitsPerPixelX = .01d;
	private double worldUnitsPerPixelY = .01d;
	private double initialDistanceToGround;
	private double initialCameraDotVertical;
	private double pickDistance;
	protected Plane orthographicPickPlane = edu.cmu.cs.dennisc.math.Plane.XZ_PLANE;
	protected Point3 orthographicOffsetToOrigin = null;
	protected Point3 originalPosition = null;
	protected Boolean hasMoved = false;
	protected Point3 offsetFromOrigin = null;
	protected Plane horizontalPlacementPlane;

	protected boolean hidCursor = false;

	protected AbstractCamera camera = null;
	private edu.cmu.cs.dennisc.render.OnscreenRenderTarget onscreenRenderTarget;
}
