/**
 * Copyright (c) 2008-2015, Washington University in St. Louis. All rights reserved.
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
 * 3. Products derived from the software may not be called "Looking Glass", nor
 *    may "Looking Glass" appear in their name, without prior written permission
 *    of Washington University in St. Louis.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software
 *    developed by Washington University in St. Louis"
 *
 * 5. The gallery of art assets and animations provided with this software is
 *    contributed by Electronic Arts Inc. and may be used for personal,
 *    non-commercial, and academic use only. Redistributions of any program
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in
 *    The Alice 3.0 Art Gallery License.
 *
 * DISCLAIMER:
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  ANY AND ALL
 * EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A PARTICULAR PURPOSE,
 * TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHORS,
 * COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package edu.wustl.lookingglass.media;

import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.alice.media.audio.AudioMuxer;

import javax.imageio.ImageIO;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Kyle J. Harms
 */
public class ImagesToWebmEncoder {

	private final double frameRate;
	private int frameCount = 0;
	private final Dimension frameDimension;

	public static final String WEBM_EXTENSION = "webm";

	private boolean isRunning = false;
	private boolean success = true;

	private FFmpegProcess ffmpegProcess;
	private AudioMuxer audioMuxer;

	private File encodedVideoFile = null;

	private List<MediaEncoderListener> listeners = new LinkedList<MediaEncoderListener>();

	public ImagesToWebmEncoder( double frameRate, Dimension dimension ) {
		this.frameRate = frameRate;
		this.frameDimension = dimension;
		this.frameCount = -1;

		// ffmpeg requires that the dimensions must be divisible by two.
		assert ( ( frameDimension.getWidth() % 2 ) == 0 ) : "ffmpeg requires dimensions that are divisble by two";
		assert ( ( frameDimension.getHeight() % 2 ) == 0 ) : "ffmpeg requires dimensions that are divisble by two";
	}

	public File getEncodedVideoFile() {
		return this.encodedVideoFile;
	}

	public boolean isRunning() {
		return this.isRunning;
	}

	public void addListener( MediaEncoderListener listener ) {
		if( !this.listeners.contains( listener ) ) {
			this.listeners.add( listener );
		}
	}

	public void removeListener( MediaEncoderListener listener ) {
		this.listeners.remove( listener );
	}

	public boolean start() {
		return start( null );
	}

	public synchronized boolean start( AudioMuxer audioCompiler ) {
		assert this.isRunning == false;

		this.audioMuxer = audioCompiler;
		this.frameCount = -1;
		this.isRunning = true;
		this.success = true;

		try {
			// Don't cache images during this process
			ImageIO.setUseCache( false );

			this.encodedVideoFile = File.createTempFile( "project", "." + WEBM_EXTENSION );
			this.encodedVideoFile.deleteOnExit();
			this.ffmpegProcess = new FFmpegProcess( "-y", "-r", String.format( "%d", (int)this.frameRate ), "-f", "image2pipe", "-vcodec", "ppm", "-i", "-", "-vf", "vflip", "-vcodec", "libvpx", "-quality", "good", "-cpu-used", "0", "-b:v", "500k", "-qmin", "10", "-qmax", "42", "-maxrate", "500k", "-bufsize", "1000k", "-pix_fmt", "yuv420p", this.encodedVideoFile.getAbsolutePath() );
			this.ffmpegProcess.start();
		} catch( IOException e ) {
			handleEncodingError( new FFmpegProcessException( e ) );
		} catch( FFmpegProcessException e ) {
			handleEncodingError( e );
		}

		if( this.isRunning ) {
			for( MediaEncoderListener l : this.listeners )
			{
				l.encodingStarted( this.success );
			}
		}
		return this.success;
	}

	public synchronized void addBufferedImage( BufferedImage frame, boolean isUpsideDown ) {
		assert this.isRunning;
		assert frame != null;
		assert ( frame.getWidth() == this.frameDimension.getWidth() );
		assert ( frame.getHeight() == this.frameDimension.getHeight() );
		assert isUpsideDown : "option \"-vf vflip\" passed to ffmpeg";

		this.frameCount++;
		try {
			OutputStream ffmpegStdOut = this.ffmpegProcess.getProcessOutputStream();
			if( ffmpegStdOut == null )
			{
				return;
			}

			//edu.cmu.cs.dennisc.java.io.InputStreamUtilities.drain( this.ffmpegProcess.getProcess().getInputStream() );
			//edu.cmu.cs.dennisc.java.io.InputStreamUtilities.drain( this.ffmpegProcess.getProcess().getErrorStream() );

			synchronized( ffmpegStdOut ) {
				ImageIO.write( frame, "ppm", ffmpegStdOut );
				ffmpegStdOut.flush();
			}

			for( MediaEncoderListener l : this.listeners )
			{
				l.frameUpdate( this.frameCount, frame );
			}
		} catch( IOException e ) {
			Logger.severe( "failed to write frame " + frame + " to ffmpeg" );
			handleEncodingError( new FFmpegProcessException( e ) );
		}
	}

	public synchronized boolean stop() {
		if( this.isRunning == false ) {
			return this.success;
		}
		this.isRunning = false;

		// Reset image caching back to default
		ImageIO.setUseCache( true );

		int status = this.ffmpegProcess.stop();
		if( status != 0 ) {
			this.success = false;
			Logger.severe( "video encoding failed; status != 0", status );
			handleEncodingError( new FFmpegProcessException( this.ffmpegProcess.getProcessInput(), this.ffmpegProcess.getProcessError() ) );
		}

		this.mixAudioTrack();

		return this.success;
	}

	private double getLength() {
		return this.frameCount / this.frameRate;
	}

	private synchronized void mixAudioTrack() {
		if( this.audioMuxer == null ) {
			return;
		}
		assert this.isRunning == false : "audio mixing is post process";

		if( this.audioMuxer.hasAudioToMix() ) {
			File muxVideoFile = null;
			try {
				muxVideoFile = File.createTempFile( "project", "." + WEBM_EXTENSION );
				muxVideoFile.deleteOnExit();
				FFmpegProcess ffmpegProcess = new FFmpegProcess( "-y", "-i", this.encodedVideoFile.getAbsolutePath(), "-codec:a", "pcm_s16le", "-i", "-", "-codec:v", "copy", "-codec:a", "libvorbis", muxVideoFile.getAbsolutePath() );
				ffmpegProcess.start();
				this.audioMuxer.mixAudioStreams( ffmpegProcess.getProcessOutputStream(), getLength() );
				ffmpegProcess.getProcessOutputStream().flush();
				int status = ffmpegProcess.stop();
				if( status != 0 ) {
					this.success = false;
					muxVideoFile.delete();
					Logger.severe( "audio muxing encoding failed; status != 0", status );
					handleEncodingError( new FFmpegProcessException( ffmpegProcess.getProcessInput(), ffmpegProcess.getProcessError() ) );
				}
				this.encodedVideoFile.delete();
				this.encodedVideoFile = muxVideoFile;
			} catch( IOException e ) {
				this.success = false;
				muxVideoFile.delete();
				handleEncodingError( new FFmpegProcessException( e ) );
			}
		}
	}

	private void handleEncodingError( FFmpegProcessException e ) {
		this.isRunning = false;
		this.success = false;
		throw e;
	}
}
