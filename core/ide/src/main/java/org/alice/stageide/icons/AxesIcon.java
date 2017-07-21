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
public class AxesIcon extends ShapeIcon {
	private static void drawLine( java.awt.Graphics2D g2, java.awt.Paint paint, java.awt.Stroke stroke, float x0, float y0, float x1, float y1 ) {
		g2.setPaint( paint );
		g2.setStroke( stroke );
		drawLine( g2, x0, y0, x1, y1 );
	}

	public AxesIcon( java.awt.Dimension size ) {
		super( size );
	}

	@Override
	protected void paintIcon( java.awt.Component c, java.awt.Graphics2D g2, int width, int height, java.awt.Paint fillPaint, java.awt.Paint drawPaint ) {
		float scaledWidth = width * .9f;
		float scaledHeight = height * .9f;
		float offsetOriginY = scaledHeight * 0.2f;
		float xInset = ( width - scaledWidth );
		float portion = 0.4f;
		float originX = ( scaledWidth * portion ) + xInset;
		java.awt.BasicStroke stroke = new java.awt.BasicStroke( scaledWidth * .04f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND );
		drawLine( g2, java.awt.Color.GREEN, stroke, originX, offsetOriginY, originX, scaledHeight - offsetOriginY );
		drawLine( g2, java.awt.Color.RED, stroke, xInset, scaledHeight, originX, scaledHeight - offsetOriginY );
		drawLine( g2, java.awt.Color.BLUE, stroke, xInset, scaledHeight - offsetOriginY - ( offsetOriginY * ( portion / ( 1 - portion ) ) ), originX, scaledHeight - offsetOriginY );
		drawLine( g2, java.awt.Color.WHITE, stroke, scaledWidth, scaledHeight, originX, scaledHeight - offsetOriginY );
	}
}
