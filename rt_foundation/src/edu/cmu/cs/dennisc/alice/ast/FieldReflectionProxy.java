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
public final class FieldReflectionProxy extends MemberReflectionProxy< java.lang.reflect.Field > {
	private String name;
	public FieldReflectionProxy( ClassReflectionProxy declaringClassReflectionProxy, String name ) {
		super( declaringClassReflectionProxy );
		this.name = name;
	}
	public FieldReflectionProxy( java.lang.reflect.Field fld ) {
		super( fld, fld.getDeclaringClass() );
		this.name = fld.getName();
	}

	@Override
	protected int hashCodeNonReifiable() {
		int rv = super.hashCodeNonReifiable();
		rv = 37*rv + this.name.hashCode();
		return rv;
	}
	@Override
	protected boolean equalsInstanceOfSameClassButNonReifiable( edu.cmu.cs.dennisc.alice.ast.ReflectionProxy< ? > o ) {
		if( super.equalsInstanceOfSameClassButNonReifiable( o ) ) {
			FieldReflectionProxy other = (FieldReflectionProxy)o;
			return this.name != null ? this.name.equals( other.name ) : other.name == null;
		} else {
			return false;
		}
	}
	
	public String getName() {
		return this.name;
	}
	@Override
	protected java.lang.reflect.Field reify() {
		Class< ? > cls = this.getDeclaringClassReflectionProxy().getReification();
		if( cls != null ) {
			try {
				return cls.getField( name );
			} catch( NoSuchFieldException nsfe ) {
				return null;
			}
		} else {
			return null;
		}
	}
}
