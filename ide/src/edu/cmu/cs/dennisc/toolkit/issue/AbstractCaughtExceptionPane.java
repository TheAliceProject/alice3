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

class ExceptionPane extends javax.swing.JPanel {
	private Thread thread;
	private Throwable throwable;

	protected Thread getThread() {
		return this.thread;
	}
	protected Throwable getThrowable() {
		return this.throwable;
	}
	public void setThreadAndThrowable( final Thread thread, final Throwable throwable ) {
		assert thread != null;
		assert throwable != null;
		this.thread = thread;
		this.throwable = throwable;
		this.removeAll();
		this.setLayout( new javax.swing.BoxLayout( this, javax.swing.BoxLayout.PAGE_AXIS ) );
		edu.cmu.cs.dennisc.javax.swing.components.JFauxHyperlink vcShowStackTrace = new edu.cmu.cs.dennisc.javax.swing.components.JFauxHyperlink( new javax.swing.AbstractAction( "show complete stack trace..." ) {
			public void actionPerformed( java.awt.event.ActionEvent e ) {
				edu.cmu.cs.dennisc.javax.swing.SwingUtilities.showMessageDialogInScrollableUneditableTextArea( ExceptionPane.this, edu.cmu.cs.dennisc.java.lang.ThrowableUtilities.getStackTraceAsString( throwable ), "Stack Trace", javax.swing.JOptionPane.INFORMATION_MESSAGE );
			}
		} );

		StringBuffer sb = new StringBuffer();
		sb.append( throwable.getClass().getSimpleName() );
		String message = throwable.getLocalizedMessage();
		if( message != null && message.length() > 0 ) {
			sb.append( "[" );
			sb.append( message );
			sb.append( "]" );
		}
		sb.append( " in " );
		sb.append( thread.getClass().getSimpleName() );
		sb.append( "[" );
		sb.append( thread.getName() );
		sb.append( "]" );

		this.add( new javax.swing.JLabel( sb.toString() ) );
		StackTraceElement[] elements = throwable.getStackTrace();
		if( elements.length > 0 ) {
			StackTraceElement e0 = elements[ 0 ];
			this.add( new javax.swing.JLabel( "class: " + e0.getClassName() ) );
			this.add( new javax.swing.JLabel( "method: " + e0.getMethodName() ) );
			this.add( new javax.swing.JLabel( "in file " + e0.getFileName() + " at line number " + e0.getLineNumber() ) );
		}
		this.add( vcShowStackTrace );
	}
}


public abstract class AbstractCaughtExceptionPane extends IssueReportPane {
	private static javax.swing.JLabel createSystemPropertyLabel( String propertyName ) {
		return new javax.swing.JLabel( propertyName + ": " + System.getProperty( propertyName ) );
	}
	class SystemPropertiesPane extends javax.swing.JPanel {
		class ShowAllSystemPropertiesAction extends javax.swing.AbstractAction {
			public ShowAllSystemPropertiesAction() {
				super( "show all system properties..." );
			}
			public void actionPerformed( java.awt.event.ActionEvent e ) {
				edu.cmu.cs.dennisc.javax.swing.SwingUtilities.showMessageDialogInScrollableUneditableTextArea( AbstractCaughtExceptionPane.this, edu.cmu.cs.dennisc.java.lang.SystemUtilities.getPropertiesAsXMLString(), "System Properties", javax.swing.JOptionPane.INFORMATION_MESSAGE );
			}
		}

		private edu.cmu.cs.dennisc.javax.swing.components.JFauxHyperlink vcShowAllSystemProperties = new edu.cmu.cs.dennisc.javax.swing.components.JFauxHyperlink( new ShowAllSystemPropertiesAction() );

		public SystemPropertiesPane() {
			this.setLayout( new javax.swing.BoxLayout( this, javax.swing.BoxLayout.PAGE_AXIS ) );
			for( String propertyName : getSystemPropertiesForEnvironmentField() ) {
				this.add( createSystemPropertyLabel( propertyName ) );
			}
			this.add( this.vcShowAllSystemProperties );
		}
	}

	private javax.swing.JLabel labelException = createLabelForMultiLine( "exception:" );
	private ExceptionPane paneException = new ExceptionPane();
	private java.awt.Component[] rowException = edu.cmu.cs.dennisc.javax.swing.SpringUtilities.createRow( labelException, paneException );

	private javax.swing.JLabel labelEnvironment = createLabelForMultiLine( "environment:" );
	private SystemPropertiesPane paneEnvironment = new SystemPropertiesPane();
	private java.awt.Component[] rowEnvironment = edu.cmu.cs.dennisc.javax.swing.SpringUtilities.createRow( labelEnvironment, paneEnvironment );

	private static final String NAME_SUGGESTIVE_TEXT = "please fill in your name (optional)";
	private javax.swing.JLabel labelName = createLabelForSingleLine( "reported by:" );
	private SuggestiveTextField textReporterName = new SuggestiveTextField( "", NAME_SUGGESTIVE_TEXT );
	private java.awt.Component[] rowName = edu.cmu.cs.dennisc.javax.swing.SpringUtilities.createRow( labelName, textReporterName );

	private static final String EMAIL_SUGGESTIVE_TEXT = "please fill in your e-mail address (optional)";
	private javax.swing.JLabel labelAddress = createLabelForSingleLine( "e-mail address:" );
	private SuggestiveTextField textReporterEMailAddress = new SuggestiveTextField( "", EMAIL_SUGGESTIVE_TEXT );
	private java.awt.Component[] rowAddress = edu.cmu.cs.dennisc.javax.swing.SpringUtilities.createRow( labelAddress, textReporterEMailAddress );

	class MyExpandPane extends edu.cmu.cs.dennisc.javax.swing.components.JExpandPane {
		@Override
		protected javax.swing.JComponent createCenterPane() {
			javax.swing.JPanel rv = new javax.swing.JPanel();
			rv.setBorder( javax.swing.BorderFactory.createEmptyBorder( 8, 0, 0, 0 ) );
			java.util.List< java.awt.Component[] > rows = new java.util.LinkedList< java.awt.Component[] >();
			rows.add( rowSummary );
			rows.add( rowDescription );
			rows.add( rowSteps );
			rows.add( rowException );
			rows.add( rowEnvironment );
			rows.add( rowName );
			rows.add( rowAddress );
			edu.cmu.cs.dennisc.javax.swing.SpringUtilities.springItUpANotch( rv, rows, 8, 4 );
			return rv;
		}
		@Override
		protected String getCollapsedButtonText() {
			return "yes >>>";
		}
		@Override
		protected String getCollapsedLabelText() {
			return "Can you provide insight into this problem?";
		}
		@Override
		protected String getExpandedLabelText() {
			return "Please provide insight:";
		}
	}

	private MyExpandPane expandPane = new MyExpandPane();
	
	public AbstractCaughtExceptionPane() {
		expandPane.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 8, 4, 8 ) );
		this.add( expandPane, java.awt.BorderLayout.CENTER );
	}
	
	@Override
	protected boolean isInclusionOfCompleteSystemPropertiesDesired() {
		return true;
	}

	@Override
	protected String getSummaryText() {
		String rv = super.getSummaryText();
		if( rv != null && rv.length() > 0 ) {
			//pass
		} else {
			StringBuffer sb = new StringBuffer();
			//			sb.append( "summary: unspecified; " );
			Throwable throwable = this.getThrowable();
			if( throwable != null ) {
				//				sb.append( "exception: " );
				sb.append( throwable.getClass().getName() );
				sb.append( "; " );
				String message = throwable.getMessage();
				if( message != null ) {
					sb.append( "message: " );
					sb.append( message );
					sb.append( "; " );
				}
				StackTraceElement[] stackTraceElements = throwable.getStackTrace();
				if( stackTraceElements != null && stackTraceElements.length > 0 && stackTraceElements[ 0 ] != null ) {
					sb.append( "stack[0]: " );
					sb.append( stackTraceElements[ 0 ].toString() );
					sb.append( "; " );
				}
				//			}
			}
			rv = sb.toString();
		}
		return rv;
	}
	@Override
	protected edu.cmu.cs.dennisc.jira.JIRAReport.Type getJIRAType() {
		return edu.cmu.cs.dennisc.jira.JIRAReport.Type.BUG;
	}
	@Override
	protected String getSMTPReplyToPersonal() {
		return this.textReporterName.getText();
	}
	@Override
	protected String getSMTPReplyTo() {
		return this.textReporterEMailAddress.getText();
	}
	@Override
	protected String getEnvironmentText() {
		return null;
	}
	@Override
	protected Throwable getThrowable() {
		return this.paneException.getThrowable();
	}
	@Override
	protected boolean isClearedToSubmit() {
		return true;
	}
	@Override
	protected int getPreferredDescriptionHeight() {
		return 64;
	}
	@Override
	protected int getPreferredStepsHeight() {
		return 64;
	}
	@Override
	protected boolean isSummaryRequired() {
		return false;
	}
	
	public void setThreadAndThrowable( Thread thread, Throwable throwable ) {
		assert this.paneException != null;
		this.paneException.setThreadAndThrowable( thread, throwable );
		this.revalidate();
	}
//	private StringBuffer updateMailSubject( StringBuffer rv, Issue issue ) {
//		rv.append( issue.getAffectsVersionText() );
//		rv.append( ": " );
//		String summary = issue.getSummary();
//		if( summary != null && summary.length() > 0 ) {
//			rv.append( summary );
//		} else {
//			rv.append( this.getSubSummary( issue ) );
//		}
//		return rv;
//	}
//	protected final String getMailSubject( Issue issue ) {
//		StringBuffer sb = new StringBuffer();
//		updateMailSubject( sb, issue );
//		return sb.toString();
//	}
//	protected final String getMailBody( Issue issue ) {
//		return "detailed decription:\n" + issue.getDescription() + "\n\nsteps to reproduce:\n" + issue.getSteps() + "\n\nexception:\n" + issue.getExceptionText();
//	}
}
