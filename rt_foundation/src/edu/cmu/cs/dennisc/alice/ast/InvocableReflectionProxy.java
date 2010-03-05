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
public abstract class InvocableReflectionProxy< E > extends MemberReflectionProxy< E > {
	protected ClassReflectionProxy[] parameterClassReflectionProxies;
	public InvocableReflectionProxy( ClassReflectionProxy declaringClassReflectionProxy, ClassReflectionProxy[] parameterClassReflectionProxies ) {
		super( declaringClassReflectionProxy );
		this.parameterClassReflectionProxies = parameterClassReflectionProxies;
	}
	public InvocableReflectionProxy( E e, Class<?> declaringCls, Class<?>[] parameterClses ) {
		super( e, declaringCls );
		this.parameterClassReflectionProxies = ClassReflectionProxy.create( parameterClses );
	}
	@Override
	protected int hashCodeNonReifiable() {
		int rv = super.hashCodeNonReifiable();
		for( ClassReflectionProxy parameterClassReflectionProxy : parameterClassReflectionProxies ) {
			rv = 37*rv + parameterClassReflectionProxy.hashCode();
		}
		return rv;
	}
	@Override
	protected boolean equalsInstanceOfSameClassButNonReifiable( edu.cmu.cs.dennisc.alice.ast.ReflectionProxy< ? > o ) {
		if( super.equalsInstanceOfSameClassButNonReifiable( o ) ) {
			InvocableReflectionProxy<E> other = (InvocableReflectionProxy<E>)o;
			if( this.parameterClassReflectionProxies.length == other.parameterClassReflectionProxies.length ) {
				for( int i=0; i<this.parameterClassReflectionProxies.length; i++ ) {
					if( edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areEquivalent( this.parameterClassReflectionProxies[ i ], other.parameterClassReflectionProxies[ i ] ) ) {
						//pass
					} else {
						return false;
					}
				}
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public ClassReflectionProxy[] getParameterClassReflectionProxies() {
		return this.parameterClassReflectionProxies;
	}
	
	protected abstract java.lang.annotation.Annotation[][] getReifiedParameterAnnotations();
	public final java.lang.annotation.Annotation[][] getParameterAnnotations() {
		java.lang.annotation.Annotation[][] rv = this.getReifiedParameterAnnotations();
		if( rv != null ) {
			//pass
		} else {
			rv = new java.lang.annotation.Annotation[ this.parameterClassReflectionProxies.length ][];
		}
		return rv;
	}
}
