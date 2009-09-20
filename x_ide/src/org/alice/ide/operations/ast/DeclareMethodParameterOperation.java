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
public class DeclareMethodParameterOperation extends AbstractCodeOperation {
	private edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method;
	private edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice parameter;
	private int index;
	private java.util.Map< edu.cmu.cs.dennisc.alice.ast.MethodInvocation, edu.cmu.cs.dennisc.alice.ast.Argument > map = new java.util.HashMap< edu.cmu.cs.dennisc.alice.ast.MethodInvocation, edu.cmu.cs.dennisc.alice.ast.Argument >();
	public DeclareMethodParameterOperation( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method ) {
		this.method = method;
		this.putValue( javax.swing.Action.NAME, "Add Parameter..." );
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice getCode() {
		return this.method;
	}
	public void perform( zoot.ActionContext actionContext ) {
		this.method = (edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice)this.getCode();
		org.alice.ide.createdeclarationpanes.CreateMethodParameterPane createMethodParameterPane = new org.alice.ide.createdeclarationpanes.CreateMethodParameterPane( method, this.getIDE().getMethodInvocations( method ) );
		this.parameter = createMethodParameterPane.showInJDialog( getIDE() );
		if( this.parameter != null ) {
			this.index = method.parameters.size();
			actionContext.commitAndInvokeRedoIfAppropriate();
		} else {
			actionContext.cancel();
		}
	}
	@Override
	public void redo() throws javax.swing.undo.CannotRedoException {
		org.alice.ide.ast.NodeUtilities.addParameter( this.map, this.method, this.parameter, this.index, this.getIDE().getMethodInvocations( this.method ) );
	}
	@Override
	public void undo() throws javax.swing.undo.CannotUndoException {
		org.alice.ide.ast.NodeUtilities.removeParameter( this.map, this.method, this.parameter, this.index, this.getIDE().getMethodInvocations( this.method ) );
	}
}
