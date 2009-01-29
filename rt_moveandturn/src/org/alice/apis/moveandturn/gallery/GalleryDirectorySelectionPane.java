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
public class GalleryDirectorySelectionPane extends edu.cmu.cs.dennisc.swing.DirectorySelectionPane {
	protected String getDesiredDirectoryName() {
		return "gallery";
	}
	protected String getDesiredChildDirectoryName() {
		return "assets";
	}
	protected String getDesiredGrandchildDirectoryName() {
		return "org.alice.apis.moveandturn.gallery";
	}

	@Override
	protected java.lang.String getFeedbackText() {
		StringBuffer sb = new StringBuffer();
		sb.append( "<html>" );
		if( this.isOKButtonValid() ) {
			sb.append( "<b>FOUND:<b> " );
		} else {
			sb.append( "<b>current directory is not valid<b><br>please select a " );
		}
		sb.append( "directory named <i>" );
		sb.append( this.getDesiredDirectoryName() );
		sb.append( "</i> with a child named <i>" );
		sb.append( this.getDesiredChildDirectoryName() );
		sb.append( "</i> and grandchild named <i>" );
		sb.append( this.getDesiredGrandchildDirectoryName() );
		sb.append( "</i></html>" );
		return sb.toString();
	}
	@Override
	public boolean isOKButtonValid() {
		java.io.File directory = this.getFileChooser().getSelectedFile();
		if( directory != null ) {
			if( this.getDesiredDirectoryName().equals( directory.getName() ) ) {
				java.io.File childDirectory = new java.io.File( directory, this.getDesiredChildDirectoryName() );
				java.io.File grandchildDirectory = new java.io.File( childDirectory, this.getDesiredGrandchildDirectoryName() );
				return super.isOKButtonValid() && childDirectory.exists() && grandchildDirectory.exists();
			}
		}
		return false;
	}
}
