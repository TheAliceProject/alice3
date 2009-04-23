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

import org.alice.ide.createdeclarationpanes.CreateDeclarationPane;

/**
 * @author Dennis Cosgrove
 */
public abstract class DeclareMethodOperation extends org.alice.ide.operations.AbstractActionOperation {
	private edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type;
	public DeclareMethodOperation( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type ) {
		this.type = type;
	}
	protected abstract CreateDeclarationPane<edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice> createCreateMethodPane( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type );
	public void perform( zoot.ActionContext actionContext ) {
		CreateDeclarationPane<edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice> createMethodPane = this.createCreateMethodPane( this.type );
		edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method = createMethodPane.showInJDialog( getIDE() );
		if( method != null ) {
			this.type.methods.add( method );
			actionContext.perform( new FocusCodeOperation( method ), null, zoot.ZManager.CANCEL_IS_FUTILE );
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
