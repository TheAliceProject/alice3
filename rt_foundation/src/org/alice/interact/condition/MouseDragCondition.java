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
import org.alice.interact.ModifierMask;

/**
 * @author David Culyba
 */
public class MouseDragCondition extends ModifierSensitiveCondition{
	
	private int mouseButton = 0;
	private PickCondition pickCondition = null;
	
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
	

//	@Override
//	public boolean justStarted( InputState currentState, InputState previousState ) {
//		//only test pick on the initial mouse down
//		//the drag is still valid if the pick becomes false
//		if (testInputs(currentState) && testPick(currentState) && !testInputs(previousState))
//		{
//			return true;
//		}
//		return false;
//	}

	@Override
	public boolean stateChanged( InputState currentState, InputState previousState ) {
		return ( super.stateChanged( currentState, previousState ) || !currentState.getMouseLocation().equals( previousState.getMouseLocation() ) );
	}

	@Override
	protected boolean testState( InputState state ) {
		return testInputs(state) && testPick( state );
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
