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
public class AnonymousConstructor extends AbstractConstructor {
	private static java.util.Map< AnonymousInnerTypeDeclaredInAlice, AnonymousConstructor > s_map;
	private AnonymousInnerTypeDeclaredInAlice type;
	public static AnonymousConstructor get( AnonymousInnerTypeDeclaredInAlice type ) {
		if( type != null ) {
			if( s_map != null ) {
				//pass
			} else {
				s_map = new java.util.HashMap< AnonymousInnerTypeDeclaredInAlice, AnonymousConstructor >();
			}
			AnonymousConstructor rv = s_map.get( type );
			if( rv != null ) {
				//pass
			} else {
				rv = new AnonymousConstructor( type );
			}
			return rv;
		} else {
			return null;
		}
	}
	private java.util.ArrayList< AbstractParameter > parameters = new java.util.ArrayList< AbstractParameter >();
	private AnonymousConstructor( AnonymousInnerTypeDeclaredInAlice type ) {
		this.type = type;
	}
	@Override
	public edu.cmu.cs.dennisc.alice.ast.Access getAccess() {
		return null;
	}
	@Override
	public edu.cmu.cs.dennisc.alice.ast.AbstractType getDeclaringType() {
		return this.type;
	}
	@Override
	public edu.cmu.cs.dennisc.alice.ast.AbstractMember getNextLongerInChain() {
		return null;
	}
	@Override
	public edu.cmu.cs.dennisc.alice.ast.AbstractMember getNextShorterInChain() {
		return null;
	}
	@Override
	public java.util.ArrayList< ? extends edu.cmu.cs.dennisc.alice.ast.AbstractParameter > getParameters() {
		return this.parameters;
	}
	@Override
	public edu.cmu.cs.dennisc.alice.annotations.Visibility getVisibility() {
		return null;
	}
}
