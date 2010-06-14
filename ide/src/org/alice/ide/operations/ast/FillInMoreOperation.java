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
public class FillInMoreOperation extends org.alice.ide.operations.ActionOperation {
	private static java.util.Map< edu.cmu.cs.dennisc.alice.ast.ExpressionStatement, FillInMoreOperation > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	public static FillInMoreOperation getInstance( edu.cmu.cs.dennisc.alice.ast.ExpressionStatement expressionStatement ) {
		FillInMoreOperation rv = map.get( expressionStatement );
		if( rv != null ) {
			//pass
		} else {
			rv = new FillInMoreOperation( expressionStatement );
			map.put( expressionStatement, rv );
		}
		return rv;
	}

	private edu.cmu.cs.dennisc.alice.ast.ExpressionStatement expressionStatement;
	private FillInMoreOperation( edu.cmu.cs.dennisc.alice.ast.ExpressionStatement expressionStatement ) {
		super( edu.cmu.cs.dennisc.alice.Project.GROUP, java.util.UUID.fromString( "e4cdc25b-d7a0-42b5-adc9-74e34db6b5fc" ) );
		assert expressionStatement != null;
		this.expressionStatement = expressionStatement;
		
		this.setName( "more..." );
		this.updateToolTipText();
	}
	
	private void updateToolTipText() {
//		edu.cmu.cs.dennisc.alice.ast.MethodInvocation methodInvocation = (edu.cmu.cs.dennisc.alice.ast.MethodInvocation)expressionStatement.expression.getValue();
//		edu.cmu.cs.dennisc.alice.ast.AbstractMethod method = methodInvocation.method.getValue();
//		final edu.cmu.cs.dennisc.alice.ast.AbstractMethod nextLongerMethod = (edu.cmu.cs.dennisc.alice.ast.AbstractMethod)method.getNextLongerInChain();
//		if( nextLongerMethod != null ) {
//			java.util.ArrayList< ? extends edu.cmu.cs.dennisc.alice.ast.AbstractParameter > parameters = nextLongerMethod.getParameters();
//			edu.cmu.cs.dennisc.alice.ast.AbstractParameter lastParameter = parameters.get( parameters.size()-1 );
//			String name = lastParameter.getName();
//			if( name != null ) {
//				this.setToolTipText( name );
//			}
//		}
	}
	@Override
	protected final void perform(final edu.cmu.cs.dennisc.croquet.ActionOperationContext operationContext) {
		class MoreEdit extends org.alice.ide.ToDoEdit {
			private edu.cmu.cs.dennisc.alice.ast.AbstractMethod nextLongerMethod;
			private edu.cmu.cs.dennisc.alice.ast.MethodInvocation nextMethodInvocation;
			private edu.cmu.cs.dennisc.alice.ast.MethodInvocation prevMethodInvocation;
			@Override
			public void doOrRedo( boolean isDo ) {
				expressionStatement.expression.setValue( this.nextMethodInvocation );
				FillInMoreOperation.this.updateToolTipText();
			}
			@Override
			public void undo() {
				expressionStatement.expression.setValue( this.prevMethodInvocation );
				FillInMoreOperation.this.updateToolTipText();
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
		operationContext.pend( new edu.cmu.cs.dennisc.croquet.PendResolver< MoreEdit, edu.cmu.cs.dennisc.alice.ast.Expression >() {
			public MoreEdit createEdit() {
				return new MoreEdit();
			}
			public MoreEdit initialize(MoreEdit rv, edu.cmu.cs.dennisc.croquet.ModelContext context, java.util.UUID id, edu.cmu.cs.dennisc.task.TaskObserver<edu.cmu.cs.dennisc.alice.ast.Expression> taskObserver) {
				rv.prevMethodInvocation = (edu.cmu.cs.dennisc.alice.ast.MethodInvocation)expressionStatement.expression.getValue();
				edu.cmu.cs.dennisc.alice.ast.AbstractMethod method = rv.prevMethodInvocation.method.getValue();
				rv.nextLongerMethod = (edu.cmu.cs.dennisc.alice.ast.AbstractMethod)method.getNextLongerInChain();
				java.util.ArrayList< ? extends edu.cmu.cs.dennisc.alice.ast.AbstractParameter > parameters = rv.nextLongerMethod.getParameters();
				edu.cmu.cs.dennisc.alice.ast.AbstractParameter lastParameter = parameters.get( parameters.size()-1 );
				getIDE().promptUserForMore( expressionStatement, lastParameter, operationContext.getViewController(), operationContext.getPoint(), taskObserver );

				return rv;
			}
			public MoreEdit handleCompletion( MoreEdit rv, edu.cmu.cs.dennisc.alice.ast.Expression expression ) {
				//todo: remove?
				getIDE().unsetPreviousExpressionAndDropStatement();
				rv.nextMethodInvocation = org.alice.ide.ast.NodeUtilities.createNextMethodInvocation( rv.prevMethodInvocation, expression, rv.nextLongerMethod );
				return rv;
			}
			public void handleCancelation() {
				//todo: remove?
				getIDE().unsetPreviousExpressionAndDropStatement();
			}
		} );
	}
}
