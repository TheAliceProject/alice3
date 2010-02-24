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
		super( org.alice.ide.IDE.PROJECT_GROUP );
		assert expressionStatement != null;
		this.expressionStatement = expressionStatement;
	}
	
	public void perform( edu.cmu.cs.dennisc.zoot.ActionContext actionContext ) {
		class MoreEdit extends edu.cmu.cs.dennisc.zoot.AbstractEdit {
			private edu.cmu.cs.dennisc.alice.ast.AbstractMethod nextLongerMethod;
			private edu.cmu.cs.dennisc.alice.ast.MethodInvocation nextMethodInvocation;
			private edu.cmu.cs.dennisc.alice.ast.MethodInvocation prevMethodInvocation;
			@Override
			public void doOrRedo( boolean isDo ) {
				expressionStatement.expression.setValue( this.nextMethodInvocation );
			}
			@Override
			public void undo() {
				expressionStatement.expression.setValue( this.prevMethodInvocation );
			}
			@Override
			protected StringBuffer updatePresentation( StringBuffer rv, java.util.Locale locale ) {
				//super.updatePresentation( rv );
				rv.append( "more: " );
				edu.cmu.cs.dennisc.alice.ast.Node.safeAppendRepr( rv, nextLongerMethod, locale );
				rv.append( " " );
				final int N = this.nextMethodInvocation.arguments.size(); 
				edu.cmu.cs.dennisc.alice.ast.Argument argument = this.nextMethodInvocation.arguments.get( N-1 );
				edu.cmu.cs.dennisc.alice.ast.Node.safeAppendRepr( rv, argument, locale );
				return rv;
			}
		}
		actionContext.pend( new edu.cmu.cs.dennisc.zoot.Resolver< MoreEdit, edu.cmu.cs.dennisc.alice.ast.Expression >() {
			public MoreEdit createEdit() {
				return new MoreEdit();
			}
			public MoreEdit initialize( MoreEdit rv, edu.cmu.cs.dennisc.zoot.Context< ? extends edu.cmu.cs.dennisc.zoot.Operation > context, edu.cmu.cs.dennisc.task.TaskObserver< edu.cmu.cs.dennisc.alice.ast.Expression > taskObserver ) {
				rv.prevMethodInvocation = (edu.cmu.cs.dennisc.alice.ast.MethodInvocation)expressionStatement.expression.getValue();
				edu.cmu.cs.dennisc.alice.ast.AbstractMethod method = rv.prevMethodInvocation.method.getValue();
				rv.nextLongerMethod = (edu.cmu.cs.dennisc.alice.ast.AbstractMethod)method.getNextLongerInChain();
				java.util.ArrayList< ? extends edu.cmu.cs.dennisc.alice.ast.AbstractParameter > parameters = rv.nextLongerMethod.getParameters();
				edu.cmu.cs.dennisc.alice.ast.AbstractParameter lastParameter = parameters.get( parameters.size()-1 );
				getIDE().promptUserForMore( lastParameter, (java.awt.event.MouseEvent)context.getEvent(), taskObserver );

				return rv;
			}
			public MoreEdit handleCompletion( MoreEdit rv, edu.cmu.cs.dennisc.alice.ast.Expression expression ) {
				//todo: remove?
				getIDE().unsetPreviousExpression();
				rv.nextMethodInvocation = org.alice.ide.ast.NodeUtilities.createNextMethodInvocation( rv.prevMethodInvocation, expression, rv.nextLongerMethod );
				return rv;
			}
			public void handleCancelation() {
				//todo: remove?
				getIDE().unsetPreviousExpression();
			}
		} );
	}
}
