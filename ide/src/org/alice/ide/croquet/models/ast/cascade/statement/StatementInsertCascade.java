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
	private final org.alice.ide.ast.draganddrop.BlockStatementIndexPair blockStatementIndexPair;
	public StatementInsertCascade( java.util.UUID id, org.alice.ide.ast.draganddrop.BlockStatementIndexPair blockStatementIndexPair, org.lgna.croquet.CascadeBlank< org.lgna.project.ast.Expression >... blanks ) {
		super( org.alice.ide.IDE.PROJECT_GROUP, id, blanks );
		this.blockStatementIndexPair = blockStatementIndexPair;
	}
	public org.alice.ide.ast.draganddrop.BlockStatementIndexPair getBlockStatementIndexPair() {
		return this.blockStatementIndexPair;
	}
	
	private final static java.util.Collection< Integer > integerLiteralValues = edu.cmu.cs.dennisc.java.util.Collections.newArrayList( 1,2,3 );
	private final static java.util.Collection< String > stringLiteralValues = edu.cmu.cs.dennisc.java.util.Collections.newArrayList( "hello" );
	protected abstract java.util.List<org.lgna.project.ast.Expression> extractExpressionsForFillInGeneration( org.lgna.project.ast.Statement statement );
	public void generateAndAddPostDragStepsToTransaction( org.lgna.croquet.history.Transaction transaction, org.lgna.project.ast.Statement statement ) {
		org.alice.ide.ast.draganddrop.BlockStatementIndexPair blockStatementIndexPair = org.alice.ide.ast.draganddrop.BlockStatementIndexPair.createInstanceFromChildStatement( statement );
		org.lgna.croquet.history.PopupPrepStep.createAndAddToTransaction( transaction, this.getRoot().getPopupPrepModel(), org.lgna.croquet.triggers.DropTrigger.createGeneratorInstance( blockStatementIndexPair ) );
		java.util.List<org.lgna.project.ast.Expression> expressions = this.extractExpressionsForFillInGeneration( statement );
		java.util.List<org.lgna.croquet.MenuItemPrepModel> prepModels = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		org.lgna.croquet.history.TransactionHistory completionStepSubTransactionHistory = null;
		for( org.lgna.project.ast.Expression expression : expressions ) {
			org.lgna.croquet.CascadeFillIn fillIn;
			if( expression instanceof org.lgna.project.ast.IntegerLiteral ) {
				org.lgna.project.ast.IntegerLiteral integerLiteral = (org.lgna.project.ast.IntegerLiteral)expression;
				int value = integerLiteral.value.getValue();
				if( integerLiteralValues.contains( value ) ) {
					fillIn = org.alice.ide.croquet.models.cascade.literals.IntegerLiteralFillIn.getInstance( value );
				} else {
					org.alice.ide.custom.NumberCustomExpressionCreatorComposite composite = org.alice.ide.custom.IntegerCustomExpressionCreatorComposite.getInstance();
					org.lgna.croquet.ValueCreator valueCreator = composite.getValueCreator();
					fillIn = valueCreator.getFillIn();
					completionStepSubTransactionHistory = new org.lgna.croquet.history.TransactionHistory();
					
					org.alice.ide.croquet.models.numberpad.NumberModel numberModel = org.alice.ide.croquet.models.numberpad.IntegerModel.getInstance();
					String text = Integer.toString( value );
					for( char c : text.toCharArray() ) {
						org.lgna.croquet.Operation subOperation;
						if( c == '-' ) {
							subOperation = org.alice.ide.croquet.models.numberpad.PlusMinusOperation.getInstance( numberModel );
						} else {
							int digit = c - '0';
							subOperation = org.alice.ide.croquet.models.numberpad.NumeralOperation.getInstance( numberModel, (short)digit );
						}
						org.lgna.croquet.history.Transaction subTransaction = org.lgna.croquet.history.Transaction.createAndAddToHistory( completionStepSubTransactionHistory );
						org.lgna.croquet.history.CompletionStep.createAndAddToTransaction( subTransaction, subOperation, org.lgna.croquet.triggers.MouseEventTrigger.createRecoveryInstance(), null );
					}
					org.lgna.croquet.history.Transaction commitTransaction = org.lgna.croquet.history.Transaction.createAndAddToHistory( completionStepSubTransactionHistory );
					org.lgna.croquet.history.CompletionStep.createAndAddToTransaction( commitTransaction, composite.getCommitOperation(), org.lgna.croquet.triggers.MouseEventTrigger.createRecoveryInstance(), null );
				}
			} else if( expression instanceof org.lgna.project.ast.DoubleLiteral ) {
				org.lgna.project.ast.DoubleLiteral doubleLiteral = (org.lgna.project.ast.DoubleLiteral)expression;
				fillIn = org.alice.ide.croquet.models.cascade.literals.DoubleLiteralFillIn.getInstance( doubleLiteral.value.getValue() );
			} else if( expression instanceof org.lgna.project.ast.StringLiteral ) {
				org.lgna.project.ast.StringLiteral stringLiteral = (org.lgna.project.ast.StringLiteral)expression;
				String value = stringLiteral.value.getValue();
				if( stringLiteralValues.contains( value ) ) {
					fillIn = org.alice.ide.croquet.models.cascade.literals.StringLiteralFillIn.getInstance( stringLiteral.value.getValue() );
				} else {
					org.alice.ide.custom.StringCustomExpressionCreatorComposite composite = org.alice.ide.custom.StringCustomExpressionCreatorComposite.getInstance();
					org.lgna.croquet.ValueCreator valueCreator = composite.getValueCreator();
					fillIn = valueCreator.getFillIn();
					completionStepSubTransactionHistory = new org.lgna.croquet.history.TransactionHistory();

					org.lgna.croquet.history.Transaction subTransaction = org.lgna.croquet.history.Transaction.createAndAddToHistory( completionStepSubTransactionHistory );
					org.lgna.croquet.history.StateChangeStep textChangeStep = org.lgna.croquet.history.StateChangeStep.createAndAddToTransaction( subTransaction, composite.getValueState(), org.lgna.croquet.triggers.DocumentEventTrigger.createRecoveryInstance() );
					textChangeStep.setEdit( new org.lgna.croquet.edits.StateEdit<String>( textChangeStep, "", value ) );
					org.lgna.croquet.history.Transaction commitTransaction = org.lgna.croquet.history.Transaction.createAndAddToHistory( completionStepSubTransactionHistory );
					org.lgna.croquet.history.CompletionStep.createAndAddToTransaction( commitTransaction, composite.getCommitOperation(), org.lgna.croquet.triggers.MouseEventTrigger.createRecoveryInstance(), null );
				}
			} else if( expression instanceof org.lgna.project.ast.FieldAccess ) {
				org.lgna.project.ast.FieldAccess fieldAccess = (org.lgna.project.ast.FieldAccess)expression;
				org.lgna.project.ast.AbstractField field = fieldAccess.field.getValue();
				if( field.isStatic() ) {
					fillIn = org.alice.ide.croquet.models.cascade.StaticFieldAccessFillIn.getInstance( field );
				} else {
					fillIn = null;
				}
			} else {
				fillIn = null;
			}
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
		org.lgna.croquet.history.CompletionStep.createAndAddToTransaction( transaction, this, org.lgna.croquet.triggers.ActionEventTrigger.createGeneratorInstance(), completionStepSubTransactionHistory );
	}
	
	protected abstract org.lgna.project.ast.Statement createStatement( org.lgna.project.ast.Expression... expressions );
	@Override
	protected org.alice.ide.croquet.edits.ast.InsertStatementEdit createEdit( org.lgna.croquet.history.CompletionStep< org.lgna.croquet.Cascade< org.lgna.project.ast.Expression >> step, org.lgna.project.ast.Expression[] values ) {
		org.lgna.project.ast.Statement statement = this.createStatement( values );
		return new org.alice.ide.croquet.edits.ast.InsertStatementEdit( step, this.blockStatementIndexPair, statement, values );
	}

	@Override
	protected <M extends org.lgna.croquet.Element> org.lgna.croquet.resolvers.Resolver< M > createResolver() {
		return new org.alice.ide.croquet.resolvers.BlockStatementIndexPairStaticGetInstanceKeyedResolver( this, blockStatementIndexPair );
	}
}