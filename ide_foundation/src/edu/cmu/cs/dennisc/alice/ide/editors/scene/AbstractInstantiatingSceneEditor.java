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
package edu.cmu.cs.dennisc.alice.ide.editors.scene;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractInstantiatingSceneEditor extends AbstractSceneEditor {
	private java.util.Map< edu.cmu.cs.dennisc.alice.ast.AbstractField, Object > mapFieldToInstance = new java.util.HashMap< edu.cmu.cs.dennisc.alice.ast.AbstractField, Object >();
	private void putInstanceForField( edu.cmu.cs.dennisc.alice.ast.AbstractField field, Object instanceInJava ) {
		mapFieldToInstance.put( field, instanceInJava );
	}
	public void addFieldToSceneType( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field, Object instanceInJava ) {
		super.addFieldToSceneType( field );
		putInstanceForField( field, instanceInJava );
	}
	
	@Override
	protected void setSceneField( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice sceneField ) {
		mapFieldToInstance.clear();
		super.setSceneField( sceneField );
		edu.cmu.cs.dennisc.alice.ast.AbstractType type = sceneField.getDeclaringType();
		assert type instanceof edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice;
		//edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice typeInAlice = (edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice)type;
		getVM().setConstructorBodyExecutionDesired( false );
		try {
			for( edu.cmu.cs.dennisc.alice.ast.AbstractField field : type.getDeclaredFields() ) {
				//todo?
				putInstanceForField( field, getVM().createInstanceEntryPoint( field.getDeclaringType() ) );
			}
		} finally {
			getVM().setConstructorBodyExecutionDesired( true );
		}
	}
}
