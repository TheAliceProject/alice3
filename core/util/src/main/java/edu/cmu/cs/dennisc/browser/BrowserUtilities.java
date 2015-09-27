/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may
 *    "Alice" appear in their name, without prior written permission of
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is
 *    contributed by Electronic Arts Inc. and may be used for personal,
 *    non-commercial, and academic use only. Redistributions of any program
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in
 *    The Alice 3.0 Art Gallery License.
 *
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *******************************************************************************/
package edu.cmu.cs.dennisc.browser;

public class BrowserUtilities {
	//this code is heavily based on public domain software by Dem Pilafian
	//http://www.centerkey.com/java/browser/
	public static void browse( String url ) throws Exception {
		String lcOSName = System.getProperty( "os.name" ).toLowerCase( java.util.Locale.ENGLISH );
		if( lcOSName.startsWith( "mac os" ) ) {
			Class<?> fileMgr = Class.forName( "com.apple.eio.FileManager" );
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
