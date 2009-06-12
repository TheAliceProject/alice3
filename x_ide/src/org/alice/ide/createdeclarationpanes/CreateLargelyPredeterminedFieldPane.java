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
public abstract class CreateLargelyPredeterminedFieldPane extends org.alice.ide.createdeclarationpanes.AbstractCreateFieldPane {
	private edu.cmu.cs.dennisc.alice.ast.AbstractType valueType;
	private edu.cmu.cs.dennisc.alice.ast.Expression initializer;

	public CreateLargelyPredeterminedFieldPane( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType, Class< ? > cls, edu.cmu.cs.dennisc.alice.ast.AbstractType valueType ) {
		super( declaringType );
		if( cls != null ) {
			assert valueType == null;
			edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava typeDeclaredInJava = edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( cls );
			this.valueType = this.getTypeDeclaredInAliceFor( typeDeclaredInJava );
		} else {
			assert valueType != null;
			this.valueType = valueType;
		}
		this.initializer = org.alice.ide.ast.NodeUtilities.createInstanceCreation( this.valueType );
	}
	
	protected edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice getTypeDeclaredInAliceFor( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava typeDeclaredInJava ) {
		return getIDE().getTypeDeclaredInAliceFor( typeDeclaredInJava );
	}

	@Override
	protected String getDefaultNameText() {
		return this.getIDE().getPotentialInstanceNameFor( this.getDeclaringType(), this.valueType );
	}

	@Override
	protected edu.cmu.cs.dennisc.alice.ast.AbstractType getValueType() {
		return this.valueType;
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.Expression getInitializer() {
		return this.initializer;
	}

	@Override
	protected boolean isEditableValueTypeComponentDesired() {
		return false;
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
	protected boolean isReassignable() {
		return false;
	}
	@Override
	protected boolean isIsReassignableComponentEnabled() {
		return false;
	}
	@Override
	protected boolean getIsReassignableInitialState() {
		return false;
	}
	
	@Override
	protected java.awt.Component createValueTypeComponent() {
		swing.LineAxisPane valueTypeLine = new swing.LineAxisPane();
		valueTypeLine.add( new org.alice.ide.common.TypeComponent( CreateLargelyPredeterminedFieldPane.this.valueType ) );
		if( CreateLargelyPredeterminedFieldPane.this.valueType instanceof edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice ) {
			valueTypeLine.add( zoot.ZLabel.acquire( " which extends ", zoot.font.ZTextPosture.OBLIQUE, zoot.font.ZTextWeight.LIGHT ) );
			valueTypeLine.add( new org.alice.ide.common.TypeComponent( CreateLargelyPredeterminedFieldPane.this.valueType.getSuperType() ) );
//			valueTypeLine.add( zoot.ZLabel.acquire( " ) ", zoot.font.ZTextPosture.OBLIQUE, zoot.font.ZTextWeight.LIGHT ) );
		}
		return valueTypeLine;
	}
	@Override
	protected java.awt.Component createInitializerComponent() {
		return new swing.LineAxisPane( getIDE().getPreviewFactory().createExpressionPane( this.getInitializer() ) );
	}
}
