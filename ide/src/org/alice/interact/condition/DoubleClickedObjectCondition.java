package org.alice.interact.condition;

import org.alice.interact.InputState;
import org.alice.interact.ModifierMask;

public class DoubleClickedObjectCondition extends ClickedObjectCondition 
{
	protected InputState[] clickArray = new InputState[2];
	
	public DoubleClickedObjectCondition(int mouseButton, PickCondition pickCondition) 
	{
		super(mouseButton, pickCondition);
	}
	
	public DoubleClickedObjectCondition( int mouseButton, PickCondition pickCondition, ModifierMask modifierMask )
	{
		super(mouseButton, pickCondition, modifierMask);
	}
	
	@Override
	protected void update(InputState currentState, InputState previousState) 
	{
		super.update(currentState, previousState);
		if (wasValidClick(currentState, previousState))
		{
			clickArray[1] = clickArray[0];
			clickArray[0] = new InputState(currentState);
		}
	}
	
	protected boolean doStatesMatch(InputState a, InputState b)
	{
		if (a == b)
		{
			return true;
		}
		if (a == null || b == null)
		{
			return false;
		}
		if (a.getTimeCaptured() == b.getTimeCaptured())
		{
			return true;
		}
		return false;
	}
	
	@Override
	public boolean clicked( InputState currentState, InputState previousState ) {
		boolean validDoubleClick = false;
		if ( doStatesMatch(currentState, clickArray[0]) )
		{
			//Clicked is called after update, so if we have a double click, it will look like this:
			// clickArray[0] = this click
			// clickArray[1] = previous click
			if (clickArray[0] != null && clickArray[1] != null)
			{
				long elapseTime = clickArray[0].getTimeCaptured() - clickArray[1].getTimeCaptured();
				double mouseDistance = clickArray[0].getMouseLocation().distance(clickArray[1].getMouseLocation());
				if (elapseTime <= MAX_CLICK_TIME && mouseDistance <= MAX_MOUSE_MOVE)
				{
					validDoubleClick = true;
				}
			}
		}
		return validDoubleClick;
	}
	
	
}
