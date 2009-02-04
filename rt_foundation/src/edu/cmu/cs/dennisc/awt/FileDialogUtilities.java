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
package edu.cmu.cs.dennisc.awt;

/**
 * @author Dennis Cosgrove
 */
public class FileDialogUtilities {
	private static edu.cmu.cs.dennisc.map.MapToMap<java.awt.Component, String, java.awt.FileDialog> mapPathToFileDialog = new edu.cmu.cs.dennisc.map.MapToMap<java.awt.Component, String, java.awt.FileDialog>();
	private static java.util.Map<String, String> mapSecondaryKeyToPath = new java.util.HashMap<String, String>();
	
	private static java.io.File showFileDialog( java.awt.Component component, String title, int mode, String directoryPath, String extension, boolean isSharingDesired ) {
		java.awt.FileDialog fileDialog;
		java.awt.Component root = javax.swing.SwingUtilities.getRoot( component );
		String secondaryKey;
		if( directoryPath != null ) {
			secondaryKey = directoryPath;
		} else {
			secondaryKey = "null";
		}
		if( isSharingDesired ) {
			fileDialog = FileDialogUtilities.mapPathToFileDialog.get( component, secondaryKey );
		} else {
			fileDialog = null;
		}
		if( fileDialog != null ) {
			//pass
		} else {
			if( root instanceof java.awt.Frame ) {
				fileDialog = new java.awt.FileDialog( (java.awt.Frame)root, title, mode );
			} else if( root instanceof java.awt.Dialog ) {
				fileDialog = new java.awt.FileDialog( (java.awt.Dialog)root, title, mode );
			} else {
				fileDialog = new java.awt.FileDialog( (java.awt.Dialog)null, title, mode );
			}
			if( isSharingDesired ) {
				FileDialogUtilities.mapPathToFileDialog.put( component, secondaryKey, fileDialog );
			}

		}

		String path;
		if( isSharingDesired ) {
			path = FileDialogUtilities.mapSecondaryKeyToPath.get( secondaryKey );
		} else {
			path = null;
		}
		if( path != null ) {
			//pass
		} else {
			path = directoryPath;
		}
		if( path != null ) {
			fileDialog.setDirectory( path );
		}
		
		fileDialog.setVisible( true );
		String fileName = fileDialog.getFile();
		java.io.File rv;
		if( fileName != null ) {
			String requestedDirectoryPath = fileDialog.getDirectory();
			if( isSharingDesired ) {
				FileDialogUtilities.mapSecondaryKeyToPath.put( secondaryKey, requestedDirectoryPath );
			}
			java.io.File directory = new java.io.File( requestedDirectoryPath );
			if( mode == java.awt.FileDialog.SAVE ) {
				if( fileName.endsWith( "." + extension ) ) {
					//pass
				} else {
					fileName += "." + extension;
				}
			}
			rv = new java.io.File( directory, fileName );
		} else {
			rv = null;
		}
		return rv;
	}
	public static java.io.File showOpenFileDialog( java.awt.Component component, String directoryPath, String extension, boolean isSharingDesired ) {
		return showFileDialog( component, "Open...", java.awt.FileDialog.LOAD, directoryPath, extension, isSharingDesired ); 
	}
	public static java.io.File showSaveFileDialog( java.awt.Component component, String directoryPath, String extension, boolean isSharingDesired ) {
		return showFileDialog( component, "Save...", java.awt.FileDialog.SAVE, directoryPath, extension, isSharingDesired ); 
	}
	public static java.io.File showOpenFileDialog( java.awt.Component component, java.io.File directory, String extension, boolean isSharingDesired ) {
		return showOpenFileDialog( component, edu.cmu.cs.dennisc.io.FileUtilities.getCanonicalPathIfPossible( directory ), extension, isSharingDesired ); 
	}
	public static java.io.File showSaveFileDialog( java.awt.Component component, java.io.File directory, String extension, boolean isSharingDesired ) {
		return showSaveFileDialog( component, edu.cmu.cs.dennisc.io.FileUtilities.getCanonicalPathIfPossible( directory ), extension, isSharingDesired ); 
	}
}
