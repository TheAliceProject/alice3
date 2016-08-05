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
public class DissolveStatementWithBodyEdit extends BlockStatementEdit<org.alice.ide.croquet.models.ast.DissolveStatementWithBodyOperation> {
	//todo:
	private static org.alice.ide.croquet.models.ast.DissolveStatementWithBodyOperation getModel( org.lgna.croquet.history.CompletionStep<org.alice.ide.croquet.models.ast.DissolveStatementWithBodyOperation> completionStep ) {
		return completionStep.getModel();
	}

	private final int index;
	private final org.lgna.project.ast.Statement[] statements;

	public DissolveStatementWithBodyEdit( org.lgna.croquet.history.CompletionStep completionStep ) {
		super( completionStep, (org.lgna.project.ast.BlockStatement)( getModel( completionStep ).getStatementWithBody().getParent() ) );
		org.lgna.project.ast.BlockStatement blockStatement = this.getBlockStatement();
		org.lgna.project.ast.AbstractStatementWithBody statementWithBody = this.getModel().getStatementWithBody();
		this.index = blockStatement.statements.indexOf( statementWithBody );
		this.statements = edu.cmu.cs.dennisc.java.lang.ArrayUtilities.createArray( statementWithBody.body.getValue().statements.getValue(), org.lgna.project.ast.Statement.class );
	}

	public DissolveStatementWithBodyEdit( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder, Object step ) {
		super( binaryDecoder, step );
		this.index = binaryDecoder.decodeInt();
		org.alice.ide.croquet.codecs.NodeCodec<org.lgna.project.ast.Statement> codec = org.alice.ide.croquet.codecs.NodeCodec.getInstance( org.lgna.project.ast.Statement.class );
		this.statements = org.lgna.croquet.ItemCodec.Arrays.decodeArray( binaryDecoder, codec );
	}

	@Override
	public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		super.encode( binaryEncoder );
		binaryEncoder.encode( this.index );
		org.alice.ide.croquet.codecs.NodeCodec<org.lgna.project.ast.Statement> codec = org.alice.ide.croquet.codecs.NodeCodec.getInstance( org.lgna.project.ast.Statement.class );
		org.lgna.croquet.ItemCodec.Arrays.encodeArray( binaryEncoder, codec, this.statements );
	}

	@Override
	protected final void doOrRedoInternal( boolean isDo ) {
		//todo: check 
		org.lgna.project.ast.BlockStatement owner = this.getBlockStatement();
		org.lgna.project.ast.AbstractStatementWithBody statementWithBody = this.getModel().getStatementWithBody();

		owner.statements.remove( index );
		statementWithBody.body.getValue().statements.clear();
		owner.statements.add( index, statements );

		//todo: remove
		org.alice.ide.project.ProjectChangeOfInterestManager.SINGLETON.fireProjectChangeOfInterestListeners();
	}

	@Override
	protected final void undoInternal() {
		//todo: check 
		org.lgna.project.ast.BlockStatement owner = this.getBlockStatement();
		org.lgna.project.ast.AbstractStatementWithBody statementWithBody = this.getModel().getStatementWithBody();
		owner.statements.removeExclusive( this.index, this.index + this.statements.length );
		statementWithBody.body.getValue().statements.add( this.statements );
		owner.statements.add( index, statementWithBody );

		//todo: remove
		org.alice.ide.project.ProjectChangeOfInterestManager.SINGLETON.fireProjectChangeOfInterestListeners();
	}

	@Override
	protected void appendDescription( StringBuilder rv, DescriptionStyle descriptionStyle ) {
		rv.append( "dissolve:" );
		org.lgna.project.ast.NodeUtilities.safeAppendRepr( rv, this.getModel().getStatementWithBody(), org.lgna.croquet.Application.getLocale() );
	}
}
