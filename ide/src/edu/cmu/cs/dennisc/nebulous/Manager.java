/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 */

package edu.cmu.cs.dennisc.nebulous;

import org.alice.ide.IDE;

/**
 * @author Dennis Cosgrove
 */
public class Manager {
	static boolean s_isInitialized = false;
	static boolean s_isLicensePromptDesired = true;
	static java.util.List< java.io.File > s_pendingBundles;

	private static native void setDebugDraw(boolean debugDraw);
	private static native void addBundlePath( String bundlePath );
	private static native void removeBundlePath( String bundlePath );
	private static native void setRawResourceDirectory( String rourcePath );

	static {
		try {
			initializeIfNecessary();
		} catch( edu.cmu.cs.dennisc.eula.LicenseRejectedException lre ) {
			javax.swing.JOptionPane.showMessageDialog( null, "license rejected" );
			//throw new RuntimeException( lre );
		} catch( Throwable t ) {
			javax.swing.JOptionPane.showMessageDialog( null, "failed to initialize art assets" );
			t.printStackTrace();
		}
	}

	private static final String IS_LICENSE_ACCEPTED_PREFERENCE_KEY = "isLicenseAccepted";

	private static java.util.List< java.io.File > getPendingBundles() {
		if( s_pendingBundles != null ) {
			//pass
		} else {
			s_pendingBundles = new java.util.LinkedList< java.io.File >();
		}
		return s_pendingBundles;
	}
	public static void initializeIfNecessary() throws edu.cmu.cs.dennisc.eula.LicenseRejectedException {
		if( isInitialized() ) {
			//pass
		} else {
			
			System.out.println(System.getProperty("java.library.path"));
			
			java.util.prefs.Preferences userPreferences = java.util.prefs.Preferences.userNodeForPackage( License.class );
			if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isPropertyTrue( "org.alice.clearAllPreferences" ) ) {
				try {
					userPreferences.clear();
				} catch( java.util.prefs.BackingStoreException bse ) {
					throw new RuntimeException( bse );
				}
			}
			boolean isLicenseAccepted = userPreferences.getBoolean( IS_LICENSE_ACCEPTED_PREFERENCE_KEY, false );
			if( isLicenseAccepted ) {
				//pass
			} else {
				if( s_isLicensePromptDesired ) {
					edu.cmu.cs.dennisc.ui.eula.EULAPane pane = new edu.cmu.cs.dennisc.ui.eula.EULAPane( License.TEXT );
					java.awt.Component owner = null;
					while( true ) {
						isLicenseAccepted = pane.showInJDialog( owner, "License Agreement" ) == Boolean.TRUE;
						if( isLicenseAccepted ) {
							break;
						} else {
							String message = "You must accept the license agreement in order to use The Sims 2 art assets and animations.\n\nWould you like to return to the license agreement?";
							String title = "Return to license agreement?";
							if( javax.swing.JOptionPane.YES_OPTION == javax.swing.JOptionPane.showConfirmDialog( owner, message, title, javax.swing.JOptionPane.YES_NO_OPTION ) ) {
								//pass
							} else {
								break;
							}
						}
					}
					s_isLicensePromptDesired = false;
				}
			}
			if( isLicenseAccepted ) {
				userPreferences.putBoolean( IS_LICENSE_ACCEPTED_PREFERENCE_KEY, true );
				System.loadLibrary( "jni_nebulous" );
				for( java.io.File directory : Manager.getPendingBundles() ) {
					Manager.addBundlePath( directory.getAbsolutePath() );
				}
				boolean debugDraw = edu.cmu.cs.dennisc.java.lang.SystemUtilities.isPropertyTrue(org.alice.ide.IDE.DEBUG_DRAW_PROPERTY_KEY);
				Manager.setDebugDraw(debugDraw);
				s_isInitialized = true;
			} else {
				throw new edu.cmu.cs.dennisc.eula.LicenseRejectedException();
			}
		}
	}
	public static boolean isInitialized() {
		return s_isInitialized;
	}
	public static void resetLicensePromptDesiredToTrue() {
		s_isLicensePromptDesired = true;
	}

	public static void setRawResourcePath( java.io.File file ) {
		Manager.setRawResourceDirectory( file.getAbsolutePath() );
	}

	public static void addBundle( java.io.File file ) {
		if( isInitialized() ) {
			Manager.addBundlePath( file.getAbsolutePath() );
		} else {
			Manager.getPendingBundles().add( file );
		}
	}
	public static void removeBundle( java.io.File file ) {
		if( isInitialized() ) {
			Manager.removeBundlePath( file.getAbsolutePath() );
		} else {
			Manager.getPendingBundles().remove( file );
		}
	}

	//	public static void main( String[] args ) {
	//	}
}
