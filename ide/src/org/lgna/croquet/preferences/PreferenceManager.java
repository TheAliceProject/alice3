/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
 */
package org.lgna.croquet.preferences;

/**
 * @author Dennis Cosgrove
 */
public class PreferenceManager {
	private PreferenceManager() {
		throw new AssertionError();
	}
	public static java.util.prefs.Preferences getUserPreferences() {
		org.lgna.croquet.Application application = org.lgna.croquet.Application.getActiveInstance();
		if( application != null ) {
			java.util.prefs.Preferences rv = java.util.prefs.Preferences.userNodeForPackage( application.getClass() );
			if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isPropertyTrue( "org.alice.clearAllPreferences" ) ) {
				try {
					rv.clear();
				} catch( java.util.prefs.BackingStoreException bse ) {
					throw new RuntimeException( bse );
				}
			}
			return rv;
		} else {
			return null;
		}
	}

//	private static java.util.List< org.lgna.croquet.BooleanState > booleanStatePreferences = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
//	private static java.util.List< org.lgna.croquet.StringState > stringStatePreferences = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
//	private static java.util.List< org.lgna.croquet.ListSelectionState< ? > > listSelectionStatePreferences = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();

//	public static void registerAndInitializePreference( org.lgna.croquet.BooleanState booleanState ) {
//		IDE ide = IDE.getActiveInstance();
//		if( ide != null ) {
//			java.util.prefs.Preferences userPreferences = java.util.prefs.Preferences.userNodeForPackage( ide.getClass() );
//			clearAllPreferencesIfRequested( userPreferences );
//			java.util.UUID id = booleanState.getId();
//			boolean value = userPreferences.getBoolean( id.toString(), booleanState.getValue() );
//			booleanState.setValue( value );
//			booleanStatePreferences.add( booleanState );
//		} else {
//			System.err.println( "registerAndInitializePreference: " + booleanState );
//		}
//	}
//	public static void registerAndInitializePreference( org.lgna.croquet.StringState stringState ) {
//		IDE ide = IDE.getActiveInstance();
//		if( ide != null ) {
//			java.util.prefs.Preferences userPreferences = java.util.prefs.Preferences.userNodeForPackage( ide.getClass() );
//			clearAllPreferencesIfRequested( userPreferences );
//			java.util.UUID id = stringState.getId();
//			String value = userPreferences.get( id.toString(), stringState.getValue() );
//			stringState.setValue( value );
//			stringStatePreferences.add( stringState );
//		} else {
//			System.err.println( "registerAndInitializePreference: " + stringState );
//		}
//	}
//	public static void registerAndInitializePreference( org.lgna.croquet.ListSelectionState< ? > listSelectionState ) {
//		IDE ide = IDE.getActiveInstance();
//		if( ide != null ) {
//			java.util.prefs.Preferences userPreferences = java.util.prefs.Preferences.userNodeForPackage( ide.getClass() );
//			clearAllPreferencesIfRequested( userPreferences );
//			try {
//				decode( listSelectionState, userPreferences );
//			} catch( Throwable t ) {
//				t.printStackTrace();
//			}
//			listSelectionStatePreferences.add( listSelectionState );
//		} else {
//			System.err.println( "registerAndInitializePreference: " + listSelectionState );
//		}
//	}
	public static void preservePreferences() throws java.util.prefs.BackingStoreException {
		java.util.prefs.Preferences userPreferences = PreferenceManager.getUserPreferences();
		if( userPreferences != null ) {
			org.lgna.croquet.preferences.PreferenceBooleanState.preserveAll( userPreferences );
			org.lgna.croquet.preferences.PreferenceStringState.preserveAll( userPreferences );
			org.lgna.croquet.preferences.PreferenceListSelectionState.preserveAll( userPreferences );
			userPreferences.flush();
		}
	}
}
