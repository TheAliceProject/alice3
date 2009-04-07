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

/**
 * @author David Culyba
 */
public class AndInputCondition extends InputCondition {

	private InputCondition[] requiredConditions;
	
	public AndInputCondition( InputCondition conditionA, InputCondition conditionB )
	{
		InputCondition[] reqConds = {conditionA, conditionB};
		this.setRequiredConditions( reqConds );
	}
	
	public AndInputCondition( InputCondition[] requiredConditions )
	{
		this.setRequiredConditions( requiredConditions );
	}
	
	public void setRequiredConditions( InputCondition[] requiredConditions )
	{
		this.requiredConditions = requiredConditions;
	}
	
	@Override
	public boolean isRunning( InputState currentState, InputState previousState ) {
		for (int i=0; i<requiredConditions.length; i++)
		{
			if (!requiredConditions[i].isRunning( currentState, previousState ))
			{
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean justEnded( InputState currentState, InputState previousState ) {
		for (int i=0; i<requiredConditions.length; i++)
		{
			if (requiredConditions[i].justEnded( currentState, previousState ))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	/*
	 * Returns true if any of the conditions have "just started" and any that haven't "just started" are already "running"
	 */
	public boolean justStarted( InputState currentState, InputState previousState ) {
		boolean anyStart = false;
		for (int i=0; i<requiredConditions.length; i++)
		{
			if (requiredConditions[i].justStarted( currentState, previousState ))
			{
				anyStart = true;
			}
			else if (!requiredConditions[i].isRunning( currentState, previousState ))
			{
				return false;
			}
		}
		if (anyStart)
		{
			return true;
		}
		return false;
	}

	@Override
	public boolean stateChanged( InputState currentState, InputState previousState ) {
		for (int i=0; i<requiredConditions.length; i++)
		{
			if (requiredConditions[i].stateChanged( currentState, previousState ))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	protected boolean testState( InputState state ) {
		for (int i=0; i<requiredConditions.length; i++)
		{
			if (!requiredConditions[i].testState( state ))
			{
				return false;
			}
		}
		return true;
	}
	
	@Override
	public String toString()
	{
		String string = "";
		boolean isFirst = true;
		for (InputCondition input : this.requiredConditions)
		{
			if (isFirst)
			{
				isFirst = false;
			}
			else
			{
				string += " AND ";
			}
			string += input;
		}
		return string;
	}


}
