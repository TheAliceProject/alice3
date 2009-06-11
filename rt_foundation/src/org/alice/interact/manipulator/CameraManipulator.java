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
package org.alice.interact.manipulator;


import org.alice.interact.InputState;
import org.alice.interact.handle.HandleSet;

import edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.Transformable;

/**
 * @author David Culyba
 */
public abstract class CameraManipulator extends AbstractManipulator implements CameraInformedManipulator {

	protected OnscreenLookingGlass onscreenLookingGlass = null;
	
	public AbstractCamera getCamera()
	{
		if( this.onscreenLookingGlass != null )
		{
			return onscreenLookingGlass.getCameraAt( 0 );
		} 
		return null;
	}
	
	public Transformable getManipulatedTransformable()
	{
		AbstractCamera camera = this.getCamera();
		if (camera.getParent() instanceof Transformable)
		{
			return (Transformable)camera.getParent();
		}
		return null;
	}

	@Override
	public boolean doStartManipulator( InputState startInput ) {
		this.manipulatedTransformable = this.getManipulatedTransformable();
		if (this.manipulatedTransformable != null)
		{
			return true;
		}
		return false;
	}
	
	public void setOnscreenLookingGlass( OnscreenLookingGlass onscreenLookingGlass ) {
		this.onscreenLookingGlass = onscreenLookingGlass;
		if (this.onscreenLookingGlass != null && this.getCamera() != null)
		{
			AbstractCamera camera = this.getCamera();
			if (camera.getParent() instanceof Transformable)
			{
				this.manipulatedTransformable = (Transformable)camera.getParent();
			}
		}
	}
	
	@Override
	//We don't want to change the handle set when moving the camera
	protected HandleSet getHandleSetToEnable()
	{
		return null;
	}
	
	
	
}
