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
package org.alice.interact;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JPanel;

import org.alice.interact.AbstractDragAdapter.CameraView;
import org.alice.interact.ModifierMask.ModifierKey;
import org.alice.interact.condition.ManipulatorConditionSet;
import org.alice.interact.condition.MouseDragCondition;
import org.alice.interact.condition.PickCondition;
import org.alice.interact.event.ManipulationEvent;
import org.alice.interact.event.ManipulationEventCriteria;
import org.alice.interact.handle.ManipulationHandle2CameraZoom;
import org.alice.interact.handle.ManipulationHandle2DCameraDriver;
import org.alice.interact.handle.ManipulationHandle2DCameraStrafe;
import org.alice.interact.handle.ManipulationHandle2DCameraTurnUpDown;
import org.alice.interact.manipulator.CameraDragDriveManipulator;
import org.alice.interact.manipulator.CameraDragStrafeManipulator;
import org.alice.interact.manipulator.CameraDragUpDownRotateManipulator;
import org.alice.interact.manipulator.CameraInformedManipulator;
import org.alice.interact.manipulator.ObjectGlobalHandleDragManipulator;
import org.alice.interact.manipulator.OrthographicCameraDragStrafeManipulator;
import org.alice.interact.manipulator.OrthographicCameraDragZoomManipulator;

import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.OrthographicCamera;
import edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera;

/**
 * @author David Culyba
 */
public class CameraNavigatorWidget extends JPanel {
	
	public enum CameraMode
	{
		ORTHOGRAPHIC,
		PERSPECTIVE,
	}
	
	protected CameraMode cameraMode = CameraMode.PERSPECTIVE; 
	protected boolean isExpanded = false;
	protected AbstractDragAdapter dragAdapter;
	protected ManipulationHandle2DCameraDriver cameraDriver;
	protected ManipulationHandle2DCameraTurnUpDown cameraControlUpDown;
	protected ManipulationHandle2DCameraStrafe cameraControlStrafe;
	protected ManipulationHandle2DCameraStrafe orthographicCameraControlStrafe;
	protected ManipulationHandle2CameraZoom orthographicCameraControlZoom;
	
	public CameraNavigatorWidget( AbstractDragAdapter dragAdapter, CameraView attachedView)
	{
		super();
		
		//this.setLayout( new FlowLayout() );
		this.setLayout( new javax.swing.BoxLayout( this, javax.swing.BoxLayout.LINE_AXIS ) );
		this.setOpaque(false);
		this.dragAdapter = dragAdapter;
		
		this.cameraControlUpDown = new ManipulationHandle2DCameraTurnUpDown();
		CameraDragUpDownRotateManipulator upDownManipulator = new CameraDragUpDownRotateManipulator(this.cameraControlUpDown);
		upDownManipulator.setDesiredCameraView( attachedView );
		this.cameraControlUpDown.setManipulation( upDownManipulator );
		for (ManipulationEvent event : upDownManipulator.getManipulationEvents())
		{
			this.cameraControlUpDown.addCondition( new ManipulationEventCriteria(
					event.getType(),
					event.getMovementDescription(),
					PickHint.CAMERA ) );
		}
		this.cameraControlUpDown.setDragAdapter( this.dragAdapter );
		this.dragAdapter.addManipulationListener( this.cameraControlUpDown );
		
		this.cameraControlStrafe = new ManipulationHandle2DCameraStrafe();
		CameraDragStrafeManipulator strafeManipulator = new CameraDragStrafeManipulator(this.cameraControlStrafe);
		strafeManipulator.setDesiredCameraView( attachedView );
		this.cameraControlStrafe.setManipulation( strafeManipulator );
		for (ManipulationEvent event : strafeManipulator.getManipulationEvents())
		{
			this.cameraControlStrafe.addCondition( new ManipulationEventCriteria(
					event.getType(),
					event.getMovementDescription(),
					PickHint.CAMERA ) );
		}
		this.dragAdapter.addManipulationListener( this.cameraControlStrafe );
		this.cameraControlStrafe.setDragAdapter( this.dragAdapter );
		
		this.orthographicCameraControlStrafe = new ManipulationHandle2DCameraStrafe();
		OrthographicCameraDragStrafeManipulator orthoStrafeManipulator = new OrthographicCameraDragStrafeManipulator(this.cameraControlStrafe);
		orthoStrafeManipulator.setDesiredCameraView( attachedView );
		this.orthographicCameraControlStrafe.setManipulation( orthoStrafeManipulator );
		for (ManipulationEvent event : orthoStrafeManipulator.getManipulationEvents())
		{
			this.orthographicCameraControlStrafe.addCondition( new ManipulationEventCriteria(
					event.getType(),
					event.getMovementDescription(),
					PickHint.CAMERA ) );
		}
		this.dragAdapter.addManipulationListener( this.orthographicCameraControlStrafe );
		this.orthographicCameraControlStrafe.setDragAdapter( this.dragAdapter );
		
		
		this.orthographicCameraControlZoom = new ManipulationHandle2CameraZoom();
		OrthographicCameraDragZoomManipulator orthoZoomManipulator = new OrthographicCameraDragZoomManipulator(this.orthographicCameraControlZoom);
		orthoZoomManipulator.setDesiredCameraView( attachedView );
		this.orthographicCameraControlZoom.setManipulation( orthoZoomManipulator );
		for (ManipulationEvent event : orthoZoomManipulator.getManipulationEvents())
		{
			this.orthographicCameraControlZoom.addCondition( new ManipulationEventCriteria(
					event.getType(),
					event.getMovementDescription(),
					PickHint.CAMERA ) );
		}
		this.dragAdapter.addManipulationListener( this.orthographicCameraControlZoom );
		this.orthographicCameraControlZoom.setDragAdapter( this.dragAdapter );
		
		this.cameraDriver = new ManipulationHandle2DCameraDriver();
		CameraDragDriveManipulator driverManipulator = new CameraDragDriveManipulator(this.cameraDriver);
		driverManipulator.setDesiredCameraView( attachedView );
		this.cameraDriver.setManipulation( driverManipulator );
		for (ManipulationEvent event : driverManipulator.getManipulationEvents())
		{
			this.cameraDriver.addCondition( new ManipulationEventCriteria(
					event.getType(),
					event.getMovementDescription(),
					PickHint.CAMERA ) );
		}
		this.dragAdapter.addManipulationListener( this.cameraDriver );
		
		driverManipulator.setDragAdapter( this.dragAdapter );
		ManipulatorConditionSet mouseHandleDrag_NoShift = new ManipulatorConditionSet( driverManipulator );
		MouseDragCondition handleNoShiftCondition = new MouseDragCondition( java.awt.event.MouseEvent.BUTTON1, new PickCondition( PickHint.MULTIMODE_HANDLES ), new ModifierMask( ModifierKey.NOT_SHIFT ));
		mouseHandleDrag_NoShift.addCondition( handleNoShiftCondition );
		this.dragAdapter.addManipulator( mouseHandleDrag_NoShift );
		
		strafeManipulator.setDragAdapter( this.dragAdapter );
		ManipulatorConditionSet mouseHandleDrag_Shift = new ManipulatorConditionSet( strafeManipulator );
		MouseDragCondition handleShiftCondition = new MouseDragCondition( java.awt.event.MouseEvent.BUTTON1, new PickCondition( PickHint.MULTIMODE_HANDLES ), new ModifierMask( ModifierKey.SHIFT ));
		mouseHandleDrag_Shift.addCondition( handleShiftCondition );
		this.dragAdapter.addManipulator( mouseHandleDrag_Shift );
		
		this.cameraDriver.setDragAdapter( this.dragAdapter );
		
		this.cameraMode = null;
		setMode(CameraMode.PERSPECTIVE); //This will set the mode and also put the controls in the panel
		
		
	}
	
	public void setExpanded( boolean isExpanded ) {
		this.isExpanded = isExpanded;
		switch (this.cameraMode)
		{
		case PERSPECTIVE:
			{
				this.cameraControlUpDown.setVisible( isExpanded );
				this.cameraControlStrafe.setVisible( isExpanded );
				this.cameraDriver.setVisible( true );
				this.orthographicCameraControlStrafe.setVisible( false );
				this.orthographicCameraControlZoom.setVisible( false );
			}
			break;
		case ORTHOGRAPHIC:
			{
				this.cameraControlUpDown.setVisible( false );
				this.cameraControlStrafe.setVisible( false );
				this.cameraDriver.setVisible( false );
				this.orthographicCameraControlStrafe.setVisible( true );
				this.orthographicCameraControlZoom.setVisible( true );
			}
			break;
		}
		
	}
	
	
	protected void setControlsBasedOnMode(CameraMode mode)
	{
		this.removeAll();
		this.setExpanded( this.isExpanded );
		switch (mode)
		{
		case PERSPECTIVE:
			{
				this.add(this.cameraControlStrafe);
				this.add(this.cameraDriver);
				this.add(this.cameraControlUpDown);
			}
			break;
		case ORTHOGRAPHIC:
			{
				this.add(this.orthographicCameraControlStrafe);
				this.add(this.orthographicCameraControlZoom);
			}
			break;
		}
	}
	
	public void setToOrthographicMode()
	{
		setMode(CameraMode.ORTHOGRAPHIC);
	}
	
	public void setToPerspectiveMode()
	{
		setMode(CameraMode.PERSPECTIVE);
	}
	
	public void setMode(CameraMode mode)
	{
		if (mode != this.cameraMode)
		{
			this.cameraMode = mode;
			setControlsBasedOnMode( this.cameraMode );
		}
	}

}
