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

package edu.cmu.cs.dennisc.javax.swing.components;

/**
 * @author Dennis Cosgrove
 */
public final class JCloseButton extends javax.swing.JButton {
	private static class CloseButtonUI extends javax.swing.plaf.basic.BasicButtonUI {
		private static final java.awt.Color BASE_COLOR = new java.awt.Color( 127, 63, 63 );
		private static final java.awt.Color HIGHLIGHT_COLOR = edu.cmu.cs.dennisc.java.awt.ColorUtilities.shiftHSB( BASE_COLOR, 0, 0, +0.25f );
		private static final java.awt.Color PRESS_COLOR = edu.cmu.cs.dennisc.java.awt.ColorUtilities.shiftHSB( BASE_COLOR, 0, 0, -0.125f );

		private static final int SIZE = 14;

		@Override
		public void paint( java.awt.Graphics g, javax.swing.JComponent c ) {
			javax.swing.AbstractButton button = (javax.swing.AbstractButton)c;
			javax.swing.ButtonModel model = button.getModel();

			java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;

			int closeWidth = SIZE;
			int closeHeight = closeWidth;
			float size = Math.min( closeWidth, closeHeight ) * 0.9f;

			float w = size;
			float h = size * 0.25f;
			float xC = -w * 0.5f;
			float yC = -h * 0.5f;
			java.awt.geom.RoundRectangle2D.Float rr = new java.awt.geom.RoundRectangle2D.Float( xC, yC, w, h, h, h );

			java.awt.geom.Area area0 = new java.awt.geom.Area( rr );
			java.awt.geom.Area area1 = new java.awt.geom.Area( rr );

			java.awt.geom.AffineTransform m0 = new java.awt.geom.AffineTransform();
			m0.rotate( Math.PI * 0.25 );
			area0.transform( m0 );

			java.awt.geom.AffineTransform m1 = new java.awt.geom.AffineTransform();
			m1.rotate( Math.PI * 0.75 );
			area1.transform( m1 );

			area0.add( area1 );

			int x0 = 0;
			int y0 = 0;

			java.awt.geom.AffineTransform m = new java.awt.geom.AffineTransform();
			m.translate( x0 + ( closeWidth / 2 ), y0 + ( closeHeight / 2 ) );
			area0.transform( m );

			java.awt.Paint prevPaint = g2.getPaint();
			g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
			if( model.isRollover() || model.isArmed() ) {
				if( model.isPressed() ) {
					//pass
				} else {
					g2.setPaint( HIGHLIGHT_COLOR );
				}
			} else {
				g2.setPaint( java.awt.Color.WHITE );
			}

			g2.fill( area0 );

			boolean isParentSelected;
			java.awt.Container parent = button.getParent();
			if( parent instanceof javax.swing.AbstractButton ) {
				javax.swing.AbstractButton parentButton = (javax.swing.AbstractButton)parent;
				isParentSelected = parentButton.isSelected();
			} else {
				isParentSelected = false;
			}

			if( isParentSelected ) {
				g2.setPaint( java.awt.Color.BLACK );
			} else {
				g2.setPaint( java.awt.Color.GRAY );
			}
			g2.draw( area0 );
			g2.setPaint( prevPaint );
		}

		@Override
		public java.awt.Dimension getPreferredSize( javax.swing.JComponent c ) {
			return new java.awt.Dimension( SIZE, SIZE );
		}
	}

	private boolean isVisibleOnlyWhenParentIsSelected;

	public JCloseButton( boolean isVisibleOnlyWhenParentIsSelected ) {
		this.isVisibleOnlyWhenParentIsSelected = isVisibleOnlyWhenParentIsSelected;
		this.setOpaque( false );
		this.setAlignmentY( java.awt.Component.CENTER_ALIGNMENT );
		this.setBorder( null );
		this.setRolloverEnabled( true );
	}

	@Override
	public void updateUI() {
		this.setUI( new CloseButtonUI() );
	}

	@Override
	public java.awt.Dimension getMaximumSize() {
		return this.getPreferredSize();
	}

	@Override
	public boolean contains( int x, int y ) {
		if( this.isVisibleOnlyWhenParentIsSelected ) {
			java.awt.Container parent = this.getParent();
			if( parent instanceof javax.swing.AbstractButton ) {
				javax.swing.AbstractButton button = (javax.swing.AbstractButton)parent;
				if( button.isSelected() ) {
					//pass
				} else {
					return false;
				}
			}
		}
		return super.contains( x, y );
	}

	@Override
	public boolean isVisible() {
		if( this.isVisibleOnlyWhenParentIsSelected ) {
			java.awt.Container parent = this.getParent();
			if( parent instanceof javax.swing.AbstractButton ) {
				javax.swing.AbstractButton button = (javax.swing.AbstractButton)parent;
				if( button.isSelected() ) {
					//pass
				} else {
					return false;
				}
			}
		}
		return super.isVisible();
	}
}
