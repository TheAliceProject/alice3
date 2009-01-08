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
package edu.cmu.cs.dennisc.sound;

/**
 * @author Dennis Cosgrove
 */
public class SoundUtilities {
	public static javax.sound.sampled.Clip newClip( java.io.File file ) {
		try {
			javax.sound.sampled.Clip clip = javax.sound.sampled.AudioSystem.getClip();
			javax.sound.sampled.AudioInputStream audioInputStream = javax.sound.sampled.AudioSystem.getAudioInputStream( file );
			clip.open( audioInputStream );
			return clip;
		} catch( Exception e ) {
			throw new RuntimeException( e );
		}
	}
	public static javax.sound.sampled.Clip newClip( String path ) {
		return newClip( new java.io.File( path ) );
	}
	
	public static void playAndDrainClip( javax.sound.sampled.Clip clip ) {
		clip.setFramePosition( 0 );
		clip.start();
		clip.drain();
	}
}
