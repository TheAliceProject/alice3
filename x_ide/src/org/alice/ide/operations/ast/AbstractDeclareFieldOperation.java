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
public abstract class AbstractDeclareFieldOperation extends org.alice.ide.operations.AbstractActionOperation {
	private edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field;
	private int index;
	protected abstract edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice getOwnerType();
	protected abstract edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice createField( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice ownerType );
	public AbstractDeclareFieldOperation() {
		super( org.alice.ide.IDE.PROJECT_GROUP );
	}
	public final void perform( edu.cmu.cs.dennisc.zoot.ActionContext actionContext ) {
		edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice ownerType = this.getOwnerType();
		this.field = this.createField( ownerType );
		if( this.field != null ) {
			this.index = ownerType.fields.size();
			actionContext.commitAndInvokeRedoIfAppropriate();
		} else {
			actionContext.cancel();
		}
	}
	@Override
	public boolean isSignificant() {
		return true;
	}
	@Override
	public void doOrRedo() throws javax.swing.undo.CannotRedoException {
		edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice ownerType = this.getOwnerType();
		ownerType.fields.add( this.index, this.field );
	}
	@Override
	public void undo() throws javax.swing.undo.CannotUndoException {
		edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice ownerType = this.getOwnerType();
		if( ownerType.fields.get( this.index ) == this.field ) {
			ownerType.fields.remove( this.index );
		} else {
			throw new javax.swing.undo.CannotUndoException();
		}
	}
}
