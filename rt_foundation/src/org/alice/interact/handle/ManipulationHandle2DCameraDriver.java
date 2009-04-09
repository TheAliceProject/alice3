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

import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import org.alice.interact.event.ManipulationEvent;

import edu.cmu.cs.dennisc.image.ImageUtilities;


/**
 * @author David Culyba
 */
public class ManipulationHandle2DCameraDriver extends ImageBasedManipulationHandle2D {
	
	private enum ControlState implements ImageBasedManipulationHandle2D.ImageState
	{
		Inactive( "images/drive.png" ),
		Highlighted( "images/driveHighlight.png" ),
		Back( "images/driveBack.png" ),
		BackLeft( "images/driveBackLeft.png" ),
		BackRight( "images/driveBackRight.png" ),
		Forward( "images/driveForward.png" ),
		ForwardLeft( "images/driveForwardLeft.png" ),
		ForwardRight( "images/driveForwardRight.png" ),
		Left( "images/driveLeft.png" ),
		Right( "images/driveRight.png" );
		
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
	
	private boolean turningLeft = false;
	private boolean turningRight = false;
	private boolean movingForward = false;
	private boolean movingBackward = false;
	
	@Override
	protected void setImageMask() {
		this.imageMask = ImageUtilities.read( this.getClass().getResource( "images/driveMask.png" ) );
	}
	
	@Override
	protected ImageState getStateForManipulationStatus()
	{
		if (this.movingBackward && !this.turningLeft && !this.turningRight)
		{
			return ControlState.Back;
		}
		else if (this.movingBackward && this.turningLeft)
		{
			return ControlState.BackLeft;
		}
		else if (this.movingBackward && this.turningRight)
		{
			return ControlState.BackRight;
		}
		else if (this.movingForward && !this.turningLeft && !this.turningRight)
		{
			return ControlState.Forward;
		}
		else if (this.movingForward && this.turningLeft)
		{
			return ControlState.ForwardLeft;
		}
		else if (this.movingForward && this.turningRight)
		{
			return ControlState.ForwardRight;
		}
		else if (this.turningLeft && !this.movingForward && !this.movingBackward)
		{
			return ControlState.Left;
		}
		else if (this.turningRight && !this.movingForward && !this.movingBackward)
		{
			return ControlState.Right;
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
		case BACKWARD : this.movingBackward = isActive; break;
		case FORWARD : this.movingForward = isActive; break;
		case UP : this.turningLeft = isActive; break;
		case DOWN : this.turningRight = isActive; break;
		}
	}
	
}
