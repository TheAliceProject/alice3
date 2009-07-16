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
	private static java.util.Map< MethodReflectionProxy, MethodDeclaredInJava > s_map = new java.util.HashMap< MethodReflectionProxy, MethodDeclaredInJava >();

	private MethodReflectionProxy methodReflectionProxy;
	private java.util.ArrayList< ParameterDeclaredInJavaMethod > parameters;

	public static MethodDeclaredInJava get( MethodReflectionProxy methodReflectionProxy ) {
		if( methodReflectionProxy != null ) {
			MethodDeclaredInJava rv = s_map.get( methodReflectionProxy );
			if( rv != null ) {
				//pass
			} else {
				rv = new MethodDeclaredInJava( methodReflectionProxy );
				s_map.put( methodReflectionProxy, rv );
			}
			return rv;
		} else {
			return null;
		}
	}
	public static MethodDeclaredInJava get( java.lang.reflect.Method mthd ) {
		return get( new MethodReflectionProxy( mthd ) );
	}
	public static MethodDeclaredInJava get( Class< ? > declaringCls, String name, Class< ? >... parameterClses ) {
		return get( edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.getMethod( declaringCls, name, parameterClses ) );
	}

	private MethodDeclaredInJava( MethodReflectionProxy methodReflectionProxy ) {
		this.methodReflectionProxy = methodReflectionProxy;
		ClassReflectionProxy[] classReflectionProxies = this.methodReflectionProxy.getParameterClassReflectionProxies();
		this.parameters = new java.util.ArrayList< ParameterDeclaredInJavaMethod >();
		this.parameters.ensureCapacity( classReflectionProxies.length );
		java.lang.annotation.Annotation[][] parameterAnnotations = this.methodReflectionProxy.getParameterAnnotations();
		for( int i = 0; i < classReflectionProxies.length; i++ ) {
			java.lang.annotation.Annotation[] annotationsI;
			if( parameterAnnotations != null ) {
				annotationsI = parameterAnnotations[ i ];
			} else {
				annotationsI = null;
			}
			this.parameters.add( new ParameterDeclaredInJavaMethod( this, i, annotationsI ) );
		}
	}

	public MethodReflectionProxy getMethodReflectionProxy() {
		return this.methodReflectionProxy;
	}

	@Override
	public String getName() {
		return this.methodReflectionProxy.getName();
	}
	@Override
	public edu.cmu.cs.dennisc.property.StringProperty getNamePropertyIfItExists() {
		return null;
	}

	@Override
	public AbstractType getReturnType() {
		java.lang.reflect.Method mthd = this.methodReflectionProxy.getMthd();
		assert mthd != null;
		return TypeDeclaredInJava.get( mthd.getReturnType() );
	}
	@Override
	public java.util.ArrayList< ? extends AbstractParameter > getParameters() {
		return this.parameters;
	}

	@Override
	public AbstractType getDeclaringType() {
		return TypeDeclaredInJava.get( this.methodReflectionProxy.getDeclaringClassReflectionProxy() );
	}
	@Override
	public edu.cmu.cs.dennisc.alice.annotations.Visibility getVisibility() {
		java.lang.reflect.Method mthd = this.methodReflectionProxy.getMthd();
		if( mthd != null ) {
			if( mthd.isAnnotationPresent( edu.cmu.cs.dennisc.alice.annotations.MethodTemplate.class ) ) {
				edu.cmu.cs.dennisc.alice.annotations.MethodTemplate mthdTemplate = mthd.getAnnotation( edu.cmu.cs.dennisc.alice.annotations.MethodTemplate.class );
				return mthdTemplate.visibility();
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public boolean isParameterInShortestChainedMethod( ParameterDeclaredInJavaMethod parameterDeclaredInJavaMethod ) {
		int index = parameterDeclaredInJavaMethod.getIndex();
		MethodDeclaredInJava methodDeclaredInJava = (MethodDeclaredInJava)getShortestInChain();
		return index < methodDeclaredInJava.getParameters().size();
	}

	private MethodDeclaredInJava nextLongerInChain = null;

	@Override
	public AbstractMember getNextLongerInChain() {
		return this.nextLongerInChain;
	}
	public void setNextLongerInChain( MethodDeclaredInJava nextLongerInChain ) {
		this.nextLongerInChain = nextLongerInChain;
	}

	private MethodDeclaredInJava nextShorterInChain = null;

	@Override
	public AbstractMember getNextShorterInChain() {
		return this.nextShorterInChain;
	}
	public void setNextShorterInChain( MethodDeclaredInJava nextShorterInChain ) {
		this.nextShorterInChain = nextShorterInChain;
	}

	@Override
	public Access getAccess() {
		java.lang.reflect.Method mthd = this.methodReflectionProxy.getMthd();
		assert mthd != null;
		return Access.get( mthd.getModifiers() );
	}
	@Override
	public boolean isStatic() {
		java.lang.reflect.Method mthd = this.methodReflectionProxy.getMthd();
		assert mthd != null;
		return java.lang.reflect.Modifier.isStatic( mthd.getModifiers() );
	}
	@Override
	public boolean isAbstract() {
		java.lang.reflect.Method mthd = this.methodReflectionProxy.getMthd();
		assert mthd != null;
		return java.lang.reflect.Modifier.isAbstract( mthd.getModifiers() );
	}
	@Override
	public boolean isFinal() {
		java.lang.reflect.Method mthd = this.methodReflectionProxy.getMthd();
		assert mthd != null;
		return java.lang.reflect.Modifier.isFinal( mthd.getModifiers() );
	}
	@Override
	public boolean isNative() {
		java.lang.reflect.Method mthd = this.methodReflectionProxy.getMthd();
		assert mthd != null;
		return java.lang.reflect.Modifier.isNative( mthd.getModifiers() );
	}
	@Override
	public boolean isSynchronized() {
		java.lang.reflect.Method mthd = this.methodReflectionProxy.getMthd();
		assert mthd != null;
		return java.lang.reflect.Modifier.isSynchronized( mthd.getModifiers() );
	}
	@Override
	public boolean isStrictFloatingPoint() {
		java.lang.reflect.Method mthd = this.methodReflectionProxy.getMthd();
		assert mthd != null;
		return java.lang.reflect.Modifier.isStrict( mthd.getModifiers() );
	}

	@Override
	public boolean isEquivalentTo( Object o ) {
		MethodDeclaredInJava other = edu.cmu.cs.dennisc.lang.ClassUtilities.getInstance( o, MethodDeclaredInJava.class );
		if( other != null ) {
			return this.methodReflectionProxy.equals( other.methodReflectionProxy );
		} else {
			return false;
		}
	}
}
