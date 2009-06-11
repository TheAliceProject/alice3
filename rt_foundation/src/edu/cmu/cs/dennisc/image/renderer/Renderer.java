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
package edu.cmu.cs.dennisc.image.renderer;

/**
 * @author Dennis Cosgrove
 */
public abstract class Renderer {
	private java.awt.image.BufferedImage m_bufferedImage = null;

	protected void createBufferedImageIfNecessary( java.awt.GraphicsConfiguration gc, int width, int height, int transparency ) {
		if( m_bufferedImage != null ) {
			if( m_bufferedImage.getWidth() < width || m_bufferedImage.getHeight() < height || m_bufferedImage.getTransparency() != transparency ) {
				m_bufferedImage = null;
			}
		}
		if( m_bufferedImage != null ) {
			//pass
		} else {
			m_bufferedImage = gc.createCompatibleImage( width, height, transparency );
		}
	}

	protected java.awt.image.BufferedImage getBufferedImage() {
		return m_bufferedImage;
	}
	protected int getAllocatedWidth() {
		if( m_bufferedImage != null ) {
			return m_bufferedImage.getWidth();
		} else {
			return 0;
		}
	}
	protected int getAllocatedHeight() {
		if( m_bufferedImage != null ) {
			return m_bufferedImage.getHeight();
		} else {
			return 0;
		}
	}
	protected abstract int getWidth();
	protected abstract int getHeight();
	protected abstract int getTransparency();

	public abstract void renderIntoBufferedImage( java.awt.GraphicsConfiguration gc );
	public abstract void paintBackground( java.awt.Graphics2D g2 );

	public void paint( java.awt.Graphics2D g2 ) {
		paint( g2, getWidth(), getHeight() );
	}
	protected final void paint( java.awt.Graphics2D g2, int width, int height ) {
		paintBackground( g2 );
		renderIntoBufferedImage( g2.getDeviceConfiguration() );
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "paint:", height, m_bufferedImage.getHeight() );
		g2.drawImage( m_bufferedImage, 0, 0, width, height, null );
	}
}
