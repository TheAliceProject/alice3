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
public class MethodReflectionProxy extends InvocableReflectionProxy< java.lang.reflect.Method > {
	private String name;
	public MethodReflectionProxy( ClassReflectionProxy declaringClassReflectionProxy, String name, ClassReflectionProxy[] parameterClassReflectionProxies ) {
		super( declaringClassReflectionProxy, parameterClassReflectionProxies );
		this.name = name;
	}
	public MethodReflectionProxy( java.lang.reflect.Method mthd ) {
		super( mthd, mthd.getDeclaringClass(), mthd.getParameterTypes() );
		this.name = mthd.getName();
	}
	@Override
	public int hashCode() {
		int rv = super.hashCode();
		rv ^= this.name.hashCode();
		return rv;
	}
	@Override
	public boolean equals( Object o ) {
		MethodReflectionProxy other = edu.cmu.cs.dennisc.lang.ClassUtilities.getInstance( o, MethodReflectionProxy.class );
		if( other != null ) {
			if( super.equals( other ) ) {
				return this.name == other.name;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public String getName() {
		return this.name;
	}
	@Override
	protected java.lang.reflect.Method reify() {
		return this.getDeclaringClassReflectionProxy().getMthd( this.name, this.parameterClassReflectionProxies );
	}
	@Override
	protected java.lang.annotation.Annotation[][] getReifiedParameterAnnotations() {
		java.lang.reflect.Method mthd = this.getReification();
		if( mthd != null ) {
			return mthd.getParameterAnnotations();
		} else {
			return null;
		}
	}
	
}
