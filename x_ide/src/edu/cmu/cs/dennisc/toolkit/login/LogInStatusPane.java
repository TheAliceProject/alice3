package edu.cmu.cs.dennisc.toolkit.login;

class PasswordPane extends edu.cmu.cs.dennisc.croquet.swing.PageAxisPane {
	private static final String HIDDEN_KEY = "HIDDEN_KEY";
	private static final String EXPOSED_KEY = "EXPOSED_KEY";

	class PasswordCardPane extends edu.cmu.cs.dennisc.croquet.swing.CardPane {
		private javax.swing.JPasswordField hidden = new javax.swing.JPasswordField() {
			@Override
			public java.awt.Dimension getPreferredSize() {
				return edu.cmu.cs.dennisc.awt.DimensionUtilties.constrainToMinimumWidth( super.getPreferredSize(), 256 );
			}
		};
		private javax.swing.JTextField exposed = new javax.swing.JTextField();
		private boolean isExposed = false;

		public PasswordCardPane() {
			this.add( hidden, HIDDEN_KEY );
			this.add( exposed, EXPOSED_KEY );
			this.exposed.setDocument( this.hidden.getDocument() );
		}
		public void toggle() {
			if( this.isExposed ) {
				this.show( HIDDEN_KEY );
				this.hidden.requestFocus();
			} else {
				this.show( EXPOSED_KEY );
				this.exposed.requestFocus();
			}
			this.isExposed = !this.isExposed;
		}
	}

	private PasswordCardPane passwordCardPane = new PasswordCardPane();
	private javax.swing.JCheckBox checkBox = new javax.swing.JCheckBox( "display password" );

	public PasswordPane() {
		checkBox.addActionListener( new java.awt.event.ActionListener() {
			public void actionPerformed( java.awt.event.ActionEvent e ) {
				passwordCardPane.toggle();
			}
		} );
		this.add( passwordCardPane );
		this.add( checkBox );
	}
	public String getPassword() {
		return passwordCardPane.exposed.getText();
	}
}

class LogInPane extends edu.cmu.cs.dennisc.croquet.swing.PageAxisPane {
	class TestLogInOperation extends edu.cmu.cs.dennisc.zoot.InconsequentialActionOperation {
		public TestLogInOperation() {
			this.putValue( javax.swing.Action.NAME, "Log In" );
		}
		@Override
		protected void performInternal( edu.cmu.cs.dennisc.zoot.ActionContext actionContext ) {
			try {
				com.atlassian.jira.rpc.soap.client.JiraSoapServiceServiceLocator jiraSoapServiceLocator = new com.atlassian.jira.rpc.soap.client.JiraSoapServiceServiceLocator();
				com.atlassian.jira.rpc.soap.client.JiraSoapService service = jiraSoapServiceLocator.getJirasoapserviceV2( new java.net.URL( "http://bugs.alice.org:8080/rpc/soap/jirasoapservice-v2" ) );
				String username = textUsername.getText();
				try {
					String password = passwordPane.getPassword();
					String token = service.login( username, password );
					try {
						com.atlassian.jira.rpc.soap.client.RemoteUser remoteUser = service.getUser( token, username );
						edu.cmu.cs.dennisc.login.AccountManager.logIn( LogInStatusPane.BUGS_ALICE_ORG_KEY, username, password, remoteUser.getFullname() );
					} finally {
						service.logout( token );
					}
					javax.swing.SwingUtilities.getRoot( LogInPane.this ).setVisible( false );
				} catch( com.atlassian.jira.rpc.soap.client.RemoteAuthenticationException rae ) {
					javax.swing.JOptionPane.showMessageDialog( null, rae );
					//edu.cmu.cs.dennisc.account.AccountManager.logOut( BUGS_ALICE_ORG_KEY );
				}
			} catch( Exception e ) {
				throw new RuntimeException( e );
			}
		}
	}

	private edu.cmu.cs.dennisc.zoot.ZTextField textUsername = new edu.cmu.cs.dennisc.zoot.ZTextField();
	private PasswordPane passwordPane = new PasswordPane();
	//todo: remove. rely only on operations.
	private javax.swing.JButton logInButton = edu.cmu.cs.dennisc.zoot.ZManager.createButton( new TestLogInOperation() );
	private java.awt.Component createLabel( String text ) {
		javax.swing.JLabel rv = edu.cmu.cs.dennisc.croquet.LabelUtilities.createLabel( text );
		rv.setVerticalAlignment( javax.swing.SwingConstants.TOP );
		rv.setHorizontalAlignment( javax.swing.SwingConstants.TRAILING );
		return rv;
	}
	public LogInPane() {
		edu.cmu.cs.dennisc.croquet.swing.RowsSpringPane rowsPane = new edu.cmu.cs.dennisc.croquet.swing.RowsSpringPane( 8, 4 ) {
			@Override
			protected java.util.List< java.awt.Component[] > addComponentRows( java.util.List< java.awt.Component[] > rv ) {
				rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( createLabel( "Username:" ), textUsername ) );
				rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( createLabel( "Password:" ), passwordPane ) );
				return rv;
			}
		};

		edu.cmu.cs.dennisc.croquet.swing.Pane signUpPane = new edu.cmu.cs.dennisc.croquet.swing.Pane();
		signUpPane.add( edu.cmu.cs.dennisc.croquet.LabelUtilities.createLabel( "Not a member?" ) );
		edu.cmu.cs.dennisc.toolkit.hyperlink.HyperlinkOperation hyperlinkOperation = new edu.cmu.cs.dennisc.toolkit.hyperlink.HyperlinkOperation( "http://bugs.alice.org:8080/secure/Signup!default.jspa", "Sign up" );
		signUpPane.add( edu.cmu.cs.dennisc.zoot.ZManager.createHyperlink( hyperlinkOperation ) );
		signUpPane.add( edu.cmu.cs.dennisc.croquet.LabelUtilities.createLabel( "for an account." ) );

		edu.cmu.cs.dennisc.croquet.swing.Pane buttonPane = new edu.cmu.cs.dennisc.croquet.swing.Pane();
		buttonPane.add( this.logInButton );

		signUpPane.setAlignmentX( javax.swing.JComponent.CENTER_ALIGNMENT );
		rowsPane.setAlignmentX( javax.swing.JComponent.CENTER_ALIGNMENT );
		buttonPane.setAlignmentX( javax.swing.JComponent.CENTER_ALIGNMENT );

		this.add( signUpPane );
		this.add( javax.swing.Box.createVerticalStrut( 32 ) );
		this.add( rowsPane );
		this.add( javax.swing.Box.createVerticalStrut( 6 ) );
		this.add( buttonPane );

		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 8, 32, 8, 32 ) );
	}
	
	public javax.swing.JButton getLogInButton() {
		return this.logInButton;
	}
}


public class LogInStatusPane extends edu.cmu.cs.dennisc.croquet.swing.CardPane {
	public static final String BUGS_ALICE_ORG_KEY = "bugs.alice.org";
	class LogInOperation extends edu.cmu.cs.dennisc.zoot.InconsequentialActionOperation {
		public LogInOperation() {
			this.putValue( javax.swing.Action.NAME, "Log In... (Optional)" );
		}
		@Override
		protected void performInternal( edu.cmu.cs.dennisc.zoot.ActionContext actionContext ) {
			LogInPane pane = new LogInPane();
			java.awt.Component owner = this.getSourceComponent( actionContext );
			javax.swing.JDialog dialog = edu.cmu.cs.dennisc.swing.JDialogUtilities.createPackedJDialog( pane, owner, "Log In", true, javax.swing.WindowConstants.DISPOSE_ON_CLOSE );
			edu.cmu.cs.dennisc.awt.WindowUtilties.setLocationOnScreenToCenteredWithin( dialog, javax.swing.SwingUtilities.getRoot( owner ) );
			dialog.getRootPane().setDefaultButton( pane.getLogInButton() );
			dialog.setVisible( true );
			edu.cmu.cs.dennisc.login.AccountInformation accountInformation = edu.cmu.cs.dennisc.login.AccountManager.get( LogInStatusPane.BUGS_ALICE_ORG_KEY );
			if( accountInformation != null ) {
				LogInStatusPane.this.onPane.refresh();
				LogInStatusPane.this.show( ON_KEY );
			}
		}
	}

	class LogOutOperation extends edu.cmu.cs.dennisc.zoot.InconsequentialActionOperation {
		public LogOutOperation() {
			this.putValue( javax.swing.Action.NAME, "Log Out" );
		}
		@Override
		protected void performInternal( edu.cmu.cs.dennisc.zoot.ActionContext actionContext ) {
			edu.cmu.cs.dennisc.login.AccountManager.logOut( LogInStatusPane.BUGS_ALICE_ORG_KEY );
			LogInStatusPane.this.show( OFF_KEY );
		}
	}

	private static final String OFF_KEY = "OFF_KEY";
	private static final String ON_KEY = "ON_KEY";
	private javax.swing.JButton logInButton = edu.cmu.cs.dennisc.zoot.ZManager.createButton( new LogInOperation() );
	private javax.swing.JButton logOutButton = edu.cmu.cs.dennisc.zoot.ZManager.createButton( new LogOutOperation() );

	class OffPane extends edu.cmu.cs.dennisc.croquet.swing.LineAxisPane {
		public OffPane() {
			this.add( javax.swing.Box.createHorizontalGlue() );
			this.add( logInButton );
		}
	}
	class OnPane extends edu.cmu.cs.dennisc.croquet.swing.LineAxisPane {
		private javax.swing.JLabel nameLabel = new javax.swing.JLabel( "Full Name" ) {
			@Override
			public java.awt.Dimension getPreferredSize() {
				return edu.cmu.cs.dennisc.awt.DimensionUtilties.constrainToMinimumWidth( super.getPreferredSize(), 320 );
			}
			@Override
			public java.awt.Dimension getMaximumSize() {
				return this.getPreferredSize();
			}
		};
		public OnPane() {
			this.refresh();
			this.nameLabel.setHorizontalAlignment( javax.swing.SwingConstants.TRAILING );
			this.nameLabel.setForeground( java.awt.Color.WHITE );
			this.add( javax.swing.Box.createHorizontalGlue() );
			this.add( this.nameLabel );
			this.add( javax.swing.Box.createHorizontalStrut( 8 ) );
			this.add( logOutButton );
		}
		public void refresh() {
			edu.cmu.cs.dennisc.login.AccountInformation accountInformation = edu.cmu.cs.dennisc.login.AccountManager.get( LogInStatusPane.BUGS_ALICE_ORG_KEY );
			if( accountInformation != null ) {
				this.nameLabel.setText( accountInformation.getFullName() );
				this.revalidate();
			}
		}
	}
	private OffPane offPane = new OffPane();
	private OnPane onPane = new OnPane();
	public LogInStatusPane() {
		this.add( this.offPane, OFF_KEY );
		this.add( this.onPane, ON_KEY );
		edu.cmu.cs.dennisc.login.AccountInformation accountInformation = edu.cmu.cs.dennisc.login.AccountManager.get( LogInStatusPane.BUGS_ALICE_ORG_KEY );
		if( accountInformation != null ) {
			LogInStatusPane.this.show( ON_KEY );
		}
	}

//	@Override
//	public void show( String key ) {
//		super.show( key );
//		this.revalidate();
//	}
	public static void main( String[] args ) {
		LogInStatusPane pane = new LogInStatusPane();
		//LogInPane pane = new LogInPane();
		//PasswordPane pane = new PasswordPane();
		javax.swing.JDialog dialog = edu.cmu.cs.dennisc.swing.JDialogUtilities.createPackedJDialog( pane, null, "", true, javax.swing.WindowConstants.DISPOSE_ON_CLOSE );
		dialog.getContentPane().setBackground( java.awt.Color.DARK_GRAY );
		dialog.setLocation( 200, 200 );
		dialog.setVisible( true );
	}
}
