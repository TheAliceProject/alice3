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
package sceneeditor;

/**
 * @author David Culyba
 */
public class ManipulatorConditionSet {
	enum RunningState
	{
		JUST_STARTED,
		IS_RUNNING,
		JUST_ENDED,
		CHANGED,
	}
	
	private DragManipulator manipulator;
	private java.util.Vector< InputCondition > inputConditions = new java.util.Vector< InputCondition >();
	
	public ManipulatorConditionSet( DragManipulator manipulator )
	{
		this.manipulator = manipulator;
	}
	
	public void addCondition( InputCondition inputCondition )
	{
		this.inputConditions.add( inputCondition );
	}
	
	public DragManipulator getManipulator()
	{
		return this.manipulator;
	}
	
	private boolean checkCondition( RunningState state, InputState current, InputState previous )
	{
		switch (state)
		{
		case CHANGED:
			for (int i=0; i<this.inputConditions.size(); i++)
			{
				if (this.inputConditions.get( i ).stateChanged( current, previous ))
				{
					return true;
				}
			}
			break;
		case JUST_STARTED:
			for (int i=0; i<this.inputConditions.size(); i++)
			{
				if (this.inputConditions.get( i ).justStarted( current, previous ))
				{
					return true;
				}
			}
			break;
		case IS_RUNNING:
			for (int i=0; i<this.inputConditions.size(); i++)
			{
				if (this.inputConditions.get( i ).isRunning( current, previous ))
				{
					return true;
				}
			}
			break;
		case JUST_ENDED:
			for (int i=0; i<this.inputConditions.size(); i++)
			{
				if (this.inputConditions.get( i ).justEnded( current, previous ))
				{
					return true;
				}
			}
			break;
		}
		return false;
		
	}
	
	public boolean stateChanged( InputState current, InputState previous )
	{
		return this.checkCondition(RunningState.CHANGED, current, previous);
	}
	
	public boolean shouldContinue(InputState current, InputState previous)
	{
		return this.checkCondition(RunningState.IS_RUNNING, current, previous);
	}
	
	public boolean justStarted(InputState current, InputState previous)
	{
		boolean someoneIsRunning = this.checkCondition(RunningState.IS_RUNNING, current, previous);
		boolean someoneJustStarted = this.checkCondition(RunningState.JUST_STARTED, current, previous);
		return (!someoneIsRunning && someoneJustStarted);
	}
	
	public boolean justEnded(InputState current, InputState previous)
	{
		boolean someoneIsRunning = this.checkCondition(RunningState.IS_RUNNING, current, previous);
		boolean someoneJustStarted = this.checkCondition(RunningState.JUST_STARTED, current, previous);
		boolean someoneJustEnded = this.checkCondition(RunningState.JUST_ENDED, current, previous);
		return (!someoneIsRunning && !someoneJustStarted && someoneJustEnded);
	}
}
