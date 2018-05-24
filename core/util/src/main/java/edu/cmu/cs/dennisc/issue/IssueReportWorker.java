/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may
 *    "Alice" appear in their name, without prior written permission of
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is
 *    contributed by Electronic Arts Inc. and may be used for personal,
 *    non-commercial, and academic use only. Redistributions of any program
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in
 *    The Alice 3.0 Art Gallery License.
 *
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *******************************************************************************/

package edu.cmu.cs.dennisc.issue;

import com.atlassian.jira.rest.client.api.domain.BasicIssue;
import com.atlassian.jira.rpc.soap.client.JiraSoapService;
import com.atlassian.jira.rpc.soap.client.JiraSoapServiceServiceLocator;
import com.atlassian.jira.rpc.soap.client.RemoteIssue;
import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.jira.JIRAReport;
import edu.cmu.cs.dennisc.jira.rest.RestUtilities;
import edu.cmu.cs.dennisc.jira.rpc.RPCUtilities;
import edu.cmu.cs.dennisc.jira.soap.SOAPUtilities;
import redstone.xmlrpc.XmlRpcClient;
import redstone.xmlrpc.XmlRpcStruct;

import javax.swing.SwingWorker;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author Dennis Cosgrove
 */
public final class IssueReportWorker extends SwingWorker<Boolean, String> {
	private final WorkerListener workerListener;
	private final ReportGenerator issueReportGenerator;
	private final ReportSubmissionConfiguration reportSubmissionConfiguration;
	private String key = null;

	public IssueReportWorker( WorkerListener workerListener, ReportGenerator issueReportGenerator, ReportSubmissionConfiguration reportSubmissionConfiguration ) {
		assert workerListener != null;
		this.workerListener = workerListener;
		this.issueReportGenerator = issueReportGenerator;
		this.reportSubmissionConfiguration = reportSubmissionConfiguration;
	}

	@Override
	protected void process( List<String> chunks ) {
		this.workerListener.process( chunks );
	}

	private void process( String... chunks ) {
		this.process( Lists.newArrayList( chunks ) );
	}

	private void uploadToJiraViaRest() throws Exception {
		JIRAReport jiraReport = issueReportGenerator.generateIssueForSOAP();
		if( jiraReport != null ) {
			BasicIssue result = RestUtilities.createIssue(reportSubmissionConfiguration.getJIRAViaRestServer(), jiraReport);
			this.key = result.getKey();

			List<Attachment> attachments = jiraReport.getAttachments();
			if( ( attachments != null ) && ( attachments.size() > 0 ) ) {
				this.process( "\n" );
				for( Attachment attachment : attachments ) {
					this.process( "\t" + attachment.getFileName() + "... " );
					RestUtilities.addAttachment(reportSubmissionConfiguration.getJIRAViaRestServer(), result, attachment);
					this.process( "done.\n" );
				}
			}
		} else {
			throw new Exception( "pass" );
		}
	}

	private boolean uploadToJIRAViaSOAP() throws Exception {
		JIRAReport jiraReport = this.issueReportGenerator.generateIssueForSOAP();
		if( jiraReport != null ) {
			JiraSoapServiceServiceLocator jiraSoapServiceLocator = new JiraSoapServiceServiceLocator();
			JiraSoapService service = jiraSoapServiceLocator.getJirasoapserviceV2( this.reportSubmissionConfiguration.getJIRAViaSOAPServer() );
			String token = this.reportSubmissionConfiguration.getJIRAViaSOAPAuthenticator().login( service );
			RemoteIssue result = SOAPUtilities.createIssue( jiraReport, service, token );

			List<Attachment> attachments = jiraReport.getAttachments();
			boolean rv = true;
			if( ( attachments != null ) && ( attachments.size() > 0 ) ) {
				this.process( "\n" );
				for( Attachment attachment : attachments ) {
					this.process( "\t" + attachment.getFileName() + "... " );
					try {
						SOAPUtilities.addAttachment( result, attachment, service, token );
						rv = true;
					} catch( RemoteException re ) {
						re.printStackTrace();
						rv = false;
					}
					this.process( "done.\n" );
				}
			}

			this.key = result.getKey();
			service.logout( token );

			return rv;
		} else {
			throw new Exception( "pass" );
		}
	}

	private void uploadToJIRAViaRPC() throws Exception {
		JIRAReport jiraReport = this.issueReportGenerator.generateIssueForRPC();
		if( jiraReport != null ) {
			final boolean STREAM_MESSAGES = true;
			XmlRpcClient client = new XmlRpcClient( this.reportSubmissionConfiguration.getJIRAViaRPCServer(), STREAM_MESSAGES );
			Object token = this.reportSubmissionConfiguration.getJIRAViaRPCAuthenticator().login( client );
			try {
				XmlRpcStruct remote = RPCUtilities.createIssue( jiraReport, client, token );
				this.key = RPCUtilities.getKey( remote );
			} finally {
				client.invoke( "jira1.logout", new Object[] { token } );
			}
		} else {
			throw new Exception( "pass" );
		}
	}

	//	protected void sendMail( boolean isTransportLayerSecurityDesired, Integer portOverride ) throws Exception {
	//		MailReport mailReport = this.issueReportGenerator.generateIssueForSMTP();
	//		if( mailReport != null ) {
	//			edu.cmu.cs.dennisc.mail.MailUtilities.sendMail( isTransportLayerSecurityDesired, portOverride, this.reportSubmissionConfiguration.getMailServer(), this.reportSubmissionConfiguration.getMailAuthenticator(), mailReport.getReplyTo(), mailReport.getReplyToPersonal(), this.reportSubmissionConfiguration.getMailRecipient(), mailReport.getSubject(), mailReport.getBody(), mailReport.getAttachments() );
	//			this.key = null;
	//		} else {
	//			throw new Exception( "pass" );
	//		}
	//	}

	@Override
	protected Boolean doInBackground() throws Exception {
		this.process( "attempting to submit bug report...\n" );

		this.process( "* uploading directly to database via REST... " );
		try {
			uploadToJiraViaRest();
			this.process( "SUCCEEDED.\n" );
		} catch ( Exception e) {
			e.printStackTrace();
			try {
				this.process( "FAILED.\n" );
				this.process( "* uploading directly to database via SOAP... " );
				boolean isSuccessAttachment = this.uploadToJIRAViaSOAP();
				if( isSuccessAttachment ) {
					this.process( "SUCCEEDED.\n" );
				} else {
					this.process( "PARTIALLY SUCCEEDED (attachments failed).\n" );
					//				this.process( "* sending mail (on smtp port)... " );
					//				try {
					//					this.sendMail( false, null );
					//					this.process( "SUCCEEDED.\n" );
					//				} catch( Exception eC ) {
					//					eC.printStackTrace();
					//					this.process( "FAILED.\n" );
					//				}
				}
			} catch( Exception eA ) {
				eA.printStackTrace();
				try {
					this.process( "FAILED.\n" );
					this.process( "* uploading directly to database via RPC... " );
					this.uploadToJIRAViaRPC();
					this.process( "SUCCEEDED.\n" );
				} catch (Exception eB) {
					eB.printStackTrace();
					this.process( "FAILED.\n" );
					//				this.process( "* sending mail (on smtp port)... " );
					//				try {
					//					this.sendMail( false, null );
					//					this.process( "SUCCEEDED.\n" );
					//				} catch( Exception eC ) {
					//					eC.printStackTrace();
					//					this.process( "FAILED.\n" );
					//					//					this.process( "* sending secure mail (on secure smtp port)... " );
					//					//					try {
					//					//						this.sendMail( true, null );
					//					//						this.process( "SUCCEEDED.\n" );
					//					//					} catch( Exception eD ) {
					//					//						eD.printStackTrace();
					//					//						this.process( "FAILED.\n" );
					//					//						this.process( "* sending secure mail (on http port)... " );
					//					//						try {
					//					//							this.sendMail( true, 80 );
					//					//							this.process( "SUCCEEDED.\n" );
					//					//						} catch( Exception eE ) {
					//					//							eE.printStackTrace();
					//					this.process( "FAILED.\n" );
					//					return false;
					//					//						}
					//					//					}
					//				}
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
				URL urlResult;
				if( this.key != null ) {
					try {
						URL urlSOAP = this.reportSubmissionConfiguration.getJIRAViaSOAPServer();
						urlResult = new URL( urlSOAP.getProtocol(), urlSOAP.getHost(), urlSOAP.getPort(), "/browse/" + this.key );
					} catch( MalformedURLException murle ) {
						urlResult = null;
					}
				} else {
					urlResult = null;
				}
				this.workerListener.done( isSuccessful, urlResult );
			} else {
				System.out.println( "IssueReportWorker: isSuccessful is null." );
			}
		} catch( ExecutionException ee ) {
			ee.printStackTrace();
		} catch( InterruptedException ie ) {
			ie.printStackTrace();
		}
	}
}
