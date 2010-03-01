package edu.cmu.cs.dennisc.history;

class HistoryStackModel extends javax.swing.AbstractListModel {
	private HistoryManager historyManager;
	public HistoryStackModel( HistoryManager historyManager ) {
		this.historyManager = historyManager;
	}
	public int getSize() {
		return historyManager.getStack().size() + 1;
	}
	public Object getElementAt( int index ) {
		if( index == 0 ) {
			return null;
		} else {
			return historyManager.getStack().elementAt( index-1 );
		}
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
		if( index == 0 ) {
			rv.setText( "Open Project" );
		} else {
			edu.cmu.cs.dennisc.zoot.Edit edit = value.getEdit();
			
			//todo
			java.util.Locale locale = javax.swing.JComponent.getDefaultLocale();
			
			String text = edit.getPresentation( locale );
//			text = "edit: " + text;
			rv.setText( text );
			
			int selectedIndex = list.getSelectedIndex();
			if( selectedIndex >= 0 && index > selectedIndex ) {
				rv.setEnabled( false );
			} else {
				rv.setEnabled( true );
			}
		}
		return rv;
	}
}

public class HistoryPane extends edu.cmu.cs.dennisc.croquet.swing.BorderPane {
	private edu.cmu.cs.dennisc.history.event.HistoryListener historyListener = new edu.cmu.cs.dennisc.history.event.HistoryListener() {
		public void operationPushing( edu.cmu.cs.dennisc.history.event.HistoryPushEvent e ) {
		}
		public void operationPushed( edu.cmu.cs.dennisc.history.event.HistoryPushEvent e ) {
		}
		public void insertionIndexChanging( edu.cmu.cs.dennisc.history.event.HistoryInsertionIndexEvent e ) {
		}
		public void insertionIndexChanged( edu.cmu.cs.dennisc.history.event.HistoryInsertionIndexEvent e ) {
			HistoryPane.this.historyStackModel.refresh();
			HistoryPane.this.list.setSelectedIndex( e.getNextIndex() );
			HistoryPane.this.list.repaint();
		}
	};

	private javax.swing.JList list = new javax.swing.JList();
	private HistoryStackModel historyStackModel;
	public HistoryPane( java.util.UUID uuid ) {
		final HistoryManager historyManager = HistoryManager.getInstance( uuid );
		this.historyStackModel = new HistoryStackModel( historyManager );
		this.list.setModel( this.historyStackModel );
		this.list.setCellRenderer( new HistoryCellRenderer() );
		this.list.addListSelectionListener( new javax.swing.event.ListSelectionListener() {
			public void valueChanged(javax.swing.event.ListSelectionEvent e) {
				if( e.getValueIsAdjusting() ) {
					//pass
				} else {
					historyManager.setInsertionIndex(list.getSelectedIndex());
					HistoryPane.this.list.repaint();
				}
			}
		} );
		javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane( this.list );
		this.add( scrollPane );
	}
	@Override
	public void addNotify() {
		super.addNotify();
		this.historyStackModel.getHistoryManager().addHistoryListener( this.historyListener );
		this.list.setSelectedIndex( this.historyStackModel.getHistoryManager().getInsertionIndex() );
	}
	@Override
	public void removeNotify() {
		this.list.setSelectedIndex( -1 );
		this.historyStackModel.getHistoryManager().removeHistoryListener( this.historyListener );
		super.removeNotify();
	}
	@Override
	public java.awt.Dimension getPreferredSize() {
		return new java.awt.Dimension( 240, 768 );
	}
}
