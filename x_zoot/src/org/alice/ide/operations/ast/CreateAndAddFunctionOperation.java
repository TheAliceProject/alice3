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
public class CreateAndAddFunctionOperation extends org.alice.ide.AbstractActionOperation {
	private edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type;
	public CreateAndAddFunctionOperation( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type ) {
		this.type = type;
		this.putValue( javax.swing.Action.NAME, "create new function..." );
	}
	public void perform( zoot.ActionContext actionContext ) {
		org.alice.ide.inputpanes.CreateFunctionPane createFunctionPane = new org.alice.ide.inputpanes.CreateFunctionPane( this.type );
		edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method = createFunctionPane.showInJDialog( getIDE() );
		if( method != null ) {
			this.type.methods.add( method );
			actionContext.commit();
		} else {
			actionContext.cancel();
		}

//		org.alice.ide.inputpanes.CreateTypedDeclarationPane createTypedDeclarationPane = new org.alice.ide.inputpanes.CreateTypedDeclarationPane( this.type );
//		String name = createTypedDeclarationPane.showInJDialog( getIDE() );
//		if( name != null && name.length() > 0 ) {
//			//this.prevValue = this.localDeclaredInAlice.name.getValue();
//			actionContext.commit();
//		} else {
//			actionContext.cancel();
//		}
	}
}
