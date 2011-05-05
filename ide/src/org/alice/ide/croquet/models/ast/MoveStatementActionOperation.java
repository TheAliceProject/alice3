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
public class MoveStatementActionOperation extends edu.cmu.cs.dennisc.croquet.ActionOperation implements org.alice.ide.croquet.models.ResponsibleModel {
	public static final Class<?>[] CONSTRUCTOR_PARAMETER_TYPES = new Class[] { edu.cmu.cs.dennisc.alice.ast.BlockStatement.class, Integer.TYPE, edu.cmu.cs.dennisc.alice.ast.Statement.class, edu.cmu.cs.dennisc.alice.ast.BlockStatement.class, Integer.TYPE };
	private edu.cmu.cs.dennisc.alice.ast.BlockStatement fromBlockStatement;
	private int fromIndex;
	private edu.cmu.cs.dennisc.alice.ast.Statement statement;
	private edu.cmu.cs.dennisc.alice.ast.BlockStatement toBlockStatement;
	private int toIndex;
	public MoveStatementActionOperation( edu.cmu.cs.dennisc.alice.ast.BlockStatement fromBlockStatement, int fromIndex, edu.cmu.cs.dennisc.alice.ast.Statement statement, edu.cmu.cs.dennisc.alice.ast.BlockStatement toBlockStatement, int toIndex ) {
		super( edu.cmu.cs.dennisc.alice.Project.GROUP, java.util.UUID.fromString( "a6aa2cea-f205-434a-8ec8-c068c9fb3b83" ) );
		this.fromBlockStatement = fromBlockStatement;
		this.fromIndex = fromIndex;
		this.statement = statement;
		this.toBlockStatement = toBlockStatement;
		this.toIndex = toIndex;
	}
	public static Object[] retargetArguments( Object[] rv, edu.cmu.cs.dennisc.croquet.Retargeter retargeter ) {
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
		org.alice.ide.IDE ide = org.alice.ide.IDE.getSingleton();
		edu.cmu.cs.dennisc.alice.ast.BlockStatement fromBlockStatement = edu.cmu.cs.dennisc.alice.project.ProjectUtilities.lookupNode( ide.getProject(), fromBlockStatementId );
		edu.cmu.cs.dennisc.alice.ast.Statement statement = edu.cmu.cs.dennisc.alice.project.ProjectUtilities.lookupNode( ide.getProject(), statementId );
		edu.cmu.cs.dennisc.alice.ast.BlockStatement toBlockStatement = edu.cmu.cs.dennisc.alice.project.ProjectUtilities.lookupNode( ide.getProject(), toBlockStatementId );
		return new Object[] { fromBlockStatement, fromIndex, statement, toBlockStatement, toIndex };
	}
	public void encodeArguments( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		binaryEncoder.encode( this.fromBlockStatement.getUUID() );
		binaryEncoder.encode( this.fromIndex );
		binaryEncoder.encode( this.statement.getUUID() );
		binaryEncoder.encode( this.toBlockStatement.getUUID() );
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
	protected edu.cmu.cs.dennisc.croquet.Edit< ? > createTutorialCompletionEdit( edu.cmu.cs.dennisc.croquet.Edit< ? > originalEdit, edu.cmu.cs.dennisc.croquet.Retargeter retargeter ) {
		return originalEdit;
	}

	public void addKeyValuePairs( edu.cmu.cs.dennisc.croquet.Retargeter retargeter, edu.cmu.cs.dennisc.croquet.Edit< ? > edit ) {
		org.alice.ide.croquet.edits.DependentEdit<MoveStatementActionOperation> replacementEdit = (org.alice.ide.croquet.edits.DependentEdit<MoveStatementActionOperation>)edit;
		MoveStatementActionOperation replacement = replacementEdit.getModel();
		retargeter.addKeyValuePair( this.fromBlockStatement, replacement.fromBlockStatement );
		retargeter.addKeyValuePair( this.statement, replacement.statement );
		retargeter.addKeyValuePair( this.toBlockStatement, replacement.toBlockStatement );
	}
	public void retarget( edu.cmu.cs.dennisc.croquet.Retargeter retargeter ) {
		this.fromBlockStatement = retargeter.retarget( this.fromBlockStatement );
		this.statement = retargeter.retarget( this.statement );
		this.toBlockStatement = retargeter.retarget( this.toBlockStatement );
	}
	
	
	public StringBuilder updatePresentation( StringBuilder rv, java.util.Locale locale ) {
		//super.updatePresentation( rv, locale );
		rv.append( "move: " );
		edu.cmu.cs.dennisc.alice.ast.NodeUtilities.safeAppendRepr( rv, this.statement, locale );
		return rv;
	}
	
	public edu.cmu.cs.dennisc.croquet.ReplacementAcceptability getReplacementAcceptability( edu.cmu.cs.dennisc.croquet.Edit< ? > replacementCandidate, edu.cmu.cs.dennisc.croquet.UserInformation userInformation ) {
		if( replacementCandidate instanceof org.alice.ide.croquet.edits.DependentEdit ) {
			return edu.cmu.cs.dennisc.croquet.ReplacementAcceptability.TO_BE_HONEST_I_DIDNT_EVEN_REALLY_CHECK;
		} else {
			return edu.cmu.cs.dennisc.croquet.ReplacementAcceptability.createRejection( "edit is not an instance of DependentEdit" );
		}
	}
	
	@Override
	protected edu.cmu.cs.dennisc.croquet.CodableResolver< MoveStatementActionOperation > createCodableResolver() {
		return new org.alice.ide.croquet.resolvers.MoveStatementActionOperationNewInstanceResolver( this );
	}
	@Override
	protected final void perform(org.lgna.croquet.steps.ActionOperationStep step) {
		step.commitAndInvokeDo( new org.alice.ide.croquet.edits.DependentEdit< MoveStatementActionOperation >() );
	}
}
