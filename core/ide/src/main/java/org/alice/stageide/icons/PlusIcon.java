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
package org.alice.stageide.icons;

/**
 * @author Dennis Cosgrove
 */
public class PlusIcon extends ShapeIcon {
	public PlusIcon( java.awt.Dimension size ) {
		super( size );
	}

	private static java.awt.geom.Ellipse2D.Float createEllipse( float portion, int width, int height ) {
		float diameter = Math.min( width, height ) * portion;
		float x = ( width - diameter ) / 2;
		float y = ( height - diameter ) / 2;
		return new java.awt.geom.Ellipse2D.Float( x, y, diameter, diameter );
	}

	protected java.awt.Paint getOuterRingPaint( javax.swing.ButtonModel buttonModel ) {
		boolean isArmed = buttonModel != null ? buttonModel.isArmed() : false;
		return isArmed ? java.awt.Color.WHITE : java.awt.Color.DARK_GRAY;
	}

	protected java.awt.Paint getInnerCirclePaint( javax.swing.ButtonModel buttonModel ) {
		boolean isArmed = buttonModel != null ? buttonModel.isArmed() : false;
		return isArmed ? java.awt.Color.GRAY : java.awt.Color.LIGHT_GRAY;
	}

	protected java.awt.Paint getPlusPaint( javax.swing.ButtonModel buttonModel ) {
		boolean isArmed = buttonModel != null ? buttonModel.isArmed() : false;
		return isArmed ? java.awt.Color.WHITE : java.awt.Color.BLACK;
	}

	protected void paintIcon( java.awt.Component c, java.awt.Graphics2D g2, int width, int height, java.awt.Paint fillPaint, java.awt.Paint drawPaint, javax.swing.ButtonModel buttonModel ) {
		java.awt.Paint paint;
		paint = this.getOuterRingPaint( buttonModel );
		if( paint != null ) {
			g2.setPaint( paint );
			g2.fill( createEllipse( 1.0f, width, height ) );
		}

		paint = this.getInnerCirclePaint( buttonModel );
		if( paint != null ) {
			g2.setPaint( paint );
			g2.fill( createEllipse( 0.9f, width, height ) );
		}

		float halfStrokeSize = 0.075f;
		float shortPosition = 0.5f - halfStrokeSize;
		float shortLength = halfStrokeSize * 2.0f;

		float longPosition = 0.2f;
		float longLength = 1.0f - ( longPosition * 2.0f );

		paint = this.getPlusPaint( buttonModel );
		if( paint != null ) {
			g2.setPaint( paint );
			g2.fill( new java.awt.geom.Rectangle2D.Float( longPosition * width, shortPosition * height, longLength * width, shortLength * height ) );
			g2.fill( new java.awt.geom.Rectangle2D.Float( shortPosition * width, longPosition * height, shortLength * width, longLength * height ) );
		}
	}

	@Override
	protected final void paintIcon( java.awt.Component c, java.awt.Graphics2D g2, int width, int height, java.awt.Paint fillPaint, java.awt.Paint drawPaint ) {
		javax.swing.ButtonModel buttonModel;
		if( c instanceof javax.swing.AbstractButton ) {
			javax.swing.AbstractButton button = (javax.swing.AbstractButton)c;
			buttonModel = button.getModel();
		} else {
			buttonModel = null;
		}
		this.paintIcon( c, g2, width, height, fillPaint, drawPaint, buttonModel );
	}
}
