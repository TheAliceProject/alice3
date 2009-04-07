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

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.alice.interact.event.ManipulationEvent;
import org.alice.interact.event.ManipulationEventCriteria;
import org.alice.interact.event.ManipulationListener;

import edu.cmu.cs.dennisc.animation.Animator;
import edu.cmu.cs.dennisc.scenegraph.Transformable;

/**
 * @author David Culyba
 */
public class HandleManager implements ManipulationListener{
	private Stack< HandleSet > handleSetStack = new Stack< HandleSet >();
	private List< ManipulationHandle > handles = new ArrayList<ManipulationHandle>();
	
	
	public void addHandle( ManipulationHandle handle )
	{
		handles.add( handle );
		handle.setHandleManager( this );
	}
	
	public HandleSet getCurrentHandleSet()
	{
		if (this.handleSetStack.empty())
		{
			return null;
		}
		else
		{
			return this.handleSetStack.peek();
		}
	}
	
	private void updateHandlesBasedOnHandleSet()
	{
		for (ManipulationHandle handle : this.handles)
		{
			if (!handle.isAlwaysVisible())
			{
				if (handle.isMemberOf( this.getCurrentHandleSet() ))
				{
					handle.setVisible( true );
				}
				else
				{
					handle.setVisible( false );
				}
			}
		}
	}
	
	public void setHandleSet( HandleSet handleSet )
	{
		if (!this.handleSetStack.empty())
		{
			this.handleSetStack.pop();
		}
		this.handleSetStack.push( handleSet );
		this.updateHandlesBasedOnHandleSet();
	}
	
	public void pushNewHandleSet( HandleSet handleSet )
	{
		this.handleSetStack.push( handleSet );
		this.updateHandlesBasedOnHandleSet();
	}
	
	public HandleSet popHandleSet()
	{
		if (this.handleSetStack.empty())
		{
			System.err.println("TRYING TO POP AN EMPTY HANDLE STACK!!!");
			Thread.dumpStack();
			return null;
		}
		HandleSet popped = this.handleSetStack.pop();	
		this.updateHandlesBasedOnHandleSet();
		return popped;
	}
	
	public void setSelectedObject(Transformable selectedObject)
	{
		for (ManipulationHandle handle : this.handles)
		{
			handle.setSelectedObject( selectedObject );
		}
	}
	
	public void setAnimator(Animator animator)
	{
		for (ManipulationHandle handle : this.handles)
		{
			handle.setAnimator( animator );
		}
	}

	public boolean isHandleVisible(ManipulationHandle handle)
	{
		if (handle.isAlwaysVisible())
		{
			return true;
		}
		else return handle.isMemberOf( this.getCurrentHandleSet() );
	}
	
	private List<ManipulationHandle> getSiblings(ManipulationHandle handle)
	{
		ArrayList<ManipulationHandle> siblings = new ArrayList< ManipulationHandle >();
		HandleSet toCheck;
		if (handle.isAlwaysVisible())
		{
			toCheck = handle.getHandleSet();
		}
		else
		{
			if (handle.isMemberOf( this.getCurrentHandleSet() ))
			{
				toCheck = this.getCurrentHandleSet();
			}
			else
			{
				toCheck = new HandleSet();
			}
		}
		for (ManipulationHandle otherHandle : this.handles)
		{
			if (otherHandle != handle)
			{
				if (handle.isAlwaysVisible())
				{
					//Handles that are always visible are siblings if their memberships are equal
					if (otherHandle.getHandleSet().equals( toCheck ))
					{
						siblings.add( otherHandle );
					}
				}
				else if (otherHandle.isMemberOf( toCheck ))
				{
					siblings.add( otherHandle );
				}
			}
		}
		return siblings;
	}
	
	public boolean isASiblingActive(ManipulationHandle handle)
	{
		List<ManipulationHandle> siblings = this.getSiblings( handle );
		for (ManipulationHandle sibling : siblings)
		{
			if (sibling.getHandleStateCopy().isActive())
			{
				return true;
			}
		}
		return false;
	}
	
	public void setHandleRollover(ManipulationHandle handle, boolean isRollover)
	{
		handle.setRollover( isRollover );
	}
	
	public void activate( ManipulationEvent event ) {
		
	}

	public void deactivate( ManipulationEvent event ) {
		// TODO Auto-generated method stub
		
	}

	public void addCondition( ManipulationEventCriteria condition ) {
		// TODO Auto-generated method stub
		
	}

	public boolean matches( ManipulationEvent event ) {
		// TODO Auto-generated method stub
		return false;
	}

	public void removeCondition( ManipulationEventCriteria condition ) {
		// TODO Auto-generated method stub
		
	}

	
}
