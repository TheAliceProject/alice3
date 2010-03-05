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
public final class PackageReflectionProxy extends ReflectionProxy< Package > {
	private String name;
	public PackageReflectionProxy( String name ) {
		this.name = name;
	}
	public PackageReflectionProxy( Package pckg ) {
		super( pckg );
		this.name = pckg.getName();
	}
	@Override
	protected int hashCodeNonReifiable() {
		return this.name.hashCode();
	}
	@Override
	protected boolean equalsInstanceOfSameClassButNonReifiable( ReflectionProxy< ? > o ) {
		PackageReflectionProxy other = (PackageReflectionProxy)o;
		return this.name != null ? this.name.equals( other.name ) : other.name == null;
	}
	public String getName() {
		return this.name;
	}
	@Override
	protected Package reify() {
		return Package.getPackage( this.name );
	}
}
