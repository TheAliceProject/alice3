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
package org.alice.ide.operations.file;

/**
 * @author Dennis Cosgrove
 */
public class RevertProjectOperation extends org.alice.ide.operations.AbstractActionOperation {
	public RevertProjectOperation() {
		super( org.alice.ide.IDE.IO_GROUP );
		this.putValue( javax.swing.Action.NAME, "Revert" );
		this.putValue( javax.swing.Action.MNEMONIC_KEY, java.awt.event.KeyEvent.VK_R );
	}
	public void perform( edu.cmu.cs.dennisc.zoot.ActionContext actionContext ) {
		if( javax.swing.JOptionPane.YES_OPTION == javax.swing.JOptionPane.showConfirmDialog( this.getIDE(), "WARNING: revert restores your project to the last saved version.\nWould you like to continue with revert?", "Revert?", javax.swing.JOptionPane.YES_NO_CANCEL_OPTION, javax.swing.JOptionPane.WARNING_MESSAGE ) ) {
			actionContext.commitAndInvokeRedoIfAppropriate( new edu.cmu.cs.dennisc.zoot.AbstractEdit() {
				@Override
				public void doOrRedo() {
					getIDE().revert();
				}
				@Override
				public void undo() {
					throw new AssertionError();
				}
				@Override
				public boolean canUndo() {
					return false;
				}
			} );
		} else {
			actionContext.cancel();
		}
	}
}
