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
	private int index;
	private edu.cmu.cs.dennisc.alice.ast.ExpressionListProperty expressionListProperty;
	public FillInExpressionListPropertyItemOperation( int index, edu.cmu.cs.dennisc.alice.ast.ExpressionListProperty expressionListProperty ) {
		//this.type = type;
		this.index = index;
		this.expressionListProperty = expressionListProperty;
	}
	protected abstract edu.cmu.cs.dennisc.alice.ast.AbstractType getFillInType();
	public void perform( zoot.ActionContext actionContext ) {
		edu.cmu.cs.dennisc.alice.ast.AbstractType type = this.getFillInType();
		edu.cmu.cs.dennisc.alice.ast.Expression prevExpression = this.expressionListProperty.get( FillInExpressionListPropertyItemOperation.this.index );
		getIDE().promptUserForExpression( type, prevExpression, (java.awt.event.MouseEvent)actionContext.getEvent(), new edu.cmu.cs.dennisc.task.TaskObserver< edu.cmu.cs.dennisc.alice.ast.Expression >() {
			public void handleCompletion( edu.cmu.cs.dennisc.alice.ast.Expression e ) {
				FillInExpressionListPropertyItemOperation.this.expressionListProperty.set( FillInExpressionListPropertyItemOperation.this.index, e );
				getIDE().unsetPreviousExpression();
				getIDE().markChanged( "expression menu" );
			}
			public void handleCancelation() {
				getIDE().unsetPreviousExpression();
			}			
		} );
	}
}
