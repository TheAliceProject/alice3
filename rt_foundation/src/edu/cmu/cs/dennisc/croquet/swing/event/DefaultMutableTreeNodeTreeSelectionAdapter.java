package edu.cmu.cs.dennisc.croquet.swing.event;

public abstract class DefaultMutableTreeNodeTreeSelectionAdapter<E> extends TreeSelectionAdapter< javax.swing.tree.DefaultMutableTreeNode > {
	protected abstract void valueChangedUserObject(javax.swing.event.TreeSelectionEvent e, E oldLeadValue, E newLeadValue );
	@Override
	protected final void valueChanged(javax.swing.event.TreeSelectionEvent e, javax.swing.tree.DefaultMutableTreeNode oldLeadValue, javax.swing.tree.DefaultMutableTreeNode newLeadValue ) {
		E oldUserObject;
		if( oldLeadValue != null ) {
			oldUserObject = (E)oldLeadValue.getUserObject();
		} else {
			oldUserObject = null;
		}
		E newUserObject;
		if( newLeadValue != null ) {
			newUserObject = (E)newLeadValue.getUserObject();
		} else {
			newUserObject = null;
		}
		this.valueChangedUserObject(e, oldUserObject, newUserObject);
	}
}
