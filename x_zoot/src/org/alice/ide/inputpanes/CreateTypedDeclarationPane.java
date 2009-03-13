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

class TypeComboBox extends zoot.ZComboBox {
	public TypeComboBox() {
		this.addItem( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.BOOLEAN_OBJECT_TYPE );
		this.addItem( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.DOUBLE_OBJECT_TYPE );
		this.addItem( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.INTEGER_OBJECT_TYPE );
	}
}

/**
 * @author Dennis Cosgrove
 */
public abstract class CreateTypedDeclarationPane<E> extends CreateDeclarationPane<E> {
	private TypeComboBox typeComboBox;
	public CreateTypedDeclarationPane( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice ownerType ) {
		super( ownerType );
	}
	protected edu.cmu.cs.dennisc.alice.ast.AbstractType getValueType() {
		return (edu.cmu.cs.dennisc.alice.ast.AbstractType)this.typeComboBox.getSelectedItem();
	}
	@Override
	protected java.util.List< java.awt.Component[] > createComponentRows() {
		zoot.ZLabel label = new zoot.ZLabel();
		label.setText( "type:" );
		this.typeComboBox = new TypeComboBox();;
		java.util.List< java.awt.Component[] > rv = super.createComponentRows();
		rv.add( new java.awt.Component[] { label, this.typeComboBox } );
		return rv;
	}
}
