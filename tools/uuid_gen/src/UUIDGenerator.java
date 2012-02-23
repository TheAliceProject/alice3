
public class UUIDGenerator {
	private static void setClipboardContents( String s ) {
		java.awt.datatransfer.StringSelection stringSelection = new java.awt.datatransfer.StringSelection( s );
		java.awt.datatransfer.Clipboard clipboard = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents( stringSelection, new java.awt.datatransfer.ClipboardOwner() { 
			public void lostOwnership( java.awt.datatransfer.Clipboard clipboard, java.awt.datatransfer.Transferable contents ) {
				//pass
			}
		} );
	}

	public static void main(String[] args) {
		//String s = "super( java.util.UUID.fromString( \"" + java.util.UUID.randomUUID().toString() + "\" ) );";
		String s = "java.util.UUID.fromString( \"" + java.util.UUID.randomUUID().toString() + "\" )";
		setClipboardContents( s );
		StringBuilder sb = new StringBuilder();
		sb.append( "The text below has been copied to your clipboard:\n" );
		sb.append( s );
		if( System.getProperty( "os.name" ).toLowerCase().startsWith( "linux" ) ) {
			sb.append( "\n\n(Paste before pressing OK.)" );
			javax.swing.JOptionPane.showMessageDialog( null, sb.toString() );
		} else {
			System.out.println( sb.toString() );
		}
	}
}
