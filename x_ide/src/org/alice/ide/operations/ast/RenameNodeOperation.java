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
public abstract class RenameNodeOperation extends org.alice.ide.operations.AbstractActionOperation {
	private edu.cmu.cs.dennisc.property.StringProperty nameProperty;
	private org.alice.ide.name.validators.NodeNameValidator nodeNameValidator;
	public RenameNodeOperation( edu.cmu.cs.dennisc.property.StringProperty nameProperty, org.alice.ide.name.validators.NodeNameValidator nodeNameValidator ) {
		super( org.alice.ide.IDE.PROJECT_GROUP );
		this.nameProperty = nameProperty;
		this.nodeNameValidator = nodeNameValidator;
		this.putValue( javax.swing.Action.NAME, "Rename..." );
	}
	public void perform( edu.cmu.cs.dennisc.zoot.ActionContext actionContext ) {
		org.alice.ide.name.RenamePane renameNodePane = new org.alice.ide.name.RenamePane( this.nodeNameValidator );
		renameNodePane.setAndSelectNameText( this.nameProperty.getValue() );
		final String nextValue = renameNodePane.showInJDialog( this.getIDE() );
		if( nextValue != null && nextValue.length() > 0 ) {
			final String prevValue = nameProperty.getValue();
			actionContext.commitAndInvokeDo( new edu.cmu.cs.dennisc.zoot.AbstractEdit() {
				@Override
				public void doOrRedo( boolean isDo ) {
					RenameNodeOperation.this.nameProperty.setValue( nextValue );
				}
				@Override
				public void undo() {
					RenameNodeOperation.this.nameProperty.setValue( prevValue );
				}
				@Override
				protected StringBuffer updatePresentation(StringBuffer rv, java.util.Locale locale) {
					rv.append( "rename: " );
					rv.append( prevValue );
					rv.append( " ===> " );
					rv.append( nextValue );
					return rv;
				}
			} );
		} else {
			actionContext.cancel();
		}
	}
}
