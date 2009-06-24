/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */

package edu.cmu.cs.dennisc.texture;

/**
 * @author Dennis Cosgrove
 */
public class BufferedImageTexture extends Texture {
	private java.awt.image.BufferedImage m_bufferedImage = null;
	private boolean m_isMipMappingDesired = true;
	private boolean m_isPotentiallyAlphaBlended = false;
	
	public java.awt.image.BufferedImage getBufferedImage() {
		return m_bufferedImage;
	}
	public void setBufferedImage( java.awt.image.BufferedImage bufferedImage ) {
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

	public void decode( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		byte[] buffer = binaryDecoder.decodeByteArray();
		java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream( buffer );
		setBufferedImage( edu.cmu.cs.dennisc.image.ImageUtilities.read( edu.cmu.cs.dennisc.image.ImageUtilities.PNG_CODEC_NAME, bais ) );
	}
	public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		//todo
		assert m_bufferedImage != null;
		java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
		edu.cmu.cs.dennisc.image.ImageUtilities.write( edu.cmu.cs.dennisc.image.ImageUtilities.PNG_CODEC_NAME, baos, m_bufferedImage );
		binaryEncoder.encode(  baos.toByteArray() );
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
	public boolean isAnimated() {
		return false;
	}
	public edu.cmu.cs.dennisc.texture.MipMapGenerationPolicy getMipMapGenerationPolicy() {
		return edu.cmu.cs.dennisc.texture.MipMapGenerationPolicy.PAINT_EACH_INDIVIDUAL_LEVEL;
	}
	public void paint( java.awt.Graphics2D g, int width, int height ) {
		g.drawImage( m_bufferedImage, 0, 0, width, height, null );
	}
}
