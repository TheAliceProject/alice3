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
public class FieldReflectionProxy extends MemberReflectionProxy {
	private java.lang.reflect.Field fld;
	private String name;

	public FieldReflectionProxy( ClassReflectionProxy declaringClassReflectionProxy, String name ) {
		super( declaringClassReflectionProxy );
		this.name = name;
	}
	public FieldReflectionProxy( java.lang.reflect.Field fld ) {
		super( fld.getDeclaringClass() );
		this.fld = fld;
		this.name = this.fld.getName();
		this.isAttempted = true;
	}

	@Override
	public int hashCode() {
		return super.hashCode() ^ this.name.hashCode();
	}
	@Override
	public boolean equals( Object o ) {
		FieldReflectionProxy other = edu.cmu.cs.dennisc.lang.ClassUtilities.getInstance( o, FieldReflectionProxy.class );
		if( other != null ) {
			return super.equals( other ) && this.name.equals( other.name );
		} else {
			return false;
		}
	}

	public String getName() {
		return this.name;
	}
	public java.lang.reflect.Field getFld() {
		if( this.isAttempted ) {
			//pass
		} else {
			this.fld = this.getDeclaringClassReflectionProxy().getFld( name );
			this.isAttempted = true;
		}
		return this.fld;
	}
}
