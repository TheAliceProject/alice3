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

public class ReportSubmissionConfiguration implements edu.cmu.cs.dennisc.issue.ReportSubmissionConfiguration {
	public java.net.URL getJIRAViaRPCServer() throws java.net.MalformedURLException {
		return new java.net.URL( "http://bugs.alice.org:8080/rpc/xmlrpc" );
	}
	public java.net.URL getJIRAViaSOAPServer() throws java.net.MalformedURLException {
		return new java.net.URL( "http://bugs.alice.org:8080/rpc/soap/jirasoapservice-v2" );
	}
	public edu.cmu.cs.dennisc.jira.rpc.Authenticator getJIRAViaRPCAuthenticator() {
		return new org.alice.ide.issue.jira.rpc.Authenticator();
	}
	public edu.cmu.cs.dennisc.jira.soap.Authenticator getJIRAViaSOAPAuthenticator() {
		return new org.alice.ide.issue.jira.soap.Authenticator();
	}
	public String getMailServer() {
		return "haru.pc.cc.cmu.edu";
	}
	public edu.cmu.cs.dennisc.mail.AbstractAuthenticator getMailAuthenticator() {
		return new org.alice.ide.issue.mail.Authenticator();
	}
	public String getMailRecipient() {
		return "alice.bugs.3.beta.xxxx@gmail.com";
	}
}
