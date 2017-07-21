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
package edu.cmu.cs.dennisc.java.util;

/**
 * @author Dennis Cosgrove
 */
public abstract class ResourceBundleUtilities {
	private ResourceBundleUtilities() {
		throw new AssertionError();
	}

	private static final class Utf8ResourceBundleControl extends java.util.ResourceBundle.Control {
		@Override
		public java.util.ResourceBundle newBundle( String baseName, java.util.Locale locale, String format, ClassLoader loader, boolean reload ) throws java.lang.IllegalAccessException, java.lang.InstantiationException, java.io.IOException {
			String bundleName = this.toBundleName( baseName, locale );
			String resourceName = this.toResourceName( bundleName, "properties" );
			java.io.InputStream stream = null;
			if( reload ) {
				java.net.URL url = loader.getResource( resourceName );
				if( url != null ) {
					java.net.URLConnection connection = url.openConnection();
					if( connection != null ) {
						connection.setUseCaches( false );
						stream = connection.getInputStream();
					}
				}
			} else {
				stream = loader.getResourceAsStream( resourceName );
			}
			java.util.ResourceBundle bundle = null;
			if( stream != null ) {
				try {
					bundle = new java.util.PropertyResourceBundle( new java.io.InputStreamReader( stream, "UTF-8" ) );
				} finally {
					stream.close();
				}
			}
			return bundle;
		}
	}

	public static java.util.ResourceBundle getUtf8Bundle( String baseName, java.util.Locale locale ) {
		return java.util.ResourceBundle.getBundle( baseName, locale, new Utf8ResourceBundleControl() );
	}

	public static String getStringForKey( String key, String bundleName, String defaultValue ) {
		try {
			java.util.Locale locale = javax.swing.JComponent.getDefaultLocale();
			java.util.ResourceBundle resourceBundle = edu.cmu.cs.dennisc.java.util.ResourceBundleUtilities.getUtf8Bundle( bundleName, locale );
			return resourceBundle.getString( key );
		} catch( java.util.MissingResourceException mre ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.throwable( mre, bundleName, key );
		}
		return defaultValue;
	}

	public static String getStringFromSimpleNames( Class<?> cls, String baseName, java.util.Locale locale ) {
		java.util.ResourceBundle resourceBundle = getUtf8Bundle( baseName, locale );
		String key;
		Class<?> c = cls;
		do {
			if( c != null ) {
				key = c.getSimpleName();
				c = c.getSuperclass();
			} else {
				throw new RuntimeException( "cannot find resource for " + cls );
				//edu.cmu.cs.dennisc.print.PrintUtilities.println( "cannot find resource for", cls );
				//return null;
			}
			try {
				String unused = resourceBundle.getString( key );
				break;
			} catch( RuntimeException re ) {
				//pass;
			}
		} while( true );
		return resourceBundle.getString( key );
	}
	//	public static String getStringFromSimpleNames( Class<?> cls, String baseName ) {
	//		return getStringFromSimpleNames( cls, baseName, javax.swing.JComponent.getDefaultLocale() );
	//	}
}
