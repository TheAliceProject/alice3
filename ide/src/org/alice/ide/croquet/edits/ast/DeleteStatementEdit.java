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
public class DeleteStatementEdit extends BlockStatementEdit< org.alice.ide.croquet.models.ast.DeleteStatementOperation > {
	//todo:
	private static org.alice.ide.croquet.models.ast.DeleteStatementOperation getModel( org.lgna.croquet.history.CompletionStep<org.alice.ide.croquet.models.ast.DeleteStatementOperation> completionStep ) {
		return completionStep.getModel();
	}
	private final int index;
	public DeleteStatementEdit( org.lgna.croquet.history.CompletionStep completionStep ) {
		super( completionStep, (org.lgna.project.ast.BlockStatement) (getModel( completionStep ).getStatement().getParent() ) );
		org.lgna.project.ast.Statement statement = this.getModel().getStatement();
		this.index = this.getBlockStatement().statements.indexOf( statement );
	}
	public DeleteStatementEdit( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder, Object step ) {
		super( binaryDecoder, step );
		this.index = binaryDecoder.decodeInt();
	}
	@Override
	protected final void doOrRedoInternal( boolean isDo ) {
		org.lgna.project.ast.BlockStatement blockStatement = this.getBlockStatement();
		org.lgna.project.ast.Statement statement = this.getModel().getStatement();
		assert blockStatement.statements.indexOf( statement ) == this.index;
		blockStatement.statements.remove( index );
		
		System.err.println( "todo: preserve deletion " + statement );
		//todo: remove
		org.alice.ide.instancefactory.InstanceFactoryState.getInstance().handleAstChangeTheCouldBeOfInterest();
	}
	@Override
	protected final void undoInternal() {
		org.lgna.project.ast.BlockStatement blockStatement = this.getBlockStatement();
		org.lgna.project.ast.Statement statement = this.getModel().getStatement();
		blockStatement.statements.add( index, statement );
		//todo: remove
		org.alice.ide.instancefactory.InstanceFactoryState.getInstance().handleAstChangeTheCouldBeOfInterest();
	}
	@Override
	protected StringBuilder updatePresentation( StringBuilder rv, java.util.Locale locale ) {
		org.lgna.project.ast.Statement statement = this.getModel().getStatement();
		rv.append( "delete:" );
		org.lgna.project.ast.NodeUtilities.safeAppendRepr(rv, statement, locale);
		return rv;
	}
}
