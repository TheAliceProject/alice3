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
package org.alice.ide.ubiquitouspane.templates;

/**
 * @author Dennis Cosgrove
 */
public class ReturnStatementTemplate extends CascadingUbiquitousStatementTemplate {
	public ReturnStatementTemplate() {
		super( edu.cmu.cs.dennisc.alice.ast.ReturnStatement.class, org.alice.ide.ast.NodeUtilities.createIncompleteReturnStatement( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( Object.class ) ) );
	}
	private edu.cmu.cs.dennisc.alice.ast.AbstractType getReturnType() {
		edu.cmu.cs.dennisc.alice.ast.AbstractCode code = getIDE().getFocusedCode();
		if( code instanceof edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice ) {
			edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method = (edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice)code;
			if( method.isFunction() ) {
				return method.returnType.getValue();
			}
		}
		return null;
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.AbstractType[] getBlankExpressionTypes() {
		edu.cmu.cs.dennisc.alice.ast.AbstractType returnType = this.getReturnType();
		if( returnType != null ) {
			return new edu.cmu.cs.dennisc.alice.ast.AbstractType[] { returnType };
		} else {
			return null;
		}
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.Statement createStatement( edu.cmu.cs.dennisc.alice.ast.Expression... expressions ) {
		edu.cmu.cs.dennisc.alice.ast.ReturnStatement rv = org.alice.ide.ast.NodeUtilities.createReturnStatement( this.getReturnType(), expressions[ 0 ] );
		return rv;
	}
}
