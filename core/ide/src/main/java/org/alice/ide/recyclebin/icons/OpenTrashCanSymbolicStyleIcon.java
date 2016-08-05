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
package org.alice.ide.recyclebin.icons;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;

/**
 * This class has been automatically generated using svg2java
 * 
 */
public class OpenTrashCanSymbolicStyleIcon implements javax.swing.Icon {

	private float origAlpha = 1.0f;

	/**
	 * Paints the transcoded SVG image on the specified graphics context. You
	 * can install a custom transformation on the graphics context to scale the
	 * image.
	 * 
	 * @param g Graphics context.
	 */
	public void paint( Graphics2D g ) {
		g.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
		origAlpha = 1.0f;
		Composite origComposite = g.getComposite();
		if( origComposite instanceof AlphaComposite ) {
			AlphaComposite origAlphaComposite =
					(AlphaComposite)origComposite;
			if( origAlphaComposite.getRule() == AlphaComposite.SRC_OVER ) {
				origAlpha = origAlphaComposite.getAlpha();
			}
		}

		// _0
		AffineTransform trans_0 = g.getTransform();
		paintRootGraphicsNode_0( g );
		g.setTransform( trans_0 );

	}

	private void paintCompositeGraphicsNode_0_0_0( Graphics2D g ) {
	}

	private void paintCompositeGraphicsNode_0_0_1( Graphics2D g ) {
	}

	private void paintCompositeGraphicsNode_0_0_2( Graphics2D g ) {
	}

	private void paintShapeNode_0_0_3_0( Graphics2D g ) {
		GeneralPath shape0 = new GeneralPath();
		shape0.moveTo( 483.95825f, 174.94391f );
		shape0.curveTo( 483.4716f, 175.00381f, 483.19604f, 175.26788f, 482.99197f, 175.5351f );
		shape0.curveTo( 482.7879f, 175.80232f, 482.62595f, 176.14285f, 482.6837f, 176.61206f );
		shape0.lineTo( 482.74088f, 177.0773f );
		shape0.lineTo( 480.75586f, 177.32158f );
		shape0.lineTo( 481.00015f, 179.30661f );
		shape0.lineTo( 495.88785f, 177.47455f );
		shape0.lineTo( 495.64355f, 175.48953f );
		shape0.lineTo( 493.65854f, 175.73381f );
		shape0.lineTo( 493.61655f, 175.39261f );
		shape0.lineTo( 493.61255f, 175.3616f );
		shape0.curveTo( 493.53625f, 174.93715f, 493.3517f, 174.63574f, 493.0834f, 174.3877f );
		shape0.curveTo( 492.82904f, 174.15256f, 492.39676f, 173.93889f, 491.8709f, 174.00163f );
		shape0.lineTo( 491.8669f, 173.97063f );
		shape0.lineTo( 491.8048f, 173.97862f );
		shape0.lineTo( 483.95776f, 174.94427f );
		shape0.closePath();
		shape0.moveTo( 484.60767f, 175.87154f );
		shape0.lineTo( 491.55527f, 175.01657f );
		shape0.lineTo( 491.67358f, 175.97807f );
		shape0.lineTo( 484.72598f, 176.83304f );
		shape0.lineTo( 484.60767f, 175.87154f );
		shape0.closePath();
		g.setPaint( this.fillColor );
		g.fill( shape0 );
	}

	private void paintShapeNode_0_0_3_1( Graphics2D g ) {
		GeneralPath shape1 = new GeneralPath();
		shape1.moveTo( 493.0002f, 180.0f );
		shape1.lineTo( 495.0002f, 180.0f );
		shape1.lineTo( 495.0002f, 188.78125f );
		shape1.curveTo( 495.0002f, 189.98206f, 494.01782f, 191.0f, 492.8127f, 191.0f );
		shape1.lineTo( 484.1877f, 191.0f );
		shape1.curveTo( 482.9826f, 191.0f, 482.0002f, 189.98206f, 482.0002f, 188.78125f );
		shape1.lineTo( 482.0002f, 180.0f );
		shape1.lineTo( 484.0002f, 180.0f );
		shape1.lineTo( 484.0002f, 189.0f );
		shape1.lineTo( 493.0002f, 189.0f );
		shape1.closePath();
		g.fill( shape1 );
	}

	private void paintShapeNode_0_0_3_2( Graphics2D g ) {
		GeneralPath shape2 = new GeneralPath();
		shape2.moveTo( 488.03146f, 181.0f );
		shape2.lineTo( 488.03146f, 188.0f );
		shape2.lineTo( 489.0002f, 188.0f );
		shape2.lineTo( 489.0002f, 181.0f );
		shape2.closePath();
		g.fill( shape2 );
	}

	private void paintShapeNode_0_0_3_3( Graphics2D g ) {
		GeneralPath shape3 = new GeneralPath();
		shape3.moveTo( 490.0002f, 181.0f );
		shape3.lineTo( 490.0002f, 188.0f );
		shape3.lineTo( 491.0002f, 188.0f );
		shape3.lineTo( 491.0002f, 181.0f );
		shape3.closePath();
		g.fill( shape3 );
	}

	private void paintShapeNode_0_0_3_4( Graphics2D g ) {
		GeneralPath shape4 = new GeneralPath();
		shape4.moveTo( 486.03146f, 181.0f );
		shape4.lineTo( 486.03146f, 188.0f );
		shape4.lineTo( 487.0002f, 188.0f );
		shape4.lineTo( 487.0002f, 181.0f );
		shape4.closePath();
		g.fill( shape4 );
	}

	private void paintCompositeGraphicsNode_0_0_3( Graphics2D g ) {
		// _0_0_3_0
		AffineTransform trans_0_0_3_0 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_3_0( g );
		g.setTransform( trans_0_0_3_0 );
		// _0_0_3_1
		AffineTransform trans_0_0_3_1 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_3_1( g );
		g.setTransform( trans_0_0_3_1 );
		// _0_0_3_2
		AffineTransform trans_0_0_3_2 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_3_2( g );
		g.setTransform( trans_0_0_3_2 );
		// _0_0_3_3
		AffineTransform trans_0_0_3_3 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_3_3( g );
		g.setTransform( trans_0_0_3_3 );
		// _0_0_3_4
		AffineTransform trans_0_0_3_4 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_3_4( g );
		g.setTransform( trans_0_0_3_4 );
	}

	private void paintCompositeGraphicsNode_0_0_4( Graphics2D g ) {
	}

	private void paintCompositeGraphicsNode_0_0_5( Graphics2D g ) {
	}

	private void paintCompositeGraphicsNode_0_0_6( Graphics2D g ) {
	}

	private void paintCompositeGraphicsNode_0_0_7( Graphics2D g ) {
	}

	private void paintCompositeGraphicsNode_0_0_8( Graphics2D g ) {
	}

	private void paintCanvasGraphicsNode_0_0( Graphics2D g ) {
		// _0_0_0
		AffineTransform trans_0_0_0 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, -480.755859375f, -173.97064208984375f ) );
		paintCompositeGraphicsNode_0_0_0( g );
		g.setTransform( trans_0_0_0 );
		// _0_0_1
		AffineTransform trans_0_0_1 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, -480.755859375f, -173.97064208984375f ) );
		paintCompositeGraphicsNode_0_0_1( g );
		g.setTransform( trans_0_0_1 );
		// _0_0_2
		AffineTransform trans_0_0_2 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, -480.755859375f, -173.97064208984375f ) );
		paintCompositeGraphicsNode_0_0_2( g );
		g.setTransform( trans_0_0_2 );
		// _0_0_3
		AffineTransform trans_0_0_3 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, -480.755859375f, -173.97064208984375f ) );
		paintCompositeGraphicsNode_0_0_3( g );
		g.setTransform( trans_0_0_3 );
		// _0_0_4
		AffineTransform trans_0_0_4 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, -480.755859375f, -173.97064208984375f ) );
		paintCompositeGraphicsNode_0_0_4( g );
		g.setTransform( trans_0_0_4 );
		// _0_0_5
		AffineTransform trans_0_0_5 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, -480.755859375f, -173.97064208984375f ) );
		paintCompositeGraphicsNode_0_0_5( g );
		g.setTransform( trans_0_0_5 );
		// _0_0_6
		AffineTransform trans_0_0_6 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, -480.755859375f, -173.97064208984375f ) );
		paintCompositeGraphicsNode_0_0_6( g );
		g.setTransform( trans_0_0_6 );
		// _0_0_7
		AffineTransform trans_0_0_7 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, -480.755859375f, -173.97064208984375f ) );
		paintCompositeGraphicsNode_0_0_7( g );
		g.setTransform( trans_0_0_7 );
		// _0_0_8
		AffineTransform trans_0_0_8 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, -480.755859375f, -173.97064208984375f ) );
		paintCompositeGraphicsNode_0_0_8( g );
		g.setTransform( trans_0_0_8 );
	}

	private void paintRootGraphicsNode_0( Graphics2D g ) {
		// _0_0
		g.setComposite( AlphaComposite.getInstance( 3, 1.0f * origAlpha ) );
		AffineTransform trans_0_0 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintCanvasGraphicsNode_0_0( g );
		g.setTransform( trans_0_0 );
	}

	/**
	 * Returns the X of the bounding box of the original SVG image.
	 * 
	 * @return The X of the bounding box of the original SVG image.
	 */
	public int getOrigX() {
		return 0;
	}

	/**
	 * Returns the Y of the bounding box of the original SVG image.
	 * 
	 * @return The Y of the bounding box of the original SVG image.
	 */
	public int getOrigY() {
		return 0;
	}

	/**
	 * Returns the width of the bounding box of the original SVG image.
	 * 
	 * @return The width of the bounding box of the original SVG image.
	 */
	public int getOrigWidth() {
		return 15; //16
	}

	/**
	 * Returns the height of the bounding box of the original SVG image.
	 * 
	 * @return The height of the bounding box of the original SVG image.
	 */
	public int getOrigHeight() {
		return 16; //18
	}

	/**
	 * The current width of this resizable icon.
	 */
	private int width;

	/**
	 * The current height of this resizable icon.
	 */
	private int height;

	private Color fillColor = new Color( 190, 190, 190, 255 );

	/**
	 * Creates a new transcoded SVG image.
	 */
	public OpenTrashCanSymbolicStyleIcon( int width, int height, Color fillColor ) {
		this.width = width;
		this.height = height;
		this.fillColor = fillColor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.Icon#getIconHeight()
	 */
	@Override
	public int getIconHeight() {
		return height;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.Icon#getIconWidth()
	 */
	@Override
	public int getIconWidth() {
		return width;
	}

	/*
	 * Set the dimension of the icon.
	 */
	public void setDimension( Dimension newDimension ) {
		this.width = newDimension.width;
		this.height = newDimension.height;
	}

	public Color getFillColor() {
		return this.fillColor;
	}

	public void setFillColor( Color fillColor ) {
		this.fillColor = fillColor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.Icon#paintIcon(java.awt.Component, java.awt.Graphics, int, int)
	 */
	@Override
	public void paintIcon( Component c, Graphics g, int x, int y ) {
		final double OFFSET_X = 2;
		final double OFFSET_Y = 8.5;
		Graphics2D g2d = (Graphics2D)g.create();
		g2d.translate( x - OFFSET_X, y - OFFSET_Y );

		double coef1 = (double)this.width / (double)getOrigWidth();
		double coef2 = (double)this.height / (double)getOrigHeight();
		double coef = Math.min( coef1, coef2 );
		g2d.scale( coef, coef );
		paint( g2d );
		g2d.dispose();
	}
}
