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
public abstract class AbstractExpressionPropertyActionOperation extends org.alice.ide.operations.ActionOperation {
	private edu.cmu.cs.dennisc.alice.ast.ExpressionProperty expressionProperty;

	public AbstractExpressionPropertyActionOperation( java.util.UUID individualId, edu.cmu.cs.dennisc.alice.ast.ExpressionProperty expressionProperty ) {
		super( edu.cmu.cs.dennisc.alice.Project.GROUP_UUID, individualId );
		this.expressionProperty = expressionProperty;
	}
	protected edu.cmu.cs.dennisc.alice.ast.ExpressionProperty getExpressionProperty() {
		return this.expressionProperty;
	}

	@Override
	protected void perform(edu.cmu.cs.dennisc.croquet.Context context, java.awt.event.ActionEvent e, edu.cmu.cs.dennisc.croquet.AbstractButton<?> button) {
		class ExpressionPropertyEdit extends org.alice.ide.ToDoEdit {
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
		context.pend( new edu.cmu.cs.dennisc.croquet.Resolver< ExpressionPropertyEdit, edu.cmu.cs.dennisc.alice.ast.Expression >() {
			public ExpressionPropertyEdit createEdit() {
				return new ExpressionPropertyEdit();
			}
			public ExpressionPropertyEdit initialize(ExpressionPropertyEdit rv, edu.cmu.cs.dennisc.croquet.Context context, java.util.UUID id, edu.cmu.cs.dennisc.task.TaskObserver<edu.cmu.cs.dennisc.alice.ast.Expression> taskObserver) {
				rv.prevExpression = AbstractExpressionPropertyActionOperation.this.expressionProperty.getValue();
				AbstractExpressionPropertyActionOperation.this.initializeInternal( context, id, taskObserver, rv.prevExpression );
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
	protected abstract void initializeInternal( edu.cmu.cs.dennisc.croquet.Context context, java.util.UUID id, edu.cmu.cs.dennisc.task.TaskObserver<edu.cmu.cs.dennisc.alice.ast.Expression> taskObserver, edu.cmu.cs.dennisc.alice.ast.Expression prevExpression );

}
