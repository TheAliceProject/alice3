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
package org.alice.ide.projecturi.views;

/**
 * @author Dennis Cosgrove
 */
public class PreviewProjectComponent extends org.lgna.croquet.views.SwingComponentView<javax.swing.AbstractButton> {
	private static final int SIZE = 64;
	private static final java.awt.Stroke stroke = new java.awt.BasicStroke( SIZE / 25.0f );

	private static final javax.swing.Icon ICON = new org.lgna.croquet.icon.AbstractIcon( new java.awt.Dimension( SIZE, SIZE ) ) {
		@Override
		protected void paintIcon( java.awt.Component c, java.awt.Graphics2D g2 ) {
			if( c instanceof javax.swing.AbstractButton ) {
				javax.swing.AbstractButton button = (javax.swing.AbstractButton)c;
				javax.swing.ButtonModel buttonModel = button.getModel();

				int width = this.getIconWidth();
				int height = this.getIconHeight();
				if( buttonModel.isEnabled() ) {
					if( buttonModel.isSelected() ) {
						g2.setPaint( button.getForeground() );
						edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.drawCenteredText( g2, "imagine the project running now", 0, 0, width, height );
					} else {
						if( buttonModel.isRollover() ) {
							float xCenter = width * 0.5f;
							float yCenter = height * 0.5f;
							g2.setPaint( new java.awt.Color( 255, 255, 191, 9 ) );
							for( float radius = 50.0f; radius < 80.0f; radius += 4.0f ) {
								java.awt.geom.Ellipse2D.Float shape = new java.awt.geom.Ellipse2D.Float( xCenter - radius, yCenter - radius, radius + radius, radius + radius );
								g2.fill( shape );
							}
						}

						java.awt.Stroke prevStroke = g2.getStroke();
						g2.setStroke( stroke );
						java.awt.geom.RoundRectangle2D.Float rr = new java.awt.geom.RoundRectangle2D.Float( 0, 0, width, height, width * 0.6f, height * 0.6f );
						g2.setColor( edu.cmu.cs.dennisc.java.awt.ColorUtilities.createGray( 70 ) );
						g2.fill( rr );
						g2.setColor( edu.cmu.cs.dennisc.java.awt.ColorUtilities.createGray( 220 ) );
						g2.draw( rr );

						int w = (int)( width * 0.4 );
						int h = (int)( height * 0.45 );
						int xFudge = width / 20;
						edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.fillTriangle( g2, edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.Heading.EAST, ( ( width - w ) / 2 ) + xFudge, ( height - h ) / 2, w, h );
						g2.setStroke( prevStroke );
					}
				} else {
					g2.setPaint( button.getForeground() );
					edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.drawCenteredText( g2, "select project", 0, 0, width, height );
				}
			}
		}
	};

	private static final int DEFAULT_WIDTH = 320;
	private static final int DEFAULT_HEIGHT = (int)( DEFAULT_WIDTH / org.alice.stageide.run.RunComposite.WIDTH_TO_HEIGHT_RATIO );

	@Override
	protected javax.swing.AbstractButton createAwtComponent() {
		javax.swing.AbstractButton rv = new javax.swing.JToggleButton() {
			@Override
			public java.awt.Dimension getPreferredSize() {
				return new java.awt.Dimension( DEFAULT_WIDTH, DEFAULT_HEIGHT );
			}

			@Override
			public java.awt.Dimension getMaximumSize() {
				return this.getPreferredSize();
			}

			@Override
			public void updateUI() {
				this.setUI( new javax.swing.plaf.basic.BasicToggleButtonUI() );
			}
		};
		rv.setRolloverEnabled( true );
		rv.setIcon( ICON );
		rv.setBackground( java.awt.Color.BLACK );
		rv.setForeground( java.awt.Color.YELLOW );
		rv.setAlignmentX( 0.0f );
		return rv;
	}
}
