package org.svg.image;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.Icon;

/**
 * This class has been automatically generated using svg2java
 * 
 */
public class ClipboardEmpty implements Icon {

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

	private void paintCompositeGraphicsNode_0_0_0_0( Graphics2D g ) {
	}

	private void paintCompositeGraphicsNode_0_0_0( Graphics2D g ) {
		// _0_0_0_0
		AffineTransform trans_0_0_0_0 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintCompositeGraphicsNode_0_0_0_0( g );
		g.setTransform( trans_0_0_0_0 );
	}

	private void paintCompositeGraphicsNode_0_0_1_0( Graphics2D g ) {
	}

	private void paintCompositeGraphicsNode_0_0_1( Graphics2D g ) {
		// _0_0_1_0
		AffineTransform trans_0_0_1_0 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintCompositeGraphicsNode_0_0_1_0( g );
		g.setTransform( trans_0_0_1_0 );
	}

	private void paintShapeNode_0_0_2_0_0_0( Graphics2D g ) {
		RoundRectangle2D.Double shape0 = new RoundRectangle2D.Double( 6.874999523162842, 35.875, 35.125, 6.5, 6.499999523162842, 6.5 );
		g.setPaint( new Color( 0, 0, 0, 255 ) );
		g.fill( shape0 );
	}

	private void paintShapeNode_0_0_2_0_0_1( Graphics2D g ) {
		RoundRectangle2D.Double shape1 = new RoundRectangle2D.Double( 6.874999523162842, 35.875, 35.125, 6.5, 6.499999523162842, 6.5 );
		g.fill( shape1 );
	}

	private void paintShapeNode_0_0_2_0_0_2( Graphics2D g ) {
		RoundRectangle2D.Double shape2 = new RoundRectangle2D.Double( 305.5, -92.5, 29.999996185302734, 31.999998092651367, 5.0, 5.0 );
		g.setPaint( new LinearGradientPaint( new Point2D.Double( 25.5, -13.625 ), new Point2D.Double( 26.0, -39.125 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 226, 179, 105, 255 ), new Color( 199, 155, 85, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 296.0f, -52.0f ) ) );
		g.fill( shape2 );
		g.setPaint( new LinearGradientPaint( new Point2D.Double( 18.39735221862793, -37.160858154296875 ), new Point2D.Double( 10.831841468811035, 4.028111457824707 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 143, 89, 2, 255 ), new Color( 233, 185, 110, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 296.0f, -50.0f ) ) );
		g.setStroke( new BasicStroke( 1.0f, 0, 0, 4.0f, null, 0.0f ) );
		g.draw( shape2 );
	}

	private void paintShapeNode_0_0_2_0_0_3( Graphics2D g ) {
		RoundRectangle2D.Double shape3 = new RoundRectangle2D.Double( 306.5, -91.5, 28.00001335144043, 30.000003814697266, 3.0, 3.0 );
		g.setPaint( new LinearGradientPaint( new Point2D.Double( 14.787761688232422, -9.017683982849121 ), new Point2D.Double( 14.787761688232422, -69.46895599365234 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 255, 255, 255, 255 ), new Color( 255, 255, 255, 0 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 296.0f, -52.0f ) ) );
		g.setStroke( new BasicStroke( 0.99999994f, 0, 0, 4.0f, null, 0.0f ) );
		g.draw( shape3 );
	}

	private void paintCompositeGraphicsNode_0_0_2_0_0( Graphics2D g ) {
		// _0_0_2_0_0_0
		g.setComposite( AlphaComposite.getInstance( 3, 0.62650603f * origAlpha ) );
		AffineTransform trans_0_0_2_0_0_0 = g.getTransform();
		g.transform( new AffineTransform( 0.9065836071968079f, 0.0f, 0.0f, 0.3078975975513458f, 298.0328674316406f, 80.26600646972656f ) );
		paintShapeNode_0_0_2_0_0_0( g );
		g.setTransform( trans_0_0_2_0_0_0 );
		// _0_0_2_0_0_1
		g.setComposite( AlphaComposite.getInstance( 3, 0.1927711f * origAlpha ) );
		AffineTransform trans_0_0_2_0_0_1 = g.getTransform();
		g.transform( new AffineTransform( 1.156583547592163f, 0.0f, 0.0f, 0.7117437720298767f, 291.9234924316406f, 64.15302276611328f ) );
		paintShapeNode_0_0_2_0_0_1( g );
		g.setTransform( trans_0_0_2_0_0_1 );
		// _0_0_2_0_0_2
		g.setComposite( AlphaComposite.getInstance( 3, 1.0f * origAlpha ) );
		AffineTransform trans_0_0_2_0_0_2 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_2_0_0_2( g );
		g.setTransform( trans_0_0_2_0_0_2 );
		// _0_0_2_0_0_3
		g.setComposite( AlphaComposite.getInstance( 3, 0.3f * origAlpha ) );
		AffineTransform trans_0_0_2_0_0_3 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_2_0_0_3( g );
		g.setTransform( trans_0_0_2_0_0_3 );
	}

	private void paintCompositeGraphicsNode_0_0_2_0_1_0_0_0( Graphics2D g ) {
	}

	private void paintCompositeGraphicsNode_0_0_2_0_1_0_0( Graphics2D g ) {
		// _0_0_2_0_1_0_0_0
		g.setComposite( AlphaComposite.getInstance( 3, 0.4f * origAlpha ) );
		AffineTransform trans_0_0_2_0_1_0_0_0 = g.getTransform();
		g.transform( new AffineTransform( 1.0526319742202759f, 0.0f, 0.0f, 1.2857129573822021f, -1.2631579637527466f, -13.428540229797363f ) );
		paintCompositeGraphicsNode_0_0_2_0_1_0_0_0( g );
		g.setTransform( trans_0_0_2_0_1_0_0_0 );
	}

	private void paintCompositeGraphicsNode_0_0_2_0_1_0( Graphics2D g ) {
		// _0_0_2_0_1_0_0
		AffineTransform trans_0_0_2_0_1_0_0 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintCompositeGraphicsNode_0_0_2_0_1_0_0( g );
		g.setTransform( trans_0_0_2_0_1_0_0 );
	}

	private void paintCompositeGraphicsNode_0_0_2_0_1_1( Graphics2D g ) {
	}

	private void paintCompositeGraphicsNode_0_0_2_0_1_2( Graphics2D g ) {
	}

	private void paintCompositeGraphicsNode_0_0_2_0_1( Graphics2D g ) {
		// _0_0_2_0_1_0
		AffineTransform trans_0_0_2_0_1_0 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 0.5555559992790222f, -4.5499979250962497E-7f, 13.888870239257812f ) );
		paintCompositeGraphicsNode_0_0_2_0_1_0( g );
		g.setTransform( trans_0_0_2_0_1_0 );
		// _0_0_2_0_1_1
		g.setComposite( AlphaComposite.getInstance( 3, 1.0f * origAlpha ) );
		AffineTransform trans_0_0_2_0_1_1 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 50.68870162963867f, 6.214990139007568f ) );
		paintCompositeGraphicsNode_0_0_2_0_1_1( g );
		g.setTransform( trans_0_0_2_0_1_1 );
		// _0_0_2_0_1_2
		AffineTransform trans_0_0_2_0_1_2 = g.getTransform();
		g.transform( new AffineTransform( 0.18670299649238586f, 0.0f, 0.0f, 0.18670299649238586f, 29.58139991760254f, 63.83797836303711f ) );
		paintCompositeGraphicsNode_0_0_2_0_1_2( g );
		g.setTransform( trans_0_0_2_0_1_2 );
	}

	private void paintCompositeGraphicsNode_0_0_2_0_2( Graphics2D g ) {
	}

	private void paintShapeNode_0_0_2_0_3_0( Graphics2D g ) {
		GeneralPath shape4 = new GeneralPath();
		shape4.moveTo( 16.722015, 14.506832 );
		shape4.lineTo( 32.354477, 14.506832 );
		shape4.curveTo( 33.326748, 14.551023, 33.381283, 12.617748, 33.381283, 12.617748 );
		shape4.lineTo( 30.497072, 10.307271 );
		shape4.lineTo( 30.506172, 9.552748 );
		shape4.curveTo( 30.506172, 9.552748, 18.491043, 9.532198, 18.491043, 9.532198 );
		shape4.lineTo( 18.491043, 10.4223995 );
		shape4.lineTo( 15.890176, 12.665036 );
		shape4.curveTo( 15.890176, 12.665036, 15.838146, 14.462638, 16.722025, 14.506832 );
		shape4.closePath();
		g.setPaint( new Color( 0, 0, 0, 255 ) );
		g.fill( shape4 );
	}

	private void paintShapeNode_0_0_2_0_3_1( Graphics2D g ) {
		GeneralPath shape5 = new GeneralPath();
		shape5.moveTo( 312.72202, 64.50683 );
		shape5.lineTo( 328.3545, 64.50683 );
		shape5.curveTo( 329.32675, 64.55102, 329.3813, 62.617744, 329.3813, 62.617744 );
		shape5.lineTo( 326.49707, 60.307266 );
		shape5.lineTo( 326.50607, 56.552742 );
		shape5.curveTo( 326.50607, 55.73013, 325.81427, 54.619465, 324.80728, 54.619465 );
		shape5.lineTo( 316.28723, 54.531075 );
		shape5.curveTo( 315.0748, 54.531075, 314.49094, 55.719227, 314.49094, 56.532192 );
		shape5.lineTo( 314.49094, 60.422394 );
		shape5.lineTo( 311.89008, 62.66503 );
		shape5.curveTo( 311.89008, 62.66503, 311.83807, 64.46263, 312.72192, 64.50683 );
		shape5.closePath();
		g.setPaint( new LinearGradientPaint( new Point2D.Double( 24.635435104370117, 3.519411563873291 ), new Point2D.Double( 24.635435104370117, 11.540999412536621 ), new float[] { 0.0f, 0.13349205f, 0.53102833f, 0.78739f, 1.0f }, new Color[] { new Color( 186, 189, 182, 255 ), new Color( 238, 238, 236, 255 ), new Color( 186, 189, 182, 255 ), new Color( 255, 255, 255, 255 ), new Color( 156, 152, 138, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 296.0f, 52.0f ) ) );
		g.fill( shape5 );
		g.setPaint( new LinearGradientPaint( new Point2D.Double( 32.91161346435547, 16.214149475097656 ), new Point2D.Double( 31.417892456054688, 4.031081199645996 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 85, 87, 83, 255 ), new Color( 186, 189, 182, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 296.0f, 50.0f ) ) );
		g.setStroke( new BasicStroke( 1.0f, 0, 0, 4.0f, null, 0.0f ) );
		g.draw( shape5 );
	}

	private void paintShapeNode_0_0_2_0_3_2( Graphics2D g ) {
		RoundRectangle2D.Double shape6 = new RoundRectangle2D.Double( 313.0, 62.0, 15.0, 1.0416321754455566, 1.0416321754455566, 1.0416321754455566 );
		g.setPaint( new Color( 255, 255, 255, 255 ) );
		g.fill( shape6 );
	}

	private void paintShapeNode_0_0_2_0_3_3( Graphics2D g ) {
		GeneralPath shape7 = new GeneralPath();
		shape7.moveTo( 316.0, 60.0 );
		shape7.lineTo( 316.0, 57.0 );
		shape7.curveTo( 315.9606, 56.368443, 316.20798, 55.966385, 317.0, 56.0 );
		shape7.lineTo( 324.0, 56.0 );
		shape7.curveTo( 324.46307, 56.07386, 324.94202, 56.11598, 325.0, 57.0 );
		shape7.lineTo( 325.0, 60.0 );
		shape7.lineTo( 324.0, 57.0 );
		shape7.lineTo( 317.0, 57.0 );
		shape7.lineTo( 316.0, 60.0 );
		shape7.closePath();
		g.setPaint( new LinearGradientPaint( new Point2D.Double( 24.49800682067871, 3.9980428218841553 ), new Point2D.Double( 24.49800682067871, 8.0 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 255, 255, 255, 255 ), new Color( 255, 255, 255, 0 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 296.0f, 52.0f ) ) );
		g.fill( shape7 );
	}

	private void paintShapeNode_0_0_2_0_3_4( Graphics2D g ) {
		GeneralPath shape8 = new GeneralPath();
		shape8.moveTo( 316.28125, 55.40625 );
		shape8.curveTo( 315.96063, 55.40625, 315.79126, 55.524544, 315.625, 55.75 );
		shape8.curveTo( 315.45874, 55.975456, 315.375, 56.32886, 315.375, 56.53125 );
		shape8.lineTo( 315.375, 60.4375 );
		shape8.curveTo( 315.37088, 60.691208, 315.25687, 60.930645, 315.0625, 61.09375 );
		shape8.lineTo( 312.78125, 63.03125 );
		shape8.curveTo( 312.79025, 63.164944, 312.77625, 63.212463, 312.81244, 63.375 );
		shape8.curveTo( 312.84085, 63.503906, 312.88025, 63.571346, 312.90613, 63.625 );
		shape8.lineTo( 328.24988, 63.625 );
		shape8.curveTo( 328.2776, 63.58374, 328.3303, 63.499012, 328.37488, 63.34375 );
		shape8.curveTo( 328.41977, 63.18762, 328.41977, 63.136784, 328.43738, 63.0 );
		shape8.lineTo( 325.93738, 61.0 );
		shape8.curveTo( 325.73462, 60.82988, 325.61972, 60.57713, 325.62488, 60.3125 );
		shape8.lineTo( 325.62488, 56.5625 );
		shape8.curveTo( 325.62488, 56.392494, 325.51932, 56.03895, 325.34363, 55.8125 );
		shape8.curveTo( 325.16806, 55.58605, 324.99304, 55.5, 324.8125, 55.5 );
		shape8.lineTo( 316.28125, 55.40625 );
		shape8.closePath();
		g.setPaint( new Color( 255, 255, 255, 255 ) );
		g.draw( shape8 );
	}

	private void paintCompositeGraphicsNode_0_0_2_0_3( Graphics2D g ) {
		// _0_0_2_0_3_0
		g.setComposite( AlphaComposite.getInstance( 3, 0.44117647f * origAlpha ) );
		AffineTransform trans_0_0_2_0_3_0 = g.getTransform();
		g.transform( new AffineTransform( 1.0502474308013916f, 0.0f, 0.0f, 1.0502474308013916f, 294.7621154785156f, 50.33353042602539f ) );
		paintShapeNode_0_0_2_0_3_0( g );
		g.setTransform( trans_0_0_2_0_3_0 );
		// _0_0_2_0_3_1
		g.setComposite( AlphaComposite.getInstance( 3, 1.0f * origAlpha ) );
		AffineTransform trans_0_0_2_0_3_1 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_2_0_3_1( g );
		g.setTransform( trans_0_0_2_0_3_1 );
		// _0_0_2_0_3_2
		g.setComposite( AlphaComposite.getInstance( 3, 0.5f * origAlpha ) );
		AffineTransform trans_0_0_2_0_3_2 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_2_0_3_2( g );
		g.setTransform( trans_0_0_2_0_3_2 );
		// _0_0_2_0_3_3
		g.setComposite( AlphaComposite.getInstance( 3, 1.0f * origAlpha ) );
		AffineTransform trans_0_0_2_0_3_3 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_2_0_3_3( g );
		g.setTransform( trans_0_0_2_0_3_3 );
		// _0_0_2_0_3_4
		g.setComposite( AlphaComposite.getInstance( 3, 0.2f * origAlpha ) );
		AffineTransform trans_0_0_2_0_3_4 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_2_0_3_4( g );
		g.setTransform( trans_0_0_2_0_3_4 );
	}

	private void paintCompositeGraphicsNode_0_0_2_0( Graphics2D g ) {
		// _0_0_2_0_0
		AffineTransform trans_0_0_2_0_0 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, -167.00001525878906f, -3.0f ) );
		paintCompositeGraphicsNode_0_0_2_0_0( g );
		g.setTransform( trans_0_0_2_0_0 );
		// _0_0_2_0_1
		g.setComposite( AlphaComposite.getInstance( 3, 1.0f * origAlpha ) );
		AffineTransform trans_0_0_2_0_1 = g.getTransform();
		g.transform( new AffineTransform( 0.6855238676071167f, 0.0f, 0.0f, 0.6771684288978577f, 137.05018615722656f, 60.773277282714844f ) );
		paintCompositeGraphicsNode_0_0_2_0_1( g );
		g.setTransform( trans_0_0_2_0_1 );
		// _0_0_2_0_2
		AffineTransform trans_0_0_2_0_2 = g.getTransform();
		g.transform( new AffineTransform( 0.6232035160064697f, 0.0f, 0.0f, 0.6771684288978577f, 164.3101348876953f, 56.7651481628418f ) );
		paintCompositeGraphicsNode_0_0_2_0_2( g );
		g.setTransform( trans_0_0_2_0_2 );
		// _0_0_2_0_3
		AffineTransform trans_0_0_2_0_3 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, -167.00001525878906f, -3.0f ) );
		paintCompositeGraphicsNode_0_0_2_0_3( g );
		g.setTransform( trans_0_0_2_0_3 );
	}

	private void paintCompositeGraphicsNode_0_0_2( Graphics2D g ) {
		// _0_0_2_0
		AffineTransform trans_0_0_2_0 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintCompositeGraphicsNode_0_0_2_0( g );
		g.setTransform( trans_0_0_2_0 );
	}

	private void paintCompositeGraphicsNode_0_0_3_0( Graphics2D g ) {
	}

	private void paintCompositeGraphicsNode_0_0_3( Graphics2D g ) {
		// _0_0_3_0
		AffineTransform trans_0_0_3_0 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintCompositeGraphicsNode_0_0_3_0( g );
		g.setTransform( trans_0_0_3_0 );
	}

	private void paintCanvasGraphicsNode_0_0( Graphics2D g ) {
		// _0_0_0
		AffineTransform trans_0_0_0 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, -128.79701232910156f, -51.03125f ) );
		paintCompositeGraphicsNode_0_0_0( g );
		g.setTransform( trans_0_0_0 );
		// _0_0_1
		AffineTransform trans_0_0_1 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, -128.79701232910156f, -51.03125f ) );
		paintCompositeGraphicsNode_0_0_1( g );
		g.setTransform( trans_0_0_1 );
		// _0_0_2
		AffineTransform trans_0_0_2 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, -128.79701232910156f, -51.03125f ) );
		paintCompositeGraphicsNode_0_0_2( g );
		g.setTransform( trans_0_0_2 );
		// _0_0_3
		g.setComposite( AlphaComposite.getInstance( 3, 1.0f * origAlpha ) );
		AffineTransform trans_0_0_3 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, -128.79701232910156f, -51.03125f ) );
		paintCompositeGraphicsNode_0_0_3( g );
		g.setTransform( trans_0_0_3 );
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
		return 1;
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
		return 48;
	}

	/**
	 * Returns the height of the bounding box of the original SVG image.
	 * 
	 * @return The height of the bounding box of the original SVG image.
	 */
	public int getOrigHeight() {
		return 43;
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
	public ClipboardEmpty() {
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
