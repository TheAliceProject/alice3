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

package org.alice.ide.ast.delete.edits;

/**
 * @author dennisc
 */
public class DeleteStatementEdit extends org.lgna.croquet.edits.Edit<org.alice.ide.ast.delete.DeleteStatementOperation> {
	private final org.lgna.project.ast.BlockStatement blockStatement;
	private final int index;
	private final org.lgna.project.ast.Statement statement;
	public DeleteStatementEdit( org.lgna.croquet.history.CompletionStep completionStep, org.lgna.project.ast.Statement statement ) {
		super( completionStep );
		this.blockStatement = (org.lgna.project.ast.BlockStatement)statement.getParent();
		assert this.blockStatement != null : statement;
		this.index = this.blockStatement.statements.indexOf( statement );
		assert this.index != -1 : statement;
		this.statement = statement;
	}
	public DeleteStatementEdit( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder, Object step ) {
		super( binaryDecoder, step );
		this.blockStatement = org.alice.ide.croquet.codecs.NodeCodec.getInstance( org.lgna.project.ast.BlockStatement.class ).decodeValue( binaryDecoder );
		this.index = binaryDecoder.decodeInt();
		this.statement = org.alice.ide.croquet.codecs.NodeCodec.getInstance( org.lgna.project.ast.Statement.class ).decodeValue( binaryDecoder );
	}
	@Override
	public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		super.encode( binaryEncoder );
		org.alice.ide.croquet.codecs.NodeCodec.getInstance( org.lgna.project.ast.BlockStatement.class ).encodeValue( binaryEncoder, this.blockStatement );
		binaryEncoder.encode( this.index );
		org.alice.ide.croquet.codecs.NodeCodec.getInstance( org.lgna.project.ast.Statement.class ).encodeValue( binaryEncoder, this.statement );
	}
	@Override
	protected final void doOrRedoInternal( boolean isDo ) {
		assert blockStatement.statements.indexOf( statement ) == this.index;
		blockStatement.statements.remove( index );
		//todo: remove
		org.alice.ide.instancefactory.croquet.InstanceFactoryState.getInstance().handleAstChangeThatCouldBeOfInterest();
	}
	@Override
	protected final void undoInternal() {
		blockStatement.statements.add( index, statement );
		//todo: remove
		org.alice.ide.instancefactory.croquet.InstanceFactoryState.getInstance().handleAstChangeThatCouldBeOfInterest();
	}
	@Override
	protected StringBuilder updatePresentation( StringBuilder rv ) {
		rv.append( "delete:" );
		org.lgna.project.ast.NodeUtilities.safeAppendRepr(rv, statement, org.lgna.croquet.Application.getLocale());
		return rv;
	}
}
