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

import edu.cmu.cs.dennisc.math.Vector3;

/**
 * @author David Culyba
 */
public class HandlelessObjectRotateDragManipulator extends ObjectRotateDragManipulator {

	protected Vector3 rotateAxis;
	
	public HandlelessObjectRotateDragManipulator( Vector3 rotateAxis )
	{
		this.rotateAxis = rotateAxis;
	}
	
	@Override
	public boolean doStartManipulator( InputState startInput ) 
	{
		this.manipulatedTransformable = startInput.getClickPickedTransformable( true );
		if (this.manipulatedTransformable != null)
		{
			//Make sure the object we're working with is selected
			this.dragAdapter.setSelectedObject( this.manipulatedTransformable );
			return true;
		}
		else
		{
			return false;
		}
	}
	
	@Override
	public void doEndManipulator( InputState endInput, InputState previousInput )
	{
		super.doEndManipulator( endInput, previousInput );
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
