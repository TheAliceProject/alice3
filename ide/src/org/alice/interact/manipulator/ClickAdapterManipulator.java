package org.alice.interact.manipulator;

import org.alice.interact.InputState;
import org.alice.interact.handle.HandleSet;

public class ClickAdapterManipulator extends AbstractManipulator {

	protected ManipulatorClickAdapter clickAdapter;
	
	public ClickAdapterManipulator(ManipulatorClickAdapter clickAdapter)
	{
		this.clickAdapter = clickAdapter;
	}
	
	public ManipulatorClickAdapter getClickAdapter()
	{
		return this.clickAdapter;
	}
	
	@Override
	public void clickManipulator(InputState clickInput, InputState previousInput) {
		this.clickAdapter.onClick(clickInput);
	}
	
	@Override
	public void doClickManipulator(InputState endInput, InputState previousInput) 
	{
	}
	
	@Override
	public void doDataUpdateManipulator(InputState currentInput,
			InputState previousInput) 
	{
	}

	@Override
	public void doEndManipulator(InputState endInput, InputState previousInput) 
	{
	}

	@Override
	public boolean doStartManipulator(InputState startInput) {
		return false;
	}

	@Override
	public void doTimeUpdateManipulator(double dTime, InputState currentInput) 
	{	
	}

	@Override
	protected HandleSet getHandleSetToEnable()
	{
		return null;
	}

	@Override
	public String getUndoRedoDescription() 
	{
		return null;
	}

}
