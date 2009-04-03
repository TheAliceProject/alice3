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
package org.alice.ide.cascade.customfillin;

/**
 * @author Dennis Cosgrove
 */
public abstract class CustomExpressionPane<E extends edu.cmu.cs.dennisc.alice.ast.Expression> extends org.alice.ide.preview.PreviewInputPane< E > {
	private zoot.ZLabel label = new zoot.ZLabel();
	private zoot.ZTextField textField = new zoot.ZTextField();
	public CustomExpressionPane() {
		this.setLabelText( "value:" );
	}
	public void setLabelText( String labelText ) {
		this.label.setText( labelText );
	}
	protected edu.cmu.cs.dennisc.alice.ast.Expression getPreviousExpression() {
		org.alice.ide.IDE ide = this.getIDE();
		if( ide != null ) {
			return ide.getPreviousExpression();
		} else {
			return null;
		}
	}

	@Override
	protected java.util.List< java.awt.Component[] > updateRows( java.util.List< java.awt.Component[] > rv ) {
		rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( this.label, this.textField ) );
		return rv;
	}

	
	public void setAndSelectText( String text ) {
		this.textField.setText( text );
		this.textField.selectAll();
	}
	protected abstract E valueOf( String text );
	public boolean isInputValid() {
		try {
			this.valueOf( this.textField.getText() );
			return true;
		} catch( RuntimeException re ) {
			return false;
		}
	}
	
	@Override
	protected java.awt.Component createPreviewSubComponent() {
		return getIDE().getPreviewFactory().createComponent( this.getActualInputValue() );
	}
	
	@Override
	public boolean isOKButtonValid() {
		return super.isOKButtonValid() && isInputValid();
	}
	@Override
	protected final E getActualInputValue() {
		return this.valueOf( this.textField.getText() );
	}
	
	public zoot.ZTextField getTextField() {
		return this.textField;
	}

}

