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

package org.alice.ide.ast.delete.edits;

/**
 * @author Dennis Cosgrove
 */
public abstract class DeleteMemberEdit<M extends org.lgna.project.ast.UserMember> extends org.lgna.croquet.edits.AbstractEdit<org.alice.ide.ast.delete.DeleteDeclarationLikeSubstanceOperation> {
	private final org.lgna.project.ast.UserType<?> declaringType;
	private final int index;
	private final M member;

	public DeleteMemberEdit( org.lgna.croquet.history.CompletionStep completionStep, M member ) {
		super( completionStep );
		this.declaringType = member.getDeclaringType();
		assert this.declaringType != null : member;
		this.index = this.getNodeListProperty( this.declaringType ).indexOf( member );
		assert this.index != -1 : member;
		this.member = member;
	}

	public DeleteMemberEdit( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder, Object step ) {
		super( binaryDecoder, step );
		this.declaringType = org.alice.ide.croquet.codecs.NodeCodec.getInstance( org.lgna.project.ast.UserType.class ).decodeValue( binaryDecoder );
		this.index = binaryDecoder.decodeInt();
		this.member = (M)org.alice.ide.croquet.codecs.NodeCodec.getInstance( org.lgna.project.ast.UserMember.class ).decodeValue( binaryDecoder );
	}

	protected abstract org.lgna.project.ast.NodeListProperty<M> getNodeListProperty( org.lgna.project.ast.UserType<?> declaringType );

	@Override
	public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		super.encode( binaryEncoder );
		org.alice.ide.croquet.codecs.NodeCodec.getInstance( org.lgna.project.ast.UserType.class ).encodeValue( binaryEncoder, this.declaringType );
		binaryEncoder.encode( this.index );
		org.alice.ide.croquet.codecs.NodeCodec.getInstance( org.lgna.project.ast.UserMember.class ).encodeValue( binaryEncoder, this.member );
	}

	@Override
	protected final void doOrRedoInternal( boolean isDo ) {
		org.lgna.project.ast.NodeListProperty<M> owner = this.getNodeListProperty( this.declaringType );
		assert this.index == owner.indexOf( this.member ) : this.member;
		owner.remove( this.index );
		//todo: remove
		org.alice.ide.project.ProjectChangeOfInterestManager.SINGLETON.fireProjectChangeOfInterestListeners();
		org.alice.ide.declarationseditor.DeclarationTabState declarationTabState = org.alice.ide.IDE.getActiveInstance().getDocumentFrame().getDeclarationsEditorComposite().getTabState();
		declarationTabState.removeAllOrphans();
	}

	@Override
	protected final void undoInternal() {
		org.lgna.project.ast.NodeListProperty<M> owner = this.getNodeListProperty( this.declaringType );
		owner.add( this.index, member );
		//todo: remove
		org.alice.ide.project.ProjectChangeOfInterestManager.SINGLETON.fireProjectChangeOfInterestListeners();

	}

	@Override
	protected void appendDescription( StringBuilder rv, DescriptionStyle descriptionStyle ) {
		rv.append( "delete:" );
		org.lgna.project.ast.NodeUtilities.safeAppendRepr( rv, member, org.lgna.croquet.Application.getLocale() );
	}
}
