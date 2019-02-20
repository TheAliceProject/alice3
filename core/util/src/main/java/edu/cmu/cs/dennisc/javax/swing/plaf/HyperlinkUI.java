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

import edu.cmu.cs.dennisc.java.awt.ColorUtilities;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

/**
 * @author Dennis Cosgrove
 */
public class HyperlinkUI extends BasicButtonUI {
	private Color disabledColor = Color.LIGHT_GRAY;
	private boolean isUnderlinedWhenDisabled = true;
	private boolean isUnderlinedOnlyWhenRolledOver = true;

	public static ComponentUI createUI( JComponent component ) {
		return new HyperlinkUI();
	}

	public Color getDisabledColor() {
		return this.disabledColor;
	}

	public void setDisabledColor( Color disabledColor ) {
		this.disabledColor = disabledColor;
	}

	public boolean isUnderlinedWhenDisabled() {
		return this.isUnderlinedWhenDisabled;
	}

	public void setUnderlinedWhenDisabled( boolean isUnderlinedWhenDisabled ) {
		this.isUnderlinedWhenDisabled = isUnderlinedWhenDisabled;
	}

	public boolean isUnderlinedOnlyWhenRolledOver() {
		return this.isUnderlinedOnlyWhenRolledOver;
	}

	public void setUnderlinedOnlyWhenRolledOver( boolean isUnderlinedOnlyWhenRolledOver ) {
		this.isUnderlinedOnlyWhenRolledOver = isUnderlinedOnlyWhenRolledOver;
	}

	@Override
	protected void paintText( Graphics g, AbstractButton b, Rectangle textRect, String text ) {
		ButtonModel model = b.getModel();

		Color backgroundColor = b.getBackground();
		Color foregroundColor = b.getForeground();

		Color color;
		if( b.isEnabled() ) {
			//			if( model.isArmed() ) {
			//				color = ARMED_COLOR;
			//			} else {
			if( model.isRollover() ) {
				float foregroundBrightness = ColorUtilities.getBrightness( foregroundColor );
				float backgroundBrightness = ColorUtilities.getBrightness( backgroundColor );
				boolean isForegroundBrighter = foregroundBrightness > backgroundBrightness;
				if( model.isPressed() ) {
					color = isForegroundBrighter ? foregroundColor.darker().darker() : foregroundColor.brighter().brighter();
				} else {
					color = isForegroundBrighter ? foregroundColor.brighter().brighter() : foregroundColor.darker().darker();
				}
			} else {
				color = foregroundColor;
			}
			//			}
		} else {
			color = this.disabledColor;
		}
		g.setColor( color );
		FontMetrics fm = g.getFontMetrics();
		int x = textRect.x + this.getTextShiftOffset();
		int y = textRect.y + fm.getAscent() + this.getTextShiftOffset();

		Graphics2D g2 = (Graphics2D)g;
		Object prevTextAntialiasing = g2.getRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING );
		g2.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
		try {
			g.drawString( text, x, y );
		} finally {
			g2.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING, prevTextAntialiasing );
		}
		if( ( b.isEnabled() || this.isUnderlinedWhenDisabled ) && ( ( this.isUnderlinedOnlyWhenRolledOver == false ) || model.isRollover() ) ) {
			g.fillRect( x, y, textRect.width, 1 );
		}
	}
}
