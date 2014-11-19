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
package org.alice.ide.croquet.edits.ast;

/**
 * @author Dennis Cosgrove
 */
public final class DeclareMethodEdit extends org.lgna.croquet.edits.AbstractEdit<org.lgna.croquet.CompletionModel> {
	private org.lgna.project.ast.UserType<?> declaringType;
	private final String methodName;
	private org.lgna.project.ast.AbstractType<?, ?, ?> returnType;
	private org.lgna.project.ast.BlockStatement body;

	private transient org.lgna.project.ast.UserMethod method;
	private transient org.alice.ide.declarationseditor.DeclarationComposite prevDeclarationComposite;

	public DeclareMethodEdit( org.lgna.croquet.history.CompletionStep completionStep, org.lgna.project.ast.UserType<?> declaringType, String methodName, org.lgna.project.ast.AbstractType<?, ?, ?> returnType, org.lgna.project.ast.BlockStatement body ) {
		super( completionStep );
		this.declaringType = declaringType;
		this.methodName = methodName;
		this.returnType = returnType;
		this.body = body;
	}

	public DeclareMethodEdit( org.lgna.croquet.history.CompletionStep completionStep, org.lgna.project.ast.UserType<?> declaringType, String methodName, org.lgna.project.ast.AbstractType<?, ?, ?> returnType ) {
		this( completionStep, declaringType, methodName, returnType, new org.lgna.project.ast.BlockStatement() );
	}

	public DeclareMethodEdit( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder, Object step ) {
		super( binaryDecoder, step );
		this.declaringType = org.alice.ide.croquet.codecs.NodeCodec.getInstance( org.lgna.project.ast.UserType.class ).decodeValue( binaryDecoder );
		this.methodName = binaryDecoder.decodeString();
		this.returnType = org.alice.ide.croquet.codecs.NodeCodec.getInstance( org.lgna.project.ast.AbstractType.class ).decodeValue( binaryDecoder );
		this.body = org.alice.ide.croquet.codecs.NodeCodec.getInstance( org.lgna.project.ast.BlockStatement.class ).decodeValue( binaryDecoder );
	}

	@Override
	public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		super.encode( binaryEncoder );
		org.alice.ide.croquet.codecs.NodeCodec.getInstance( org.lgna.project.ast.UserType.class ).encodeValue( binaryEncoder, this.declaringType );
		binaryEncoder.encode( this.methodName );
		org.alice.ide.croquet.codecs.NodeCodec.getInstance( org.lgna.project.ast.AbstractType.class ).encodeValue( binaryEncoder, this.returnType );
		org.alice.ide.croquet.codecs.NodeCodec.getInstance( org.lgna.project.ast.BlockStatement.class ).encodeValue( binaryEncoder, this.body );
	}

	@Override
	protected void preCopy() {
		super.preCopy();
		org.alice.ide.croquet.codecs.NodeCodec.addNodeToGlobalMap( this.body );
	}

	@Override
	protected void postCopy( org.lgna.croquet.edits.AbstractEdit<?> result ) {
		org.alice.ide.croquet.codecs.NodeCodec.removeNodeFromGlobalMap( this.body );
		super.postCopy( result );
	}

	public org.lgna.project.ast.UserType<?> getDeclaringType() {
		return this.declaringType;
	}

	public String getMethodName() {
		return this.methodName;
	}

	public org.lgna.project.ast.AbstractType<?, ?, ?> getReturnType() {
		return this.returnType;
	}

	//	public org.lgna.project.ast.UserMethod getMethod() {
	//		return this.method;
	//	}

	public void EPIC_HACK_FOR_TUTORIAL_GENERATION_setMethod( org.lgna.project.ast.UserMethod method ) {
		this.method = method;
	}

	@Override
	protected final void doOrRedoInternal( boolean isDo ) {
		if( isDo ) {
			//todo: create new every time?
			this.method = new org.lgna.project.ast.UserMethod( this.methodName, this.returnType, new org.lgna.project.ast.UserParameter[ 0 ], this.body );
		}
		org.alice.ide.declarationseditor.DeclarationTabState declarationTabState = org.alice.ide.declarationseditor.DeclarationsEditorComposite.getInstance().getTabState();
		this.prevDeclarationComposite = declarationTabState.getValue();
		this.declaringType.methods.add( this.method );
		declarationTabState.setValueTransactionlessly( org.alice.ide.declarationseditor.CodeComposite.getInstance( this.method ) );
	}

	@Override
	protected final void undoInternal() {
		int index = this.declaringType.methods.indexOf( this.method );
		if( index != -1 ) {
			this.declaringType.methods.remove( index );
			org.alice.ide.declarationseditor.DeclarationTabState declarationTabState = org.alice.ide.declarationseditor.DeclarationsEditorComposite.getInstance().getTabState();
			if( this.prevDeclarationComposite != null ) {
				if( declarationTabState.containsItem( this.prevDeclarationComposite ) ) {
					declarationTabState.setValueTransactionlessly( this.prevDeclarationComposite );
				}
			}
			declarationTabState.removeAllOrphans();
		} else {
			throw new javax.swing.undo.CannotUndoException();
		}
	}

	@Override
	protected void appendDescription( StringBuilder rv, DescriptionStyle descriptionStyle ) {
		rv.append( "declare: " );
		rv.append( this.methodName );
	}

	@Override
	public org.lgna.croquet.edits.ReplacementAcceptability getReplacementAcceptability( org.lgna.croquet.edits.Edit<?> replacementCandidate ) {
		if( replacementCandidate instanceof DeclareMethodEdit ) {
			DeclareMethodEdit declareMethodEdit = (DeclareMethodEdit)replacementCandidate;
			org.lgna.project.ast.AbstractType<?, ?, ?> originalReturnType = this.getReturnType();
			org.lgna.project.ast.AbstractType<?, ?, ?> replacementReturnType = declareMethodEdit.getReturnType();
			if( originalReturnType == replacementReturnType ) {
				String originalName = this.getMethodName();
				String replacementName = declareMethodEdit.getMethodName();
				if( edu.cmu.cs.dennisc.java.util.Objects.equals( originalName, replacementName ) ) {
					return org.lgna.croquet.edits.ReplacementAcceptability.PERFECT_MATCH;
				} else {
					StringBuilder sb = new StringBuilder();
					sb.append( "original name: " );
					sb.append( originalName );
					sb.append( "; changed to: " );
					sb.append( replacementName );
					//sb.append( "." );
					return org.lgna.croquet.edits.ReplacementAcceptability.createDeviation( org.lgna.croquet.edits.ReplacementAcceptability.DeviationSeverity.SHOULD_BE_FINE, sb.toString() );
				}
			} else {
				org.alice.ide.formatter.Formatter formatter = org.alice.ide.croquet.models.ui.formatter.FormatterState.getInstance().getValue();
				return org.lgna.croquet.edits.ReplacementAcceptability.createRejection( "<html>return type <strong>MUST</strong> be <strong>" + formatter.getTextForType( originalReturnType ) + "</strong></html>" );
			}
		} else {
			return org.lgna.croquet.edits.ReplacementAcceptability.createRejection( "replacement is not an instance of DeclareMethodEdit" );
		}
	}

	@Override
	public void retarget( org.lgna.croquet.Retargeter retargeter ) {
		super.retarget( retargeter );
		this.declaringType = retargeter.retarget( this.declaringType );
		this.returnType = retargeter.retarget( this.returnType );
		if( this.method != null ) {
			this.method = retargeter.retarget( this.method );
		}
	}

	@Override
	public void addKeyValuePairs( org.lgna.croquet.Retargeter retargeter, org.lgna.croquet.edits.Edit<?> edit ) {
		super.addKeyValuePairs( retargeter, edit );
		assert edit instanceof DeclareMethodEdit;
		DeclareMethodEdit replacementEdit = (DeclareMethodEdit)edit;
		retargeter.addKeyValuePair( this.declaringType, replacementEdit.declaringType );
		retargeter.addKeyValuePair( this.returnType, replacementEdit.returnType );
		if( this.method != null ) {
			retargeter.addKeyValuePair( this.method, replacementEdit.method );
			retargeter.addKeyValuePair( this.method.body.getValue(), replacementEdit.method.body.getValue() );
		}
	}
}
