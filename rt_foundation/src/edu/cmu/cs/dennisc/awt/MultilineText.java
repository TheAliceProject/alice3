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
public class MultilineText {
	public enum Alignment {
		LEADING,
		CENTER,
		TRAILING
	}
	class Line {
		private String paragraph;
		private java.awt.font.TextLayout textLayout;
		private int startIndex;
		private int endIndex;
		public Line( String paragraph, java.awt.font.TextLayout textLayout, int startIndex, int endIndex ) {
			this.paragraph = paragraph;
			this.textLayout = textLayout;
			this.startIndex = startIndex;
			this.endIndex = endIndex;
		}
		public void draw( java.awt.Graphics2D g2, float x, float y ) {
			g2.drawString( this.paragraph.substring( this.startIndex, this.endIndex ), x, y );
		}
	}

	private String text;
	private String[] paragraphs;
	private java.awt.FontMetrics fm;
	private double widthGuide;
	private java.util.List< Line > lines;
	private java.awt.geom.Dimension2D aggregateSize;
	public MultilineText( String text ) {
		assert text != null;
		this.text = text;
		this.paragraphs = this.text.split( "\r\n|\r|\n" );
	}
	
	public String getText() {
		return this.text;
	}
	private void updateBoundsIfNecessary( java.awt.Graphics g, double widthGuide ) {
		java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
		java.awt.font.FontRenderContext frc = g2.getFontRenderContext();
		java.awt.FontMetrics fm = g.getFontMetrics();
		if( this.lines == null || this.aggregateSize == null || this.fm != fm || this.widthGuide != widthGuide ) {
			this.lines = new java.util.LinkedList< Line >();
			for( String paragraph : paragraphs ) {
				java.text.AttributedString as = new java.text.AttributedString( paragraph );
				as.addAttribute( java.awt.font.TextAttribute.FONT, g.getFont() );
				java.text.AttributedCharacterIterator aci = as.getIterator();
				
				java.awt.font.LineBreakMeasurer lineBreakMeasurer = new java.awt.font.LineBreakMeasurer( aci, frc );
				while( lineBreakMeasurer.getPosition() < paragraph.length() ) {
					int start = lineBreakMeasurer.getPosition();
					java.awt.font.TextLayout textLayout = lineBreakMeasurer.nextLayout( (float)widthGuide );
					int end = lineBreakMeasurer.getPosition();
					this.lines.add( new Line( paragraph, textLayout, start, end ) );
				}
			}
			assert this.lines.size() > 0;
			this.aggregateSize = new java.awt.Dimension( 0, 0 );
			for( Line line : this.lines ) {
				java.awt.geom.Rectangle2D rect = line.textLayout.getBounds();
				double width = Math.max( aggregateSize.getWidth(), rect.getWidth() );
				double height = aggregateSize.getHeight() + line.textLayout.getAscent() + line.textLayout.getDescent() + line.textLayout.getLeading();
				aggregateSize.setSize( width, height );
			}
			
			this.fm = fm;
			this.widthGuide = widthGuide;
		}
	}
	public java.awt.geom.Dimension2D getDimension( java.awt.Graphics g, double widthGuide ) {
		this.updateBoundsIfNecessary( g, widthGuide );
		return this.aggregateSize;
	}
	public void paint( java.awt.Graphics g, double widthGuide, Alignment alignment, double xBound, double yBound, double widthBound, double heightBound ) {
		this.updateBoundsIfNecessary( g, widthGuide );
		java.awt.geom.Dimension2D size = this.getDimension( g, widthGuide );
		java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
		float x = (float)xBound;
		float y = (float)(yBound + ( heightBound - size.getHeight() ) * 0.5);
		for( Line line : this.lines ) {
			y += line.textLayout.getAscent();

			java.awt.geom.Rectangle2D rect = line.textLayout.getBounds();
			float xPixel = (float)(x - rect.getX());
			float yPixel = (float)(y - rect.getY());
			
			if( alignment == Alignment.CENTER ) {
				xPixel += widthBound * 0.5f - (float)rect.getWidth() * 0.5f;
			} else if( alignment == Alignment.TRAILING ) {
				xPixel += widthBound - (float)rect.getWidth();
			}
			line.draw( g2, xPixel, yPixel );
			y += line.textLayout.getDescent() + line.textLayout.getLeading();
		}
		assert alignment != null;
	}
	public void paint( java.awt.Graphics g, double widthGuide, Alignment alignment, java.awt.geom.Rectangle2D bounds ) {
		this.paint( g, widthGuide, alignment, bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight() );
	}
}
