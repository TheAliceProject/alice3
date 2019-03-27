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
package org.alice.media.youtube.croquet.views.icons;

import edu.cmu.cs.dennisc.java.awt.ColorUtilities;
import edu.cmu.cs.dennisc.java.awt.GraphicsContext;

import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.Icon;

/**
 * @author Dennis Cosgrove
 */
public class IsPlayingIcon implements Icon {
	private static final int SIZE = 20;
	private static final Color PLAY_BASE_COLOR = new Color( 63, 191, 63 );
	private static final Color PLAY_HIGHLIGHT_COLOR = ColorUtilities.scaleHSB( PLAY_BASE_COLOR, 1.0, 1.0, 1.5 );
	private static final Color PLAY_SHADOW_COLOR = ColorUtilities.scaleHSB( PLAY_BASE_COLOR, 1.0, 1.0, 0.5 );
	private static final Color PAUSE_COLOR = Color.BLACK;

	@Override
	public int getIconHeight() {
		return SIZE;
	}

	@Override
	public int getIconWidth() {
		return SIZE;
	}

	@Override
	public void paintIcon( Component c, Graphics g, int x, int y ) {
		if( c instanceof AbstractButton ) {
			GraphicsContext gc = GraphicsContext.getInstanceAndPushGraphics( g );
			gc.pushAndSetAntialiasing( true );
			gc.pushPaint();
			try {
				AbstractButton button = (AbstractButton)c;
				ButtonModel buttonModel = button.getModel();
				if( buttonModel.isSelected() ) {
					g.setColor( PAUSE_COLOR );
					int width = 4;
					int height = SIZE - 4;
					int x0 = x + 2;
					int x1 = ( x + SIZE ) - width - 4;
					int y0 = y + 2;
					g.fillRect( x0, y0, width, height );
					g.fillRect( x1, y0, width, height );
				} else {
					double x0 = x + 2;
					double x1 = ( x + SIZE ) - 4;
					double y0 = y + 2;
					double y1 = ( y0 + SIZE ) - 4;
					double yC = ( y0 + y1 ) * 0.5;
					GeneralPath path = new GeneralPath();
					path.moveTo( x0, y0 );
					path.lineTo( x0, y1 );
					path.lineTo( x1, yC );
					path.closePath();
					Graphics2D g2 = (Graphics2D)g;
					g2.setPaint( new GradientPaint( x, y, PLAY_HIGHLIGHT_COLOR, x + SIZE, y + SIZE, PLAY_SHADOW_COLOR ) );
					g2.fill( path );
					g.setColor( Color.BLACK );
					g2.draw( path );
				}
			} finally {
				gc.popAll();
			}
		}
	}
}
