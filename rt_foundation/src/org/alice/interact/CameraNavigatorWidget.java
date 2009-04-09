/**
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package org.alice.interact;

import java.awt.FlowLayout;

import javax.swing.JPanel;

import org.alice.interact.event.ManipulationEvent;
import org.alice.interact.event.ManipulationEventCriteria;
import org.alice.interact.handle.ManipulationHandle2DCameraDriver;
import org.alice.interact.handle.ManipulationHandle2DCameraStrafe;
import org.alice.interact.handle.ManipulationHandle2DCameraTurnUpDown;
import org.alice.interact.manipulator.CameraDragDriveManipulator;
import org.alice.interact.manipulator.CameraDragStrafeManipulator;
import org.alice.interact.manipulator.CameraDragUpDownRotateManipulator;

/**
 * @author David Culyba
 */
public class CameraNavigatorWidget extends JPanel {
	
	AbstractDragAdapter dragAdapter;
	ManipulationHandle2DCameraDriver cameraDriver;
	ManipulationHandle2DCameraTurnUpDown cameraControlUpDown;
	ManipulationHandle2DCameraStrafe cameraControlStrafe;
	
	public CameraNavigatorWidget( AbstractDragAdapter dragAdapter )
	{
		super();
		this.setLayout( new FlowLayout() );
		this.setOpaque(false);
		this.dragAdapter = dragAdapter;
		this.cameraDriver = new ManipulationHandle2DCameraDriver();
		CameraDragDriveManipulator driverManipulator = new CameraDragDriveManipulator(this.cameraDriver);
		this.cameraDriver.setManipulation( driverManipulator );
		for (ManipulationEvent event : driverManipulator.getManipulationEvents())
		{
			this.cameraDriver.addCondition( new ManipulationEventCriteria(
					event.getType(),
					event.getMovementDescription(),
					PickHint.CAMERA ) );
		}
		this.dragAdapter.addListeners( this.cameraDriver );
		this.dragAdapter.addManipulationListener( this.cameraDriver );
		this.dragAdapter.addHandle( this.cameraDriver );
		
		this.cameraControlUpDown = new ManipulationHandle2DCameraTurnUpDown();
		CameraDragUpDownRotateManipulator upDownManipulator = new CameraDragUpDownRotateManipulator(this.cameraControlUpDown);
		this.cameraControlUpDown.setManipulation( upDownManipulator );
		for (ManipulationEvent event : upDownManipulator.getManipulationEvents())
		{
			this.cameraControlUpDown.addCondition( new ManipulationEventCriteria(
					event.getType(),
					event.getMovementDescription(),
					PickHint.CAMERA ) );
		}
		this.dragAdapter.addListeners( this.cameraControlUpDown );
		this.dragAdapter.addManipulationListener( this.cameraControlUpDown );
		this.dragAdapter.addHandle( this.cameraControlUpDown );
		
		this.cameraControlStrafe = new ManipulationHandle2DCameraStrafe();
		CameraDragStrafeManipulator strafeManipulator = new CameraDragStrafeManipulator(this.cameraControlStrafe);
		this.cameraControlStrafe.setManipulation( strafeManipulator );
		for (ManipulationEvent event : strafeManipulator.getManipulationEvents())
		{
			this.cameraControlStrafe.addCondition( new ManipulationEventCriteria(
					event.getType(),
					event.getMovementDescription(),
					PickHint.CAMERA ) );
		}
		this.dragAdapter.addListeners( this.cameraControlStrafe );
		this.dragAdapter.addManipulationListener( this.cameraControlStrafe );
		this.dragAdapter.addHandle( this.cameraControlStrafe );
		
		this.add(this.cameraControlStrafe);
		this.add(this.cameraDriver);
		this.add(this.cameraControlUpDown);
		
	}

}
