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
package edu.cmu.cs.dennisc.render.gl.imp.adapters.graphics;

public abstract class GlrTitle<T extends edu.cmu.cs.dennisc.scenegraph.graphics.Title> extends GlrShapeEnclosedText<T> {
	protected static final float VERTICAL_MARGIN_TIMES_2 = 20.0f;
	private java.awt.geom.Rectangle2D.Float backgroundBounds = new java.awt.geom.Rectangle2D.Float();

	@Override
	protected float getWrapWidth( java.awt.Rectangle actualViewport ) {
		return (float)( actualViewport.getWidth() * 0.9 );
	}

	protected abstract java.awt.geom.Rectangle2D.Float getFillBounds( java.awt.geom.Rectangle2D.Float rv, java.awt.Rectangle actualViewport, java.awt.geom.Dimension2D multilineTextSize );

	@Override
	protected void render(
			edu.cmu.cs.dennisc.render.Graphics2D g2,
			edu.cmu.cs.dennisc.render.RenderTarget renderTarget,
			java.awt.Rectangle actualViewport,
			edu.cmu.cs.dennisc.scenegraph.AbstractCamera camera,
			edu.cmu.cs.dennisc.java.awt.MultilineText multilineText,
			java.awt.Font font,
			java.awt.Color textColor,
			float wrapWidth,
			java.awt.Color fillColor,
			java.awt.Color outlineColor ) {
		synchronized( this.backgroundBounds ) {
			g2.setFont( font );
			java.awt.geom.Dimension2D size = multilineText.getDimension( g2, wrapWidth );
			this.getFillBounds( this.backgroundBounds, actualViewport, size );
			if( fillColor != null ) {
				g2.setColor( fillColor );
				g2.fill( this.backgroundBounds );
			}
			if( outlineColor != null ) {
				g2.setColor( outlineColor );
				g2.draw( this.backgroundBounds );
			}
			if( textColor != null ) {
				g2.setColor( textColor );
				g2.setFont( font );
				multilineText.paint( g2, wrapWidth, edu.cmu.cs.dennisc.java.awt.TextAlignment.CENTER, this.backgroundBounds );
				//				float x = this.backgroundBounds.x - (float)textBounds.getX() + this.backgroundBounds.width * 0.5f - (float)textBounds.getWidth() * 0.5f;
				//				float y = this.backgroundBounds.y - (float)textBounds.getY() + this.backgroundBounds.height * 0.5f - (float)textBounds.getHeight() * 0.5f;
				//				g2.setFont( font );
				//				g2.drawString( text, x, y );
			}
		}
	}
}
