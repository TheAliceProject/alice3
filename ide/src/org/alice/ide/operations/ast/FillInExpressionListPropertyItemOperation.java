/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may 
 *    "Alice" appear in their name, without prior written permission of 
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software 
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is 
 *    contributed by Electronic Arts Inc. and may be used for personal, 
 *    non-commercial, and academic use only. Redistributions of any program 
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in 
 *    The Alice 3.0 Art Gallery License.
 * 
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A 
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO 
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.alice.ide.operations.ast;

/**
 * @author Dennis Cosgrove
 */
public abstract class FillInExpressionListPropertyItemOperation extends org.alice.ide.operations.ActionOperation {
//	private edu.cmu.cs.dennisc.alice.ast.AbstractType type;
	private edu.cmu.cs.dennisc.alice.ast.ExpressionListProperty expressionListProperty;
	private int index;
	public FillInExpressionListPropertyItemOperation( edu.cmu.cs.dennisc.croquet.Group group, java.util.UUID individualId, int index, edu.cmu.cs.dennisc.alice.ast.ExpressionListProperty expressionListProperty ) {
		super( group, individualId );
		//this.type = type;
		this.index = index;
		this.expressionListProperty = expressionListProperty;
	}
	protected abstract edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> getFillInType();
	@Override
	protected final void perform(edu.cmu.cs.dennisc.croquet.ActionOperationContext operationContext) {
		class FillInExpressionEdit extends org.alice.ide.ToDoEdit {
			private edu.cmu.cs.dennisc.alice.ast.Expression prevExpression;
			private edu.cmu.cs.dennisc.alice.ast.Expression nextExpression;
			@Override
			protected final void doOrRedoInternal( boolean isDo ) {
				expressionListProperty.set( index, this.nextExpression );
			}
			@Override
			protected final void undoInternal() {
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
		final edu.cmu.cs.dennisc.croquet.ViewController<?, ?> viewController = operationContext.getViewController();
		final java.awt.Point p = operationContext.getPoint();
		operationContext.pend( new edu.cmu.cs.dennisc.croquet.PendResolver< FillInExpressionEdit, edu.cmu.cs.dennisc.alice.ast.Expression >() {
			public FillInExpressionEdit createEdit() {
				return new FillInExpressionEdit();
			}
			public FillInExpressionEdit initialize(FillInExpressionEdit rv, edu.cmu.cs.dennisc.croquet.ModelContext context, java.util.UUID id, edu.cmu.cs.dennisc.task.TaskObserver<edu.cmu.cs.dennisc.alice.ast.Expression> taskObserver) {
				rv.prevExpression = expressionListProperty.get( index );
				edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> type = getFillInType();
				getIDE().promptUserForExpression( type, rv.prevExpression, viewController, p, taskObserver );
				return rv;
			}
			public FillInExpressionEdit handleCompletion( FillInExpressionEdit rv, edu.cmu.cs.dennisc.alice.ast.Expression expression ) {
				//todo: remove?
				getIDE().unsetPreviousExpressionAndDropStatement();
				rv.nextExpression = expression;
				return rv;
			}
			public void handleCancelation() {
				//todo: remove?
				getIDE().unsetPreviousExpressionAndDropStatement();
			}
		} );
	}
}
