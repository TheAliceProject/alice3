package edu.cmu.cs.dennisc.browser;

public class BrowserUtilities {
	//this code is heavily based on public domain software by Dem Pilafian
	//http://www.centerkey.com/java/browser/
	public static void browse( String url ) throws Exception {
		String lcOSName = System.getProperty( "os.name" ).toLowerCase();
		if( lcOSName.startsWith( "mac os" ) ) {
			Class< ? > fileMgr = Class.forName( "com.apple.eio.FileManager" );
			java.lang.reflect.Method openURL = fileMgr.getDeclaredMethod( "openURL", new Class[] { String.class } );
			openURL.invoke( null, new Object[] { url } );
		} else {
			Runtime runtime = Runtime.getRuntime();
			if( lcOSName.startsWith( "windows" ) ) {
				runtime.exec( "rundll32 url.dll,FileProtocolHandler " + url );
			} else {
				final String[] browsers = { "firefox", "opera", "konqueror", "epiphany", "seamonkey", "galeon", "kazehakase", "mozilla", "netscape" };
				boolean found = false;
				for( String browser : browsers ) {
					found = runtime.exec( new String[] { "which", browser } ).waitFor() == 0;
					if( found ) {
						runtime.exec( new String[] { browser, url } );
						break;
					}
				}
				if( !found ) {
					throw new RuntimeException( "unable to find browser from amongst: " + java.util.Arrays.toString( browsers ) );
				}
			}
		}
	}
	public static void browse( java.net.URI uri ) throws Exception {
		browse( uri.getRawPath() );
	}
	public static void browse( java.net.URL url ) throws Exception {
		browse( url.toExternalForm() );
	}
}
