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
package edu.cmu.cs.dennisc.media;

/**
 * @author Dennis Cosgrove
 */
public class Manager {
	private static java.util.Map< org.alice.virtualmachine.Resource, javax.media.protocol.DataSource > resourceToDataSourceMap = new java.util.HashMap< org.alice.virtualmachine.Resource, javax.media.protocol.DataSource >();
	private static java.util.Map< String, String > extensionToContentTypeMap;
	static {
		System.out.print( "Attempting to register mp3 capability... " );
		com.sun.media.codec.audio.mp3.JavaDecoder.main( new String[] {} );
		Manager.extensionToContentTypeMap = new java.util.HashMap< String, String >();
		Manager.extensionToContentTypeMap.put( "mp3", javax.media.protocol.FileTypeDescriptor.MPEG_AUDIO );
		Manager.extensionToContentTypeMap.put( "wav", javax.media.protocol.FileTypeDescriptor.WAVE );
		Manager.extensionToContentTypeMap.put( "au", javax.media.protocol.FileTypeDescriptor.BASIC_AUDIO );
	}

	public static String getContentType( String path ) {
		String extension = edu.cmu.cs.dennisc.io.FileUtilities.getExtension( path );
		String contentType = Manager.extensionToContentTypeMap.get( extension.toLowerCase() );
		return contentType;
	}
	public static boolean isAcceptableContentType( String contentType ) {
		return Manager.extensionToContentTypeMap.containsValue( contentType );
	}
	
	public static java.io.FilenameFilter createFilenameFilter( final boolean areDirectoriesAccepted ) {
		return new java.io.FilenameFilter() {
			public boolean accept( java.io.File dir, String name ) {
				java.io.File file = new java.io.File( dir, name );
				if( file.isDirectory() ) {
					return areDirectoriesAccepted;
				} else {
					return getContentType( name ) != null;
				}
			}
		};
	}
	
	private static javax.media.Player getPlayer( org.alice.virtualmachine.Resource resource ) {
		assert resource != null;
		javax.media.protocol.DataSource dataSource = Manager.resourceToDataSourceMap.get( resource );
		if( dataSource != null ) {
			//pass
		} else {
			dataSource = new edu.cmu.cs.dennisc.media.protocol.ByteArrayDataSource( resource.getData(), resource.getContentType() );
			Manager.resourceToDataSourceMap.put( resource, dataSource );
		}
		try {
			return javax.media.Manager.createPlayer( dataSource );
		} catch( javax.media.NoPlayerException npe ) {
			throw new RuntimeException( resource.toString(), npe );
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( resource.toString(), ioe );
		}
	}
	public static MediaPlayerAnimation createMediaPlayerAnimation( org.alice.virtualmachine.Resource resource, double fromTime, double toTime ) {
		return new MediaPlayerAnimation( new Player( getPlayer( resource ), fromTime, toTime ) );
	}

}
