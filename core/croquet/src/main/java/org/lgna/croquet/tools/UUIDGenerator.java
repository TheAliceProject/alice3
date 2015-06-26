package org.lgna.croquet.tools;

public class UUIDGenerator {
	private static void setClipboardContents( String s ) {
		java.awt.datatransfer.StringSelection stringSelection = new java.awt.datatransfer.StringSelection( s );
		java.awt.datatransfer.Clipboard clipboard = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents( stringSelection, null );
	}

	public static void main( String[] args ) {
		String s = "java.util.UUID.fromString( \"" + java.util.UUID.randomUUID().toString() + "\" )";
		setClipboardContents( s );
		StringBuilder sb = new StringBuilder();
		sb.append( "The text below has been copied to your clipboard:\n" );
		sb.append( s );
		System.out.println( sb.toString() );
		if( System.getProperty( "os.name" ).toLowerCase( java.util.Locale.ENGLISH ).startsWith( "linux" ) ) {
			javax.swing.JOptionPane.showMessageDialog( null, sb.toString() );
		}
	}
}
