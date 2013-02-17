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

import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.player.MediaPlayer;

/**
 * @author Dennis Cosgrove
 */
public class VlcjVideoPlayer implements edu.cmu.cs.dennisc.video.VideoPlayer {
	private final uk.co.caprica.vlcj.player.MediaPlayerEventListener mediaPlayerEventListener = new uk.co.caprica.vlcj.player.MediaPlayerEventListener() {
		public void mediaChanged(MediaPlayer mediaPlayer, libvlc_media_t media, String mrl) {
		}

		public void opening(MediaPlayer mediaPlayer) {
		}

		public void buffering(MediaPlayer mediaPlayer, float newCache) {
		}

		public void playing(MediaPlayer mediaPlayer) {
		}

		public void paused(MediaPlayer mediaPlayer) {
		}

		public void stopped(MediaPlayer mediaPlayer) {
		}

		public void forward(MediaPlayer mediaPlayer) {
		}

		public void backward(MediaPlayer mediaPlayer) {
		}

		public void finished(MediaPlayer mediaPlayer) {
			fireFinished();
		}

		public void timeChanged(MediaPlayer mediaPlayer, long newTime) {
		}

		public void positionChanged(MediaPlayer mediaPlayer, float newPosition) {
			firePositionChanged( newPosition );
		}

		public void seekableChanged(MediaPlayer mediaPlayer, int newSeekable) {
		}

		public void pausableChanged(MediaPlayer mediaPlayer, int newSeekable) {
		}

		public void titleChanged(MediaPlayer mediaPlayer, int newTitle) {
		}

		public void snapshotTaken(MediaPlayer mediaPlayer, String filename) {
		}

		public void lengthChanged(MediaPlayer mediaPlayer, long newLength) {
		}

		public void videoOutput(MediaPlayer mediaPlayer, int newCount) {
		}

		public void error(MediaPlayer mediaPlayer) {
		}

		public void mediaMetaChanged(MediaPlayer mediaPlayer, int metaType) {
		}

		public void mediaSubItemAdded(MediaPlayer mediaPlayer, libvlc_media_t subItem) {
		}

		public void mediaDurationChanged(MediaPlayer mediaPlayer, long newDuration) {
		}

		public void mediaParsedChanged(MediaPlayer mediaPlayer, int newStatus) {
		}

		public void mediaFreed(MediaPlayer mediaPlayer) {
		}

		public void mediaStateChanged(MediaPlayer mediaPlayer, int newState) {
		}

		public void newMedia(MediaPlayer mediaPlayer) {
		}

		public void subItemPlayed(MediaPlayer mediaPlayer, int subItemIndex) {
		}

		public void subItemFinished(MediaPlayer mediaPlayer, int subItemIndex) {
		}

		public void endOfSubItems(MediaPlayer mediaPlayer) {
		}
		
	};
	private final java.util.List<edu.cmu.cs.dennisc.video.event.MediaListener> mediaListeners = new java.util.concurrent.CopyOnWriteArrayList<edu.cmu.cs.dennisc.video.event.MediaListener>();
	private final uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent embeddedMediaPlayerComponent;
	public VlcjVideoPlayer() {
		this.embeddedMediaPlayerComponent = new uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent();
		this.embeddedMediaPlayerComponent.getMediaPlayer().addMediaPlayerEventListener( this.mediaPlayerEventListener );
	}
	
	private void firePositionChanged( float position ) {
		for( edu.cmu.cs.dennisc.video.event.MediaListener mediaListener : mediaListeners ) {
			mediaListener.positionChanged( position );
		}
	}
	private void fireFinished() {
		for( edu.cmu.cs.dennisc.video.event.MediaListener mediaListener : mediaListeners ) {
			mediaListener.finished();
		}
	}
	
	public void addMediaListener(edu.cmu.cs.dennisc.video.event.MediaListener listener) {
		this.mediaListeners.add( listener );
	}
	
	public void removeMediaListener(edu.cmu.cs.dennisc.video.event.MediaListener listener) {
		this.mediaListeners.add( listener );
	}
	
	public java.awt.Canvas getVideoSurface() {
		return this.embeddedMediaPlayerComponent.getVideoSurface();
	}
	
	public void prepareMedia( java.io.File file ) {
		this.embeddedMediaPlayerComponent.getMediaPlayer().prepareMedia(file.getAbsolutePath());
	}

	public boolean isPlayable() {
		return this.embeddedMediaPlayerComponent.getMediaPlayer().isPlayable();
	}
	
	public boolean isPlaying() {
		return this.embeddedMediaPlayerComponent.getMediaPlayer().isPlaying();
	}
	
	public void playResume() {
		this.embeddedMediaPlayerComponent.getMediaPlayer().start();
	}
		
	public void pause() {
		this.embeddedMediaPlayerComponent.getMediaPlayer().pause();
	}
	
	public void setPosition(float position) {
		MediaPlayer mediaPlayer = this.embeddedMediaPlayerComponent.getMediaPlayer();
		if( mediaPlayer.isSeekable() ) {
			//pass
		} else {
			mediaPlayer.start();
			mediaPlayer.pause();
		}
		mediaPlayer.setPosition( position );
	}
}
