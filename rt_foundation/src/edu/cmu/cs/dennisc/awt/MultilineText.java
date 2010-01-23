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
	private String text;
	private String[] paragraphs;
	private java.awt.FontMetrics fm;
	private double widthGuide;
	private java.util.List< edu.cmu.cs.dennisc.pattern.Tuple2< String, java.awt.geom.Rectangle2D > > lineRectPairs;
	private java.awt.geom.Dimension2D aggregateSize;
	public MultilineText( String text ) {
		assert text != null;
		this.text = text;
		this.paragraphs = this.text.split( "\n" );
	}
	
	public String getText() {
		return this.text;
	}
	private static float getLineSpacing( String line, java.awt.Graphics g, java.awt.FontMetrics fm ) {
		java.awt.font.LineMetrics lm = fm.getLineMetrics( line, g );
		return lm.getLeading();
	}
	private void updateBoundsIfNecessary( java.awt.Graphics g, double widthGuide ) {
		java.awt.FontMetrics fm = g.getFontMetrics();
		if( this.lineRectPairs == null || this.aggregateSize == null || this.fm != fm || this.widthGuide != widthGuide ) {
			this.lineRectPairs = new java.util.LinkedList< edu.cmu.cs.dennisc.pattern.Tuple2< String, java.awt.geom.Rectangle2D > >();
			for( String paragraph : paragraphs ) {
				java.awt.geom.Rectangle2D bounds = null;
				int n = paragraph.length();
//				while( n>0 ) {
					bounds = fm.getStringBounds( paragraph, 0, n, g );
//					if( bounds.getWidth() < widthMax ) {
//						break;
//					}
//				}
					this.lineRectPairs.add( new edu.cmu.cs.dennisc.pattern.Tuple2< String, java.awt.geom.Rectangle2D >( paragraph, bounds ) );
			}
			assert this.lineRectPairs.size() > 0;
			this.aggregateSize = new java.awt.Dimension( 0, 0 );
			for( edu.cmu.cs.dennisc.pattern.Tuple2< String, java.awt.geom.Rectangle2D > lineRectPair : this.lineRectPairs ) {
				String line = lineRectPair.getA();
				java.awt.geom.Rectangle2D rect = lineRectPair.getB();
				double width = Math.max( aggregateSize.getWidth(), rect.getWidth() );
				double height = aggregateSize.getHeight() + rect.getHeight() + getLineSpacing( line, g, fm );
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
		double x = xBound;
		double y = yBound + ( heightBound - size.getHeight() ) * 0.5;
		for( edu.cmu.cs.dennisc.pattern.Tuple2< String, java.awt.geom.Rectangle2D > lineRectPair : this.lineRectPairs ) {
			String line = lineRectPair.getA();
			java.awt.geom.Rectangle2D rect = lineRectPair.getB();
			java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
			
			float xPixel = (float)(x - rect.getX());
			float yPixel = (float)(y - rect.getY());
			
			if( alignment == Alignment.CENTER ) {
				xPixel += widthBound * 0.5f - (float)rect.getWidth() * 0.5f;
			} else if( alignment == Alignment.TRAILING ) {
				xPixel += widthBound - (float)rect.getWidth();
			}
			g2.drawString( line, xPixel, yPixel );
			y += rect.getHeight();
			y += MultilineText.getLineSpacing( line, g2, fm );
		}
		assert alignment != null;
	}
	public void paint( java.awt.Graphics g, double widthGuide, Alignment alignment, java.awt.geom.Rectangle2D bounds ) {
		this.paint( g, widthGuide, alignment, bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight() );
	}
}
