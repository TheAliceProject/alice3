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
package alice.ide.lookandfeel;

/**
 * @author Dennis Cosgrove
 */
public class ArrowDropDownRenderer implements DropDownRenderer {
	public void fillBounds( Object context, java.awt.Component c, java.awt.Graphics2D g2, int x, int y, int width, int height ) {
		g2.fillRect( x, y, width, height );
	}
	public void paintPrologue( Object context, java.awt.Component c, java.awt.Graphics2D g2, int x, int y, int width, int height, boolean isActive, boolean isPressed, boolean isSelected ) {
		java.awt.Color prev = g2.getColor();
		try {
			//g2.setColor( edu.cmu.cs.dennisc.awt.ColorUtilities.createGray( 230 ) );
			g2.setColor( java.awt.Color.WHITE );
			fillBounds( context, c, g2, x, y, width, height );
			
			final int INSET = 4;
			int size = ArrowDropDownBorderFactory.AFFORDANCE_SIZE;

			int x0 = width - INSET / 2 - ArrowDropDownBorderFactory.AFFORDANCE_SIZE;
			int x1 = x0 + size;
			int xC = (x0 + x1) / 2;

			int y0 = INSET + 2;
			int y1 = y0 + size;

			java.awt.Color triangleFill;
			java.awt.Color triangleOutline;
			if( isActive ) {
				triangleFill = java.awt.Color.YELLOW;
				triangleOutline = java.awt.Color.BLACK;
			} else {
				triangleFill = edu.cmu.cs.dennisc.awt.ColorUtilities.createGray( 192 );
				triangleOutline = null;
			}

			g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );

			int[] xs = { x0, xC, x1 };
			int[] ys = { y0, y1, y0 };
			g2.setColor( triangleFill );
			g2.fillPolygon( xs, ys, 3 );
			if( triangleOutline != null ) {
				g2.setColor( triangleOutline );
				g2.drawPolygon( xs, ys, 3 );
			}
		} finally {
			g2.setColor( prev );
		}
	}
	public void paintEpilogue( Object context, java.awt.Component c, java.awt.Graphics2D g2, int x, int y, int width, int height, boolean isActive, boolean isPressed, boolean isSelected ) {
		if( isActive ) {
			g2.setStroke( new java.awt.BasicStroke( 3.0f ) );
			g2.setColor( java.awt.Color.BLUE );
			g2.draw( new java.awt.geom.Rectangle2D.Float( 1.5f, 1.5f, width-3.0f, height-3.0f ) );
		} else {
			g2.setColor( java.awt.Color.LIGHT_GRAY );
			g2.drawRect( x, y, width-1, height-1 );
		}
	}
}
