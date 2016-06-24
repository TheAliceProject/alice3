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
package edu.cmu.cs.dennisc.javax.swing.plaf;

/**
 * @author Dennis Cosgrove
 */
public class SmallerFootprintScrollBarUI extends javax.swing.plaf.basic.BasicScrollBarUI {
	public static final int INSET = 2;

	public static javax.swing.plaf.ScrollBarUI createUI() {
		return new SmallerFootprintScrollBarUI();
	}

	@Override
	protected void installDefaults() {
		super.installDefaults();
		this.thumbRolloverColor = edu.cmu.cs.dennisc.java.awt.ColorUtilities.createGray( 100 );
		this.thumbPressedColor = new java.awt.Color( 100, 140, 255 );
	}

	@Override
	protected void paintThumb( java.awt.Graphics g, javax.swing.JComponent c, java.awt.Rectangle thumbBounds ) {
		//super.paintThumb( g, c, thumbBounds );
		if( c instanceof javax.swing.JScrollBar ) {
			javax.swing.JScrollBar jScrollBar = (javax.swing.JScrollBar)c;
			int span = jScrollBar.getOrientation() == javax.swing.JScrollBar.VERTICAL ? c.getWidth() : c.getHeight();
			int arc = span - INSET - INSET;
			java.awt.Shape shape = new java.awt.geom.RoundRectangle2D.Float( thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, arc, arc );
			java.awt.Paint paint;
			if( this.isDragging ) {
				paint = this.thumbPressedColor;
			} else {
				paint = isThumbRollover() ? this.thumbRolloverColor : this.thumbColor;
			}
			java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
			Object prevAntialiasing = edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.setAntialiasing( g2, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
			g2.setPaint( paint );
			g2.fill( shape );
			edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.setAntialiasing( g2, prevAntialiasing );
		}
	}

	@Override
	protected void paintTrack( java.awt.Graphics g, javax.swing.JComponent c, java.awt.Rectangle trackBounds ) {
		//super.paintTrack( g, c, trackBounds );
	}

	private static javax.swing.JButton create0SizeButton() {
		javax.swing.JButton rv = new javax.swing.JButton();
		java.awt.Dimension size = new java.awt.Dimension( 0, 0 );
		rv.setMinimumSize( size );
		rv.setPreferredSize( size );
		rv.setMaximumSize( size );
		return rv;
	}

	@Override
	protected javax.swing.JButton createIncreaseButton( int orientation ) {
		return create0SizeButton();
	}

	@Override
	protected javax.swing.JButton createDecreaseButton( int orientation ) {
		return create0SizeButton();
	}

	private java.awt.Color thumbPressedColor;
	private java.awt.Color thumbRolloverColor;
}
