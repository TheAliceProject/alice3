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
	private java.util.Map< FieldDeclaredInAlice, Object > m_map = new java.util.HashMap< FieldDeclaredInAlice, Object >();
	private Object m_instanceInJava;
	private AbstractTypeDeclaredInAlice<?> m_type;
	public void initialize( VirtualMachine vm, ConstructorDeclaredInAlice constructor, Object[] arguments ) {
		m_type = constructor.getDeclaringType();
		assert m_type != null;
		assert m_type instanceof TypeDeclaredInAlice;
		assert arguments.length == 0;
		AbstractType<?,?,?> t = m_type;
		while( t instanceof TypeDeclaredInAlice ) {
			for( AbstractField field : t.getDeclaredFields() ) {
				assert field instanceof FieldDeclaredInAlice;
				FieldDeclaredInAlice fieldDeclaredInAlice = (FieldDeclaredInAlice)field;
				set( fieldDeclaredInAlice, vm.evaluate( fieldDeclaredInAlice.initializer.getValue() ) );
			}
			t = t.getSuperType();
		}
		assert t instanceof TypeDeclaredInJava;
		TypeDeclaredInJava typeDeclaredInJava = (TypeDeclaredInJava)t;

		
		//todo
		
		//return edu.cmu.cs.dennisc.reflect.ReflectionUtilities.newInstance( m_cls, parameterClses, arguments );
		ClassReflectionProxy classReflectionProxy = typeDeclaredInJava.getClassReflectionProxy();
		assert classReflectionProxy != null;
		Class<?> cls = classReflectionProxy.getReification();
		assert cls != null : classReflectionProxy.getName();
		m_instanceInJava = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.newInstance( cls );
	}
	
	@Deprecated
	public void EPIC_HACK_FOR_SCENE_EDITOR_setInstanceInJava( Object instanceInJava ) {
		m_instanceInJava = instanceInJava;
	}
	public AbstractTypeDeclaredInAlice<?> getType() {
		return m_type;
	}
	public Object getInstanceInJava() {
		return m_instanceInJava;
	}
	public <E> E getInstanceInJava( Class<E> cls ) {
		return edu.cmu.cs.dennisc.java.lang.ClassUtilities.getInstance( m_instanceInJava, cls );
	}
	public Object get( FieldDeclaredInAlice field ) {
		return m_map.get( field );
	}
	public void set( FieldDeclaredInAlice field, Object value ) {
		m_map.put( field, value );
	}

	@Override
	public String toString() {
		if( m_instanceInJava != null ) {
			return m_instanceInJava.toString();
		} else {
			return null;
		}
	}
}
