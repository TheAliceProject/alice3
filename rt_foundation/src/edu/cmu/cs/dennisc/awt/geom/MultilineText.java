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
package edu.cmu.cs.dennisc.awt.geom;

/**
 * @author Dennis Cosgrove
 */
public class MultilineText extends Transformable {
	private edu.cmu.cs.dennisc.awt.MultilineText multilineText;
	private java.awt.Font font;
	private edu.cmu.cs.dennisc.awt.TextAlignment alignment;
	private java.awt.Paint paint;
	private float wrapWidth;
	public MultilineText( String text, java.awt.Font font, edu.cmu.cs.dennisc.awt.TextAlignment alignment, java.awt.Paint paint ) {
		this.multilineText = new edu.cmu.cs.dennisc.awt.MultilineText( text );
		this.font = font;
		this.alignment = alignment;
		this.paint = paint;
		this.wrapWidth = Float.NaN;
	}
	public java.awt.Font getFont() {
		return this.font;
	}
	public void setFont( java.awt.Font font ) {
		this.font = font;
	}
	public edu.cmu.cs.dennisc.awt.TextAlignment getAlignment() {
		return this.alignment;
	}
	public void setAlignment( edu.cmu.cs.dennisc.awt.TextAlignment alignment ) {
		this.alignment = alignment;
	}
	public java.awt.Paint getPaint() {
		return this.paint;
	}
	public void setPaint( java.awt.Paint paint ) {
		this.paint = paint;
	}
	
	public float getWrapWidth() {
		return this.wrapWidth;
	}
	public void setWrapWidth( float wrapWidth ) {
		this.wrapWidth = wrapWidth;
	}
	
	public java.awt.geom.Rectangle2D getBounds( java.awt.Graphics g ) {
		java.awt.geom.Dimension2D size = this.multilineText.getDimension( g, this.wrapWidth );
		double width = size.getWidth();
		double height = size.getHeight();
		return new java.awt.geom.Rectangle2D.Double( -width*0.5, -height*0.5, width, height );
	}
	
	@Override
	protected void paintComponent( edu.cmu.cs.dennisc.awt.geom.GraphicsContext gc ) {
		java.awt.Graphics2D g2 = gc.getAWTGraphics2D();
		g2.setPaint( this.paint );
		java.awt.geom.Rectangle2D bounds = this.getBounds( g2 );
		this.multilineText.paint( g2, this.wrapWidth, this.alignment, bounds );
	}

	@Override
	protected java.awt.geom.Area update( java.awt.geom.Area rv, edu.cmu.cs.dennisc.awt.geom.TransformContext tc ) {
		//todo
		return rv;
	}
}
