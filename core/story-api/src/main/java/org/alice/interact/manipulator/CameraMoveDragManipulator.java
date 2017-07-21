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
import org.alice.interact.PlaneUtilities;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.Plane;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Ray;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;
import edu.cmu.cs.dennisc.scenegraph.OrthographicCamera;

public class CameraMoveDragManipulator extends CameraManipulator implements OnscreenPicturePlaneInformedManipulator {
	private static final double PIXEL_DISTANCE_FACTOR = 200.0d;
	private static final double MAX_DISTANCE_PER_PIXEL = .05d;

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
		return "Camera Move";
	}

	@Override
	public CameraView getDesiredCameraView() {
		return CameraView.PICK_CAMERA;
	}

	@Override
	public void doDataUpdateManipulator( InputState currentInput, InputState previousInput ) {
		int xChange = currentInput.getMouseLocation().x - originalMousePoint.x;
		int yChange = currentInput.getMouseLocation().y - originalMousePoint.y;
		xChange *= -1; //invert X

		Vector3 translationX = Vector3.createMultiplication( moveXVector, xChange * this.worldUnitsPerPixelX );
		Vector3 translationY = Vector3.createMultiplication( moveYVector, yChange * this.worldUnitsPerPixelY );

		this.manipulatedTransformable.setLocalTransformation( this.originalLocalTransformation );
		this.manipulatedTransformable.applyTranslation( translationX, AsSeenBy.SCENE );
		this.manipulatedTransformable.applyTranslation( translationY, AsSeenBy.SCENE );
	}

	@Override
	public void doEndManipulator( InputState endInput, InputState previousInput ) {
	}

	@Override
	public void doClickManipulator( InputState clickInput, InputState previousInput ) {
		//Do nothing
	}

	@Override
	public boolean doStartManipulator( InputState startInput ) {
		if( super.doStartManipulator( startInput ) ) {
			this.originalLocalTransformation = new AffineMatrix4x4( manipulatedTransformable.getLocalTransformation() );
			this.originalMousePoint = new Point( startInput.getMouseLocation() );
			AffineMatrix4x4 absoluteTransform = this.manipulatedTransformable.getAbsoluteTransformation();
			initialCameraDotVertical = Vector3.calculateDotProduct( absoluteTransform.orientation.backward, Vector3.accessPositiveYAxis() );
			initialCameraDotVertical = Math.abs( initialCameraDotVertical );
			if( this.camera instanceof OrthographicCamera ) {
				moveXVector = new Vector3( absoluteTransform.orientation.right );
				moveYVector = new Vector3( absoluteTransform.orientation.up );
			} else {
				if( initialCameraDotVertical > .99999 ) {
					moveYVector = new Vector3( absoluteTransform.orientation.up );
				} else {
					moveYVector = new Vector3( absoluteTransform.orientation.backward );
					moveYVector.multiply( -1 );
				}
				moveXVector = new Vector3( absoluteTransform.orientation.right );
				moveYVector.y = 0;
				moveYVector.normalize();
				moveXVector.y = 0;
				moveXVector.normalize();
			}

			initialDistanceToGround = Math.abs( absoluteTransform.translation.y );
			pickDistance = -1;
			Vector3 cameraForward = new Vector3( absoluteTransform.orientation.backward );
			cameraForward.multiply( -1.0d );
			Point3 pickPoint = PlaneUtilities.getPointInPlane( Plane.XZ_PLANE, new edu.cmu.cs.dennisc.math.Ray( this.manipulatedTransformable.getAbsoluteTransformation().translation, cameraForward ) );
			if( pickPoint != null ) {
				pickDistance = Point3.calculateDistanceBetween( pickPoint, absoluteTransform.translation );
			}
			calculateMovementFactors( startInput.getMouseLocation() );
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

	@Override
	public void doTimeUpdateManipulator( double time, InputState currentInput ) {
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

	private edu.cmu.cs.dennisc.render.OnscreenRenderTarget onscreenRenderTarget;
}
