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

package org.alice.ide.name.validators;

public abstract class TransientNameValidator extends NodeNameValidator {
	private edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice code;
	private edu.cmu.cs.dennisc.alice.ast.BlockStatement block;
	public TransientNameValidator( edu.cmu.cs.dennisc.alice.ast.Node node, edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice code, edu.cmu.cs.dennisc.alice.ast.BlockStatement block ) {
		super( node );
//		assert code != null;
//		assert block != null;
		this.code = code;
		this.block = block;
	}
	@Override
	protected boolean isNameAvailable( String name ) {
		if( this.code != null ) {
			edu.cmu.cs.dennisc.alice.ast.Node node = this.getNode();
			for( edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice parameter : this.code.getParamtersProperty() ) {
				if( parameter == node ) {
					//pass
				} else {
					if( name.equals( parameter.name.getValue() ) ) {
						return false;
					}
				}
			}
//			if( this.block != null ) {
//				
//			}
			edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< edu.cmu.cs.dennisc.alice.ast.LocalDeclaredInAlice > crawler = new edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< edu.cmu.cs.dennisc.alice.ast.LocalDeclaredInAlice >( edu.cmu.cs.dennisc.alice.ast.LocalDeclaredInAlice.class );
			((edu.cmu.cs.dennisc.alice.ast.AbstractCode)this.code).crawl( crawler, false );
			for( edu.cmu.cs.dennisc.alice.ast.LocalDeclaredInAlice local : crawler.getList() ) {
				if( local == node ) {
					//pass
				} else {
					if( name.equals( local.name.getValue() ) ) {
						return false;
					}
				}
			}
		}
		return true;
	}
}
