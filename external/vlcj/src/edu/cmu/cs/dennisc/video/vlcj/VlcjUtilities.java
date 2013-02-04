/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may 
 *    "Alice" appear in their name, without prior written permission of 
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software 
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is 
 *    contributed by Electronic Arts Inc. and may be used for personal, 
 *    non-commercial, and academic use only. Redistributions of any program 
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in 
 *    The Alice 3.0 Art Gallery License.
 * 
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A 
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO 
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package edu.cmu.cs.dennisc.video.vlcj;

/**
 * @author Dennis Cosgrove
 */
public class VlcjUtilities {
	private static boolean isInitializationAttempted = false;
	private static boolean isInitialized = false;

	private static void initializeIfNecessary() {
		if( isInitializationAttempted ) {
			//pass
		} else {
			isInitializationAttempted = true;
			String lowercaseOSName = System.getProperty( "os.name" ).toLowerCase();
			if( lowercaseOSName.contains( "windows" ) ) {
				//todo
				com.sun.jna.NativeLibrary.addSearchPath( uk.co.caprica.vlcj.runtime.RuntimeUtil.getLibVlcLibraryName(), "c:/Program Files/VideoLAN/VLC/" );
			}
			com.sun.jna.Native.loadLibrary( uk.co.caprica.vlcj.runtime.RuntimeUtil.getLibVlcLibraryName(), uk.co.caprica.vlcj.binding.LibVlc.class );
			isInitialized = true;
		}
	}

	public static java.awt.Component createEmbeddedMediaPlayerComponent() {
		initializeIfNecessary();
		if( isInitialized ) {
			return new uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent() {
				@Override
				public void error( uk.co.caprica.vlcj.player.MediaPlayer mediaPlayer ) {
					super.error( mediaPlayer );
					System.out.println( mediaPlayer );
				}

				@Override
				public void newMedia( uk.co.caprica.vlcj.player.MediaPlayer mediaPlayer ) {
					super.newMedia( mediaPlayer );
					System.out.println( "newMedia: " + mediaPlayer );
				}

				@Override
				public void playing( uk.co.caprica.vlcj.player.MediaPlayer mediaPlayer ) {
					super.playing( mediaPlayer );
					System.out.println( "playing: " + mediaPlayer );
				}

				@Override
				public void finished( uk.co.caprica.vlcj.player.MediaPlayer mediaPlayer ) {
					super.finished( mediaPlayer );
					System.out.println( "finished: " + mediaPlayer );
				}

				@Override
				public void positionChanged( uk.co.caprica.vlcj.player.MediaPlayer mediaPlayer, float newPosition ) {
					super.positionChanged( mediaPlayer, newPosition );
					System.out.println( "positionChanged: " + mediaPlayer + " " + newPosition );
				}

				@Override
				public void stopped( uk.co.caprica.vlcj.player.MediaPlayer mediaPlayer ) {
					super.stopped( mediaPlayer );
					System.out.println( "stopped: " + mediaPlayer );
				}
			};
		} else {
			return new javax.swing.JLabel( "unable to initialize vlcj" );
		}
	}
	
	public static void playMedia( java.awt.Component component, String path ) {
		if( component instanceof uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent ) {
			uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent  embeddedMediaPlayerComponent = (uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent )component;
			embeddedMediaPlayerComponent.getMediaPlayer().playMedia( path );
		} else {
			System.err.println( "playMedia: " + path );
		}
	}
}
