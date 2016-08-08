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

/**
 * @author Dennis Cosgrove
 */
public final class IssueReportWorker extends javax.swing.SwingWorker<Boolean, String> {
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
	protected void process( java.util.List<String> chunks ) {
		this.workerListener.process( chunks );
	}

	private void process( String... chunks ) {
		this.process( edu.cmu.cs.dennisc.java.util.Lists.newArrayList( chunks ) );
	}

	protected boolean uploadToJIRAViaSOAP() throws Exception {
		edu.cmu.cs.dennisc.jira.JIRAReport jiraReport = this.issueReportGenerator.generateIssueForSOAP();
		if( jiraReport != null ) {
			com.atlassian.jira.rpc.soap.client.JiraSoapServiceServiceLocator jiraSoapServiceLocator = new com.atlassian.jira.rpc.soap.client.JiraSoapServiceServiceLocator();
			com.atlassian.jira.rpc.soap.client.JiraSoapService service = jiraSoapServiceLocator.getJirasoapserviceV2( this.reportSubmissionConfiguration.getJIRAViaSOAPServer() );
			String token = this.reportSubmissionConfiguration.getJIRAViaSOAPAuthenticator().login( service );
			com.atlassian.jira.rpc.soap.client.RemoteIssue result = edu.cmu.cs.dennisc.jira.soap.SOAPUtilities.createIssue( jiraReport, service, token );

			java.util.List<Attachment> attachments = jiraReport.getAttachments();
			boolean rv = true;
			if( ( attachments != null ) && ( attachments.size() > 0 ) ) {
				this.process( "\n" );
				for( Attachment attachment : attachments ) {
					this.process( "\t" + attachment.getFileName() + "... " );
					try {
						edu.cmu.cs.dennisc.jira.soap.SOAPUtilities.addAttachment( result, attachment, service, token );
						rv = true;
					} catch( java.rmi.RemoteException re ) {
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

	protected void uploadToJIRAViaRPC() throws Exception {
		edu.cmu.cs.dennisc.jira.JIRAReport jiraReport = this.issueReportGenerator.generateIssueForRPC();
		if( jiraReport != null ) {
			final boolean STREAM_MESSAGES = true;
			redstone.xmlrpc.XmlRpcClient client = new redstone.xmlrpc.XmlRpcClient( this.reportSubmissionConfiguration.getJIRAViaRPCServer(), STREAM_MESSAGES );
			Object token = this.reportSubmissionConfiguration.getJIRAViaRPCAuthenticator().login( client );
			try {
				redstone.xmlrpc.XmlRpcStruct remote = edu.cmu.cs.dennisc.jira.rpc.RPCUtilities.createIssue( jiraReport, client, token );
				this.key = edu.cmu.cs.dennisc.jira.rpc.RPCUtilities.getKey( remote );
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
		try {
			this.process( "* uploading directly to database... " );
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
			} catch( Exception eB ) {
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
		return true;
	}

	@Override
	protected void done() {
		try {
			Boolean isSuccessful = this.get();
			if( isSuccessful != null ) {
				java.net.URL urlResult;
				if( this.key != null ) {
					try {
						java.net.URL urlSOAP = this.reportSubmissionConfiguration.getJIRAViaSOAPServer();
						urlResult = new java.net.URL( urlSOAP.getProtocol(), urlSOAP.getHost(), urlSOAP.getPort(), "/browse/" + this.key );
					} catch( java.net.MalformedURLException murle ) {
						urlResult = null;
					}
				} else {
					urlResult = null;
				}
				this.workerListener.done( isSuccessful, urlResult );
			} else {
				System.out.println( "IssueReportWorker: isSuccessful is null." );
			}
		} catch( java.util.concurrent.ExecutionException ee ) {
			ee.printStackTrace();
		} catch( InterruptedException ie ) {
			ie.printStackTrace();
		}
	}
}
