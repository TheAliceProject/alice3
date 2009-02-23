public class Main {
	public static void main( String[] args ) throws java.util.prefs.BackingStoreException {
		for( Class<?> cls : new Class<?>[] { edu.cmu.cs.dennisc.alice.License.class, edu.wustl.cse.lookingglass.apis.walkandtouch.License.class, edu.cmu.cs.dennisc.nebulous.Manager.class } ) {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( cls );
			java.util.prefs.Preferences preferences = java.util.prefs.Preferences.userNodeForPackage( cls );
			preferences.clear();
		}
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "done" );		
	}
}
