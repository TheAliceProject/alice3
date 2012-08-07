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
package org.alice.ide.ast.declaration;

/**
 * @author Dennis Cosgrove
 */
public class InsertLocalDeclarationStatementComposite extends InsertStatementComposite<org.lgna.project.ast.LocalDeclarationStatement> {
	private static java.util.Map< org.alice.ide.ast.draganddrop.BlockStatementIndexPair, InsertLocalDeclarationStatementComposite > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	public static InsertLocalDeclarationStatementComposite getInstance( org.alice.ide.ast.draganddrop.BlockStatementIndexPair blockStatementIndexPair ) {
		synchronized( map ) {
			InsertLocalDeclarationStatementComposite rv = map.get( blockStatementIndexPair );
			if( rv != null ) {
				//pass
			} else {
				rv = new InsertLocalDeclarationStatementComposite( blockStatementIndexPair );
				map.put( blockStatementIndexPair, rv );
			}
			return rv;
		}
	}
	private InsertLocalDeclarationStatementComposite( org.alice.ide.ast.draganddrop.BlockStatementIndexPair blockStatementIndexPair ) {
		super( java.util.UUID.fromString( "1c257483-36c6-41d8-9d65-4a49bfa11009" ), new Details()
			.valueComponentType( ApplicabilityStatus.EDITABLE, null )
			.valueIsArrayType( ApplicabilityStatus.EDITABLE, false )
			.name( ApplicabilityStatus.EDITABLE )
			.initializer( ApplicabilityStatus.EDITABLE, null )
		, blockStatementIndexPair );
	}
	@Override
	protected org.lgna.project.ast.LocalDeclarationStatement createStatement() {
		boolean isFinal = false;
		org.lgna.project.ast.UserLocal variable = new org.lgna.project.ast.UserLocal( this.getDeclarationLikeSubstanceName(), this.getValueType(), isFinal );
		return new org.lgna.project.ast.LocalDeclarationStatement( variable, this.getInitializer() );
	}
	@Override
	protected boolean isNullAllowedForInitializer() {
		return org.alice.ide.croquet.models.ui.preferences.IsNullAllowedForLocalInitializers.getInstance().getValue();
	}
	
	@Override
	public void addGeneratedSubTransactions( org.lgna.croquet.history.TransactionHistory subTransactionHistory, org.lgna.croquet.edits.Edit< ? > ownerEdit ) {
		org.alice.ide.croquet.edits.ast.InsertStatementEdit insertStatementEdit = (org.alice.ide.croquet.edits.ast.InsertStatementEdit)ownerEdit;
		org.lgna.project.ast.Statement statement = insertStatementEdit.getStatement();
		org.lgna.project.ast.LocalDeclarationStatement localDeclarationStatement = (org.lgna.project.ast.LocalDeclarationStatement)statement;

		org.lgna.project.ast.UserLocal local = localDeclarationStatement.local.getValue();
		this.getValueComponentTypeState().addGeneratedStateChangeTransaction( subTransactionHistory, null, local.getValueType() );
		this.getNameState().addGeneratedStateChangeTransaction( subTransactionHistory, "", local.name.getValue() );
		this.getInitializerState().addGeneratedStateChangeTransaction( subTransactionHistory, null, localDeclarationStatement.initializer.getValue() );
		
		super.addGeneratedSubTransactions( subTransactionHistory, ownerEdit );
	}
}
