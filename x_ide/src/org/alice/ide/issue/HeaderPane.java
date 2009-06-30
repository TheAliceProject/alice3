package org.alice.ide.issue;

class LogInPane extends swing.RowsSpringPane {
	public LogInPane() {
		super( 0, 0 );
	}
	@Override
	protected java.util.List< java.awt.Component[] > addComponentRows( java.util.List< java.awt.Component[] > rv ) {
		return null;
	}
}

class LogInOperation extends zoot.AbstractActionOperation {
	public LogInOperation() {
		this.putValue( javax.swing.Action.NAME, "Log In (Optional)" );
	}
	public void perform( zoot.ActionContext actionContext ) {
		Object source = actionContext.getEvent().getSource();
		java.awt.Component parentComponent = edu.cmu.cs.dennisc.lang.ClassUtilities.getInstance( source, java.awt.Component.class );
		javax.swing.JOptionPane.showMessageDialog( parentComponent, "Coming Soon" );
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
		javax.swing.JFrame frame = org.alice.ide.IDE.getSingleton();
		if( frame != null ) {
			//pass
		} else {
			frame = new javax.swing.JFrame();
		}
		javax.swing.JDialog window = new javax.swing.JDialog( frame, "Report Issue", true );
		window.getContentPane().add( pane );
		window.pack();
		window.setDefaultCloseOperation( javax.swing.JFrame.DISPOSE_ON_CLOSE );
		window.setVisible( true );
	}
}
