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
package alice.ide.operations.ast;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractDeleteNodeOperation extends alice.ide.AbstractActionOperation {
	private edu.cmu.cs.dennisc.alice.ast.Node node;
	private edu.cmu.cs.dennisc.alice.ast.NodeListProperty owner;
	private int index;
	public AbstractDeleteNodeOperation( edu.cmu.cs.dennisc.alice.ast.Node node, edu.cmu.cs.dennisc.alice.ast.NodeListProperty< ? extends edu.cmu.cs.dennisc.alice.ast.Node > owner ) {
		this.node = node;
		this.owner = owner;
		this.putValue( javax.swing.Action.NAME, "delete" );
	}
	protected abstract boolean isClearToDelete();
	public void perform( zoot.ActionContext actionContext ) {
		if( this.isClearToDelete() ) {
			this.index = this.owner.indexOf( this.node );
			this.redo();
			actionContext.commit();
		} else {
			actionContext.cancel();
		}
	}
	public final void redo() {
		this.owner.remove( this.index );
	}
	public final void undo() {
		this.owner.add( this.index, this.node );
	}
}
