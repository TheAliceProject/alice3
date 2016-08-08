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

package org.lgna.croquet.views;

/**
 * @author Dennis Cosgrove
 */
public class StatusLabel extends SwingComponentView<javax.swing.JLabel> {
	private static final String TEXT_TO_USE_FOR_GOOD_TO_GO_STATUS = "good to go";

	public StatusLabel() {
	}

	public void setStatus( org.lgna.croquet.AbstractSeverityStatusComposite.Status status ) {
		this.checkEventDispatchThread();
		String text;
		if( org.lgna.croquet.AbstractSeverityStatusComposite.IS_GOOD_TO_GO_STATUS == status ) {
			text = TEXT_TO_USE_FOR_GOOD_TO_GO_STATUS;
		} else {
			text = status.getText();
		}
		this.getAwtComponent().setText( text );
	}

	@Override
	protected javax.swing.JLabel createAwtComponent() {
		javax.swing.JLabel rv = new javax.swing.JLabel( TEXT_TO_USE_FOR_GOOD_TO_GO_STATUS ) {
			@Override
			protected void paintComponent( java.awt.Graphics g ) {
				if( this.getText() == TEXT_TO_USE_FOR_GOOD_TO_GO_STATUS ) {
					//pass
				} else {
					super.paintComponent( g );
				}
			}
		};

		float f0 = 0.0f;
		float fA = 0.333f;
		float fB = 0.667f;
		float f1 = 1.0f;

		final int SCALE = 20;
		final int OFFSET = 0;

		f0 *= SCALE;
		fA *= SCALE;
		fB *= SCALE;
		f1 *= SCALE;

		final java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
		path.moveTo( fA, f0 );
		path.lineTo( fB, f0 );
		path.lineTo( f1, fA );
		path.lineTo( f1, fB );
		path.lineTo( fB, f1 );
		path.lineTo( fA, f1 );
		path.lineTo( f0, fB );
		path.lineTo( f0, fA );
		path.closePath();
		rv.setIcon( new javax.swing.Icon() {
			@Override
			public int getIconWidth() {
				return SCALE + OFFSET + OFFSET;
			}

			@Override
			public int getIconHeight() {
				return SCALE + OFFSET + OFFSET;
			}

			@Override
			public void paintIcon( java.awt.Component c, java.awt.Graphics g, int x, int y ) {
				java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
				java.awt.geom.AffineTransform m = g2.getTransform();
				java.awt.Font font = g2.getFont();
				java.awt.Paint paint = g2.getPaint();
				try {
					g2.translate( OFFSET + x, OFFSET + y );
					g2.fill( path );
					g2.setPaint( java.awt.Color.WHITE );
					g2.draw( path );
					g2.setPaint( java.awt.Color.WHITE );
					g2.setFont( new java.awt.Font( null, java.awt.Font.BOLD, 12 ) );
					g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
					final int FUDGE_X = 1;
					edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.drawCenteredText( g2, "X", FUDGE_X, 0, SCALE, SCALE );
				} finally {
					g2.setTransform( m );
					g2.setPaint( paint );
					g2.setFont( font );
				}
			}
		} );
		return rv;
	}
}
