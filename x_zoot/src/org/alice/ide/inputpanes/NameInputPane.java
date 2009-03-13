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
package org.alice.ide.inputpanes;

/**
 * @author Dennis Cosgrove
 */
abstract class NameInputPane<E> extends RowsInputPane< E > {
	private javax.swing.JTextField textField;

	public NameInputPane() {
		this.textField.getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
			public void changedUpdate( javax.swing.event.DocumentEvent e ) {
			}
			public void insertUpdate( javax.swing.event.DocumentEvent e ) {
			}
			public void removeUpdate( javax.swing.event.DocumentEvent e ) {
			}
		} );
		this.textField.addActionListener( new java.awt.event.ActionListener() {
			public void actionPerformed( java.awt.event.ActionEvent e ) {
			}
		} );
	}

	public void setAndSelectNameText( String text ) {
		if( text != null ) {
			this.textField.setText( text );
			this.textField.selectAll();
		}
	}

	public String getName() {
		return this.textField.getText();
	}

	@Override
	protected java.util.List< java.awt.Component[] > createComponentRows() {
		java.util.List< java.awt.Component[] > rv = super.createComponentRows();
		zoot.ZLabel label = new zoot.ZLabel();
		label.setText( "name:" );
		this.textField = new javax.swing.JTextField( 10 );
		rv.add( new java.awt.Component[] { label, this.textField } );
		return rv;
	}
}
