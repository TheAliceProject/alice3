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

class PlayCanvasIcon implements javax.swing.Icon {
	public static void paint( java.awt.Component c, java.awt.Graphics g, javax.swing.ButtonModel buttonModel, java.awt.Stroke stroke, int x, int y, int width, int height ) {
		if( buttonModel.isEnabled() ) {
			if( buttonModel.isSelected() ) {
				//pass
			} else {
				java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
				edu.cmu.cs.dennisc.java.awt.GraphicsContext gc = edu.cmu.cs.dennisc.java.awt.GraphicsContext.getInstanceAndPushGraphics( g2 );
				try {
					gc.pushAndSetAntialiasing( true );
					gc.pushStroke();
					gc.pushTransform();
					gc.pushPaint();
					g.translate( x, y );
					if( buttonModel.isRollover() ) {
						float xCenter = width * 0.5f;
						float yCenter = height * 0.5f;
						g2.setPaint( new java.awt.Color( 255, 255, 191, 9 ) );
						for( float radius = 50.0f; radius < 80.0f; radius += 4.0f ) {
							java.awt.geom.Ellipse2D.Float shape = new java.awt.geom.Ellipse2D.Float( xCenter - radius, yCenter - radius, radius + radius, radius + radius );
							g2.fill( shape );
						}
					}

					g2.setStroke( stroke );
					java.awt.geom.RoundRectangle2D.Float rr = new java.awt.geom.RoundRectangle2D.Float( 0, 0, width, height, width * 0.6f, height * 0.6f );
					g2.setColor( edu.cmu.cs.dennisc.java.awt.ColorUtilities.createGray( 70 ) );
					g2.fill( rr );
					g2.setColor( edu.cmu.cs.dennisc.java.awt.ColorUtilities.createGray( 220 ) );
					g2.draw( rr );

					int w = (int)( width * 0.4 );
					int h = (int)( height * 0.45 );
					int xFudge = width / 20;
					edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.fillTriangle( g2, edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.Heading.EAST, ( ( width - w ) / 2 ) + xFudge, ( height - h ) / 2, w, h );
				} finally {
					gc.popAll();
				}
			}
		}
	}

	private final java.awt.Stroke stroke;
	private final int width;
	private final int height;

	public PlayCanvasIcon( int width, int height ) {
		this.width = width;
		this.height = height;
		int size = Math.max( this.width, this.height );
		this.stroke = new java.awt.BasicStroke( size / 25.0f );
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
	public void paintIcon( java.awt.Component c, java.awt.Graphics g, int x, int y ) {
		if( c instanceof javax.swing.AbstractButton ) {
			javax.swing.AbstractButton button = (javax.swing.AbstractButton)c;
			javax.swing.ButtonModel buttonModel = button.getModel();
			paint( c, g, buttonModel, this.stroke, x, y, this.width, this.height );
		}
	}
}

class PlayButtonIcon implements javax.swing.Icon {
	private final java.awt.Dimension size;

	public PlayButtonIcon( java.awt.Dimension size ) {
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
	public void paintIcon( java.awt.Component c, java.awt.Graphics g, int x, int y ) {
		javax.swing.AbstractButton b = (javax.swing.AbstractButton)c;
		javax.swing.ButtonModel model = b.getModel();
		java.awt.Color color = b.getForeground();
		if( model.isRollover() ) {
			color = color.brighter();
		}
		g.setColor( color );
		Object prevAntialiasing = edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.setAntialiasing( g, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
		try {
			edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.fillTriangle( g, edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.Heading.EAST, x, y, size.width, size.height );
		} finally {
			edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.setAntialiasing( g, prevAntialiasing );
		}
	}
}

class PauseButtonIcon implements javax.swing.Icon {
	private final java.awt.Dimension size;

	public PauseButtonIcon( java.awt.Dimension size ) {
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
	public void paintIcon( java.awt.Component c, java.awt.Graphics g, int x, int y ) {
		javax.swing.AbstractButton b = (javax.swing.AbstractButton)c;
		javax.swing.ButtonModel model = b.getModel();
		java.awt.Color color = b.getForeground();
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
public class VideoView extends org.lgna.croquet.views.BorderPanel {
	private static final java.text.SimpleDateFormat FORMAT = new java.text.SimpleDateFormat( "m:ss" );

	private java.net.URI uri;
	private boolean isErrorFreeSinceLastPrepareMedia = true;
	private edu.cmu.cs.dennisc.video.VideoPlayer videoPlayer;
	private final JFauxSlider jSlider = new JFauxSlider();

	private final javax.swing.ButtonModel buttonModel = new javax.swing.JToggleButton.ToggleButtonModel();
	private static final int SIZE = 64;
	private static final java.awt.Stroke STROKE = new java.awt.BasicStroke( SIZE / 25.0f );
	private final edu.cmu.cs.dennisc.java.awt.Painter<edu.cmu.cs.dennisc.video.VideoPlayer> painter = new edu.cmu.cs.dennisc.java.awt.Painter<edu.cmu.cs.dennisc.video.VideoPlayer>() {
		@Override
		public void paint( java.awt.Graphics2D g2, edu.cmu.cs.dennisc.video.VideoPlayer videoPlayer, int width, int height ) {
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

	private final edu.cmu.cs.dennisc.video.event.MediaListener mediaListener = new edu.cmu.cs.dennisc.video.event.MediaListener() {
		@Override
		public void mediaChanged( edu.cmu.cs.dennisc.video.VideoPlayer videoPlayer ) {
		}

		@Override
		public void newMedia( edu.cmu.cs.dennisc.video.VideoPlayer videoPlayer ) {
			javax.swing.SwingUtilities.invokeLater( new Runnable() {
				@Override
				public void run() {
					jSlider.setPortion( 0.0f );
				}
			} );
		}

		@Override
		public void videoOutput( edu.cmu.cs.dennisc.video.VideoPlayer videoPlayer, int count ) {
			if( count > 0 ) {
				java.awt.Dimension dimension = videoPlayer.getVideoSize();
			}
		}

		@Override
		public void positionChanged( edu.cmu.cs.dennisc.video.VideoPlayer videoPlayer, final float f ) {
			javax.swing.SwingUtilities.invokeLater( new Runnable() {
				@Override
				public void run() {
					jSlider.setPortion( f );
				}
			} );
		}

		@Override
		public void lengthChanged( edu.cmu.cs.dennisc.video.VideoPlayer videoPlayer, long lengthInMsec ) {
			java.util.GregorianCalendar calendar = new java.util.GregorianCalendar();
			calendar.setTimeInMillis( lengthInMsec );
			durationLabel.setText( FORMAT.format( calendar.getTime() ) );
		}

		@Override
		public void opening( edu.cmu.cs.dennisc.video.VideoPlayer videoPlayer ) {
		}

		@Override
		public void playing( edu.cmu.cs.dennisc.video.VideoPlayer videoPlayer ) {
			setIconToPause();
		}

		@Override
		public void paused( edu.cmu.cs.dennisc.video.VideoPlayer videoPlayer ) {
			setIconToPlay();
		}

		@Override
		public void finished( final edu.cmu.cs.dennisc.video.VideoPlayer videoPlayer ) {
			setIconToPlay();
			javax.swing.SwingUtilities.invokeLater( new Runnable() {
				@Override
				public void run() {
					videoPlayer.stop();
				}
			} );
		}

		@Override
		public void stopped( edu.cmu.cs.dennisc.video.VideoPlayer videoPlayer ) {
			setIconToPlay();
		}

		@Override
		public void error( edu.cmu.cs.dennisc.video.VideoPlayer videoPlayer ) {
			handleError();
		}
	};

	private final java.awt.event.MouseListener mouseListener = new java.awt.event.MouseListener() {
		@Override
		public void mouseEntered( java.awt.event.MouseEvent e ) {
			buttonModel.setRollover( true );
			getVideoPlayer().getVideoSurface().repaint();
		}

		@Override
		public void mouseExited( java.awt.event.MouseEvent e ) {
			buttonModel.setRollover( false );
			getVideoPlayer().getVideoSurface().repaint();
		}

		@Override
		public void mousePressed( java.awt.event.MouseEvent e ) {
			if( isErrorFreeSinceLastPrepareMedia ) {
				edu.cmu.cs.dennisc.video.VideoPlayer videoPlayer = getVideoPlayer();
				if( videoPlayer.isPlaying() ) {
					videoPlayer.pause();
				} else {
					videoPlayer.playResume();
				}
			} else {
				edu.cmu.cs.dennisc.java.util.logging.Logger.severe( e );
			}
			buttonModel.setPressed( true );
			getVideoPlayer().getVideoSurface().repaint();
		}

		@Override
		public void mouseReleased( java.awt.event.MouseEvent e ) {
			buttonModel.setPressed( false );
			getVideoPlayer().getVideoSurface().repaint();
		}

		@Override
		public void mouseClicked( java.awt.event.MouseEvent e ) {
		}
	};

	private final org.alice.ide.video.preview.views.events.ThumbListener thumbListener = new org.alice.ide.video.preview.views.events.ThumbListener() {
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
	private final java.awt.Dimension PLAY_PAUSE_ICON_SIZE = new java.awt.Dimension( 16, 16 );
	private final javax.swing.Icon PLAY_ICON = new PlayButtonIcon( PLAY_PAUSE_ICON_SIZE );
	private final javax.swing.Icon PAUSE_ICON = new PauseButtonIcon( PLAY_PAUSE_ICON_SIZE );
	private final org.lgna.croquet.views.Label durationLabel = new org.lgna.croquet.views.Label( "0:00" );

	public VideoView( org.alice.ide.video.preview.VideoComposite composite ) {
		super( composite );
		this.jSlider.addThumbListener( this.thumbListener );
		this.playPauseButton = new UnadornedButton( composite.getTogglePlayPauseOperation() );
		this.playPauseButton.setBorder( javax.swing.BorderFactory.createEmptyBorder( 0, 6, 0, 0 ) );
		this.playPauseButton.setClobberIcon( PLAY_ICON );
		this.playPauseButton.setForegroundColor( java.awt.Color.LIGHT_GRAY );
		//jPlayView.addItemListener( this.itemListener );

		this.durationLabel.setForegroundColor( java.awt.Color.LIGHT_GRAY );

		org.lgna.croquet.views.BorderPanel pageEndPanel = new org.lgna.croquet.views.BorderPanel();
		pageEndPanel.addLineStartComponent( playPauseButton );
		pageEndPanel.getAwtComponent().add( this.jSlider, java.awt.BorderLayout.CENTER );
		pageEndPanel.addLineEndComponent( this.durationLabel );
		pageEndPanel.setBorder( javax.swing.BorderFactory.createEmptyBorder( 0, 2, 0, 4 ) );

		this.addPageEndComponent( pageEndPanel );
		this.setBackgroundColor( edu.cmu.cs.dennisc.java.awt.ColorUtilities.createGray( 80 ) );
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
		edu.cmu.cs.dennisc.java.util.logging.Logger.severe();
	}

	public boolean isErrorFreeSinceLastPrepareMedia() {
		return this.isErrorFreeSinceLastPrepareMedia;
	}

	public void setUri( java.net.URI uri ) {
		if( edu.cmu.cs.dennisc.java.util.Objects.equals( this.uri, uri ) ) {
			//pass
		} else {
			this.uri = uri;
			if( this.videoPlayer != null ) {
				if( this.videoPlayer.isPlaying() ) {
					this.videoPlayer.stop();
				}
				this.prepareMedia();
			} else {
				getVideoPlayer();
			}
		}
	}

	public edu.cmu.cs.dennisc.video.VideoPlayer getVideoPlayer() {
		if( this.videoPlayer != null ) {
			//pass
		} else {
			this.videoPlayer = edu.cmu.cs.dennisc.video.VideoUtilities.createVideoPlayer();
			this.videoPlayer.setPainter( this.painter );
			this.videoPlayer.addMediaListener( this.mediaListener );
			if( this.uri != null ) {
				this.prepareMedia();
			}
			java.awt.Component videoSurface = this.videoPlayer.getVideoSurface();
			java.awt.Component component;
			if( videoSurface != null ) {
				videoSurface.setPreferredSize( new java.awt.Dimension( 320, 180 ) );
				videoSurface.addMouseListener( this.mouseListener );
				videoSurface.setEnabled( true );
				component = videoSurface;
			} else {
				component = new javax.swing.JLabel( "error" );
			}
			this.getAwtComponent().add( component, java.awt.BorderLayout.CENTER );
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
				edu.cmu.cs.dennisc.video.VideoPlayer videoPlayer = this.getVideoPlayer();
				videoPlayer.playResume();
				this.revalidateAndRepaint();
			}
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe();
		}
	}

	private void pause() {
		if( this.isErrorFreeSinceLastPrepareMedia ) {
			if( this.videoPlayer != null ) {
				this.videoPlayer.pause();
				this.revalidateAndRepaint();
			}
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe();
		}
	}
}
