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
package edu.cmu.cs.dennisc.javax.swing.border;

/**
 * @author Dennis Cosgrove
 */
public abstract class BevelBorder extends javax.swing.border.EmptyBorder {
	private static final java.awt.Color INNER_SHADOW = edu.cmu.cs.dennisc.java.awt.ColorUtilities.createGray( 63 );
	private static final java.awt.Color INNER_HIGHLIGHT = edu.cmu.cs.dennisc.java.awt.ColorUtilities.createGray( 191 );
	private static final java.awt.Color OUTER_SHADOW = INNER_SHADOW.darker();
	private static final java.awt.Color OUTER_HIGHLIGHT = INNER_HIGHLIGHT.brighter();
	private boolean isRaised;

	public BevelBorder( java.awt.Insets insets, boolean isRaised ) {
		super( insets );
		this.isRaised = isRaised;
	}

	public BevelBorder( int top, int left, int bottom, int right, boolean isRaised ) {
		super( top, left, bottom, right );
		this.isRaised = isRaised;
	}

	@Override
	public final void paintBorder( java.awt.Component c, java.awt.Graphics g, int x, int y, int width, int height ) {
		java.awt.Color prevColor = g.getColor();

		int x0 = x;
		int x1 = ( x0 + width ) - 1;
		int y0 = y;
		int y1 = ( y0 + height ) - 1;

		java.awt.Color outerA;
		java.awt.Color outerB;
		java.awt.Color innerA;
		java.awt.Color innerB;
		if( this.isRaised ) {
			outerA = OUTER_SHADOW;
			innerA = INNER_SHADOW;
			innerB = INNER_HIGHLIGHT;
			outerB = OUTER_HIGHLIGHT;
		} else {
			outerB = OUTER_SHADOW;
			innerB = INNER_SHADOW;
			innerA = INNER_HIGHLIGHT;
			outerA = OUTER_HIGHLIGHT;
		}

		g.setColor( outerA );
		g.drawLine( x0, y1, x1, y1 );
		g.drawLine( x1, y1, x1, y0 );
		g.setColor( outerB );
		g.drawLine( x0, y1, x0, y0 );
		g.drawLine( x0, y0, x1, y0 );

		x0 += 1;
		y0 += 1;
		x1 -= 1;
		y1 -= 1;

		g.setColor( innerA );
		g.drawLine( x0, y1, x1, y1 );
		g.drawLine( x1, y1, x1, y0 );
		g.setColor( innerB );
		g.drawLine( x0, y1, x0, y0 );
		g.drawLine( x0, y0, x1, y0 );

		g.setColor( prevColor );
	}
}
