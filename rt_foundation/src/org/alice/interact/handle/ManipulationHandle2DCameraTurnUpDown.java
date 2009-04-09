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
package org.alice.interact.handle;


import javax.swing.ImageIcon;

import org.alice.interact.event.ManipulationEvent;

/**
 * @author David Culyba
 */
public class ManipulationHandle2DCameraTurnUpDown extends ImageBasedManipulationHandle2D {
	private enum ControlState implements ImageBasedManipulationHandle2D.ImageState
	{
		Inactive("images/tilt.gif"),
		Highlighted("images/tiltHighlight.gif"),
		TurningForward("images/tiltDown.gif"),
		TurningBackward("images/tiltUp.gif");
		
		private ImageIcon icon;
		private ControlState(String resourceString)
		{
			this.icon = new ImageIcon( this.getClass().getResource( resourceString ));
		}
		
		public ImageIcon getIcon()
		{
			return this.icon;
		}
	}
	
	private boolean turningForward = false;
	private boolean turningBackward = false;

	
	@Override
	protected ControlState getStateForManipulationStatus()
	{
		if (this.turningForward)
		{
			return ControlState.TurningForward;
		}
		else if (this.turningBackward)
		{
			return ControlState.TurningBackward;
		}
		//If we're not moving in one of the directions, choose highlighted or inactive
		else if (this.state.isRollover())
		{
			return ControlState.Highlighted;
		}
		else
		{
			return ControlState.Inactive;
		}
	}
	
	
	@Override
	protected void setManipulationState(ManipulationEvent event, boolean isActive)
	{
		switch (event.getMovementDescription().direction)
		{
			case LEFT : this.turningForward = isActive; break;
			case RIGHT : this.turningBackward = isActive; break;
		}
	}
	
	
}
