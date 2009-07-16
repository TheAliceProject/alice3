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
public class AnonymousInnerTypeDeclaredInAlice extends AbstractTypeDeclaredInAlice {
	private java.util.ArrayList< AbstractConstructor > constructors = new java.util.ArrayList< AbstractConstructor >();
	public AnonymousInnerTypeDeclaredInAlice() {
	}
	public AnonymousInnerTypeDeclaredInAlice( AbstractType superType, MethodDeclaredInAlice[] methods, FieldDeclaredInAlice[] fields ) {
		super( superType, methods, fields );
	}
	public AnonymousInnerTypeDeclaredInAlice( Class< ? > superCls, MethodDeclaredInAlice[] methods, FieldDeclaredInAlice[] fields ) {
		this( TypeDeclaredInJava.get( superCls ), methods, fields );
	}
	@Override
	public java.util.ArrayList< ? extends edu.cmu.cs.dennisc.alice.ast.AbstractConstructor > getDeclaredConstructors() {
		return this.constructors;
	}
	@Override
	public edu.cmu.cs.dennisc.property.StringProperty getNamePropertyIfItExists() {
		return null;
	}
	@Override
	public AbstractPackage getPackage() {
		//todo?
		return null;
	}
	@Override
	public Access getAccess() {
		//todo?
		return null;
	}
	
	//An anonymous class is never abstract (8.1.1.1). An anonymous class is always an inner class (8.1.3); it is never static (8.1.1, 8.5.2). An anonymous class is always implicitly final (8.1.1.2).
	@Override
	public boolean isAbstract() {
		return false;
	}
	@Override
	public boolean isStatic() {
		return false;
	}
	@Override
	public boolean isFinal() {
		return true;
	}
	@Override
	public boolean isStrictFloatingPoint() {
		//todo?
		return false;
	}
	
}
