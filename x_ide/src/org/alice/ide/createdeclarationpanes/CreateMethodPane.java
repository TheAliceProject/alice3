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
public abstract class CreateMethodPane extends CreateDeclarationWithDeclaringTypePane< edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice > {
	public CreateMethodPane( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType ) {
		super( new org.alice.ide.namevalidators.MethodNameValidator( declaringType ) );
	}
	@Override
	protected String getDeclarationText() {
		return "Procedure";
	}
	@Override
	protected java.awt.Component createInitializerComponent() {
		return null;
	}
	@Override
	protected boolean isIsReassignableComponentDesired() {
		return false;
	}
	
	@Override
	protected boolean isEditableInitializerComponentDesired() {
		return false;
	}
	@Override
	protected java.awt.Component createPreviewSubComponent() {
		return new org.alice.ide.codeeditor.MethodHeaderPane( this.getActualInputValue(), null );
	}
	
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice getActualInputValue() {
		return new edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice( this.getDeclarationName(), this.getValueType(), new edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice[] {}, new edu.cmu.cs.dennisc.alice.ast.BlockStatement() );
	}
}
