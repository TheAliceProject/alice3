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
	private BlockStatementGenerator() {
		throw new AssertionError();
	}

	private static final java.util.Map<Class<? extends org.lgna.project.ast.Statement>, StatementGenerator> mapStatementClassToGenerator = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
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
		mapStatementClassToGenerator.put( org.lgna.project.ast.LocalDeclarationStatement.class, org.alice.ide.ast.draganddrop.statement.DeclareLocalDragModel.getInstance() );
	}

	private static org.alice.ide.ast.draganddrop.BlockStatementIndexPair createRetargetedLocation( org.alice.ide.ast.draganddrop.BlockStatementIndexPair original, org.alice.ide.ast.draganddrop.BlockStatementIndexPair destination ) {
		org.lgna.project.ast.BlockStatement nextBlockStatement = destination.getBlockStatement();
		int nextIndex = destination.getIndex() + original.getIndex();
		return new org.alice.ide.ast.draganddrop.BlockStatementIndexPair( nextBlockStatement, nextIndex );
	}

	public static void generateAndAddToTransactionHistory( final org.lgna.croquet.history.TransactionHistory history, org.lgna.project.ast.BlockStatement blockStatement ) throws org.lgna.croquet.UnsupportedGenerationException {
		for( org.lgna.project.ast.Statement statement : blockStatement.statements ) {
			if( statement.isEnabled.getValue() ) {
				StatementGenerator statementGenerator;
				org.alice.ide.instancefactory.InstanceFactory instanceFactory = null;
				org.alice.ide.member.MemberOrControlFlowTabComposite<?> templateComposite = null;
				org.lgna.project.ast.MethodInvocation methodInvocation = null;
				org.lgna.project.ast.Expression[] initialExpressions = {};
				org.alice.ide.members.MembersComposite membersComposite = org.alice.ide.members.MembersComposite.getInstance();
				if( statement instanceof org.lgna.project.ast.ExpressionStatement ) {
					org.lgna.project.ast.ExpressionStatement expressionStatement = (org.lgna.project.ast.ExpressionStatement)statement;
					org.lgna.project.ast.Expression expression = expressionStatement.expression.getValue();
					if( expression instanceof org.lgna.project.ast.MethodInvocation ) {
						methodInvocation = (org.lgna.project.ast.MethodInvocation)expression;

						org.lgna.project.ast.AbstractMethod method = methodInvocation.method.getValue();

						if( method instanceof org.lgna.project.ast.UserMethod ) {
							final org.lgna.project.ast.UserMethod userMethod = (org.lgna.project.ast.UserMethod)method;
							//todo: check to see if generation actually required

							org.lgna.project.ast.NamedUserType rootType = (org.lgna.project.ast.NamedUserType)userMethod.getDeclaringType();
							edu.cmu.cs.dennisc.java.util.logging.Logger.todo( "root type should be program type", rootType );

							//							org.lgna.project.ast.UserMethod userMethodRetarget = org.lgna.project.ast.AstUtilities.createCopyWithoutBodyStatements( userMethod, rootType, false );
							//							//note: when parameters rear their ugly heads, we will need to address them too
							//							org.alice.ide.croquet.codecs.NodeCodec.addNodeToGlobalMap( userMethodRetarget );

							org.lgna.project.ast.UserType<?> declaringType = userMethod.getDeclaringType();
							org.alice.ide.croquet.edits.ast.DeclareMethodEdit declareMethodEdit = new org.alice.ide.croquet.edits.ast.DeclareMethodEdit( null, declaringType, userMethod.getName(), userMethod.getReturnType() );
							declareMethodEdit.EPIC_HACK_FOR_TUTORIAL_GENERATION_setMethod( userMethod );

							//todo: add observer for pre and post step generation (inside of push and pop context)
							org.lgna.croquet.CompletionModel.AddGeneratedTransactionObserver observer = new org.lgna.croquet.CompletionModel.AddGeneratedTransactionObserver() {
								public void prePopGeneratedContexts() throws org.lgna.croquet.UnsupportedGenerationException {
									org.lgna.cheshire.ast.BlockStatementGenerator.generateAndAddToTransactionHistory( history, userMethod.body.getValue() );
								}
							};
							org.alice.ide.ast.declaration.AddProcedureComposite.getInstance( declaringType ).getOperation().addGeneratedTransaction( history, org.lgna.croquet.triggers.ActionEventTrigger.createGeneratorInstance(), declareMethodEdit, observer );
						}

						org.lgna.project.ast.Expression instanceExpression = methodInvocation.expression.getValue();

						final int N = methodInvocation.requiredArguments.size();
						initialExpressions = new org.lgna.project.ast.Expression[ N ];
						for( int i = 0; i < N; i++ ) {
							initialExpressions[ i ] = methodInvocation.requiredArguments.get( i ).expression.getValue();
						}
						instanceFactory = org.alice.ide.instancefactory.InstanceFactoryUtilities.getInstanceFactoryForExpression( instanceExpression );
						if( instanceFactory != null ) {
							//pass
						} else {
							edu.cmu.cs.dennisc.java.util.logging.Logger.severe( instanceExpression );
						}
						statementGenerator = org.alice.ide.ast.draganddrop.statement.ProcedureInvocationTemplateDragModel.getInstance( method );

						boolean isFieldTemplateCompositeValid = org.alice.ide.croquet.models.ui.preferences.IsAlwaysShowingBlocksState.getInstance().getValue();
						//todo
						if( method.isProcedure() ) {
							templateComposite = membersComposite.getProcedureTabComposite();
						} else {
							templateComposite = membersComposite.getFunctionTabComposite();
						}
					} else {
						org.lgna.croquet.Application.getActiveInstance().showMessageDialog( "todo: handle expression " + expression );
						statementGenerator = null;
					}
				} else {
					statementGenerator = mapStatementClassToGenerator.get( statement.getClass() );
					if( org.alice.ide.croquet.models.ui.preferences.IsAlwaysShowingBlocksState.getInstance().getValue() ) {
						// pass
					} else {
						templateComposite = membersComposite.getControlStructureTabComposite();
					}
				}
				boolean isReorderingDesired = statement instanceof org.lgna.project.ast.CountLoop;

				if( isReorderingDesired ) {
					if( statement instanceof org.lgna.project.ast.AbstractStatementWithBody ) {
						org.lgna.project.ast.AbstractStatementWithBody statementWithBody = (org.lgna.project.ast.AbstractStatementWithBody)statement;
						BlockStatementGenerator.generateAndAddToTransactionHistory( history, statementWithBody.body.getValue() );
					}
				}

				if( statementGenerator != null ) {
					if( instanceFactory != null ) {
						org.alice.ide.instancefactory.croquet.InstanceFactoryState.getInstance().pushGeneratedValue( instanceFactory );
					}
					if( templateComposite != null ) {
						membersComposite.getTabState().pushGeneratedValue( templateComposite );
					}
					statementGenerator.generateAndAddStepsToTransaction( history, statement, initialExpressions );
					if( templateComposite != null ) {
						membersComposite.getTabState().popGeneratedValue();
					}
					if( instanceFactory != null ) {
						org.alice.ide.instancefactory.croquet.InstanceFactoryState.getInstance().popGeneratedValue();
					}
				} else {
					throw new UnsupportedNodeGenerationException( statement );
				}
				if( methodInvocation != null ) {
					org.alice.ide.croquet.models.ast.keyed.KeyedMoreCascade moreCascade = org.alice.ide.croquet.models.ast.keyed.KeyedMoreCascade.getInstance( methodInvocation );
					for( org.lgna.project.ast.JavaKeyedArgument argument : methodInvocation.keyedArguments ) {
						org.lgna.croquet.history.Transaction transaction = org.lgna.croquet.history.Transaction.createAndAddToHistory( history );
						org.lgna.croquet.history.PopupPrepStep.createAndAddToTransaction( transaction, moreCascade.getRoot().getPopupPrepModel(), org.lgna.croquet.triggers.MouseEventTrigger.createGeneratorInstance() );

						java.util.List<org.lgna.croquet.MenuItemPrepModel> prepModels = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
						prepModels.add( org.alice.ide.croquet.models.ast.keyed.JavaKeyedArgumentFillIn.getInstance( argument.getKeyMethod() ) );

						org.alice.ide.croquet.models.MenuBarComposite menuBarComposite = null;
						org.lgna.croquet.MenuItemPrepModel[] menuItemPrepModels = edu.cmu.cs.dennisc.java.lang.ArrayUtilities.createArray( prepModels, org.lgna.croquet.MenuItemPrepModel.class );
						org.lgna.croquet.history.MenuItemSelectStep.createAndAddToTransaction( transaction, menuBarComposite, menuItemPrepModels, org.lgna.croquet.triggers.ChangeEventTrigger.createGeneratorInstance() );

						org.lgna.croquet.history.TransactionHistory[] bufferForCompletionStepSubTransactionHistory = { null };

						org.lgna.project.ast.MethodInvocation keyedArgumentMethodInvocation = (org.lgna.project.ast.MethodInvocation)argument.expression.getValue();

						org.lgna.croquet.CascadeFillIn fillIn = ExpressionFillInGenerator.generateFillInForExpression( keyedArgumentMethodInvocation.requiredArguments.get( 0 ).expression.getValue(), bufferForCompletionStepSubTransactionHistory );

						if( fillIn != null ) {
							prepModels.add( fillIn );
							menuItemPrepModels = edu.cmu.cs.dennisc.java.lang.ArrayUtilities.createArray( prepModels, org.lgna.croquet.MenuItemPrepModel.class );
							org.lgna.croquet.history.MenuItemSelectStep.createAndAddToTransaction( transaction, menuBarComposite, menuItemPrepModels, org.lgna.croquet.triggers.ChangeEventTrigger.createGeneratorInstance() );
						}

						org.lgna.croquet.history.CompletionStep completionStep = org.lgna.croquet.history.CompletionStep.createAndAddToTransaction( transaction, moreCascade, org.lgna.croquet.triggers.MouseEventTrigger.createGeneratorInstance(), bufferForCompletionStepSubTransactionHistory[ 0 ] );
						completionStep.ACCEPTABLE_HACK_FOR_TUTORIAL_setEdit( new org.alice.ide.croquet.edits.ast.keyed.AddKeyedArgumentEdit( completionStep, argument ) );
					}
				}
				if( isReorderingDesired ) {
					if( statement instanceof org.lgna.project.ast.AbstractStatementWithBody ) {
						final org.lgna.project.ast.AbstractStatementWithBody statementWithBody = (org.lgna.project.ast.AbstractStatementWithBody)statement;
						final org.alice.ide.ast.draganddrop.BlockStatementIndexPair destination = org.alice.ide.ast.draganddrop.BlockStatementIndexPair.createInstanceFromChildStatement( statementWithBody );
						org.lgna.croquet.Retargeter retargeter = new org.lgna.croquet.Retargeter() {
							public void addKeyValuePair( java.lang.Object key, java.lang.Object value ) {
							}

							public <T> T retarget( T value ) {
								if( value instanceof org.alice.ide.ast.draganddrop.BlockStatementIndexPair ) {
									org.alice.ide.ast.draganddrop.BlockStatementIndexPair blockStatementIndexPair = (org.alice.ide.ast.draganddrop.BlockStatementIndexPair)value;
									if( blockStatementIndexPair.getBlockStatement() == statementWithBody.body.getValue() ) {
										//										org.lgna.project.ast.BlockStatement nextBlockStatement = destination.getBlockStatement();
										//										int nextIndex = destination.getIndex() + blockStatementIndexPair.getIndex();
										//										return (T)new org.alice.ide.ast.draganddrop.BlockStatementIndexPair( nextBlockStatement, nextIndex );
										return (T)createRetargetedLocation( blockStatementIndexPair, destination );
									}
								}
								return value;
							}
						};
						history.retarget( retargeter );

						for( org.lgna.project.ast.Statement subStatement : statementWithBody.body.getValue().statements ) {
							org.lgna.croquet.DragModel dragModel = org.alice.ide.ast.draganddrop.statement.StatementDragModel.getInstance( subStatement );

							org.alice.ide.ast.draganddrop.BlockStatementIndexPair toLocation = org.alice.ide.ast.draganddrop.BlockStatementIndexPair.createInstanceFromChildStatement( subStatement );
							org.alice.ide.ast.draganddrop.BlockStatementIndexPair fromLocation = createRetargetedLocation( toLocation, destination );

							org.lgna.croquet.triggers.DragTrigger dragTrigger = org.lgna.croquet.triggers.DragTrigger.createGeneratorInstance();
							org.lgna.croquet.triggers.DropTrigger dropTrigger = org.lgna.croquet.triggers.DropTrigger.createGeneratorInstance( toLocation );
							//org.lgna.croquet.Model tempDropModel = lastStatementDragModel.getDropModel( null, nextLocation );
							org.alice.ide.ast.code.MoveStatementOperation dropModel = org.alice.ide.ast.code.MoveStatementOperation.getInstance( fromLocation, subStatement, toLocation );

							org.lgna.croquet.history.Transaction moveStatementTransaction = org.lgna.croquet.history.Transaction.createAndAddToHistory( history );
							org.lgna.croquet.history.DragStep.createAndAddToTransaction( moveStatementTransaction, dragModel, dragTrigger );
							org.lgna.croquet.history.CompletionStep completionStep = org.lgna.croquet.history.CompletionStep.createAndAddToTransaction( moveStatementTransaction, dropModel, dropTrigger, null );
							org.alice.ide.ast.code.edits.MoveStatementEdit moveStatementEdit = new org.alice.ide.ast.code.edits.MoveStatementEdit( completionStep, fromLocation, subStatement, toLocation );
						}
					}
				} else {
					if( statement instanceof org.lgna.project.ast.AbstractStatementWithBody ) {
						org.lgna.project.ast.AbstractStatementWithBody statementWithBody = (org.lgna.project.ast.AbstractStatementWithBody)statement;
						BlockStatementGenerator.generateAndAddToTransactionHistory( history, statementWithBody.body.getValue() );
					}
				}
			}
		}
	}
}
