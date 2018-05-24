/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may
 *    "Alice" appear in their name, without prior written permission of
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is
 *    contributed by Electronic Arts Inc. and may be used for personal,
 *    non-commercial, and academic use only. Redistributions of any program
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in
 *    The Alice 3.0 Art Gallery License.
 *
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *******************************************************************************/
package edu.cmu.cs.dennisc.java.awt;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public class MultilineText {
	private class Line {
		private String paragraph;
		private TextLayout textLayout;
		private int startIndex;
		private int endIndex;

		public Line( String paragraph, TextLayout textLayout, int startIndex, int endIndex ) {
			this.paragraph = paragraph;
			this.textLayout = textLayout;
			this.startIndex = startIndex;
			this.endIndex = endIndex;
		}

		public void draw( Graphics2D g2, float x, float y ) {
			g2.drawString( this.paragraph.substring( this.startIndex, this.endIndex ), x, y );
		}
	}

	private String text;
	private String[] paragraphs;
	private FontMetrics fm;
	private float wrapWidth;
	private List<Line> lines;
	private Dimension2D aggregateSize;

	public MultilineText( String text ) {
		assert text != null;
		this.text = text;
		this.paragraphs = this.text.split( "\r\n|\r|\n" );
	}

	public String getText() {
		return this.text;
	}

	private void updateBoundsIfNecessary( Graphics g, float wrapWidth ) {
		assert Float.isNaN( wrapWidth ) == false;
		Graphics2D g2 = (Graphics2D)g;
		FontRenderContext frc = g2.getFontRenderContext();
		FontMetrics fm = g.getFontMetrics();
		if( ( this.lines == null ) || ( this.aggregateSize == null ) || ( this.fm != fm ) || ( this.wrapWidth != wrapWidth ) ) {
			this.lines = new LinkedList<Line>();
			for( String paragraph : paragraphs ) {
				if( paragraph.length() > 0 ) {
					AttributedString as = new AttributedString( paragraph );
					as.addAttribute( TextAttribute.FONT, g.getFont() );
					AttributedCharacterIterator aci = as.getIterator();

					LineBreakMeasurer lineBreakMeasurer = new LineBreakMeasurer( aci, frc );
					while( lineBreakMeasurer.getPosition() < paragraph.length() ) {
						int start = lineBreakMeasurer.getPosition();
						TextLayout textLayout = lineBreakMeasurer.nextLayout( wrapWidth );
						int end = lineBreakMeasurer.getPosition();
						this.lines.add( new Line( paragraph, textLayout, start, end ) );
					}
				}
			}
			this.aggregateSize = new Dimension( 1, 1 );
			if( this.lines.size() > 0 ) {
				for( Line line : this.lines ) {
					Rectangle2D rect = line.textLayout.getBounds();
					double width = Math.max( aggregateSize.getWidth(), rect.getWidth() );
					double height = aggregateSize.getHeight() + line.textLayout.getAscent() + line.textLayout.getDescent() + line.textLayout.getLeading();
					aggregateSize.setSize( width, height );
				}
			}
			this.fm = fm;
			this.wrapWidth = wrapWidth;
		}
	}

	public Dimension2D getDimension( Graphics g, float wrapWidth ) {
		this.updateBoundsIfNecessary( g, wrapWidth );
		return this.aggregateSize;
	}

	public void paint( Graphics g, float wrapWidth, TextAlignment alignment, double xBound, double yBound, double widthBound, double heightBound ) {
		this.updateBoundsIfNecessary( g, wrapWidth );
		Dimension2D size = this.getDimension( g, wrapWidth );
		Graphics2D g2 = (Graphics2D)g;
		float x = (float)xBound;
		float y = (float)( yBound + ( ( heightBound - size.getHeight() ) * 0.5 ) );
		for( Line line : this.lines ) {
			y += line.textLayout.getAscent();

			Rectangle2D rect = line.textLayout.getBounds();
			//			float xPixel = (float)(x - rect.getX());
			//			float yPixel = (float)(y - rect.getY());
			float xPixel = x;
			float yPixel = y;

			if( alignment == TextAlignment.CENTER ) {
				xPixel += ( widthBound * 0.5f ) - ( (float)rect.getWidth() * 0.5f );
			} else if( alignment == TextAlignment.TRAILING ) {
				xPixel += widthBound - (float)rect.getWidth();
			}
			line.draw( g2, xPixel, yPixel );
			y += line.textLayout.getDescent() + line.textLayout.getLeading();
		}
		assert alignment != null;
	}

	public void paint( Graphics g, float wrapWidth, TextAlignment alignment, Rectangle2D bounds ) {
		double x;
		double y;
		double width;
		double height;
		if( bounds != null ) {
			x = bounds.getX();
			y = bounds.getY();
			width = bounds.getWidth();
			height = bounds.getHeight();
		} else {
			x = 0;
			y = 0;
			Dimension2D size = this.getDimension( g, wrapWidth );
			width = size.getWidth();
			height = size.getHeight();
		}
		this.paint( g, wrapWidth, alignment, x, y, width, height );
	}
}
