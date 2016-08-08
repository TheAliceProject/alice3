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
package edu.cmu.cs.dennisc.java.awt.geom;

/**
 * @author Dennis Cosgrove
 */
public class MultilineText extends Transformable {
	private edu.cmu.cs.dennisc.java.awt.MultilineText multilineText;
	private java.awt.Font font;
	private edu.cmu.cs.dennisc.java.awt.TextAlignment alignment;
	private java.awt.Paint paint;
	private float wrapWidth;

	public MultilineText( String text, java.awt.Font font, edu.cmu.cs.dennisc.java.awt.TextAlignment alignment, java.awt.Paint paint ) {
		this.multilineText = new edu.cmu.cs.dennisc.java.awt.MultilineText( text );
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

	public edu.cmu.cs.dennisc.java.awt.TextAlignment getAlignment() {
		return this.alignment;
	}

	public void setAlignment( edu.cmu.cs.dennisc.java.awt.TextAlignment alignment ) {
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
		return new java.awt.geom.Rectangle2D.Double( -width * 0.5, -height * 0.5, width, height );
	}

	@Override
	protected void paintComponent( edu.cmu.cs.dennisc.java.awt.geom.GraphicsContext gc ) {
		java.awt.Graphics2D g2 = gc.getAWTGraphics2D();
		g2.setPaint( this.paint );
		java.awt.geom.Rectangle2D bounds = this.getBounds( g2 );
		this.multilineText.paint( g2, this.wrapWidth, this.alignment, bounds );
	}

	@Override
	protected java.awt.geom.Area update( java.awt.geom.Area rv, edu.cmu.cs.dennisc.java.awt.geom.TransformContext tc ) {
		//todo
		return rv;
	}
}
