package org.alice.interact.handle;

import javax.swing.ImageIcon;

import org.alice.interact.event.ManipulationEvent;

import edu.cmu.cs.dennisc.image.ImageUtilities;

public class ManipulationHandle2CameraZoom extends ImageBasedManipulationHandle2D 
{
	private enum ControlState implements ImageBasedManipulationHandle2D.ImageState
	{
		Inactive("images/zoom.png"),
		Highlighted("images/zoomHighlight.png"),
		ZoomingIn("images/zoomIn.png"),
		ZoomingOut("images/zoomOut.png");
		
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
	
	private boolean zoomingIn = false;
	private boolean zoomingOut = false;
	
	@Override
	protected ImageState getStateForManipulationStatus() {	
		if (this.zoomingIn)
		{
			return ControlState.ZoomingIn;
		}
		else if (this.zoomingOut)
		{
			return ControlState.ZoomingOut;
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
	protected void setImageMask() {
		this.imageMask = ImageUtilities.read( this.getClass().getResource( "images/zoomMask.png" ) );
	}

	@Override
	protected void setManipulationState(ManipulationEvent event,
			boolean isActive) 
	{
		switch (event.getMovementDescription().direction)
		{
			case FORWARD : this.zoomingIn = isActive; break;
			case BACKWARD : this.zoomingOut = isActive; break;
		}
	}

}
