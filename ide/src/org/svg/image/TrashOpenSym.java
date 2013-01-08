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
public class TrashOpenSym implements Icon {

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
		shape0.moveTo( 483.95825, 174.94391 );
		shape0.curveTo( 483.4716, 175.00381, 483.19604, 175.26788, 482.99197, 175.5351 );
		shape0.curveTo( 482.7879, 175.80232, 482.62595, 176.14285, 482.6837, 176.61206 );
		shape0.lineTo( 482.74088, 177.0773 );
		shape0.lineTo( 480.75586, 177.32158 );
		shape0.lineTo( 481.00015, 179.30661 );
		shape0.lineTo( 495.88785, 177.47455 );
		shape0.lineTo( 495.64355, 175.48953 );
		shape0.lineTo( 493.65854, 175.73381 );
		shape0.lineTo( 493.61655, 175.39261 );
		shape0.lineTo( 493.61255, 175.3616 );
		shape0.curveTo( 493.53625, 174.93715, 493.3517, 174.63574, 493.0834, 174.3877 );
		shape0.curveTo( 492.82904, 174.15256, 492.39676, 173.93889, 491.8709, 174.00163 );
		shape0.lineTo( 491.8669, 173.97063 );
		shape0.lineTo( 491.8048, 173.97862 );
		shape0.lineTo( 483.95776, 174.94427 );
		shape0.closePath();
		shape0.moveTo( 484.60767, 175.87154 );
		shape0.lineTo( 491.55527, 175.01657 );
		shape0.lineTo( 491.67358, 175.97807 );
		shape0.lineTo( 484.72598, 176.83304 );
		shape0.lineTo( 484.60767, 175.87154 );
		shape0.closePath();
		g.setPaint( new Color( 190, 190, 190, 255 ) );
		g.fill( shape0 );
	}

	private void paintShapeNode_0_0_3_1( Graphics2D g ) {
		GeneralPath shape1 = new GeneralPath();
		shape1.moveTo( 493.0002, 180.0 );
		shape1.lineTo( 495.0002, 180.0 );
		shape1.lineTo( 495.0002, 188.78125 );
		shape1.curveTo( 495.0002, 189.98206, 494.01782, 191.0, 492.8127, 191.0 );
		shape1.lineTo( 484.1877, 191.0 );
		shape1.curveTo( 482.9826, 191.0, 482.0002, 189.98206, 482.0002, 188.78125 );
		shape1.lineTo( 482.0002, 180.0 );
		shape1.lineTo( 484.0002, 180.0 );
		shape1.lineTo( 484.0002, 189.0 );
		shape1.lineTo( 493.0002, 189.0 );
		shape1.closePath();
		g.fill( shape1 );
	}

	private void paintShapeNode_0_0_3_2( Graphics2D g ) {
		GeneralPath shape2 = new GeneralPath();
		shape2.moveTo( 488.03146, 181.0 );
		shape2.lineTo( 488.03146, 188.0 );
		shape2.lineTo( 489.0002, 188.0 );
		shape2.lineTo( 489.0002, 181.0 );
		shape2.closePath();
		g.fill( shape2 );
	}

	private void paintShapeNode_0_0_3_3( Graphics2D g ) {
		GeneralPath shape3 = new GeneralPath();
		shape3.moveTo( 490.0002, 181.0 );
		shape3.lineTo( 490.0002, 188.0 );
		shape3.lineTo( 491.0002, 188.0 );
		shape3.lineTo( 491.0002, 181.0 );
		shape3.closePath();
		g.fill( shape3 );
	}

	private void paintShapeNode_0_0_3_4( Graphics2D g ) {
		GeneralPath shape4 = new GeneralPath();
		shape4.moveTo( 486.03146, 181.0 );
		shape4.lineTo( 486.03146, 188.0 );
		shape4.lineTo( 487.0002, 188.0 );
		shape4.lineTo( 487.0002, 181.0 );
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
		return 16;
	}

	/**
	 * Returns the height of the bounding box of the original SVG image.
	 * 
	 * @return The height of the bounding box of the original SVG image.
	 */
	public int getOrigHeight() {
		return 18;
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
	public TrashOpenSym() {
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
