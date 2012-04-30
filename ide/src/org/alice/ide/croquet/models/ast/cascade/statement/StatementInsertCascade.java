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
	
	protected abstract java.util.List<org.lgna.project.ast.Expression> extractExpressionsForFillInGeneration( org.lgna.project.ast.Statement statement );
	public void generateAndAddPostDragStepsToTransaction( org.lgna.croquet.history.Transaction transaction, org.lgna.project.ast.Statement statement ) {
		org.lgna.croquet.history.PopupPrepStep.createAndAddToTransaction( transaction, this.getRoot().getPopupPrepModel(), new org.lgna.croquet.triggers.SimulatedTrigger() );
		java.util.List<org.lgna.project.ast.Expression> expressions = this.extractExpressionsForFillInGeneration( statement );
		java.util.List<org.lgna.croquet.MenuItemPrepModel> prepModels = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		for( org.lgna.project.ast.Expression expression : expressions ) {
			org.lgna.croquet.CascadeFillIn fillIn;
			if( expression instanceof org.lgna.project.ast.IntegerLiteral ) {
				org.lgna.project.ast.IntegerLiteral integerLiteral = (org.lgna.project.ast.IntegerLiteral)expression;
				fillIn = org.alice.ide.croquet.models.cascade.literals.IntegerLiteralFillIn.getInstance( integerLiteral.value.getValue() );
			} else if( expression instanceof org.lgna.project.ast.DoubleLiteral ) {
				org.lgna.project.ast.DoubleLiteral doubleLiteral = (org.lgna.project.ast.DoubleLiteral)expression;
				fillIn = org.alice.ide.croquet.models.cascade.literals.DoubleLiteralFillIn.getInstance( doubleLiteral.value.getValue() );
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
				edu.cmu.cs.dennisc.java.util.logging.Logger.errln( "todo: pass fill in to menu selection step ", fillIn );
			} else {
				edu.cmu.cs.dennisc.java.util.logging.Logger.errln( "todo: handle expression ", expression );
			}
			org.lgna.croquet.MenuBarComposite menuBarComposite = null;
			org.lgna.croquet.MenuItemPrepModel[] menuItemPrepModels = edu.cmu.cs.dennisc.java.lang.ArrayUtilities.createArray( prepModels, org.lgna.croquet.MenuItemPrepModel.class );
			
			org.lgna.croquet.history.MenuItemSelectStep.createAndAddToTransaction( transaction, menuBarComposite, menuItemPrepModels, new org.lgna.croquet.triggers.SimulatedTrigger() );
		}
		org.lgna.croquet.history.CompletionStep.createAndAddToTransaction( transaction, this, new org.lgna.croquet.triggers.SimulatedTrigger(), null );
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