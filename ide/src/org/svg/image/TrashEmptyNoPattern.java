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
public class TrashEmptyNoPattern implements Icon {

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

	private void paintCompositeGraphicsNode_0_0_0_1( Graphics2D g ) {
	}

	private void paintCompositeGraphicsNode_0_0_0_2( Graphics2D g ) {
	}

	private void paintCompositeGraphicsNode_0_0_0( Graphics2D g ) {
		// _0_0_0_0
		AffineTransform trans_0_0_0_0 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintCompositeGraphicsNode_0_0_0_0( g );
		g.setTransform( trans_0_0_0_0 );
		// _0_0_0_1
		AffineTransform trans_0_0_0_1 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintCompositeGraphicsNode_0_0_0_1( g );
		g.setTransform( trans_0_0_0_1 );
		// _0_0_0_2
		AffineTransform trans_0_0_0_2 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 20.0f, 30.0f ) );
		paintCompositeGraphicsNode_0_0_0_2( g );
		g.setTransform( trans_0_0_0_2 );
	}

	private void paintCompositeGraphicsNode_0_0_1_0( Graphics2D g ) {
	}

	private void paintShapeNode_0_0_1_1_0_0( Graphics2D g ) {
		GeneralPath shape0 = new GeneralPath();
		shape0.moveTo( 778.0, 53.5 );
		shape0.curveTo( 787.65955, 53.5, 795.5, 55.964, 795.5, 59.0 );
		shape0.lineTo( 795.5, 62.5 );
		shape0.lineTo( 793.0, 86.5 );
		shape0.curveTo( 793.0, 89.81647, 786.28, 92.5, 778.0, 92.5 );
		shape0.curveTo( 769.72, 92.5, 763.0, 89.81647, 763.0, 86.5 );
		shape0.lineTo( 760.5, 62.5 );
		shape0.lineTo( 760.5, 59.0 );
		shape0.curveTo( 760.5, 55.964, 768.3404, 53.5, 778.0, 53.5 );
		shape0.closePath();
		g.setPaint( new RadialGradientPaint( new Point2D.Double( 778.0, 69.73368072509766 ), 17.5f, new Point2D.Double( 778.0, 69.73368072509766 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 255, 255, 255, 255 ), new Color( 255, 255, 255, 0 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 1.3574435710906982f, -2.624109811222297E-6f, 1.1885714457093854E-6f, 0.6148442029953003f, -278.0911865234375f, 23.055614471435547f ) ) );
		g.fill( shape0 );
	}

	private void paintShapeNode_0_0_1_1_0_1_0( Graphics2D g ) {
		GeneralPath shape1 = new GeneralPath();
		shape1.moveTo( 703.65625, 57.34375 );
		shape1.lineTo( 706.6244, 80.75 );
		shape1.curveTo( 706.6244, 81.27971, 706.8295, 81.79167, 707.218, 82.28125 );
		shape1.curveTo( 709.43005, 83.08921, 712.3989, 80.7654, 715.875, 81.21875 );
		shape1.curveTo( 715.8408, 81.01217, 715.8255, 80.806816, 715.8255, 80.59375 );
		shape1.lineTo( 714.292, 56.25 );
		shape1.curveTo( 710.1154, 55.811577, 706.466, 58.144646, 703.65625, 57.34375 );
		shape1.closePath();
		g.setPaint( new RadialGradientPaint( new Point2D.Double( 709.765625, 79.28125 ), 6.109375f, new Point2D.Double( 709.765625, 79.28125 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 255, 255, 255, 255 ), new Color( 255, 255, 255, 0 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 0.7793732285499573f, -0.05980715900659561f, 0.4712935984134674f, 6.141634941101074f, 119.2285385131836f, -377.1861572265625f ) ) );
		g.fill( shape1 );
	}

	private void paintShapeNode_0_0_1_1_0_1_1( Graphics2D g ) {
		GeneralPath shape2 = new GeneralPath();
		shape2.moveTo( 732.875, 57.34375 );
		shape2.lineTo( 729.90686, 80.75 );
		shape2.curveTo( 729.90686, 81.27971, 729.7017, 81.79167, 729.31323, 82.28125 );
		shape2.curveTo( 727.1012, 83.08921, 724.1323, 80.7654, 720.65625, 81.21875 );
		shape2.curveTo( 720.6904, 81.01217, 720.70575, 80.806816, 720.70575, 80.59375 );
		shape2.lineTo( 722.23926, 56.25 );
		shape2.curveTo( 726.41583, 55.811577, 730.06525, 58.144646, 732.875, 57.34375 );
		shape2.closePath();
		g.setPaint( new RadialGradientPaint( new Point2D.Double( 711.0411987304688, 79.29367065429688 ), 6.109375f, new Point2D.Double( 711.0411987304688, 79.29367065429688 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 255, 255, 255, 255 ), new Color( 255, 255, 255, 0 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( -0.7793732285499573f, -0.05980715900659561f, -0.4712935984134674f, 6.141634941101074f, 1317.302734375f, -377.1861572265625f ) ) );
		g.fill( shape2 );
	}

	private void paintCompositeGraphicsNode_0_0_1_1_0_1( Graphics2D g ) {
		// _0_0_1_1_0_1_0
		g.setComposite( AlphaComposite.getInstance( 3, 0.2f * origAlpha ) );
		AffineTransform trans_0_0_1_1_0_1_0 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_1_1_0_1_0( g );
		g.setTransform( trans_0_0_1_1_0_1_0 );
		// _0_0_1_1_0_1_1
		g.setComposite( AlphaComposite.getInstance( 3, 0.5f * origAlpha ) );
		AffineTransform trans_0_0_1_1_0_1_1 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_1_1_0_1_1( g );
		g.setTransform( trans_0_0_1_1_0_1_1 );
	}

	private void paintShapeNode_0_0_1_1_0_2( Graphics2D g ) {
		GeneralPath shape3 = new GeneralPath();
		shape3.moveTo( 729.875, 60.625 );
		shape3.curveTo( 729.875, 63.90419, 723.2992, 66.5625, 715.1875, 66.5625 );
		shape3.curveTo( 707.0758, 66.5625, 700.5, 63.90419, 700.5, 60.625 );
		shape3.curveTo( 700.5, 57.34581, 707.0758, 54.6875, 715.1875, 54.6875 );
		shape3.curveTo( 723.2992, 54.6875, 729.875, 57.34581, 729.875, 60.625 );
		shape3.closePath();
		g.setPaint( new RadialGradientPaint( new Point2D.Double( 715.1875, 60.625 ), 14.6875f, new Point2D.Double( 715.1875, 60.625 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 0, 0, 0, 255 ), new Color( 0, 0, 0, 0 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 1.0f, 0.0f, 0.0f, 0.40425530076026917f, 0.0f, 36.11701965332031f ) ) );
		g.fill( shape3 );
	}

	private void paintShapeNode_0_0_1_1_0_3( Graphics2D g ) {
		GeneralPath shape4 = new GeneralPath();
		shape4.moveTo( 729.875, 60.625 );
		shape4.curveTo( 729.875, 63.90419, 723.2992, 66.5625, 715.1875, 66.5625 );
		shape4.curveTo( 707.0758, 66.5625, 700.5, 63.90419, 700.5, 60.625 );
		shape4.curveTo( 700.5, 57.34581, 707.0758, 54.6875, 715.1875, 54.6875 );
		shape4.curveTo( 723.2992, 54.6875, 729.875, 57.34581, 729.875, 60.625 );
		shape4.closePath();
		g.setPaint( new Color( 0, 0, 0, 255 ) );
		g.fill( shape4 );
	}

	private void paintShapeNode_0_0_1_1_0_4_0( Graphics2D g ) {
		GeneralPath shape5 = new GeneralPath();
		shape5.moveTo( 718.0, 100.875 );
		shape5.curveTo( 709.16797, 100.875, 701.47284, 103.52466, 702.0, 106.6875 );
		shape5.lineTo( 702.5, 109.6875 );
		shape5.curveTo( 703.02716, 112.85034, 709.444, 115.5, 718.0, 115.5 );
		shape5.curveTo( 726.556, 115.5, 732.97284, 112.85034, 733.5, 109.6875 );
		shape5.lineTo( 734.0, 106.6875 );
		shape5.curveTo( 734.52716, 103.52466, 726.83203, 100.875, 718.0, 100.875 );
		shape5.closePath();
		g.setPaint( new LinearGradientPaint( new Point2D.Double( 703.6697998046875, 88.1875 ), new Point2D.Double( 732.3302001953125, 88.1875 ), new float[] { 0.0f, 0.19592221f, 0.326537f, 0.46741682f, 0.6147452f, 0.77435094f, 0.9032633f, 1.0f }, new Color[] { new Color( 81, 87, 85, 255 ), new Color( 255, 255, 255, 255 ), new Color( 255, 255, 255, 255 ), new Color( 174, 179, 176, 255 ), new Color( 148, 153, 150, 255 ), new Color( 240, 241, 241, 255 ), new Color( 237, 238, 238, 255 ), new Color( 90, 90, 90, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 20.0f ) ) );
		g.fill( shape5 );
		g.setPaint( new LinearGradientPaint( new Point2D.Double( 714.625, 108.20012664794922 ), new Point2D.Double( 714.625, 103.625 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 85, 87, 83, 255 ), new Color( 136, 138, 133, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) ) );
		g.setStroke( new BasicStroke( 0.99999994f, 1, 1, 4.0f, null, 0.0f ) );
		g.draw( shape5 );
	}

	private void paintShapeNode_0_0_1_1_0_4_1( Graphics2D g ) {
		GeneralPath shape6 = new GeneralPath();
		shape6.moveTo( 718.0, 101.25 );
		shape6.curveTo( 713.7954, 101.25, 709.86945, 101.89964, 707.07935, 102.9375 );
		shape6.curveTo( 705.6843, 103.45643, 704.5703, 104.07409, 703.86914, 104.71875 );
		shape6.curveTo( 703.16797, 105.36341, 702.8966, 105.98437, 702.9991, 106.625 );
		shape6.lineTo( 703.1491, 107.625 );
		shape6.curveTo( 703.29456, 107.32766, 703.53925, 107.02207, 703.86914, 106.71875 );
		shape6.curveTo( 704.5703, 106.07409, 705.6843, 105.45643, 707.07935, 104.9375 );
		shape6.curveTo( 709.8694, 103.89964, 713.7954, 103.25, 718.0, 103.25 );
		shape6.curveTo( 722.2046, 103.25, 726.13055, 103.89964, 728.92065, 104.9375 );
		shape6.curveTo( 730.3157, 105.45643, 731.4297, 106.07409, 732.13086, 106.71875 );
		shape6.curveTo( 732.46075, 107.02207, 732.70544, 107.32766, 732.8509, 107.625 );
		shape6.lineTo( 733.0009, 106.625 );
		shape6.curveTo( 733.10345, 105.98437, 732.83203, 105.36341, 732.13086, 104.71875 );
		shape6.curveTo( 731.4297, 104.07409, 730.3157, 103.45643, 728.92065, 102.9375 );
		shape6.curveTo( 726.1306, 101.89964, 722.2046, 101.25, 718.0, 101.25 );
		shape6.closePath();
		g.setPaint( new LinearGradientPaint( new Point2D.Double( 702.9782104492188, 104.4375 ), new Point2D.Double( 733.0217895507812, 104.4375 ), new float[] { 0.0f, 0.6353322f, 1.0f }, new Color[] { new Color( 186, 189, 182, 255 ), new Color( 238, 238, 236, 255 ), new Color( 186, 189, 182, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) ) );
		g.fill( shape6 );
	}

	private void paintCompositeGraphicsNode_0_0_1_1_0_4( Graphics2D g ) {
		// _0_0_1_1_0_4_0
		AffineTransform trans_0_0_1_1_0_4_0 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_1_1_0_4_0( g );
		g.setTransform( trans_0_0_1_1_0_4_0 );
		// _0_0_1_1_0_4_1
		AffineTransform trans_0_0_1_1_0_4_1 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_1_1_0_4_1( g );
		g.setTransform( trans_0_0_1_1_0_4_1 );
	}

	private void paintShapeNode_0_0_1_1_0_5( Graphics2D g ) {
		GeneralPath shape7 = new GeneralPath();
		shape7.moveTo( 729.875, 60.625 );
		shape7.curveTo( 729.875, 63.90419, 723.2992, 66.5625, 715.1875, 66.5625 );
		shape7.curveTo( 707.0758, 66.5625, 700.5, 63.90419, 700.5, 60.625 );
		shape7.curveTo( 700.5, 57.34581, 707.0758, 54.6875, 715.1875, 54.6875 );
		shape7.curveTo( 723.2992, 54.6875, 729.875, 57.34581, 729.875, 60.625 );
		shape7.closePath();
		g.setPaint( new RadialGradientPaint( new Point2D.Double( 718.778564453125, 58.22014617919922 ), 14.6875f, new Point2D.Double( 718.778564453125, 58.22014617919922 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 211, 215, 207, 255 ), new Color( 85, 87, 83, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 1.2169979810714722f, -1.6399266719818115f, 0.5263804197311401f, 0.7093070149421692f, -186.63723754882812f, 1192.598388671875f ) ) );
		g.fill( shape7 );
	}

	private void paintShapeNode_0_0_1_1_0_6_0( Graphics2D g ) {
		GeneralPath shape8 = new GeneralPath();
		shape8.moveTo( 688.0, 53.5 );
		shape8.curveTo( 678.34045, 53.5, 670.5, 55.964, 670.5, 59.0 );
		shape8.lineTo( 670.5, 62.5 );
		shape8.lineTo( 673.0, 86.5 );
		shape8.curveTo( 673.0, 89.81647, 679.72, 92.5, 688.0, 92.5 );
		shape8.lineTo( 688.0, 53.5 );
		shape8.closePath();
		g.setPaint( new LinearGradientPaint( new Point2D.Double( 701.375, 73.0 ), new Point2D.Double( 711.375, 71.875 ), new float[] { 0.0f, 0.29281864f, 1.0f }, new Color[] { new Color( 186, 189, 182, 255 ), new Color( 136, 138, 133, 255 ), new Color( 136, 138, 133, 0 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, -30.0f, 0.0f ) ) );
		g.fill( shape8 );
	}

	private void paintShapeNode_0_0_1_1_0_6_1( Graphics2D g ) {
		GeneralPath shape9 = new GeneralPath();
		shape9.moveTo( 688.0, 53.5 );
		shape9.lineTo( 688.0, 92.5 );
		shape9.curveTo( 696.28, 92.5, 703.0, 89.81647, 703.0, 86.5 );
		shape9.lineTo( 705.5, 62.5 );
		shape9.lineTo( 705.5, 59.0 );
		shape9.curveTo( 705.5, 55.964, 697.65955, 53.5, 688.0, 53.5 );
		shape9.closePath();
		g.setPaint( new LinearGradientPaint( new Point2D.Double( 701.375, 73.0 ), new Point2D.Double( 711.375, 71.875 ), new float[] { 0.0f, 0.26535374f, 1.0f }, new Color[] { new Color( 136, 138, 133, 255 ), new Color( 85, 87, 83, 255 ), new Color( 136, 138, 133, 0 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( -1.0f, 0.0f, 0.0f, 1.0f, 1406.0f, 0.0f ) ) );
		g.fill( shape9 );
	}

	private void paintShapeNode_0_0_1_1_0_6_2( Graphics2D g ) {
		GeneralPath shape10 = new GeneralPath();
		shape10.moveTo( 688.0, 53.5 );
		shape10.curveTo( 678.34045, 53.5, 670.5, 55.964, 670.5, 59.0 );
		shape10.lineTo( 670.5, 62.5 );
		shape10.lineTo( 673.0, 86.5 );
		shape10.curveTo( 673.0, 89.81647, 679.72, 92.5, 688.0, 92.5 );
		shape10.curveTo( 696.28, 92.5, 703.0, 89.81647, 703.0, 86.5 );
		shape10.lineTo( 705.5, 62.5 );
		shape10.lineTo( 705.5, 59.0 );
		shape10.curveTo( 705.5, 55.964, 697.6596, 53.5, 688.0, 53.5 );
		shape10.closePath();
		g.setPaint( new LinearGradientPaint( new Point2D.Double( 717.3125, 86.96126556396484 ), new Point2D.Double( 717.3125, 89.6151123046875 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 46, 52, 54, 255 ), new Color( 238, 238, 236, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, -30.0f, 0.0f ) ) );
		g.setStroke( new BasicStroke( 1.0f, 0, 0, 4.0f, null, 0.0f ) );
		g.draw( shape10 );
	}

	private void paintShapeNode_0_0_1_1_0_6_3( Graphics2D g ) {
		GeneralPath shape11 = new GeneralPath();
		shape11.moveTo( 673.65625, 66.34375 );
		shape11.lineTo( 676.6244, 88.75 );
		shape11.curveTo( 676.6244, 89.27971, 676.8295, 89.79167, 677.218, 90.28125 );
		shape11.curveTo( 679.43005, 91.08921, 682.3989, 91.7654, 685.875, 92.21875 );
		shape11.curveTo( 685.8408, 92.01217, 685.8255, 91.806816, 685.8255, 91.59375 );
		shape11.lineTo( 684.292, 68.25 );
		shape11.curveTo( 680.1154, 67.81158, 676.466, 67.144646, 673.65625, 66.34375 );
		shape11.closePath();
		g.setPaint( new RadialGradientPaint( new Point2D.Double( 709.765625, 79.28125 ), 6.109375f, new Point2D.Double( 709.765625, 79.28125 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 255, 255, 255, 255 ), new Color( 255, 255, 255, 0 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 0.7793732285499573f, -0.05980715900659561f, 0.4712935984134674f, 6.141634941101074f, 89.2285385131836f, -365.1861572265625f ) ) );
		g.fill( shape11 );
	}

	private void paintShapeNode_0_0_1_1_0_6_4( Graphics2D g ) {
		GeneralPath shape12 = new GeneralPath();
		shape12.moveTo( 702.875, 66.34375 );
		shape12.lineTo( 699.90686, 88.75 );
		shape12.curveTo( 699.90686, 89.27971, 699.7017, 89.79167, 699.31323, 90.28125 );
		shape12.curveTo( 697.1012, 91.08921, 694.1323, 91.7654, 690.65625, 92.21875 );
		shape12.curveTo( 690.6904, 92.01217, 690.70575, 91.806816, 690.70575, 91.59375 );
		shape12.lineTo( 692.23926, 68.25 );
		shape12.curveTo( 696.41583, 67.81158, 700.06525, 67.144646, 702.875, 66.34375 );
		shape12.closePath();
		g.setPaint( new RadialGradientPaint( new Point2D.Double( 711.0411987304688, 79.29367065429688 ), 6.109375f, new Point2D.Double( 711.0411987304688, 79.29367065429688 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 255, 255, 255, 255 ), new Color( 255, 255, 255, 0 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( -0.7793732285499573f, -0.05980715900659561f, -0.4712935984134674f, 6.141634941101074f, 1287.302734375f, -365.1861572265625f ) ) );
		g.fill( shape12 );
	}

	private void paintCompositeGraphicsNode_0_0_1_1_0_6( Graphics2D g ) {
		// _0_0_1_1_0_6_0
		AffineTransform trans_0_0_1_1_0_6_0 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_1_1_0_6_0( g );
		g.setTransform( trans_0_0_1_1_0_6_0 );
		// _0_0_1_1_0_6_1
		AffineTransform trans_0_0_1_1_0_6_1 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_1_1_0_6_1( g );
		g.setTransform( trans_0_0_1_1_0_6_1 );
		// _0_0_1_1_0_6_2
		AffineTransform trans_0_0_1_1_0_6_2 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_1_1_0_6_2( g );
		g.setTransform( trans_0_0_1_1_0_6_2 );
		// _0_0_1_1_0_6_3
		AffineTransform trans_0_0_1_1_0_6_3 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_1_1_0_6_3( g );
		g.setTransform( trans_0_0_1_1_0_6_3 );
		// _0_0_1_1_0_6_4
		g.setComposite( AlphaComposite.getInstance( 3, 0.4f * origAlpha ) );
		AffineTransform trans_0_0_1_1_0_6_4 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_1_1_0_6_4( g );
		g.setTransform( trans_0_0_1_1_0_6_4 );
	}

	private void paintShapeNode_0_0_1_1_0_7( Graphics2D g ) {
		GeneralPath shape13 = new GeneralPath();
		shape13.moveTo( 720.0, 55.5 );
		shape13.curveTo( 712.24286, 55.5, 705.6225, 57.182053, 702.875, 59.5625 );
		shape13.lineTo( 702.875, 62.25 );
		shape13.lineTo( 703.53125, 62.25 );
		shape13.curveTo( 703.51465, 62.16703, 703.46875, 62.084084, 703.46875, 62.0 );
		shape13.curveTo( 703.46875, 59.501564, 710.8776, 57.46875, 720.0, 57.46875 );
		shape13.curveTo( 729.12244, 57.468746, 736.53125, 59.501564, 736.53125, 62.0 );
		shape13.curveTo( 736.53125, 62.08408, 736.48535, 62.16703, 736.46875, 62.25 );
		shape13.lineTo( 737.25, 62.25 );
		shape13.lineTo( 737.25, 59.6875 );
		shape13.curveTo( 734.59076, 57.243168, 727.88794, 55.5, 720.0, 55.5 );
		shape13.closePath();
		g.setPaint( new Color( 0, 0, 0, 255 ) );
		g.fill( shape13 );
		g.setStroke( new BasicStroke( 0.9f, 1, 1, 4.0f, null, 0.0f ) );
		g.draw( shape13 );
	}

	private void paintShapeNode_0_0_1_1_0_8( Graphics2D g ) {
		GeneralPath shape14 = new GeneralPath();
		shape14.moveTo( 703.6875, 65.15625 );
		shape14.lineTo( 703.90625, 67.1875 );
		shape14.curveTo( 707.0876, 69.155106, 713.1033, 70.5, 720.0, 70.5 );
		shape14.curveTo( 726.8967, 70.5, 732.9124, 69.155106, 736.09375, 67.1875 );
		shape14.lineTo( 736.3125, 65.15625 );
		shape14.lineTo( 703.6875, 65.15625 );
		shape14.closePath();
		g.fill( shape14 );
		g.draw( shape14 );
	}

	private void paintShapeNode_0_0_1_1_0_9( Graphics2D g ) {
		GeneralPath shape15 = new GeneralPath();
		shape15.moveTo( 778.0, 53.5 );
		shape15.curveTo( 787.65955, 53.5, 795.5, 55.964, 795.5, 59.0 );
		shape15.lineTo( 795.5, 62.5 );
		shape15.curveTo( 784.2893, 65.46398, 772.65326, 65.66294, 760.5, 62.5 );
		shape15.lineTo( 760.5, 59.0 );
		shape15.curveTo( 760.5, 55.964, 768.3404, 53.5, 778.0, 53.5 );
		shape15.closePath();
		g.setPaint( new LinearGradientPaint( new Point2D.Double( 734.1218872070312, 72.8232192993164 ), new Point2D.Double( 700.4962768554688, 72.8232192993164 ), new float[] { 0.0f, 0.25f, 0.5f, 0.75f, 1.0f }, new Color[] { new Color( 11, 11, 11, 255 ), new Color( 186, 189, 182, 153 ), new Color( 197, 197, 197, 255 ), new Color( 211, 215, 207, 51 ), new Color( 136, 138, 133, 0 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( -1.0f, 0.0f, 0.0f, 1.0f, 1496.0f, 0.0f ) ) );
		g.fill( shape15 );
	}

	private void paintShapeNode_0_0_1_1_0_10( Graphics2D g ) {
		GeneralPath shape16 = new GeneralPath();
		shape16.moveTo( 729.875, 60.625 );
		shape16.curveTo( 729.875, 63.90419, 723.2992, 66.5625, 715.1875, 66.5625 );
		shape16.curveTo( 707.0758, 66.5625, 700.5, 63.90419, 700.5, 60.625 );
		shape16.curveTo( 700.5, 57.34581, 707.0758, 54.6875, 715.1875, 54.6875 );
		shape16.curveTo( 723.2992, 54.6875, 729.875, 57.34581, 729.875, 60.625 );
		shape16.closePath();
		g.setPaint( new RadialGradientPaint( new Point2D.Double( 717.30126953125, 48.93881607055664 ), 16.05958f, new Point2D.Double( 717.30126953125, 48.93881607055664 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 201, 202, 200, 255 ), new Color( 86, 87, 84, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 1.0f, 0.0f, 0.0f, 0.45515385270118713f, 0.0f, 33.03129577636719f ) ) );
		g.setStroke( new BasicStroke( 2.7441592f, 1, 1, 4.0f, null, 0.0f ) );
		g.draw( shape16 );
	}

	private void paintShapeNode_0_0_1_1_0_11( Graphics2D g ) {
		GeneralPath shape17 = new GeneralPath();
		shape17.moveTo( 720.0, 53.5 );
		shape17.curveTo( 709.788, 53.5, 701.5, 56.412003, 701.5, 60.0 );
		shape17.lineTo( 701.5, 63.0 );
		shape17.curveTo( 701.5, 66.588, 709.788, 69.5, 720.0, 69.5 );
		shape17.curveTo( 730.212, 69.5, 738.5, 66.588, 738.5, 63.0 );
		shape17.lineTo( 738.5, 60.0 );
		shape17.curveTo( 738.5, 56.412, 730.212, 53.5, 720.0, 53.5 );
		shape17.closePath();
		shape17.moveTo( 720.0, 55.46875 );
		shape17.curveTo( 729.12244, 55.468746, 736.53125, 57.501564, 736.53125, 60.0 );
		shape17.curveTo( 736.53125, 62.498436, 729.1224, 64.53125, 720.0, 64.53125 );
		shape17.curveTo( 710.8776, 64.53125, 703.46875, 62.498436, 703.46875, 60.0 );
		shape17.curveTo( 703.46875, 57.501564, 710.8776, 55.46875, 720.0, 55.46875 );
		shape17.closePath();
		g.setPaint( new LinearGradientPaint( new Point2D.Double( 700.99267578125, 60.5 ), new Point2D.Double( 735.00732421875, 60.5 ), new float[] { 0.0f, 0.19592221f, 0.326537f, 0.46741682f, 0.6147452f, 0.77435094f, 0.9032633f, 1.0f }, new Color[] { new Color( 81, 87, 85, 255 ), new Color( 255, 255, 255, 255 ), new Color( 255, 255, 255, 255 ), new Color( 174, 179, 176, 255 ), new Color( 148, 153, 150, 255 ), new Color( 240, 241, 241, 255 ), new Color( 237, 238, 238, 255 ), new Color( 90, 90, 90, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 2.0f, 1.0f ) ) );
		g.fill( shape17 );
		g.setPaint( new LinearGradientPaint( new Point2D.Double( 728.4083862304688, 64.52535247802734 ), new Point2D.Double( 728.4083862304688, 71.11156463623047 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 46, 52, 54, 255 ), new Color( 186, 189, 182, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 2.0f, 1.0f ) ) );
		g.setStroke( new BasicStroke( 0.9f, 1, 1, 4.0f, null, 0.0f ) );
		g.draw( shape17 );
	}

	private void paintShapeNode_0_0_1_1_0_12( Graphics2D g ) {
		GeneralPath shape18 = new GeneralPath();
		shape18.moveTo( 720.0, 54.5 );
		shape18.curveTo( 714.9884, 54.5, 710.4495, 55.21959, 707.25, 56.34375 );
		shape18.curveTo( 705.6502, 56.90583, 704.39355, 57.585976, 703.59375, 58.25 );
		shape18.curveTo( 702.79395, 58.914024, 702.5, 59.48539, 702.5, 60.0 );
		shape18.lineTo( 702.5, 63.0 );
		shape18.curveTo( 702.5, 63.421837, 702.7459, 63.935043, 703.375, 64.53125 );
		shape18.curveTo( 703.5514, 64.69486, 703.6625, 64.91695, 703.6875, 65.15625 );
		shape18.lineTo( 705.8125, 84.59375 );
		shape18.curveTo( 705.8373, 84.88274, 705.7346, 85.16814, 705.53125, 85.375 );
		shape18.curveTo( 705.0467, 85.87041, 704.9166, 86.21856, 704.96875, 86.53125 );
		shape18.lineTo( 705.46875, 89.53125 );
		shape18.curveTo( 705.63226, 90.512375, 707.00775, 91.84529, 709.59375, 92.84375 );
		shape18.curveTo( 712.17975, 93.84221, 715.8245, 94.5, 720.0, 94.5 );
		shape18.curveTo( 724.1755, 94.5, 727.82025, 93.84221, 730.40625, 92.84375 );
		shape18.curveTo( 732.99225, 91.84529, 734.36774, 90.512375, 734.53125, 89.53125 );
		shape18.lineTo( 735.03125, 86.53125 );
		shape18.curveTo( 735.0834, 86.21856, 734.95337, 85.87041, 734.46875, 85.375 );
		shape18.curveTo( 734.26544, 85.16814, 734.1627, 84.88274, 734.1875, 84.59375 );
		shape18.lineTo( 736.3125, 65.15625 );
		shape18.curveTo( 736.3375, 64.91695, 736.4486, 64.69486, 736.625, 64.53125 );
		shape18.curveTo( 737.2541, 63.935043, 737.5, 63.421852, 737.5, 63.0 );
		shape18.lineTo( 737.5, 60.0 );
		shape18.curveTo( 737.5, 59.48539, 737.20605, 58.914024, 736.40625, 58.25 );
		shape18.curveTo( 735.60645, 57.585976, 734.3498, 56.90583, 732.75, 56.34375 );
		shape18.curveTo( 729.5505, 55.21959, 725.0116, 54.5, 720.0, 54.5 );
		shape18.closePath();
		g.setPaint( new LinearGradientPaint( new Point2D.Double( 704.625, 62.875 ), new Point2D.Double( 736.0, 97.625 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 255, 255, 255, 255 ), new Color( 255, 255, 255, 0 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 2.0f, 0.0f ) ) );
		g.setStroke( new BasicStroke( 0.99999994f, 1, 1, 4.0f, null, 0.0f ) );
		g.draw( shape18 );
	}

	private void paintShapeNode_0_0_1_1_0_13( Graphics2D g ) {
		GeneralPath shape19 = new GeneralPath();
		shape19.moveTo( 729.875, 60.625 );
		shape19.curveTo( 729.875, 63.90419, 723.2992, 66.5625, 715.1875, 66.5625 );
		shape19.curveTo( 707.0758, 66.5625, 700.5, 63.90419, 700.5, 60.625 );
		shape19.curveTo( 700.5, 57.34581, 707.0758, 54.6875, 715.1875, 54.6875 );
		shape19.curveTo( 723.2992, 54.6875, 729.875, 57.34581, 729.875, 60.625 );
		shape19.closePath();
		g.setPaint( new RadialGradientPaint( new Point2D.Double( 712.1088256835938, 64.05938720703125 ), 15.137135f, new Point2D.Double( 712.1088256835938, 64.05938720703125 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 255, 255, 255, 255 ), new Color( 211, 215, 207, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 0.429599791765213f, 1.1141945123672485f, -3.6476752758026123f, 2.3118515014648438f, 635.1559448242188f, -875.8652954101562f ) ) );
		g.setStroke( new BasicStroke( 0.8992707f, 1, 1, 4.0f, null, 0.0f ) );
		g.draw( shape19 );
	}

	private void paintShapeNode_0_0_1_1_0_14( Graphics2D g ) {
		GeneralPath shape20 = new GeneralPath();
		shape20.moveTo( 708.1875, 61.8125 );
		shape20.curveTo( 708.1875, 63.22773, 707.0402, 64.375, 705.625, 64.375 );
		shape20.curveTo( 704.2098, 64.375, 703.0625, 63.22773, 703.0625, 61.8125 );
		shape20.curveTo( 703.0625, 60.39727, 704.2098, 59.25, 705.625, 59.25 );
		shape20.curveTo( 707.0402, 59.25, 708.1875, 60.39727, 708.1875, 61.8125 );
		shape20.closePath();
		g.setPaint( new RadialGradientPaint( new Point2D.Double( 705.625, 61.8125 ), 2.5625f, new Point2D.Double( 705.625, 61.8125 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 255, 255, 255, 255 ), new Color( 255, 255, 255, 0 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) ) );
		g.fill( shape20 );
	}

	private void paintShapeNode_0_0_1_1_0_15( Graphics2D g ) {
		GeneralPath shape21 = new GeneralPath();
		shape21.moveTo( 708.1875, 61.8125 );
		shape21.curveTo( 708.1875, 63.22773, 707.0402, 64.375, 705.625, 64.375 );
		shape21.curveTo( 704.2098, 64.375, 703.0625, 63.22773, 703.0625, 61.8125 );
		shape21.curveTo( 703.0625, 60.39727, 704.2098, 59.25, 705.625, 59.25 );
		shape21.curveTo( 707.0402, 59.25, 708.1875, 60.39727, 708.1875, 61.8125 );
		shape21.closePath();
		g.setPaint( new Color( 255, 255, 255, 255 ) );
		g.fill( shape21 );
	}

	private void paintShapeNode_0_0_1_1_0_16( Graphics2D g ) {
		GeneralPath shape22 = new GeneralPath();
		shape22.moveTo( 708.1875, 61.8125 );
		shape22.curveTo( 708.1875, 63.22773, 707.0402, 64.375, 705.625, 64.375 );
		shape22.curveTo( 704.2098, 64.375, 703.0625, 63.22773, 703.0625, 61.8125 );
		shape22.curveTo( 703.0625, 60.39727, 704.2098, 59.25, 705.625, 59.25 );
		shape22.curveTo( 707.0402, 59.25, 708.1875, 60.39727, 708.1875, 61.8125 );
		shape22.closePath();
		g.setPaint( new RadialGradientPaint( new Point2D.Double( 705.625, 61.8125 ), 2.5625f, new Point2D.Double( 705.625, 61.8125 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 255, 255, 255, 255 ), new Color( 255, 255, 255, 0 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) ) );
		g.fill( shape22 );
	}

	private void paintShapeNode_0_0_1_1_0_17( Graphics2D g ) {
		GeneralPath shape23 = new GeneralPath();
		shape23.moveTo( 708.1875, 61.8125 );
		shape23.curveTo( 708.1875, 63.22773, 707.0402, 64.375, 705.625, 64.375 );
		shape23.curveTo( 704.2098, 64.375, 703.0625, 63.22773, 703.0625, 61.8125 );
		shape23.curveTo( 703.0625, 60.39727, 704.2098, 59.25, 705.625, 59.25 );
		shape23.curveTo( 707.0402, 59.25, 708.1875, 60.39727, 708.1875, 61.8125 );
		shape23.closePath();
		g.setPaint( new Color( 255, 255, 255, 255 ) );
		g.fill( shape23 );
	}

	private void paintShapeNode_0_0_1_1_0_18( Graphics2D g ) {
		GeneralPath shape24 = new GeneralPath();
		shape24.moveTo( 708.1875, 61.8125 );
		shape24.curveTo( 708.1875, 63.22773, 707.0402, 64.375, 705.625, 64.375 );
		shape24.curveTo( 704.2098, 64.375, 703.0625, 63.22773, 703.0625, 61.8125 );
		shape24.curveTo( 703.0625, 60.39727, 704.2098, 59.25, 705.625, 59.25 );
		shape24.curveTo( 707.0402, 59.25, 708.1875, 60.39727, 708.1875, 61.8125 );
		shape24.closePath();
		g.fill( shape24 );
	}

	private void paintShapeNode_0_0_1_1_0_19( Graphics2D g ) {
		GeneralPath shape25 = new GeneralPath();
		shape25.moveTo( 703.8125, 70.625 );
		shape25.lineTo( 705.46875, 86.4375 );
		shape25.curveTo( 705.4697, 86.45831, 705.4697, 86.47919, 705.46875, 86.5 );
		shape25.curveTo( 705.46875, 87.1672, 705.804, 87.841965, 706.5, 88.5 );
		shape25.curveTo( 707.196, 89.158035, 708.244, 89.785385, 709.5625, 90.3125 );
		shape25.curveTo( 712.1994, 91.36673, 715.9118, 92.03125, 720.0, 92.03125 );
		shape25.curveTo( 724.0882, 92.03125, 727.8006, 91.36673, 730.4375, 90.3125 );
		shape25.curveTo( 731.756, 89.785385, 732.804, 89.158035, 733.5, 88.5 );
		shape25.curveTo( 734.196, 87.841965, 734.53125, 87.1672, 734.53125, 86.5 );
		shape25.curveTo( 734.5303, 86.47919, 734.5303, 86.45831, 734.53125, 86.4375 );
		shape25.lineTo( 736.1875, 70.625 );
		shape25.lineTo( 703.8125, 70.625 );
		shape25.closePath();
		g.setPaint( new LinearGradientPaint( new Point2D.Double( 688.0, 92.03125 ), new Point2D.Double( 688.0, 73.0 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 238, 238, 236, 255 ), new Color( 186, 189, 182, 0 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 32.0f, 0.0f ) ) );
		g.fill( shape25 );
	}

	private void paintShapeNode_0_0_1_1_0_20_0( Graphics2D g ) {
		GeneralPath shape26 = new GeneralPath();
		shape26.moveTo( 762.5, 59.1875 );
		shape26.lineTo( 762.5, 61.5 );
		shape26.lineTo( 765.0, 85.5 );
		shape26.curveTo( 765.0, 88.81647, 771.72, 91.5, 780.0, 91.5 );
		shape26.curveTo( 788.28, 91.5, 795.0, 88.81647, 795.0, 85.5 );
		shape26.lineTo( 797.5, 61.5 );
		shape26.lineTo( 797.5, 59.1875 );
		shape26.curveTo( 797.1822, 62.150127, 789.4729, 64.53125, 780.0, 64.53125 );
		shape26.curveTo( 770.5271, 64.53125, 762.81775, 62.150127, 762.5, 59.1875 );
		shape26.closePath();
		g.setPaint( new RadialGradientPaint( new Point2D.Double( 767.9999389648438, 78.99209594726562 ), 17.5f, new Point2D.Double( 767.9999389648438, 78.99209594726562 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 255, 255, 255, 255 ), new Color( 255, 255, 255, 76 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 0.9001134037971497f, 0.0f, 0.0f, 3.4375f, 88.71292877197266f, -185.869140625f ) ) );
		g.fill( shape26 );
	}

	private void paintShapeNode_0_0_1_1_0_20_1( Graphics2D g ) {
		GeneralPath shape27 = new GeneralPath();
		shape27.moveTo( 751.25, 65.8125 );
		shape27.lineTo( 753.5, 86.5 );
		shape27.curveTo( 753.5, 89.81647, 759.72, 92.0, 768.0, 92.0 );
		shape27.curveTo( 776.28, 92.0, 782.625, 89.81647, 782.625, 86.5 );
		shape27.lineTo( 784.625, 65.8125 );
		shape27.curveTo( 776.68756, 70.67296, 759.64923, 70.800766, 751.25, 65.8125 );
		shape27.closePath();
	}

	private void paintCompositeGraphicsNode_0_0_1_1_0_20( Graphics2D g ) {
		// _0_0_1_1_0_20_0
		g.setComposite( AlphaComposite.getInstance( 3, 1.0f * origAlpha ) );
		AffineTransform trans_0_0_1_1_0_20_0 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_1_1_0_20_0( g );
		g.setTransform( trans_0_0_1_1_0_20_0 );
		// _0_0_1_1_0_20_1
		AffineTransform trans_0_0_1_1_0_20_1 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 12.0f, 0.0f ) );
		paintShapeNode_0_0_1_1_0_20_1( g );
		g.setTransform( trans_0_0_1_1_0_20_1 );
	}

	private void paintCompositeGraphicsNode_0_0_1_1_0( Graphics2D g ) {
		// _0_0_1_1_0_0
		g.setComposite( AlphaComposite.getInstance( 3, 0.4f * origAlpha ) );
		AffineTransform trans_0_0_1_1_0_0 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, -58.0f, -1.0f ) );
		paintShapeNode_0_0_1_1_0_0( g );
		g.setTransform( trans_0_0_1_1_0_0 );
		// _0_0_1_1_0_1
		g.setComposite( AlphaComposite.getInstance( 3, 1.0f * origAlpha ) );
		AffineTransform trans_0_0_1_1_0_1 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 2.0f, 0.0f ) );
		paintCompositeGraphicsNode_0_0_1_1_0_1( g );
		g.setTransform( trans_0_0_1_1_0_1 );
		// _0_0_1_1_0_2
		g.setComposite( AlphaComposite.getInstance( 3, 1.0f * origAlpha ) );
		AffineTransform trans_0_0_1_1_0_2 = g.getTransform();
		g.transform( new AffineTransform( 1.3276629447937012f, 0.0f, 0.0f, 1.2711591720581055f, -229.52792358398438f, 13.388467788696289f ) );
		paintShapeNode_0_0_1_1_0_2( g );
		g.setTransform( trans_0_0_1_1_0_2 );
		// _0_0_1_1_0_3
		g.setComposite( AlphaComposite.getInstance( 3, 0.3f * origAlpha ) );
		AffineTransform trans_0_0_1_1_0_3 = g.getTransform();
		g.transform( new AffineTransform( 1.1362465620040894f, 0.0f, 0.0f, 1.0947370529174805f, -92.62933349609375f, 24.13157081604004f ) );
		paintShapeNode_0_0_1_1_0_3( g );
		g.setTransform( trans_0_0_1_1_0_3 );
		// _0_0_1_1_0_4
		g.setComposite( AlphaComposite.getInstance( 3, 1.0f * origAlpha ) );
		AffineTransform trans_0_0_1_1_0_4 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 2.0f, -20.0f ) );
		paintCompositeGraphicsNode_0_0_1_1_0_4( g );
		g.setTransform( trans_0_0_1_1_0_4 );
		// _0_0_1_1_0_5
		AffineTransform trans_0_0_1_1_0_5 = g.getTransform();
		g.transform( new AffineTransform( 1.0212769508361816f, 0.0f, 0.0f, 0.7578948736190796f, -10.404540061950684f, 41.5526237487793f ) );
		paintShapeNode_0_0_1_1_0_5( g );
		g.setTransform( trans_0_0_1_1_0_5 );
		// _0_0_1_1_0_6
		AffineTransform trans_0_0_1_1_0_6 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 32.0f, 0.0f ) );
		paintCompositeGraphicsNode_0_0_1_1_0_6( g );
		g.setTransform( trans_0_0_1_1_0_6 );
		// _0_0_1_1_0_7
		g.setComposite( AlphaComposite.getInstance( 3, 0.2f * origAlpha ) );
		AffineTransform trans_0_0_1_1_0_7 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_1_1_0_7( g );
		g.setTransform( trans_0_0_1_1_0_7 );
		// _0_0_1_1_0_8
		g.setComposite( AlphaComposite.getInstance( 3, 0.1f * origAlpha ) );
		AffineTransform trans_0_0_1_1_0_8 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_1_1_0_8( g );
		g.setTransform( trans_0_0_1_1_0_8 );
		// _0_0_1_1_0_9
		g.setComposite( AlphaComposite.getInstance( 3, 1.0f * origAlpha ) );
		AffineTransform trans_0_0_1_1_0_9 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, -58.0f, 0.0f ) );
		paintShapeNode_0_0_1_1_0_9( g );
		g.setTransform( trans_0_0_1_1_0_9 );
		// _0_0_1_1_0_10
		AffineTransform trans_0_0_1_1_0_10 = g.getTransform();
		g.transform( new AffineTransform( 1.1827048063278198f, 0.0f, 0.0f, 1.0105259418487549f, -125.85575103759766f, 0.9868555665016174f ) );
		paintShapeNode_0_0_1_1_0_10( g );
		g.setTransform( trans_0_0_1_1_0_10 );
		// _0_0_1_1_0_11
		AffineTransform trans_0_0_1_1_0_11 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_1_1_0_11( g );
		g.setTransform( trans_0_0_1_1_0_11 );
		// _0_0_1_1_0_12
		g.setComposite( AlphaComposite.getInstance( 3, 0.4f * origAlpha ) );
		AffineTransform trans_0_0_1_1_0_12 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_1_1_0_12( g );
		g.setTransform( trans_0_0_1_1_0_12 );
		// _0_0_1_1_0_13
		g.setComposite( AlphaComposite.getInstance( 3, 1.0f * origAlpha ) );
		AffineTransform trans_0_0_1_1_0_13 = g.getTransform();
		g.transform( new AffineTransform( 1.1932722330093384f, 0.0f, 0.0f, 0.9307202100753784f, -133.4133758544922f, 3.575089931488037f ) );
		paintShapeNode_0_0_1_1_0_13( g );
		g.setTransform( trans_0_0_1_1_0_13 );
		// _0_0_1_1_0_14
		AffineTransform trans_0_0_1_1_0_14 = g.getTransform();
		g.transform( new AffineTransform( 0.8536584973335266f, 0.0f, 0.0f, 0.8536584973335266f, 108.97374725341797f, 12.123395919799805f ) );
		paintShapeNode_0_0_1_1_0_14( g );
		g.setTransform( trans_0_0_1_1_0_14 );
		// _0_0_1_1_0_15
		AffineTransform trans_0_0_1_1_0_15 = g.getTransform();
		g.transform( new AffineTransform( 0.41044938564300537f, 0.0f, 0.0f, 0.41044938564300537f, 421.7131652832031f, 39.519256591796875f ) );
		paintShapeNode_0_0_1_1_0_15( g );
		g.setTransform( trans_0_0_1_1_0_15 );
		// _0_0_1_1_0_16
		AffineTransform trans_0_0_1_1_0_16 = g.getTransform();
		g.transform( new AffineTransform( 0.8536584973335266f, 0.0f, 0.0f, 0.8536584973335266f, 109.15052795410156f, 38.7005615234375f ) );
		paintShapeNode_0_0_1_1_0_16( g );
		g.setTransform( trans_0_0_1_1_0_16 );
		// _0_0_1_1_0_17
		AffineTransform trans_0_0_1_1_0_17 = g.getTransform();
		g.transform( new AffineTransform( 0.41044938564300537f, 0.0f, 0.0f, 0.41044938564300537f, 421.88995361328125f, 66.09642028808594f ) );
		paintShapeNode_0_0_1_1_0_17( g );
		g.setTransform( trans_0_0_1_1_0_17 );
		// _0_0_1_1_0_18
		AffineTransform trans_0_0_1_1_0_18 = g.getTransform();
		g.transform( new AffineTransform( 0.31288841366767883f, 0.0f, 0.0f, 0.31288841366767883f, 508.5306091308594f, 36.097084045410156f ) );
		paintShapeNode_0_0_1_1_0_18( g );
		g.setTransform( trans_0_0_1_1_0_18 );
		// _0_0_1_1_0_19
		g.setComposite( AlphaComposite.getInstance( 3, 0.7f * origAlpha ) );
		AffineTransform trans_0_0_1_1_0_19 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_1_1_0_19( g );
		g.setTransform( trans_0_0_1_1_0_19 );
		// _0_0_1_1_0_20
		g.setComposite( AlphaComposite.getInstance( 3, 0.19902912f * origAlpha ) );
		AffineTransform trans_0_0_1_1_0_20 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, -59.993717193603516f, 0.08838800340890884f ) );
		paintCompositeGraphicsNode_0_0_1_1_0_20( g );
		g.setTransform( trans_0_0_1_1_0_20 );
	}

	private void paintCompositeGraphicsNode_0_0_1_1( Graphics2D g ) {
		// _0_0_1_1_0
		AffineTransform trans_0_0_1_1_0 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintCompositeGraphicsNode_0_0_1_1_0( g );
		g.setTransform( trans_0_0_1_1_0 );
	}

	private void paintCompositeGraphicsNode_0_0_1_2_0( Graphics2D g ) {
	}

	private void paintCompositeGraphicsNode_0_0_1_2( Graphics2D g ) {
		// _0_0_1_2_0
		AffineTransform trans_0_0_1_2_0 = g.getTransform();
		g.transform( new AffineTransform( 0.8779329061508179f, 0.0f, 0.0f, 0.8955637812614441f, 1146.8990478515625f, 30.009098052978516f ) );
		paintCompositeGraphicsNode_0_0_1_2_0( g );
		g.setTransform( trans_0_0_1_2_0 );
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
		// _0_0_1_2
		AffineTransform trans_0_0_1_2 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintCompositeGraphicsNode_0_0_1_2( g );
		g.setTransform( trans_0_0_1_2 );
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
		return 40;
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
	public TrashEmptyNoPattern() {
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
