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
public abstract class CreateParameterPane extends CreateDeclarationPane<edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice> {
	private edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice code;
	public CreateParameterPane( edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice code ) {
		super( new org.alice.ide.namevalidators.ParameterNameValidator( code ) );
		this.code = code;
		this.setBackground( getIDE().getParameterColor() );
		
	}
	protected edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice getCode() {
		return this.code;
	}
	@Override
	protected boolean isEditableInitializerComponentDesired() {
		return false;
	}
	@Override
	protected boolean isIsReassignableComponentDesired() {
		return false;
	}
	@Override
	protected boolean isEditableValueTypeComponentDesired() {
		return true;
	}
	@Override
	protected String getValueTypeText() {
		return "value type:";
	}
	@Override
	protected java.awt.Component createInitializerComponent() {
		return null;
	}
	@Override
	protected java.awt.Component createPreviewSubComponent() {
		return new org.alice.ide.codeeditor.TypedParameterPane( null, this.getActualInputValue() );
	}
	@Override
	protected String getTitleDefault() {
		return "Declare Parameter";
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice getActualInputValue() {
		return new edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice( this.getDeclarationName(), this.getValueType() );
	}
}
