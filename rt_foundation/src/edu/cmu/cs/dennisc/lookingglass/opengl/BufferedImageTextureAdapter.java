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
public class BufferedImageTextureAdapter extends TextureAdapter< edu.cmu.cs.dennisc.texture.BufferedImageTexture > {
	@Override
	public java.awt.Graphics2D createGraphics() {
		throw new RuntimeException( "TODO" );
	}
	@Override
	public void commitGraphics( java.awt.Graphics2D g, int x, int y, int width, int height ) {
		throw new RuntimeException( "TODO" );
	}
	@Override
	public java.awt.Image getImage() {
		throw new RuntimeException( "TODO" );
	}
	
	@Override
	protected com.sun.opengl.util.texture.Texture newTexture( com.sun.opengl.util.texture.Texture currentTexture ) {
		if( currentTexture != null ) {
			currentTexture.dispose();
		}
		return com.sun.opengl.util.texture.TextureIO.newTexture( m_element.getBufferedImage(), m_element.isMipMappingDesired() );
	}
//	
//	@Override
//	protected void propertyChanged( edu.cmu.cs.dennisc.property.InstanceProperty<?> property ) {
//		if( property == m_texture.bufferedImage ) {
//			setDirty( true );
//		} else {
//			super.propertyChanged( property );
//		}
//	}
}
