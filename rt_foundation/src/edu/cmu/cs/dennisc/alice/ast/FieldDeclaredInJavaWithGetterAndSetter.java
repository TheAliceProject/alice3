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
	private static java.util.Map< MethodReflectionProxy, FieldDeclaredInJavaWithGetterAndSetter > s_map = new java.util.HashMap< MethodReflectionProxy, FieldDeclaredInJavaWithGetterAndSetter >();

	private MethodReflectionProxy getterReflectionProxy;
	private MethodReflectionProxy setterReflectionProxy;
	private java.util.ArrayList< ParameterDeclaredInJavaMethod > parameters;
	private java.lang.annotation.Annotation[] parameterAnnotations0;

	public static FieldDeclaredInJavaWithGetterAndSetter get( MethodReflectionProxy getterReflectionProxy, MethodReflectionProxy setterReflectionProxy ) {
		if( setterReflectionProxy != null ) {
			FieldDeclaredInJavaWithGetterAndSetter rv = s_map.get( setterReflectionProxy );
			if( rv != null ) {
				//pass
			} else {
				rv = new FieldDeclaredInJavaWithGetterAndSetter( getterReflectionProxy, setterReflectionProxy );
				s_map.put( setterReflectionProxy, rv );
			}
			return rv;
		} else {
			return null;
		}
	}
	public static FieldDeclaredInJavaWithGetterAndSetter get( java.lang.reflect.Method getter, java.lang.reflect.Method setter ) {
		return get( new MethodReflectionProxy( getter ), new MethodReflectionProxy( setter ) );
	}

	private FieldDeclaredInJavaWithGetterAndSetter( MethodReflectionProxy getterReflectionProxy, MethodReflectionProxy setterReflectionProxy ) {
		this.getterReflectionProxy = getterReflectionProxy;
		this.setterReflectionProxy = setterReflectionProxy;

		java.lang.annotation.Annotation[][] parameterAnnotations = this.setterReflectionProxy.getParameterAnnotations();
		this.parameterAnnotations0 = parameterAnnotations[ 0 ];
	}

	public MethodReflectionProxy getGetterReflectionProxy() {
		return this.getterReflectionProxy;
	}
	public MethodReflectionProxy getSetterReflectionProxy() {
		return this.setterReflectionProxy;
	}
	
	//	private java.lang.reflect.Method m_gttr;
	//	private java.lang.reflect.Method m_sttr;
	//	private java.lang.annotation.Annotation[] m_sttrParameterAnnotations;
	//
	//	private FieldDeclaredInJavaWithGetterAndSetter( java.lang.reflect.Method gttr, java.lang.reflect.Method sttr, java.lang.annotation.Annotation[] sttrParameterAnnotations ) {
	//		m_gttr = gttr;
	//		m_sttr = sttr;
	//		m_sttrParameterAnnotations = sttrParameterAnnotations;
	//	}
	//	//todo: reduce visibility?
	//	public java.lang.reflect.Method getGttr() {
	//		return m_gttr;
	//	}
	//	//todo: reduce visibility?
	//	public java.lang.reflect.Method getSttr() {
	//		return m_sttr;
	//	}

	@Override
	public AbstractType getDeclaringType() {
		return TypeDeclaredInJava.get( this.setterReflectionProxy.getDeclaringClassReflectionProxy() );
	}

	@Override
	public edu.cmu.cs.dennisc.alice.annotations.Visibility getVisibility() {
		java.lang.reflect.Method gttr = this.getterReflectionProxy.getReification();
		if( gttr != null ) {
			if( gttr.isAnnotationPresent( edu.cmu.cs.dennisc.alice.annotations.PropertyGetterTemplate.class ) ) {
				edu.cmu.cs.dennisc.alice.annotations.PropertyGetterTemplate propertyFieldTemplate = gttr.getAnnotation( edu.cmu.cs.dennisc.alice.annotations.PropertyGetterTemplate.class );
				return propertyFieldTemplate.visibility();
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	@Override
	public String getName() {
		java.lang.reflect.Method gttr = this.getterReflectionProxy.getReification();
		assert gttr != null;
		return edu.cmu.cs.dennisc.property.PropertyUtilities.getPropertyNameForGetter( gttr );
	}
	@Override
	public AbstractType getValueType() {
		java.lang.reflect.Method gttr = this.getterReflectionProxy.getReification();
		assert gttr != null;
		return TypeDeclaredInJava.get( gttr.getReturnType() );
	}
	@Override
	public AbstractType getDesiredValueType() {
		if( this.parameterAnnotations0 != null ) {
			for( java.lang.annotation.Annotation annotation : parameterAnnotations0 ) {
					if( annotation instanceof edu.cmu.cs.dennisc.alice.annotations.ParameterTemplate ) {
					edu.cmu.cs.dennisc.alice.annotations.ParameterTemplate parameterTemplate = (edu.cmu.cs.dennisc.alice.annotations.ParameterTemplate)annotation;
					return TypeDeclaredInJava.get( parameterTemplate.preferredArgumentClass() );
				}
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
		java.lang.reflect.Method gttr = this.getterReflectionProxy.getReification();
		assert gttr != null;
		return Access.get( gttr.getModifiers() );
	}
	@Override
	public boolean isStatic() {
		java.lang.reflect.Method gttr = this.getterReflectionProxy.getReification();
		assert gttr != null;
		return java.lang.reflect.Modifier.isStatic( gttr.getModifiers() );
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
	public boolean isEquivalentTo( Object o ) {
		FieldDeclaredInJavaWithGetterAndSetter other = edu.cmu.cs.dennisc.lang.ClassUtilities.getInstance( o, FieldDeclaredInJavaWithGetterAndSetter.class );
		if( other != null ) {
			return this.getterReflectionProxy.equals( other.getterReflectionProxy ) && this.setterReflectionProxy.equals( other.setterReflectionProxy );
		} else {
			return false;
		}
	}
}
