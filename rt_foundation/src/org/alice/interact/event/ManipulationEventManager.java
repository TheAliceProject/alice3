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
package org.alice.interact.event;

import java.util.List;

import org.alice.interact.ManipulatorConditionSet;
import org.alice.interact.MovementDescription;
import org.alice.interact.PickCondition;
import org.alice.interact.PickHint;

/**
 * @author David Culyba
 */
public class ManipulationEventManager {

	private class DescriptionAndTargetCriteria
	{
		public MovementDescription description;
		public PickHint targetCriteria;
		
		public DescriptionAndTargetCriteria( MovementDescription description, PickHint targetCriteria)
		{
			this.description = description;
			this.targetCriteria = targetCriteria;
		}
		
		public boolean matches( ManipulationEvent event )
		{
			boolean isValidTarget = this.targetCriteria.intersects( PickCondition.getPickType( event.getTarget() ) );
			boolean isValidMovement = event.getMovementDescription().equals(this.description);
			return isValidTarget && isValidMovement;
		}
		
		@Override
		public String toString()
		{
			return this.description.toString() + ", "+ this.targetCriteria; 
		}
	}
	
	private class ListenerAndConditions
	{
		public ManipulationListener listener;
		public List< DescriptionAndTargetCriteria > conditions = new java.util.LinkedList  < DescriptionAndTargetCriteria >();
		
		public ListenerAndConditions( ManipulationListener listener)
		{
			this.listener = listener;
		}
		
		public ListenerAndConditions( ManipulationListener listener, DescriptionAndTargetCriteria condition)
		{
			this.listener = listener;
			this.conditions.add( condition );
		}
		
		@Override
		public boolean equals(Object o)
		{
			if (o instanceof ListenerAndConditions)
			{
				return this.equals( ((ListenerAndConditions)o).listener );
			}
			else if (o instanceof ManipulationListener)
			{
				return this.listener == ((ManipulationListener)o);
			}
			return false;
		}
		
		public boolean matches( ManipulationEvent event )
		{
			for (int i=0; i<this.conditions.size(); i++)
			{
				if (this.conditions.get( i ).matches( event ))
				{
					return true;
				}
			}
			return false;
		}
		
	}
	
	private List< ListenerAndConditions > manipulationListeners = new java.util.LinkedList< ListenerAndConditions >();

	public void addManipulationListener( ManipulationListener listener, MovementDescription movementDescription, PickHint targetCriteria)
	{
		synchronized( this.manipulationListeners ) {
			DescriptionAndTargetCriteria condition = new DescriptionAndTargetCriteria(movementDescription, targetCriteria);
			int listenerIndex = this.manipulationListeners.indexOf( listener );
			if ( listenerIndex > -1 )
			{
				ListenerAndConditions lAndC = this.manipulationListeners.get( listenerIndex );
				lAndC.conditions.add( condition );
			}
			else
			{
				this.manipulationListeners.add( new ListenerAndConditions( listener, condition ) );
			}
		}
	}
	
	public void removeManipulationListener( ManipulationListener listener )
	{
		removeManipulationListener( listener, null );
	}
	
	public void removeManipulationListener( ManipulationListener listener, ManipulatorConditionSet conditions )
	{
		synchronized( this.manipulationListeners ) {
			//Passing in null signals that we want to remove the entire listener entry
			if (conditions != null)
			{
				int listenerIndex = this.manipulationListeners.indexOf( listener );
				if ( listenerIndex > -1 )
				{
					ListenerAndConditions lAndC = this.manipulationListeners.get( listenerIndex );
					lAndC.conditions.remove( conditions );
					//If the condition list is now empty, remove the listener entry from the listener array
					if (lAndC.conditions.size() == 0)
					{
						this.manipulationListeners.remove( listener );
					}
				}
			}
			else
			{
				this.manipulationListeners.remove( listener );
			}
		}
	}
	
	public void triggerEvent( ManipulationEvent event, boolean isActivate )
	{
		for (int i=0; i<this.manipulationListeners.size(); i++)
		{
			ListenerAndConditions currentListener = this.manipulationListeners.get( i );
			if (currentListener.matches( event ))
			{
				if (isActivate)
				{
					currentListener.listener.activate(event);
				}
				else
				{
					currentListener.listener.deactivate(event);
				}
			}
		}
	}
}
