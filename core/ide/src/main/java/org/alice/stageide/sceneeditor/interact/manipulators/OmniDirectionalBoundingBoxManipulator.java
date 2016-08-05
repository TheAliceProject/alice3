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
import org.alice.interact.handle.HandleSet;
import org.alice.interact.manipulator.OmniDirectionalDragManipulator;
import org.alice.interact.manipulator.TargetManipulator;
import org.lgna.croquet.views.DragComponent;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.ClippedZPlane;
import edu.cmu.cs.dennisc.math.ForwardAndUpGuide;
import edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3;
import edu.cmu.cs.dennisc.math.Plane;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Ray;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;
import edu.cmu.cs.dennisc.scenegraph.OrthographicCamera;
import edu.cmu.cs.dennisc.scenegraph.Transformable;
import edu.cmu.cs.dennisc.scenegraph.util.BoundingBoxDecorator;
import edu.cmu.cs.dennisc.scenegraph.util.ModestAxes;

public class OmniDirectionalBoundingBoxManipulator extends OmniDirectionalDragManipulator implements TargetManipulator
{

	protected Transformable sgBoundingBoxTransformable = new Transformable();
	protected Transformable sgBoundingBoxOffsetTransformable = new Transformable();
	protected Transformable sgDecoratorOffsetTransformable = new Transformable();
	protected BoundingBoxDecorator sgBoundingBoxDecorator = new BoundingBoxDecorator();
	protected ModestAxes sgAxes = null;

	public OmniDirectionalBoundingBoxManipulator() {
		this.sgBoundingBoxOffsetTransformable.setParent( this.sgBoundingBoxTransformable );
		this.sgBoundingBoxOffsetTransformable.setLocalTransformation( AffineMatrix4x4.createIdentity() );
		AffineMatrix4x4 decoratorTransform = AffineMatrix4x4.createIdentity();
		decoratorTransform.translation.y = .01;
		this.sgDecoratorOffsetTransformable.setLocalTransformation( decoratorTransform );
		this.sgDecoratorOffsetTransformable.setParent( this.sgBoundingBoxOffsetTransformable );
		this.sgBoundingBoxDecorator.setParent( this.sgDecoratorOffsetTransformable );
	}

	@Override
	public AffineMatrix4x4 getTargetTransformation() {
		return this.sgBoundingBoxOffsetTransformable.getAbsoluteTransformation();
	}

	@Override
	protected void initializeEventMessages() {
		this.clearManipulationEvents();
	}

	@Override
	protected HandleSet getHandleSetToEnable() {
		return null;
	}

	@Override
	protected Transformable getInitialTransformable( InputState startInput ) {
		return this.sgBoundingBoxTransformable;
	}

	@Override
	protected Point3 getInitialClickPoint( InputState startInput ) {
		return this.manipulatedTransformable.getAbsoluteTransformation().translation;
	}

	private int getHorizonPixelLocation() {
		assert this.camera instanceof OrthographicCamera;
		OrthographicCamera orthoCamera = (OrthographicCamera)this.camera;
		AffineMatrix4x4 cameraTransform = orthoCamera.getAbsoluteTransformation();
		double dotProd = Vector3.calculateDotProduct( cameraTransform.orientation.up, Vector3.accessPositiveYAxis() );
		if( ( dotProd == 1 ) || ( dotProd == -1 ) ) {
			Point3 cameraPosition = orthoCamera.getAbsoluteTransformation().translation;
			ClippedZPlane dummyPlane = new ClippedZPlane( orthoCamera.picturePlane.getValue(), this.onscreenRenderTarget.getActualViewport( orthoCamera ) );
			double yRatio = this.onscreenRenderTarget.getSurfaceHeight() / dummyPlane.getHeight();
			double horizonInCameraSpace = 0.0d - cameraPosition.y;
			double distanceFromMaxY = dummyPlane.getYMaximum() - horizonInCameraSpace;
			int horizonLinePixelVal = (int)( yRatio * distanceFromMaxY );
			return horizonLinePixelVal;
		}
		return -1;
	}

	private boolean isHorizonInView() {
		assert this.camera instanceof OrthographicCamera;
		int horizonLinePixelVal = this.getHorizonPixelLocation();
		double lookingGlassHeight = this.onscreenRenderTarget.getSurfaceHeight();
		if( ( horizonLinePixelVal >= 0 ) && ( horizonLinePixelVal <= lookingGlassHeight ) ) {
			return true;
		}
		return false;
	}

	@Override
	protected Point3 getOthographicMovementVector( InputState currentInput, InputState previousInput ) {
		Ray pickRay = PlaneUtilities.getRayFromPixel( this.onscreenRenderTarget, this.getCamera(), currentInput.getMouseLocation().x, currentInput.getMouseLocation().y );
		Point3 pickPoint = PlaneUtilities.getPointInPlane( this.orthographicPickPlane, pickRay );
		if( isHorizonInView() ) {
			pickPoint.y = 0;
		}
		Point3 newPosition = Point3.createAddition( pickPoint, this.orthographicOffsetToOrigin );
		return Point3.createSubtraction( newPosition, this.getManipulatedTransformable().getAbsoluteTransformation().translation );
	}

	@Override
	public boolean doStartManipulator( InputState startInput ) {
		if( this.manipulatedTransformable != null ) {
			assert this.camera != null;
			this.sgBoundingBoxTransformable.setParent( this.camera.getRoot() );
			this.sgBoundingBoxTransformable.setTranslationOnly( new Point3( 0, 0, 0 ), AsSeenBy.SCENE );
			this.sgBoundingBoxDecorator.isShowing.setValue( true );
			this.setManipulatedTransformable( this.sgBoundingBoxTransformable );
			this.hidCursor = false;
			this.initializeEventMessages();
			this.hasMoved = false;
			this.orthographicOffsetToOrigin = new Point3( 0, 0, 0 );
			this.offsetFromOrigin = new Point3( 0, 0, 0 );
			this.mousePlaneOffset = new Point( 0, 0 );
			this.originalPosition = new Point3( 0, 0, 0 );

			org.lgna.croquet.history.DragStep dragStep = (org.lgna.croquet.history.DragStep)startInput.getDragAndDropContext();
			org.lgna.croquet.DragModel dragModel = dragStep.getModel();
			DragComponent dragSource = dragStep.getDragSource();
			dragSource.hideDragProxy();
			org.alice.ide.croquet.models.gallerybrowser.GalleryDragModel galleryDragModel = null;
			if( dragModel instanceof org.alice.ide.croquet.models.gallerybrowser.GalleryDragModel ) {
				galleryDragModel = (org.alice.ide.croquet.models.gallerybrowser.GalleryDragModel)dragModel;
			}

			//We don't need special planes for the orthographic camera
			if( !( this.getCamera() instanceof OrthographicCamera ) ) {
				setUpPlanes( new Point3( 0, 0, 0 ), startInput.getMouseLocation() );
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
				this.sgBoundingBoxTransformable.setAxesOnly( facingCameraOrientation, AsSeenBy.SCENE );
				this.sgBoundingBoxTransformable.setTranslationOnly( this.getPerspectivePositionBasedOnInput( startInput ), AsSeenBy.SCENE );
			}
			this.originalPosition = this.manipulatedTransformable.getAbsoluteTransformation().translation;
			AffineMatrix4x4 cameraTransform = this.getCamera().getParent().getAbsoluteTransformation();
			Vector3 toOrigin = Vector3.createSubtraction( this.originalPosition, cameraTransform.translation );
			toOrigin.normalize();
			Vector3 cameraFacingNormal = Vector3.createMultiplication( cameraTransform.orientation.backward, -1 );
			this.orthographicPickPlane = Plane.createInstance( new Point3( 0, 0, 0 ), cameraFacingNormal );
			addPlaneTransitionPointSphereToScene();

			boolean placeOnGround = false;
			edu.cmu.cs.dennisc.math.AxisAlignedBox box = null;
			if( galleryDragModel != null ) {
				box = galleryDragModel.getBoundingBox();
				placeOnGround = galleryDragModel.placeOnGround();
			}

			if( box == null ) {
				box = new AxisAlignedBox( new Point3( -.5, 0, -.5 ), new Point3( .5, 1, .5 ) );
			}

			AffineMatrix4x4 offsetTransform = AffineMatrix4x4.createIdentity();
			if( this.sgAxes != null ) {
				this.sgAxes.setParent( null );
			}
			if( box.isNaN() ) {
				this.sgBoundingBoxDecorator.isShowing.setValue( false );
				this.sgAxes = new ModestAxes( 1.0 );
			} else {
				if( placeOnGround ) {
					offsetTransform.translation.y += -box.getMinimum().y;
				}
				this.sgBoundingBoxDecorator.setBox( box );
				this.sgAxes = new ModestAxes( box.getWidth() * .5 );
			}
			this.sgBoundingBoxOffsetTransformable.setLocalTransformation( offsetTransform );
			this.sgAxes.setParent( this.sgDecoratorOffsetTransformable );
			return true;
		}
		return false;
	}

	@Override
	protected void hideCursor() {
		//don't hide the cursor
	}

	@Override
	protected void showCursor() {
		//don't hide the cursor, so don't re-show it
	}

	@Override
	public void doEndManipulator( InputState endInput, InputState previousInput ) {
		super.doEndManipulator( endInput, previousInput );
		this.sgBoundingBoxDecorator.isShowing.setValue( false );
		if( this.sgAxes != null ) {
			this.sgAxes.isShowing.setValue( false );
		}
		//		this.sgBoundingBoxTransformable.setParent(null);
		//		System.out.println("End drag position = "+this.sgBoundingBoxTransformable.localTransformation.getValue().translation);
		org.lgna.croquet.history.DragStep dragStep = (org.lgna.croquet.history.DragStep)endInput.getDragAndDropContext();
		if( dragStep != null ) {
			DragComponent dragSource = dragStep.getDragSource();
			dragSource.showDragProxy();
		}
	}

	@Override
	public boolean isUndoable() {
		return false;
	}

}
