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

import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.Icon;

/**
 * @author Matt May
 */
public class IsRecordingIcon implements Icon {
	private static final int SIZE = 20;
	private static final Color RECORD_BASE_COLOR = new Color( 191, 63, 63 );
	private static final Color RECORD_HIGHLIGHT_COLOR = ColorUtilities.scaleHSB( RECORD_BASE_COLOR, 1.0, 1.0, 1.5 );
	private static final Color RECORD_SHADOW_COLOR = ColorUtilities.scaleHSB( RECORD_BASE_COLOR, 1.0, 1.0, 0.5 );
	private static final Color STOP_COLOR = Color.BLACK;

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
		Graphics2D g2 = (Graphics2D)g;
		Paint prevPaint = g2.getPaint();
		g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
		if( c instanceof AbstractButton ) {
			AbstractButton button = (AbstractButton)c;
			ButtonModel buttonModel = button.getModel();
			if( buttonModel.isSelected() ) {
				g.setColor( STOP_COLOR );
				g.fillRect( x, y, SIZE, SIZE );
			} else {
				g2.setPaint( new GradientPaint( x, y, RECORD_HIGHLIGHT_COLOR, x + SIZE, y + SIZE, RECORD_SHADOW_COLOR ) );
				g.fillOval( x, y, SIZE, SIZE );
				g.setColor( Color.BLACK );
				g.drawOval( x, y, SIZE, SIZE );
			}
		}
		g2.setPaint( prevPaint );
	}
}
