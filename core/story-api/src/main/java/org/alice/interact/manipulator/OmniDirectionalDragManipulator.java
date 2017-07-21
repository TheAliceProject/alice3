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
import org.alice.interact.VectorUtilities;
import org.alice.interact.condition.MovementDescription;
import org.alice.interact.debug.DebugSphere;
import org.alice.interact.event.ManipulationEvent;
import org.alice.interact.handle.HandleSet;

import edu.cmu.cs.dennisc.java.awt.CursorUtilities;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.AngleInRadians;
import edu.cmu.cs.dennisc.math.Plane;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Ray;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.AbstractTransformable;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;
import edu.cmu.cs.dennisc.scenegraph.OrthographicCamera;

/**
 * @author David Culyba
 */
public class OmniDirectionalDragManipulator extends AbstractManipulator implements CameraInformedManipulator, OnscreenPicturePlaneInformedManipulator {

	private static final boolean SHOW_PLANE_TRANSITION_POINT = false;
	private static final double MAX_DISTANCE_PER_PIXEL = .17d;

	protected void addPlaneTransitionPointSphereToScene() {
		if( SHOW_PLANE_TRANSITION_POINT ) {
			if( ( this.camera != null ) && ( this.planeTransitionPointDebugSphere.getParent() == null ) ) {
				this.camera.getRoot().addComponent( this.planeTransitionPointDebugSphere );
			}
		}
	}

	private void removePlaneTransitionPointSphereFromScene() {
		if( SHOW_PLANE_TRANSITION_POINT ) {
			if( ( this.camera != null ) && ( this.planeTransitionPointDebugSphere.getParent() == this.camera.getRoot() ) ) {
				this.camera.getRoot().removeComponent( this.planeTransitionPointDebugSphere );
			}
		}
	}

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
	protected void initializeEventMessages() {
		this.setMainManipulationEvent( new ManipulationEvent( ManipulationEvent.EventType.Translate, null, this.manipulatedTransformable ) );
		this.clearManipulationEvents();
		this.addManipulationEvent( new ManipulationEvent( ManipulationEvent.EventType.Translate, new MovementDescription( MovementDirection.LEFT, MovementType.ABSOLUTE ), this.manipulatedTransformable ) );
		this.addManipulationEvent( new ManipulationEvent( ManipulationEvent.EventType.Translate, new MovementDescription( MovementDirection.RIGHT, MovementType.ABSOLUTE ), this.manipulatedTransformable ) );
		this.addManipulationEvent( new ManipulationEvent( ManipulationEvent.EventType.Translate, new MovementDescription( MovementDirection.FORWARD, MovementType.ABSOLUTE ), this.manipulatedTransformable ) );
		this.addManipulationEvent( new ManipulationEvent( ManipulationEvent.EventType.Translate, new MovementDescription( MovementDirection.BACKWARD, MovementType.ABSOLUTE ), this.manipulatedTransformable ) );
	}

	protected Plane createCameraPickPlane( Point3 clickPoint ) {
		Vector3 clickPlaneNormal = this.getCamera().getAxes( AsSeenBy.SCENE ).backward;
		//		clickPlaneNormal.y += 2d;  //Make the bad plane slightly tilted so moving the mouse will always move the object in the plane
		clickPlaneNormal.normalize();
		return Plane.createInstance( clickPoint, clickPlaneNormal );
	}

	protected Plane createLevelPickPlane( Point3 clickPoint ) {
		Vector3 levelPlaneNormal = MovementDirection.UP.getVector();
		//		clickPlaneNormal.y += 2d;  //Make the bad plane slightly tilted so moving the mouse will always move the object in the plane
		levelPlaneNormal.normalize();
		return Plane.createInstance( clickPoint, levelPlaneNormal );
	}

	private Vector3 getMouseMovementFromVector( Point mouseVector ) {
		if( ( mouseVector.x == 0 ) && ( mouseVector.y == 0 ) )
		{
			return new Vector3( 0, 0, 0 );
		}

		Vector3 mouseRelativeMovement = new Vector3( mouseVector.x, 0d, mouseVector.y );
		getCamera().getRoot().transformFrom_AffectReturnValuePassedIn( mouseRelativeMovement, getCamera() );
		mouseRelativeMovement.y = 0d;
		mouseRelativeMovement.normalize();

		double MOVEMENT_SCALE = .02d;
		double movementAmount = mouseVector.distance( 0f, 0f ) * MOVEMENT_SCALE;
		mouseRelativeMovement.multiply( movementAmount );

		if( mouseRelativeMovement.isNaN() )
		{
			System.out.println( "NaN!" );
		}

		return mouseRelativeMovement;
	}

	private Point3 getMovementVectorBasedOnCamera( InputState currentInput, InputState previousInput ) {
		if( this.getCamera() instanceof OrthographicCamera ) {
			return getOthographicMovementVector( currentInput, previousInput );
		} else {
			return getPerspectiveMovementVector( currentInput, previousInput );
		}
	}

	protected Point3 getOthographicMovementVector( InputState currentInput, InputState previousInput ) {
		Ray pickRay = PlaneUtilities.getRayFromPixel( this.onscreenRenderTarget, this.getCamera(), currentInput.getMouseLocation().x, currentInput.getMouseLocation().y );
		Point3 pickPoint = PlaneUtilities.getPointInPlane( this.orthographicPickPlane, pickRay );
		Point3 newPosition = Point3.createAddition( pickPoint, this.orthographicOffsetToOrigin );

		return Point3.createSubtraction( newPosition, this.getManipulatedTransformable().getAbsoluteTransformation().translation );
	}

	protected Point3 getPerspectivePositionBasedOnInput( InputState currentInput ) {
		Point mousePoint = new Point( currentInput.getMouseLocation().x + this.mousePlaneOffset.x, currentInput.getMouseLocation().y + this.mousePlaneOffset.y );
		Vector3 cameraForward = this.getCamera().getParent().getAbsoluteTransformation().orientation.backward;
		cameraForward.multiply( -1 );
		Ray pickRay = PlaneUtilities.getRayFromPixel( this.onscreenRenderTarget, this.getCamera(), mousePoint.x, mousePoint.y );

		Point3 levelPickPoint = null;
		Point3 skewedPickPoint = null;
		if( this.pickPlane != null ) {
			levelPickPoint = PlaneUtilities.getPointInPlane( this.pickPlane, pickRay );
			//force the pick point to be at the original Y level
			if( levelPickPoint != null ) {
				levelPickPoint.y = originalPosition.y;
			}
		}
		if( this.backPlane != null ) {
			skewedPickPoint = PlaneUtilities.getPointInPlane( this.backPlane, pickRay );
			//force the pick point to be at the original Y level
			if( skewedPickPoint != null ) {
				skewedPickPoint.y = originalPosition.y;
			}
		}
		Point3 pointToUse;
		if( ( levelPickPoint == null ) && ( skewedPickPoint == null ) ) {
			pointToUse = null;
		} else if( ( levelPickPoint == null ) && ( skewedPickPoint != null ) ) {
			pointToUse = skewedPickPoint;
		} else if( ( levelPickPoint != null ) && ( skewedPickPoint == null ) ) {
			pointToUse = levelPickPoint;
		} else {
			Point3 cameraPosition = this.getCamera().getParent().getAbsoluteTransformation().translation;
			double levelDistanceToCamera = Point3.calculateDistanceBetween( cameraPosition, levelPickPoint );
			double skewedDistanceToCamera = Point3.calculateDistanceBetween( cameraPosition, skewedPickPoint );
			if( levelDistanceToCamera <= skewedDistanceToCamera ) {
				pointToUse = levelPickPoint;
			} else {
				pointToUse = skewedPickPoint;
			}
		}
		if( pointToUse != null ) {
			pointToUse.y = this.manipulatedTransformable.getAbsoluteTransformation().translation.y;
			return pointToUse;
		} else {
			return null;
		}
	}

	private Point3 getPerspectiveMovementVector( InputState currentInput, InputState previousInput ) {
		if( ( this.pickPlane == null ) && ( this.backPlane == null ) ) {
			int mouseDifX = ( currentInput.getMouseLocation().x - previousInput.getMouseLocation().x );
			double rightMovement = mouseDifX * this.movementScale;
			Vector3 rightVector = new Vector3( this.camera.getAbsoluteTransformation().orientation.right );
			rightVector.y = 0;
			rightVector.normalize();
			Point3 movementVector = Point3.createMultiplication( rightVector, rightMovement );
			int mouseDifY = ( currentInput.getMouseLocation().y - previousInput.getMouseLocation().y );
			double forwardMovement = mouseDifY * this.movementScale;
			Vector3 backwardVector = new Vector3( this.camera.getAbsoluteTransformation().orientation.backward );
			backwardVector.y = 0;
			backwardVector.normalize();
			if( backwardVector.isNaN() ) {
				backwardVector = new Vector3( this.camera.getAbsoluteTransformation().orientation.up );
				backwardVector.y = 0;
				backwardVector.multiply( -1 );
				backwardVector.normalize();
			}
			movementVector.add( Point3.createMultiplication( backwardVector, forwardMovement ) );
			return movementVector;
		}
		Point3 pointToUse = getPerspectivePositionBasedOnInput( currentInput );
		if( pointToUse != null ) {
			Point3 movementChange = Point3.createSubtraction( pointToUse, this.getManipulatedTransformable().getAbsoluteTransformation().translation );
			movementChange.y = 0;
			return movementChange;
		} else {
			return new Point3( 0, 0, 0 );
		}
	}

	@Override
	public void doDataUpdateManipulator( InputState currentInput, InputState previousInput ) {
		if( !currentInput.getMouseLocation().equals( previousInput.getMouseLocation() ) && ( this.manipulatedTransformable != null ) ) {
			if( !this.hasMoved ) {
				this.hasMoved = true;
				this.hideCursor();
			}

			Point3 movementVector = getMovementVectorBasedOnCamera( currentInput, previousInput );
			Point3 currentPosition = this.manipulatedTransformable.getAbsoluteTransformation().translation;
			Point3 newPosition = Point3.createAddition( currentPosition, movementVector );

			newPosition = SnapUtilities.doMovementSnapping( this.manipulatedTransformable, newPosition, this.dragAdapter, this.manipulatedTransformable.getRoot(), this.getCamera() );

			//Send manipulation events
			Vector3 movementDif = Vector3.createSubtraction( newPosition, this.manipulatedTransformable.getAbsoluteTransformation().translation );
			if( movementDif.x > .1 ) {
				movementVector = getMovementVectorBasedOnCamera( currentInput, previousInput );
			}
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
				planeTransitionPointDebugSphere.setLocalTranslation( newPosition );
				//				Point awtPoint = getMouseCursorPositionInLookingGlass();
				//				if (!isPointInsideLookingGlass(awtPoint))
				//				{
				//					moveCursorToPointInLookingGlass(awtPoint);
				//				}
			}
		}
	}

	@Override
	public void doClickManipulator( InputState clickInput, InputState previousInput ) {
		//Do nothing
	}

	@Override
	public void doEndManipulator( InputState endInput, InputState previousInput ) {
		removePlaneTransitionPointSphereFromScene();
		this.showCursor();
	}

	protected AbstractTransformable getInitialTransformable( InputState startInput ) {
		return startInput.getClickPickTransformable();
	}

	protected Point3 getInitialClickPoint( InputState startInput ) {
		Point3 initialClickPoint = new Point3();
		startInput.getClickPickResult().getPositionInSource( initialClickPoint );
		startInput.getClickPickResult().getSource().transformTo_AffectReturnValuePassedIn( initialClickPoint, startInput.getClickPickResult().getSource().getRoot() );
		return initialClickPoint;
	}

	@Override
	public boolean doStartManipulator( InputState startInput ) {
		this.setManipulatedTransformable( this.getInitialTransformable( startInput ) );
		this.hidCursor = false;
		if( this.manipulatedTransformable != null ) {
			this.initializeEventMessages();
			this.hasMoved = false;
			this.originalPosition = this.manipulatedTransformable.getAbsoluteTransformation().translation;
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
			this.mousePlaneOffset = calculateMousePlaneOffset( startInput.getMouseLocation(), this.manipulatedTransformable );

			//We don't need special planes for the orthographic camera
			if( !( this.getCamera() instanceof OrthographicCamera ) ) {
				Point mousePoint = new Point( startInput.getMouseLocation().x + this.mousePlaneOffset.x, startInput.getMouseLocation().y + this.mousePlaneOffset.y );
				setUpPlanes( this.originalPosition, mousePoint );
			}

			addPlaneTransitionPointSphereToScene();
			return true;
		}
		return false;
	}

	protected void setUpPlanes( Point3 planePosition, Point mousePoint ) {
		Plane horizontalPlane = Plane.createInstance( planePosition, Vector3.accessPositiveYAxis() );
		AffineMatrix4x4 cameraTransform = this.getCamera().getParent().getAbsoluteTransformation();
		boolean isAbove = cameraTransform.translation.y > planePosition.y;

		Point3 pointInCameraSidewaysPlane = PlaneUtilities.projectPointIntoPlane( Plane.createInstance( cameraTransform.translation, cameraTransform.orientation.right ), planePosition );
		Vector3 toCamera = Vector3.createSubtraction( cameraTransform.translation, pointInCameraSidewaysPlane );
		toCamera.normalize();
		double verticalDistance = Math.abs( cameraTransform.translation.y - planePosition.y );
		double distance = Point3.calculateDistanceBetween( planePosition, cameraTransform.translation );
		double dot = Vector3.calculateDotProduct( toCamera, cameraTransform.orientation.backward );
		double dotLevel = Vector3.calculateDotProduct( Vector3.accessPositiveYAxis(), cameraTransform.orientation.up );
		AngleInRadians angle = VectorUtilities.getAngleBetweenVectors( toCamera, cameraTransform.orientation.backward );
		AngleInRadians angleUp = VectorUtilities.getAngleBetweenVectors( cameraTransform.orientation.up, Vector3.accessPositiveYAxis() );

		Ray pickRay = PlaneUtilities.getRayFromPixel( this.onscreenRenderTarget, this.getCamera(), mousePoint.x, mousePoint.y );
		double pickDotHorizontal = Vector3.calculateDotProduct( pickRay.accessDirection(), Vector3.accessPositiveYAxis() );
		//		PrintUtilities.println("pick: "+pickDotHorizontal+", object dot: "+dot+", leveDot: "+dotLevel+", angle: "+angle.getAsDegrees()+", angle up: "+angleUp.getAsDegrees());
		if( Math.abs( pickDotHorizontal ) < .001 ) {
			//			PrintUtilities.println("Special!");
			this.pickPlane = null;
			this.backPlane = null;
			double distanceToObject = Point3.calculateDistanceBetween( planePosition, cameraTransform.translation );
			this.movementScale = distanceToObject * .001;
			return;
		}

		Vector3 worstCasePlaneNormal = new Vector3( cameraTransform.orientation.backward );
		worstCasePlaneNormal.y = 0;
		worstCasePlaneNormal.normalize();
		worstCasePlaneNormal.y = Math.tan( ( Math.PI * 75.0 ) / ( 180.0 ) );
		if( !isAbove ) {
			worstCasePlaneNormal.y *= -1;
		}
		worstCasePlaneNormal.normalize();

		Ray centerRay = PlaneUtilities.getRayFromPixel( this.onscreenRenderTarget, this.getCamera(), mousePoint.x, mousePoint.y );
		Ray oneUp = PlaneUtilities.getRayFromPixel( this.onscreenRenderTarget, this.getCamera(), mousePoint.x, mousePoint.y - 1 );
		Ray oneDown = PlaneUtilities.getRayFromPixel( this.onscreenRenderTarget, this.getCamera(), mousePoint.x, mousePoint.y + 1 );
		Point3 centerPoint = PlaneUtilities.getPointInPlane( horizontalPlane, centerRay );
		Point3 upPoint = PlaneUtilities.getPointInPlane( horizontalPlane, oneUp );
		Point3 downPoint = PlaneUtilities.getPointInPlane( horizontalPlane, oneDown );

		boolean shouldUseHorizontalPlane = false;
		if( ( centerPoint != null ) && ( upPoint != null ) && ( downPoint != null ) ) {
			double cameraHeight = Math.abs( cameraTransform.translation.y - planePosition.y );
			double cameraDotUp = Vector3.calculateDotProduct( cameraTransform.orientation.up, Vector3.accessPositiveYAxis() );

			//			System.out.println("height: "+cameraHeight+", dot: "+cameraDotUp+", ratio: "+cameraDotUp/cameraHeight);

			double distanceUp = Point3.calculateDistanceBetween( centerPoint, upPoint );
			double distanceDown = Point3.calculateDistanceBetween( centerPoint, downPoint );
			double higher, lower;
			Vector3 awayFromCamera;
			if( distanceUp >= distanceDown ) {
				higher = distanceUp;
				lower = distanceDown;
				awayFromCamera = Vector3.createSubtraction( upPoint, centerPoint );
				awayFromCamera.normalize();
			} else {
				higher = distanceDown;
				lower = distanceUp;
				awayFromCamera = Vector3.createSubtraction( downPoint, centerPoint );
				awayFromCamera.normalize();
			}
			double distanceIncreasePerPixel = ( higher - lower );
			double pixelsToMaxPixel = ( MAX_DISTANCE_PER_PIXEL - higher ) / distanceIncreasePerPixel;
			double totalExtraDistance = MAX_DISTANCE_PER_PIXEL * pixelsToMaxPixel * .5; //basically the integration of this linear equations
			Vector3 extraVector = Vector3.createMultiplication( awayFromCamera, totalExtraDistance );
			planePosition.add( extraVector );
			shouldUseHorizontalPlane = true;
		}

		//		planeTransitionPointDebugSphere.setLocalTranslation(planePosition);
		Plane backPlane = Plane.createInstance( planePosition, worstCasePlaneNormal );
		if( shouldUseHorizontalPlane ) {
			this.pickPlane = horizontalPlane;
		} else {
			this.pickPlane = backPlane;
		}
		this.backPlane = backPlane;
	}

	protected Point calculateMousePlaneOffset( Point mousePosition, edu.cmu.cs.dennisc.scenegraph.AbstractTransformable transformable ) {
		Point3 pointInCamera = transformable.getTranslation( this.getCamera() );
		Point awtPoint = edu.cmu.cs.dennisc.render.PicturePlaneUtils.transformFromCameraToAWT_New( pointInCamera, this.onscreenRenderTarget, this.getCamera() );
		return new Point( awtPoint.x - mousePosition.x, awtPoint.y - mousePosition.y );
	}

	protected boolean isPointInsideLookingGlass( Point awtPoint ) {
		return this.onscreenRenderTarget.getAwtComponent().contains( awtPoint );
	}

	protected Point getMouseCursorPositionInLookingGlass() {
		Point3 new3DPoint = Point3.createAddition( this.manipulatedTransformable.getAbsoluteTransformation().translation, this.offsetFromOrigin );
		Point3 pointInCamera = this.camera.transformFrom_New( new3DPoint, this.camera.getRoot() );
		Point awtPoint = edu.cmu.cs.dennisc.render.PicturePlaneUtils.transformFromCameraToAWT_New( pointInCamera, this.onscreenRenderTarget, this.getCamera() );
		return awtPoint;
	}

	protected void moveCursorToPointInLookingGlass( Point awtPoint ) {
		edu.cmu.cs.dennisc.java.awt.RobotUtilities.mouseMove( this.onscreenRenderTarget.getAwtComponent(), awtPoint );
	}

	protected void moveCursorToObjectRelativePosition() {
		Point awtPoint = getMouseCursorPositionInLookingGlass();
		moveCursorToPointInLookingGlass( awtPoint );
	}

	protected void hideCursor() {
		CursorUtilities.pushAndSet( this.onscreenRenderTarget.getAwtComponent(), CursorUtilities.NULL_CURSOR );
		this.hidCursor = true;
	}

	protected void showCursor() {
		if( this.hidCursor ) {
			this.moveCursorToObjectRelativePosition();
			CursorUtilities.popAndSet( this.onscreenRenderTarget.getAwtComponent() );
		}
	}

	@Override
	public void doTimeUpdateManipulator( double time, InputState currentInput ) {
	}

	@Override
	protected HandleSet getHandleSetToEnable() {
		return HandleSet.ABSOLUTE_GROUND_TRANSLATION_VISUALIZATION;
	}

	private final DebugSphere planeTransitionPointDebugSphere = new DebugSphere();
	private Plane pickPlane = edu.cmu.cs.dennisc.math.Plane.XZ_PLANE;
	private Plane backPlane;
	protected Plane orthographicPickPlane = edu.cmu.cs.dennisc.math.Plane.XZ_PLANE;
	protected Point3 orthographicOffsetToOrigin = null;
	protected Boolean hasMoved = false;
	protected Point3 originalPosition = null;
	protected Point3 offsetFromOrigin = null;
	protected Point mousePlaneOffset;
	protected boolean hidCursor = false;
	private double movementScale = 1.0;
	protected AbstractCamera camera = null;
	protected edu.cmu.cs.dennisc.render.OnscreenRenderTarget onscreenRenderTarget;
}
