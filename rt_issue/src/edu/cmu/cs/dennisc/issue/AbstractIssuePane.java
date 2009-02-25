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
class FocusAdapter implements java.awt.event.FocusListener {
	private javax.swing.text.JTextComponent textComponent;

	public FocusAdapter( javax.swing.text.JTextComponent textComponent ) {
		this.textComponent = textComponent;
	}
	public void focusGained( java.awt.event.FocusEvent arg0 ) {
		if( this.textComponent.getText().length() == 0 ) {
			this.textComponent.repaint();
		}
		//this.textComponent.setBackground( new java.awt.Color( 230, 230, 255 ) );
	}
	public void focusLost( java.awt.event.FocusEvent arg0 ) {
		if( this.textComponent.getText().length() == 0 ) {
			this.textComponent.repaint();
		}
		//this.textComponent.setBackground( java.awt.Color.WHITE );
	}
}

/**
 * @author Dennis Cosgrove
 */
class SuggestiveTextUtilties {
	public static void drawBlankTextIfNecessary( javax.swing.text.JTextComponent textComponent, java.awt.Graphics g, String textForBlankCondition ) {
		String text = textComponent.getText();
		if( text.length() > 0 ) {
			//pass
		} else {
			java.awt.Font font = textComponent.getFont().deriveFont( java.awt.Font.ITALIC );
			g.setFont( font );
			int grayscale = 160;
			//			int grayscale;
			//			if( textComponent.isFocusOwner() ) {
			//				grayscale = 160;
			//			} else {
			//				grayscale = 100;
			//			}
			g.setColor( new java.awt.Color( grayscale, grayscale, grayscale ) );
			java.awt.geom.Rectangle2D bounds = g.getFontMetrics().getStringBounds( text, g );
			g.drawString( textForBlankCondition, 4, (int)bounds.getHeight() + 0 );
		}
	}
}

/**
 * @author Dennis Cosgrove
 */
class SuggestiveTextField extends javax.swing.JTextField {
	private String textForBlankCondition;

	public SuggestiveTextField( String text, String textForBlankCondition ) {
		super( text );
		this.setBorder( javax.swing.BorderFactory.createBevelBorder( javax.swing.border.BevelBorder.LOWERED ) );
		this.textForBlankCondition = textForBlankCondition;
		this.addFocusListener( new FocusAdapter( this ) );
		//setToolTipText( this.textForBlankCondition );
	}
	@Override
	public java.awt.Dimension getMaximumSize() {
		java.awt.Dimension rv = super.getMaximumSize();
		java.awt.Dimension preferred = getPreferredSize();
		rv.height = preferred.height;
		return rv;
	}
	@Override
	protected void paintComponent( java.awt.Graphics g ) {
		super.paintComponent( g );
		SuggestiveTextUtilties.drawBlankTextIfNecessary( this, g, this.textForBlankCondition );
	}
}

/**
 * @author Dennis Cosgrove
 */
class SuggestiveTextArea extends javax.swing.JTextArea {
	private String textForBlankCondition;

	public SuggestiveTextArea( String text, String textForBlankCondition ) {
		super( text );
		this.setBorder( javax.swing.BorderFactory.createBevelBorder( javax.swing.border.BevelBorder.LOWERED ) );
		this.textForBlankCondition = textForBlankCondition;
		//this.setToolTipText( this.textForBlankCondition );
		this.addFocusListener( new FocusAdapter( this ) );
		this.addKeyListener( new java.awt.event.KeyListener() {
			public void keyPressed( java.awt.event.KeyEvent e ) {
				if( e.getKeyCode() == java.awt.event.KeyEvent.VK_TAB ) {
					e.consume();
					if( e.isShiftDown() ) {
						java.awt.KeyboardFocusManager.getCurrentKeyboardFocusManager().focusPreviousComponent();
					} else {
						java.awt.KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent();
					}
				}
			}
			public void keyReleased( java.awt.event.KeyEvent e ) {
			}
			public void keyTyped( java.awt.event.KeyEvent e ) {
			}
		} );
	}
	@Override
	public boolean isManagingFocus() {
		return false;
	}
	@Override
	public java.awt.Dimension getPreferredSize() {
		java.awt.Dimension rv = super.getPreferredSize();
		rv.height = Math.max( rv.height, 64 );
		return rv;
	}
	@Override
	protected void paintComponent( java.awt.Graphics g ) {
		super.paintComponent( g );
		SuggestiveTextUtilties.drawBlankTextIfNecessary( this, g, this.textForBlankCondition );
	}
}

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractIssuePane extends javax.swing.JPanel {
	private static javax.swing.JLabel createSystemPropertyLabel( String propertyName ) {
		return new javax.swing.JLabel( propertyName + ": " + System.getProperty( propertyName ) );
	}

	class SystemPropertiesPane extends javax.swing.JPanel {
		class ShowAllSystemPropertiesAction extends javax.swing.AbstractAction {
			public ShowAllSystemPropertiesAction() {
				super( "show all system properties..." );
			}
			public void actionPerformed( java.awt.event.ActionEvent e ) {
				edu.cmu.cs.dennisc.swing.SwingUtilities.showMessageDialogInScrollableUneditableTextArea( AbstractIssuePane.this, edu.cmu.cs.dennisc.lang.SystemUtilities.getPropertiesAsXMLString(), "System Properties", javax.swing.JOptionPane.INFORMATION_MESSAGE );
			}
		}

		private edu.cmu.cs.dennisc.swing.Hyperlink vcShowAllSystemProperties = new edu.cmu.cs.dennisc.swing.Hyperlink( new ShowAllSystemPropertiesAction() );

		public SystemPropertiesPane() {
			this.setLayout( new javax.swing.BoxLayout( this, javax.swing.BoxLayout.PAGE_AXIS ) );
			this.add( createSystemPropertyLabel( "java.version" ) );
			this.add( createSystemPropertyLabel( "os.name" ) );
			this.add( this.vcShowAllSystemProperties );
		}

	}

	private static final String SUMMARY_TEXT = "please fill in a one line synopsis of the bug";
	private static final String DESCRIPTION_TEXT = "please fill in a detailed description of the bug";
	private static final String STEPS_TEXT = "please fill in the steps required to reproduce the bug";
	private SuggestiveTextField vcSummary = new SuggestiveTextField( "", SUMMARY_TEXT );
	private SuggestiveTextArea vcDescription = new SuggestiveTextArea( "", DESCRIPTION_TEXT );
	private SuggestiveTextArea vcStepsToReproduce = new SuggestiveTextArea( "", STEPS_TEXT );
	private SystemPropertiesPane vcSystemPropertiesPane = new SystemPropertiesPane();

	private static final String NAME_TEXT = "please fill in your name (optional)";
	private static final String EMAIL_TEXT = "please fill in your e-mail address (optional)";
	private SuggestiveTextField vcReporterName = new SuggestiveTextField( "", NAME_TEXT );
	private SuggestiveTextField vcReporterEMailAddress = new SuggestiveTextField( "", EMAIL_TEXT );

	class SubmitAction extends javax.swing.AbstractAction {
		public SubmitAction() {
			super( "submit bug report" );
		}
		public void actionPerformed( java.awt.event.ActionEvent e ) {
			AbstractIssuePane.this.isSubmitAttempted = true;
			AbstractIssuePane.this.isSubmitSuccessful = AbstractIssuePane.this.submit();
			if( AbstractIssuePane.this.window != null ) {
				AbstractIssuePane.this.window.setVisible( false );
			}
		}
	}

	private javax.swing.JButton vcSubmit = new javax.swing.JButton( new SubmitAction() );

	public AbstractIssuePane() {
		javax.swing.JPanel reporterPane = new javax.swing.JPanel();
		//reporterPane.setBorder( javax.swing.BorderFactory.createTitledBorder( "about you" ) );
		javax.swing.JPanel bugPane = new javax.swing.JPanel();
		//bugPane.setBorder( javax.swing.BorderFactory.createTitledBorder( "about the bug" ) );
		
		class MyExpandPane extends edu.cmu.cs.dennisc.swing.ExpandPane {
			@Override
			protected javax.swing.JComponent createCenterPane() {
				javax.swing.JPanel rv = new javax.swing.JPanel();
				rv.setBorder( javax.swing.BorderFactory.createEmptyBorder( 8, 0, 0, 0 ) );
				edu.cmu.cs.dennisc.swing.SpringUtilities.springItUpANotch( rv, AbstractIssuePane.this.createInsightPaneRows(), 8, 4 );
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
		edu.cmu.cs.dennisc.swing.SpringUtilities.springItUpANotch( bugPane, createBugPaneRows(), 8, 4 );
		edu.cmu.cs.dennisc.swing.SpringUtilities.springItUpANotch( reporterPane, createReporterPaneRows(), 8, 4 );
		//edu.cmu.cs.dennisc.swing.SpringUtilities.springItUpANotch( insightPane, createInsightPaneRows(), 8, 4 );

		//this.vcSubmit.setBackground( java.awt.Color.GREEN.brighter() );
		java.awt.Font font = this.vcSubmit.getFont();
		this.vcSubmit.setFont( font.deriveFont( font.getSize2D() * 1.5f ) );
		this.vcSubmit.setAlignmentX( java.awt.Component.CENTER_ALIGNMENT );
		

		StringBuffer sb = new StringBuffer();
		sb.append( "An exception has been caught:\n\n" );
		sb.append( "  If you were running your program then:\n    it could be either a bug(error) in Alice or your code.\n\n" );
		sb.append( "  If you were building your program then:\n    it is a bug in Alice.\n\n" );
		sb.append( "Please press the \"submit bug report\" button" );
		//sb.append( " and accept our apologies" );
		sb.append( "." );
		javax.swing.JTextArea header = new javax.swing.JTextArea( sb.toString() ) {
			@Override
			public void paint(java.awt.Graphics g) {
				java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
				g2.setRenderingHint( java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
				super.paint( g );
			}
		};
		header.setEditable( false );
		//header.setOpaque( false );
		header.setLineWrap( true );
		header.setWrapStyleWord( true );
		header.setFont( font.deriveFont( font.getSize2D() * 1.15f ) );
		header.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4,8,4,0 ) );

		javax.swing.JPanel headerPane = new javax.swing.JPanel();
		headerPane.setLayout( new java.awt.BorderLayout() );
		headerPane.add( header );
		headerPane.setBorder( javax.swing.BorderFactory.createEtchedBorder() );
		
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 8, 8, 8, 8 ) );
		this.setLayout( new javax.swing.BoxLayout( this, javax.swing.BoxLayout.PAGE_AXIS ) );
		this.add( headerPane );
		this.add( javax.swing.Box.createRigidArea( new java.awt.Dimension( 0, 16 ) ) );
		this.add( reporterPane );
		this.add( javax.swing.Box.createRigidArea( new java.awt.Dimension( 0, 24 ) ) );
		this.add( bugPane );
		this.add( javax.swing.Box.createRigidArea( new java.awt.Dimension( 0, 24 ) ) );
		this.add( expandPane );
		this.add( javax.swing.Box.createRigidArea( new java.awt.Dimension( 0, 16 ) ) );
		this.add( this.vcSubmit );
	}

	private java.awt.Window window;

	public java.awt.Window setWindow() {
		return this.window;
	}
	public void setWindow( java.awt.Window window ) {
		this.window = window;
	}

	private java.util.ArrayList< java.awt.Component[] > createInsightPaneRows() {
		return addInsightPaneRows( new java.util.ArrayList< java.awt.Component[] >() );
	}
	protected java.util.ArrayList< java.awt.Component[] > addInsightPaneRows( java.util.ArrayList< java.awt.Component[] > rv ) {
		javax.swing.JLabel summaryLabel = new javax.swing.JLabel( "summary:", javax.swing.SwingConstants.TRAILING );
		javax.swing.JLabel descriptionLabel = new javax.swing.JLabel( "description:", javax.swing.SwingConstants.TRAILING );
		javax.swing.JLabel stepsToReproduceLabel = new javax.swing.JLabel( "steps:", javax.swing.SwingConstants.TRAILING );
		summaryLabel.setToolTipText( SUMMARY_TEXT );
		descriptionLabel.setToolTipText( DESCRIPTION_TEXT );
		stepsToReproduceLabel.setToolTipText( STEPS_TEXT );

		descriptionLabel.setVerticalAlignment( javax.swing.SwingConstants.TOP );
		stepsToReproduceLabel.setVerticalAlignment( javax.swing.SwingConstants.TOP );

		this.vcDescription.setBorder( javax.swing.BorderFactory.createEmptyBorder() );
		this.vcStepsToReproduce.setBorder( javax.swing.BorderFactory.createEmptyBorder() );
		rv.add( new java.awt.Component[] { summaryLabel, this.vcSummary } );
		rv.add( new java.awt.Component[] { descriptionLabel, new javax.swing.JScrollPane( this.vcDescription ) } );
		rv.add( new java.awt.Component[] { stepsToReproduceLabel, new javax.swing.JScrollPane( this.vcStepsToReproduce ) } );
		return rv;
	}

	private java.util.ArrayList< java.awt.Component[] > createBugPaneRows() {
		return addBugPaneRows( new java.util.ArrayList< java.awt.Component[] >() );
	}
	protected java.util.ArrayList< java.awt.Component[] > addBugPaneRows( java.util.ArrayList< java.awt.Component[] > rv ) {
		javax.swing.JLabel systemLabel = new javax.swing.JLabel( "system:", javax.swing.SwingConstants.TRAILING );
		systemLabel.setVerticalAlignment( javax.swing.SwingConstants.TOP );
		rv.add( new java.awt.Component[] { systemLabel, this.vcSystemPropertiesPane } );
		return rv;
	}

	private java.util.ArrayList< java.awt.Component[] > createReporterPaneRows() {
		return addReporterPaneRows( new java.util.ArrayList< java.awt.Component[] >() );
	}
	protected java.util.ArrayList< java.awt.Component[] > addReporterPaneRows( java.util.ArrayList< java.awt.Component[] > rv ) {
		javax.swing.JLabel nameLabel = new javax.swing.JLabel( "reported by:", javax.swing.SwingConstants.TRAILING );
		javax.swing.JLabel emailLabel = new javax.swing.JLabel( "e-mail address:", javax.swing.SwingConstants.TRAILING );
		nameLabel.setToolTipText( NAME_TEXT );
		emailLabel.setToolTipText( EMAIL_TEXT );
		rv.add( new java.awt.Component[] { nameLabel, this.vcReporterName } );
		rv.add( new java.awt.Component[] { emailLabel, this.vcReporterEMailAddress } );
		return rv;
	}

	public javax.swing.JButton getSubmitButton() {
		return this.vcSubmit;
	}
	@Override
	public java.awt.Dimension getPreferredSize() {
		java.awt.Dimension rv = super.getPreferredSize();
		//rv.width = Math.max( rv.width, 640 );
		rv.width = 480;
		return rv;
	}

	private String getReporterEMailAddress() {
		return this.vcReporterEMailAddress.getText();
	}
	private String getReporterName() {
		return this.vcReporterName.getText();
	}

	protected Issue updateIssue( Issue rv ) {
		rv.setSummary( this.getSummary() );
		rv.setDescription( this.getDescription() );
		rv.setSteps( this.getStepsToReproduce() );
		for( edu.cmu.cs.dennisc.mail.Attachment attachment : this.createAttachments() ) {
			rv.addAttachment( attachment );
		}
		return rv;
	}
	
	
	private String getSubSummary( Issue issue ) {
		String summary = issue.getSummary();
		if( summary != null && summary.length() > 0 ) {
			return summary;
		} else {
			StringBuffer sb = new StringBuffer();
			sb.append( "summary: unspecified; " );
			Throwable throwable = issue.getThrowable();
			if( throwable != null ) {
				sb.append( "exception: " );
				sb.append( throwable.getClass().getName() );
				sb.append( "; " );
				String message = throwable.getMessage();
				if( message != null ) {
					sb.append( "message: " );
					sb.append( message );
					sb.append( "; " );
				}
				//todo: handle PyException
				//			if( throwable instanceof org.python.core.PyException ) {
				//				org.python.core.PyException pyException = new org.python.core.PyException();
				//				sb.append( "frame: " );
				//				sb.append( pyException.traceback.tb_frame );
				//				sb.append( "; line: " );
				//				sb.append( pyException.traceback.tb_lineno );
				//				sb.append( "; " );
				//			} else {
				StackTraceElement[] stackTraceElements = throwable.getStackTrace();
				if( stackTraceElements != null && stackTraceElements.length > 0 && stackTraceElements[ 0 ] != null ) {
					sb.append( "stack[0]: " );
					sb.append( stackTraceElements[ 0 ].toString() );
					sb.append( "; " );
				}
				//			}
			}
			return sb.toString();
		}
	}
	
	protected final String getJIRASummary( Issue issue ) {
		return getSubSummary( issue );
	}
	private StringBuffer updateMailSubject( StringBuffer rv, Issue issue ) {
		rv.append( issue.getAffectsVersionText() );
		rv.append( ": " );
		String summary = issue.getSummary();
		if( summary != null && summary.length() > 0 ) {
			rv.append( summary );
		} else {
			rv.append( this.getSubSummary( issue ) );
		}
		return rv;
	}
	protected final String getMailSubject( Issue issue ) {
		StringBuffer sb = new StringBuffer();
		updateMailSubject( sb, issue );
		return sb.toString();
	}
	protected final String getMailBody( Issue issue ) {
		return "detailed decription:\n" + issue.getDescription() + "\n\nsteps to reproduce:\n" + issue.getSteps() + "\n\nexception:\n" + issue.getExceptionText();
	}
	protected edu.cmu.cs.dennisc.issue.Issue createIssue() {
		return new edu.cmu.cs.dennisc.issue.Issue() {
			@Override
			public String getJIRASummary() {
				return AbstractIssuePane.this.getJIRASummary( this );
			}
			@Override
			public String getMailSubject() {
				return AbstractIssuePane.this.getMailSubject( this );
			}
			@Override
			public String getMailBody() {
				return AbstractIssuePane.this.getMailBody( this );
			}
		};
	}
	private String getSummary() {
		return this.vcSummary.getText();
	}
	private String getDescription() {
		return this.vcDescription.getText();
	}
	private String getStepsToReproduce() {
		return this.vcStepsToReproduce.getText();
	}
	protected java.util.ArrayList< edu.cmu.cs.dennisc.mail.Attachment > updateCriticalAttachments( java.util.ArrayList< edu.cmu.cs.dennisc.mail.Attachment > rv ) {
		rv.add( new edu.cmu.cs.dennisc.mail.Attachment() {
			public javax.activation.DataSource getDataSource() {
				return new javax.mail.util.ByteArrayDataSource( edu.cmu.cs.dennisc.lang.SystemUtilities.getPropertiesAsXMLByteArray(), "application/xml" );
			}
			public String getFileName() {
				return "systemProperties.xml";
			}
		} );
		return rv;
	}
	protected java.util.ArrayList< edu.cmu.cs.dennisc.mail.Attachment > updateBonusAttachments( java.util.ArrayList< edu.cmu.cs.dennisc.mail.Attachment > rv ) {
		return rv;
	}
	protected java.util.ArrayList< edu.cmu.cs.dennisc.mail.Attachment > createAttachments() {
		java.util.ArrayList< edu.cmu.cs.dennisc.mail.Attachment > rv = new java.util.ArrayList< edu.cmu.cs.dennisc.mail.Attachment >();
		updateCriticalAttachments( rv );
		try {
			java.util.ArrayList< edu.cmu.cs.dennisc.mail.Attachment > bonus = updateBonusAttachments( new java.util.ArrayList< edu.cmu.cs.dennisc.mail.Attachment >() );
			rv.addAll( bonus );
		} catch( Throwable t ) {
			//todo:
			t.printStackTrace();
		}
		return rv;
	}

	protected abstract String getJIRAServer();
	protected abstract String getMailServer();
	protected abstract String getMailRecipient();
	protected abstract edu.cmu.cs.dennisc.jira.Authenticator getJIRAAuthenticator();
	protected abstract edu.cmu.cs.dennisc.mail.AbstractAuthenticator getMailAuthenticator();

	private boolean isSubmitAttempted = false;
	private boolean isSubmitSuccessful = false;
	private boolean isSubmitBackgrounded = false;

	public boolean isSubmitAttempted() {
		return this.isSubmitAttempted;
	}
	public boolean isSubmitBackgrounded() {
		return this.isSubmitBackgrounded;
	}
	public boolean isSubmitSuccessful() {
		return this.isSubmitSuccessful;
	}
	protected boolean submit() {
		Issue issue = this.createIssue();
		updateIssue( issue );
		ProgressPane progressPane = new ProgressPane();
		final String RPC_PATH = "/rpc/xmlrpc";
		java.net.URL jiraURL;
		try {
			jiraURL = new java.net.URL( this.getJIRAServer() + RPC_PATH );
		} catch( java.net.MalformedURLException murl ) {
			throw new RuntimeException( murl );
		}
		progressPane.initializeAndExecuteWorker( issue, jiraURL, this.getJIRAAuthenticator(), this.getMailServer(), this.getMailAuthenticator(), this.getReporterEMailAddress(), this.getReporterName(), this.getMailRecipient() );

		this.isSubmitBackgrounded = false;
		javax.swing.JFrame frame = new javax.swing.JFrame();
		javax.swing.JDialog dialog = new javax.swing.JDialog( frame, "Uploading Bug Report", true );
		dialog.addWindowListener( new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing( java.awt.event.WindowEvent e ) {
				AbstractIssuePane.this.isSubmitBackgrounded = true;
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
		
		return progressPane.isSuccessful();
	}
}
