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
package org.alice.ide.video.preview.views;

import edu.cmu.cs.dennisc.java.awt.ColorUtilities;
import edu.cmu.cs.dennisc.java.awt.GraphicsContext;
import edu.cmu.cs.dennisc.java.awt.GraphicsUtilities;
import edu.cmu.cs.dennisc.java.awt.Painter;
import edu.cmu.cs.dennisc.java.util.Objects;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.video.VideoPlayer;
import edu.cmu.cs.dennisc.video.VideoUtilities;
import edu.cmu.cs.dennisc.video.event.MediaListener;
import org.alice.ide.video.preview.VideoComposite;
import org.alice.ide.video.preview.views.events.ThumbListener;
import org.lgna.croquet.views.BorderPanel;
import org.lgna.croquet.views.Label;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

class PlayCanvasIcon implements Icon {
	public static void paint( Component c, Graphics g, ButtonModel buttonModel, Stroke stroke, int x, int y, int width, int height ) {
		if( buttonModel.isEnabled() ) {
			if( buttonModel.isSelected() ) {
				//pass
			} else {
				Graphics2D g2 = (Graphics2D)g;
				GraphicsContext gc = GraphicsContext.getInstanceAndPushGraphics( g2 );
				try {
					gc.pushAndSetAntialiasing( true );
					gc.pushStroke();
					gc.pushTransform();
					gc.pushPaint();
					g.translate( x, y );
					if( buttonModel.isRollover() ) {
						float xCenter = width * 0.5f;
						float yCenter = height * 0.5f;
						g2.setPaint( new Color( 255, 255, 191, 9 ) );
						for( float radius = 50.0f; radius < 80.0f; radius += 4.0f ) {
							Ellipse2D.Float shape = new Ellipse2D.Float( xCenter - radius, yCenter - radius, radius + radius, radius + radius );
							g2.fill( shape );
						}
					}

					g2.setStroke( stroke );
					RoundRectangle2D.Float rr = new RoundRectangle2D.Float( 0, 0, width, height, width * 0.6f, height * 0.6f );
					g2.setColor( ColorUtilities.createGray( 70 ) );
					g2.fill( rr );
					g2.setColor( ColorUtilities.createGray( 220 ) );
					g2.draw( rr );

					int w = (int)( width * 0.4 );
					int h = (int)( height * 0.45 );
					int xFudge = width / 20;
					GraphicsUtilities.fillTriangle( g2, GraphicsUtilities.Heading.EAST, ( ( width - w ) / 2 ) + xFudge, ( height - h ) / 2, w, h );
				} finally {
					gc.popAll();
				}
			}
		}
	}

	private final Stroke stroke;
	private final int width;
	private final int height;

	public PlayCanvasIcon( int width, int height ) {
		this.width = width;
		this.height = height;
		int size = Math.max( this.width, this.height );
		this.stroke = new BasicStroke( size / 25.0f );
	}

	@Override
	public int getIconWidth() {
		return this.width;
	}

	@Override
	public int getIconHeight() {
		return this.height;
	}

	@Override
	public void paintIcon( Component c, Graphics g, int x, int y ) {
		if( c instanceof AbstractButton ) {
			AbstractButton button = (AbstractButton)c;
			ButtonModel buttonModel = button.getModel();
			paint( c, g, buttonModel, this.stroke, x, y, this.width, this.height );
		}
	}
}

class PlayButtonIcon implements Icon {
	private final Dimension size;

	public PlayButtonIcon( Dimension size ) {
		this.size = size;
	}

	@Override
	public int getIconWidth() {
		return this.size.width;
	}

	@Override
	public int getIconHeight() {
		return this.size.width;
	}

	@Override
	public void paintIcon( Component c, Graphics g, int x, int y ) {
		AbstractButton b = (AbstractButton)c;
		ButtonModel model = b.getModel();
		Color color = b.getForeground();
		if( model.isRollover() ) {
			color = color.brighter();
		}
		g.setColor( color );
		Object prevAntialiasing = GraphicsUtilities.setAntialiasing( g, RenderingHints.VALUE_ANTIALIAS_ON );
		try {
			GraphicsUtilities.fillTriangle( g, GraphicsUtilities.Heading.EAST, x, y, size.width, size.height );
		} finally {
			GraphicsUtilities.setAntialiasing( g, prevAntialiasing );
		}
	}
}

class PauseButtonIcon implements Icon {
	private final Dimension size;

	public PauseButtonIcon( Dimension size ) {
		this.size = size;
	}

	@Override
	public int getIconWidth() {
		return this.size.width;
	}

	@Override
	public int getIconHeight() {
		return this.size.width;
	}

	@Override
	public void paintIcon( Component c, Graphics g, int x, int y ) {
		AbstractButton b = (AbstractButton)c;
		ButtonModel model = b.getModel();
		Color color = b.getForeground();
		if( model.isRollover() ) {
			color = color.brighter();
		}
		g.setColor( color );
		g.fillRect( x, y, 4, this.getIconHeight() );
		g.fillRect( ( x + this.getIconWidth() ) - 6, y, 4, this.getIconHeight() );
	}
}

/**
 * @author Dennis Cosgrove
 */
public class VideoView extends BorderPanel {
	private static final SimpleDateFormat FORMAT = new SimpleDateFormat( "m:ss" );

	private URI uri;
	private boolean isErrorFreeSinceLastPrepareMedia = true;
	private VideoPlayer videoPlayer;
	private final JFauxSlider jSlider = new JFauxSlider();

	private final ButtonModel buttonModel = new JToggleButton.ToggleButtonModel();
	private static final int SIZE = 64;
	private static final Stroke STROKE = new BasicStroke( SIZE / 25.0f );
	private final Painter<VideoPlayer> painter = new Painter<VideoPlayer>() {
		@Override
		public void paint( Graphics2D g2, VideoPlayer videoPlayer, int width, int height ) {
			if( videoPlayer != null ) {
				if( videoPlayer.isPlaying() ) {
					//pass
				} else {
					int x = ( width - SIZE ) / 2;
					int y = ( height - SIZE ) / 2;
					PlayCanvasIcon.paint( null, g2, buttonModel, STROKE, x, y, SIZE, SIZE );
				}
			}
		}
	};

	private final MediaListener mediaListener = new MediaListener() {
		@Override
		public void mediaChanged( VideoPlayer videoPlayer ) {
		}

		@Override
		public void newMedia( VideoPlayer videoPlayer ) {
			SwingUtilities.invokeLater( new Runnable() {
				@Override
				public void run() {
					jSlider.setPortion( 0.0f );
				}
			} );
		}

		@Override
		public void videoOutput( VideoPlayer videoPlayer, int count ) {
			if( count > 0 ) {
				Dimension dimension = videoPlayer.getVideoSize();
			}
		}

		@Override
		public void positionChanged( VideoPlayer videoPlayer, final float f ) {
			SwingUtilities.invokeLater( new Runnable() {
				@Override
				public void run() {
					jSlider.setPortion( f );
				}
			} );
		}

		@Override
		public void lengthChanged( VideoPlayer videoPlayer, long lengthInMsec ) {
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTimeInMillis( lengthInMsec );
			durationLabel.setText( FORMAT.format( calendar.getTime() ) );
		}

		@Override
		public void opening( VideoPlayer videoPlayer ) {
		}

		@Override
		public void playing( VideoPlayer videoPlayer ) {
			setIconToPause();
		}

		@Override
		public void paused( VideoPlayer videoPlayer ) {
			setIconToPlay();
		}

		@Override
		public void finished( final VideoPlayer videoPlayer ) {
			setIconToPlay();
			SwingUtilities.invokeLater( new Runnable() {
				@Override
				public void run() {
					videoPlayer.stop();
				}
			} );
		}

		@Override
		public void stopped( VideoPlayer videoPlayer ) {
			setIconToPlay();
		}

		@Override
		public void error( VideoPlayer videoPlayer ) {
			handleError();
		}
	};

	private final MouseListener mouseListener = new MouseListener() {
		@Override
		public void mouseEntered( MouseEvent e ) {
			buttonModel.setRollover( true );
			getVideoPlayer().getVideoSurface().repaint();
		}

		@Override
		public void mouseExited( MouseEvent e ) {
			buttonModel.setRollover( false );
			getVideoPlayer().getVideoSurface().repaint();
		}

		@Override
		public void mousePressed( MouseEvent e ) {
			if( isErrorFreeSinceLastPrepareMedia ) {
				VideoPlayer videoPlayer = getVideoPlayer();
				if( videoPlayer.isPlaying() ) {
					videoPlayer.pause();
				} else {
					videoPlayer.playResume();
				}
			} else {
				Logger.severe( e );
			}
			buttonModel.setPressed( true );
			getVideoPlayer().getVideoSurface().repaint();
		}

		@Override
		public void mouseReleased( MouseEvent e ) {
			buttonModel.setPressed( false );
			getVideoPlayer().getVideoSurface().repaint();
		}

		@Override
		public void mouseClicked( MouseEvent e ) {
		}
	};

	private final ThumbListener thumbListener = new ThumbListener() {
		private boolean wasPlaying;

		@Override
		public void thumbPressed( float position ) {
			this.wasPlaying = getVideoPlayer().isPlaying();
			pause();
		}

		@Override
		public void thumbDragged( float position ) {
			setPosition( position );
		}

		@Override
		public void thumbReleased( float position ) {
			setPosition( position );
			if( this.wasPlaying ) {
				play();
			} else {
				pause();
			}
		}
	};

	private final UnadornedButton playPauseButton;
	private final Dimension PLAY_PAUSE_ICON_SIZE = new Dimension( 16, 16 );
	private final Icon PLAY_ICON = new PlayButtonIcon( PLAY_PAUSE_ICON_SIZE );
	private final Icon PAUSE_ICON = new PauseButtonIcon( PLAY_PAUSE_ICON_SIZE );
	private final Label durationLabel = new Label( "0:00" );

	public VideoView( VideoComposite composite ) {
		super( composite );
		this.jSlider.addThumbListener( this.thumbListener );
		this.playPauseButton = new UnadornedButton( composite.getTogglePlayPauseOperation() );
		this.playPauseButton.setBorder( BorderFactory.createEmptyBorder( 0, 6, 0, 0 ) );
		this.playPauseButton.setClobberIcon( PLAY_ICON );
		this.playPauseButton.setForegroundColor( Color.LIGHT_GRAY );
		//jPlayView.addItemListener( this.itemListener );

		this.durationLabel.setForegroundColor( Color.LIGHT_GRAY );

		BorderPanel pageEndPanel = new BorderPanel();
		pageEndPanel.addLineStartComponent( playPauseButton );
		pageEndPanel.getAwtComponent().add( this.jSlider, BorderLayout.CENTER );
		pageEndPanel.addLineEndComponent( this.durationLabel );
		pageEndPanel.setBorder( BorderFactory.createEmptyBorder( 0, 2, 0, 4 ) );

		this.addPageEndComponent( pageEndPanel );
		this.setBackgroundColor( ColorUtilities.createGray( 80 ) );
	}

	private void setIconToPlay() {
		this.playPauseButton.setClobberIcon( PLAY_ICON );
		this.playPauseButton.repaint();
	}

	private void setIconToPause() {
		this.playPauseButton.setClobberIcon( PAUSE_ICON );
		this.playPauseButton.repaint();
	}

	private void prepareMedia() {
		this.isErrorFreeSinceLastPrepareMedia = true;
		this.videoPlayer.prepareMedia( this.uri );
	}

	private void handleError() {
		this.isErrorFreeSinceLastPrepareMedia = false;
		Logger.severe();
	}

	public boolean isErrorFreeSinceLastPrepareMedia() {
		return this.isErrorFreeSinceLastPrepareMedia;
	}

	public void setUri( URI uri ) {
		if( Objects.equals( this.uri, uri ) ) {
			//pass
		} else {
			releaseVideoPlayer();
			this.uri = uri;
			if( this.videoPlayer != null ) {
				boolean prepared = this.videoPlayer.isPrepared();
				boolean playable = this.videoPlayer.isPlayable();

				if( this.videoPlayer.isPlaying() ) {
					this.videoPlayer.stop();
				}
				this.prepareMedia();
			} else {
				if( uri != null ) {
					getVideoPlayer();
				}
			}
		}
	}

	public void releaseVideoPlayer() {
		if( this.videoPlayer != null ) {
			this.getAwtComponent().remove( this.videoPlayer.getVideoSurface() );
			this.videoPlayer.setPainter( null );
			this.videoPlayer.removeMediaListener( this.mediaListener );
			this.videoPlayer.prepareMedia( null );
			this.videoPlayer.release();
			this.videoPlayer = null;
		}
	}

	public VideoPlayer getVideoPlayer() {
		if( this.videoPlayer != null ) {
			//pass
		} else {
			this.videoPlayer = VideoUtilities.createVideoPlayer();
			this.videoPlayer.setPainter( this.painter );
			this.videoPlayer.addMediaListener( this.mediaListener );
			if( this.uri != null ) {
				this.prepareMedia();
			}
			Component videoSurface = this.videoPlayer.getVideoSurface();
			Component component;
			if( videoSurface != null ) {
				videoSurface.setPreferredSize( new Dimension( 320, 180 ) );
				videoSurface.addMouseListener( this.mouseListener );
				videoSurface.setEnabled( true );
				component = videoSurface;
			} else {
				component = new JLabel( "error" );
			}
			this.getAwtComponent().add( component, BorderLayout.CENTER );
			this.revalidateAndRepaint();
		}
		return this.videoPlayer;
	}

	private void setPosition( float position ) {
		if( this.isErrorFreeSinceLastPrepareMedia ) {
			this.getVideoPlayer().setPosition( position );
		}
	}

	private void play() {
		if( this.isErrorFreeSinceLastPrepareMedia ) {
			if( this.uri != null ) {
				VideoPlayer videoPlayer = this.getVideoPlayer();
				videoPlayer.playResume();
				this.revalidateAndRepaint();
			}
		} else {
			Logger.severe();
		}
	}

	private void pause() {
		if( this.isErrorFreeSinceLastPrepareMedia ) {
			if( this.videoPlayer != null ) {
				this.videoPlayer.pause();
				this.revalidateAndRepaint();
			}
		} else {
			Logger.severe();
		}
	}
}
