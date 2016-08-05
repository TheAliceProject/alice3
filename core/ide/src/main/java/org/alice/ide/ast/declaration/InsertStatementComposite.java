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
package org.alice.ide.ast.declaration;

/**
 * @author Dennis Cosgrove
 */
public abstract class InsertStatementComposite<S extends org.lgna.project.ast.Statement> extends DeclarationLikeSubstanceComposite<S> {
	private final org.alice.ide.ast.draganddrop.BlockStatementIndexPair blockStatementIndexPair;
	private final boolean isEnveloping;
	//todo: remove
	private final org.alice.ide.name.validators.LocalNameValidator nameValidator;

	public InsertStatementComposite( java.util.UUID migrationId, Details details, org.alice.ide.ast.draganddrop.BlockStatementIndexPair blockStatementIndexPair, boolean isEnveloping ) {
		super( migrationId, details );
		this.blockStatementIndexPair = blockStatementIndexPair;
		this.isEnveloping = isEnveloping;
		this.nameValidator = new org.alice.ide.name.validators.LocalNameValidator( blockStatementIndexPair );
	}

	protected abstract S createStatement();

	public org.alice.ide.ast.draganddrop.BlockStatementIndexPair getBlockStatementIndexPair() {
		return this.blockStatementIndexPair;
	}

	public boolean isEnveloping() {
		return this.isEnveloping;
	}

	@Override
	public S getPreviewValue() {
		return this.createStatement();
	}

	@Override
	protected org.lgna.croquet.edits.Edit createEdit( org.lgna.croquet.history.CompletionStep<?> completionStep ) {
		return new org.alice.ide.croquet.edits.ast.InsertStatementEdit( completionStep, this.blockStatementIndexPair, this.createStatement(), new org.lgna.project.ast.Expression[ 0 ], this.isEnveloping );
	}

	@Override
	public org.lgna.project.ast.UserType<?> getDeclaringType() {
		return null;
	}

	@Override
	protected boolean isNameAvailable( String name ) {
		return this.nameValidator.isNameAvailable( name );
	}

	@Override
	protected org.alice.ide.ast.declaration.views.DeclarationLikeSubstanceView createView() {
		return new org.alice.ide.ast.declaration.views.InsertStatementView( this );
	}
}
