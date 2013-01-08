package org.svg.image;

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

import javax.swing.Icon;

/**
 * This class has been automatically generated using svg2java
 * 
 */
public class TrashFullSym implements Icon {

	private float origAlpha = 1.0f;

	/**
	 * Paints the transcoded SVG image on the specified graphics context. You
	 * can install a custom transformation on the graphics context to scale the
	 * image.
	 * 
	 * @param g
	 *            Graphics context.
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
		shape0.moveTo( -15.21875, 0.25 );
		shape0.curveTo( -15.70907, 0.25, -16.01482, 0.47845, -16.25, 0.71875 );
		shape0.curveTo( -16.48517, 0.95905, -16.6875, 1.2772601, -16.6875, 1.75 );
		shape0.lineTo( -16.6875, 2.21875 );
		shape0.lineTo( -18.6875, 2.21875 );
		shape0.lineTo( -18.6875, 4.21875 );
		shape0.lineTo( -3.6875, 4.21875 );
		shape0.lineTo( -3.6875, 2.21875 );
		shape0.lineTo( -5.6875, 2.21875 );
		shape0.lineTo( -5.6875, 1.875 );
		shape0.lineTo( -5.6875, 1.84375 );
		shape0.curveTo( -5.7114, 1.41315, -5.85774, 1.09146, -6.09375, 0.8125 );
		shape0.curveTo( -6.31748, 0.54806, -6.72044, 0.28319, -7.25, 0.28125 );
		shape0.lineTo( -7.25, 0.25 );
		shape0.lineTo( -7.3125, 0.25 );
		shape0.lineTo( -15.21875, 0.25 );
		shape0.closePath();
		shape0.moveTo( -14.6875, 1.25 );
		shape0.lineTo( -7.6875, 1.25 );
		shape0.lineTo( -7.6875, 2.21875 );
		shape0.lineTo( -14.6875, 2.21875 );
		shape0.lineTo( -14.6875, 1.25 );
		shape0.closePath();
		g.setPaint( new Color( 190, 190, 190, 255 ) );
		g.fill( shape0 );
	}

	private void paintShapeNode_0_0_3_1( Graphics2D g ) {
		GeneralPath shape1 = new GeneralPath();
		shape1.moveTo( 474.30283, 180.2261 );
		shape1.lineTo( 476.30283, 180.2261 );
		shape1.curveTo( 477.97647, 183.15318, 477.4405, 186.08028, 476.30283, 189.00735 );
		shape1.curveTo( 475.86783, 190.12659, 475.32043, 191.2261, 474.11533, 191.2261 );
		shape1.lineTo( 465.49033, 191.2261 );
		shape1.curveTo( 464.28522, 191.2261, 463.71375, 190.13565, 463.30283, 189.00735 );
		shape1.curveTo( 462.26053, 186.14542, 461.7039, 183.25313, 463.30283, 180.2261 );
		shape1.lineTo( 465.30283, 180.2261 );
		shape1.curveTo( 463.58154, 183.17395, 464.31796, 186.19626, 465.30283, 189.2261 );
		shape1.lineTo( 474.30283, 189.2261 );
		shape1.curveTo( 475.39874, 186.1929, 475.93423, 183.17667, 474.30283, 180.2261 );
		shape1.closePath();
		g.fill( shape1 );
	}

	private void paintShapeNode_0_0_3_2( Graphics2D g ) {
		GeneralPath shape2 = new GeneralPath();
		shape2.moveTo( 469.33408, 180.20961 );
		shape2.lineTo( 469.33408, 188.22607 );
		shape2.lineTo( 470.30283, 188.22607 );
		shape2.lineTo( 470.30283, 180.20961 );
		shape2.closePath();
		g.fill( shape2 );
	}

	private void paintShapeNode_0_0_3_3( Graphics2D g ) {
		GeneralPath shape3 = new GeneralPath();
		shape3.moveTo( 471.30283, 180.20961 );
		shape3.curveTo( 473.06503, 182.43282, 472.13315, 185.84085, 471.30283, 188.22607 );
		shape3.lineTo( 472.30283, 188.22607 );
		shape3.curveTo( 473.12778, 185.91774, 474.14224, 182.59868, 472.30283, 180.20961 );
		shape3.closePath();
		g.fill( shape3 );
	}

	private void paintShapeNode_0_0_3_4( Graphics2D g ) {
		GeneralPath shape4 = new GeneralPath();
		shape4.moveTo( 467.33408, 180.20961 );
		shape4.curveTo( 465.5168, 182.81828, 466.4778, 186.02248, 467.33408, 188.22607 );
		shape4.lineTo( 468.30283, 188.22607 );
		shape4.curveTo( 467.43762, 186.00438, 466.64014, 182.75748, 468.30283, 180.20961 );
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
	int width;

	/**
	 * The current height of this resizable icon.
	 */
	int height;

	/**
	 * Creates a new transcoded SVG image.
	 */
	public TrashFullSym() {
		this.width = getOrigWidth();
		this.height = getOrigHeight();
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
