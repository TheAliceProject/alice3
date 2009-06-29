/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package edu.cmu.cs.dennisc.issue;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractCaughtExceptionPane extends AbstractIssuePane {
	class ExceptionPane extends javax.swing.JPanel {
		private Thread thread;
		private Throwable throwable;

		protected Thread getThread() {
			return this.thread;
		}
		protected Throwable getThrowable() {
			return this.throwable;
		}
		private void setThreadAndThrowable( final Thread thread, final Throwable throwable ) {
			assert thread != null;
			assert throwable != null;
			this.thread = thread;
			this.throwable = throwable;
			this.removeAll();
			this.setLayout( new javax.swing.BoxLayout( this, javax.swing.BoxLayout.PAGE_AXIS ) );
			edu.cmu.cs.dennisc.swing.FauxHyperlink vcShowStackTrace = new edu.cmu.cs.dennisc.swing.FauxHyperlink( new javax.swing.AbstractAction( "show complete stack trace..." ) {
				public void actionPerformed( java.awt.event.ActionEvent e ) {
					edu.cmu.cs.dennisc.swing.SwingUtilities.showMessageDialogInScrollableUneditableTextArea( ExceptionPane.this, edu.cmu.cs.dennisc.lang.ThrowableUtilities.getStackTraceAsString( throwable ), "Stack Trace", javax.swing.JOptionPane.INFORMATION_MESSAGE );
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

	private ExceptionPane vcExceptionPane;

	public AbstractCaughtExceptionPane() {
		class MyExpandPane extends edu.cmu.cs.dennisc.swing.ExpandPane {
			@Override
			protected javax.swing.JComponent createCenterPane() {
				javax.swing.JPanel rv = new javax.swing.JPanel();
				rv.setBorder( javax.swing.BorderFactory.createEmptyBorder( 8, 0, 0, 0 ) );
				edu.cmu.cs.dennisc.swing.SpringUtilities.springItUpANotch( rv, AbstractCaughtExceptionPane.this.createInsightPaneRows(), 8, 4 );
				return rv;
			}
			@Override
			protected java.lang.String getCollapsedButtonText() {
				return "yes >>>";
			}
			@Override
			protected java.lang.String getCollapsedLabelText() {
				return "Can you provide insight into this problem?";
			}
			@Override
			protected java.lang.String getExpandedLabelText() {
				return "Please provide insight:";
			}
		}
		MyExpandPane expandPane = new MyExpandPane();

		javax.swing.JPanel reporterPane = new javax.swing.JPanel();
		javax.swing.JPanel bugPane = new javax.swing.JPanel();
		edu.cmu.cs.dennisc.swing.SpringUtilities.springItUpANotch( bugPane, createBugPaneRows(), 8, 4 );
		edu.cmu.cs.dennisc.swing.SpringUtilities.springItUpANotch( reporterPane, createReporterPaneRows(), 8, 4 );
		//edu.cmu.cs.dennisc.swing.SpringUtilities.springItUpANotch( insightPane, createInsightPaneRows(), 8, 4 );


		StringBuffer sb = new StringBuffer();
		sb.append( "An exception has been caught:\n\n" );
		sb.append( "  If you were running your program then:\n    it could be either a bug(error) in Alice or your code.\n\n" );
		sb.append( "  If you were building your program then:\n    it is a bug in Alice.\n\n" );
		sb.append( "Please press the \"submit bug report\" button." );
		javax.swing.JTextArea header = new javax.swing.JTextArea( sb.toString() ) {
			@Override
			public void paint( java.awt.Graphics g ) {
				java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
				g2.setRenderingHint( java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
				super.paint( g );
			}
		};
		header.setEditable( false );
		//header.setOpaque( false );
		header.setLineWrap( true );
		header.setWrapStyleWord( true );
		java.awt.Font font = header.getFont();
		font = font.deriveFont( font.getSize2D() * 1.15f );
		font = font.deriveFont( java.awt.Font.BOLD );
		header.setFont( font );
		header.setOpaque( false );
		header.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 8, 4, 0 ) );

		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 8, 8, 8, 8 ) );
		this.setLayout( new javax.swing.BoxLayout( this, javax.swing.BoxLayout.PAGE_AXIS ) );
		this.add( header );
		this.add( javax.swing.Box.createRigidArea( new java.awt.Dimension( 0, 16 ) ) );
		this.add( reporterPane );
		this.add( javax.swing.Box.createRigidArea( new java.awt.Dimension( 0, 24 ) ) );
		this.add( bugPane );
		this.add( javax.swing.Box.createRigidArea( new java.awt.Dimension( 0, 24 ) ) );
		this.add( expandPane );
		this.add( javax.swing.Box.createRigidArea( new java.awt.Dimension( 0, 16 ) ) );
		this.add( this.getSubmitButton() );
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
	@Override
	protected boolean isStepsPaneDesired() {
		return true;
	}
	
	@Override
	protected boolean isInclusionOfCompleteSystemPropertiesDesired() {
		return true;
	}
	@Override
	protected java.util.ArrayList< edu.cmu.cs.dennisc.issue.Attachment > updateCriticalAttachments( java.util.ArrayList< edu.cmu.cs.dennisc.issue.Attachment > rv ) {
		rv = super.updateCriticalAttachments( rv );
		rv.add( new edu.cmu.cs.dennisc.issue.Attachment() {
			public byte[] getBytes() {
				Throwable throwable = AbstractCaughtExceptionPane.this.vcExceptionPane.getThrowable();
				return edu.cmu.cs.dennisc.lang.ThrowableUtilities.getStackTraceAsByteArray( throwable );
			}
			public String getMIMEType() {
				return "text/plain";
			}
			public String getFileName() {
				return "stacktrace.txt";
			}
		} );
		return rv;
	}
	public void setThreadAndThrowable( Thread thread, Throwable throwable ) {
		assert this.vcExceptionPane != null;
		this.vcExceptionPane.setThreadAndThrowable( thread, throwable );
		this.revalidate();
	}
	
	@Override
	protected Issue updateIssue( edu.cmu.cs.dennisc.issue.Issue rv ) {
		super.updateIssue( rv );
		rv.setType( Issue.Type.BUG );
		rv.setThrowable( this.vcExceptionPane.getThrowable() );
		return rv;
	}

	
	@Override
	protected java.util.ArrayList< java.awt.Component[] > addBugPaneRows( java.util.ArrayList< java.awt.Component[] > rv ) {
		assert this.vcExceptionPane == null;
		this.vcExceptionPane = new ExceptionPane();
		javax.swing.JLabel exceptionLabel = new javax.swing.JLabel( "exception:", javax.swing.SwingConstants.TRAILING );
		exceptionLabel.setVerticalAlignment( javax.swing.SwingConstants.TOP );
		rv.add( new java.awt.Component[] { exceptionLabel, this.vcExceptionPane } );
		rv = super.addBugPaneRows( rv );
		return rv;
	}
}
