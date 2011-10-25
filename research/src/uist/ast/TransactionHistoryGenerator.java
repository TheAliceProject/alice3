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
	public org.lgna.croquet.history.TransactionHistory generate() {
		throw new RuntimeException( "todo" );
	}
//	public edu.cmu.cs.dennisc.croquet.RootContext generate( edu.cmu.cs.dennisc.croquet.UserInformation userInformation ) {
//		edu.cmu.cs.dennisc.croquet.RootContext rv = new edu.cmu.cs.dennisc.croquet.RootContext();
//		generate( rv, this.src, this.dst, this.dstIndex0 );
//		rv.addChild( createDialogOperationContext( org.alice.stageide.croquet.models.run.RunOperation.getInstance() ) );
//		return rv;
//	}
//	
//	private static edu.cmu.cs.dennisc.croquet.OperationContext< ? > generateMethodDeclarationContext( org.lgna.project.ast.MethodDeclaredInAlice methodInAlice ) {
//		org.lgna.project.ast.AbstractTypeDeclaredInAlice<?> declaringType = methodInAlice.getDeclaringType();
//		org.alice.ide.croquet.models.ast.DeclareMethodOperation declareMethodOperation;
//		if( methodInAlice.isProcedure() ) {
//			declareMethodOperation = org.alice.ide.croquet.models.ast.DeclareProcedureOperation.getInstance( declaringType );
//		} else {
//			declareMethodOperation = org.alice.ide.croquet.models.ast.DeclareFunctionOperation.getInstance( declaringType );
//		}
//		edu.cmu.cs.dennisc.croquet.OperationContext< org.alice.ide.croquet.models.ast.DeclareMethodOperation > rv = new org.lgna.croquet.steps.InputDialogOperationStep( declareMethodOperation );
//		
//		org.alice.ide.croquet.edits.ast.DeclareMethodEdit edit = new org.alice.ide.croquet.edits.ast.DeclareMethodEdit( declaringType, methodInAlice );
//		addEdit( rv, edit );
//		return rv;
//	}
//	
//	private static edu.cmu.cs.dennisc.croquet.ModelContext< ? > generate( edu.cmu.cs.dennisc.croquet.ModelContext< ? > rv, org.lgna.project.ast.BlockStatement blockStatement ) {
//		//will get re-targeted later
//		return generate( rv, blockStatement, blockStatement, 0 );
//	}
//	
//	private static edu.cmu.cs.dennisc.croquet.ModelContext< ? > generate( edu.cmu.cs.dennisc.croquet.ModelContext< ? > rv, org.lgna.project.ast.BlockStatement src, org.lgna.project.ast.BlockStatement dst, int dstIndex0 ) {
//		int dstIndex = dstIndex0; 
//		for( org.lgna.project.ast.Statement statement : src.statements ) {
//			org.alice.ide.croquet.models.ast.InsertStatementActionOperation insertStatementActionOperation = new org.alice.ide.croquet.models.ast.InsertStatementActionOperation( dst, dstIndex, statement );
//			edu.cmu.cs.dennisc.croquet.ActionOperationContext insertStatementContext = new edu.cmu.cs.dennisc.croquet.ActionOperationContext( insertStatementActionOperation );
//			addEdit( insertStatementContext, new org.alice.ide.croquet.edits.DependentEdit() );
//			
//			edu.cmu.cs.dennisc.croquet.DragAndDropModel dragAndDropModel;
//			if( statement instanceof org.lgna.project.ast.ExpressionStatement ) {
//				org.lgna.project.ast.ExpressionStatement expressionStatement = (org.lgna.project.ast.ExpressionStatement)statement;
//				org.lgna.project.ast.Expression expression = expressionStatement.expression.getValue();
//				if( expression instanceof org.lgna.project.ast.MethodInvocation ) {
//					org.lgna.project.ast.MethodInvocation methodInvocation = (org.lgna.project.ast.MethodInvocation)expression;
//					org.lgna.project.ast.AbstractMethod method = methodInvocation.method.getValue();
//					if( method instanceof org.lgna.project.ast.MethodDeclaredInAlice ) {
//						org.lgna.project.ast.MethodDeclaredInAlice methodInAlice = (org.lgna.project.ast.MethodDeclaredInAlice)method;
//						rv.addChild( generateMethodDeclarationContext( methodInAlice ) );
//						
//						generate( rv, methodInAlice.body.getValue() );
//
//						org.lgna.project.ast.MethodDeclaredInAlice invokedFromMethod = dst.getFirstAncestorAssignableTo( org.lgna.project.ast.MethodDeclaredInAlice.class );
//						
//						edu.cmu.cs.dennisc.croquet.ListSelectionStateContext< org.alice.ide.editorstabbedpane.CodeComposite > listSelectionStateContext = new edu.cmu.cs.dennisc.croquet.ListSelectionStateContext< org.alice.ide.editorstabbedpane.CodeComposite >( org.alice.ide.editorstabbedpane.EditorsTabSelectionState.getInstance() );
//						org.lgna.croquet.edits.ListSelectionStateEdit< org.alice.ide.editorstabbedpane.CodeComposite > edit = new org.lgna.croquet.edits.ListSelectionStateEdit< org.alice.ide.editorstabbedpane.CodeComposite >( org.alice.ide.editorstabbedpane.CodeComposite.getInstance( methodInAlice ), org.alice.ide.editorstabbedpane.CodeComposite.getInstance( invokedFromMethod ) );
//						addEdit( listSelectionStateContext, edit );
//						rv.addChild( listSelectionStateContext );
//					}
//					dragAndDropModel = org.alice.ide.croquet.models.ast.MethodTemplateDragModel.getInstance( method );
//				} else {
//					dragAndDropModel = null;
//				}
//			} else {
//				dragAndDropModel = org.alice.ide.croquet.models.ast.StatementClassTemplateDragModel.getInstance( statement.getClass() );
//			}
//			
//			edu.cmu.cs.dennisc.croquet.DragAndDropContext dragAndDropContext = createDragAndDropContext( dragAndDropModel ); 
//			edu.cmu.cs.dennisc.croquet.DropReceptor dropReceptor = null;
//			edu.cmu.cs.dennisc.croquet.DropSite dropSite = new org.alice.ide.codeeditor.BlockStatementIndexPair( dst, dstIndex );
//			edu.cmu.cs.dennisc.croquet.DragAndDropContext.EnteredDropReceptorEvent enteredDropReceptorEvent = new edu.cmu.cs.dennisc.croquet.DragAndDropContext.EnteredDropReceptorEvent( dropReceptor );
//			edu.cmu.cs.dennisc.croquet.DragAndDropContext.EnteredPotentialDropSiteEvent enteredPotentialDropSiteEvent = new edu.cmu.cs.dennisc.croquet.DragAndDropContext.EnteredPotentialDropSiteEvent( dropReceptor, dropSite );
//			edu.cmu.cs.dennisc.croquet.DragAndDropContext.DroppedEvent droppedEvent = new edu.cmu.cs.dennisc.croquet.DragAndDropContext.DroppedEvent( dropReceptor );
//			dragAndDropContext.addChild( enteredDropReceptorEvent );
//			dragAndDropContext.addChild( enteredPotentialDropSiteEvent );
//			dragAndDropContext.addChild( droppedEvent );
//			
//			if( statement instanceof org.lgna.project.ast.CountLoop ) {
//				edu.cmu.cs.dennisc.croquet.CascadePopupOperation popupMenuOperation = null;
//				edu.cmu.cs.dennisc.croquet.CascadePopupOperationContext popupMenuOperationContext = new edu.cmu.cs.dennisc.croquet.CascadePopupOperationContext( popupMenuOperation );
//				java.util.List< edu.cmu.cs.dennisc.croquet.Model > models = edu.cmu.cs.dennisc.java.util.Collections.newArrayList();
//				
//				
//				//todo
//				//models.add( edu.cmu.cs.dennisc.cascade.InternalCascadingItemOperation.getInstance( edu.cmu.cs.dennisc.alice.Project.GROUP, null ) ); 
//				
//				
//				edu.cmu.cs.dennisc.croquet.PopupOperationContext.MenuSelectionEvent menuSelectionEvent = new edu.cmu.cs.dennisc.croquet.PopupOperationContext.MenuSelectionEvent( models );
//				popupMenuOperationContext.addChild( menuSelectionEvent );
//				popupMenuOperationContext.addChild( insertStatementContext );
//				dragAndDropContext.addChild( popupMenuOperationContext );
//			} else {
//				dragAndDropContext.addChild( insertStatementContext );
//			}
//			
//			
//			dstIndex++;
//			rv.addChild( dragAndDropContext );
//			
//			if( statement instanceof org.lgna.project.ast.AbstractStatementWithBody ) {
//				org.lgna.project.ast.AbstractStatementWithBody statementWithBody = (org.lgna.project.ast.AbstractStatementWithBody)statement;
//				generate( rv, statementWithBody.body.getValue() );
//			}
//		}
//		return rv;
//	}
//
//	private static <M extends edu.cmu.cs.dennisc.croquet.CompletionModel> edu.cmu.cs.dennisc.croquet.CompletionContext< M > addEdit( edu.cmu.cs.dennisc.croquet.CompletionContext< M > rv, edu.cmu.cs.dennisc.croquet.Edit< M > edit ) {
//		edu.cmu.cs.dennisc.croquet.CommitEvent commitEvent = new edu.cmu.cs.dennisc.croquet.CommitEvent( edit );
//		rv.addChild( commitEvent );
//		edit.setContext( rv );
//		return rv;
//	}
//	private static edu.cmu.cs.dennisc.croquet.DragAndDropContext createDragAndDropContext( edu.cmu.cs.dennisc.croquet.DragAndDropModel dragAndDropModel ) {
//		edu.cmu.cs.dennisc.croquet.DragAndDropContext rv = new edu.cmu.cs.dennisc.croquet.DragAndDropContext( dragAndDropModel );
//		return rv;
//	}
//	private static edu.cmu.cs.dennisc.croquet.PlainDialogOperationContext createDialogOperationContext( edu.cmu.cs.dennisc.croquet.PlainDialogOperation dialogOperation ) {
//		edu.cmu.cs.dennisc.croquet.PlainDialogOperationContext rv = new edu.cmu.cs.dennisc.croquet.PlainDialogOperationContext( dialogOperation );
//		edu.cmu.cs.dennisc.croquet.FinishEvent finishEvent = new edu.cmu.cs.dennisc.croquet.FinishEvent();
//		rv.addChild( finishEvent );
//		return rv;
//	}
	
	public static void main( String[] args ) {
		org.lgna.project.ast.BlockStatement dst = new org.lgna.project.ast.BlockStatement(
				org.lgna.project.ast.AstUtilities.createDoInOrder()
		);
		org.lgna.project.ast.BlockStatement src = new org.lgna.project.ast.BlockStatement(
				org.lgna.project.ast.AstUtilities.createDoTogether(),
				org.lgna.project.ast.AstUtilities.createCountLoop( new org.lgna.project.ast.IntegerLiteral( 3 ) )
		);
		
		TransactionHistoryGenerator generator = new TransactionHistoryGenerator( src, dst, 1 );
//		edu.cmu.cs.dennisc.croquet.RootContext rootContext = generator.generate( null );
//		edu.cmu.cs.dennisc.print.PrintUtilities.println( rootContext );
	}
}
