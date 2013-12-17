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

package org.alice.ide.croquet.models.ast.cascade.statement;

/**
 * @author Dennis Cosgrove
 */
public abstract class StatementInsertCascade extends org.alice.ide.croquet.models.ast.cascade.ExpressionsCascade implements org.alice.ide.croquet.models.ast.InsertStatementCompletionModel {

	private static boolean EPIC_HACK_isActive;

	public static boolean EPIC_HACK_isActive() {
		return EPIC_HACK_isActive;
	}

	private final org.alice.ide.ast.draganddrop.BlockStatementIndexPair blockStatementIndexPair;
	private final boolean isEnveloping;

	public StatementInsertCascade( java.util.UUID id, org.alice.ide.ast.draganddrop.BlockStatementIndexPair blockStatementIndexPair, boolean isEnveloping, org.lgna.croquet.CascadeBlank<org.lgna.project.ast.Expression>... blanks ) {
		super( org.alice.ide.IDE.PROJECT_GROUP, id, blanks );
		this.blockStatementIndexPair = blockStatementIndexPair;
		this.isEnveloping = isEnveloping;
	}

	public org.alice.ide.ast.draganddrop.BlockStatementIndexPair getBlockStatementIndexPair() {
		return this.blockStatementIndexPair;
	}

	public boolean isEnveloping() {
		return this.isEnveloping;
	}

	protected abstract java.util.List<org.lgna.project.ast.Expression> extractExpressionsForFillInGeneration( org.lgna.project.ast.Statement statement );

	public java.util.List<org.lgna.project.ast.Expression> generateAndAddPostDragStepsToTransaction( org.lgna.croquet.history.Transaction transaction, org.lgna.project.ast.Statement statement, org.alice.ide.ast.draganddrop.BlockStatementIndexPair blockStatementIndexPair ) throws org.lgna.croquet.UnsupportedGenerationException {
		java.util.List<org.lgna.project.ast.Expression> expressions = this.extractExpressionsForFillInGeneration( statement );
		org.lgna.croquet.triggers.DropTrigger dropTrigger = org.lgna.croquet.triggers.DropTrigger.createGeneratorInstance( blockStatementIndexPair );
		if( expressions.size() > 0 ) {
			org.lgna.croquet.history.PopupPrepStep.createAndAddToTransaction( transaction, this.getRoot().getPopupPrepModel(), dropTrigger );
			java.util.List<org.lgna.croquet.MenuItemPrepModel> prepModels = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
			org.lgna.croquet.history.TransactionHistory[] bufferForCompletionStepSubTransactionHistory = { null };
			for( org.lgna.project.ast.Expression expression : expressions ) {
				org.lgna.croquet.CascadeFillIn fillIn = org.lgna.cheshire.ast.ExpressionFillInGenerator.generateFillInForExpression( expression, bufferForCompletionStepSubTransactionHistory );
				if( fillIn != null ) {
					prepModels.add( fillIn );
					edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "todo: pass fill in to menu selection step ", fillIn );
				} else {
					edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "todo: handle expression ", expression );
				}
				org.lgna.croquet.MenuBarComposite menuBarComposite = null;
				org.lgna.croquet.MenuItemPrepModel[] menuItemPrepModels = edu.cmu.cs.dennisc.java.lang.ArrayUtilities.createArray( prepModels, org.lgna.croquet.MenuItemPrepModel.class );
				if( menuItemPrepModels.length > 0 ) {
					org.lgna.croquet.history.MenuItemSelectStep.createAndAddToTransaction( transaction, menuBarComposite, menuItemPrepModels, org.lgna.croquet.triggers.ChangeEventTrigger.createGeneratorInstance() );
				}
			}
			org.lgna.croquet.history.CompletionStep.createAndAddToTransaction( transaction, this, org.lgna.croquet.triggers.ActionEventTrigger.createGeneratorInstance(), bufferForCompletionStepSubTransactionHistory[ 0 ] );
		} else {
			org.lgna.croquet.history.CompletionStep.createAndAddToTransaction( transaction, this, dropTrigger, null );
		}
		return expressions;
	}

	protected abstract org.lgna.project.ast.Statement createStatement( org.lgna.project.ast.Expression... expressions );

	@Override
	protected void prologue( org.lgna.croquet.triggers.Trigger trigger ) {
		EPIC_HACK_isActive = true;
		super.prologue( trigger );
	}

	@Override
	protected void epilogue() {
		super.epilogue();
		EPIC_HACK_isActive = false;
	}

	@Override
	protected org.alice.ide.croquet.edits.ast.InsertStatementEdit createEdit( org.lgna.croquet.history.CompletionStep<org.lgna.croquet.Cascade<org.lgna.project.ast.Expression>> step, org.lgna.project.ast.Expression[] values ) {
		org.lgna.project.ast.Statement statement = this.createStatement( values );
		return new org.alice.ide.croquet.edits.ast.InsertStatementEdit( step, this.blockStatementIndexPair, statement, values, this.isEnveloping );
	}

	@Override
	protected org.lgna.croquet.edits.AbstractEdit<?> createTutorialCompletionEdit( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.croquet.edits.AbstractEdit<?> originalEdit, org.lgna.croquet.Retargeter retargeter ) {
		org.alice.ide.croquet.edits.ast.InsertStatementEdit originalInsertStatementEdit = (org.alice.ide.croquet.edits.ast.InsertStatementEdit)originalEdit;
		org.lgna.project.ast.Expression[] values = originalInsertStatementEdit.getInitialExpressions();
		org.lgna.project.ast.Expression[] retargetedValues = new org.lgna.project.ast.Expression[ values.length ];
		for( int i = 0; i < values.length; i++ ) {
			retargetedValues[ i ] = retargeter.retarget( values[ i ] );
		}
		org.lgna.project.ast.BlockStatement retargetedBlockStatement = retargeter.retarget( originalInsertStatementEdit.getBlockStatement() );
		org.lgna.project.ast.Statement statement = this.createStatement( retargetedValues );
		org.alice.ide.croquet.edits.ast.InsertStatementEdit retargetedEdit = new org.alice.ide.croquet.edits.ast.InsertStatementEdit( step, new org.alice.ide.ast.draganddrop.BlockStatementIndexPair( retargetedBlockStatement, originalInsertStatementEdit.getSpecifiedIndex() ), statement, retargetedValues );
		//		retargetedEdit.retarget( retargeter );
		return retargetedEdit;
	}
}
