/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/
package org.alice.ide.croquet.models.ast;

/**
 * @author Dennis Cosgrove
 */
public abstract class DeleteMemberOperation<N extends org.lgna.project.ast.AbstractMember> extends org.lgna.croquet.ActionOperation implements org.alice.ide.croquet.models.ResponsibleModel {
	private N member;
	private org.lgna.project.ast.UserType<?> declaringType;

	//todo
	//note: index not preserved and restored
	//in the case where it is undone across sessions, it will not know where to insert the declaration
	private transient int index = -1;

	public DeleteMemberOperation( java.util.UUID individualId, N node, org.lgna.project.ast.UserType<?> declaringType ) {
		super( org.lgna.croquet.Application.PROJECT_GROUP, individualId );
		this.member = node;
		this.declaringType = declaringType;
	}

	public abstract Class<N> getNodeParameterType();

	public org.lgna.project.ast.UserType<?> getDeclaringType() {
		return this.declaringType;
	}

	public N getMember() {
		return this.member;
	}

	protected abstract org.lgna.project.ast.NodeListProperty<N> getNodeListProperty( org.lgna.project.ast.UserType<?> declaringType );

	protected abstract boolean isClearToDelete( N node );

	@Override
	public void doOrRedoInternal( boolean isDo ) {
		org.lgna.project.ast.NodeListProperty<N> owner = this.getNodeListProperty( this.declaringType );
		this.index = owner.indexOf( this.member );
		owner.remove( index );
		org.alice.ide.declarationseditor.DeclarationTabState declarationTabState = org.alice.ide.IDE.getActiveInstance().getDocumentFrame().getDeclarationsEditorComposite().getTabState();
		declarationTabState.removeAllOrphans();
	}

	@Override
	public void undoInternal() {
		org.lgna.project.ast.NodeListProperty<N> owner = this.getNodeListProperty( this.declaringType );
		if( this.index == -1 ) {
			this.index += owner.size();
		}
		owner.add( this.index, member );
	}

	@Override
	public void appendDescription( StringBuilder rv, boolean isDetailed ) {
		rv.append( "delete: " );
		org.lgna.project.ast.NodeUtilities.safeAppendRepr( rv, member, org.lgna.croquet.Application.getLocale() );
	}

	@Override
	protected void perform( org.lgna.croquet.history.CompletionStep<?> step ) {
		if( this.isClearToDelete( this.member ) ) {
			step.commitAndInvokeDo( new org.alice.ide.croquet.edits.DependentEdit<DeleteMemberOperation<N>>( step ) );
		} else {
			step.cancel();
		}
	}
}
