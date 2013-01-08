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
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

import javax.swing.Icon;

/**
 * This class has been automatically generated using svg2java
 * 
 */
public class TrashEmptyNoPattern2 implements Icon {

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

	private void paintShapeNode_0_0_1_0_0_0( Graphics2D g ) {
		GeneralPath shape0 = new GeneralPath();
		shape0.moveTo( 778.0, 53.5 );
		shape0.curveTo( 787.6596, 53.5, 795.5, 55.964, 795.5, 59.0 );
		shape0.lineTo( 795.5, 62.5 );
		shape0.lineTo( 793.0, 86.5 );
		shape0.curveTo( 793.0, 89.8165, 786.28, 92.5, 778.0, 92.5 );
		shape0.curveTo( 769.72, 92.5, 763.0, 89.8165, 763.0, 86.5 );
		shape0.lineTo( 760.5, 62.5 );
		shape0.lineTo( 760.5, 59.0 );
		shape0.curveTo( 760.5, 55.964, 768.3404, 53.5, 778.0, 53.5 );
		shape0.closePath();
		g.setPaint( new RadialGradientPaint( new Point2D.Double( 778.0, 69.73400115966797 ), 17.5f, new Point2D.Double( 778.0, 69.73400115966797 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 255, 255, 255, 255 ), new Color( 255, 255, 255, 0 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 1.3574435710906982f, -2.624109811222297E-6f, 1.1885714457093854E-6f, 0.6148442029953003f, -278.0911865234375f, 23.055614471435547f ) ) );
		g.fill( shape0 );
	}

	private void paintShapeNode_0_0_1_0_0_1_0( Graphics2D g ) {
		GeneralPath shape1 = new GeneralPath();
		shape1.moveTo( 703.66, 57.344 );
		shape1.lineTo( 706.62, 80.75 );
		shape1.curveTo( 706.62, 81.27971, 706.82513, 81.7917, 707.2136, 82.2812 );
		shape1.curveTo( 709.4256, 83.08916, 712.39453, 80.765396, 715.8706, 81.2187 );
		shape1.curveTo( 715.8364, 81.012115, 715.8211, 80.806755, 715.8211, 80.5937 );
		shape1.lineTo( 714.2911, 56.250694 );
		shape1.curveTo( 710.1145, 55.812275, 706.4651, 58.145294, 703.6551, 57.344494 );
		shape1.closePath();
		g.setPaint( new RadialGradientPaint( new Point2D.Double( 709.77001953125, 79.28099822998047 ), 6.1094f, new Point2D.Double( 709.77001953125, 79.28099822998047 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 255, 255, 255, 255 ), new Color( 255, 255, 255, 0 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 0.7793732285499573f, -0.05980715900659561f, 0.4712935984134674f, 6.141634941101074f, 119.2285385131836f, -377.1861572265625f ) ) );
		g.fill( shape1 );
	}

	private void paintShapeNode_0_0_1_0_0_1_1( Graphics2D g ) {
		GeneralPath shape2 = new GeneralPath();
		shape2.moveTo( 732.88, 57.344 );
		shape2.lineTo( 729.91, 80.75 );
		shape2.curveTo( 729.91, 81.27971, 729.70483, 81.7917, 729.31635, 82.2812 );
		shape2.curveTo( 727.1044, 83.08916, 724.13544, 80.765396, 720.65936, 81.2187 );
		shape2.curveTo( 720.69354, 81.012115, 720.70886, 80.806755, 720.70886, 80.5937 );
		shape2.lineTo( 722.2389, 56.250694 );
		shape2.curveTo( 726.41547, 55.812275, 730.0649, 58.145294, 732.8749, 57.344494 );
		shape2.closePath();
		g.setPaint( new RadialGradientPaint( new Point2D.Double( 711.0399780273438, 79.29399871826172 ), 6.1094f, new Point2D.Double( 711.0399780273438, 79.29399871826172 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 255, 255, 255, 255 ), new Color( 255, 255, 255, 0 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( -0.7793732285499573f, -0.05980715900659561f, -0.4712935984134674f, 6.141634941101074f, 1317.302734375f, -377.1861572265625f ) ) );
		g.fill( shape2 );
	}

	private void paintCompositeGraphicsNode_0_0_1_0_0_1( Graphics2D g ) {
		// _0_0_1_0_0_1_0
		g.setComposite( AlphaComposite.getInstance( 3, 0.2f * origAlpha ) );
		AffineTransform trans_0_0_1_0_0_1_0 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_1_0_0_1_0( g );
		g.setTransform( trans_0_0_1_0_0_1_0 );
		// _0_0_1_0_0_1_1
		g.setComposite( AlphaComposite.getInstance( 3, 0.5f * origAlpha ) );
		AffineTransform trans_0_0_1_0_0_1_1 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_1_0_0_1_1( g );
		g.setTransform( trans_0_0_1_0_0_1_1 );
	}

	private void paintShapeNode_0_0_1_0_0_2( Graphics2D g ) {
		GeneralPath shape3 = new GeneralPath();
		shape3.moveTo( 729.88, 60.625 );
		shape3.curveTo( 729.9235, 62.757862, 727.13367, 64.73628, 722.5715, 65.80782 );
		shape3.curveTo( 718.0092, 66.879364, 712.3758, 66.879364, 707.81354, 65.80782 );
		shape3.curveTo( 703.25134, 64.73628, 700.4615, 62.757862, 700.505, 60.625 );
		shape3.curveTo( 700.4615, 58.492138, 703.25134, 56.51372, 707.81354, 55.44218 );
		shape3.curveTo( 712.3758, 54.370636, 718.0092, 54.370636, 722.5715, 55.44218 );
		shape3.curveTo( 727.13367, 56.51372, 729.9235, 58.492138, 729.88, 60.625 );
		shape3.closePath();
		g.setPaint( new RadialGradientPaint( new Point2D.Double( 715.1900024414062, 60.625 ), 14.688f, new Point2D.Double( 715.1900024414062, 60.625 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 0, 0, 0, 255 ), new Color( 0, 0, 0, 0 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 1.0f, 0.0f, 0.0f, 0.40425530076026917f, 0.0f, 36.11701965332031f ) ) );
		g.fill( shape3 );
	}

	private void paintShapeNode_0_0_1_0_0_3( Graphics2D g ) {
		GeneralPath shape4 = new GeneralPath();
		shape4.moveTo( 729.88, 60.625 );
		shape4.curveTo( 729.9235, 62.757862, 727.13367, 64.73628, 722.5715, 65.80782 );
		shape4.curveTo( 718.0092, 66.879364, 712.3758, 66.879364, 707.81354, 65.80782 );
		shape4.curveTo( 703.25134, 64.73628, 700.4615, 62.757862, 700.505, 60.625 );
		shape4.curveTo( 700.4615, 58.492138, 703.25134, 56.51372, 707.81354, 55.44218 );
		shape4.curveTo( 712.3758, 54.370636, 718.0092, 54.370636, 722.5715, 55.44218 );
		shape4.curveTo( 727.13367, 56.51372, 729.9235, 58.492138, 729.88, 60.625 );
		shape4.closePath();
		g.setPaint( new Color( 0, 0, 0, 255 ) );
		g.fill( shape4 );
	}

	private void paintShapeNode_0_0_1_0_0_4_0( Graphics2D g ) {
		GeneralPath shape5 = new GeneralPath();
		shape5.moveTo( 718.0, 100.88 );
		shape5.curveTo( 709.168, 100.88, 701.473, 103.52969, 702.0, 106.6925 );
		shape5.lineTo( 702.5, 109.6925 );
		shape5.curveTo( 703.02716, 112.8553, 709.444, 115.505, 718.0, 115.505 );
		shape5.curveTo( 726.556, 115.505, 732.973, 112.8553, 733.5, 109.6925 );
		shape5.lineTo( 734.0, 106.6925 );
		shape5.curveTo( 734.52716, 103.52969, 726.832, 100.88, 718.0, 100.88 );
		shape5.closePath();
		g.setPaint( new LinearGradientPaint( new Point2D.Double( 703.6699829101562, 88.18800354003906 ), new Point2D.Double( 732.3300170898438, 88.18800354003906 ), new float[] { 0.0f, 0.19592f, 0.32654f, 0.46742f, 0.61475f, 0.77435f, 0.90326f, 1.0f }, new Color[] { new Color( 81, 87, 85, 255 ), new Color( 255, 255, 255, 255 ), new Color( 255, 255, 255, 255 ), new Color( 174, 179, 176, 255 ), new Color( 148, 153, 150, 255 ), new Color( 240, 241, 241, 255 ), new Color( 237, 238, 238, 255 ), new Color( 90, 90, 90, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 20.0f ) ) );
		g.fill( shape5 );
		g.setPaint( new LinearGradientPaint( new Point2D.Double( 714.6199951171875, 108.19999694824219 ), new Point2D.Double( 714.6199951171875, 103.62000274658203 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 85, 87, 83, 255 ), new Color( 136, 138, 133, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) ) );
		g.setStroke( new BasicStroke( 0.99999994f, 1, 1, 4.0f, null, 0.0f ) );
		g.draw( shape5 );
	}

	private void paintShapeNode_0_0_1_0_0_4_1( Graphics2D g ) {
		GeneralPath shape6 = new GeneralPath();
		shape6.moveTo( 718.0, 101.25 );
		shape6.curveTo( 713.7954, 101.25, 709.8694, 101.89964, 707.079, 102.9375 );
		shape6.curveTo( 705.68396, 103.45643, 704.57, 104.0741, 703.8688, 104.7187 );
		shape6.curveTo( 703.1676, 105.36336, 702.89624, 105.9843, 702.9987, 106.62489 );
		shape6.lineTo( 703.14874, 107.62489 );
		shape6.curveTo( 703.2942, 107.32755, 703.5389, 107.021965, 703.8688, 106.71864 );
		shape6.curveTo( 704.56995, 106.07398, 705.68396, 105.456345, 707.079, 104.93745 );
		shape6.curveTo( 709.86896, 103.89954, 713.795, 103.24995, 718.0, 103.24995 );
		shape6.curveTo( 722.2046, 103.24995, 726.1306, 103.89959, 728.921, 104.93745 );
		shape6.curveTo( 730.31604, 105.456375, 731.43, 106.07404, 732.1312, 106.71864 );
		shape6.curveTo( 732.4611, 107.021965, 732.7058, 107.32755, 732.85126, 107.62489 );
		shape6.lineTo( 733.0013, 106.62489 );
		shape6.curveTo( 733.1038, 105.98426, 732.8324, 105.3633, 732.1312, 104.7187 );
		shape6.curveTo( 731.43005, 104.074036, 730.31604, 103.4564, 728.921, 102.9375 );
		shape6.curveTo( 726.13104, 101.8975, 722.20105, 101.2475, 718.00104, 101.2475 );
		shape6.closePath();
		g.setPaint( new LinearGradientPaint( new Point2D.Double( 702.97998046875, 104.44000244140625 ), new Point2D.Double( 733.02001953125, 104.44000244140625 ), new float[] { 0.0f, 0.63533f, 1.0f }, new Color[] { new Color( 186, 189, 182, 255 ), new Color( 238, 238, 236, 255 ), new Color( 186, 189, 182, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) ) );
		g.fill( shape6 );
	}

	private void paintCompositeGraphicsNode_0_0_1_0_0_4( Graphics2D g ) {
		// _0_0_1_0_0_4_0
		AffineTransform trans_0_0_1_0_0_4_0 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_1_0_0_4_0( g );
		g.setTransform( trans_0_0_1_0_0_4_0 );
		// _0_0_1_0_0_4_1
		AffineTransform trans_0_0_1_0_0_4_1 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_1_0_0_4_1( g );
		g.setTransform( trans_0_0_1_0_0_4_1 );
	}

	private void paintShapeNode_0_0_1_0_0_5( Graphics2D g ) {
		GeneralPath shape7 = new GeneralPath();
		shape7.moveTo( 729.88, 60.625 );
		shape7.curveTo( 729.9235, 62.757862, 727.13367, 64.73628, 722.5715, 65.80782 );
		shape7.curveTo( 718.0092, 66.879364, 712.3758, 66.879364, 707.81354, 65.80782 );
		shape7.curveTo( 703.25134, 64.73628, 700.4615, 62.757862, 700.505, 60.625 );
		shape7.curveTo( 700.4615, 58.492138, 703.25134, 56.51372, 707.81354, 55.44218 );
		shape7.curveTo( 712.3758, 54.370636, 718.0092, 54.370636, 722.5715, 55.44218 );
		shape7.curveTo( 727.13367, 56.51372, 729.9235, 58.492138, 729.88, 60.625 );
		shape7.closePath();
		g.setPaint( new RadialGradientPaint( new Point2D.Double( 718.780029296875, 58.220001220703125 ), 14.688f, new Point2D.Double( 718.780029296875, 58.220001220703125 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 211, 215, 207, 255 ), new Color( 85, 87, 83, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 1.2169979810714722f, -1.6399266719818115f, 0.5263804197311401f, 0.7093070149421692f, -186.63723754882812f, 1192.598388671875f ) ) );
		g.fill( shape7 );
	}

	private void paintShapeNode_0_0_1_0_0_6_0( Graphics2D g ) {
		GeneralPath shape8 = new GeneralPath();
		shape8.moveTo( 688.0, 53.5 );
		shape8.curveTo( 678.3404, 53.5, 670.5, 55.964, 670.5, 59.0 );
		shape8.lineTo( 670.5, 62.5 );
		shape8.lineTo( 673.0, 86.5 );
		shape8.curveTo( 673.0, 89.8165, 679.72, 92.5, 688.0, 92.5 );
		shape8.lineTo( 688.0, 53.5 );
		shape8.closePath();
		g.setPaint( new LinearGradientPaint( new Point2D.Double( 701.3800048828125, 73.0 ), new Point2D.Double( 711.3800048828125, 71.875 ), new float[] { 0.0f, 0.29282f, 1.0f }, new Color[] { new Color( 186, 189, 182, 255 ), new Color( 136, 138, 133, 255 ), new Color( 136, 138, 133, 0 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, -30.0f, 0.0f ) ) );
		g.fill( shape8 );
	}

	private void paintShapeNode_0_0_1_0_0_6_1( Graphics2D g ) {
		GeneralPath shape9 = new GeneralPath();
		shape9.moveTo( 688.0, 53.5 );
		shape9.lineTo( 688.0, 92.5 );
		shape9.curveTo( 696.28, 92.5, 703.0, 89.8165, 703.0, 86.5 );
		shape9.lineTo( 705.5, 62.5 );
		shape9.lineTo( 705.5, 59.0 );
		shape9.curveTo( 705.5, 55.964, 697.6596, 53.5, 688.0, 53.5 );
		shape9.closePath();
		g.setPaint( new LinearGradientPaint( new Point2D.Double( 701.3800048828125, 73.0 ), new Point2D.Double( 711.3800048828125, 71.875 ), new float[] { 0.0f, 0.26535f, 1.0f }, new Color[] { new Color( 136, 138, 133, 255 ), new Color( 85, 87, 83, 255 ), new Color( 136, 138, 133, 0 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( -1.0f, 0.0f, 0.0f, 1.0f, 1406.0f, 0.0f ) ) );
		g.fill( shape9 );
	}

	private void paintShapeNode_0_0_1_0_0_6_2( Graphics2D g ) {
		GeneralPath shape10 = new GeneralPath();
		shape10.moveTo( 688.0, 53.5 );
		shape10.curveTo( 678.3404, 53.5, 670.5, 55.964, 670.5, 59.0 );
		shape10.lineTo( 670.5, 62.5 );
		shape10.lineTo( 673.0, 86.5 );
		shape10.curveTo( 673.0, 89.8165, 679.72, 92.5, 688.0, 92.5 );
		shape10.curveTo( 696.28, 92.5, 703.0, 89.8165, 703.0, 86.5 );
		shape10.lineTo( 705.5, 62.5 );
		shape10.lineTo( 705.5, 59.0 );
		shape10.curveTo( 705.5, 55.964, 697.6596, 53.5, 688.0, 53.5 );
		shape10.closePath();
		g.setPaint( new LinearGradientPaint( new Point2D.Double( 717.3099975585938, 86.96099853515625 ), new Point2D.Double( 717.3099975585938, 89.61499786376953 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 46, 52, 54, 255 ), new Color( 238, 238, 236, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, -30.0f, 0.0f ) ) );
		g.setStroke( new BasicStroke( 1.0f, 0, 0, 4.0f, null, 0.0f ) );
		g.draw( shape10 );
	}

	private void paintShapeNode_0_0_1_0_0_6_3( Graphics2D g ) {
		GeneralPath shape11 = new GeneralPath();
		shape11.moveTo( 673.66, 66.344 );
		shape11.lineTo( 676.62, 88.75 );
		shape11.curveTo( 676.62, 89.27971, 676.82513, 89.7917, 677.2136, 90.2812 );
		shape11.curveTo( 679.4256, 91.08916, 682.39453, 91.765396, 685.8706, 92.2187 );
		shape11.curveTo( 685.8364, 92.012115, 685.8211, 91.806755, 685.8211, 91.5937 );
		shape11.lineTo( 684.2911, 68.250694 );
		shape11.curveTo( 680.1145, 67.81227, 676.4651, 67.145294, 673.6551, 66.3445 );
		shape11.closePath();
		g.setPaint( new RadialGradientPaint( new Point2D.Double( 709.77001953125, 79.28099822998047 ), 6.1094f, new Point2D.Double( 709.77001953125, 79.28099822998047 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 255, 255, 255, 255 ), new Color( 255, 255, 255, 0 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 0.7793732285499573f, -0.05980715900659561f, 0.4712935984134674f, 6.141634941101074f, 89.2285385131836f, -365.1861572265625f ) ) );
		g.fill( shape11 );
	}

	private void paintShapeNode_0_0_1_0_0_6_4( Graphics2D g ) {
		GeneralPath shape12 = new GeneralPath();
		shape12.moveTo( 702.88, 66.344 );
		shape12.lineTo( 699.91, 88.75 );
		shape12.curveTo( 699.91, 89.27971, 699.70483, 89.7917, 699.31635, 90.2812 );
		shape12.curveTo( 697.1044, 91.08916, 694.13544, 91.765396, 690.65936, 92.2187 );
		shape12.curveTo( 690.69354, 92.012115, 690.70886, 91.806755, 690.70886, 91.5937 );
		shape12.lineTo( 692.2389, 68.250694 );
		shape12.curveTo( 696.41547, 67.81227, 700.0649, 67.145294, 702.8749, 66.3445 );
		shape12.closePath();
		g.setPaint( new RadialGradientPaint( new Point2D.Double( 711.0399780273438, 79.29399871826172 ), 6.1094f, new Point2D.Double( 711.0399780273438, 79.29399871826172 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 255, 255, 255, 255 ), new Color( 255, 255, 255, 0 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( -0.7793732285499573f, -0.05980715900659561f, -0.4712935984134674f, 6.141634941101074f, 1287.302734375f, -365.1861572265625f ) ) );
		g.fill( shape12 );
	}

	private void paintCompositeGraphicsNode_0_0_1_0_0_6( Graphics2D g ) {
		// _0_0_1_0_0_6_0
		AffineTransform trans_0_0_1_0_0_6_0 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_1_0_0_6_0( g );
		g.setTransform( trans_0_0_1_0_0_6_0 );
		// _0_0_1_0_0_6_1
		AffineTransform trans_0_0_1_0_0_6_1 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_1_0_0_6_1( g );
		g.setTransform( trans_0_0_1_0_0_6_1 );
		// _0_0_1_0_0_6_2
		AffineTransform trans_0_0_1_0_0_6_2 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_1_0_0_6_2( g );
		g.setTransform( trans_0_0_1_0_0_6_2 );
		// _0_0_1_0_0_6_3
		AffineTransform trans_0_0_1_0_0_6_3 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_1_0_0_6_3( g );
		g.setTransform( trans_0_0_1_0_0_6_3 );
		// _0_0_1_0_0_6_4
		g.setComposite( AlphaComposite.getInstance( 3, 0.4f * origAlpha ) );
		AffineTransform trans_0_0_1_0_0_6_4 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_1_0_0_6_4( g );
		g.setTransform( trans_0_0_1_0_0_6_4 );
	}

	private void paintShapeNode_0_0_1_0_0_7( Graphics2D g ) {
		GeneralPath shape13 = new GeneralPath();
		shape13.moveTo( 720.0, 55.5 );
		shape13.curveTo( 712.2429, 55.5, 705.622, 57.1821, 702.875, 59.5625 );
		shape13.lineTo( 702.875, 62.25 );
		shape13.lineTo( 703.53125, 62.25 );
		shape13.curveTo( 703.51465, 62.16703, 703.46875, 62.08408, 703.46875, 62.0 );
		shape13.curveTo( 703.46875, 59.5016, 710.87756, 57.4688, 719.99976, 57.4688 );
		shape13.curveTo( 729.12213, 57.468796, 736.53076, 59.5016, 736.53076, 62.0 );
		shape13.curveTo( 736.53076, 62.08408, 736.48486, 62.16703, 736.46826, 62.25 );
		shape13.lineTo( 737.2495, 62.25 );
		shape13.lineTo( 737.2495, 59.6875 );
		shape13.curveTo( 734.5795, 57.2425, 727.8795, 55.4995, 719.9895, 55.4995 );
		shape13.closePath();
		g.setPaint( new Color( 0, 0, 0, 255 ) );
		g.fill( shape13 );
		g.setStroke( new BasicStroke( 0.9f, 1, 1, 4.0f, null, 0.0f ) );
		g.draw( shape13 );
	}

	private void paintShapeNode_0_0_1_0_0_8( Graphics2D g ) {
		GeneralPath shape14 = new GeneralPath();
		shape14.moveTo( 703.69, 65.156 );
		shape14.lineTo( 703.90875, 67.187195 );
		shape14.curveTo( 707.09, 69.15479, 713.1058, 70.499695, 720.00275, 70.499695 );
		shape14.curveTo( 726.8995, 70.499695, 732.91473, 69.15479, 736.09674, 67.187195 );
		shape14.lineTo( 736.3155, 65.156 );
		shape14.lineTo( 703.6905, 65.156 );
		shape14.closePath();
		g.fill( shape14 );
		g.draw( shape14 );
	}

	private void paintShapeNode_0_0_1_0_0_9( Graphics2D g ) {
		GeneralPath shape15 = new GeneralPath();
		shape15.moveTo( 778.0, 53.5 );
		shape15.curveTo( 787.6596, 53.5, 795.5, 55.964, 795.5, 59.0 );
		shape15.lineTo( 795.5, 62.5 );
		shape15.curveTo( 784.289, 65.464, 772.653, 65.6629, 760.5, 62.5 );
		shape15.lineTo( 760.5, 59.0 );
		shape15.curveTo( 760.5, 55.964, 768.3404, 53.5, 778.0, 53.5 );
		shape15.closePath();
		g.setPaint( new LinearGradientPaint( new Point2D.Double( 734.1199951171875, 72.822998046875 ), new Point2D.Double( 700.5, 72.822998046875 ), new float[] { 0.0f, 0.25f, 0.5f, 0.75f, 1.0f }, new Color[] { new Color( 11, 11, 11, 255 ), new Color( 186, 189, 182, 153 ), new Color( 197, 197, 197, 255 ), new Color( 211, 215, 207, 51 ), new Color( 136, 138, 133, 0 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( -1.0f, 0.0f, 0.0f, 1.0f, 1496.0f, 0.0f ) ) );
		g.fill( shape15 );
	}

	private void paintShapeNode_0_0_1_0_0_10( Graphics2D g ) {
		GeneralPath shape16 = new GeneralPath();
		shape16.moveTo( 729.88, 60.625 );
		shape16.curveTo( 729.9235, 62.757862, 727.13367, 64.73628, 722.5715, 65.80782 );
		shape16.curveTo( 718.0092, 66.879364, 712.3758, 66.879364, 707.81354, 65.80782 );
		shape16.curveTo( 703.25134, 64.73628, 700.4615, 62.757862, 700.505, 60.625 );
		shape16.curveTo( 700.4615, 58.492138, 703.25134, 56.51372, 707.81354, 55.44218 );
		shape16.curveTo( 712.3758, 54.370636, 718.0092, 54.370636, 722.5715, 55.44218 );
		shape16.curveTo( 727.13367, 56.51372, 729.9235, 58.492138, 729.88, 60.625 );
		shape16.closePath();
		g.setPaint( new RadialGradientPaint( new Point2D.Double( 717.2999877929688, 48.93899917602539 ), 16.06f, new Point2D.Double( 717.2999877929688, 48.93899917602539 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 201, 202, 200, 255 ), new Color( 86, 87, 84, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 1.0f, 0.0f, 0.0f, 0.45515385270118713f, 0.0f, 33.03129577636719f ) ) );
		g.setStroke( new BasicStroke( 2.7441592f, 1, 1, 4.0f, null, 0.0f ) );
		g.draw( shape16 );
	}

	private void paintShapeNode_0_0_1_0_0_11( Graphics2D g ) {
		GeneralPath shape17 = new GeneralPath();
		shape17.moveTo( 720.0, 53.5 );
		shape17.curveTo( 709.788, 53.5, 701.5, 56.412, 701.5, 60.0 );
		shape17.lineTo( 701.5, 63.0 );
		shape17.curveTo( 701.5, 66.588, 709.788, 69.5, 720.0, 69.5 );
		shape17.curveTo( 730.212, 69.5, 738.5, 66.588, 738.5, 63.0 );
		shape17.lineTo( 738.5, 60.0 );
		shape17.curveTo( 738.5, 56.412, 730.212, 53.5, 720.0, 53.5 );
		shape17.closePath();
		shape17.moveTo( 720.0, 55.4688 );
		shape17.curveTo( 729.1224, 55.468796, 736.531, 57.5016, 736.531, 60.0 );
		shape17.curveTo( 736.531, 62.4984, 729.1222, 64.5312, 720.0, 64.5312 );
		shape17.curveTo( 710.8776, 64.5312, 703.469, 62.498398, 703.469, 59.999996 );
		shape17.curveTo( 703.469, 57.501595, 710.8778, 55.468796, 720.0, 55.468796 );
		shape17.closePath();
		g.setPaint( new LinearGradientPaint( new Point2D.Double( 700.989990234375, 60.5 ), new Point2D.Double( 735.010009765625, 60.5 ), new float[] { 0.0f, 0.19592f, 0.32654f, 0.46742f, 0.61475f, 0.77435f, 0.90326f, 1.0f }, new Color[] { new Color( 81, 87, 85, 255 ), new Color( 255, 255, 255, 255 ), new Color( 255, 255, 255, 255 ), new Color( 174, 179, 176, 255 ), new Color( 148, 153, 150, 255 ), new Color( 240, 241, 241, 255 ), new Color( 237, 238, 238, 255 ), new Color( 90, 90, 90, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 2.0f, 1.0f ) ) );
		g.fill( shape17 );
		g.setPaint( new LinearGradientPaint( new Point2D.Double( 728.4099731445312, 64.5250015258789 ), new Point2D.Double( 728.4099731445312, 71.11199951171875 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 46, 52, 54, 255 ), new Color( 186, 189, 182, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 2.0f, 1.0f ) ) );
		g.setStroke( new BasicStroke( 0.9f, 1, 1, 4.0f, null, 0.0f ) );
		g.draw( shape17 );
	}

	private void paintShapeNode_0_0_1_0_0_12( Graphics2D g ) {
		GeneralPath shape18 = new GeneralPath();
		shape18.moveTo( 720.0, 54.5 );
		shape18.curveTo( 714.9884, 54.5, 710.4495, 55.21959, 707.25, 56.3438 );
		shape18.curveTo( 705.6502, 56.90588, 704.3936, 57.586, 703.5938, 58.25 );
		shape18.curveTo( 702.7938, 58.914, 702.5038, 59.485, 702.5038, 60.0 );
		shape18.lineTo( 702.5038, 63.0 );
		shape18.curveTo( 702.5038, 63.42184, 702.7497, 63.93504, 703.3788, 64.5312 );
		shape18.curveTo( 703.5552, 64.69481, 703.66626, 64.91689, 703.6913, 65.1562 );
		shape18.lineTo( 705.8163, 84.59419 );
		shape18.curveTo( 705.84106, 84.88318, 705.7384, 85.16857, 705.53503, 85.37544 );
		shape18.curveTo( 705.0505, 85.87085, 704.9204, 86.219, 704.97253, 86.53164 );
		shape18.lineTo( 705.47253, 89.53164 );
		shape18.curveTo( 705.63605, 90.51277, 707.01154, 91.84564, 709.59753, 92.84414 );
		shape18.curveTo( 712.17755, 93.84214, 715.8175, 94.50014, 719.99756, 94.50014 );
		shape18.curveTo( 724.17303, 94.50014, 727.8179, 93.84235, 730.40356, 92.84394 );
		shape18.curveTo( 732.98956, 91.84548, 734.36505, 90.51254, 734.52856, 89.53144 );
		shape18.lineTo( 735.02856, 86.53144 );
		shape18.curveTo( 735.0807, 86.21875, 734.9507, 85.8706, 734.46606, 85.375244 );
		shape18.curveTo( 734.26276, 85.16837, 734.16003, 84.88298, 734.1848, 84.593994 );
		shape18.lineTo( 736.3098, 65.15599 );
		shape18.curveTo( 736.33484, 64.91669, 736.4459, 64.6946, 736.6223, 64.53099 );
		shape18.curveTo( 737.2514, 63.93479, 737.4973, 63.42159, 737.4973, 62.99979 );
		shape18.lineTo( 737.4973, 59.99979 );
		shape18.curveTo( 737.4973, 59.48518, 737.20337, 58.91379, 736.4035, 58.24979 );
		shape18.curveTo( 735.6035, 57.58479, 734.3435, 56.90479, 732.7435, 56.34279 );
		shape18.curveTo( 729.5435, 55.21879, 725.00354, 54.498787, 719.9935, 54.498787 );
		shape18.closePath();
		g.setPaint( new LinearGradientPaint( new Point2D.Double( 704.6199951171875, 62.875 ), new Point2D.Double( 736.0, 97.625 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 255, 255, 255, 255 ), new Color( 255, 255, 255, 0 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 2.0f, 0.0f ) ) );
		g.setStroke( new BasicStroke( 0.99999994f, 1, 1, 4.0f, null, 0.0f ) );
		g.draw( shape18 );
	}

	private void paintShapeNode_0_0_1_0_0_13( Graphics2D g ) {
		GeneralPath shape19 = new GeneralPath();
		shape19.moveTo( 729.88, 60.625 );
		shape19.curveTo( 729.9235, 62.757862, 727.13367, 64.73628, 722.5715, 65.80782 );
		shape19.curveTo( 718.0092, 66.879364, 712.3758, 66.879364, 707.81354, 65.80782 );
		shape19.curveTo( 703.25134, 64.73628, 700.4615, 62.757862, 700.505, 60.625 );
		shape19.curveTo( 700.4615, 58.492138, 703.25134, 56.51372, 707.81354, 55.44218 );
		shape19.curveTo( 712.3758, 54.370636, 718.0092, 54.370636, 722.5715, 55.44218 );
		shape19.curveTo( 727.13367, 56.51372, 729.9235, 58.492138, 729.88, 60.625 );
		shape19.closePath();
		g.setPaint( new RadialGradientPaint( new Point2D.Double( 712.1099853515625, 64.05899810791016 ), 15.137f, new Point2D.Double( 712.1099853515625, 64.05899810791016 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 255, 255, 255, 255 ), new Color( 211, 215, 207, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 0.429599791765213f, 1.1141945123672485f, -3.6476752758026123f, 2.3118515014648438f, 635.1559448242188f, -875.8652954101562f ) ) );
		g.setStroke( new BasicStroke( 0.8992707f, 1, 1, 4.0f, null, 0.0f ) );
		g.draw( shape19 );
	}

	private void paintShapeNode_0_0_1_0_0_14( Graphics2D g ) {
		GeneralPath shape20 = new GeneralPath();
		shape20.moveTo( 708.19, 61.812 );
		shape20.curveTo( 708.19, 63.22723, 707.0427, 64.3745, 705.6275, 64.3745 );
		shape20.curveTo( 704.2123, 64.3745, 703.065, 63.22723, 703.065, 61.812 );
		shape20.curveTo( 703.065, 60.39677, 704.2123, 59.2495, 705.6275, 59.2495 );
		shape20.curveTo( 707.0427, 59.2495, 708.19, 60.39677, 708.19, 61.812 );
		shape20.closePath();
		g.setPaint( new RadialGradientPaint( new Point2D.Double( 705.6199951171875, 61.8120002746582 ), 2.5625f, new Point2D.Double( 705.6199951171875, 61.8120002746582 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 255, 255, 255, 255 ), new Color( 255, 255, 255, 0 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) ) );
		g.fill( shape20 );
	}

	private void paintShapeNode_0_0_1_0_0_15( Graphics2D g ) {
		GeneralPath shape21 = new GeneralPath();
		shape21.moveTo( 708.19, 61.812 );
		shape21.curveTo( 708.19, 63.22723, 707.0427, 64.3745, 705.6275, 64.3745 );
		shape21.curveTo( 704.2123, 64.3745, 703.065, 63.22723, 703.065, 61.812 );
		shape21.curveTo( 703.065, 60.39677, 704.2123, 59.2495, 705.6275, 59.2495 );
		shape21.curveTo( 707.0427, 59.2495, 708.19, 60.39677, 708.19, 61.812 );
		shape21.closePath();
		g.setPaint( new Color( 255, 255, 255, 255 ) );
		g.fill( shape21 );
	}

	private void paintShapeNode_0_0_1_0_0_16( Graphics2D g ) {
		GeneralPath shape22 = new GeneralPath();
		shape22.moveTo( 708.19, 61.812 );
		shape22.curveTo( 708.19, 63.22723, 707.0427, 64.3745, 705.6275, 64.3745 );
		shape22.curveTo( 704.2123, 64.3745, 703.065, 63.22723, 703.065, 61.812 );
		shape22.curveTo( 703.065, 60.39677, 704.2123, 59.2495, 705.6275, 59.2495 );
		shape22.curveTo( 707.0427, 59.2495, 708.19, 60.39677, 708.19, 61.812 );
		shape22.closePath();
		g.setPaint( new RadialGradientPaint( new Point2D.Double( 705.6199951171875, 61.8120002746582 ), 2.5625f, new Point2D.Double( 705.6199951171875, 61.8120002746582 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 255, 255, 255, 255 ), new Color( 255, 255, 255, 0 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) ) );
		g.fill( shape22 );
	}

	private void paintShapeNode_0_0_1_0_0_17( Graphics2D g ) {
		GeneralPath shape23 = new GeneralPath();
		shape23.moveTo( 708.19, 61.812 );
		shape23.curveTo( 708.19, 63.22723, 707.0427, 64.3745, 705.6275, 64.3745 );
		shape23.curveTo( 704.2123, 64.3745, 703.065, 63.22723, 703.065, 61.812 );
		shape23.curveTo( 703.065, 60.39677, 704.2123, 59.2495, 705.6275, 59.2495 );
		shape23.curveTo( 707.0427, 59.2495, 708.19, 60.39677, 708.19, 61.812 );
		shape23.closePath();
		g.setPaint( new Color( 255, 255, 255, 255 ) );
		g.fill( shape23 );
	}

	private void paintShapeNode_0_0_1_0_0_18( Graphics2D g ) {
		GeneralPath shape24 = new GeneralPath();
		shape24.moveTo( 708.19, 61.812 );
		shape24.curveTo( 708.19, 63.22723, 707.0427, 64.3745, 705.6275, 64.3745 );
		shape24.curveTo( 704.2123, 64.3745, 703.065, 63.22723, 703.065, 61.812 );
		shape24.curveTo( 703.065, 60.39677, 704.2123, 59.2495, 705.6275, 59.2495 );
		shape24.curveTo( 707.0427, 59.2495, 708.19, 60.39677, 708.19, 61.812 );
		shape24.closePath();
		g.fill( shape24 );
	}

	private void paintShapeNode_0_0_1_0_0_19( Graphics2D g ) {
		GeneralPath shape25 = new GeneralPath();
		shape25.moveTo( 703.81, 70.625 );
		shape25.lineTo( 705.4662, 86.437 );
		shape25.curveTo( 705.46716, 86.45781, 705.46716, 86.47868, 705.4662, 86.4995 );
		shape25.curveTo( 705.4662, 87.166695, 705.80145, 87.8415, 706.4974, 88.4995 );
		shape25.curveTo( 707.19336, 89.157524, 708.2414, 89.7849, 709.5599, 90.312 );
		shape25.curveTo( 712.1968, 91.366196, 715.9092, 92.0308, 719.99786, 92.0308 );
		shape25.curveTo( 724.08606, 92.0308, 727.79846, 91.36628, 730.43585, 90.312 );
		shape25.curveTo( 731.75433, 89.78489, 732.80237, 89.15749, 733.49835, 88.4995 );
		shape25.curveTo( 734.19434, 87.84147, 734.52954, 87.166695, 734.52954, 86.4995 );
		shape25.curveTo( 734.52856, 86.47868, 734.52856, 86.45781, 734.52954, 86.437 );
		shape25.lineTo( 736.1857, 70.625 );
		shape25.lineTo( 703.8107, 70.625 );
		shape25.closePath();
		g.setPaint( new LinearGradientPaint( new Point2D.Double( 688.0, 92.03099822998047 ), new Point2D.Double( 688.0, 73.0 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 238, 238, 236, 255 ), new Color( 186, 189, 182, 0 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 32.0f, 0.0f ) ) );
		g.fill( shape25 );
	}

	private void paintShapeNode_0_0_1_0_0_20_0( Graphics2D g ) {
		GeneralPath shape26 = new GeneralPath();
		shape26.moveTo( 762.5, 59.188 );
		shape26.lineTo( 762.5, 61.5005 );
		shape26.lineTo( 765.0, 85.5005 );
		shape26.curveTo( 765.0, 88.817, 771.72, 91.5005, 780.0, 91.5005 );
		shape26.curveTo( 788.28, 91.5005, 795.0, 88.817, 795.0, 85.5005 );
		shape26.lineTo( 797.5, 61.500504 );
		shape26.lineTo( 797.5, 59.188004 );
		shape26.curveTo( 797.1822, 62.150604, 789.4729, 64.53181, 780.0, 64.53181 );
		shape26.curveTo( 770.5271, 64.53181, 762.818, 62.150707, 762.5, 59.188007 );
		shape26.closePath();
		g.setPaint( new RadialGradientPaint( new Point2D.Double( 768.0, 78.99199676513672 ), 17.5f, new Point2D.Double( 768.0, 78.99199676513672 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 255, 255, 255, 255 ), new Color( 255, 255, 255, 76 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 0.9001134037971497f, 0.0f, 0.0f, 3.4375f, 88.71292877197266f, -185.869140625f ) ) );
		g.fill( shape26 );
	}

	private void paintShapeNode_0_0_1_0_0_20_1( Graphics2D g ) {
		GeneralPath shape27 = new GeneralPath();
		shape27.moveTo( 751.25, 65.812 );
		shape27.lineTo( 753.5, 86.5 );
		shape27.curveTo( 753.5, 89.8165, 759.72, 92.0, 768.0, 92.0 );
		shape27.curveTo( 776.28, 92.0, 782.625, 89.8165, 782.625, 86.5 );
		shape27.lineTo( 784.625, 65.812 );
		shape27.curveTo( 776.6876, 70.67249, 759.649, 70.80029, 751.25, 65.812 );
		shape27.closePath();
		g.setPaint( new Color( 0, 0, 0, 255 ) );
		g.fill( shape27 );
	}

	private void paintCompositeGraphicsNode_0_0_1_0_0_20( Graphics2D g ) {
		// _0_0_1_0_0_20_0
		g.setComposite( AlphaComposite.getInstance( 3, 1.0f * origAlpha ) );
		AffineTransform trans_0_0_1_0_0_20_0 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_1_0_0_20_0( g );
		g.setTransform( trans_0_0_1_0_0_20_0 );
		// _0_0_1_0_0_20_1
		AffineTransform trans_0_0_1_0_0_20_1 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 12.0f, 0.0f ) );
		paintShapeNode_0_0_1_0_0_20_1( g );
		g.setTransform( trans_0_0_1_0_0_20_1 );
	}

	private void paintCompositeGraphicsNode_0_0_1_0_0( Graphics2D g ) {
		// _0_0_1_0_0_0
		g.setComposite( AlphaComposite.getInstance( 3, 0.4f * origAlpha ) );
		AffineTransform trans_0_0_1_0_0_0 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, -58.0f, -1.0f ) );
		paintShapeNode_0_0_1_0_0_0( g );
		g.setTransform( trans_0_0_1_0_0_0 );
		// _0_0_1_0_0_1
		g.setComposite( AlphaComposite.getInstance( 3, 1.0f * origAlpha ) );
		AffineTransform trans_0_0_1_0_0_1 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 2.0f, 0.0f ) );
		paintCompositeGraphicsNode_0_0_1_0_0_1( g );
		g.setTransform( trans_0_0_1_0_0_1 );
		// _0_0_1_0_0_2
		g.setComposite( AlphaComposite.getInstance( 3, 1.0f * origAlpha ) );
		AffineTransform trans_0_0_1_0_0_2 = g.getTransform();
		g.transform( new AffineTransform( 1.3276629447937012f, 0.0f, 0.0f, 1.2711591720581055f, -229.52792358398438f, 13.388467788696289f ) );
		paintShapeNode_0_0_1_0_0_2( g );
		g.setTransform( trans_0_0_1_0_0_2 );
		// _0_0_1_0_0_3
		g.setComposite( AlphaComposite.getInstance( 3, 0.3f * origAlpha ) );
		AffineTransform trans_0_0_1_0_0_3 = g.getTransform();
		g.transform( new AffineTransform( 1.1362465620040894f, 0.0f, 0.0f, 1.0947370529174805f, -92.62933349609375f, 24.13157081604004f ) );
		paintShapeNode_0_0_1_0_0_3( g );
		g.setTransform( trans_0_0_1_0_0_3 );
		// _0_0_1_0_0_4
		g.setComposite( AlphaComposite.getInstance( 3, 1.0f * origAlpha ) );
		AffineTransform trans_0_0_1_0_0_4 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 2.0f, -20.0f ) );
		paintCompositeGraphicsNode_0_0_1_0_0_4( g );
		g.setTransform( trans_0_0_1_0_0_4 );
		// _0_0_1_0_0_5
		AffineTransform trans_0_0_1_0_0_5 = g.getTransform();
		g.transform( new AffineTransform( 1.0212769508361816f, 0.0f, 0.0f, 0.7578948736190796f, -10.404540061950684f, 41.5526237487793f ) );
		paintShapeNode_0_0_1_0_0_5( g );
		g.setTransform( trans_0_0_1_0_0_5 );
		// _0_0_1_0_0_6
		AffineTransform trans_0_0_1_0_0_6 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 32.0f, 0.0f ) );
		paintCompositeGraphicsNode_0_0_1_0_0_6( g );
		g.setTransform( trans_0_0_1_0_0_6 );
		// _0_0_1_0_0_7
		g.setComposite( AlphaComposite.getInstance( 3, 0.2f * origAlpha ) );
		AffineTransform trans_0_0_1_0_0_7 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_1_0_0_7( g );
		g.setTransform( trans_0_0_1_0_0_7 );
		// _0_0_1_0_0_8
		g.setComposite( AlphaComposite.getInstance( 3, 0.1f * origAlpha ) );
		AffineTransform trans_0_0_1_0_0_8 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_1_0_0_8( g );
		g.setTransform( trans_0_0_1_0_0_8 );
		// _0_0_1_0_0_9
		g.setComposite( AlphaComposite.getInstance( 3, 1.0f * origAlpha ) );
		AffineTransform trans_0_0_1_0_0_9 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, -58.0f, 0.0f ) );
		paintShapeNode_0_0_1_0_0_9( g );
		g.setTransform( trans_0_0_1_0_0_9 );
		// _0_0_1_0_0_10
		AffineTransform trans_0_0_1_0_0_10 = g.getTransform();
		g.transform( new AffineTransform( 1.1827048063278198f, 0.0f, 0.0f, 1.0105259418487549f, -125.85575103759766f, 0.9868555665016174f ) );
		paintShapeNode_0_0_1_0_0_10( g );
		g.setTransform( trans_0_0_1_0_0_10 );
		// _0_0_1_0_0_11
		AffineTransform trans_0_0_1_0_0_11 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_1_0_0_11( g );
		g.setTransform( trans_0_0_1_0_0_11 );
		// _0_0_1_0_0_12
		g.setComposite( AlphaComposite.getInstance( 3, 0.4f * origAlpha ) );
		AffineTransform trans_0_0_1_0_0_12 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_1_0_0_12( g );
		g.setTransform( trans_0_0_1_0_0_12 );
		// _0_0_1_0_0_13
		g.setComposite( AlphaComposite.getInstance( 3, 1.0f * origAlpha ) );
		AffineTransform trans_0_0_1_0_0_13 = g.getTransform();
		g.transform( new AffineTransform( 1.1932722330093384f, 0.0f, 0.0f, 0.9307202100753784f, -133.4133758544922f, 3.575089931488037f ) );
		paintShapeNode_0_0_1_0_0_13( g );
		g.setTransform( trans_0_0_1_0_0_13 );
		// _0_0_1_0_0_14
		AffineTransform trans_0_0_1_0_0_14 = g.getTransform();
		g.transform( new AffineTransform( 0.8536584973335266f, 0.0f, 0.0f, 0.8536584973335266f, 108.97374725341797f, 12.123395919799805f ) );
		paintShapeNode_0_0_1_0_0_14( g );
		g.setTransform( trans_0_0_1_0_0_14 );
		// _0_0_1_0_0_15
		AffineTransform trans_0_0_1_0_0_15 = g.getTransform();
		g.transform( new AffineTransform( 0.41044938564300537f, 0.0f, 0.0f, 0.41044938564300537f, 421.7131652832031f, 39.519256591796875f ) );
		paintShapeNode_0_0_1_0_0_15( g );
		g.setTransform( trans_0_0_1_0_0_15 );
		// _0_0_1_0_0_16
		AffineTransform trans_0_0_1_0_0_16 = g.getTransform();
		g.transform( new AffineTransform( 0.8536584973335266f, 0.0f, 0.0f, 0.8536584973335266f, 109.15052795410156f, 38.7005615234375f ) );
		paintShapeNode_0_0_1_0_0_16( g );
		g.setTransform( trans_0_0_1_0_0_16 );
		// _0_0_1_0_0_17
		AffineTransform trans_0_0_1_0_0_17 = g.getTransform();
		g.transform( new AffineTransform( 0.41044938564300537f, 0.0f, 0.0f, 0.41044938564300537f, 421.88995361328125f, 66.09642028808594f ) );
		paintShapeNode_0_0_1_0_0_17( g );
		g.setTransform( trans_0_0_1_0_0_17 );
		// _0_0_1_0_0_18
		AffineTransform trans_0_0_1_0_0_18 = g.getTransform();
		g.transform( new AffineTransform( 0.31288841366767883f, 0.0f, 0.0f, 0.31288841366767883f, 508.5306091308594f, 36.097084045410156f ) );
		paintShapeNode_0_0_1_0_0_18( g );
		g.setTransform( trans_0_0_1_0_0_18 );
		// _0_0_1_0_0_19
		g.setComposite( AlphaComposite.getInstance( 3, 0.7f * origAlpha ) );
		AffineTransform trans_0_0_1_0_0_19 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_1_0_0_19( g );
		g.setTransform( trans_0_0_1_0_0_19 );
		// _0_0_1_0_0_20
		g.setComposite( AlphaComposite.getInstance( 3, 0.19902912f * origAlpha ) );
		AffineTransform trans_0_0_1_0_0_20 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, -59.993717193603516f, 0.08838800340890884f ) );
		paintCompositeGraphicsNode_0_0_1_0_0_20( g );
		g.setTransform( trans_0_0_1_0_0_20 );
	}

	private void paintCompositeGraphicsNode_0_0_1_0( Graphics2D g ) {
		// _0_0_1_0_0
		AffineTransform trans_0_0_1_0_0 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintCompositeGraphicsNode_0_0_1_0_0( g );
		g.setTransform( trans_0_0_1_0_0 );
	}

	private void paintCompositeGraphicsNode_0_0_1_1( Graphics2D g ) {
	}

	private void paintCompositeGraphicsNode_0_0_1( Graphics2D g ) {
		// _0_0_1_0
		AffineTransform trans_0_0_1_0 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintCompositeGraphicsNode_0_0_1_0( g );
		g.setTransform( trans_0_0_1_0 );
		// _0_0_1_1
		AffineTransform trans_0_0_1_1 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintCompositeGraphicsNode_0_0_1_1( g );
		g.setTransform( trans_0_0_1_1 );
	}

	private void paintCanvasGraphicsNode_0_0( Graphics2D g ) {
		// _0_0_0
		AffineTransform trans_0_0_0 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, -700.5f, -52.5f ) );
		paintCompositeGraphicsNode_0_0_0( g );
		g.setTransform( trans_0_0_0 );
		// _0_0_1
		AffineTransform trans_0_0_1 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, -700.5f, -52.5f ) );
		paintCompositeGraphicsNode_0_0_1( g );
		g.setTransform( trans_0_0_1 );
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
		return 39;
	}

	/**
	 * Returns the height of the bounding box of the original SVG image.
	 * 
	 * @return The height of the bounding box of the original SVG image.
	 */
	public int getOrigHeight() {
		return 46;
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
	public TrashEmptyNoPattern2() {
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
