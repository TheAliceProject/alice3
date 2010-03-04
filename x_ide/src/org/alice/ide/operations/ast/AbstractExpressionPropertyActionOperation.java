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
public abstract class AbstractExpressionPropertyActionOperation extends org.alice.ide.operations.AbstractActionOperation {
	private edu.cmu.cs.dennisc.alice.ast.ExpressionProperty expressionProperty;

	public AbstractExpressionPropertyActionOperation( edu.cmu.cs.dennisc.alice.ast.ExpressionProperty expressionProperty ) {
		super( edu.cmu.cs.dennisc.alice.Project.GROUP_UUID );
		this.expressionProperty = expressionProperty;
	}
	protected edu.cmu.cs.dennisc.alice.ast.ExpressionProperty getExpressionProperty() {
		return this.expressionProperty;
	}

	public void perform( edu.cmu.cs.dennisc.zoot.ActionContext actionContext ) {
		class ExpressionPropertyEdit extends edu.cmu.cs.dennisc.zoot.AbstractEdit {
			private edu.cmu.cs.dennisc.alice.ast.Expression prevExpression;
			private edu.cmu.cs.dennisc.alice.ast.Expression nextExpression;

			@Override
			public void doOrRedo( boolean isDo ) {
				expressionProperty.setValue( this.nextExpression );
			}
			@Override
			public void undo() {
				expressionProperty.setValue( this.prevExpression );
			}

			@Override
			protected StringBuffer updatePresentation( StringBuffer rv, java.util.Locale locale ) {
				//super.updatePresentation( rv );
				rv.append( "set: " );
				edu.cmu.cs.dennisc.alice.ast.Node.safeAppendRepr( rv, this.prevExpression, locale );
				rv.append( " ===> " );
				edu.cmu.cs.dennisc.alice.ast.Node.safeAppendRepr( rv, this.nextExpression, locale );
				return rv;
			}
		}
		actionContext.pend( new edu.cmu.cs.dennisc.zoot.Resolver< ExpressionPropertyEdit, edu.cmu.cs.dennisc.alice.ast.Expression >() {
			public ExpressionPropertyEdit createEdit() {
				return new ExpressionPropertyEdit();
			}
			public ExpressionPropertyEdit initialize( ExpressionPropertyEdit rv, edu.cmu.cs.dennisc.zoot.Context< ? extends edu.cmu.cs.dennisc.zoot.Operation > context,
					edu.cmu.cs.dennisc.task.TaskObserver< edu.cmu.cs.dennisc.alice.ast.Expression > taskObserver ) {
				rv.prevExpression = AbstractExpressionPropertyActionOperation.this.expressionProperty.getValue();
				AbstractExpressionPropertyActionOperation.this.initializeInternal( context, taskObserver, rv.prevExpression );
				return rv;
			}
			public ExpressionPropertyEdit handleCompletion( ExpressionPropertyEdit rv, edu.cmu.cs.dennisc.alice.ast.Expression expression ) {
				//todo: remove?
				getIDE().unsetPreviousExpression();
				rv.nextExpression = expression;
				return rv;
			}
			public void handleCancelation() {
				//todo: remove?
				getIDE().unsetPreviousExpression();
			}
		} );
	}
	protected abstract void initializeInternal( edu.cmu.cs.dennisc.zoot.Context< ? extends edu.cmu.cs.dennisc.zoot.Operation > context, edu.cmu.cs.dennisc.task.TaskObserver< edu.cmu.cs.dennisc.alice.ast.Expression > taskObserver,
			edu.cmu.cs.dennisc.alice.ast.Expression prevExpression );

}
