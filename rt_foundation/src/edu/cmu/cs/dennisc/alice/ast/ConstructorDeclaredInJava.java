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

/**
 * @author Dennis Cosgrove
 */
public class ConstructorDeclaredInJava extends AbstractConstructor {
	private java.lang.reflect.Constructor< ? > m_cnstrctr;
	private java.util.ArrayList< ParameterDeclaredInJavaConstructor > m_parameters;

	/*package-private*/ConstructorDeclaredInJava( java.lang.reflect.Constructor< ? > cnstrctr ) {
		m_cnstrctr = cnstrctr;
		Class< ? >[] clses = m_cnstrctr.getParameterTypes();
		m_parameters = new java.util.ArrayList< ParameterDeclaredInJavaConstructor >();
		m_parameters.ensureCapacity( clses.length );
		java.lang.annotation.Annotation[][] parameterAnnotations = m_cnstrctr.getParameterAnnotations();
		for( int i = 0; i < clses.length; i++ ) {
			m_parameters.add( new ParameterDeclaredInJavaConstructor( this, i, parameterAnnotations[ i ] ) );
		}
	}
	//todo: reduce visibility?
	public java.lang.reflect.Constructor< ? > getCnstrctr() {
		return m_cnstrctr;
	}

	@Override
	public java.util.ArrayList< ? extends AbstractParameter > getParameters() {
		return m_parameters;
	}

	@Override
	public AbstractType getDeclaringType() {
		return TypeDeclaredInJava.get( m_cnstrctr.getDeclaringClass() );
	}
	@Override
	public edu.cmu.cs.dennisc.alice.annotations.Visibility getVisibility() {
		if( m_cnstrctr.isAnnotationPresent( edu.cmu.cs.dennisc.alice.annotations.ConstructorTemplate.class ) ) {
			//todo: investigate cast requirement
			edu.cmu.cs.dennisc.alice.annotations.ConstructorTemplate cnstrctrTemplate = m_cnstrctr.getAnnotation( edu.cmu.cs.dennisc.alice.annotations.ConstructorTemplate.class );
			return cnstrctrTemplate.visibility();
		} else {
			return null;
		}
	}

	private ConstructorDeclaredInJava m_nextLongerInChain = null;

	@Override
	public AbstractMember getNextLongerInChain() {
		return m_nextLongerInChain;
	}
	public void setNextLongerInChain( ConstructorDeclaredInJava nextLongerInChain ) {
		m_nextLongerInChain = nextLongerInChain;
	}

	private ConstructorDeclaredInJava m_nextShorterInChain = null;

	@Override
	public AbstractMember getNextShorterInChain() {
		return m_nextShorterInChain;
	}
	public void setNextShorterInChain( ConstructorDeclaredInJava nextShorterInChain ) {
		m_nextShorterInChain = nextShorterInChain;
	}

	@Override
	public Access getAccess() {
		return Access.get( m_cnstrctr.getModifiers() );
	}

	@Override
	public boolean isEquivalentTo( Object other ) {
		if( other instanceof ConstructorDeclaredInJava ) {
			return m_cnstrctr.equals( ((ConstructorDeclaredInJava)other).m_cnstrctr );
		} else {
			return false;
		}
	}
}
