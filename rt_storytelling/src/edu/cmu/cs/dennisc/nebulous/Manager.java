/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * THE ALICE 3.0 ART GALLERY LICENSE.
 * 
 * The Alice 3.0 gallery of The Sims 2 art assets and animations is provided by 
 * Electronic Arts Inc. pursuant to this license.  
 * Copyright (c) 2004 Electronic Arts Inc. All rights reserved.
 * 
 * Redistribution and use of the The Sims 2 art assets, animations, and other 
 * materials ("The Sims 2 Assets"), without modification, are permitted solely 
 * with programs written with Alice 3.0 for personal, non-commercial, and 
 * academic use only, provided that the following conditions are met:
 * 
 * 1. Redistributions of any program source code that uses The Sims 2 Assets 
 *    must retain the above copyright notice, this list of conditions and the 
 *    following disclaimer.
 * 2. Redistributions of any program in binary form that uses The Sims 2 Assets 
 *    must reproduce the above copyright notice, this list of conditions and the 
 *    following disclaimer in the documentation and/or other materials provided 
 *    with the distribution.
 * 3. Neither the name of the Electronic Arts Inc. nor any of its trademarks, 
 *    including the trademark THE SIMS 2, may be used to endorse or promote 
 *    programs or products derived from Alice 3.0 without specific prior written
 *    permission from Electronic Arts Inc.
 * 4. All promotional materials mentioning features or use of Alice 3.0 must 
 *    display the following acknowledgement:
 *     
 *    "This program/product includes art and animations developed by Electronic Arts Inc."
 *
 * THE SIMS 2 ASSETS ARE PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS 
 * "AS IS" WITHOUT WARRANTY OF ANY KIND.  ANY AND ALL EXPRESS, STATUTORY OR 
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
 * MERCHANTABILITY,  FITNESS FOR A PARTICULAR PURPOSE, TITLE, AND 
 * NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR 
 * OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO THE USE OF OR OTHER DEALINGS 
 * WITH THE SIMS 2 ASSETS, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 */

package edu.cmu.cs.dennisc.nebulous;

/**
 * @author Dennis Cosgrove
 */
public class Manager {
	static boolean s_isInitialized = false;
	static boolean s_isLicensePromptDesired = true;
	static java.util.List< java.io.File > s_pendingBundles;
	private static native void addBundlePath( String bundlePath );
	private static native void removeBundlePath( String bundlePath );
	static {
		try {
			initializeIfNecessary();
			java.io.File directory = new java.io.File( "s:/AliceBundles" );
			for( java.io.File file : directory.listFiles() ) {
				Manager.addBundle( file );
			}
		} catch( LicenseRejectedException lre ) {
			throw new RuntimeException( lre );
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
	public static void initializeIfNecessary() throws LicenseRejectedException {
		if( isInitialized() ) {
			//pass
		} else {
			java.util.prefs.Preferences preferences = java.util.prefs.Preferences.userNodeForPackage( Manager.class );
//			try {
//				preferences.clear();
//			} catch( java.util.prefs.BackingStoreException bse ) {
//				throw new RuntimeException( bse );
//			}
			boolean isLicenseAccepted = preferences.getBoolean( IS_LICENSE_ACCEPTED_PREFERENCE_KEY, false );
			if( isLicenseAccepted ) {
				//pass
			} else {
				if( s_isLicensePromptDesired ) {
					StringBuffer sb = new StringBuffer();
					sb.append( "THE ALICE 3.0 ART GALLERY LICENSE.\n\n" );
					sb.append( "The Alice 3.0 gallery of The Sims 2 art assets and animations is provided by Electronic Arts Inc. pursuant to this license.  Copyright (c) 2004 Electronic Arts Inc. All rights reserved.\n\n" );
					sb.append( "Redistribution and use of the The Sims 2 art assets, animations, and other materials (\"The Sims 2 Assets\"), without modification, are permitted solely with programs written with Alice 3.0 for personal, non-commercial, and academic use only, provided that the following conditions are met:\n\n" );
					sb.append( "1. Redistributions of any program source code that uses The Sims 2 Assets must retain the above copyright notice, this list of conditions and the following disclaimer. \n\n" );
					sb.append( "2. Redistributions of any program in binary form that uses The Sims 2 Assets must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.\n\n" );
					sb.append( "3. Neither the name of the Electronic Arts Inc. nor any of its trademarks, including the trademark THE SIMS 2, may be used to endorse or promote programs or products derived from Alice 3.0 without specific prior written permission from Electronic Arts Inc.\n\n" );
					sb.append( "4. All promotional materials mentioning features or use of Alice 3.0 must display the following acknowledgement: \"This program/product includes art and animations developed by Electronic Arts Inc.\"\n\n" );
					sb.append( "THE SIMS 2 ASSETS ARE PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS \"AS IS\" WITHOUT WARRANTY OF ANY KIND.  ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO THE USE OF OR OTHER DEALINGS WITH THE SIMS 2 ASSETS, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE." );
					edu.cmu.cs.dennisc.ui.eula.EULAPane pane = new edu.cmu.cs.dennisc.ui.eula.EULAPane( sb.toString() );
					java.awt.Component owner = null;
					while( true ) {
						isLicenseAccepted = pane.showInJDialog( owner, "License Agreement" ) == Boolean.TRUE;
						if( isLicenseAccepted ) {
							break;
						} else {
							String message = "You must accept the license agreement in order to use The Sims 2 art assets and animations.\n\nWould you like to return the license agreement?";
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
				preferences.putBoolean( IS_LICENSE_ACCEPTED_PREFERENCE_KEY, true );
				System.loadLibrary( "jni_nebulous" );
				for( java.io.File directory : Manager.getPendingBundles() ) {
					Manager.addBundlePath( directory.getAbsolutePath() );
				}
				s_isInitialized = true;
			} else {
				throw new LicenseRejectedException();
			}
		}
	}
	public static boolean isInitialized() {
		return s_isInitialized;
	}
	public static void resetLicensePromptDesiredToTrue() {
		s_isLicensePromptDesired = true;
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
