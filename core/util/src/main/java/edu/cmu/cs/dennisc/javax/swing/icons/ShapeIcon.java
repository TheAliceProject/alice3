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
package edu.cmu.cs.dennisc.javax.swing.icons;

/**
 * @author Dennis Cosgrove
 */
public class ShapeIcon implements javax.swing.Icon {
	private java.awt.Shape shape;
	private java.awt.Paint fillPaint;
	private java.awt.Paint drawPaint;
	private int top;
	private int left;
	private int bottom;
	private int right;

	public ShapeIcon( java.awt.Shape shape, java.awt.Paint fillPaint, java.awt.Paint drawPaint, int top, int left, int bottom, int right ) {
		this.shape = shape;
		this.fillPaint = fillPaint;
		this.drawPaint = drawPaint;
		this.top = top;
		this.left = left;
		this.bottom = bottom;
		this.right = right;
	}

	public ShapeIcon( java.awt.Shape shape, java.awt.Paint fillPaint, java.awt.Paint drawPaint, java.awt.Insets insets ) {
		this( shape, fillPaint, drawPaint, insets.top, insets.left, insets.bottom, insets.right );
	}

	public ShapeIcon( java.awt.Shape shape, java.awt.Paint fillPaint, java.awt.Paint drawPaint ) {
		this( shape, fillPaint, drawPaint, 0, 0, 0, 0 );
	}

	@Override
	public int getIconWidth() {
		int rv = this.shape.getBounds().width;
		rv += this.left;
		rv += this.right;
		rv += 1;
		return rv;
	}

	@Override
	public int getIconHeight() {
		int rv = this.shape.getBounds().height;
		rv += this.top;
		rv += this.bottom;
		rv += 1;
		return rv;
	}

	@Override
	public void paintIcon( java.awt.Component c, java.awt.Graphics g, int x, int y ) {
		java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;

		g2.translate( x + this.left, y + this.top );

		java.awt.Paint prevPaint = g2.getPaint();
		if( this.fillPaint != null ) {
			g2.setPaint( this.fillPaint );
			g2.fill( this.shape );
		}
		if( this.drawPaint != null ) {
			g2.setPaint( this.drawPaint );
			g2.draw( this.shape );
		}

		g2.translate( -( x + this.left ), -( y + this.top ) );

		g2.setPaint( prevPaint );
	}
}
