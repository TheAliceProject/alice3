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
package edu.cmu.cs.dennisc.toolkit.issue;

import edu.cmu.cs.dennisc.issue.ProgressPane;
import edu.cmu.cs.dennisc.issue.ReportGenerator;
import edu.cmu.cs.dennisc.issue.ReportSubmissionConfiguration;

/**
 * @author Dennis Cosgrove
 */
public abstract class IssueReportPane extends javax.swing.JPanel implements ReportGenerator {
	protected abstract ReportSubmissionConfiguration getReportSubmissionConfiguration();
	public Iterable< String > getSystemPropertiesForEnvironmentField() {
		java.util.List< String > rv = new java.util.LinkedList< String >();
		rv.add( "java.version" );
		rv.add( "os.name" );
		rv.add( "os.arch" );
		rv.add( "os.version" );
		rv.add( "sun.arch.data.model" );
		return rv;
	}

	class SubmitAction extends javax.swing.AbstractAction {
		public SubmitAction() {
			super( "submit bug report" );
		}
		public void actionPerformed( java.awt.event.ActionEvent e ) {
			if( IssueReportPane.this.isClearedToSubmit() ) {
				IssueReportPane.this.isSubmitAttempted = true;
				IssueReportPane.this.isSubmitSuccessful = IssueReportPane.this.submit();
				java.awt.Component root = javax.swing.SwingUtilities.getRoot( IssueReportPane.this );
				root.setVisible( false );
				//				if( IssueReportPane.this.window != null ) {
				//					IssueReportPane.this.window.setVisible( false );
				//				}
			}
		}
	}

	private javax.swing.JButton submitButton = new javax.swing.JButton( new SubmitAction() );

	protected abstract int getPreferredDescriptionHeight();
	protected abstract int getPreferredStepsHeight();

	private static final String SUMMARY_SUGGESTIVE_TEXT = "please fill in a one line synopsis";
	private static final String DESCRIPTION_SUGGESTIVE_TEXT = "please fill in a detailed description";
	private static final String STEPS_SUGGESTIVE_TEXT = "please fill in the steps required to reproduce the bug";

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
	private SuggestiveTextField textSummary = new SuggestiveTextField( "", this.getSummarySuggestiveText() );
	protected java.awt.Component[] rowSummary = edu.cmu.cs.dennisc.javax.swing.SpringUtilities.createRow( labelSummary, textSummary );

	private javax.swing.JLabel labelDescription = createLabelForMultiLine( "description:" );
	private SuggestiveTextArea textDescription = new SuggestiveTextArea( "", DESCRIPTION_SUGGESTIVE_TEXT );
	private javax.swing.JScrollPane scrollDescription = new javax.swing.JScrollPane( this.textDescription, javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER ) {
		@Override
		public java.awt.Dimension getPreferredSize() {
			return edu.cmu.cs.dennisc.java.awt.DimensionUtilities.constrainToMinimumHeight( super.getPreferredSize(), IssueReportPane.this.getPreferredDescriptionHeight() );
		}
	};
	protected java.awt.Component[] rowDescription = edu.cmu.cs.dennisc.javax.swing.SpringUtilities.createRow( labelDescription, scrollDescription );

	private javax.swing.JLabel labelSteps = createLabelForMultiLine( "steps:" );
	private SuggestiveTextArea textSteps = new SuggestiveTextArea( "", STEPS_SUGGESTIVE_TEXT );
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

		this.labelSummary.setToolTipText( textSummary.getSuggestiveText() );
		this.labelDescription.setToolTipText( textDescription.getSuggestiveText() );
		this.labelSteps.setToolTipText( textSteps.getSuggestiveText() );
		
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
	protected abstract edu.cmu.cs.dennisc.jira.JIRAReport.Type getJIRAType();
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

	protected String getSMTPSubject() {
		return this.getSummaryText();
	}
	protected String getSMTPBody() {
		StringBuffer sb = new StringBuffer();
		sb.append( "affects versions: " );
		sb.append( java.util.Arrays.toString( this.getAffectsVersions() ) );
		sb.append( "\n" );
		sb.append( "description: " );
		sb.append( this.getDescriptionText() );
		sb.append( "\n" );
		sb.append( "environment: " );
		sb.append( this.getEnvironmentText() );
		sb.append( "\n" );
		sb.append( "steps: " );
		sb.append( this.getStepsText() );
		sb.append( "\n" );
		sb.append( "exception: " );
		sb.append( this.getExceptionText() );
		sb.append( "\n" );
		return sb.toString();
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
	private edu.cmu.cs.dennisc.jira.JIRAReport generateIssue() {
		edu.cmu.cs.dennisc.jira.JIRAReport rv = new edu.cmu.cs.dennisc.jira.JIRAReport();
		rv.setProjectKey( this.getJIRAProjectKey() );
		rv.setType( this.getJIRAType() );
		rv.setSummary( this.getSummaryText() );
		rv.setDescription( this.getDescriptionText() );
		rv.setEnvironment( this.getEnvironmentText() );
		rv.setSteps( this.getStepsText() );
		rv.setException( this.getExceptionText() );
		rv.setAffectsVersions( this.getAffectsVersions() );
		return rv;
	}

	public edu.cmu.cs.dennisc.jira.JIRAReport generateIssueForRPC() {
		edu.cmu.cs.dennisc.jira.JIRAReport rv = this.generateIssue();
		return rv;
	}
	public edu.cmu.cs.dennisc.jira.JIRAReport generateIssueForSOAP() {
		edu.cmu.cs.dennisc.jira.JIRAReport rv = this.generateIssue();
		this.addAttachments( rv );
		return rv;
	}
	public edu.cmu.cs.dennisc.mail.MailReport generateIssueForSMTP() {
		edu.cmu.cs.dennisc.mail.MailReport rv = new edu.cmu.cs.dennisc.mail.MailReport();
		rv.setSubject( this.getSMTPSubject() );
		rv.setBody( this.getSMTPBody() );
		rv.setReplyTo( this.getSMTPReplyTo() );
		rv.setReplyToPersonal( this.getSMTPReplyToPersonal() );
		this.addAttachments( rv );
		return rv;
	}

	//	private java.awt.Window window;
	//	public java.awt.Window setWindow() {
	//		return this.window;
	//	}
	//	public void setWindow( java.awt.Window window ) {
	//		this.window = window;
	//	}

//	protected String getSummary() {
//		return this.textSummary.getText();
//	}
//	protected String getDescription() {
//		return this.textDescription.getText();
//	}
//	protected String getStepsToReproduce() {
//		return this.vcSteps.getText();
//	}

	public javax.swing.JButton getSubmitButton() {
		return this.submitButton;
	}

//	protected abstract int getPreferredWidth();
//
//	@Override
//	public final java.awt.Dimension getPreferredSize() {
//		java.awt.Dimension rv = super.getPreferredSize();
//		rv.width = this.getPreferredWidth();
//		return rv;
//	}

	private boolean isSubmitAttempted = false;
	private boolean isSubmitSuccessful = false;
	private boolean isSubmitBackgrounded = false;
	private java.net.URL urlResult = null;

	public boolean isSubmitAttempted() {
		return this.isSubmitAttempted;
	}
	public boolean isSubmitBackgrounded() {
		return this.isSubmitBackgrounded;
	}
	public boolean isSubmitSuccessful() {
		return this.isSubmitSuccessful;
	}
	public java.net.URL getURLResult() {
		return this.urlResult;
	}
	protected abstract boolean isClearedToSubmit();

	protected boolean submit() {
		ProgressPane progressPane = new ProgressPane();
		progressPane.initializeAndExecuteWorker( this, this.getReportSubmissionConfiguration() );

		this.isSubmitBackgrounded = false;
		javax.swing.JFrame frame = new javax.swing.JFrame();
		javax.swing.JDialog dialog = new javax.swing.JDialog( frame, "Uploading Bug Report", true );
		dialog.addWindowListener( new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing( java.awt.event.WindowEvent e ) {
				IssueReportPane.this.isSubmitBackgrounded = true;
				e.getComponent().setVisible( false );
			}
		} );
		dialog.getContentPane().add( progressPane );
		dialog.setDefaultCloseOperation( javax.swing.JFrame.DISPOSE_ON_CLOSE );
		dialog.pack();
		dialog.setVisible( true );

		if( this.isSubmitBackgrounded ) {
			//pass
		} else {
			this.isSubmitBackgrounded = progressPane.isBackgrounded();
		}

		this.urlResult = progressPane.getURLResult();
		
		return progressPane.isSuccessful();
	}
}
