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

package edu.cmu.cs.dennisc.lookingglass.opengl;

/**
 * @author Dennis Cosgrove
 */
public class CustomTextureAdapter extends TextureAdapter< edu.cmu.cs.dennisc.texture.CustomTexture > {
	private com.sun.opengl.util.j2d.TextureRenderer m_textureRenderer = null;
	@Override
	protected boolean isDirty() {
		return m_element.isAnimated() || super.isDirty();
	}
	
	@Override
	public java.awt.Graphics2D createGraphics() {
//		edu.cmu.cs.dennisc.scenegraph.CustomTexture sgCustomTexture = m_sgE;
//		if( m_textureRenderer != null ) {
//			m_textureRenderer.dispose();
//		}
//		m_textureRenderer = new com.sun.opengl.util.j2d.TextureRenderer( sgCustomTexture.getWidth(), sgCustomTexture.getHeight(), sgCustomTexture.isPotentiallyAlphaBlended() );
		java.awt.Graphics2D g = m_textureRenderer.createGraphics();
//		sgCustomTexture.paint( g );
		//m_textureRenderer.beginOrthoRendering( m_textureRenderer.getWidth(), m_textureRenderer.getHeight() );
		return g;
		
	}
	@Override
	public void commitGraphics( java.awt.Graphics2D g, int x, int y, int width, int height ) {
		//m_textureRenderer.drawOrthoRect( width, height );
		//m_textureRenderer.sync( x, y, width, height );
		m_textureRenderer.markDirty( x, y, width, height );
		//m_textureRenderer.endOrthoRendering();
	}
	@Override
	public java.awt.Image getImage() {
		return m_textureRenderer.getImage();
	}
		
	@Override
	protected com.sun.opengl.util.texture.Texture newTexture( com.sun.opengl.util.texture.Texture currentTexture ) {
		boolean isNewTextureRendererRequired;
		if( currentTexture != null ) {
			if( m_textureRenderer != null ) {
				//todo: check mip mapping
				isNewTextureRendererRequired = currentTexture.getWidth() != m_textureRenderer.getWidth() || currentTexture.getHeight() != m_textureRenderer.getHeight();
			} else {
				isNewTextureRendererRequired = true;
			}
		} else {
			isNewTextureRendererRequired = true;
		}
		if( isNewTextureRendererRequired ) {
			if( m_textureRenderer != null ) {
				m_textureRenderer.dispose();
			}
			m_textureRenderer = new com.sun.opengl.util.j2d.TextureRenderer( m_element.getWidth(), m_element.getHeight(), m_element.isPotentiallyAlphaBlended() );
		}
		java.awt.Graphics2D g = m_textureRenderer.createGraphics();
		m_element.paint( g, m_textureRenderer.getWidth(), m_textureRenderer.getHeight() );
		g.dispose();
		m_textureRenderer.markDirty( 0, 0, m_textureRenderer.getWidth(), m_textureRenderer.getHeight() );

		if( m_element.isMipMappingDesired() ) {
			java.awt.Image image = m_textureRenderer.getImage();
			if( image instanceof java.awt.image.BufferedImage ) {
				java.awt.image.BufferedImage bufferedImage = (java.awt.image.BufferedImage)image;
				if( m_element.isPotentiallyAlphaBlended() ) {
					//pass
				} else {
					try {
						return com.sun.opengl.util.texture.TextureIO.newTexture( bufferedImage, true );
					} catch( AssertionError ae ) {
						System.err.println( "WARNING: unable to directly generate mipmapped texture." );
					} catch( RuntimeException re ) {
						System.err.println( "WARNING: unable to directly generate mipmapped texture." );
					}
				}
				java.awt.image.BufferedImage hackBI = new java.awt.image.BufferedImage( bufferedImage.getWidth(), bufferedImage.getHeight(), java.awt.image.BufferedImage.TYPE_4BYTE_ABGR_PRE );
				java.awt.Graphics2D hackG = hackBI.createGraphics();
				hackG.drawImage( bufferedImage, 0, 0, edu.cmu.cs.dennisc.image.ImageUtilities.accessImageObserver() );
				hackG.dispose();
				bufferedImage = hackBI;
				return com.sun.opengl.util.texture.TextureIO.newTexture( bufferedImage, true );
			} else {
				return m_textureRenderer.getTexture();
			}
		} else {
			return m_textureRenderer.getTexture();
		}
	}
}
