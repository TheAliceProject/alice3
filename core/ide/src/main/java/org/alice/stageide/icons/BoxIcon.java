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
public class BoxIcon extends ShapeIcon {
	private static final float x0 = 0.0f;
	private static final float xA = 0.4f;
	private static final float xB = 0.6f;
	private static final float x1 = 1.0f;

	private static final float y0 = 0.0f;
	private static final float yA = 0.2f;
	private static final float yB = 0.4f;
	private static final float yC = 0.8f;
	private static final float y1 = 1.0f;

	private static final java.awt.geom.Point2D.Float a0 = new java.awt.geom.Point2D.Float( xA, y1 );
	private static final java.awt.geom.Point2D.Float b0 = new java.awt.geom.Point2D.Float( x0, yC );
	private static final java.awt.geom.Point2D.Float c0 = new java.awt.geom.Point2D.Float( x0, yA );
	private static final java.awt.geom.Point2D.Float d0 = new java.awt.geom.Point2D.Float( xA, yB );

	private static final java.awt.geom.Point2D.Float a1 = c0;
	private static final java.awt.geom.Point2D.Float b1 = d0;
	private static final java.awt.geom.Point2D.Float c1 = new java.awt.geom.Point2D.Float( x1, yA );
	private static final java.awt.geom.Point2D.Float d1 = new java.awt.geom.Point2D.Float( xB, y0 );

	private static final java.awt.geom.Point2D.Float a2 = c1;
	private static final java.awt.geom.Point2D.Float b2 = b1;
	private static final java.awt.geom.Point2D.Float c2 = a0;
	private static final java.awt.geom.Point2D.Float d2 = new java.awt.geom.Point2D.Float( x1, yC );

	private static final java.awt.Color SHADOW_COLOR = FILL_PAINT.darker();
	private final java.awt.Stroke STROKE = new java.awt.BasicStroke( 0.0f );

	private static java.awt.Shape createFace( java.awt.geom.Point2D.Float a, java.awt.geom.Point2D.Float b, java.awt.geom.Point2D.Float c, java.awt.geom.Point2D.Float d, int width, int height ) {
		java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
		path.moveTo( a.x * width, a.y * height );
		path.lineTo( b.x * width, b.y * height );
		path.lineTo( c.x * width, c.y * height );
		path.lineTo( d.x * width, d.y * height );
		path.closePath();
		return path;
	}

	public BoxIcon( java.awt.Dimension size ) {
		super( size );
	}

	@Override
	protected void paintIcon( java.awt.Component c, java.awt.Graphics2D g2, int width, int height, java.awt.Paint fillPaint, java.awt.Paint drawPaint ) {
		java.awt.Stroke prevStroke = g2.getStroke();
		try {
			g2.setStroke( STROKE );
			java.awt.Shape face0 = createFace( a0, b0, c0, d0, width, height );
			java.awt.Shape face1 = createFace( a1, b1, c1, d1, width, height );
			java.awt.Shape face2 = createFace( a2, b2, c2, d2, width, height );

			g2.setPaint( SHADOW_COLOR );
			g2.fill( face0 );
			g2.setPaint( fillPaint );
			g2.fill( face1 );
			g2.fill( face2 );
			g2.setPaint( drawPaint );
			g2.draw( face0 );
			g2.draw( face1 );
			g2.draw( face2 );
		} finally {
			g2.setStroke( prevStroke );
		}

	}
}
