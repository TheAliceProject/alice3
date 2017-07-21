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

import java.awt.Color;

import org.alice.interact.InputState;
import org.alice.interact.MovementDirection;
import org.alice.interact.MovementType;
import org.alice.interact.condition.MovementDescription;
import org.alice.interact.event.ManipulationEvent;
import org.alice.stageide.sceneeditor.interact.croquet.PredeterminedSetOrthographicPicturePlaneActionOperation;
import org.alice.stageide.sceneeditor.interact.handles.ImageBasedManipulationHandle2D;

import edu.cmu.cs.dennisc.math.ClippedZPlane;
import edu.cmu.cs.dennisc.math.Vector2;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.OrthographicCamera;
import edu.cmu.cs.dennisc.scenegraph.ReferenceFrame;

public class OrthographicCameraDragZoomManipulator extends Camera2DDragManipulator {
	private static final Color IN = Color.RED;
	private static final Color OUT = Color.GREEN;

	private static final double INITIAL_ZOOM_FACTOR = .1d;
	private static final double ZOOMS_PER_SECOND = .1d;
	private static final double ZOOM_CLICK_FACTOR = 1d;

	public static final double MAX_ZOOM = 30.0d;
	public static final double MIN_ZOOM = .01d;

	public OrthographicCameraDragZoomManipulator( ImageBasedManipulationHandle2D handle ) {
		super( handle );
	}

	public double getCameraZoom() {
		OrthographicCamera orthoCam = (OrthographicCamera)this.camera;
		ClippedZPlane picturePlane = orthoCam.picturePlane.getValue();
		return picturePlane.getHeight();
	}

	public void setCameraZoom( double amount ) {
		OrthographicCamera orthoCam = (OrthographicCamera)this.camera;
		ClippedZPlane picturePlane = orthoCam.picturePlane.getValue();
		double newZoom = picturePlane.getHeight() + amount;
		if( newZoom > MAX_ZOOM ) {
			newZoom = MAX_ZOOM;
		} else if( newZoom < MIN_ZOOM ) {
			newZoom = MIN_ZOOM;
		}
		picturePlane.setHeight( newZoom );
		orthoCam.picturePlane.setValue( picturePlane );
	}

	@Override
	protected void initializeEventMessages() {
		this.setMainManipulationEvent( new ManipulationEvent( ManipulationEvent.EventType.Zoom, null, this.manipulatedTransformable ) );
		this.clearManipulationEvents();
		this.addManipulationEvent( new ManipulationEvent( ManipulationEvent.EventType.Zoom, new MovementDescription( MovementDirection.FORWARD, MovementType.LOCAL ), this.manipulatedTransformable ) );
		this.addManipulationEvent( new ManipulationEvent( ManipulationEvent.EventType.Zoom, new MovementDescription( MovementDirection.BACKWARD, MovementType.LOCAL ), this.manipulatedTransformable ) );
	}

	@Override
	public void setCamera( AbstractCamera camera ) {
		super.setCamera( camera );
		assert camera instanceof OrthographicCamera : this;
		initializeEventMessages();
	}

	protected double getZoomValueForColor( Color color ) {
		double initialZoom = 0.0d;
		if( color != null ) {
			if( color.equals( IN ) ) {
				initialZoom = -INITIAL_ZOOM_FACTOR;
			} else if( color.equals( OUT ) ) {
				initialZoom = INITIAL_ZOOM_FACTOR;
			}
		}
		return initialZoom;
	}

	@Override
	public void undoRedoBeginManipulation() {
		if( this.getCamera() != null ) {
			this.originalZoomValue = this.getCameraZoom();
		}
	}

	@Override
	public void undoRedoEndManipulation() {
		if( this.getCamera() != null ) {
			double newZoom = this.getCameraZoom();

			if( newZoom == this.originalZoomValue ) {
				edu.cmu.cs.dennisc.java.util.logging.Logger.warning( "Adding an undoable action for a manipulation that didn't actually change the zoom." );
			}
			edu.cmu.cs.dennisc.animation.Animator animator;
			if( this.dragAdapter != null ) {
				animator = this.dragAdapter.getAnimator();
			} else {
				animator = null;
			}
			PredeterminedSetOrthographicPicturePlaneActionOperation undoOperation = new PredeterminedSetOrthographicPicturePlaneActionOperation( org.lgna.croquet.Application.PROJECT_GROUP, false, animator, (OrthographicCamera)this.camera, this.originalZoomValue, newZoom, getUndoRedoDescription() );
			undoOperation.fire();
		}
	}

	@Override
	public String getUndoRedoDescription() {
		return "Camera Zoom";
	}

	protected double getRelativeZoomAmount( Vector2 mousePos, double time ) {
		Vector2 relativeMousePos = Vector2.createSubtraction( mousePos, this.initialMousePosition );
		double amountToZoom = relativeMousePos.y * ZOOMS_PER_SECOND * time;
		return amountToZoom;
	}

	protected double getTotalZoomAmount( Vector2 mousePos, double time ) {
		double relativeZoomAmount = this.getRelativeZoomAmount( mousePos, time );
		double amountToZoomInitial = this.initialZoomValue * ZOOMS_PER_SECOND * time;
		double amountToZoom = relativeZoomAmount + amountToZoomInitial;
		return amountToZoom;
	}

	@Override
	public boolean doStartManipulator( InputState startInput ) {
		if( super.doStartManipulator( startInput ) ) {
			this.initialZoomValue = this.getZoomValueForColor( this.initialHandleColor );
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void doClickManipulator( InputState clickInput, InputState previousInput ) {
		if( doStartManipulator( clickInput ) ) {
			double amountToZoomClick = this.initialZoomValue * ZOOM_CLICK_FACTOR;
			applyZoom( amountToZoomClick );
		}
	}

	protected void applyZoom( double zoomAmount ) {
		this.setCameraZoom( zoomAmount );

		for( ManipulationEvent event : this.getManipulationEvents() ) {
			if( event.getMovementDescription().direction == MovementDirection.FORWARD ) {
				this.dragAdapter.triggerManipulationEvent( event, zoomAmount < 0.0d );
			} else if( event.getMovementDescription().direction == MovementDirection.BACKWARD ) {
				this.dragAdapter.triggerManipulationEvent( event, zoomAmount >= 0.0d );
			}
		}
	}

	@Override
	public void doTimeUpdateManipulator( double time, InputState currentInput ) {
		if( time < MIN_TIME ) {
			time = MIN_TIME;
		} else if( time > MAX_TIME ) {
			time = MAX_TIME;
		}

		Vector2 mousePos = new Vector2( currentInput.getMouseLocation().x, currentInput.getMouseLocation().y );
		double zoomAmount = this.getTotalZoomAmount( mousePos, time );
		applyZoom( zoomAmount );
	}

	@Override
	protected Vector3 getRelativeRotationAmount( Vector2 mousePos, double time ) {
		return new Vector3( 0.0d, 0.0d, 0.0d );
	}

	@Override
	protected ReferenceFrame getRotationReferenceFrame() {
		return this.getManipulatedTransformable();
	}

	@Override
	protected ReferenceFrame getMovementReferenceFrame() {
		return this.getManipulatedTransformable();
	}

	@Override
	protected Vector3 getMovementVectorForColor( Color color ) {
		return null;
	}

	@Override
	protected Vector3 getRelativeMovementAmount( Vector2 mousePos, double time ) {
		return null;
	}

	@Override
	protected Vector3 getRotationVectorForColor( Color color ) {
		return null;
	}

	private double initialZoomValue = 0.0d;
	private double originalZoomValue = 0.0d;
}
