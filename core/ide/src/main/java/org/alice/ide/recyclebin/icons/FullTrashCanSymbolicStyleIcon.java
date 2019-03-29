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

import javax.swing.Icon;
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
public class FullTrashCanSymbolicStyleIcon implements Icon {

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
		shape0.moveTo( -15.21875f, 0.25f );
		shape0.curveTo( -15.70907f, 0.25f, -16.01482f, 0.47845f, -16.25f, 0.71875f );
		shape0.curveTo( -16.48517f, 0.95905f, -16.6875f, 1.2772601f, -16.6875f, 1.75f );
		shape0.lineTo( -16.6875f, 2.21875f );
		shape0.lineTo( -18.6875f, 2.21875f );
		shape0.lineTo( -18.6875f, 4.21875f );
		shape0.lineTo( -3.6875f, 4.21875f );
		shape0.lineTo( -3.6875f, 2.21875f );
		shape0.lineTo( -5.6875f, 2.21875f );
		shape0.lineTo( -5.6875f, 1.875f );
		shape0.lineTo( -5.6875f, 1.84375f );
		shape0.curveTo( -5.7114f, 1.41315f, -5.85774f, 1.09146f, -6.09375f, 0.8125f );
		shape0.curveTo( -6.31748f, 0.54806f, -6.72044f, 0.28319f, -7.25f, 0.28125f );
		shape0.lineTo( -7.25f, 0.25f );
		shape0.lineTo( -7.3125f, 0.25f );
		shape0.lineTo( -15.21875f, 0.25f );
		shape0.closePath();
		shape0.moveTo( -14.6875f, 1.25f );
		shape0.lineTo( -7.6875f, 1.25f );
		shape0.lineTo( -7.6875f, 2.21875f );
		shape0.lineTo( -14.6875f, 2.21875f );
		shape0.lineTo( -14.6875f, 1.25f );
		shape0.closePath();
		g.setPaint( this.fillColor );
		g.fill( shape0 );
	}

	private void paintShapeNode_0_0_3_1( Graphics2D g ) {
		GeneralPath shape1 = new GeneralPath();
		shape1.moveTo( 474.30283f, 180.2261f );
		shape1.lineTo( 476.30283f, 180.2261f );
		shape1.curveTo( 477.97647f, 183.15318f, 477.4405f, 186.08028f, 476.30283f, 189.00735f );
		shape1.curveTo( 475.86783f, 190.12659f, 475.32043f, 191.2261f, 474.11533f, 191.2261f );
		shape1.lineTo( 465.49033f, 191.2261f );
		shape1.curveTo( 464.28522f, 191.2261f, 463.71375f, 190.13565f, 463.30283f, 189.00735f );
		shape1.curveTo( 462.26053f, 186.14542f, 461.7039f, 183.25313f, 463.30283f, 180.2261f );
		shape1.lineTo( 465.30283f, 180.2261f );
		shape1.curveTo( 463.58154f, 183.17395f, 464.31796f, 186.19626f, 465.30283f, 189.2261f );
		shape1.lineTo( 474.30283f, 189.2261f );
		shape1.curveTo( 475.39874f, 186.1929f, 475.93423f, 183.17667f, 474.30283f, 180.2261f );
		shape1.closePath();
		g.fill( shape1 );
	}

	private void paintShapeNode_0_0_3_2( Graphics2D g ) {
		GeneralPath shape2 = new GeneralPath();
		shape2.moveTo( 469.33408f, 180.20961f );
		shape2.lineTo( 469.33408f, 188.22607f );
		shape2.lineTo( 470.30283f, 188.22607f );
		shape2.lineTo( 470.30283f, 180.20961f );
		shape2.closePath();
		g.fill( shape2 );
	}

	private void paintShapeNode_0_0_3_3( Graphics2D g ) {
		GeneralPath shape3 = new GeneralPath();
		shape3.moveTo( 471.30283f, 180.20961f );
		shape3.curveTo( 473.06503f, 182.43282f, 472.13315f, 185.84085f, 471.30283f, 188.22607f );
		shape3.lineTo( 472.30283f, 188.22607f );
		shape3.curveTo( 473.12778f, 185.91774f, 474.14224f, 182.59868f, 472.30283f, 180.20961f );
		shape3.closePath();
		g.fill( shape3 );
	}

	private void paintShapeNode_0_0_3_4( Graphics2D g ) {
		GeneralPath shape4 = new GeneralPath();
		shape4.moveTo( 467.33408f, 180.20961f );
		shape4.curveTo( 465.5168f, 182.81828f, 466.4778f, 186.02248f, 467.33408f, 188.22607f );
		shape4.lineTo( 468.30283f, 188.22607f );
		shape4.curveTo( 467.43762f, 186.00438f, 466.64014f, 182.75748f, 468.30283f, 180.20961f );
		shape4.closePath();
		g.fill( shape4 );
	}

	private void paintCompositeGraphicsNode_0_0_3( Graphics2D g ) {
		// _0_0_3_0
		AffineTransform trans_0_0_3_0 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 481.0002136230469f, 175.0f ) );
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
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, -462.3016052246094f, -175.25f ) );
		paintCompositeGraphicsNode_0_0_0( g );
		g.setTransform( trans_0_0_0 );
		// _0_0_1
		AffineTransform trans_0_0_1 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, -462.3016052246094f, -175.25f ) );
		paintCompositeGraphicsNode_0_0_1( g );
		g.setTransform( trans_0_0_1 );
		// _0_0_2
		AffineTransform trans_0_0_2 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, -462.3016052246094f, -175.25f ) );
		paintCompositeGraphicsNode_0_0_2( g );
		g.setTransform( trans_0_0_2 );
		// _0_0_3
		AffineTransform trans_0_0_3 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, -462.3016052246094f, -175.25f ) );
		paintCompositeGraphicsNode_0_0_3( g );
		g.setTransform( trans_0_0_3 );
		// _0_0_4
		AffineTransform trans_0_0_4 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, -462.3016052246094f, -175.25f ) );
		paintCompositeGraphicsNode_0_0_4( g );
		g.setTransform( trans_0_0_4 );
		// _0_0_5
		AffineTransform trans_0_0_5 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, -462.3016052246094f, -175.25f ) );
		paintCompositeGraphicsNode_0_0_5( g );
		g.setTransform( trans_0_0_5 );
		// _0_0_6
		AffineTransform trans_0_0_6 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, -462.3016052246094f, -175.25f ) );
		paintCompositeGraphicsNode_0_0_6( g );
		g.setTransform( trans_0_0_6 );
		// _0_0_7
		AffineTransform trans_0_0_7 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, -462.3016052246094f, -175.25f ) );
		paintCompositeGraphicsNode_0_0_7( g );
		g.setTransform( trans_0_0_7 );
		// _0_0_8
		AffineTransform trans_0_0_8 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, -462.3016052246094f, -175.25f ) );
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
		return 16;
	}

	/**
	 * Returns the height of the bounding box of the original SVG image.
	 *
	 * @return The height of the bounding box of the original SVG image.
	 */
	public int getOrigHeight() {
		return 16;
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
	 * @param width dimension
	 * @param height dimension
	 * @param fillColor background
	 */
	public FullTrashCanSymbolicStyleIcon( int width, int height, Color fillColor ) {
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

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.swing.Icon#paintIcon(java.awt.Component, java.awt.Graphics, int, int)
	 */
	@Override
	public void paintIcon( Component c, Graphics g, int x, int y ) {
		Graphics2D g2d = (Graphics2D)g.create();
		g2d.translate( x, y );

		double coef1 = (double)this.width / (double)getOrigWidth();
		double coef2 = (double)this.height / (double)getOrigHeight();
		double coef = Math.min( coef1, coef2 );
		g2d.scale( coef, coef );
		paint( g2d );
		g2d.dispose();
	}
}
