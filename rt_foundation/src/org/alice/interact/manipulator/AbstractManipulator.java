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

import java.util.Vector;

import org.alice.interact.GlobalDragAdapter;
import org.alice.interact.InputState;
import org.alice.interact.event.ManipulationEvent;
import org.alice.interact.handle.HandleSet;

/**
 * @author David Culyba
 */
public abstract class AbstractManipulator {
	
	protected edu.cmu.cs.dennisc.scenegraph.Transformable manipulatedTransformable = null;
	private boolean hasStarted = false;
	protected GlobalDragAdapter dragAdapter;
	
	protected Vector< ManipulationEvent > manipulationEvents = new Vector< ManipulationEvent >();
	
	public void setManipulatedTransformable( edu.cmu.cs.dennisc.scenegraph.Transformable manipulatedTransformable)
	{
		this.manipulatedTransformable = manipulatedTransformable;
	}
	
	public boolean hasStarted()
	{
		return this.hasStarted;
	}
	
	public void setDragAdapter( GlobalDragAdapter dragAdapter )
	{
		this.dragAdapter = dragAdapter;
	}
	
	protected abstract HandleSet getHandleSetToEnable();
	
	protected void initializeEventMessages()
	{
		this.manipulationEvents.clear();
	}
	
	public void triggerAllDeactivateEvents()
	{
		for (int i=0; i<this.manipulationEvents.size(); i++)
		{
			this.dragAdapter.triggerManipulationEvent( this.manipulationEvents.get( i ), false );
		}
	}
	
	public void startManipulator( InputState startInput )
	{
		this.hasStarted = doStartManipulator( startInput );
		if (this.hasStarted)
		{
			HandleSet setToShow = this.getHandleSetToEnable();
			if (setToShow != null && this.dragAdapter != null)
			{
				this.dragAdapter.pushHandleSet( setToShow );
			}
		}
	}
	
	public void dataUpdateManipulator(InputState currentInput, InputState previousInput  )
	{
		if (this.hasStarted)
		{
			doDataUpdateManipulator( currentInput, previousInput );
		}
	}
	
	public void timeUpdateManipulator( double dTime, InputState currentInput )
	{
		if (this.hasStarted)
		{
			doTimeUpdateManipulator( dTime, currentInput );
		}
	}
	
	public void endManipulator(InputState endInput, InputState previousInput  )
	{
		triggerAllDeactivateEvents();
		doEndManipulator( endInput, previousInput );
		if (this.hasStarted)
		{
			this.hasStarted = false;
			HandleSet setToShow = this.getHandleSetToEnable();
			if (setToShow != null && this.dragAdapter != null)
			{
				this.dragAdapter.popHandleSet();
			}
		}
		
	}
	
	public abstract boolean doStartManipulator( InputState startInput );
	
	public abstract void doDataUpdateManipulator( InputState currentInput, InputState previousInput );
	
	public abstract void doTimeUpdateManipulator( double dTime, InputState currentInput );
	
	public abstract void doEndManipulator( InputState endInput, InputState previousInput  );
	

}
