/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
 */

package org.alice.interact.manipulator;

import org.alice.interact.AbstractDragAdapter.CameraView;
import org.alice.interact.InputState;
import org.alice.interact.PlaneUtilities;
import org.alice.interact.VectorUtilities;
import org.alice.interact.debug.DebugSphere;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.Angle;
import edu.cmu.cs.dennisc.math.Plane;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Ray;
import edu.cmu.cs.dennisc.math.Tuple3;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;
import edu.cmu.cs.dennisc.scenegraph.StandIn;
import edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera;

/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
 */

public class CameraTiltDragManipulator extends CameraManipulator implements OnscreenPicturePlaneInformedManipulator {

	private static final boolean SHOW_PICK_POINT = false;

	@Override
	public edu.cmu.cs.dennisc.render.OnscreenRenderTarget getOnscreenRenderTarget() {
		return this.onscreenRenderTarget;
	}

	@Override
	public void setOnscreenRenderTarget( edu.cmu.cs.dennisc.render.OnscreenRenderTarget onscreenRenderTarget ) {
		this.onscreenRenderTarget = onscreenRenderTarget;
	}

	private void addPickPointSphereToScene() {
		if( SHOW_PICK_POINT ) {
			if( ( this.camera != null ) && ( this.pickPointDebugSphere.getParent() == null ) ) {
				this.camera.getRoot().addComponent( this.pickPointDebugSphere );
			}
		}
	}

	private void removePickPointSphereFromScene() {
		if( SHOW_PICK_POINT ) {
			if( ( this.camera != null ) && ( this.pickPointDebugSphere.getParent() == this.camera.getRoot() ) ) {
				this.camera.getRoot().removeComponent( this.pickPointDebugSphere );
			}
		}
	}

	private void setPickPoint( Tuple3 position ) {
		if( SHOW_PICK_POINT ) {
			this.pickPointDebugSphere.setLocalTranslation( position );
		}
	}

	public void setPlaneDiscPoint( Point3 planeDiscPoint ) {
		this.pickPoint = planeDiscPoint;
		setPickPoint( this.pickPoint );

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
		Ray oldPickRay = PlaneUtilities.getRayFromPixel( this.onscreenRenderTarget, this.getCamera(), previousInput.getMouseLocation().x, previousInput.getMouseLocation().y );
		Ray newPickRay = PlaneUtilities.getRayFromPixel( this.onscreenRenderTarget, this.getCamera(), currentInput.getMouseLocation().x, currentInput.getMouseLocation().y );
		Point3 oldPickPoint = PlaneUtilities.getPointInPlane( this.cameraFacingPickPlane, oldPickRay );
		Point3 newPickPoint = PlaneUtilities.getPointInPlane( this.cameraFacingPickPlane, newPickRay );
		if( ( newPickPoint != null ) && ( oldPickPoint != null ) ) {
			this.setPlaneDiscPoint( pickPoint );

			Point3 oldPointInCamera = this.camera.transformFrom_New( oldPickPoint, this.camera.getRoot() );
			Point3 newPointInCamera = this.camera.transformFrom_New( newPickPoint, this.camera.getRoot() );

			Vector3 xDif = new Vector3( newPointInCamera.x, oldPointInCamera.y, oldPointInCamera.z );
			xDif.normalize();
			Vector3 yDif = new Vector3( oldPointInCamera.x, newPointInCamera.y, oldPointInCamera.z );
			yDif.normalize();

			Vector3 oldDirection = new Vector3( oldPointInCamera );
			oldDirection.normalize();

			Angle xAngle = VectorUtilities.getAngleBetweenVectors( oldDirection, xDif );
			if( currentInput.getMouseLocation().x < previousInput.getMouseLocation().x ) {
				xAngle.setAsRadians( xAngle.getAsRadians() * -1 );
			}
			Angle yAngle = VectorUtilities.getAngleBetweenVectors( oldDirection, yDif );
			if( currentInput.getMouseLocation().y < previousInput.getMouseLocation().y ) {
				yAngle.setAsRadians( yAngle.getAsRadians() * -1 );
			}

			StandIn standIn = new StandIn();
			standIn.setName( "CameraOrbitStandIn" );
			standIn.setVehicle( this.getCamera().getRoot() );
			try {
				standIn.setTransformation( this.manipulatedTransformable.getAbsoluteTransformation(), AsSeenBy.SCENE );
				standIn.setAxesOnlyToStandUp();
				this.manipulatedTransformable.applyRotationAboutXAxis( yAngle, standIn );
				this.manipulatedTransformable.applyRotationAboutYAxis( xAngle, standIn );
			} finally {
				standIn.setVehicle( null );
			}

			//Make sure the camera's x-axis is still horizontal
			AffineMatrix4x4 cameraTransform = this.manipulatedTransformable.getAbsoluteTransformation();
			Vector3 rightAxis = cameraTransform.orientation.right;
			rightAxis.y = 0;
			rightAxis.normalize();
			Vector3 upAxis = Vector3.createCrossProduct( cameraTransform.orientation.backward, rightAxis );
			upAxis.normalize();
			cameraTransform.orientation.right.set( rightAxis );
			cameraTransform.orientation.up.set( upAxis );
			this.manipulatedTransformable.setTransformation( cameraTransform, AsSeenBy.SCENE );

			this.cameraFacingPickPlane = Plane.createInstance( newPickPoint, this.manipulatedTransformable.getAbsoluteTransformation().orientation.backward );
		}
	}

	@Override
	public void doEndManipulator( InputState endInput, InputState previousInput ) {
		removePickPointSphereFromScene();
	}

	@Override
	public void doClickManipulator( InputState clickInput, InputState previousInput ) {
		//Do nothing
	}

	@Override
	public boolean doStartManipulator( InputState startInput ) {
		if( super.doStartManipulator( startInput ) && ( this.camera instanceof SymmetricPerspectiveCamera ) ) {
			boolean success = false;
			Vector3 cameraForward = this.manipulatedTransformable.getAbsoluteTransformation().orientation.backward;
			cameraForward.multiply( -10.0d );

			addPickPointSphereToScene();

			Ray pickRay = PlaneUtilities.getRayFromPixel( this.onscreenRenderTarget, this.getCamera(), startInput.getMouseLocation().x, startInput.getMouseLocation().y );

			Point3 planePoint = Point3.createAddition( this.manipulatedTransformable.getAbsoluteTransformation().translation, cameraForward );
			this.cameraFacingPickPlane = Plane.createInstance( planePoint, this.manipulatedTransformable.getAbsoluteTransformation().orientation.backward );

			Point3 pickPoint = PlaneUtilities.getPointInPlane( this.cameraFacingPickPlane, pickRay );
			if( pickPoint != null ) {
				this.setPlaneDiscPoint( pickPoint );
				success = true;
			}
			return success;
		}
		return false;
	}

	@Override
	public void doTimeUpdateManipulator( double time, InputState currentInput ) {
	}

	private final DebugSphere pickPointDebugSphere = new DebugSphere();

	private Plane cameraFacingPickPlane;
	private Point3 pickPoint = null;
	private edu.cmu.cs.dennisc.render.OnscreenRenderTarget onscreenRenderTarget;
}
