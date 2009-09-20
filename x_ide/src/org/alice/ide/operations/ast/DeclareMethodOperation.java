/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package org.alice.ide.operations.ast;

/**
 * @author Dennis Cosgrove
 */
public abstract class DeclareMethodOperation extends org.alice.ide.operations.AbstractActionOperation {
	private edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type;
	public DeclareMethodOperation( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type ) {
		this.type = type;
	}
	protected abstract org.alice.ide.createdeclarationpanes.CreateDeclarationPane<edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice> createCreateMethodPane( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type );
	private edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method;
	private edu.cmu.cs.dennisc.alice.ast.AbstractCode prevCode;
	public void perform( zoot.ActionContext actionContext ) {
		org.alice.ide.createdeclarationpanes.CreateDeclarationPane<edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice> createMethodPane = this.createCreateMethodPane( this.type );
		this.method = createMethodPane.showInJDialog( getIDE() );
		if( this.method != null ) {
			this.prevCode = getIDE().getFocusedCode();
			actionContext.commitAndInvokeRedoIfAppropriate();
		} else {
			actionContext.cancel();
		}
	}
	@Override
	public void redo() throws javax.swing.undo.CannotRedoException {
		this.type.methods.add( this.method );
		this.getIDE().setFocusedCode( this.method );
	}
	@Override
	public void undo() throws javax.swing.undo.CannotUndoException {
		int index = this.type.methods.indexOf( this.method );
		if( index != -1 ) {
			this.type.methods.remove( index );
			this.getIDE().setFocusedCode( this.prevCode );
		} else {
			throw new javax.swing.undo.CannotUndoException();
		}
	}
	@Override
	public boolean isSignificant() {
		return true;
	}
}
