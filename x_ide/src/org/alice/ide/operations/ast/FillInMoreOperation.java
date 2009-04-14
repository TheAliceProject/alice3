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
package org.alice.ide.operations.ast;

/**
 * @author Dennis Cosgrove
 */
public class FillInMoreOperation extends org.alice.ide.operations.AbstractActionOperation {
	private edu.cmu.cs.dennisc.alice.ast.ExpressionStatement expressionStatement;
	public FillInMoreOperation( edu.cmu.cs.dennisc.alice.ast.ExpressionStatement expressionStatement ) {
		assert expressionStatement != null;
		this.expressionStatement = expressionStatement;
	}
	public void perform( zoot.ActionContext actionContext ) {
		final edu.cmu.cs.dennisc.alice.ast.MethodInvocation methodInvocation = (edu.cmu.cs.dennisc.alice.ast.MethodInvocation)expressionStatement.expression.getValue();
		edu.cmu.cs.dennisc.alice.ast.AbstractMethod method = methodInvocation.method.getValue();
		final edu.cmu.cs.dennisc.alice.ast.AbstractMethod nextLongerMethod = (edu.cmu.cs.dennisc.alice.ast.AbstractMethod)method.getNextLongerInChain();
		java.util.ArrayList< ? extends edu.cmu.cs.dennisc.alice.ast.AbstractParameter > parameters = nextLongerMethod.getParameters();
		edu.cmu.cs.dennisc.alice.ast.AbstractParameter lastParameter = parameters.get( parameters.size()-1 );
		getIDE().promptUserForMore( lastParameter, (java.awt.event.MouseEvent)actionContext.getEvent(), new edu.cmu.cs.dennisc.task.TaskObserver< edu.cmu.cs.dennisc.alice.ast.Expression >() {
			public void handleCompletion( edu.cmu.cs.dennisc.alice.ast.Expression expression ) {
				expressionStatement.expression.setValue( org.alice.ide.ast.NodeUtilities.createNextMethodInvocation( methodInvocation, expression, nextLongerMethod ) );
				getIDE().markChanged( "more menu" );
			}
			public void handleCancelation() {
				getIDE().unsetPreviousExpression();
			}			
		} );
	}
}
