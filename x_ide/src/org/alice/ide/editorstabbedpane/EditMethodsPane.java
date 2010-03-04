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
package org.alice.ide.editorstabbedpane;

/**
* @author Dennis Cosgrove
*/
public abstract class EditMethodsPane extends EditMembersPane< edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice > {
	private edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType;
	public EditMethodsPane( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType ) {
		super( declaringType.methods );
		this.declaringType = declaringType;
	}
	@Override
	protected java.awt.Component createCellRendererComponent( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice e ) {
		return new org.alice.ide.codeeditor.MethodHeaderPane( e, null );
	}
	@Override
	protected edu.cmu.cs.dennisc.zoot.ActionOperation createEditOperation( java.util.UUID groupUUID, String name ) {
		return new org.alice.ide.operations.ast.AbstractRenameNodeOperation( groupUUID, name ) {
			public void perform( edu.cmu.cs.dennisc.zoot.ActionContext actionContext ) {
				edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method = getSelectedItem();
				if( method != null ) {
					this.perform( actionContext, method.name, new org.alice.ide.name.validators.MethodNameValidator( method ) );
					getIDE().setFocusedCode( method );
				} else {
					actionContext.cancel();
				}
			}
		};
	}
	protected abstract org.alice.ide.createdeclarationpanes.CreateMethodPane createCreateMethodPane( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType );
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice createMember() {
		org.alice.ide.createdeclarationpanes.CreateMethodPane createMethodPane = this.createCreateMethodPane( declaringType );
		return createMethodPane.showInJDialog( org.alice.ide.IDE.getSingleton() );
	}
	@Override
	protected boolean isEditItemEnabledFor( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice e ) {
		return e.isSignatureLocked.getValue() == false;
	}
	@Override
	protected boolean isRemoveItemEnabledFor( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice e ) {
		return e.isDeletionAllowed.getValue();
	}
}
