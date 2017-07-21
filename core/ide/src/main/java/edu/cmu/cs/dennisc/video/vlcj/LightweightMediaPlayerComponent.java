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
package edu.cmu.cs.dennisc.video.vlcj;

/**
 * @author Kyle J. Harms
 */
/* package-private */class LightweightMediaPlayerComponent extends uk.co.caprica.vlcj.component.DirectMediaPlayerComponent implements VlcjMediaPlayerComponent {

	private final javax.swing.JPanel panel;

	private Object lock = "LightweightMediaPlayerComponent lock";

	private java.awt.image.BufferedImage image;
	private int[] rgbs;
	private edu.cmu.cs.dennisc.java.awt.Painter<edu.cmu.cs.dennisc.video.VideoPlayer> painter;

	public LightweightMediaPlayerComponent( final VlcjVideoPlayer videoPlayer ) {
		super( new uk.co.caprica.vlcj.player.direct.BufferFormatCallback() {
			@Override
			public uk.co.caprica.vlcj.player.direct.BufferFormat getBufferFormat( int sourceWidth, int sourceHeight ) {
				return new uk.co.caprica.vlcj.player.direct.format.RV32BufferFormat( sourceWidth, sourceHeight );
			}
		} );

		this.panel = new javax.swing.JPanel() {
			@Override
			public void paint( java.awt.Graphics g ) {
				synchronized( lock ) {
					if( image != null ) {
						int imageWidth = image.getWidth();
						int imageHeight = image.getHeight();

						java.awt.Dimension componentSize = this.getSize();

						if( ( imageWidth == componentSize.width ) || ( imageHeight == componentSize.height ) ) {
							g.drawImage( image, 0, 0, this );
						} else {
							java.awt.Dimension imageSize = new java.awt.Dimension( imageWidth, imageHeight );
							java.awt.Dimension size = edu.cmu.cs.dennisc.java.awt.DimensionUtilities.calculateBestFittingSize( componentSize, imageSize.width / (double)imageSize.height );

							int x0 = ( componentSize.width - size.width ) / 2;
							int x1 = x0 + size.width;
							int y0 = ( componentSize.height - size.height ) / 2;
							int y1 = y0 + size.height;

							super.paint( g ); //todo: only paint outer bars

							g.drawImage( image, x0, y0, x1, y1, 0, 0, imageWidth, imageHeight, this );
						}
					} else {
						super.paint( g );
					}
				}
				if( painter != null ) {
					painter.paint( (java.awt.Graphics2D)g, videoPlayer, this.getWidth(), this.getHeight() );
				}
			}
		};
		this.panel.setBackground( java.awt.Color.BLACK );
		this.panel.setOpaque( true );
	}

	@Override
	public void display( uk.co.caprica.vlcj.player.direct.DirectMediaPlayer mediaPlayer, com.sun.jna.Memory[] nativeBuffers, uk.co.caprica.vlcj.player.direct.BufferFormat bufferFormat ) {
		//super.display( mediaPlayer, nativeBuffers, bufferFormat );
		synchronized( this.lock ) {
			int width = bufferFormat.getWidth();
			int height = bufferFormat.getHeight();

			//todo: bufferFormat pitches and lines?

			if( this.image != null ) {
				if( ( this.image.getWidth() != width ) || ( this.image.getHeight() != height ) ) {
					this.image = null;
				}
			}
			if( this.rgbs != null ) {
				if( this.rgbs.length != ( width * height ) ) {
					this.rgbs = null;
				}
			}
			if( this.image != null ) {
				//pass
			} else {
				this.image = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage( width, height );
			}
			if( this.rgbs != null ) {
				//pass
			} else {
				this.rgbs = new int[ width * height ];
			}

			java.nio.ByteBuffer byteBuffer = nativeBuffers[ 0 ].getByteBuffer( 0L, nativeBuffers[ 0 ].size() );
			java.nio.IntBuffer intBuffer = byteBuffer.asIntBuffer();
			intBuffer.get( this.rgbs, 0, bufferFormat.getHeight() * bufferFormat.getWidth() );

			this.image.setRGB( 0, 0, width, height, this.rgbs, 0, width );
		}
		this.panel.repaint();
	}

	//	@Override
	//	protected RenderCallback onGetRenderCallback() {
	//		return new RenderCallbackAdapter( new int[ width * height ] ) {
	//			@Override
	//			protected void onDisplay( DirectMediaPlayer mediaPlayer, int[] rgbBuffer ) {
	//				// Simply copy buffer to the image and repaint
	//				LightweightMediaPlayerComponent.this.image.setRGB( 0, 0, width, height, rgbBuffer, 0, width );
	//				LightweightMediaPlayerComponent.this.panel.repaint();
	//			}
	//		};
	//	}

	@Override
	public java.awt.Component getVideoSurface() {
		return this.panel;
	}

	@Override
	public edu.cmu.cs.dennisc.java.awt.Painter<edu.cmu.cs.dennisc.video.VideoPlayer> getPainter() {
		return this.painter;
	}

	@Override
	public void setPainter( edu.cmu.cs.dennisc.java.awt.Painter<edu.cmu.cs.dennisc.video.VideoPlayer> painter ) {
		this.painter = painter;
	}
}
