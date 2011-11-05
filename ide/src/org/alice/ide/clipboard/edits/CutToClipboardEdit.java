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

package org.alice.ide.clipboard.edits;

/**
 * @author Dennis Cosgrove
 */
public class CutToClipboardEdit extends org.lgna.croquet.edits.Edit {
	private org.lgna.project.ast.Statement statement;
	private org.lgna.project.ast.BlockStatement originalBlockStatement;
	private int originalIndex;
	public CutToClipboardEdit( org.lgna.croquet.history.CompletionStep completionStep, org.lgna.project.ast.Statement statement ) {
		super( completionStep );
		this.statement = statement;
		this.originalBlockStatement = (org.lgna.project.ast.BlockStatement)this.statement.getParent();;
		assert this.originalBlockStatement != null;
		this.originalIndex = this.originalBlockStatement.statements.indexOf( this.statement );
		assert this.originalIndex != -1;
	}
	public CutToClipboardEdit( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder, Object step ) {
		super( binaryDecoder, step );
		org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
		org.lgna.project.Project project = ide.getProject();
		java.util.UUID statementId = binaryDecoder.decodeId();
		this.statement = org.lgna.project.ProgramTypeUtilities.lookupNode( project, statementId );
		java.util.UUID blockStatementId = binaryDecoder.decodeId();
		this.originalBlockStatement = org.lgna.project.ProgramTypeUtilities.lookupNode( project, blockStatementId );
		this.originalIndex = binaryDecoder.decodeInt();
	}
	@Override
	public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		super.encode( binaryEncoder );
		binaryEncoder.encode( this.statement.getUUID() );
		binaryEncoder.encode( this.originalBlockStatement.getUUID() );
		binaryEncoder.encode( this.originalIndex );
	}
	@Override
	protected void doOrRedoInternal( boolean isDo ) {
		org.alice.ide.clipboard.Clipboard.getInstance().push( this.statement );
		this.originalBlockStatement.statements.remove( this.originalIndex );
	}
	@Override
	protected void undoInternal() {
		org.alice.ide.clipboard.Clipboard.getInstance().pop();
		this.originalBlockStatement.statements.add( this.originalIndex, this.statement );
	}
	@Override
	protected StringBuilder updatePresentation( StringBuilder rv, java.util.Locale locale ) {
		rv.append( "cut to clipboard" );
		return rv;
	}
}
