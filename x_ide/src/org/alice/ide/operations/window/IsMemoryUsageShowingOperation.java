package org.alice.ide.operations.window;

public class IsMemoryUsageShowingOperation extends IsFrameShowingOperation {
	public IsMemoryUsageShowingOperation() {
		//todo: PREFERENCES_GROUP?
		super( org.alice.ide.IDE.INTERFACE_GROUP, true );
		this.putValue( javax.swing.Action.NAME, "Show Memory Usage?" );
	}
	@Override
	protected String getTitle() {
		return "Memory Usage";
	}
	@Override
	protected java.awt.Component createPane() {
		return new edu.cmu.cs.dennisc.memory.MemoryPane();
	}
}
