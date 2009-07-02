package edu.cmu.cs.dennisc.toolkit.login;

class PasswordPane extends swing.PageAxisPane {
	private static final String HIDDEN_KEY = "HIDDEN_KEY";
	private static final String EXPOSED_KEY = "EXPOSED_KEY";

	class PasswordCardPane extends swing.CardPane {
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

class LogInPane extends swing.PageAxisPane {
	class TestLogInOperation extends zoot.AbstractActionOperation {
		public TestLogInOperation() {
			this.putValue( javax.swing.Action.NAME, "Log In" );
		}
		public void perform( zoot.ActionContext actionContext ) {
			try {
				com.atlassian.jira.rpc.soap.client.JiraSoapServiceServiceLocator jiraSoapServiceLocator = new com.atlassian.jira.rpc.soap.client.JiraSoapServiceServiceLocator();
				com.atlassian.jira.rpc.soap.client.JiraSoapService service = jiraSoapServiceLocator.getJirasoapserviceV2( new java.net.URL( "http://bugs.alice.org:8080/rpc/soap/jirasoapservice-v2" ) );
				String username = textUsername.getText();
				try {
					String token = service.login( username, passwordPane.getPassword() );
					try {
						com.atlassian.jira.rpc.soap.client.RemoteUser remoteUser = service.getUser( token, username );
						StringBuffer sb = new StringBuffer();
//						sb.append( "name: " );
//						sb.append( remoteUser.getName() );
//						sb.append( "\n" );
						sb.append( "full name: " );
						sb.append( remoteUser.getFullname() );
//						sb.append( "\n" );
//						sb.append( "email: " );
//						sb.append( remoteUser.getEmail() );
//						sb.append( "\n" );
						javax.swing.JOptionPane.showMessageDialog( null, sb.toString() );
					} finally {
						service.logout( token );
					}
				} catch( com.atlassian.jira.rpc.soap.client.RemoteAuthenticationException rae ) {
					javax.swing.JOptionPane.showMessageDialog( null, rae.getLocalizedMessage() );
				}
			} catch( Exception e ) {
				throw new RuntimeException( e );
			}
		}
	}

	private zoot.ZTextField textUsername = new zoot.ZTextField();
	private PasswordPane passwordPane = new PasswordPane();
	private zoot.ZButton logInButton = new zoot.ZButton( new TestLogInOperation() );
	private java.awt.Component createLabel( String text ) {
		zoot.ZLabel rv = zoot.ZLabel.acquire( text );
		rv.setVerticalAlignment( javax.swing.SwingConstants.TOP );
		rv.setHorizontalAlignment( javax.swing.SwingConstants.TRAILING );
		return rv;
	}
	public LogInPane() {
		swing.RowsSpringPane rowsPane = new swing.RowsSpringPane( 8, 4 ) {
			@Override
			protected java.util.List< java.awt.Component[] > addComponentRows( java.util.List< java.awt.Component[] > rv ) {
				rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( createLabel( "Username:" ), textUsername ) );
				rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( createLabel( "Password:" ), passwordPane ) );
				return rv;
			}
		};

		swing.Pane signUpPane = new swing.Pane();
		signUpPane.add( zoot.ZLabel.acquire( "Not a member?" ) );
		edu.cmu.cs.dennisc.toolkit.hyperlink.HyperlinkOperation hyperlinkOperation = new edu.cmu.cs.dennisc.toolkit.hyperlink.HyperlinkOperation( "http://bugs.alice.org:8080/secure/Signup!default.jspa", "Sign up" );
		signUpPane.add( new zoot.ZHyperlink( hyperlinkOperation ) );
		signUpPane.add( zoot.ZLabel.acquire( "for an account." ) );

		swing.Pane buttonPane = new swing.Pane();
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
	
	public zoot.ZButton getLogInButton() {
		return this.logInButton;
	}
}


public class LogInStatusPane extends swing.CardPane {
	class LogInOperation extends zoot.AbstractActionOperation {
		public LogInOperation() {
			this.putValue( javax.swing.Action.NAME, "Log In... (Optional)" );
		}
		public void perform( zoot.ActionContext actionContext ) {
			LogInPane pane = new LogInPane();
			java.awt.Component owner = this.getSourceComponent( actionContext );
			javax.swing.JDialog dialog = edu.cmu.cs.dennisc.swing.JDialogUtilities.createPackedJDialog( pane, owner, "Log In", true, javax.swing.WindowConstants.DISPOSE_ON_CLOSE );
			edu.cmu.cs.dennisc.awt.WindowUtilties.setLocationOnScreenToCenteredWithin( dialog, javax.swing.SwingUtilities.getRoot( this.getSourceComponent( actionContext ) ) );
			dialog.getRootPane().setDefaultButton( pane.getLogInButton() );
			dialog.setVisible( true );
			LogInStatusPane.this.show( ON_KEY );
		}
	}

	class LogOutOperation extends zoot.AbstractActionOperation {
		public LogOutOperation() {
			this.putValue( javax.swing.Action.NAME, "Log Out" );
		}
		public void perform( zoot.ActionContext actionContext ) {
			LogInStatusPane.this.show( OFF_KEY );
		}
	}

	private static final String OFF_KEY = "OFF_KEY";
	private static final String ON_KEY = "ON_KEY";
	private zoot.ZButton logInButton = new zoot.ZButton( new LogInOperation() );
	private zoot.ZButton logOutButton = new zoot.ZButton( new LogOutOperation() );

	class OffPane extends swing.LineAxisPane {
		public OffPane() {
			this.add( javax.swing.Box.createHorizontalGlue() );
			this.add( logInButton );
		}
	}
	class OnPane extends swing.LineAxisPane {
		public OnPane() {
			javax.swing.JLabel nameLabel = new javax.swing.JLabel( "Pedro Estrian" ) {
				@Override
				public java.awt.Dimension getMaximumSize() {
					return this.getPreferredSize();
				}
			};
			nameLabel.setForeground( java.awt.Color.WHITE );
			this.add( javax.swing.Box.createHorizontalGlue() );
			this.add( nameLabel );
			this.add( javax.swing.Box.createHorizontalStrut( 8 ) );
			this.add( logOutButton );
		}
	}
	public LogInStatusPane() {
		this.add( new OffPane(), OFF_KEY );
		this.add( new OnPane(), ON_KEY );
	}

//	@Override
//	public void show( java.lang.String key ) {
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
