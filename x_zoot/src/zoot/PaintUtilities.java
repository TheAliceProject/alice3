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
package zoot;

/**
 * @author Dennis Cosgrove
 */
public class PaintUtilities {
	private static java.awt.Paint disabledTexturePaint = null;
	public static java.awt.Paint getDisabledTexturePaint() {
		if( PaintUtilities.disabledTexturePaint != null ) {
			//pass
		} else {
			int width = 8;
			int height = 8;
			java.awt.image.BufferedImage image = new java.awt.image.BufferedImage( width, height, java.awt.image.BufferedImage.TYPE_INT_ARGB );
			java.awt.Graphics g = image.getGraphics();
			g.setColor( new java.awt.Color( 128, 128, 128, 96 ) );
			g.fillRect( 0, 0, width, height );
			g.setColor( java.awt.Color.DARK_GRAY );
			g.drawLine( 0, height, width, 0 );
			g.drawLine( 0, 0, 0, 0 );
			g.dispose();
			PaintUtilities.disabledTexturePaint = new java.awt.TexturePaint( image, new java.awt.Rectangle( 0, 0, width, height ) );
		}
		return PaintUtilities.disabledTexturePaint;
	}
	private static java.awt.TexturePaint copyTexturePaint = null;

	public static java.awt.TexturePaint getCopyTexturePaint() {
		if( PaintUtilities.copyTexturePaint != null ) {
			//pass
		} else {
			int width = 8;
			int height = 8;
			java.awt.image.BufferedImage image = new java.awt.image.BufferedImage( width, height, java.awt.image.BufferedImage.TYPE_INT_ARGB );
			java.awt.Graphics g = image.getGraphics();
			g.setColor( new java.awt.Color( 0, 0, 255, 96 ) );
			g.drawLine( 2, 4, 6, 4 );
			g.drawLine( 4, 2, 4, 6 );
			g.dispose();
			PaintUtilities.copyTexturePaint = new java.awt.TexturePaint( image, new java.awt.Rectangle( 0, 0, width, height ) );
		}
		return PaintUtilities.copyTexturePaint;
	}
}
