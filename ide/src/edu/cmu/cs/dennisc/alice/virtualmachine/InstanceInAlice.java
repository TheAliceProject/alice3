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

package edu.cmu.cs.dennisc.alice.virtualmachine;

import edu.cmu.cs.dennisc.alice.ast.*;

/**
 * @author Dennis Cosgrove
 */
public class InstanceInAlice {
	public static InstanceInAlice createInstance( VirtualMachine vm, ConstructorDeclaredInAlice constructor, Object[] arguments ) {
		return new InstanceInAlice( vm, constructor, arguments, new java.util.HashMap< FieldDeclaredInAlice, Object >(), null );
	}
	public static InstanceInAlice createInstanceWithInverseMap( VirtualMachine vm, ConstructorDeclaredInAlice constructor, Object[] arguments ) {
		return new InstanceInAlice( vm, constructor, arguments, new java.util.HashMap< FieldDeclaredInAlice, Object >(), new java.util.HashMap< Object, FieldDeclaredInAlice >() );
	}
	
	private final Object nextInstance;
	private final AbstractTypeDeclaredInAlice<?> type;
	private final java.util.Map< FieldDeclaredInAlice, Object > fieldMap;
	private final java.util.Map< Object, FieldDeclaredInAlice > inverseFieldMap;
	private InstanceInAlice( VirtualMachine vm, ConstructorDeclaredInAlice constructor, Object[] arguments, java.util.Map< FieldDeclaredInAlice, Object > fieldMap, java.util.Map< Object, FieldDeclaredInAlice > inverseFieldMap ) {
		this.type = constructor.getDeclaringType();
		this.fieldMap = fieldMap;
		this.inverseFieldMap = inverseFieldMap;
		
		ConstructorBlockStatement constructorBlockStatement = constructor.body.getValue();
		ConstructorInvocationStatement constructorInvocationStatement = constructorBlockStatement.constructorInvocationStatement.getValue();
		AbstractConstructor nextConstructor = constructorInvocationStatement.contructor.getValue();
		java.util.Map<edu.cmu.cs.dennisc.alice.ast.AbstractParameter,Object> stackMap = new java.util.HashMap< edu.cmu.cs.dennisc.alice.ast.AbstractParameter, Object >();
		for( int i=0; i<arguments.length; i++ ) {
			stackMap.put( constructor.parameters.get( i ), arguments[ i ] );
		}
		edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type = (edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice)constructor.getDeclaringType();
		vm.pushConstructorFrame( type, stackMap );
		Object[] nextArguments = vm.evaluateArguments( nextConstructor.getParameters(), constructorInvocationStatement.arguments );
		if( nextConstructor.isDeclaredInAlice() ) {
			this.nextInstance = new InstanceInAlice( vm, (ConstructorDeclaredInAlice)nextConstructor, nextArguments, fieldMap, inverseFieldMap );
		} else {
			ConstructorDeclaredInJava nextConstructorDeclaredInJava = (ConstructorDeclaredInJava)nextConstructor;
			ConstructorReflectionProxy constructorReflectionProxy =  nextConstructorDeclaredInJava.getConstructorReflectionProxy();
			java.lang.reflect.Constructor<?> cnstrctr = constructorReflectionProxy.getReification();
			assert cnstrctr != null : constructorReflectionProxy.getDeclaringClassReflectionProxy().getName();
			this.nextInstance = vm.createInstance( this.type, this, cnstrctr, nextArguments );
		}
		vm.setConstructorFrameInstanceInAlice( this );
		for( AbstractField field : this.type.getDeclaredFields() ) {
			assert field instanceof FieldDeclaredInAlice;
			FieldDeclaredInAlice fieldDeclaredInAlice = (FieldDeclaredInAlice)field;
			set( fieldDeclaredInAlice, vm.evaluate( fieldDeclaredInAlice.initializer.getValue() ) );
		}
		if( vm.isConstructorBodyExecutionDesired() ) {
			try {
				vm.executeBlockStatement( constructorBlockStatement );
			} catch( ReturnException re ) {
				throw new RuntimeException( re );
			}
		}
		vm.popFrame();
	}

	public AbstractTypeDeclaredInAlice<?> getType() {
		return this.type;
	}
	public Object getInstanceInJava() {
		if( this.nextInstance instanceof InstanceInAlice ) {
			return ((InstanceInAlice)this.nextInstance).getInstanceInJava();
		} else {
			return this.nextInstance;
		}
	}
	public <E> E getInstanceInJava( Class<E> cls ) {
		return edu.cmu.cs.dennisc.java.lang.ClassUtilities.getInstance( this.getInstanceInJava(), cls );
	}
	public Object get( FieldDeclaredInAlice field ) {
		return this.fieldMap.get( field );
	}
	public void set( FieldDeclaredInAlice field, Object value ) {
		this.fieldMap.put( field, value );
		if( this.inverseFieldMap != null ) {
			this.inverseFieldMap.put( value, field );
		}
	}
	
	public FieldDeclaredInAlice ACCEPTABLE_HACK_FOR_SCENE_EDITOR_getField( Object key ) {
		assert this.inverseFieldMap != null;
		return this.inverseFieldMap.get( key );
	}

	@Override
	public String toString() {
		if( this.nextInstance != null ) {
			return this.nextInstance.toString();
		} else {
			return null;
		}
	}
}
