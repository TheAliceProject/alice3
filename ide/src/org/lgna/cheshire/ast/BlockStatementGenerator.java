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

package org.lgna.cheshire.ast;

/**
 * @author Dennis Cosgrove
 */
public class BlockStatementGenerator {
	private static final java.util.Map<Class<? extends org.lgna.project.ast.Statement>,StatementGenerator> mapStatementClassToGenerator = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	static {
		mapStatementClassToGenerator.put( org.lgna.project.ast.Comment.class, org.alice.ide.ast.draganddrop.statement.CommentTemplateDragModel.getInstance() );
		mapStatementClassToGenerator.put( org.lgna.project.ast.ConditionalStatement.class, org.alice.ide.ast.draganddrop.statement.ConditionalStatementTemplateDragModel.getInstance() );
		mapStatementClassToGenerator.put( org.lgna.project.ast.CountLoop.class, org.alice.ide.ast.draganddrop.statement.CountLoopTemplateDragModel.getInstance() );
		mapStatementClassToGenerator.put( org.lgna.project.ast.DoInOrder.class, org.alice.ide.ast.draganddrop.statement.DoInOrderTemplateDragModel.getInstance() );
		mapStatementClassToGenerator.put( org.lgna.project.ast.DoTogether.class, org.alice.ide.ast.draganddrop.statement.DoTogetherTemplateDragModel.getInstance() );
		mapStatementClassToGenerator.put( org.lgna.project.ast.EachInArrayTogether.class, org.alice.ide.ast.draganddrop.statement.EachInArrayTogetherTemplateDragModel.getInstance() );
		mapStatementClassToGenerator.put( org.lgna.project.ast.ForEachInArrayLoop.class, org.alice.ide.ast.draganddrop.statement.ForEachInArrayLoopTemplateDragModel.getInstance() );
		//mapStatementClassToGenerator.put( org.lgna.project.ast.ReturnStatement.class, org.alice.ide.ast.draganddrop.statement.ReturnStatementTemplateDragModel.getInstance() );
		mapStatementClassToGenerator.put( org.lgna.project.ast.WhileLoop.class, org.alice.ide.ast.draganddrop.statement.WhileLoopTemplateDragModel.getInstance() );
	}
	public static void generateAndAddToTransactionHistory( org.lgna.croquet.history.TransactionHistory history, org.lgna.project.ast.BlockStatement blockStatement ) {
		for( org.lgna.project.ast.Statement statement : blockStatement.statements ) {
			StatementGenerator statementGenerator;
			org.alice.ide.instancefactory.InstanceFactory instanceFactory = null;
			org.alice.ide.members.TemplateComposite templateComposite = null;
			if( statement instanceof org.lgna.project.ast.ExpressionStatement ) {
				org.lgna.project.ast.ExpressionStatement expressionStatement = (org.lgna.project.ast.ExpressionStatement)statement;
				org.lgna.project.ast.Expression expression = expressionStatement.expression.getValue();
				if( expression instanceof org.lgna.project.ast.MethodInvocation ) {
					org.lgna.project.ast.MethodInvocation methodInvocation = (org.lgna.project.ast.MethodInvocation)expression;

					org.lgna.project.ast.AbstractMethod method = methodInvocation.method.getValue();

					if( method instanceof org.lgna.project.ast.UserMethod ) {
						org.lgna.project.ast.UserMethod userMethod = (org.lgna.project.ast.UserMethod)method;
						//todo: check to see if generation actually required
						org.alice.ide.croquet.models.declaration.ProcedureDeclarationOperation.getInstance( userMethod.getDeclaringType() ).generateAndAddToTransactionHistory( history, userMethod );
					}

					org.lgna.project.ast.Expression instanceExpression = methodInvocation.expression.getValue();

					instanceFactory = org.alice.ide.instancefactory.InstanceFactoryUtilities.getInstanceFactoryForExpression( instanceExpression );
					if( instanceFactory != null ) {
						//pass
					} else {
						edu.cmu.cs.dennisc.java.util.logging.Logger.severe( instanceExpression );
					}
					statementGenerator = org.alice.ide.ast.draganddrop.statement.ProcedureInvocationTemplateDragModel.getInstance( methodInvocation.method.getValue() );
					//todo
					if( method.isProcedure() ) {
						if( method.getName().startsWith( "set" ) ) {
							templateComposite = org.alice.ide.members.FieldTemplateComposite.getInstance();
						} else {
							templateComposite = org.alice.ide.members.ProcedureTemplateComposite.getInstance();
						}
					} else {
						if( method.getName().startsWith( "get" ) || method.getName().startsWith( "is" ) ) {
							templateComposite = org.alice.ide.members.FieldTemplateComposite.getInstance();
						} else {
							templateComposite = org.alice.ide.members.FunctionTemplateComposite.getInstance();
						}
					}
				} else {
					edu.cmu.cs.dennisc.java.util.logging.Logger.errln( expression );
					statementGenerator = null;
				}
			} else {
				statementGenerator = mapStatementClassToGenerator.get( statement.getClass() );
				if( org.alice.ide.croquet.models.ui.preferences.IsAlwaysShowingBlocksState.getInstance().getValue() ) {
					// pass
				} else {
					templateComposite = org.alice.ide.controlflow.ControlFlowComposite.getInstance( /*todo*/null );
				}
			}
			if( statementGenerator != null ) {
				if( instanceFactory != null ) {
					org.alice.ide.instancefactory.croquet.InstanceFactoryState.getInstance().pushGeneratedValue( instanceFactory );
				}
				if( templateComposite != null ) {
					if( org.alice.ide.croquet.models.ui.preferences.IsAlwaysShowingBlocksState.getInstance().getValue() ) {
						org.alice.ide.members.ProcedureFunctionPropertyTabState.getInstance().pushGeneratedValue( templateComposite );
					} else {
						org.alice.ide.members.ProcedureFunctionControlFlowTabState.getInstance().pushGeneratedValue( templateComposite );
					}
				}
				statementGenerator.generateAndAddStepsToTransaction( history, statement );
				if( templateComposite != null ) {
					if( org.alice.ide.croquet.models.ui.preferences.IsAlwaysShowingBlocksState.getInstance().getValue() ) {
						org.alice.ide.members.ProcedureFunctionPropertyTabState.getInstance().popGeneratedValue();
					} else {
						org.alice.ide.members.ProcedureFunctionControlFlowTabState.getInstance().popGeneratedValue();
					}
				}
				if( instanceFactory != null ) {
					org.alice.ide.instancefactory.croquet.InstanceFactoryState.getInstance().popGeneratedValue();
				}
			} else {
				edu.cmu.cs.dennisc.java.util.logging.Logger.errln( statement );
			}
			if( statement instanceof org.lgna.project.ast.AbstractStatementWithBody ) {
				org.lgna.project.ast.AbstractStatementWithBody statementWithBody = (org.lgna.project.ast.AbstractStatementWithBody)statement;
				BlockStatementGenerator.generateAndAddToTransactionHistory( history, statementWithBody.body.getValue() );
			}
		}
	}
}
