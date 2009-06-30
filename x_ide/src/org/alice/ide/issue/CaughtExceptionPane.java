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
public class CaughtExceptionPane extends edu.cmu.cs.dennisc.issue.AbstractCaughtExceptionPane {
	@Override
	protected java.lang.String getProjectKey() {
		return "AIIIP";
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
		if( javax.swing.JOptionPane.YES_OPTION == javax.swing.JOptionPane.showConfirmDialog( this, "Submitting your current project would greatly help the Alice team in diagnosing and fixing this bug.\n\nWould you like to submit your project with this bug report?", "Submit project?", javax.swing.JOptionPane.YES_NO_OPTION ) ) {
			rv.add( new CurrentProjectAttachment() );
		}
		return rv;

	}
	@Override
	protected edu.cmu.cs.dennisc.issue.Issue updateIssue( edu.cmu.cs.dennisc.issue.Issue rv ) {
		rv.setAffectsVersions( edu.cmu.cs.dennisc.alice.Version.getCurrentVersionText() );
		return super.updateIssue( rv );
	}
}
