package org.alice.ide.clipboard.icons;

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
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;

/**
 * This class has been automatically generated using svg2java
 * 
 */
public class FullClipboardIcon implements ClipboardIcon {

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

		Color color = this.dragReceptorState.getColor();
		g.setPaint( new LinearGradientPaint( new Point2D.Double( 25.5, -13.625 ), new Point2D.Double( 26.0, -39.125 ), new float[] { 0.0f, 1.0f }, new Color[] { color, new Color( 199, 155, 85, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 296.0f, -52.0f ) ) );
		//g.setPaint( new LinearGradientPaint( new Point2D.Double( 25.5, -13.625 ), new Point2D.Double( 26.0, -39.125 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 226, 179, 105, 255 ), new Color( 199, 155, 85, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 296.0f, -52.0f ) ) );

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

	private void paintShapeNode_0_0_2_0_1( Graphics2D g ) {
		GeneralPath shape4 = new GeneralPath();
		shape4.moveTo( 142.93753, 60.5 );
		shape4.lineTo( 164.068, 60.5 );
		shape4.curveTo( 164.86101, 60.5, 165.49942, 61.138416, 165.49942, 61.931427 );
		shape4.lineTo( 165.49973, 82.5 );
		shape4.curveTo( 165.49973, 82.5, 160.49973, 87.5, 160.49973, 87.5 );
		shape4.lineTo( 142.93753, 87.5057 );
		shape4.curveTo( 142.14452, 87.5057, 141.5061, 86.86729, 141.5061, 86.07427 );
		shape4.lineTo( 141.5061, 61.931465 );
		shape4.curveTo( 141.5061, 61.138454, 142.14452, 60.50004, 142.93753, 60.50004 );
		shape4.closePath();
		g.setPaint( new Color( 0, 0, 0, 255 ) );
		g.fill( shape4 );
		g.setStroke( new BasicStroke( 1.0000001f, 0, 0, 4.0f, null, 0.0f ) );
		g.draw( shape4 );
	}

	private void paintCompositeGraphicsNode_0_0_2_0_2_0_0_0( Graphics2D g ) {
	}

	private void paintCompositeGraphicsNode_0_0_2_0_2_0_0( Graphics2D g ) {
		// _0_0_2_0_2_0_0_0
		g.setComposite( AlphaComposite.getInstance( 3, 0.4f * origAlpha ) );
		AffineTransform trans_0_0_2_0_2_0_0_0 = g.getTransform();
		g.transform( new AffineTransform( 1.0526319742202759f, 0.0f, 0.0f, 1.2857129573822021f, -1.2631579637527466f, -13.428540229797363f ) );
		paintCompositeGraphicsNode_0_0_2_0_2_0_0_0( g );
		g.setTransform( trans_0_0_2_0_2_0_0_0 );
	}

	private void paintCompositeGraphicsNode_0_0_2_0_2_0( Graphics2D g ) {
		// _0_0_2_0_2_0_0
		AffineTransform trans_0_0_2_0_2_0_0 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintCompositeGraphicsNode_0_0_2_0_2_0_0( g );
		g.setTransform( trans_0_0_2_0_2_0_0 );
	}

	private void paintCompositeGraphicsNode_0_0_2_0_2_1( Graphics2D g ) {
	}

	private void paintCompositeGraphicsNode_0_0_2_0_2_2( Graphics2D g ) {
	}

	private void paintCompositeGraphicsNode_0_0_2_0_2( Graphics2D g ) {
		// _0_0_2_0_2_0
		AffineTransform trans_0_0_2_0_2_0 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 0.5555559992790222f, -4.5499979250962497E-7f, 13.888870239257812f ) );
		paintCompositeGraphicsNode_0_0_2_0_2_0( g );
		g.setTransform( trans_0_0_2_0_2_0 );
		// _0_0_2_0_2_1
		g.setComposite( AlphaComposite.getInstance( 3, 1.0f * origAlpha ) );
		AffineTransform trans_0_0_2_0_2_1 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 50.68870162963867f, 6.214990139007568f ) );
		paintCompositeGraphicsNode_0_0_2_0_2_1( g );
		g.setTransform( trans_0_0_2_0_2_1 );
		// _0_0_2_0_2_2
		AffineTransform trans_0_0_2_0_2_2 = g.getTransform();
		g.transform( new AffineTransform( 0.18670299649238586f, 0.0f, 0.0f, 0.18670299649238586f, 29.58139991760254f, 63.83797836303711f ) );
		paintCompositeGraphicsNode_0_0_2_0_2_2( g );
		g.setTransform( trans_0_0_2_0_2_2 );
	}

	private void paintCompositeGraphicsNode_0_0_2_0_3( Graphics2D g ) {
	}

	private void paintShapeNode_0_0_2_0_4_0( Graphics2D g ) {
		GeneralPath shape5 = new GeneralPath();
		shape5.moveTo( -34.29474, 4.03866 );
		shape5.lineTo( -0.3885193, 4.03866 );
		shape5.curveTo( 0.88395476, 4.03866, 1.9083652, 4.9814334, 1.9083652, 6.1525016 );
		shape5.lineTo( 1.9088448, 36.526886 );
		shape5.curveTo( 1.9088448, 36.526886, -6.114217, 43.910576, -6.114217, 43.910576 );
		shape5.lineTo( -34.29474, 43.918976 );
		shape5.curveTo( -35.56721, 43.918976, -36.59162, 42.976204, -36.59162, 41.805134 );
		shape5.lineTo( -36.59162, 6.152546 );
		shape5.curveTo( -36.59162, 4.9814777, -35.56721, 4.0387044, -34.29474, 4.0387044 );
		shape5.closePath();
		g.setPaint( new RadialGradientPaint( new Point2D.Double( -117.93485260009766, 5.198304176330566 ), 18.000002f, new Point2D.Double( -117.93485260009766, 5.198304176330566 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 255, 255, 255, 255 ), new Color( 211, 215, 207, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 3.19461989402771f, 0.0f, 0.0f, 1.5470696687698364f, 365.3152770996094f, 23.795835494995117f ) ) );
		g.fill( shape5 );
	}

	private void paintCompositeGraphicsNode_0_0_2_0_4( Graphics2D g ) {
		// _0_0_2_0_4_0
		AffineTransform trans_0_0_2_0_4_0 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_2_0_4_0( g );
		g.setTransform( trans_0_0_2_0_4_0 );
	}

	private void paintShapeNode_0_0_2_0_5_0( Graphics2D g ) {
		GeneralPath shape6 = new GeneralPath();
		shape6.moveTo( -34.29474, 4.03866 );
		shape6.lineTo( -0.3885193, 4.03866 );
		shape6.curveTo( 0.88395476, 4.03866, 1.9083652, 4.9814334, 1.9083652, 6.1525016 );
		shape6.lineTo( 1.9088448, 36.526886 );
		shape6.curveTo( 1.9088448, 36.526886, -6.114217, 43.910576, -6.114217, 43.910576 );
		shape6.lineTo( -34.29474, 43.918976 );
		shape6.curveTo( -35.56721, 43.918976, -36.59162, 42.976204, -36.59162, 41.805134 );
		shape6.lineTo( -36.59162, 6.152546 );
		shape6.curveTo( -36.59162, 4.9814777, -35.56721, 4.0387044, -34.29474, 4.0387044 );
		shape6.closePath();
		g.setPaint( new LinearGradientPaint( new Point2D.Double( -367.9520568847656, 16.063671112060547 ), new Point2D.Double( -393.3939208984375, -46.69970703125 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 85, 87, 83, 255 ), new Color( 186, 189, 182, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 356.0f, 50.0f ) ) );
		g.setStroke( new BasicStroke( 1.5393476f, 0, 0, 4.0f, null, 0.0f ) );
		g.draw( shape6 );
	}

	private void paintCompositeGraphicsNode_0_0_2_0_5( Graphics2D g ) {
		// _0_0_2_0_5_0
		AffineTransform trans_0_0_2_0_5_0 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_2_0_5_0( g );
		g.setTransform( trans_0_0_2_0_5_0 );
	}

	private void paintShapeNode_0_0_2_0_6( Graphics2D g ) {
		GeneralPath shape7 = new GeneralPath();
		shape7.moveTo( 331.5, 84.5 );
		shape7.lineTo( 326.5, 84.5 );
		shape7.lineTo( 326.5, 89.5 );
		shape7.lineTo( 331.5, 84.5 );
		shape7.closePath();
		g.setPaint( new Color( 0, 0, 0, 255 ) );
		g.fill( shape7 );
	}

	private void paintShapeNode_0_0_2_0_7( Graphics2D g ) {
		GeneralPath shape8 = new GeneralPath();
		shape8.moveTo( 165.49973, 81.5 );
		shape8.lineTo( 160.49973, 81.5 );
		shape8.lineTo( 160.49973, 86.5 );
		shape8.lineTo( 165.49973, 81.5 );
		shape8.closePath();
		g.setPaint( new RadialGradientPaint( new Point2D.Double( 328.5484619140625, 85.5484619140625 ), 3.0f, new Point2D.Double( 328.5484619140625, 85.5484619140625 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 255, 255, 255, 255 ), new Color( 136, 138, 133, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 0.6394925117492676f, 0.0f, 0.0f, 0.6394924521446228f, -48.60456085205078f, 27.792404174804688f ) ) );
		g.fill( shape8 );
		g.setPaint( new RadialGradientPaint( new Point2D.Double( 327.53125, 84.5 ), 3.0f, new Point2D.Double( 327.53125, 84.5 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 211, 215, 207, 255 ), new Color( 85, 87, 83, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 1.9562286138534546f, 0.0f, 0.0f, 1.9562286138534546f, -480.1950378417969f, -83.80131530761719f ) ) );
		g.setStroke( new BasicStroke( 1.0f, 0, 1, 4.0f, null, 0.0f ) );
		g.draw( shape8 );
	}

	private void paintShapeNode_0_0_2_0_8( Graphics2D g ) {
		GeneralPath shape9 = new GeneralPath();
		shape9.moveTo( 143.07256, 60.5 );
		shape9.lineTo( 163.92691, 60.5 );
		shape9.curveTo( 164.24425, 60.5, 164.49973, 60.755478, 164.49973, 61.07282 );
		shape9.lineTo( 164.49973, 81.0 );
		shape9.curveTo( 164.49973, 81.0, 159.99973, 85.5, 159.99973, 85.5 );
		shape9.lineTo( 143.07254, 85.5 );
		shape9.curveTo( 142.7552, 85.5, 142.49973, 85.24452, 142.49973, 84.927185 );
		shape9.lineTo( 142.49973, 61.07282 );
		shape9.curveTo( 142.49973, 60.755478, 142.7552, 60.5, 143.07254, 60.5 );
		shape9.closePath();
		g.setPaint( new LinearGradientPaint( new Point2D.Double( 325.5882263183594, 82.02571105957031 ), new Point2D.Double( 333.8441162109375, 90.2815933227539 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 255, 255, 255, 255 ), new Color( 255, 255, 255, 0 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, -167.00027465820312f, -3.0f ) ) );
		g.setStroke( new BasicStroke( 0.99999946f, 0, 0, 4.0f, null, 0.0f ) );
		g.draw( shape9 );
	}

	private void paintShapeNode_0_0_2_0_9_0( Graphics2D g ) {
		GeneralPath shape10 = new GeneralPath();
		shape10.moveTo( 16.722015, 14.506832 );
		shape10.lineTo( 32.354477, 14.506832 );
		shape10.curveTo( 33.326748, 14.551023, 33.381283, 12.617748, 33.381283, 12.617748 );
		shape10.lineTo( 30.497072, 10.307271 );
		shape10.lineTo( 30.506172, 9.552748 );
		shape10.curveTo( 30.506172, 9.552748, 18.491043, 9.532198, 18.491043, 9.532198 );
		shape10.lineTo( 18.491043, 10.4223995 );
		shape10.lineTo( 15.890176, 12.665036 );
		shape10.curveTo( 15.890176, 12.665036, 15.838146, 14.462638, 16.722025, 14.506832 );
		shape10.closePath();
		g.setPaint( new Color( 0, 0, 0, 255 ) );
		g.fill( shape10 );
	}

	private void paintShapeNode_0_0_2_0_9_1( Graphics2D g ) {
		GeneralPath shape11 = new GeneralPath();
		shape11.moveTo( 312.72202, 64.50683 );
		shape11.lineTo( 328.3545, 64.50683 );
		shape11.curveTo( 329.32675, 64.55102, 329.3813, 62.617744, 329.3813, 62.617744 );
		shape11.lineTo( 326.49707, 60.307266 );
		shape11.lineTo( 326.50607, 56.552742 );
		shape11.curveTo( 326.50607, 55.73013, 325.81427, 54.619465, 324.80728, 54.619465 );
		shape11.lineTo( 316.28723, 54.531075 );
		shape11.curveTo( 315.0748, 54.531075, 314.49094, 55.719227, 314.49094, 56.532192 );
		shape11.lineTo( 314.49094, 60.422394 );
		shape11.lineTo( 311.89008, 62.66503 );
		shape11.curveTo( 311.89008, 62.66503, 311.83807, 64.46263, 312.72192, 64.50683 );
		shape11.closePath();
		g.setPaint( new LinearGradientPaint( new Point2D.Double( 24.635435104370117, 3.519411563873291 ), new Point2D.Double( 24.635435104370117, 11.540999412536621 ), new float[] { 0.0f, 0.13349205f, 0.53102833f, 0.78739f, 1.0f }, new Color[] { new Color( 186, 189, 182, 255 ), new Color( 238, 238, 236, 255 ), new Color( 186, 189, 182, 255 ), new Color( 255, 255, 255, 255 ), new Color( 156, 152, 138, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 296.0f, 52.0f ) ) );
		g.fill( shape11 );
		g.setPaint( new LinearGradientPaint( new Point2D.Double( 32.91161346435547, 16.214149475097656 ), new Point2D.Double( 31.417892456054688, 4.031081199645996 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 85, 87, 83, 255 ), new Color( 186, 189, 182, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 296.0f, 50.0f ) ) );
		g.setStroke( new BasicStroke( 1.0f, 0, 0, 4.0f, null, 0.0f ) );
		g.draw( shape11 );
	}

	private void paintShapeNode_0_0_2_0_9_2( Graphics2D g ) {
		RoundRectangle2D.Double shape12 = new RoundRectangle2D.Double( 313.0, 62.0, 15.0, 1.0416321754455566, 1.0416321754455566, 1.0416321754455566 );
		g.setPaint( new Color( 255, 255, 255, 255 ) );
		g.fill( shape12 );
	}

	private void paintShapeNode_0_0_2_0_9_3( Graphics2D g ) {
		GeneralPath shape13 = new GeneralPath();
		shape13.moveTo( 316.0, 60.0 );
		shape13.lineTo( 316.0, 57.0 );
		shape13.curveTo( 315.9606, 56.368443, 316.20798, 55.966385, 317.0, 56.0 );
		shape13.lineTo( 324.0, 56.0 );
		shape13.curveTo( 324.46307, 56.07386, 324.94202, 56.11598, 325.0, 57.0 );
		shape13.lineTo( 325.0, 60.0 );
		shape13.lineTo( 324.0, 57.0 );
		shape13.lineTo( 317.0, 57.0 );
		shape13.lineTo( 316.0, 60.0 );
		shape13.closePath();
		g.setPaint( new LinearGradientPaint( new Point2D.Double( 24.49800682067871, 3.9980428218841553 ), new Point2D.Double( 24.49800682067871, 8.0 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 255, 255, 255, 255 ), new Color( 255, 255, 255, 0 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 296.0f, 52.0f ) ) );
		g.fill( shape13 );
	}

	private void paintShapeNode_0_0_2_0_9_4( Graphics2D g ) {
		GeneralPath shape14 = new GeneralPath();
		shape14.moveTo( 316.28125, 55.40625 );
		shape14.curveTo( 315.96063, 55.40625, 315.79126, 55.524544, 315.625, 55.75 );
		shape14.curveTo( 315.45874, 55.975456, 315.375, 56.32886, 315.375, 56.53125 );
		shape14.lineTo( 315.375, 60.4375 );
		shape14.curveTo( 315.37088, 60.691208, 315.25687, 60.930645, 315.0625, 61.09375 );
		shape14.lineTo( 312.78125, 63.03125 );
		shape14.curveTo( 312.79025, 63.164944, 312.77625, 63.212463, 312.81244, 63.375 );
		shape14.curveTo( 312.84085, 63.503906, 312.88025, 63.571346, 312.90613, 63.625 );
		shape14.lineTo( 328.24988, 63.625 );
		shape14.curveTo( 328.2776, 63.58374, 328.3303, 63.499012, 328.37488, 63.34375 );
		shape14.curveTo( 328.41977, 63.18762, 328.41977, 63.136784, 328.43738, 63.0 );
		shape14.lineTo( 325.93738, 61.0 );
		shape14.curveTo( 325.73462, 60.82988, 325.61972, 60.57713, 325.62488, 60.3125 );
		shape14.lineTo( 325.62488, 56.5625 );
		shape14.curveTo( 325.62488, 56.392494, 325.51932, 56.03895, 325.34363, 55.8125 );
		shape14.curveTo( 325.16806, 55.58605, 324.99304, 55.5, 324.8125, 55.5 );
		shape14.lineTo( 316.28125, 55.40625 );
		shape14.closePath();
		g.setPaint( new Color( 255, 255, 255, 255 ) );
		g.draw( shape14 );
	}

	private void paintCompositeGraphicsNode_0_0_2_0_9( Graphics2D g ) {
		// _0_0_2_0_9_0
		g.setComposite( AlphaComposite.getInstance( 3, 0.44117647f * origAlpha ) );
		AffineTransform trans_0_0_2_0_9_0 = g.getTransform();
		g.transform( new AffineTransform( 1.0502474308013916f, 0.0f, 0.0f, 1.0502474308013916f, 294.7621154785156f, 50.33353042602539f ) );
		paintShapeNode_0_0_2_0_9_0( g );
		g.setTransform( trans_0_0_2_0_9_0 );
		// _0_0_2_0_9_1
		g.setComposite( AlphaComposite.getInstance( 3, 1.0f * origAlpha ) );
		AffineTransform trans_0_0_2_0_9_1 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_2_0_9_1( g );
		g.setTransform( trans_0_0_2_0_9_1 );
		// _0_0_2_0_9_2
		g.setComposite( AlphaComposite.getInstance( 3, 0.5f * origAlpha ) );
		AffineTransform trans_0_0_2_0_9_2 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_2_0_9_2( g );
		g.setTransform( trans_0_0_2_0_9_2 );
		// _0_0_2_0_9_3
		g.setComposite( AlphaComposite.getInstance( 3, 1.0f * origAlpha ) );
		AffineTransform trans_0_0_2_0_9_3 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_2_0_9_3( g );
		g.setTransform( trans_0_0_2_0_9_3 );
		// _0_0_2_0_9_4
		g.setComposite( AlphaComposite.getInstance( 3, 0.2f * origAlpha ) );
		AffineTransform trans_0_0_2_0_9_4 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_2_0_9_4( g );
		g.setTransform( trans_0_0_2_0_9_4 );
	}

	private void paintCompositeGraphicsNode_0_0_2_0( Graphics2D g ) {
		// _0_0_2_0_0
		AffineTransform trans_0_0_2_0_0 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, -167.00001525878906f, -3.0f ) );
		paintCompositeGraphicsNode_0_0_2_0_0( g );
		g.setTransform( trans_0_0_2_0_0 );
		// _0_0_2_0_1
		g.setComposite( AlphaComposite.getInstance( 3, 0.2f * origAlpha ) );
		AffineTransform trans_0_0_2_0_1 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_2_0_1( g );
		g.setTransform( trans_0_0_2_0_1 );
		// _0_0_2_0_2
		g.setComposite( AlphaComposite.getInstance( 3, 1.0f * origAlpha ) );
		AffineTransform trans_0_0_2_0_2 = g.getTransform();
		g.transform( new AffineTransform( 0.6855238676071167f, 0.0f, 0.0f, 0.6771684288978577f, 137.05018615722656f, 60.773277282714844f ) );
		paintCompositeGraphicsNode_0_0_2_0_2( g );
		g.setTransform( trans_0_0_2_0_2 );
		// _0_0_2_0_3
		AffineTransform trans_0_0_2_0_3 = g.getTransform();
		g.transform( new AffineTransform( 0.6232035160064697f, 0.0f, 0.0f, 0.6771684288978577f, 164.3101348876953f, 56.7651481628418f ) );
		paintCompositeGraphicsNode_0_0_2_0_3( g );
		g.setTransform( trans_0_0_2_0_3 );
		// _0_0_2_0_4
		AffineTransform trans_0_0_2_0_4 = g.getTransform();
		g.transform( new AffineTransform( 0.6232035160064697f, 0.0f, 0.0f, 0.6771684288978577f, 164.3101348876953f, 56.7651481628418f ) );
		paintCompositeGraphicsNode_0_0_2_0_4( g );
		g.setTransform( trans_0_0_2_0_4 );
		// _0_0_2_0_5
		AffineTransform trans_0_0_2_0_5 = g.getTransform();
		g.transform( new AffineTransform( 0.6232035160064697f, 0.0f, 0.0f, 0.6771684288978577f, 164.3101348876953f, 56.7651481628418f ) );
		paintCompositeGraphicsNode_0_0_2_0_5( g );
		g.setTransform( trans_0_0_2_0_5 );
		// _0_0_2_0_6
		g.setComposite( AlphaComposite.getInstance( 3, 0.2f * origAlpha ) );
		AffineTransform trans_0_0_2_0_6 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 0.903225839138031f, -167.00027465820312f, 5.119354724884033f ) );
		paintShapeNode_0_0_2_0_6( g );
		g.setTransform( trans_0_0_2_0_6 );
		// _0_0_2_0_7
		g.setComposite( AlphaComposite.getInstance( 3, 1.0f * origAlpha ) );
		AffineTransform trans_0_0_2_0_7 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_2_0_7( g );
		g.setTransform( trans_0_0_2_0_7 );
		// _0_0_2_0_8
		AffineTransform trans_0_0_2_0_8 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_2_0_8( g );
		g.setTransform( trans_0_0_2_0_8 );
		// _0_0_2_0_9
		AffineTransform trans_0_0_2_0_9 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, -167.00001525878906f, -3.0f ) );
		paintCompositeGraphicsNode_0_0_2_0_9( g );
		g.setTransform( trans_0_0_2_0_9 );
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
	private int width;

	/**
	 * The current height of this resizable icon.
	 */
	private int height;

	private org.alice.ide.clipboard.DragReceptorState dragReceptorState = org.alice.ide.clipboard.DragReceptorState.IDLE;

	/**
	 * Creates a new transcoded SVG image.
	 */
	public FullClipboardIcon() {
		this.width = getOrigWidth();
		this.height = getOrigHeight();
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

	public void setDragReceptorState( org.alice.ide.clipboard.DragReceptorState dragReceptorState ) {
		this.dragReceptorState = dragReceptorState;
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
