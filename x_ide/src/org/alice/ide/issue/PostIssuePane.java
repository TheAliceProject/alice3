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
package org.alice.ide.issue;

/**
 * @author Dennis Cosgrove
 */
public class PostIssuePane extends edu.cmu.cs.dennisc.issue.AbstractPostIssuePane {
	public PostIssuePane( edu.cmu.cs.dennisc.issue.Issue.Type issueType ) {
		super( issueType );

		javax.swing.JPanel northPane = new javax.swing.JPanel();

		edu.cmu.cs.dennisc.swing.Hyperlink hyperlink = new edu.cmu.cs.dennisc.swing.Hyperlink( "http://bugs.alice.org:8080" );

		northPane.add( zoot.ZLabel.acquire( "alice bug database: " ) );
		northPane.add( hyperlink );

		this.add( northPane, java.awt.BorderLayout.NORTH );
	}
	@Override
	protected String getProjectKey() {
		return "AIII";
	}
	@Override
	protected java.net.URL getJIRAViaRPCServer() throws java.net.MalformedURLException {
		return new java.net.URL( "http://bugs.alice.org:8080/rpc/xmlrpc" );
	}
	@Override
	protected java.net.URL getJIRAViaSOAPServer() throws java.net.MalformedURLException {
		return new java.net.URL( "http://bugs.alice.org:8080/rpc/soap/jirasoapservice-v2" );
	}
	@Override
	protected edu.cmu.cs.dennisc.jira.rpc.Authenticator getJIRAViaRPCAuthenticator() {
		return new org.alice.ide.issue.jira.rpc.Authenticator();
	}
	@Override
	protected edu.cmu.cs.dennisc.jira.soap.Authenticator getJIRAViaSOAPAuthenticator() {
		return new org.alice.ide.issue.jira.soap.Authenticator();
	}
	@Override
	protected String getMailServer() {
		return "haru.pc.cc.cmu.edu";
	}
	@Override
	protected edu.cmu.cs.dennisc.mail.AbstractAuthenticator getMailAuthenticator() {
		return new org.alice.ide.issue.mail.Authenticator();
	}
	@Override
	protected String getMailRecipient() {
		return "alice.bugs.3.beta.xxxx@gmail.com";
	}

	@Override
	protected java.util.ArrayList< edu.cmu.cs.dennisc.issue.Attachment > updateCriticalAttachments( java.util.ArrayList< edu.cmu.cs.dennisc.issue.Attachment > rv ) {
		rv = super.updateCriticalAttachments( rv );
		if( javax.swing.JOptionPane.YES_OPTION == javax.swing.JOptionPane.showConfirmDialog( this, "Is your current project relevant to this issue and are you willing to attach your project with this report?", "Submit project?", javax.swing.JOptionPane.YES_NO_OPTION ) ) {
			rv.add( new CurrentProjectAttachment() );
		}
		return rv;

	}
	@Override
	protected edu.cmu.cs.dennisc.issue.Issue updateIssue( edu.cmu.cs.dennisc.issue.Issue rv ) {
		rv.setAffectsVersions( edu.cmu.cs.dennisc.alice.Version.getCurrentVersionText() );
		return super.updateIssue( rv );
	}

	public static void main( String[] args ) {
		PostIssuePane pane = new PostIssuePane( edu.cmu.cs.dennisc.issue.Issue.Type.BUG );
		javax.swing.JFrame frame = org.alice.ide.IDE.getSingleton();
		if( frame != null ) {
			//pass
		} else {
			frame = new javax.swing.JFrame();
		}
		javax.swing.JDialog window = new javax.swing.JDialog( frame, "Report Issue", true );
		pane.setWindow( window );
		window.getContentPane().add( pane );
		window.pack();
		window.setDefaultCloseOperation( javax.swing.JFrame.DISPOSE_ON_CLOSE );
		window.getRootPane().setDefaultButton( pane.getSubmitButton() );
		window.setVisible( true );
	}

}
