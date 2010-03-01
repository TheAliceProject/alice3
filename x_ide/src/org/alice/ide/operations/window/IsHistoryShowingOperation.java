package org.alice.ide.operations.window;

public class IsHistoryShowingOperation extends IsFrameShowingOperation {
	public IsHistoryShowingOperation() {
		//todo: PREFERENCES_GROUP?
		super( org.alice.ide.IDE.INTERFACE_GROUP, false );
		this.putValue( javax.swing.Action.NAME, "Show History?" );
	}
	@Override
	protected String getTitle() {
		return "History";
	}
	@Override
	protected java.awt.Component createPane() {
		return new edu.cmu.cs.dennisc.history.HistoryPane( org.alice.ide.IDE.PROJECT_GROUP );
	}
}
