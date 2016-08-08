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

package edu.cmu.cs.dennisc.render.gl.imp;

/**
 * @author Dennis Cosgrove
 */
class Pixels implements edu.cmu.cs.dennisc.texture.event.TextureListener {
	private static final java.awt.image.ComponentColorModel RGBA_COLOR_MODEL = new java.awt.image.ComponentColorModel(
			java.awt.color.ColorSpace.getInstance( java.awt.color.ColorSpace.CS_sRGB ),
			new int[] { 8, 8, 8, 8 },
			true,
			false,
			java.awt.image.ComponentColorModel.TRANSLUCENT,
			java.awt.image.DataBuffer.TYPE_BYTE
			);

	private edu.cmu.cs.dennisc.texture.Texture m_texture;

	private java.awt.image.WritableRaster m_writableRaster = null;
	private java.awt.image.BufferedImage m_bufferedImage = null;
	private java.nio.ByteBuffer m_data = null;

	public Pixels( edu.cmu.cs.dennisc.texture.Texture texture ) {
		m_texture = texture;
		m_texture.addTextureListener( this );
	}

	@Override
	public void textureChanged( edu.cmu.cs.dennisc.texture.event.TextureEvent textureEvent ) {
		if( ( m_texture != null ) && ( m_writableRaster != null ) && ( m_bufferedImage != null ) ) {
			if( ( m_texture.getWidth() != m_bufferedImage.getWidth() ) || ( m_texture.getHeight() != m_bufferedImage.getHeight() ) ) {
				touchImage();
			}
		}
	}

	public void touchImage() {
		m_data = null;
		if( m_writableRaster != null ) {
			if( ( m_texture.getWidth() != m_writableRaster.getWidth() ) || ( m_texture.getHeight() != m_writableRaster.getHeight() ) ) {
				m_writableRaster = null;
				m_bufferedImage = null;
			}
		}
	}

	public void release() {
		if( m_texture != null ) {
			m_texture.removeTextureListener( this );
			m_writableRaster = null;
			m_bufferedImage = null;
			m_data = null;
			m_texture = null;
		}
	}

	public int getWidth() {
		return m_texture.getWidth();
	}

	public int getHeight() {
		return m_texture.getHeight();
	}

	private java.awt.image.BufferedImage getBufferedImage() {
		if( m_bufferedImage != null ) {
			//pass
		} else {
			m_writableRaster = java.awt.image.Raster.createInterleavedRaster( java.awt.image.DataBuffer.TYPE_BYTE, getWidth(), getHeight(), 4, null );
			m_bufferedImage = new java.awt.image.BufferedImage( RGBA_COLOR_MODEL, m_writableRaster, false, null );
		}
		return m_bufferedImage;
	}

	public java.nio.ByteBuffer getRGBA() {
		if( m_data != null ) {
			//pass
		} else {
			java.awt.image.BufferedImage bufferedImage = getBufferedImage();
			if( bufferedImage != null ) {
				java.awt.Graphics2D g = bufferedImage.createGraphics();
				java.awt.geom.AffineTransform gt = new java.awt.geom.AffineTransform();
				gt.translate( 0, bufferedImage.getHeight() );
				gt.scale( 1.0, -1.0 );
				g.transform( gt );
				if( m_texture instanceof edu.cmu.cs.dennisc.texture.BufferedImageTexture ) {
					edu.cmu.cs.dennisc.texture.BufferedImageTexture bufferedImageTexture = (edu.cmu.cs.dennisc.texture.BufferedImageTexture)m_texture;
					g.drawImage( bufferedImageTexture.getBufferedImage(), null, null );
				} else if( m_texture instanceof edu.cmu.cs.dennisc.texture.CustomTexture ) {
					edu.cmu.cs.dennisc.texture.CustomTexture customTexture = (edu.cmu.cs.dennisc.texture.CustomTexture)m_texture;
					customTexture.paint( g, bufferedImage.getWidth(), bufferedImage.getHeight() );
				}
				g.dispose();
				java.awt.image.DataBufferByte dataBufferByte = (java.awt.image.DataBufferByte)m_writableRaster.getDataBuffer();
				byte[] data = dataBufferByte.getData();
				m_data = java.nio.ByteBuffer.wrap( data );
			} else {
				m_data = null;
			}
		}
		return m_data;
	}
}
