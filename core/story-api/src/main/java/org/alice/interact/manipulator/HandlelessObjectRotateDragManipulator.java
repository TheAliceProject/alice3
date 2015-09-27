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
import org.alice.interact.PlaneUtilities;
import org.alice.interact.condition.MovementDescription;
import org.alice.interact.event.ManipulationEvent;
import org.alice.interact.handle.HandleSet;
import org.alice.interact.handle.ManipulationHandle3D;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.Angle;
import edu.cmu.cs.dennisc.math.AngleInRadians;
import edu.cmu.cs.dennisc.math.AngleUtilities;
import edu.cmu.cs.dennisc.math.Ray;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.Transformable;

/**
 * @author David Culyba
 */
public class HandlelessObjectRotateDragManipulator extends AbstractManipulator implements CameraInformedManipulator, OnscreenPicturePlaneInformedManipulator {
	protected static final double MOUSE_DISTANCE_TO_RADIANS_MULTIPLIER = .025d;

	public HandlelessObjectRotateDragManipulator() {
		super();
		this.standUpReference.setName( "Rotation StandUp Reference" );
		if( org.alice.interact.debug.DebugInteractUtilities.isDebugEnabled() ) {
			this.standUpReference.putBonusDataFor( ManipulationHandle3D.DEBUG_PARENT_TRACKER_KEY, this );
		}
	}

	public HandlelessObjectRotateDragManipulator( MovementDirection rotateAxisDirection ) {
		this.rotateAxisDirection = rotateAxisDirection;
		this.rotateAxis = this.rotateAxisDirection.getVector();
	}

	@Override
	public AbstractCamera getCamera() {
		return this.camera;
	}

	@Override
	public void setCamera( AbstractCamera camera ) {
		this.camera = camera;
		if( ( this.camera != null ) && ( this.camera.getParent() instanceof edu.cmu.cs.dennisc.scenegraph.AbstractTransformable ) ) {
			this.setManipulatedTransformable( (edu.cmu.cs.dennisc.scenegraph.AbstractTransformable)this.camera.getParent() );
		}
	}

	@Override
	public void setDesiredCameraView( CameraView cameraView ) {
		//this can only be ACTIVE_VIEW
	}

	@Override
	public CameraView getDesiredCameraView() {
		return CameraView.ACTIVE_VIEW;
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
		return "Object Rotate";
	}

	protected Angle getRotationBasedOnMouse( Point mouseLocation ) {
		Ray pickRay = PlaneUtilities.getRayFromPixel( this.onscreenRenderTarget, this.getCamera(), mouseLocation.x, mouseLocation.y );
		if( pickRay != null )
		{
			int xDif = mouseLocation.x - this.initialPoint.x;
			return new AngleInRadians( xDif * MOUSE_DISTANCE_TO_RADIANS_MULTIPLIER );
		}
		return null;
	}

	protected void initManipulator( InputState startInput ) {
		this.absoluteRotationAxis = this.manipulatedTransformable.getAbsoluteTransformation().createTransformed( this.rotateAxis );
		this.initialPoint = new Point( startInput.getMouseLocation() );
	}

	@Override
	protected void initializeEventMessages() {
		this.setMainManipulationEvent( new ManipulationEvent( ManipulationEvent.EventType.Rotate, null, this.manipulatedTransformable ) );
		this.clearManipulationEvents();
		if( this.rotateAxisDirection != null ) {
			this.addManipulationEvent( new ManipulationEvent( ManipulationEvent.EventType.Rotate, new MovementDescription( this.rotateAxisDirection, org.alice.interact.MovementType.STOOD_UP ), this.manipulatedTransformable ) );
		}
	}

	@Override
	public boolean doStartManipulator( InputState startInput ) {
		this.setManipulatedTransformable( startInput.getClickPickTransformable() );
		if( this.manipulatedTransformable != null ) {
			this.initManipulator( startInput );
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void doDataUpdateManipulator( InputState currentInput, InputState previousInput ) {
		if( !currentInput.getMouseLocation().equals( previousInput.getMouseLocation() ) ) {
			Angle currentAngle = getRotationBasedOnMouse( currentInput.getMouseLocation() );
			Angle previousAngle = getRotationBasedOnMouse( previousInput.getMouseLocation() );
			if( ( currentAngle != null ) && ( previousAngle != null ) ) {
				Angle angleDif = AngleUtilities.createSubtraction( currentAngle, previousAngle );
				//The angleDif is the amount the object as rotated relative to the start of the manipulation
				//By snapping on angleDif, we're snapping to snap angles relative to the orientation at the start of the manipulation
				Angle snappedAngle = SnapUtilities.doRotationSnapping( angleDif, this.dragAdapter );
				boolean didSnap = snappedAngle.getAsDegrees() != angleDif.getAsDegrees();
				if( didSnap ) {
					angleDif = snappedAngle;
				}
				this.standUpReference.setParent( this.manipulatedTransformable );
				this.standUpReference.localTransformation.setValue( AffineMatrix4x4.createIdentity() );
				this.standUpReference.setAxesOnlyToStandUp();
				this.manipulatedTransformable.applyRotationAboutArbitraryAxis( this.rotateAxis, angleDif, this.standUpReference );
			}
		}

	}

	@Override
	public void doClickManipulator( InputState clickInput, InputState previousInput ) {
		//Do nothing
	}

	@Override
	public void doEndManipulator( InputState endInput, InputState previousInput ) {
	}

	@Override
	public void doTimeUpdateManipulator( double time, InputState currentInput ) {
	}

	@Override
	protected HandleSet getHandleSetToEnable() {
		//		HandleSet.HandleGroup axisGroup;
		//		switch( STOOD_UP_TRANSLATION ) {
		//		case Y_AXIS:
		//			axisGroup = HandleSet.HandleGroup.STOOD_UP_Y_AXIS;
		//			break;
		//		case X_AXIS:
		//			axisGroup = HandleSet.HandleGroup.STOOD_UP_X_AXIS;
		//			break;
		//		case Z_AXIS:
		//			axisGroup = HandleSet.HandleGroup.STOOD_UP_Z_AXIS;
		//			break;
		//		default:
		//			axisGroup = HandleSet.HandleGroup.STOOD_UP_Y_AXIS;
		//		}
		return new HandleSet( this.rotateAxisDirection.getHandleGroup(), HandleSet.HandleGroup.VISUALIZATION, HandleSet.HandleGroup.STOOD_UP_ROTATION );
	}

	private Vector3 rotateAxis;
	private MovementDirection rotateAxisDirection;
	private edu.cmu.cs.dennisc.render.OnscreenRenderTarget onscreenRenderTarget;

	private Point initialPoint;
	private Vector3 absoluteRotationAxis;
	private Transformable standUpReference = new Transformable();

	private AbstractCamera camera = null;
}
