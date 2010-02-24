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
public class FocusCodeOperation extends org.alice.ide.operations.AbstractActionOperation {
	private edu.cmu.cs.dennisc.alice.ast.AbstractCode nextCode;
	public FocusCodeOperation( edu.cmu.cs.dennisc.alice.ast.AbstractCode nextCode ) {
		super( org.alice.ide.IDE.INTERFACE_GROUP );
		this.nextCode = nextCode;
		String name;
		if( nextCode instanceof edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInAlice ) {
			name = "Edit Constructor";
		} else {
			name = "Edit";
		}
		this.putValue( javax.swing.Action.NAME, name );
	}
	public void perform( edu.cmu.cs.dennisc.zoot.ActionContext actionContext ) {
		final edu.cmu.cs.dennisc.alice.ast.AbstractCode prevCode = getIDE().getFocusedCode();
		actionContext.commitAndInvokeDo( new edu.cmu.cs.dennisc.zoot.AbstractEdit() {
			@Override
			public void doOrRedo( boolean isDo ) {
				getIDE().setFocusedCode( nextCode );
			}
			@Override
			public void undo() {
				getIDE().setFocusedCode( prevCode );
			}
		} );
	}
}
