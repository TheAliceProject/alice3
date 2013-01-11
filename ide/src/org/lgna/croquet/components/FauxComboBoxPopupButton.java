/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
 */
package org.lgna.croquet.components;

/**
 * @author Dennis Cosgrove
 */
public class FauxComboBoxPopupButton extends AbstractPopupButton {
	public FauxComboBoxPopupButton( org.lgna.croquet.PopupPrepModel model ) {
		super( model );
	}

	private static final java.awt.Color TOP_COLOR = new java.awt.Color( 255, 255, 255, 91 );
	private static final java.awt.Color BOTTOM_COLOR = new java.awt.Color( 57, 105, 138, 91 );
	private static final java.awt.Color LINE_COLOR = new java.awt.Color( 169, 176, 190 );

	private static final java.awt.Color SELECTED_COLOR = new java.awt.Color( 57, 105, 138 );
	private static final java.awt.Color SELECTED_LINE_COLOR = java.awt.Color.DARK_GRAY;

	@Override
	protected javax.swing.AbstractButton createSwingButton() {
		final int OUTER_PAD = 6;
		JPopupButton rv = new JPopupButton() {
			private int getArrowSize() {
				return ( this.getHeight() * 1 ) / 3;
			}

			private int getComboPad() {
				return this.getArrowSize() / 2;
			}

			@Override
			public java.awt.Insets getMargin() {
				java.awt.Insets rv = super.getMargin();
				if( rv != null ) {
					rv.right += this.getArrowSize();
					rv.right += this.getComboPad();
					rv.right += OUTER_PAD;
				}
				return rv;
			}

			@Override
			protected void paintBorder( java.awt.Graphics g ) {
				super.paintBorder( g );
				final int SIZE = this.getArrowSize();
				final javax.swing.Icon ARROW_ICON = new edu.cmu.cs.dennisc.javax.swing.icons.DropDownArrowIcon( SIZE );

				java.awt.Insets insets = this.getInsets();
				java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
				int width = this.getWidth();
				int height = this.getHeight();
				int x = ( width - insets.right ) + OUTER_PAD;
				if( this.getUI().getClass().getSimpleName().contains( "Synth" ) ) {
					double round = 8;
					double inset = 2;
					java.awt.geom.RoundRectangle2D r = new java.awt.geom.RoundRectangle2D.Double( inset, inset, width - ( inset * 2 ), ( height - ( inset * 2 ) ) + 1, round, round );

					java.awt.Shape prevClip = g2.getClip();

					g2.setClip( edu.cmu.cs.dennisc.java.awt.geom.AreaUtilities.createIntersection( prevClip, r ) );

					javax.swing.ButtonModel buttonModel = this.getModel();
					boolean isSelected = buttonModel.isPressed() || buttonModel.isSelected();
					if( isSelected ) {
						g2.setPaint( SELECTED_COLOR );
					} else {
						g2.setPaint( new java.awt.GradientPaint( width, 0, TOP_COLOR, width, ( 2 * height ) / 3, BOTTOM_COLOR ) );
					}
					g.fillRect( x, 0, width - x, height );
					if( isSelected ) {
						g2.setPaint( SELECTED_LINE_COLOR );
					} else {
						g2.setPaint( LINE_COLOR );
					}
					g2.fillRect( x, 0, 1, height );
					g2.setClip( prevClip );
				}
				ARROW_ICON.paintIcon( this, g2, x + this.getComboPad(), ( height - SIZE ) / 2 );
			}
		};
		rv.setHorizontalTextPosition( javax.swing.SwingConstants.LEADING );
		return rv;
	}
}
