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

import edu.cmu.cs.dennisc.codec.BinaryDecoder;
import edu.cmu.cs.dennisc.codec.BinaryEncoder;
import org.alice.ide.ast.delete.DeleteStatementOperation;
import org.alice.ide.croquet.codecs.NodeCodec;
import org.alice.ide.croquet.edits.ast.StatementEdit;
import org.alice.ide.project.ProjectChangeOfInterestManager;
import org.lgna.croquet.Application;
import org.lgna.croquet.history.CompletionStep;
import org.lgna.project.ast.BlockStatement;
import org.lgna.project.ast.NodeUtilities;
import org.lgna.project.ast.Statement;

/**
 * @author Dennis Cosgrove
 */
public class DeleteStatementEdit extends StatementEdit<DeleteStatementOperation> {
	private final BlockStatement blockStatement;
	private final int index;

	public DeleteStatementEdit( CompletionStep completionStep, Statement statement ) {
		super( completionStep, statement );
		this.blockStatement = (BlockStatement)statement.getParent();
		assert this.blockStatement != null : statement;
		this.index = this.blockStatement.statements.indexOf( statement );
		assert this.index != -1 : statement;
	}

	public DeleteStatementEdit( BinaryDecoder binaryDecoder, Object step ) {
		super( binaryDecoder, step );
		this.blockStatement = NodeCodec.getInstance( BlockStatement.class ).decodeValue( binaryDecoder );
		this.index = binaryDecoder.decodeInt();
	}

	@Override
	public void encode( BinaryEncoder binaryEncoder ) {
		super.encode( binaryEncoder );
		NodeCodec.getInstance( BlockStatement.class ).encodeValue( binaryEncoder, this.blockStatement );
		binaryEncoder.encode( this.index );
	}

	@Override
	protected final void doOrRedoInternal( boolean isDo ) {
		Statement statement = this.getStatement();
		assert blockStatement.statements.indexOf( statement ) == this.index;
		blockStatement.statements.remove( index );
		//todo: remove
		ProjectChangeOfInterestManager.SINGLETON.fireProjectChangeOfInterestListeners();
	}

	@Override
	protected final void undoInternal() {
		Statement statement = this.getStatement();
		blockStatement.statements.add( index, statement );
		//todo: remove
		ProjectChangeOfInterestManager.SINGLETON.fireProjectChangeOfInterestListeners();
	}

	@Override
	protected void appendDescription( StringBuilder rv, DescriptionStyle descriptionStyle ) {
		Statement statement = this.getStatement();
		rv.append( "delete:" );
		NodeUtilities.safeAppendRepr( rv, statement, Application.getLocale() );
	}
}
