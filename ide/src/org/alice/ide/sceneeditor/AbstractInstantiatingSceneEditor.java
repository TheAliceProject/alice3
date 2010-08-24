/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may 
 *    "Alice" appear in their name, without prior written permission of 
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software 
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is 
 *    contributed by Electronic Arts Inc. and may be used for personal, 
 *    non-commercial, and academic use only. Redistributions of any program 
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in 
 *    The Alice 3.0 Art Gallery License.
 * 
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A 
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO 
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.alice.ide.sceneeditor;

import edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice;
import edu.cmu.cs.dennisc.print.PrintUtilities;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractInstantiatingSceneEditor extends AbstractSceneEditor {
	private java.util.Map< edu.cmu.cs.dennisc.alice.ast.AbstractField, Object > mapFieldToInstance = new java.util.HashMap< edu.cmu.cs.dennisc.alice.ast.AbstractField, Object >();
	private java.util.Map< Object, edu.cmu.cs.dennisc.alice.ast.AbstractField > mapInstanceToField = new java.util.HashMap< Object, edu.cmu.cs.dennisc.alice.ast.AbstractField >();
	private java.util.Map< Object, edu.cmu.cs.dennisc.alice.ast.AbstractField > mapInstanceInJavaToField = new java.util.HashMap< Object, edu.cmu.cs.dennisc.alice.ast.AbstractField >();

	protected void putFieldForInstanceInJava( Object instanceInJava, edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		PrintUtilities.println("Adding "+instanceInJava.getClass()+":"+instanceInJava.hashCode()+" -> "+field.getName()+":"+field.hashCode());
		this.mapInstanceInJavaToField.put( instanceInJava, field );
	}

	protected void removeField( edu.cmu.cs.dennisc.alice.ast.AbstractField field )
	{
		Object instance = this.mapFieldToInstance.get(field);
		Object javaInstance = this.mapInstanceInJavaToField.get(field);
		this.mapFieldToInstance.remove(field);
		if (instance != null)
		{
			this.mapInstanceToField.remove(instance);
		}
		if (javaInstance != null)
		{
			this.mapInstanceInJavaToField.remove(javaInstance);
		}
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

	@Override
	public Object getInstanceInJavaForUndo( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field ) {
		if( field.getDeclaringType() == getIDE().getSceneType() ) {
			return this.getInstanceInJavaForField( field );
		} else {
			return null;
		}
	}
	protected Object getInstanceForField( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		return this.mapFieldToInstance.get( field );
	}
	@Override
	public void putInstanceForInitializingPendingField(FieldDeclaredInAlice field, Object instance) 
	{
		this.putInstanceForField(field, instance);
	}
	
	protected Object getInstanceInJavaForField( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		Object rv;
		Object instance = this.getInstanceForField( field );
		if( instance instanceof edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice ) {
			edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice instanceInAlice = (edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice)instance;
			rv = instanceInAlice.getInstanceInJava();
		} else {
			rv = instance;
		}
		return rv;
	}
	//todo: reduce visibility?
	public <E> E getInstanceInJavaForField( edu.cmu.cs.dennisc.alice.ast.AbstractField field, Class<E> cls ) {
		return edu.cmu.cs.dennisc.java.lang.ClassUtilities.getInstance( getInstanceInJavaForField( field ), cls );
	}
	
	protected edu.cmu.cs.dennisc.alice.ast.AbstractField getFieldForInstance( Object instance ) {
		return this.mapInstanceToField.get( instance );
	}
	protected edu.cmu.cs.dennisc.alice.ast.AbstractField getFieldForInstanceInJava( Object instanceInJava ) {
		return this.mapInstanceInJavaToField.get( instanceInJava );
	}
	protected Object createScene( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice sceneField ) {
		getVM().setConstructorBodyExecutionDesired( false );
		try {
			edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> sceneType = sceneField.getValueType();
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
