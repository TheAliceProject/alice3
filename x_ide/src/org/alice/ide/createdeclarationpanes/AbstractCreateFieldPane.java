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
package org.alice.ide.createdeclarationpanes;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractCreateFieldPane extends CreateDeclarationWithDeclaringTypePane< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > {
	public AbstractCreateFieldPane( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType ) {
		super( new org.alice.ide.namevalidators.FieldNameValidator( declaringType ) );
		this.setBackground( org.alice.ide.IDE.getFieldColor() );
	}
	@Override
	protected final String getDeclarationText() {
		return "Property";
	}
	@Override
	protected java.awt.Component createPreviewSubComponent() {
		return new org.alice.ide.common.FieldDeclarationPane( org.alice.ide.IDE.getSingleton().getPreviewFactory(), this.getActualInputValue() );
	}
	@Override
	protected final edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice getActualInputValue() {
		edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice rv = new edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice( this.getDeclarationName(), this.getValueType(), this.getInitializer() );
		edu.cmu.cs.dennisc.alice.ast.FieldModifierFinalVolatileOrNeither value;
		if( this.isReassignable() ) {
			value = edu.cmu.cs.dennisc.alice.ast.FieldModifierFinalVolatileOrNeither.NEITHER;
		} else {
			value = edu.cmu.cs.dennisc.alice.ast.FieldModifierFinalVolatileOrNeither.FINAL;
		}
		rv.finalVolatileOrNeither.setValue( value );
		rv.access.setValue( edu.cmu.cs.dennisc.alice.ast.Access.PRIVATE );
		return rv;
	}
}
