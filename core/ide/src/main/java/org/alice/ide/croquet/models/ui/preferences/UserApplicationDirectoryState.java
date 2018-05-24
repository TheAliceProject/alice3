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
package org.alice.ide.croquet.models.ui.preferences;

import edu.cmu.cs.dennisc.java.io.FileUtilities;
import org.alice.ide.IDE;
import org.lgna.croquet.Application;

import java.io.File;
import java.net.URI;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Kyle Harms, Dennis Cosgrove
 */
public class UserApplicationDirectoryState extends DirectoryState {
	public static final String KEY = "${user_application_documents}";
	private static final Pattern KEY_PATTERN = Pattern.compile( Pattern.quote( KEY ) );

	public String substituteKeyIfNecessary( String value, String defaultValue ) {
		Matcher matcher = KEY_PATTERN.matcher( value );
		String userApplicationDirectoryValue = this.getValue();
		try {
			//throw new IllegalArgumentException();
			return matcher.replaceAll( userApplicationDirectoryValue );
		} catch( IllegalArgumentException iae ) {
			//			edu.cmu.cs.dennisc.java.awt.datatransfer.ClipboardUtilities.setClipboardContents( value );
			//			StringBuilder sb = new StringBuilder();
			//			sb.append( "the following value has been copied to your clipboard: \n\n\t" );
			//			sb.append( value );
			//			sb.append( userApplicationDirectoryValue );
			//			sb.append( "\n\nplease paste it in an email and send it to dennis." );
			//
			//			javax.swing.JOptionPane.showMessageDialog( null, sb.toString() );
			if( value.startsWith( KEY ) ) {
				return userApplicationDirectoryValue + value.substring( KEY.length() );
			} else if( defaultValue.startsWith( KEY ) ) {
				return userApplicationDirectoryValue + defaultValue.substring( KEY.length() );
			} else {
				Matcher defaultMatcher = KEY_PATTERN.matcher( defaultValue );
				return defaultMatcher.replaceAll( userApplicationDirectoryValue );
			}
		}
	}

	private static String getInitialValue() {
		File defaultDirectory = FileUtilities.getDefaultDirectory();
		File directory = new File( defaultDirectory, IDE.getApplicationSubPath() );
		URI uri = directory.toURI();
		return uri.toString();
	}

	private static class SingletonHolder {
		private static UserApplicationDirectoryState instance = new UserApplicationDirectoryState();
	}

	public static UserApplicationDirectoryState getInstance() {
		return SingletonHolder.instance;
	}

	private UserApplicationDirectoryState() {
		super(
				Application.DOCUMENT_UI_GROUP,
				UUID.fromString( "5f80de2f-5119-4131-96d0-c0b80919a589" ),
				getInitialValue() );
	}

	@Override
	protected String getPath() {
		return this.getValue();
	}
}
