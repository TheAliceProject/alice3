/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
 */
package org.alice.ide.croquet.models.help;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractIssueComposite<V extends org.alice.ide.croquet.models.help.views.AbstractIssueView> extends org.lgna.croquet.FrameComposite<V> implements edu.cmu.cs.dennisc.issue.ReportGenerator {
	public static final org.lgna.croquet.Group ISSUE_GROUP = org.lgna.croquet.Group.getInstance( java.util.UUID.fromString( "af49d17b-9299-4a0d-b931-0a18a8abf0dd" ), "ISSUE_GROUP" );

	private final org.lgna.croquet.Operation submitBugOperation = createActionOperation( this.createKey( "submitBugOperation" ), new Action() {

		public org.lgna.croquet.edits.Edit<?> perform( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.croquet.AbstractComposite.InternalActionOperation source ) throws org.lgna.croquet.CancelException {
			submitBugOperation.setEnabled( false );
			try {
				if( isClearedToSubmitBug() ) {
					org.alice.ide.issue.swing.views.ProgressPane progressPane = org.alice.ide.issue.SubmitReportUtilities.submitReport( AbstractIssueComposite.this, createReportSubmissionConfiguration() );
					org.lgna.croquet.components.AbstractWindow<?> root = AbstractIssueComposite.this.getView().getRoot();
					if( root != null ) {
						if( progressPane.isDone() ) {
							if( progressPane.isSuccessful() ) {
								javax.swing.JOptionPane.showMessageDialog( root.getAwtComponent(), "Your bug report has been successfully submitted.  Thank you." );
								root.setVisible( false );
							} else {
								javax.swing.JOptionPane.showMessageDialog( root.getAwtComponent(), "Your bug report FAILED to submit.  Thank you for trying." );
							}
						} else {
							root.setVisible( false );
						}
					}
				}
			} finally {
				submitBugOperation.setEnabled( true );
			}
			return null;
		}
	} );

	public AbstractIssueComposite( java.util.UUID migrationId ) {
		super( migrationId, ISSUE_GROUP );
	}

	public org.lgna.croquet.Operation getSubmitBugOperation() {
		return this.submitBugOperation;
	}

	protected abstract boolean isClearedToSubmitBug();

	protected abstract boolean isPublic();

	private String getProjectKey() {
		if( this.isPublic() ) {
			return "AIII";
		} else {
			return "AIIIP";
		}
	}

	protected abstract edu.cmu.cs.dennisc.jira.JIRAReport.Type getReportType();

	protected abstract String getSummaryText();

	protected abstract String getDescriptionText();

	protected abstract String getStepsText();

	protected abstract Throwable getThrowable();

	private String getExceptionText() {
		Throwable throwable = this.getThrowable();
		if( throwable != null ) {
			return throwable.toString();
		} else {
			return "";
		}
	}

	protected abstract boolean isProjectAttachmentDesired();

	private edu.cmu.cs.dennisc.jira.JIRAReport generateIssue() {
		edu.cmu.cs.dennisc.jira.JIRAReport rv = new edu.cmu.cs.dennisc.jira.JIRAReport();
		rv.setProjectKey( this.getProjectKey() );
		rv.setType( this.getReportType() );
		rv.setSummary( this.getSummaryText() );
		rv.setDescription( this.getDescriptionText() );
		rv.setEnvironment( org.alice.ide.issue.swing.views.IssueReportPane.getEnvironmentShortDescription() );
		rv.setSteps( this.getStepsText() );
		rv.setException( this.getExceptionText() );
		rv.setAffectsVersions( new String[] { org.lgna.project.Version.getCurrentVersionText() } );
		return rv;
	}

	public edu.cmu.cs.dennisc.jira.JIRAReport generateIssueForRPC() {
		edu.cmu.cs.dennisc.jira.JIRAReport rv = this.generateIssue();
		return rv;
	}

	public edu.cmu.cs.dennisc.jira.JIRAReport generateIssueForSOAP() {
		edu.cmu.cs.dennisc.jira.JIRAReport rv = this.generateIssue();
		rv.addAttachment( new edu.cmu.cs.dennisc.issue.SystemPropertiesAttachment() );
		if( this.isProjectAttachmentDesired() ) {
			rv.addAttachment( new org.alice.ide.issue.CurrentProjectAttachment() );
		}
		return rv;
	}

	private org.alice.ide.issue.ReportSubmissionConfiguration createReportSubmissionConfiguration() {
		return new org.alice.ide.issue.ReportSubmissionConfiguration() {
			@Override
			public edu.cmu.cs.dennisc.jira.rpc.Authenticator getJIRAViaRPCAuthenticator() {
				final edu.cmu.cs.dennisc.login.AccountInformation accountInformation = edu.cmu.cs.dennisc.login.AccountManager.get( org.alice.ide.issue.swing.views.LogInStatusPane.BUGS_ALICE_ORG_KEY );
				if( accountInformation != null ) {
					return new edu.cmu.cs.dennisc.jira.rpc.Authenticator() {
						public Object login( redstone.xmlrpc.XmlRpcClient client ) throws redstone.xmlrpc.XmlRpcException, redstone.xmlrpc.XmlRpcFault {
							return edu.cmu.cs.dennisc.jira.rpc.RPCUtilities.logIn( client, accountInformation.getID(), accountInformation.getPassword() );
						}
					};
				} else {
					return super.getJIRAViaRPCAuthenticator();
				}
			}

			@Override
			public edu.cmu.cs.dennisc.jira.soap.Authenticator getJIRAViaSOAPAuthenticator() {
				final edu.cmu.cs.dennisc.login.AccountInformation accountInformation = edu.cmu.cs.dennisc.login.AccountManager.get( org.alice.ide.issue.swing.views.LogInStatusPane.BUGS_ALICE_ORG_KEY );
				if( accountInformation != null ) {
					return new edu.cmu.cs.dennisc.jira.soap.Authenticator() {
						public String login( com.atlassian.jira.rpc.soap.client.JiraSoapService service ) throws java.rmi.RemoteException {
							return service.login( accountInformation.getID(), accountInformation.getPassword() );
						}
					};
				} else {
					return super.getJIRAViaSOAPAuthenticator();
				}
			}
		};
	}

}
