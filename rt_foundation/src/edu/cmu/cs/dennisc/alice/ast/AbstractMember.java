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

//todo: < E extends AbstractMember> for getPreviousInChain and getNextInChain
/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractMember extends AbstractAccessibleDeclaration {
	public abstract edu.cmu.cs.dennisc.alice.annotations.Visibility getVisibility();
	public abstract AbstractType getDeclaringType();
	@Override
	public boolean isDeclaredInAlice() {
		AbstractType declaringType = getDeclaringType();
		//assert declaringType != null;
		if( declaringType != null ) {
			return declaringType.isDeclaredInAlice();
		} else {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "WARNING: declaring type is null for:", this );
			return false;
		}
	}
	
	public AbstractMember getShortestInChain() {
		AbstractMember next = this.getNextShorterInChain();
		if( next != null ) {
			return next.getShortestInChain();
		} else {
			return this;
		}
	}
	public AbstractMember getLongestInChain() {
		AbstractMember next = this.getNextLongerInChain();
		if( next != null ) {
			return next.getLongestInChain();
		} else {
			return this;
		}
	}
	public abstract AbstractMember getNextLongerInChain();
	public abstract AbstractMember getNextShorterInChain();		
}
