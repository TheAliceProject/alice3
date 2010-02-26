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
package org.alice.ide.common;

/**
 * @author Dennis Cosgrove
 */
public class DeclarationNameLabel extends javax.swing.JLabel {
	private edu.cmu.cs.dennisc.alice.ast.AbstractDeclaration declaration;

	private class NamePropertyAdapter implements edu.cmu.cs.dennisc.property.event.PropertyListener {
		public void propertyChanging( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
		}
		public void propertyChanged( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
			DeclarationNameLabel.this.updateText();
		}
	}

	private NamePropertyAdapter namePropertyAdapter = new NamePropertyAdapter();

	public DeclarationNameLabel( edu.cmu.cs.dennisc.alice.ast.AbstractDeclaration declaration ) {
		this.declaration = declaration;
		this.updateText();
	}
	@Override
	public void addNotify() {
		super.addNotify();
		if( this.declaration != null ) {
			edu.cmu.cs.dennisc.property.StringProperty nameProperty = this.declaration.getNamePropertyIfItExists();
			if( nameProperty != null ) {
				nameProperty.addPropertyListener( this.namePropertyAdapter );
			}
		}
	}
	@Override
	public void removeNotify() {
		if( this.declaration != null ) {
			edu.cmu.cs.dennisc.property.StringProperty nameProperty = this.declaration.getNamePropertyIfItExists();
			if( nameProperty != null ) {
				nameProperty.removePropertyListener( this.namePropertyAdapter );
			}
		}
		super.removeNotify();
	}
	protected edu.cmu.cs.dennisc.alice.ast.AbstractDeclaration getDeclaration() {
		return this.declaration;
	}
	
	protected String getNameText() {
		return this.declaration.getName();
	}
	
	protected String getTextForNullName() {
		return org.alice.ide.IDE.getSingleton().getTextForNull();
	}
	protected final String getTextForBlankName() {
		return "<unset>";
	}
	private void updateText() {
		String text;
		if( this.declaration != null ) {
			text = getNameText();
		} else {
			text = null;
		}
		if( text != null ) {
			//pass
		} else {
			text = this.getTextForNullName();
		}
		if( text.length() > 0 ) {
			//pass
		} else {
			text = this.getTextForBlankName();
		}
		this.setText( text );
		this.repaint();
	}
}
