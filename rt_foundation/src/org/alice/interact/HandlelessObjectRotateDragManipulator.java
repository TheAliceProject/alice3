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

import edu.cmu.cs.dennisc.math.Vector3;

/**
 * @author David Culyba
 */
public class HandlelessObjectRotateDragManipulator extends ObjectRotateDragManipulator {

	protected Vector3 rotateAxis;
	protected RotationRingHandle rotationHandle;
	
	public HandlelessObjectRotateDragManipulator( Vector3 rotateAxis )
	{
		this.rotateAxis = rotateAxis;
	}
	
	@Override
	public void startManipulator( InputState startInput ) 
	{
		this.manipulatedTransformable = startInput.getClickPickedTransformable( true );
		//Make sure the object we're working with is selected
		this.dragAdapter.setSelectedObject( this.manipulatedTransformable );
		this.rotationHandle = this.dragAdapter.getCurrentRotationRingForAxis( this.rotateAxis, this.manipulatedTransformable );
		if (this.rotationHandle != null)
		{
			this.initManipulator( this.rotationHandle, startInput );
			this.dragAdapter.setActivateHandle( this.rotationHandle, true );
		}
	}
	
	@Override
	public void endManipulator( InputState endInput, InputState previousInput )
	{
		super.endManipulator( endInput, previousInput );
		if (this.rotationHandle != null)
		{
			this.dragAdapter.setActivateHandle( this.rotationHandle, false );
		}
	}
	
	@Override
	protected void hideCursor()
	{
		//We don't hide the cursor on this guy
	}
	
	@Override
	protected void showCursor()
	{
		//No hiding means now showing too
	}
	
}
