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
public class DeclarationNameLabel extends zoot.ZLabel {
	private edu.cmu.cs.dennisc.alice.ast.AbstractDeclaration declaration;

	private class NamePropertyAdapter implements edu.cmu.cs.dennisc.property.event.PropertyListener {
		public void propertyChanging( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
		}
		public void propertyChanged( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
			DeclarationNameLabel.this.updateText();
		}
	}

	private NamePropertyAdapter namePropertyAdapter = null;
	private edu.cmu.cs.dennisc.property.StringProperty nameProperty = null;

	public DeclarationNameLabel( edu.cmu.cs.dennisc.alice.ast.AbstractDeclaration declaration ) {
		this.declaration = declaration;
		this.updateText();
		this.nameProperty = declaration.getNamePropertyIfItExists();
	}
	@Override
	public void addNotify() {
		super.addNotify();
		if( this.nameProperty != null ) {
			this.namePropertyAdapter = new NamePropertyAdapter();
			this.nameProperty.addPropertyListener( this.namePropertyAdapter );
		}
	}
	@Override
	public void removeNotify() {
		if( this.nameProperty != null ) {
			this.nameProperty.removePropertyListener( this.namePropertyAdapter );
			this.namePropertyAdapter = null;
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
