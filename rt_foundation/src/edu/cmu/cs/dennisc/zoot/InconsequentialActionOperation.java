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
package edu.cmu.cs.dennisc.zoot;

//todo: rename
/**
 * @author Dennis Cosgrove
 */
public abstract class InconsequentialActionOperation extends AbstractActionOperation {
	public InconsequentialActionOperation() {
		super( ZManager.UNKNOWN_GROUP );
	}
	protected abstract void performInternal(edu.cmu.cs.dennisc.zoot.ActionContext actionContext);
	public final void perform(edu.cmu.cs.dennisc.zoot.ActionContext actionContext) {
		performInternal(actionContext);
		if( actionContext.isCancelled() ) {
			//pass
		} else {
			actionContext.commitAndInvokeRedoIfAppropriate();
		}
	}
	@Override
	public final boolean canDoOrRedo() {
		return false;
	}
	@Override
	public final boolean canUndo() {
		return false;
	}
	@Override
	public final boolean isSignificant() {
		return false;
	}
	@Override
	public void doOrRedo() throws javax.swing.undo.CannotRedoException {
		throw new javax.swing.undo.CannotRedoException();
	}
	@Override
	public void undo() throws javax.swing.undo.CannotUndoException {
		throw new javax.swing.undo.CannotUndoException();
	}
}
