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
package org.alice.ide.cascade.customfillin;

/**
 * @author Dennis Cosgrove
 */
public abstract class ImportNewResourceFillIn extends edu.cmu.cs.dennisc.cascade.FillIn< edu.cmu.cs.dennisc.alice.ast.ResourceExpression > {
	@Override
	public edu.cmu.cs.dennisc.alice.ast.ResourceExpression getTransientValue() {
		return null;
	}
	protected abstract String getInitialFileText();
	protected abstract String getContentType( String path );
	
	@Override
	public edu.cmu.cs.dennisc.alice.ast.ResourceExpression getValue() {
		java.awt.FileDialog fileDialog = new java.awt.FileDialog( org.alice.ide.IDE.getSingleton() );
		fileDialog.setFilenameFilter( edu.cmu.cs.dennisc.media.MediaFactory.createFilenameFilter( true ) );
		//todo?
		if( edu.cmu.cs.dennisc.lang.SystemUtilities.isWindows() ) {
			fileDialog.setFile( this.getInitialFileText() );
		}
		fileDialog.setMode( java.awt.FileDialog.LOAD );
		fileDialog.setVisible( true );
		//edu.cmu.cs.dennisc.resource.ClassResource classResource = null;
		String filename = fileDialog.getFile();
		if( filename != null ) {
			String contentType = this.getContentType( filename );
			if( contentType != null ) {
				java.io.File directory = new java.io.File( fileDialog.getDirectory() );
				java.io.File file = new java.io.File( directory, filename );

				try {
					byte[] data = edu.cmu.cs.dennisc.io.InputStreamUtilities.getBytes( file );
					
					edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: ImportNewResourceFillIn" );
					
					org.alice.virtualmachine.resources.AudioResource resource = org.alice.virtualmachine.resources.AudioResource.valueOf( java.util.UUID.randomUUID().toString() );
					resource.setName( filename );
					resource.setContentType( contentType );
					resource.setData( data );
	
					org.alice.ide.IDE ide = org.alice.ide.IDE.getSingleton();
					ide.getProject().addResource( resource );
					
					edu.cmu.cs.dennisc.alice.ast.ResourceExpression resourceExpression = new edu.cmu.cs.dennisc.alice.ast.ResourceExpression( org.alice.virtualmachine.resources.AudioResource.class, resource );
					return resourceExpression;
				} catch( java.io.IOException ioe ) {
					throw new edu.cmu.cs.dennisc.cascade.CancelException( "" );			
				}
			} else {
				throw new edu.cmu.cs.dennisc.cascade.CancelException( "" );			
			}
		} else {
			throw new edu.cmu.cs.dennisc.cascade.CancelException( "" );			
		}
	}
	@Override
	protected void addChildren() {
	}
	
	protected abstract String getMenuText();
	@Override
	protected javax.swing.JComponent createMenuProxy() {
		return edu.cmu.cs.dennisc.zoot.ZLabel.acquire( this.getMenuText() );
	}
}
