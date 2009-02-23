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

package edu.cmu.cs.dennisc.issue;

/**
 * @author Dennis Cosgrove
 */
public class UploadWorker extends org.jdesktop.swingworker.SwingWorker< Boolean, String > {
	private ProgressPane progressPane;

	private Issue issue;
	
	private java.net.URL jiraServer;
	
	private String mailServer;
	private edu.cmu.cs.dennisc.mail.AbstractAuthenticator mailAuthenticator;
	private String subject;
	private String reporterEMailAddress;
	private String reporterName;

	public UploadWorker( ProgressPane progressPane ) {
		this.progressPane = progressPane;
	}
	public void initialize( Issue issue, java.net.URL jiraServer, String mailServer, edu.cmu.cs.dennisc.mail.AbstractAuthenticator mailAuthenticator, String subject, String reporterEMailAddress, String reporterName ) {
		this.jiraServer = jiraServer;
		this.mailServer = mailServer;
		this.mailAuthenticator = mailAuthenticator;
		this.issue = issue;
		this.subject = subject;
		this.reporterEMailAddress = reporterEMailAddress;
		this.reporterName = reporterName;
	}
	@Override
	protected void process( java.util.List< String > chunks ) {
		this.progressPane.handleProcess( chunks );
	}
	private void process( String... chunks ) {
		this.process( edu.cmu.cs.dennisc.util.CollectionUtilities.createArrayList( chunks ) );
	}

	//	protected abstract void uploadToJIRA() throws Exception;
	//	protected abstract void sendMail( boolean isTransportLayerSecurityDesired, Integer portOverride ) throws Exception;

	private String getSummary() {
		String rv = this.issue.getSummary();
		if( rv != null && rv.length() > 0 ) {
			//pass
		} else {
			//todo?
			rv = "";
		}
		return rv;
	}
	
	protected void uploadToJIRA() throws Exception {
//		final boolean STREAM_MESSAGES = true;
//		redstone.xmlrpc.XmlRpcClient client = new redstone.xmlrpc.XmlRpcClient( this.jiraServer, STREAM_MESSAGES );
//		Object token = client.invoke( "jira1.login", new Object[] { USER_NAME, PASSWORD } );
//		try {
//			redstone.xmlrpc.XmlRpcStruct serverInfo = (redstone.xmlrpc.XmlRpcStruct)client.invoke( "jira1.getServerInfo", new Object[] { token } );
//			//System.out.println( "serverInfo: " + serverInfo );
//			//java.util.List< redstone.xmlrpc.XmlRpcStruct > projects = (java.util.List< redstone.xmlrpc.XmlRpcStruct >)client.invoke( "jira1.getProjectsNoSchemes", new Object[] { token } );
//			//System.out.println( "projects: " );
//			//for( redstone.xmlrpc.XmlRpcStruct project : projects ) {
//			//	System.out.println( "\t" + project );
//			//}
//			redstone.xmlrpc.XmlRpcStruct remote = edu.cmu.cs.dennisc.jira.JIRAUtilities.createIssue( issue, "AIIIP" );
//			client.invoke( "jira1.createIssue", new Object[] { token, remote } );
//		} finally {
//			client.invoke( "jira1.logout", new Object[] { token } );
//			//System.out.println( "done." );
//		}
////		edu.cmu.cs.dennisc.lang.ThreadUtilities.sleep( 1000 );
////		throw new Exception();
	}

	protected void sendMail( boolean isTransportLayerSecurityDesired, Integer portOverride ) throws Exception {
//		StringBuffer sb = new StringBuffer();
//		if( isTransportLayerSecurityDesired ) {
//			sb.append( "SECURE: " );
//		} else {
//			sb.append( "unsecure: " );
//		}
//		if( portOverride != null ) {
//			sb.append( "port override: " );
//			sb.append( portOverride );
//		}
//		String subject = sb.toString();
//
//		edu.cmu.cs.dennisc.mail.MailUtilities.sendMail( isTransportLayerSecurityDesired, portOverride, this.mailServer, this.mailAuthenticator, this.getReplyTo(), this.getReplyToPersonal(), this.recipient, subject, body, 
//				new edu.cmu.cs.dennisc.mail.Attachment[] {} );
////		edu.cmu.cs.dennisc.lang.ThreadUtilities.sleep( 1000 );
////		throw new Exception();
	}
	@Override
	protected Boolean doInBackground() throws Exception {
		this.process( "attempting to submit bug report...\n" );
		try {
			this.process( "* uploading directly to database... " );
			this.uploadToJIRA();
			this.process( "SUCCEEDED.\n" );
		} catch( Exception e0 ) {
			this.process( "FAILED.\n" );
			this.process( "* sending mail (on smtp port)... " );
			try {
				this.sendMail( false, null );
				this.process( "SUCCEEDED.\n" );
			} catch( Exception e1 ) {
				this.process( "FAILED.\n" );
				this.process( "* sending secure mail (on secure smtp port)... " );
				try {
					this.sendMail( true, null );
					this.process( "SUCCEEDED.\n" );
				} catch( Exception e2 ) {
					this.process( "FAILED.\n" );
					this.process( "* sending secure mail (on http port)... " );
					try {
						this.sendMail( true, 80 );
						this.process( "SUCCEEDED.\n" );
					} catch( Exception e3 ) {
						this.process( "FAILED.\n" );
						return false;
					}
				}
			}
		}
		return true;
	}
	@Override
	protected void done() {
		try {
			this.progressPane.handleDone( this.get() );
		} catch( Exception e ) {
			throw new RuntimeException( e );
		}
	}
}
