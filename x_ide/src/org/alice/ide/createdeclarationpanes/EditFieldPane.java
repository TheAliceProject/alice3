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
public class EditFieldPane extends AbstractDeclarationPane< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > {
	private edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice fieldDeclaredInAlice;
	private boolean isReferenced;
	private boolean isReassigned;
	private boolean isDropDownForFieldInitializerDesired;
	public EditFieldPane( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice fieldDeclaredInAlice, boolean isReferenced, boolean isReassigned ) {
		super( new org.alice.ide.name.validators.FieldNameValidator( fieldDeclaredInAlice ), fieldDeclaredInAlice.valueType.getValue(), fieldDeclaredInAlice.initializer.getValue() );
		this.fieldDeclaredInAlice = fieldDeclaredInAlice;
		this.isReferenced = isReferenced;
		this.isReassigned = isReassigned;
		this.isDropDownForFieldInitializerDesired = this.getIDE().isDropDownDesiredForFieldInitializer( this.fieldDeclaredInAlice );
	}
	@Override
	protected String getDefaultNameText() {
		return this.fieldDeclaredInAlice.getName();
	}
	
	@Override
	protected boolean isIsReassignableComponentEnabled() {
		return this.isReassigned == false;
	}
	@Override
	protected boolean isValueTypeComponentEnabled() {
		return this.isReferenced == false;
	}
	@Override
	protected boolean getIsReassignableInitialState() {
		return this.fieldDeclaredInAlice.isFinal() == false;
	}
	@Override
	protected boolean isReassignable() {
		if( this.isDropDownForFieldInitializerDesired ) {
			return super.isReassignable();
		} else {
			return fieldDeclaredInAlice.isFinal() == false;
		}
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.AbstractType getValueType() {
		if( this.isDropDownForFieldInitializerDesired ) {
			return super.getValueType();
		} else {
			return fieldDeclaredInAlice.valueType.getValue();
		}
	}
	
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.Expression getInitializer() {
		if( this.isDropDownForFieldInitializerDesired ) {
			return super.getInitializer();
		} else {
			return fieldDeclaredInAlice.initializer.getValue();
		}
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
	@Override
	protected boolean isIsReassignableComponentDesired() {
		return this.isDropDownForFieldInitializerDesired;
	}
	@Override
	protected boolean isEditableInitializerComponentDesired() {
		return this.isDropDownForFieldInitializerDesired;
	}
	@Override
	protected boolean isEditableValueTypeComponentDesired() {
		return this.isDropDownForFieldInitializerDesired;
	}
}
