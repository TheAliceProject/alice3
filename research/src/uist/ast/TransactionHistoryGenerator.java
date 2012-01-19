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
package uist.ast;

/**
 * @author Dennis Cosgrove
 */
public class TransactionHistoryGenerator {
	private org.lgna.project.ast.BlockStatement src;
	private org.lgna.project.ast.BlockStatement dst;
	private int dstIndex0;
	public TransactionHistoryGenerator( org.lgna.project.ast.BlockStatement src, org.lgna.project.ast.BlockStatement dst, int dstIndex0 ) {
		this.src = src;
		this.dst = dst;
		this.dstIndex0 = dstIndex0;
	}

	public org.lgna.croquet.history.TransactionHistory generate( org.lgna.croquet.UserInformation userInformation ) {
		// For now
		assert userInformation == null;
		
		org.lgna.croquet.history.TransactionHistory rv = new org.lgna.croquet.history.TransactionHistory();
		//generate( rv, this.src, this.dst, this.dstIndex0 );
		
		// TODO: Do we want this?
		rv.addTransaction( createOpenAndClosePlainDialogOperationTransaction( rv, org.alice.stageide.croquet.models.run.RunOperation.getInstance() ) );
		return rv;
	}
	
	private static org.lgna.croquet.history.Transaction generateMethodDeclarationTransaction( org.lgna.croquet.history.TransactionHistory transactionHistory, org.lgna.project.ast.UserMethod userMethod ) {
		org.lgna.croquet.history.Transaction rv = new org.lgna.croquet.history.Transaction( transactionHistory );
		
		org.lgna.project.ast.UserType<?> declaringType = userMethod.getDeclaringType();
		org.alice.ide.croquet.models.declaration.MethodDeclarationOperation operation;
		if( userMethod.isProcedure() ) {
			operation = org.alice.ide.croquet.models.declaration.ProcedureDeclarationOperation.getInstance( declaringType );
		} else {
			operation = org.alice.ide.croquet.models.declaration.FunctionDeclarationOperation.getInstance( declaringType );
		}
		
		org.lgna.croquet.history.InputDialogOperationStep step = org.lgna.croquet.history.InputDialogOperationStep.createAndAddToTransaction( rv, operation, new org.lgna.croquet.triggers.SimulatedTrigger() );
		org.alice.ide.croquet.edits.ast.DeclareMethodEdit edit = new org.alice.ide.croquet.edits.ast.DeclareMethodEdit( step, declaringType, userMethod );
		addEdit( step, edit );
		return rv;
	}

	private static void generate( org.lgna.croquet.history.TransactionHistory history, org.lgna.project.ast.BlockStatement src, org.lgna.project.ast.BlockStatement dst, int dstIndex0 ) {
		int dstIndex = dstIndex0; 
		for( org.lgna.project.ast.Statement statement : src.statements ) {
			org.lgna.croquet.history.Transaction transaction = new org.lgna.croquet.history.Transaction( history );
			
		}
//			org.alice.ide.croquet.models.ast.InsertStatementActionOperation insertStatementActionOperation = new org.alice.ide.croquet.models.ast.InsertStatementActionOperation( dst, dstIndex, statement );
//			org.lgna.croquet.history.ActionOperationStep insertStatementContext = org.lgna.croquet.history.ActionOperationStep.createAndAddToTransaction( transaction, insertStatementActionOperation, new org.lgna.croquet.triggers.SimulatedTrigger() );
//			addEdit( insertStatementContext, new org.alice.ide.croquet.edits.DependentEdit() );
//
//			org.lgna.croquet.DragModel dragModel;
//			if( statement instanceof org.lgna.project.ast.ExpressionStatement ) {
//				org.lgna.project.ast.ExpressionStatement expressionStatement = (org.lgna.project.ast.ExpressionStatement)statement;
//				org.lgna.project.ast.Expression expression = expressionStatement.expression.getValue();
//				if( expression instanceof org.lgna.project.ast.MethodInvocation ) {
//					org.lgna.project.ast.MethodInvocation methodInvocation = (org.lgna.project.ast.MethodInvocation)expression;
//					org.lgna.project.ast.AbstractMethod method = methodInvocation.method.getValue();
//					if( method instanceof org.lgna.project.ast.UserMethod ) {
//						org.lgna.project.ast.UserMethod userMethod = (org.lgna.project.ast.UserMethod)method;
//						history.addTransaction( generateMethodDeclarationTransaction( history, userMethod ) );
//
//						generate( history, userMethod.body.getValue() );
//
//						org.lgna.project.ast.UserMethod invokedFromMethod = dst.getFirstAncestorAssignableTo( org.lgna.project.ast.UserMethod.class );
//
//						org.lgna.croquet.history.ListSelectionStateChangeStep< org.alice.ide.croquet.models.typeeditor.DeclarationComposite > listSelectionStateChangeStep = org.lgna.croquet.history.ListSelectionStateChangeStep< org.alice.ide.editorstabbedpane.CodeComposite >( org.alice.ide.editorstabbedpane.EditorsTabSelectionState.getInstance() );
//						org.lgna.croquet.edits.ListSelectionStateEdit< org.alice.ide.croquet.models.typeeditor.DeclarationComposite > edit = new org.lgna.croquet.edits.ListSelectionStateEdit< org.alice.ide.croquet.models.typeeditor.DeclarationComposite >( org.alice.ide.editorstabbedpane.CodeComposite.getInstance( userMethod ), org.alice.ide.croquet.models.typeeditor.DeclarationComposite.getInstance( invokedFromMethod ) );
//						addEdit( listSelectionStateContext, edit );
//						history.addChild( listSelectionStateContext );
//					}
//					dragModel = org.alice.ide.croquet.models.ast.MethodTemplateDragModel.getInstance( method );
//				} else {
//					dragModel = null;
//				}
//			} else {
//				dragModel = org.alice.ide.croquet.models.ast.StatementClassTemplateDragModel.getInstance( statement.getClass() );
//			}
//
//			org.lgna.croquet.DragAndDropContext dragAndDropContext = createDragAndDropContext( dragModel ); 
//			org.lgna.croquet.DropReceptor dropReceptor = null;
//			org.lgna.croquet.DropSite dropSite = new org.alice.ide.ast.draganddrop.BlockStatementIndexPair( dst, dstIndex );
//			org.lgna.croquet.DragAndDropContext.EnteredDropReceptorEvent enteredDropReceptorEvent = new org.lgna.croquet.DragAndDropContext.EnteredDropReceptorEvent( dropReceptor );
//			org.lgna.croquet.DragAndDropContext.EnteredPotentialDropSiteEvent enteredPotentialDropSiteEvent = new org.lgna.croquet.DragAndDropContext.EnteredPotentialDropSiteEvent( dropReceptor, dropSite );
//			org.lgna.croquet.DragAndDropContext.DroppedEvent droppedEvent = new org.lgna.croquet.DragAndDropContext.DroppedEvent( dropReceptor );
//			dragAndDropContext.addChild( enteredDropReceptorEvent );
//			dragAndDropContext.addChild( enteredPotentialDropSiteEvent );
//			dragAndDropContext.addChild( droppedEvent );
//
//			if( statement instanceof org.lgna.project.ast.CountLoop ) {
//				org.lgna.croquet.CascadePopupOperation popupMenuOperation = null;
//				org.lgna.croquet.CascadePopupOperationContext popupMenuOperationContext = new org.lgna.croquet.CascadePopupOperationContext( popupMenuOperation );
//				java.util.List< org.lgna.croquet.Model > models = edu.cmu.cs.dennisc.java.util.Collections.newArrayList();
//
//
//				//todo
//				//models.add( edu.cmu.cs.dennisc.cascade.InternalCascadingItemOperation.getInstance( edu.cmu.cs.dennisc.alice.Project.GROUP, null ) ); 
//
//
//				org.lgna.croquet.PopupOperationContext.MenuSelectionEvent menuSelectionEvent = new org.lgna.croquet.PopupOperationContext.MenuSelectionEvent( models );
//				popupMenuOperationContext.addChild( menuSelectionEvent );
//				popupMenuOperationContext.addChild( insertStatementContext );
//				dragAndDropContext.addChild( popupMenuOperationContext );
//			} else {
//				dragAndDropContext.addChild( insertStatementContext );
//			}
//
//
//			dstIndex++;
//			history.addChild( dragAndDropContext );
//
//			if( statement instanceof org.lgna.project.ast.AbstractStatementWithBody ) {
//				org.lgna.project.ast.AbstractStatementWithBody statementWithBody = (org.lgna.project.ast.AbstractStatementWithBody)statement;
//				generate( history, statementWithBody.body.getValue() );
//			}
//		}
	}

	private static <M extends org.lgna.croquet.CompletionModel> org.lgna.croquet.history.CompletionStep< M > addEdit( org.lgna.croquet.history.CompletionStep< M > rv, org.lgna.croquet.edits.Edit< M > edit ) {
//		org.lgna.croquet.history.CommitEvent commitEvent = new org.lgna.croquet.history.CommitEvent( edit );
//		rv.addChild( commitEvent );
//		edit.setContext( rv );
		return rv;
	}
	
	private static org.lgna.croquet.history.Transaction createOpenAndClosePlainDialogOperationTransaction( org.lgna.croquet.history.TransactionHistory parent, org.lgna.croquet.PlainDialogOperation plainDialogOperation ) {
		org.lgna.croquet.history.Transaction rv = new org.lgna.croquet.history.Transaction( parent );
		org.lgna.croquet.history.PlainDialogOperationStep step = org.lgna.croquet.history.PlainDialogOperationStep.createAndAddToTransaction( rv, plainDialogOperation, new org.lgna.croquet.triggers.SimulatedTrigger() );
		step.finish();
		
		// TODO: This may not be right. this should probably be step.getParent()
		org.lgna.croquet.history.PlainDialogCloseOperationStep step2 = org.lgna.croquet.history.PlainDialogCloseOperationStep.createAndAddToTransaction( step.getTransactionHistory().getActiveTransaction(), plainDialogOperation.getCloseOperation(), new org.lgna.croquet.triggers.SimulatedTrigger());
		step2.finish();
		return rv;
	}
	
	public static void main( String[] args ) {
		org.lgna.project.ast.BlockStatement dst = new org.lgna.project.ast.BlockStatement(
				org.lgna.project.ast.AstUtilities.createDoInOrder()
		);
		org.lgna.project.ast.BlockStatement src = new org.lgna.project.ast.BlockStatement(
				org.lgna.project.ast.AstUtilities.createDoTogether(),
				org.lgna.project.ast.AstUtilities.createCountLoop( new org.lgna.project.ast.IntegerLiteral( 3 ) )
		);
		
		TransactionHistoryGenerator generator = new TransactionHistoryGenerator( src, dst, 1 );
//		org.lgna.croquet.RootContext rootContext = generator.generate( null );
//		edu.cmu.cs.dennisc.print.PrintUtilities.println( rootContext );
	}
}
