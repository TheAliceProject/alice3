/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package edu.cmu.cs.dennisc.eula;

/**
 * @author Dennis Cosgrove
 */
public class EULAUtilities extends Exception {
	public static void promptUserToAcceptEULAIfNecessary( Class preferencesCls, String preferencesKey, String license, String message ) throws LicenseRejectedException {
		java.util.prefs.Preferences preferences = java.util.prefs.Preferences.userNodeForPackage( preferencesCls );
//		try {
//			preferences.clear();
//		} catch( java.util.prefs.BackingStoreException bse ) {
//			throw new RuntimeException( bse );
//		}
		boolean isLicenseAccepted = preferences.getBoolean( preferencesKey, false );
		if( isLicenseAccepted ) {
			//pass
		} else {
			edu.cmu.cs.dennisc.ui.eula.EULAPane pane = new edu.cmu.cs.dennisc.ui.eula.EULAPane( license );
			java.awt.Component owner = null;
			while( true ) {
				isLicenseAccepted = pane.showInJDialog( owner, "License Agreement" ) == Boolean.TRUE;
				if( isLicenseAccepted ) {
					break;
				} else {
					String title = "Return to license agreement?";
					if( javax.swing.JOptionPane.YES_OPTION == javax.swing.JOptionPane.showConfirmDialog( owner, message, title, javax.swing.JOptionPane.YES_NO_OPTION ) ) {
						//pass
					} else {
						break;
					}
				}
			}
		}
		if( isLicenseAccepted ) {
			preferences.putBoolean( preferencesKey, true );
		} else {
			throw new edu.cmu.cs.dennisc.eula.LicenseRejectedException();
		}
	}
}
