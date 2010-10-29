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

import edu.cmu.cs.dennisc.jira.JIRAReport;
import edu.cmu.cs.dennisc.mail.MailReport;
import edu.cmu.cs.dennisc.toolkit.issue.ProgressPane;

/**
 * @author Dennis Cosgrove
 */
public class IssueReportWorker extends org.jdesktop.swingworker.SwingWorker< Boolean, String > {
	private ProgressPane progressPane;
	private ReportGenerator issueReportGenerator;
	private ReportSubmissionConfiguration reportSubmissionConfiguration;
	public IssueReportWorker( ProgressPane progressPane, ReportGenerator issueReportGenerator, ReportSubmissionConfiguration reportSubmissionConfiguration ) {
		assert progressPane != null;
		this.progressPane = progressPane;
		this.issueReportGenerator = issueReportGenerator;
		this.reportSubmissionConfiguration = reportSubmissionConfiguration;
	}
	@Override
	protected void process( java.util.List< String > chunks ) {
		this.progressPane.handleProcess( chunks );
	}
	private void process( String... chunks ) {
		this.process( edu.cmu.cs.dennisc.util.CollectionUtilities.createArrayList( chunks ) );
	}

	protected void uploadToJIRAViaRPC() throws Exception {
		JIRAReport jiraReport = this.issueReportGenerator.generateIssueForRPC();
		final boolean STREAM_MESSAGES = true;
		redstone.xmlrpc.XmlRpcClient client = new redstone.xmlrpc.XmlRpcClient( this.reportSubmissionConfiguration.getJIRAViaRPCServer(), STREAM_MESSAGES );
		Object token = this.reportSubmissionConfiguration.getJIRAViaRPCAuthenticator().login( client );
		try {
			redstone.xmlrpc.XmlRpcStruct remote = edu.cmu.cs.dennisc.jira.rpc.RPCUtilities.createIssue( jiraReport, client, token );
		} finally {
			client.invoke( "jira1.logout", new Object[] { token } );
		}
	}
	protected void uploadToJIRAViaSOAP() throws Exception {
		JIRAReport jiraReport = this.issueReportGenerator.generateIssueForSOAP();
		com.atlassian.jira.rpc.soap.client.JiraSoapServiceServiceLocator jiraSoapServiceLocator = new com.atlassian.jira.rpc.soap.client.JiraSoapServiceServiceLocator();
		com.atlassian.jira.rpc.soap.client.JiraSoapService service = jiraSoapServiceLocator.getJirasoapserviceV2( this.reportSubmissionConfiguration.getJIRAViaSOAPServer() );
		
		String token = this.reportSubmissionConfiguration.getJIRAViaSOAPAuthenticator().login( service );
	    com.atlassian.jira.rpc.soap.client.RemoteIssue result = edu.cmu.cs.dennisc.jira.soap.SOAPUtilities.createIssue( jiraReport, service, token );
		service.logout( token );
	}

	protected void sendMail( boolean isTransportLayerSecurityDesired, Integer portOverride ) throws Exception {
		MailReport mailReport = this.issueReportGenerator.generateIssueForSMTP();
		edu.cmu.cs.dennisc.mail.MailUtilities.sendMail( isTransportLayerSecurityDesired, portOverride, this.reportSubmissionConfiguration.getMailServer(), this.reportSubmissionConfiguration.getMailAuthenticator(), mailReport.getReplyTo(), mailReport.getReplyToPersonal(), this.reportSubmissionConfiguration.getMailRecipient(), mailReport.getSubject(), mailReport.getBody(), mailReport.getAttachments() );
	}
	@Override
	protected Boolean doInBackground() throws Exception {
		this.process( "attempting to submit bug report...\n" );
		try {
			this.process( "* uploading directly to database... " );
			this.uploadToJIRAViaSOAP();
			this.process( "SUCCEEDED.\n" );
		} catch( Exception eA ) {
			eA.printStackTrace();
			try {
				this.process( "FAILED.\n" );
				this.process( "* uploading directly to database via RPC... " );
				this.uploadToJIRAViaRPC();
				this.process( "SUCCEEDED.\n" );
			} catch( Exception eB ) {
				eB.printStackTrace();
				this.process( "FAILED.\n" );
				this.process( "* sending mail (on smtp port)... " );
				try {
					this.sendMail( false, null );
					this.process( "SUCCEEDED.\n" );
				} catch( Exception eC ) {
					eC.printStackTrace();
					this.process( "FAILED.\n" );
					this.process( "* sending secure mail (on secure smtp port)... " );
					try {
						this.sendMail( true, null );
						this.process( "SUCCEEDED.\n" );
					} catch( Exception e2 ) {
						e2.printStackTrace();
						this.process( "FAILED.\n" );
						this.process( "* sending secure mail (on http port)... " );
						try {
							this.sendMail( true, 80 );
							this.process( "SUCCEEDED.\n" );
						} catch( Exception e3 ) {
							e3.printStackTrace();
							this.process( "FAILED.\n" );
							return false;
						}
					}
				}
			}
		}
		return true;
	}
	@Override
	protected void done() {
		try {
			Boolean isSuccessful = this.get();
			if( isSuccessful != null ) {
				this.progressPane.handleDone( isSuccessful );
			} else {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "isSuccessful is null" );
			}
		} catch( java.util.concurrent.ExecutionException ee ) {
			ee.printStackTrace();
		} catch( InterruptedException ie ) {
			ie.printStackTrace();
		}
	}
}
