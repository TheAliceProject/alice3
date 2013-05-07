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
	private final Class<? extends org.lgna.project.ast.Statement> statementCls;
	private final org.lgna.project.ast.Statement possiblyIncompleteStatement;

	public StatementTemplateDragModel( java.util.UUID id, Class<? extends org.lgna.project.ast.Statement> statementCls, org.lgna.project.ast.Statement possiblyIncompleteStatement ) {
		super( id );
		this.statementCls = statementCls;
		this.possiblyIncompleteStatement = possiblyIncompleteStatement;

		if( org.alice.ide.croquet.models.ui.preferences.IsAlwaysShowingBlocksState.getInstance().getValue() ) {
			// pass
		} else {
			this.addContextFactory( org.alice.ide.members.MembersComposite.getInstance().getTabState() );
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

	public Class<? extends org.lgna.project.ast.Statement> getStatementCls() {
		return this.statementCls;
	}

	public void generateAndAddStepsToTransaction( org.lgna.croquet.history.TransactionHistory history, org.lgna.project.ast.Statement statement, org.lgna.project.ast.Expression[] initialExpressions ) throws org.lgna.croquet.UnsupportedGenerationException {
		org.alice.ide.ast.draganddrop.BlockStatementIndexPair blockStatementIndexPair = org.alice.ide.ast.draganddrop.BlockStatementIndexPair.createInstanceFromChildStatement( statement );
		org.lgna.croquet.triggers.DropTrigger dropTrigger = org.lgna.croquet.triggers.DropTrigger.createGeneratorInstance( blockStatementIndexPair );
		org.lgna.croquet.Model tempDropModel = this.getDropModel( null, blockStatementIndexPair );
		if( tempDropModel instanceof org.lgna.croquet.OwnedByCompositeOperation ) {
			org.lgna.croquet.OwnedByCompositeOperation ownedByCompositeOperation = (org.lgna.croquet.OwnedByCompositeOperation)tempDropModel;
			org.lgna.croquet.history.CompletionStep completionStep = null;
			org.lgna.croquet.edits.Edit edit = new org.alice.ide.croquet.edits.ast.InsertStatementEdit( completionStep, blockStatementIndexPair, statement, initialExpressions );
			org.lgna.croquet.history.Transaction transaction = ownedByCompositeOperation.addGeneratedTransaction( history, org.lgna.croquet.triggers.DropTrigger.createGeneratorInstance( blockStatementIndexPair ), edit, null );
			org.lgna.croquet.history.DragStep dragStep = org.lgna.croquet.history.DragStep.createAndAddToTransaction( transaction, this, org.lgna.croquet.triggers.DragTrigger.createGeneratorInstance() );
		} else {
			org.lgna.croquet.history.Transaction transaction = new org.lgna.croquet.history.Transaction( history );
			org.lgna.croquet.history.DragStep dragStep = org.lgna.croquet.history.DragStep.createAndAddToTransaction( transaction, this, org.lgna.croquet.triggers.DragTrigger.createGeneratorInstance() );
			org.lgna.croquet.Model dropModel = this.getDropModel( dragStep, blockStatementIndexPair );
			history.addTransaction( transaction );
			if( dropModel instanceof org.alice.ide.croquet.models.ast.cascade.statement.StatementInsertCascade ) {
				org.alice.ide.croquet.models.ast.cascade.statement.StatementInsertCascade statementInsertCascade = (org.alice.ide.croquet.models.ast.cascade.statement.StatementInsertCascade)dropModel;
				java.util.List<org.lgna.project.ast.Expression> expressionList = statementInsertCascade.generateAndAddPostDragStepsToTransaction( transaction, statement, blockStatementIndexPair );
				initialExpressions = edu.cmu.cs.dennisc.java.lang.ArrayUtilities.createArray( expressionList, org.lgna.project.ast.Expression.class );
			} else if( dropModel instanceof org.alice.ide.croquet.models.ast.cascade.statement.StatementInsertOperation ) {
				org.alice.ide.croquet.models.ast.cascade.statement.StatementInsertOperation statementInsertOperation = (org.alice.ide.croquet.models.ast.cascade.statement.StatementInsertOperation)dropModel;
				org.lgna.croquet.history.CompletionStep.createAndAddToTransaction( transaction, statementInsertOperation, dropTrigger, null );
			} else {
				throw new org.lgna.cheshire.ast.UnsupportedNodeGenerationException( statement );
			}
			org.lgna.croquet.history.CompletionStep<?> completionStep = transaction.getCompletionStep();
			if( completionStep != null ) {
				completionStep.setEdit( new org.alice.ide.croquet.edits.ast.InsertStatementEdit( completionStep, blockStatementIndexPair, statement, initialExpressions ) );
			}
		}
	}
}
