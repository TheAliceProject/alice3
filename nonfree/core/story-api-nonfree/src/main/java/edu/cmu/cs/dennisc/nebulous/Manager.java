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

package edu.cmu.cs.dennisc.nebulous;

/**
 * @author Dennis Cosgrove
 */
public class Manager {
	public static final double NEBULOUS_VERSION = 1.7;

	private static boolean s_isInitialized = false;
	private static boolean s_isLicensePromptDesired = true;
	private static final String IS_LICENSE_ACCEPTED_PREFERENCE_KEY = "isLicenseAccepted";

	private static java.util.List<java.io.File> s_pendingBundles;

	private static native void setVersion( double version );

	private static native void addBundlePath( String bundlePath );

	private static native void removeBundlePath( String bundlePath );

	private static native void setRawResourceDirectory( String rourcePath );

	private static native void unloadActiveModelData();

	private static native void unloadUnusedTextures( javax.media.opengl.GL gl );

	public static native void setDebugDraw( boolean debugDraw );

	private static void doInitializationIfNecessary() {
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

	private static java.util.List<java.io.File> getPendingBundles() {
		if( s_pendingBundles != null ) {
			//pass
		} else {
			s_pendingBundles = new java.util.LinkedList<java.io.File>();
		}
		return s_pendingBundles;
	}

	public static void unloadNebulousModelData() {
		if( isInitialized() ) {
			unloadActiveModelData();
		}
	}

	public static void unloadUnusedNebulousTextureData( javax.media.opengl.GL gl ) {
		if( isInitialized() ) {
			try {
				unloadUnusedTextures( gl );
			} catch( RuntimeException e ) {
				e.printStackTrace();
			}
		}
	}

	public static void initializeIfNecessary() throws edu.cmu.cs.dennisc.eula.LicenseRejectedException {
		if( isInitialized() ) {
			//pass
		} else {
			if( s_isLicensePromptDesired ) {
				edu.cmu.cs.dennisc.eula.EULAUtilities.promptUserToAcceptEULAIfNecessary(
						edu.cmu.cs.dennisc.nebulous.License.class,
						IS_LICENSE_ACCEPTED_PREFERENCE_KEY,
						"License Agreement: The Sims (TM) 2 Art Assets",
						edu.cmu.cs.dennisc.nebulous.License.TEXT,
						"The Sims (TM) 2 Art Assets" );
				java.util.prefs.Preferences userPreferences = java.util.prefs.Preferences.userNodeForPackage( License.class );
				boolean isLicenseAccepted = userPreferences.getBoolean( IS_LICENSE_ACCEPTED_PREFERENCE_KEY, false );
				if( isLicenseAccepted ) {
					//pass
				} else {
					s_isLicensePromptDesired = false;
				}
				if( isLicenseAccepted ) {
					userPreferences.putBoolean( IS_LICENSE_ACCEPTED_PREFERENCE_KEY, true );
					if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isPropertyTrue( "org.alice.ide.internalDebugMode" ) ) {
						edu.cmu.cs.dennisc.java.lang.SystemUtilities.loadLibrary( "", "jni_nebulous", edu.cmu.cs.dennisc.java.lang.LoadLibraryReportStyle.EXCEPTION );
					}
					else {
						edu.cmu.cs.dennisc.java.lang.SystemUtilities.loadLibrary( "nebulous", "jni_nebulous", edu.cmu.cs.dennisc.java.lang.LoadLibraryReportStyle.EXCEPTION );
					}
					for( java.io.File directory : Manager.getPendingBundles() ) {
						Manager.addBundlePath( directory.getAbsolutePath() );
					}
					Manager.setVersion( NEBULOUS_VERSION );

					s_isInitialized = true;
				} else {
					throw new edu.cmu.cs.dennisc.eula.LicenseRejectedException();
				}
			}
			edu.cmu.cs.dennisc.render.gl.imp.RenderContext.addUnusedTexturesListener( new edu.cmu.cs.dennisc.render.gl.imp.RenderContext.UnusedTexturesListener() {
				@Override
				public void unusedTexturesCleared( javax.media.opengl.GL gl ) {
					unloadUnusedNebulousTextureData( gl );
				}
			} );
		}
	}

	public static boolean isInitialized() {
		return s_isInitialized;
	}

	public static void resetLicensePromptDesiredToTrue() {
		s_isLicensePromptDesired = true;
	}

	public static void setRawResourcePath( java.io.File file ) {
		doInitializationIfNecessary();
		Manager.setRawResourceDirectory( file.getAbsolutePath() );
	}

	public static void addBundle( java.io.File file ) {
		doInitializationIfNecessary();
		if( isInitialized() ) {
			Manager.addBundlePath( file.getAbsolutePath() );
		} else {
			Manager.getPendingBundles().add( file );
		}
	}

	public static void removeBundle( java.io.File file ) {
		doInitializationIfNecessary();
		if( isInitialized() ) {
			Manager.removeBundlePath( file.getAbsolutePath() );
		} else {
			Manager.getPendingBundles().remove( file );
		}
	}
}
