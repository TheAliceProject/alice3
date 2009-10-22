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
public abstract class AssetsPane extends edu.cmu.cs.dennisc.croquet.swing.BorderPane {
	private ThumbnailsPane thumbnailsPane;
	private PathControl pathControl;

	public AssetsPane( java.io.File rootDirectory, javax.swing.Icon folderIcon, javax.swing.ImageIcon folderIconSmall ) {
		this.thumbnailsPane = this.createThumbnailsPane();
		this.thumbnailsPane.setFolderIcon( folderIcon );
		this.pathControl = this.createPathControl( rootDirectory );
		this.pathControl.setFolderIconSmall( folderIconSmall );
		this.add( this.pathControl, java.awt.BorderLayout.NORTH );
		this.add( this.thumbnailsPane, java.awt.BorderLayout.CENTER );
	}
	protected PathControl createPathControl( java.io.File rootDirectory ) {
		return new PathControl( rootDirectory ) {
			@Override
			public void setCurrentDirectory(java.io.File nextDirectory) {
				super.setCurrentDirectory( nextDirectory );
				AssetsPane.this.handleDirectoryActivationFromPathControl( nextDirectory );
			}
			@Override
			protected void handleSelectFile( java.io.File file ) {
				assert file.isFile();
				//todo: remove?
				this.setCurrentDirectory( file.getParentFile() );
				AssetsPane.this.handleFileActivationFromThumbnails( file );
			}
			@Override
			protected String getTextFor( java.io.File file ) {
				return AssetsPane.this.getTextFor( file, true );
			}
		};
	}
	public PathControl getPathControl() {
		return this.pathControl;
	}
	
	protected ThumbnailsPane createThumbnailsPane() {
		return new ThumbnailsPane() {
			@Override
			protected void handleFileActivation( java.io.File file ) {
				AssetsPane.this.handleFileActivationFromThumbnails( file );
			}
			@Override
			protected String getTextFor( java.io.File file ) {
				return AssetsPane.this.getTextFor( file, false );
			}
			@Override
			protected void handleBackSpaceKey() {
				AssetsPane.this.pathControl.setCurrentDirectoryUpOneLevelIfAppropriate();
			}
		};
	}
	protected abstract String getTextFor( java.io.File file, boolean isRequestedByPath );
	protected void handleFileActivationFromThumbnails( java.io.File file ) {
		if( file.isDirectory() ) {
			this.pathControl.setCurrentDirectory( file );
		}
	}
	protected void handleDirectoryActivationFromPathControl( java.io.File directory ) {
		this.thumbnailsPane.setDirectory( directory );
	}
}
