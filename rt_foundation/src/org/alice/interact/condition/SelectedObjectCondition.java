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
package org.alice.interact.condition;

import org.alice.interact.InputState;
import org.alice.interact.PickHint;

/**
 * @author David Culyba
 */
public class SelectedObjectCondition extends InputCondition {

	public enum ObjectSwitchBehavior
	{
		END_ON_SWITCH,
		IGNORE_SWITCH,
	}
	
	PickHint acceptableType = PickHint.EVERYTHING;
	
	protected boolean isNot = false;
	private ObjectSwitchBehavior switchBehavior = ObjectSwitchBehavior.END_ON_SWITCH;
	
	public SelectedObjectCondition( PickHint acceptableType )
	{
		this(acceptableType, ObjectSwitchBehavior.END_ON_SWITCH);
	}
	
	public SelectedObjectCondition( PickHint acceptableType, ObjectSwitchBehavior switchBehavior )
	{
		this.acceptableType = acceptableType;
		this.switchBehavior = switchBehavior;
	}
	
	@Override
	protected boolean testState( InputState state )
	{
		boolean isValid = this.acceptableType.intersects( PickCondition.getPickType( state.getCurrentlySelectedObject() ) );
		if (isNot)
		{
			return !isValid;
		}
		else 
		{
			return isValid; 
		}
	}
	
	protected boolean selectedObjectSwitched( InputState currentState, InputState previousState )
	{
		if (this.switchBehavior == ObjectSwitchBehavior.END_ON_SWITCH)
		{
			return currentState.getCurrentlySelectedObject() != previousState.getCurrentlySelectedObject();
		}
		else
		{
			return false;
		}
	}
	
	@Override
	public boolean isRunning( InputState currentState, InputState previousState ) {
		return ( super.isRunning( currentState, previousState ) && !selectedObjectSwitched( currentState, previousState ) );
	}

	@Override
	public boolean justEnded( InputState currentState, InputState previousState ) {
		if ( super.justEnded( currentState, previousState ))
		{
			return true;
		}
		else if ( testState(previousState) )
		{
			return selectedObjectSwitched( currentState, previousState );
		}
		else
		{
			return false;
		}
	}

	@Override
	public boolean justStarted( InputState currentState, InputState previousState ) {
		if ( super.justStarted( currentState, previousState ) )
		{
			return true;
		}
		else if ( testState(currentState) )
		{
			return selectedObjectSwitched( currentState, previousState );
		}
		else
		{
			return false;
		}
	}

	@Override
	public boolean stateChanged( InputState currentState, InputState previousState ) {
		if ( (this.switchBehavior == ObjectSwitchBehavior.END_ON_SWITCH) && currentState.getCurrentlySelectedObject() != previousState.getCurrentlySelectedObject() )
		{
			return true;
		}
		else return ( testState( currentState ) != testState( previousState ));
	}


}
