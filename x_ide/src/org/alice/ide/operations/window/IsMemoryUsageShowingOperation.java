package org.alice.ide.operations.window;

public class IsMemoryUsageShowingOperation extends IsFrameShowingOperation {
	public IsMemoryUsageShowingOperation() {
		this.putValue( javax.swing.Action.NAME, "Show Memory Usage?" );
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
