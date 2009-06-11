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

package edu.cmu.cs.dennisc.alice.virtualmachine;

import edu.cmu.cs.dennisc.alice.ast.*;

//todo: rename
/**
 * @author Dennis Cosgrove
 */
public class InstanceInAlice {
	private java.util.Map< FieldDeclaredInAlice, Object > m_map = new java.util.HashMap< FieldDeclaredInAlice, Object >();
	private Object m_instanceInJava;
	private AbstractType m_type;
//	public InstanceInAlice( VirtualMachine vm, ConstructorDeclaredInAlice constructor, Object[] arguments ) {
//		
//		
//		//todo
//
//		
//		m_type = constructor.getDeclaringType();
//		assert m_type != null;
//		assert m_type instanceof TypeDeclaredInAlice;
//		assert arguments.length == 0;
//		AbstractType t = m_type;
//		while( t instanceof TypeDeclaredInAlice ) {
//			for( AbstractField field : t.getDeclaredFields() ) {
//				assert field instanceof FieldDeclaredInAlice;
//				FieldDeclaredInAlice fieldDeclaredInAlice = (FieldDeclaredInAlice)field;
//				set( fieldDeclaredInAlice, vm.evaluate( fieldDeclaredInAlice.initializer.getValue() ) );
//			}
//			t = t.getSuperType();
//		}
//		
//		assert t instanceof TypeDeclaredInJava;
//		TypeDeclaredInJava typeDeclaredInJava = (TypeDeclaredInJava)t;
//
//		
//		//todo
//		
//		//return edu.cmu.cs.dennisc.reflect.ReflectionUtilities.newInstance( m_cls, parameterClses, arguments );
//		
//		m_instanceInJava = edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.newInstance( typeDeclaredInJava.getCls() );
//	}
	public void initialize( VirtualMachine vm, ConstructorDeclaredInAlice constructor, Object[] arguments ) {
		m_type = constructor.getDeclaringType();
		assert m_type != null;
		assert m_type instanceof TypeDeclaredInAlice;
		assert arguments.length == 0;
		AbstractType t = m_type;
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
		
		m_instanceInJava = edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.newInstance( typeDeclaredInJava.getCls() );
	}
	public AbstractType getType() {
		return m_type;
	}
	public Object getInstanceInJava() {
		return m_instanceInJava;
	}
	public Object get( FieldDeclaredInAlice field ) {
		return m_map.get( (FieldDeclaredInAlice)field );
	}
	public void set( FieldDeclaredInAlice field, Object value ) {
		m_map.put( (FieldDeclaredInAlice)field, value );
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append( InstanceInAlice.class.getName() );
		sb.append( "[type=" );
		sb.append( m_type );
		sb.append( ", instanceInJava=" );
		sb.append( m_instanceInJava );
		sb.append( " ]" );
		return sb.toString();
	}
}
