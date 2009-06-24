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
package org.alice.ide.operations.help;

/**
 * @author Dennis Cosgrove
 */
public class WarningOperation extends org.alice.ide.operations.AbstractActionOperation {
	private boolean isSolicited;
	public WarningOperation( boolean isSolicited ) {
		this.putValue( javax.swing.Action.NAME, "Display Warning..." );
		this.isSolicited = isSolicited;
	}
	public void perform( zoot.ActionContext actionContext ) {
		org.alice.ide.warningpane.WarningPane warningPane = new org.alice.ide.warningpane.WarningPane( this.isSolicited );
		javax.swing.JOptionPane.showMessageDialog( this.getIDE(), warningPane, "Alice3 is currently under development", javax.swing.JOptionPane.WARNING_MESSAGE ); 
		actionContext.put( org.alice.ide.IDE.IS_PROJECT_CHANGED_KEY, false );
		actionContext.commit();
	}
}
