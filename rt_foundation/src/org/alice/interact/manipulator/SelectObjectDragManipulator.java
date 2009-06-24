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

import org.alice.interact.GlobalDragAdapter;
import org.alice.interact.InputState;
import org.alice.interact.PickHint;
import org.alice.interact.condition.PickCondition;
import org.alice.interact.handle.HandleSet;
import org.alice.interact.handle.RotationRingHandle;

import edu.cmu.cs.dennisc.scenegraph.Transformable;

/**
 * @author David Culyba
 */
public class SelectObjectDragManipulator extends AbstractManipulator {

	protected GlobalDragAdapter globalDragAdapter;
	
	public SelectObjectDragManipulator( GlobalDragAdapter globalDragAdapter )
	{
		this.globalDragAdapter = globalDragAdapter;
	}
	
	@Override
	public void doDataUpdateManipulator( InputState currentInput, InputState previousInput ) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doEndManipulator( InputState endInput, InputState previousInput ) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean doStartManipulator( InputState startInput ) {
		PickHint clickedObjectType = PickCondition.getPickType( startInput.getClickPickResult() );
		if ( clickedObjectType.intersects( PickHint.MOVEABLE_OBJECTS) )
		{
			this.globalDragAdapter.setSelectedObject( startInput.getClickPickedTransformable(true) );
		}
		else if (clickedObjectType.intersects( PickHint.HANDLES) )
		{
			Transformable pickedHandle = startInput.getClickPickedTransformable(true);
			if (pickedHandle instanceof RotationRingHandle)
			{
				this.globalDragAdapter.setSelectedObject( ((RotationRingHandle)pickedHandle).getManipulatedObject() );
			}
		}
		else
		{
			this.globalDragAdapter.setSelectedObject( null );
		}
		return true;

	}

	@Override
	public void doTimeUpdateManipulator( double time, InputState currentInput ) {
		// TODO Auto-generated method stub

	}

	@Override
	protected HandleSet getHandleSetToEnable() {
		return null;
	}

}
