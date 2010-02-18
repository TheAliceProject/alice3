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
package edu.cmu.cs.dennisc.media.jmf;

/**
 * @author Dennis Cosgrove
 */
public class MediaFactory extends edu.cmu.cs.dennisc.media.MediaFactory {
	private java.util.Map< org.alice.virtualmachine.resources.AudioResource, javax.media.protocol.DataSource > audioResourceToDataSourceMap = new java.util.HashMap< org.alice.virtualmachine.resources.AudioResource, javax.media.protocol.DataSource >();
	static {
		System.out.print( "Attempting to register mp3 capability... " );
		com.sun.media.codec.audio.mp3.JavaDecoder.main( new String[] {} );
		edu.cmu.cs.dennisc.javax.media.renderer.audio.FixedJavaSoundRenderer.usurpControlFromJavaSoundRenderer();
	}

	private static MediaFactory singleton;
	static {
		MediaFactory.singleton = new MediaFactory();
	}
	public static MediaFactory getSingleton() {
		return MediaFactory.singleton;
	}

	private MediaFactory() {
	}
	
	public org.alice.virtualmachine.resources.AudioResource createAudioResource( java.io.File file ) throws java.io.IOException {
		String contentType = org.alice.virtualmachine.resources.AudioResource.getContentType( file );
		if( contentType != null ) {
			final org.alice.virtualmachine.resources.AudioResource rv = new org.alice.virtualmachine.resources.AudioResource( file, contentType );
			Runnable runnable = new Runnable() {
				public void run() {
					Player player = new Player( acquirePlayer( rv ), 1.0, 0.0, Double.NaN );
					player.realize();
					rv.setDuration( player.getDuration() );
				}
			};
			final boolean USE_THREAD_JUST_TO_BE_SORT_OF_SAFE = true;
			if( USE_THREAD_JUST_TO_BE_SORT_OF_SAFE ) {
				new Thread( runnable ).start();
			} else {
				runnable.run();
			}
			return rv;			
		} else {
			throw new RuntimeException( "content type not found for " + file );
		}
	}
	
	private javax.media.Player acquirePlayer( org.alice.virtualmachine.resources.AudioResource audioResource ) {
		assert audioResource != null;
		javax.media.protocol.DataSource dataSource = this.audioResourceToDataSourceMap.get( audioResource );
		if( dataSource != null ) {
			//pass
		} else {
			dataSource = new edu.cmu.cs.dennisc.javax.media.protocol.ByteArrayDataSource( audioResource.getData(), audioResource.getContentType() );
			this.audioResourceToDataSourceMap.put( audioResource, dataSource );
		}
		try {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "MediaFactory.acquirePlayer", audioResource );
			return javax.media.Manager.createPlayer( dataSource );
		} catch( javax.media.NoPlayerException npe ) {
			throw new RuntimeException( audioResource.toString(), npe );
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( audioResource.toString(), ioe );
		}
	}
	@Override
	public Player createPlayer( org.alice.virtualmachine.resources.AudioResource audioResource, double volume, double startTime, double stopTime ) {
		Player player = new Player( acquirePlayer( audioResource ), volume, startTime, stopTime );
		if( Double.isNaN( audioResource.getDuration() ) ) {
			player.realize();
			audioResource.setDuration( player.getDuration() );
		}
		return player;
	}
}
