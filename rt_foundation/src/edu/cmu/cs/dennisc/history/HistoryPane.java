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

class HistoryCellRenderer extends edu.cmu.cs.dennisc.croquet.swing.ListCellRenderer< edu.cmu.cs.dennisc.zoot.event.CommitEvent > {
	@Override
	protected javax.swing.JLabel getListCellRendererComponent( javax.swing.JLabel rv, javax.swing.JList list, edu.cmu.cs.dennisc.zoot.event.CommitEvent value, int index, boolean isSelected, boolean cellHasFocus ) {
		edu.cmu.cs.dennisc.zoot.Edit edit = value.getEdit();
		
		//todo
		java.util.Locale locale = javax.swing.JComponent.getDefaultLocale();
		
		
		rv.setText( edit.getPresentation( locale ) );
		
		int selectedIndex = list.getSelectedIndex();
		if( selectedIndex >= 0 && index > selectedIndex ) {
			rv.setEnabled( false );
		} else {
			rv.setEnabled( true );
		}
		return rv;
	}
}

public class HistoryPane extends edu.cmu.cs.dennisc.croquet.swing.BorderPane {
	private edu.cmu.cs.dennisc.history.event.HistoryListener historyListener = new edu.cmu.cs.dennisc.history.event.HistoryListener() {
		public void operationPushing( edu.cmu.cs.dennisc.history.event.HistoryEvent e ) {
		}
		public void operationPushed( edu.cmu.cs.dennisc.history.event.HistoryEvent e ) {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "hashCode:", e.getEdit().hashCode() );
			HistoryPane.this.historyStackModel.refresh();
		}
	};

	private javax.swing.JList list = new javax.swing.JList();
	private HistoryStackModel historyStackModel;
	public HistoryPane( java.util.UUID uuid ) {
		HistoryManager historyManager = HistoryManager.get( uuid );
		this.historyStackModel = new HistoryStackModel( historyManager );
		this.list.setModel( this.historyStackModel );
		this.list.setCellRenderer( new HistoryCellRenderer() );
		javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane( this.list );
		this.add( scrollPane );
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
	@Override
	public java.awt.Dimension getPreferredSize() {
		return new java.awt.Dimension( 240, 768 );
	}
}
