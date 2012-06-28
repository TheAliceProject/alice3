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

package org.alice.ide.ast.draganddrop.statement;

/**
 * @author Dennis Cosgrove
 */
public abstract class StatementTemplateDragModel extends AbstractStatementDragModel implements org.lgna.cheshire.ast.StatementGenerator {
	private final Class<? extends org.lgna.project.ast.Statement > statementCls;
	private final org.lgna.project.ast.Statement possiblyIncompleteStatement;
	public StatementTemplateDragModel( java.util.UUID id, Class<? extends org.lgna.project.ast.Statement > statementCls, org.lgna.project.ast.Statement possiblyIncompleteStatement ) {
		super( id );
		this.statementCls = statementCls;
		this.possiblyIncompleteStatement = possiblyIncompleteStatement;

		if( org.alice.ide.croquet.models.ui.preferences.IsAlwaysShowingBlocksState.getInstance().getValue() ) {
			// pass
		} else {
			this.addContextFactory( org.alice.ide.members.ProcedureFunctionControlFlowTabState.getInstance() );
		}
	}
	public org.lgna.project.ast.Statement getPossiblyIncompleteStatement() {
		return this.possiblyIncompleteStatement;
	}
	@Override
	public boolean isAddEventListenerLikeSubstance() {
		return false;
	}
	protected abstract org.lgna.croquet.Model getDropModel( org.lgna.croquet.history.DragStep step, org.alice.ide.ast.draganddrop.BlockStatementIndexPair dropSite );
	@Override
	public final org.lgna.croquet.Model getDropModel( org.lgna.croquet.history.DragStep step, org.lgna.croquet.DropSite dropSite ) {
		assert dropSite instanceof org.alice.ide.ast.draganddrop.BlockStatementIndexPair;
		org.alice.ide.ast.draganddrop.BlockStatementIndexPair blockStatementIndexPair = (org.alice.ide.ast.draganddrop.BlockStatementIndexPair)dropSite;
		return this.getDropModel( step, blockStatementIndexPair );
	}
	public Class< ? extends org.lgna.project.ast.Statement > getStatementCls() {
		return this.statementCls;
	}
	
	public void generateAndAddStepsToTransaction( org.lgna.croquet.history.TransactionHistory history, org.lgna.project.ast.Statement statement, org.lgna.project.ast.Expression[] initialExpressions ) {
		org.alice.ide.ast.draganddrop.BlockStatementIndexPair blockStatementIndexPair = org.alice.ide.ast.draganddrop.BlockStatementIndexPair.createInstanceFromChildStatement( statement );

		org.lgna.croquet.history.Transaction transaction = new org.lgna.croquet.history.Transaction( history );
		history.addTransaction( transaction );

		org.lgna.croquet.history.DragStep dragStep = org.lgna.croquet.history.DragStep.createAndAddToTransaction( transaction, this, org.lgna.croquet.triggers.DragTrigger.createGeneratorInstance() );

		org.lgna.croquet.Model dropModel = this.getDropModel( dragStep, blockStatementIndexPair );
		
		org.lgna.croquet.triggers.DropTrigger dropTrigger = org.lgna.croquet.triggers.DropTrigger.createGeneratorInstance( blockStatementIndexPair );
		
		if( dropModel instanceof org.alice.ide.croquet.models.ast.cascade.statement.StatementInsertCascade ) {
			org.alice.ide.croquet.models.ast.cascade.statement.StatementInsertCascade statementInsertCascade = (org.alice.ide.croquet.models.ast.cascade.statement.StatementInsertCascade)dropModel;
			statementInsertCascade.generateAndAddPostDragStepsToTransaction( transaction, statement, blockStatementIndexPair );
		} else if( dropModel instanceof org.alice.ide.croquet.models.ast.cascade.statement.StatementInsertOperation ) {
			org.alice.ide.croquet.models.ast.cascade.statement.StatementInsertOperation statementInsertOperation = (org.alice.ide.croquet.models.ast.cascade.statement.StatementInsertOperation)dropModel;
			org.lgna.croquet.history.CompletionStep.createAndAddToTransaction( transaction, statementInsertOperation, dropTrigger, null );
		} else if( dropModel instanceof org.lgna.croquet.OwnedByCompositeOperation ) {
			org.lgna.croquet.OwnedByCompositeOperation ownedByCompositeOperation = (org.lgna.croquet.OwnedByCompositeOperation)dropModel;
			org.lgna.croquet.OperationOwningComposite composite = ownedByCompositeOperation.getComposite();
			if( composite instanceof org.alice.ide.ast.declaration.EachInArrayComposite ) {
				org.alice.ide.ast.declaration.EachInArrayComposite eachInArrayComposite = (org.alice.ide.ast.declaration.EachInArrayComposite)composite;
				org.lgna.project.ast.ForEachInArrayLoop eachIn = (org.lgna.project.ast.ForEachInArrayLoop)statement;
				org.lgna.croquet.history.TransactionHistory subTransactionHistory = new org.lgna.croquet.history.TransactionHistory();

				org.lgna.project.ast.AbstractType itemType = eachIn.item.getValue().getValueType();
				org.alice.ide.croquet.models.declaration.ValueComponentTypeState itemState = eachInArrayComposite.getValueComponentTypeState();
				org.lgna.croquet.history.Transaction itemTypeTransaction = org.lgna.croquet.history.Transaction.createAndAddToHistory( subTransactionHistory );
				org.lgna.croquet.PopupPrepModel itemPopupPrepModel = itemState.getCascadeRoot().getPopupPrepModel();
				org.lgna.croquet.history.PopupPrepStep.createAndAddToTransaction( itemTypeTransaction, itemPopupPrepModel, org.lgna.croquet.triggers.PopupMenuEventTrigger.createGeneratorInstance() );
				org.lgna.croquet.history.CompletionStep itemTypeChangeStep = org.lgna.croquet.history.CompletionStep.createAndAddToTransaction( itemTypeTransaction, itemState, org.lgna.croquet.triggers.ChangeEventTrigger.createGeneratorInstance(), null );
				itemTypeChangeStep.setEdit( new org.lgna.croquet.edits.StateEdit<org.lgna.project.ast.AbstractType>( itemTypeChangeStep, null, itemType ) );
				
				org.lgna.croquet.history.Transaction nameTransaction = org.lgna.croquet.history.Transaction.createAndAddToHistory( subTransactionHistory );
				org.lgna.croquet.history.CompletionStep nameChangeStep = org.lgna.croquet.history.CompletionStep.createAndAddToTransaction( nameTransaction, eachInArrayComposite.getNameState(), org.lgna.croquet.triggers.DocumentEventTrigger.createGeneratorInstance(), null );
				nameChangeStep.setEdit( new org.lgna.croquet.edits.StateEdit<String>( nameChangeStep, "", eachIn.item.getValue().name.getValue() ) );

				org.alice.ide.croquet.models.declaration.ValueComponentTypeState initializerState = eachInArrayComposite.getValueComponentTypeState();
				org.lgna.croquet.history.Transaction initializerTypeTransaction = org.lgna.croquet.history.Transaction.createAndAddToHistory( subTransactionHistory );
				org.lgna.croquet.PopupPrepModel initializerPopupPrepModel = initializerState.getCascadeRoot().getPopupPrepModel();
				org.lgna.croquet.history.PopupPrepStep.createAndAddToTransaction( initializerTypeTransaction, initializerPopupPrepModel, org.lgna.croquet.triggers.PopupMenuEventTrigger.createGeneratorInstance() );


				org.lgna.croquet.history.TransactionHistory subSubTransactionHistory = new org.lgna.croquet.history.TransactionHistory();

				org.lgna.croquet.history.Transaction subCommitTransaction = org.lgna.croquet.history.Transaction.createAndAddToHistory( subTransactionHistory );
				org.lgna.croquet.history.CompletionStep<?> subCommitStep = org.lgna.croquet.history.CompletionStep.createAndAddToTransaction( subCommitTransaction, org.alice.ide.croquet.models.custom.CustomArrayInputDialogOperation.getInstance( itemType ), org.lgna.croquet.triggers.ActionEventTrigger.createGeneratorInstance(), subSubTransactionHistory );
				subCommitStep.finish();

				org.lgna.project.ast.ArrayInstanceCreation arrayInstanceCreation = (org.lgna.project.ast.ArrayInstanceCreation)eachIn.array.getValue();
				//todo generate for each expression in arrayInstanceCreation
				
				
				org.lgna.croquet.history.Transaction commitTransaction = org.lgna.croquet.history.Transaction.createAndAddToHistory( subTransactionHistory );
				org.lgna.croquet.history.CompletionStep<?> commitStep = org.lgna.croquet.history.CompletionStep.createAndAddToTransaction( commitTransaction, eachInArrayComposite.getCommitOperation(), org.lgna.croquet.triggers.ActionEventTrigger.createGeneratorInstance(), null );
				commitStep.finish();
				
				//eachInArrayComposite.getInitializerState();
				
				org.lgna.croquet.history.CompletionStep.createAndAddToTransaction( transaction, ownedByCompositeOperation, dropTrigger, subTransactionHistory );
			} else {
				edu.cmu.cs.dennisc.java.util.logging.Logger.severe( dropModel );
			}
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( dropModel );
//			assert false : dropModel;
		}
		
		org.lgna.croquet.history.CompletionStep<?> completionStep = transaction.getCompletionStep();
		if( completionStep != null ) {
			completionStep.setEdit( new org.alice.ide.croquet.edits.ast.InsertStatementEdit( completionStep, blockStatementIndexPair, statement, initialExpressions ) );
		}
		
	}
}