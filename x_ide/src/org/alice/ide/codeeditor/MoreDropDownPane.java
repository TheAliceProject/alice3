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
package org.alice.ide.codeeditor;

/**
 * @author Dennis Cosgrove
 */
public class MoreDropDownPane extends DropDownPane {
	private edu.cmu.cs.dennisc.alice.ast.ExpressionStatement expressionStatement;
	public MoreDropDownPane( edu.cmu.cs.dennisc.alice.ast.ExpressionStatement expressionStatement ) {
		super( null, edu.cmu.cs.dennisc.croquet.CroquetUtilities.createLabel( "more"), null );
		this.expressionStatement = expressionStatement;

		edu.cmu.cs.dennisc.alice.ast.MethodInvocation methodInvocation = (edu.cmu.cs.dennisc.alice.ast.MethodInvocation)expressionStatement.expression.getValue();
		edu.cmu.cs.dennisc.alice.ast.AbstractMethod method = methodInvocation.method.getValue();
		final edu.cmu.cs.dennisc.alice.ast.AbstractMethod nextLongerMethod = (edu.cmu.cs.dennisc.alice.ast.AbstractMethod)method.getNextLongerInChain();
		java.util.ArrayList< ? extends edu.cmu.cs.dennisc.alice.ast.AbstractParameter > parameters = nextLongerMethod.getParameters();
		edu.cmu.cs.dennisc.alice.ast.AbstractParameter lastParameter = parameters.get( parameters.size()-1 );
		String name = lastParameter.getName();
		if( name != null ) {
			this.setToolTipText( name );
		}
		this.setLeftButtonPressOperation( new org.alice.ide.operations.ast.FillInMoreOperation( this.expressionStatement ) );
	}
	@Override
	protected int getInsetLeft() {
		return super.getInsetLeft() + 2;
	}
//	public edu.cmu.cs.dennisc.alice.ast.ExpressionStatement getMethod() {
//		return this.expressionStatement;
//	}
}
