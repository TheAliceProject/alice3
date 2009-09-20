package org.alice.ide.operations.view;

public class IsMemoryUsageShowingOperation extends IsFrameShowingOperation {
	public IsMemoryUsageShowingOperation() {
		this.putValue( javax.swing.Action.NAME, "Memory Usage" );
	}
	@Override
	protected java.awt.Component createPane() {
		return new edu.cmu.cs.dennisc.memory.MemoryPane();
	}
	@Override
	public boolean isSignificant() {
		return false;
	}
	
}
