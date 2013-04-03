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
import org.lgna.croquet.ListSelectionState;
import org.lgna.croquet.State;
import org.lgna.croquet.State.ValueListener;
import org.lgna.croquet.StringState;

import edu.cmu.cs.dennisc.jira.JIRAReport;

/**
 * @author Matt May
 */
public abstract class ReportIssueComposite extends AbstractIssueComposite<ReportIssueView> {
	private final ListSelectionState<BugSubmitVisibility> visibilityState = createListSelectionStateForEnum( this.createKey( "visibilityState" ), BugSubmitVisibility.class, BugSubmitVisibility.PRIVATE );

	private final edu.cmu.cs.dennisc.jira.JIRAReport.Type initialReportTypeValue;
	private final ListSelectionState<JIRAReport.Type> reportTypeState;
	private final StringState summaryState = createStringState( this.createKey( "summaryState" ) );
	private final StringState descriptionState = createStringState( this.createKey( "descriptionState" ) );
	private final ListSelectionState<BugSubmitAttachment> attachmentState = createListSelectionStateForEnum( this.createKey( "attachmentState" ), BugSubmitAttachment.class, null );
	private final org.lgna.croquet.Operation browserOperation = new org.alice.ide.browser.ImmutableBrowserOperation( java.util.UUID.fromString( "55806b33-8b8a-43e0-ad5a-823d733be2f8" ), "http://bugs.alice.org:8080/" );

	//	private final LogInCard logInCard = new LogInCard( BugLoginComposite.getInstance() );
	//	private final LogOutCard logOutCard = new LogOutCard();
	private final LogInOutComposite logInOutComposite = new LogInOutComposite( java.util.UUID.fromString( "079f108d-c3bb-4581-b107-f21b8d7286ca" ), new BugLoginComposite() );

	//	private final org.lgna.croquet.CardOwnerComposite logInOutComposite = this.createAndRegisterCardOwnerComposite( new BugLoginComposite() );

	private final ValueListener<String> adapter = new ValueListener<String>() {

		public void changing( State<String> state, String prevValue, String nextValue, boolean isAdjusting ) {
		}

		public void changed( State<String> state, String prevValue, String nextValue, boolean isAdjusting ) {
			getSubmitBugOperation().setEnabled( summaryState.getValue().length() > 0 );
		}

	};

	public ReportIssueComposite( java.util.UUID migrationId, edu.cmu.cs.dennisc.jira.JIRAReport.Type initialReportTypeValue ) {
		super( migrationId, false );
		this.initialReportTypeValue = initialReportTypeValue;
		this.reportTypeState = createListSelectionStateForEnum( createKey( "reportTypeState" ), JIRAReport.Type.class, this.initialReportTypeValue );
		this.registerSubComposite( logInOutComposite );
	}

	@Override
	protected Throwable getThrowable() {
		return null;
	}

	@Override
	protected boolean isPublic() {
		return this.getVisibilityState().getValue().equals( BugSubmitVisibility.PUBLIC );
	}

	public ListSelectionState<BugSubmitVisibility> getVisibilityState() {
		return this.visibilityState;
	}

	@Override
	protected edu.cmu.cs.dennisc.jira.JIRAReport.Type getReportType() {
		return this.reportTypeState.getValue();
	}

	public ListSelectionState<edu.cmu.cs.dennisc.jira.JIRAReport.Type> getReportTypeState() {
		return this.reportTypeState;
	}

	@Override
	protected String getSummaryText() {
		return this.summaryState.getValue();
	}

	public StringState getSummaryState() {
		return this.summaryState;
	}

	@Override
	protected String getDescriptionText() {
		return this.descriptionState.getValue();
	}

	public StringState getDescriptionState() {
		return this.descriptionState;
	}

	public ListSelectionState<BugSubmitAttachment> getAttachmentState() {
		return this.attachmentState;
	}

	public org.lgna.croquet.CardOwnerComposite getLogInOutCardComposite() {
		return this.logInOutComposite;
	}

	@Override
	protected ReportIssueView createView() {
		return new ReportIssueView( this );
	}

	public org.lgna.croquet.Operation getBrowserOperation() {
		return this.browserOperation;
	}

	@Override
	protected boolean isProjectAttachmentDesired() {
		return this.attachmentState.getValue().equals( BugSubmitAttachment.YES );
	}

	@Override
	protected boolean isClearedToSubmitBug() {
		boolean rv;
		if( this.attachmentState.getValue() != null ) {
			rv = true;
		} else {
			org.lgna.croquet.YesNoCancelOption option = org.lgna.croquet.Application.getActiveInstance().showYesNoCancelConfirmDialog( "Is your current project relevant to this issue report?", "Attach current project?", org.lgna.croquet.MessageType.QUESTION );
			if( option == org.lgna.croquet.YesNoCancelOption.YES ) {
				this.attachmentState.setValueTransactionlessly( BugSubmitAttachment.YES );
				rv = true;
			} else if( option == org.lgna.croquet.YesNoCancelOption.NO ) {
				this.attachmentState.setValueTransactionlessly( BugSubmitAttachment.NO );
				rv = true;
			} else {
				rv = false;
			}
		}
		return rv;
	}

	@Override
	public void handlePreActivation() {
		this.summaryState.setValueTransactionlessly( "" );
		this.descriptionState.setValueTransactionlessly( "" );
		this.visibilityState.setValueTransactionlessly( BugSubmitVisibility.PUBLIC );
		this.attachmentState.setValueTransactionlessly( null );
		this.reportTypeState.setValueTransactionlessly( this.initialReportTypeValue );

		this.summaryState.addAndInvokeValueListener( this.adapter );
		super.handlePreActivation();
	}

	@Override
	public void handlePostDeactivation() {
		super.handlePostDeactivation();
		this.summaryState.removeValueListener( this.adapter );
	}
}
