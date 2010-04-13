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
package org.alice.apis.moveandturn.gallery;

/**
 * @author Dennis Cosgrove
 */
public class GalleryRootUtilities {
	private static final String ROOT_PATH_KEY = "rootPath";
	public static java.io.File calculateGalleryRootDirectory( Class<?> cls, String subPath, String name, String childName, String grandchildName, String titleForPromptingUserToSpecifyOrInstall, String applicationName ) {
		java.io.File rv = null;
		java.util.prefs.Preferences userPreferences = java.util.prefs.Preferences.userNodeForPackage( cls );
		if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isPropertyTrue( "org.alice.clearAllPreferences" ) ) {
			try {
				userPreferences.clear();
			} catch( java.util.prefs.BackingStoreException bse ) {
				throw new RuntimeException( bse );
			}
		}
		String rootPathUserPreference = userPreferences.get( ROOT_PATH_KEY, null );
//		java.util.prefs.Preferences systemPreferences = java.util.prefs.Preferences.systemNodeForPackage( cls );
//		String rootPathSystemPreference = systemPreferences.get( ROOT_PATH_KEY, null );
		do {
			java.util.List< String > potentialPaths = new java.util.LinkedList< String >();
			
			String galleryDirPath = System.getProperty( "org.alice.apis.gallery.dir" );
			if( galleryDirPath != null && galleryDirPath.length() > 0 ) {
				java.io.File galleryDir = new java.io.File( galleryDirPath );
				if( galleryDir != null && galleryDir.exists() ) {
					potentialPaths.add( galleryDirPath );
				}
			}

			String installDirPath = System.getProperty( "org.alice.ide.IDE.install.dir" );
			if( installDirPath != null && installDirPath.length() > 0 ) {
				java.io.File installDir = new java.io.File( installDirPath );
				if( installDir != null && installDir.exists() ) {
					potentialPaths.add( edu.cmu.cs.dennisc.java.io.FileUtilities.getCanonicalPathIfPossible( installDir ) + "/gallery" );
				}
			}

			String rootPathProperty = System.getProperty( cls.getName() + "." + ROOT_PATH_KEY );
			if( rootPathProperty != null ) {
				potentialPaths.add( rootPathProperty );
			}
			potentialPaths.add( "/Program Files" + subPath );
			potentialPaths.add( "/Applications" + subPath );
			potentialPaths.add( "/Applications/Programming" + subPath );
			if( subPath.startsWith( "/LookingGlass" ) ) {
				potentialPaths.add( "/Applications/Programming/Alice" + subPath );
			}
			potentialPaths.add( "c:/Program Files" + subPath );
			potentialPaths.add( "c:/Program Files (x86)" + subPath );
			potentialPaths.add( System.getProperty( "user.home" ) + subPath );
			
			java.io.File userDir = new java.io.File( System.getProperty( "user.dir" ) );
			if( userDir != null && userDir.exists() ) {
				potentialPaths.add( edu.cmu.cs.dennisc.java.io.FileUtilities.getCanonicalPathIfPossible( userDir ) + "/" + name );
			}
			if( rootPathUserPreference != null ) {
				potentialPaths.add( rootPathUserPreference );
			}
//			if( rootPathSystemPreference != null ) {
//				potentialPaths.add( rootPathSystemPreference );
//			}
			
			for( String path : potentialPaths ) {
				java.io.File directory = new java.io.File( path );
				if( directory.exists() && directory.isDirectory() ) {
					rv = directory;
					break;
				}
			}
			if( rv != null ) {
				//pass
			} else {
				String expectedRoot;
				if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isWindows() ) {
					expectedRoot = "c:/Program Files";
				} else if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isMac() ) {
					expectedRoot = "/Applications";
				} else {
					expectedRoot = System.getProperty( "user.home" );
				}
				int result = GalleryRootUtilities.promptUserToSpecifyOrInstall( expectedRoot + subPath, titleForPromptingUserToSpecifyOrInstall );
				if( result == javax.swing.JOptionPane.YES_OPTION ) {
					GalleryDirectorySelectionPane galleryDirectorySelectionPane = new GalleryDirectorySelectionPane( name, childName, grandchildName );
					java.io.File directory = galleryDirectorySelectionPane.showInJDialog( null );
					if( directory != null ) {
						rv = directory;
					}
				} else {
					result = GalleryRootUtilities.promptUserToRetryOrExit( titleForPromptingUserToSpecifyOrInstall, applicationName );
					if( result == javax.swing.JOptionPane.YES_OPTION ) {
						//pass
					} else {
						System.exit( -1 );
					}
				}
			}
		} while( rv == null );
		if( rv != null ) {
			String path = edu.cmu.cs.dennisc.java.io.FileUtilities.getCanonicalPathIfPossible( rv );
			try {
				userPreferences.put( ROOT_PATH_KEY, path );
				//systemPreferences.put( ROOT_PATH_KEY, path );
			} catch( Exception e ) {
				e.printStackTrace();
			}
		}
		return rv;
	}
	private static int promptUserToSpecifyOrInstall( String expectedLocation, String title ) {
		java.awt.Component owner = null;
		title = "Cannot find " + title;
		String message = title + " in its expected location:\n    " + expectedLocation;
		Object[] options = { "Specify Actual Gallery Location", "Exit and Install Gallery" }; 
		return javax.swing.JOptionPane.showOptionDialog( owner, message, title, javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.ERROR_MESSAGE, null, options, null );
	}
	
	private static int promptUserToRetryOrExit( String title, String applicationName ) {
		java.awt.Component owner = null;
		title = "Must Install " + title;
		String message = applicationName + " will not work until the gallery is successfully installed.\nIf you are a student, please check with your instructor on how to install the gallery.\nIf you are an instructor, please check the wiki for information.";
		Object[] options = { "Retry", "Exit" }; 
		return javax.swing.JOptionPane.showOptionDialog( owner, message, title, javax.swing.JOptionPane.YES_OPTION, javax.swing.JOptionPane.ERROR_MESSAGE, null, options, null );
	}
}
