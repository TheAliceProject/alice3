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
public class ExpressionFillInGenerator {
	private ExpressionFillInGenerator() {
		throw new AssertionError();
	}
	private static org.lgna.croquet.history.TransactionHistory generateNumber( org.alice.ide.custom.NumberCustomExpressionCreatorComposite composite, String text ) {
		org.alice.ide.croquet.models.numberpad.NumberModel numberModel = composite.getNumberModel();
		org.lgna.croquet.history.TransactionHistory completionStepSubTransactionHistory = new org.lgna.croquet.history.TransactionHistory();
		for( char c : text.toCharArray() ) {
			org.lgna.croquet.Operation subOperation;
			if( c == '-' ) {
				subOperation = org.alice.ide.croquet.models.numberpad.PlusMinusOperation.getInstance( numberModel );
			} else if( c == '.' ) {
				subOperation = org.alice.ide.croquet.models.numberpad.DecimalPointOperation.getInstance( numberModel );
			} else {
				int digit = c - '0';
				subOperation = org.alice.ide.croquet.models.numberpad.NumeralOperation.getInstance( numberModel, (short)digit );
			}
			org.lgna.croquet.history.Transaction subTransaction = org.lgna.croquet.history.Transaction.createAndAddToHistory( completionStepSubTransactionHistory );
			org.lgna.croquet.history.CompletionStep.createAndAddToTransaction( subTransaction, subOperation, org.lgna.croquet.triggers.MouseEventTrigger.createRecoveryInstance(), null );
		}
		org.lgna.croquet.history.Transaction commitTransaction = org.lgna.croquet.history.Transaction.createAndAddToHistory( completionStepSubTransactionHistory );
		org.lgna.croquet.history.CompletionStep.createAndAddToTransaction( commitTransaction, composite.getCommitOperation(), org.lgna.croquet.triggers.MouseEventTrigger.createRecoveryInstance(), null );
		return completionStepSubTransactionHistory;
	}
	private final static java.util.Collection< Integer > integerLiteralValues = edu.cmu.cs.dennisc.java.util.Collections.newArrayList( 1,2,3 );
	private final static java.util.Collection< Double > doubleLiteralValues = edu.cmu.cs.dennisc.java.util.Collections.newArrayList( 0.25, 0.5, 1.0, 2.0, 10.0 ); //todo: handle portion, and others
	private final static java.util.Collection< String > stringLiteralValues = edu.cmu.cs.dennisc.java.util.Collections.newArrayList( "hello" );
	
	public static org.lgna.croquet.CascadeFillIn generateFillInForExpression( org.lgna.project.ast.Expression expression, org.lgna.croquet.history.TransactionHistory[] bufferForCompletionStepSubTransactionHistory ) {
		org.lgna.croquet.CascadeFillIn fillIn;
		if( expression instanceof org.lgna.project.ast.IntegerLiteral ) {
			org.lgna.project.ast.IntegerLiteral integerLiteral = (org.lgna.project.ast.IntegerLiteral)expression;
			int value = integerLiteral.value.getValue();
			if( integerLiteralValues.contains( value ) ) {
				fillIn = org.alice.ide.croquet.models.cascade.literals.IntegerLiteralFillIn.getInstance( value );
			} else {
				org.alice.ide.custom.NumberCustomExpressionCreatorComposite composite = org.alice.ide.custom.IntegerCustomExpressionCreatorComposite.getInstance();
				bufferForCompletionStepSubTransactionHistory[ 0 ] = generateNumber( composite, Integer.toString( value ) );
				fillIn = composite.getValueCreator().getFillIn();
			}
		} else if( expression instanceof org.lgna.project.ast.DoubleLiteral ) {
			org.lgna.project.ast.DoubleLiteral doubleLiteral = (org.lgna.project.ast.DoubleLiteral)expression;
			double value = doubleLiteral.value.getValue();
			if( doubleLiteralValues.contains( value ) ) {
				fillIn = org.alice.ide.croquet.models.cascade.literals.DoubleLiteralFillIn.getInstance( value );
			} else {
				org.alice.ide.custom.NumberCustomExpressionCreatorComposite composite = org.alice.ide.custom.DoubleCustomExpressionCreatorComposite.getInstance();
				bufferForCompletionStepSubTransactionHistory[ 0 ] = generateNumber( composite, Double.toString( value ) );
				fillIn = composite.getValueCreator().getFillIn();
			}
		} else if( expression instanceof org.lgna.project.ast.StringLiteral ) {
			org.lgna.project.ast.StringLiteral stringLiteral = (org.lgna.project.ast.StringLiteral)expression;
			String value = stringLiteral.value.getValue();
			if( stringLiteralValues.contains( value ) ) {
				fillIn = org.alice.ide.croquet.models.cascade.literals.StringLiteralFillIn.getInstance( value );
			} else {
				org.alice.ide.custom.StringCustomExpressionCreatorComposite composite = org.alice.ide.custom.StringCustomExpressionCreatorComposite.getInstance();
				fillIn = composite.getValueCreator().getFillIn();
				bufferForCompletionStepSubTransactionHistory[ 0 ] = new org.lgna.croquet.history.TransactionHistory();
				org.lgna.croquet.history.Transaction subTransaction = org.lgna.croquet.history.Transaction.createAndAddToHistory( bufferForCompletionStepSubTransactionHistory[ 0 ] );
				org.lgna.croquet.history.StateChangeStep textChangeStep = org.lgna.croquet.history.StateChangeStep.createAndAddToTransaction( subTransaction, composite.getValueState(), org.lgna.croquet.triggers.DocumentEventTrigger.createGeneratorInstance() );
				textChangeStep.setEdit( new org.lgna.croquet.edits.StateEdit<String>( textChangeStep, "", value ) );
				org.lgna.croquet.history.Transaction commitTransaction = org.lgna.croquet.history.Transaction.createAndAddToHistory( bufferForCompletionStepSubTransactionHistory[ 0 ] );
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
		} else if( expression instanceof org.lgna.project.ast.ArrayInstanceCreation ) {
			org.lgna.project.ast.ArrayInstanceCreation arrayInstanceCreation = (org.lgna.project.ast.ArrayInstanceCreation)expression;
			org.lgna.project.ast.AbstractType<?,?,?> arrayType = arrayInstanceCreation.arrayType.getValue();
			fillIn = org.alice.ide.croquet.models.custom.CustomArrayInputDialogOperation.getInstance( arrayType.getComponentType() ).getFillIn();
		} else {
			fillIn = null;
		}
		return fillIn;
	}
}
