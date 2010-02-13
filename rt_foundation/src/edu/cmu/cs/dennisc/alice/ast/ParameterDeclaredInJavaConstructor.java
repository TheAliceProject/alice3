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

package edu.cmu.cs.dennisc.alice.ast;

//todo: name
/**
 * @author Dennis Cosgrove
 */
public class ParameterDeclaredInJavaConstructor extends ParameterDeclaredInJava {
	private static String getParameterNameFor( ConstructorReflectionProxy constructorReflectionProxy, int index ) {
		String rv = null;
		edu.cmu.cs.dennisc.alice.reflect.ClassInfo classInfo = edu.cmu.cs.dennisc.alice.reflect.ClassInfoManager.get( constructorReflectionProxy.getDeclaringClassReflectionProxy().getReification() );
		if( classInfo != null ) {
			edu.cmu.cs.dennisc.alice.reflect.ConstructorInfo constructorInfo = classInfo.lookupInfo( constructorReflectionProxy.getReification() );
			if( constructorInfo != null ) {
				String[] parameterNames = constructorInfo.getParameterNames();
				if( parameterNames != null ) {
					rv = parameterNames[ index ];
				}
			}
		}
		return rv;
	}
	private ConstructorDeclaredInJava m_constructor;
	private int m_index;
	private String m_name;
	private TypeDeclaredInJava m_valueType;
	/*package-private*/ ParameterDeclaredInJavaConstructor( ConstructorDeclaredInJava constructor, int index, java.lang.annotation.Annotation[] annotations ) {
		super( annotations );
		m_constructor = constructor;
		m_index = index;
		ConstructorReflectionProxy constructorReflectionProxy = constructor.getConstructorReflectionProxy();
		m_name = getParameterNameFor( constructorReflectionProxy, m_index );
		m_valueType = TypeDeclaredInJava.get( constructorReflectionProxy.getParameterClassReflectionProxies()[ m_index ] );
		assert m_valueType != null;
	}
	
	public ConstructorDeclaredInJava getConstructor() {
		return m_constructor;
	}
	public int getIndex() {
		return m_index;
	}
	
	@Override
	public String getName() {
		return m_name;
	}
	@Override
	public AbstractType getValueType() {
		return m_valueType;
	}

	@Override
	public boolean isEquivalentTo( Object other ) {
		if( other instanceof ParameterDeclaredInJavaConstructor ) {
			ParameterDeclaredInJavaConstructor otherPDIJC = (ParameterDeclaredInJavaConstructor)other;
			return m_constructor.equals( otherPDIJC.m_constructor ) && m_index == otherPDIJC.m_index && edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areEquivalent( m_name, otherPDIJC.m_name ) && m_valueType.equals( otherPDIJC.m_valueType );
		} else {
			return false;
		}
	}
}
