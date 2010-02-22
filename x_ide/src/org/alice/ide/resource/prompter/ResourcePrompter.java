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
package org.alice.ide.resource.prompter;

//todo: rename
/**
 * @author Dennis Cosgrove
 */
public abstract class ResourcePrompter<E extends org.alice.virtualmachine.Resource> {
	protected abstract String getInitialFileText();
	protected abstract E createResourceFromFile( java.io.File file ) throws java.io.IOException;
	protected abstract String getFileDialogTitle();
	public E promptUserForResource( java.awt.Component owner ) throws java.io.IOException {
		java.awt.FileDialog fileDialog = new java.awt.FileDialog( org.alice.ide.IDE.getSingleton() );
		fileDialog.setFilenameFilter( org.alice.virtualmachine.resources.AudioResource.createFilenameFilter( true ) );
		fileDialog.setTitle( this.getFileDialogTitle() );
		//todo?
		if( edu.cmu.cs.dennisc.lang.SystemUtilities.isWindows() ) {
			fileDialog.setFile( this.getInitialFileText() );
		}
		fileDialog.setMode( java.awt.FileDialog.LOAD );
		fileDialog.setVisible( true );
		String filename = fileDialog.getFile();
		if( filename != null ) {
			java.io.File directory = new java.io.File( fileDialog.getDirectory() );
			java.io.File file = new java.io.File( directory, filename );
			return this.createResourceFromFile( file );
		} else {
			return null;			
		}
	}
}
