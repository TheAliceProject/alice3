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
		final org.alice.ide.issue.PostIssuePane pane = new org.alice.ide.issue.PostIssuePane( this.getIssueType() );

		final javax.swing.JFrame owner = org.alice.ide.IDE.getSingleton();
		javax.swing.JFrame window = edu.cmu.cs.dennisc.swing.JFrameUtilities.createPackedJFrame( pane, "Report Issue", javax.swing.WindowConstants.DISPOSE_ON_CLOSE );
		edu.cmu.cs.dennisc.awt.WindowUtilties.setLocationOnScreenToCenteredWithin( window, this.getSourceComponent( actionContext ) );
		window.getRootPane().setDefaultButton( pane.getSubmitButton() );
		window.addComponentListener( new java.awt.event.ComponentListener() {
			public void componentHidden( java.awt.event.ComponentEvent e ) {
				if( pane.isSubmitAttempted() ) {
					if( pane.isSubmitBackgrounded() ) {
						//pass
					} else {
						if( pane.isSubmitSuccessful() ) {
							
							java.net.URL urlResult = pane.getURLResult();
							final String MESSAGE = "Your issue report has been successfully submitted.  Thank you.";
							Object message;
							String title = "Report Successfully Submitted";
							if( urlResult != null ) {
								edu.cmu.cs.dennisc.ui.html.HTMLPane htmlPane = new edu.cmu.cs.dennisc.ui.html.HTMLPane();
								StringBuffer sb = new StringBuffer();
								sb.append( "<html>" );
								sb.append( MESSAGE );
								sb.append( "<br>" );
								sb.append( "<br>" );
								//sb.append( "You can view your report (if it is public) at: " );
								sb.append( "View your report: " );
								sb.append( "<a href=\"" );
								sb.append( urlResult.toExternalForm() );
								sb.append( "\">" );
								sb.append( urlResult.toExternalForm() );
								sb.append( "</a>" );
								sb.append( "</html>" );
								htmlPane.setText( sb.toString() );
								htmlPane.setOpaque( false );
								message = htmlPane;
							} else {
								message = MESSAGE;
							}
							javax.swing.JOptionPane.showMessageDialog( owner, message, title, javax.swing.JOptionPane.PLAIN_MESSAGE );
						} else {
							javax.swing.JOptionPane.showMessageDialog( owner, "Your issue report FAILED to submit.  Thank you for trying.", "Report Submission Failed", javax.swing.JOptionPane.ERROR_MESSAGE );
						}
					}
				}
			}

			public void componentMoved( java.awt.event.ComponentEvent e ) {
			}

			public void componentResized( java.awt.event.ComponentEvent e ) {
			}

			public void componentShown( java.awt.event.ComponentEvent e ) {
			}

		} );
		window.setVisible( true );
	}
}
