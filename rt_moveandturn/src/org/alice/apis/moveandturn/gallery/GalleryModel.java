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

import edu.cmu.cs.dennisc.alice.annotations.PropertyGetterTemplate;
import edu.cmu.cs.dennisc.alice.annotations.Visibility;

/**
 * @author Dennis Cosgrove
 */
public abstract class GalleryModel extends org.alice.apis.moveandturn.PolygonalModel {
	public static final edu.cmu.cs.dennisc.property.GetterSetterProperty< String > MODELED_BY_CREDIT_PROPERTY = new edu.cmu.cs.dennisc.property.GetterSetterProperty< String >( GalleryModel.class, "ModeledByCredit" );
	public static final edu.cmu.cs.dennisc.property.GetterSetterProperty< String > PAINTED_BY_CREDIT_PROPERTY = new edu.cmu.cs.dennisc.property.GetterSetterProperty< String >( GalleryModel.class, "PaintedByCredit" );
	public static final edu.cmu.cs.dennisc.property.GetterSetterProperty< String > PROGRAMMED_BY_CREDIT_PROPERTY = new edu.cmu.cs.dennisc.property.GetterSetterProperty< String >( GalleryModel.class, "ProgrammedByCredit" );
	private String m_modeledByCredit = null;
	private String m_paintedByCredit = null;
	private String m_programmedByCredit = null;
	
	private static final String ROOT_PATH_KEY = "rootPath";
	private static java.io.File s_galleryRootDirectory;
	static {
		do {
			java.util.List< String > potentialPaths = new java.util.LinkedList< String >();
			String rootPathProperty = System.getProperty( GalleryModel.class.getName() + "." + ROOT_PATH_KEY );
			if( rootPathProperty != null ) {
				potentialPaths.add( rootPathProperty );
			}
			String subPath = "/Alice/3.beta.0000/gallery";
			potentialPaths.add( "/Program Files" + subPath );
			potentialPaths.add( "/Applications" + subPath );
			potentialPaths.add( "/Applications/Programming" + subPath );
			potentialPaths.add( "c:/Program Files" + subPath );
			potentialPaths.add( "c:/Program Files (x86)" + subPath );

			java.util.prefs.Preferences userPreferences = java.util.prefs.Preferences.userNodeForPackage( GalleryModel.class );
			String rootPathUserPreference = userPreferences.get( ROOT_PATH_KEY, null );
			if( rootPathUserPreference != null ) {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "rootPathUserPreference", rootPathUserPreference );
				potentialPaths.add( rootPathUserPreference );
			}
			java.util.prefs.Preferences systemPreferences = java.util.prefs.Preferences.systemNodeForPackage( GalleryModel.class );
			String rootPathSystemPreference = systemPreferences.get( ROOT_PATH_KEY, null );
			if( rootPathSystemPreference != null ) {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "rootPathSystemPreference", rootPathSystemPreference );
				potentialPaths.add( rootPathSystemPreference );
			}
			
			for( String path : potentialPaths ) {
				java.io.File directory = new java.io.File( path );
				if( directory.exists() && directory.isDirectory() ) {
					s_galleryRootDirectory = directory;
					break;
				}
			}
			if( s_galleryRootDirectory != null ) {
				//pass
			} else {
				String expectedRoot;
				if( edu.cmu.cs.dennisc.lang.SystemUtilities.isWindows() ) {
					expectedRoot = "c:/Program Files";
				} else {
					expectedRoot = "/Applications/Programming";
				}
				int result = promptUserToSpecifyOrInstall( expectedRoot + subPath );
				if( result == javax.swing.JOptionPane.YES_OPTION ) {
					GalleryDirectorySelectionPane galleryDirectorySelectionPane = new GalleryDirectorySelectionPane();
					java.io.File directory = galleryDirectorySelectionPane.showInJDialog( null );
					if( directory != null ) {
						String path = edu.cmu.cs.dennisc.io.FileUtilities.getCanonicalPathIfPossible( directory );
						userPreferences.put( ROOT_PATH_KEY, path );
						systemPreferences.put( ROOT_PATH_KEY, path );
						s_galleryRootDirectory = directory;
					}
				} else {
					result = promptUserToRetryOrExit();
					if( result == javax.swing.JOptionPane.YES_OPTION ) {
						//pass
					} else {
						System.exit( -1 );
					}
				}
			}
		} while( s_galleryRootDirectory == null );
	}
	private static int promptUserToSpecifyOrInstall( String expectedLocation ) {
		java.awt.Component owner = null;
		String message = "Cannot find Alice Move & Turn Gallery in its expected location:\n    " + expectedLocation;
		String title = "Cannot find Alice Move & Turn Gallery";
		Object[] options = { "Specify Actual Gallery Location", "Exit and Install Gallery" }; 
		return javax.swing.JOptionPane.showOptionDialog( owner, message, title, javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.ERROR_MESSAGE, null, options, null );
	}
	
	private static int promptUserToRetryOrExit() {
		java.awt.Component owner = null;
		String message = "Alice will not work until the gallery is successfully installed.\nIf you are a student, please check with your instructor on how to install the gallery.\nIf you are an instructor, please check the wiki for information.";
		String title = "Must Install Alice Move & Turn Gallery";
		Object[] options = { "Retry", "Exit" }; 
		return javax.swing.JOptionPane.showOptionDialog( owner, message, title, javax.swing.JOptionPane.YES_OPTION, javax.swing.JOptionPane.ERROR_MESSAGE, null, options, null );
	}
	
	public static java.io.File getGalleryRootDirectory() {
		return s_galleryRootDirectory;
	}
	
	private static final String SUB_PATH = "assets/org.alice.apis.moveandturn.gallery";
	
	protected GalleryModel() {
	}
	public GalleryModel( String path ) {
		java.io.File directory = new java.io.File( s_galleryRootDirectory, SUB_PATH );
		java.io.File file = new java.io.File( directory, path + ".zip" );
		
		assert file.exists();
		edu.cmu.cs.dennisc.codec.CodecUtilities.decodeZippedReferenceableBinary( this, file.getAbsolutePath(), "element.bin" );	
	}
	@PropertyGetterTemplate(visibility = Visibility.TUCKED_AWAY)
	public String getModeledByCredit() {
		return m_modeledByCredit;
	}
	public void setModeledByCredit( String modeledByCredit ) {
		m_modeledByCredit = modeledByCredit;
	}
	@PropertyGetterTemplate(visibility = Visibility.TUCKED_AWAY)
	public String getPaintedByCredit() {
		return m_paintedByCredit;
	}
	public void setPaintedByCredit( String paintedByCredit ) {
		m_paintedByCredit = paintedByCredit;
	}
	@PropertyGetterTemplate(visibility = Visibility.TUCKED_AWAY)
	public String getProgrammedByCredit() {
		return m_programmedByCredit;
	}
	public void setProgrammedByCredit( String programmedByCredit ) {
		m_programmedByCredit = programmedByCredit;
	}
	
	public static void main( String[] args ) throws java.util.prefs.BackingStoreException {
		if( args.length > 0 && args[ 0 ].equals( "clearPreferences" ) ) {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "clearPreferences" );
			java.util.prefs.Preferences userPreferences = java.util.prefs.Preferences.userNodeForPackage( GalleryModel.class );
			userPreferences.clear();
			java.util.prefs.Preferences systemPreferences = java.util.prefs.Preferences.systemNodeForPackage( GalleryModel.class );
			systemPreferences.clear();
		}
	}
}
