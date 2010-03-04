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
public abstract class AbstractRenameNodeOperation extends org.alice.ide.operations.AbstractActionOperation {
	public AbstractRenameNodeOperation( java.util.UUID groupUUID, String name ) {
		super( groupUUID );
		this.putValue( javax.swing.Action.NAME, name );
	}
	protected final void perform( edu.cmu.cs.dennisc.zoot.ActionContext actionContext, final edu.cmu.cs.dennisc.property.StringProperty nameProperty, org.alice.ide.name.validators.NodeNameValidator nodeNameValidator ) {
		org.alice.ide.name.RenamePane renameNodePane = new org.alice.ide.name.RenamePane( nodeNameValidator );
		renameNodePane.setAndSelectNameText( nameProperty.getValue() );
		final String nextValue = renameNodePane.showInJDialog( this.getIDE() );
		if( nextValue != null && nextValue.length() > 0 ) {
			final String prevValue = nameProperty.getValue();
			actionContext.commitAndInvokeDo( new edu.cmu.cs.dennisc.zoot.AbstractEdit() {
				@Override
				public void doOrRedo( boolean isDo ) {
					nameProperty.setValue( nextValue );
				}
				@Override
				public void undo() {
					nameProperty.setValue( prevValue );
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
