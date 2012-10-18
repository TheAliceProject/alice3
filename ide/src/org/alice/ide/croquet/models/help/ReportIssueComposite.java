/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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

import org.alice.ide.croquet.models.help.views.ReportIssueView;
import org.alice.ide.issue.CurrentProjectAttachment;
import org.alice.ide.issue.ReportSubmissionConfiguration;
import org.lgna.croquet.ActionOperation;
import org.lgna.croquet.CancelException;
import org.lgna.croquet.ListSelectionState;
import org.lgna.croquet.State;
import org.lgna.croquet.State.ValueListener;
import org.lgna.croquet.StringState;
import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.history.CompletionStep;

import edu.cmu.cs.dennisc.jira.JIRAReport;

/**
 * @author Matt May
 */
public abstract class ReportIssueComposite extends org.lgna.croquet.FrameComposite<ReportIssueView> {
	private final edu.cmu.cs.dennisc.jira.JIRAReport.Type type;
	private final String environment = org.alice.ide.issue.swing.views.IssueReportPane.getEnvironmentLongDescription();

	public ReportIssueComposite( java.util.UUID migrationId, edu.cmu.cs.dennisc.jira.JIRAReport.Type type ) {
		super( migrationId, ISSUE_GROUP );
		this.type = type;
		initReportSubmissionConfiguration();
		initAdapter();
		environmentState.setEnabled( false );
	}

	private void initAdapter() {
		summaryState.addValueListener( adapter );
		descriptionState.addValueListener( adapter );
		stepsState.addValueListener( adapter );
		adapter.changed( null, "", "", true );
	}

	private final ValueListener<String> adapter = new ValueListener<String>() {

		public void changing( State<String> state, String prevValue, String nextValue, boolean isAdjusting ) {
		}

		public void changed( State<String> state, String prevValue, String nextValue, boolean isAdjusting ) {
			if( !( summaryState.getValue().length() > 0 ) ) {
				submitBugOperation.setEnabled( false );
				return;
			}
			if( !( descriptionState.getValue().length() > 0 ) ) {
				submitBugOperation.setEnabled( false );
				return;
			}
			if( !( stepsState.getValue().length() > 0 ) ) {
				submitBugOperation.setEnabled( false );
				return;
			}
			submitBugOperation.setEnabled( true );
		}

	};

	public static final org.lgna.croquet.Group ISSUE_GROUP = org.lgna.croquet.Group.getInstance( java.util.UUID.fromString( "af49d17b-9299-4a0d-b931-0a18a8abf0dd" ), "ISSUE_GROUP" );
	private final ListSelectionState<BugSubmitVisibility> visibilityState = createListSelectionStateForEnum( this.createKey( "visibilityState" ), BugSubmitVisibility.class, BugSubmitVisibility.PRIVATE );
	private final ListSelectionState<JIRAReport.Type> typeState = createListSelectionStateForEnum( createKey( "typeState" ), JIRAReport.Type.class, JIRAReport.Type.BUG );
	private final StringState summaryState = createStringState( this.createKey( "summaryState" ) );
	private final StringState descriptionState = createStringState( this.createKey( "descriptionState" ) );
	private final StringState stepsState = createStringState( this.createKey( "stepsState" ) );
	private final StringState environmentState = createStringState( this.createKey( "environmentState" ), environment );
	private final ListSelectionState<BugSubmitAttachment> attachmentState = createListSelectionStateForEnum( this.createKey( "attachmentState" ), BugSubmitAttachment.class, BugSubmitAttachment.YES );
	private final org.lgna.croquet.Operation operation = new org.alice.ide.browser.ImmutableBrowserOperation( java.util.UUID.fromString( "55806b33-8b8a-43e0-ad5a-823d733be2f8" ), "http://bugs.alice.org:8080/" );

	private ActionOperation submitBugOperation = createActionOperation( this.createKey( "submitBugOperation" ), new Action() {

		public Edit perform( CompletionStep<?> step, org.lgna.croquet.AbstractComposite.InternalActionOperation source ) throws CancelException {
			JIRAReport report = createJIRAReport();
			if( attachmentState.getSelectedItem().equals( BugSubmitAttachment.YES ) ) {
				report.addAttachment( new CurrentProjectAttachment() );
			}
			boolean success = false;
			try {
				success = uploadToJIRAViaSOAP( report );
			} catch( Exception e ) {
				e.printStackTrace();
			}
			System.out.println( "great Success!: " + success );
			if( !success ) {
				try {
					uploadToJIRAViaRPC( report );
				} catch( Exception e ) {
					e.printStackTrace();
				}
			}
			return null;
		}
	} );

	private ReportSubmissionConfiguration reportSubmissionConfiguration;

	private LogInOutCardOwnerComposite logInOutCardComposite = new LogInOutCardOwnerComposite();

	public ListSelectionState<BugSubmitVisibility> getVisibilityState() {
		return this.visibilityState;
	}

	public ListSelectionState<edu.cmu.cs.dennisc.jira.JIRAReport.Type> getTypeState() {
		return this.typeState;
	}

	public StringState getSummaryState() {
		return this.summaryState;
	}

	public StringState getDescriptionState() {
		return this.descriptionState;
	}

	public StringState getStepsState() {
		return this.stepsState;
	}

	public StringState getEnvironmentState() {
		return this.environmentState;
	}

	public ListSelectionState<BugSubmitAttachment> getAttachmentState() {
		return this.attachmentState;
	}

	public LogInOutCardOwnerComposite getLogInOutCardComposite() {
		return this.logInOutCardComposite;
	}

	public ActionOperation getSubmitBugOperation() {
		return this.submitBugOperation;
	}

	@Override
	protected ReportIssueView createView() {
		return new ReportIssueView( this );
	}

	public org.lgna.croquet.Operation getBrowserOperation() {
		return this.operation;
	}

	protected String getJIRAProjectKey() {
		if( this.getVisibilityState().getValue().equals( BugSubmitVisibility.PUBLIC ) ) {
			return "AIII";
		} else {
			return "AIIIP";
		}
	}

	public JIRAReport createJIRAReport() {
		JIRAReport rv = new JIRAReport();
		rv.setProjectKey( getJIRAProjectKey() );
		rv.setType( typeState.getSelectedItem() );
		rv.setSummary( summaryState.getValue() );
		rv.setDescription( descriptionState.getValue() );
		rv.setEnvironment( org.alice.ide.issue.swing.views.IssueReportPane.getEnvironmentShortDescription() );
		rv.setSteps( stepsState.getValue() );
		rv.setException( "" );
		rv.setAffectsVersions( new String[] { org.lgna.project.Version.getCurrentVersionText() } );
		return rv;
	}

	public boolean uploadToJIRAViaSOAP( JIRAReport jiraReport ) throws Exception {
		if( jiraReport != null ) {
			com.atlassian.jira.rpc.soap.client.JiraSoapServiceServiceLocator jiraSoapServiceLocator = new com.atlassian.jira.rpc.soap.client.JiraSoapServiceServiceLocator();
			com.atlassian.jira.rpc.soap.client.JiraSoapService service = jiraSoapServiceLocator.getJirasoapserviceV2( reportSubmissionConfiguration.getJIRAViaSOAPServer() );
			String token = reportSubmissionConfiguration.getJIRAViaSOAPAuthenticator().login( service );
			com.atlassian.jira.rpc.soap.client.RemoteIssue result = edu.cmu.cs.dennisc.jira.soap.SOAPUtilities.createIssue( jiraReport, service, token );

			boolean rv;
			//todo?
			try {
				boolean isBase64EncodingDesired = false;
				edu.cmu.cs.dennisc.jira.soap.SOAPUtilities.addAttachments( result, jiraReport, service, token, isBase64EncodingDesired );
				rv = true;
			} catch( java.rmi.RemoteException re ) {
				re.printStackTrace();
				rv = false;
			}

			//			String key = result.getKey();
			service.logout( token );

			return rv;
		} else {
			throw new Exception( "pass" );
		}
	}

	public void uploadToJIRAViaRPC( JIRAReport jiraReport ) throws Exception {
		if( jiraReport != null ) {
			final boolean STREAM_MESSAGES = true;
			redstone.xmlrpc.XmlRpcClient client = new redstone.xmlrpc.XmlRpcClient( reportSubmissionConfiguration.getJIRAViaRPCServer(), STREAM_MESSAGES );
			Object token = reportSubmissionConfiguration.getJIRAViaRPCAuthenticator().login( client );
			try {
				redstone.xmlrpc.XmlRpcStruct remote = edu.cmu.cs.dennisc.jira.rpc.RPCUtilities.createIssue( jiraReport, client, token );
				//				String key = edu.cmu.cs.dennisc.jira.rpc.RPCUtilities.getKey( remote );
			} finally {
				client.invoke( "jira1.logout", new Object[] { token } );
			}
		} else {
			throw new Exception( "pass" );
		}
	}

	@Override
	public void handlePreActivation() {
		this.descriptionState.setValueTransactionlessly( "" );
		this.attachmentState.setValueTransactionlessly( BugSubmitAttachment.YES );
		this.visibilityState.setValueTransactionlessly( BugSubmitVisibility.PUBLIC );
		this.typeState.setValue( this.type );
		this.logInOutCardComposite.handlePreActivation();
		super.handlePreActivation();
	}

	@Override
	public void handlePostDeactivation() {
		this.logInOutCardComposite.handlePostDeactivation();
		super.handlePostDeactivation();
	}

	private void initReportSubmissionConfiguration() {
		this.reportSubmissionConfiguration = new ReportSubmissionConfiguration() {
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
