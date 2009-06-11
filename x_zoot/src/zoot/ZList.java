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
package zoot;

/**
 * @author Dennis Cosgrove
 */
public class ZList<E> extends javax.swing.JList {
	public ZList() {
	}
	public ZList( ItemSelectionOperation itemSelectionOperation ) {
		super( itemSelectionOperation.getComboBoxModel() );
		this.itemSelectionOperation = itemSelectionOperation;
		this.addListSelectionListener( this.listSelectionAdapter );
		this.updateSelection();
	}
	private ItemSelectionOperation itemSelectionOperation;
	private javax.swing.event.ListSelectionListener listSelectionAdapter = new javax.swing.event.ListSelectionListener() {
		public void valueChanged( javax.swing.event.ListSelectionEvent e ) {
//			edu.cmu.cs.dennisc.print.PrintUtilities.println( "valueChanged", e );
			if( e.getValueIsAdjusting() ) {
				//pass
			} else {
				if( ZList.this.itemSelectionOperation != null ) {
					ZManager.performIfAppropriate( ZList.this.itemSelectionOperation, e, ZManager.CANCEL_IS_FUTILE, null, ZList.this.getSelectedValue() );
				}
			}
		}
	};
	private void updateSelection() {
		if( this.itemSelectionOperation != null ) {
			javax.swing.ComboBoxModel comboBoxModel = this.itemSelectionOperation.getComboBoxModel();
			this.setSelectedValue( comboBoxModel.getSelectedItem(), true );
		} else {
			this.setSelectedIndex( -1 );
		}
	}
	public ItemSelectionOperation getItemSelectionOperation() {
		return this.itemSelectionOperation;
	}
	public void setItemSelectionOperation( ItemSelectionOperation itemSelectionOperation ) {
		if( this.itemSelectionOperation != null ) {
			this.removeListSelectionListener( this.listSelectionAdapter );
			this.setModel( new javax.swing.DefaultListModel() );
		}
		this.itemSelectionOperation = itemSelectionOperation;
		if( this.itemSelectionOperation != null ) {
			this.setModel( this.itemSelectionOperation.getComboBoxModel() );
			this.addListSelectionListener( this.listSelectionAdapter );
			this.updateSelection();
		}
	}
}
