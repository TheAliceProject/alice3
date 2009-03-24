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

import org.alice.interact.AbstractDragAdapter.HandleSet;
import org.alice.interact.event.ManipulationEvent;
import org.alice.interact.event.ManipulationListener;

import edu.cmu.cs.dennisc.animation.Animator;
import edu.cmu.cs.dennisc.scenegraph.Transformable;

/**
 * @author David Culyba
 */
public class ManipulationHandleIndirection  implements ManipulationListener{
	private ManipulationHandle currentHandle;
	private ManipulationHandle nextHandle;
	
	private HandleSet currentHandleSet;
	
	public ManipulationHandleIndirection( ManipulationHandle handle )
	{
		this.currentHandle = handle;
		this.nextHandle = this.currentHandle.clone();
	}
	
	public void setAnimator( Animator animator )
	{
		this.currentHandle.setAnimator( animator );
		this.nextHandle.setAnimator( animator );
	}
	
	public void addToGroups( HandleGroup...groups)
	{
		for (int i=0; i<groups.length; i++)
		{
			this.currentHandle.addToGroup( groups[i]);
			this.nextHandle.addToGroup( groups[i]);
		}
	}
	
	public void setCurrentHandleSet( HandleSet handleSet )
	{
		this.currentHandleSet = handleSet;
		if (this.currentHandle.isMemberOf( this.currentHandleSet.getBitSet() ))
		{
			if (!this.currentHandle.isVisible())
			{
				this.currentHandle.setVisible( true );
			}
		}
		else
		{
			if (this.currentHandle.isVisible())
			{
				this.currentHandle.setVisible( false );
			}
		}
	}
	
	public void setManipulatedObject(Transformable manipulatedObject)
	{
		if (this.currentHandle.getManipulatedObject() != manipulatedObject)
		{
			if (this.currentHandle.isVisible())
			{
				this.currentHandle.setVisible( false );
			}
			this.nextHandle.setManipulatedObject( manipulatedObject );	
			if (this.nextHandle.isMemberOf( this.currentHandleSet.getBitSet() ))
			{
				this.nextHandle.setVisible( true );
			}
			
			ManipulationHandle tempHandle = this.currentHandle;
			this.currentHandle = this.nextHandle;
			this.nextHandle = tempHandle;
		}
	}

	public ManipulationHandle getCurrentHandle()
	{
		return this.currentHandle;
	}

	public ManipulationHandle getNextHandle()
	{
		return this.nextHandle;
	}

	public void activate( ManipulationEvent event )
	{
		this.currentHandle.activate( event );
	}

	public void deactivate( ManipulationEvent event )
	{
		this.currentHandle.deactivate( event );
	}
	
}
