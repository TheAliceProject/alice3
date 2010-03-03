/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package edu.cmu.cs.dennisc.zoot.list;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractEditableListPane< E > extends AbstractEditableListLikeSubstancePane< E > {
	public AbstractEditableListPane( java.util.UUID groupUUID, javax.swing.JList list ) {
		super( groupUUID, list );
	}
	public javax.swing.JList getList() {
		return (javax.swing.JList)this.getListLikeSubstance();
	}
	@Override
	protected int getSelectedIndex() {
		return this.getList().getSelectionModel().getMinSelectionIndex();
	}
	@Override
	protected void setSelectedIndex( int index ) {
		this.getList().getSelectionModel().setSelectionInterval( index, index );
	}
	@Override
	protected E getItemAt( int index ) {
		return (E)this.getList().getModel().getElementAt( index );
	}
	@Override
	protected int getListSize() {
		return this.getList().getModel().getSize();
	}
	
	private javax.swing.event.ListSelectionListener listSelectionListener = new javax.swing.event.ListSelectionListener() {
		public void valueChanged( javax.swing.event.ListSelectionEvent e ) {
			updateOperationsEnabledStates();
		}
	};
	@Override
	public void addNotify() {
		this.getList().getSelectionModel().addListSelectionListener( this.listSelectionListener );
		super.addNotify();
	}
	@Override
	public void removeNotify() {
		super.removeNotify();
		this.getList().getSelectionModel().removeListSelectionListener( this.listSelectionListener );
	}
}
