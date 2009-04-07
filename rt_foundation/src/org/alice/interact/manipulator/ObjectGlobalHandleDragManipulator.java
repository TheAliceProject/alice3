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
import org.alice.interact.PickHint;
import org.alice.interact.handle.HandleSet;
import org.alice.interact.handle.ManipulationHandle3D;

import edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.Transformable;

/**
 * @author David Culyba
 */
public class ObjectGlobalHandleDragManipulator extends AbstractManipulator implements CameraInformedManipulator {

	protected AbstractManipulator activeManipulator;
	protected OnscreenLookingGlass onscreenLookingGlass = null;
	protected ManipulationHandle3D activeHandle = null;
	
	public ObjectGlobalHandleDragManipulator()
	{
	}
	
	@Override
	public void doDataUpdateManipulator( InputState currentInput, InputState previousInput ) {
		if (this.activeManipulator != null)
		{
			this.activeManipulator.doDataUpdateManipulator( currentInput, previousInput );
		}

	}
	
	@Override
	public void triggerAllDeactivateEvents()
	{
		if (this.activeManipulator != null)
		{
			this.activeManipulator.triggerAllDeactivateEvents();
		}
	}

	@Override
	public void doEndManipulator( InputState endInput, InputState previousInput ) {
		if (this.activeManipulator != null)
		{
			this.activeManipulator.doEndManipulator( endInput, previousInput );
		}
//		if (activeHandle != null)
//		{
//			this.dragAdapter.setActivateHandle( this.activeHandle, false );
//		}
	}

	@Override
	public boolean doStartManipulator( InputState startInput ) {
		Transformable clickedHandle = PickHint.HANDLES.getMatchingTransformable( startInput.getClickPickedTransformable(true) );
		if (clickedHandle instanceof ManipulationHandle3D)
		{
			this.activeHandle = ((ManipulationHandle3D)clickedHandle);
		//	this.dragAdapter.setActivateHandle( this.activeHandle, true );
		}
		this.activeManipulator = this.activeHandle.getManipulation( startInput );
		if (this.activeManipulator != null)
		{
			this.activeManipulator.setDragAdapter( this.dragAdapter );
			if (this.activeManipulator instanceof CameraInformedManipulator)
			{
				CameraInformedManipulator cIM = (CameraInformedManipulator)this.activeManipulator;
				cIM.setOnscreenLookingGlass( this.onscreenLookingGlass );
			}
			return this.activeManipulator.doStartManipulator( startInput );
		}
		else
		{
			return false;
		}

	}

	@Override
	public void doTimeUpdateManipulator( double time, InputState currentInput ) {
		if (this.activeManipulator != null)
		{
			this.activeManipulator.doTimeUpdateManipulator( time, currentInput );
		}

	}
	
	@Override
	protected HandleSet getHandleSetToEnable() {
		if (this.activeManipulator != null)
		{
			return this.activeManipulator.getHandleSetToEnable();
		}
		else
		{
			return null;
		}
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

	}

}
