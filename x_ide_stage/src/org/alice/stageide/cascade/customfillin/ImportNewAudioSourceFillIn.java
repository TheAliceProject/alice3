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
package org.alice.stageide.cascade.customfillin;

/**
 * @author Dennis Cosgrove
 */
public class ImportNewAudioSourceFillIn extends edu.cmu.cs.dennisc.cascade.FillIn< edu.cmu.cs.dennisc.alice.ast.InstanceCreation > {
	@Override
	public edu.cmu.cs.dennisc.alice.ast.InstanceCreation getTransientValue() {
		return null;
	}
	@Override
	public edu.cmu.cs.dennisc.alice.ast.InstanceCreation getValue() {
		java.awt.FileDialog fileDialog = new java.awt.FileDialog( org.alice.ide.IDE.getSingleton() );
		fileDialog.setFilenameFilter( edu.cmu.cs.dennisc.media.MediaFactory.createFilenameFilter( true ) );
		//todo?
		if( edu.cmu.cs.dennisc.lang.SystemUtilities.isWindows() ) {
			fileDialog.setFile( "*.mp3;*.wav;*.au" );
		}
		fileDialog.setMode( java.awt.FileDialog.LOAD );
		fileDialog.setVisible( true );
		//edu.cmu.cs.dennisc.resource.ClassResource classResource = null;
		String filename = fileDialog.getFile();
		if( filename != null ) {
			String contentType = edu.cmu.cs.dennisc.media.MediaFactory.getContentType( filename );
			if( contentType != null ) {
				java.io.File directory = new java.io.File( fileDialog.getDirectory() );
				java.io.File file = new java.io.File( directory, filename );

				try {
					byte[] data = edu.cmu.cs.dennisc.io.InputStreamUtilities.getBytes( file );
					//edu.cmu.cs.dennisc.alice.resource.FileResource fileResource = new edu.cmu.cs.dennisc.alice.resource.FileResource( file );
					org.alice.virtualmachine.resources.AudioResource audioResource = org.alice.virtualmachine.resources.AudioResource.valueOf( java.util.UUID.randomUUID().toString() );
					audioResource.setName( filename );
					audioResource.setContentType( contentType );
					audioResource.setData( data );
	
					org.alice.ide.IDE ide = org.alice.ide.IDE.getSingleton();
					ide.getProject().addResource( audioResource );
					
					edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava constructor = edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava.get( org.alice.apis.moveandturn.AudioSource.class, org.alice.virtualmachine.resources.AudioResource.class );
					edu.cmu.cs.dennisc.alice.ast.ResourceExpression resourceExpression = new edu.cmu.cs.dennisc.alice.ast.ResourceExpression( org.alice.virtualmachine.resources.AudioResource.class, audioResource );
					edu.cmu.cs.dennisc.alice.ast.AbstractParameter parameter0 = constructor.getParameters().get( 0 );
					edu.cmu.cs.dennisc.alice.ast.Argument argument0 = new edu.cmu.cs.dennisc.alice.ast.Argument( parameter0, resourceExpression );
					return new edu.cmu.cs.dennisc.alice.ast.InstanceCreation( constructor, argument0 );
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
	@Override
	protected javax.swing.JComponent createMenuProxy() {
		return new javax.swing.JLabel( "Import New Audio Source..." );
	}
}
