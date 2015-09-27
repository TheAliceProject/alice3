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
package org.alice.ide.issue.swing.views;

import edu.cmu.cs.dennisc.issue.ReportGenerator;
import edu.cmu.cs.dennisc.issue.ReportSubmissionConfiguration;

/**
 * @author Dennis Cosgrove
 */
public abstract class IssueReportPane extends javax.swing.JPanel implements ReportGenerator {
	protected abstract ReportSubmissionConfiguration getReportSubmissionConfiguration();

	private static final java.util.List<String> systemPropertiesForEnnvironmentField = java.util.Collections.unmodifiableList( edu.cmu.cs.dennisc.java.util.Lists.newArrayList( "java.version", "os.name", "os.arch" ) );

	public static java.util.List<String> getSystemPropertiesForEnvironmentField() {
		return systemPropertiesForEnnvironmentField;
	}

	public static final String getEnvironmentLongDescription() {
		StringBuilder sb = new StringBuilder();
		String intersticial = "";
		for( String propertyName : systemPropertiesForEnnvironmentField ) {
			sb.append( intersticial );
			sb.append( propertyName );
			sb.append( ": " );
			sb.append( System.getProperty( propertyName ) );
			intersticial = "\n";
		}
		return sb.toString();
	}

	public static final String getEnvironmentShortDescription() {
		StringBuilder sb = new StringBuilder();
		String intersticial = "";
		for( String propertyName : systemPropertiesForEnnvironmentField ) {
			sb.append( intersticial );
			sb.append( System.getProperty( propertyName ) );
			intersticial = ";";
		}
		if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isMac() ) {
			sb.append( ";" );
			sb.append( System.getProperty( "os.version" ) );
		}
		return sb.toString();
	}

	private class SubmitAction extends org.alice.ide.issue.swing.SubmitReportAction {
		@Override
		public void actionPerformed( java.awt.event.ActionEvent e ) {
			IssueReportPane.this.submit();
		}
	}

	private final javax.swing.Action submitAction = new SubmitAction();

	private javax.swing.JButton submitButton = new javax.swing.JButton( submitAction );

	protected abstract int getPreferredDescriptionHeight();

	protected abstract int getPreferredStepsHeight();

	private static final String SUMMARY_SUGGESTIVE_TEXT = "please fill in a one line synopsis";
	private static final String DESCRIPTION_SUGGESTIVE_TEXT = "please fill in a detailed description";
	private static final String STEPS_SUGGESTIVE_TEXT = "please fill in the steps required to reproduce the bug";

	protected javax.swing.Action getSubmitAction() {
		return this.submitAction;
	}

	protected abstract boolean isSummaryRequired();

	private String getSummarySuggestiveText() {
		String rv = SUMMARY_SUGGESTIVE_TEXT;
		if( this.isSummaryRequired() ) {
			rv = rv + " (Required)";
		}
		return rv;
	}

	protected static javax.swing.JLabel createLabelForSingleLine( String text ) {
		return new javax.swing.JLabel( text, javax.swing.SwingConstants.TRAILING );
	}

	protected static javax.swing.JLabel createLabelForMultiLine( String text ) {
		javax.swing.JLabel rv = createLabelForSingleLine( text );
		rv.setVerticalAlignment( javax.swing.SwingConstants.TOP );
		return rv;
	}

	private javax.swing.JLabel labelSummary = createLabelForSingleLine( "summary:" );
	private edu.cmu.cs.dennisc.javax.swing.components.JSuggestiveTextField textSummary = new edu.cmu.cs.dennisc.javax.swing.components.JSuggestiveTextField( "", this.getSummarySuggestiveText() );
	protected java.awt.Component[] rowSummary = edu.cmu.cs.dennisc.javax.swing.SpringUtilities.createRow( labelSummary, textSummary );

	private javax.swing.JLabel labelDescription = createLabelForMultiLine( "description:" );
	private edu.cmu.cs.dennisc.javax.swing.components.JSuggestiveTextArea textDescription = new edu.cmu.cs.dennisc.javax.swing.components.JSuggestiveTextArea( "", DESCRIPTION_SUGGESTIVE_TEXT );
	private javax.swing.JScrollPane scrollDescription = new javax.swing.JScrollPane( this.textDescription, javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER ) {
		@Override
		public java.awt.Dimension getPreferredSize() {
			return edu.cmu.cs.dennisc.java.awt.DimensionUtilities.constrainToMinimumHeight( super.getPreferredSize(), IssueReportPane.this.getPreferredDescriptionHeight() );
		}
	};
	protected java.awt.Component[] rowDescription = edu.cmu.cs.dennisc.javax.swing.SpringUtilities.createRow( labelDescription, scrollDescription );

	private javax.swing.JLabel labelSteps = createLabelForMultiLine( "steps:" );
	private edu.cmu.cs.dennisc.javax.swing.components.JSuggestiveTextArea textSteps = new edu.cmu.cs.dennisc.javax.swing.components.JSuggestiveTextArea( "", STEPS_SUGGESTIVE_TEXT );
	private javax.swing.JScrollPane scrollSteps = new javax.swing.JScrollPane( this.textSteps, javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER ) {
		@Override
		public java.awt.Dimension getPreferredSize() {
			return edu.cmu.cs.dennisc.java.awt.DimensionUtilities.constrainToMinimumHeight( super.getPreferredSize(), IssueReportPane.this.getPreferredStepsHeight() );
		}
	};
	protected java.awt.Component[] rowSteps = edu.cmu.cs.dennisc.javax.swing.SpringUtilities.createRow( labelSteps, scrollSteps );

	public IssueReportPane() {
		java.awt.Font font = this.submitButton.getFont();
		this.submitButton.setFont( font.deriveFont( font.getSize2D() * 1.5f ) );
		this.submitButton.setAlignmentX( java.awt.Component.CENTER_ALIGNMENT );

		this.labelSummary.setToolTipText( textSummary.getTextForBlankCondition() );
		this.labelDescription.setToolTipText( textDescription.getTextForBlankCondition() );
		this.labelSteps.setToolTipText( textSteps.getTextForBlankCondition() );

		this.scrollDescription.setBorder( javax.swing.BorderFactory.createEmptyBorder() );
		this.scrollSteps.setBorder( javax.swing.BorderFactory.createEmptyBorder() );

		this.textDescription.setLineWrap( true );
		this.textDescription.setWrapStyleWord( true );
		this.textSteps.setLineWrap( true );
		this.textSteps.setWrapStyleWord( true );

		this.setLayout( new java.awt.BorderLayout() );
		javax.swing.JPanel southPane = new javax.swing.JPanel();
		southPane.add( this.getSubmitButton() );
		this.add( southPane, java.awt.BorderLayout.SOUTH );
	}

	protected abstract String getJIRAProjectKey();

	protected abstract edu.cmu.cs.dennisc.issue.IssueType getIssueType();

	protected String getSummaryText() {
		return this.textSummary.getText();
	}

	protected String getDescriptionText() {
		return this.textDescription.getText();
	}

	protected abstract String getEnvironmentText();

	protected String getStepsText() {
		return this.textSteps.getText();
	}

	protected abstract Thread getThread();

	protected abstract Throwable getThrowable();

	protected abstract String[] getAffectsVersions();

	private String getExceptionText() {
		Throwable throwable = this.getThrowable();
		if( throwable != null ) {
			return edu.cmu.cs.dennisc.java.lang.ThrowableUtilities.getStackTraceAsString( throwable );
		} else {
			return "";
		}
	}

	protected abstract String getSMTPReplyTo();

	protected abstract String getSMTPReplyToPersonal();

	protected abstract boolean isInclusionOfCompleteSystemPropertiesDesired();

	protected edu.cmu.cs.dennisc.issue.AbstractReport addAttachments( edu.cmu.cs.dennisc.issue.AbstractReport rv ) {
		Throwable throwable = this.getThrowable();
		if( throwable != null ) {
			rv.addAttachment( new edu.cmu.cs.dennisc.issue.StackTraceAttachment( throwable ) );
		}
		if( this.isInclusionOfCompleteSystemPropertiesDesired() ) {
			rv.addAttachment( new edu.cmu.cs.dennisc.issue.SystemPropertiesAttachment() );
		}
		return rv;
	}

	private edu.cmu.cs.dennisc.issue.Issue.Builder createIssueBuilder() {
		return new edu.cmu.cs.dennisc.issue.Issue.Builder()
				.type( this.getIssueType() )
				.summary( this.getSummaryText() )
				.description( this.getDescriptionText() )
				.environment( this.getEnvironmentText() )
				.steps( this.getStepsText() )
				.threadAndThrowable( this.getThread(), this.getThrowable() )
				.version( org.lgna.project.ProjectVersion.getCurrentVersionText() )
				.reportedBy( this.getSMTPReplyToPersonal() )
				.emailAddress( this.getSMTPReplyTo() );
	}

	private edu.cmu.cs.dennisc.jira.JIRAReport createJiraReport() {
		edu.cmu.cs.dennisc.issue.Issue.Builder builder = this.createIssueBuilder();
		edu.cmu.cs.dennisc.jira.JIRAReport rv = new edu.cmu.cs.dennisc.jira.JIRAReport( builder.build(), this.getJIRAProjectKey() );
		return rv;
	}

	@Override
	public edu.cmu.cs.dennisc.jira.JIRAReport generateIssueForRPC() {
		edu.cmu.cs.dennisc.jira.JIRAReport rv = this.createJiraReport();
		return rv;
	}

	@Override
	public edu.cmu.cs.dennisc.jira.JIRAReport generateIssueForSOAP() {
		edu.cmu.cs.dennisc.jira.JIRAReport rv = this.createJiraReport();
		this.addAttachments( rv );
		return rv;
	}

	//	public edu.cmu.cs.dennisc.mail.MailReport generateIssueForSMTP() {
	//		edu.cmu.cs.dennisc.mail.MailReport rv = new edu.cmu.cs.dennisc.mail.MailReport();
	//		rv.setSubject( this.getSMTPSubject() );
	//		rv.setBody( this.getSMTPBody() );
	//		rv.setReplyTo( this.getSMTPReplyTo() );
	//		rv.setReplyToPersonal( this.getSMTPReplyToPersonal() );
	//		this.addAttachments( rv );
	//		return rv;
	//	}

	public javax.swing.JButton getSubmitButton() {
		return this.submitButton;
	}

	private boolean isSubmitAttempted = false;
	private boolean isSubmitSuccessful = false;
	private boolean isSubmitDone = false;
	private java.net.URL urlResult = null;

	public boolean isSubmitAttempted() {
		return this.isSubmitAttempted;
	}

	public boolean isSubmitDone() {
		return this.isSubmitDone;
	}

	public boolean isSubmitSuccessful() {
		return this.isSubmitSuccessful;
	}

	public java.net.URL getURLResult() {
		return this.urlResult;
	}

	protected void submit() {
		this.isSubmitSuccessful = false;
		this.isSubmitDone = false;
		this.urlResult = null;
		this.isSubmitAttempted = true;
		ProgressPane progressPane = org.alice.ide.issue.SubmitReportUtilities.submitReport( this, this.getReportSubmissionConfiguration() );
		this.urlResult = progressPane.getURLResult();
		this.isSubmitSuccessful = progressPane.isSuccessful();
		this.isSubmitDone = progressPane.isDone();
		java.awt.Component root = javax.swing.SwingUtilities.getRoot( this );
		if( root != null ) {
			root.setVisible( false );
		}
	}
}
