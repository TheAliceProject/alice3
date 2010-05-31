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
public class DeclareLocalOperation extends org.alice.ide.operations.InputDialogWithPreviewOperation {
	private org.alice.ide.declarationpanes.CreateLocalPane createLocalPane;
	private edu.cmu.cs.dennisc.alice.ast.BlockStatement block;
	private edu.cmu.cs.dennisc.alice.ast.LocalDeclarationStatement localDeclarationStatement;
	public DeclareLocalOperation( edu.cmu.cs.dennisc.alice.ast.BlockStatement block ) {
		super( edu.cmu.cs.dennisc.alice.Project.GROUP, java.util.UUID.fromString( "aa3d337d-b409-46ae-816f-54f139b32d86" ) );
		this.block = block;
	}
	@Override
	protected edu.cmu.cs.dennisc.croquet.Component<?> prologue(edu.cmu.cs.dennisc.croquet.ModelContext context) {
		this.localDeclarationStatement = null;
		this.createLocalPane = new org.alice.ide.declarationpanes.CreateLocalPane( this.block );
		return this.createLocalPane;
	}
	@Override
	protected org.alice.ide.preview.PanelWithPreview getPanelWithPreview() {
		return this.createLocalPane;
	}
	
	public edu.cmu.cs.dennisc.alice.ast.LocalDeclarationStatement getLocalDeclarationStatement() {
		return this.localDeclarationStatement;
	}
	@Override
	protected void epilogue(edu.cmu.cs.dennisc.croquet.ModelContext context, boolean isOk) {
		if( isOk ) {
			this.localDeclarationStatement = this.createLocalPane.getActualInputValue();
			context.finish();
		} else {
			context.cancel();
		}
	}
}
