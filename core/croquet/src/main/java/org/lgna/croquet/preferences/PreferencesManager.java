/*******************************************************************************
 * Copyright (c) 2018, Carnegie Mellon University. All rights reserved.
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
package org.lgna.croquet.preferences;

import edu.cmu.cs.dennisc.java.io.FileUtilities;
import edu.cmu.cs.dennisc.java.lang.SystemUtilities;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.lgna.croquet.Application;

import java.io.File;
import java.net.URI;
import java.util.UUID;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class PreferencesManager {

	static final String ORG_ALICE_CLEAR_ALL_PREFERENCES = "org.alice.clearAllPreferences";
	private static final String KEY = "${user_application_documents}";

	public PreferencesManager( Application application ) {
		this.application = application;
	}

	private Preferences getUserPreferences() {
		Preferences prefs = Preferences.userNodeForPackage( application.getClass() );
		if ( SystemUtilities.isPropertyTrue( ORG_ALICE_CLEAR_ALL_PREFERENCES ) ) {
			try {
				prefs.clear();
			} catch (BackingStoreException bse) {
				throw new RuntimeException( bse );
			}
		}
		return prefs;
	}

	private String getInitialUserDirectory() {
		File defaultDirectory = FileUtilities.getDefaultDirectory();
		File directory = new File( defaultDirectory, application.getApplicationSubPath() );
		return directory.toURI().toString();
	}

	private String getUserDirectory() {
		return getUserPreferences().get( "5f80de2f-5119-4131-96d0-c0b80919a589", getInitialUserDirectory() );
	}

	public File getUserDirectory( UUID leafMigrationId, String defaultLeafDirectory ) {
		// Group g = Application.DOCUMENT_UI_GROUP;
		final String userDirName = getUserDirectory();
		File userDirectory = getDirectory( userDirName );
		String path = getUserPreferences()
				.get( leafMigrationId.toString(), userDirectory.toPath().resolve(defaultLeafDirectory ).toString() );

		File dir = getDirectory( substituteKeyIfNecessary( path, userDirName ) );
		if ( !dir.exists() && !dir.mkdirs() ) {
			System.err.println( "Unable to create user directory: " + dir );
		}
		return dir;
	}

	private String substituteKeyIfNecessary( String path, String userParentDirectory ) {
		if ( path.startsWith( KEY ) ) {
			return userParentDirectory + path.substring( KEY.length() );
		}
		return path;
	}

	private static File getDirectory( String path ) {
		try {
			URI uri = new URI( path );
			return new File( uri );
		} catch (Exception urise) {
			Logger.throwable( urise, "URI failure:", path );
			return new File( path );
		}
	}

	private Application application;

	public String getValue( String propertyName, String defaultValue ) {
		return getUserPreferences().get( propertyName, defaultValue );
	}

	public void setValue( String propertyName, String newValue ) {
		getUserPreferences().put( propertyName, newValue );
	}
}
