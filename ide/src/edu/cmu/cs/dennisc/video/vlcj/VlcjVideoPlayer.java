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

import uk.co.caprica.vlcj.player.MediaPlayer;

/**
 * @author Dennis Cosgrove
 */
public class VlcjVideoPlayer implements edu.cmu.cs.dennisc.video.VideoPlayer {
	private final uk.co.caprica.vlcj.player.MediaPlayerEventListener mediaPlayerEventListener = new uk.co.caprica.vlcj.player.MediaPlayerEventListener() {
		public void mediaChanged( MediaPlayer mediaPlayer, uk.co.caprica.vlcj.binding.internal.libvlc_media_t media, String mrl ) {
			//edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "mediaChanged", mediaPlayer, media, mrl );
			//uk.co.caprica.vlcj.player.MediaDetails mediaDetails = mediaPlayer.getMediaDetails();
			fireMediaChanged();
		}

		public void opening( MediaPlayer mediaPlayer ) {
			fireOpening();
		}

		public void buffering( MediaPlayer mediaPlayer, float newCache ) {
		}

		public void playing( MediaPlayer mediaPlayer ) {
			firePlaying();
		}

		public void paused( MediaPlayer mediaPlayer ) {
			firePaused();
		}

		public void stopped( MediaPlayer mediaPlayer ) {
			fireStopped();
		}

		public void forward( MediaPlayer mediaPlayer ) {
		}

		public void backward( MediaPlayer mediaPlayer ) {
		}

		public void finished( MediaPlayer mediaPlayer ) {
			fireFinished();
		}

		public void timeChanged( MediaPlayer mediaPlayer, long newTime ) {
			//edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "time changed", newTime );
		}

		public void positionChanged( MediaPlayer mediaPlayer, float newPosition ) {
			firePositionChanged( newPosition );
		}

		public void seekableChanged( MediaPlayer mediaPlayer, int newSeekable ) {
		}

		public void pausableChanged( MediaPlayer mediaPlayer, int newSeekable ) {
		}

		public void titleChanged( MediaPlayer mediaPlayer, int newTitle ) {
		}

		public void snapshotTaken( MediaPlayer mediaPlayer, String filename ) {
		}

		public void lengthChanged( MediaPlayer mediaPlayer, long newLength ) {
			fireLengthChanged( newLength );
		}

		public void videoOutput( MediaPlayer mediaPlayer, int newCount ) {
			fireVideoOutput( newCount );
		}

		public void error( MediaPlayer mediaPlayer ) {
			fireError();
		}

		public void mediaMetaChanged( MediaPlayer mediaPlayer, int metaType ) {
		}

		public void mediaSubItemAdded( MediaPlayer mediaPlayer, uk.co.caprica.vlcj.binding.internal.libvlc_media_t subItem ) {
		}

		public void mediaDurationChanged( MediaPlayer mediaPlayer, long newDuration ) {
		}

		public void mediaParsedChanged( MediaPlayer mediaPlayer, int newStatus ) {
		}

		public void mediaFreed( MediaPlayer mediaPlayer ) {
		}

		public void mediaStateChanged( MediaPlayer mediaPlayer, int newState ) {
		}

		public void newMedia( MediaPlayer mediaPlayer ) {
			fireNewMedia();
		}

		public void subItemPlayed( MediaPlayer mediaPlayer, int subItemIndex ) {
		}

		public void subItemFinished( MediaPlayer mediaPlayer, int subItemIndex ) {
		}

		public void endOfSubItems( MediaPlayer mediaPlayer ) {
		}

	};
	private final java.util.List<edu.cmu.cs.dennisc.video.event.MediaListener> mediaListeners = new java.util.concurrent.CopyOnWriteArrayList<edu.cmu.cs.dennisc.video.event.MediaListener>();
	private final uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent embeddedMediaPlayerComponent;

	private edu.cmu.cs.dennisc.java.awt.Painter painter;
	private String mediaPath = null;

	public VlcjVideoPlayer() {
		this.embeddedMediaPlayerComponent = new uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent() {
			@Override
			protected java.awt.Canvas onGetCanvas() {
				java.awt.Canvas rv;
				if( uk.co.caprica.vlcj.runtime.RuntimeUtil.isWindows() ) {
					rv = new uk.co.caprica.vlcj.runtime.windows.WindowsCanvas() {
						@Override
						public void paint( java.awt.Graphics g ) {
							super.paint( g );
							if( painter != null ) {
								painter.paint( (java.awt.Graphics2D)g, this.getWidth(), this.getHeight() );
							}
						}
					};
				} else {
					rv = new java.awt.Canvas() {
						@Override
						public void paint( java.awt.Graphics g ) {
							super.paint( g );
							if( painter != null ) {
								painter.paint( (java.awt.Graphics2D)g, this.getWidth(), this.getHeight() );
							}
						}
					};
				}
				rv.setBackground( java.awt.Color.BLACK );
				return rv;
			}
		};
		uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer mediaPlayer = this.embeddedMediaPlayerComponent.getMediaPlayer();
		mediaPlayer.setEnableMouseInputHandling( false );
		mediaPlayer.setEnableKeyInputHandling( false );
		mediaPlayer.addMediaPlayerEventListener( this.mediaPlayerEventListener );
	}

	private void fireNewMedia() {
		for( edu.cmu.cs.dennisc.video.event.MediaListener mediaListener : mediaListeners ) {
			mediaListener.newMedia( this );
		}
	}

	private void fireVideoOutput( int count ) {
		for( edu.cmu.cs.dennisc.video.event.MediaListener mediaListener : mediaListeners ) {
			mediaListener.videoOutput( this, count );
		}
	}

	private void firePositionChanged( float position ) {
		for( edu.cmu.cs.dennisc.video.event.MediaListener mediaListener : mediaListeners ) {
			mediaListener.positionChanged( this, position );
		}
	}

	private void fireLengthChanged( long length ) {
		for( edu.cmu.cs.dennisc.video.event.MediaListener mediaListener : mediaListeners ) {
			mediaListener.lengthChanged( this, length );
		}
	}

	private void fireMediaChanged() {
		for( edu.cmu.cs.dennisc.video.event.MediaListener mediaListener : mediaListeners ) {
			mediaListener.mediaChanged( this );
		}
	}

	private void fireOpening() {
		for( edu.cmu.cs.dennisc.video.event.MediaListener mediaListener : mediaListeners ) {
			mediaListener.opening( this );
		}
	}

	private void firePlaying() {
		for( edu.cmu.cs.dennisc.video.event.MediaListener mediaListener : mediaListeners ) {
			mediaListener.playing( this );
		}
	}

	private void firePaused() {
		for( edu.cmu.cs.dennisc.video.event.MediaListener mediaListener : mediaListeners ) {
			mediaListener.paused( this );
		}
	}

	private void fireStopped() {
		for( edu.cmu.cs.dennisc.video.event.MediaListener mediaListener : mediaListeners ) {
			mediaListener.stopped( this );
		}
	}

	private void fireFinished() {
		for( edu.cmu.cs.dennisc.video.event.MediaListener mediaListener : mediaListeners ) {
			mediaListener.finished( this );
		}
	}

	private void fireError() {
		for( edu.cmu.cs.dennisc.video.event.MediaListener mediaListener : mediaListeners ) {
			mediaListener.error( this );
		}
	}

	public void addMediaListener( edu.cmu.cs.dennisc.video.event.MediaListener listener ) {
		this.mediaListeners.add( listener );
	}

	public void removeMediaListener( edu.cmu.cs.dennisc.video.event.MediaListener listener ) {
		this.mediaListeners.add( listener );
	}

	public java.awt.Canvas getVideoSurface() {
		return this.embeddedMediaPlayerComponent.getVideoSurface();
	}

	public edu.cmu.cs.dennisc.java.awt.Painter getPainter() {
		return this.painter;
	}

	public void setPainter( edu.cmu.cs.dennisc.java.awt.Painter painter ) {
		this.painter = painter;
	}

	public void prepareMedia( java.net.URI uri ) {
		if( uri != null ) {
			String scheme = uri.getScheme();
			if( scheme.equalsIgnoreCase( "file" ) ) {
				java.io.File file = new java.io.File( uri );
				this.mediaPath = file.getAbsolutePath();
			} else {
				this.mediaPath = uri.toString();
			}
			uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer mediaPlayer = this.embeddedMediaPlayerComponent.getMediaPlayer();
			mediaPlayer.prepareMedia( this.mediaPath );
		} else {
			System.err.println( "uri is null" );
		}
	}

	public boolean isPlayable() {
		MediaPlayer mediaPlayer = this.embeddedMediaPlayerComponent.getMediaPlayer();
		return mediaPlayer.isPlayable();
	}

	public boolean isPlaying() {
		MediaPlayer mediaPlayer = this.embeddedMediaPlayerComponent.getMediaPlayer();
		return mediaPlayer.isPlaying();
	}

	public void playResume() {
		MediaPlayer mediaPlayer = this.embeddedMediaPlayerComponent.getMediaPlayer();
		if( mediaPlayer.isPlayable() ) {
			mediaPlayer.play();
		} else {
			//			if( mediaPlayer.isSeekable() ) {
			//				mediaPlayer.setPosition( 0.0f );
			//			}
			mediaPlayer.start();
		}
	}

	public void pause() {
		MediaPlayer mediaPlayer = this.embeddedMediaPlayerComponent.getMediaPlayer();
		if( mediaPlayer.canPause() ) {
			mediaPlayer.pause();
		} else {
			System.err.println( "cannot pause " + mediaPlayer );
		}
	}

	public void stop() {
		MediaPlayer mediaPlayer = this.embeddedMediaPlayerComponent.getMediaPlayer();
		mediaPlayer.stop();
	}

	public float getPosition() {
		MediaPlayer mediaPlayer = this.embeddedMediaPlayerComponent.getMediaPlayer();
		return mediaPlayer.getPosition();
	}

	public void setPosition( float position ) {
		MediaPlayer mediaPlayer = this.embeddedMediaPlayerComponent.getMediaPlayer();
		if( mediaPlayer.isSeekable() ) {
			//pass
		} else {
			//System.err.println( "cannot setPosition " + position + " " + mediaPlayer + " starting." );
			mediaPlayer.start();
			//mediaPlayer.pause();
		}
		mediaPlayer.setPosition( position );
	}

	public long getLengthInMilliseconds() {
		MediaPlayer mediaPlayer = this.embeddedMediaPlayerComponent.getMediaPlayer();
		return mediaPlayer.getLength();
	}

	public float getVolume() {
		MediaPlayer mediaPlayer = this.embeddedMediaPlayerComponent.getMediaPlayer();
		return mediaPlayer.getVolume() * 0.01f;
	}

	public void setVolume( float volume ) {
		MediaPlayer mediaPlayer = this.embeddedMediaPlayerComponent.getMediaPlayer();
		mediaPlayer.setVolume( Math.round( volume * 100 ) ); // 200?
	}

	public boolean isMuted() {
		MediaPlayer mediaPlayer = this.embeddedMediaPlayerComponent.getMediaPlayer();
		return mediaPlayer.isMute();
	}

	public void setMuted( boolean isMuted ) {
		MediaPlayer mediaPlayer = this.embeddedMediaPlayerComponent.getMediaPlayer();
		mediaPlayer.mute( isMuted );
	}

	public boolean writeSnapshot( java.io.File file ) {
		MediaPlayer mediaPlayer = this.embeddedMediaPlayerComponent.getMediaPlayer();
		double seconds = ( (double)mediaPlayer.getTime() ) / 1000.0;
		try {
			edu.wustl.cse.lookingglass.media.FFmpegImageExtractor.getFrameAt( this.mediaPath, seconds, file );
			return true;
		} catch( Exception e ) {
			return false;
		}
	}

	public java.awt.Image getSnapshot() {
		MediaPlayer mediaPlayer = this.embeddedMediaPlayerComponent.getMediaPlayer();
		double seconds = ( (double)mediaPlayer.getTime() ) / 1000.0;
		return edu.wustl.cse.lookingglass.media.FFmpegImageExtractor.getFrameAt( this.mediaPath, seconds );
	}

	public void release() {
		this.embeddedMediaPlayerComponent.release();
	}

	public java.awt.Dimension getVideoSize() {
		return this.embeddedMediaPlayerComponent.getMediaPlayer().getVideoDimension();
	}
}
