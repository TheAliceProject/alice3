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
public class MoveStatementActionOperation extends org.lgna.croquet.ActionOperation implements org.alice.ide.croquet.models.ResponsibleModel {
	public static final Class<?>[] CONSTRUCTOR_PARAMETER_TYPES = new Class[] { org.lgna.project.ast.BlockStatement.class, Integer.TYPE, org.lgna.project.ast.Statement.class, org.lgna.project.ast.BlockStatement.class, Integer.TYPE };
	private org.lgna.project.ast.BlockStatement fromBlockStatement;
	private int fromIndex;
	private org.lgna.project.ast.Statement statement;
	private org.lgna.project.ast.BlockStatement toBlockStatement;
	private int toIndex;
	public MoveStatementActionOperation( org.lgna.project.ast.BlockStatement fromBlockStatement, int fromIndex, org.lgna.project.ast.Statement statement, org.lgna.project.ast.BlockStatement toBlockStatement, int toIndex ) {
		super( org.alice.ide.IDE.PROJECT_GROUP, java.util.UUID.fromString( "8c06bb7d-b35f-4f37-97fc-a30a097f42a2" ) );
		this.fromBlockStatement = fromBlockStatement;
		this.fromIndex = fromIndex;
		this.statement = statement;
		this.toBlockStatement = toBlockStatement;
		this.toIndex = toIndex;
	}
	public static Object[] retargetArguments( Object[] rv, org.lgna.croquet.Retargeter retargeter ) {
		assert rv != null;
		assert rv.length == 5;
		rv[ 0 ] = retargeter.retarget( rv[ 0 ] );
		//todo: retarget fromIndex?
		rv[ 2 ] = retargeter.retarget( rv[ 2 ] );
		rv[ 3 ] = retargeter.retarget( rv[ 3 ] );
		//todo: retarget toIndex?
		return rv;
	}
	

	public static Object[] decodeArguments( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		java.util.UUID fromBlockStatementId = binaryDecoder.decodeId();
		int fromIndex = binaryDecoder.decodeInt();
		java.util.UUID statementId = binaryDecoder.decodeId();
		java.util.UUID toBlockStatementId = binaryDecoder.decodeId();
		int toIndex = binaryDecoder.decodeInt();
		org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
		org.lgna.project.ast.BlockStatement fromBlockStatement = org.lgna.project.ProgramTypeUtilities.lookupNode( ide.getProject(), fromBlockStatementId );
		org.lgna.project.ast.Statement statement = org.lgna.project.ProgramTypeUtilities.lookupNode( ide.getProject(), statementId );
		org.lgna.project.ast.BlockStatement toBlockStatement = org.lgna.project.ProgramTypeUtilities.lookupNode( ide.getProject(), toBlockStatementId );
		return new Object[] { fromBlockStatement, fromIndex, statement, toBlockStatement, toIndex };
	}
	public void encodeArguments( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		binaryEncoder.encode( this.fromBlockStatement.getId() );
		binaryEncoder.encode( this.fromIndex );
		binaryEncoder.encode( this.statement.getId() );
		binaryEncoder.encode( this.toBlockStatement.getId() );
		binaryEncoder.encode( this.toIndex );
	}
	private int getToDelta() {
		int toDelta;
		if( this.fromBlockStatement == this.toBlockStatement ) {
			if( this.fromIndex < this.toIndex ) {
				toDelta = -1;
			} else {
				toDelta = 0;
			}
		} else {
			toDelta = 0;
		}
		return toDelta;
	}
	public void doOrRedoInternal( boolean isDo ) {
		int toDelta = this.getToDelta();
		this.fromBlockStatement.statements.remove( this.fromIndex );
		this.toBlockStatement.statements.add( this.toIndex + toDelta, this.statement );
	}

	public void undoInternal() {
		int toDelta = this.getToDelta();
		this.toBlockStatement.statements.remove( this.toIndex + toDelta );
		this.fromBlockStatement.statements.add( this.fromIndex, this.statement );
	}
	
	@Override
	protected org.lgna.croquet.edits.Edit< ? > createTutorialCompletionEdit( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.croquet.edits.Edit< ? > originalEdit, org.lgna.croquet.Retargeter retargeter ) {
		return originalEdit;
	}

	public void addKeyValuePairs( org.lgna.croquet.Retargeter retargeter, org.lgna.croquet.edits.Edit< ? > edit ) {
		org.alice.ide.croquet.edits.DependentEdit<MoveStatementActionOperation> replacementEdit = (org.alice.ide.croquet.edits.DependentEdit<MoveStatementActionOperation>)edit;
		MoveStatementActionOperation replacement = replacementEdit.getModel();
		retargeter.addKeyValuePair( this.fromBlockStatement, replacement.fromBlockStatement );
		retargeter.addKeyValuePair( this.statement, replacement.statement );
		retargeter.addKeyValuePair( this.toBlockStatement, replacement.toBlockStatement );
	}
	public void retarget( org.lgna.croquet.Retargeter retargeter ) {
		this.fromBlockStatement = retargeter.retarget( this.fromBlockStatement );
		this.statement = retargeter.retarget( this.statement );
		this.toBlockStatement = retargeter.retarget( this.toBlockStatement );
	}
	
	
	public StringBuilder updatePresentation( StringBuilder rv, java.util.Locale locale ) {
		//super.updatePresentation( rv, locale );
		rv.append( "move: " );
		org.lgna.project.ast.NodeUtilities.safeAppendRepr( rv, this.statement, locale );
		return rv;
	}
	
	public org.lgna.croquet.edits.ReplacementAcceptability getReplacementAcceptability( org.lgna.croquet.edits.Edit< ? > replacementCandidate, org.lgna.croquet.UserInformation userInformation ) {
		if( replacementCandidate instanceof org.alice.ide.croquet.edits.DependentEdit ) {
			return org.lgna.croquet.edits.ReplacementAcceptability.TO_BE_HONEST_I_DIDNT_EVEN_REALLY_CHECK;
		} else {
			return org.lgna.croquet.edits.ReplacementAcceptability.createRejection( "edit is not an instance of DependentEdit" );
		}
	}
	
	@Override
	protected org.alice.ide.croquet.resolvers.MoveStatementActionOperationNewInstanceResolver createResolver() {
		return new org.alice.ide.croquet.resolvers.MoveStatementActionOperationNewInstanceResolver( this );
	}
	@Override
	protected final void perform(org.lgna.croquet.history.CompletionStep<?> step) {
		step.commitAndInvokeDo( new org.alice.ide.croquet.edits.DependentEdit< MoveStatementActionOperation >( step ) );
	}
}
