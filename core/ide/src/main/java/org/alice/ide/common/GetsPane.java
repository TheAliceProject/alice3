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
package org.alice.ide.common;

/**
 * @author Dennis Cosgrove
 */
public class GetsPane extends org.lgna.croquet.views.Label {
	private static java.awt.Paint createGradientPaint( int width, boolean isReversalDesired ) {
		java.awt.Color colorStart = org.alice.ide.ThemeUtilities.getActiveTheme().getColorFor( org.lgna.project.ast.ExpressionStatement.class );
		java.awt.Color colorEnd = colorStart.darker();
		java.awt.Color color0;
		java.awt.Color color1;
		if( isReversalDesired ) {
			color0 = colorEnd;
			color1 = colorStart;
		} else {
			color0 = colorStart;
			color1 = colorEnd;
		}
		return new java.awt.GradientPaint( 0.0f, 0.0f, color0, width, 0.0f, color1 );
	}

	private GetsPane( boolean isTowardLeadingEdge, int length ) {
		this.isTowardLeadingEdge = isTowardLeadingEdge;
		this.length = length;
		this.setIcon( new javax.swing.Icon() {
			private java.awt.FontMetrics getFontMetrics() {
				return GetsPane.this.getAwtComponent().getFontMetrics( GetsPane.this.getFont() );
			}

			@Override
			public int getIconWidth() {
				if( org.alice.ide.croquet.models.ui.formatter.FormatterState.isJava() ) {
					java.awt.FontMetrics fontMetrics = this.getFontMetrics();
					return fontMetrics.getHeight();
				} else {
					return ( this.getIconHeight() * GetsPane.this.length ) + 1;
				}
			}

			@Override
			public int getIconHeight() {
				if( org.alice.ide.croquet.models.ui.formatter.FormatterState.isJava() ) {
					java.awt.FontMetrics fontMetrics = this.getFontMetrics();
					return fontMetrics.charWidth( '=' );
				} else {
					return (int)( GetsPane.this.getFont().getSize2D() * 1.4f );
				}
			}

			@Override
			public void paintIcon( java.awt.Component c, java.awt.Graphics g, int x, int y ) {
				int width = this.getIconWidth();
				int height = this.getIconHeight();
				if( org.alice.ide.croquet.models.ui.formatter.FormatterState.isJava() ) {
					edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.drawCenteredText( g, "=", x, y, width, height );
				} else {

					int halfLineSize = height / 5;
					int yTop = 0;
					int yBottom = getHeight() - 1;
					int yCenter = ( yTop + yBottom ) / 2;
					int yTopLine = yCenter - halfLineSize;
					int yBottomLine = yCenter + halfLineSize;

					final int INSET = 2;
					int xLeft = INSET;
					int xHeadRight = yBottom;
					int xHeadRightInABit = ( xHeadRight * 4 ) / 5;
					int xRight = getWidth() - 1 - ( INSET * 2 );

					int[] xPoints = { xLeft, xHeadRight, xHeadRightInABit, xRight, xRight, xHeadRightInABit, xHeadRight };
					int[] yPoints = { yCenter, yTop, yTopLine, yTopLine, yBottomLine, yBottomLine, yBottom };

					boolean isReversalDesired = GetsPane.this.isReversalDesired();
					if( isReversalDesired ) {
						for( int i = 0; i < xPoints.length; i++ ) {
							xPoints[ i ] = getWidth() - xPoints[ i ];
						}
					}

					java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
					edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.setRenderingHint( g2, java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );

					g2.setPaint( GetsPane.createGradientPaint( width, isReversalDesired ) );
					g2.fillPolygon( xPoints, yPoints, xPoints.length );
					g2.setColor( java.awt.Color.GRAY );
					g2.drawPolygon( xPoints, yPoints, xPoints.length );
				}
			}
		} );
	}

	public GetsPane( boolean isTowardLeadingEdge ) {
		this( isTowardLeadingEdge, 2 );
	}

	private boolean isReversalDesired() {
		java.awt.ComponentOrientation componentOrientation = this.getComponentOrientation();
		if( componentOrientation.isLeftToRight() ) {
			return isTowardLeadingEdge == false;
		} else {
			return isTowardLeadingEdge;
		}
	}

	private final boolean isTowardLeadingEdge;
	private final int length;
}
