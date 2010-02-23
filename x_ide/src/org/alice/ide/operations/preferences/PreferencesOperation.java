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
package org.alice.ide.operations.preferences;

/**
 * @author Dennis Cosgrove
 */
public class PreferencesOperation extends org.alice.ide.operations.AbstractActionOperation {
	public PreferencesOperation() {
		super( edu.cmu.cs.dennisc.zoot.ZManager.UNKNOWN_GROUP );
		this.putValue( javax.swing.Action.NAME, "Preferences..." );
	}
	
	public void perform( edu.cmu.cs.dennisc.zoot.ActionContext actionContext ) {
		org.alice.ide.preferencesinputpane.PreferencesInputPane preferencesInputPane = new org.alice.ide.preferencesinputpane.PreferencesInputPane();
		preferencesInputPane.showInJDialog( this.getIDE() );
//		if( this.method != null ) {
//			this.prevCode = getIDE().getFocusedCode();
			actionContext.commitAndInvokeRedoIfAppropriate();
//		} else {
//			actionContext.cancel();
//		}
	}
	@Override
	public void doOrRedo() throws javax.swing.undo.CannotRedoException {
	}
	@Override
	public void undo() throws javax.swing.undo.CannotUndoException {
	}
	@Override
	public boolean isSignificant() {
		return false;
//		return true;
	}
	
}
