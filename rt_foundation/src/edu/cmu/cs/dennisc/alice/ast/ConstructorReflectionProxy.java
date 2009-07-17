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
public class ConstructorReflectionProxy extends InvocableReflectionProxy< java.lang.reflect.Constructor< ? > > {
	public ConstructorReflectionProxy( ClassReflectionProxy declaringClassReflectionProxy, ClassReflectionProxy[] parameterClassReflectionProxies ) {
		super( declaringClassReflectionProxy, parameterClassReflectionProxies );
	}
	public ConstructorReflectionProxy( java.lang.reflect.Constructor< ? > cnstrctr ) {
		super( cnstrctr, cnstrctr.getDeclaringClass(), cnstrctr.getParameterTypes() );
	}
	@Override
	protected java.lang.reflect.Constructor< ? > reify() {
		return this.getDeclaringClassReflectionProxy().getCnstrctr( this.parameterClassReflectionProxies );
	}
	@Override
	protected java.lang.annotation.Annotation[][] getReifiedParameterAnnotations() {
		java.lang.reflect.Constructor< ? > cnstrctr = this.getReification();
		if( cnstrctr != null ) {
			return cnstrctr.getParameterAnnotations();
		} else {
			return null;
		}
	}
}
