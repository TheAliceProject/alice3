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
package org.alice.ide.renamenodepanes;

import org.alice.ide.createdeclarationpanes.RowsInputPane;

/**
 * @author Dennis Cosgrove
 */
public abstract class NameInputPane<E> extends RowsInputPane< E > {
	private javax.swing.JTextField textField;

	public void setAndSelectNameText( String text ) {
		if( text != null ) {
			this.textField.setText( text );
			this.textField.selectAll();
		}
	}

	protected abstract boolean isNameAcceptable( String name );

	public String getNameText() {
		return this.textField.getText();
	}
	@Override
	public boolean isOKButtonValid() {
		return super.isOKButtonValid() && this.isNameAcceptable( this.textField.getText() );
	}
	protected void handleNameTextChange( String nameText ) {
		updateOKButton();
	}

	@Override
	protected java.util.List< java.awt.Component[] > createComponentRows() {
		java.util.List< java.awt.Component[] > rv = super.createComponentRows();
		zoot.ZLabel label = zoot.ZLabel.acquire();
		label.setText( "name:" );
		this.textField = new javax.swing.JTextField( 10 );
		this.textField.getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
			private void handleUpdate( javax.swing.event.DocumentEvent e ) {
				javax.swing.text.Document document = e.getDocument();
				try {
					handleNameTextChange( document.getText( 0, document.getLength() ) );
				} catch( javax.swing.text.BadLocationException ble ) {
					throw new RuntimeException( ble );
				}
			}
			public void changedUpdate( javax.swing.event.DocumentEvent e ) {
				this.handleUpdate( e );
			}
			public void insertUpdate( javax.swing.event.DocumentEvent e ) {
				this.handleUpdate( e );
			}
			public void removeUpdate( javax.swing.event.DocumentEvent e ) {
				this.handleUpdate( e );
			}
		} );
//		javax.swing.text.Keymap keymap = this.textField.getKeymap();
//		edu.cmu.cs.dennisc.print.PrintUtilities.println( keymap );
//		javax.swing.KeyStroke enterKeyStroke = javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_ENTER, 0, false );
//		keymap.removeKeyStrokeBinding( enterKeyStroke );
//		this.textField.setKeymap( keymap );
		rv.add( new java.awt.Component[] { label, this.textField } );
		return rv;
	}
}
