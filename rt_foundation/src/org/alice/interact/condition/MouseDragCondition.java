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

import java.awt.Point;

import org.alice.interact.InputState;
import org.alice.interact.ModifierMask;

/**
 * @author David Culyba
 */
public class MouseDragCondition extends ModifierSensitiveCondition{
	
	protected static final double MIN_MOUSE_MOVE = 2.0d;
	private int mouseButton = 0;
	private PickCondition pickCondition = null;
	protected Point mouseDownLocation;
	protected boolean hasStarted = false;
	
	
	enum PickClasses
	{
		ANYTHING,
		MOVEABLE_OBJECTS,
		NOTHING,
	}
	
	public MouseDragCondition( int mouseButton, PickCondition pickCondition )
	{
		this(mouseButton, pickCondition, null);
	}
	
	public MouseDragCondition( int mouseButton, PickCondition pickCondition, ModifierMask modifierMask )
	{
		super(modifierMask);
		this.pickCondition = pickCondition;
		this.mouseButton = mouseButton;
	}

	@Override
	public boolean stateChanged( InputState currentState, InputState previousState ) {
		return ( super.stateChanged( currentState, previousState ) || !currentState.getMouseLocation().equals( previousState.getMouseLocation() ) );
	}

	@Override
	protected boolean testState( InputState state ) {
		return testInputs(state) && testPick( state );
	}
	
	@Override
	public boolean isRunning( InputState currentState, InputState previousState ) {
		if (this.hasStarted && testState(currentState) && testState(previousState))
		{
			return true;
		}
		return false;
	}
	
	@Override
	public boolean justStarted( InputState currentState, InputState previousState ) 
	{
		if (testClick(currentState) && !testInputs(previousState))
		{
			this.mouseDownLocation = new Point(currentState.getMouseLocation());
		}
		if (testInputs(currentState))
		{
			if (this.mouseDownLocation != null &&  currentState.getMouseLocation().distance( this.mouseDownLocation ) >= MIN_MOUSE_MOVE)
			{
				this.mouseDownLocation = null;
				this.hasStarted = true;
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean justEnded( InputState currentState, InputState previousState ) {
		if (this.hasStarted && !testState(currentState) && testState(previousState))
		{
			this.hasStarted = false;
			return true;
		}
		return false;
	}
	
	protected boolean testClick(InputState state)
	{
		if (testInputs(state))
		{
			return testPick(state);
		}
		return false;
	}
	
	protected boolean testPick(InputState state)
	{
		return pickCondition.evalutateChain( state );
	}
	
	protected boolean testMouse( InputState state )
	{
		return state.isMouseDown( this.mouseButton );
	}
	
	protected boolean testInputs( InputState state )
	{
		return (super.testState( state ) && testMouse( state ) );
	}
	
	
}
