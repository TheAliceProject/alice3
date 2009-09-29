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
public abstract class AbstractIsEnabledStatementOperation extends org.alice.ide.operations.AbstractActionOperation {
	private edu.cmu.cs.dennisc.alice.ast.Statement statement;
	private boolean prevValue;
	public AbstractIsEnabledStatementOperation( edu.cmu.cs.dennisc.alice.ast.Statement statement ) {
		this.statement = statement;
	}
	public void perform( edu.cmu.cs.dennisc.zoot.ActionContext actionContext ) {
		this.prevValue = this.statement.isEnabled.getValue();
		actionContext.commitAndInvokeRedoIfAppropriate();
	}
	protected abstract boolean getDesiredValue();
	@Override
	public final void doOrRedo() throws javax.swing.undo.CannotRedoException {
		this.statement.isEnabled.setValue( this.getDesiredValue() );
	}
	@Override
	public final void undo() throws javax.swing.undo.CannotUndoException {
		this.statement.isEnabled.setValue( this.prevValue );
	}
	@Override
	public boolean isSignificant() {
		return true;
	}
}
