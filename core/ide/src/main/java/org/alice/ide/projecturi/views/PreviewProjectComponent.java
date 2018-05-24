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

import edu.cmu.cs.dennisc.java.awt.ColorUtilities;
import edu.cmu.cs.dennisc.java.awt.GraphicsUtilities;
import org.alice.stageide.run.RunComposite;
import org.lgna.croquet.icon.AbstractIcon;
import org.lgna.croquet.views.SwingComponentView;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JToggleButton;
import javax.swing.plaf.basic.BasicToggleButtonUI;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

/**
 * @author Dennis Cosgrove
 */
public class PreviewProjectComponent extends SwingComponentView<AbstractButton> {
	private static final int SIZE = 64;
	private static final Stroke stroke = new BasicStroke( SIZE / 25.0f );

	private static final Icon ICON = new AbstractIcon( new Dimension( SIZE, SIZE ) ) {
		@Override
		protected void paintIcon( Component c, Graphics2D g2 ) {
			if( c instanceof AbstractButton ) {
				AbstractButton button = (AbstractButton)c;
				ButtonModel buttonModel = button.getModel();

				int width = this.getIconWidth();
				int height = this.getIconHeight();
				if( buttonModel.isEnabled() ) {
					if( buttonModel.isSelected() ) {
						g2.setPaint( button.getForeground() );
						GraphicsUtilities.drawCenteredText( g2, "imagine the project running now", 0, 0, width, height );
					} else {
						if( buttonModel.isRollover() ) {
							float xCenter = width * 0.5f;
							float yCenter = height * 0.5f;
							g2.setPaint( new Color( 255, 255, 191, 9 ) );
							for( float radius = 50.0f; radius < 80.0f; radius += 4.0f ) {
								Ellipse2D.Float shape = new Ellipse2D.Float( xCenter - radius, yCenter - radius, radius + radius, radius + radius );
								g2.fill( shape );
							}
						}

						Stroke prevStroke = g2.getStroke();
						g2.setStroke( stroke );
						RoundRectangle2D.Float rr = new RoundRectangle2D.Float( 0, 0, width, height, width * 0.6f, height * 0.6f );
						g2.setColor( ColorUtilities.createGray( 70 ) );
						g2.fill( rr );
						g2.setColor( ColorUtilities.createGray( 220 ) );
						g2.draw( rr );

						int w = (int)( width * 0.4 );
						int h = (int)( height * 0.45 );
						int xFudge = width / 20;
						GraphicsUtilities.fillTriangle( g2, GraphicsUtilities.Heading.EAST, ( ( width - w ) / 2 ) + xFudge, ( height - h ) / 2, w, h );
						g2.setStroke( prevStroke );
					}
				} else {
					g2.setPaint( button.getForeground() );
					GraphicsUtilities.drawCenteredText( g2, "select project", 0, 0, width, height );
				}
			}
		}
	};

	private static final int DEFAULT_WIDTH = 320;
	private static final int DEFAULT_HEIGHT = (int)( DEFAULT_WIDTH / RunComposite.WIDTH_TO_HEIGHT_RATIO );

	@Override
	protected AbstractButton createAwtComponent() {
		AbstractButton rv = new JToggleButton() {
			@Override
			public Dimension getPreferredSize() {
				return new Dimension( DEFAULT_WIDTH, DEFAULT_HEIGHT );
			}

			@Override
			public Dimension getMaximumSize() {
				return this.getPreferredSize();
			}

			@Override
			public void updateUI() {
				this.setUI( new BasicToggleButtonUI() );
			}
		};
		rv.setRolloverEnabled( true );
		rv.setIcon( ICON );
		rv.setBackground( Color.BLACK );
		rv.setForeground( Color.YELLOW );
		rv.setAlignmentX( 0.0f );
		return rv;
	}
}
