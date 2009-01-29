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
package edu.cmu.cs.dennisc.ui.bugreport;

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
		rv.height = Math.max( rv.height, 100 );
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
public abstract class AbstractBugReportPane extends javax.swing.JPanel {
	private static javax.swing.JLabel createSystemPropertyLabel( String propertyName ) {
		return new javax.swing.JLabel( propertyName + ": " + System.getProperty( propertyName ) );
	}
	class SystemPropertiesPane extends javax.swing.JPanel {
		class ShowAllSystemPropertiesAction extends javax.swing.AbstractAction {
			public ShowAllSystemPropertiesAction() {
				super( "show all system properties..." );
			}
			public void actionPerformed( java.awt.event.ActionEvent e ) {
				edu.cmu.cs.dennisc.swing.SwingUtilities.showMessageDialogInScrollableUneditableTextArea( AbstractBugReportPane.this, edu.cmu.cs.dennisc.lang.SystemUtilities.getPropertiesAsXMLString(), "System Properties", javax.swing.JOptionPane.INFORMATION_MESSAGE );
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

	private static final String SYNOPSIS_TEXT = "please fill in a one line synopsis of the bug";
	private static final String DESCRIPTION_TEXT = "please fill in a detailed description of the bug";
	private static final String STEPS_TEXT = "please fill in the steps required to reproduce the bug";
	private SuggestiveTextField vcSynopsis = new SuggestiveTextField( "", SYNOPSIS_TEXT );
	private SuggestiveTextArea vcDecription = new SuggestiveTextArea( "", DESCRIPTION_TEXT );
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
			AbstractBugReportPane.this.submit();
			if( AbstractBugReportPane.this.window != null ) {
				AbstractBugReportPane.this.window.setVisible( false );
			}
		}
	}
	private javax.swing.JButton vcSubmit = new javax.swing.JButton( new SubmitAction() );

	public AbstractBugReportPane() {
		javax.swing.JPanel bugPane = new javax.swing.JPanel();
		//bugPane.setBorder( javax.swing.BorderFactory.createTitledBorder( "about the bug" ) );
		javax.swing.JPanel reporterPane = new javax.swing.JPanel();
		//reporterPane.setBorder( javax.swing.BorderFactory.createTitledBorder( "about you" ) );
		edu.cmu.cs.dennisc.swing.SpringUtilities.springItUpANotch( bugPane, createBugPaneRows(), 8, 4 );
		edu.cmu.cs.dennisc.swing.SpringUtilities.springItUpANotch( reporterPane, createReporterPaneRows(), 8, 4 );

		//this.vcSubmit.setBackground( java.awt.Color.GREEN.brighter() );
		java.awt.Font font = this.vcSubmit.getFont();
		this.vcSubmit.setFont( font.deriveFont( font.getSize() * 1.5f ) );
		this.vcSubmit.setAlignmentX( java.awt.Component.CENTER_ALIGNMENT );
		
		this.setLayout( new javax.swing.BoxLayout( this, javax.swing.BoxLayout.PAGE_AXIS ) );
		this.add( bugPane );
		this.add( javax.swing.Box.createRigidArea( new java.awt.Dimension( 0, 40 ) ) );
		this.add( reporterPane );
		this.add( javax.swing.Box.createRigidArea( new java.awt.Dimension( 0, 40 ) ) );
		this.add( this.vcSubmit );
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 8, 8, 8, 8 ) );
	}
	
	private java.awt.Window window;
	public java.awt.Window setWindow() {
		return this.window;
	}
	public void setWindow( java.awt.Window  window ) {
		this.window = window;
	}

	private java.util.ArrayList< java.awt.Component[] > createBugPaneRows() {
		return addBugPaneRows( new java.util.ArrayList< java.awt.Component[] >() );
	}
	protected java.util.ArrayList< java.awt.Component[] > addBugPaneRows( java.util.ArrayList< java.awt.Component[] > rv ) { 
		javax.swing.JLabel synopsisLabel = new javax.swing.JLabel( "synopsis:", javax.swing.SwingConstants.TRAILING );
		javax.swing.JLabel descriptionLabel = new javax.swing.JLabel( "description:", javax.swing.SwingConstants.TRAILING );
		javax.swing.JLabel stepsToReproduceLabel = new javax.swing.JLabel( "steps:", javax.swing.SwingConstants.TRAILING );
		javax.swing.JLabel exceptionLabel = new javax.swing.JLabel( "exception:", javax.swing.SwingConstants.TRAILING );
		javax.swing.JLabel systemLabel = new javax.swing.JLabel( "system:", javax.swing.SwingConstants.TRAILING );
		
		synopsisLabel.setToolTipText( SYNOPSIS_TEXT );
		descriptionLabel.setToolTipText( DESCRIPTION_TEXT );
		stepsToReproduceLabel.setToolTipText( STEPS_TEXT );

		descriptionLabel.setVerticalAlignment( javax.swing.SwingConstants.TOP );
		stepsToReproduceLabel.setVerticalAlignment( javax.swing.SwingConstants.TOP );
		exceptionLabel.setVerticalAlignment( javax.swing.SwingConstants.TOP );
		systemLabel.setVerticalAlignment( javax.swing.SwingConstants.TOP );
		rv.add( new java.awt.Component[] { synopsisLabel, this.vcSynopsis } ); 
		rv.add( new java.awt.Component[] { descriptionLabel, this.vcDecription } ); 
		rv.add( new java.awt.Component[] { stepsToReproduceLabel, this.vcStepsToReproduce } ); 
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
		rv.width = Math.max( rv.width, 640 );
		return rv;
	}
	
	private java.lang.String getReplyTo() {
		String address = this.vcReporterEMailAddress.getText();
		return address;
	}
	private java.lang.String getReplyToPersonal() {
		String address = this.vcReporterName.getText();
		return address;
	}

	protected abstract String getHost();
	protected abstract String getTo();
	
	protected StringBuffer updateSubject( StringBuffer rv ) {
		rv.append( this.vcSynopsis.getText() );
		return rv;
	}
	private String getSubject() {
		StringBuffer sb = new StringBuffer();
		updateSubject( sb );
		return sb.toString();
	}
	protected java.lang.String getBody() {
		return "detailed decription:\n" + this.vcDecription.getText() + "\n\nsteps to reproduce:\n" + this.vcStepsToReproduce.getText();
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
	
	protected abstract edu.cmu.cs.dennisc.mail.AbstractAuthenticator getAuthenticator();
	
	private boolean isSubmitAttempted = false;
	private boolean isSubmitSuccessful = false;
	public boolean isSubmitAttempted() { 
		return this.isSubmitAttempted;
	}
	public boolean isSubmitSuccessful() { 
		return this.isSubmitSuccessful;
	}
	protected abstract boolean isTransportLayerSecurityDesired();
	private void submit() {
		this.isSubmitAttempted = true;
		//for( boolean isSecureDesired : new boolean[] { false, true } ) {
		for( int port : new int[] { 25, /*567, 465,*/ 80 } ) {
			try {
				edu.cmu.cs.dennisc.mail.MailUtilities.sendMail( this.isTransportLayerSecurityDesired(), port, getHost(), getAuthenticator(), getReplyTo(), getReplyToPersonal(), getTo(), getSubject(), getBody(), createAttachments() );
				this.isSubmitSuccessful = true;
				break;
			} catch( javax.mail.MessagingException me ) {
				me.printStackTrace();
			}
		}
	}
}
