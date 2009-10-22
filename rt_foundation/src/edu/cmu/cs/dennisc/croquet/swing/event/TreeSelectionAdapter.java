package edu.cmu.cs.dennisc.croquet.swing.event;

public abstract class TreeSelectionAdapter<E> implements javax.swing.event.TreeSelectionListener {
	protected abstract void valueChanged(javax.swing.event.TreeSelectionEvent e, E oldLeadValue, E newLeadValue );
	public final void valueChanged(javax.swing.event.TreeSelectionEvent e) {
		//javax.swing.JTree tree = (javax.swing.JTree)e.getSource();
		javax.swing.tree.TreePath oldLeadPath = e.getOldLeadSelectionPath();
		javax.swing.tree.TreePath newLeadPath = e.getNewLeadSelectionPath();
		//int oldRow = tree.getRowForPath( oldLeadPath );
		//int newRow = tree.getRowForPath( newLeadPath );
		E oldLeadValue;
		if( oldLeadPath != null ) {
			oldLeadValue = (E)oldLeadPath.getLastPathComponent();
		} else {
			oldLeadValue = null;
		}
		E newLeadValue;
		if( newLeadPath != null ) {
			newLeadValue = (E)newLeadPath.getLastPathComponent();
		} else {
			newLeadValue = null;
		}
		this.valueChanged( e, oldLeadValue, newLeadValue );
	}
}
