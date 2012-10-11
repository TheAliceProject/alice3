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
package org.alice.media;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFileFormat.Type;
import javax.sound.sampled.AudioFormat;

import org.alice.media.audio.AudioTrackMixer;
import org.alice.media.audio.ScheduledAudioStream;
import org.monte.media.quicktime.QuickTimeWriter;

import edu.cmu.cs.dennisc.animation.MediaPlayerObserver;
import edu.cmu.cs.dennisc.java.io.FileUtilities;
import edu.cmu.cs.dennisc.media.animation.MediaPlayerAnimation;

/**
 * @author Matt May
 */
public class MattsMovieEncoder extends QuickTimeWriter implements MediaPlayerObserver {

	private List<ScheduledAudioStream> audioStreams = new LinkedList<ScheduledAudioStream>();
	private List<BufferedImage> images = new LinkedList<BufferedImage>();
	private boolean isRunning = false;
	private int framesPerSecond;
	private int frameCount;
	private static float RATE_22 = 22050f;
	private static float RATE_44 = 44100f;
	private static AudioFormat QUICKTIME_AUDIO_FORMAT_PCM = new AudioFormat( AudioFormat.Encoding.PCM_SIGNED, RATE_44, 16, 1, 2, RATE_44, false );

	public MattsMovieEncoder( File file ) throws IOException {
		super( file );
		framesPerSecond = framesPerSecond;
	}

	public void playerStarted( MediaPlayerAnimation playerAnimation, double playTime ) {
		edu.cmu.cs.dennisc.media.Player player = playerAnimation.getPlayer();
		if( player instanceof edu.cmu.cs.dennisc.media.jmf.Player ) {
			edu.cmu.cs.dennisc.media.jmf.Player jmfPlayer = (edu.cmu.cs.dennisc.media.jmf.Player)player;
			ScheduledAudioStream audioStream = new ScheduledAudioStream( jmfPlayer.getAudioResource(), playTime, jmfPlayer.getStartTime(), jmfPlayer.getStopTime(), jmfPlayer.getVolumeLevel() );
			this.audioStreams.add( audioStream );
		}
	}

	public void stop() {
		this.isRunning = false;
		boolean success = false;
		File audioFile = null;
		try {
			audioFile = this.createAudioFile();
			for( BufferedImage image : images ) {
				this.write( 0, image, 1 );
				//				this.writeFrame( image, 1 );
			}
			success = true;
		} catch( Exception e ) {
			e.printStackTrace();
			success = false;
		} finally {
			if( audioFile != null ) {
				FileUtilities.delete( audioFile );
			}
		}
		//		for( EncoderListener l : this.listeners ) {
		//			l.encodingFinished( success );
		//		}

	}

	public void start() {
		this.isRunning = true;
		this.frameCount = 0;
		if( this.getTrackCount() < 1 ) {
			try {
				this.addTrack( VIDEO_ANIMATION );
				//				this.addTrack( VIDEO_JPEG );
				//				this.addTrack( VIDEO_PNG );
				//				this.addTrack( VIDEO_RAW );
				//				this.addTrack( QUICKTIME );
				System.out.println( "go!" );
			} catch( IOException e ) {
				e.printStackTrace();
			}
			//		for( EncoderListener l : this.listeners ) {
			//			l.encodingStarted();
			//		}
		}
	}

	public void addBufferedImage( BufferedImage image ) {
		images.add( image );
	}

	private double movieLength() {
		double totalTime = this.frameCount / this.framesPerSecond;
		return totalTime;
	}

	private File createAudioFile() {
		if( this.audioStreams.size() > 0 ) {
			AudioTrackMixer mixer = new AudioTrackMixer( QUICKTIME_AUDIO_FORMAT_PCM, movieLength() );
			for( ScheduledAudioStream stream : this.audioStreams ) {
				mixer.addScheduledStream( stream );
			}
			try {
				Type type = AudioFileFormat.Type.AU;
				File tempFile = File.createTempFile( "TEMP_soundTrack", type.getExtension() );
				FileOutputStream out = new FileOutputStream( tempFile );
				mixer.write( out );
				out.close();
				return tempFile;
			} catch( Exception e ) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public void setFrameRate( int value ) {
		this.framesPerSecond = value;
	}
}
