package org.alice.interact.condition;

import org.alice.interact.InputState;
import org.alice.interact.ModifierMask;

public abstract class MousePickBasedCondition extends ModifierSensitiveCondition 
{
	protected int mouseButton = 0;
	protected PickCondition pickCondition = null;
		
	public MousePickBasedCondition( int mouseButton, PickCondition pickCondition )
	{
		this(mouseButton, pickCondition, null);
	}
	
	public MousePickBasedCondition( int mouseButton, PickCondition pickCondition, ModifierMask modifierMask )
	{
		super(modifierMask);
		this.pickCondition = pickCondition;
		this.mouseButton = mouseButton;
	}

	@Override
	protected boolean testState( InputState state ) {
		boolean inputTest = testInputs(state);
		boolean pickTest = false;
		if (inputTest)
		{
			pickTest = testPick( state );
		}
		return  inputTest && pickTest;
	}
	
	protected boolean testInputsAndPick(InputState state)
	{
		if (testInputs(state))
		{
			return testPick(state);
		}
		return false;
	}
	
	protected boolean testPick(InputState state)
	{
		boolean pickValid = pickCondition.evalutateChain( state );
		return pickValid; 
	}
	
	protected boolean testMouse( InputState state )
	{
		boolean mouseTest = state.isMouseDown( this.mouseButton );
		return mouseTest;
	}
	
	protected boolean testInputs( InputState state )
	{
		boolean superTest = super.testState( state );
		boolean mouseTest = testMouse( state );
		return (superTest && mouseTest);
	}
}
