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
public class MethodDeclaredInJava extends AbstractMethod {
	private java.lang.reflect.Method m_mthd;
	private java.util.ArrayList< ParameterDeclaredInJavaMethod > m_parameters;

	/*package-private*/ MethodDeclaredInJava( java.lang.reflect.Method mthd ) {
		m_mthd = mthd;
		Class<?>[] clses = m_mthd.getParameterTypes();
		m_parameters = new java.util.ArrayList< ParameterDeclaredInJavaMethod >();
		m_parameters.ensureCapacity( clses.length );
		java.lang.annotation.Annotation[][] parameterAnnotations = m_mthd.getParameterAnnotations();
		for( int i = 0; i < clses.length; i++ ) {
			m_parameters.add( new ParameterDeclaredInJavaMethod( this, i, parameterAnnotations[ i ] ) );
		}
	}
	//todo: reduce visibility?
	public java.lang.reflect.Method getMthd() {
		return m_mthd;
	}

	@Override
	public String getName() {
		return m_mthd.getName();
	}
	@Override
	public edu.cmu.cs.dennisc.property.StringProperty getNamePropertyIfItExists() {
		return null;
	}
	
	@Override
	public AbstractType getReturnType() {
		return TypeDeclaredInJava.get( m_mthd.getReturnType() );
	}
	@Override
	public java.util.ArrayList< ? extends AbstractParameter > getParameters() {
		return m_parameters;
	}

	@Override
	public AbstractType getDeclaringType() {
		return TypeDeclaredInJava.get( m_mthd.getDeclaringClass() );
	}
	@Override
	public edu.cmu.cs.dennisc.alice.annotations.Visibility getVisibility() {
		if( m_mthd.isAnnotationPresent( edu.cmu.cs.dennisc.alice.annotations.MethodTemplate.class ) ) {
			edu.cmu.cs.dennisc.alice.annotations.MethodTemplate mthdTemplate = m_mthd.getAnnotation( edu.cmu.cs.dennisc.alice.annotations.MethodTemplate.class );
			return mthdTemplate.visibility();
		} else {
			return null;
		}
	}
	
	public boolean isParameterInShortestChainedMethod( ParameterDeclaredInJavaMethod parameterDeclaredInJavaMethod ) {
		int index = parameterDeclaredInJavaMethod.getIndex();
		MethodDeclaredInJava methodDeclaredInJava = (MethodDeclaredInJava)getShortestInChain();
		return index < methodDeclaredInJava.getParameters().size();
	}

	private MethodDeclaredInJava m_nextLongerInChain = null;

	@Override
	public AbstractMember getNextLongerInChain() {
		return m_nextLongerInChain;
	}
	public void setNextLongerInChain( MethodDeclaredInJava nextLongerInChain ) {
		m_nextLongerInChain = nextLongerInChain;
	}

	private MethodDeclaredInJava m_nextShorterInChain = null;

	@Override
	public AbstractMember getNextShorterInChain() {
		return m_nextShorterInChain;
	}
	public void setNextShorterInChain( MethodDeclaredInJava nextShorterInChain ) {
		m_nextShorterInChain = nextShorterInChain;
	}

	@Override
	public Access getAccess() {
		return Access.get( m_mthd.getModifiers() );
	}
	@Override
	public boolean isStatic() {
		return java.lang.reflect.Modifier.isStatic( m_mthd.getModifiers() );
	}
	@Override
	public boolean isAbstract() {
		return java.lang.reflect.Modifier.isAbstract( m_mthd.getModifiers() );
	}
	@Override
	public boolean isFinal() {
		return java.lang.reflect.Modifier.isFinal( m_mthd.getModifiers() );
	}
	@Override
	public boolean isNative() {
		return java.lang.reflect.Modifier.isNative( m_mthd.getModifiers() );
	}
	@Override
	public boolean isSynchronized() {
		return java.lang.reflect.Modifier.isSynchronized( m_mthd.getModifiers() );
	}
	@Override
	public boolean isStrictFloatingPoint() {
		return java.lang.reflect.Modifier.isStrict( m_mthd.getModifiers() );
	}
	
	@Override
	public boolean isEquivalentTo( Object other ) {
		if( other instanceof MethodDeclaredInJava ) {
			return m_mthd.equals( ((MethodDeclaredInJava)other).m_mthd );
		} else {
			return false;
		}
	}
}
