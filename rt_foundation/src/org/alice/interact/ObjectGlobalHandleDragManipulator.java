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

import edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.Transformable;

/**
 * @author David Culyba
 */
public class ObjectGlobalHandleDragManipulator extends DragManipulator implements CameraInformedManipulator {

	protected DragManipulator activeManipulator;
	protected AbstractCamera camera = null;
	protected OnscreenLookingGlass onscreenLookingGlass = null;
	protected ManipulationHandle activeHandle = null;
	
	public ObjectGlobalHandleDragManipulator()
	{
	}
	
	@Override
	public void dataUpdateManipulator( InputState currentInput, InputState previousInput ) {
		if (this.activeManipulator != null)
		{
			this.activeManipulator.dataUpdateManipulator( currentInput, previousInput );
		}

	}

	@Override
	public void endManipulator( InputState endInput, InputState previousInput ) {
		if (this.activeManipulator != null)
		{
			this.activeManipulator.endManipulator( endInput, previousInput );
		}
		if (activeHandle != null)
		{
			this.dragAdapter.setActivateHandle( this.activeHandle, false );
		}
	}

	@Override
	public void startManipulator( InputState startInput ) {
		Transformable clickedHandle = PickHint.HANDLES.getMatchingTransformable( startInput.getClickPickedTransformable(true) );
		if (clickedHandle instanceof ManipulationHandle)
		{
			this.activeHandle = ((ManipulationHandle)clickedHandle);
			this.dragAdapter.setActivateHandle( this.activeHandle, true );
		}
		if (this.activeHandle instanceof RotationRingHandle)
		{
			this.activeManipulator = new ObjectRotateDragManipulator();
		}
		else if (this.activeHandle instanceof LinearTranslateHandle)
		{
			this.activeManipulator = new LinearDragManipulator();
		}
		else if (this.activeHandle instanceof LinearScaleHandle)
		{
			this.activeManipulator = new ScaleDragManipulator();
		}
		if (this.activeManipulator != null)
		{
			if (this.activeManipulator instanceof CameraInformedManipulator)
			{
				CameraInformedManipulator cIM = (CameraInformedManipulator)this.activeManipulator;
				cIM.setCamera( this.camera );
				cIM.setOnscreenLookingGlass( this.onscreenLookingGlass );
			}
			this.activeManipulator.startManipulator( startInput );
		}

	}

	@Override
	public void timeUpdateManipulator( double time, InputState currentInput ) {
		if (this.activeManipulator != null)
		{
			this.activeManipulator.timeUpdateManipulator( time, currentInput );
		}

	}

	public void setCamera( AbstractCamera camera ) {
		this.camera = camera;

	}

	public void setOnscreenLookingGlass( OnscreenLookingGlass onscreenLookingGlass ) {
		this.onscreenLookingGlass = onscreenLookingGlass;

	}

}
