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
public abstract class AbstractMethod extends AbstractCode {
	//public ObjectProperty< Boolean > isEntryPoint = new ObjectProperty< Boolean >( this );
	
	public abstract boolean isStatic();
	public abstract boolean isAbstract();
	public abstract boolean isFinal();
	public abstract boolean isNative();
	public abstract boolean isSynchronized();
	public abstract boolean isStrictFloatingPoint();
	public abstract AbstractType getReturnType();
	
	public boolean isOverride() {
		java.util.ArrayList< ? extends AbstractParameter > parameters = this.getParameters();
		final int N = parameters.size();
		AbstractType[] parameterTypes = new AbstractType[ N ];
		for( int i=0; i<N; i++ ) {
			parameterTypes[ i ] = parameters.get( i ).getValueType();
		}
		return this.getDeclaringType().getSuperType().findMethod( this.getName(), parameterTypes ) != null;
	}
	
	public boolean isFunction() {
		return getReturnType() != TypeDeclaredInJava.VOID_TYPE;
	}
	public boolean isProcedure() {
		return isFunction() == false;
	}
}
