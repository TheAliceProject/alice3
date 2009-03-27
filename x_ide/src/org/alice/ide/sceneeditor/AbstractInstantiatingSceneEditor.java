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
package org.alice.ide.sceneeditor;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractInstantiatingSceneEditor extends AbstractSceneEditor {
	private java.util.Map< edu.cmu.cs.dennisc.alice.ast.AbstractField, Object > mapFieldToInstance = new java.util.HashMap< edu.cmu.cs.dennisc.alice.ast.AbstractField, Object >();
	private java.util.Map< Object, edu.cmu.cs.dennisc.alice.ast.AbstractField > mapInstanceToField = new java.util.HashMap< Object, edu.cmu.cs.dennisc.alice.ast.AbstractField >();
	private java.util.Map< Object, edu.cmu.cs.dennisc.alice.ast.AbstractField > mapInstanceInJavaToField = new java.util.HashMap< Object, edu.cmu.cs.dennisc.alice.ast.AbstractField >();

	protected void putFieldForInstanceInJava( Object instanceInJava, edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		this.mapInstanceInJavaToField.put( instanceInJava, field );
	}

	protected void putInstanceForField( edu.cmu.cs.dennisc.alice.ast.AbstractField field, Object instance ) {
		this.mapFieldToInstance.put( field, instance );
		this.mapInstanceToField.put( instance, field );
		Object instanceInJava;
		if( instance instanceof edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice ) {
			edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice instanceInAlice = (edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice)instance;
			instanceInJava = instanceInAlice.getInstanceInJava();
		} else {
			instanceInJava = instance;
		}
		this.putFieldForInstanceInJava( instanceInJava, field );
	}

	protected Object getInstanceForField( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		return this.mapFieldToInstance.get( field );
	}
	protected Object getInstanceInJavaForField( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		Object rv;
		Object instance = this.mapFieldToInstance.get( field );
		if( instance instanceof edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice ) {
			edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice instanceInAlice = (edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice)instance;
			rv = instanceInAlice.getInstanceInJava();
		} else {
			rv = instance;
		}
		return rv;
	}
	protected <E> E getInstanceInJavaForField( edu.cmu.cs.dennisc.alice.ast.AbstractField field, Class<E> cls ) {
		return edu.cmu.cs.dennisc.lang.ClassUtilities.getInstance( getInstanceInJavaForField( field ), cls );
	}
	
	protected edu.cmu.cs.dennisc.alice.ast.AbstractField getFieldForInstance( Object instance ) {
		return this.mapInstanceToField.get( instance );
	}
	protected edu.cmu.cs.dennisc.alice.ast.AbstractField getFieldForInstanceInJava( Object instanceInJava ) {
		return this.mapInstanceInJavaToField.get( instanceInJava );
	}

	
	public void addFieldToSceneType( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field, Object instance ) {
		super.addFieldToSceneType( field );
		putInstanceForField( field, instance );
	}
	
	protected Object createScene( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice sceneField ) {
		getVM().setConstructorBodyExecutionDesired( false );
		try {
			edu.cmu.cs.dennisc.alice.ast.AbstractType sceneType = sceneField.getValueType();
			assert sceneType instanceof edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice;
			Object rv = getVM().createInstanceEntryPoint( sceneType );
			putInstanceForField( sceneField, rv );
			for( edu.cmu.cs.dennisc.alice.ast.AbstractField field : sceneType.getDeclaredFields() ) {
				Object value = this.getVM().getAccessForSceneEditor( field, rv );
				putInstanceForField( field, value );
			}
			return rv;
		} finally {
			getVM().setConstructorBodyExecutionDesired( true );
		}
	}
	@Override
	protected void setSceneField( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice sceneField ) {
		mapFieldToInstance.clear();
		super.setSceneField( sceneField );
		Object unused = this.createScene( sceneField );
	}
	
	protected Object getSceneInstanceInJava() {
		return this.getInstanceInJavaForField( this.getSceneField() );
	}
}
