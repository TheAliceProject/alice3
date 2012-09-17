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
package org.alice.ide.croquet.models.ast;

/**
 * @author Dennis Cosgrove
 */
public class InsertStatementActionOperation extends org.lgna.croquet.ActionOperation implements org.alice.ide.croquet.models.ResponsibleModel {
	public static final Class<?>[] CONSTRUCTOR_PARAMETER_TYPES = new Class[] { org.lgna.project.ast.BlockStatement.class, Integer.TYPE, org.lgna.project.ast.Statement.class };
	private org.lgna.project.ast.BlockStatement blockStatement;
	private int index;
	private org.lgna.project.ast.Statement statement;

	public InsertStatementActionOperation( org.lgna.project.ast.BlockStatement blockStatement, int index, org.lgna.project.ast.Statement statement ) {
		super( org.alice.ide.IDE.PROJECT_GROUP, java.util.UUID.fromString( "a6aa2cea-f205-434a-8ec8-c068c9fb3b83" ) );
		this.blockStatement = blockStatement;
		this.index = index;
		this.statement = statement;
	}

	public Object[] getArguments() {
		return new Object[] {
				this.blockStatement,
				this.index,
				this.statement
		};
	}

	public void doOrRedoInternal( boolean isDo ) {
		this.blockStatement.statements.add( this.index, this.statement );
		org.alice.ide.declarationseditor.DeclarationTabState.getInstance().handleAstChangeThatCouldBeOfInterest();
	}

	public void undoInternal() {
		if( this.blockStatement.statements.get( this.index ) == this.statement ) {
			this.blockStatement.statements.remove( this.index );
			org.alice.ide.declarationseditor.DeclarationTabState.getInstance().handleAstChangeThatCouldBeOfInterest();
		} else {
			throw new javax.swing.undo.CannotUndoException();
		}
	}

	@Override
	protected org.lgna.croquet.edits.Edit<?> createTutorialCompletionEdit( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.croquet.edits.Edit<?> originalEdit, org.lgna.croquet.Retargeter retargeter ) {
		return originalEdit;
	}

	public void addKeyValuePairs( org.lgna.croquet.Retargeter retargeter, org.lgna.croquet.edits.Edit<?> edit ) {
		org.alice.ide.croquet.edits.DependentEdit<InsertStatementActionOperation> replacementEdit = (org.alice.ide.croquet.edits.DependentEdit<InsertStatementActionOperation>)edit;
		InsertStatementActionOperation replacement = replacementEdit.getModel();
		retargeter.addKeyValuePair( this.blockStatement, replacement.blockStatement );
		retargeter.addKeyValuePair( this.statement, replacement.statement );
		//		org.lgna.project.ast.AbstractMethod method = getMethod( this.statement );
		//		if( method != null ) {
		//			retargeter.addKeyValuePair( method, getMethod( replacement.statement ) );
		//		}
	}

	public void retarget( org.lgna.croquet.Retargeter retargeter ) {
		this.blockStatement = retargeter.retarget( this.blockStatement );
		org.lgna.project.ast.Statement originalStatement = this.statement;
		this.statement = retargeter.retarget( originalStatement );
		//		org.lgna.project.ast.MethodInvocation methodInvocation = getMethodInvocation( this.statement );
		//		if( methodInvocation != null ) {
		//			methodInvocation.method.setValue( retargeter.retarget( getMethod( originalStatement ) ) );
		//		}
	}

	public StringBuilder updatePresentation( StringBuilder rv ) {
		//super.updatePresentation( rv, locale );
		rv.append( "create: " );
		org.lgna.project.ast.NodeUtilities.safeAppendRepr( rv, this.statement, org.lgna.croquet.Application.getLocale() );
		return rv;
	}

	public org.lgna.croquet.edits.ReplacementAcceptability getReplacementAcceptability( org.lgna.croquet.edits.Edit<?> replacementCandidate ) {
		if( replacementCandidate instanceof org.alice.ide.croquet.edits.DependentEdit ) {
			return org.lgna.croquet.edits.ReplacementAcceptability.TO_BE_HONEST_I_DIDNT_EVEN_REALLY_CHECK;
		} else {
			return org.lgna.croquet.edits.ReplacementAcceptability.createRejection( "edit is not an instance of DependentEdit" );
		}
	}

	@Override
	protected org.alice.ide.croquet.resolvers.InsertStatementActionOperationNewInstanceResolver createResolver() {
		return new org.alice.ide.croquet.resolvers.InsertStatementActionOperationNewInstanceResolver( this );
	}

	@Override
	protected final void perform( org.lgna.croquet.history.Transaction transaction, org.lgna.croquet.triggers.Trigger trigger ) {
		org.lgna.croquet.history.CompletionStep<?> step = transaction.createAndSetCompletionStep( this, trigger );
		step.commitAndInvokeDo( new org.alice.ide.croquet.edits.DependentEdit<InsertStatementActionOperation>( step ) );
	}
}
