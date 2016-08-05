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
import org.alice.interact.PickHint;
import org.alice.interact.PlaneUtilities;
import org.alice.interact.VectorUtilities;
import org.alice.interact.condition.MovementDescription;
import org.alice.interact.event.ManipulationEvent;
import org.alice.interact.handle.HandleSet;
import org.alice.interact.handle.RotationRingHandle;

import edu.cmu.cs.dennisc.java.awt.CursorUtilities;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.Angle;
import edu.cmu.cs.dennisc.math.AngleInRadians;
import edu.cmu.cs.dennisc.math.AngleUtilities;
import edu.cmu.cs.dennisc.math.Plane;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Ray;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.AbstractTransformable;
import edu.cmu.cs.dennisc.scenegraph.util.TransformationUtilities;

/**
 * @author David Culyba
 */
public class ObjectRotateDragManipulator extends AbstractManipulator implements CameraInformedManipulator, OnscreenPicturePlaneInformedManipulator {

	private static final double BAD_ANGLE_THRESHOLD = 2.0d * Math.PI * ( 15.0d / 360.0d );
	private static final double WORLD_DISTANCE_TO_RADIANS_MULTIPLIER = 1.1d;

	//	protected Sphere sgSphere = new Sphere();
	//	protected Transformable sphereTransformable = new Transformable();
	//	protected Visual sgSphereVisual = new Visual();

	//	private void DEBUG_setupDebugSphere()
	//	{
	//		SingleAppearance sgFrontFacingAppearance = new SingleAppearance();
	//		sgFrontFacingAppearance.diffuseColor.setValue( Color4f.RED );
	//		sgFrontFacingAppearance.opacity.setValue( new Float(1.0) );
	//
	//		this.sgSphereVisual.frontFacingAppearance.setValue( sgFrontFacingAppearance );
	//		this.sgSphereVisual.setParent( this.sphereTransformable );
	//		this.sgSphereVisual.geometries.setValue( new Geometry[] { this.sgSphere } );
	//		this.sgSphere.radius.setValue( .2d);
	//	}
	//
	//	private void DEBUG_addDebugSphereToScene()
	//	{
	//		if (this.camera != null && this.sphereTransformable.getParent() == null)
	//		{
	//			this.camera.getRoot().addComponent(this.sphereTransformable);
	//		}
	//	}
	//
	//	private void DEBUG_removeDebugSphereFromScene()
	//	{
	//		if (this.camera != null && this.sphereTransformable.getParent() == this.camera.getRoot())
	//		{
	//			this.camera.getRoot().removeComponent(this.sphereTransformable);
	//		}
	//	}
	//
	//	private void DEBUG_setDebugSpherePosition(Tuple3 position)
	//	{
	//		this.sphereTransformable.setTranslationOnly(position, AsSeenBy.SCENE);
	//	}

	public ObjectRotateDragManipulator() {
		//		DEBUG_setupDebugSphere();
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
	protected void initializeEventMessages() {
		this.setMainManipulationEvent( new ManipulationEvent( ManipulationEvent.EventType.Rotate, null, this.manipulatedTransformable ) );
		this.clearManipulationEvents();
		if( rotationHandle != null ) {
			org.alice.interact.MovementType type = this.rotationHandle instanceof org.alice.interact.handle.StoodUpRotationRingHandle ? org.alice.interact.MovementType.STOOD_UP : org.alice.interact.MovementType.LOCAL;
			this.addManipulationEvent( new ManipulationEvent( ManipulationEvent.EventType.Rotate, new MovementDescription( this.rotationHandle.getRotationDirection(), type ), this.manipulatedTransformable ) );
		}
	}

	protected void initManipulator( RotationRingHandle handle, InputState startInput ) {
		//		DEBUG_addDebugSphereToScene();
		this.hidCursor = false;
		this.rotationHandle = handle;
		this.setManipulatedTransformable( this.rotationHandle.getManipulatedObject() );
		this.absoluteRotationAxis = this.rotationHandle.getReferenceFrame().getAbsoluteTransformation().createTransformed( this.rotationHandle.getRotationAxis() );
		this.absoluteRotationAxis.normalize();
		//PickResult pick = this.onscreenLookingGlass.pickFrontMost( startInput.getMouseLocation().x, startInput.getMouseLocation().y, /*isSubElementRequired=*/false );
		startInput.getClickPickResult().getPositionInSource( this.initialClickPoint );
		startInput.getClickPickResult().getSource().transformTo_AffectReturnValuePassedIn( this.initialClickPoint, startInput.getClickPickResult().getSource().getRoot() );
		Vector3 rotationAxis = this.absoluteRotationAxis;
		this.rotationPlane = Plane.createInstance( this.initialClickPoint, rotationAxis );

		this.rotationHandle.initializeSnapReferenceFrame();

		Ray originRay = new Ray( this.manipulatedTransformable.getAbsoluteTransformation().translation, rotationAxis );

		this.objectOriginInPlane = PlaneUtilities.getPointInPlane( this.rotationPlane, originRay );
		if( this.objectOriginInPlane == null ) {
			originRay = new Ray( this.manipulatedTransformable.getAbsoluteTransformation().translation, Vector3.createMultiplication( rotationAxis, -1.0d ) );
			this.objectOriginInPlane = PlaneUtilities.getPointInPlane( this.rotationPlane, originRay );
		}
		if( this.objectOriginInPlane != null ) {
			Vector3 toMouse = Vector3.createSubtraction( this.initialClickPoint, this.objectOriginInPlane );
			toMouse.normalize();
			this.originalMouseDirection = new Vector3( toMouse );
			this.originalMouseRightDirection = Vector3.createCrossProduct( this.originalMouseDirection, rotationAxis );

			this.rotationHandle.setSphereVisibility( true );
			Vector3 sphereDirection = TransformationUtilities.transformFromAbsolute_New( toMouse, this.rotationHandle );
			this.rotationHandle.setSphereDirection( sphereDirection );
			//Hide the cursor

		}

		//		DEBUG_setDebugSpherePosition(this.initialClickPoint);

		this.cameraFacingPlane = Plane.createInstance( this.initialClickPoint, this.getCamera().getAbsoluteTransformation().orientation.backward );
		this.originalLocalTransformation = new AffineMatrix4x4( manipulatedTransformable.getLocalTransformation() );
		this.originalAbsoluteTransformation = manipulatedTransformable.getAbsoluteTransformation();
		this.originalAngleBasedOnMouse = getRotationBasedOnMouse( startInput.getMouseLocation() );
	}

	@Override
	public boolean doStartManipulator( InputState startInput ) {
		if( startInput.getClickPickHint().intersects( PickHint.PickType.THREE_D_HANDLE.pickHint() ) ) {
			AbstractTransformable clickedHandle = startInput.getClickPickedTransformable( true );
			if( clickedHandle instanceof RotationRingHandle ) {
				this.initManipulator( (RotationRingHandle)clickedHandle, startInput );
				return true;
			}
		}
		return false;

	}

	protected Vector3 getVectorForAngle( Angle angle ) {
		AffineMatrix4x4 rotationTransform = this.rotationHandle.getAbsoluteTransformation();
		rotationTransform.applyRotationAboutYAxis( angle );
		return Vector3.createMultiplication( rotationTransform.orientation.backward, -1 );
	}

	protected Angle getRotationBasedOnMouse( Point mouseLocation ) {
		Ray pickRay = PlaneUtilities.getRayFromPixel( this.onscreenRenderTarget, this.getCamera(), mouseLocation.x, mouseLocation.y );
		if( pickRay != null ) {
			AngleInRadians angleBetweenVector = VectorUtilities.getAngleBetweenVectors( this.absoluteRotationAxis, this.getCamera().getAbsoluteTransformation().orientation.backward );
			double distanceToRightAngle = Math.abs( ( Math.PI * .5d ) - angleBetweenVector.getAsRadians() );
			if( distanceToRightAngle < BAD_ANGLE_THRESHOLD ) {
				Point3 pointInPlane = PlaneUtilities.getPointInPlane( this.cameraFacingPlane, pickRay );
				Vector3 fromOriginalMouseToCurrentMouse = Vector3.createSubtraction( pointInPlane, this.initialClickPoint );
				Vector3 rotationRightAxis = Vector3.createCrossProduct( this.absoluteRotationAxis, this.getCamera().getAbsoluteTransformation().orientation.backward );
				double mouseDistance = Vector3.calculateDotProduct( fromOriginalMouseToCurrentMouse, rotationRightAxis );

				return new AngleInRadians( mouseDistance * WORLD_DISTANCE_TO_RADIANS_MULTIPLIER );
			} else {
				Point3 pointInPlane = PlaneUtilities.getPointInPlane( this.rotationPlane, pickRay );
				if( pointInPlane != null ) {

					//<DEBUG>
					//					Point3 pickOrigin = new Point3(pickRay.accessOrigin());
					//					pickOrigin.y = 0;
					//					DEBUG_setDebugSpherePosition(pickOrigin);
					//</DEBUG>

					Vector3 toMouse = Vector3.createSubtraction( pointInPlane, this.objectOriginInPlane );
					double toMouseDotOriginalRight = Vector3.calculateDotProduct( toMouse, this.originalMouseRightDirection );
					//					double toMouseDotOriginalRight = Vector3.calculateDotProduct( toMouse, this.originalAbsoluteTransformation.orientation.right );
					boolean isToTheRight = toMouseDotOriginalRight > 0.0d;
					toMouse.normalize();
					Vector3 toMouseDirection = new Vector3( toMouse );
					double cosOfAngleBetween = Vector3.calculateDotProduct( this.originalMouseDirection, toMouseDirection );
					//					double cosOfAngleBetween = Vector3.calculateDotProduct(Vector3.createMultiplication(this.originalAbsoluteTransformation.orientation.backward, -1), toMouseDirection );
					if( cosOfAngleBetween > 1.0d ) {
						cosOfAngleBetween = 1.0d;
					} else if( cosOfAngleBetween < -1.0d ) {
						cosOfAngleBetween = -1.0d;
					}
					double angleInRadians = Math.acos( cosOfAngleBetween );
					if( isToTheRight ) {
						angleInRadians = ( Math.PI * 2.0d ) - angleInRadians;
					}
					return new AngleInRadians( angleInRadians );
				}
			}
		}
		return null;
	}

	@Override
	public void doDataUpdateManipulator( InputState currentInput, InputState previousInput ) {
		if( !currentInput.getMouseLocation().equals( previousInput.getMouseLocation() ) ) {
			if( !this.hidCursor ) {
				this.hideCursor();
			}
			Angle currentAngle = getRotationBasedOnMouse( currentInput.getMouseLocation() );
			if( ( currentAngle != null ) && ( this.originalAngleBasedOnMouse != null ) ) {
				Angle angleDif = AngleUtilities.createSubtraction( currentAngle, this.originalAngleBasedOnMouse );
				//The angleDif is the amount the object as rotated relative to the start of the manipulation
				//By snapping on angleDif, we're snapping to snap angles relative to the orientation at the start of the manipulation
				Angle snappedAngle = SnapUtilities.doRotationSnapping( angleDif, this.dragAdapter );
				boolean didSnap = snappedAngle.getAsDegrees() != angleDif.getAsDegrees();
				if( didSnap ) {
					angleDif = snappedAngle;
				}

				this.manipulatedTransformable.setLocalTransformation( this.originalLocalTransformation );
				this.manipulatedTransformable.applyRotationAboutArbitraryAxis( this.rotationHandle.getRotationAxis(), angleDif, this.rotationHandle.getReferenceFrame() );

				if( didSnap ) {
					SnapUtilities.showSnapRotation( this.rotationHandle );
				} else {
					SnapUtilities.hideRotationSnapVisualization();
				}
			}
		}
	}

	@Override
	public void doTimeUpdateManipulator( double time, InputState currentInput ) {
	}

	@Override
	public void doClickManipulator( InputState clickInput, InputState previousInput ) {
		//Do nothing
	}

	protected void hideCursor() {
		CursorUtilities.pushAndSet( this.onscreenRenderTarget.getAwtComponent(), CursorUtilities.NULL_CURSOR );
		this.hidCursor = true;
	}

	protected void showCursor() {
		if( this.hidCursor )
		{
			try {
				Point3 pointInCamera = this.rotationHandle.getSphereLocation( this.getCamera() );
				Point awtPoint = edu.cmu.cs.dennisc.render.PicturePlaneUtils.transformFromCameraToAWT_New( pointInCamera, this.onscreenRenderTarget, this.getCamera() );
				edu.cmu.cs.dennisc.java.awt.RobotUtilities.mouseMove( this.onscreenRenderTarget.getAwtComponent(), awtPoint );
			} finally {
				CursorUtilities.popAndSet( this.onscreenRenderTarget.getAwtComponent() );
				//mmay ask dave?
				hidCursor = false;
			}
		}
	}

	@Override
	public String getUndoRedoDescription() {
		return "Object Rotate";
	}

	@Override
	public void doEndManipulator( InputState endInput, InputState previousInput ) {
		this.rotationHandle.setSphereVisibility( false );
		SnapUtilities.hideRotationSnapVisualization();
		this.showCursor();
		//		DEBUG_removeDebugSphereFromScene();
	}

	@Override
	protected HandleSet getHandleSetToEnable() {
		return new HandleSet( this.rotationHandle.getRotationDirection().getHandleGroup(), HandleSet.HandleGroup.VISUALIZATION, HandleSet.HandleGroup.ROTATION );
	}

	private Point3 initialClickPoint = new Point3();
	private Point3 objectOriginInPlane;
	private Plane rotationPlane;
	private Vector3 originalMouseDirection;
	private Vector3 originalMouseRightDirection;
	private Vector3 absoluteRotationAxis;
	private Angle originalAngleBasedOnMouse;
	private AffineMatrix4x4 originalLocalTransformation;
	private AffineMatrix4x4 originalAbsoluteTransformation;
	private Plane cameraFacingPlane;
	protected RotationRingHandle rotationHandle;
	private AbstractCamera camera = null;
	private edu.cmu.cs.dennisc.render.OnscreenRenderTarget onscreenRenderTarget;
	private boolean hidCursor = false;
}
