/**
 * Copyright (c) 2008-2013, Washington University in St. Louis. All rights reserved.
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
package edu.cmu.cs.dennisc.video.vlcj;

import uk.co.caprica.vlcj.player.direct.DirectMediaPlayer;
import uk.co.caprica.vlcj.player.direct.RenderCallback;
import uk.co.caprica.vlcj.player.direct.RenderCallbackAdapter;

/**
 * @author Kyle J. Harms
 */
public class LightweightMediaPlayerComponent extends uk.co.caprica.vlcj.component.DirectMediaPlayerComponent implements VlcjMediaPlayerComponent {

	private final javax.swing.JPanel panel;
	private final java.awt.image.BufferedImage image;

	private final int width = 640;
	private final int height = 360;

	private edu.cmu.cs.dennisc.java.awt.Painter painter;

	public LightweightMediaPlayerComponent() {
		super( new uk.co.caprica.vlcj.player.direct.BufferFormatCallback() {
			@Override
			public uk.co.caprica.vlcj.player.direct.BufferFormat getBufferFormat( int sourceWidth, int sourceHeight ) {
				return new uk.co.caprica.vlcj.player.direct.format.RV32BufferFormat( sourceWidth, sourceHeight );
			}
		} );

		this.panel = new javax.swing.JPanel() {
			@Override
			protected void paintComponent( java.awt.Graphics g ) {
				java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
				g2.setColor( java.awt.Color.black );
				g2.fillRect( 0, 0, getWidth(), getHeight() );
				g2.drawImage( image, null, 0, 0 );
			}

			@Override
			public void paint( java.awt.Graphics g ) {
				super.paint( g );
				if( painter != null ) {
					painter.paint( (java.awt.Graphics2D)g, this.getWidth(), this.getHeight() );
				}
			}
		};
		this.panel.setBackground( java.awt.Color.black );
		this.panel.setOpaque( true );
		//		this.panel.setPreferredSize( new java.awt.Dimension( bufferWidth, bufferHeight ) );
		//		this.panel.setMinimumSize( new java.awt.Dimension( bufferWidth, bufferHeight ) );
		//		this.panel.setMaximumSize( new java.awt.Dimension( bufferWidth, bufferHeight ) );

		this.image = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage( width, height );
	}

	@Override
	protected RenderCallback onGetRenderCallback() {
		return new RenderCallbackAdapter( new int[ width * height ] ) {
			@Override
			protected void onDisplay( DirectMediaPlayer mediaPlayer, int[] rgbBuffer ) {
				// Simply copy buffer to the image and repaint
				LightweightMediaPlayerComponent.this.image.setRGB( 0, 0, width, height, rgbBuffer, 0, width );
				LightweightMediaPlayerComponent.this.panel.repaint();
			}
		};
	}

	public java.awt.Component getVideoSurface() {
		return this.panel;
	}

	public edu.cmu.cs.dennisc.java.awt.Painter getPainter() {
		return this.painter;
	}

	public void setPainter( edu.cmu.cs.dennisc.java.awt.Painter painter ) {
		this.painter = painter;
	}
}
