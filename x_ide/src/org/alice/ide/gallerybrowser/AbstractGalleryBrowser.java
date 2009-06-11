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
package org.alice.ide.gallerybrowser;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractGalleryBrowser extends org.alice.ide.Viewer< Void > {
	private AssetsPane assetsPane;
	protected void initialize( java.io.File rootDirectory ) {
		this.assetsPane = this.createAssetsPane( rootDirectory );
		final int GAP = 4;
		this.setLayout( new java.awt.BorderLayout( GAP*2, 0 ) );
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( GAP, GAP, GAP, GAP ) );
		this.add( this.assetsPane, java.awt.BorderLayout.CENTER );
	}
	protected AssetsPane createAssetsPane( java.io.File rootDirectory ) {
		java.io.InputStream is = AbstractGalleryBrowser.class.getResourceAsStream( "images/folder.png" );
		java.awt.Image image = edu.cmu.cs.dennisc.image.ImageUtilities.read( edu.cmu.cs.dennisc.image.ImageUtilities.PNG_CODEC_NAME, is );
		javax.swing.ImageIcon folderIcon = new javax.swing.ImageIcon( image );
		javax.swing.ImageIcon folderIconSmall = new javax.swing.ImageIcon( image.getScaledInstance( 16, 16, java.awt.Image.SCALE_SMOOTH ) );
		
		return new AssetsPane( rootDirectory, folderIcon, folderIconSmall ) {
			@Override
			protected String getTextFor( java.io.File file, boolean isRequestedByPath ) {
				return AbstractGalleryBrowser.this.getTextFor( file, isRequestedByPath );
			}
			@Override
			protected void handleFileActivationFromThumbnails( java.io.File file ) {
				super.handleFileActivationFromThumbnails( file );
				if( file.isDirectory() ) {
					//pass
				} else {
					AbstractGalleryBrowser.this.handleFileActivation( file );
				}
			}
		};
	}
	
	protected java.io.File getRootDirectory() {
		return this.assetsPane.getPathControl().getRootDirectory();
	}
	
	protected abstract void handleFileActivation( java.io.File file );
	protected String getAdornedTextFor( String name, boolean isDirectory, boolean isRequestedByPath ) {
		String rv;
		if( isRequestedByPath ) {
			rv = name;
		} else {
			if( isDirectory ) {
				rv = "<html><i>package:</i> <b>" + name + "</b></html>"; 
			} else {
				rv = "<html><i>class:</i> <b>" + name + "</b></html>"; 
			}
		}
		return rv;
	}
	protected final String getTextFor( java.io.File file, boolean isRequestedByPath ) {
		String name = file.getName();
		if( file.isDirectory() ) {
			//pass
		} else {
			name = name.substring( 0, name.length() - 4 );
		}
		return this.getAdornedTextFor( name, file.isDirectory(), isRequestedByPath );
	}
}
