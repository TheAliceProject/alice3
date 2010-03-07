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
		super( edu.cmu.cs.dennisc.alice.Project.GROUP_UUID );
		this.type = type;
	}
	protected abstract org.alice.ide.declarationpanes.CreateDeclarationPane<edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice> createCreateMethodPane( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type );
	public void perform( edu.cmu.cs.dennisc.zoot.ActionContext actionContext ) {
		org.alice.ide.declarationpanes.CreateDeclarationPane<edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice> createMethodPane = this.createCreateMethodPane( this.type );
		final edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method = createMethodPane.showInJDialog( getIDE() );
		if( method != null ) {
			final edu.cmu.cs.dennisc.alice.ast.AbstractCode prevCode = getIDE().getFocusedCode();
			actionContext.commitAndInvokeDo( new edu.cmu.cs.dennisc.zoot.AbstractEdit() {
				@Override
				public void doOrRedo( boolean isDo ) {
					type.methods.add( method );
					getIDE().setFocusedCode( method );
				}
				@Override
				public void undo() {
					int index = type.methods.indexOf( method );
					if( index != -1 ) {
						type.methods.remove( index );
						getIDE().setFocusedCode( prevCode );
					} else {
						throw new javax.swing.undo.CannotUndoException();
					}
				}
				@Override
				protected StringBuffer updatePresentation(StringBuffer rv, java.util.Locale locale) {
					rv.append( "declare:" );
					edu.cmu.cs.dennisc.alice.ast.Node.safeAppendRepr(rv, method, locale);
					return rv;
				}
			} );
		} else {
			actionContext.cancel();
		}
	}
}
