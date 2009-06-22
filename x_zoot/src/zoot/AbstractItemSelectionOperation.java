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
public abstract class AbstractItemSelectionOperation<E> extends AbstractOperation implements ItemSelectionOperation< E > {
	private javax.swing.ButtonGroup buttonGroup = new javax.swing.ButtonGroup();
	private javax.swing.Action[] actions;
	private javax.swing.ButtonModel[] buttonModels;
	private javax.swing.ComboBoxModel comboBoxModel;
	public AbstractItemSelectionOperation( javax.swing.ComboBoxModel comboBoxModel ) {
		this.comboBoxModel = comboBoxModel;
		int N = this.comboBoxModel.getSize();
		this.actions = new javax.swing.Action[ N ];
		this.buttonModels = new javax.swing.ButtonModel[ N ];
		E selectedItem = (E)comboBoxModel.getSelectedItem();
		for( int i=0; i<N; i++ ) {
			class Action extends javax.swing.AbstractAction {
				public Action( int i, E item ) {
					this.putValue( NAME, getNameFor( i, item ) );
				}
				public void actionPerformed( java.awt.event.ActionEvent e ) {
				}
			}
			final E item = (E)this.comboBoxModel.getElementAt( i );
			this.actions[ i ] = new Action( i, item ); 
			this.buttonModels[ i ] = new javax.swing.JToggleButton.ToggleButtonModel();
			this.buttonModels[ i ].setGroup( buttonGroup );
			if( item == selectedItem ) {
				this.buttonModels[ i ].setSelected( true );
			}
			this.buttonModels[ i ].addItemListener( new java.awt.event.ItemListener() {
				public void itemStateChanged( java.awt.event.ItemEvent e ) {
					if( e.getStateChange() == java.awt.event.ItemEvent.SELECTED ) {
						ZManager.performIfAppropriate( AbstractItemSelectionOperation.this, e, ZManager.CANCEL_IS_FUTILE, null, item );
					}
				}
			} );
		}
	}
	
	protected String getNameFor( int index, E item ) {
		if( item != null ) {
			return item.toString();
		} else {
			return "null";
		}
	}
	public javax.swing.KeyStroke getAcceleratorForConfiguringSwing( int index ) {
		return null;
	}
	public javax.swing.Action getActionForConfiguringSwing( int index ) {
		return this.actions[ index ];
	}
	public javax.swing.ButtonModel getButtonModelForConfiguringSwing( int index ) {
		return this.buttonModels[ index ];
	}
	public javax.swing.ComboBoxModel getComboBoxModel() {
		return this.comboBoxModel;
	}
	public void handleKeyPressed( java.awt.event.KeyEvent e ) {
		int N = this.comboBoxModel.getSize();
		for( int i=0; i<N; i++ ) {
			javax.swing.KeyStroke acceleratorI = this.getAcceleratorForConfiguringSwing( i );
			if( acceleratorI != null ) {
				if( e.getKeyCode() == acceleratorI.getKeyCode() && e.getModifiersEx() == acceleratorI.getModifiers() ) {
					java.awt.event.ActionEvent actionEvent = new java.awt.event.ActionEvent(e.getSource(), java.awt.event.ActionEvent.ACTION_PERFORMED, null );
					this.getActionForConfiguringSwing( i ).actionPerformed( actionEvent );
				}
			}
		}
	}
}
