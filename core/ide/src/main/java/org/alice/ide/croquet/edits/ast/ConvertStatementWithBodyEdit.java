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

package org.alice.ide.croquet.edits.ast;

/**
 * @author Dennis Cosgrove
 */
public class ConvertStatementWithBodyEdit extends BlockStatementEdit<org.alice.ide.croquet.models.ast.ConvertStatementWithBodyOperation> {
	//todo:
	private static org.alice.ide.croquet.models.ast.ConvertStatementWithBodyOperation getModel( org.lgna.croquet.history.CompletionStep<org.alice.ide.croquet.models.ast.ConvertStatementWithBodyOperation> completionStep ) {
		return completionStep.getModel();
	}

	private final org.lgna.project.ast.AbstractStatementWithBody replacement;

	public ConvertStatementWithBodyEdit( org.lgna.croquet.history.CompletionStep completionStep, org.lgna.project.ast.AbstractStatementWithBody replacement ) {
		super( completionStep, (org.lgna.project.ast.BlockStatement)( getModel( completionStep ).getOriginal().getParent() ) );
		this.replacement = replacement;
	}

	public ConvertStatementWithBodyEdit( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder, Object step ) {
		super( binaryDecoder, step );
		this.replacement = org.alice.ide.croquet.codecs.NodeCodec.getInstance( org.lgna.project.ast.AbstractStatementWithBody.class ).decodeValue( binaryDecoder );
	}

	@Override
	public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		super.encode( binaryEncoder );
		org.alice.ide.croquet.codecs.NodeCodec.getInstance( org.lgna.project.ast.AbstractStatementWithBody.class ).encodeValue( binaryEncoder, this.replacement );
	}

	@Override
	protected final void doOrRedoInternal( boolean isDo ) {
		org.lgna.project.ast.StatementListProperty property = this.getBlockStatement().statements;
		org.lgna.project.ast.AbstractStatementWithBody original = this.getModel().getOriginal();
		org.lgna.project.ast.BlockStatement body = original.body.getValue();
		int index = property.indexOf( original );

		property.remove( index );
		original.body.setValue( null );
		replacement.body.setValue( body );
		property.add( index, replacement );
		//todo: remove
		org.alice.ide.project.ProjectChangeOfInterestManager.SINGLETON.fireProjectChangeOfInterestListeners();
	}

	@Override
	protected final void undoInternal() {
		org.lgna.project.ast.StatementListProperty property = this.getBlockStatement().statements;
		org.lgna.project.ast.AbstractStatementWithBody original = this.getModel().getOriginal();
		org.lgna.project.ast.BlockStatement body = this.replacement.body.getValue();
		int index = property.indexOf( replacement );

		property.remove( index );
		replacement.body.setValue( null );
		original.body.setValue( body );
		property.add( index, original );
		//todo: remove
		org.alice.ide.project.ProjectChangeOfInterestManager.SINGLETON.fireProjectChangeOfInterestListeners();
	}

	@Override
	protected void appendDescription( StringBuilder rv, DescriptionStyle descriptionStyle ) {
		org.lgna.project.ast.AbstractStatementWithBody original = this.getModel().getOriginal();
		rv.append( "convert:" );
		org.lgna.project.ast.NodeUtilities.safeAppendRepr( rv, original, org.lgna.croquet.Application.getLocale() );
		rv.append( " --> " );
		org.lgna.project.ast.NodeUtilities.safeAppendRepr( rv, replacement, org.lgna.croquet.Application.getLocale() );
	}
}
