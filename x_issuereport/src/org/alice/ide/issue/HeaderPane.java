package org.alice.ide.issue;

class PasswordPane extends swing.PageAxisPane {
	private static final String HIDDEN_KEY = "HIDDEN_KEY";
	private static final String EXPOSED_KEY = "EXPOSED_KEY";
	class PasswordCardPane extends swing.CardPane {
		private javax.swing.JPasswordField hidden = new javax.swing.JPasswordField(){
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
			this.isExposed = ! this.isExposed;
		}
	}
	private PasswordCardPane passwordCardPane = new PasswordCardPane();
	private javax.swing.JCheckBox checkBox = new javax.swing.JCheckBox( "display password" );
	
	public PasswordPane() {
		checkBox.addActionListener( new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				passwordCardPane.toggle();
			}
		} );
		
		//this.setLayout( new java.awt.GridLayout( 2, 1 ) );
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
			javax.swing.JOptionPane.showMessageDialog( this.getSourceComponent( actionContext ), "Coming soon" );
		}
	}
	
	private PasswordPane passwordPane = new PasswordPane();
	
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
				rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( createLabel( "Username:" ), new zoot.ZTextField() ) );
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
		buttonPane.add( new zoot.ZButton( new TestLogInOperation() ) );

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
}

class LogInOperation extends zoot.AbstractActionOperation {
	public LogInOperation() {
		this.putValue( javax.swing.Action.NAME, "Log In... (Optional)" );
	}
	public void perform( zoot.ActionContext actionContext ) {
		LogInPane pane = new LogInPane();
//		javax.swing.JFrame frame = org.alice.ide.IDE.getSingleton();
//		if( frame != null ) {
//			//pass
//		} else {
//			frame = new javax.swing.JFrame();
//		}
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: getIDE()" );
		javax.swing.JFrame frame = new javax.swing.JFrame();
		javax.swing.JDialog window = new javax.swing.JDialog( frame, "Log In", true );
		window.getContentPane().add( pane );
		window.pack();
		window.setDefaultCloseOperation( javax.swing.JFrame.DISPOSE_ON_CLOSE );
		
		edu.cmu.cs.dennisc.awt.WindowUtilties.setLocationOnScreenToCenteredWithin( window, javax.swing.SwingUtilities.getRoot( this.getSourceComponent( actionContext ) ) );
		window.setVisible( true );
	}
}

class LogOutOperation extends zoot.AbstractActionOperation {
	public LogOutOperation() {
		this.putValue( javax.swing.Action.NAME, "Log Out" );
	}
	public void perform( zoot.ActionContext actionContext ) {
	}
}

class LogInStatusPane extends swing.CardPane {
	private static final String LOG_IN_KEY = "LOG_IN_KEY";
	private static final String LOG_OUT_KEY = "LOG_OUT_KEY";
	private zoot.ZButton logInButton = new zoot.ZButton( new LogInOperation() );
	private zoot.ZButton logOutButton = new zoot.ZButton( new LogOutOperation() );

	public LogInStatusPane() {
		this.add( this.logInButton, LOG_IN_KEY );
		this.add( this.logOutButton, LOG_OUT_KEY );
	}
}

public class HeaderPane extends swing.LineAxisPane {
	public HeaderPane() {
		this.setBackground( java.awt.Color.DARK_GRAY );
		this.setOpaque( true );

		edu.cmu.cs.dennisc.swing.Hyperlink hyperlink = new edu.cmu.cs.dennisc.swing.Hyperlink( "http://bugs.alice.org:8080/" );
		hyperlink.setIcon( new javax.swing.ImageIcon( HeaderPane.class.getResource( "images/logo.png" ) ) );
		hyperlink.setDefaultColor( java.awt.Color.LIGHT_GRAY );
		hyperlink.setArmedColor( java.awt.Color.YELLOW );
		hyperlink.setForeground( hyperlink.getDefaultColor() );
		hyperlink.setVerticalTextPosition( javax.swing.SwingConstants.BOTTOM );
		hyperlink.setHorizontalTextPosition( javax.swing.SwingConstants.CENTER );
		this.add( hyperlink );
		this.add( javax.swing.Box.createHorizontalGlue() );
		this.add( new LogInStatusPane() );
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 2, 6, 8, 12 ) );
	}

	public static void main( String[] args ) {
		HeaderPane pane = new HeaderPane();
		//LogInPane pane = new LogInPane();
		//PasswordPane pane = new PasswordPane();
		javax.swing.JDialog dialog = edu.cmu.cs.dennisc.swing.JDialogUtilities.createPackedJDialog( pane, null, "", true, javax.swing.WindowConstants.EXIT_ON_CLOSE );
		edu.cmu.cs.dennisc.awt.WindowUtilties.setLocationOnScreenToCenteredWithin( dialog, null );
		dialog.setVisible( true );
	}
}
