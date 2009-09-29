package org.alice.ide.issue;

public class HeaderPane extends edu.cmu.cs.dennisc.croquet.LineAxisPane {
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
		this.add( javax.swing.Box.createHorizontalStrut( 32 ) );
		this.add( javax.swing.Box.createHorizontalGlue() );
		this.add( new edu.cmu.cs.dennisc.toolkit.login.LogInStatusPane() );
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 2, 6, 8, 12 ) );
	}

	public static void main( String[] args ) {
		HeaderPane pane = new HeaderPane();
		//LogInPane pane = new LogInPane();
		//PasswordPane pane = new PasswordPane();
		javax.swing.JDialog dialog = edu.cmu.cs.dennisc.swing.JDialogUtilities.createPackedJDialog( pane, null, "", true, javax.swing.WindowConstants.DISPOSE_ON_CLOSE );
		edu.cmu.cs.dennisc.awt.WindowUtilties.setLocationOnScreenToCenteredWithin( dialog, null );
		dialog.setVisible( true );
	}
}
