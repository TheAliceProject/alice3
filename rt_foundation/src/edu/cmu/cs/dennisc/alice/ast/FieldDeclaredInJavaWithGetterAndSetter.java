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
public class FieldDeclaredInJavaWithGetterAndSetter extends FieldDeclaredInJava {
	private java.lang.reflect.Method m_gttr;
	private java.lang.reflect.Method m_sttr;
	private java.lang.annotation.Annotation[] m_sttrParameterAnnotations;

	/*package-private*/ FieldDeclaredInJavaWithGetterAndSetter( java.lang.reflect.Method gttr, java.lang.reflect.Method sttr, java.lang.annotation.Annotation[] sttrParameterAnnotations ) {
		m_gttr = gttr;
		m_sttr = sttr;
		m_sttrParameterAnnotations = sttrParameterAnnotations;
	}
	//todo: reduce visibility?
	public java.lang.reflect.Method getGttr() {
		return m_gttr;
	}
	//todo: reduce visibility?
	public java.lang.reflect.Method getSttr() {
		return m_sttr;
	}

	@Override
	public AbstractType getDeclaringType() {
		return TypeDeclaredInJava.get( m_gttr.getDeclaringClass() );
	}

	@Override
	public edu.cmu.cs.dennisc.alice.annotations.Visibility getVisibility() {
		if( m_gttr.isAnnotationPresent( edu.cmu.cs.dennisc.alice.annotations.PropertyGetterTemplate.class ) ) {
			edu.cmu.cs.dennisc.alice.annotations.PropertyGetterTemplate propertyFieldTemplate = m_gttr.getAnnotation( edu.cmu.cs.dennisc.alice.annotations.PropertyGetterTemplate.class );
			return propertyFieldTemplate.visibility();
		} else {
			return null;
		}
	}

	@Override
	public String getName() {
		return edu.cmu.cs.dennisc.property.PropertyUtilities.getPropertyNameForGetter( m_gttr );
	}
	@Override
	public AbstractType getValueType() {
		return TypeDeclaredInJava.get( m_gttr.getReturnType() );
	}
	@Override
	public AbstractType getDesiredValueType() {
		for( java.lang.annotation.Annotation annotation : m_sttrParameterAnnotations ) {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( annotation );
			if( annotation instanceof edu.cmu.cs.dennisc.alice.annotations.ParameterTemplate ) {
				edu.cmu.cs.dennisc.alice.annotations.ParameterTemplate parameterTemplate = (edu.cmu.cs.dennisc.alice.annotations.ParameterTemplate)annotation;
				return TypeDeclaredInJava.get( parameterTemplate.preferredArgumentClass() );
			}
		}
		return getValueType();
	}
	
	//todo
	@Override
	public AbstractMember getNextLongerInChain() {
		return null;
	}
	//todo
	@Override
	public AbstractMember getNextShorterInChain() {
		return null;
	}
	
	@Override
	public Access getAccess() {
		return Access.get( m_gttr.getModifiers() );
	}	
	@Override
	public boolean isStatic() {
		return java.lang.reflect.Modifier.isStatic( m_gttr.getModifiers() );
	}
	@Override
	public boolean isFinal() {
		return false;
	}
	@Override
	public boolean isVolatile() {
		return false;
	}
	@Override
	public boolean isTransient() {
		return false;
	}

	@Override
	public boolean equals( Object other ) {
		if( other instanceof FieldDeclaredInJavaWithGetterAndSetter ) {
			return m_gttr.equals( ((FieldDeclaredInJavaWithGetterAndSetter)other).m_gttr ) && m_sttr.equals( ((FieldDeclaredInJavaWithGetterAndSetter)other).m_sttr );
		} else {
			return false;
		}
	}
}
