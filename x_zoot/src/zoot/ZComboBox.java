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
public class ZComboBox extends javax.swing.JComboBox {
	private ItemSelectionOperation itemSelectionOperation;
	private java.awt.event.ItemListener itemAdapter = new java.awt.event.ItemListener() {
		public void itemStateChanged( java.awt.event.ItemEvent e ) {
			if( e.getStateChange() == java.awt.event.ItemEvent.SELECTED ) {
				if( ZComboBox.this.itemSelectionOperation != null ) {
					ZManager.performIfAppropriate( ZComboBox.this.itemSelectionOperation, e, ZManager.CANCEL_IS_FUTILE, null, ZComboBox.this.getSelectedItem() );
				}
			}
		}
	};
	public ItemSelectionOperation getItemSelectionOperation() {
		return this.itemSelectionOperation;
	}
	public void setItemSelectionOperation( ItemSelectionOperation itemSelectionOperation ) {
		if( this.itemSelectionOperation != null ) {
			this.removeItemListener( this.itemAdapter );
			this.setModel( new javax.swing.DefaultComboBoxModel() );
		}
		this.itemSelectionOperation = itemSelectionOperation;
		if( this.itemSelectionOperation != null ) {
			this.setModel( this.itemSelectionOperation.getComboBoxModel() );
			this.addItemListener( this.itemAdapter );
		}
	}
}
