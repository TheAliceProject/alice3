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

package edu.cmu.cs.dennisc.javax.swing.icons;

/**
 * @author Dennis Cosgrove
 */
public class DropDownArrowIcon extends AbstractArrowIcon {
	private final java.awt.Paint selectedFillPaint;

	public DropDownArrowIcon( int size, java.awt.Paint selectedFillPaint ) {
		super( size );
		this.selectedFillPaint = selectedFillPaint;
	}

	protected javax.swing.ButtonModel getButtonModel( java.awt.Component c ) {
		javax.swing.AbstractButton button = (javax.swing.AbstractButton)c;
		return button.getModel();
	}

	@Override
	public void paintIcon( java.awt.Component c, java.awt.Graphics g, int x, int y ) {
		javax.swing.ButtonModel buttonModel = this.getButtonModel( c );
		java.awt.geom.GeneralPath path = this.createPath( x, y, Heading.SOUTH );
		java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
		Object prevAntialiasing = g2.getRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING );
		g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );

		java.awt.Paint fillPaint;
		java.awt.Paint drawPaint;
		if( buttonModel.isEnabled() ) {
			if( buttonModel.isPressed() || buttonModel.isSelected() ) {
				fillPaint = selectedFillPaint;
				drawPaint = null;
			} else {
				if( buttonModel.isRollover() || buttonModel.isArmed() ) {
					fillPaint = java.awt.Color.DARK_GRAY;
				} else {
					fillPaint = java.awt.Color.BLACK;
				}
				drawPaint = null;
			}
		} else {
			fillPaint = java.awt.Color.LIGHT_GRAY;
			drawPaint = null;
		}
		if( fillPaint != null ) {
			g2.setPaint( fillPaint );
			g2.fill( path );
		}
		if( drawPaint != null ) {
			g2.setPaint( drawPaint );
			g2.draw( path );
		}
		g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, prevAntialiasing );
	}
}
