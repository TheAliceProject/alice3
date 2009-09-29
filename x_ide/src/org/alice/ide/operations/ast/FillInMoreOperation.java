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
public class FillInMoreOperation extends org.alice.ide.operations.AbstractActionOperation implements edu.cmu.cs.dennisc.zoot.Resolver< edu.cmu.cs.dennisc.alice.ast.Expression >{
	private edu.cmu.cs.dennisc.alice.ast.ExpressionStatement expressionStatement;
	private edu.cmu.cs.dennisc.alice.ast.AbstractMethod nextLongerMethod;
	private edu.cmu.cs.dennisc.alice.ast.MethodInvocation nextMethodInvocation;
	private edu.cmu.cs.dennisc.alice.ast.MethodInvocation prevMethodInvocation;
	public FillInMoreOperation( edu.cmu.cs.dennisc.alice.ast.ExpressionStatement expressionStatement ) {
		assert expressionStatement != null;
		this.expressionStatement = expressionStatement;
	}
	public void perform( edu.cmu.cs.dennisc.zoot.ActionContext actionContext ) {
		actionContext.pend( this );
	}
	public void initialize( edu.cmu.cs.dennisc.zoot.Context<? extends edu.cmu.cs.dennisc.zoot.Operation> context, edu.cmu.cs.dennisc.task.TaskObserver<edu.cmu.cs.dennisc.alice.ast.Expression> taskObserver ) {
		this.prevMethodInvocation = (edu.cmu.cs.dennisc.alice.ast.MethodInvocation)expressionStatement.expression.getValue();
		edu.cmu.cs.dennisc.alice.ast.AbstractMethod method = this.prevMethodInvocation.method.getValue();
		this.nextLongerMethod = (edu.cmu.cs.dennisc.alice.ast.AbstractMethod)method.getNextLongerInChain();
		java.util.ArrayList< ? extends edu.cmu.cs.dennisc.alice.ast.AbstractParameter > parameters = this.nextLongerMethod.getParameters();
		edu.cmu.cs.dennisc.alice.ast.AbstractParameter lastParameter = parameters.get( parameters.size()-1 );
		this.getIDE().promptUserForMore( lastParameter, (java.awt.event.MouseEvent)context.getEvent(), taskObserver );
	}
	public void handleCompletion(edu.cmu.cs.dennisc.alice.ast.Expression expression) {
		this.nextMethodInvocation = org.alice.ide.ast.NodeUtilities.createNextMethodInvocation( this.prevMethodInvocation, expression, this.nextLongerMethod );
		//todo: remove?
		getIDE().unsetPreviousExpression();
	}
	public void handleCancelation() {
		//todo: remove?
		getIDE().unsetPreviousExpression();
	}
	
	@Override
	public void doOrRedo() throws javax.swing.undo.CannotRedoException {
		expressionStatement.expression.setValue( this.nextMethodInvocation );
	}
	@Override
	public void undo() throws javax.swing.undo.CannotUndoException {
		expressionStatement.expression.setValue( this.prevMethodInvocation );
	}
	@Override
	public boolean isSignificant() {
		return true;
	}
}
