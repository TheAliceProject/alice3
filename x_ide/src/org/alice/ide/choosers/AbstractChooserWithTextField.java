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
package org.alice.ide.choosers;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractChooserWithTextField<E> extends AbstractChooser<E> {
	private edu.cmu.cs.dennisc.zoot.ZSuggestiveTextField textField = new edu.cmu.cs.dennisc.zoot.ZSuggestiveTextField( "", "" ) {
		@Override
		public java.awt.Dimension getPreferredSize() {
			return edu.cmu.cs.dennisc.awt.DimensionUtilties.constrainToMinimumWidth( super.getPreferredSize(), 240 );
		}
	};
	private java.awt.Component[] components = { this.textField };

	public java.awt.Component[] getComponents() {
		return this.components;
	}
	protected abstract E valueOf( String text );
	public final E getValue() {
		return this.valueOf( this.textField.getText() );
	}
	public boolean isInputValid() {
		try {
			this.valueOf( this.textField.getText() );
			return true;
		} catch( RuntimeException re ) {
			return false;
		}
	}

	public void setAndSelectText( String text ) {
		this.textField.setText( text );
		this.textField.selectAll();
	}
	@Override
	public void setInputPane( final edu.cmu.cs.dennisc.zoot.ZInputPane< ? > inputPane ) {
		super.setInputPane( inputPane );
		this.textField.getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
			public void changedUpdate( javax.swing.event.DocumentEvent e ) {
				inputPane.updateOKButton();
			}
			public void insertUpdate( javax.swing.event.DocumentEvent e ) {
				inputPane.updateOKButton();
			}
			public void removeUpdate( javax.swing.event.DocumentEvent e ) {
				inputPane.updateOKButton();
			}
		} );
	}
}
