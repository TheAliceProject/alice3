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
public abstract class FillInExpressionListPropertyItemOperation extends org.alice.ide.operations.AbstractActionOperation {
//	private edu.cmu.cs.dennisc.alice.ast.AbstractType type;
	private edu.cmu.cs.dennisc.alice.ast.ExpressionListProperty expressionListProperty;
	private int index;
	public FillInExpressionListPropertyItemOperation( int index, edu.cmu.cs.dennisc.alice.ast.ExpressionListProperty expressionListProperty ) {
		super( edu.cmu.cs.dennisc.alice.Project.GROUP_UUID );
		//this.type = type;
		this.index = index;
		this.expressionListProperty = expressionListProperty;
	}
	protected abstract edu.cmu.cs.dennisc.alice.ast.AbstractType getFillInType();
	public void perform( edu.cmu.cs.dennisc.zoot.ActionContext actionContext ) {
		class FillInExpressionEdit extends edu.cmu.cs.dennisc.zoot.AbstractEdit {
			private edu.cmu.cs.dennisc.alice.ast.Expression prevExpression;
			private edu.cmu.cs.dennisc.alice.ast.Expression nextExpression;
			@Override
			public void doOrRedo( boolean isDo ) {
				expressionListProperty.set( index, this.nextExpression );
			}
			@Override
			public void undo() {
				expressionListProperty.set( index, this.prevExpression );
			}
			@Override
			protected StringBuffer updatePresentation(StringBuffer rv, java.util.Locale locale) {
				rv.append( "set: " );
				edu.cmu.cs.dennisc.alice.ast.Node.safeAppendRepr(rv, this.prevExpression, locale);
				rv.append( " ===> " );
				edu.cmu.cs.dennisc.alice.ast.Node.safeAppendRepr(rv, this.nextExpression, locale);
				return rv;
			}
		}
		actionContext.pend( new edu.cmu.cs.dennisc.zoot.Resolver< FillInExpressionEdit, edu.cmu.cs.dennisc.alice.ast.Expression >() {
			public FillInExpressionEdit createEdit() {
				return new FillInExpressionEdit();
			}
			public FillInExpressionEdit initialize( FillInExpressionEdit rv, edu.cmu.cs.dennisc.zoot.Context< ? extends edu.cmu.cs.dennisc.zoot.Operation > context, edu.cmu.cs.dennisc.task.TaskObserver< edu.cmu.cs.dennisc.alice.ast.Expression > taskObserver ) {
				rv.prevExpression = expressionListProperty.get( index );
				edu.cmu.cs.dennisc.alice.ast.AbstractType type = getFillInType();
				getIDE().promptUserForExpression( type, rv.prevExpression, (java.awt.event.MouseEvent)context.getEvent(), taskObserver );
				return rv;
			}
			public FillInExpressionEdit handleCompletion( FillInExpressionEdit rv, edu.cmu.cs.dennisc.alice.ast.Expression expression ) {
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
}
