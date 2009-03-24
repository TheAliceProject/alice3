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
package org.alice.ide.operations.edit;

/**
 * @author Dennis Cosgrove
 */
public class CopyOperation extends org.alice.ide.operations.AbstractActionOperation {
	public CopyOperation() {
		this.putValue( javax.swing.Action.NAME, "Copy" );
		this.putValue( javax.swing.Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_C, edu.cmu.cs.dennisc.awt.event.InputEventUtilities.getAcceleratorMask() ) );
	}
	public void perform( zoot.ActionContext actionContext ) {
		String title = "Copy coming soon";
		String message = "Copy is not yet implemented.  Apologies.";
		message += "\n\nNOTE: one can copy by dragging with the ";
		if( edu.cmu.cs.dennisc.lang.SystemUtilities.isMac() ) {
			message += "Alt";
		} else {
			message += "Control";
		}
		message += " key pressed.";
		javax.swing.JOptionPane.showMessageDialog( this.getIDE(), message, title, javax.swing.JOptionPane.INFORMATION_MESSAGE );
		actionContext.cancel();
	}
}
