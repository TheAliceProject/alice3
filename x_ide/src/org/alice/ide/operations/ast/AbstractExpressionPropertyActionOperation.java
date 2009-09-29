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
public abstract class AbstractExpressionPropertyActionOperation extends org.alice.ide.operations.AbstractActionOperation implements edu.cmu.cs.dennisc.zoot.Resolver< edu.cmu.cs.dennisc.alice.ast.Expression > {
	private edu.cmu.cs.dennisc.alice.ast.ExpressionProperty expressionProperty;
	private edu.cmu.cs.dennisc.alice.ast.Expression nextExpression;
	private edu.cmu.cs.dennisc.alice.ast.Expression prevExpression;
	public AbstractExpressionPropertyActionOperation( edu.cmu.cs.dennisc.alice.ast.ExpressionProperty expressionProperty ) {
		this.expressionProperty = expressionProperty;
	}
	protected edu.cmu.cs.dennisc.alice.ast.ExpressionProperty getExpressionProperty() {
		return this.expressionProperty;
	}
	public void perform( edu.cmu.cs.dennisc.zoot.ActionContext actionContext ) {
		actionContext.pend( this );
	}
	protected abstract void initializeInternal( edu.cmu.cs.dennisc.zoot.Context<? extends edu.cmu.cs.dennisc.zoot.Operation> context, edu.cmu.cs.dennisc.task.TaskObserver<edu.cmu.cs.dennisc.alice.ast.Expression> taskObserver, edu.cmu.cs.dennisc.alice.ast.Expression prevExpression );
	public void initialize( edu.cmu.cs.dennisc.zoot.Context<? extends edu.cmu.cs.dennisc.zoot.Operation> context, edu.cmu.cs.dennisc.task.TaskObserver<edu.cmu.cs.dennisc.alice.ast.Expression> taskObserver ) {
		this.prevExpression = this.expressionProperty.getValue();
		this.initializeInternal(context, taskObserver, this.prevExpression);
	}
	public void handleCompletion(edu.cmu.cs.dennisc.alice.ast.Expression expression) {
		this.nextExpression = expression;
		//todo: remove?
		getIDE().unsetPreviousExpression();
	}
	public void handleCancelation() {
		//todo: remove?
		getIDE().unsetPreviousExpression();
	}
	
	@Override
	public void doOrRedo() throws javax.swing.undo.CannotRedoException {
		this.expressionProperty.setValue( this.nextExpression );
	}
	@Override
	public void undo() throws javax.swing.undo.CannotUndoException {
		this.expressionProperty.setValue( this.prevExpression );
	}
	@Override
	public boolean isSignificant() {
		return true;
	}
}
