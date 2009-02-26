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
package org.alice.apis.moveandturn.gallery;

/**
 * @author Dennis Cosgrove
 */
public class GalleryRootUtilities {
	private static java.io.File APPLICATION_ROOT;
	static {
		APPLICATION_ROOT = new java.io.File( System.getProperty( "user.dir" ) );
	}
	private static final String ROOT_PATH_KEY = "rootPath";
	public static java.io.File calculateGalleryRootDirectory( Class<?> cls, String subPath, String name, String childName, String grandchildName, String titleForPromptingUserToSpecifyOrInstall, String applicationName ) {
		java.io.File rv = null;
		java.util.prefs.Preferences userPreferences = java.util.prefs.Preferences.userNodeForPackage( cls );
		String rootPathUserPreference = userPreferences.get( ROOT_PATH_KEY, null );
		java.util.prefs.Preferences systemPreferences = java.util.prefs.Preferences.systemNodeForPackage( cls );
		String rootPathSystemPreference = systemPreferences.get( ROOT_PATH_KEY, null );
		do {
			java.util.List< String > potentialPaths = new java.util.LinkedList< String >();
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
			if( APPLICATION_ROOT != null && APPLICATION_ROOT.exists() ) {
				potentialPaths.add( edu.cmu.cs.dennisc.io.FileUtilities.getCanonicalPathIfPossible( APPLICATION_ROOT ) + "/" + name );
			}
			if( rootPathUserPreference != null ) {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "rootPathUserPreference", rootPathUserPreference );
				potentialPaths.add( rootPathUserPreference );
			}
			if( rootPathSystemPreference != null ) {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "rootPathSystemPreference", rootPathSystemPreference );
				potentialPaths.add( rootPathSystemPreference );
			}
			
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
				if( edu.cmu.cs.dennisc.lang.SystemUtilities.isWindows() ) {
					expectedRoot = "c:/Program Files";
				} else {
					expectedRoot = "/Applications/Programming";
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
		String path = edu.cmu.cs.dennisc.io.FileUtilities.getCanonicalPathIfPossible( rv );
		userPreferences.put( ROOT_PATH_KEY, path );
		systemPreferences.put( ROOT_PATH_KEY, path );
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
