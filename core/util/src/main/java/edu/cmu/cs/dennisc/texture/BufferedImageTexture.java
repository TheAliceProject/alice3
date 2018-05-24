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

package edu.cmu.cs.dennisc.texture;

import edu.cmu.cs.dennisc.codec.BinaryDecoder;
import edu.cmu.cs.dennisc.codec.BinaryEncoder;
import edu.cmu.cs.dennisc.image.ImageUtilities;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author Dennis Cosgrove
 */
public class BufferedImageTexture extends Texture {
	private BufferedImage m_bufferedImage = null;
	private boolean m_isMipMappingDesired = true;
	private boolean m_isPotentiallyAlphaBlended = false;

	public BufferedImageTexture() {
	}

	public BufferedImageTexture( BinaryDecoder binaryDecoder ) {
		super( binaryDecoder );
		byte[] buffer = binaryDecoder.decodeByteArray();
		ByteArrayInputStream bais = new ByteArrayInputStream( buffer );
		try {
			setBufferedImage( ImageUtilities.read( ImageUtilities.PNG_CODEC_NAME, bais ) );
		} catch( IOException ioe ) {
			throw new RuntimeException( binaryDecoder.toString(), ioe );
		}
	}

	@Override
	public void encode( BinaryEncoder binaryEncoder ) {
		//todo
		assert m_bufferedImage != null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ImageUtilities.write( ImageUtilities.PNG_CODEC_NAME, baos, m_bufferedImage );
		} catch( IOException ioe ) {
			throw new RuntimeException( binaryEncoder.toString(), ioe );
		}
		binaryEncoder.encode( baos.toByteArray() );
	}

	public BufferedImage getBufferedImage() {
		return m_bufferedImage;
	}

	public void setBufferedImage( BufferedImage bufferedImage ) {
		if( m_bufferedImage != bufferedImage ) {
			m_bufferedImage = bufferedImage;
			fireTextureChanged();
		}
	}

	@Override
	public boolean isMipMappingDesired() {
		return m_isMipMappingDesired;
	}

	public void setMipMappingDesired( boolean isMipMappingDesired ) {
		if( m_isMipMappingDesired != isMipMappingDesired ) {
			m_isMipMappingDesired = isMipMappingDesired;
			fireTextureChanged();
		}
	}

	public void directSetMipMappingDesired( boolean isMipMappingDesired ) {
		if( m_isMipMappingDesired != isMipMappingDesired ) {
			m_isMipMappingDesired = isMipMappingDesired;
		}
	}

	@Override
	public boolean isPotentiallyAlphaBlended() {
		return m_isPotentiallyAlphaBlended;
	}

	public void setPotentiallyAlphaBlended( boolean isPotentiallyAlphaBlended ) {
		if( m_isPotentiallyAlphaBlended != isPotentiallyAlphaBlended ) {
			m_isPotentiallyAlphaBlended = isPotentiallyAlphaBlended;
			fireTextureChanged();
		}
	}

	@Override
	public int getWidth() {
		if( m_bufferedImage != null ) {
			return m_bufferedImage.getWidth();
		} else {
			return 0;
		}
	}

	@Override
	public int getHeight() {
		if( m_bufferedImage != null ) {
			return m_bufferedImage.getHeight();
		} else {
			return 0;
		}
	}

	@Override
	public boolean isAnimated() {
		return false;
	}

	@Override
	public MipMapGenerationPolicy getMipMapGenerationPolicy() {
		return MipMapGenerationPolicy.PAINT_EACH_INDIVIDUAL_LEVEL;
	}

	@Override
	public void paint( Graphics2D g, int width, int height ) {
		g.drawImage( m_bufferedImage, 0, 0, width, height, null );
	}
}
