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
package org.alice.stageide.sceneeditor.interact;

import org.alice.interact.AbstractDragAdapter;
import org.alice.interact.AbstractDragAdapter.CameraView;
import org.alice.interact.ModifierMask;
import org.alice.interact.ModifierMask.ModifierKey;
import org.alice.interact.PickHint;
import org.alice.interact.condition.ManipulatorConditionSet;
import org.alice.interact.condition.MouseDragCondition;
import org.alice.interact.condition.PickCondition;
import org.alice.interact.event.ManipulationEvent;
import org.alice.interact.event.ManipulationEventCriteria;
import org.alice.stageide.sceneeditor.interact.handles.ManipulationHandle2DCameraDriver;
import org.alice.stageide.sceneeditor.interact.handles.ManipulationHandle2DCameraStrafe;
import org.alice.stageide.sceneeditor.interact.handles.ManipulationHandle2DCameraTurnUpDown;
import org.alice.stageide.sceneeditor.interact.handles.ManipulationHandle2DCameraZoom;
import org.alice.stageide.sceneeditor.interact.manipulators.Camera2DDragDriveManipulator;
import org.alice.stageide.sceneeditor.interact.manipulators.Camera2DDragStrafeManipulator;
import org.alice.stageide.sceneeditor.interact.manipulators.Camera2DDragUpDownRotateManipulator;
import org.alice.stageide.sceneeditor.interact.manipulators.OrthographicCameraDragStrafeManipulator;
import org.alice.stageide.sceneeditor.interact.manipulators.OrthographicCameraDragZoomManipulator;

/**
 * @author David Culyba
 */
public class CameraNavigatorWidget extends org.lgna.croquet.views.LineAxisPanel {

	public static enum CameraMode {
		ORTHOGRAPHIC,
		PERSPECTIVE,
	}

	public CameraNavigatorWidget( AbstractDragAdapter dragAdapter, CameraView attachedView ) {
		super();

		//this.setLayout( new FlowLayout() );
		this.setBackgroundColor( null ); //transparent
		this.dragAdapter = dragAdapter;

		//CAMERA DRIVER
		//Create the new handle
		this.cameraDriver = new ManipulationHandle2DCameraDriver();
		//Create the manipulator
		Camera2DDragDriveManipulator driverManipulator = new Camera2DDragDriveManipulator( this.cameraDriver );
		//Set the desired view so the manipulator knows which camera to control 
		driverManipulator.setDesiredCameraView( attachedView );
		//Set up the handle to know about its own manipulator and conditions so the ObjectGlobalHandleDragManipulator can activate the control
		this.cameraDriver.setManipulation( driverManipulator );
		for( ManipulationEvent event : driverManipulator.getManipulationEvents() ) {
			this.cameraDriver.addCondition( new ManipulationEventCriteria(
					event.getType(),
					event.getMovementDescription(),
					PickHint.PickType.PERSPECTIVE_CAMERA.pickHint() ) );
		}
		//Set the handle to listen to the relevant events so it can update its appearance as things happen
		this.dragAdapter.addManipulationListener( this.cameraDriver );
		//Set the dragAdapter on the handle which sets up listening and adds the handle to the dragAdapter
		this.cameraDriver.setDragAdapterAndAddHandle( this.dragAdapter );

		//CAMERA ROTATE UP/DOWN
		//Create the new handle
		this.cameraControlUpDown = new ManipulationHandle2DCameraTurnUpDown();
		//Create the manipulator
		Camera2DDragUpDownRotateManipulator upDownManipulator = new Camera2DDragUpDownRotateManipulator( this.cameraControlUpDown );
		//Set the desired view so the manipulator knows which camera to control 
		upDownManipulator.setDesiredCameraView( attachedView );
		//Set up the handle to know about its own manipulator and conditions so the ObjectGlobalHandleDragManipulator can activate the control
		this.cameraControlUpDown.setManipulation( upDownManipulator );
		for( ManipulationEvent event : upDownManipulator.getManipulationEvents() ) {
			this.cameraControlUpDown.addCondition( new ManipulationEventCriteria(
					event.getType(),
					event.getMovementDescription(),
					PickHint.PickType.PERSPECTIVE_CAMERA.pickHint() ) );
		}
		//Set the handle to listen to the relevant events so it can update its appearance as things happen
		this.dragAdapter.addManipulationListener( this.cameraControlUpDown );
		//Set the dragAdapter on the handle which sets up listening and adds the handle to the dragAdapter
		this.cameraControlUpDown.setDragAdapterAndAddHandle( this.dragAdapter );

		//CAMERA STRAFE
		//Create the new handle
		this.cameraControlStrafe = new ManipulationHandle2DCameraStrafe();
		//Create the manipulator
		Camera2DDragStrafeManipulator strafeManipulator = new Camera2DDragStrafeManipulator( this.cameraControlStrafe );
		//Set the desired view so the manipulator knows which camera to control 
		strafeManipulator.setDesiredCameraView( attachedView );
		//Set up the handle to know about its own manipulator and conditions so the ObjectGlobalHandleDragManipulator can activate the control
		this.cameraControlStrafe.setManipulation( strafeManipulator );
		for( ManipulationEvent event : strafeManipulator.getManipulationEvents() ) {
			this.cameraControlStrafe.addCondition( new ManipulationEventCriteria(
					event.getType(),
					event.getMovementDescription(),
					PickHint.PickType.PERSPECTIVE_CAMERA.pickHint() ) );
		}
		//Set the handle to listen to the relevant events so it can update its appearance as things happen
		this.dragAdapter.addManipulationListener( this.cameraControlStrafe );
		//Set the dragAdapter on the handle which sets up listening and adds the handle to the dragAdapter
		this.cameraControlStrafe.setDragAdapterAndAddHandle( this.dragAdapter );

		//This is the manipulator used to strafe the camera when holding down shift and using the camera widget
		//Note that this is the only manipulator directly added to the dragAdapter
		//The dragAdapter will automatically activate the correct manipulator based on which handle was clicked
		strafeManipulator.setDragAdapter( this.dragAdapter );
		ManipulatorConditionSet mouseHandleDrag_Shift = new ManipulatorConditionSet( strafeManipulator );
		MouseDragCondition handleShiftCondition = new MouseDragCondition( java.awt.event.MouseEvent.BUTTON1, new PickCondition( PickHint.PickType.TWO_D_HANDLE.pickHint() ), new ModifierMask( ModifierKey.SHIFT ) );
		mouseHandleDrag_Shift.addCondition( handleShiftCondition );
		this.dragAdapter.addManipulatorConditionSet( mouseHandleDrag_Shift );

		//ORTHOGRAPHIC STRAFE
		//Create the new handle
		this.orthographicCameraControlStrafe = new ManipulationHandle2DCameraStrafe();
		//Create the manipulator
		OrthographicCameraDragStrafeManipulator orthoStrafeManipulator = new OrthographicCameraDragStrafeManipulator( this.cameraControlStrafe );
		//Set the desired view so the manipulator knows which camera to control 
		orthoStrafeManipulator.setDesiredCameraView( attachedView );
		//Set up the handle to know about its own manipulator and conditions so the ObjectGlobalHandleDragManipulator can activate the control
		this.orthographicCameraControlStrafe.setManipulation( orthoStrafeManipulator );
		for( ManipulationEvent event : orthoStrafeManipulator.getManipulationEvents() ) {
			this.orthographicCameraControlStrafe.addCondition( new ManipulationEventCriteria(
					event.getType(),
					event.getMovementDescription(),
					PickHint.PickType.ORTHOGRAPHIC_CAMERA.pickHint() ) );
		}
		//Set the handle to listen to the relevant events so it can update its appearance as things happen
		this.dragAdapter.addManipulationListener( this.orthographicCameraControlStrafe );
		//Set the dragAdapter on the handle which sets up listening and adds the handle to the dragAdapter
		this.orthographicCameraControlStrafe.setDragAdapterAndAddHandle( this.dragAdapter );

		//ORTHOGRAPHIC ZOOM
		//Create the new handle
		this.orthographicCameraControlZoom = new ManipulationHandle2DCameraZoom();
		//Create the manipulator
		OrthographicCameraDragZoomManipulator orthoZoomManipulator = new OrthographicCameraDragZoomManipulator( this.orthographicCameraControlZoom );
		//Set the desired view so the manipulator knows which camera to control 
		orthoZoomManipulator.setDesiredCameraView( attachedView );
		//Set up the handle to know about its own manipulator and conditions so the ObjectGlobalHandleDragManipulator can activate the control
		this.orthographicCameraControlZoom.setManipulation( orthoZoomManipulator );
		for( ManipulationEvent event : orthoZoomManipulator.getManipulationEvents() ) {
			this.orthographicCameraControlZoom.addCondition( new ManipulationEventCriteria(
					event.getType(),
					event.getMovementDescription(),
					PickHint.PickType.ORTHOGRAPHIC_CAMERA.pickHint() ) );
		}
		//Set the handle to listen to the relevant events so it can update its appearance as things happen
		this.dragAdapter.addManipulationListener( this.orthographicCameraControlZoom );
		//Set the dragAdapter on the handle which sets up listening and adds the handle to the dragAdapter
		this.orthographicCameraControlZoom.setDragAdapterAndAddHandle( this.dragAdapter );

		this.cameraMode = null;
		setMode( CameraMode.PERSPECTIVE ); //This will set the mode and also put the controls in the panel
	}

	public void setExpanded( boolean isExpanded ) {
		this.isExpanded = isExpanded;
		switch( this.cameraMode ) {
		case PERSPECTIVE: {
			this.cameraControlUpDown.setVisible( isExpanded );
			this.cameraControlStrafe.setVisible( isExpanded );
			this.cameraDriver.setVisible( true );
			this.orthographicCameraControlStrafe.setVisible( false );
			this.orthographicCameraControlZoom.setVisible( false );
		}
			break;
		case ORTHOGRAPHIC: {
			this.cameraControlUpDown.setVisible( false );
			this.cameraControlStrafe.setVisible( false );
			this.cameraDriver.setVisible( false );
			this.orthographicCameraControlStrafe.setVisible( true );
			this.orthographicCameraControlZoom.setVisible( true );
		}
			break;
		}

	}

	protected void setControlsBasedOnMode( CameraMode mode ) {
		this.removeAllComponents();
		this.setExpanded( this.isExpanded );
		javax.swing.JPanel jPanel = this.getAwtComponent();
		switch( mode ) {
		case PERSPECTIVE:
			jPanel.add( this.cameraControlStrafe );
			jPanel.add( this.cameraDriver );
			jPanel.add( this.cameraControlUpDown );
			break;
		case ORTHOGRAPHIC:
			jPanel.add( this.orthographicCameraControlStrafe );
			jPanel.add( this.orthographicCameraControlZoom );
			break;
		}
	}

	public void setToOrthographicMode() {
		setMode( CameraMode.ORTHOGRAPHIC );
	}

	public void setToPerspectiveMode() {
		setMode( CameraMode.PERSPECTIVE );
	}

	public void setMode( CameraMode mode ) {
		if( mode != this.cameraMode ) {
			this.cameraMode = mode;
			setControlsBasedOnMode( this.cameraMode );
		}
	}

	private CameraMode cameraMode = CameraMode.PERSPECTIVE;
	private boolean isExpanded = false;
	private AbstractDragAdapter dragAdapter;
	private ManipulationHandle2DCameraDriver cameraDriver;
	private ManipulationHandle2DCameraTurnUpDown cameraControlUpDown;
	private ManipulationHandle2DCameraStrafe cameraControlStrafe;
	private ManipulationHandle2DCameraStrafe orthographicCameraControlStrafe;
	private ManipulationHandle2DCameraZoom orthographicCameraControlZoom;
}
