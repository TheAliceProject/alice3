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
package edu.cmu.cs.dennisc.eula;

import edu.cmu.cs.dennisc.eula.swing.JEulaPane;

/**
 * @author Dennis Cosgrove
 */
public class EULAUtilities {
	private static java.util.List<Class<?>> alreadyClearedPreferences;

	public static void promptUserToAcceptEULAIfNecessary( Class<?> preferencesCls, String preferencesKey, String title, String license, String name ) throws LicenseRejectedException {
		java.util.prefs.Preferences userPreferences = java.util.prefs.Preferences.userNodeForPackage( preferencesCls );
		if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isPropertyTrue( "org.alice.clearAllPreferences" ) ) {
			if( alreadyClearedPreferences != null ) {
				//pass
			} else {
				alreadyClearedPreferences = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
			}
			if( alreadyClearedPreferences.contains( preferencesCls ) ) {
				//pass
			} else {
				alreadyClearedPreferences.add( preferencesCls );
				try {
					edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "clearing", userPreferences );
					userPreferences.clear();
				} catch( java.util.prefs.BackingStoreException bse ) {
					throw new RuntimeException( bse );
				}
			}
		}
		boolean isLicenseAccepted = userPreferences.getBoolean( preferencesKey, false );
		if( isLicenseAccepted ) {
			//pass
		} else {
			JEulaPane eulaPane = new JEulaPane( license );
			java.awt.Component owner = edu.cmu.cs.dennisc.javax.swing.WindowStack.peek();
			//			if( owner.isVisible() ) {
			//				//pass
			//			} else {
			//				owner.setVisible( true );
			//			}
			while( true ) {
				javax.swing.JDialog dialog = new edu.cmu.cs.dennisc.javax.swing.JDialogBuilder()
						.owner( owner )
						.isModal( true )
						.title( title )
						.build();
				dialog.getContentPane().add( eulaPane, java.awt.BorderLayout.CENTER );
				dialog.pack();
				if( ( owner != null ) && owner.isVisible() ) {
					edu.cmu.cs.dennisc.java.awt.WindowUtilities.setLocationOnScreenToCenteredWithin( dialog, owner );
				}
				dialog.setVisible( true );
				isLicenseAccepted = eulaPane.isAccepted();
				if( isLicenseAccepted ) {
					break;
				} else {
					String message = "You must accept the license agreement in order to use " + name + ".\n\nWould you like to return to the license agreement?";
					if( javax.swing.JOptionPane.YES_OPTION == javax.swing.JOptionPane.showConfirmDialog( owner, message, "Return to license agreement?", javax.swing.JOptionPane.YES_NO_OPTION ) ) {
						//pass
					} else {
						break;
					}
				}
			}
		}
		if( isLicenseAccepted ) {
			userPreferences.putBoolean( preferencesKey, true );
		} else {
			throw new edu.cmu.cs.dennisc.eula.LicenseRejectedException();
		}
	}
}
