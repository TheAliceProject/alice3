package org.alice.ide.members.components.icons;

import java.awt.AlphaComposite;
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
public class UserTrashSymbolic implements Icon {

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

	private void paintCompositeGraphicsNode_0_0_3( Graphics2D g ) {
	}

	private void paintCompositeGraphicsNode_0_0_4_0( Graphics2D g ) {
	}

	private void paintShapeNode_0_0_4_1_0( Graphics2D g ) {
		GeneralPath shape0 = new GeneralPath();
		shape0.moveTo( 4.0, 2.0 );
		shape0.lineTo( 4.0, 1.03125 );
		shape0.lineTo( 11.0, 1.03125 );
		shape0.lineTo( 11.0, 2.0 );
		shape0.lineTo( 4.0, 2.0 );
		shape0.closePath();
		shape0.moveTo( 3.46875, 0.03125 );
		shape0.curveTo( 2.978423, 0.03125, 2.672682, 0.2597, 2.4375, 0.5 );
		shape0.curveTo( 2.202318, 0.7403, 2.0, 1.05851, 2.0, 1.53125 );
		shape0.lineTo( 2.0, 2.0 );
		shape0.lineTo( 0.0, 2.0 );
		shape0.lineTo( 0.0, 4.0 );
		shape0.lineTo( 15.0, 4.0 );
		shape0.lineTo( 15.0, 2.0 );
		shape0.lineTo( 13.0, 2.0 );
		shape0.lineTo( 13.0, 1.65625 );
		shape0.lineTo( 13.0, 1.62495 );
		shape0.curveTo( 12.97607, 1.19435, 12.829764, 0.87266, 12.59375, 0.5937 );
		shape0.curveTo( 12.370016, 0.32926, 11.967063, 0.06439, 11.4375, 0.06245 );
		shape0.lineTo( 11.4375, 0.031149998 );
		shape0.lineTo( 11.375, 0.031149998 );
		shape0.lineTo( 3.46875, 0.031149998 );
		shape0.lineTo( 3.46875, 0.031249998 );
		shape0.closePath();
		shape0.moveTo( 12.0, 5.0 );
		shape0.lineTo( 14.0, 5.0 );
		shape0.lineTo( 14.0, 13.78125 );
		shape0.curveTo( 14.0, 14.98205, 13.01762, 16.0, 11.8125, 16.0 );
		shape0.lineTo( 3.1875, 16.0 );
		shape0.curveTo( 1.98238, 16.0, 1.0, 14.98205, 1.0, 13.78125 );
		shape0.lineTo( 1.0, 5.0 );
		shape0.lineTo( 3.0, 5.0 );
		shape0.lineTo( 3.0, 14.0 );
		shape0.lineTo( 12.0, 14.0 );
		shape0.lineTo( 12.0, 5.0 );
		shape0.closePath();
		shape0.moveTo( 7.03125, 6.0 );
		shape0.lineTo( 7.03125, 13.0 );
		shape0.lineTo( 8.0, 13.0 );
		shape0.lineTo( 8.0, 6.0 );
		shape0.lineTo( 7.03125, 6.0 );
		shape0.closePath();
		shape0.moveTo( 8.96875, 6.0 );
		shape0.lineTo( 8.93755, 13.0 );
		shape0.lineTo( 9.9688, 13.0 );
		shape0.lineTo( 10.0, 6.0 );
		shape0.lineTo( 8.96875, 6.0 );
		shape0.closePath();
		shape0.moveTo( 5.03125, 6.0 );
		shape0.lineTo( 5.03125, 13.0 );
		shape0.lineTo( 6.0, 13.0 );
		shape0.lineTo( 6.0, 6.0 );
		shape0.lineTo( 5.03125, 6.0 );
		shape0.closePath();

		java.awt.Paint prevPaint = g.getPaint();
		Object prevAntialias = g.getRenderingHint( RenderingHints.KEY_ANTIALIASING );
		g.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
		if( this.fillPaint != null ) {
			g.setPaint( this.fillPaint );
			g.fill( shape0 );
		}
		if( this.drawPaint != null ) {
			java.awt.Stroke prevStroke = g.getStroke();
			g.setStroke( new java.awt.BasicStroke( 0.0f ) );
			g.setPaint( this.drawPaint );
			g.draw( shape0 );
			g.setStroke( prevStroke );
		}
		g.setRenderingHint( RenderingHints.KEY_ANTIALIASING, prevAntialias );
		g.setPaint( prevPaint );
	}

	private void paintCompositeGraphicsNode_0_0_4_1( Graphics2D g ) {
		// _0_0_4_1_0
		AffineTransform trans_0_0_4_1_0 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_4_1_0( g );
		g.setTransform( trans_0_0_4_1_0 );
	}

	private void paintCompositeGraphicsNode_0_0_4( Graphics2D g ) {
		// _0_0_4_0
		AffineTransform trans_0_0_4_0 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 254.0001983642578f, -820.0f ) );
		paintCompositeGraphicsNode_0_0_4_0( g );
		g.setTransform( trans_0_0_4_0 );
		// _0_0_4_1
		AffineTransform trans_0_0_4_1 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 482.0f, 176.0f ) );
		paintCompositeGraphicsNode_0_0_4_1( g );
		g.setTransform( trans_0_0_4_1 );
	}

	private void paintCompositeGraphicsNode_0_0_5( Graphics2D g ) {
	}

	private void paintCompositeGraphicsNode_0_0_6( Graphics2D g ) {
	}

	private void paintCanvasGraphicsNode_0_0( Graphics2D g ) {
		// _0_0_0
		AffineTransform trans_0_0_0 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, -482.0f, -176.0f ) );
		paintCompositeGraphicsNode_0_0_0( g );
		g.setTransform( trans_0_0_0 );
		// _0_0_1
		AffineTransform trans_0_0_1 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, -482.0f, -176.0f ) );
		paintCompositeGraphicsNode_0_0_1( g );
		g.setTransform( trans_0_0_1 );
		// _0_0_2
		AffineTransform trans_0_0_2 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, -482.0f, -176.0f ) );
		paintCompositeGraphicsNode_0_0_2( g );
		g.setTransform( trans_0_0_2 );
		// _0_0_3
		AffineTransform trans_0_0_3 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, -482.0f, -176.0f ) );
		paintCompositeGraphicsNode_0_0_3( g );
		g.setTransform( trans_0_0_3 );
		// _0_0_4
		AffineTransform trans_0_0_4 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, -482.0f, -176.0f ) );
		paintCompositeGraphicsNode_0_0_4( g );
		g.setTransform( trans_0_0_4 );
		// _0_0_5
		AffineTransform trans_0_0_5 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, -482.0f, -176.0f ) );
		paintCompositeGraphicsNode_0_0_5( g );
		g.setTransform( trans_0_0_5 );
		// _0_0_6
		AffineTransform trans_0_0_6 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, -482.0f, -176.0f ) );
		paintCompositeGraphicsNode_0_0_6( g );
		g.setTransform( trans_0_0_6 );
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
		return 1;
	}

	/**
	 * Returns the width of the bounding box of the original SVG image.
	 * 
	 * @return The width of the bounding box of the original SVG image.
	 */
	public int getOrigWidth() {
		return 15;
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

	private java.awt.Paint fillPaint = java.awt.Color.WHITE;
	private java.awt.Paint drawPaint = java.awt.Color.BLACK;

	/**
	 * Creates a new transcoded SVG image.
	 */
	public UserTrashSymbolic( int width, int height ) {
		this.width = width;
		this.height = height;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.Icon#getIconHeight()
	 */
	public int getIconHeight() {
		return height;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.Icon#getIconWidth()
	 */
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
