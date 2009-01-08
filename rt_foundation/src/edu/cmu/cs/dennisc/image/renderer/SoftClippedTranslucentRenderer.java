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
public abstract class SoftClippedTranslucentRenderer extends TranslucentRenderer {
	protected abstract void renderClip( java.awt.Graphics2D g2, int x, int y, int width, int height );
	@Override
	protected void clear( java.awt.Graphics2D g2, int x, int y, int width, int height ) {
		super.clear( g2, x, y, width, height );
		g2.setComposite( java.awt.AlphaComposite.Src );
		g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
		g2.setPaint( java.awt.Color.WHITE );
		renderClip( g2, x, y, width, height );
	}

	protected abstract void renderForegroundIntoBufferedImage( java.awt.Graphics2D g2, int x, int y, int width, int height );

	@Override
	public final void renderIntoBufferedImage( java.awt.GraphicsConfiguration gc ) {
		int x = 0;
		int y = 0;
		int width = getWidth();
		int height = getHeight();
		int transparency = getTransparency();
		createBufferedImageIfNecessary( gc, width, height, transparency );
		java.awt.Graphics2D g2 = getBufferedImage().createGraphics();
		clear( g2, x, y, getAllocatedWidth(), getAllocatedHeight() );
		g2.dispose();
		
		java.awt.Graphics2D _g2 = getBufferedImage().createGraphics();
		_g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
		renderForegroundIntoBufferedImage( _g2, x, y, width, height );
		_g2.dispose();
		
//		int x = 0;
//		int y = 0;
//		int width = getWidth();
//		int height = getHeight();
//		int transparency = getTransparency();
//		createBufferedImageIfNecessary( gc, width, height, transparency );
//
//		java.awt.Graphics2D g2 = getBufferedImage().createGraphics();
//		java.awt.Composite prevComposite = g2.getComposite();
//		try {
//			g2.setComposite( java.awt.AlphaComposite.Clear );
//			g2.fillRect( x, y, width, height );
//			g2.setComposite( java.awt.AlphaComposite.Src );
//			g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
//			g2.setPaint( java.awt.Color.WHITE );
//			renderClip( g2, x, y, width, height );
//		} finally {
//			g2.setComposite( prevComposite );
//			g2.dispose();
//		}
//
//		java.awt.Graphics2D _g2 = getBufferedImage().createGraphics();
//		_g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
//		renderForeground( _g2, x, y, width, height );
//		_g2.dispose();
	}
}
