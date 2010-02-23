package edu.cmu.cs.dennisc.history;

class HistoryStackModel extends javax.swing.AbstractListModel {
	private HistoryManager historyManager;
	public HistoryStackModel( HistoryManager historyManager ) {
		this.historyManager = historyManager;
	}
	public int getSize() {
		return historyManager.getStack().size();
	}
	public Object getElementAt( int index ) {
		return historyManager.getStack().elementAt( index );
	}
	public HistoryManager getHistoryManager() {
		return this.historyManager;
	}
	public void refresh() {
		this.fireContentsChanged( this, 0, this.getSize() );
	}
};

public class HistoryViewer extends javax.swing.JList {
	private edu.cmu.cs.dennisc.history.event.HistoryListener historyListener = new edu.cmu.cs.dennisc.history.event.HistoryListener() {
		public void operationPushing( edu.cmu.cs.dennisc.history.event.HistoryEvent e ) {
		}
		public void operationPushed( edu.cmu.cs.dennisc.history.event.HistoryEvent e ) {
			HistoryViewer.this.historyStackModel.refresh();
		}
	};

	private HistoryStackModel historyStackModel;
	public HistoryViewer( java.util.UUID uuid ) {
		HistoryManager historyManager = HistoryManager.get( uuid );
		this.historyStackModel = new HistoryStackModel( historyManager );
		this.setModel( this.historyStackModel );
	}
	@Override
	public void addNotify() {
		super.addNotify();
		this.historyStackModel.getHistoryManager().addHistoryListener( this.historyListener );
	}
	@Override
	public void removeNotify() {
		this.historyStackModel.getHistoryManager().removeHistoryListener( this.historyListener );
		super.removeNotify();
	}
}
