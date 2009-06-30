package org.alice.ide.issue;

class LogInPane extends swing.BorderPane {
	class TestLogInOperation extends zoot.AbstractActionOperation {
		public TestLogInOperation() {
			this.putValue( javax.swing.Action.NAME, "Log In" );
		}
		public void perform( zoot.ActionContext actionContext ) {
			javax.swing.JOptionPane.showMessageDialog( this.getSourceComponent( actionContext ), "Coming soon" );
		}
	}
	public LogInPane() {
		swing.RowsSpringPane rowsPane = new swing.RowsSpringPane( 4, 4 ) {
			@Override
			protected java.util.List< java.awt.Component[] > addComponentRows( java.util.List< java.awt.Component[] > rv ) {
				rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( zoot.ZLabel.acquire( "username:" ), new zoot.ZTextField() ) );
				rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( zoot.ZLabel.acquire( "password:" ), new zoot.ZTextField() ) );
				return rv;
			}
		};

		swing.Pane signUpPane = new swing.Pane();
		signUpPane.add( zoot.ZLabel.acquire( "Not a member?" ) );
		org.alice.ide.operations.hyperlink.HyperlinkOperation hyperlinkOperation = new org.alice.ide.operations.hyperlink.HyperlinkOperation( "http://bugs.alice.org:8080/secure/Signup!default.jspa", "Sign up" );
		signUpPane.add( new zoot.ZHyperlink( hyperlinkOperation ) );
		signUpPane.add( zoot.ZLabel.acquire( "for an account." ) );

		swing.Pane buttonPane = new swing.Pane();
		buttonPane.add( new zoot.ZButton( new TestLogInOperation() ) );

		this.add( rowsPane, java.awt.BorderLayout.NORTH );
		this.add( buttonPane, java.awt.BorderLayout.CENTER );
		this.add( signUpPane, java.awt.BorderLayout.SOUTH );

		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 8, 8, 8, 8 ) );
	}
}

class LogInOperation extends zoot.AbstractActionOperation {
	public LogInOperation() {
		this.putValue( javax.swing.Action.NAME, "Log In... (Optional)" );
	}
	public void perform( zoot.ActionContext actionContext ) {
		LogInPane pane = new LogInPane();
		javax.swing.JFrame frame = org.alice.ide.IDE.getSingleton();
		if( frame != null ) {
			//pass
		} else {
			frame = new javax.swing.JFrame();
		}
		javax.swing.JDialog window = new javax.swing.JDialog( frame, "Log In", true );
		window.getContentPane().add( pane );
		window.pack();
		window.setDefaultCloseOperation( javax.swing.JFrame.DISPOSE_ON_CLOSE );
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

class LogInStatusPane extends swing.LineAxisPane {
	private zoot.ZButton logInButton = new zoot.ZButton( new LogInOperation() );
	private zoot.ZButton logOutButton = new zoot.ZButton( new LogOutOperation() );

	public LogInStatusPane() {
		this.add( this.logInButton );
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
		//this.add( javax.swing.Box.createHorizontalStrut( 12 ) );
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 2, 6, 8, 12 ) );
	}

	public static void main( String[] args ) {
		HeaderPane pane = new HeaderPane();
		//LogInPane pane = new LogInPane();
		javax.swing.JFrame frame = org.alice.ide.IDE.getSingleton();
		if( frame != null ) {
			//pass
		} else {
			frame = new javax.swing.JFrame();
		}
		javax.swing.JDialog window = new javax.swing.JDialog( frame, "", true );
		window.getContentPane().add( pane );
		window.pack();
		window.setDefaultCloseOperation( javax.swing.JFrame.DISPOSE_ON_CLOSE );
		window.setVisible( true );
	}
}
