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
public abstract class PostIssueOperation extends org.alice.ide.operations.AbstractActionOperation {
	protected abstract edu.cmu.cs.dennisc.jira.JIRAReport.Type getIssueType();
	public void perform( zoot.ActionContext actionContext ) {
		org.alice.ide.issue.PostIssuePane pane = new org.alice.ide.issue.PostIssuePane( this.getIssueType() );
		
		javax.swing.JFrame owner = org.alice.ide.IDE.getSingleton();
		javax.swing.JFrame dialog = edu.cmu.cs.dennisc.swing.JFrameUtilities.createPackedJFrame( pane, "Report Issue", javax.swing.WindowConstants.DISPOSE_ON_CLOSE );
		edu.cmu.cs.dennisc.awt.WindowUtilties.setLocationOnScreenToCenteredWithin( dialog, this.getSourceComponent( actionContext ) );
		dialog.getRootPane().setDefaultButton( pane.getSubmitButton() );
		dialog.setVisible( true );
		if( pane.isSubmitAttempted() ) {
			if( pane.isSubmitBackgrounded() ) {
				//javax.swing.JOptionPane.showMessageDialog( frame, "Thank you for submitting a bug report." );
			} else {
				if( pane.isSubmitSuccessful() ) {
					javax.swing.JOptionPane.showMessageDialog( owner, "Your issue report has been successfully submitted.  Thank you." );
				} else {
					javax.swing.JOptionPane.showMessageDialog( owner, "Your issue report FAILED to submitted.  Thank you for trying." );
				}
			}
		}
	}
}
