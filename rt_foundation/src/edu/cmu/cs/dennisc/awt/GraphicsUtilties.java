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
package edu.cmu.cs.dennisc.awt;

/**
 * @author Dennis Cosgrove
 */
public class GraphicsUtilties {
	public static void drawCenteredText( java.awt.Graphics g, String s, int x, int y, int width, int height ) {
		java.awt.FontMetrics fm = g.getFontMetrics();
		int messageWidth = fm.stringWidth( s );
	    int ascent = fm.getMaxAscent ();
	    int descent= fm.getMaxDescent ();
		g.drawString( s, x+width/2-messageWidth/2, y+height/2+ascent/2-descent/2 );
	}
	public static void drawCenteredText( java.awt.Graphics g, String s, java.awt.Dimension size ) {
		drawCenteredText( g, s, 0, 0, size.width, size.height );
	}
	public static void drawCenteredText( java.awt.Graphics g, String s, java.awt.Rectangle rect ) {
		drawCenteredText( g, s, rect.x, rect.y, rect.width, rect.height );
	}
}
