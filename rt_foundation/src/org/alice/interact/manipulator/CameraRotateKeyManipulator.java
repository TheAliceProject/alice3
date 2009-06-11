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

import org.alice.interact.MovementKey;

import edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;

/**
 * @author David Culyba
 */
public class CameraRotateKeyManipulator extends RotateKeyManipulator implements CameraInformedManipulator {

	protected OnscreenLookingGlass onscreenLookingGlass = null;
	
	public CameraRotateKeyManipulator( MovementKey[] directionKeys )
	{
		super(directionKeys);
	}
	
	public AbstractCamera getCamera()
	{
		if( this.onscreenLookingGlass != null )
		{
			return onscreenLookingGlass.getCameraAt( 0 );
		} 
		return null;
	}

	public void setOnscreenLookingGlass( OnscreenLookingGlass onscreenLookingGlass ) {
		this.onscreenLookingGlass = onscreenLookingGlass;
		if (this.getCamera() != null)
		{
			this.manipulatedTransformable = (edu.cmu.cs.dennisc.scenegraph.Transformable)getCamera().getParent();
		}
	}

}
