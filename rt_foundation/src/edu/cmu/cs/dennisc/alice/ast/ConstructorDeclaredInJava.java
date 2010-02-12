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
	private static java.util.Map< ConstructorReflectionProxy, ConstructorDeclaredInJava > s_map = new java.util.HashMap< ConstructorReflectionProxy, ConstructorDeclaredInJava >();

	private ConstructorReflectionProxy constructorReflectionProxy;
	private java.util.ArrayList< ParameterDeclaredInJavaConstructor > parameters;

	public static ConstructorDeclaredInJava get( ConstructorReflectionProxy constructorReflectionProxy ) {
		if( constructorReflectionProxy != null ) {
			ConstructorDeclaredInJava rv = s_map.get( constructorReflectionProxy );
			if( rv != null ) {
				//pass
			} else {
				rv = new ConstructorDeclaredInJava( constructorReflectionProxy );
				s_map.put( constructorReflectionProxy, rv );
			}
			return rv;
		} else {
			return null;
		}
	}
	public static ConstructorDeclaredInJava get( java.lang.reflect.Constructor< ? > cnstrctr ) {
		return get( new ConstructorReflectionProxy( cnstrctr ) );
	}
	public static ConstructorDeclaredInJava get( Class<?> declaringCls, Class<?>... parameterClses ) {
		return get( edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.getConstructor( declaringCls, parameterClses ) );
	}
	
	private ConstructorDeclaredInJava( ConstructorReflectionProxy constructorReflectionProxy ) {
		this.constructorReflectionProxy = constructorReflectionProxy;
		ClassReflectionProxy[] classReflectionProxies = this.constructorReflectionProxy.getParameterClassReflectionProxies();
		this.parameters = new java.util.ArrayList< ParameterDeclaredInJavaConstructor >();
		this.parameters.ensureCapacity( classReflectionProxies.length );
		java.lang.annotation.Annotation[][] parameterAnnotations = this.constructorReflectionProxy.getParameterAnnotations();
		for( int i = 0; i < classReflectionProxies.length; i++ ) {
			this.parameters.add( new ParameterDeclaredInJavaConstructor( this, i, parameterAnnotations[ i ] ) );
		}
	}
	
	public ConstructorReflectionProxy getConstructorReflectionProxy() {
		return this.constructorReflectionProxy;
	}
	
	@Override
	public AbstractType getDeclaringType() {
		return TypeDeclaredInJava.get( this.constructorReflectionProxy.getDeclaringClassReflectionProxy() );
	}
	@Override
	public java.util.ArrayList< ? extends AbstractParameter > getParameters() {
		return this.parameters;
	}
	@Override
	public edu.cmu.cs.dennisc.alice.annotations.Visibility getVisibility() {
		java.lang.reflect.Constructor< ? > cnstrctr = this.constructorReflectionProxy.getReification();
		if( cnstrctr != null ) {
			if( cnstrctr.isAnnotationPresent( edu.cmu.cs.dennisc.alice.annotations.ConstructorTemplate.class ) ) {
				//todo: investigate cast requirement
				edu.cmu.cs.dennisc.alice.annotations.ConstructorTemplate cnstrctrTemplate = cnstrctr.getAnnotation( edu.cmu.cs.dennisc.alice.annotations.ConstructorTemplate.class );
				return cnstrctrTemplate.visibility();
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	private ConstructorDeclaredInJava nextLongerInChain = null;

	public boolean isParameterInShortestChainedConstructor( ParameterDeclaredInJavaConstructor parameterDeclaredInJavaConstructor ) {
		int index = parameterDeclaredInJavaConstructor.getIndex();
		ConstructorDeclaredInJava constructorDeclaredInJava = (ConstructorDeclaredInJava)getShortestInChain();
		return index < constructorDeclaredInJava.getParameters().size();
	}
	
	@Override
	public AbstractMember getNextLongerInChain() {
		return this.nextLongerInChain;
	}
	public void setNextLongerInChain( ConstructorDeclaredInJava nextLongerInChain ) {
		this.nextLongerInChain = nextLongerInChain;
	}

	private ConstructorDeclaredInJava nextShorterInChain = null;

	@Override
	public AbstractMember getNextShorterInChain() {
		return this.nextShorterInChain;
	}
	public void setNextShorterInChain( ConstructorDeclaredInJava nextShorterInChain ) {
		this.nextShorterInChain = nextShorterInChain;
	}

	@Override
	public Access getAccess() {
		java.lang.reflect.Constructor< ? > cnstrctr = this.constructorReflectionProxy.getReification();
		if( cnstrctr != null ) {
			return Access.get( cnstrctr.getModifiers() );
		} else {
			return null;
		}
	}

	@Override
	public boolean isEquivalentTo( Object o ) {
		ConstructorDeclaredInJava other = edu.cmu.cs.dennisc.lang.ClassUtilities.getInstance( o, ConstructorDeclaredInJava.class );
		if( other != null ) {
			return this.constructorReflectionProxy.equals( other.constructorReflectionProxy );
		} else {
			return false;
		}
	}
}
