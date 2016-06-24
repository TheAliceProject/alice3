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

import edu.cmu.cs.dennisc.color.Color4f;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.AngleInDegrees;
import edu.cmu.cs.dennisc.math.Plane;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Tuple3;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.scenegraph.AbstractTransformable;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;
import edu.cmu.cs.dennisc.scenegraph.Geometry;
import edu.cmu.cs.dennisc.scenegraph.SimpleAppearance;
import edu.cmu.cs.dennisc.scenegraph.Sphere;
import edu.cmu.cs.dennisc.scenegraph.StandIn;
import edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera;
import edu.cmu.cs.dennisc.scenegraph.Transformable;
import edu.cmu.cs.dennisc.scenegraph.Visual;

/**
 * @author David Culyba
 */
public class CameraOrbitDragManipulator extends CameraManipulator {
	private static final double TURN_RATE = .2d;
	private static final boolean SHOW_SPHERE = false;

	public CameraOrbitDragManipulator() {
		super();
		setupPivotSphere();
	}

	private void setupPivotSphere() {
		if( SHOW_SPHERE ) {
			SimpleAppearance sgFrontFacingAppearance = new SimpleAppearance();
			sgFrontFacingAppearance.diffuseColor.setValue( Color4f.RED );
			sgFrontFacingAppearance.opacity.setValue( new Float( 1.0 ) );

			this.sgPivotSphereVisual.frontFacingAppearance.setValue( sgFrontFacingAppearance );
			this.sgPivotSphereVisual.setParent( this.pivotSphereTransformable );
			this.sgPivotSphereVisual.geometries.setValue( new Geometry[] { this.sgPivotSphere } );
			this.sgPivotSphere.radius.setValue( .1d );
		}
	}

	private void addPivotSphereToScene() {
		if( SHOW_SPHERE ) {
			if( ( this.camera != null ) && ( this.pivotSphereTransformable.getParent() == null ) ) {
				this.camera.getRoot().addComponent( this.pivotSphereTransformable );
			}
		}
	}

	private void removePivotSphereToScene() {
		if( SHOW_SPHERE ) {
			if( ( this.camera != null ) && ( this.pivotSphereTransformable.getParent() == this.camera.getRoot() ) ) {
				this.camera.getRoot().removeComponent( this.pivotSphereTransformable );
			}
		}
	}

	private void setPivotSpherePosition( Tuple3 position ) {
		if( SHOW_SPHERE ) {
			AffineMatrix4x4 transform = this.pivotSphereTransformable.localTransformation.getValue();
			transform.translation.set( position );
			this.pivotSphereTransformable.localTransformation.setValue( transform );
		}
	}

	public void setPivotPoint( Point3 pivotPoint ) {
		this.pivotPoint = pivotPoint;
		setPivotSpherePosition( pivotPoint );
	}

	@Override
	public String getUndoRedoDescription() {
		return "Camera Rotate";
	}

	@Override
	public CameraView getDesiredCameraView() {
		return CameraView.PICK_CAMERA;
	}

	@Override
	public void doDataUpdateManipulator( InputState currentInput, InputState previousInput ) {
		int xChange = currentInput.getMouseLocation().x - originalMousePoint.x;
		int yChange = currentInput.getMouseLocation().y - originalMousePoint.y;

		xChange *= -1; //Invert left to right

		double leftRightRotationAngle = xChange * TURN_RATE;
		double upDownRotationAngle = yChange * TURN_RATE;

		this.manipulatedTransformable.setLocalTransformation( this.originalLocalTransformation );

		StandIn standIn = new StandIn();
		standIn.setName( "CameraOrbitStandIn" );
		standIn.setVehicle( this.getCamera().getRoot() );
		try {
			standIn.setTranslationOnly( this.pivotPoint, AsSeenBy.SCENE );
			standIn.setAxesOnlyToPointAt( this.getCamera() );
			standIn.setAxesOnlyToStandUp();
			this.manipulatedTransformable.applyRotationAboutXAxis( new AngleInDegrees( upDownRotationAngle ), standIn );
			this.manipulatedTransformable.applyRotationAboutYAxis( new AngleInDegrees( leftRightRotationAngle ), standIn );
		} finally {
			standIn.setVehicle( null );
		}
	}

	@Override
	public void doEndManipulator( InputState endInput, InputState previousInput ) {
		removePivotSphereToScene();
	}

	@Override
	public void doClickManipulator( InputState clickInput, InputState previousInput ) {
		//Do nothing
	}

	@Override
	public boolean doStartManipulator( InputState startInput ) {
		if( super.doStartManipulator( startInput ) && ( this.camera instanceof SymmetricPerspectiveCamera ) ) {
			boolean success = false;

			this.originalLocalTransformation = new AffineMatrix4x4( manipulatedTransformable.getLocalTransformation() );
			this.originalMousePoint = new Point( startInput.getMouseLocation() );

			addPivotSphereToScene();

			AbstractTransformable clickedObject = startInput.getClickPickTransformable();
			if( clickedObject != null ) {
				this.setPivotPoint( clickedObject.getAbsoluteTransformation().translation );
				success = true;
			} else {
				Vector3 cameraForward = this.manipulatedTransformable.getAbsoluteTransformation().orientation.backward;
				cameraForward.multiply( -1.0d );

				double dotWithVertical = Math.abs( Vector3.calculateDotProduct( cameraForward, Vector3.accessPositiveYAxis() ) );
				if( dotWithVertical < .5 ) {
					double downwardShiftFactor = ( ( .5 - dotWithVertical ) / .5 ) * -.2;
					cameraForward.add( new Vector3( 0, downwardShiftFactor, 0 ) );
					cameraForward.normalize();
				}
				Point3 pickPoint = PlaneUtilities.getPointInPlane( Plane.XZ_PLANE, new edu.cmu.cs.dennisc.math.Ray( this.manipulatedTransformable.getAbsoluteTransformation().translation, cameraForward ) );
				if( pickPoint != null ) {
					this.setPivotPoint( pickPoint );
					success = true;
				}
			}
			return success;
		}
		return false;

	}

	@Override
	public void doTimeUpdateManipulator( double time, InputState currentInput ) {
	}

	private Point originalMousePoint;
	private AffineMatrix4x4 originalLocalTransformation;
	private Point3 pivotPoint = null;

	private Sphere sgPivotSphere = new Sphere();
	private Transformable pivotSphereTransformable = new Transformable();
	private Visual sgPivotSphereVisual = new Visual();
}
