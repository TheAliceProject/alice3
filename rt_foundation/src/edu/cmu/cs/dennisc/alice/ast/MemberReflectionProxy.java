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
public abstract class MemberReflectionProxy extends ReflectionProxy {
	private ClassReflectionProxy declaringClassReflectionProxy;
	public MemberReflectionProxy( ClassReflectionProxy declaringClassReflectionProxy ) {
		this.declaringClassReflectionProxy = declaringClassReflectionProxy;
	}
	public MemberReflectionProxy( Class<?> declaringCls ) {
		this( new ClassReflectionProxy( declaringCls ) );
	}
	@Override
	public int hashCode() {
		return this.declaringClassReflectionProxy.hashCode();
	}
	@Override
	public boolean equals( Object o ) {
		MemberReflectionProxy other = edu.cmu.cs.dennisc.lang.ClassUtilities.getInstance( o, MemberReflectionProxy.class );
		if( other != null ) {
			return this.declaringClassReflectionProxy.equals( other.declaringClassReflectionProxy );
		} else {
			return false;
		}
	}
	public ClassReflectionProxy getDeclaringClassReflectionProxy() {
		return this.declaringClassReflectionProxy;
	}
}
