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
	
	public static Object[] retargetArguments( Object[] rv, org.lgna.croquet.Retargeter retargeter ) {
		assert rv != null;
		assert rv.length == 3;
		rv[ 0 ] = retargeter.retarget( rv[ 0 ] );
		//todo: retarget index?
		rv[ 2 ] = retargeter.retarget( rv[ 2 ] );
		return rv;
	}
	

	public static Object[] decodeArguments( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		java.util.UUID blockStatementId = binaryDecoder.decodeId();
		int index = binaryDecoder.decodeInt();
		java.util.UUID statementId = binaryDecoder.decodeId();
		org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
		org.lgna.project.ast.BlockStatement blockStatement = org.lgna.project.project.ProjectUtilities.lookupNode( ide.getProject(), blockStatementId );
		org.lgna.project.ast.Statement statement = org.lgna.project.project.ProjectUtilities.lookupNode( ide.getProject(), statementId );
		return new Object[] { blockStatement, index, statement };
	}
	public void encodeArguments( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		binaryEncoder.encode( this.blockStatement.getUUID() );
		binaryEncoder.encode( this.index );
		binaryEncoder.encode( this.statement.getUUID() );
	}
	
	public void doOrRedoInternal( boolean isDo ) {
		this.blockStatement.statements.add( this.index, this.statement );
	}

	public void undoInternal() {
		if( this.blockStatement.statements.get( this.index ) == this.statement ) {
			this.blockStatement.statements.remove( this.index );
		} else {
			throw new javax.swing.undo.CannotUndoException();
		}
	}
	
	@Override
	protected org.lgna.croquet.edits.Edit< ? > createTutorialCompletionEdit( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.croquet.edits.Edit< ? > originalEdit, org.lgna.croquet.Retargeter retargeter ) {
		return originalEdit;
	}

//	private static edu.cmu.cs.dennisc.alice.ast.MethodInvocation getMethodInvocation( edu.cmu.cs.dennisc.alice.ast.Statement statement ) {
//		if( statement instanceof edu.cmu.cs.dennisc.alice.ast.ExpressionStatement ) {
//			edu.cmu.cs.dennisc.alice.ast.ExpressionStatement expressionStatement = (edu.cmu.cs.dennisc.alice.ast.ExpressionStatement)statement;
//			edu.cmu.cs.dennisc.alice.ast.Expression expression = expressionStatement.expression.getValue();
//			if( expression instanceof edu.cmu.cs.dennisc.alice.ast.MethodInvocation ) {
//				return (edu.cmu.cs.dennisc.alice.ast.MethodInvocation)expression;
//			}
//		}
//		return null;
//	}
//	private static edu.cmu.cs.dennisc.alice.ast.AbstractMethod getMethod( edu.cmu.cs.dennisc.alice.ast.Statement statement ) {
//		edu.cmu.cs.dennisc.alice.ast.MethodInvocation methodInvocation = getMethodInvocation( statement );
//		if( methodInvocation != null ) {
//			return methodInvocation.method.getValue();
//		}
//		return null;
//	}
	public void addKeyValuePairs( org.lgna.croquet.Retargeter retargeter, org.lgna.croquet.edits.Edit< ? > edit ) {
		org.alice.ide.croquet.edits.DependentEdit<InsertStatementActionOperation> replacementEdit = (org.alice.ide.croquet.edits.DependentEdit<InsertStatementActionOperation>)edit;
		InsertStatementActionOperation replacement = replacementEdit.getModel();
		retargeter.addKeyValuePair( this.blockStatement, replacement.blockStatement );
		retargeter.addKeyValuePair( this.statement, replacement.statement );
//		edu.cmu.cs.dennisc.alice.ast.AbstractMethod method = getMethod( this.statement );
//		if( method != null ) {
//			retargeter.addKeyValuePair( method, getMethod( replacement.statement ) );
//		}
	}
	public void retarget( org.lgna.croquet.Retargeter retargeter ) {
		this.blockStatement = retargeter.retarget( this.blockStatement );
		org.lgna.project.ast.Statement originalStatement = this.statement;
		this.statement = retargeter.retarget( originalStatement );
//		edu.cmu.cs.dennisc.alice.ast.MethodInvocation methodInvocation = getMethodInvocation( this.statement );
//		if( methodInvocation != null ) {
//			methodInvocation.method.setValue( retargeter.retarget( getMethod( originalStatement ) ) );
//		}
	}
	
	
	public StringBuilder updatePresentation( StringBuilder rv, java.util.Locale locale ) {
		//super.updatePresentation( rv, locale );
		rv.append( "create: " );
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
	protected org.lgna.croquet.resolvers.CodableResolver< InsertStatementActionOperation > createCodableResolver() {
		return new org.alice.ide.croquet.resolvers.InsertStatementActionOperationNewInstanceResolver( this );
	}
	@Override
	protected final void perform(org.lgna.croquet.history.ActionOperationStep step) {
		step.commitAndInvokeDo( new org.alice.ide.croquet.edits.DependentEdit< InsertStatementActionOperation >( step ) );
	}
}
