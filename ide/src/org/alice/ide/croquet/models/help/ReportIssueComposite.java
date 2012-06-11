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

import org.lgna.croquet.ActionOperation;
import org.lgna.croquet.BooleanState;
import org.lgna.croquet.CancelException;
import org.lgna.croquet.ListSelectionState;
import org.lgna.croquet.OperationInputDialogCoreComposite;
import org.lgna.croquet.StringState;
import org.lgna.croquet.StringValue;
import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.history.CompletionStep;
import org.lgna.croquet.history.Transaction;
import org.lgna.croquet.triggers.Trigger;

/**
 * @author Matt May
 */
public class ReportIssueComposite extends OperationInputDialogCoreComposite<ReportIssueView> {

	private static final org.lgna.croquet.Group ISSUE_GROUP = org.lgna.croquet.Group.getInstance( java.util.UUID.fromString( "af49d17b-9299-4a0d-b931-0a18a8abf0dd" ), "ISSUE_GROUP" );
	private StringValue visibilityLabel = createStringValue( this.createKey( "visibilityLabel" ) );
	private BooleanState visibilityState = createBooleanState( this.createKey( "visibilityState" ), true );
	private ListSelectionState<BugSubmitVisibility> visibilityList= createListSelectionStateForEnum( this.createKey( "visibilityList" ), BugSubmitVisibility.class, BugSubmitVisibility.PRIVATE );
	private StringValue typeLabel = createStringValue( this.createKey( "typeLabel" ) );
	private ListSelectionState<edu.cmu.cs.dennisc.jira.JIRAReport.Type> typeList = createListSelectionStateForEnum( createKey( "typeList" ), edu.cmu.cs.dennisc.jira.JIRAReport.Type.class, edu.cmu.cs.dennisc.jira.JIRAReport.Type.BUG );
	private StringValue summaryLabel = createStringValue( this.createKey( "summaryLabel" ) );
	private StringState summaryBlank = createStringState( this.createKey( "summaryBlank" ) );
	private StringValue descriptionLabel = createStringValue( this.createKey( "descriptionLabel" ) );
	private StringState descriptionBlank = createStringState( this.createKey( "descriptionBlank" ) );
	private StringValue stepsLabel = createStringValue( this.createKey( "stepsLabel" ) );
	private StringState stepsBlank = createStringState( this.createKey( "stepsBlank" ) );
	private StringValue environmentLabel = createStringValue( this.createKey( "environmentLabel" ) );
	private StringState environmentBlank = createStringState( this.createKey( "environmentBlank" ) );
	private StringValue attachmentLabel = createStringValue( this.createKey( "attachmentLabel" ) );
	private ListSelectionState<BugSubmitAttachment> attachmentList = createListSelectionStateForEnum( this.createKey( "attachmentList" ), BugSubmitAttachment.class, BugSubmitAttachment.YES );
	private BooleanState attachmentState = createBooleanState( this.createKey( "attachmentState" ), true );
	private ActionOperation loginOperation = createActionOperation( this.createKey( "loginOperation" ), new Action() {

		public Edit perform( CompletionStep<?> step, org.lgna.croquet.AbstractComposite.InternalActionOperation source ) throws CancelException {
			return null;
		}
	} );
	private ActionOperation submitBugOperation = createActionOperation( this.createKey( "submitBugOperation" ), new Action() {

		public Edit perform( CompletionStep<?> step, org.lgna.croquet.AbstractComposite.InternalActionOperation source ) throws CancelException {
			return null;
		}
	} );

	public StringValue getVisibilityLabel() {
		return this.visibilityLabel;
	}
	public BooleanState getVisibilityState() {
		return this.visibilityState;
	}
	public ListSelectionState<BugSubmitVisibility> getVisibilityList() {
		return this.visibilityList;
	}
	public StringValue getTypeLabel() {
		return this.typeLabel;
	}
	public ListSelectionState<edu.cmu.cs.dennisc.jira.JIRAReport.Type> getTypeList() {
		return this.typeList;
	}
	public StringValue getSummaryLabel() {
		return this.summaryLabel;
	}
	public StringState getSummaryBlank() {
		return this.summaryBlank;
	}
	public StringValue getDescriptionLabel() {
		return this.descriptionLabel;
	}
	public StringState getDescriptionBlank() {
		return this.descriptionBlank;
	}
	public StringValue getStepsLabel() {
		return this.stepsLabel;
	}
	public StringState getStepsBlank() {
		return this.stepsBlank;
	}
	public StringValue getEnvironmentLabel() {
		return this.environmentLabel;
	}
	public StringState getEnvironmentBlank() {
		return this.environmentBlank;
	}
	public StringValue getAttachmentLabel() {
		return this.attachmentLabel;
	}
	public BooleanState getAttachmentState() {
		return this.attachmentState;
	}
	public ListSelectionState<BugSubmitAttachment> getAttachmentList() {
		return this.attachmentList;
	}
	public ActionOperation getLoginOperation() {
		return this.loginOperation;
	}
	public ActionOperation getSubmitBugOperation() {
		return this.submitBugOperation;
	}

	public ReportIssueComposite() {
		super( java.util.UUID.fromString( "31d2104a-c90c-4861-9082-3a0390527a04" ), ISSUE_GROUP );
	}

	@Override
	protected ReportIssueView createView() {
		return new ReportIssueView( this );
	}
	@Override
	protected Status getStatus( CompletionStep<?> step ) {
		return IS_GOOD_TO_GO_STATUS;
	}
	@Override
	protected Edit createEdit( CompletionStep<?> completionStep ) {
		return null;
	}

	public static void main( String[] args ) throws Exception {
		javax.swing.UIManager.LookAndFeelInfo lookAndFeelInfo = edu.cmu.cs.dennisc.javax.swing.plaf.PlafUtilities.getInstalledLookAndFeelInfoNamed( "Nimbus" );
		if( lookAndFeelInfo != null ) {
			javax.swing.UIManager.setLookAndFeel( lookAndFeelInfo.getClassName() );
		}
		new org.alice.stageide.StageIDE();
		try {
			org.lgna.croquet.triggers.Trigger trigger = null;
			new ReportIssueComposite().getOperation().fire( trigger );
		} catch( org.lgna.croquet.CancelException ce ) {
			//pass
		}
		System.exit( 0 );
	}
}
