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
package org.alice.ide.croquet.models.help;

import com.atlassian.jira.rpc.soap.client.JiraSoapService;
import edu.cmu.cs.dennisc.issue.Issue;
import edu.cmu.cs.dennisc.issue.IssueType;
import edu.cmu.cs.dennisc.issue.ReportGenerator;
import edu.cmu.cs.dennisc.issue.StackTraceAttachment;
import edu.cmu.cs.dennisc.issue.SystemPropertiesAttachment;
import edu.cmu.cs.dennisc.jira.JIRAReport;
import edu.cmu.cs.dennisc.jira.rpc.RPCUtilities;
import edu.cmu.cs.dennisc.jira.soap.Authenticator;
import edu.cmu.cs.dennisc.login.AccountInformation;
import edu.cmu.cs.dennisc.login.AccountManager;
import org.alice.ide.croquet.models.help.views.AbstractIssueView;
import org.alice.ide.issue.CurrentProjectAttachment;
import org.alice.ide.issue.ReportSubmissionConfiguration;
import org.alice.ide.issue.SubmitReportUtilities;
import org.alice.ide.issue.swing.views.IssueReportPane;
import org.alice.ide.issue.swing.views.LogInStatusPane;
import org.alice.ide.issue.swing.views.ProgressPane;
import org.lgna.croquet.CancelException;
import org.lgna.croquet.Group;
import org.lgna.croquet.LaunchOperationUnadornedDialogCoreComposite;
import org.lgna.croquet.Operation;
import org.lgna.croquet.StringState;
import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.history.UserActivity;
import org.lgna.croquet.views.AbstractWindow;
import org.lgna.project.ProjectVersion;
import redstone.xmlrpc.XmlRpcClient;
import redstone.xmlrpc.XmlRpcException;
import redstone.xmlrpc.XmlRpcFault;

import javax.swing.JOptionPane;
import java.rmi.RemoteException;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractIssueComposite<V extends AbstractIssueView> extends LaunchOperationUnadornedDialogCoreComposite<V> implements ReportGenerator {
	public static final Group ISSUE_GROUP = Group.getInstance( UUID.fromString( "af49d17b-9299-4a0d-b931-0a18a8abf0dd" ), "ISSUE_GROUP" );

	public AbstractIssueComposite( UUID migrationId, IsModal isModal ) {
		super( migrationId, isModal, ISSUE_GROUP );
		this.environmentState.setEnabled( false );
	}

	private String getStepsText() {
		return this.stepsState.getValue();
	}

	public StringState getStepsState() {
		return this.stepsState;
	}

	public StringState getEnvironmentState() {
		return this.environmentState;
	}

	public Operation getSubmitBugOperation() {
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

	protected abstract IssueType getReportType();

	protected abstract String getSummaryText();

	protected abstract String getDescriptionText();

	protected abstract Thread getThread();

	protected abstract Throwable getThrowable();

	protected abstract boolean isProjectAttachmentDesired();

	@Override
	protected GoldenRatioPolicy getGoldenRatioPolicy() {
		return GoldenRatioPolicy.WIDTH_LONG_SIDE;
	}

	private Issue.Builder createIssueBuilder() {
		return new Issue.Builder()
				.type( this.getReportType() )
				.summary( this.getSummaryText() )
				.description( this.getDescriptionText() )
				.environment( IssueReportPane.getEnvironmentShortDescription() )
				.steps( this.getStepsText() )
				.threadAndThrowable( this.getThread(), this.getThrowable() )
				.version( ProjectVersion.getCurrentVersionText() );
	}

	private JIRAReport createJiraReport() {
		Issue.Builder builder = this.createIssueBuilder();
		JIRAReport rv = new JIRAReport( builder.build(), this.getProjectKey() );
		return rv;
	}

	@Override
	public JIRAReport generateIssueForRPC() {
		JIRAReport rv = this.createJiraReport();
		return rv;
	}

	protected void addAttachments( JIRAReport report ) {
		report.addAttachment( new SystemPropertiesAttachment() );
		Throwable throwable = this.getThrowable();
		if( throwable != null ) {
			report.addAttachment( new StackTraceAttachment( throwable ) );
		}
		if( this.isProjectAttachmentDesired() ) {
			report.addAttachment( new CurrentProjectAttachment() );
		}
	}

	@Override
	public JIRAReport generateIssueForSOAP() {
		JIRAReport rv = this.createJiraReport();
		this.addAttachments( rv );
		return rv;
	}

	private ReportSubmissionConfiguration createReportSubmissionConfiguration() {
		return new ReportSubmissionConfiguration() {
			@Override
			public edu.cmu.cs.dennisc.jira.rpc.Authenticator getJIRAViaRPCAuthenticator() {
				final AccountInformation accountInformation = AccountManager.get( LogInStatusPane.BUGS_ALICE_ORG_KEY );
				if( accountInformation != null ) {
					return new edu.cmu.cs.dennisc.jira.rpc.Authenticator() {
						@Override
						public Object login( XmlRpcClient client ) throws XmlRpcException, XmlRpcFault {
							return RPCUtilities.logIn( client, accountInformation.getID(), accountInformation.getPassword() );
						}
					};
				} else {
					return super.getJIRAViaRPCAuthenticator();
				}
			}

			@Override
			public Authenticator getJIRAViaSOAPAuthenticator() {
				final AccountInformation accountInformation = AccountManager.get( LogInStatusPane.BUGS_ALICE_ORG_KEY );
				if( accountInformation != null ) {
					return new Authenticator() {
						@Override
						public String login( JiraSoapService service ) throws RemoteException {
							return service.login( accountInformation.getID(), accountInformation.getPassword() );
						}
					};
				} else {
					return super.getJIRAViaSOAPAuthenticator();
				}
			}
		};
	}

	private final StringState stepsState = createStringState( "stepsState" );
	private final StringState environmentState = createStringState( "environmentState", IssueReportPane.getEnvironmentLongDescription() );
	private final Operation submitBugOperation = createActionOperation( "submitBugOperation", new Action() {

		@Override
		public Edit perform( UserActivity userActivity, InternalActionOperation source ) throws CancelException {
			submitBugOperation.setEnabled( false );
			try {
				if( isClearedToSubmitBug() ) {
					ProgressPane progressPane = SubmitReportUtilities.submitReport( AbstractIssueComposite.this, createReportSubmissionConfiguration() );
					AbstractWindow<?> root = AbstractIssueComposite.this.getView().getRoot();
					if( root != null ) {
						if( progressPane.isDone() ) {
							if( progressPane.isSuccessful() ) {
								JOptionPane.showMessageDialog( root.getAwtComponent(), "Your bug report has been successfully submitted.  Thank you." );
								root.setVisible( false );
							} else {
								JOptionPane.showMessageDialog( root.getAwtComponent(), "Your bug report FAILED to submit.  Thank you for trying." );
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
}
