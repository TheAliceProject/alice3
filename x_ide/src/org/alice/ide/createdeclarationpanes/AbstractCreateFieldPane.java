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

class IsFinalStateOperation extends org.alice.ide.operations.AbstractBooleanStateOperation {
	public IsFinalStateOperation( boolean initialValue ) {
		super( initialValue );
		//this.putValue( javax.swing.Action.NAME, "is constant" );
	}
	public void performStateChange( zoot.BooleanStateContext booleanStateContext ) {
		booleanStateContext.cancel();
	}
}
/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractCreateFieldPane extends CreateDeclarationWithDeclaringTypePane< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > {
	private zoot.ZCheckBox isFinalCheckBox;
	
	protected abstract boolean getIsFinalInitialValue();
	protected abstract boolean getIsFinalEnabled();
	
	@Override
	protected java.awt.Component createIsFinalComponent() {
		this.isFinalCheckBox = new zoot.ZCheckBox( new IsFinalStateOperation( this.getIsFinalInitialValue() ) );
		this.isFinalCheckBox.setOpaque( false );
		this.isFinalCheckBox.setEnabled( this.getIsFinalEnabled() );
		return this.isFinalCheckBox;
	}
	
	public AbstractCreateFieldPane( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType ) {
		super( declaringType );
		this.setBackground( getIDE().getFieldColor() );
	}
	@Override
	protected final String getDeclarationText() {
		return "Property";
	}
	protected abstract edu.cmu.cs.dennisc.alice.ast.Expression getInitializer();
	protected abstract edu.cmu.cs.dennisc.alice.ast.AbstractType getValueType();
	@Override
	protected final edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice getActualInputValue() {
		edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice rv = new edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice( this.getDeclarationName(), this.getValueType(), this.getInitializer() );
		rv.finalVolatileOrNeither.setValue( edu.cmu.cs.dennisc.alice.ast.FieldModifierFinalVolatileOrNeither.FINAL );
		return rv;
	}
}
