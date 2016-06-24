/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/
package org.alice.media.video;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;

import org.alice.media.audio.ScheduledAudioStream;

import edu.cmu.cs.dennisc.media.MediaPlayerObserver;
import edu.cmu.cs.dennisc.media.animation.MediaPlayerAnimation;
import edu.wustl.lookingglass.media.ImagesToWebmEncoder;

/**
 * @author Matt May
 */
public class WebmRecordingAdapter implements MediaPlayerObserver {

	private Integer frameRate;
	private Dimension dimension;

	private org.alice.media.audio.AudioMuxer audioMuxer;
	private ImagesToWebmEncoder encoder;

	public WebmRecordingAdapter() {
	}

	public void setFrameRate( Integer value ) {
		frameRate = value;
	}

	public void setDimension( Dimension dimension ) {
		this.dimension = dimension;
	}

	public File getEncodedVideoFile() {
		return this.encoder != null ? this.encoder.getEncodedVideoFile() : null;
	}

	public void initializeAudioRecording() {
		this.audioMuxer = new org.alice.media.audio.AudioMuxer();
	}

	@Override
	public void mediaPlayerStarted( MediaPlayerAnimation playerAnimation, double playTime ) {
		assert this.audioMuxer != null;
		edu.cmu.cs.dennisc.media.Player player = playerAnimation.getPlayer();
		if( player instanceof edu.cmu.cs.dennisc.media.jmf.Player ) {
			edu.cmu.cs.dennisc.media.jmf.Player jmfPlayer = (edu.cmu.cs.dennisc.media.jmf.Player)player;
			ScheduledAudioStream audioStream = new ScheduledAudioStream( jmfPlayer.getAudioResource(), playTime, jmfPlayer.getStartTime(), jmfPlayer.getStopTime(), jmfPlayer.getVolumeLevel() );
			this.audioMuxer.addAudioStream( audioStream );
		}
	}

	public void startVideoEncoding() {
		assert ( this.encoder == null ) || ( this.encoder.isRunning() == false );
		assert frameRate != null;
		assert dimension != null;

		this.encoder = new ImagesToWebmEncoder( frameRate, dimension );
		synchronized( this.encoder ) {
			this.encoder.start( this.audioMuxer );
		}
	}

	public void stopVideoEncoding() {
		if( this.encoder != null ) {
			synchronized( this.encoder ) {
				if( this.encoder.isRunning() ) {
					this.encoder.stop();
				}
			}

			// Reset the audio compiler, just in case. We want to make sure someone restarts it.
			this.audioMuxer = null;
		}
	}

	public void addBufferedImage( BufferedImage image, boolean isUpsideDown ) {
		if( this.encoder != null ) {
			synchronized( this.encoder ) {
				if( this.encoder.isRunning() ) {
					this.encoder.addBufferedImage( image, isUpsideDown );
				}
			}
		}
	}
}
