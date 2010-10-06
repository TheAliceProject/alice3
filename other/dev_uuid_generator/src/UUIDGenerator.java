
public class UUIDGenerator {
	public static void main(String[] args) {
		String s = "java.util.UUID.fromString( \"" + java.util.UUID.randomUUID().toString() + "\" );";
		edu.cmu.cs.dennisc.clipboard.ClipboardUtilities.setClipboardContents( s );
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "the text below has been copied to your clipboard:" );
		edu.cmu.cs.dennisc.print.PrintUtilities.println( s );
	}
}
