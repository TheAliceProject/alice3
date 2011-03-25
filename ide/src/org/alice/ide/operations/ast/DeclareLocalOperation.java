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
package org.alice.ide.operations.ast;

/**
 * @author Dennis Cosgrove
 */
public class DeclareLocalOperation extends org.alice.ide.croquet.models.InputDialogWithPreviewOperation<org.alice.ide.declarationpanes.CreateLocalPane> {
	private final org.alice.ide.codeeditor.BlockStatementIndexPair blockStatementIndexPair;
	public DeclareLocalOperation( org.alice.ide.codeeditor.BlockStatementIndexPair blockStatementIndexPair ) {
		super( edu.cmu.cs.dennisc.alice.Project.GROUP, java.util.UUID.fromString( "aa3d337d-b409-46ae-816f-54f139b32d86" ) );
		this.blockStatementIndexPair = blockStatementIndexPair;
	}
	@Override
	protected org.alice.ide.declarationpanes.CreateLocalPane prologue(edu.cmu.cs.dennisc.croquet.InputDialogOperationContext<org.alice.ide.declarationpanes.CreateLocalPane> context) {
		return new org.alice.ide.declarationpanes.CreateLocalPane( this.blockStatementIndexPair );
	}
	@Override
	protected void epilogue(edu.cmu.cs.dennisc.croquet.InputDialogOperationContext<org.alice.ide.declarationpanes.CreateLocalPane> context, boolean isOk) {
		if( isOk ) {
			org.alice.ide.declarationpanes.CreateLocalPane createLocalPane = context.getMainPanel();
			edu.cmu.cs.dennisc.alice.ast.LocalDeclarationStatement localDeclarationStatement = createLocalPane.getActualInputValue();
			context.commitAndInvokeDo( new org.alice.ide.croquet.edits.ast.InsertStatementEdit( this.blockStatementIndexPair.getBlockStatement(), this.blockStatementIndexPair.getIndex(), localDeclarationStatement ) );
		} else {
			context.cancel();
		}
	}
}
