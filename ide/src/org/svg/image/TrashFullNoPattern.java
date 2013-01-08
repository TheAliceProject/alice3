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
public class TrashFullNoPattern implements Icon {

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

	private void paintShapeNode_0_0_0_0_0_0( Graphics2D g ) {
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

	private void paintShapeNode_0_0_0_0_0_1_0( Graphics2D g ) {
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

	private void paintShapeNode_0_0_0_0_0_1_1( Graphics2D g ) {
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

	private void paintCompositeGraphicsNode_0_0_0_0_0_1( Graphics2D g ) {
		// _0_0_0_0_0_1_0
		g.setComposite( AlphaComposite.getInstance( 3, 0.2f * origAlpha ) );
		AffineTransform trans_0_0_0_0_0_1_0 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_1_0( g );
		g.setTransform( trans_0_0_0_0_0_1_0 );
		// _0_0_0_0_0_1_1
		g.setComposite( AlphaComposite.getInstance( 3, 0.5f * origAlpha ) );
		AffineTransform trans_0_0_0_0_0_1_1 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_1_1( g );
		g.setTransform( trans_0_0_0_0_0_1_1 );
	}

	private void paintShapeNode_0_0_0_0_0_2( Graphics2D g ) {
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

	private void paintShapeNode_0_0_0_0_0_3( Graphics2D g ) {
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

	private void paintShapeNode_0_0_0_0_0_4( Graphics2D g ) {
		GeneralPath shape5 = new GeneralPath();
		shape5.moveTo( 778.0, 53.5 );
		shape5.curveTo( 787.65955, 53.5, 795.5, 55.964, 795.5, 59.0 );
		shape5.lineTo( 795.5, 62.5 );
		shape5.lineTo( 793.0, 86.5 );
		shape5.curveTo( 793.0, 89.81647, 786.28, 92.5, 778.0, 92.5 );
		shape5.curveTo( 769.72, 92.5, 763.0, 89.81647, 763.0, 86.5 );
		shape5.lineTo( 760.5, 62.5 );
		shape5.lineTo( 760.5, 59.0 );
		shape5.curveTo( 760.5, 55.964, 768.3404, 53.5, 778.0, 53.5 );
		shape5.closePath();
		g.setPaint( new LinearGradientPaint( new Point2D.Double( 734.1218872070312, 72.8232192993164 ), new Point2D.Double( 700.4962768554688, 72.8232192993164 ), new float[] { 0.0f, 0.25f, 0.5f, 0.75f, 1.0f }, new Color[] { new Color( 137, 139, 134, 255 ), new Color( 186, 189, 182, 153 ), new Color( 0, 0, 0, 127 ), new Color( 211, 215, 207, 51 ), new Color( 136, 138, 133, 0 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( -1.0f, 0.0f, 0.0f, 1.0f, 1496.0f, 0.0f ) ) );
		g.fill( shape5 );
	}

	private void paintShapeNode_0_0_0_0_0_5_0( Graphics2D g ) {
		GeneralPath shape6 = new GeneralPath();
		shape6.moveTo( 718.0, 100.875 );
		shape6.curveTo( 709.16797, 100.875, 701.47284, 103.52466, 702.0, 106.6875 );
		shape6.lineTo( 702.5, 109.6875 );
		shape6.curveTo( 703.02716, 112.85034, 709.444, 115.5, 718.0, 115.5 );
		shape6.curveTo( 726.556, 115.5, 732.97284, 112.85034, 733.5, 109.6875 );
		shape6.lineTo( 734.0, 106.6875 );
		shape6.curveTo( 734.52716, 103.52466, 726.83203, 100.875, 718.0, 100.875 );
		shape6.closePath();
		g.setPaint( new LinearGradientPaint( new Point2D.Double( 703.6697998046875, 88.1875 ), new Point2D.Double( 732.3302001953125, 88.1875 ), new float[] { 0.0f, 0.19592221f, 0.326537f, 0.46741682f, 0.6147452f, 0.77435094f, 0.9032633f, 1.0f }, new Color[] { new Color( 81, 87, 85, 255 ), new Color( 255, 255, 255, 255 ), new Color( 255, 255, 255, 255 ), new Color( 174, 179, 176, 255 ), new Color( 148, 153, 150, 255 ), new Color( 240, 241, 241, 255 ), new Color( 237, 238, 238, 255 ), new Color( 90, 90, 90, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 20.0f ) ) );
		g.fill( shape6 );
		g.setPaint( new LinearGradientPaint( new Point2D.Double( 714.625, 108.20012664794922 ), new Point2D.Double( 714.625, 103.625 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 85, 87, 83, 255 ), new Color( 136, 138, 133, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) ) );
		g.setStroke( new BasicStroke( 0.99999994f, 1, 1, 4.0f, null, 0.0f ) );
		g.draw( shape6 );
	}

	private void paintShapeNode_0_0_0_0_0_5_1( Graphics2D g ) {
		GeneralPath shape7 = new GeneralPath();
		shape7.moveTo( 718.0, 101.25 );
		shape7.curveTo( 713.7954, 101.25, 709.86945, 101.89964, 707.07935, 102.9375 );
		shape7.curveTo( 705.6843, 103.45643, 704.5703, 104.07409, 703.86914, 104.71875 );
		shape7.curveTo( 703.16797, 105.36341, 702.8966, 105.98437, 702.9991, 106.625 );
		shape7.lineTo( 703.1491, 107.625 );
		shape7.curveTo( 703.29456, 107.32766, 703.53925, 107.02207, 703.86914, 106.71875 );
		shape7.curveTo( 704.5703, 106.07409, 705.6843, 105.45643, 707.07935, 104.9375 );
		shape7.curveTo( 709.8694, 103.89964, 713.7954, 103.25, 718.0, 103.25 );
		shape7.curveTo( 722.2046, 103.25, 726.13055, 103.89964, 728.92065, 104.9375 );
		shape7.curveTo( 730.3157, 105.45643, 731.4297, 106.07409, 732.13086, 106.71875 );
		shape7.curveTo( 732.46075, 107.02207, 732.70544, 107.32766, 732.8509, 107.625 );
		shape7.lineTo( 733.0009, 106.625 );
		shape7.curveTo( 733.10345, 105.98437, 732.83203, 105.36341, 732.13086, 104.71875 );
		shape7.curveTo( 731.4297, 104.07409, 730.3157, 103.45643, 728.92065, 102.9375 );
		shape7.curveTo( 726.1306, 101.89964, 722.2046, 101.25, 718.0, 101.25 );
		shape7.closePath();
		g.setPaint( new LinearGradientPaint( new Point2D.Double( 702.9782104492188, 104.4375 ), new Point2D.Double( 733.0217895507812, 104.4375 ), new float[] { 0.0f, 0.6353322f, 1.0f }, new Color[] { new Color( 186, 189, 182, 255 ), new Color( 238, 238, 236, 255 ), new Color( 186, 189, 182, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) ) );
		g.fill( shape7 );
	}

	private void paintCompositeGraphicsNode_0_0_0_0_0_5( Graphics2D g ) {
		// _0_0_0_0_0_5_0
		AffineTransform trans_0_0_0_0_0_5_0 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_5_0( g );
		g.setTransform( trans_0_0_0_0_0_5_0 );
		// _0_0_0_0_0_5_1
		AffineTransform trans_0_0_0_0_0_5_1 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_5_1( g );
		g.setTransform( trans_0_0_0_0_0_5_1 );
	}

	private void paintShapeNode_0_0_0_0_0_6( Graphics2D g ) {
		GeneralPath shape8 = new GeneralPath();
		shape8.moveTo( 729.875, 60.625 );
		shape8.curveTo( 729.875, 63.90419, 723.2992, 66.5625, 715.1875, 66.5625 );
		shape8.curveTo( 707.0758, 66.5625, 700.5, 63.90419, 700.5, 60.625 );
		shape8.curveTo( 700.5, 57.34581, 707.0758, 54.6875, 715.1875, 54.6875 );
		shape8.curveTo( 723.2992, 54.6875, 729.875, 57.34581, 729.875, 60.625 );
		shape8.closePath();
		g.setPaint( new RadialGradientPaint( new Point2D.Double( 718.778564453125, 58.22014617919922 ), 14.6875f, new Point2D.Double( 718.778564453125, 58.22014617919922 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 211, 215, 207, 255 ), new Color( 85, 87, 83, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 1.2169979810714722f, -1.6399266719818115f, 0.5263804197311401f, 0.7093070149421692f, -186.63723754882812f, 1192.598388671875f ) ) );
		g.fill( shape8 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_0( Graphics2D g ) {
		GeneralPath shape9 = new GeneralPath();
		shape9.moveTo( -132.39502, -132.53253 );
		shape9.curveTo( -132.78226, -132.74329, -132.38332, -134.5506, -134.23735, -134.31387 );
		shape9.curveTo( -136.09142, -134.07713, -142.0526, -134.12912, -142.0526, -134.12912 );
		shape9.lineTo( -144.99408, -131.88876 );
		shape9.lineTo( -146.26927, -125.86054 );
		shape9.lineTo( -149.37119, -116.595924 );
		shape9.lineTo( -158.95056, -111.090294 );
		shape9.lineTo( -155.8683, -106.71347 );
		shape9.lineTo( -153.53702, -106.79427 );
		shape9.lineTo( -153.23976, -103.95916 );
		shape9.lineTo( -155.78229, -98.38711 );
		shape9.lineTo( -154.99219, -92.94785 );
		shape9.lineTo( -156.88928, -93.020035 );
		shape9.lineTo( -155.4694, -87.96476 );
		shape9.lineTo( -148.56165, -86.25559 );
		shape9.lineTo( -92.603004, -122.944405 );
		shape9.lineTo( -96.71791, -127.02674 );
		shape9.lineTo( -97.73094, -137.80421 );
		shape9.lineTo( -117.87928, -133.89806 );
		shape9.lineTo( -127.34128, -135.7256 );
		shape9.lineTo( -122.56923, -131.59708 );
		shape9.lineTo( -132.395, -132.53252 );
		shape9.lineTo( -132.395, -132.53255 );
		shape9.closePath();
		g.setPaint( new LinearGradientPaint( new Point2D.Double( -190.5, 104.1875 ), new Point2D.Double( -96.75, 104.1875 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 237, 244, 252, 255 ), new Color( 192, 198, 214, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( -0.7197251915931702f, 0.4388335943222046f, 0.5945504903793335f, 0.5312241911888123f, -297.7123107910156f, -110.38337707519531f ) ) );
		g.fill( shape9 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_1( Graphics2D g ) {
		GeneralPath shape10 = new GeneralPath();
		shape10.moveTo( -134.39539, -101.38344 );
		shape10.lineTo( -147.14307, -103.441956 );
		shape10.curveTo( -147.14307, -103.441956, -148.18748, -95.99042, -148.47304, -95.7046 );
		shape10.curveTo( -148.75858, -95.41878, -153.93748, -91.25563, -153.93748, -91.25563 );
		shape10.lineTo( -144.36986, -92.17369 );
		shape10.lineTo( -142.10507, -93.6663 );
		shape10.lineTo( -132.05634, -92.53165 );
		shape10.lineTo( -134.39539, -101.38344 );
		shape10.lineTo( -134.39539, -101.38344 );
		shape10.lineTo( -134.39539, -101.38344 );
		shape10.closePath();
		g.setPaint( new Color( 252, 253, 255, 255 ) );
		g.fill( shape10 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_2( Graphics2D g ) {
		GeneralPath shape11 = new GeneralPath();
		shape11.moveTo( -75.17744, -117.08164 );
		shape11.lineTo( -75.74074, -110.14688 );
		shape11.lineTo( -85.51964, -101.1681 );
		shape11.lineTo( -96.585365, -102.12953 );
		shape11.lineTo( -94.82121, -110.01991 );
		shape11.lineTo( -88.40236, -114.157074 );
		shape11.lineTo( -77.18797, -115.63235 );
		shape11.lineTo( -76.08882, -117.08453 );
		shape11.lineTo( -75.17743, -117.081535 );
		shape11.lineTo( -75.17743, -117.08164 );
		shape11.closePath();
		g.setPaint( new LinearGradientPaint( new Point2D.Double( -228.375, 89.875 ), new Point2D.Double( -196.875, 89.875 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 189, 208, 225, 255 ), new Color( 255, 255, 255, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( -0.7197251915931702f, 0.4388335943222046f, 0.5945504903793335f, 0.5312241911888123f, -291.4935302734375f, -63.27872848510742f ) ) );
		g.fill( shape11 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_3( Graphics2D g ) {
		GeneralPath shape12 = new GeneralPath();
		shape12.moveTo( -114.43921, -99.37829 );
		shape12.lineTo( -115.87085, -97.2765 );
		shape12.lineTo( -128.43471, -94.978424 );
		shape12.lineTo( -129.20921, -92.83044 );
		shape12.lineTo( -134.51717, -92.163536 );
		shape12.lineTo( -127.75409, -96.398865 );
		shape12.lineTo( -116.59839, -98.06178 );
		shape12.lineTo( -115.40927, -99.56883 );
		shape12.lineTo( -114.43921, -99.37828 );
		shape12.lineTo( -114.43921, -99.37828 );
		shape12.lineTo( -114.43921, -99.37828 );
		shape12.closePath();
		g.setPaint( new Color( 255, 255, 255, 255 ) );
		g.fill( shape12 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_4( Graphics2D g ) {
		GeneralPath shape13 = new GeneralPath();
		shape13.moveTo( -84.48459, -118.00424 );
		shape13.lineTo( -106.1819, -114.941124 );
		shape13.lineTo( -112.12742, -117.68386 );
		shape13.lineTo( -111.67369, -116.06132 );
		shape13.lineTo( -106.71387, -113.38787 );
		shape13.lineTo( -108.39193, -112.58816 );
		shape13.lineTo( -116.739136, -113.419655 );
		shape13.lineTo( -106.73734, -111.921234 );
		shape13.lineTo( -90.05072, -114.05184 );
		shape13.lineTo( -84.48458, -118.004234 );
		shape13.closePath();
		g.fill( shape13 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_5( Graphics2D g ) {
		GeneralPath shape14 = new GeneralPath();
		shape14.moveTo( -106.22024, -109.61507 );
		shape14.lineTo( -102.92678, -100.45148 );
		shape14.lineTo( -117.9588, -102.79294 );
		shape14.lineTo( -121.7139, -99.3862 );
		shape14.lineTo( -123.235466, -102.36856 );
		shape14.lineTo( -118.744995, -106.33539 );
		shape14.lineTo( -106.22024, -109.61507 );
		shape14.lineTo( -106.22024, -109.61507 );
		shape14.lineTo( -106.22024, -109.61507 );
		shape14.closePath();
		g.setPaint( new Color( 185, 200, 219, 255 ) );
		g.fill( shape14 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_6( Graphics2D g ) {
		GeneralPath shape15 = new GeneralPath();
		shape15.moveTo( -147.66048, -96.62752 );
		shape15.curveTo( -147.21457, -96.22911, -137.18544, -86.59201, -137.18544, -86.59201 );
		shape15.lineTo( -136.75516, -88.64183 );
		shape15.lineTo( -146.52614, -96.42542 );
		shape15.lineTo( -147.66048, -96.627525 );
		shape15.closePath();
		g.setPaint( new Color( 180, 198, 218, 255 ) );
		g.fill( shape15 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_7( Graphics2D g ) {
		GeneralPath shape16 = new GeneralPath();
		shape16.moveTo( -91.19981, -104.92167 );
		shape16.lineTo( -89.09154, -94.923706 );
		shape16.lineTo( -94.74372, -94.158646 );
		shape16.lineTo( -94.40735, -90.34193 );
		shape16.lineTo( -97.86901, -98.84438 );
		shape16.lineTo( -109.36112, -95.859184 );
		shape16.lineTo( -113.828125, -90.78948 );
		shape16.lineTo( -119.72672, -91.32649 );
		shape16.lineTo( -117.778755, -94.860275 );
		shape16.lineTo( -109.71316, -96.98514 );
		shape16.lineTo( -104.52645, -99.92417 );
		shape16.lineTo( -96.42565, -100.394745 );
		shape16.lineTo( -91.19982, -104.92168 );
		shape16.lineTo( -91.19982, -104.92168 );
		shape16.lineTo( -91.19982, -104.92168 );
		shape16.closePath();
		g.setPaint( new Color( 177, 186, 203, 255 ) );
		g.fill( shape16 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_8( Graphics2D g ) {
		GeneralPath shape17 = new GeneralPath();
		shape17.moveTo( -137.23425, -121.96007 );
		shape17.lineTo( -139.48343, -115.449715 );
		shape17.lineTo( -133.54181, -114.60378 );
		shape17.lineTo( -135.44278, -121.71177 );
		shape17.lineTo( -137.23425, -121.96006 );
		shape17.closePath();
		g.setPaint( new Color( 217, 225, 236, 255 ) );
		g.fill( shape17 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_9( Graphics2D g ) {
		GeneralPath shape18 = new GeneralPath();
		shape18.moveTo( -137.4622, -109.68778 );
		shape18.lineTo( -126.73609, -99.4702 );
		shape18.lineTo( -134.0427, -101.561104 );
		shape18.lineTo( -139.06201, -99.54806 );
		shape18.lineTo( -145.45647, -98.922165 );
		shape18.lineTo( -137.4622, -109.68778 );
		shape18.lineTo( -137.4622, -109.68778 );
		shape18.lineTo( -137.4622, -109.68778 );
		shape18.closePath();
		g.setPaint( new Color( 195, 215, 255, 255 ) );
		g.fill( shape18 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_10( Graphics2D g ) {
		GeneralPath shape19 = new GeneralPath();
		shape19.moveTo( -135.87839, -117.10134 );
		shape19.lineTo( -126.72545, -108.38234 );
		shape19.lineTo( -132.96043, -110.16657 );
		shape19.lineTo( -137.24358, -108.44878 );
		shape19.lineTo( -142.70018, -107.91469 );
		shape19.lineTo( -135.8784, -117.10135 );
		shape19.lineTo( -135.8784, -117.10135 );
		shape19.lineTo( -135.8784, -117.10135 );
		shape19.closePath();
		g.setPaint( new Color( 252, 253, 255, 255 ) );
		g.fill( shape19 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_11( Graphics2D g ) {
		GeneralPath shape20 = new GeneralPath();
		shape20.moveTo( -143.28148, -136.88266 );
		shape20.lineTo( -137.94614, -137.11938 );
		shape20.lineTo( -140.47691, -136.13489 );
		shape20.lineTo( -143.28148, -136.88264 );
		shape20.closePath();
		g.setPaint( new Color( 192, 198, 214, 255 ) );
		g.fill( shape20 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_12( Graphics2D g ) {
		GeneralPath shape21 = new GeneralPath();
		shape21.moveTo( -123.52238, -114.61906 );
		shape21.lineTo( -125.11831, -109.95932 );
		shape21.lineTo( -122.77531, -109.48872 );
		shape21.lineTo( -124.82888, -105.77883 );
		shape21.lineTo( -117.46343, -110.82831 );
		shape21.lineTo( -119.48567, -115.06943 );
		shape21.lineTo( -123.522385, -114.61906 );
		shape21.lineTo( -123.522385, -114.61906 );
		shape21.lineTo( -123.522385, -114.61906 );
		shape21.closePath();
		g.fill( shape21 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_13( Graphics2D g ) {
		GeneralPath shape22 = new GeneralPath();
		shape22.moveTo( -133.04099, -121.66503 );
		shape22.curveTo( -133.72943, -121.46871, -139.83142, -120.42938, -139.83142, -120.42938 );
		shape22.lineTo( -140.17957, -117.089035 );
		shape22.lineTo( -137.98518, -119.32075 );
		shape22.lineTo( -133.04099, -121.66504 );
		shape22.closePath();
		g.fill( shape22 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_14( Graphics2D g ) {
		GeneralPath shape23 = new GeneralPath();
		shape23.moveTo( -115.65102, -115.25619 );
		shape23.lineTo( -106.83447, -105.2149 );
		shape23.lineTo( -93.55867, -111.74543 );
		shape23.lineTo( -95.08415, -116.6246 );
		shape23.lineTo( -102.86811, -116.6824 );
		shape23.lineTo( -105.66095, -114.30923 );
		shape23.lineTo( -114.6966, -114.94442 );
		shape23.lineTo( -115.65101, -115.25623 );
		shape23.lineTo( -115.65101, -115.2562 );
		shape23.lineTo( -115.65101, -115.2562 );
		shape23.closePath();
		g.fill( shape23 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_15( Graphics2D g ) {
		GeneralPath shape24 = new GeneralPath();
		shape24.moveTo( -115.36851, -107.04673 );
		shape24.lineTo( -121.145836, -107.88111 );
		shape24.lineTo( -134.41379, -105.265465 );
		shape24.lineTo( -130.15807, -99.70493 );
		shape24.lineTo( -132.10603, -96.17115 );
		shape24.lineTo( -130.1933, -96.22025 );
		shape24.lineTo( -128.83206, -99.061134 );
		shape24.lineTo( -134.15955, -105.308784 );
		shape24.lineTo( -115.368515, -107.046745 );
		shape24.lineTo( -115.368515, -107.04674 );
		shape24.lineTo( -115.368515, -107.04674 );
		shape24.closePath();
		g.fill( shape24 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_16( Graphics2D g ) {
		GeneralPath shape25 = new GeneralPath();
		shape25.moveTo( -134.92313, -119.70906 );
		shape25.lineTo( -137.02757, -114.96271 );
		shape25.lineTo( -132.78746, -114.41993 );
		shape25.lineTo( -134.92313, -119.70906 );
		shape25.lineTo( -134.92313, -119.70906 );
		shape25.lineTo( -134.92313, -119.70906 );
		shape25.closePath();
		g.fill( shape25 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_17( Graphics2D g ) {
		GeneralPath shape26 = new GeneralPath();
		shape26.moveTo( -144.7842, -104.21853 );
		shape26.curveTo( -144.7842, -104.21853, -135.91676, -100.3527, -136.46829, -100.57501 );
		shape26.curveTo( -137.01982, -100.79731, -146.09067, -103.0868, -146.09067, -103.0868 );
		shape26.lineTo( -153.01411, -99.53571 );
		shape26.lineTo( -144.78421, -104.21854 );
		shape26.lineTo( -144.78421, -104.21854 );
		shape26.lineTo( -144.78421, -104.21854 );
		shape26.closePath();
		g.fill( shape26 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_18( Graphics2D g ) {
		GeneralPath shape27 = new GeneralPath();
		shape27.moveTo( -106.29318, -120.61398 );
		shape27.lineTo( -118.26643, -114.542496 );
		shape27.lineTo( -109.508545, -107.25838 );
		shape27.lineTo( -108.33502, -116.35268 );
		shape27.lineTo( -104.885056, -116.110146 );
		shape27.lineTo( -103.21484, -112.99499 );
		shape27.lineTo( -102.068756, -114.81096 );
		shape27.lineTo( -105.59303, -117.68938 );
		shape27.lineTo( -106.29318, -120.61398 );
		shape27.lineTo( -106.29318, -120.61398 );
		shape27.lineTo( -106.29318, -120.61398 );
		shape27.closePath();
		g.fill( shape27 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_19( Graphics2D g ) {
		GeneralPath shape28 = new GeneralPath();
		shape28.moveTo( -91.16675, -96.86413 );
		shape28.curveTo( -91.44839, -97.251, -96.423836, -99.803185, -96.423836, -99.803185 );
		shape28.lineTo( -95.0509, -96.953636 );
		shape28.lineTo( -98.83337, -96.54658 );
		shape28.lineTo( -105.967995, -97.782265 );
		shape28.lineTo( -95.074394, -92.9175 );
		shape28.lineTo( -91.16675, -96.86412 );
		shape28.closePath();
		g.fill( shape28 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_20( Graphics2D g ) {
		GeneralPath shape29 = new GeneralPath();
		shape29.moveTo( -138.2815, -105.49613 );
		shape29.curveTo( -138.14458, -105.91475, -137.93335, -108.83647, -137.93335, -108.83647 );
		shape29.lineTo( -132.26942, -111.6196 );
		shape29.lineTo( -131.47539, -109.42253 );
		shape29.lineTo( -138.2815, -105.49613 );
		shape29.closePath();
		g.fill( shape29 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_21( Graphics2D g ) {
		GeneralPath shape30 = new GeneralPath();
		shape30.moveTo( -103.72688, -141.6636 );
		shape30.lineTo( -114.068985, -141.16707 );
		shape30.lineTo( -108.31122, -139.53874 );
		shape30.lineTo( -103.72689, -141.6636 );
		shape30.lineTo( -103.72689, -141.6636 );
		shape30.lineTo( -103.72689, -141.6636 );
		shape30.closePath();
		g.setPaint( new Color( 186, 189, 182, 255 ) );
		g.fill( shape30 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_22( Graphics2D g ) {
		GeneralPath shape31 = new GeneralPath();
		shape31.moveTo( -128.75777, -107.38237 );
		shape31.lineTo( -135.247, -109.1233 );
		shape31.lineTo( -131.02255, -105.889755 );
		shape31.lineTo( -128.75777, -107.38236 );
		shape31.closePath();
		g.setPaint( new Color( 192, 198, 214, 255 ) );
		g.fill( shape31 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_23( Graphics2D g ) {
		GeneralPath shape32 = new GeneralPath();
		shape32.moveTo( -115.52779, -113.62524 );
		shape32.lineTo( -125.553055, -113.657036 );
		shape32.lineTo( -131.0018, -117.03782 );
		shape32.lineTo( -130.04741, -114.15651 );
		shape32.lineTo( -126.096756, -112.65522 );
		shape32.lineTo( -115.52778, -113.62525 );
		shape32.lineTo( -115.52778, -113.625244 );
		shape32.closePath();
		g.fill( shape32 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_24( Graphics2D g ) {
		GeneralPath shape33 = new GeneralPath();
		shape33.moveTo( -100.97263, -122.36054 );
		shape33.lineTo( -98.96604, -117.99816 );
		shape33.lineTo( -103.1631, -120.80152 );
		shape33.lineTo( -100.97263, -122.36054 );
		shape33.lineTo( -100.97263, -122.36054 );
		shape33.lineTo( -100.97263, -122.36054 );
		shape33.closePath();
		g.fill( shape33 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_25( Graphics2D g ) {
		GeneralPath shape34 = new GeneralPath();
		shape34.moveTo( -97.46421, -118.17365 );
		shape34.lineTo( -97.31951, -114.79865 );
		shape34.lineTo( -94.16681, -114.821754 );
		shape34.lineTo( -95.70797, -111.87116 );
		shape34.lineTo( -99.00931, -111.98088 );
		shape34.lineTo( -97.46421, -118.17366 );
		shape34.lineTo( -97.46421, -118.17365 );
		shape34.lineTo( -97.46421, -118.17365 );
		shape34.closePath();
		g.setPaint( new Color( 145, 159, 186, 255 ) );
		g.fill( shape34 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_26( Graphics2D g ) {
		GeneralPath shape35 = new GeneralPath();
		shape35.moveTo( -144.14174, -130.13132 );
		shape35.curveTo( -143.95007, -129.68959, -142.97221, -125.70543, -142.97221, -125.70543 );
		shape35.lineTo( -146.34395, -126.55424 );
		shape35.lineTo( -144.14172, -130.13132 );
		shape35.lineTo( -144.14172, -130.13132 );
		shape35.lineTo( -144.14172, -130.13132 );
		shape35.closePath();
		g.fill( shape35 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_27( Graphics2D g ) {
		GeneralPath shape36 = new GeneralPath();
		shape36.moveTo( -88.66009, -101.76395 );
		shape36.lineTo( -89.86095, -98.23883 );
		shape36.lineTo( -95.79867, -97.18795 );
		shape36.lineTo( -99.59286, -97.33231 );
		shape36.lineTo( -95.38013, -99.7892 );
		shape36.lineTo( -94.656494, -98.33122 );
		shape36.lineTo( -91.0344, -99.42252 );
		shape36.lineTo( -88.66008, -101.76394 );
		shape36.lineTo( -88.66008, -101.76394 );
		shape36.lineTo( -88.66008, -101.76394 );
		shape36.closePath();
		g.fill( shape36 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_28( Graphics2D g ) {
		GeneralPath shape37 = new GeneralPath();
		shape37.moveTo( -143.93529, -125.38695 );
		shape37.lineTo( -144.79587, -118.7178 );
		shape37.lineTo( -141.73703, -120.58285 );
		shape37.lineTo( -142.41762, -124.3014 );
		shape37.lineTo( -139.60522, -124.89902 );
		shape37.lineTo( -143.93529, -125.38695 );
		shape37.closePath();
		g.fill( shape37 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_29( Graphics2D g ) {
		GeneralPath shape38 = new GeneralPath();
		shape38.moveTo( -118.91435, -123.78149 );
		shape38.curveTo( -118.91435, -123.78149, -122.17662, -115.201096, -121.63683, -115.53023 );
		shape38.curveTo( -121.09703, -115.85936, -118.143814, -117.54829, -118.143814, -117.54829 );
		shape38.lineTo( -118.91435, -123.78149 );
		shape38.closePath();
		g.fill( shape38 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_30( Graphics2D g ) {
		GeneralPath shape39 = new GeneralPath();
		shape39.moveTo( -111.46347, -107.91678 );
		shape39.curveTo( -111.46347, -107.91678, -107.59883, -114.74182, -107.80614, -115.06228 );
		shape39.curveTo( -108.01345, -115.38275, -109.30814, -118.83859, -109.30814, -118.83859 );
		shape39.lineTo( -106.53488, -115.27882 );
		shape39.lineTo( -107.7162, -109.97815 );
		shape39.lineTo( -111.46346, -107.91679 );
		shape39.closePath();
		g.fill( shape39 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_31( Graphics2D g ) {
		GeneralPath shape40 = new GeneralPath();
		shape40.moveTo( -118.62937, -101.40666 );
		shape40.lineTo( -115.25369, -103.80004 );
		shape40.lineTo( -114.40486, -108.45112 );
		shape40.lineTo( -119.25907, -111.30068 );
		shape40.lineTo( -116.80265, -107.21257 );
		shape40.lineTo( -118.62937, -101.40666 );
		shape40.lineTo( -118.62937, -101.40666 );
		shape40.lineTo( -118.62937, -101.40666 );
		shape40.closePath();
		g.fill( shape40 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_32( Graphics2D g ) {
		GeneralPath shape41 = new GeneralPath();
		shape41.moveTo( -136.38467, -101.42022 );
		shape41.lineTo( -136.65065, -104.49785 );
		shape41.lineTo( -130.97108, -107.40223 );
		shape41.lineTo( -136.8071, -105.85477 );
		shape41.lineTo( -142.87779, -107.62746 );
		shape41.lineTo( -137.51509, -104.86451 );
		shape41.lineTo( -138.90372, -99.8843 );
		shape41.lineTo( -136.38467, -101.42022 );
		shape41.lineTo( -136.38467, -101.42022 );
		shape41.lineTo( -136.38467, -101.42022 );
		shape41.closePath();
		g.setPaint( new Color( 186, 189, 182, 255 ) );
		g.fill( shape41 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_33( Graphics2D g ) {
		GeneralPath shape42 = new GeneralPath();
		shape42.moveTo( -135.45757, -125.49986 );
		shape42.lineTo( -145.1582, -122.26637 );
		shape42.lineTo( -155.2343, -126.400696 );
		shape42.lineTo( -158.67255, -123.52229 );
		shape42.lineTo( -152.69577, -115.883064 );
		shape42.lineTo( -151.1272, -123.54247 );
		shape42.lineTo( -143.78917, -121.31363 );
		shape42.lineTo( -138.09006, -122.44245 );
		shape42.lineTo( -135.45757, -125.49986 );
		shape42.lineTo( -135.45757, -125.49986 );
		shape42.lineTo( -135.45757, -125.49986 );
		shape42.closePath();
		g.setPaint( new Color( 255, 255, 255, 255 ) );
		g.fill( shape42 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_34( Graphics2D g ) {
		GeneralPath shape43 = new GeneralPath();
		shape43.moveTo( -94.8302, -126.59109 );
		shape43.lineTo( -95.86286, -123.727104 );
		shape43.lineTo( -101.51505, -120.39255 );
		shape43.lineTo( -101.01435, -124.27278 );
		shape43.lineTo( -94.45469, -126.93176 );
		shape43.lineTo( -94.8302, -126.591095 );
		shape43.lineTo( -94.8302, -126.591095 );
		shape43.lineTo( -94.8302, -126.591095 );
		shape43.closePath();
		g.setPaint( new Color( 223, 227, 235, 255 ) );
		g.fill( shape43 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_35( Graphics2D g ) {
		GeneralPath shape44 = new GeneralPath();
		shape44.moveTo( -125.35453, -117.29214 );
		shape44.curveTo( -125.26063, -117.435585, -124.57269, -117.22394, -124.60601, -117.95008 );
		shape44.curveTo( -124.63931, -118.67618, -124.43127, -120.9851, -124.43127, -120.9851 );
		shape44.lineTo( -125.20692, -122.19585 );
		shape44.lineTo( -127.503265, -122.88013 );
		shape44.lineTo( -130.99648, -124.37446 );
		shape44.lineTo( -132.82852, -128.26099 );
		shape44.lineTo( -134.62215, -127.204254 );
		shape44.lineTo( -134.66435, -126.2981 );
		shape44.lineTo( -135.77261, -126.2722 );
		shape44.lineTo( -137.85222, -127.43332 );
		shape44.lineTo( -139.98538, -127.29853 );
		shape44.lineTo( -139.89758, -128.03157 );
		shape44.lineTo( -141.90178, -127.64058 );
		shape44.lineTo( -142.782, -125.01699 );
		shape44.lineTo( -130.32521, -102.17086 );
		shape44.lineTo( -128.61317, -103.63713 );
		shape44.lineTo( -124.40388, -103.69003 );
		shape44.lineTo( -125.282814, -111.622696 );
		shape44.lineTo( -124.27621, -115.23257 );
		shape44.lineTo( -126.02685, -113.51305 );
		shape44.lineTo( -125.35456, -117.292046 );
		shape44.lineTo( -125.35454, -117.29214 );
		shape44.lineTo( -125.35454, -117.29214 );
		shape44.closePath();
		g.setPaint( new LinearGradientPaint( new Point2D.Double( -190.5, 104.1875 ), new Point2D.Double( -96.75, 104.1875 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 237, 244, 252, 255 ), new Color( 192, 198, 214, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( -0.14740629494190216f, -0.2927992045879364f, -0.22464419901371002f, 0.2137041985988617f, -128.72862243652344f, -182.0675811767578f ) ) );
		g.fill( shape44 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_36( Graphics2D g ) {
		GeneralPath shape45 = new GeneralPath();
		shape45.moveTo( -122.48882, -115.01198 );
		shape45.lineTo( -119.4038, -127.55086 );
		shape45.curveTo( -119.4038, -127.55086, -126.74617, -129.19583, -127.007904, -129.5036 );
		shape45.curveTo( -127.269646, -129.81137, -130.99931, -135.31067, -130.99931, -135.31067 );
		shape45.lineTo( -130.85979, -125.70011 );
		shape45.lineTo( -129.55566, -123.3218 );
		shape45.lineTo( -131.50108, -113.3981 );
		shape45.lineTo( -122.48883, -115.01197 );
		shape45.lineTo( -122.48883, -115.01197 );
		shape45.lineTo( -122.48883, -115.01197 );
		shape45.closePath();
		g.setPaint( new Color( 252, 253, 255, 255 ) );
		g.fill( shape45 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_37( Graphics2D g ) {
		GeneralPath shape46 = new GeneralPath();
		shape46.moveTo( -91.66337, -94.91746 );
		shape46.lineTo( -98.529655, -96.041 );
		shape46.lineTo( -106.68626, -106.5155 );
		shape46.lineTo( -104.831055, -117.466896 );
		shape46.lineTo( -97.10963, -115.06899 );
		shape46.lineTo( -93.50636, -108.33592 );
		shape46.lineTo( -92.94493, -97.03886 );
		shape46.lineTo( -91.58661, -95.825615 );
		shape46.lineTo( -91.663506, -94.917465 );
		shape46.lineTo( -91.66335, -94.91746 );
		shape46.lineTo( -91.66335, -94.91746 );
		shape46.closePath();
		g.setPaint( new LinearGradientPaint( new Point2D.Double( -228.375, 89.875 ), new Point2D.Double( -196.875, 89.875 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 189, 208, 225, 255 ), new Color( 255, 255, 255, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( -0.3790521025657654f, -0.7529268264770508f, -0.5776677131652832f, 0.5495356917381287f, -127.75569152832031f, -314.8827819824219f ) ) );
		g.fill( shape46 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_38( Graphics2D g ) {
		GeneralPath shape47 = new GeneralPath();
		shape47.moveTo( -106.58678, -93.99409 );
		shape47.lineTo( -108.56562, -95.59138 );
		shape47.lineTo( -109.83776, -108.30018 );
		shape47.lineTo( -111.9159, -109.24623 );
		shape47.lineTo( -112.15037, -114.59078 );
		shape47.lineTo( -108.47716, -107.50666 );
		shape47.lineTo( -107.72394, -96.252884 );
		shape47.lineTo( -106.31823, -94.945526 );
		shape47.lineTo( -106.58678, -93.994095 );
		shape47.lineTo( -106.58678, -93.994095 );
		shape47.lineTo( -106.58678, -93.994095 );
		shape47.closePath();
		g.setPaint( new Color( 255, 255, 255, 255 ) );
		g.fill( shape47 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_39( Graphics2D g ) {
		GeneralPath shape48 = new GeneralPath();
		shape48.moveTo( -137.34933, -86.69519 );
		shape48.lineTo( -138.64369, -108.56939 );
		shape48.lineTo( -135.42805, -114.27304 );
		shape48.lineTo( -137.08203, -113.95231 );
		shape48.lineTo( -140.1487, -109.22551 );
		shape48.lineTo( -140.80975, -110.96287 );
		shape48.lineTo( -139.3044, -119.21522 );
		shape48.lineTo( -141.60858, -109.36779 );
		shape48.lineTo( -140.83752, -92.563385 );
		shape48.lineTo( -137.3493, -86.6952 );
		shape48.lineTo( -137.3493, -86.6952 );
		shape48.lineTo( -137.3493, -86.6952 );
		shape48.closePath();
		g.fill( shape48 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_40( Graphics2D g ) {
		GeneralPath shape49 = new GeneralPath();
		shape49.moveTo( -100.63056, -95.58696 );
		shape49.lineTo( -110.03095, -93.0471 );
		shape49.lineTo( -106.478775, -107.83987 );
		shape49.lineTo( -109.56994, -111.85875 );
		shape49.lineTo( -106.47406, -113.13358 );
		shape49.lineTo( -102.88426, -108.33635 );
		shape49.lineTo( -100.63057, -95.58697 );
		shape49.lineTo( -100.63057, -95.58697 );
		shape49.lineTo( -100.63057, -95.58697 );
		shape49.closePath();
		g.setPaint( new Color( 185, 200, 219, 255 ) );
		g.fill( shape49 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_41( Graphics2D g ) {
		GeneralPath shape50 = new GeneralPath();
		shape50.moveTo( -130.91817, -120.23411 );
		shape50.curveTo( -131.35141, -119.82196, -141.76971, -110.60698, -141.76971, -110.60698 );
		shape50.lineTo( -139.76152, -110.01196 );
		shape50.lineTo( -131.21156, -119.119896 );
		shape50.lineTo( -130.91818, -120.234116 );
		shape50.closePath();
		g.setPaint( new Color( 180, 198, 218, 255 ) );
		g.fill( shape50 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_42( Graphics2D g ) {
		GeneralPath shape51 = new GeneralPath();
		shape51.moveTo( -144.84949, -98.93359 );
		shape51.lineTo( -154.98544, -97.64265 );
		shape51.lineTo( -155.28984, -103.33824 );
		shape51.lineTo( -159.12126, -103.31234 );
		shape51.lineTo( -150.3662, -106.07344 );
		shape51.lineTo( -152.41008, -117.769714 );
		shape51.lineTo( -157.10101, -122.63294 );
		shape51.lineTo( -156.08766, -128.4686 );
		shape51.lineTo( -152.7234, -126.24061 );
		shape51.lineTo( -151.2593, -118.02931 );
		shape51.lineTo( -148.75034, -112.62145 );
		shape51.lineTo( -148.93793, -104.50916 );
		shape51.lineTo( -144.84947, -98.93359 );
		shape51.lineTo( -144.84947, -98.93359 );
		shape51.lineTo( -144.84947, -98.93359 );
		shape51.closePath();
		g.setPaint( new Color( 177, 186, 203, 255 ) );
		g.fill( shape51 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_43( Graphics2D g ) {
		GeneralPath shape52 = new GeneralPath();
		shape52.moveTo( -114.44417, -107.84219 );
		shape52.lineTo( -120.7508, -110.611664 );
		shape52.lineTo( -122.075554, -104.758156 );
		shape52.lineTo( -114.83686, -106.07674 );
		shape52.lineTo( -114.44417, -107.84219 );
		shape52.lineTo( -114.44417, -107.84219 );
		shape52.lineTo( -114.44417, -107.84219 );
		shape52.closePath();
		g.setPaint( new Color( 217, 225, 236, 255 ) );
		g.fill( shape52 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_44( Graphics2D g ) {
		GeneralPath shape53 = new GeneralPath();
		shape53.moveTo( -97.67246, -111.52222 );
		shape53.lineTo( -108.725845, -101.659584 );
		shape53.lineTo( -106.04958, -108.772675 );
		shape53.lineTo( -107.649155, -113.93864 );
		shape53.lineTo( -107.754684, -120.362785 );
		shape53.lineTo( -97.67246, -111.52222 );
		shape53.lineTo( -97.67246, -111.52222 );
		shape53.lineTo( -97.67246, -111.52222 );
		shape53.closePath();
		g.setPaint( new Color( 195, 215, 255, 255 ) );
		g.fill( shape53 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_45( Graphics2D g ) {
		GeneralPath shape54 = new GeneralPath();
		shape54.moveTo( -116.04751, -107.16817 );
		shape54.lineTo( -125.47972, -98.75206 );
		shape54.lineTo( -123.195984, -104.82191 );
		shape54.lineTo( -124.56097, -109.23019 );
		shape54.lineTo( -124.65096, -114.71213 );
		shape54.lineTo( -116.04747, -107.16817 );
		shape54.lineTo( -116.04751, -107.16817 );
		shape54.lineTo( -116.04751, -107.16817 );
		shape54.closePath();
		g.setPaint( new Color( 252, 253, 255, 255 ) );
		g.fill( shape54 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_46( Graphics2D g ) {
		GeneralPath shape55 = new GeneralPath();
		shape55.moveTo( -138.0984, -117.23 );
		shape55.lineTo( -138.2949, -111.893036 );
		shape55.lineTo( -139.07103, -114.49528 );
		shape55.lineTo( -138.0984, -117.23001 );
		shape55.lineTo( -138.0984, -117.23001 );
		shape55.lineTo( -138.0984, -117.23001 );
		shape55.closePath();
		g.setPaint( new Color( 192, 198, 214, 255 ) );
		g.fill( shape55 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_47( Graphics2D g ) {
		GeneralPath shape56 = new GeneralPath();
		shape56.moveTo( -139.65446, -105.60096 );
		shape56.lineTo( -144.16951, -107.569336 );
		shape56.lineTo( -144.82848, -105.27219 );
		shape56.lineTo( -148.35971, -107.619705 );
		shape56.lineTo( -143.92384, -99.8692 );
		shape56.lineTo( -139.53278, -101.54102 );
		shape56.lineTo( -139.65446, -105.60095 );
		shape56.closePath();
		g.fill( shape56 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_48( Graphics2D g ) {
		GeneralPath shape57 = new GeneralPath();
		shape57.moveTo( -146.23312, -112.18406 );
		shape57.curveTo( -146.373, -112.88614, -146.9143, -119.05231, -146.9143, -119.05231 );
		shape57.lineTo( -150.21545, -119.67006 );
		shape57.lineTo( -148.16896, -117.30201 );
		shape57.lineTo( -146.23312, -112.18406 );
		shape57.lineTo( -146.23312, -112.18406 );
		shape57.lineTo( -146.23312, -112.18406 );
		shape57.closePath();
		g.fill( shape57 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_49( Graphics2D g ) {
		GeneralPath shape58 = new GeneralPath();
		shape58.moveTo( -118.81447, -103.36701 );
		shape58.lineTo( -129.53734, -95.39338 );
		shape58.lineTo( -124.10438, -81.63193 );
		shape58.lineTo( -119.11761, -82.756905 );
		shape58.lineTo( -118.42907, -90.51057 );
		shape58.lineTo( -120.56807, -93.48658 );
		shape58.lineTo( -119.202576, -102.44101 );
		shape58.lineTo( -118.81444, -103.36701 );
		shape58.lineTo( -118.81447, -103.36701 );
		shape58.lineTo( -118.81447, -103.36701 );
		shape58.closePath();
		g.fill( shape58 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_50( Graphics2D g ) {
		GeneralPath shape59 = new GeneralPath();
		shape59.moveTo( -122.31061, -121.76999 );
		shape59.lineTo( -121.01069, -127.46068 );
		shape59.lineTo( -122.54229, -140.89699 );
		shape59.lineTo( -128.42947, -137.10597 );
		shape59.lineTo( -131.79373, -139.33395 );
		shape59.lineTo( -131.89983, -137.42354 );
		shape59.lineTo( -129.17862, -135.83652 );
		shape59.lineTo( -122.51971, -140.64006 );
		shape59.lineTo( -122.31058, -121.76999 );
		shape59.lineTo( -122.310585, -121.76999 );
		shape59.lineTo( -122.310585, -121.76999 );
		shape59.closePath();
		g.fill( shape59 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_51( Graphics2D g ) {
		GeneralPath shape60 = new GeneralPath();
		shape60.moveTo( -100.97362, -123.74181 );
		shape60.lineTo( -105.533775, -126.224045 );
		shape60.lineTo( -106.41846, -122.04188 );
		shape60.lineTo( -100.97362, -123.74182 );
		shape60.closePath();
		g.fill( shape60 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_52( Graphics2D g ) {
		GeneralPath shape61 = new GeneralPath();
		shape61.moveTo( -105.03012, -125.40315 );
		shape61.curveTo( -105.03012, -125.40315, -109.60198, -116.87824, -109.3357, -117.409935 );
		shape61.curveTo( -109.06942, -117.94163, -106.05223, -126.79704, -106.05223, -126.79704 );
		shape61.lineTo( -109.03045, -133.98555 );
		shape61.lineTo( -105.03012, -125.40316 );
		shape61.lineTo( -105.03012, -125.40316 );
		shape61.lineTo( -105.03012, -125.40316 );
		shape61.closePath();
		g.fill( shape61 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_53( Graphics2D g ) {
		GeneralPath shape62 = new GeneralPath();
		shape62.moveTo( -103.74948, -119.92615 );
		shape62.lineTo( -108.83049, -132.35213 );
		shape62.lineTo( -116.80052, -124.21347 );
		shape62.lineTo( -107.83126, -122.30667 );
		shape62.lineTo( -108.35263, -118.88771 );
		shape62.lineTo( -111.59292, -117.4755 );
		shape62.lineTo( -109.87582, -116.18598 );
		shape62.lineTo( -106.72121, -119.465355 );
		shape62.lineTo( -103.74947, -119.926155 );
		shape62.lineTo( -103.74947, -119.926155 );
		shape62.lineTo( -103.74947, -119.926155 );
		shape62.closePath();
		g.fill( shape62 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_54( Graphics2D g ) {
		GeneralPath shape63 = new GeneralPath();
		shape63.moveTo( -113.01835, -104.19628 );
		shape63.curveTo( -112.60993, -104.44564, -109.66285, -109.19785, -109.66285, -109.19785 );
		shape63.lineTo( -112.61431, -108.06041 );
		shape63.lineTo( -112.71341, -111.86341 );
		shape63.lineTo( -110.90349, -118.87441 );
		shape63.lineTo( -116.635216, -108.41097 );
		shape63.lineTo( -113.01832, -104.19628 );
		shape63.lineTo( -113.01835, -104.19628 );
		shape63.lineTo( -113.01835, -104.19628 );
		shape63.closePath();
		g.fill( shape63 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_55( Graphics2D g ) {
		GeneralPath shape64 = new GeneralPath();
		shape64.moveTo( -122.34034, -128.05212 );
		shape64.curveTo( -121.9342, -127.88174, -119.03921, -127.434364, -119.03921, -127.434364 );
		shape64.lineTo( -116.72433, -121.56349 );
		shape64.lineTo( -118.97853, -120.950165 );
		shape64.lineTo( -122.34034, -128.05212 );
		shape64.closePath();
		g.fill( shape64 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_56( Graphics2D g ) {
		GeneralPath shape65 = new GeneralPath();
		shape65.moveTo( -131.22617, -83.955605 );
		shape65.lineTo( -130.8828, -94.303925 );
		shape65.lineTo( -132.97247, -88.69708 );
		shape65.lineTo( -131.22617, -83.955605 );
		shape65.lineTo( -131.22617, -83.955605 );
		shape65.lineTo( -131.22617, -83.955605 );
		shape65.closePath();
		g.fill( shape65 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_57( Graphics2D g ) {
		GeneralPath shape66 = new GeneralPath();
		shape66.moveTo( -134.79109, -118.34518 );
		shape66.lineTo( -132.5299, -124.671936 );
		shape66.lineTo( -136.09521, -120.723495 );
		shape66.lineTo( -134.79109, -118.34518 );
		shape66.closePath();
		g.fill( shape66 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_58( Graphics2D g ) {
		GeneralPath shape67 = new GeneralPath();
		shape67.moveTo( -144.89034, -115.10358 );
		shape67.lineTo( -144.04604, -125.093285 );
		shape67.lineTo( -140.23473, -130.25008 );
		shape67.lineTo( -143.18391, -129.53236 );
		shape67.lineTo( -145.00049, -125.7164 );
		shape67.lineTo( -144.89034, -115.10357 );
		shape67.lineTo( -144.89035, -115.10357 );
		shape67.lineTo( -144.89035, -115.10357 );
		shape67.closePath();
		g.fill( shape67 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_59( Graphics2D g ) {
		GeneralPath shape68 = new GeneralPath();
		shape68.moveTo( -103.70774, -100.41719 );
		shape68.lineTo( -108.21842, -98.77079 );
		shape68.lineTo( -105.08409, -102.72682 );
		shape68.lineTo( -103.70774, -100.41719 );
		shape68.lineTo( -103.70774, -100.41719 );
		shape68.lineTo( -103.70774, -100.41719 );
		shape68.closePath();
		g.fill( shape68 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_60( Graphics2D g ) {
		GeneralPath shape69 = new GeneralPath();
		shape69.moveTo( -130.08437, -109.41773 );
		shape69.lineTo( -133.45999, -109.54707 );
		shape69.lineTo( -133.6925, -106.40287 );
		shape69.lineTo( -136.50847, -108.17812 );
		shape69.lineTo( -136.13152, -111.4597 );
		shape69.lineTo( -130.08435, -109.41773 );
		shape69.lineTo( -130.08437, -109.41773 );
		shape69.closePath();
		g.setPaint( new Color( 145, 159, 186, 255 ) );
		g.fill( shape69 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_61( Graphics2D g ) {
		GeneralPath shape70 = new GeneralPath();
		shape70.moveTo( -111.05447, -106.78952 );
		shape70.curveTo( -111.51027, -106.63429, -115.560585, -105.98259, -115.560585, -105.98259 );
		shape70.lineTo( -114.44128, -109.27443 );
		shape70.lineTo( -111.05447, -106.78952 );
		shape70.closePath();
		g.fill( shape70 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_62( Graphics2D g ) {
		GeneralPath shape71 = new GeneralPath();
		shape71.moveTo( -110.96047, -107.81577 );
		shape71.lineTo( -114.376656, -109.298416 );
		shape71.lineTo( -114.94279, -115.30177 );
		shape71.lineTo( -114.49136, -119.071785 );
		shape71.lineTo( -112.384026, -114.673775 );
		shape71.lineTo( -113.89586, -114.070694 );
		shape71.lineTo( -113.10173, -110.37207 );
		shape71.lineTo( -110.96048, -107.81577 );
		shape71.lineTo( -110.96048, -107.81577 );
		shape71.lineTo( -110.96048, -107.81577 );
		shape71.closePath();
		g.fill( shape71 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_63( Graphics2D g ) {
		GeneralPath shape72 = new GeneralPath();
		shape72.moveTo( -114.59449, -120.31398 );
		shape72.lineTo( -121.171936, -121.7123 );
		shape72.lineTo( -119.56097, -118.51235 );
		shape72.lineTo( -115.79948, -118.88929 );
		shape72.lineTo( -115.431786, -116.03771 );
		shape72.lineTo( -114.59448, -120.31398 );
		shape72.lineTo( -114.59448, -120.31398 );
		shape72.lineTo( -114.59448, -120.31398 );
		shape72.closePath();
		g.fill( shape72 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_64( Graphics2D g ) {
		GeneralPath shape73 = new GeneralPath();
		shape73.moveTo( -125.6262, -118.76841 );
		shape73.curveTo( -125.6262, -118.76841, -133.91393, -122.71543, -133.62964, -122.15074 );
		shape73.curveTo( -133.34535, -121.586044, -131.90135, -118.50564, -131.90135, -118.50564 );
		shape73.lineTo( -125.626205, -118.76841 );
		shape73.lineTo( -125.626205, -118.76841 );
		shape73.lineTo( -125.626205, -118.76841 );
		shape73.closePath();
		g.fill( shape73 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_65( Graphics2D g ) {
		GeneralPath shape74 = new GeneralPath();
		shape74.moveTo( -143.77301, -110.82659 );
		shape74.curveTo( -143.77301, -110.82659, -137.28368, -106.42146, -136.94746, -106.60211 );
		shape74.curveTo( -136.61124, -106.78276, -133.06183, -107.79309, -133.06183, -107.79309 );
		shape74.lineTo( -136.83469, -105.31749 );
		shape74.lineTo( -142.02217, -106.92457 );
		shape74.lineTo( -143.77301, -110.82659 );
		shape74.lineTo( -143.77301, -110.82659 );
		shape74.lineTo( -143.77301, -110.82659 );
		shape74.closePath();
		g.fill( shape74 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_66( Graphics2D g ) {
		GeneralPath shape75 = new GeneralPath();
		shape75.moveTo( -150.80025, -103.64741 );
		shape75.lineTo( -148.68835, -100.08884 );
		shape75.lineTo( -144.12138, -98.86581 );
		shape75.lineTo( -140.88774, -103.47307 );
		shape75.lineTo( -145.1615, -101.356094 );
		shape75.lineTo( -150.80023, -103.64741 );
		shape75.lineTo( -150.80023, -103.64741 );
		shape75.lineTo( -150.80023, -103.64741 );
		shape75.closePath();
		g.fill( shape75 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_67( Graphics2D g ) {
		GeneralPath shape76 = new GeneralPath();
		shape76.moveTo( -100.04461, -96.73875 );
		shape76.lineTo( -96.95555, -96.75439 );
		shape76.lineTo( -94.52109, -90.85809 );
		shape76.lineTo( -95.590416, -96.80034 );
		shape76.lineTo( -93.3315, -102.70737 );
		shape76.lineTo( -96.52003, -97.586266 );
		shape76.lineTo( -101.3713, -99.374 );
		shape76.lineTo( -100.04461, -96.738754 );
		shape76.lineTo( -100.04461, -96.73875 );
		shape76.lineTo( -100.04461, -96.73875 );
		shape76.closePath();
		g.fill( shape76 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_68( Graphics2D g ) {
		GeneralPath shape77 = new GeneralPath();
		shape77.moveTo( -125.1758, -106.42465 );
		shape77.lineTo( -127.61236, -116.35545 );
		shape77.lineTo( -122.67492, -126.06329 );
		shape77.lineTo( -125.26516, -129.72353 );
		shape77.lineTo( -133.36371, -124.38561 );
		shape77.lineTo( -125.856636, -122.20136 );
		shape77.lineTo( -128.67293, -115.068146 );
		shape77.lineTo( -128.00977, -109.29629 );
		shape77.lineTo( -125.1758, -106.424644 );
		shape77.closePath();
		g.setPaint( new Color( 255, 255, 255, 255 ) );
		g.fill( shape77 );
	}

	private void paintShapeNode_0_0_0_0_0_7_0_69( Graphics2D g ) {
		GeneralPath shape78 = new GeneralPath();
		shape78.moveTo( -125.73364, -92.68954 );
		shape78.lineTo( -128.50449, -93.950935 );
		shape78.lineTo( -131.36993, -99.85481 );
		shape78.lineTo( -127.54305, -99.041245 );
		shape78.lineTo( -125.42452, -92.28764 );
		shape78.lineTo( -125.733635, -92.68953 );
		shape78.closePath();
		g.setPaint( new Color( 223, 227, 235, 255 ) );
		g.fill( shape78 );
	}

	private void paintCompositeGraphicsNode_0_0_0_0_0_7_0( Graphics2D g ) {
		// _0_0_0_0_0_7_0_0
		AffineTransform trans_0_0_0_0_0_7_0_0 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_0( g );
		g.setTransform( trans_0_0_0_0_0_7_0_0 );
		// _0_0_0_0_0_7_0_1
		AffineTransform trans_0_0_0_0_0_7_0_1 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_1( g );
		g.setTransform( trans_0_0_0_0_0_7_0_1 );
		// _0_0_0_0_0_7_0_2
		AffineTransform trans_0_0_0_0_0_7_0_2 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_2( g );
		g.setTransform( trans_0_0_0_0_0_7_0_2 );
		// _0_0_0_0_0_7_0_3
		AffineTransform trans_0_0_0_0_0_7_0_3 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_3( g );
		g.setTransform( trans_0_0_0_0_0_7_0_3 );
		// _0_0_0_0_0_7_0_4
		AffineTransform trans_0_0_0_0_0_7_0_4 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_4( g );
		g.setTransform( trans_0_0_0_0_0_7_0_4 );
		// _0_0_0_0_0_7_0_5
		AffineTransform trans_0_0_0_0_0_7_0_5 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_5( g );
		g.setTransform( trans_0_0_0_0_0_7_0_5 );
		// _0_0_0_0_0_7_0_6
		AffineTransform trans_0_0_0_0_0_7_0_6 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_6( g );
		g.setTransform( trans_0_0_0_0_0_7_0_6 );
		// _0_0_0_0_0_7_0_7
		AffineTransform trans_0_0_0_0_0_7_0_7 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_7( g );
		g.setTransform( trans_0_0_0_0_0_7_0_7 );
		// _0_0_0_0_0_7_0_8
		AffineTransform trans_0_0_0_0_0_7_0_8 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_8( g );
		g.setTransform( trans_0_0_0_0_0_7_0_8 );
		// _0_0_0_0_0_7_0_9
		AffineTransform trans_0_0_0_0_0_7_0_9 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_9( g );
		g.setTransform( trans_0_0_0_0_0_7_0_9 );
		// _0_0_0_0_0_7_0_10
		AffineTransform trans_0_0_0_0_0_7_0_10 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_10( g );
		g.setTransform( trans_0_0_0_0_0_7_0_10 );
		// _0_0_0_0_0_7_0_11
		g.setComposite( AlphaComposite.getInstance( 3, 0.6456311f * origAlpha ) );
		AffineTransform trans_0_0_0_0_0_7_0_11 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_11( g );
		g.setTransform( trans_0_0_0_0_0_7_0_11 );
		// _0_0_0_0_0_7_0_12
		AffineTransform trans_0_0_0_0_0_7_0_12 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_12( g );
		g.setTransform( trans_0_0_0_0_0_7_0_12 );
		// _0_0_0_0_0_7_0_13
		AffineTransform trans_0_0_0_0_0_7_0_13 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_13( g );
		g.setTransform( trans_0_0_0_0_0_7_0_13 );
		// _0_0_0_0_0_7_0_14
		AffineTransform trans_0_0_0_0_0_7_0_14 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_14( g );
		g.setTransform( trans_0_0_0_0_0_7_0_14 );
		// _0_0_0_0_0_7_0_15
		AffineTransform trans_0_0_0_0_0_7_0_15 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_15( g );
		g.setTransform( trans_0_0_0_0_0_7_0_15 );
		// _0_0_0_0_0_7_0_16
		AffineTransform trans_0_0_0_0_0_7_0_16 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_16( g );
		g.setTransform( trans_0_0_0_0_0_7_0_16 );
		// _0_0_0_0_0_7_0_17
		AffineTransform trans_0_0_0_0_0_7_0_17 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_17( g );
		g.setTransform( trans_0_0_0_0_0_7_0_17 );
		// _0_0_0_0_0_7_0_18
		AffineTransform trans_0_0_0_0_0_7_0_18 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_18( g );
		g.setTransform( trans_0_0_0_0_0_7_0_18 );
		// _0_0_0_0_0_7_0_19
		AffineTransform trans_0_0_0_0_0_7_0_19 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_19( g );
		g.setTransform( trans_0_0_0_0_0_7_0_19 );
		// _0_0_0_0_0_7_0_20
		AffineTransform trans_0_0_0_0_0_7_0_20 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_20( g );
		g.setTransform( trans_0_0_0_0_0_7_0_20 );
		// _0_0_0_0_0_7_0_21
		AffineTransform trans_0_0_0_0_0_7_0_21 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_21( g );
		g.setTransform( trans_0_0_0_0_0_7_0_21 );
		// _0_0_0_0_0_7_0_22
		AffineTransform trans_0_0_0_0_0_7_0_22 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_22( g );
		g.setTransform( trans_0_0_0_0_0_7_0_22 );
		// _0_0_0_0_0_7_0_23
		AffineTransform trans_0_0_0_0_0_7_0_23 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_23( g );
		g.setTransform( trans_0_0_0_0_0_7_0_23 );
		// _0_0_0_0_0_7_0_24
		AffineTransform trans_0_0_0_0_0_7_0_24 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_24( g );
		g.setTransform( trans_0_0_0_0_0_7_0_24 );
		// _0_0_0_0_0_7_0_25
		AffineTransform trans_0_0_0_0_0_7_0_25 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_25( g );
		g.setTransform( trans_0_0_0_0_0_7_0_25 );
		// _0_0_0_0_0_7_0_26
		AffineTransform trans_0_0_0_0_0_7_0_26 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_26( g );
		g.setTransform( trans_0_0_0_0_0_7_0_26 );
		// _0_0_0_0_0_7_0_27
		AffineTransform trans_0_0_0_0_0_7_0_27 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_27( g );
		g.setTransform( trans_0_0_0_0_0_7_0_27 );
		// _0_0_0_0_0_7_0_28
		AffineTransform trans_0_0_0_0_0_7_0_28 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_28( g );
		g.setTransform( trans_0_0_0_0_0_7_0_28 );
		// _0_0_0_0_0_7_0_29
		AffineTransform trans_0_0_0_0_0_7_0_29 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_29( g );
		g.setTransform( trans_0_0_0_0_0_7_0_29 );
		// _0_0_0_0_0_7_0_30
		AffineTransform trans_0_0_0_0_0_7_0_30 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_30( g );
		g.setTransform( trans_0_0_0_0_0_7_0_30 );
		// _0_0_0_0_0_7_0_31
		AffineTransform trans_0_0_0_0_0_7_0_31 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_31( g );
		g.setTransform( trans_0_0_0_0_0_7_0_31 );
		// _0_0_0_0_0_7_0_32
		AffineTransform trans_0_0_0_0_0_7_0_32 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_32( g );
		g.setTransform( trans_0_0_0_0_0_7_0_32 );
		// _0_0_0_0_0_7_0_33
		AffineTransform trans_0_0_0_0_0_7_0_33 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_33( g );
		g.setTransform( trans_0_0_0_0_0_7_0_33 );
		// _0_0_0_0_0_7_0_34
		AffineTransform trans_0_0_0_0_0_7_0_34 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_34( g );
		g.setTransform( trans_0_0_0_0_0_7_0_34 );
		// _0_0_0_0_0_7_0_35
		g.setComposite( AlphaComposite.getInstance( 3, 1.0f * origAlpha ) );
		AffineTransform trans_0_0_0_0_0_7_0_35 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_35( g );
		g.setTransform( trans_0_0_0_0_0_7_0_35 );
		// _0_0_0_0_0_7_0_36
		AffineTransform trans_0_0_0_0_0_7_0_36 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_36( g );
		g.setTransform( trans_0_0_0_0_0_7_0_36 );
		// _0_0_0_0_0_7_0_37
		AffineTransform trans_0_0_0_0_0_7_0_37 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_37( g );
		g.setTransform( trans_0_0_0_0_0_7_0_37 );
		// _0_0_0_0_0_7_0_38
		AffineTransform trans_0_0_0_0_0_7_0_38 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_38( g );
		g.setTransform( trans_0_0_0_0_0_7_0_38 );
		// _0_0_0_0_0_7_0_39
		AffineTransform trans_0_0_0_0_0_7_0_39 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_39( g );
		g.setTransform( trans_0_0_0_0_0_7_0_39 );
		// _0_0_0_0_0_7_0_40
		AffineTransform trans_0_0_0_0_0_7_0_40 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_40( g );
		g.setTransform( trans_0_0_0_0_0_7_0_40 );
		// _0_0_0_0_0_7_0_41
		AffineTransform trans_0_0_0_0_0_7_0_41 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_41( g );
		g.setTransform( trans_0_0_0_0_0_7_0_41 );
		// _0_0_0_0_0_7_0_42
		AffineTransform trans_0_0_0_0_0_7_0_42 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_42( g );
		g.setTransform( trans_0_0_0_0_0_7_0_42 );
		// _0_0_0_0_0_7_0_43
		AffineTransform trans_0_0_0_0_0_7_0_43 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_43( g );
		g.setTransform( trans_0_0_0_0_0_7_0_43 );
		// _0_0_0_0_0_7_0_44
		AffineTransform trans_0_0_0_0_0_7_0_44 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_44( g );
		g.setTransform( trans_0_0_0_0_0_7_0_44 );
		// _0_0_0_0_0_7_0_45
		AffineTransform trans_0_0_0_0_0_7_0_45 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_45( g );
		g.setTransform( trans_0_0_0_0_0_7_0_45 );
		// _0_0_0_0_0_7_0_46
		g.setComposite( AlphaComposite.getInstance( 3, 0.6456311f * origAlpha ) );
		AffineTransform trans_0_0_0_0_0_7_0_46 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_46( g );
		g.setTransform( trans_0_0_0_0_0_7_0_46 );
		// _0_0_0_0_0_7_0_47
		AffineTransform trans_0_0_0_0_0_7_0_47 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_47( g );
		g.setTransform( trans_0_0_0_0_0_7_0_47 );
		// _0_0_0_0_0_7_0_48
		AffineTransform trans_0_0_0_0_0_7_0_48 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_48( g );
		g.setTransform( trans_0_0_0_0_0_7_0_48 );
		// _0_0_0_0_0_7_0_49
		AffineTransform trans_0_0_0_0_0_7_0_49 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_49( g );
		g.setTransform( trans_0_0_0_0_0_7_0_49 );
		// _0_0_0_0_0_7_0_50
		AffineTransform trans_0_0_0_0_0_7_0_50 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_50( g );
		g.setTransform( trans_0_0_0_0_0_7_0_50 );
		// _0_0_0_0_0_7_0_51
		AffineTransform trans_0_0_0_0_0_7_0_51 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_51( g );
		g.setTransform( trans_0_0_0_0_0_7_0_51 );
		// _0_0_0_0_0_7_0_52
		AffineTransform trans_0_0_0_0_0_7_0_52 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_52( g );
		g.setTransform( trans_0_0_0_0_0_7_0_52 );
		// _0_0_0_0_0_7_0_53
		AffineTransform trans_0_0_0_0_0_7_0_53 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_53( g );
		g.setTransform( trans_0_0_0_0_0_7_0_53 );
		// _0_0_0_0_0_7_0_54
		AffineTransform trans_0_0_0_0_0_7_0_54 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_54( g );
		g.setTransform( trans_0_0_0_0_0_7_0_54 );
		// _0_0_0_0_0_7_0_55
		AffineTransform trans_0_0_0_0_0_7_0_55 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_55( g );
		g.setTransform( trans_0_0_0_0_0_7_0_55 );
		// _0_0_0_0_0_7_0_56
		AffineTransform trans_0_0_0_0_0_7_0_56 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_56( g );
		g.setTransform( trans_0_0_0_0_0_7_0_56 );
		// _0_0_0_0_0_7_0_57
		AffineTransform trans_0_0_0_0_0_7_0_57 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_57( g );
		g.setTransform( trans_0_0_0_0_0_7_0_57 );
		// _0_0_0_0_0_7_0_58
		AffineTransform trans_0_0_0_0_0_7_0_58 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_58( g );
		g.setTransform( trans_0_0_0_0_0_7_0_58 );
		// _0_0_0_0_0_7_0_59
		AffineTransform trans_0_0_0_0_0_7_0_59 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_59( g );
		g.setTransform( trans_0_0_0_0_0_7_0_59 );
		// _0_0_0_0_0_7_0_60
		AffineTransform trans_0_0_0_0_0_7_0_60 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_60( g );
		g.setTransform( trans_0_0_0_0_0_7_0_60 );
		// _0_0_0_0_0_7_0_61
		AffineTransform trans_0_0_0_0_0_7_0_61 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_61( g );
		g.setTransform( trans_0_0_0_0_0_7_0_61 );
		// _0_0_0_0_0_7_0_62
		AffineTransform trans_0_0_0_0_0_7_0_62 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_62( g );
		g.setTransform( trans_0_0_0_0_0_7_0_62 );
		// _0_0_0_0_0_7_0_63
		AffineTransform trans_0_0_0_0_0_7_0_63 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_63( g );
		g.setTransform( trans_0_0_0_0_0_7_0_63 );
		// _0_0_0_0_0_7_0_64
		AffineTransform trans_0_0_0_0_0_7_0_64 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_64( g );
		g.setTransform( trans_0_0_0_0_0_7_0_64 );
		// _0_0_0_0_0_7_0_65
		AffineTransform trans_0_0_0_0_0_7_0_65 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_65( g );
		g.setTransform( trans_0_0_0_0_0_7_0_65 );
		// _0_0_0_0_0_7_0_66
		AffineTransform trans_0_0_0_0_0_7_0_66 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_66( g );
		g.setTransform( trans_0_0_0_0_0_7_0_66 );
		// _0_0_0_0_0_7_0_67
		AffineTransform trans_0_0_0_0_0_7_0_67 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_67( g );
		g.setTransform( trans_0_0_0_0_0_7_0_67 );
		// _0_0_0_0_0_7_0_68
		AffineTransform trans_0_0_0_0_0_7_0_68 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_68( g );
		g.setTransform( trans_0_0_0_0_0_7_0_68 );
		// _0_0_0_0_0_7_0_69
		AffineTransform trans_0_0_0_0_0_7_0_69 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_0_69( g );
		g.setTransform( trans_0_0_0_0_0_7_0_69 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_0( Graphics2D g ) {
		GeneralPath shape79 = new GeneralPath();
		shape79.moveTo( -132.39502, -132.53253 );
		shape79.curveTo( -132.78226, -132.74329, -132.38332, -134.5506, -134.23735, -134.31387 );
		shape79.curveTo( -136.09142, -134.07713, -142.0526, -134.12912, -142.0526, -134.12912 );
		shape79.lineTo( -144.99408, -131.88876 );
		shape79.lineTo( -146.26927, -125.86054 );
		shape79.lineTo( -149.37119, -116.595924 );
		shape79.lineTo( -158.95056, -111.090294 );
		shape79.lineTo( -155.8683, -106.71347 );
		shape79.lineTo( -153.53702, -106.79427 );
		shape79.lineTo( -153.23976, -103.95916 );
		shape79.lineTo( -155.78229, -98.38711 );
		shape79.lineTo( -154.99219, -92.94785 );
		shape79.lineTo( -156.88928, -93.020035 );
		shape79.lineTo( -155.4694, -87.96476 );
		shape79.lineTo( -148.56165, -86.25559 );
		shape79.lineTo( -92.603004, -122.944405 );
		shape79.lineTo( -96.71791, -127.02674 );
		shape79.lineTo( -97.73094, -137.80421 );
		shape79.lineTo( -117.87928, -133.89806 );
		shape79.lineTo( -127.34128, -135.7256 );
		shape79.lineTo( -122.56923, -131.59708 );
		shape79.lineTo( -132.395, -132.53252 );
		shape79.lineTo( -132.395, -132.53255 );
		shape79.closePath();
		g.setPaint( new LinearGradientPaint( new Point2D.Double( -163.8931884765625, -87.38768768310547 ), new Point2D.Double( -112.16423797607422, -119.7637710571289 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 186, 189, 182, 255 ), new Color( 218, 223, 232, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) ) );
		g.fill( shape79 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_1( Graphics2D g ) {
		GeneralPath shape80 = new GeneralPath();
		shape80.moveTo( -134.39539, -101.38344 );
		shape80.lineTo( -147.14307, -103.441956 );
		shape80.curveTo( -147.14307, -103.441956, -148.18748, -95.99042, -148.47304, -95.7046 );
		shape80.curveTo( -148.75858, -95.41878, -153.93748, -91.25563, -153.93748, -91.25563 );
		shape80.lineTo( -144.36986, -92.17369 );
		shape80.lineTo( -142.10507, -93.6663 );
		shape80.lineTo( -132.05634, -92.53165 );
		shape80.lineTo( -134.39539, -101.38344 );
		shape80.lineTo( -134.39539, -101.38344 );
		shape80.lineTo( -134.39539, -101.38344 );
		shape80.closePath();
		g.setPaint( new Color( 186, 189, 182, 255 ) );
		g.fill( shape80 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_2( Graphics2D g ) {
		GeneralPath shape81 = new GeneralPath();
		shape81.moveTo( -75.17744, -117.08164 );
		shape81.lineTo( -75.74074, -110.14688 );
		shape81.lineTo( -85.51964, -101.1681 );
		shape81.lineTo( -96.585365, -102.12953 );
		shape81.lineTo( -94.82121, -110.01991 );
		shape81.lineTo( -88.40236, -114.157074 );
		shape81.lineTo( -77.18797, -115.63235 );
		shape81.lineTo( -76.08882, -117.08453 );
		shape81.lineTo( -75.17743, -117.081535 );
		shape81.lineTo( -75.17743, -117.08164 );
		shape81.closePath();
		g.setPaint( new LinearGradientPaint( new Point2D.Double( -228.375, 89.875 ), new Point2D.Double( -196.875, 89.875 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 189, 208, 225, 255 ), new Color( 255, 255, 255, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( -0.7197251915931702f, 0.4388335943222046f, 0.5945504903793335f, 0.5312241911888123f, -291.4935302734375f, -63.27872848510742f ) ) );
		g.fill( shape81 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_3( Graphics2D g ) {
		GeneralPath shape82 = new GeneralPath();
		shape82.moveTo( -114.43921, -99.37829 );
		shape82.lineTo( -115.87085, -97.2765 );
		shape82.lineTo( -128.43471, -94.978424 );
		shape82.lineTo( -129.20921, -92.83044 );
		shape82.lineTo( -134.51717, -92.163536 );
		shape82.lineTo( -127.75409, -96.398865 );
		shape82.lineTo( -116.59839, -98.06178 );
		shape82.lineTo( -115.40927, -99.56883 );
		shape82.lineTo( -114.43921, -99.37828 );
		shape82.lineTo( -114.43921, -99.37828 );
		shape82.lineTo( -114.43921, -99.37828 );
		shape82.closePath();
		g.setPaint( new Color( 255, 255, 255, 255 ) );
		g.fill( shape82 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_4( Graphics2D g ) {
		GeneralPath shape83 = new GeneralPath();
		shape83.moveTo( -84.48459, -118.00424 );
		shape83.lineTo( -106.1819, -114.941124 );
		shape83.lineTo( -112.12742, -117.68386 );
		shape83.lineTo( -111.67369, -116.06132 );
		shape83.lineTo( -106.71387, -113.38787 );
		shape83.lineTo( -108.39193, -112.58816 );
		shape83.lineTo( -116.739136, -113.419655 );
		shape83.lineTo( -106.73734, -111.921234 );
		shape83.lineTo( -90.05072, -114.05184 );
		shape83.lineTo( -84.48458, -118.004234 );
		shape83.closePath();
		g.fill( shape83 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_5( Graphics2D g ) {
		GeneralPath shape84 = new GeneralPath();
		shape84.moveTo( -106.22024, -109.61507 );
		shape84.lineTo( -102.92678, -100.45148 );
		shape84.lineTo( -117.9588, -102.79294 );
		shape84.lineTo( -121.7139, -99.3862 );
		shape84.lineTo( -123.235466, -102.36856 );
		shape84.lineTo( -118.744995, -106.33539 );
		shape84.lineTo( -106.22024, -109.61507 );
		shape84.lineTo( -106.22024, -109.61507 );
		shape84.lineTo( -106.22024, -109.61507 );
		shape84.closePath();
		g.setPaint( new Color( 185, 200, 219, 255 ) );
		g.fill( shape84 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_6( Graphics2D g ) {
		GeneralPath shape85 = new GeneralPath();
		shape85.moveTo( -147.66048, -96.62752 );
		shape85.curveTo( -147.21457, -96.22911, -137.18544, -86.59201, -137.18544, -86.59201 );
		shape85.lineTo( -136.75516, -88.64183 );
		shape85.lineTo( -146.52614, -96.42542 );
		shape85.lineTo( -147.66048, -96.627525 );
		shape85.closePath();
		g.setPaint( new Color( 180, 198, 218, 255 ) );
		g.fill( shape85 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_7( Graphics2D g ) {
		GeneralPath shape86 = new GeneralPath();
		shape86.moveTo( -91.19981, -104.92167 );
		shape86.lineTo( -89.09154, -94.923706 );
		shape86.lineTo( -94.74372, -94.158646 );
		shape86.lineTo( -94.40735, -90.34193 );
		shape86.lineTo( -97.86901, -98.84438 );
		shape86.lineTo( -109.36112, -95.859184 );
		shape86.lineTo( -113.828125, -90.78948 );
		shape86.lineTo( -119.72672, -91.32649 );
		shape86.lineTo( -117.778755, -94.860275 );
		shape86.lineTo( -109.71316, -96.98514 );
		shape86.lineTo( -104.52645, -99.92417 );
		shape86.lineTo( -96.42565, -100.394745 );
		shape86.lineTo( -91.19982, -104.92168 );
		shape86.lineTo( -91.19982, -104.92168 );
		shape86.lineTo( -91.19982, -104.92168 );
		shape86.closePath();
		g.setPaint( new Color( 177, 186, 203, 255 ) );
		g.fill( shape86 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_8( Graphics2D g ) {
		GeneralPath shape87 = new GeneralPath();
		shape87.moveTo( -137.23425, -121.96007 );
		shape87.lineTo( -139.48343, -115.449715 );
		shape87.lineTo( -133.54181, -114.60378 );
		shape87.lineTo( -135.44278, -121.71177 );
		shape87.lineTo( -137.23425, -121.96006 );
		shape87.closePath();
		g.setPaint( new Color( 217, 225, 236, 255 ) );
		g.fill( shape87 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_9( Graphics2D g ) {
		GeneralPath shape88 = new GeneralPath();
		shape88.moveTo( -137.4622, -109.68778 );
		shape88.lineTo( -126.73609, -99.4702 );
		shape88.lineTo( -134.0427, -101.561104 );
		shape88.lineTo( -139.06201, -99.54806 );
		shape88.lineTo( -145.45647, -98.922165 );
		shape88.lineTo( -137.4622, -109.68778 );
		shape88.lineTo( -137.4622, -109.68778 );
		shape88.lineTo( -137.4622, -109.68778 );
		shape88.closePath();
		g.setPaint( new Color( 186, 189, 182, 255 ) );
		g.fill( shape88 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_10( Graphics2D g ) {
		GeneralPath shape89 = new GeneralPath();
		shape89.moveTo( -135.87839, -117.10134 );
		shape89.lineTo( -126.72545, -108.38234 );
		shape89.lineTo( -132.96043, -110.16657 );
		shape89.lineTo( -137.24358, -108.44878 );
		shape89.lineTo( -142.70018, -107.91469 );
		shape89.lineTo( -135.8784, -117.10135 );
		shape89.lineTo( -135.8784, -117.10135 );
		shape89.lineTo( -135.8784, -117.10135 );
		shape89.closePath();
		g.setPaint( new Color( 252, 253, 255, 255 ) );
		g.fill( shape89 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_11( Graphics2D g ) {
		GeneralPath shape90 = new GeneralPath();
		shape90.moveTo( -143.28148, -136.88266 );
		shape90.lineTo( -137.94614, -137.11938 );
		shape90.lineTo( -140.47691, -136.13489 );
		shape90.lineTo( -143.28148, -136.88264 );
		shape90.closePath();
		g.setPaint( new Color( 192, 198, 214, 255 ) );
		g.fill( shape90 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_12( Graphics2D g ) {
		GeneralPath shape91 = new GeneralPath();
		shape91.moveTo( -123.52238, -114.61906 );
		shape91.lineTo( -125.11831, -109.95932 );
		shape91.lineTo( -122.77531, -109.48872 );
		shape91.lineTo( -124.82888, -105.77883 );
		shape91.lineTo( -117.46343, -110.82831 );
		shape91.lineTo( -119.48567, -115.06943 );
		shape91.lineTo( -123.522385, -114.61906 );
		shape91.lineTo( -123.522385, -114.61906 );
		shape91.lineTo( -123.522385, -114.61906 );
		shape91.closePath();
		g.fill( shape91 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_13( Graphics2D g ) {
		GeneralPath shape92 = new GeneralPath();
		shape92.moveTo( -133.04099, -121.66503 );
		shape92.curveTo( -133.72943, -121.46871, -139.83142, -120.42938, -139.83142, -120.42938 );
		shape92.lineTo( -140.17957, -117.089035 );
		shape92.lineTo( -137.98518, -119.32075 );
		shape92.lineTo( -133.04099, -121.66504 );
		shape92.closePath();
		g.fill( shape92 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_14( Graphics2D g ) {
		GeneralPath shape93 = new GeneralPath();
		shape93.moveTo( -115.65102, -115.25619 );
		shape93.lineTo( -106.83447, -105.2149 );
		shape93.lineTo( -93.55867, -111.74543 );
		shape93.lineTo( -95.08415, -116.6246 );
		shape93.lineTo( -102.86811, -116.6824 );
		shape93.lineTo( -105.66095, -114.30923 );
		shape93.lineTo( -114.6966, -114.94442 );
		shape93.lineTo( -115.65101, -115.25623 );
		shape93.lineTo( -115.65101, -115.2562 );
		shape93.lineTo( -115.65101, -115.2562 );
		shape93.closePath();
		g.fill( shape93 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_15( Graphics2D g ) {
		GeneralPath shape94 = new GeneralPath();
		shape94.moveTo( -115.36851, -107.04673 );
		shape94.lineTo( -121.145836, -107.88111 );
		shape94.lineTo( -134.41379, -105.265465 );
		shape94.lineTo( -130.15807, -99.70493 );
		shape94.lineTo( -132.10603, -96.17115 );
		shape94.lineTo( -130.1933, -96.22025 );
		shape94.lineTo( -128.83206, -99.061134 );
		shape94.lineTo( -134.15955, -105.308784 );
		shape94.lineTo( -115.368515, -107.046745 );
		shape94.lineTo( -115.368515, -107.04674 );
		shape94.lineTo( -115.368515, -107.04674 );
		shape94.closePath();
		g.fill( shape94 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_16( Graphics2D g ) {
		GeneralPath shape95 = new GeneralPath();
		shape95.moveTo( -134.92313, -119.70906 );
		shape95.lineTo( -137.02757, -114.96271 );
		shape95.lineTo( -132.78746, -114.41993 );
		shape95.lineTo( -134.92313, -119.70906 );
		shape95.lineTo( -134.92313, -119.70906 );
		shape95.lineTo( -134.92313, -119.70906 );
		shape95.closePath();
		g.fill( shape95 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_17( Graphics2D g ) {
		GeneralPath shape96 = new GeneralPath();
		shape96.moveTo( -144.7842, -104.21853 );
		shape96.curveTo( -144.7842, -104.21853, -135.91676, -100.3527, -136.46829, -100.57501 );
		shape96.curveTo( -137.01982, -100.79731, -146.09067, -103.0868, -146.09067, -103.0868 );
		shape96.lineTo( -153.01411, -99.53571 );
		shape96.lineTo( -144.78421, -104.21854 );
		shape96.lineTo( -144.78421, -104.21854 );
		shape96.lineTo( -144.78421, -104.21854 );
		shape96.closePath();
		g.fill( shape96 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_18( Graphics2D g ) {
		GeneralPath shape97 = new GeneralPath();
		shape97.moveTo( -106.29318, -120.61398 );
		shape97.lineTo( -118.26643, -114.542496 );
		shape97.lineTo( -109.508545, -107.25838 );
		shape97.lineTo( -108.33502, -116.35268 );
		shape97.lineTo( -104.885056, -116.110146 );
		shape97.lineTo( -103.21484, -112.99499 );
		shape97.lineTo( -102.068756, -114.81096 );
		shape97.lineTo( -105.59303, -117.68938 );
		shape97.lineTo( -106.29318, -120.61398 );
		shape97.lineTo( -106.29318, -120.61398 );
		shape97.lineTo( -106.29318, -120.61398 );
		shape97.closePath();
		g.fill( shape97 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_19( Graphics2D g ) {
		GeneralPath shape98 = new GeneralPath();
		shape98.moveTo( -91.16675, -96.86413 );
		shape98.curveTo( -91.44839, -97.251, -96.423836, -99.803185, -96.423836, -99.803185 );
		shape98.lineTo( -95.0509, -96.953636 );
		shape98.lineTo( -98.83337, -96.54658 );
		shape98.lineTo( -105.967995, -97.782265 );
		shape98.lineTo( -95.074394, -92.9175 );
		shape98.lineTo( -91.16675, -96.86412 );
		shape98.closePath();
		g.fill( shape98 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_20( Graphics2D g ) {
		GeneralPath shape99 = new GeneralPath();
		shape99.moveTo( -138.2815, -105.49613 );
		shape99.curveTo( -138.14458, -105.91475, -137.93335, -108.83647, -137.93335, -108.83647 );
		shape99.lineTo( -132.26942, -111.6196 );
		shape99.lineTo( -131.47539, -109.42253 );
		shape99.lineTo( -138.2815, -105.49613 );
		shape99.closePath();
		g.fill( shape99 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_21( Graphics2D g ) {
		GeneralPath shape100 = new GeneralPath();
		shape100.moveTo( -103.72688, -141.6636 );
		shape100.lineTo( -114.068985, -141.16707 );
		shape100.lineTo( -108.31122, -139.53874 );
		shape100.lineTo( -103.72689, -141.6636 );
		shape100.lineTo( -103.72689, -141.6636 );
		shape100.lineTo( -103.72689, -141.6636 );
		shape100.closePath();
		g.fill( shape100 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_22( Graphics2D g ) {
		GeneralPath shape101 = new GeneralPath();
		shape101.moveTo( -128.75777, -107.38237 );
		shape101.lineTo( -135.247, -109.1233 );
		shape101.lineTo( -131.02255, -105.889755 );
		shape101.lineTo( -128.75777, -107.38236 );
		shape101.closePath();
		g.fill( shape101 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_23( Graphics2D g ) {
		GeneralPath shape102 = new GeneralPath();
		shape102.moveTo( -115.52779, -113.62524 );
		shape102.lineTo( -125.553055, -113.657036 );
		shape102.lineTo( -131.0018, -117.03782 );
		shape102.lineTo( -130.04741, -114.15651 );
		shape102.lineTo( -126.096756, -112.65522 );
		shape102.lineTo( -115.52778, -113.62525 );
		shape102.lineTo( -115.52778, -113.625244 );
		shape102.closePath();
		g.fill( shape102 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_24( Graphics2D g ) {
		GeneralPath shape103 = new GeneralPath();
		shape103.moveTo( -100.97263, -122.36054 );
		shape103.lineTo( -98.96604, -117.99816 );
		shape103.lineTo( -103.1631, -120.80152 );
		shape103.lineTo( -100.97263, -122.36054 );
		shape103.lineTo( -100.97263, -122.36054 );
		shape103.lineTo( -100.97263, -122.36054 );
		shape103.closePath();
		g.fill( shape103 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_25( Graphics2D g ) {
		GeneralPath shape104 = new GeneralPath();
		shape104.moveTo( -97.46421, -118.17365 );
		shape104.lineTo( -97.31951, -114.79865 );
		shape104.lineTo( -94.16681, -114.821754 );
		shape104.lineTo( -95.70797, -111.87116 );
		shape104.lineTo( -99.00931, -111.98088 );
		shape104.lineTo( -97.46421, -118.17366 );
		shape104.lineTo( -97.46421, -118.17365 );
		shape104.lineTo( -97.46421, -118.17365 );
		shape104.closePath();
		g.setPaint( new Color( 145, 159, 186, 255 ) );
		g.fill( shape104 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_26( Graphics2D g ) {
		GeneralPath shape105 = new GeneralPath();
		shape105.moveTo( -144.14174, -130.13132 );
		shape105.curveTo( -143.95007, -129.68959, -142.97221, -125.70543, -142.97221, -125.70543 );
		shape105.lineTo( -146.34395, -126.55424 );
		shape105.lineTo( -144.14172, -130.13132 );
		shape105.lineTo( -144.14172, -130.13132 );
		shape105.lineTo( -144.14172, -130.13132 );
		shape105.closePath();
		g.fill( shape105 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_27( Graphics2D g ) {
		GeneralPath shape106 = new GeneralPath();
		shape106.moveTo( -88.66009, -101.76395 );
		shape106.lineTo( -89.86095, -98.23883 );
		shape106.lineTo( -95.79867, -97.18795 );
		shape106.lineTo( -99.59286, -97.33231 );
		shape106.lineTo( -95.38013, -99.7892 );
		shape106.lineTo( -94.656494, -98.33122 );
		shape106.lineTo( -91.0344, -99.42252 );
		shape106.lineTo( -88.66008, -101.76394 );
		shape106.lineTo( -88.66008, -101.76394 );
		shape106.lineTo( -88.66008, -101.76394 );
		shape106.closePath();
		g.fill( shape106 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_28( Graphics2D g ) {
		GeneralPath shape107 = new GeneralPath();
		shape107.moveTo( -143.93529, -125.38695 );
		shape107.lineTo( -144.79587, -118.7178 );
		shape107.lineTo( -141.73703, -120.58285 );
		shape107.lineTo( -142.41762, -124.3014 );
		shape107.lineTo( -139.60522, -124.89902 );
		shape107.lineTo( -143.93529, -125.38695 );
		shape107.closePath();
		g.fill( shape107 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_29( Graphics2D g ) {
		GeneralPath shape108 = new GeneralPath();
		shape108.moveTo( -118.91435, -123.78149 );
		shape108.curveTo( -118.91435, -123.78149, -122.17662, -115.201096, -121.63683, -115.53023 );
		shape108.curveTo( -121.09703, -115.85936, -118.143814, -117.54829, -118.143814, -117.54829 );
		shape108.lineTo( -118.91435, -123.78149 );
		shape108.closePath();
		g.fill( shape108 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_30( Graphics2D g ) {
		GeneralPath shape109 = new GeneralPath();
		shape109.moveTo( -111.46347, -107.91678 );
		shape109.curveTo( -111.46347, -107.91678, -107.59883, -114.74182, -107.80614, -115.06228 );
		shape109.curveTo( -108.01345, -115.38275, -109.30814, -118.83859, -109.30814, -118.83859 );
		shape109.lineTo( -106.53488, -115.27882 );
		shape109.lineTo( -107.7162, -109.97815 );
		shape109.lineTo( -111.46346, -107.91679 );
		shape109.closePath();
		g.fill( shape109 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_31( Graphics2D g ) {
		GeneralPath shape110 = new GeneralPath();
		shape110.moveTo( -118.62937, -101.40666 );
		shape110.lineTo( -115.25369, -103.80004 );
		shape110.lineTo( -114.40486, -108.45112 );
		shape110.lineTo( -119.25907, -111.30068 );
		shape110.lineTo( -116.80265, -107.21257 );
		shape110.lineTo( -118.62937, -101.40666 );
		shape110.lineTo( -118.62937, -101.40666 );
		shape110.lineTo( -118.62937, -101.40666 );
		shape110.closePath();
		g.fill( shape110 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_32( Graphics2D g ) {
		GeneralPath shape111 = new GeneralPath();
		shape111.moveTo( -136.38467, -101.42022 );
		shape111.lineTo( -136.65065, -104.49785 );
		shape111.lineTo( -130.97108, -107.40223 );
		shape111.lineTo( -136.8071, -105.85477 );
		shape111.lineTo( -142.87779, -107.62746 );
		shape111.lineTo( -137.51509, -104.86451 );
		shape111.lineTo( -138.90372, -99.8843 );
		shape111.lineTo( -136.38467, -101.42022 );
		shape111.lineTo( -136.38467, -101.42022 );
		shape111.lineTo( -136.38467, -101.42022 );
		shape111.closePath();
		g.fill( shape111 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_33( Graphics2D g ) {
		GeneralPath shape112 = new GeneralPath();
		shape112.moveTo( -135.45757, -125.49986 );
		shape112.lineTo( -145.1582, -122.26637 );
		shape112.lineTo( -155.2343, -126.400696 );
		shape112.lineTo( -158.67255, -123.52229 );
		shape112.lineTo( -152.69577, -115.883064 );
		shape112.lineTo( -151.1272, -123.54247 );
		shape112.lineTo( -143.78917, -121.31363 );
		shape112.lineTo( -138.09006, -122.44245 );
		shape112.lineTo( -135.45757, -125.49986 );
		shape112.lineTo( -135.45757, -125.49986 );
		shape112.lineTo( -135.45757, -125.49986 );
		shape112.closePath();
		g.setPaint( new Color( 255, 255, 255, 255 ) );
		g.fill( shape112 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_34( Graphics2D g ) {
		GeneralPath shape113 = new GeneralPath();
		shape113.moveTo( -94.8302, -126.59109 );
		shape113.lineTo( -95.86286, -123.727104 );
		shape113.lineTo( -101.51505, -120.39255 );
		shape113.lineTo( -101.01435, -124.27278 );
		shape113.lineTo( -94.45469, -126.93176 );
		shape113.lineTo( -94.8302, -126.591095 );
		shape113.lineTo( -94.8302, -126.591095 );
		shape113.lineTo( -94.8302, -126.591095 );
		shape113.closePath();
		g.setPaint( new Color( 223, 227, 235, 255 ) );
		g.fill( shape113 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_35( Graphics2D g ) {
		GeneralPath shape114 = new GeneralPath();
		shape114.moveTo( -125.35453, -117.29214 );
		shape114.curveTo( -125.26063, -117.435585, -124.57269, -117.22394, -124.60601, -117.95008 );
		shape114.curveTo( -124.63931, -118.67618, -124.43127, -120.9851, -124.43127, -120.9851 );
		shape114.lineTo( -125.20692, -122.19585 );
		shape114.lineTo( -127.503265, -122.88013 );
		shape114.lineTo( -130.99648, -124.37446 );
		shape114.lineTo( -132.82852, -128.26099 );
		shape114.lineTo( -134.62215, -127.204254 );
		shape114.lineTo( -134.66435, -126.2981 );
		shape114.lineTo( -135.77261, -126.2722 );
		shape114.lineTo( -137.85222, -127.43332 );
		shape114.lineTo( -139.98538, -127.29853 );
		shape114.lineTo( -139.89758, -128.03157 );
		shape114.lineTo( -141.90178, -127.64058 );
		shape114.lineTo( -142.782, -125.01699 );
		shape114.lineTo( -130.32521, -102.17086 );
		shape114.lineTo( -128.61317, -103.63713 );
		shape114.lineTo( -124.40388, -103.69003 );
		shape114.lineTo( -125.282814, -111.622696 );
		shape114.lineTo( -124.27621, -115.23257 );
		shape114.lineTo( -126.02685, -113.51305 );
		shape114.lineTo( -125.35456, -117.292046 );
		shape114.lineTo( -125.35454, -117.29214 );
		shape114.lineTo( -125.35454, -117.29214 );
		shape114.closePath();
		g.setPaint( new LinearGradientPaint( new Point2D.Double( -190.5, 104.1875 ), new Point2D.Double( -96.75, 104.1875 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 237, 244, 252, 255 ), new Color( 192, 198, 214, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( -0.14740629494190216f, -0.2927992045879364f, -0.22464419901371002f, 0.2137041985988617f, -128.72862243652344f, -182.0675811767578f ) ) );
		g.fill( shape114 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_36( Graphics2D g ) {
		GeneralPath shape115 = new GeneralPath();
		shape115.moveTo( -122.48882, -115.01198 );
		shape115.lineTo( -119.4038, -127.55086 );
		shape115.curveTo( -119.4038, -127.55086, -126.74617, -129.19583, -127.007904, -129.5036 );
		shape115.curveTo( -127.269646, -129.81137, -130.99931, -135.31067, -130.99931, -135.31067 );
		shape115.lineTo( -130.85979, -125.70011 );
		shape115.lineTo( -129.55566, -123.3218 );
		shape115.lineTo( -131.50108, -113.3981 );
		shape115.lineTo( -122.48883, -115.01197 );
		shape115.lineTo( -122.48883, -115.01197 );
		shape115.lineTo( -122.48883, -115.01197 );
		shape115.closePath();
		g.setPaint( new Color( 252, 253, 255, 255 ) );
		g.fill( shape115 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_37( Graphics2D g ) {
		GeneralPath shape116 = new GeneralPath();
		shape116.moveTo( -91.66337, -94.91746 );
		shape116.lineTo( -98.529655, -96.041 );
		shape116.lineTo( -106.68626, -106.5155 );
		shape116.lineTo( -104.831055, -117.466896 );
		shape116.lineTo( -97.10963, -115.06899 );
		shape116.lineTo( -93.50636, -108.33592 );
		shape116.lineTo( -92.94493, -97.03886 );
		shape116.lineTo( -91.58661, -95.825615 );
		shape116.lineTo( -91.663506, -94.917465 );
		shape116.lineTo( -91.66335, -94.91746 );
		shape116.lineTo( -91.66335, -94.91746 );
		shape116.closePath();
		g.setPaint( new LinearGradientPaint( new Point2D.Double( -228.375, 89.875 ), new Point2D.Double( -196.875, 89.875 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 189, 208, 225, 255 ), new Color( 255, 255, 255, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( -0.3790521025657654f, -0.7529268264770508f, -0.5776677131652832f, 0.5495356917381287f, -127.75569152832031f, -314.8827819824219f ) ) );
		g.fill( shape116 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_38( Graphics2D g ) {
		GeneralPath shape117 = new GeneralPath();
		shape117.moveTo( -106.58678, -93.99409 );
		shape117.lineTo( -108.56562, -95.59138 );
		shape117.lineTo( -109.83776, -108.30018 );
		shape117.lineTo( -111.9159, -109.24623 );
		shape117.lineTo( -112.15037, -114.59078 );
		shape117.lineTo( -108.47716, -107.50666 );
		shape117.lineTo( -107.72394, -96.252884 );
		shape117.lineTo( -106.31823, -94.945526 );
		shape117.lineTo( -106.58678, -93.994095 );
		shape117.lineTo( -106.58678, -93.994095 );
		shape117.lineTo( -106.58678, -93.994095 );
		shape117.closePath();
		g.setPaint( new Color( 255, 255, 255, 255 ) );
		g.fill( shape117 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_39( Graphics2D g ) {
		GeneralPath shape118 = new GeneralPath();
		shape118.moveTo( -137.34933, -86.69519 );
		shape118.lineTo( -138.64369, -108.56939 );
		shape118.lineTo( -135.42805, -114.27304 );
		shape118.lineTo( -137.08203, -113.95231 );
		shape118.lineTo( -140.1487, -109.22551 );
		shape118.lineTo( -140.80975, -110.96287 );
		shape118.lineTo( -139.3044, -119.21522 );
		shape118.lineTo( -141.60858, -109.36779 );
		shape118.lineTo( -140.83752, -92.563385 );
		shape118.lineTo( -137.3493, -86.6952 );
		shape118.lineTo( -137.3493, -86.6952 );
		shape118.lineTo( -137.3493, -86.6952 );
		shape118.closePath();
		g.fill( shape118 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_40( Graphics2D g ) {
		GeneralPath shape119 = new GeneralPath();
		shape119.moveTo( -100.63056, -95.58696 );
		shape119.lineTo( -110.03095, -93.0471 );
		shape119.lineTo( -106.478775, -107.83987 );
		shape119.lineTo( -109.56994, -111.85875 );
		shape119.lineTo( -106.47406, -113.13358 );
		shape119.lineTo( -102.88426, -108.33635 );
		shape119.lineTo( -100.63057, -95.58697 );
		shape119.lineTo( -100.63057, -95.58697 );
		shape119.lineTo( -100.63057, -95.58697 );
		shape119.closePath();
		g.setPaint( new Color( 185, 200, 219, 255 ) );
		g.fill( shape119 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_41( Graphics2D g ) {
		GeneralPath shape120 = new GeneralPath();
		shape120.moveTo( -130.91817, -120.23411 );
		shape120.curveTo( -131.35141, -119.82196, -141.76971, -110.60698, -141.76971, -110.60698 );
		shape120.lineTo( -139.76152, -110.01196 );
		shape120.lineTo( -131.21156, -119.119896 );
		shape120.lineTo( -130.91818, -120.234116 );
		shape120.closePath();
		g.setPaint( new Color( 180, 198, 218, 255 ) );
		g.fill( shape120 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_42( Graphics2D g ) {
		GeneralPath shape121 = new GeneralPath();
		shape121.moveTo( -144.84949, -98.93359 );
		shape121.lineTo( -154.98544, -97.64265 );
		shape121.lineTo( -155.28984, -103.33824 );
		shape121.lineTo( -159.12126, -103.31234 );
		shape121.lineTo( -150.3662, -106.07344 );
		shape121.lineTo( -152.41008, -117.769714 );
		shape121.lineTo( -157.10101, -122.63294 );
		shape121.lineTo( -156.08766, -128.4686 );
		shape121.lineTo( -152.7234, -126.24061 );
		shape121.lineTo( -151.2593, -118.02931 );
		shape121.lineTo( -148.75034, -112.62145 );
		shape121.lineTo( -148.93793, -104.50916 );
		shape121.lineTo( -144.84947, -98.93359 );
		shape121.lineTo( -144.84947, -98.93359 );
		shape121.lineTo( -144.84947, -98.93359 );
		shape121.closePath();
		g.setPaint( new Color( 238, 238, 236, 255 ) );
		g.fill( shape121 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_43( Graphics2D g ) {
		GeneralPath shape122 = new GeneralPath();
		shape122.moveTo( -114.44417, -107.84219 );
		shape122.lineTo( -120.7508, -110.611664 );
		shape122.lineTo( -122.075554, -104.758156 );
		shape122.lineTo( -114.83686, -106.07674 );
		shape122.lineTo( -114.44417, -107.84219 );
		shape122.lineTo( -114.44417, -107.84219 );
		shape122.lineTo( -114.44417, -107.84219 );
		shape122.closePath();
		g.setPaint( new Color( 217, 225, 236, 255 ) );
		g.fill( shape122 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_44( Graphics2D g ) {
		GeneralPath shape123 = new GeneralPath();
		shape123.moveTo( -97.67246, -111.52222 );
		shape123.lineTo( -108.725845, -101.659584 );
		shape123.lineTo( -106.04958, -108.772675 );
		shape123.lineTo( -107.649155, -113.93864 );
		shape123.lineTo( -107.754684, -120.362785 );
		shape123.lineTo( -97.67246, -111.52222 );
		shape123.lineTo( -97.67246, -111.52222 );
		shape123.lineTo( -97.67246, -111.52222 );
		shape123.closePath();
		g.setPaint( new Color( 186, 189, 182, 255 ) );
		g.fill( shape123 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_45( Graphics2D g ) {
		GeneralPath shape124 = new GeneralPath();
		shape124.moveTo( -116.04751, -107.16817 );
		shape124.lineTo( -125.47972, -98.75206 );
		shape124.lineTo( -123.195984, -104.82191 );
		shape124.lineTo( -124.56097, -109.23019 );
		shape124.lineTo( -124.65096, -114.71213 );
		shape124.lineTo( -116.04747, -107.16817 );
		shape124.lineTo( -116.04751, -107.16817 );
		shape124.lineTo( -116.04751, -107.16817 );
		shape124.closePath();
		g.setPaint( new Color( 252, 253, 255, 255 ) );
		g.fill( shape124 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_46( Graphics2D g ) {
		GeneralPath shape125 = new GeneralPath();
		shape125.moveTo( -138.0984, -117.23 );
		shape125.lineTo( -138.2949, -111.893036 );
		shape125.lineTo( -139.07103, -114.49528 );
		shape125.lineTo( -138.0984, -117.23001 );
		shape125.lineTo( -138.0984, -117.23001 );
		shape125.lineTo( -138.0984, -117.23001 );
		shape125.closePath();
		g.setPaint( new Color( 192, 198, 214, 255 ) );
		g.fill( shape125 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_47( Graphics2D g ) {
		GeneralPath shape126 = new GeneralPath();
		shape126.moveTo( -139.65446, -105.60096 );
		shape126.lineTo( -144.16951, -107.569336 );
		shape126.lineTo( -144.82848, -105.27219 );
		shape126.lineTo( -148.35971, -107.619705 );
		shape126.lineTo( -143.92384, -99.8692 );
		shape126.lineTo( -139.53278, -101.54102 );
		shape126.lineTo( -139.65446, -105.60095 );
		shape126.closePath();
		g.fill( shape126 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_48( Graphics2D g ) {
		GeneralPath shape127 = new GeneralPath();
		shape127.moveTo( -146.23312, -112.18406 );
		shape127.curveTo( -146.373, -112.88614, -146.9143, -119.05231, -146.9143, -119.05231 );
		shape127.lineTo( -150.21545, -119.67006 );
		shape127.lineTo( -148.16896, -117.30201 );
		shape127.lineTo( -146.23312, -112.18406 );
		shape127.lineTo( -146.23312, -112.18406 );
		shape127.lineTo( -146.23312, -112.18406 );
		shape127.closePath();
		g.fill( shape127 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_49( Graphics2D g ) {
		GeneralPath shape128 = new GeneralPath();
		shape128.moveTo( -118.81447, -103.36701 );
		shape128.lineTo( -130.1222, -97.418564 );
		shape128.lineTo( -126.5277, -90.02317 );
		shape128.lineTo( -121.57872, -91.27902 );
		shape128.lineTo( -119.98419, -95.8955 );
		shape128.lineTo( -121.69222, -97.37919 );
		shape128.lineTo( -119.300186, -102.77899 );
		shape128.lineTo( -118.81444, -103.36701 );
		shape128.lineTo( -118.81447, -103.36701 );
		shape128.lineTo( -118.81447, -103.36701 );
		shape128.closePath();
		g.fill( shape128 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_50( Graphics2D g ) {
		GeneralPath shape129 = new GeneralPath();
		shape129.moveTo( -122.31061, -121.76999 );
		shape129.lineTo( -121.01069, -127.46068 );
		shape129.lineTo( -122.54229, -140.89699 );
		shape129.lineTo( -128.42947, -137.10597 );
		shape129.lineTo( -131.79373, -139.33395 );
		shape129.lineTo( -131.89983, -137.42354 );
		shape129.lineTo( -129.17862, -135.83652 );
		shape129.lineTo( -122.51971, -140.64006 );
		shape129.lineTo( -122.31058, -121.76999 );
		shape129.lineTo( -122.310585, -121.76999 );
		shape129.lineTo( -122.310585, -121.76999 );
		shape129.closePath();
		g.fill( shape129 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_51( Graphics2D g ) {
		GeneralPath shape130 = new GeneralPath();
		shape130.moveTo( -100.97362, -123.74181 );
		shape130.lineTo( -105.533775, -126.224045 );
		shape130.lineTo( -106.41846, -122.04188 );
		shape130.lineTo( -100.97362, -123.74182 );
		shape130.closePath();
		g.fill( shape130 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_52( Graphics2D g ) {
		GeneralPath shape131 = new GeneralPath();
		shape131.moveTo( -105.03012, -125.40315 );
		shape131.curveTo( -105.03012, -125.40315, -109.60198, -116.87824, -109.3357, -117.409935 );
		shape131.curveTo( -109.06942, -117.94163, -106.05223, -126.79704, -106.05223, -126.79704 );
		shape131.lineTo( -109.03045, -133.98555 );
		shape131.lineTo( -105.03012, -125.40316 );
		shape131.lineTo( -105.03012, -125.40316 );
		shape131.lineTo( -105.03012, -125.40316 );
		shape131.closePath();
		g.fill( shape131 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_53( Graphics2D g ) {
		GeneralPath shape132 = new GeneralPath();
		shape132.moveTo( -103.74948, -119.92615 );
		shape132.lineTo( -108.83049, -132.35213 );
		shape132.lineTo( -116.80052, -124.21347 );
		shape132.lineTo( -107.83126, -122.30667 );
		shape132.lineTo( -108.35263, -118.88771 );
		shape132.lineTo( -111.59292, -117.4755 );
		shape132.lineTo( -109.87582, -116.18598 );
		shape132.lineTo( -106.72121, -119.465355 );
		shape132.lineTo( -103.74947, -119.926155 );
		shape132.lineTo( -103.74947, -119.926155 );
		shape132.lineTo( -103.74947, -119.926155 );
		shape132.closePath();
		g.fill( shape132 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_54( Graphics2D g ) {
		GeneralPath shape133 = new GeneralPath();
		shape133.moveTo( -113.01835, -104.19628 );
		shape133.curveTo( -112.60993, -104.44564, -109.66285, -109.19785, -109.66285, -109.19785 );
		shape133.lineTo( -112.61431, -108.06041 );
		shape133.lineTo( -112.71341, -111.86341 );
		shape133.lineTo( -110.90349, -118.87441 );
		shape133.lineTo( -116.635216, -108.41097 );
		shape133.lineTo( -113.01832, -104.19628 );
		shape133.lineTo( -113.01835, -104.19628 );
		shape133.lineTo( -113.01835, -104.19628 );
		shape133.closePath();
		g.fill( shape133 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_55( Graphics2D g ) {
		GeneralPath shape134 = new GeneralPath();
		shape134.moveTo( -122.34034, -128.05212 );
		shape134.curveTo( -121.9342, -127.88174, -119.03921, -127.434364, -119.03921, -127.434364 );
		shape134.lineTo( -116.72433, -121.56349 );
		shape134.lineTo( -118.97853, -120.950165 );
		shape134.lineTo( -122.34034, -128.05212 );
		shape134.closePath();
		g.fill( shape134 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_56( Graphics2D g ) {
		GeneralPath shape135 = new GeneralPath();
		shape135.moveTo( -131.22617, -83.955605 );
		shape135.lineTo( -130.8828, -94.303925 );
		shape135.lineTo( -132.97247, -88.69708 );
		shape135.lineTo( -131.22617, -83.955605 );
		shape135.lineTo( -131.22617, -83.955605 );
		shape135.lineTo( -131.22617, -83.955605 );
		shape135.closePath();
		g.fill( shape135 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_57( Graphics2D g ) {
		GeneralPath shape136 = new GeneralPath();
		shape136.moveTo( -134.79109, -118.34518 );
		shape136.lineTo( -132.5299, -124.671936 );
		shape136.lineTo( -136.09521, -120.723495 );
		shape136.lineTo( -134.79109, -118.34518 );
		shape136.closePath();
		g.fill( shape136 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_58( Graphics2D g ) {
		GeneralPath shape137 = new GeneralPath();
		shape137.moveTo( -144.89034, -115.10358 );
		shape137.lineTo( -144.04604, -125.093285 );
		shape137.lineTo( -140.23473, -130.25008 );
		shape137.lineTo( -143.18391, -129.53236 );
		shape137.lineTo( -145.00049, -125.7164 );
		shape137.lineTo( -144.89034, -115.10357 );
		shape137.lineTo( -144.89035, -115.10357 );
		shape137.lineTo( -144.89035, -115.10357 );
		shape137.closePath();
		g.fill( shape137 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_59( Graphics2D g ) {
		GeneralPath shape138 = new GeneralPath();
		shape138.moveTo( -103.70774, -100.41719 );
		shape138.lineTo( -108.21842, -98.77079 );
		shape138.lineTo( -105.08409, -102.72682 );
		shape138.lineTo( -103.70774, -100.41719 );
		shape138.lineTo( -103.70774, -100.41719 );
		shape138.lineTo( -103.70774, -100.41719 );
		shape138.closePath();
		g.fill( shape138 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_60( Graphics2D g ) {
		GeneralPath shape139 = new GeneralPath();
		shape139.moveTo( -130.08437, -109.41773 );
		shape139.lineTo( -133.45999, -109.54707 );
		shape139.lineTo( -133.6925, -106.40287 );
		shape139.lineTo( -136.50847, -108.17812 );
		shape139.lineTo( -136.13152, -111.4597 );
		shape139.lineTo( -130.08435, -109.41773 );
		shape139.lineTo( -130.08437, -109.41773 );
		shape139.closePath();
		g.setPaint( new Color( 145, 159, 186, 255 ) );
		g.fill( shape139 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_61( Graphics2D g ) {
		GeneralPath shape140 = new GeneralPath();
		shape140.moveTo( -111.05447, -106.78952 );
		shape140.curveTo( -111.51027, -106.63429, -115.560585, -105.98259, -115.560585, -105.98259 );
		shape140.lineTo( -114.44128, -109.27443 );
		shape140.lineTo( -111.05447, -106.78952 );
		shape140.closePath();
		g.fill( shape140 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_62( Graphics2D g ) {
		GeneralPath shape141 = new GeneralPath();
		shape141.moveTo( -110.96047, -107.81577 );
		shape141.lineTo( -114.376656, -109.298416 );
		shape141.lineTo( -114.94279, -115.30177 );
		shape141.lineTo( -114.49136, -119.071785 );
		shape141.lineTo( -112.384026, -114.673775 );
		shape141.lineTo( -113.89586, -114.070694 );
		shape141.lineTo( -113.10173, -110.37207 );
		shape141.lineTo( -110.96048, -107.81577 );
		shape141.lineTo( -110.96048, -107.81577 );
		shape141.lineTo( -110.96048, -107.81577 );
		shape141.closePath();
		g.fill( shape141 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_63( Graphics2D g ) {
		GeneralPath shape142 = new GeneralPath();
		shape142.moveTo( -114.59449, -120.31398 );
		shape142.lineTo( -121.171936, -121.7123 );
		shape142.lineTo( -119.56097, -118.51235 );
		shape142.lineTo( -115.79948, -118.88929 );
		shape142.lineTo( -115.431786, -116.03771 );
		shape142.lineTo( -114.59448, -120.31398 );
		shape142.lineTo( -114.59448, -120.31398 );
		shape142.lineTo( -114.59448, -120.31398 );
		shape142.closePath();
		g.fill( shape142 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_64( Graphics2D g ) {
		GeneralPath shape143 = new GeneralPath();
		shape143.moveTo( -125.6262, -118.76841 );
		shape143.curveTo( -125.6262, -118.76841, -133.91393, -122.71543, -133.62964, -122.15074 );
		shape143.curveTo( -133.34535, -121.586044, -131.90135, -118.50564, -131.90135, -118.50564 );
		shape143.lineTo( -125.626205, -118.76841 );
		shape143.lineTo( -125.626205, -118.76841 );
		shape143.lineTo( -125.626205, -118.76841 );
		shape143.closePath();
		g.fill( shape143 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_65( Graphics2D g ) {
		GeneralPath shape144 = new GeneralPath();
		shape144.moveTo( -143.77301, -110.82659 );
		shape144.curveTo( -143.77301, -110.82659, -137.28368, -106.42146, -136.94746, -106.60211 );
		shape144.curveTo( -136.61124, -106.78276, -133.06183, -107.79309, -133.06183, -107.79309 );
		shape144.lineTo( -136.83469, -105.31749 );
		shape144.lineTo( -142.02217, -106.92457 );
		shape144.lineTo( -143.77301, -110.82659 );
		shape144.lineTo( -143.77301, -110.82659 );
		shape144.lineTo( -143.77301, -110.82659 );
		shape144.closePath();
		g.setPaint( new Color( 136, 138, 133, 255 ) );
		g.fill( shape144 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_66( Graphics2D g ) {
		GeneralPath shape145 = new GeneralPath();
		shape145.moveTo( -150.80025, -103.64741 );
		shape145.lineTo( -148.68835, -100.08884 );
		shape145.lineTo( -144.12138, -98.86581 );
		shape145.lineTo( -140.88774, -103.47307 );
		shape145.lineTo( -145.1615, -101.356094 );
		shape145.lineTo( -150.80023, -103.64741 );
		shape145.lineTo( -150.80023, -103.64741 );
		shape145.lineTo( -150.80023, -103.64741 );
		shape145.closePath();
		g.setPaint( new Color( 145, 159, 186, 255 ) );
		g.fill( shape145 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_67( Graphics2D g ) {
		GeneralPath shape146 = new GeneralPath();
		shape146.moveTo( -100.04461, -96.73875 );
		shape146.lineTo( -96.95555, -96.75439 );
		shape146.lineTo( -94.52109, -90.85809 );
		shape146.lineTo( -95.590416, -96.80034 );
		shape146.lineTo( -93.3315, -102.70737 );
		shape146.lineTo( -96.52003, -97.586266 );
		shape146.lineTo( -101.3713, -99.374 );
		shape146.lineTo( -100.04461, -96.738754 );
		shape146.lineTo( -100.04461, -96.73875 );
		shape146.lineTo( -100.04461, -96.73875 );
		shape146.closePath();
		g.fill( shape146 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_68( Graphics2D g ) {
		GeneralPath shape147 = new GeneralPath();
		shape147.moveTo( -125.1758, -106.42465 );
		shape147.lineTo( -127.61236, -116.35545 );
		shape147.lineTo( -122.67492, -126.06329 );
		shape147.lineTo( -125.26516, -129.72353 );
		shape147.lineTo( -133.36371, -124.38561 );
		shape147.lineTo( -125.856636, -122.20136 );
		shape147.lineTo( -128.67293, -115.068146 );
		shape147.lineTo( -128.00977, -109.29629 );
		shape147.lineTo( -125.1758, -106.424644 );
		shape147.closePath();
		g.setPaint( new Color( 255, 255, 255, 255 ) );
		g.fill( shape147 );
	}

	private void paintShapeNode_0_0_0_0_0_7_1_69( Graphics2D g ) {
		GeneralPath shape148 = new GeneralPath();
		shape148.moveTo( -125.73364, -92.68954 );
		shape148.lineTo( -128.50449, -93.950935 );
		shape148.lineTo( -131.36993, -99.85481 );
		shape148.lineTo( -127.54305, -99.041245 );
		shape148.lineTo( -125.42452, -92.28764 );
		shape148.lineTo( -125.733635, -92.68953 );
		shape148.closePath();
		g.setPaint( new Color( 223, 227, 235, 255 ) );
		g.fill( shape148 );
	}

	private void paintCompositeGraphicsNode_0_0_0_0_0_7_1( Graphics2D g ) {
		// _0_0_0_0_0_7_1_0
		AffineTransform trans_0_0_0_0_0_7_1_0 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_0( g );
		g.setTransform( trans_0_0_0_0_0_7_1_0 );
		// _0_0_0_0_0_7_1_1
		AffineTransform trans_0_0_0_0_0_7_1_1 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_1( g );
		g.setTransform( trans_0_0_0_0_0_7_1_1 );
		// _0_0_0_0_0_7_1_2
		AffineTransform trans_0_0_0_0_0_7_1_2 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_2( g );
		g.setTransform( trans_0_0_0_0_0_7_1_2 );
		// _0_0_0_0_0_7_1_3
		AffineTransform trans_0_0_0_0_0_7_1_3 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_3( g );
		g.setTransform( trans_0_0_0_0_0_7_1_3 );
		// _0_0_0_0_0_7_1_4
		AffineTransform trans_0_0_0_0_0_7_1_4 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_4( g );
		g.setTransform( trans_0_0_0_0_0_7_1_4 );
		// _0_0_0_0_0_7_1_5
		AffineTransform trans_0_0_0_0_0_7_1_5 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_5( g );
		g.setTransform( trans_0_0_0_0_0_7_1_5 );
		// _0_0_0_0_0_7_1_6
		AffineTransform trans_0_0_0_0_0_7_1_6 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_6( g );
		g.setTransform( trans_0_0_0_0_0_7_1_6 );
		// _0_0_0_0_0_7_1_7
		AffineTransform trans_0_0_0_0_0_7_1_7 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_7( g );
		g.setTransform( trans_0_0_0_0_0_7_1_7 );
		// _0_0_0_0_0_7_1_8
		AffineTransform trans_0_0_0_0_0_7_1_8 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_8( g );
		g.setTransform( trans_0_0_0_0_0_7_1_8 );
		// _0_0_0_0_0_7_1_9
		AffineTransform trans_0_0_0_0_0_7_1_9 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_9( g );
		g.setTransform( trans_0_0_0_0_0_7_1_9 );
		// _0_0_0_0_0_7_1_10
		AffineTransform trans_0_0_0_0_0_7_1_10 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_10( g );
		g.setTransform( trans_0_0_0_0_0_7_1_10 );
		// _0_0_0_0_0_7_1_11
		g.setComposite( AlphaComposite.getInstance( 3, 0.6456311f * origAlpha ) );
		AffineTransform trans_0_0_0_0_0_7_1_11 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_11( g );
		g.setTransform( trans_0_0_0_0_0_7_1_11 );
		// _0_0_0_0_0_7_1_12
		AffineTransform trans_0_0_0_0_0_7_1_12 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_12( g );
		g.setTransform( trans_0_0_0_0_0_7_1_12 );
		// _0_0_0_0_0_7_1_13
		AffineTransform trans_0_0_0_0_0_7_1_13 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_13( g );
		g.setTransform( trans_0_0_0_0_0_7_1_13 );
		// _0_0_0_0_0_7_1_14
		AffineTransform trans_0_0_0_0_0_7_1_14 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_14( g );
		g.setTransform( trans_0_0_0_0_0_7_1_14 );
		// _0_0_0_0_0_7_1_15
		AffineTransform trans_0_0_0_0_0_7_1_15 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_15( g );
		g.setTransform( trans_0_0_0_0_0_7_1_15 );
		// _0_0_0_0_0_7_1_16
		AffineTransform trans_0_0_0_0_0_7_1_16 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_16( g );
		g.setTransform( trans_0_0_0_0_0_7_1_16 );
		// _0_0_0_0_0_7_1_17
		AffineTransform trans_0_0_0_0_0_7_1_17 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_17( g );
		g.setTransform( trans_0_0_0_0_0_7_1_17 );
		// _0_0_0_0_0_7_1_18
		AffineTransform trans_0_0_0_0_0_7_1_18 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_18( g );
		g.setTransform( trans_0_0_0_0_0_7_1_18 );
		// _0_0_0_0_0_7_1_19
		AffineTransform trans_0_0_0_0_0_7_1_19 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_19( g );
		g.setTransform( trans_0_0_0_0_0_7_1_19 );
		// _0_0_0_0_0_7_1_20
		AffineTransform trans_0_0_0_0_0_7_1_20 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_20( g );
		g.setTransform( trans_0_0_0_0_0_7_1_20 );
		// _0_0_0_0_0_7_1_21
		AffineTransform trans_0_0_0_0_0_7_1_21 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_21( g );
		g.setTransform( trans_0_0_0_0_0_7_1_21 );
		// _0_0_0_0_0_7_1_22
		AffineTransform trans_0_0_0_0_0_7_1_22 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_22( g );
		g.setTransform( trans_0_0_0_0_0_7_1_22 );
		// _0_0_0_0_0_7_1_23
		AffineTransform trans_0_0_0_0_0_7_1_23 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_23( g );
		g.setTransform( trans_0_0_0_0_0_7_1_23 );
		// _0_0_0_0_0_7_1_24
		AffineTransform trans_0_0_0_0_0_7_1_24 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_24( g );
		g.setTransform( trans_0_0_0_0_0_7_1_24 );
		// _0_0_0_0_0_7_1_25
		AffineTransform trans_0_0_0_0_0_7_1_25 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_25( g );
		g.setTransform( trans_0_0_0_0_0_7_1_25 );
		// _0_0_0_0_0_7_1_26
		AffineTransform trans_0_0_0_0_0_7_1_26 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_26( g );
		g.setTransform( trans_0_0_0_0_0_7_1_26 );
		// _0_0_0_0_0_7_1_27
		AffineTransform trans_0_0_0_0_0_7_1_27 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_27( g );
		g.setTransform( trans_0_0_0_0_0_7_1_27 );
		// _0_0_0_0_0_7_1_28
		AffineTransform trans_0_0_0_0_0_7_1_28 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_28( g );
		g.setTransform( trans_0_0_0_0_0_7_1_28 );
		// _0_0_0_0_0_7_1_29
		AffineTransform trans_0_0_0_0_0_7_1_29 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_29( g );
		g.setTransform( trans_0_0_0_0_0_7_1_29 );
		// _0_0_0_0_0_7_1_30
		AffineTransform trans_0_0_0_0_0_7_1_30 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_30( g );
		g.setTransform( trans_0_0_0_0_0_7_1_30 );
		// _0_0_0_0_0_7_1_31
		AffineTransform trans_0_0_0_0_0_7_1_31 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_31( g );
		g.setTransform( trans_0_0_0_0_0_7_1_31 );
		// _0_0_0_0_0_7_1_32
		AffineTransform trans_0_0_0_0_0_7_1_32 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_32( g );
		g.setTransform( trans_0_0_0_0_0_7_1_32 );
		// _0_0_0_0_0_7_1_33
		AffineTransform trans_0_0_0_0_0_7_1_33 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_33( g );
		g.setTransform( trans_0_0_0_0_0_7_1_33 );
		// _0_0_0_0_0_7_1_34
		AffineTransform trans_0_0_0_0_0_7_1_34 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_34( g );
		g.setTransform( trans_0_0_0_0_0_7_1_34 );
		// _0_0_0_0_0_7_1_35
		g.setComposite( AlphaComposite.getInstance( 3, 1.0f * origAlpha ) );
		AffineTransform trans_0_0_0_0_0_7_1_35 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_35( g );
		g.setTransform( trans_0_0_0_0_0_7_1_35 );
		// _0_0_0_0_0_7_1_36
		AffineTransform trans_0_0_0_0_0_7_1_36 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_36( g );
		g.setTransform( trans_0_0_0_0_0_7_1_36 );
		// _0_0_0_0_0_7_1_37
		AffineTransform trans_0_0_0_0_0_7_1_37 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_37( g );
		g.setTransform( trans_0_0_0_0_0_7_1_37 );
		// _0_0_0_0_0_7_1_38
		AffineTransform trans_0_0_0_0_0_7_1_38 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_38( g );
		g.setTransform( trans_0_0_0_0_0_7_1_38 );
		// _0_0_0_0_0_7_1_39
		AffineTransform trans_0_0_0_0_0_7_1_39 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_39( g );
		g.setTransform( trans_0_0_0_0_0_7_1_39 );
		// _0_0_0_0_0_7_1_40
		AffineTransform trans_0_0_0_0_0_7_1_40 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_40( g );
		g.setTransform( trans_0_0_0_0_0_7_1_40 );
		// _0_0_0_0_0_7_1_41
		AffineTransform trans_0_0_0_0_0_7_1_41 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_41( g );
		g.setTransform( trans_0_0_0_0_0_7_1_41 );
		// _0_0_0_0_0_7_1_42
		AffineTransform trans_0_0_0_0_0_7_1_42 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_42( g );
		g.setTransform( trans_0_0_0_0_0_7_1_42 );
		// _0_0_0_0_0_7_1_43
		AffineTransform trans_0_0_0_0_0_7_1_43 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_43( g );
		g.setTransform( trans_0_0_0_0_0_7_1_43 );
		// _0_0_0_0_0_7_1_44
		AffineTransform trans_0_0_0_0_0_7_1_44 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_44( g );
		g.setTransform( trans_0_0_0_0_0_7_1_44 );
		// _0_0_0_0_0_7_1_45
		AffineTransform trans_0_0_0_0_0_7_1_45 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_45( g );
		g.setTransform( trans_0_0_0_0_0_7_1_45 );
		// _0_0_0_0_0_7_1_46
		g.setComposite( AlphaComposite.getInstance( 3, 0.6456311f * origAlpha ) );
		AffineTransform trans_0_0_0_0_0_7_1_46 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_46( g );
		g.setTransform( trans_0_0_0_0_0_7_1_46 );
		// _0_0_0_0_0_7_1_47
		AffineTransform trans_0_0_0_0_0_7_1_47 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_47( g );
		g.setTransform( trans_0_0_0_0_0_7_1_47 );
		// _0_0_0_0_0_7_1_48
		AffineTransform trans_0_0_0_0_0_7_1_48 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_48( g );
		g.setTransform( trans_0_0_0_0_0_7_1_48 );
		// _0_0_0_0_0_7_1_49
		AffineTransform trans_0_0_0_0_0_7_1_49 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_49( g );
		g.setTransform( trans_0_0_0_0_0_7_1_49 );
		// _0_0_0_0_0_7_1_50
		AffineTransform trans_0_0_0_0_0_7_1_50 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_50( g );
		g.setTransform( trans_0_0_0_0_0_7_1_50 );
		// _0_0_0_0_0_7_1_51
		AffineTransform trans_0_0_0_0_0_7_1_51 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_51( g );
		g.setTransform( trans_0_0_0_0_0_7_1_51 );
		// _0_0_0_0_0_7_1_52
		AffineTransform trans_0_0_0_0_0_7_1_52 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_52( g );
		g.setTransform( trans_0_0_0_0_0_7_1_52 );
		// _0_0_0_0_0_7_1_53
		AffineTransform trans_0_0_0_0_0_7_1_53 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_53( g );
		g.setTransform( trans_0_0_0_0_0_7_1_53 );
		// _0_0_0_0_0_7_1_54
		AffineTransform trans_0_0_0_0_0_7_1_54 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_54( g );
		g.setTransform( trans_0_0_0_0_0_7_1_54 );
		// _0_0_0_0_0_7_1_55
		AffineTransform trans_0_0_0_0_0_7_1_55 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_55( g );
		g.setTransform( trans_0_0_0_0_0_7_1_55 );
		// _0_0_0_0_0_7_1_56
		AffineTransform trans_0_0_0_0_0_7_1_56 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_56( g );
		g.setTransform( trans_0_0_0_0_0_7_1_56 );
		// _0_0_0_0_0_7_1_57
		AffineTransform trans_0_0_0_0_0_7_1_57 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_57( g );
		g.setTransform( trans_0_0_0_0_0_7_1_57 );
		// _0_0_0_0_0_7_1_58
		AffineTransform trans_0_0_0_0_0_7_1_58 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_58( g );
		g.setTransform( trans_0_0_0_0_0_7_1_58 );
		// _0_0_0_0_0_7_1_59
		AffineTransform trans_0_0_0_0_0_7_1_59 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_59( g );
		g.setTransform( trans_0_0_0_0_0_7_1_59 );
		// _0_0_0_0_0_7_1_60
		AffineTransform trans_0_0_0_0_0_7_1_60 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_60( g );
		g.setTransform( trans_0_0_0_0_0_7_1_60 );
		// _0_0_0_0_0_7_1_61
		AffineTransform trans_0_0_0_0_0_7_1_61 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_61( g );
		g.setTransform( trans_0_0_0_0_0_7_1_61 );
		// _0_0_0_0_0_7_1_62
		AffineTransform trans_0_0_0_0_0_7_1_62 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_62( g );
		g.setTransform( trans_0_0_0_0_0_7_1_62 );
		// _0_0_0_0_0_7_1_63
		AffineTransform trans_0_0_0_0_0_7_1_63 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_63( g );
		g.setTransform( trans_0_0_0_0_0_7_1_63 );
		// _0_0_0_0_0_7_1_64
		AffineTransform trans_0_0_0_0_0_7_1_64 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_64( g );
		g.setTransform( trans_0_0_0_0_0_7_1_64 );
		// _0_0_0_0_0_7_1_65
		AffineTransform trans_0_0_0_0_0_7_1_65 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_65( g );
		g.setTransform( trans_0_0_0_0_0_7_1_65 );
		// _0_0_0_0_0_7_1_66
		AffineTransform trans_0_0_0_0_0_7_1_66 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_66( g );
		g.setTransform( trans_0_0_0_0_0_7_1_66 );
		// _0_0_0_0_0_7_1_67
		AffineTransform trans_0_0_0_0_0_7_1_67 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_67( g );
		g.setTransform( trans_0_0_0_0_0_7_1_67 );
		// _0_0_0_0_0_7_1_68
		AffineTransform trans_0_0_0_0_0_7_1_68 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_68( g );
		g.setTransform( trans_0_0_0_0_0_7_1_68 );
		// _0_0_0_0_0_7_1_69
		AffineTransform trans_0_0_0_0_0_7_1_69 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_1_69( g );
		g.setTransform( trans_0_0_0_0_0_7_1_69 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_0( Graphics2D g ) {
		GeneralPath shape149 = new GeneralPath();
		shape149.moveTo( -132.39502, -132.53253 );
		shape149.curveTo( -132.78226, -132.74329, -132.38332, -134.5506, -134.23735, -134.31387 );
		shape149.curveTo( -136.09142, -134.07713, -142.0526, -134.12912, -142.0526, -134.12912 );
		shape149.lineTo( -144.99408, -131.88876 );
		shape149.lineTo( -146.26927, -125.86054 );
		shape149.lineTo( -149.37119, -116.595924 );
		shape149.lineTo( -158.95056, -111.090294 );
		shape149.lineTo( -155.8683, -106.71347 );
		shape149.lineTo( -153.53702, -106.79427 );
		shape149.lineTo( -153.23976, -103.95916 );
		shape149.lineTo( -155.78229, -98.38711 );
		shape149.lineTo( -154.99219, -92.94785 );
		shape149.lineTo( -156.88928, -93.020035 );
		shape149.lineTo( -155.4694, -87.96476 );
		shape149.lineTo( -148.56165, -86.25559 );
		shape149.lineTo( -92.603004, -122.944405 );
		shape149.lineTo( -96.71791, -127.02674 );
		shape149.lineTo( -97.73094, -137.80421 );
		shape149.lineTo( -117.87928, -133.89806 );
		shape149.lineTo( -127.34128, -135.7256 );
		shape149.lineTo( -122.56923, -131.59708 );
		shape149.lineTo( -132.395, -132.53252 );
		shape149.lineTo( -132.395, -132.53255 );
		shape149.closePath();
		g.setPaint( new LinearGradientPaint( new Point2D.Double( -190.5, 104.1875 ), new Point2D.Double( -96.75, 104.1875 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 237, 244, 252, 255 ), new Color( 192, 198, 214, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( -0.7197251915931702f, 0.4388335943222046f, 0.5945504903793335f, 0.5312241911888123f, -297.7123107910156f, -110.38337707519531f ) ) );
		g.fill( shape149 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_1( Graphics2D g ) {
		GeneralPath shape150 = new GeneralPath();
		shape150.moveTo( -134.39539, -101.38344 );
		shape150.lineTo( -147.14307, -103.441956 );
		shape150.curveTo( -147.14307, -103.441956, -148.18748, -95.99042, -148.47304, -95.7046 );
		shape150.curveTo( -148.75858, -95.41878, -153.93748, -91.25563, -153.93748, -91.25563 );
		shape150.lineTo( -144.36986, -92.17369 );
		shape150.lineTo( -142.10507, -93.6663 );
		shape150.lineTo( -132.05634, -92.53165 );
		shape150.lineTo( -134.39539, -101.38344 );
		shape150.lineTo( -134.39539, -101.38344 );
		shape150.lineTo( -134.39539, -101.38344 );
		shape150.closePath();
		g.setPaint( new Color( 252, 253, 255, 255 ) );
		g.fill( shape150 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_2( Graphics2D g ) {
		GeneralPath shape151 = new GeneralPath();
		shape151.moveTo( -85.25999, -114.38192 );
		shape151.lineTo( -85.82329, -107.44716 );
		shape151.lineTo( -95.60219, -98.46838 );
		shape151.lineTo( -106.66792, -99.42982 );
		shape151.lineTo( -104.90377, -107.3202 );
		shape151.lineTo( -98.48492, -111.45736 );
		shape151.lineTo( -87.27052, -112.93263 );
		shape151.lineTo( -86.17137, -114.38482 );
		shape151.lineTo( -85.25998, -114.38182 );
		shape151.lineTo( -85.25998, -114.38193 );
		shape151.lineTo( -85.25998, -114.38193 );
		shape151.closePath();
		g.setPaint( new LinearGradientPaint( new Point2D.Double( -228.375, 89.875 ), new Point2D.Double( -196.875, 89.875 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 189, 208, 225, 255 ), new Color( 255, 255, 255, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( -0.7197251915931702f, 0.4388335943222046f, 0.5945504903793335f, 0.5312241911888123f, -301.5760803222656f, -60.57901382446289f ) ) );
		g.fill( shape151 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_3( Graphics2D g ) {
		GeneralPath shape152 = new GeneralPath();
		shape152.moveTo( -114.43921, -99.37829 );
		shape152.lineTo( -115.87085, -97.2765 );
		shape152.lineTo( -128.43471, -94.978424 );
		shape152.lineTo( -129.20921, -92.83044 );
		shape152.lineTo( -134.51717, -92.163536 );
		shape152.lineTo( -127.75409, -96.398865 );
		shape152.lineTo( -116.59839, -98.06178 );
		shape152.lineTo( -115.40927, -99.56883 );
		shape152.lineTo( -114.43921, -99.37828 );
		shape152.lineTo( -114.43921, -99.37828 );
		shape152.lineTo( -114.43921, -99.37828 );
		shape152.closePath();
		g.setPaint( new Color( 255, 255, 255, 255 ) );
		g.fill( shape152 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_4( Graphics2D g ) {
		GeneralPath shape153 = new GeneralPath();
		shape153.moveTo( -84.48459, -118.00424 );
		shape153.lineTo( -106.1819, -114.941124 );
		shape153.lineTo( -112.12742, -117.68386 );
		shape153.lineTo( -111.67369, -116.06132 );
		shape153.lineTo( -106.71387, -113.38787 );
		shape153.lineTo( -108.39193, -112.58816 );
		shape153.lineTo( -116.739136, -113.419655 );
		shape153.lineTo( -106.73734, -111.921234 );
		shape153.lineTo( -90.05072, -114.05184 );
		shape153.lineTo( -84.48458, -118.004234 );
		shape153.closePath();
		g.fill( shape153 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_5( Graphics2D g ) {
		GeneralPath shape154 = new GeneralPath();
		shape154.moveTo( -106.22024, -109.61507 );
		shape154.lineTo( -102.92678, -100.45148 );
		shape154.lineTo( -117.9588, -102.79294 );
		shape154.lineTo( -121.7139, -99.3862 );
		shape154.lineTo( -123.235466, -102.36856 );
		shape154.lineTo( -118.744995, -106.33539 );
		shape154.lineTo( -106.22024, -109.61507 );
		shape154.lineTo( -106.22024, -109.61507 );
		shape154.lineTo( -106.22024, -109.61507 );
		shape154.closePath();
		g.setPaint( new Color( 185, 200, 219, 255 ) );
		g.fill( shape154 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_6( Graphics2D g ) {
		GeneralPath shape155 = new GeneralPath();
		shape155.moveTo( -147.66048, -96.62752 );
		shape155.curveTo( -147.21457, -96.22911, -137.18544, -86.59201, -137.18544, -86.59201 );
		shape155.lineTo( -136.75516, -88.64183 );
		shape155.lineTo( -146.52614, -96.42542 );
		shape155.lineTo( -147.66048, -96.627525 );
		shape155.closePath();
		g.setPaint( new Color( 180, 198, 218, 255 ) );
		g.fill( shape155 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_7( Graphics2D g ) {
		GeneralPath shape156 = new GeneralPath();
		shape156.moveTo( -91.19981, -104.92167 );
		shape156.lineTo( -89.09154, -94.923706 );
		shape156.lineTo( -94.74372, -94.158646 );
		shape156.lineTo( -94.40735, -90.34193 );
		shape156.lineTo( -97.86901, -98.84438 );
		shape156.lineTo( -109.36112, -95.859184 );
		shape156.lineTo( -113.828125, -90.78948 );
		shape156.lineTo( -119.72672, -91.32649 );
		shape156.lineTo( -117.778755, -94.860275 );
		shape156.lineTo( -109.71316, -96.98514 );
		shape156.lineTo( -104.52645, -99.92417 );
		shape156.lineTo( -96.42565, -100.394745 );
		shape156.lineTo( -91.19982, -104.92168 );
		shape156.lineTo( -91.19982, -104.92168 );
		shape156.lineTo( -91.19982, -104.92168 );
		shape156.closePath();
		g.setPaint( new Color( 177, 186, 203, 255 ) );
		g.fill( shape156 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_8( Graphics2D g ) {
		GeneralPath shape157 = new GeneralPath();
		shape157.moveTo( -137.23425, -121.96007 );
		shape157.lineTo( -139.48343, -115.449715 );
		shape157.lineTo( -133.54181, -114.60378 );
		shape157.lineTo( -135.44278, -121.71177 );
		shape157.lineTo( -137.23425, -121.96006 );
		shape157.closePath();
		g.setPaint( new Color( 217, 225, 236, 255 ) );
		g.fill( shape157 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_9( Graphics2D g ) {
		GeneralPath shape158 = new GeneralPath();
		shape158.moveTo( -137.4622, -109.68778 );
		shape158.lineTo( -126.73609, -99.4702 );
		shape158.lineTo( -134.0427, -101.561104 );
		shape158.lineTo( -139.06201, -99.54806 );
		shape158.lineTo( -145.45647, -98.922165 );
		shape158.lineTo( -137.4622, -109.68778 );
		shape158.lineTo( -137.4622, -109.68778 );
		shape158.lineTo( -137.4622, -109.68778 );
		shape158.closePath();
		g.setPaint( new Color( 186, 189, 182, 255 ) );
		g.fill( shape158 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_10( Graphics2D g ) {
		GeneralPath shape159 = new GeneralPath();
		shape159.moveTo( -135.87839, -117.10134 );
		shape159.lineTo( -126.72545, -108.38234 );
		shape159.lineTo( -132.96043, -110.16657 );
		shape159.lineTo( -137.24358, -108.44878 );
		shape159.lineTo( -142.70018, -107.91469 );
		shape159.lineTo( -135.8784, -117.10135 );
		shape159.lineTo( -135.8784, -117.10135 );
		shape159.lineTo( -135.8784, -117.10135 );
		shape159.closePath();
		g.setPaint( new Color( 252, 253, 255, 255 ) );
		g.fill( shape159 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_11( Graphics2D g ) {
		GeneralPath shape160 = new GeneralPath();
		shape160.moveTo( -143.28148, -136.88266 );
		shape160.lineTo( -137.94614, -137.11938 );
		shape160.lineTo( -140.47691, -136.13489 );
		shape160.lineTo( -143.28148, -136.88264 );
		shape160.closePath();
		g.setPaint( new Color( 192, 198, 214, 255 ) );
		g.fill( shape160 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_12( Graphics2D g ) {
		GeneralPath shape161 = new GeneralPath();
		shape161.moveTo( -123.52238, -114.61906 );
		shape161.lineTo( -125.11831, -109.95932 );
		shape161.lineTo( -122.77531, -109.48872 );
		shape161.lineTo( -124.82888, -105.77883 );
		shape161.lineTo( -117.46343, -110.82831 );
		shape161.lineTo( -119.48567, -115.06943 );
		shape161.lineTo( -123.522385, -114.61906 );
		shape161.lineTo( -123.522385, -114.61906 );
		shape161.lineTo( -123.522385, -114.61906 );
		shape161.closePath();
		g.fill( shape161 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_13( Graphics2D g ) {
		GeneralPath shape162 = new GeneralPath();
		shape162.moveTo( -133.04099, -121.66503 );
		shape162.curveTo( -133.72943, -121.46871, -139.83142, -120.42938, -139.83142, -120.42938 );
		shape162.lineTo( -140.17957, -117.089035 );
		shape162.lineTo( -137.98518, -119.32075 );
		shape162.lineTo( -133.04099, -121.66504 );
		shape162.closePath();
		g.fill( shape162 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_14( Graphics2D g ) {
		GeneralPath shape163 = new GeneralPath();
		shape163.moveTo( -115.65102, -115.25619 );
		shape163.lineTo( -106.83447, -105.2149 );
		shape163.lineTo( -93.55867, -111.74543 );
		shape163.lineTo( -95.08415, -116.6246 );
		shape163.lineTo( -102.86811, -116.6824 );
		shape163.lineTo( -105.66095, -114.30923 );
		shape163.lineTo( -114.6966, -114.94442 );
		shape163.lineTo( -115.65101, -115.25623 );
		shape163.lineTo( -115.65101, -115.2562 );
		shape163.lineTo( -115.65101, -115.2562 );
		shape163.closePath();
		g.fill( shape163 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_15( Graphics2D g ) {
		GeneralPath shape164 = new GeneralPath();
		shape164.moveTo( -115.36851, -107.04673 );
		shape164.lineTo( -121.145836, -107.88111 );
		shape164.lineTo( -134.41379, -105.265465 );
		shape164.lineTo( -130.15807, -99.70493 );
		shape164.lineTo( -132.10603, -96.17115 );
		shape164.lineTo( -130.1933, -96.22025 );
		shape164.lineTo( -128.83206, -99.061134 );
		shape164.lineTo( -134.15955, -105.308784 );
		shape164.lineTo( -115.368515, -107.046745 );
		shape164.lineTo( -115.368515, -107.04674 );
		shape164.lineTo( -115.368515, -107.04674 );
		shape164.closePath();
		g.fill( shape164 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_16( Graphics2D g ) {
		GeneralPath shape165 = new GeneralPath();
		shape165.moveTo( -134.92313, -119.70906 );
		shape165.lineTo( -137.02757, -114.96271 );
		shape165.lineTo( -132.78746, -114.41993 );
		shape165.lineTo( -134.92313, -119.70906 );
		shape165.lineTo( -134.92313, -119.70906 );
		shape165.lineTo( -134.92313, -119.70906 );
		shape165.closePath();
		g.fill( shape165 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_17( Graphics2D g ) {
		GeneralPath shape166 = new GeneralPath();
		shape166.moveTo( -144.7842, -104.21853 );
		shape166.curveTo( -144.7842, -104.21853, -135.91676, -100.3527, -136.46829, -100.57501 );
		shape166.curveTo( -137.01982, -100.79731, -146.09067, -103.0868, -146.09067, -103.0868 );
		shape166.lineTo( -153.01411, -99.53571 );
		shape166.lineTo( -144.78421, -104.21854 );
		shape166.lineTo( -144.78421, -104.21854 );
		shape166.lineTo( -144.78421, -104.21854 );
		shape166.closePath();
		g.fill( shape166 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_18( Graphics2D g ) {
		GeneralPath shape167 = new GeneralPath();
		shape167.moveTo( -106.29318, -120.61398 );
		shape167.lineTo( -118.26643, -114.542496 );
		shape167.lineTo( -109.508545, -107.25838 );
		shape167.lineTo( -108.33502, -116.35268 );
		shape167.lineTo( -104.885056, -116.110146 );
		shape167.lineTo( -103.21484, -112.99499 );
		shape167.lineTo( -102.068756, -114.81096 );
		shape167.lineTo( -105.59303, -117.68938 );
		shape167.lineTo( -106.29318, -120.61398 );
		shape167.lineTo( -106.29318, -120.61398 );
		shape167.lineTo( -106.29318, -120.61398 );
		shape167.closePath();
		g.fill( shape167 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_19( Graphics2D g ) {
		GeneralPath shape168 = new GeneralPath();
		shape168.moveTo( -91.16675, -96.86413 );
		shape168.curveTo( -91.44839, -97.251, -96.423836, -99.803185, -96.423836, -99.803185 );
		shape168.lineTo( -95.0509, -96.953636 );
		shape168.lineTo( -98.83337, -96.54658 );
		shape168.lineTo( -105.967995, -97.782265 );
		shape168.lineTo( -95.074394, -92.9175 );
		shape168.lineTo( -91.16675, -96.86412 );
		shape168.closePath();
		g.fill( shape168 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_20( Graphics2D g ) {
		GeneralPath shape169 = new GeneralPath();
		shape169.moveTo( -138.2815, -105.49613 );
		shape169.curveTo( -138.14458, -105.91475, -137.93335, -108.83647, -137.93335, -108.83647 );
		shape169.lineTo( -132.26942, -111.6196 );
		shape169.lineTo( -131.47539, -109.42253 );
		shape169.lineTo( -138.2815, -105.49613 );
		shape169.closePath();
		g.fill( shape169 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_21( Graphics2D g ) {
		GeneralPath shape170 = new GeneralPath();
		shape170.moveTo( -103.72688, -141.6636 );
		shape170.lineTo( -114.068985, -141.16707 );
		shape170.lineTo( -108.31122, -139.53874 );
		shape170.lineTo( -103.72689, -141.6636 );
		shape170.lineTo( -103.72689, -141.6636 );
		shape170.lineTo( -103.72689, -141.6636 );
		shape170.closePath();
		g.fill( shape170 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_22( Graphics2D g ) {
		GeneralPath shape171 = new GeneralPath();
		shape171.moveTo( -128.75777, -107.38237 );
		shape171.lineTo( -135.247, -109.1233 );
		shape171.lineTo( -131.02255, -105.889755 );
		shape171.lineTo( -128.75777, -107.38236 );
		shape171.closePath();
		g.fill( shape171 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_23( Graphics2D g ) {
		GeneralPath shape172 = new GeneralPath();
		shape172.moveTo( -115.52779, -113.62524 );
		shape172.lineTo( -125.553055, -113.657036 );
		shape172.lineTo( -131.0018, -117.03782 );
		shape172.lineTo( -130.04741, -114.15651 );
		shape172.lineTo( -126.096756, -112.65522 );
		shape172.lineTo( -115.52778, -113.62525 );
		shape172.lineTo( -115.52778, -113.625244 );
		shape172.closePath();
		g.fill( shape172 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_24( Graphics2D g ) {
		GeneralPath shape173 = new GeneralPath();
		shape173.moveTo( -100.97263, -122.36054 );
		shape173.lineTo( -98.96604, -117.99816 );
		shape173.lineTo( -103.1631, -120.80152 );
		shape173.lineTo( -100.97263, -122.36054 );
		shape173.lineTo( -100.97263, -122.36054 );
		shape173.lineTo( -100.97263, -122.36054 );
		shape173.closePath();
		g.fill( shape173 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_25( Graphics2D g ) {
		GeneralPath shape174 = new GeneralPath();
		shape174.moveTo( -97.46421, -118.17365 );
		shape174.lineTo( -97.31951, -114.79865 );
		shape174.lineTo( -94.16681, -114.821754 );
		shape174.lineTo( -95.70797, -111.87116 );
		shape174.lineTo( -99.00931, -111.98088 );
		shape174.lineTo( -97.46421, -118.17366 );
		shape174.lineTo( -97.46421, -118.17365 );
		shape174.lineTo( -97.46421, -118.17365 );
		shape174.closePath();
		g.setPaint( new Color( 145, 159, 186, 255 ) );
		g.fill( shape174 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_26( Graphics2D g ) {
		GeneralPath shape175 = new GeneralPath();
		shape175.moveTo( -144.14174, -130.13132 );
		shape175.curveTo( -143.95007, -129.68959, -142.97221, -125.70543, -142.97221, -125.70543 );
		shape175.lineTo( -146.34395, -126.55424 );
		shape175.lineTo( -144.14172, -130.13132 );
		shape175.lineTo( -144.14172, -130.13132 );
		shape175.lineTo( -144.14172, -130.13132 );
		shape175.closePath();
		g.fill( shape175 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_27( Graphics2D g ) {
		GeneralPath shape176 = new GeneralPath();
		shape176.moveTo( -88.66009, -101.76395 );
		shape176.lineTo( -89.86095, -98.23883 );
		shape176.lineTo( -95.79867, -97.18795 );
		shape176.lineTo( -99.59286, -97.33231 );
		shape176.lineTo( -95.38013, -99.7892 );
		shape176.lineTo( -94.656494, -98.33122 );
		shape176.lineTo( -91.0344, -99.42252 );
		shape176.lineTo( -88.66008, -101.76394 );
		shape176.lineTo( -88.66008, -101.76394 );
		shape176.lineTo( -88.66008, -101.76394 );
		shape176.closePath();
		g.fill( shape176 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_28( Graphics2D g ) {
		GeneralPath shape177 = new GeneralPath();
		shape177.moveTo( -143.93529, -125.38695 );
		shape177.lineTo( -144.79587, -118.7178 );
		shape177.lineTo( -141.73703, -120.58285 );
		shape177.lineTo( -142.41762, -124.3014 );
		shape177.lineTo( -139.60522, -124.89902 );
		shape177.lineTo( -143.93529, -125.38695 );
		shape177.closePath();
		g.fill( shape177 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_29( Graphics2D g ) {
		GeneralPath shape178 = new GeneralPath();
		shape178.moveTo( -118.91435, -123.78149 );
		shape178.curveTo( -118.91435, -123.78149, -122.17662, -115.201096, -121.63683, -115.53023 );
		shape178.curveTo( -121.09703, -115.85936, -118.143814, -117.54829, -118.143814, -117.54829 );
		shape178.lineTo( -118.91435, -123.78149 );
		shape178.closePath();
		g.fill( shape178 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_30( Graphics2D g ) {
		GeneralPath shape179 = new GeneralPath();
		shape179.moveTo( -111.46347, -107.91678 );
		shape179.curveTo( -111.46347, -107.91678, -107.59883, -114.74182, -107.80614, -115.06228 );
		shape179.curveTo( -108.01345, -115.38275, -109.30814, -118.83859, -109.30814, -118.83859 );
		shape179.lineTo( -106.53488, -115.27882 );
		shape179.lineTo( -107.7162, -109.97815 );
		shape179.lineTo( -111.46346, -107.91679 );
		shape179.closePath();
		g.fill( shape179 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_31( Graphics2D g ) {
		GeneralPath shape180 = new GeneralPath();
		shape180.moveTo( -118.62937, -101.40666 );
		shape180.lineTo( -115.25369, -103.80004 );
		shape180.lineTo( -114.40486, -108.45112 );
		shape180.lineTo( -119.25907, -111.30068 );
		shape180.lineTo( -116.80265, -107.21257 );
		shape180.lineTo( -118.62937, -101.40666 );
		shape180.lineTo( -118.62937, -101.40666 );
		shape180.lineTo( -118.62937, -101.40666 );
		shape180.closePath();
		g.fill( shape180 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_32( Graphics2D g ) {
		GeneralPath shape181 = new GeneralPath();
		shape181.moveTo( -136.38467, -101.42022 );
		shape181.lineTo( -136.65065, -104.49785 );
		shape181.lineTo( -130.97108, -107.40223 );
		shape181.lineTo( -136.8071, -105.85477 );
		shape181.lineTo( -142.87779, -107.62746 );
		shape181.lineTo( -137.51509, -104.86451 );
		shape181.lineTo( -138.90372, -99.8843 );
		shape181.lineTo( -136.38467, -101.42022 );
		shape181.lineTo( -136.38467, -101.42022 );
		shape181.lineTo( -136.38467, -101.42022 );
		shape181.closePath();
		g.fill( shape181 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_33( Graphics2D g ) {
		GeneralPath shape182 = new GeneralPath();
		shape182.moveTo( -135.45757, -125.49986 );
		shape182.lineTo( -145.1582, -122.26637 );
		shape182.lineTo( -155.2343, -126.400696 );
		shape182.lineTo( -158.67255, -123.52229 );
		shape182.lineTo( -152.69577, -115.883064 );
		shape182.lineTo( -151.1272, -123.54247 );
		shape182.lineTo( -143.78917, -121.31363 );
		shape182.lineTo( -138.09006, -122.44245 );
		shape182.lineTo( -135.45757, -125.49986 );
		shape182.lineTo( -135.45757, -125.49986 );
		shape182.lineTo( -135.45757, -125.49986 );
		shape182.closePath();
		g.setPaint( new Color( 255, 255, 255, 255 ) );
		g.fill( shape182 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_34( Graphics2D g ) {
		GeneralPath shape183 = new GeneralPath();
		shape183.moveTo( -94.8302, -126.59109 );
		shape183.lineTo( -95.86286, -123.727104 );
		shape183.lineTo( -101.51505, -120.39255 );
		shape183.lineTo( -101.01435, -124.27278 );
		shape183.lineTo( -94.45469, -126.93176 );
		shape183.lineTo( -94.8302, -126.591095 );
		shape183.lineTo( -94.8302, -126.591095 );
		shape183.lineTo( -94.8302, -126.591095 );
		shape183.closePath();
		g.setPaint( new Color( 223, 227, 235, 255 ) );
		g.fill( shape183 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_35( Graphics2D g ) {
		GeneralPath shape184 = new GeneralPath();
		shape184.moveTo( -125.35453, -117.29214 );
		shape184.curveTo( -125.26063, -117.435585, -124.57269, -117.22394, -124.60601, -117.95008 );
		shape184.curveTo( -124.63931, -118.67618, -124.43127, -120.9851, -124.43127, -120.9851 );
		shape184.lineTo( -125.20692, -122.19585 );
		shape184.lineTo( -127.503265, -122.88013 );
		shape184.lineTo( -130.99648, -124.37446 );
		shape184.lineTo( -132.82852, -128.26099 );
		shape184.lineTo( -134.62215, -127.204254 );
		shape184.lineTo( -134.66435, -126.2981 );
		shape184.lineTo( -135.77261, -126.2722 );
		shape184.lineTo( -137.85222, -127.43332 );
		shape184.lineTo( -139.98538, -127.29853 );
		shape184.lineTo( -139.89758, -128.03157 );
		shape184.lineTo( -141.90178, -127.64058 );
		shape184.lineTo( -142.782, -125.01699 );
		shape184.lineTo( -130.32521, -102.17086 );
		shape184.lineTo( -128.61317, -103.63713 );
		shape184.lineTo( -124.40388, -103.69003 );
		shape184.lineTo( -125.282814, -111.622696 );
		shape184.lineTo( -124.27621, -115.23257 );
		shape184.lineTo( -126.02685, -113.51305 );
		shape184.lineTo( -125.35456, -117.292046 );
		shape184.lineTo( -125.35454, -117.29214 );
		shape184.lineTo( -125.35454, -117.29214 );
		shape184.closePath();
		g.setPaint( new LinearGradientPaint( new Point2D.Double( -190.5, 104.1875 ), new Point2D.Double( -96.75, 104.1875 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 237, 244, 252, 255 ), new Color( 192, 198, 214, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( -0.14740629494190216f, -0.2927992045879364f, -0.22464419901371002f, 0.2137041985988617f, -128.72862243652344f, -182.0675811767578f ) ) );
		g.fill( shape184 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_36( Graphics2D g ) {
		GeneralPath shape185 = new GeneralPath();
		shape185.moveTo( -122.48882, -115.01198 );
		shape185.lineTo( -119.4038, -127.55086 );
		shape185.curveTo( -119.4038, -127.55086, -126.74617, -129.19583, -127.007904, -129.5036 );
		shape185.curveTo( -127.269646, -129.81137, -130.99931, -135.31067, -130.99931, -135.31067 );
		shape185.lineTo( -130.85979, -125.70011 );
		shape185.lineTo( -129.55566, -123.3218 );
		shape185.lineTo( -131.50108, -113.3981 );
		shape185.lineTo( -122.48883, -115.01197 );
		shape185.lineTo( -122.48883, -115.01197 );
		shape185.lineTo( -122.48883, -115.01197 );
		shape185.closePath();
		g.setPaint( new Color( 252, 253, 255, 255 ) );
		g.fill( shape185 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_37( Graphics2D g ) {
		GeneralPath shape186 = new GeneralPath();
		shape186.moveTo( -91.66337, -94.91746 );
		shape186.lineTo( -98.529655, -96.041 );
		shape186.lineTo( -106.68626, -106.5155 );
		shape186.lineTo( -104.831055, -117.466896 );
		shape186.lineTo( -97.10963, -115.06899 );
		shape186.lineTo( -93.50636, -108.33592 );
		shape186.lineTo( -92.94493, -97.03886 );
		shape186.lineTo( -91.58661, -95.825615 );
		shape186.lineTo( -91.663506, -94.917465 );
		shape186.lineTo( -91.66335, -94.91746 );
		shape186.lineTo( -91.66335, -94.91746 );
		shape186.closePath();
		g.setPaint( new LinearGradientPaint( new Point2D.Double( -228.375, 89.875 ), new Point2D.Double( -196.875, 89.875 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 189, 208, 225, 255 ), new Color( 255, 255, 255, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( -0.3790521025657654f, -0.7529268264770508f, -0.5776677131652832f, 0.5495356917381287f, -127.75569152832031f, -314.8827819824219f ) ) );
		g.fill( shape186 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_38( Graphics2D g ) {
		GeneralPath shape187 = new GeneralPath();
		shape187.moveTo( -106.58678, -93.99409 );
		shape187.lineTo( -108.56562, -95.59138 );
		shape187.lineTo( -109.83776, -108.30018 );
		shape187.lineTo( -111.9159, -109.24623 );
		shape187.lineTo( -112.15037, -114.59078 );
		shape187.lineTo( -108.47716, -107.50666 );
		shape187.lineTo( -107.72394, -96.252884 );
		shape187.lineTo( -106.31823, -94.945526 );
		shape187.lineTo( -106.58678, -93.994095 );
		shape187.lineTo( -106.58678, -93.994095 );
		shape187.lineTo( -106.58678, -93.994095 );
		shape187.closePath();
		g.setPaint( new Color( 255, 255, 255, 255 ) );
		g.fill( shape187 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_39( Graphics2D g ) {
		GeneralPath shape188 = new GeneralPath();
		shape188.moveTo( -137.34933, -86.69519 );
		shape188.lineTo( -138.64369, -108.56939 );
		shape188.lineTo( -135.42805, -114.27304 );
		shape188.lineTo( -137.08203, -113.95231 );
		shape188.lineTo( -140.1487, -109.22551 );
		shape188.lineTo( -140.80975, -110.96287 );
		shape188.lineTo( -139.3044, -119.21522 );
		shape188.lineTo( -141.60858, -109.36779 );
		shape188.lineTo( -140.83752, -92.563385 );
		shape188.lineTo( -137.3493, -86.6952 );
		shape188.lineTo( -137.3493, -86.6952 );
		shape188.lineTo( -137.3493, -86.6952 );
		shape188.closePath();
		g.fill( shape188 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_40( Graphics2D g ) {
		GeneralPath shape189 = new GeneralPath();
		shape189.moveTo( -100.63056, -95.58696 );
		shape189.lineTo( -110.03095, -93.0471 );
		shape189.lineTo( -106.478775, -107.83987 );
		shape189.lineTo( -109.56994, -111.85875 );
		shape189.lineTo( -106.47406, -113.13358 );
		shape189.lineTo( -102.88426, -108.33635 );
		shape189.lineTo( -100.63057, -95.58697 );
		shape189.lineTo( -100.63057, -95.58697 );
		shape189.lineTo( -100.63057, -95.58697 );
		shape189.closePath();
		g.setPaint( new Color( 178, 179, 176, 255 ) );
		g.fill( shape189 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_41( Graphics2D g ) {
		GeneralPath shape190 = new GeneralPath();
		shape190.moveTo( -130.91817, -120.23411 );
		shape190.curveTo( -131.35141, -119.82196, -141.76971, -110.60698, -141.76971, -110.60698 );
		shape190.lineTo( -139.76152, -110.01196 );
		shape190.lineTo( -131.21156, -119.119896 );
		shape190.lineTo( -130.91818, -120.234116 );
		shape190.closePath();
		g.setPaint( new Color( 180, 198, 218, 255 ) );
		g.fill( shape190 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_42( Graphics2D g ) {
		GeneralPath shape191 = new GeneralPath();
		shape191.moveTo( -144.84949, -98.93359 );
		shape191.lineTo( -154.98544, -97.64265 );
		shape191.lineTo( -155.28984, -103.33824 );
		shape191.lineTo( -159.12126, -103.31234 );
		shape191.lineTo( -150.3662, -106.07344 );
		shape191.lineTo( -152.41008, -117.769714 );
		shape191.lineTo( -157.10101, -122.63294 );
		shape191.lineTo( -156.08766, -128.4686 );
		shape191.lineTo( -152.7234, -126.24061 );
		shape191.lineTo( -151.2593, -118.02931 );
		shape191.lineTo( -148.75034, -112.62145 );
		shape191.lineTo( -148.93793, -104.50916 );
		shape191.lineTo( -144.84947, -98.93359 );
		shape191.lineTo( -144.84947, -98.93359 );
		shape191.lineTo( -144.84947, -98.93359 );
		shape191.closePath();
		g.setPaint( new Color( 177, 186, 203, 255 ) );
		g.fill( shape191 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_43( Graphics2D g ) {
		GeneralPath shape192 = new GeneralPath();
		shape192.moveTo( -114.44417, -107.84219 );
		shape192.lineTo( -120.7508, -110.611664 );
		shape192.lineTo( -122.075554, -104.758156 );
		shape192.lineTo( -114.83686, -106.07674 );
		shape192.lineTo( -114.44417, -107.84219 );
		shape192.lineTo( -114.44417, -107.84219 );
		shape192.lineTo( -114.44417, -107.84219 );
		shape192.closePath();
		g.setPaint( new Color( 217, 225, 236, 255 ) );
		g.fill( shape192 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_44( Graphics2D g ) {
		GeneralPath shape193 = new GeneralPath();
		shape193.moveTo( -97.67246, -111.52222 );
		shape193.lineTo( -108.725845, -101.659584 );
		shape193.lineTo( -106.04958, -108.772675 );
		shape193.lineTo( -107.649155, -113.93864 );
		shape193.lineTo( -107.754684, -120.362785 );
		shape193.lineTo( -97.67246, -111.52222 );
		shape193.lineTo( -97.67246, -111.52222 );
		shape193.lineTo( -97.67246, -111.52222 );
		shape193.closePath();
		g.setPaint( new Color( 211, 215, 207, 255 ) );
		g.fill( shape193 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_45( Graphics2D g ) {
		GeneralPath shape194 = new GeneralPath();
		shape194.moveTo( -116.04751, -107.16817 );
		shape194.lineTo( -125.47972, -98.75206 );
		shape194.lineTo( -123.195984, -104.82191 );
		shape194.lineTo( -124.56097, -109.23019 );
		shape194.lineTo( -124.65096, -114.71213 );
		shape194.lineTo( -116.04747, -107.16817 );
		shape194.lineTo( -116.04751, -107.16817 );
		shape194.lineTo( -116.04751, -107.16817 );
		shape194.closePath();
		g.setPaint( new Color( 252, 253, 255, 255 ) );
		g.fill( shape194 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_46( Graphics2D g ) {
		GeneralPath shape195 = new GeneralPath();
		shape195.moveTo( -138.0984, -117.23 );
		shape195.lineTo( -138.2949, -111.893036 );
		shape195.lineTo( -139.07103, -114.49528 );
		shape195.lineTo( -138.0984, -117.23001 );
		shape195.lineTo( -138.0984, -117.23001 );
		shape195.lineTo( -138.0984, -117.23001 );
		shape195.closePath();
		g.setPaint( new Color( 192, 198, 214, 255 ) );
		g.fill( shape195 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_47( Graphics2D g ) {
		GeneralPath shape196 = new GeneralPath();
		shape196.moveTo( -139.65446, -105.60096 );
		shape196.lineTo( -144.16951, -107.569336 );
		shape196.lineTo( -144.82848, -105.27219 );
		shape196.lineTo( -148.35971, -107.619705 );
		shape196.lineTo( -143.92384, -99.8692 );
		shape196.lineTo( -139.53278, -101.54102 );
		shape196.lineTo( -139.65446, -105.60095 );
		shape196.closePath();
		g.fill( shape196 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_48( Graphics2D g ) {
		GeneralPath shape197 = new GeneralPath();
		shape197.moveTo( -146.23312, -112.18406 );
		shape197.curveTo( -146.373, -112.88614, -146.9143, -119.05231, -146.9143, -119.05231 );
		shape197.lineTo( -150.21545, -119.67006 );
		shape197.lineTo( -148.16896, -117.30201 );
		shape197.lineTo( -146.23312, -112.18406 );
		shape197.lineTo( -146.23312, -112.18406 );
		shape197.lineTo( -146.23312, -112.18406 );
		shape197.closePath();
		g.fill( shape197 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_49( Graphics2D g ) {
		GeneralPath shape198 = new GeneralPath();
		shape198.moveTo( -118.81447, -103.36701 );
		shape198.lineTo( -129.53734, -95.39338 );
		shape198.lineTo( -124.10438, -81.63193 );
		shape198.lineTo( -119.11761, -82.756905 );
		shape198.lineTo( -118.42907, -90.51057 );
		shape198.lineTo( -120.56807, -93.48658 );
		shape198.lineTo( -119.202576, -102.44101 );
		shape198.lineTo( -118.81444, -103.36701 );
		shape198.lineTo( -118.81447, -103.36701 );
		shape198.lineTo( -118.81447, -103.36701 );
		shape198.closePath();
		g.fill( shape198 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_50( Graphics2D g ) {
		GeneralPath shape199 = new GeneralPath();
		shape199.moveTo( -122.31061, -121.76999 );
		shape199.lineTo( -121.01069, -127.46068 );
		shape199.lineTo( -122.54229, -140.89699 );
		shape199.lineTo( -128.42947, -137.10597 );
		shape199.lineTo( -131.79373, -139.33395 );
		shape199.lineTo( -131.89983, -137.42354 );
		shape199.lineTo( -129.17862, -135.83652 );
		shape199.lineTo( -122.51971, -140.64006 );
		shape199.lineTo( -122.31058, -121.76999 );
		shape199.lineTo( -122.310585, -121.76999 );
		shape199.lineTo( -122.310585, -121.76999 );
		shape199.closePath();
		g.fill( shape199 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_51( Graphics2D g ) {
		GeneralPath shape200 = new GeneralPath();
		shape200.moveTo( -100.97362, -123.74181 );
		shape200.lineTo( -105.533775, -126.224045 );
		shape200.lineTo( -106.41846, -122.04188 );
		shape200.lineTo( -100.97362, -123.74182 );
		shape200.closePath();
		g.fill( shape200 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_52( Graphics2D g ) {
		GeneralPath shape201 = new GeneralPath();
		shape201.moveTo( -105.03012, -125.40315 );
		shape201.curveTo( -105.03012, -125.40315, -109.60198, -116.87824, -109.3357, -117.409935 );
		shape201.curveTo( -109.06942, -117.94163, -106.05223, -126.79704, -106.05223, -126.79704 );
		shape201.lineTo( -109.03045, -133.98555 );
		shape201.lineTo( -105.03012, -125.40316 );
		shape201.lineTo( -105.03012, -125.40316 );
		shape201.lineTo( -105.03012, -125.40316 );
		shape201.closePath();
		g.fill( shape201 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_53( Graphics2D g ) {
		GeneralPath shape202 = new GeneralPath();
		shape202.moveTo( -103.74948, -119.92615 );
		shape202.lineTo( -108.83049, -132.35213 );
		shape202.lineTo( -116.80052, -124.21347 );
		shape202.lineTo( -107.83126, -122.30667 );
		shape202.lineTo( -108.35263, -118.88771 );
		shape202.lineTo( -111.59292, -117.4755 );
		shape202.lineTo( -109.87582, -116.18598 );
		shape202.lineTo( -106.72121, -119.465355 );
		shape202.lineTo( -103.74947, -119.926155 );
		shape202.lineTo( -103.74947, -119.926155 );
		shape202.lineTo( -103.74947, -119.926155 );
		shape202.closePath();
		g.fill( shape202 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_54( Graphics2D g ) {
		GeneralPath shape203 = new GeneralPath();
		shape203.moveTo( -113.01835, -104.19628 );
		shape203.curveTo( -112.60993, -104.44564, -109.66285, -109.19785, -109.66285, -109.19785 );
		shape203.lineTo( -112.61431, -108.06041 );
		shape203.lineTo( -112.71341, -111.86341 );
		shape203.lineTo( -110.90349, -118.87441 );
		shape203.lineTo( -116.635216, -108.41097 );
		shape203.lineTo( -113.01832, -104.19628 );
		shape203.lineTo( -113.01835, -104.19628 );
		shape203.lineTo( -113.01835, -104.19628 );
		shape203.closePath();
		g.fill( shape203 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_55( Graphics2D g ) {
		GeneralPath shape204 = new GeneralPath();
		shape204.moveTo( -122.34034, -128.05212 );
		shape204.curveTo( -121.9342, -127.88174, -119.03921, -127.434364, -119.03921, -127.434364 );
		shape204.lineTo( -116.72433, -121.56349 );
		shape204.lineTo( -118.97853, -120.950165 );
		shape204.lineTo( -122.34034, -128.05212 );
		shape204.closePath();
		g.fill( shape204 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_56( Graphics2D g ) {
		GeneralPath shape205 = new GeneralPath();
		shape205.moveTo( -131.22617, -83.955605 );
		shape205.lineTo( -130.8828, -94.303925 );
		shape205.lineTo( -132.97247, -88.69708 );
		shape205.lineTo( -131.22617, -83.955605 );
		shape205.lineTo( -131.22617, -83.955605 );
		shape205.lineTo( -131.22617, -83.955605 );
		shape205.closePath();
		g.fill( shape205 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_57( Graphics2D g ) {
		GeneralPath shape206 = new GeneralPath();
		shape206.moveTo( -134.79109, -118.34518 );
		shape206.lineTo( -132.5299, -124.671936 );
		shape206.lineTo( -136.09521, -120.723495 );
		shape206.lineTo( -134.79109, -118.34518 );
		shape206.closePath();
		g.fill( shape206 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_58( Graphics2D g ) {
		GeneralPath shape207 = new GeneralPath();
		shape207.moveTo( -144.89034, -115.10358 );
		shape207.lineTo( -144.04604, -125.093285 );
		shape207.lineTo( -140.23473, -130.25008 );
		shape207.lineTo( -143.18391, -129.53236 );
		shape207.lineTo( -145.00049, -125.7164 );
		shape207.lineTo( -144.89034, -115.10357 );
		shape207.lineTo( -144.89035, -115.10357 );
		shape207.lineTo( -144.89035, -115.10357 );
		shape207.closePath();
		g.fill( shape207 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_59( Graphics2D g ) {
		GeneralPath shape208 = new GeneralPath();
		shape208.moveTo( -103.70774, -100.41719 );
		shape208.lineTo( -108.21842, -98.77079 );
		shape208.lineTo( -105.08409, -102.72682 );
		shape208.lineTo( -103.70774, -100.41719 );
		shape208.lineTo( -103.70774, -100.41719 );
		shape208.lineTo( -103.70774, -100.41719 );
		shape208.closePath();
		g.fill( shape208 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_60( Graphics2D g ) {
		GeneralPath shape209 = new GeneralPath();
		shape209.moveTo( -130.08437, -109.41773 );
		shape209.lineTo( -133.45999, -109.54707 );
		shape209.lineTo( -133.6925, -106.40287 );
		shape209.lineTo( -136.50847, -108.17812 );
		shape209.lineTo( -136.13152, -111.4597 );
		shape209.lineTo( -130.08435, -109.41773 );
		shape209.lineTo( -130.08437, -109.41773 );
		shape209.closePath();
		g.setPaint( new Color( 145, 159, 186, 255 ) );
		g.fill( shape209 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_61( Graphics2D g ) {
		GeneralPath shape210 = new GeneralPath();
		shape210.moveTo( -111.05447, -106.78952 );
		shape210.curveTo( -111.51027, -106.63429, -115.560585, -105.98259, -115.560585, -105.98259 );
		shape210.lineTo( -114.44128, -109.27443 );
		shape210.lineTo( -111.05447, -106.78952 );
		shape210.closePath();
		g.fill( shape210 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_62( Graphics2D g ) {
		GeneralPath shape211 = new GeneralPath();
		shape211.moveTo( -110.96047, -107.81577 );
		shape211.lineTo( -114.376656, -109.298416 );
		shape211.lineTo( -114.94279, -115.30177 );
		shape211.lineTo( -114.49136, -119.071785 );
		shape211.lineTo( -112.384026, -114.673775 );
		shape211.lineTo( -113.89586, -114.070694 );
		shape211.lineTo( -113.10173, -110.37207 );
		shape211.lineTo( -110.96048, -107.81577 );
		shape211.lineTo( -110.96048, -107.81577 );
		shape211.lineTo( -110.96048, -107.81577 );
		shape211.closePath();
		g.fill( shape211 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_63( Graphics2D g ) {
		GeneralPath shape212 = new GeneralPath();
		shape212.moveTo( -114.59449, -120.31398 );
		shape212.lineTo( -121.171936, -121.7123 );
		shape212.lineTo( -119.56097, -118.51235 );
		shape212.lineTo( -115.79948, -118.88929 );
		shape212.lineTo( -115.431786, -116.03771 );
		shape212.lineTo( -114.59448, -120.31398 );
		shape212.lineTo( -114.59448, -120.31398 );
		shape212.lineTo( -114.59448, -120.31398 );
		shape212.closePath();
		g.fill( shape212 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_64( Graphics2D g ) {
		GeneralPath shape213 = new GeneralPath();
		shape213.moveTo( -125.6262, -118.76841 );
		shape213.curveTo( -125.6262, -118.76841, -133.91393, -122.71543, -133.62964, -122.15074 );
		shape213.curveTo( -133.34535, -121.586044, -131.90135, -118.50564, -131.90135, -118.50564 );
		shape213.lineTo( -125.626205, -118.76841 );
		shape213.lineTo( -125.626205, -118.76841 );
		shape213.lineTo( -125.626205, -118.76841 );
		shape213.closePath();
		g.fill( shape213 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_65( Graphics2D g ) {
		GeneralPath shape214 = new GeneralPath();
		shape214.moveTo( -143.77301, -110.82659 );
		shape214.curveTo( -143.77301, -110.82659, -137.28368, -106.42146, -136.94746, -106.60211 );
		shape214.curveTo( -136.61124, -106.78276, -133.06183, -107.79309, -133.06183, -107.79309 );
		shape214.lineTo( -136.83469, -105.31749 );
		shape214.lineTo( -142.02217, -106.92457 );
		shape214.lineTo( -143.77301, -110.82659 );
		shape214.lineTo( -143.77301, -110.82659 );
		shape214.lineTo( -143.77301, -110.82659 );
		shape214.closePath();
		g.fill( shape214 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_66( Graphics2D g ) {
		GeneralPath shape215 = new GeneralPath();
		shape215.moveTo( -150.80025, -103.64741 );
		shape215.lineTo( -148.68835, -100.08884 );
		shape215.lineTo( -144.12138, -98.86581 );
		shape215.lineTo( -140.88774, -103.47307 );
		shape215.lineTo( -145.1615, -101.356094 );
		shape215.lineTo( -150.80023, -103.64741 );
		shape215.lineTo( -150.80023, -103.64741 );
		shape215.lineTo( -150.80023, -103.64741 );
		shape215.closePath();
		g.fill( shape215 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_67( Graphics2D g ) {
		GeneralPath shape216 = new GeneralPath();
		shape216.moveTo( -100.04461, -96.73875 );
		shape216.lineTo( -96.95555, -96.75439 );
		shape216.lineTo( -94.52109, -90.85809 );
		shape216.lineTo( -95.590416, -96.80034 );
		shape216.lineTo( -93.3315, -102.70737 );
		shape216.lineTo( -96.52003, -97.586266 );
		shape216.lineTo( -101.3713, -99.374 );
		shape216.lineTo( -100.04461, -96.738754 );
		shape216.lineTo( -100.04461, -96.73875 );
		shape216.lineTo( -100.04461, -96.73875 );
		shape216.closePath();
		g.fill( shape216 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_68( Graphics2D g ) {
		GeneralPath shape217 = new GeneralPath();
		shape217.moveTo( -125.1758, -106.42465 );
		shape217.lineTo( -127.61236, -116.35545 );
		shape217.lineTo( -122.67492, -126.06329 );
		shape217.lineTo( -125.26516, -129.72353 );
		shape217.lineTo( -133.36371, -124.38561 );
		shape217.lineTo( -125.856636, -122.20136 );
		shape217.lineTo( -128.67293, -115.068146 );
		shape217.lineTo( -128.00977, -109.29629 );
		shape217.lineTo( -125.1758, -106.424644 );
		shape217.closePath();
		g.setPaint( new Color( 255, 255, 255, 255 ) );
		g.fill( shape217 );
	}

	private void paintShapeNode_0_0_0_0_0_7_2_69( Graphics2D g ) {
		GeneralPath shape218 = new GeneralPath();
		shape218.moveTo( -125.73364, -92.68954 );
		shape218.lineTo( -128.50449, -93.950935 );
		shape218.lineTo( -131.36993, -99.85481 );
		shape218.lineTo( -127.54305, -99.041245 );
		shape218.lineTo( -125.42452, -92.28764 );
		shape218.lineTo( -125.733635, -92.68953 );
		shape218.closePath();
		g.setPaint( new Color( 223, 227, 235, 255 ) );
		g.fill( shape218 );
	}

	private void paintCompositeGraphicsNode_0_0_0_0_0_7_2( Graphics2D g ) {
		// _0_0_0_0_0_7_2_0
		AffineTransform trans_0_0_0_0_0_7_2_0 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_0( g );
		g.setTransform( trans_0_0_0_0_0_7_2_0 );
		// _0_0_0_0_0_7_2_1
		AffineTransform trans_0_0_0_0_0_7_2_1 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_1( g );
		g.setTransform( trans_0_0_0_0_0_7_2_1 );
		// _0_0_0_0_0_7_2_2
		AffineTransform trans_0_0_0_0_0_7_2_2 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_2( g );
		g.setTransform( trans_0_0_0_0_0_7_2_2 );
		// _0_0_0_0_0_7_2_3
		AffineTransform trans_0_0_0_0_0_7_2_3 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_3( g );
		g.setTransform( trans_0_0_0_0_0_7_2_3 );
		// _0_0_0_0_0_7_2_4
		AffineTransform trans_0_0_0_0_0_7_2_4 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_4( g );
		g.setTransform( trans_0_0_0_0_0_7_2_4 );
		// _0_0_0_0_0_7_2_5
		AffineTransform trans_0_0_0_0_0_7_2_5 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_5( g );
		g.setTransform( trans_0_0_0_0_0_7_2_5 );
		// _0_0_0_0_0_7_2_6
		AffineTransform trans_0_0_0_0_0_7_2_6 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_6( g );
		g.setTransform( trans_0_0_0_0_0_7_2_6 );
		// _0_0_0_0_0_7_2_7
		AffineTransform trans_0_0_0_0_0_7_2_7 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_7( g );
		g.setTransform( trans_0_0_0_0_0_7_2_7 );
		// _0_0_0_0_0_7_2_8
		AffineTransform trans_0_0_0_0_0_7_2_8 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_8( g );
		g.setTransform( trans_0_0_0_0_0_7_2_8 );
		// _0_0_0_0_0_7_2_9
		AffineTransform trans_0_0_0_0_0_7_2_9 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_9( g );
		g.setTransform( trans_0_0_0_0_0_7_2_9 );
		// _0_0_0_0_0_7_2_10
		AffineTransform trans_0_0_0_0_0_7_2_10 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_10( g );
		g.setTransform( trans_0_0_0_0_0_7_2_10 );
		// _0_0_0_0_0_7_2_11
		g.setComposite( AlphaComposite.getInstance( 3, 0.6456311f * origAlpha ) );
		AffineTransform trans_0_0_0_0_0_7_2_11 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_11( g );
		g.setTransform( trans_0_0_0_0_0_7_2_11 );
		// _0_0_0_0_0_7_2_12
		AffineTransform trans_0_0_0_0_0_7_2_12 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_12( g );
		g.setTransform( trans_0_0_0_0_0_7_2_12 );
		// _0_0_0_0_0_7_2_13
		AffineTransform trans_0_0_0_0_0_7_2_13 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_13( g );
		g.setTransform( trans_0_0_0_0_0_7_2_13 );
		// _0_0_0_0_0_7_2_14
		AffineTransform trans_0_0_0_0_0_7_2_14 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_14( g );
		g.setTransform( trans_0_0_0_0_0_7_2_14 );
		// _0_0_0_0_0_7_2_15
		AffineTransform trans_0_0_0_0_0_7_2_15 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_15( g );
		g.setTransform( trans_0_0_0_0_0_7_2_15 );
		// _0_0_0_0_0_7_2_16
		AffineTransform trans_0_0_0_0_0_7_2_16 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_16( g );
		g.setTransform( trans_0_0_0_0_0_7_2_16 );
		// _0_0_0_0_0_7_2_17
		AffineTransform trans_0_0_0_0_0_7_2_17 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_17( g );
		g.setTransform( trans_0_0_0_0_0_7_2_17 );
		// _0_0_0_0_0_7_2_18
		AffineTransform trans_0_0_0_0_0_7_2_18 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_18( g );
		g.setTransform( trans_0_0_0_0_0_7_2_18 );
		// _0_0_0_0_0_7_2_19
		AffineTransform trans_0_0_0_0_0_7_2_19 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_19( g );
		g.setTransform( trans_0_0_0_0_0_7_2_19 );
		// _0_0_0_0_0_7_2_20
		AffineTransform trans_0_0_0_0_0_7_2_20 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_20( g );
		g.setTransform( trans_0_0_0_0_0_7_2_20 );
		// _0_0_0_0_0_7_2_21
		AffineTransform trans_0_0_0_0_0_7_2_21 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_21( g );
		g.setTransform( trans_0_0_0_0_0_7_2_21 );
		// _0_0_0_0_0_7_2_22
		AffineTransform trans_0_0_0_0_0_7_2_22 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_22( g );
		g.setTransform( trans_0_0_0_0_0_7_2_22 );
		// _0_0_0_0_0_7_2_23
		AffineTransform trans_0_0_0_0_0_7_2_23 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_23( g );
		g.setTransform( trans_0_0_0_0_0_7_2_23 );
		// _0_0_0_0_0_7_2_24
		AffineTransform trans_0_0_0_0_0_7_2_24 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_24( g );
		g.setTransform( trans_0_0_0_0_0_7_2_24 );
		// _0_0_0_0_0_7_2_25
		AffineTransform trans_0_0_0_0_0_7_2_25 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_25( g );
		g.setTransform( trans_0_0_0_0_0_7_2_25 );
		// _0_0_0_0_0_7_2_26
		AffineTransform trans_0_0_0_0_0_7_2_26 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_26( g );
		g.setTransform( trans_0_0_0_0_0_7_2_26 );
		// _0_0_0_0_0_7_2_27
		AffineTransform trans_0_0_0_0_0_7_2_27 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_27( g );
		g.setTransform( trans_0_0_0_0_0_7_2_27 );
		// _0_0_0_0_0_7_2_28
		AffineTransform trans_0_0_0_0_0_7_2_28 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_28( g );
		g.setTransform( trans_0_0_0_0_0_7_2_28 );
		// _0_0_0_0_0_7_2_29
		AffineTransform trans_0_0_0_0_0_7_2_29 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_29( g );
		g.setTransform( trans_0_0_0_0_0_7_2_29 );
		// _0_0_0_0_0_7_2_30
		AffineTransform trans_0_0_0_0_0_7_2_30 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_30( g );
		g.setTransform( trans_0_0_0_0_0_7_2_30 );
		// _0_0_0_0_0_7_2_31
		AffineTransform trans_0_0_0_0_0_7_2_31 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_31( g );
		g.setTransform( trans_0_0_0_0_0_7_2_31 );
		// _0_0_0_0_0_7_2_32
		AffineTransform trans_0_0_0_0_0_7_2_32 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_32( g );
		g.setTransform( trans_0_0_0_0_0_7_2_32 );
		// _0_0_0_0_0_7_2_33
		AffineTransform trans_0_0_0_0_0_7_2_33 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_33( g );
		g.setTransform( trans_0_0_0_0_0_7_2_33 );
		// _0_0_0_0_0_7_2_34
		AffineTransform trans_0_0_0_0_0_7_2_34 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_34( g );
		g.setTransform( trans_0_0_0_0_0_7_2_34 );
		// _0_0_0_0_0_7_2_35
		g.setComposite( AlphaComposite.getInstance( 3, 1.0f * origAlpha ) );
		AffineTransform trans_0_0_0_0_0_7_2_35 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_35( g );
		g.setTransform( trans_0_0_0_0_0_7_2_35 );
		// _0_0_0_0_0_7_2_36
		AffineTransform trans_0_0_0_0_0_7_2_36 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_36( g );
		g.setTransform( trans_0_0_0_0_0_7_2_36 );
		// _0_0_0_0_0_7_2_37
		AffineTransform trans_0_0_0_0_0_7_2_37 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_37( g );
		g.setTransform( trans_0_0_0_0_0_7_2_37 );
		// _0_0_0_0_0_7_2_38
		AffineTransform trans_0_0_0_0_0_7_2_38 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_38( g );
		g.setTransform( trans_0_0_0_0_0_7_2_38 );
		// _0_0_0_0_0_7_2_39
		AffineTransform trans_0_0_0_0_0_7_2_39 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_39( g );
		g.setTransform( trans_0_0_0_0_0_7_2_39 );
		// _0_0_0_0_0_7_2_40
		AffineTransform trans_0_0_0_0_0_7_2_40 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_40( g );
		g.setTransform( trans_0_0_0_0_0_7_2_40 );
		// _0_0_0_0_0_7_2_41
		AffineTransform trans_0_0_0_0_0_7_2_41 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_41( g );
		g.setTransform( trans_0_0_0_0_0_7_2_41 );
		// _0_0_0_0_0_7_2_42
		AffineTransform trans_0_0_0_0_0_7_2_42 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_42( g );
		g.setTransform( trans_0_0_0_0_0_7_2_42 );
		// _0_0_0_0_0_7_2_43
		AffineTransform trans_0_0_0_0_0_7_2_43 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_43( g );
		g.setTransform( trans_0_0_0_0_0_7_2_43 );
		// _0_0_0_0_0_7_2_44
		AffineTransform trans_0_0_0_0_0_7_2_44 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_44( g );
		g.setTransform( trans_0_0_0_0_0_7_2_44 );
		// _0_0_0_0_0_7_2_45
		AffineTransform trans_0_0_0_0_0_7_2_45 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_45( g );
		g.setTransform( trans_0_0_0_0_0_7_2_45 );
		// _0_0_0_0_0_7_2_46
		g.setComposite( AlphaComposite.getInstance( 3, 0.6456311f * origAlpha ) );
		AffineTransform trans_0_0_0_0_0_7_2_46 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_46( g );
		g.setTransform( trans_0_0_0_0_0_7_2_46 );
		// _0_0_0_0_0_7_2_47
		AffineTransform trans_0_0_0_0_0_7_2_47 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_47( g );
		g.setTransform( trans_0_0_0_0_0_7_2_47 );
		// _0_0_0_0_0_7_2_48
		AffineTransform trans_0_0_0_0_0_7_2_48 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_48( g );
		g.setTransform( trans_0_0_0_0_0_7_2_48 );
		// _0_0_0_0_0_7_2_49
		AffineTransform trans_0_0_0_0_0_7_2_49 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_49( g );
		g.setTransform( trans_0_0_0_0_0_7_2_49 );
		// _0_0_0_0_0_7_2_50
		AffineTransform trans_0_0_0_0_0_7_2_50 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_50( g );
		g.setTransform( trans_0_0_0_0_0_7_2_50 );
		// _0_0_0_0_0_7_2_51
		AffineTransform trans_0_0_0_0_0_7_2_51 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_51( g );
		g.setTransform( trans_0_0_0_0_0_7_2_51 );
		// _0_0_0_0_0_7_2_52
		AffineTransform trans_0_0_0_0_0_7_2_52 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_52( g );
		g.setTransform( trans_0_0_0_0_0_7_2_52 );
		// _0_0_0_0_0_7_2_53
		AffineTransform trans_0_0_0_0_0_7_2_53 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_53( g );
		g.setTransform( trans_0_0_0_0_0_7_2_53 );
		// _0_0_0_0_0_7_2_54
		AffineTransform trans_0_0_0_0_0_7_2_54 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_54( g );
		g.setTransform( trans_0_0_0_0_0_7_2_54 );
		// _0_0_0_0_0_7_2_55
		AffineTransform trans_0_0_0_0_0_7_2_55 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_55( g );
		g.setTransform( trans_0_0_0_0_0_7_2_55 );
		// _0_0_0_0_0_7_2_56
		AffineTransform trans_0_0_0_0_0_7_2_56 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_56( g );
		g.setTransform( trans_0_0_0_0_0_7_2_56 );
		// _0_0_0_0_0_7_2_57
		AffineTransform trans_0_0_0_0_0_7_2_57 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_57( g );
		g.setTransform( trans_0_0_0_0_0_7_2_57 );
		// _0_0_0_0_0_7_2_58
		AffineTransform trans_0_0_0_0_0_7_2_58 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_58( g );
		g.setTransform( trans_0_0_0_0_0_7_2_58 );
		// _0_0_0_0_0_7_2_59
		AffineTransform trans_0_0_0_0_0_7_2_59 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_59( g );
		g.setTransform( trans_0_0_0_0_0_7_2_59 );
		// _0_0_0_0_0_7_2_60
		AffineTransform trans_0_0_0_0_0_7_2_60 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_60( g );
		g.setTransform( trans_0_0_0_0_0_7_2_60 );
		// _0_0_0_0_0_7_2_61
		AffineTransform trans_0_0_0_0_0_7_2_61 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_61( g );
		g.setTransform( trans_0_0_0_0_0_7_2_61 );
		// _0_0_0_0_0_7_2_62
		AffineTransform trans_0_0_0_0_0_7_2_62 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_62( g );
		g.setTransform( trans_0_0_0_0_0_7_2_62 );
		// _0_0_0_0_0_7_2_63
		AffineTransform trans_0_0_0_0_0_7_2_63 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_63( g );
		g.setTransform( trans_0_0_0_0_0_7_2_63 );
		// _0_0_0_0_0_7_2_64
		AffineTransform trans_0_0_0_0_0_7_2_64 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_64( g );
		g.setTransform( trans_0_0_0_0_0_7_2_64 );
		// _0_0_0_0_0_7_2_65
		AffineTransform trans_0_0_0_0_0_7_2_65 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_65( g );
		g.setTransform( trans_0_0_0_0_0_7_2_65 );
		// _0_0_0_0_0_7_2_66
		AffineTransform trans_0_0_0_0_0_7_2_66 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_66( g );
		g.setTransform( trans_0_0_0_0_0_7_2_66 );
		// _0_0_0_0_0_7_2_67
		AffineTransform trans_0_0_0_0_0_7_2_67 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_67( g );
		g.setTransform( trans_0_0_0_0_0_7_2_67 );
		// _0_0_0_0_0_7_2_68
		AffineTransform trans_0_0_0_0_0_7_2_68 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_68( g );
		g.setTransform( trans_0_0_0_0_0_7_2_68 );
		// _0_0_0_0_0_7_2_69
		AffineTransform trans_0_0_0_0_0_7_2_69 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_2_69( g );
		g.setTransform( trans_0_0_0_0_0_7_2_69 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_0( Graphics2D g ) {
		GeneralPath shape219 = new GeneralPath();
		shape219.moveTo( -132.39502, -132.53253 );
		shape219.curveTo( -132.78226, -132.74329, -132.38332, -134.5506, -134.23735, -134.31387 );
		shape219.curveTo( -136.09142, -134.07713, -142.0526, -134.12912, -142.0526, -134.12912 );
		shape219.lineTo( -144.99408, -131.88876 );
		shape219.lineTo( -146.26927, -125.86054 );
		shape219.lineTo( -149.37119, -116.595924 );
		shape219.lineTo( -158.95056, -111.090294 );
		shape219.lineTo( -155.8683, -106.71347 );
		shape219.lineTo( -153.53702, -106.79427 );
		shape219.lineTo( -153.23976, -103.95916 );
		shape219.lineTo( -155.78229, -98.38711 );
		shape219.lineTo( -154.99219, -92.94785 );
		shape219.lineTo( -156.88928, -93.020035 );
		shape219.lineTo( -155.4694, -87.96476 );
		shape219.lineTo( -148.56165, -86.25559 );
		shape219.lineTo( -92.603004, -122.944405 );
		shape219.lineTo( -96.71791, -127.02674 );
		shape219.lineTo( -97.73094, -137.80421 );
		shape219.lineTo( -117.87928, -133.89806 );
		shape219.lineTo( -127.34128, -135.7256 );
		shape219.lineTo( -122.56923, -131.59708 );
		shape219.lineTo( -132.395, -132.53252 );
		shape219.lineTo( -132.395, -132.53255 );
		shape219.closePath();
		g.setPaint( new LinearGradientPaint( new Point2D.Double( -190.5, 104.1875 ), new Point2D.Double( -96.75, 104.1875 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 237, 244, 252, 255 ), new Color( 192, 198, 214, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( -0.7197251915931702f, 0.4388335943222046f, 0.5945504903793335f, 0.5312241911888123f, -297.7123107910156f, -110.38337707519531f ) ) );
		g.fill( shape219 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_1( Graphics2D g ) {
		GeneralPath shape220 = new GeneralPath();
		shape220.moveTo( -134.39539, -101.38344 );
		shape220.lineTo( -147.14307, -103.441956 );
		shape220.curveTo( -147.14307, -103.441956, -148.18748, -95.99042, -148.47304, -95.7046 );
		shape220.curveTo( -148.75858, -95.41878, -153.93748, -91.25563, -153.93748, -91.25563 );
		shape220.lineTo( -144.36986, -92.17369 );
		shape220.lineTo( -142.10507, -93.6663 );
		shape220.lineTo( -132.05634, -92.53165 );
		shape220.lineTo( -134.39539, -101.38344 );
		shape220.lineTo( -134.39539, -101.38344 );
		shape220.lineTo( -134.39539, -101.38344 );
		shape220.closePath();
		g.setPaint( new Color( 252, 253, 255, 255 ) );
		g.fill( shape220 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_2( Graphics2D g ) {
		GeneralPath shape221 = new GeneralPath();
		shape221.moveTo( -75.17744, -117.08164 );
		shape221.lineTo( -75.74074, -110.14688 );
		shape221.lineTo( -85.51964, -101.1681 );
		shape221.lineTo( -96.585365, -102.12953 );
		shape221.lineTo( -94.82121, -110.01991 );
		shape221.lineTo( -88.40236, -114.157074 );
		shape221.lineTo( -77.18797, -115.63235 );
		shape221.lineTo( -76.08882, -117.08453 );
		shape221.lineTo( -75.17743, -117.081535 );
		shape221.lineTo( -75.17743, -117.08164 );
		shape221.closePath();
		g.setPaint( new LinearGradientPaint( new Point2D.Double( -228.375, 89.875 ), new Point2D.Double( -196.875, 89.875 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 189, 208, 225, 255 ), new Color( 255, 255, 255, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( -0.7197251915931702f, 0.4388335943222046f, 0.5945504903793335f, 0.5312241911888123f, -291.4935302734375f, -63.27872848510742f ) ) );
		g.fill( shape221 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_3( Graphics2D g ) {
		GeneralPath shape222 = new GeneralPath();
		shape222.moveTo( -114.43921, -99.37829 );
		shape222.lineTo( -115.87085, -97.2765 );
		shape222.lineTo( -128.43471, -94.978424 );
		shape222.lineTo( -129.20921, -92.83044 );
		shape222.lineTo( -134.51717, -92.163536 );
		shape222.lineTo( -127.75409, -96.398865 );
		shape222.lineTo( -116.59839, -98.06178 );
		shape222.lineTo( -115.40927, -99.56883 );
		shape222.lineTo( -114.43921, -99.37828 );
		shape222.lineTo( -114.43921, -99.37828 );
		shape222.lineTo( -114.43921, -99.37828 );
		shape222.closePath();
		g.setPaint( new Color( 255, 255, 255, 255 ) );
		g.fill( shape222 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_4( Graphics2D g ) {
		GeneralPath shape223 = new GeneralPath();
		shape223.moveTo( -84.48459, -118.00424 );
		shape223.lineTo( -106.1819, -114.941124 );
		shape223.lineTo( -112.12742, -117.68386 );
		shape223.lineTo( -111.67369, -116.06132 );
		shape223.lineTo( -106.71387, -113.38787 );
		shape223.lineTo( -108.39193, -112.58816 );
		shape223.lineTo( -116.739136, -113.419655 );
		shape223.lineTo( -106.73734, -111.921234 );
		shape223.lineTo( -90.05072, -114.05184 );
		shape223.lineTo( -84.48458, -118.004234 );
		shape223.closePath();
		g.fill( shape223 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_5( Graphics2D g ) {
		GeneralPath shape224 = new GeneralPath();
		shape224.moveTo( -106.22024, -109.61507 );
		shape224.lineTo( -102.92678, -100.45148 );
		shape224.lineTo( -117.9588, -102.79294 );
		shape224.lineTo( -121.7139, -99.3862 );
		shape224.lineTo( -123.235466, -102.36856 );
		shape224.lineTo( -118.744995, -106.33539 );
		shape224.lineTo( -106.22024, -109.61507 );
		shape224.lineTo( -106.22024, -109.61507 );
		shape224.lineTo( -106.22024, -109.61507 );
		shape224.closePath();
		g.setPaint( new Color( 185, 200, 219, 255 ) );
		g.fill( shape224 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_6( Graphics2D g ) {
		GeneralPath shape225 = new GeneralPath();
		shape225.moveTo( -147.66048, -96.62752 );
		shape225.curveTo( -147.21457, -96.22911, -137.18544, -86.59201, -137.18544, -86.59201 );
		shape225.lineTo( -136.75516, -88.64183 );
		shape225.lineTo( -146.52614, -96.42542 );
		shape225.lineTo( -147.66048, -96.627525 );
		shape225.closePath();
		g.setPaint( new Color( 180, 198, 218, 255 ) );
		g.fill( shape225 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_7( Graphics2D g ) {
		GeneralPath shape226 = new GeneralPath();
		shape226.moveTo( -91.19981, -104.92167 );
		shape226.lineTo( -89.09154, -94.923706 );
		shape226.lineTo( -94.74372, -94.158646 );
		shape226.lineTo( -94.40735, -90.34193 );
		shape226.lineTo( -97.86901, -98.84438 );
		shape226.lineTo( -109.36112, -95.859184 );
		shape226.lineTo( -113.828125, -90.78948 );
		shape226.lineTo( -119.72672, -91.32649 );
		shape226.lineTo( -117.778755, -94.860275 );
		shape226.lineTo( -109.71316, -96.98514 );
		shape226.lineTo( -104.52645, -99.92417 );
		shape226.lineTo( -96.42565, -100.394745 );
		shape226.lineTo( -91.19982, -104.92168 );
		shape226.lineTo( -91.19982, -104.92168 );
		shape226.lineTo( -91.19982, -104.92168 );
		shape226.closePath();
		g.setPaint( new Color( 186, 189, 182, 255 ) );
		g.fill( shape226 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_8( Graphics2D g ) {
		GeneralPath shape227 = new GeneralPath();
		shape227.moveTo( -137.23425, -121.96007 );
		shape227.lineTo( -139.48343, -115.449715 );
		shape227.lineTo( -133.54181, -114.60378 );
		shape227.lineTo( -135.44278, -121.71177 );
		shape227.lineTo( -137.23425, -121.96006 );
		shape227.closePath();
		g.setPaint( new Color( 217, 225, 236, 255 ) );
		g.fill( shape227 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_9( Graphics2D g ) {
		GeneralPath shape228 = new GeneralPath();
		shape228.moveTo( -137.4622, -109.68778 );
		shape228.lineTo( -126.73609, -99.4702 );
		shape228.lineTo( -134.0427, -101.561104 );
		shape228.lineTo( -139.06201, -99.54806 );
		shape228.lineTo( -145.45647, -98.922165 );
		shape228.lineTo( -137.4622, -109.68778 );
		shape228.lineTo( -137.4622, -109.68778 );
		shape228.lineTo( -137.4622, -109.68778 );
		shape228.closePath();
		g.setPaint( new Color( 186, 189, 182, 255 ) );
		g.fill( shape228 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_10( Graphics2D g ) {
		GeneralPath shape229 = new GeneralPath();
		shape229.moveTo( -135.87839, -117.10134 );
		shape229.lineTo( -126.72545, -108.38234 );
		shape229.lineTo( -132.96043, -110.16657 );
		shape229.lineTo( -137.24358, -108.44878 );
		shape229.lineTo( -142.70018, -107.91469 );
		shape229.lineTo( -135.8784, -117.10135 );
		shape229.lineTo( -135.8784, -117.10135 );
		shape229.lineTo( -135.8784, -117.10135 );
		shape229.closePath();
		g.setPaint( new Color( 252, 253, 255, 255 ) );
		g.fill( shape229 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_11( Graphics2D g ) {
		GeneralPath shape230 = new GeneralPath();
		shape230.moveTo( -143.28148, -136.88266 );
		shape230.lineTo( -137.94614, -137.11938 );
		shape230.lineTo( -140.47691, -136.13489 );
		shape230.lineTo( -143.28148, -136.88264 );
		shape230.closePath();
		g.setPaint( new Color( 192, 198, 214, 255 ) );
		g.fill( shape230 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_12( Graphics2D g ) {
		GeneralPath shape231 = new GeneralPath();
		shape231.moveTo( -123.52238, -114.61906 );
		shape231.lineTo( -125.11831, -109.95932 );
		shape231.lineTo( -122.77531, -109.48872 );
		shape231.lineTo( -124.82888, -105.77883 );
		shape231.lineTo( -117.46343, -110.82831 );
		shape231.lineTo( -119.48567, -115.06943 );
		shape231.lineTo( -123.522385, -114.61906 );
		shape231.lineTo( -123.522385, -114.61906 );
		shape231.lineTo( -123.522385, -114.61906 );
		shape231.closePath();
		g.fill( shape231 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_13( Graphics2D g ) {
		GeneralPath shape232 = new GeneralPath();
		shape232.moveTo( -133.04099, -121.66503 );
		shape232.curveTo( -133.72943, -121.46871, -139.83142, -120.42938, -139.83142, -120.42938 );
		shape232.lineTo( -140.17957, -117.089035 );
		shape232.lineTo( -137.98518, -119.32075 );
		shape232.lineTo( -133.04099, -121.66504 );
		shape232.closePath();
		g.fill( shape232 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_14( Graphics2D g ) {
		GeneralPath shape233 = new GeneralPath();
		shape233.moveTo( -115.65102, -115.25619 );
		shape233.lineTo( -106.83447, -105.2149 );
		shape233.lineTo( -93.55867, -111.74543 );
		shape233.lineTo( -95.08415, -116.6246 );
		shape233.lineTo( -102.86811, -116.6824 );
		shape233.lineTo( -105.66095, -114.30923 );
		shape233.lineTo( -114.6966, -114.94442 );
		shape233.lineTo( -115.65101, -115.25623 );
		shape233.lineTo( -115.65101, -115.2562 );
		shape233.lineTo( -115.65101, -115.2562 );
		shape233.closePath();
		g.fill( shape233 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_15( Graphics2D g ) {
		GeneralPath shape234 = new GeneralPath();
		shape234.moveTo( -115.36851, -107.04673 );
		shape234.lineTo( -121.145836, -107.88111 );
		shape234.lineTo( -134.41379, -105.265465 );
		shape234.lineTo( -130.15807, -99.70493 );
		shape234.lineTo( -132.10603, -96.17115 );
		shape234.lineTo( -130.1933, -96.22025 );
		shape234.lineTo( -128.83206, -99.061134 );
		shape234.lineTo( -134.15955, -105.308784 );
		shape234.lineTo( -115.368515, -107.046745 );
		shape234.lineTo( -115.368515, -107.04674 );
		shape234.lineTo( -115.368515, -107.04674 );
		shape234.closePath();
		g.fill( shape234 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_16( Graphics2D g ) {
		GeneralPath shape235 = new GeneralPath();
		shape235.moveTo( -134.92313, -119.70906 );
		shape235.lineTo( -137.02757, -114.96271 );
		shape235.lineTo( -132.78746, -114.41993 );
		shape235.lineTo( -134.92313, -119.70906 );
		shape235.lineTo( -134.92313, -119.70906 );
		shape235.lineTo( -134.92313, -119.70906 );
		shape235.closePath();
		g.fill( shape235 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_17( Graphics2D g ) {
		GeneralPath shape236 = new GeneralPath();
		shape236.moveTo( -144.7842, -104.21853 );
		shape236.curveTo( -144.7842, -104.21853, -135.91676, -100.3527, -136.46829, -100.57501 );
		shape236.curveTo( -137.01982, -100.79731, -146.09067, -103.0868, -146.09067, -103.0868 );
		shape236.lineTo( -153.01411, -99.53571 );
		shape236.lineTo( -144.78421, -104.21854 );
		shape236.lineTo( -144.78421, -104.21854 );
		shape236.lineTo( -144.78421, -104.21854 );
		shape236.closePath();
		g.fill( shape236 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_18( Graphics2D g ) {
		GeneralPath shape237 = new GeneralPath();
		shape237.moveTo( -106.29318, -120.61398 );
		shape237.lineTo( -118.26643, -114.542496 );
		shape237.lineTo( -109.508545, -107.25838 );
		shape237.lineTo( -108.33502, -116.35268 );
		shape237.lineTo( -104.885056, -116.110146 );
		shape237.lineTo( -103.21484, -112.99499 );
		shape237.lineTo( -102.068756, -114.81096 );
		shape237.lineTo( -105.59303, -117.68938 );
		shape237.lineTo( -106.29318, -120.61398 );
		shape237.lineTo( -106.29318, -120.61398 );
		shape237.lineTo( -106.29318, -120.61398 );
		shape237.closePath();
		g.fill( shape237 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_19( Graphics2D g ) {
		GeneralPath shape238 = new GeneralPath();
		shape238.moveTo( -91.16675, -96.86413 );
		shape238.curveTo( -91.44839, -97.251, -96.423836, -99.803185, -96.423836, -99.803185 );
		shape238.lineTo( -95.0509, -96.953636 );
		shape238.lineTo( -98.83337, -96.54658 );
		shape238.lineTo( -105.967995, -97.782265 );
		shape238.lineTo( -95.074394, -92.9175 );
		shape238.lineTo( -91.16675, -96.86412 );
		shape238.closePath();
		g.fill( shape238 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_20( Graphics2D g ) {
		GeneralPath shape239 = new GeneralPath();
		shape239.moveTo( -138.2815, -105.49613 );
		shape239.curveTo( -138.14458, -105.91475, -137.93335, -108.83647, -137.93335, -108.83647 );
		shape239.lineTo( -132.26942, -111.6196 );
		shape239.lineTo( -131.47539, -109.42253 );
		shape239.lineTo( -138.2815, -105.49613 );
		shape239.closePath();
		g.fill( shape239 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_21( Graphics2D g ) {
		GeneralPath shape240 = new GeneralPath();
		shape240.moveTo( -103.72688, -141.6636 );
		shape240.lineTo( -114.068985, -141.16707 );
		shape240.lineTo( -108.31122, -139.53874 );
		shape240.lineTo( -103.72689, -141.6636 );
		shape240.lineTo( -103.72689, -141.6636 );
		shape240.lineTo( -103.72689, -141.6636 );
		shape240.closePath();
		g.fill( shape240 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_22( Graphics2D g ) {
		GeneralPath shape241 = new GeneralPath();
		shape241.moveTo( -128.75777, -107.38237 );
		shape241.lineTo( -135.247, -109.1233 );
		shape241.lineTo( -131.02255, -105.889755 );
		shape241.lineTo( -128.75777, -107.38236 );
		shape241.closePath();
		g.fill( shape241 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_23( Graphics2D g ) {
		GeneralPath shape242 = new GeneralPath();
		shape242.moveTo( -115.52779, -113.62524 );
		shape242.lineTo( -125.553055, -113.657036 );
		shape242.lineTo( -131.0018, -117.03782 );
		shape242.lineTo( -130.04741, -114.15651 );
		shape242.lineTo( -126.096756, -112.65522 );
		shape242.lineTo( -115.52778, -113.62525 );
		shape242.lineTo( -115.52778, -113.625244 );
		shape242.closePath();
		g.fill( shape242 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_24( Graphics2D g ) {
		GeneralPath shape243 = new GeneralPath();
		shape243.moveTo( -100.97263, -122.36054 );
		shape243.lineTo( -98.96604, -117.99816 );
		shape243.lineTo( -103.1631, -120.80152 );
		shape243.lineTo( -100.97263, -122.36054 );
		shape243.lineTo( -100.97263, -122.36054 );
		shape243.lineTo( -100.97263, -122.36054 );
		shape243.closePath();
		g.fill( shape243 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_25( Graphics2D g ) {
		GeneralPath shape244 = new GeneralPath();
		shape244.moveTo( -97.46421, -118.17365 );
		shape244.lineTo( -97.31951, -114.79865 );
		shape244.lineTo( -94.16681, -114.821754 );
		shape244.lineTo( -95.70797, -111.87116 );
		shape244.lineTo( -99.00931, -111.98088 );
		shape244.lineTo( -97.46421, -118.17366 );
		shape244.lineTo( -97.46421, -118.17365 );
		shape244.lineTo( -97.46421, -118.17365 );
		shape244.closePath();
		g.setPaint( new Color( 145, 159, 186, 255 ) );
		g.fill( shape244 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_26( Graphics2D g ) {
		GeneralPath shape245 = new GeneralPath();
		shape245.moveTo( -144.14174, -130.13132 );
		shape245.curveTo( -143.95007, -129.68959, -142.97221, -125.70543, -142.97221, -125.70543 );
		shape245.lineTo( -146.34395, -126.55424 );
		shape245.lineTo( -144.14172, -130.13132 );
		shape245.lineTo( -144.14172, -130.13132 );
		shape245.lineTo( -144.14172, -130.13132 );
		shape245.closePath();
		g.fill( shape245 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_27( Graphics2D g ) {
		GeneralPath shape246 = new GeneralPath();
		shape246.moveTo( -88.66009, -101.76395 );
		shape246.lineTo( -89.86095, -98.23883 );
		shape246.lineTo( -95.79867, -97.18795 );
		shape246.lineTo( -99.59286, -97.33231 );
		shape246.lineTo( -95.38013, -99.7892 );
		shape246.lineTo( -94.656494, -98.33122 );
		shape246.lineTo( -91.0344, -99.42252 );
		shape246.lineTo( -88.66008, -101.76394 );
		shape246.lineTo( -88.66008, -101.76394 );
		shape246.lineTo( -88.66008, -101.76394 );
		shape246.closePath();
		g.fill( shape246 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_28( Graphics2D g ) {
		GeneralPath shape247 = new GeneralPath();
		shape247.moveTo( -143.93529, -125.38695 );
		shape247.lineTo( -144.79587, -118.7178 );
		shape247.lineTo( -141.73703, -120.58285 );
		shape247.lineTo( -142.41762, -124.3014 );
		shape247.lineTo( -139.60522, -124.89902 );
		shape247.lineTo( -143.93529, -125.38695 );
		shape247.closePath();
		g.fill( shape247 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_29( Graphics2D g ) {
		GeneralPath shape248 = new GeneralPath();
		shape248.moveTo( -118.91435, -123.78149 );
		shape248.curveTo( -118.91435, -123.78149, -122.17662, -115.201096, -121.63683, -115.53023 );
		shape248.curveTo( -121.09703, -115.85936, -118.143814, -117.54829, -118.143814, -117.54829 );
		shape248.lineTo( -118.91435, -123.78149 );
		shape248.closePath();
		g.fill( shape248 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_30( Graphics2D g ) {
		GeneralPath shape249 = new GeneralPath();
		shape249.moveTo( -111.46347, -107.91678 );
		shape249.curveTo( -111.46347, -107.91678, -107.59883, -114.74182, -107.80614, -115.06228 );
		shape249.curveTo( -108.01345, -115.38275, -109.30814, -118.83859, -109.30814, -118.83859 );
		shape249.lineTo( -106.53488, -115.27882 );
		shape249.lineTo( -107.7162, -109.97815 );
		shape249.lineTo( -111.46346, -107.91679 );
		shape249.closePath();
		g.fill( shape249 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_31( Graphics2D g ) {
		GeneralPath shape250 = new GeneralPath();
		shape250.moveTo( -118.62937, -101.40666 );
		shape250.lineTo( -115.25369, -103.80004 );
		shape250.lineTo( -114.40486, -108.45112 );
		shape250.lineTo( -119.25907, -111.30068 );
		shape250.lineTo( -116.80265, -107.21257 );
		shape250.lineTo( -118.62937, -101.40666 );
		shape250.lineTo( -118.62937, -101.40666 );
		shape250.lineTo( -118.62937, -101.40666 );
		shape250.closePath();
		g.fill( shape250 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_32( Graphics2D g ) {
		GeneralPath shape251 = new GeneralPath();
		shape251.moveTo( -136.38467, -101.42022 );
		shape251.lineTo( -136.65065, -104.49785 );
		shape251.lineTo( -130.97108, -107.40223 );
		shape251.lineTo( -136.8071, -105.85477 );
		shape251.lineTo( -142.87779, -107.62746 );
		shape251.lineTo( -137.51509, -104.86451 );
		shape251.lineTo( -138.90372, -99.8843 );
		shape251.lineTo( -136.38467, -101.42022 );
		shape251.lineTo( -136.38467, -101.42022 );
		shape251.lineTo( -136.38467, -101.42022 );
		shape251.closePath();
		g.fill( shape251 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_33( Graphics2D g ) {
		GeneralPath shape252 = new GeneralPath();
		shape252.moveTo( -135.45757, -125.49986 );
		shape252.lineTo( -145.1582, -122.26637 );
		shape252.lineTo( -155.2343, -126.400696 );
		shape252.lineTo( -158.67255, -123.52229 );
		shape252.lineTo( -152.69577, -115.883064 );
		shape252.lineTo( -151.1272, -123.54247 );
		shape252.lineTo( -143.78917, -121.31363 );
		shape252.lineTo( -138.09006, -122.44245 );
		shape252.lineTo( -135.45757, -125.49986 );
		shape252.lineTo( -135.45757, -125.49986 );
		shape252.lineTo( -135.45757, -125.49986 );
		shape252.closePath();
		g.setPaint( new Color( 255, 255, 255, 255 ) );
		g.fill( shape252 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_34( Graphics2D g ) {
		GeneralPath shape253 = new GeneralPath();
		shape253.moveTo( -94.8302, -126.59109 );
		shape253.lineTo( -95.86286, -123.727104 );
		shape253.lineTo( -101.51505, -120.39255 );
		shape253.lineTo( -101.01435, -124.27278 );
		shape253.lineTo( -94.45469, -126.93176 );
		shape253.lineTo( -94.8302, -126.591095 );
		shape253.lineTo( -94.8302, -126.591095 );
		shape253.lineTo( -94.8302, -126.591095 );
		shape253.closePath();
		g.setPaint( new Color( 223, 227, 235, 255 ) );
		g.fill( shape253 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_35( Graphics2D g ) {
		GeneralPath shape254 = new GeneralPath();
		shape254.moveTo( -125.35453, -117.29214 );
		shape254.curveTo( -125.26063, -117.435585, -124.57269, -117.22394, -124.60601, -117.95008 );
		shape254.curveTo( -124.63931, -118.67618, -124.43127, -120.9851, -124.43127, -120.9851 );
		shape254.lineTo( -125.20692, -122.19585 );
		shape254.lineTo( -127.503265, -122.88013 );
		shape254.lineTo( -130.99648, -124.37446 );
		shape254.lineTo( -132.82852, -128.26099 );
		shape254.lineTo( -134.62215, -127.204254 );
		shape254.lineTo( -134.66435, -126.2981 );
		shape254.lineTo( -135.77261, -126.2722 );
		shape254.lineTo( -137.85222, -127.43332 );
		shape254.lineTo( -139.98538, -127.29853 );
		shape254.lineTo( -139.89758, -128.03157 );
		shape254.lineTo( -141.90178, -127.64058 );
		shape254.lineTo( -142.782, -125.01699 );
		shape254.lineTo( -130.32521, -102.17086 );
		shape254.lineTo( -128.61317, -103.63713 );
		shape254.lineTo( -124.40388, -103.69003 );
		shape254.lineTo( -125.282814, -111.622696 );
		shape254.lineTo( -124.27621, -115.23257 );
		shape254.lineTo( -126.02685, -113.51305 );
		shape254.lineTo( -125.35456, -117.292046 );
		shape254.lineTo( -125.35454, -117.29214 );
		shape254.lineTo( -125.35454, -117.29214 );
		shape254.closePath();
		g.setPaint( new LinearGradientPaint( new Point2D.Double( -190.5, 104.1875 ), new Point2D.Double( -96.75, 104.1875 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 237, 244, 252, 255 ), new Color( 192, 198, 214, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( -0.14740629494190216f, -0.2927992045879364f, -0.22464419901371002f, 0.2137041985988617f, -128.72862243652344f, -182.0675811767578f ) ) );
		g.fill( shape254 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_36( Graphics2D g ) {
		GeneralPath shape255 = new GeneralPath();
		shape255.moveTo( -122.48882, -115.01198 );
		shape255.lineTo( -119.4038, -127.55086 );
		shape255.curveTo( -119.4038, -127.55086, -126.74617, -129.19583, -127.007904, -129.5036 );
		shape255.curveTo( -127.269646, -129.81137, -130.99931, -135.31067, -130.99931, -135.31067 );
		shape255.lineTo( -130.85979, -125.70011 );
		shape255.lineTo( -129.55566, -123.3218 );
		shape255.lineTo( -131.50108, -113.3981 );
		shape255.lineTo( -122.48883, -115.01197 );
		shape255.lineTo( -122.48883, -115.01197 );
		shape255.lineTo( -122.48883, -115.01197 );
		shape255.closePath();
		g.setPaint( new Color( 252, 253, 255, 255 ) );
		g.fill( shape255 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_37( Graphics2D g ) {
		GeneralPath shape256 = new GeneralPath();
		shape256.moveTo( -91.66337, -94.91746 );
		shape256.lineTo( -98.529655, -96.041 );
		shape256.lineTo( -106.68626, -106.5155 );
		shape256.lineTo( -104.831055, -117.466896 );
		shape256.lineTo( -97.10963, -115.06899 );
		shape256.lineTo( -93.50636, -108.33592 );
		shape256.lineTo( -92.94493, -97.03886 );
		shape256.lineTo( -91.58661, -95.825615 );
		shape256.lineTo( -91.663506, -94.917465 );
		shape256.lineTo( -91.66335, -94.91746 );
		shape256.lineTo( -91.66335, -94.91746 );
		shape256.closePath();
		g.setPaint( new LinearGradientPaint( new Point2D.Double( -228.375, 89.875 ), new Point2D.Double( -196.875, 89.875 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 189, 208, 225, 255 ), new Color( 255, 255, 255, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( -0.3790521025657654f, -0.7529268264770508f, -0.5776677131652832f, 0.5495356917381287f, -127.75569152832031f, -314.8827819824219f ) ) );
		g.fill( shape256 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_38( Graphics2D g ) {
		GeneralPath shape257 = new GeneralPath();
		shape257.moveTo( -106.58678, -93.99409 );
		shape257.lineTo( -108.56562, -95.59138 );
		shape257.lineTo( -109.83776, -108.30018 );
		shape257.lineTo( -111.9159, -109.24623 );
		shape257.lineTo( -112.15037, -114.59078 );
		shape257.lineTo( -108.47716, -107.50666 );
		shape257.lineTo( -107.72394, -96.252884 );
		shape257.lineTo( -106.31823, -94.945526 );
		shape257.lineTo( -106.58678, -93.994095 );
		shape257.lineTo( -106.58678, -93.994095 );
		shape257.lineTo( -106.58678, -93.994095 );
		shape257.closePath();
		g.setPaint( new Color( 255, 255, 255, 255 ) );
		g.fill( shape257 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_39( Graphics2D g ) {
		GeneralPath shape258 = new GeneralPath();
		shape258.moveTo( -137.34933, -86.69519 );
		shape258.lineTo( -138.64369, -108.56939 );
		shape258.lineTo( -135.42805, -114.27304 );
		shape258.lineTo( -137.08203, -113.95231 );
		shape258.lineTo( -140.1487, -109.22551 );
		shape258.lineTo( -140.80975, -110.96287 );
		shape258.lineTo( -139.3044, -119.21522 );
		shape258.lineTo( -141.60858, -109.36779 );
		shape258.lineTo( -140.83752, -92.563385 );
		shape258.lineTo( -137.3493, -86.6952 );
		shape258.lineTo( -137.3493, -86.6952 );
		shape258.lineTo( -137.3493, -86.6952 );
		shape258.closePath();
		g.fill( shape258 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_40( Graphics2D g ) {
		GeneralPath shape259 = new GeneralPath();
		shape259.moveTo( -100.63056, -95.58696 );
		shape259.lineTo( -110.03095, -93.0471 );
		shape259.lineTo( -106.478775, -107.83987 );
		shape259.lineTo( -109.56994, -111.85875 );
		shape259.lineTo( -106.47406, -113.13358 );
		shape259.lineTo( -102.88426, -108.33635 );
		shape259.lineTo( -100.63057, -95.58697 );
		shape259.lineTo( -100.63057, -95.58697 );
		shape259.lineTo( -100.63057, -95.58697 );
		shape259.closePath();
		g.setPaint( new Color( 185, 200, 219, 255 ) );
		g.fill( shape259 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_41( Graphics2D g ) {
		GeneralPath shape260 = new GeneralPath();
		shape260.moveTo( -130.91817, -120.23411 );
		shape260.curveTo( -131.35141, -119.82196, -141.76971, -110.60698, -141.76971, -110.60698 );
		shape260.lineTo( -139.76152, -110.01196 );
		shape260.lineTo( -131.21156, -119.119896 );
		shape260.lineTo( -130.91818, -120.234116 );
		shape260.closePath();
		g.setPaint( new Color( 180, 198, 218, 255 ) );
		g.fill( shape260 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_42( Graphics2D g ) {
		GeneralPath shape261 = new GeneralPath();
		shape261.moveTo( -144.84949, -98.93359 );
		shape261.lineTo( -154.98544, -97.64265 );
		shape261.lineTo( -155.28984, -103.33824 );
		shape261.lineTo( -159.12126, -103.31234 );
		shape261.lineTo( -150.3662, -106.07344 );
		shape261.lineTo( -152.41008, -117.769714 );
		shape261.lineTo( -157.10101, -122.63294 );
		shape261.lineTo( -156.08766, -128.4686 );
		shape261.lineTo( -152.7234, -126.24061 );
		shape261.lineTo( -151.2593, -118.02931 );
		shape261.lineTo( -148.75034, -112.62145 );
		shape261.lineTo( -148.93793, -104.50916 );
		shape261.lineTo( -144.84947, -98.93359 );
		shape261.lineTo( -144.84947, -98.93359 );
		shape261.lineTo( -144.84947, -98.93359 );
		shape261.closePath();
		g.setPaint( new Color( 177, 186, 203, 255 ) );
		g.fill( shape261 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_43( Graphics2D g ) {
		GeneralPath shape262 = new GeneralPath();
		shape262.moveTo( -114.44417, -107.84219 );
		shape262.lineTo( -120.7508, -110.611664 );
		shape262.lineTo( -122.075554, -104.758156 );
		shape262.lineTo( -114.83686, -106.07674 );
		shape262.lineTo( -114.44417, -107.84219 );
		shape262.lineTo( -114.44417, -107.84219 );
		shape262.lineTo( -114.44417, -107.84219 );
		shape262.closePath();
		g.setPaint( new Color( 217, 225, 236, 255 ) );
		g.fill( shape262 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_44( Graphics2D g ) {
		GeneralPath shape263 = new GeneralPath();
		shape263.moveTo( -97.67246, -111.52222 );
		shape263.lineTo( -108.725845, -101.659584 );
		shape263.lineTo( -106.04958, -108.772675 );
		shape263.lineTo( -107.649155, -113.93864 );
		shape263.lineTo( -107.754684, -120.362785 );
		shape263.lineTo( -97.67246, -111.52222 );
		shape263.lineTo( -97.67246, -111.52222 );
		shape263.lineTo( -97.67246, -111.52222 );
		shape263.closePath();
		g.setPaint( new Color( 186, 189, 182, 255 ) );
		g.fill( shape263 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_45( Graphics2D g ) {
		GeneralPath shape264 = new GeneralPath();
		shape264.moveTo( -116.04751, -107.16817 );
		shape264.lineTo( -125.47972, -98.75206 );
		shape264.lineTo( -123.195984, -104.82191 );
		shape264.lineTo( -124.56097, -109.23019 );
		shape264.lineTo( -124.65096, -114.71213 );
		shape264.lineTo( -116.04747, -107.16817 );
		shape264.lineTo( -116.04751, -107.16817 );
		shape264.lineTo( -116.04751, -107.16817 );
		shape264.closePath();
		g.setPaint( new Color( 252, 253, 255, 255 ) );
		g.fill( shape264 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_46( Graphics2D g ) {
		GeneralPath shape265 = new GeneralPath();
		shape265.moveTo( -138.0984, -117.23 );
		shape265.lineTo( -138.2949, -111.893036 );
		shape265.lineTo( -139.07103, -114.49528 );
		shape265.lineTo( -138.0984, -117.23001 );
		shape265.lineTo( -138.0984, -117.23001 );
		shape265.lineTo( -138.0984, -117.23001 );
		shape265.closePath();
		g.setPaint( new Color( 192, 198, 214, 255 ) );
		g.fill( shape265 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_47( Graphics2D g ) {
		GeneralPath shape266 = new GeneralPath();
		shape266.moveTo( -139.65446, -105.60096 );
		shape266.lineTo( -144.16951, -107.569336 );
		shape266.lineTo( -144.82848, -105.27219 );
		shape266.lineTo( -148.35971, -107.619705 );
		shape266.lineTo( -143.92384, -99.8692 );
		shape266.lineTo( -139.53278, -101.54102 );
		shape266.lineTo( -139.65446, -105.60095 );
		shape266.closePath();
		g.fill( shape266 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_48( Graphics2D g ) {
		GeneralPath shape267 = new GeneralPath();
		shape267.moveTo( -146.23312, -112.18406 );
		shape267.curveTo( -146.373, -112.88614, -146.9143, -119.05231, -146.9143, -119.05231 );
		shape267.lineTo( -150.21545, -119.67006 );
		shape267.lineTo( -148.16896, -117.30201 );
		shape267.lineTo( -146.23312, -112.18406 );
		shape267.lineTo( -146.23312, -112.18406 );
		shape267.lineTo( -146.23312, -112.18406 );
		shape267.closePath();
		g.fill( shape267 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_49( Graphics2D g ) {
		GeneralPath shape268 = new GeneralPath();
		shape268.moveTo( -118.81447, -103.36701 );
		shape268.lineTo( -129.53734, -95.39338 );
		shape268.lineTo( -124.10438, -81.63193 );
		shape268.lineTo( -119.11761, -82.756905 );
		shape268.lineTo( -118.42907, -90.51057 );
		shape268.lineTo( -120.56807, -93.48658 );
		shape268.lineTo( -119.202576, -102.44101 );
		shape268.lineTo( -118.81444, -103.36701 );
		shape268.lineTo( -118.81447, -103.36701 );
		shape268.lineTo( -118.81447, -103.36701 );
		shape268.closePath();
		g.fill( shape268 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_50( Graphics2D g ) {
		GeneralPath shape269 = new GeneralPath();
		shape269.moveTo( -122.31061, -121.76999 );
		shape269.lineTo( -121.01069, -127.46068 );
		shape269.lineTo( -122.54229, -140.89699 );
		shape269.lineTo( -128.42947, -137.10597 );
		shape269.lineTo( -131.79373, -139.33395 );
		shape269.lineTo( -131.89983, -137.42354 );
		shape269.lineTo( -129.17862, -135.83652 );
		shape269.lineTo( -122.51971, -140.64006 );
		shape269.lineTo( -122.31058, -121.76999 );
		shape269.lineTo( -122.310585, -121.76999 );
		shape269.lineTo( -122.310585, -121.76999 );
		shape269.closePath();
		g.fill( shape269 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_51( Graphics2D g ) {
		GeneralPath shape270 = new GeneralPath();
		shape270.moveTo( -100.97362, -123.74181 );
		shape270.lineTo( -105.533775, -126.224045 );
		shape270.lineTo( -106.41846, -122.04188 );
		shape270.lineTo( -100.97362, -123.74182 );
		shape270.closePath();
		g.fill( shape270 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_52( Graphics2D g ) {
		GeneralPath shape271 = new GeneralPath();
		shape271.moveTo( -105.03012, -125.40315 );
		shape271.curveTo( -105.03012, -125.40315, -109.60198, -116.87824, -109.3357, -117.409935 );
		shape271.curveTo( -109.06942, -117.94163, -106.05223, -126.79704, -106.05223, -126.79704 );
		shape271.lineTo( -109.03045, -133.98555 );
		shape271.lineTo( -105.03012, -125.40316 );
		shape271.lineTo( -105.03012, -125.40316 );
		shape271.lineTo( -105.03012, -125.40316 );
		shape271.closePath();
		g.fill( shape271 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_53( Graphics2D g ) {
		GeneralPath shape272 = new GeneralPath();
		shape272.moveTo( -103.74948, -119.92615 );
		shape272.lineTo( -108.83049, -132.35213 );
		shape272.lineTo( -116.80052, -124.21347 );
		shape272.lineTo( -107.83126, -122.30667 );
		shape272.lineTo( -108.35263, -118.88771 );
		shape272.lineTo( -111.59292, -117.4755 );
		shape272.lineTo( -109.87582, -116.18598 );
		shape272.lineTo( -106.72121, -119.465355 );
		shape272.lineTo( -103.74947, -119.926155 );
		shape272.lineTo( -103.74947, -119.926155 );
		shape272.lineTo( -103.74947, -119.926155 );
		shape272.closePath();
		g.fill( shape272 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_54( Graphics2D g ) {
		GeneralPath shape273 = new GeneralPath();
		shape273.moveTo( -113.01835, -104.19628 );
		shape273.curveTo( -112.60993, -104.44564, -109.66285, -109.19785, -109.66285, -109.19785 );
		shape273.lineTo( -112.61431, -108.06041 );
		shape273.lineTo( -112.71341, -111.86341 );
		shape273.lineTo( -110.90349, -118.87441 );
		shape273.lineTo( -116.635216, -108.41097 );
		shape273.lineTo( -113.01832, -104.19628 );
		shape273.lineTo( -113.01835, -104.19628 );
		shape273.lineTo( -113.01835, -104.19628 );
		shape273.closePath();
		g.fill( shape273 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_55( Graphics2D g ) {
		GeneralPath shape274 = new GeneralPath();
		shape274.moveTo( -122.34034, -128.05212 );
		shape274.curveTo( -121.9342, -127.88174, -119.03921, -127.434364, -119.03921, -127.434364 );
		shape274.lineTo( -116.72433, -121.56349 );
		shape274.lineTo( -118.97853, -120.950165 );
		shape274.lineTo( -122.34034, -128.05212 );
		shape274.closePath();
		g.fill( shape274 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_56( Graphics2D g ) {
		GeneralPath shape275 = new GeneralPath();
		shape275.moveTo( -131.22617, -83.955605 );
		shape275.lineTo( -130.8828, -94.303925 );
		shape275.lineTo( -132.97247, -88.69708 );
		shape275.lineTo( -131.22617, -83.955605 );
		shape275.lineTo( -131.22617, -83.955605 );
		shape275.lineTo( -131.22617, -83.955605 );
		shape275.closePath();
		g.fill( shape275 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_57( Graphics2D g ) {
		GeneralPath shape276 = new GeneralPath();
		shape276.moveTo( -134.79109, -118.34518 );
		shape276.lineTo( -132.5299, -124.671936 );
		shape276.lineTo( -136.09521, -120.723495 );
		shape276.lineTo( -134.79109, -118.34518 );
		shape276.closePath();
		g.fill( shape276 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_58( Graphics2D g ) {
		GeneralPath shape277 = new GeneralPath();
		shape277.moveTo( -144.89034, -115.10358 );
		shape277.lineTo( -144.04604, -125.093285 );
		shape277.lineTo( -140.23473, -130.25008 );
		shape277.lineTo( -143.18391, -129.53236 );
		shape277.lineTo( -145.00049, -125.7164 );
		shape277.lineTo( -144.89034, -115.10357 );
		shape277.lineTo( -144.89035, -115.10357 );
		shape277.lineTo( -144.89035, -115.10357 );
		shape277.closePath();
		g.fill( shape277 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_59( Graphics2D g ) {
		GeneralPath shape278 = new GeneralPath();
		shape278.moveTo( -103.70774, -100.41719 );
		shape278.lineTo( -108.21842, -98.77079 );
		shape278.lineTo( -105.08409, -102.72682 );
		shape278.lineTo( -103.70774, -100.41719 );
		shape278.lineTo( -103.70774, -100.41719 );
		shape278.lineTo( -103.70774, -100.41719 );
		shape278.closePath();
		g.fill( shape278 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_60( Graphics2D g ) {
		GeneralPath shape279 = new GeneralPath();
		shape279.moveTo( -130.08437, -109.41773 );
		shape279.lineTo( -133.45999, -109.54707 );
		shape279.lineTo( -133.6925, -106.40287 );
		shape279.lineTo( -136.50847, -108.17812 );
		shape279.lineTo( -136.13152, -111.4597 );
		shape279.lineTo( -130.08435, -109.41773 );
		shape279.lineTo( -130.08437, -109.41773 );
		shape279.closePath();
		g.setPaint( new Color( 145, 159, 186, 255 ) );
		g.fill( shape279 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_61( Graphics2D g ) {
		GeneralPath shape280 = new GeneralPath();
		shape280.moveTo( -111.05447, -106.78952 );
		shape280.curveTo( -111.51027, -106.63429, -115.560585, -105.98259, -115.560585, -105.98259 );
		shape280.lineTo( -114.44128, -109.27443 );
		shape280.lineTo( -111.05447, -106.78952 );
		shape280.closePath();
		g.fill( shape280 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_62( Graphics2D g ) {
		GeneralPath shape281 = new GeneralPath();
		shape281.moveTo( -110.96047, -107.81577 );
		shape281.lineTo( -114.376656, -109.298416 );
		shape281.lineTo( -114.94279, -115.30177 );
		shape281.lineTo( -114.49136, -119.071785 );
		shape281.lineTo( -112.384026, -114.673775 );
		shape281.lineTo( -113.89586, -114.070694 );
		shape281.lineTo( -113.10173, -110.37207 );
		shape281.lineTo( -110.96048, -107.81577 );
		shape281.lineTo( -110.96048, -107.81577 );
		shape281.lineTo( -110.96048, -107.81577 );
		shape281.closePath();
		g.fill( shape281 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_63( Graphics2D g ) {
		GeneralPath shape282 = new GeneralPath();
		shape282.moveTo( -114.59449, -120.31398 );
		shape282.lineTo( -121.171936, -121.7123 );
		shape282.lineTo( -119.56097, -118.51235 );
		shape282.lineTo( -115.79948, -118.88929 );
		shape282.lineTo( -115.431786, -116.03771 );
		shape282.lineTo( -114.59448, -120.31398 );
		shape282.lineTo( -114.59448, -120.31398 );
		shape282.lineTo( -114.59448, -120.31398 );
		shape282.closePath();
		g.fill( shape282 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_64( Graphics2D g ) {
		GeneralPath shape283 = new GeneralPath();
		shape283.moveTo( -125.6262, -118.76841 );
		shape283.curveTo( -125.6262, -118.76841, -133.91393, -122.71543, -133.62964, -122.15074 );
		shape283.curveTo( -133.34535, -121.586044, -131.90135, -118.50564, -131.90135, -118.50564 );
		shape283.lineTo( -125.626205, -118.76841 );
		shape283.lineTo( -125.626205, -118.76841 );
		shape283.lineTo( -125.626205, -118.76841 );
		shape283.closePath();
		g.fill( shape283 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_65( Graphics2D g ) {
		GeneralPath shape284 = new GeneralPath();
		shape284.moveTo( -143.77301, -110.82659 );
		shape284.curveTo( -143.77301, -110.82659, -137.28368, -106.42146, -136.94746, -106.60211 );
		shape284.curveTo( -136.61124, -106.78276, -133.06183, -107.79309, -133.06183, -107.79309 );
		shape284.lineTo( -136.83469, -105.31749 );
		shape284.lineTo( -142.02217, -106.92457 );
		shape284.lineTo( -143.77301, -110.82659 );
		shape284.lineTo( -143.77301, -110.82659 );
		shape284.lineTo( -143.77301, -110.82659 );
		shape284.closePath();
		g.fill( shape284 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_66( Graphics2D g ) {
		GeneralPath shape285 = new GeneralPath();
		shape285.moveTo( -150.80025, -103.64741 );
		shape285.lineTo( -148.68835, -100.08884 );
		shape285.lineTo( -144.12138, -98.86581 );
		shape285.lineTo( -140.88774, -103.47307 );
		shape285.lineTo( -145.1615, -101.356094 );
		shape285.lineTo( -150.80023, -103.64741 );
		shape285.lineTo( -150.80023, -103.64741 );
		shape285.lineTo( -150.80023, -103.64741 );
		shape285.closePath();
		g.fill( shape285 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_67( Graphics2D g ) {
		GeneralPath shape286 = new GeneralPath();
		shape286.moveTo( -100.04461, -96.73875 );
		shape286.lineTo( -96.95555, -96.75439 );
		shape286.lineTo( -94.52109, -90.85809 );
		shape286.lineTo( -95.590416, -96.80034 );
		shape286.lineTo( -93.3315, -102.70737 );
		shape286.lineTo( -96.52003, -97.586266 );
		shape286.lineTo( -101.3713, -99.374 );
		shape286.lineTo( -100.04461, -96.738754 );
		shape286.lineTo( -100.04461, -96.73875 );
		shape286.lineTo( -100.04461, -96.73875 );
		shape286.closePath();
		g.fill( shape286 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_68( Graphics2D g ) {
		GeneralPath shape287 = new GeneralPath();
		shape287.moveTo( -125.1758, -106.42465 );
		shape287.lineTo( -127.61236, -116.35545 );
		shape287.lineTo( -122.67492, -126.06329 );
		shape287.lineTo( -125.26516, -129.72353 );
		shape287.lineTo( -133.36371, -124.38561 );
		shape287.lineTo( -125.856636, -122.20136 );
		shape287.lineTo( -128.67293, -115.068146 );
		shape287.lineTo( -128.00977, -109.29629 );
		shape287.lineTo( -125.1758, -106.424644 );
		shape287.closePath();
		g.setPaint( new Color( 255, 255, 255, 255 ) );
		g.fill( shape287 );
	}

	private void paintShapeNode_0_0_0_0_0_7_3_69( Graphics2D g ) {
		GeneralPath shape288 = new GeneralPath();
		shape288.moveTo( -125.73364, -92.68954 );
		shape288.lineTo( -128.50449, -93.950935 );
		shape288.lineTo( -131.36993, -99.85481 );
		shape288.lineTo( -127.54305, -99.041245 );
		shape288.lineTo( -125.42452, -92.28764 );
		shape288.lineTo( -125.733635, -92.68953 );
		shape288.closePath();
		g.setPaint( new Color( 223, 227, 235, 255 ) );
		g.fill( shape288 );
	}

	private void paintCompositeGraphicsNode_0_0_0_0_0_7_3( Graphics2D g ) {
		// _0_0_0_0_0_7_3_0
		AffineTransform trans_0_0_0_0_0_7_3_0 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_0( g );
		g.setTransform( trans_0_0_0_0_0_7_3_0 );
		// _0_0_0_0_0_7_3_1
		AffineTransform trans_0_0_0_0_0_7_3_1 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_1( g );
		g.setTransform( trans_0_0_0_0_0_7_3_1 );
		// _0_0_0_0_0_7_3_2
		AffineTransform trans_0_0_0_0_0_7_3_2 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_2( g );
		g.setTransform( trans_0_0_0_0_0_7_3_2 );
		// _0_0_0_0_0_7_3_3
		AffineTransform trans_0_0_0_0_0_7_3_3 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_3( g );
		g.setTransform( trans_0_0_0_0_0_7_3_3 );
		// _0_0_0_0_0_7_3_4
		AffineTransform trans_0_0_0_0_0_7_3_4 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_4( g );
		g.setTransform( trans_0_0_0_0_0_7_3_4 );
		// _0_0_0_0_0_7_3_5
		AffineTransform trans_0_0_0_0_0_7_3_5 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_5( g );
		g.setTransform( trans_0_0_0_0_0_7_3_5 );
		// _0_0_0_0_0_7_3_6
		AffineTransform trans_0_0_0_0_0_7_3_6 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_6( g );
		g.setTransform( trans_0_0_0_0_0_7_3_6 );
		// _0_0_0_0_0_7_3_7
		AffineTransform trans_0_0_0_0_0_7_3_7 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_7( g );
		g.setTransform( trans_0_0_0_0_0_7_3_7 );
		// _0_0_0_0_0_7_3_8
		AffineTransform trans_0_0_0_0_0_7_3_8 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_8( g );
		g.setTransform( trans_0_0_0_0_0_7_3_8 );
		// _0_0_0_0_0_7_3_9
		AffineTransform trans_0_0_0_0_0_7_3_9 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_9( g );
		g.setTransform( trans_0_0_0_0_0_7_3_9 );
		// _0_0_0_0_0_7_3_10
		AffineTransform trans_0_0_0_0_0_7_3_10 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_10( g );
		g.setTransform( trans_0_0_0_0_0_7_3_10 );
		// _0_0_0_0_0_7_3_11
		g.setComposite( AlphaComposite.getInstance( 3, 0.6456311f * origAlpha ) );
		AffineTransform trans_0_0_0_0_0_7_3_11 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_11( g );
		g.setTransform( trans_0_0_0_0_0_7_3_11 );
		// _0_0_0_0_0_7_3_12
		AffineTransform trans_0_0_0_0_0_7_3_12 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_12( g );
		g.setTransform( trans_0_0_0_0_0_7_3_12 );
		// _0_0_0_0_0_7_3_13
		AffineTransform trans_0_0_0_0_0_7_3_13 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_13( g );
		g.setTransform( trans_0_0_0_0_0_7_3_13 );
		// _0_0_0_0_0_7_3_14
		AffineTransform trans_0_0_0_0_0_7_3_14 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_14( g );
		g.setTransform( trans_0_0_0_0_0_7_3_14 );
		// _0_0_0_0_0_7_3_15
		AffineTransform trans_0_0_0_0_0_7_3_15 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_15( g );
		g.setTransform( trans_0_0_0_0_0_7_3_15 );
		// _0_0_0_0_0_7_3_16
		AffineTransform trans_0_0_0_0_0_7_3_16 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_16( g );
		g.setTransform( trans_0_0_0_0_0_7_3_16 );
		// _0_0_0_0_0_7_3_17
		AffineTransform trans_0_0_0_0_0_7_3_17 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_17( g );
		g.setTransform( trans_0_0_0_0_0_7_3_17 );
		// _0_0_0_0_0_7_3_18
		AffineTransform trans_0_0_0_0_0_7_3_18 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_18( g );
		g.setTransform( trans_0_0_0_0_0_7_3_18 );
		// _0_0_0_0_0_7_3_19
		AffineTransform trans_0_0_0_0_0_7_3_19 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_19( g );
		g.setTransform( trans_0_0_0_0_0_7_3_19 );
		// _0_0_0_0_0_7_3_20
		AffineTransform trans_0_0_0_0_0_7_3_20 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_20( g );
		g.setTransform( trans_0_0_0_0_0_7_3_20 );
		// _0_0_0_0_0_7_3_21
		AffineTransform trans_0_0_0_0_0_7_3_21 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_21( g );
		g.setTransform( trans_0_0_0_0_0_7_3_21 );
		// _0_0_0_0_0_7_3_22
		AffineTransform trans_0_0_0_0_0_7_3_22 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_22( g );
		g.setTransform( trans_0_0_0_0_0_7_3_22 );
		// _0_0_0_0_0_7_3_23
		AffineTransform trans_0_0_0_0_0_7_3_23 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_23( g );
		g.setTransform( trans_0_0_0_0_0_7_3_23 );
		// _0_0_0_0_0_7_3_24
		AffineTransform trans_0_0_0_0_0_7_3_24 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_24( g );
		g.setTransform( trans_0_0_0_0_0_7_3_24 );
		// _0_0_0_0_0_7_3_25
		AffineTransform trans_0_0_0_0_0_7_3_25 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_25( g );
		g.setTransform( trans_0_0_0_0_0_7_3_25 );
		// _0_0_0_0_0_7_3_26
		AffineTransform trans_0_0_0_0_0_7_3_26 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_26( g );
		g.setTransform( trans_0_0_0_0_0_7_3_26 );
		// _0_0_0_0_0_7_3_27
		AffineTransform trans_0_0_0_0_0_7_3_27 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_27( g );
		g.setTransform( trans_0_0_0_0_0_7_3_27 );
		// _0_0_0_0_0_7_3_28
		AffineTransform trans_0_0_0_0_0_7_3_28 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_28( g );
		g.setTransform( trans_0_0_0_0_0_7_3_28 );
		// _0_0_0_0_0_7_3_29
		AffineTransform trans_0_0_0_0_0_7_3_29 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_29( g );
		g.setTransform( trans_0_0_0_0_0_7_3_29 );
		// _0_0_0_0_0_7_3_30
		AffineTransform trans_0_0_0_0_0_7_3_30 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_30( g );
		g.setTransform( trans_0_0_0_0_0_7_3_30 );
		// _0_0_0_0_0_7_3_31
		AffineTransform trans_0_0_0_0_0_7_3_31 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_31( g );
		g.setTransform( trans_0_0_0_0_0_7_3_31 );
		// _0_0_0_0_0_7_3_32
		AffineTransform trans_0_0_0_0_0_7_3_32 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_32( g );
		g.setTransform( trans_0_0_0_0_0_7_3_32 );
		// _0_0_0_0_0_7_3_33
		AffineTransform trans_0_0_0_0_0_7_3_33 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_33( g );
		g.setTransform( trans_0_0_0_0_0_7_3_33 );
		// _0_0_0_0_0_7_3_34
		AffineTransform trans_0_0_0_0_0_7_3_34 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_34( g );
		g.setTransform( trans_0_0_0_0_0_7_3_34 );
		// _0_0_0_0_0_7_3_35
		g.setComposite( AlphaComposite.getInstance( 3, 1.0f * origAlpha ) );
		AffineTransform trans_0_0_0_0_0_7_3_35 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_35( g );
		g.setTransform( trans_0_0_0_0_0_7_3_35 );
		// _0_0_0_0_0_7_3_36
		AffineTransform trans_0_0_0_0_0_7_3_36 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_36( g );
		g.setTransform( trans_0_0_0_0_0_7_3_36 );
		// _0_0_0_0_0_7_3_37
		AffineTransform trans_0_0_0_0_0_7_3_37 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_37( g );
		g.setTransform( trans_0_0_0_0_0_7_3_37 );
		// _0_0_0_0_0_7_3_38
		AffineTransform trans_0_0_0_0_0_7_3_38 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_38( g );
		g.setTransform( trans_0_0_0_0_0_7_3_38 );
		// _0_0_0_0_0_7_3_39
		AffineTransform trans_0_0_0_0_0_7_3_39 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_39( g );
		g.setTransform( trans_0_0_0_0_0_7_3_39 );
		// _0_0_0_0_0_7_3_40
		AffineTransform trans_0_0_0_0_0_7_3_40 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_40( g );
		g.setTransform( trans_0_0_0_0_0_7_3_40 );
		// _0_0_0_0_0_7_3_41
		AffineTransform trans_0_0_0_0_0_7_3_41 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_41( g );
		g.setTransform( trans_0_0_0_0_0_7_3_41 );
		// _0_0_0_0_0_7_3_42
		AffineTransform trans_0_0_0_0_0_7_3_42 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_42( g );
		g.setTransform( trans_0_0_0_0_0_7_3_42 );
		// _0_0_0_0_0_7_3_43
		AffineTransform trans_0_0_0_0_0_7_3_43 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_43( g );
		g.setTransform( trans_0_0_0_0_0_7_3_43 );
		// _0_0_0_0_0_7_3_44
		AffineTransform trans_0_0_0_0_0_7_3_44 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_44( g );
		g.setTransform( trans_0_0_0_0_0_7_3_44 );
		// _0_0_0_0_0_7_3_45
		AffineTransform trans_0_0_0_0_0_7_3_45 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_45( g );
		g.setTransform( trans_0_0_0_0_0_7_3_45 );
		// _0_0_0_0_0_7_3_46
		g.setComposite( AlphaComposite.getInstance( 3, 0.6456311f * origAlpha ) );
		AffineTransform trans_0_0_0_0_0_7_3_46 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_46( g );
		g.setTransform( trans_0_0_0_0_0_7_3_46 );
		// _0_0_0_0_0_7_3_47
		AffineTransform trans_0_0_0_0_0_7_3_47 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_47( g );
		g.setTransform( trans_0_0_0_0_0_7_3_47 );
		// _0_0_0_0_0_7_3_48
		AffineTransform trans_0_0_0_0_0_7_3_48 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_48( g );
		g.setTransform( trans_0_0_0_0_0_7_3_48 );
		// _0_0_0_0_0_7_3_49
		AffineTransform trans_0_0_0_0_0_7_3_49 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_49( g );
		g.setTransform( trans_0_0_0_0_0_7_3_49 );
		// _0_0_0_0_0_7_3_50
		AffineTransform trans_0_0_0_0_0_7_3_50 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_50( g );
		g.setTransform( trans_0_0_0_0_0_7_3_50 );
		// _0_0_0_0_0_7_3_51
		AffineTransform trans_0_0_0_0_0_7_3_51 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_51( g );
		g.setTransform( trans_0_0_0_0_0_7_3_51 );
		// _0_0_0_0_0_7_3_52
		AffineTransform trans_0_0_0_0_0_7_3_52 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_52( g );
		g.setTransform( trans_0_0_0_0_0_7_3_52 );
		// _0_0_0_0_0_7_3_53
		AffineTransform trans_0_0_0_0_0_7_3_53 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_53( g );
		g.setTransform( trans_0_0_0_0_0_7_3_53 );
		// _0_0_0_0_0_7_3_54
		AffineTransform trans_0_0_0_0_0_7_3_54 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_54( g );
		g.setTransform( trans_0_0_0_0_0_7_3_54 );
		// _0_0_0_0_0_7_3_55
		AffineTransform trans_0_0_0_0_0_7_3_55 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_55( g );
		g.setTransform( trans_0_0_0_0_0_7_3_55 );
		// _0_0_0_0_0_7_3_56
		AffineTransform trans_0_0_0_0_0_7_3_56 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_56( g );
		g.setTransform( trans_0_0_0_0_0_7_3_56 );
		// _0_0_0_0_0_7_3_57
		AffineTransform trans_0_0_0_0_0_7_3_57 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_57( g );
		g.setTransform( trans_0_0_0_0_0_7_3_57 );
		// _0_0_0_0_0_7_3_58
		AffineTransform trans_0_0_0_0_0_7_3_58 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_58( g );
		g.setTransform( trans_0_0_0_0_0_7_3_58 );
		// _0_0_0_0_0_7_3_59
		AffineTransform trans_0_0_0_0_0_7_3_59 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_59( g );
		g.setTransform( trans_0_0_0_0_0_7_3_59 );
		// _0_0_0_0_0_7_3_60
		AffineTransform trans_0_0_0_0_0_7_3_60 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_60( g );
		g.setTransform( trans_0_0_0_0_0_7_3_60 );
		// _0_0_0_0_0_7_3_61
		AffineTransform trans_0_0_0_0_0_7_3_61 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_61( g );
		g.setTransform( trans_0_0_0_0_0_7_3_61 );
		// _0_0_0_0_0_7_3_62
		AffineTransform trans_0_0_0_0_0_7_3_62 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_62( g );
		g.setTransform( trans_0_0_0_0_0_7_3_62 );
		// _0_0_0_0_0_7_3_63
		AffineTransform trans_0_0_0_0_0_7_3_63 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_63( g );
		g.setTransform( trans_0_0_0_0_0_7_3_63 );
		// _0_0_0_0_0_7_3_64
		AffineTransform trans_0_0_0_0_0_7_3_64 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_64( g );
		g.setTransform( trans_0_0_0_0_0_7_3_64 );
		// _0_0_0_0_0_7_3_65
		AffineTransform trans_0_0_0_0_0_7_3_65 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_65( g );
		g.setTransform( trans_0_0_0_0_0_7_3_65 );
		// _0_0_0_0_0_7_3_66
		AffineTransform trans_0_0_0_0_0_7_3_66 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_66( g );
		g.setTransform( trans_0_0_0_0_0_7_3_66 );
		// _0_0_0_0_0_7_3_67
		AffineTransform trans_0_0_0_0_0_7_3_67 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_67( g );
		g.setTransform( trans_0_0_0_0_0_7_3_67 );
		// _0_0_0_0_0_7_3_68
		AffineTransform trans_0_0_0_0_0_7_3_68 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_68( g );
		g.setTransform( trans_0_0_0_0_0_7_3_68 );
		// _0_0_0_0_0_7_3_69
		AffineTransform trans_0_0_0_0_0_7_3_69 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_7_3_69( g );
		g.setTransform( trans_0_0_0_0_0_7_3_69 );
	}

	private void paintCompositeGraphicsNode_0_0_0_0_0_7( Graphics2D g ) {
		// _0_0_0_0_0_7_0
		AffineTransform trans_0_0_0_0_0_7_0 = g.getTransform();
		g.transform( new AffineTransform( 1.0324491262435913f, 0.0f, 0.0f, -1.0324491262435913f, 291.3728332519531f, 99.96508026123047f ) );
		paintCompositeGraphicsNode_0_0_0_0_0_7_0( g );
		g.setTransform( trans_0_0_0_0_0_7_0 );
		// _0_0_0_0_0_7_1
		g.setComposite( AlphaComposite.getInstance( 3, 1.0f * origAlpha ) );
		AffineTransform trans_0_0_0_0_0_7_1 = g.getTransform();
		g.transform( new AffineTransform( 0.4066241979598999f, -1.4080145359039307f, 1.4080145359039307f, 0.4066241979598999f, 395.6860656738281f, 53.76996994018555f ) );
		paintCompositeGraphicsNode_0_0_0_0_0_7_1( g );
		g.setTransform( trans_0_0_0_0_0_7_1 );
		// _0_0_0_0_0_7_2
		g.setComposite( AlphaComposite.getInstance( 3, 1.0f * origAlpha ) );
		AffineTransform trans_0_0_0_0_0_7_2 = g.getTransform();
		g.transform( new AffineTransform( 0.094065822660923f, 1.4625319242477417f, 1.4625319242477417f, -0.094065822660923f, 305.6005554199219f, 361.9144287109375f ) );
		paintCompositeGraphicsNode_0_0_0_0_0_7_2( g );
		g.setTransform( trans_0_0_0_0_0_7_2 );
		// _0_0_0_0_0_7_3
		g.setComposite( AlphaComposite.getInstance( 3, 1.0f * origAlpha ) );
		AffineTransform trans_0_0_0_0_0_7_3 = g.getTransform();
		g.transform( new AffineTransform( 1.4655537605285645f, 0.0f, 0.0f, 1.4655537605285645f, 312.78900146484375f, 317.36151123046875f ) );
		paintCompositeGraphicsNode_0_0_0_0_0_7_3( g );
		g.setTransform( trans_0_0_0_0_0_7_3 );
	}

	private void paintShapeNode_0_0_0_0_0_8_0( Graphics2D g ) {
		GeneralPath shape289 = new GeneralPath();
		shape289.moveTo( 320.3003, 86.30013 );
		shape289.lineTo( 316.88177, 88.77781 );
		shape289.lineTo( 317.0972, 85.03448 );
		shape289.curveTo( 317.0683, 84.87234, 317.35257, 84.79798, 317.48016, 84.6797 );
		shape289.lineTo( 320.3003, 86.30013 );
		shape289.lineTo( 320.3003, 86.30013 );
		shape289.closePath();
		g.setPaint( new Color( 0, 0, 0, 255 ) );
		g.fill( shape289 );
	}

	private void paintShapeNode_0_0_0_0_0_8_1( Graphics2D g ) {
		GeneralPath shape290 = new GeneralPath();
		shape290.moveTo( 319.9149, 73.56953 );
		shape290.lineTo( 317.9031, 70.35443 );
		shape290.lineTo( 321.20697, 70.36538 );
		shape290.curveTo( 321.3521, 70.32845, 321.4028, 70.602806, 321.50073, 70.72141 );
		shape290.lineTo( 319.9149, 73.56953 );
		shape290.lineTo( 319.9149, 73.56953 );
		shape290.lineTo( 319.9149, 73.56953 );
		shape290.closePath();
		g.fill( shape290 );
	}

	private void paintShapeNode_0_0_0_0_0_8_2( Graphics2D g ) {
		GeneralPath shape291 = new GeneralPath();
		shape291.moveTo( 315.35025, 82.599625 );
		shape291.lineTo( 312.2365, 78.88464 );
		shape291.lineTo( 316.59106, 79.37223 );
		shape291.curveTo( 316.77695, 79.35379, 316.88327, 79.65337, 317.0294, 79.79384 );
		shape291.lineTo( 315.35022, 82.599625 );
		shape291.lineTo( 315.35022, 82.599625 );
		shape291.lineTo( 315.35022, 82.599625 );
		shape291.closePath();
		g.fill( shape291 );
	}

	private void paintShapeNode_0_0_0_0_0_8_3( Graphics2D g ) {
		GeneralPath shape292 = new GeneralPath();
		shape292.moveTo( 325.51697, 82.59113 );
		shape292.lineTo( 319.92688, 79.35251 );
		shape292.lineTo( 324.199, 78.62271 );
		shape292.curveTo( 324.35184, 78.5477, 324.67276, 78.85125, 324.9096, 78.96539 );
		shape292.lineTo( 325.51697, 82.59113 );
		shape292.lineTo( 325.51697, 82.59113 );
		shape292.lineTo( 325.51697, 82.59113 );
		shape292.closePath();
		g.fill( shape292 );
	}

	private void paintShapeNode_0_0_0_0_0_8_4( Graphics2D g ) {
		GeneralPath shape293 = new GeneralPath();
		shape293.moveTo( 318.09186, 76.50481 );
		shape293.lineTo( 319.55493, 73.16653 );
		shape293.lineTo( 322.63986, 76.236755 );
		shape293.curveTo( 322.81433, 76.35449, 322.57605, 76.527115, 322.54437, 76.67228 );
		shape293.lineTo( 318.0919, 76.50481 );
		shape293.lineTo( 318.0919, 76.50481 );
		shape293.lineTo( 318.0919, 76.50481 );
		shape293.closePath();
		g.fill( shape293 );
	}

	private void paintShapeNode_0_0_0_0_0_8_5( Graphics2D g ) {
		GeneralPath shape294 = new GeneralPath();
		shape294.moveTo( 314.85995, 87.62546 );
		shape294.lineTo( 311.19135, 85.535835 );
		shape294.lineTo( 314.68933, 84.18561 );
		shape294.curveTo( 314.82504, 84.092316, 315.0102, 84.32048, 315.17062, 84.38779 );
		shape294.lineTo( 314.85995, 87.62546 );
		shape294.lineTo( 314.85995, 87.62546 );
		shape294.closePath();
		g.fill( shape294 );
	}

	private void paintShapeNode_0_0_0_0_0_8_6( Graphics2D g ) {
		GeneralPath shape295 = new GeneralPath();
		shape295.moveTo( 324.75793, 75.300186 );
		shape295.lineTo( 326.85477, 72.13991 );
		shape295.lineTo( 328.20966, 75.15319 );
		shape295.curveTo( 328.30325, 75.270096, 328.07434, 75.429596, 328.00677, 75.567795 );
		shape295.lineTo( 324.7579, 75.300186 );
		shape295.lineTo( 324.7579, 75.300186 );
		shape295.lineTo( 324.7579, 75.300186 );
		shape295.closePath();
		g.fill( shape295 );
	}

	private void paintShapeNode_0_0_0_0_0_8_7( Graphics2D g ) {
		GeneralPath shape296 = new GeneralPath();
		shape296.moveTo( 312.2593, 74.44772 );
		shape296.lineTo( 314.35614, 70.077415 );
		shape296.lineTo( 315.71103, 74.24445 );
		shape296.curveTo( 315.80463, 74.40613, 315.5757, 74.62669, 315.50815, 74.8178 );
		shape296.lineTo( 312.2593, 74.44773 );
		shape296.lineTo( 312.2593, 74.44773 );
		shape296.lineTo( 312.2593, 74.44773 );
		shape296.closePath();
		g.fill( shape296 );
	}

	private void paintShapeNode_0_0_0_0_0_8_8( Graphics2D g ) {
		GeneralPath shape297 = new GeneralPath();
		shape297.moveTo( 320.30942, 91.56802 );
		shape297.lineTo( 320.94943, 85.139305 );
		shape297.lineTo( 323.37894, 88.72837 );
		shape297.curveTo( 323.51038, 88.83655, 323.36655, 89.254234, 323.36044, 89.51707 );
		shape297.lineTo( 320.30945, 91.568016 );
		shape297.lineTo( 320.30945, 91.568016 );
		shape297.lineTo( 320.30945, 91.568016 );
		shape297.closePath();
		g.fill( shape297 );
	}

	private void paintShapeNode_0_0_0_0_0_8_9( Graphics2D g ) {
		GeneralPath shape298 = new GeneralPath();
		shape298.moveTo( 326.26083, 82.08754 );
		shape298.lineTo( 329.90536, 82.04084 );
		shape298.lineTo( 328.3838, 86.11856 );
		shape298.curveTo( 328.3486, 86.32609, 328.09302, 86.18044, 327.94772, 86.21152 );
		shape298.lineTo( 326.26086, 82.08754 );
		shape298.lineTo( 326.26086, 82.08754 );
		shape298.lineTo( 326.26086, 82.08754 );
		shape298.closePath();
		g.fill( shape298 );
	}

	private void paintCompositeGraphicsNode_0_0_0_0_0_8( Graphics2D g ) {
		// _0_0_0_0_0_8_0
		g.setComposite( AlphaComposite.getInstance( 3, 1.0f * origAlpha ) );
		AffineTransform trans_0_0_0_0_0_8_0 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_8_0( g );
		g.setTransform( trans_0_0_0_0_0_8_0 );
		// _0_0_0_0_0_8_1
		AffineTransform trans_0_0_0_0_0_8_1 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_8_1( g );
		g.setTransform( trans_0_0_0_0_0_8_1 );
		// _0_0_0_0_0_8_2
		AffineTransform trans_0_0_0_0_0_8_2 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_8_2( g );
		g.setTransform( trans_0_0_0_0_0_8_2 );
		// _0_0_0_0_0_8_3
		AffineTransform trans_0_0_0_0_0_8_3 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_8_3( g );
		g.setTransform( trans_0_0_0_0_0_8_3 );
		// _0_0_0_0_0_8_4
		AffineTransform trans_0_0_0_0_0_8_4 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_8_4( g );
		g.setTransform( trans_0_0_0_0_0_8_4 );
		// _0_0_0_0_0_8_5
		AffineTransform trans_0_0_0_0_0_8_5 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_8_5( g );
		g.setTransform( trans_0_0_0_0_0_8_5 );
		// _0_0_0_0_0_8_6
		AffineTransform trans_0_0_0_0_0_8_6 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_8_6( g );
		g.setTransform( trans_0_0_0_0_0_8_6 );
		// _0_0_0_0_0_8_7
		AffineTransform trans_0_0_0_0_0_8_7 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_8_7( g );
		g.setTransform( trans_0_0_0_0_0_8_7 );
		// _0_0_0_0_0_8_8
		AffineTransform trans_0_0_0_0_0_8_8 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_8_8( g );
		g.setTransform( trans_0_0_0_0_0_8_8 );
		// _0_0_0_0_0_8_9
		AffineTransform trans_0_0_0_0_0_8_9 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_8_9( g );
		g.setTransform( trans_0_0_0_0_0_8_9 );
	}

	private void paintShapeNode_0_0_0_0_0_9_0( Graphics2D g ) {
		GeneralPath shape299 = new GeneralPath();
		shape299.moveTo( 762.5, 59.1875 );
		shape299.lineTo( 762.5, 61.5 );
		shape299.lineTo( 765.0, 85.5 );
		shape299.curveTo( 765.0, 88.81647, 771.72, 91.5, 780.0, 91.5 );
		shape299.curveTo( 788.28, 91.5, 795.0, 88.81647, 795.0, 85.5 );
		shape299.lineTo( 797.5, 61.5 );
		shape299.lineTo( 797.5, 59.1875 );
		shape299.curveTo( 797.1822, 62.150127, 789.4729, 64.53125, 780.0, 64.53125 );
		shape299.curveTo( 770.5271, 64.53125, 762.81775, 62.150127, 762.5, 59.1875 );
		shape299.closePath();
		g.setPaint( new RadialGradientPaint( new Point2D.Double( 767.9999389648438, 78.99209594726562 ), 17.5f, new Point2D.Double( 767.9999389648438, 78.99209594726562 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 255, 255, 255, 255 ), new Color( 255, 255, 255, 76 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 0.9001134037971497f, 0.0f, 0.0f, 3.4375f, 88.71292877197266f, -185.869140625f ) ) );
		g.fill( shape299 );
	}

	private void paintShapeNode_0_0_0_0_0_9_1( Graphics2D g ) {
		GeneralPath shape300 = new GeneralPath();
		shape300.moveTo( 750.5, 59.1875 );
		shape300.lineTo( 750.5, 62.5 );
		shape300.lineTo( 753.0, 86.5 );
		shape300.curveTo( 753.0, 89.81647, 759.72, 92.5, 768.0, 92.5 );
		shape300.curveTo( 776.28, 92.5, 783.0, 89.81647, 783.0, 86.5 );
		shape300.lineTo( 785.5, 62.5 );
		shape300.lineTo( 785.5, 59.1875 );
		shape300.curveTo( 785.1822, 62.150127, 777.4729, 64.53125, 768.0, 64.53125 );
		shape300.curveTo( 758.5271, 64.53125, 750.81775, 62.150127, 750.5, 59.1875 );
		shape300.closePath();
		g.setPaint( new LinearGradientPaint( new Point2D.Double( 734.1218872070312, 72.8232192993164 ), new Point2D.Double( 700.4962768554688, 72.8232192993164 ), new float[] { 0.0f, 0.25f, 0.5f, 0.75f, 1.0f }, new Color[] { new Color( 137, 139, 134, 255 ), new Color( 186, 189, 182, 153 ), new Color( 0, 0, 0, 127 ), new Color( 211, 215, 207, 51 ), new Color( 136, 138, 133, 0 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 50.0f, 0.0f ) ) );
		g.fill( shape300 );
	}

	private void paintCompositeGraphicsNode_0_0_0_0_0_9( Graphics2D g ) {
		// _0_0_0_0_0_9_0
		AffineTransform trans_0_0_0_0_0_9_0 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_9_0( g );
		g.setTransform( trans_0_0_0_0_0_9_0 );
		// _0_0_0_0_0_9_1
		AffineTransform trans_0_0_0_0_0_9_1 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 12.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_9_1( g );
		g.setTransform( trans_0_0_0_0_0_9_1 );
	}

	private void paintShapeNode_0_0_0_0_0_10_0( Graphics2D g ) {
		GeneralPath shape301 = new GeneralPath();
		shape301.moveTo( 688.0, 53.5 );
		shape301.curveTo( 678.34045, 53.5, 670.5, 55.964, 670.5, 59.0 );
		shape301.lineTo( 670.5, 62.5 );
		shape301.lineTo( 673.0, 86.5 );
		shape301.curveTo( 673.0, 89.81647, 679.72, 92.5, 688.0, 92.5 );
		shape301.lineTo( 688.0, 53.5 );
		shape301.closePath();
		g.setPaint( new LinearGradientPaint( new Point2D.Double( 701.375, 73.0 ), new Point2D.Double( 711.375, 71.875 ), new float[] { 0.0f, 0.29281864f, 1.0f }, new Color[] { new Color( 186, 189, 182, 255 ), new Color( 136, 138, 133, 255 ), new Color( 136, 138, 133, 0 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, -30.0f, 0.0f ) ) );
		g.fill( shape301 );
	}

	private void paintShapeNode_0_0_0_0_0_10_1( Graphics2D g ) {
		GeneralPath shape302 = new GeneralPath();
		shape302.moveTo( 688.0, 53.5 );
		shape302.lineTo( 688.0, 92.5 );
		shape302.curveTo( 696.28, 92.5, 703.0, 89.81647, 703.0, 86.5 );
		shape302.lineTo( 705.5, 62.5 );
		shape302.lineTo( 705.5, 59.0 );
		shape302.curveTo( 705.5, 55.964, 697.65955, 53.5, 688.0, 53.5 );
		shape302.closePath();
		g.setPaint( new LinearGradientPaint( new Point2D.Double( 701.375, 73.0 ), new Point2D.Double( 711.375, 71.875 ), new float[] { 0.0f, 0.26535374f, 1.0f }, new Color[] { new Color( 136, 138, 133, 255 ), new Color( 85, 87, 83, 255 ), new Color( 136, 138, 133, 0 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( -1.0f, 0.0f, 0.0f, 1.0f, 1406.0f, 0.0f ) ) );
		g.fill( shape302 );
	}

	private void paintShapeNode_0_0_0_0_0_10_2( Graphics2D g ) {
		GeneralPath shape303 = new GeneralPath();
		shape303.moveTo( 688.0, 53.5 );
		shape303.curveTo( 678.34045, 53.5, 670.5, 55.964, 670.5, 59.0 );
		shape303.lineTo( 670.5, 62.5 );
		shape303.lineTo( 673.0, 86.5 );
		shape303.curveTo( 673.0, 89.81647, 679.72, 92.5, 688.0, 92.5 );
		shape303.curveTo( 696.28, 92.5, 703.0, 89.81647, 703.0, 86.5 );
		shape303.lineTo( 705.5, 62.5 );
		shape303.lineTo( 705.5, 59.0 );
		shape303.curveTo( 705.5, 55.964, 697.6596, 53.5, 688.0, 53.5 );
		shape303.closePath();
		g.setPaint( new LinearGradientPaint( new Point2D.Double( 717.3125, 86.96126556396484 ), new Point2D.Double( 717.3125, 89.6151123046875 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 46, 52, 54, 255 ), new Color( 238, 238, 236, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, -30.0f, 0.0f ) ) );
		g.setStroke( new BasicStroke( 1.0f, 0, 0, 4.0f, null, 0.0f ) );
		g.draw( shape303 );
	}

	private void paintShapeNode_0_0_0_0_0_10_3( Graphics2D g ) {
		GeneralPath shape304 = new GeneralPath();
		shape304.moveTo( 673.65625, 66.34375 );
		shape304.lineTo( 676.6244, 88.75 );
		shape304.curveTo( 676.6244, 89.27971, 676.8295, 89.79167, 677.218, 90.28125 );
		shape304.curveTo( 679.43005, 91.08921, 682.3989, 91.7654, 685.875, 92.21875 );
		shape304.curveTo( 685.8408, 92.01217, 685.8255, 91.806816, 685.8255, 91.59375 );
		shape304.lineTo( 684.292, 68.25 );
		shape304.curveTo( 680.1154, 67.81158, 676.466, 67.144646, 673.65625, 66.34375 );
		shape304.closePath();
		g.setPaint( new RadialGradientPaint( new Point2D.Double( 709.765625, 79.28125 ), 6.109375f, new Point2D.Double( 709.765625, 79.28125 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 255, 255, 255, 255 ), new Color( 255, 255, 255, 0 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 0.7793732285499573f, -0.05980715900659561f, 0.4712935984134674f, 6.141634941101074f, 89.2285385131836f, -365.1861572265625f ) ) );
		g.fill( shape304 );
	}

	private void paintShapeNode_0_0_0_0_0_10_4( Graphics2D g ) {
		GeneralPath shape305 = new GeneralPath();
		shape305.moveTo( 702.875, 66.34375 );
		shape305.lineTo( 699.90686, 88.75 );
		shape305.curveTo( 699.90686, 89.27971, 699.7017, 89.79167, 699.31323, 90.28125 );
		shape305.curveTo( 697.1012, 91.08921, 694.1323, 91.7654, 690.65625, 92.21875 );
		shape305.curveTo( 690.6904, 92.01217, 690.70575, 91.806816, 690.70575, 91.59375 );
		shape305.lineTo( 692.23926, 68.25 );
		shape305.curveTo( 696.41583, 67.81158, 700.06525, 67.144646, 702.875, 66.34375 );
		shape305.closePath();
		g.setPaint( new RadialGradientPaint( new Point2D.Double( 711.0411987304688, 79.29367065429688 ), 6.109375f, new Point2D.Double( 711.0411987304688, 79.29367065429688 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 255, 255, 255, 255 ), new Color( 255, 255, 255, 0 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( -0.7793732285499573f, -0.05980715900659561f, -0.4712935984134674f, 6.141634941101074f, 1287.302734375f, -365.1861572265625f ) ) );
		g.fill( shape305 );
	}

	private void paintCompositeGraphicsNode_0_0_0_0_0_10( Graphics2D g ) {
		// _0_0_0_0_0_10_0
		AffineTransform trans_0_0_0_0_0_10_0 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_10_0( g );
		g.setTransform( trans_0_0_0_0_0_10_0 );
		// _0_0_0_0_0_10_1
		AffineTransform trans_0_0_0_0_0_10_1 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_10_1( g );
		g.setTransform( trans_0_0_0_0_0_10_1 );
		// _0_0_0_0_0_10_2
		AffineTransform trans_0_0_0_0_0_10_2 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_10_2( g );
		g.setTransform( trans_0_0_0_0_0_10_2 );
		// _0_0_0_0_0_10_3
		AffineTransform trans_0_0_0_0_0_10_3 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_10_3( g );
		g.setTransform( trans_0_0_0_0_0_10_3 );
		// _0_0_0_0_0_10_4
		g.setComposite( AlphaComposite.getInstance( 3, 0.4f * origAlpha ) );
		AffineTransform trans_0_0_0_0_0_10_4 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_10_4( g );
		g.setTransform( trans_0_0_0_0_0_10_4 );
	}

	private void paintShapeNode_0_0_0_0_0_11( Graphics2D g ) {
		GeneralPath shape306 = new GeneralPath();
		shape306.moveTo( 770.0, 55.5 );
		shape306.curveTo( 762.24286, 55.5, 755.6225, 57.182053, 752.875, 59.5625 );
		shape306.lineTo( 752.875, 62.25 );
		shape306.lineTo( 753.53125, 62.25 );
		shape306.curveTo( 753.51465, 62.16703, 753.46875, 62.084084, 753.46875, 62.0 );
		shape306.curveTo( 753.46875, 59.501564, 760.8776, 57.46875, 770.0, 57.46875 );
		shape306.curveTo( 779.12244, 57.468746, 786.53125, 59.501564, 786.53125, 62.0 );
		shape306.curveTo( 786.53125, 62.08408, 786.48535, 62.16703, 786.46875, 62.25 );
		shape306.lineTo( 787.25, 62.25 );
		shape306.lineTo( 787.25, 59.6875 );
		shape306.curveTo( 784.59076, 57.243168, 777.88794, 55.5, 770.0, 55.5 );
		shape306.closePath();
		g.setPaint( new Color( 0, 0, 0, 255 ) );
		g.fill( shape306 );
		g.setStroke( new BasicStroke( 0.9f, 1, 1, 4.0f, null, 0.0f ) );
		g.draw( shape306 );
	}

	private void paintShapeNode_0_0_0_0_0_12( Graphics2D g ) {
		GeneralPath shape307 = new GeneralPath();
		shape307.moveTo( 753.6875, 65.15625 );
		shape307.lineTo( 753.90625, 67.1875 );
		shape307.curveTo( 757.0876, 69.155106, 763.1033, 70.5, 770.0, 70.5 );
		shape307.curveTo( 776.8967, 70.5, 782.9124, 69.155106, 786.09375, 67.1875 );
		shape307.lineTo( 786.3125, 65.15625 );
		shape307.lineTo( 753.6875, 65.15625 );
		shape307.closePath();
		g.fill( shape307 );
		g.draw( shape307 );
	}

	private void paintShapeNode_0_0_0_0_0_13( Graphics2D g ) {
		GeneralPath shape308 = new GeneralPath();
		shape308.moveTo( 729.875, 60.625 );
		shape308.curveTo( 729.875, 63.90419, 723.2992, 66.5625, 715.1875, 66.5625 );
		shape308.curveTo( 707.0758, 66.5625, 700.5, 63.90419, 700.5, 60.625 );
		shape308.curveTo( 700.5, 57.34581, 707.0758, 54.6875, 715.1875, 54.6875 );
		shape308.curveTo( 723.2992, 54.6875, 729.875, 57.34581, 729.875, 60.625 );
		shape308.closePath();
		g.setPaint( new Color( 136, 138, 133, 255 ) );
		g.setStroke( new BasicStroke( 1.7608786f, 1, 1, 4.0f, null, 0.0f ) );
		g.draw( shape308 );
	}

	private void paintShapeNode_0_0_0_0_0_14( Graphics2D g ) {
		GeneralPath shape309 = new GeneralPath();
		shape309.moveTo( 770.0, 53.5 );
		shape309.curveTo( 759.788, 53.5, 751.5, 56.412003, 751.5, 60.0 );
		shape309.lineTo( 751.5, 63.0 );
		shape309.curveTo( 751.5, 66.588, 759.788, 69.5, 770.0, 69.5 );
		shape309.curveTo( 780.212, 69.5, 788.5, 66.588, 788.5, 63.0 );
		shape309.lineTo( 788.5, 60.0 );
		shape309.curveTo( 788.5, 56.412, 780.212, 53.5, 770.0, 53.5 );
		shape309.closePath();
		shape309.moveTo( 770.0, 55.46875 );
		shape309.curveTo( 779.12244, 55.468746, 786.53125, 57.501564, 786.53125, 60.0 );
		shape309.curveTo( 786.53125, 62.498436, 779.1224, 64.53125, 770.0, 64.53125 );
		shape309.curveTo( 760.8776, 64.53125, 753.46875, 62.498436, 753.46875, 60.0 );
		shape309.curveTo( 753.46875, 57.501564, 760.8776, 55.46875, 770.0, 55.46875 );
		shape309.closePath();
		g.setPaint( new LinearGradientPaint( new Point2D.Double( 700.99267578125, 60.5 ), new Point2D.Double( 735.00732421875, 60.5 ), new float[] { 0.0f, 0.19592221f, 0.326537f, 0.46741682f, 0.6147452f, 0.77435094f, 0.9032633f, 1.0f }, new Color[] { new Color( 81, 87, 85, 255 ), new Color( 255, 255, 255, 255 ), new Color( 255, 255, 255, 255 ), new Color( 174, 179, 176, 255 ), new Color( 148, 153, 150, 255 ), new Color( 240, 241, 241, 255 ), new Color( 237, 238, 238, 255 ), new Color( 90, 90, 90, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 52.0f, 1.0f ) ) );
		g.fill( shape309 );
		g.setPaint( new LinearGradientPaint( new Point2D.Double( 728.4083862304688, 64.52535247802734 ), new Point2D.Double( 728.4083862304688, 71.11156463623047 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 46, 52, 54, 255 ), new Color( 186, 189, 182, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 52.0f, 1.0f ) ) );
		g.setStroke( new BasicStroke( 0.9f, 1, 1, 4.0f, null, 0.0f ) );
		g.draw( shape309 );
	}

	private void paintShapeNode_0_0_0_0_0_15( Graphics2D g ) {
		GeneralPath shape310 = new GeneralPath();
		shape310.moveTo( 770.0, 54.5 );
		shape310.curveTo( 764.9884, 54.5, 760.4495, 55.21959, 757.25, 56.34375 );
		shape310.curveTo( 755.6502, 56.90583, 754.39355, 57.585976, 753.59375, 58.25 );
		shape310.curveTo( 752.79395, 58.914024, 752.5, 59.48539, 752.5, 60.0 );
		shape310.lineTo( 752.5, 63.0 );
		shape310.curveTo( 752.5, 63.421837, 752.7459, 63.935043, 753.375, 64.53125 );
		shape310.curveTo( 753.5514, 64.69486, 753.6625, 64.91695, 753.6875, 65.15625 );
		shape310.lineTo( 755.8125, 84.59375 );
		shape310.curveTo( 755.8373, 84.88274, 755.7346, 85.16814, 755.53125, 85.375 );
		shape310.curveTo( 755.0467, 85.87041, 754.9166, 86.21856, 754.96875, 86.53125 );
		shape310.lineTo( 755.46875, 89.53125 );
		shape310.curveTo( 755.63226, 90.512375, 757.00775, 91.84529, 759.59375, 92.84375 );
		shape310.curveTo( 762.17975, 93.84221, 765.8245, 94.5, 770.0, 94.5 );
		shape310.curveTo( 774.1755, 94.5, 777.82025, 93.84221, 780.40625, 92.84375 );
		shape310.curveTo( 782.99225, 91.84529, 784.36774, 90.512375, 784.53125, 89.53125 );
		shape310.lineTo( 785.03125, 86.53125 );
		shape310.curveTo( 785.0834, 86.21856, 784.95337, 85.87041, 784.46875, 85.375 );
		shape310.curveTo( 784.26544, 85.16814, 784.1627, 84.88274, 784.1875, 84.59375 );
		shape310.lineTo( 786.3125, 65.15625 );
		shape310.curveTo( 786.3375, 64.91695, 786.4486, 64.69486, 786.625, 64.53125 );
		shape310.curveTo( 787.2541, 63.935043, 787.5, 63.421852, 787.5, 63.0 );
		shape310.lineTo( 787.5, 60.0 );
		shape310.curveTo( 787.5, 59.48539, 787.20605, 58.914024, 786.40625, 58.25 );
		shape310.curveTo( 785.60645, 57.585976, 784.3498, 56.90583, 782.75, 56.34375 );
		shape310.curveTo( 779.5505, 55.21959, 775.0116, 54.5, 770.0, 54.5 );
		shape310.closePath();
		g.setPaint( new LinearGradientPaint( new Point2D.Double( 704.625, 62.875 ), new Point2D.Double( 736.0, 97.625 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 255, 255, 255, 255 ), new Color( 255, 255, 255, 0 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 52.0f, 0.0f ) ) );
		g.setStroke( new BasicStroke( 0.99999994f, 1, 1, 4.0f, null, 0.0f ) );
		g.draw( shape310 );
	}

	private void paintShapeNode_0_0_0_0_0_16( Graphics2D g ) {
		GeneralPath shape311 = new GeneralPath();
		shape311.moveTo( 729.875, 60.625 );
		shape311.curveTo( 729.875, 63.90419, 723.2992, 66.5625, 715.1875, 66.5625 );
		shape311.curveTo( 707.0758, 66.5625, 700.5, 63.90419, 700.5, 60.625 );
		shape311.curveTo( 700.5, 57.34581, 707.0758, 54.6875, 715.1875, 54.6875 );
		shape311.curveTo( 723.2992, 54.6875, 729.875, 57.34581, 729.875, 60.625 );
		shape311.closePath();
		g.setPaint( new RadialGradientPaint( new Point2D.Double( 712.1088256835938, 64.05938720703125 ), 15.137135f, new Point2D.Double( 712.1088256835938, 64.05938720703125 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 255, 255, 255, 255 ), new Color( 211, 215, 207, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 0.429599791765213f, 1.1141945123672485f, -3.6476752758026123f, 2.3118515014648438f, 635.1559448242188f, -875.8652954101562f ) ) );
		g.setStroke( new BasicStroke( 0.8992707f, 1, 1, 4.0f, null, 0.0f ) );
		g.draw( shape311 );
	}

	private void paintShapeNode_0_0_0_0_0_17( Graphics2D g ) {
		GeneralPath shape312 = new GeneralPath();
		shape312.moveTo( 708.1875, 61.8125 );
		shape312.curveTo( 708.1875, 63.22773, 707.0402, 64.375, 705.625, 64.375 );
		shape312.curveTo( 704.2098, 64.375, 703.0625, 63.22773, 703.0625, 61.8125 );
		shape312.curveTo( 703.0625, 60.39727, 704.2098, 59.25, 705.625, 59.25 );
		shape312.curveTo( 707.0402, 59.25, 708.1875, 60.39727, 708.1875, 61.8125 );
		shape312.closePath();
		g.setPaint( new RadialGradientPaint( new Point2D.Double( 705.625, 61.8125 ), 2.5625f, new Point2D.Double( 705.625, 61.8125 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 255, 255, 255, 255 ), new Color( 255, 255, 255, 0 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) ) );
		g.fill( shape312 );
	}

	private void paintShapeNode_0_0_0_0_0_18( Graphics2D g ) {
		GeneralPath shape313 = new GeneralPath();
		shape313.moveTo( 708.1875, 61.8125 );
		shape313.curveTo( 708.1875, 63.22773, 707.0402, 64.375, 705.625, 64.375 );
		shape313.curveTo( 704.2098, 64.375, 703.0625, 63.22773, 703.0625, 61.8125 );
		shape313.curveTo( 703.0625, 60.39727, 704.2098, 59.25, 705.625, 59.25 );
		shape313.curveTo( 707.0402, 59.25, 708.1875, 60.39727, 708.1875, 61.8125 );
		shape313.closePath();
		g.setPaint( new Color( 255, 255, 255, 255 ) );
		g.fill( shape313 );
	}

	private void paintShapeNode_0_0_0_0_0_19( Graphics2D g ) {
		GeneralPath shape314 = new GeneralPath();
		shape314.moveTo( 708.1875, 61.8125 );
		shape314.curveTo( 708.1875, 63.22773, 707.0402, 64.375, 705.625, 64.375 );
		shape314.curveTo( 704.2098, 64.375, 703.0625, 63.22773, 703.0625, 61.8125 );
		shape314.curveTo( 703.0625, 60.39727, 704.2098, 59.25, 705.625, 59.25 );
		shape314.curveTo( 707.0402, 59.25, 708.1875, 60.39727, 708.1875, 61.8125 );
		shape314.closePath();
		g.setPaint( new RadialGradientPaint( new Point2D.Double( 705.625, 61.8125 ), 2.5625f, new Point2D.Double( 705.625, 61.8125 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 255, 255, 255, 255 ), new Color( 255, 255, 255, 0 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) ) );
		g.fill( shape314 );
	}

	private void paintShapeNode_0_0_0_0_0_20( Graphics2D g ) {
		GeneralPath shape315 = new GeneralPath();
		shape315.moveTo( 708.1875, 61.8125 );
		shape315.curveTo( 708.1875, 63.22773, 707.0402, 64.375, 705.625, 64.375 );
		shape315.curveTo( 704.2098, 64.375, 703.0625, 63.22773, 703.0625, 61.8125 );
		shape315.curveTo( 703.0625, 60.39727, 704.2098, 59.25, 705.625, 59.25 );
		shape315.curveTo( 707.0402, 59.25, 708.1875, 60.39727, 708.1875, 61.8125 );
		shape315.closePath();
		g.setPaint( new Color( 255, 255, 255, 255 ) );
		g.fill( shape315 );
	}

	private void paintShapeNode_0_0_0_0_0_21( Graphics2D g ) {
		GeneralPath shape316 = new GeneralPath();
		shape316.moveTo( 708.1875, 61.8125 );
		shape316.curveTo( 708.1875, 63.22773, 707.0402, 64.375, 705.625, 64.375 );
		shape316.curveTo( 704.2098, 64.375, 703.0625, 63.22773, 703.0625, 61.8125 );
		shape316.curveTo( 703.0625, 60.39727, 704.2098, 59.25, 705.625, 59.25 );
		shape316.curveTo( 707.0402, 59.25, 708.1875, 60.39727, 708.1875, 61.8125 );
		shape316.closePath();
		g.fill( shape316 );
	}

	private void paintShapeNode_0_0_0_0_0_22_0( Graphics2D g ) {
		GeneralPath shape317 = new GeneralPath();
		shape317.moveTo( 725.40625, 50.5 );
		shape317.curveTo( 724.6349, 51.04249, 723.8142, 51.46891, 723.81696, 52.349884 );
		shape317.curveTo( 723.4196, 52.77492, 723.02234, 53.199963, 722.625, 53.625 );
		shape317.curveTo( 722.625, 53.114582, 722.625, 52.604168, 722.625, 52.09375 );
		shape317.curveTo( 722.30383, 52.17686, 722.24634, 52.15161, 722.086, 52.80447 );
		shape317.curveTo( 721.83777, 53.285122, 721.5076, 53.72775, 720.9758, 53.91085 );
		shape317.curveTo( 719.8563, 54.520466, 719.40564, 54.65179, 718.25, 55.1875 );
		shape317.curveTo( 718.6212, 55.443405, 718.73737, 56.169624, 718.1983, 55.999313 );
		shape317.curveTo( 717.6947, 55.582874, 717.1911, 55.16644, 716.6875, 54.75 );
		shape317.curveTo( 716.4507, 54.609478, 715.8245, 54.70939, 715.43964, 54.66671 );
		shape317.curveTo( 714.7858, 54.511814, 714.16516, 55.31343, 713.6129, 55.571922 );
		shape317.curveTo( 713.1786, 55.726322, 712.7783, 56.263046, 712.30475, 55.9766 );
		shape317.curveTo( 711.72, 55.94475, 712.4887, 56.433907, 712.6363, 56.66126 );
		shape317.curveTo( 713.03296, 57.018147, 713.3065, 57.59734, 713.91437, 57.572826 );
		shape317.curveTo( 714.60956, 57.715218, 715.3048, 57.857613, 716.0, 58.000004 );
		shape317.curveTo( 715.64166, 58.42967, 715.0943, 57.982204, 714.63824, 57.958393 );
		shape317.curveTo( 714.13385, 57.836845, 713.6294, 57.7153, 713.125, 57.593754 );
		shape317.curveTo( 713.1713, 57.011032, 712.7001, 58.065136, 712.52344, 58.210934 );
		shape317.curveTo( 712.1424, 57.977596, 712.02026, 57.48959, 711.71875, 58.000004 );
		shape317.curveTo( 711.1031, 58.206635, 710.3569, 58.175613, 709.943, 58.749603 );
		shape317.curveTo( 709.6827, 59.07267, 709.20044, 59.329544, 709.1664, 59.4899 );
		shape317.curveTo( 709.0286, 59.743824, 709.073, 58.782387, 708.96875, 58.531254 );
		shape317.curveTo( 708.5921, 58.401356, 708.1358, 57.803272, 707.813, 57.990185 );
		shape317.curveTo( 707.13574, 58.82679, 706.146, 58.9759, 705.46875, 59.812504 );
		shape317.curveTo( 704.75, 59.760426, 704.03125, 59.708336, 703.3125, 59.656254 );
		shape317.curveTo( 703.3662, 60.478775, 704.5244, 61.62564, 705.21875, 61.875004 );
		shape317.curveTo( 706.3631, 62.579174, 707.78204, 62.761993, 708.9474, 63.235508 );
		shape317.curveTo( 709.44727, 63.311996, 709.5674, 63.588013, 710.0321, 63.59717 );
		shape317.curveTo( 710.88086, 63.791428, 711.7277, 63.83627, 712.5938, 64.03125 );
		shape317.curveTo( 717.3758, 64.68131, 722.25024, 64.631836, 727.0428, 64.09382 );
		shape317.curveTo( 729.89526, 63.6859, 732.88745, 63.285446, 735.37836, 61.739826 );
		shape317.curveTo( 736.20044, 61.29655, 736.63043, 60.35727, 735.5068, 60.186493 );
		shape317.curveTo( 734.78015, 59.915115, 734.9808, 59.86898, 735.6466, 59.93586 );
		shape317.curveTo( 735.9684, 59.591896, 736.2092, 59.091434, 736.5, 58.687496 );
		shape317.curveTo( 736.3189, 58.227097, 735.9281, 57.78262, 735.875, 57.312496 );
		shape317.curveTo( 735.5222, 57.93011, 735.28766, 57.349976, 735.0558, 56.972878 );
		shape317.curveTo( 734.7495, 56.248596, 733.9667, 56.007526, 733.3712, 55.576435 );
		shape317.curveTo( 733.08777, 55.31209, 732.4978, 54.855213, 733.125, 54.625 );
		shape317.curveTo( 733.2043, 54.276974, 732.9391, 53.77511, 732.8917, 53.364845 );
		shape317.curveTo( 732.81934, 52.971996, 732.103, 53.346737, 731.7663, 53.265305 );
		shape317.curveTo( 731.268, 53.274204, 730.7286, 53.392937, 730.2528, 53.299854 );
		shape317.curveTo( 729.0433, 52.472916, 727.333, 51.444237, 726.1455, 50.61373 );
		shape317.curveTo( 725.89905, 50.57582, 725.65265, 50.53791, 725.40625, 50.5 );
		shape317.closePath();
		shape317.moveTo( 713.125, 59.15625 );
		shape317.curveTo( 713.27246, 59.366318, 712.78326, 59.82601, 713.125, 59.15625 );
		shape317.closePath();
		g.setPaint( new LinearGradientPaint( new Point2D.Double( -190.5, 104.1875 ), new Point2D.Double( -96.75, 104.1875 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 237, 244, 252, 255 ), new Color( 192, 198, 214, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( -0.1949329972267151f, 0.0f, 0.0f, 0.19016219675540924f, 690.71142578125f, 40.308677673339844f ) ) );
		g.fill( shape317 );
		g.setPaint( new Color( 85, 87, 83, 255 ) );
		g.setStroke( new BasicStroke( 1.0f, 0, 0, 4.0f, null, 0.0f ) );
		g.draw( shape317 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_0_0( Graphics2D g ) {
		GeneralPath shape318 = new GeneralPath();
		shape318.moveTo( -157.0, 88.0 );
		shape318.curveTo( -156.875, 87.5, -158.875, 85.75, -157.125, 84.75 );
		shape318.curveTo( -155.375, 83.75, -150.5, 79.625, -150.5, 79.625 );
		shape318.lineTo( -146.0, 80.125 );
		shape318.lineTo( -139.375, 86.0 );
		shape318.lineTo( -128.25, 94.25 );
		shape318.lineTo( -115.25, 93.875 );
		shape318.lineTo( -113.75, 100.875 );
		shape318.lineTo( -115.75, 102.375 );
		shape318.lineTo( -113.375, 105.75 );
		shape318.lineTo( -106.125, 110.25 );
		shape318.lineTo( -101.75, 116.875 );
		shape318.lineTo( -100.25, 115.5 );
		shape318.lineTo( -96.75, 122.125 );
		shape318.lineTo( -112.16713, 124.83539 );
		shape318.curveTo( -138.106, 128.99321, -159.32202, 127.14014, -181.0, 125.875 );
		shape318.lineTo( -181.375, 118.5 );
		shape318.lineTo( -190.5, 105.75 );
		shape318.lineTo( -170.25, 96.375 );
		shape318.lineTo( -164.125, 87.875 );
		shape318.lineTo( -164.25, 95.75 );
		shape318.lineTo( -157.0, 88.0 );
		shape318.closePath();
		g.setPaint( new LinearGradientPaint( new Point2D.Double( -190.5, 104.1875 ), new Point2D.Double( -96.75, 104.1875 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 237, 244, 252, 255 ), new Color( 192, 198, 214, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) ) );
		g.fill( shape318 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_0_1( Graphics2D g ) {
		GeneralPath shape319 = new GeneralPath();
		shape319.moveTo( -163.625, 96.25 );
		shape319.lineTo( -155.0, 85.25 );
		shape319.curveTo( -155.0, 85.25, -147.25, 92.875, -146.75, 93.0 );
		shape319.curveTo( -146.25, 93.125, -138.125, 94.25, -138.125, 94.25 );
		shape319.lineTo( -146.875, 99.75 );
		shape319.lineTo( -150.125, 99.625 );
		shape319.lineTo( -157.375, 107.75 );
		shape319.lineTo( -163.625, 96.25 );
		shape319.closePath();
		g.setPaint( new Color( 252, 253, 255, 255 ) );
		g.fill( shape319 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_0_2( Graphics2D g ) {
		GeneralPath shape320 = new GeneralPath();
		shape320.moveTo( -225.13652, 107.7264 );
		shape320.lineTo( -218.26152, 115.1014 );
		shape320.lineTo( -201.88652, 118.4764 );
		shape320.lineTo( -193.63652, 109.8514 );
		shape320.lineTo( -202.38652, 102.2264 );
		shape320.lineTo( -211.51152, 101.9764 );
		shape320.lineTo( -222.13652, 107.9764 );
		shape320.lineTo( -224.38652, 107.1014 );
		shape320.lineTo( -225.13652, 107.7264 );
		shape320.closePath();
		g.setPaint( new LinearGradientPaint( new Point2D.Double( -228.375, 89.875 ), new Point2D.Double( -196.875, 89.875 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 189, 208, 225, 255 ), new Color( 255, 255, 255, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 3.2384822368621826f, 20.351398468017578f ) ) );
		g.fill( shape320 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_0_3( Graphics2D g ) {
		GeneralPath shape321 = new GeneralPath();
		shape321.moveTo( -221.04579, 107.9764 );
		shape321.lineTo( -217.92079, 109.3514 );
		shape321.lineTo( -205.42079, 103.3514 );
		shape321.lineTo( -202.79579, 105.2264 );
		shape321.lineTo( -197.79579, 102.3514 );
		shape321.lineTo( -207.29579, 102.2264 );
		shape321.lineTo( -218.04579, 107.9764 );
		shape321.lineTo( -220.42079, 107.1014 );
		shape321.lineTo( -221.04579, 107.9764 );
		shape321.closePath();
		g.setPaint( new Color( 255, 255, 255, 255 ) );
		g.fill( shape321 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_0_4( Graphics2D g ) {
		GeneralPath shape322 = new GeneralPath();
		shape322.moveTo( -188.75, 106.0 );
		shape322.lineTo( -168.0, 94.625 );
		shape322.lineTo( -165.625, 87.5 );
		shape322.lineTo( -164.5, 89.625 );
		shape322.lineTo( -166.125, 96.0 );
		shape322.lineTo( -164.0, 95.75 );
		shape322.lineTo( -157.875, 89.125 );
		shape322.lineTo( -164.75, 97.625 );
		shape322.lineTo( -180.5, 106.625 );
		shape322.lineTo( -188.75, 106.0 );
		shape322.closePath();
		g.fill( shape322 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_0_5( Graphics2D g ) {
		GeneralPath shape323 = new GeneralPath();
		shape323.moveTo( -179.125, 109.0 );
		shape323.lineTo( -173.375, 121.5 );
		shape323.lineTo( -163.125, 108.625 );
		shape323.lineTo( -156.875, 109.875 );
		shape323.lineTo( -158.375, 105.5 );
		shape323.lineTo( -165.75, 104.125 );
		shape323.lineTo( -179.125, 109.0 );
		shape323.closePath();
		g.setPaint( new Color( 185, 200, 219, 255 ) );
		g.fill( shape323 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_0_6( Graphics2D g ) {
		GeneralPath shape324 = new GeneralPath();
		shape324.moveTo( -156.5, 85.375 );
		shape324.curveTo( -156.5, 86.125, -155.875, 103.75, -155.875, 103.75 );
		shape324.lineTo( -158.125, 101.75 );
		shape324.lineTo( -157.25, 86.375 );
		shape324.lineTo( -156.5, 85.375 );
		shape324.closePath();
		g.setPaint( new Color( 180, 198, 218, 255 ) );
		g.fill( shape324 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_0_7( Graphics2D g ) {
		GeneralPath shape325 = new GeneralPath();
		shape325.moveTo( -189.125, 107.0 );
		shape325.lineTo( -181.625, 119.625 );
		shape325.lineTo( -176.25, 116.625 );
		shape325.lineTo( -173.0, 121.125 );
		shape325.lineTo( -178.0, 109.25 );
		shape325.lineTo( -165.75, 104.75 );
		shape325.lineTo( -157.375, 107.375 );
		shape325.lineTo( -153.0, 102.75 );
		shape325.lineTo( -157.875, 100.125 );
		shape325.lineTo( -166.5, 103.25 );
		shape325.lineTo( -173.5, 103.5 );
		shape325.lineTo( -180.625, 108.5 );
		shape325.lineTo( -189.125, 107.0 );
		shape325.closePath();
		g.setPaint( new Color( 177, 186, 203, 255 ) );
		g.fill( shape325 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_0_8( Graphics2D g ) {
		GeneralPath shape326 = new GeneralPath();
		shape326.moveTo( -161.625, 96.0 );
		shape326.lineTo( -153.75, 101.75 );
		shape326.lineTo( -157.875, 106.75 );
		shape326.lineTo( -162.875, 97.5 );
		shape326.lineTo( -161.625, 96.0 );
		shape326.closePath();
		g.setPaint( new Color( 217, 225, 236, 255 ) );
		g.fill( shape326 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_0_9( Graphics2D g ) {
		GeneralPath shape327 = new GeneralPath();
		shape327.moveTo( -154.83887, 89.125 );
		shape327.lineTo( -154.25293, 107.875 );
		shape327.lineTo( -150.15137, 100.55078 );
		shape327.lineTo( -144.14551, 99.37891 );
		shape327.lineTo( -138.28613, 95.7168 );
		shape327.lineTo( -154.83887, 89.125 );
		shape327.closePath();
		g.setPaint( new Color( 195, 215, 255, 255 ) );
		g.fill( shape327 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_0_10( Graphics2D g ) {
		GeneralPath shape328 = new GeneralPath();
		shape328.moveTo( -154.75, 89.625 );
		shape328.lineTo( -154.25, 105.625 );
		shape328.lineTo( -150.75, 99.375 );
		shape328.lineTo( -145.625, 98.375 );
		shape328.lineTo( -140.625, 95.25 );
		shape328.lineTo( -154.75, 89.625 );
		shape328.closePath();
		g.setPaint( new Color( 252, 253, 255, 255 ) );
		g.fill( shape328 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_0_11( Graphics2D g ) {
		GeneralPath shape329 = new GeneralPath();
		shape329.moveTo( -136.0, 116.375 );
		shape329.lineTo( -140.625, 119.75 );
		shape329.lineTo( -137.625, 119.125 );
		shape329.lineTo( -136.0, 116.375 );
		shape329.closePath();
		g.setPaint( new Color( 192, 198, 214, 255 ) );
		g.fill( shape329 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_0_12( Graphics2D g ) {
		GeneralPath shape330 = new GeneralPath();
		shape330.moveTo( -140.5, 113.375 );
		shape330.lineTo( -134.875, 117.5 );
		shape330.lineTo( -136.375, 119.625 );
		shape330.lineTo( -131.25, 122.375 );
		shape330.lineTo( -142.0, 121.75 );
		shape330.lineTo( -144.25, 115.625 );
		shape330.lineTo( -140.5, 113.375 );
		shape330.closePath();
		g.fill( shape330 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_0_13( Graphics2D g ) {
		GeneralPath shape331 = new GeneralPath();
		shape331.moveTo( -144.5, 113.125 );
		shape331.curveTo( -143.75, 112.875, -137.75, 109.875, -137.75, 109.875 );
		shape331.lineTo( -134.375, 113.375 );
		shape331.lineTo( -138.25, 112.375 );
		shape331.lineTo( -144.5, 113.125 );
		shape331.closePath();
		g.fill( shape331 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_0_14( Graphics2D g ) {
		GeneralPath shape332 = new GeneralPath();
		shape332.moveTo( -136.625, 95.5 );
		shape332.lineTo( -134.625, 112.75 );
		shape332.lineTo( -151.625, 114.5 );
		shape332.lineTo( -154.875, 108.0 );
		shape332.lineTo( -148.5, 102.625 );
		shape332.lineTo( -144.0, 103.375 );
		shape332.lineTo( -137.125, 96.5 );
		shape332.lineTo( -136.625, 95.5 );
		shape332.closePath();
		g.fill( shape332 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_0_15( Graphics2D g ) {
		GeneralPath shape333 = new GeneralPath();
		shape333.moveTo( -153.875, 106.0 );
		shape333.lineTo( -149.875, 101.125 );
		shape333.lineTo( -136.5, 95.0 );
		shape333.lineTo( -134.875, 104.125 );
		shape333.lineTo( -130.0, 106.75 );
		shape333.lineTo( -131.625, 108.0 );
		shape333.lineTo( -135.375, 105.75 );
		shape333.lineTo( -136.75, 95.125 );
		shape333.lineTo( -153.875, 106.0 );
		shape333.closePath();
		g.fill( shape333 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_0_16( Graphics2D g ) {
		GeneralPath shape334 = new GeneralPath();
		shape334.moveTo( -135.375, 94.875 );
		shape334.lineTo( -129.25, 98.75 );
		shape334.lineTo( -132.25, 102.25 );
		shape334.lineTo( -135.375, 94.875 );
		shape334.closePath();
		g.fill( shape334 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_0_17( Graphics2D g ) {
		GeneralPath shape335 = new GeneralPath();
		shape335.moveTo( -128.0, 95.0 );
		shape335.curveTo( -128.0, 95.0, -131.75, 105.375, -131.5, 104.75 );
		shape335.curveTo( -131.25, 104.125, -125.875, 95.375, -125.875, 95.375 );
		shape335.lineTo( -116.875, 94.625 );
		shape335.lineTo( -128.0, 95.0 );
		shape335.closePath();
		g.fill( shape335 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_0_18( Graphics2D g ) {
		GeneralPath shape336 = new GeneralPath();
		shape336.moveTo( -127.5, 109.125 );
		shape336.lineTo( -112.0, 107.75 );
		shape336.lineTo( -112.5, 121.875 );
		shape336.lineTo( -121.875, 112.5 );
		shape336.lineTo( -124.5, 115.125 );
		shape336.lineTo( -123.0, 119.75 );
		shape336.lineTo( -125.625, 118.5 );
		shape336.lineTo( -125.375, 112.875 );
		shape336.lineTo( -127.5, 109.125 );
		shape336.closePath();
		g.fill( shape336 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_0_19( Graphics2D g ) {
		GeneralPath shape337 = new GeneralPath();
		shape337.moveTo( -106.55702, 114.08006 );
		shape337.curveTo( -106.68202, 113.45506, -104.93202, 107.20506, -104.93202, 107.20506 );
		shape337.lineTo( -103.43202, 111.33006 );
		shape337.lineTo( -99.93203, 109.20506 );
		shape337.lineTo( -95.18203, 102.95506 );
		shape337.lineTo( -99.68203, 115.83006 );
		shape337.lineTo( -106.55702, 114.08006 );
		shape337.closePath();
		g.fill( shape337 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_0_20( Graphics2D g ) {
		GeneralPath shape338 = new GeneralPath();
		shape338.moveTo( -115.07949, 125.0427 );
		shape338.curveTo( -115.57949, 124.6677, -118.45449, 121.5427, -118.45449, 121.5427 );
		shape338.lineTo( -125.70449, 122.2927 );
		shape338.lineTo( -124.32949, 125.2927 );
		shape338.lineTo( -115.07949, 125.0427 );
		shape338.closePath();
		g.fill( shape338 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_0_21( Graphics2D g ) {
		GeneralPath shape339 = new GeneralPath();
		shape339.moveTo( -170.5, 125.875 );
		shape339.lineTo( -161.5, 119.375 );
		shape339.lineTo( -164.75, 125.125 );
		shape339.lineTo( -170.5, 125.875 );
		shape339.closePath();
		g.fill( shape339 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_0_22( Graphics2D g ) {
		GeneralPath shape340 = new GeneralPath();
		shape340.moveTo( -164.875, 125.375 );
		shape340.lineTo( -161.125, 119.0 );
		shape340.lineTo( -161.625, 125.5 );
		shape340.lineTo( -164.875, 125.375 );
		shape340.closePath();
		g.fill( shape340 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_0_23( Graphics2D g ) {
		GeneralPath shape341 = new GeneralPath();
		shape341.moveTo( -171.125, 125.625 );
		shape341.lineTo( -162.875, 118.75 );
		shape341.lineTo( -161.5, 111.25 );
		shape341.lineTo( -159.625, 115.125 );
		shape341.lineTo( -161.5, 119.5 );
		shape341.lineTo( -171.125, 125.625 );
		shape341.closePath();
		g.fill( shape341 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_0_24( Graphics2D g ) {
		GeneralPath shape342 = new GeneralPath();
		shape342.moveTo( -156.25, 118.625 );
		shape342.lineTo( -153.875, 124.875 );
		shape342.lineTo( -153.0, 118.875 );
		shape342.lineTo( -156.25, 118.625 );
		shape342.closePath();
		g.fill( shape342 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_0_25( Graphics2D g ) {
		GeneralPath shape343 = new GeneralPath();
		shape343.moveTo( -144.66096, 105.77304 );
		shape343.lineTo( -141.66096, 109.64804 );
		shape343.lineTo( -144.28596, 111.77304 );
		shape343.lineTo( -140.28596, 114.02304 );
		shape343.lineTo( -137.66096, 111.64804 );
		shape343.lineTo( -144.66096, 105.77304 );
		shape343.closePath();
		g.setPaint( new Color( 145, 159, 186, 255 ) );
		g.fill( shape343 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_0_26( Graphics2D g ) {
		GeneralPath shape344 = new GeneralPath();
		shape344.moveTo( -147.16096, 86.39804 );
		shape344.curveTo( -146.91096, 87.02304, -144.03596, 92.14804, -144.03596, 92.14804 );
		shape344.lineTo( -142.03596, 88.89804 );
		shape344.lineTo( -147.16096, 86.39804 );
		shape344.closePath();
		g.fill( shape344 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_0_27( Graphics2D g ) {
		GeneralPath shape345 = new GeneralPath();
		shape345.moveTo( -176.28596, 111.64804 );
		shape345.lineTo( -169.41096, 118.52304 );
		shape345.lineTo( -173.66096, 118.52304 );
		shape345.lineTo( -176.53596, 113.89804 );
		shape345.lineTo( -179.41096, 115.14804 );
		shape345.lineTo( -176.28596, 111.64804 );
		shape345.closePath();
		g.fill( shape345 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_0_28( Graphics2D g ) {
		GeneralPath shape346 = new GeneralPath();
		shape346.moveTo( -194.66096, 110.02304 );
		shape346.curveTo( -194.66096, 110.02304, -184.03596, 117.39804, -184.78596, 117.39804 );
		shape346.curveTo( -185.53596, 117.39804, -189.53596, 117.52304, -189.53596, 117.52304 );
		shape346.lineTo( -194.66096, 110.02304 );
		shape346.closePath();
		g.fill( shape346 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_0_29( Graphics2D g ) {
		GeneralPath shape347 = new GeneralPath();
		shape347.moveTo( -191.41096, 117.02304 );
		shape347.curveTo( -191.41096, 117.02304, -200.91096, 112.02304, -201.03596, 111.52304 );
		shape347.curveTo( -201.16096, 111.02304, -203.28596, 106.27304, -203.28596, 106.27304 );
		shape347.lineTo( -202.28596, 112.14804 );
		shape347.lineTo( -196.41096, 117.27304 );
		shape347.lineTo( -191.41096, 117.02304 );
		shape347.closePath();
		g.fill( shape347 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_0_30( Graphics2D g ) {
		GeneralPath shape348 = new GeneralPath();
		shape348.moveTo( -195.78596, 117.52304 );
		shape348.lineTo( -200.78596, 117.14804 );
		shape348.lineTo( -205.78596, 112.52304 );
		shape348.lineTo( -204.41096, 106.02304 );
		shape348.lineTo( -202.66096, 112.27304 );
		shape348.lineTo( -195.78596, 117.52304 );
		shape348.closePath();
		g.fill( shape348 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_0_31( Graphics2D g ) {
		GeneralPath shape349 = new GeneralPath();
		shape349.moveTo( -203.91096, 117.14804 );
		shape349.lineTo( -206.53596, 113.52304 );
		shape349.lineTo( -213.91096, 114.14804 );
		shape349.lineTo( -207.66096, 111.89804 );
		shape349.lineTo( -204.28596, 105.77304 );
		shape349.lineTo( -206.16096, 112.52304 );
		shape349.lineTo( -200.41096, 117.14804 );
		shape349.lineTo( -203.91096, 117.14804 );
		shape349.closePath();
		g.fill( shape349 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_0_32( Graphics2D g ) {
		GeneralPath shape350 = new GeneralPath();
		shape350.moveTo( -218.66096, 113.02304 );
		shape350.lineTo( -207.66096, 110.02304 );
		shape350.lineTo( -203.16096, 98.52304 );
		shape350.lineTo( -197.66096, 99.39804 );
		shape350.lineTo( -195.53596, 112.02304 );
		shape350.lineTo( -203.91096, 104.52304 );
		shape350.lineTo( -207.91096, 112.02304 );
		shape350.lineTo( -213.66096, 114.64804 );
		shape350.lineTo( -218.66096, 113.02304 );
		shape350.closePath();
		g.setPaint( new Color( 255, 255, 255, 255 ) );
		g.fill( shape350 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_0_33( Graphics2D g ) {
		GeneralPath shape351 = new GeneralPath();
		shape351.moveTo( -215.28596, 114.77304 );
		shape351.lineTo( -211.78596, 117.27304 );
		shape351.lineTo( -204.03596, 117.14804 );
		shape351.lineTo( -208.03596, 113.14804 );
		shape351.lineTo( -215.91096, 114.64804 );
		shape351.lineTo( -215.28596, 114.77304 );
		shape351.closePath();
		g.setPaint( new Color( 223, 227, 235, 255 ) );
		g.fill( shape351 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_0_34( Graphics2D g ) {
		GeneralPath shape352 = new GeneralPath();
		shape352.moveTo( 142.125, 98.875 );
		shape352.lineTo( 140.875, 106.0 );
		shape352.lineTo( 136.5, 107.625 );
		shape352.lineTo( 141.5, 106.5 );
		shape352.lineTo( 142.5, 100.75 );
		shape352.lineTo( 147.375, 105.25 );
		shape352.lineTo( 152.0, 105.5 );
		shape352.lineTo( 157.25, 109.125 );
		shape352.lineTo( 153.125, 103.875 );
		shape352.lineTo( 149.5, 104.75 );
		shape352.lineTo( 142.125, 98.875 );
		shape352.closePath();
		g.setPaint( new Color( 255, 255, 255, 255 ) );
		g.fill( shape352 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_0_35( Graphics2D g ) {
		GeneralPath shape353 = new GeneralPath();
		shape353.moveTo( 150.375, 119.875 );
		shape353.lineTo( 148.0, 122.25 );
		shape353.lineTo( 143.25, 124.25 );
		shape353.lineTo( 139.75, 121.875 );
		shape353.lineTo( 142.5, 125.375 );
		shape353.lineTo( 144.5, 125.375 );
		shape353.lineTo( 149.5, 122.5 );
		shape353.lineTo( 150.375, 119.875 );
		shape353.closePath();
		g.fill( shape353 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_0_36( Graphics2D g ) {
		GeneralPath shape354 = new GeneralPath();
		shape354.moveTo( 183.875, 94.125 );
		shape354.lineTo( 187.5, 91.125 );
		shape354.lineTo( 192.25, 96.0 );
		shape354.lineTo( 187.25, 92.5 );
		shape354.lineTo( 183.875, 94.125 );
		shape354.closePath();
		g.fill( shape354 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_0_37( Graphics2D g ) {
		GeneralPath shape355 = new GeneralPath();
		shape355.moveTo( 175.25, 87.125 );
		shape355.lineTo( 180.875, 82.25 );
		shape355.lineTo( 185.0, 84.125 );
		shape355.lineTo( 191.0, 77.125 );
		shape355.lineTo( 184.75, 85.5 );
		shape355.lineTo( 180.625, 83.25 );
		shape355.lineTo( 174.5, 87.25 );
		shape355.lineTo( 175.25, 87.125 );
		shape355.closePath();
		g.fill( shape355 );
	}

	private void paintCompositeGraphicsNode_0_0_0_0_0_22_1_0( Graphics2D g ) {
		// _0_0_0_0_0_22_1_0_0
		AffineTransform trans_0_0_0_0_0_22_1_0_0 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_0_0( g );
		g.setTransform( trans_0_0_0_0_0_22_1_0_0 );
		// _0_0_0_0_0_22_1_0_1
		AffineTransform trans_0_0_0_0_0_22_1_0_1 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_0_1( g );
		g.setTransform( trans_0_0_0_0_0_22_1_0_1 );
		// _0_0_0_0_0_22_1_0_2
		AffineTransform trans_0_0_0_0_0_22_1_0_2 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_0_2( g );
		g.setTransform( trans_0_0_0_0_0_22_1_0_2 );
		// _0_0_0_0_0_22_1_0_3
		AffineTransform trans_0_0_0_0_0_22_1_0_3 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_0_3( g );
		g.setTransform( trans_0_0_0_0_0_22_1_0_3 );
		// _0_0_0_0_0_22_1_0_4
		AffineTransform trans_0_0_0_0_0_22_1_0_4 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_0_4( g );
		g.setTransform( trans_0_0_0_0_0_22_1_0_4 );
		// _0_0_0_0_0_22_1_0_5
		AffineTransform trans_0_0_0_0_0_22_1_0_5 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_0_5( g );
		g.setTransform( trans_0_0_0_0_0_22_1_0_5 );
		// _0_0_0_0_0_22_1_0_6
		AffineTransform trans_0_0_0_0_0_22_1_0_6 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_0_6( g );
		g.setTransform( trans_0_0_0_0_0_22_1_0_6 );
		// _0_0_0_0_0_22_1_0_7
		AffineTransform trans_0_0_0_0_0_22_1_0_7 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_0_7( g );
		g.setTransform( trans_0_0_0_0_0_22_1_0_7 );
		// _0_0_0_0_0_22_1_0_8
		AffineTransform trans_0_0_0_0_0_22_1_0_8 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_0_8( g );
		g.setTransform( trans_0_0_0_0_0_22_1_0_8 );
		// _0_0_0_0_0_22_1_0_9
		AffineTransform trans_0_0_0_0_0_22_1_0_9 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_0_9( g );
		g.setTransform( trans_0_0_0_0_0_22_1_0_9 );
		// _0_0_0_0_0_22_1_0_10
		AffineTransform trans_0_0_0_0_0_22_1_0_10 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_0_10( g );
		g.setTransform( trans_0_0_0_0_0_22_1_0_10 );
		// _0_0_0_0_0_22_1_0_11
		g.setComposite( AlphaComposite.getInstance( 3, 0.6456311f * origAlpha ) );
		AffineTransform trans_0_0_0_0_0_22_1_0_11 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_0_11( g );
		g.setTransform( trans_0_0_0_0_0_22_1_0_11 );
		// _0_0_0_0_0_22_1_0_12
		AffineTransform trans_0_0_0_0_0_22_1_0_12 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_0_12( g );
		g.setTransform( trans_0_0_0_0_0_22_1_0_12 );
		// _0_0_0_0_0_22_1_0_13
		AffineTransform trans_0_0_0_0_0_22_1_0_13 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_0_13( g );
		g.setTransform( trans_0_0_0_0_0_22_1_0_13 );
		// _0_0_0_0_0_22_1_0_14
		AffineTransform trans_0_0_0_0_0_22_1_0_14 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_0_14( g );
		g.setTransform( trans_0_0_0_0_0_22_1_0_14 );
		// _0_0_0_0_0_22_1_0_15
		AffineTransform trans_0_0_0_0_0_22_1_0_15 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_0_15( g );
		g.setTransform( trans_0_0_0_0_0_22_1_0_15 );
		// _0_0_0_0_0_22_1_0_16
		AffineTransform trans_0_0_0_0_0_22_1_0_16 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_0_16( g );
		g.setTransform( trans_0_0_0_0_0_22_1_0_16 );
		// _0_0_0_0_0_22_1_0_17
		AffineTransform trans_0_0_0_0_0_22_1_0_17 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_0_17( g );
		g.setTransform( trans_0_0_0_0_0_22_1_0_17 );
		// _0_0_0_0_0_22_1_0_18
		AffineTransform trans_0_0_0_0_0_22_1_0_18 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_0_18( g );
		g.setTransform( trans_0_0_0_0_0_22_1_0_18 );
		// _0_0_0_0_0_22_1_0_19
		AffineTransform trans_0_0_0_0_0_22_1_0_19 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_0_19( g );
		g.setTransform( trans_0_0_0_0_0_22_1_0_19 );
		// _0_0_0_0_0_22_1_0_20
		g.setComposite( AlphaComposite.getInstance( 3, 1.0f * origAlpha ) );
		AffineTransform trans_0_0_0_0_0_22_1_0_20 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_0_20( g );
		g.setTransform( trans_0_0_0_0_0_22_1_0_20 );
		// _0_0_0_0_0_22_1_0_21
		g.setComposite( AlphaComposite.getInstance( 3, 0.6456311f * origAlpha ) );
		AffineTransform trans_0_0_0_0_0_22_1_0_21 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_0_21( g );
		g.setTransform( trans_0_0_0_0_0_22_1_0_21 );
		// _0_0_0_0_0_22_1_0_22
		AffineTransform trans_0_0_0_0_0_22_1_0_22 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_0_22( g );
		g.setTransform( trans_0_0_0_0_0_22_1_0_22 );
		// _0_0_0_0_0_22_1_0_23
		AffineTransform trans_0_0_0_0_0_22_1_0_23 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_0_23( g );
		g.setTransform( trans_0_0_0_0_0_22_1_0_23 );
		// _0_0_0_0_0_22_1_0_24
		AffineTransform trans_0_0_0_0_0_22_1_0_24 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_0_24( g );
		g.setTransform( trans_0_0_0_0_0_22_1_0_24 );
		// _0_0_0_0_0_22_1_0_25
		AffineTransform trans_0_0_0_0_0_22_1_0_25 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_0_25( g );
		g.setTransform( trans_0_0_0_0_0_22_1_0_25 );
		// _0_0_0_0_0_22_1_0_26
		AffineTransform trans_0_0_0_0_0_22_1_0_26 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_0_26( g );
		g.setTransform( trans_0_0_0_0_0_22_1_0_26 );
		// _0_0_0_0_0_22_1_0_27
		AffineTransform trans_0_0_0_0_0_22_1_0_27 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_0_27( g );
		g.setTransform( trans_0_0_0_0_0_22_1_0_27 );
		// _0_0_0_0_0_22_1_0_28
		AffineTransform trans_0_0_0_0_0_22_1_0_28 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_0_28( g );
		g.setTransform( trans_0_0_0_0_0_22_1_0_28 );
		// _0_0_0_0_0_22_1_0_29
		AffineTransform trans_0_0_0_0_0_22_1_0_29 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_0_29( g );
		g.setTransform( trans_0_0_0_0_0_22_1_0_29 );
		// _0_0_0_0_0_22_1_0_30
		AffineTransform trans_0_0_0_0_0_22_1_0_30 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_0_30( g );
		g.setTransform( trans_0_0_0_0_0_22_1_0_30 );
		// _0_0_0_0_0_22_1_0_31
		AffineTransform trans_0_0_0_0_0_22_1_0_31 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_0_31( g );
		g.setTransform( trans_0_0_0_0_0_22_1_0_31 );
		// _0_0_0_0_0_22_1_0_32
		AffineTransform trans_0_0_0_0_0_22_1_0_32 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_0_32( g );
		g.setTransform( trans_0_0_0_0_0_22_1_0_32 );
		// _0_0_0_0_0_22_1_0_33
		AffineTransform trans_0_0_0_0_0_22_1_0_33 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_0_33( g );
		g.setTransform( trans_0_0_0_0_0_22_1_0_33 );
		// _0_0_0_0_0_22_1_0_34
		g.setComposite( AlphaComposite.getInstance( 3, 1.0f * origAlpha ) );
		AffineTransform trans_0_0_0_0_0_22_1_0_34 = g.getTransform();
		g.transform( new AffineTransform( -1.2044939994812012f, 0.0f, 0.0f, 1.2044939994812012f, 34.70191955566406f, -23.509807586669922f ) );
		paintShapeNode_0_0_0_0_0_22_1_0_34( g );
		g.setTransform( trans_0_0_0_0_0_22_1_0_34 );
		// _0_0_0_0_0_22_1_0_35
		AffineTransform trans_0_0_0_0_0_22_1_0_35 = g.getTransform();
		g.transform( new AffineTransform( -1.2044939994812012f, 0.0f, 0.0f, 1.2044939994812012f, 34.70191955566406f, -23.509807586669922f ) );
		paintShapeNode_0_0_0_0_0_22_1_0_35( g );
		g.setTransform( trans_0_0_0_0_0_22_1_0_35 );
		// _0_0_0_0_0_22_1_0_36
		AffineTransform trans_0_0_0_0_0_22_1_0_36 = g.getTransform();
		g.transform( new AffineTransform( -1.2044939994812012f, 0.0f, 0.0f, 1.2044939994812012f, 34.70191955566406f, -23.509807586669922f ) );
		paintShapeNode_0_0_0_0_0_22_1_0_36( g );
		g.setTransform( trans_0_0_0_0_0_22_1_0_36 );
		// _0_0_0_0_0_22_1_0_37
		AffineTransform trans_0_0_0_0_0_22_1_0_37 = g.getTransform();
		g.transform( new AffineTransform( -1.2044939994812012f, 0.0f, 0.0f, 1.2044939994812012f, 34.70191955566406f, -23.509807586669922f ) );
		paintShapeNode_0_0_0_0_0_22_1_0_37( g );
		g.setTransform( trans_0_0_0_0_0_22_1_0_37 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_0( Graphics2D g ) {
		GeneralPath shape356 = new GeneralPath();
		shape356.moveTo( 173.96375, 70.628624 );
		shape356.curveTo( 174.06827, 70.210495, 172.39577, 68.747055, 173.85922, 67.910805 );
		shape356.curveTo( 175.32266, 67.074554, 179.3994, 63.625008, 179.3994, 63.625008 );
		shape356.lineTo( 183.16254, 64.04314 );
		shape356.lineTo( 188.70271, 68.95612 );
		shape356.lineTo( 198.00603, 75.85521 );
		shape356.lineTo( 208.87732, 75.54162 );
		shape356.lineTo( 210.1317, 81.395386 );
		shape356.lineTo( 208.45918, 82.649765 );
		shape356.lineTo( 210.44528, 85.472115 );
		shape356.lineTo( 216.50812, 89.23525 );
		shape356.lineTo( 220.16672, 94.77543 );
		shape356.lineTo( 221.4211, 93.62558 );
		shape356.lineTo( 224.34799, 99.16576 );
		shape356.lineTo( 220.89844, 104.70593 );
		shape356.lineTo( 153.89368, 102.301704 );
		shape356.lineTo( 153.5801, 96.134346 );
		shape356.lineTo( 145.94928, 85.47212 );
		shape356.lineTo( 162.88339, 77.632256 );
		shape356.lineTo( 168.00545, 70.52411 );
		shape356.lineTo( 167.90092, 77.109604 );
		shape356.lineTo( 173.96376, 70.62865 );
		shape356.lineTo( 173.96376, 70.62865 );
		shape356.lineTo( 173.96376, 70.62865 );
		shape356.closePath();
		g.setPaint( new LinearGradientPaint( new Point2D.Double( -190.5, 104.1875 ), new Point2D.Double( -96.75, 104.1875 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 237, 244, 252, 255 ), new Color( 192, 198, 214, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 0.8362528085708618f, 0.0f, 0.0f, 0.8362528085708618f, 305.25543212890625f, -2.9616212844848633f ) ) );
		g.fill( shape356 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_1( Graphics2D g ) {
		GeneralPath shape357 = new GeneralPath();
		shape357.moveTo( 168.42357, 77.52771 );
		shape357.lineTo( 175.63625, 68.32893 );
		shape357.curveTo( 175.63625, 68.32893, 182.1172, 74.70536, 182.53534, 74.80989 );
		shape357.curveTo( 182.95348, 74.91442, 189.74802, 75.85521, 189.74802, 75.85521 );
		shape357.lineTo( 182.4308, 80.4546 );
		shape357.lineTo( 179.71298, 80.35007 );
		shape357.lineTo( 173.65015, 87.14462 );
		shape357.lineTo( 168.42357, 77.52772 );
		shape357.lineTo( 168.42357, 77.52772 );
		shape357.lineTo( 168.42357, 77.52772 );
		shape357.closePath();
		g.setPaint( new Color( 252, 253, 255, 255 ) );
		g.fill( shape357 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_2( Graphics2D g ) {
		GeneralPath shape358 = new GeneralPath();
		shape358.moveTo( 120.21262, 87.751434 );
		shape358.lineTo( 125.96185, 93.9188 );
		shape358.lineTo( 139.65549, 96.74115 );
		shape358.lineTo( 146.55458, 89.52847 );
		shape358.lineTo( 139.23737, 83.15205 );
		shape358.lineTo( 131.60655, 82.942986 );
		shape358.lineTo( 122.72136, 87.9605 );
		shape358.lineTo( 120.8398, 87.22878 );
		shape358.lineTo( 120.21261, 87.75144 );
		shape358.lineTo( 120.21261, 87.75144 );
		shape358.lineTo( 120.21261, 87.75144 );
		shape358.closePath();
		g.setPaint( new LinearGradientPaint( new Point2D.Double( -228.375, 89.875 ), new Point2D.Double( -196.875, 89.875 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 189, 208, 225, 255 ), new Color( 255, 255, 255, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 0.8362528085708618f, 0.0f, 0.0f, 0.8362528085708618f, 311.19183349609375f, 14.683847427368164f ) ) );
		g.fill( shape358 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_3( Graphics2D g ) {
		GeneralPath shape359 = new GeneralPath();
		shape359.moveTo( 120.10808, 87.960495 );
		shape359.lineTo( 122.72137, 89.110344 );
		shape359.lineTo( 133.17453, 84.09283 );
		shape359.lineTo( 135.3697, 85.6608 );
		shape359.lineTo( 139.55096, 83.25657 );
		shape359.lineTo( 131.60657, 83.15204 );
		shape359.lineTo( 122.616844, 87.960495 );
		shape359.lineTo( 120.630745, 87.228775 );
		shape359.lineTo( 120.108086, 87.960495 );
		shape359.lineTo( 120.108086, 87.960495 );
		shape359.lineTo( 120.108086, 87.960495 );
		shape359.closePath();
		g.setPaint( new Color( 255, 255, 255, 255 ) );
		g.fill( shape359 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_4( Graphics2D g ) {
		GeneralPath shape360 = new GeneralPath();
		shape360.moveTo( 147.41272, 85.68117 );
		shape360.lineTo( 164.76497, 76.16879 );
		shape360.lineTo( 166.75107, 70.210495 );
		shape360.lineTo( 167.69185, 71.98753 );
		shape360.lineTo( 166.33293, 77.31864 );
		shape360.lineTo( 168.10997, 77.10958 );
		shape360.lineTo( 173.23203, 71.569405 );
		shape360.lineTo( 167.48279, 78.67755 );
		shape360.lineTo( 154.31181, 86.20383 );
		shape360.lineTo( 147.41272, 85.68117 );
		shape360.lineTo( 147.41272, 85.68117 );
		shape360.lineTo( 147.41272, 85.68117 );
		shape360.closePath();
		g.fill( shape360 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_5( Graphics2D g ) {
		GeneralPath shape361 = new GeneralPath();
		shape361.moveTo( 155.46165, 88.18993 );
		shape361.lineTo( 160.27011, 98.6431 );
		shape361.lineTo( 168.8417, 87.87634 );
		shape361.lineTo( 174.06828, 88.92166 );
		shape361.lineTo( 172.8139, 85.263054 );
		shape361.lineTo( 166.64655, 84.113205 );
		shape361.lineTo( 155.46167, 88.18994 );
		shape361.lineTo( 155.46167, 88.18994 );
		shape361.lineTo( 155.46167, 88.18994 );
		shape361.closePath();
		g.setPaint( new Color( 185, 200, 219, 255 ) );
		g.fill( shape361 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_6( Graphics2D g ) {
		GeneralPath shape362 = new GeneralPath();
		shape362.moveTo( 174.38187, 68.43346 );
		shape362.curveTo( 174.38187, 69.060646, 174.90453, 83.7996, 174.90453, 83.7996 );
		shape362.lineTo( 173.02295, 82.12709 );
		shape362.lineTo( 173.75468, 69.26971 );
		shape362.lineTo( 174.38187, 68.43346 );
		shape362.closePath();
		g.setPaint( new Color( 180, 198, 218, 255 ) );
		g.fill( shape362 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_7( Graphics2D g ) {
		GeneralPath shape363 = new GeneralPath();
		shape363.moveTo( 147.09914, 86.517426 );
		shape363.lineTo( 153.37103, 97.07512 );
		shape363.lineTo( 157.86589, 94.56636 );
		shape363.lineTo( 160.58371, 98.3295 );
		shape363.lineTo( 156.40245, 88.398994 );
		shape363.lineTo( 166.64655, 84.63586 );
		shape363.lineTo( 173.65016, 86.831024 );
		shape363.lineTo( 177.30878, 82.963356 );
		shape363.lineTo( 173.23204, 80.76819 );
		shape363.lineTo( 166.01936, 83.38148 );
		shape363.lineTo( 160.16559, 83.59054 );
		shape363.lineTo( 154.20729, 87.771805 );
		shape363.lineTo( 147.09914, 86.517426 );
		shape363.closePath();
		g.setPaint( new Color( 177, 186, 203, 255 ) );
		g.fill( shape363 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_8( Graphics2D g ) {
		GeneralPath shape364 = new GeneralPath();
		shape364.moveTo( 170.09608, 77.31864 );
		shape364.lineTo( 176.68158, 82.1271 );
		shape364.lineTo( 173.23204, 86.308365 );
		shape364.lineTo( 169.05077, 78.57303 );
		shape364.lineTo( 170.09608, 77.31865 );
		shape364.lineTo( 170.09608, 77.31865 );
		shape364.lineTo( 170.09608, 77.31865 );
		shape364.closePath();
		g.setPaint( new Color( 217, 225, 236, 255 ) );
		g.fill( shape364 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_9( Graphics2D g ) {
		GeneralPath shape365 = new GeneralPath();
		shape365.moveTo( 175.771, 71.569405 );
		shape365.lineTo( 176.26099, 87.249146 );
		shape365.lineTo( 179.69093, 81.124245 );
		shape365.lineTo( 184.71335, 80.144264 );
		shape365.lineTo( 189.61327, 77.08182 );
		shape365.lineTo( 175.771, 71.56941 );
		shape365.lineTo( 175.771, 71.56941 );
		shape365.lineTo( 175.771, 71.56941 );
		shape365.closePath();
		g.setPaint( new Color( 195, 215, 255, 255 ) );
		g.fill( shape365 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_10( Graphics2D g ) {
		GeneralPath shape366 = new GeneralPath();
		shape366.moveTo( 175.84532, 71.98753 );
		shape366.lineTo( 176.26344, 85.36758 );
		shape366.lineTo( 179.19034, 80.141 );
		shape366.lineTo( 183.47614, 79.30475 );
		shape366.lineTo( 187.65741, 76.69146 );
		shape366.lineTo( 175.84534, 71.98754 );
		shape366.lineTo( 175.84534, 71.98754 );
		shape366.lineTo( 175.84534, 71.98754 );
		shape366.closePath();
		g.setPaint( new Color( 252, 253, 255, 255 ) );
		g.fill( shape366 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_11( Graphics2D g ) {
		GeneralPath shape367 = new GeneralPath();
		shape367.moveTo( 191.52505, 94.35729 );
		shape367.lineTo( 187.65738, 97.17964 );
		shape367.lineTo( 190.16614, 96.65698 );
		shape367.lineTo( 191.52505, 94.357285 );
		shape367.closePath();
		g.setPaint( new Color( 192, 198, 214, 255 ) );
		g.fill( shape367 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_12( Graphics2D g ) {
		GeneralPath shape368 = new GeneralPath();
		shape368.moveTo( 187.76192, 91.84853 );
		shape368.lineTo( 192.46584, 95.29808 );
		shape368.lineTo( 191.21146, 97.07512 );
		shape368.lineTo( 195.49725, 99.37482 );
		shape368.lineTo( 186.50754, 98.85216 );
		shape368.lineTo( 184.62596, 93.73011 );
		shape368.lineTo( 187.76192, 91.84854 );
		shape368.lineTo( 187.76192, 91.84854 );
		shape368.lineTo( 187.76192, 91.84854 );
		shape368.closePath();
		g.fill( shape368 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_13( Graphics2D g ) {
		GeneralPath shape369 = new GeneralPath();
		shape369.moveTo( 184.41692, 91.63947 );
		shape369.curveTo( 185.04411, 91.43041, 190.06161, 88.92165, 190.06161, 88.92165 );
		shape369.lineTo( 192.88397, 91.84854 );
		shape369.lineTo( 189.6435, 91.01229 );
		shape369.lineTo( 184.41692, 91.63948 );
		shape369.closePath();
		g.fill( shape369 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_14( Graphics2D g ) {
		GeneralPath shape370 = new GeneralPath();
		shape370.moveTo( 191.0024, 76.90052 );
		shape370.lineTo( 192.6749, 91.32588 );
		shape370.lineTo( 178.4586, 92.78932 );
		shape370.lineTo( 175.74078, 87.353676 );
		shape370.lineTo( 181.0719, 82.85882 );
		shape370.lineTo( 184.83502, 83.48601 );
		shape370.lineTo( 190.58426, 77.73677 );
		shape370.lineTo( 191.0024, 76.90052 );
		shape370.lineTo( 191.0024, 76.90052 );
		shape370.lineTo( 191.0024, 76.90052 );
		shape370.closePath();
		g.fill( shape370 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_15( Graphics2D g ) {
		GeneralPath shape371 = new GeneralPath();
		shape371.moveTo( 176.57704, 85.68117 );
		shape371.lineTo( 179.92206, 81.60444 );
		shape371.lineTo( 191.10693, 76.48239 );
		shape371.lineTo( 192.46585, 84.1132 );
		shape371.lineTo( 196.54259, 86.308365 );
		shape371.lineTo( 195.18367, 87.35368 );
		shape371.lineTo( 192.04771, 85.472115 );
		shape371.lineTo( 190.89787, 76.58693 );
		shape371.lineTo( 176.57704, 85.681175 );
		shape371.lineTo( 176.57704, 85.681175 );
		shape371.lineTo( 176.57704, 85.681175 );
		shape371.closePath();
		g.fill( shape371 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_16( Graphics2D g ) {
		GeneralPath shape372 = new GeneralPath();
		shape372.moveTo( 192.04771, 76.37786 );
		shape372.lineTo( 197.16977, 79.61834 );
		shape372.lineTo( 194.66101, 82.54523 );
		shape372.lineTo( 192.04771, 76.37786 );
		shape372.lineTo( 192.04771, 76.37786 );
		shape372.lineTo( 192.04771, 76.37786 );
		shape372.closePath();
		g.fill( shape372 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_17( Graphics2D g ) {
		GeneralPath shape373 = new GeneralPath();
		shape373.moveTo( 198.21507, 76.48239 );
		shape373.curveTo( 198.21507, 76.48239, 195.07912, 85.158516, 195.28818, 84.63586 );
		shape373.curveTo( 195.49725, 84.1132, 199.99211, 76.79599, 199.99211, 76.79599 );
		shape373.lineTo( 207.51839, 76.1688 );
		shape373.lineTo( 198.21507, 76.4824 );
		shape373.lineTo( 198.21507, 76.4824 );
		shape373.lineTo( 198.21507, 76.4824 );
		shape373.closePath();
		g.fill( shape373 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_18( Graphics2D g ) {
		GeneralPath shape374 = new GeneralPath();
		shape374.moveTo( 198.6332, 88.29446 );
		shape374.lineTo( 211.59511, 87.14461 );
		shape374.lineTo( 211.17699, 98.95668 );
		shape374.lineTo( 203.33711, 91.11681 );
		shape374.lineTo( 201.14194, 93.31198 );
		shape374.lineTo( 202.39632, 97.17965 );
		shape374.lineTo( 200.20116, 96.13433 );
		shape374.lineTo( 200.41022, 91.43041 );
		shape374.lineTo( 198.63318, 88.294464 );
		shape374.lineTo( 198.63318, 88.294464 );
		shape374.lineTo( 198.63318, 88.294464 );
		shape374.closePath();
		g.fill( shape374 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_19( Graphics2D g ) {
		GeneralPath shape375 = new GeneralPath();
		shape375.moveTo( 212.11778, 102.51076 );
		shape375.curveTo( 212.01326, 101.9881, 213.4767, 96.76152, 213.4767, 96.76152 );
		shape375.lineTo( 214.73108, 100.21106 );
		shape375.lineTo( 217.65796, 98.43402 );
		shape375.lineTo( 221.63017, 93.20744 );
		shape375.lineTo( 217.86703, 103.9742 );
		shape375.lineTo( 212.1178, 102.51076 );
		shape375.closePath();
		g.fill( shape375 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_20( Graphics2D g ) {
		GeneralPath shape376 = new GeneralPath();
		shape376.moveTo( 210.02715, 103.24248 );
		shape376.curveTo( 209.60901, 102.92888, 207.20479, 100.31559, 207.20479, 100.31559 );
		shape376.lineTo( 201.14195, 100.94278 );
		shape376.lineTo( 202.29181, 103.45154 );
		shape376.lineTo( 210.02715, 103.24248 );
		shape376.closePath();
		g.fill( shape376 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_21( Graphics2D g ) {
		GeneralPath shape377 = new GeneralPath();
		shape377.moveTo( 162.67435, 102.30169 );
		shape377.lineTo( 170.20062, 96.86605 );
		shape377.lineTo( 167.4828, 101.6745 );
		shape377.lineTo( 162.67435, 102.30169 );
		shape377.lineTo( 162.67435, 102.30169 );
		shape377.lineTo( 162.67435, 102.30169 );
		shape377.closePath();
		g.fill( shape377 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_22( Graphics2D g ) {
		GeneralPath shape378 = new GeneralPath();
		shape378.moveTo( 167.37827, 101.88357 );
		shape378.lineTo( 170.51422, 96.55245 );
		shape378.lineTo( 170.09608, 101.9881 );
		shape378.lineTo( 167.37827, 101.88357 );
		shape378.lineTo( 167.37827, 101.88357 );
		shape378.lineTo( 167.37827, 101.88357 );
		shape378.closePath();
		g.fill( shape378 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_23( Graphics2D g ) {
		GeneralPath shape379 = new GeneralPath();
		shape379.moveTo( 162.15169, 102.09263 );
		shape379.lineTo( 169.05077, 96.34339 );
		shape379.lineTo( 170.20062, 90.071495 );
		shape379.lineTo( 171.76859, 93.31197 );
		shape379.lineTo( 170.20062, 96.97058 );
		shape379.lineTo( 162.15169, 102.09263 );
		shape379.lineTo( 162.15169, 102.09263 );
		shape379.lineTo( 162.15169, 102.09263 );
		shape379.closePath();
		g.fill( shape379 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_24( Graphics2D g ) {
		GeneralPath shape380 = new GeneralPath();
		shape380.moveTo( 174.59094, 96.23886 );
		shape380.lineTo( 176.57704, 101.46544 );
		shape380.lineTo( 177.30876, 96.44792 );
		shape380.lineTo( 174.59094, 96.23886 );
		shape380.closePath();
		g.fill( shape380 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_25( Graphics2D g ) {
		GeneralPath shape381 = new GeneralPath();
		shape381.moveTo( 147.51726, 91.53494 );
		shape381.lineTo( 150.026, 94.77542 );
		shape381.lineTo( 147.83084, 96.55246 );
		shape381.lineTo( 151.17586, 98.43403 );
		shape381.lineTo( 153.37102, 96.44793 );
		shape381.lineTo( 147.51726, 91.53494 );
		shape381.closePath();
		g.setPaint( new Color( 145, 159, 186, 255 ) );
		g.fill( shape381 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_26( Graphics2D g ) {
		GeneralPath shape382 = new GeneralPath();
		shape382.moveTo( 134.9968, 115.1073 );
		shape382.curveTo( 135.20587, 115.62996, 137.61009, 119.91575, 137.61009, 119.91575 );
		shape382.lineTo( 139.28261, 117.19793 );
		shape382.lineTo( 134.99681, 115.1073 );
		shape382.lineTo( 134.99681, 115.1073 );
		shape382.lineTo( 134.99681, 115.1073 );
		shape382.closePath();
		g.fill( shape382 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_27( Graphics2D g ) {
		GeneralPath shape383 = new GeneralPath();
		shape383.moveTo( 140.02164, 121.24839 );
		shape383.lineTo( 143.57571, 123.86168 );
		shape383.lineTo( 148.48871, 121.45745 );
		shape383.lineTo( 150.99747, 119.15776 );
		shape383.lineTo( 146.18901, 119.26229 );
		shape383.lineTo( 146.81621, 121.03933 );
		shape383.lineTo( 143.47119, 122.08464 );
		shape383.lineTo( 140.02165, 121.24839 );
		shape383.closePath();
		g.fill( shape383 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_28( Graphics2D g ) {
		GeneralPath shape384 = new GeneralPath();
		shape384.moveTo( 121.07076, 96.44792 );
		shape384.lineTo( 126.82, 102.19716 );
		shape384.lineTo( 123.26593, 102.19716 );
		shape384.lineTo( 120.8617, 98.32949 );
		shape384.lineTo( 118.45747, 99.37481 );
		shape384.lineTo( 121.07076, 96.44792 );
		shape384.lineTo( 121.07076, 96.44792 );
		shape384.lineTo( 121.07076, 96.44792 );
		shape384.closePath();
		g.fill( shape384 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_29( Graphics2D g ) {
		GeneralPath shape385 = new GeneralPath();
		shape385.moveTo( 104.31651, 111.44791 );
		shape385.curveTo( 104.31651, 111.44791, 110.65329, 120.213066, 110.0616, 120.00505 );
		shape385.curveTo( 109.46991, 119.79704, 106.27957, 118.78623, 106.27957, 118.78623 );
		shape385.lineTo( 104.31651, 111.447914 );
		shape385.lineTo( 104.31651, 111.447914 );
		shape385.lineTo( 104.31651, 111.447914 );
		shape385.closePath();
		g.fill( shape385 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_30( Graphics2D g ) {
		GeneralPath shape386 = new GeneralPath();
		shape386.moveTo( 104.93902, 117.87173 );
		shape386.curveTo( 104.93902, 117.87173, 98.831055, 111.292274, 98.87112, 110.863144 );
		shape386.curveTo( 98.91118, 110.43402, 98.552155, 106.097275, 98.552155, 106.097275 );
		shape386.lineTo( 97.711624, 111.00954 );
		shape386.lineTo( 100.925095, 116.6822 );
		shape386.lineTo( 104.939026, 117.87173 );
		shape386.lineTo( 104.939026, 117.87173 );
		shape386.lineTo( 104.939026, 117.87173 );
		shape386.closePath();
		g.setPaint( new Color( 95, 114, 151, 255 ) );
		g.fill( shape386 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_31( Graphics2D g ) {
		GeneralPath shape387 = new GeneralPath();
		shape387.moveTo( 101.34883, 117.05277 );
		shape387.lineTo( 97.50824, 115.37016 );
		shape387.lineTo( 94.846405, 110.33464 );
		shape387.lineTo( 97.73397, 105.58802 );
		shape387.lineTo( 97.38111, 111.00414 );
		shape387.lineTo( 101.34883, 117.05277 );
		shape387.closePath();
		g.setPaint( new Color( 145, 159, 186, 255 ) );
		g.fill( shape387 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_32( Graphics2D g ) {
		GeneralPath shape388 = new GeneralPath();
		shape388.moveTo( 83.73789, 106.40817 );
		shape388.lineTo( 93.49806, 107.0923 );
		shape388.lineTo( 100.61276, 99.14283 );
		shape388.lineTo( 105.146645, 102.10857 );
		shape388.lineTo( 104.509, 113.34556 );
		shape388.lineTo( 106.41945, 118.73083 );
		shape388.curveTo( 101.70743, 117.47436, 96.99578, 116.22088, 92.121124, 113.6633 );
		shape388.curveTo( 89.154816, 112.35405, 86.511604, 110.61399, 84.04428, 108.63943 );
		shape388.lineTo( 83.73789, 106.408165 );
		shape388.lineTo( 83.73789, 106.408165 );
		shape388.lineTo( 83.73789, 106.408165 );
		shape388.closePath();
		g.setPaint( new Color( 203, 209, 223, 255 ) );
		g.fill( shape388 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_33( Graphics2D g ) {
		GeneralPath shape389 = new GeneralPath();
		shape389.moveTo( 95.04286, 114.50343 );
		shape389.lineTo( 93.977356, 110.91555 );
		shape389.lineTo( 87.98573, 109.363144 );
		shape389.lineTo( 93.54051, 109.32144 );
		shape389.lineTo( 97.90192, 105.425385 );
		shape389.lineTo( 94.55055, 110.23055 );
		shape389.lineTo( 97.80408, 115.47409 );
		shape389.lineTo( 95.04286, 114.50335 );
		shape389.lineTo( 95.04286, 114.503426 );
		shape389.lineTo( 95.04286, 114.503426 );
		shape389.closePath();
		g.setPaint( new Color( 86, 103, 137, 255 ) );
		g.fill( shape389 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_34( Graphics2D g ) {
		GeneralPath shape390 = new GeneralPath();
		shape390.moveTo( 84.55039, 107.15817 );
		shape390.lineTo( 94.06056, 107.8423 );
		shape390.lineTo( 100.80026, 100.01783 );
		shape390.lineTo( 104.896645, 102.23357 );
		shape390.lineTo( 103.0715, 112.78306 );
		shape390.lineTo( 98.54446, 104.54333 );
		shape390.lineTo( 93.30863, 109.3508 );
		shape390.lineTo( 88.04429, 109.82693 );
		shape390.lineTo( 84.5504, 107.158165 );
		shape390.lineTo( 84.5504, 107.158165 );
		shape390.lineTo( 84.5504, 107.158165 );
		shape390.closePath();
		g.setPaint( new Color( 255, 255, 255, 255 ) );
		g.fill( shape390 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_35( Graphics2D g ) {
		GeneralPath shape391 = new GeneralPath();
		shape391.moveTo( 86.727615, 109.47485 );
		shape391.lineTo( 88.79545, 112.41789 );
		shape391.lineTo( 94.94424, 114.468765 );
		shape391.lineTo( 92.89798, 110.203674 );
		shape391.lineTo( 86.26921, 109.202896 );
		shape391.lineTo( 86.727615, 109.47485 );
		shape391.closePath();
		g.setPaint( new Color( 78, 93, 124, 255 ) );
		g.fill( shape391 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_36( Graphics2D g ) {
		GeneralPath shape392 = new GeneralPath();
		shape392.moveTo( 124.97888, 95.84946 );
		shape392.lineTo( 124.99748, 104.28094 );
		shape392.lineTo( 115.430756, 121.700615 );
		shape392.lineTo( 105.464905, 118.65114 );
		shape392.lineTo( 107.94792, 105.495346 );
		shape392.lineTo( 113.3752, 100.127144 );
		shape392.lineTo( 123.290436, 97.716774 );
		shape392.lineTo( 124.16377, 95.896614 );
		shape392.lineTo( 124.978836, 95.84946 );
		shape392.lineTo( 124.978874, 95.849464 );
		shape392.lineTo( 124.978874, 95.849464 );
		shape392.lineTo( 124.978874, 95.849464 );
		shape392.closePath();
		g.setPaint( new LinearGradientPaint( new Point2D.Double( -228.375, 89.875 ), new Point2D.Double( -200.9934539794922, 92.91219329833984 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 189, 208, 225, 255 ), new Color( 255, 255, 255, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( -0.610429584980011f, 0.5715718269348145f, 0.5715718269348145f, 0.610429584980011f, -64.36907196044922f, 173.04588317871094f ) ) );
		g.fill( shape392 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_37( Graphics2D g ) {
		GeneralPath shape393 = new GeneralPath();
		shape393.moveTo( 125.19808, 95.93062 );
		shape393.lineTo( 124.0764, 98.55612 );
		shape393.lineTo( 113.016594, 102.038185 );
		shape393.lineTo( 112.48592, 104.68311 );
		shape393.lineTo( 107.790504, 105.785995 );
		shape393.lineTo( 113.51814, 100.279755 );
		shape393.lineTo( 123.36679, 97.64533 );
		shape393.lineTo( 124.31643, 95.75372 );
		shape393.lineTo( 125.19808, 95.93062 );
		shape393.lineTo( 125.19808, 95.93062 );
		shape393.lineTo( 125.19808, 95.93062 );
		shape393.closePath();
		g.setPaint( new Color( 255, 255, 255, 255 ) );
		g.fill( shape393 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_38( Graphics2D g ) {
		GeneralPath shape394 = new GeneralPath();
		shape394.moveTo( 206.43462, 97.62862 );
		shape394.curveTo( 206.3301, 97.210495, 208.00258, 95.747055, 206.53914, 94.9108 );
		shape394.curveTo( 205.0757, 94.07455, 200.99896, 90.62501, 200.99896, 90.62501 );
		shape394.lineTo( 197.23582, 91.04313 );
		shape394.lineTo( 191.69565, 95.956116 );
		shape394.lineTo( 182.39233, 102.8552 );
		shape394.lineTo( 171.52106, 102.54161 );
		shape394.lineTo( 170.26668, 108.39538 );
		shape394.lineTo( 171.93918, 109.64975 );
		shape394.lineTo( 169.95308, 112.47211 );
		shape394.lineTo( 163.89024, 116.23524 );
		shape394.lineTo( 160.23164, 121.77542 );
		shape394.lineTo( 158.97726, 120.62557 );
		shape394.lineTo( 156.05037, 126.16576 );
		shape394.lineTo( 159.49991, 131.70593 );
		shape394.lineTo( 226.50467, 129.3017 );
		shape394.lineTo( 226.81825, 123.13434 );
		shape394.lineTo( 234.44907, 112.47212 );
		shape394.lineTo( 217.51495, 104.632256 );
		shape394.lineTo( 212.3929, 97.5241 );
		shape394.lineTo( 212.49742, 104.1096 );
		shape394.lineTo( 206.43459, 97.62863 );
		shape394.lineTo( 206.43459, 97.62863 );
		shape394.lineTo( 206.43459, 97.62863 );
		shape394.closePath();
		g.setPaint( new LinearGradientPaint( new Point2D.Double( -190.5, 104.1875 ), new Point2D.Double( -96.75, 104.1875 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 237, 244, 252, 255 ), new Color( 192, 198, 214, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( -0.8362528085708618f, 0.0f, 0.0f, 0.8362528085708618f, 75.14293670654297f, 24.038379669189453f ) ) );
		g.fill( shape394 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_39( Graphics2D g ) {
		GeneralPath shape395 = new GeneralPath();
		shape395.moveTo( 211.9748, 104.52771 );
		shape395.lineTo( 204.76212, 95.328926 );
		shape395.curveTo( 204.76212, 95.328926, 198.28116, 101.70535, 197.86304, 101.80988 );
		shape395.curveTo( 197.4449, 101.91442, 190.65036, 102.8552, 190.65036, 102.8552 );
		shape395.lineTo( 197.96758, 107.45459 );
		shape395.lineTo( 200.6854, 107.35006 );
		shape395.lineTo( 206.74823, 114.14461 );
		shape395.lineTo( 211.97481, 104.52771 );
		shape395.lineTo( 211.97481, 104.52771 );
		shape395.lineTo( 211.97481, 104.52771 );
		shape395.closePath();
		g.setPaint( new Color( 252, 253, 255, 255 ) );
		g.fill( shape395 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_40( Graphics2D g ) {
		GeneralPath shape396 = new GeneralPath();
		shape396.moveTo( 191.18575, 100.75143 );
		shape396.lineTo( 185.43651, 106.91879 );
		shape396.lineTo( 171.74287, 109.74114 );
		shape396.lineTo( 164.84378, 102.528465 );
		shape396.lineTo( 172.161, 96.15204 );
		shape396.lineTo( 179.79181, 95.94298 );
		shape396.lineTo( 188.67699, 100.96049 );
		shape396.lineTo( 190.55856, 100.22877 );
		shape396.lineTo( 191.18576, 100.75143 );
		shape396.lineTo( 191.18576, 100.75143 );
		shape396.lineTo( 191.18576, 100.75143 );
		shape396.closePath();
		g.setPaint( new LinearGradientPaint( new Point2D.Double( -228.375, 89.875 ), new Point2D.Double( -196.875, 89.875 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 189, 208, 225, 255 ), new Color( 255, 255, 255, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( -0.8362528085708618f, 0.0f, 0.0f, 0.8362528085708618f, 0.20653310418128967f, 27.683847427368164f ) ) );
		g.fill( shape396 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_41( Graphics2D g ) {
		GeneralPath shape397 = new GeneralPath();
		shape397.moveTo( 191.29028, 100.96049 );
		shape397.lineTo( 188.67699, 102.11034 );
		shape397.lineTo( 178.22383, 97.09282 );
		shape397.lineTo( 176.02867, 98.66079 );
		shape397.lineTo( 171.84741, 96.25656 );
		shape397.lineTo( 179.79181, 96.15203 );
		shape397.lineTo( 188.78152, 100.96048 );
		shape397.lineTo( 190.76764, 100.22876 );
		shape397.lineTo( 191.29028, 100.96048 );
		shape397.lineTo( 191.29028, 100.96048 );
		shape397.lineTo( 191.29028, 100.96048 );
		shape397.closePath();
		g.setPaint( new Color( 255, 255, 255, 255 ) );
		g.fill( shape397 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_42( Graphics2D g ) {
		GeneralPath shape398 = new GeneralPath();
		shape398.moveTo( 192.66634, 118.43117 );
		shape398.lineTo( 210.01857, 108.918785 );
		shape398.lineTo( 212.00467, 102.96049 );
		shape398.lineTo( 212.94547, 104.737526 );
		shape398.lineTo( 211.58655, 110.068634 );
		shape398.lineTo( 213.36357, 109.85957 );
		shape398.lineTo( 218.48563, 104.319405 );
		shape398.lineTo( 212.73639, 111.42754 );
		shape398.lineTo( 199.56541, 118.95383 );
		shape398.lineTo( 192.66634, 118.43117 );
		shape398.lineTo( 192.66634, 118.43117 );
		shape398.lineTo( 192.66634, 118.43117 );
		shape398.closePath();
		g.fill( shape398 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_43( Graphics2D g ) {
		GeneralPath shape399 = new GeneralPath();
		shape399.moveTo( 206.0165, 95.43346 );
		shape399.curveTo( 206.0165, 96.060646, 205.49385, 110.7996, 205.49385, 110.7996 );
		shape399.lineTo( 207.37541, 109.1271 );
		shape399.lineTo( 206.64369, 96.26971 );
		shape399.lineTo( 206.0165, 95.43346 );
		shape399.closePath();
		g.setPaint( new Color( 180, 198, 218, 255 ) );
		g.fill( shape399 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_44( Graphics2D g ) {
		GeneralPath shape400 = new GeneralPath();
		shape400.moveTo( 233.29924, 113.51742 );
		shape400.lineTo( 227.02734, 124.0751 );
		shape400.lineTo( 222.5325, 121.56635 );
		shape400.lineTo( 219.81467, 125.32948 );
		shape400.lineTo( 223.99594, 115.39899 );
		shape400.lineTo( 213.75185, 111.63585 );
		shape400.lineTo( 206.74823, 113.83102 );
		shape400.lineTo( 203.08963, 109.96335 );
		shape400.lineTo( 207.16637, 107.76819 );
		shape400.lineTo( 214.37904, 110.38148 );
		shape400.lineTo( 220.23282, 110.59054 );
		shape400.lineTo( 226.19112, 114.7718 );
		shape400.lineTo( 233.29927, 113.51742 );
		shape400.closePath();
		g.setPaint( new Color( 177, 186, 203, 255 ) );
		g.fill( shape400 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_45( Graphics2D g ) {
		GeneralPath shape401 = new GeneralPath();
		shape401.moveTo( 210.30229, 104.31864 );
		shape401.lineTo( 203.7168, 109.127106 );
		shape401.lineTo( 207.16634, 113.308365 );
		shape401.lineTo( 211.3476, 105.57303 );
		shape401.lineTo( 210.30229, 104.31865 );
		shape401.lineTo( 210.30229, 104.31865 );
		shape401.lineTo( 210.30229, 104.31865 );
		shape401.closePath();
		g.setPaint( new Color( 217, 225, 236, 255 ) );
		g.fill( shape401 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_46( Graphics2D g ) {
		GeneralPath shape402 = new GeneralPath();
		shape402.moveTo( 204.62737, 98.569405 );
		shape402.lineTo( 204.13736, 114.24914 );
		shape402.lineTo( 200.70741, 108.124245 );
		shape402.lineTo( 195.685, 107.14426 );
		shape402.lineTo( 190.78506, 104.08181 );
		shape402.lineTo( 204.62735, 98.569405 );
		shape402.lineTo( 204.62735, 98.569405 );
		shape402.lineTo( 204.62735, 98.569405 );
		shape402.closePath();
		g.setPaint( new Color( 195, 215, 255, 255 ) );
		g.fill( shape402 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_47( Graphics2D g ) {
		GeneralPath shape403 = new GeneralPath();
		shape403.moveTo( 192.74098, 98.98753 );
		shape403.lineTo( 193.15912, 112.367584 );
		shape403.lineTo( 196.086, 107.14101 );
		shape403.lineTo( 200.3718, 106.30475 );
		shape403.lineTo( 204.55305, 103.69146 );
		shape403.lineTo( 192.74098, 98.98754 );
		shape403.closePath();
		g.setPaint( new Color( 252, 253, 255, 255 ) );
		g.fill( shape403 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_48( Graphics2D g ) {
		GeneralPath shape404 = new GeneralPath();
		shape404.moveTo( 188.8733, 121.35729 );
		shape404.lineTo( 192.74098, 124.17965 );
		shape404.lineTo( 190.23222, 123.65699 );
		shape404.lineTo( 188.8733, 121.35729 );
		shape404.closePath();
		g.setPaint( new Color( 192, 198, 214, 255 ) );
		g.fill( shape404 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_49( Graphics2D g ) {
		GeneralPath shape405 = new GeneralPath();
		shape405.moveTo( 192.63644, 118.84853 );
		shape405.lineTo( 187.93253, 122.29808 );
		shape405.lineTo( 189.1869, 124.07511 );
		shape405.lineTo( 184.90111, 126.37481 );
		shape405.lineTo( 193.89082, 125.85215 );
		shape405.lineTo( 195.7724, 120.7301 );
		shape405.lineTo( 192.63644, 118.84853 );
		shape405.closePath();
		g.fill( shape405 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_50( Graphics2D g ) {
		GeneralPath shape406 = new GeneralPath();
		shape406.moveTo( 195.98146, 118.63947 );
		shape406.curveTo( 195.35426, 118.43041, 190.33675, 115.92165, 190.33675, 115.92165 );
		shape406.lineTo( 187.51439, 118.84853 );
		shape406.lineTo( 190.75487, 118.01228 );
		shape406.lineTo( 195.98145, 118.63947 );
		shape406.lineTo( 195.98145, 118.63947 );
		shape406.lineTo( 195.98145, 118.63947 );
		shape406.closePath();
		g.fill( shape406 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_51( Graphics2D g ) {
		GeneralPath shape407 = new GeneralPath();
		shape407.moveTo( 189.39597, 103.90052 );
		shape407.lineTo( 187.72345, 118.32588 );
		shape407.lineTo( 201.93974, 119.78932 );
		shape407.lineTo( 204.65756, 114.35368 );
		shape407.lineTo( 199.32645, 109.858826 );
		shape407.lineTo( 195.56331, 110.486015 );
		shape407.lineTo( 189.81407, 104.73678 );
		shape407.lineTo( 189.39594, 103.90053 );
		shape407.lineTo( 189.39594, 103.90053 );
		shape407.lineTo( 189.39594, 103.90053 );
		shape407.closePath();
		g.setPaint( new Color( 177, 186, 203, 255 ) );
		g.fill( shape407 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_52( Graphics2D g ) {
		GeneralPath shape408 = new GeneralPath();
		shape408.moveTo( 203.82133, 112.68117 );
		shape408.lineTo( 200.47632, 108.60444 );
		shape408.lineTo( 189.29144, 103.48239 );
		shape408.lineTo( 187.93253, 111.113205 );
		shape408.lineTo( 183.85579, 113.308365 );
		shape408.lineTo( 185.2147, 114.35368 );
		shape408.lineTo( 188.35065, 112.472115 );
		shape408.lineTo( 189.5005, 103.58692 );
		shape408.lineTo( 203.82133, 112.68117 );
		shape408.closePath();
		g.setPaint( new Color( 192, 198, 214, 255 ) );
		g.fill( shape408 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_53( Graphics2D g ) {
		GeneralPath shape409 = new GeneralPath();
		shape409.moveTo( 188.35065, 103.37786 );
		shape409.lineTo( 183.2286, 106.61834 );
		shape409.lineTo( 185.73735, 109.54522 );
		shape409.lineTo( 188.35065, 103.37786 );
		shape409.closePath();
		g.fill( shape409 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_54( Graphics2D g ) {
		GeneralPath shape410 = new GeneralPath();
		shape410.moveTo( 182.18329, 103.48239 );
		shape410.curveTo( 182.18329, 103.48239, 185.31924, 112.15851, 185.11017, 111.63585 );
		shape410.curveTo( 184.90111, 111.1132, 180.40625, 103.79599, 180.40625, 103.79599 );
		shape410.lineTo( 172.87997, 103.1688 );
		shape410.lineTo( 182.18329, 103.48239 );
		shape410.closePath();
		g.fill( shape410 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_55( Graphics2D g ) {
		GeneralPath shape411 = new GeneralPath();
		shape411.moveTo( 181.76517, 115.29446 );
		shape411.lineTo( 168.80325, 114.14461 );
		shape411.lineTo( 169.22137, 125.95668 );
		shape411.lineTo( 177.06125, 118.11681 );
		shape411.lineTo( 179.25642, 120.31198 );
		shape411.lineTo( 178.00204, 124.17965 );
		shape411.lineTo( 180.1972, 123.13433 );
		shape411.lineTo( 179.98814, 118.43041 );
		shape411.lineTo( 181.76517, 115.294464 );
		shape411.lineTo( 181.76517, 115.294464 );
		shape411.lineTo( 181.76517, 115.294464 );
		shape411.closePath();
		g.setPaint( new Color( 196, 202, 217, 255 ) );
		g.fill( shape411 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_56( Graphics2D g ) {
		GeneralPath shape412 = new GeneralPath();
		shape412.moveTo( 168.2806, 129.51076 );
		shape412.curveTo( 168.38512, 128.9881, 166.92168, 123.76152, 166.92168, 123.76152 );
		shape412.lineTo( 165.6673, 127.21106 );
		shape412.lineTo( 162.7404, 125.43402 );
		shape412.lineTo( 158.7682, 120.20745 );
		shape412.lineTo( 162.53134, 130.9742 );
		shape412.lineTo( 168.28058, 129.51076 );
		shape412.lineTo( 168.28058, 129.51076 );
		shape412.lineTo( 168.28058, 129.51076 );
		shape412.closePath();
		g.setPaint( new Color( 192, 198, 214, 255 ) );
		g.fill( shape412 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_57( Graphics2D g ) {
		GeneralPath shape413 = new GeneralPath();
		shape413.moveTo( 170.37122, 130.24248 );
		shape413.curveTo( 170.78935, 129.92888, 173.19357, 127.31559, 173.19357, 127.31559 );
		shape413.lineTo( 179.25641, 127.94278 );
		shape413.lineTo( 178.10655, 130.45154 );
		shape413.lineTo( 170.37122, 130.24248 );
		shape413.closePath();
		g.fill( shape413 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_58( Graphics2D g ) {
		GeneralPath shape414 = new GeneralPath();
		shape414.moveTo( 217.72403, 129.3017 );
		shape414.lineTo( 210.19775, 123.86606 );
		shape414.lineTo( 212.91557, 128.67451 );
		shape414.lineTo( 217.72403, 129.30171 );
		shape414.lineTo( 217.72403, 129.30171 );
		shape414.lineTo( 217.72403, 129.30171 );
		shape414.closePath();
		g.fill( shape414 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_59( Graphics2D g ) {
		GeneralPath shape415 = new GeneralPath();
		shape415.moveTo( 213.02011, 128.88358 );
		shape415.lineTo( 209.88416, 123.55247 );
		shape415.lineTo( 210.30229, 128.98811 );
		shape415.lineTo( 213.02011, 128.88359 );
		shape415.lineTo( 213.02011, 128.88359 );
		shape415.lineTo( 213.02011, 128.88359 );
		shape415.closePath();
		g.fill( shape415 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_60( Graphics2D g ) {
		GeneralPath shape416 = new GeneralPath();
		shape416.moveTo( 218.24669, 129.09264 );
		shape416.lineTo( 211.3476, 123.3434 );
		shape416.lineTo( 210.19775, 117.07151 );
		shape416.lineTo( 208.62978, 120.31199 );
		shape416.lineTo( 210.19775, 123.97059 );
		shape416.lineTo( 218.24669, 129.09264 );
		shape416.lineTo( 218.24669, 129.09264 );
		shape416.lineTo( 218.24669, 129.09264 );
		shape416.closePath();
		g.fill( shape416 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_61( Graphics2D g ) {
		GeneralPath shape417 = new GeneralPath();
		shape417.moveTo( 205.80743, 123.23886 );
		shape417.lineTo( 203.82133, 128.46544 );
		shape417.lineTo( 203.08961, 123.44792 );
		shape417.lineTo( 205.80743, 123.23886 );
		shape417.closePath();
		g.fill( shape417 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_62( Graphics2D g ) {
		GeneralPath shape418 = new GeneralPath();
		shape418.moveTo( 163.8811, 104.53494 );
		shape418.lineTo( 161.37234, 107.77542 );
		shape418.lineTo( 163.56752, 109.55246 );
		shape418.lineTo( 160.2225, 111.43402 );
		shape418.lineTo( 158.02733, 109.44792 );
		shape418.lineTo( 163.8811, 104.53494 );
		shape418.closePath();
		g.setPaint( new Color( 145, 159, 186, 255 ) );
		g.fill( shape418 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_63( Graphics2D g ) {
		GeneralPath shape419 = new GeneralPath();
		shape419.moveTo( 165.97174, 88.33254 );
		shape419.curveTo( 165.76268, 88.8552, 163.35844, 93.141, 163.35844, 93.141 );
		shape419.lineTo( 161.68594, 90.42318 );
		shape419.lineTo( 165.97174, 88.33255 );
		shape419.lineTo( 165.97174, 88.33255 );
		shape419.lineTo( 165.97174, 88.33255 );
		shape419.closePath();
		g.fill( shape419 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_64( Graphics2D g ) {
		GeneralPath shape420 = new GeneralPath();
		shape420.moveTo( 208.44688, 109.09863 );
		shape420.lineTo( 204.8928, 111.71192 );
		shape420.lineTo( 199.97983, 109.30769 );
		shape420.lineTo( 197.47107, 107.008 );
		shape420.lineTo( 202.27953, 107.11253 );
		shape420.lineTo( 201.65233, 108.88957 );
		shape420.lineTo( 204.99734, 109.93488 );
		shape420.lineTo( 208.44688, 109.09863 );
		shape420.closePath();
		g.fill( shape420 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_65( Graphics2D g ) {
		GeneralPath shape421 = new GeneralPath();
		shape421.moveTo( 167.8276, 104.44792 );
		shape421.lineTo( 162.07837, 110.197174 );
		shape421.lineTo( 165.63245, 110.197174 );
		shape421.lineTo( 168.03668, 106.32949 );
		shape421.lineTo( 170.44092, 107.37481 );
		shape421.lineTo( 167.82762, 104.44792 );
		shape421.closePath();
		g.fill( shape421 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_66( Graphics2D g ) {
		GeneralPath shape422 = new GeneralPath();
		shape422.moveTo( 184.58186, 119.44791 );
		shape422.curveTo( 184.58186, 119.44791, 178.24507, 128.21307, 178.83678, 128.00505 );
		shape422.curveTo( 179.42847, 127.79704, 182.6188, 126.78624, 182.6188, 126.78624 );
		shape422.lineTo( 184.58186, 119.44791 );
		shape422.lineTo( 184.58186, 119.44791 );
		shape422.lineTo( 184.58186, 119.44791 );
		shape422.closePath();
		g.fill( shape422 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_67( Graphics2D g ) {
		GeneralPath shape423 = new GeneralPath();
		shape423.moveTo( 183.95935, 125.87174 );
		shape423.curveTo( 183.95935, 125.87174, 190.0673, 119.29228, 190.02725, 118.86315 );
		shape423.curveTo( 189.98715, 118.43403, 190.3462, 114.09728, 190.3462, 114.09728 );
		shape423.lineTo( 191.18675, 119.009544 );
		shape423.lineTo( 187.97328, 124.682205 );
		shape423.lineTo( 183.95935, 125.87174 );
		shape423.lineTo( 183.95935, 125.87174 );
		shape423.lineTo( 183.95935, 125.87174 );
		shape423.closePath();
		g.fill( shape423 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_68( Graphics2D g ) {
		GeneralPath shape424 = new GeneralPath();
		shape424.moveTo( 187.54955, 125.05278 );
		shape424.lineTo( 191.39014, 123.37016 );
		shape424.lineTo( 194.05197, 118.33464 );
		shape424.lineTo( 191.1644, 113.58803 );
		shape424.lineTo( 191.51726, 119.004135 );
		shape424.lineTo( 187.54953, 125.05277 );
		shape424.lineTo( 187.54953, 125.05277 );
		shape424.lineTo( 187.54953, 125.05277 );
		shape424.closePath();
		g.fill( shape424 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_69( Graphics2D g ) {
		GeneralPath shape425 = new GeneralPath();
		shape425.moveTo( 193.85551, 122.50344 );
		shape425.lineTo( 194.92102, 118.91555 );
		shape425.lineTo( 200.91264, 117.363144 );
		shape425.lineTo( 195.35786, 117.32154 );
		shape425.lineTo( 190.99646, 113.42548 );
		shape425.lineTo( 194.34782, 118.230644 );
		shape425.lineTo( 191.0943, 123.47418 );
		shape425.lineTo( 193.85551, 122.50344 );
		shape425.lineTo( 193.85551, 122.50344 );
		shape425.lineTo( 193.85551, 122.50344 );
		shape425.closePath();
		g.fill( shape425 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_70( Graphics2D g ) {
		GeneralPath shape426 = new GeneralPath();
		shape426.moveTo( 204.34798, 115.15818 );
		shape426.lineTo( 194.8378, 115.84231 );
		shape426.lineTo( 188.0981, 108.01783 );
		shape426.lineTo( 184.00172, 110.23358 );
		shape426.lineTo( 185.82686, 120.783066 );
		shape426.lineTo( 190.3539, 112.543335 );
		shape426.lineTo( 195.58972, 117.350815 );
		shape426.lineTo( 200.85406, 117.826935 );
		shape426.lineTo( 204.34796, 115.15819 );
		shape426.closePath();
		g.setPaint( new Color( 255, 255, 255, 255 ) );
		g.fill( shape426 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_71( Graphics2D g ) {
		GeneralPath shape427 = new GeneralPath();
		shape427.moveTo( 202.17075, 117.47486 );
		shape427.lineTo( 200.10292, 120.41789 );
		shape427.lineTo( 193.95412, 122.468765 );
		shape427.lineTo( 196.00038, 118.203674 );
		shape427.lineTo( 202.62915, 117.202896 );
		shape427.lineTo( 202.17075, 117.47487 );
		shape427.lineTo( 202.17075, 117.47487 );
		shape427.lineTo( 202.17075, 117.47487 );
		shape427.closePath();
		g.setPaint( new Color( 223, 227, 235, 255 ) );
		g.fill( shape427 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_72( Graphics2D g ) {
		GeneralPath shape428 = new GeneralPath();
		shape428.moveTo( 163.9195, 103.84946 );
		shape428.lineTo( 163.90079, 112.28094 );
		shape428.lineTo( 171.96751, 123.700615 );
		shape428.lineTo( 181.93336, 123.15114 );
		shape428.lineTo( 180.95033, 113.495346 );
		shape428.lineTo( 175.52306, 108.12716 );
		shape428.lineTo( 165.6078, 105.71678 );
		shape428.lineTo( 164.73447, 103.89662 );
		shape428.lineTo( 163.91942, 103.84942 );
		shape428.lineTo( 163.91948, 103.84946 );
		shape428.lineTo( 163.91948, 103.84946 );
		shape428.closePath();
		g.setPaint( new LinearGradientPaint( new Point2D.Double( -228.375, 89.875 ), new Point2D.Double( -196.875, 89.875 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 189, 208, 225, 255 ), new Color( 255, 255, 255, 255 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 0.610429584980011f, 0.5715718269348145f, -0.5715718269348145f, 0.610429584980011f, 353.2674255371094f, 181.04588317871094f ) ) );
		g.fill( shape428 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_73( Graphics2D g ) {
		GeneralPath shape429 = new GeneralPath();
		shape429.moveTo( 163.70029, 103.93062 );
		shape429.lineTo( 164.82196, 106.55613 );
		shape429.lineTo( 175.88176, 110.03819 );
		shape429.lineTo( 176.41245, 112.683136 );
		shape429.lineTo( 181.10786, 113.786 );
		shape429.lineTo( 175.38022, 108.27976 );
		shape429.lineTo( 165.53157, 105.64534 );
		shape429.lineTo( 164.58192, 103.75373 );
		shape429.lineTo( 163.70029, 103.93062 );
		shape429.closePath();
		g.setPaint( new Color( 255, 255, 255, 255 ) );
		g.fill( shape429 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_74( Graphics2D g ) {
		GeneralPath shape430 = new GeneralPath();
		shape430.moveTo( 172.99998, 68.75 );
		shape430.lineTo( 179.74998, 63.5 );
		shape430.lineTo( 183.12498, 64.125 );
		shape430.lineTo( 197.74998, 76.0 );
		shape430.lineTo( 208.87498, 75.375 );
		shape430.lineTo( 209.87498, 81.5 );
		shape430.lineTo( 207.99998, 76.875 );
		shape430.lineTo( 197.37498, 77.0 );
		shape430.lineTo( 182.49998, 64.875 );
		shape430.lineTo( 179.49998, 64.625 );
		shape430.lineTo( 172.99998, 68.75 );
		shape430.closePath();
		g.fill( shape430 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_75( Graphics2D g ) {
		GeneralPath shape431 = new GeneralPath();
		shape431.moveTo( 198.875, 78.875 );
		shape431.lineTo( 194.25, 85.625 );
		shape431.lineTo( 200.0, 79.375 );
		shape431.lineTo( 198.875, 78.875 );
		shape431.closePath();
		g.fill( shape431 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_76( Graphics2D g ) {
		GeneralPath shape432 = new GeneralPath();
		shape432.moveTo( 190.875, 96.625 );
		shape432.lineTo( 197.625, 92.0 );
		shape432.lineTo( 200.0, 91.625 );
		shape432.lineTo( 206.875, 96.125 );
		shape432.lineTo( 200.625, 90.75 );
		shape432.lineTo( 196.5, 91.375 );
		shape432.lineTo( 190.875, 96.625 );
		shape432.closePath();
		g.fill( shape432 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_77( Graphics2D g ) {
		GeneralPath shape433 = new GeneralPath();
		shape433.moveTo( 198.25, 88.75 );
		shape433.curveTo( 198.75, 88.75, 210.75, 87.625, 210.75, 87.625 );
		shape433.curveTo( 210.75, 87.625, 211.0, 98.5, 211.0, 98.0 );
		shape433.curveTo( 211.0, 97.5, 210.0, 88.75, 210.0, 88.75 );
		shape433.lineTo( 198.25, 88.75 );
		shape433.closePath();
		g.fill( shape433 );
	}

	private void paintShapeNode_0_0_0_0_0_22_1_1_78( Graphics2D g ) {
		GeneralPath shape434 = new GeneralPath();
		shape434.moveTo( 175.625, 87.375 );
		shape434.lineTo( 180.375, 82.125 );
		shape434.lineTo( 183.25, 82.875 );
		shape434.lineTo( 190.375, 77.375 );
		shape434.lineTo( 184.5, 84.375 );
		shape434.lineTo( 180.625, 83.5 );
		shape434.lineTo( 175.625, 87.375 );
		shape434.closePath();
		g.fill( shape434 );
	}

	private void paintCompositeGraphicsNode_0_0_0_0_0_22_1_1( Graphics2D g ) {
		// _0_0_0_0_0_22_1_1_0
		AffineTransform trans_0_0_0_0_0_22_1_1_0 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_0( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_0 );
		// _0_0_0_0_0_22_1_1_1
		AffineTransform trans_0_0_0_0_0_22_1_1_1 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_1( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_1 );
		// _0_0_0_0_0_22_1_1_2
		AffineTransform trans_0_0_0_0_0_22_1_1_2 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_2( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_2 );
		// _0_0_0_0_0_22_1_1_3
		AffineTransform trans_0_0_0_0_0_22_1_1_3 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_3( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_3 );
		// _0_0_0_0_0_22_1_1_4
		AffineTransform trans_0_0_0_0_0_22_1_1_4 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_4( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_4 );
		// _0_0_0_0_0_22_1_1_5
		AffineTransform trans_0_0_0_0_0_22_1_1_5 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_5( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_5 );
		// _0_0_0_0_0_22_1_1_6
		AffineTransform trans_0_0_0_0_0_22_1_1_6 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_6( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_6 );
		// _0_0_0_0_0_22_1_1_7
		AffineTransform trans_0_0_0_0_0_22_1_1_7 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_7( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_7 );
		// _0_0_0_0_0_22_1_1_8
		AffineTransform trans_0_0_0_0_0_22_1_1_8 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_8( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_8 );
		// _0_0_0_0_0_22_1_1_9
		AffineTransform trans_0_0_0_0_0_22_1_1_9 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_9( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_9 );
		// _0_0_0_0_0_22_1_1_10
		AffineTransform trans_0_0_0_0_0_22_1_1_10 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_10( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_10 );
		// _0_0_0_0_0_22_1_1_11
		g.setComposite( AlphaComposite.getInstance( 3, 0.6456311f * origAlpha ) );
		AffineTransform trans_0_0_0_0_0_22_1_1_11 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_11( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_11 );
		// _0_0_0_0_0_22_1_1_12
		AffineTransform trans_0_0_0_0_0_22_1_1_12 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_12( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_12 );
		// _0_0_0_0_0_22_1_1_13
		AffineTransform trans_0_0_0_0_0_22_1_1_13 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_13( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_13 );
		// _0_0_0_0_0_22_1_1_14
		AffineTransform trans_0_0_0_0_0_22_1_1_14 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_14( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_14 );
		// _0_0_0_0_0_22_1_1_15
		AffineTransform trans_0_0_0_0_0_22_1_1_15 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_15( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_15 );
		// _0_0_0_0_0_22_1_1_16
		AffineTransform trans_0_0_0_0_0_22_1_1_16 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_16( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_16 );
		// _0_0_0_0_0_22_1_1_17
		AffineTransform trans_0_0_0_0_0_22_1_1_17 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_17( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_17 );
		// _0_0_0_0_0_22_1_1_18
		AffineTransform trans_0_0_0_0_0_22_1_1_18 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_18( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_18 );
		// _0_0_0_0_0_22_1_1_19
		AffineTransform trans_0_0_0_0_0_22_1_1_19 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_19( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_19 );
		// _0_0_0_0_0_22_1_1_20
		AffineTransform trans_0_0_0_0_0_22_1_1_20 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_20( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_20 );
		// _0_0_0_0_0_22_1_1_21
		AffineTransform trans_0_0_0_0_0_22_1_1_21 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_21( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_21 );
		// _0_0_0_0_0_22_1_1_22
		AffineTransform trans_0_0_0_0_0_22_1_1_22 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_22( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_22 );
		// _0_0_0_0_0_22_1_1_23
		AffineTransform trans_0_0_0_0_0_22_1_1_23 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_23( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_23 );
		// _0_0_0_0_0_22_1_1_24
		AffineTransform trans_0_0_0_0_0_22_1_1_24 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_24( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_24 );
		// _0_0_0_0_0_22_1_1_25
		AffineTransform trans_0_0_0_0_0_22_1_1_25 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_25( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_25 );
		// _0_0_0_0_0_22_1_1_26
		AffineTransform trans_0_0_0_0_0_22_1_1_26 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_26( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_26 );
		// _0_0_0_0_0_22_1_1_27
		AffineTransform trans_0_0_0_0_0_22_1_1_27 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_27( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_27 );
		// _0_0_0_0_0_22_1_1_28
		AffineTransform trans_0_0_0_0_0_22_1_1_28 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_28( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_28 );
		// _0_0_0_0_0_22_1_1_29
		AffineTransform trans_0_0_0_0_0_22_1_1_29 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_29( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_29 );
		// _0_0_0_0_0_22_1_1_30
		g.setComposite( AlphaComposite.getInstance( 3, 1.0f * origAlpha ) );
		AffineTransform trans_0_0_0_0_0_22_1_1_30 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_30( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_30 );
		// _0_0_0_0_0_22_1_1_31
		AffineTransform trans_0_0_0_0_0_22_1_1_31 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_31( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_31 );
		// _0_0_0_0_0_22_1_1_32
		AffineTransform trans_0_0_0_0_0_22_1_1_32 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_32( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_32 );
		// _0_0_0_0_0_22_1_1_33
		g.setComposite( AlphaComposite.getInstance( 3, 0.6456311f * origAlpha ) );
		AffineTransform trans_0_0_0_0_0_22_1_1_33 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_33( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_33 );
		// _0_0_0_0_0_22_1_1_34
		g.setComposite( AlphaComposite.getInstance( 3, 1.0f * origAlpha ) );
		AffineTransform trans_0_0_0_0_0_22_1_1_34 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_34( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_34 );
		// _0_0_0_0_0_22_1_1_35
		AffineTransform trans_0_0_0_0_0_22_1_1_35 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_35( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_35 );
		// _0_0_0_0_0_22_1_1_36
		AffineTransform trans_0_0_0_0_0_22_1_1_36 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_36( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_36 );
		// _0_0_0_0_0_22_1_1_37
		AffineTransform trans_0_0_0_0_0_22_1_1_37 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_37( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_37 );
		// _0_0_0_0_0_22_1_1_38
		AffineTransform trans_0_0_0_0_0_22_1_1_38 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_38( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_38 );
		// _0_0_0_0_0_22_1_1_39
		AffineTransform trans_0_0_0_0_0_22_1_1_39 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_39( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_39 );
		// _0_0_0_0_0_22_1_1_40
		AffineTransform trans_0_0_0_0_0_22_1_1_40 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_40( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_40 );
		// _0_0_0_0_0_22_1_1_41
		AffineTransform trans_0_0_0_0_0_22_1_1_41 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_41( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_41 );
		// _0_0_0_0_0_22_1_1_42
		AffineTransform trans_0_0_0_0_0_22_1_1_42 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_42( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_42 );
		// _0_0_0_0_0_22_1_1_43
		AffineTransform trans_0_0_0_0_0_22_1_1_43 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_43( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_43 );
		// _0_0_0_0_0_22_1_1_44
		AffineTransform trans_0_0_0_0_0_22_1_1_44 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_44( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_44 );
		// _0_0_0_0_0_22_1_1_45
		AffineTransform trans_0_0_0_0_0_22_1_1_45 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_45( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_45 );
		// _0_0_0_0_0_22_1_1_46
		AffineTransform trans_0_0_0_0_0_22_1_1_46 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_46( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_46 );
		// _0_0_0_0_0_22_1_1_47
		AffineTransform trans_0_0_0_0_0_22_1_1_47 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_47( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_47 );
		// _0_0_0_0_0_22_1_1_48
		g.setComposite( AlphaComposite.getInstance( 3, 0.6456311f * origAlpha ) );
		AffineTransform trans_0_0_0_0_0_22_1_1_48 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_48( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_48 );
		// _0_0_0_0_0_22_1_1_49
		AffineTransform trans_0_0_0_0_0_22_1_1_49 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_49( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_49 );
		// _0_0_0_0_0_22_1_1_50
		AffineTransform trans_0_0_0_0_0_22_1_1_50 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_50( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_50 );
		// _0_0_0_0_0_22_1_1_51
		g.setComposite( AlphaComposite.getInstance( 3, 1.0f * origAlpha ) );
		AffineTransform trans_0_0_0_0_0_22_1_1_51 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_51( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_51 );
		// _0_0_0_0_0_22_1_1_52
		g.setComposite( AlphaComposite.getInstance( 3, 0.6456311f * origAlpha ) );
		AffineTransform trans_0_0_0_0_0_22_1_1_52 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_52( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_52 );
		// _0_0_0_0_0_22_1_1_53
		AffineTransform trans_0_0_0_0_0_22_1_1_53 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_53( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_53 );
		// _0_0_0_0_0_22_1_1_54
		AffineTransform trans_0_0_0_0_0_22_1_1_54 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_54( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_54 );
		// _0_0_0_0_0_22_1_1_55
		g.setComposite( AlphaComposite.getInstance( 3, 1.0f * origAlpha ) );
		AffineTransform trans_0_0_0_0_0_22_1_1_55 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_55( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_55 );
		// _0_0_0_0_0_22_1_1_56
		g.setComposite( AlphaComposite.getInstance( 3, 0.6456311f * origAlpha ) );
		AffineTransform trans_0_0_0_0_0_22_1_1_56 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_56( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_56 );
		// _0_0_0_0_0_22_1_1_57
		AffineTransform trans_0_0_0_0_0_22_1_1_57 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_57( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_57 );
		// _0_0_0_0_0_22_1_1_58
		AffineTransform trans_0_0_0_0_0_22_1_1_58 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_58( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_58 );
		// _0_0_0_0_0_22_1_1_59
		AffineTransform trans_0_0_0_0_0_22_1_1_59 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_59( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_59 );
		// _0_0_0_0_0_22_1_1_60
		AffineTransform trans_0_0_0_0_0_22_1_1_60 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_60( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_60 );
		// _0_0_0_0_0_22_1_1_61
		AffineTransform trans_0_0_0_0_0_22_1_1_61 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_61( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_61 );
		// _0_0_0_0_0_22_1_1_62
		AffineTransform trans_0_0_0_0_0_22_1_1_62 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_62( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_62 );
		// _0_0_0_0_0_22_1_1_63
		AffineTransform trans_0_0_0_0_0_22_1_1_63 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_63( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_63 );
		// _0_0_0_0_0_22_1_1_64
		AffineTransform trans_0_0_0_0_0_22_1_1_64 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_64( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_64 );
		// _0_0_0_0_0_22_1_1_65
		AffineTransform trans_0_0_0_0_0_22_1_1_65 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_65( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_65 );
		// _0_0_0_0_0_22_1_1_66
		AffineTransform trans_0_0_0_0_0_22_1_1_66 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_66( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_66 );
		// _0_0_0_0_0_22_1_1_67
		AffineTransform trans_0_0_0_0_0_22_1_1_67 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_67( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_67 );
		// _0_0_0_0_0_22_1_1_68
		AffineTransform trans_0_0_0_0_0_22_1_1_68 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_68( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_68 );
		// _0_0_0_0_0_22_1_1_69
		AffineTransform trans_0_0_0_0_0_22_1_1_69 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_69( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_69 );
		// _0_0_0_0_0_22_1_1_70
		AffineTransform trans_0_0_0_0_0_22_1_1_70 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_70( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_70 );
		// _0_0_0_0_0_22_1_1_71
		AffineTransform trans_0_0_0_0_0_22_1_1_71 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_71( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_71 );
		// _0_0_0_0_0_22_1_1_72
		g.setComposite( AlphaComposite.getInstance( 3, 1.0f * origAlpha ) );
		AffineTransform trans_0_0_0_0_0_22_1_1_72 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_72( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_72 );
		// _0_0_0_0_0_22_1_1_73
		AffineTransform trans_0_0_0_0_0_22_1_1_73 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_73( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_73 );
		// _0_0_0_0_0_22_1_1_74
		AffineTransform trans_0_0_0_0_0_22_1_1_74 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_74( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_74 );
		// _0_0_0_0_0_22_1_1_75
		AffineTransform trans_0_0_0_0_0_22_1_1_75 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.5f, -0.5f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_75( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_75 );
		// _0_0_0_0_0_22_1_1_76
		AffineTransform trans_0_0_0_0_0_22_1_1_76 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.5f, -0.5f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_76( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_76 );
		// _0_0_0_0_0_22_1_1_77
		AffineTransform trans_0_0_0_0_0_22_1_1_77 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.5f, -0.5f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_77( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_77 );
		// _0_0_0_0_0_22_1_1_78
		AffineTransform trans_0_0_0_0_0_22_1_1_78 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.5f, -0.5f ) );
		paintShapeNode_0_0_0_0_0_22_1_1_78( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1_78 );
	}

	private void paintCompositeGraphicsNode_0_0_0_0_0_22_1( Graphics2D g ) {
		// _0_0_0_0_0_22_1_0
		AffineTransform trans_0_0_0_0_0_22_1_0 = g.getTransform();
		g.transform( new AffineTransform( -0.1904858946800232f, 0.0f, 0.0f, 0.18252180516719818f, 691.3810424804688f, 20.780651092529297f ) );
		paintCompositeGraphicsNode_0_0_0_0_0_22_1_0( g );
		g.setTransform( trans_0_0_0_0_0_22_1_0 );
		// _0_0_0_0_0_22_1_1
		AffineTransform trans_0_0_0_0_0_22_1_1 = g.getTransform();
		g.transform( new AffineTransform( 0.22943909466266632f, 0.0f, 0.0f, 0.21984639763832092f, 684.6561279296875f, 16.59952163696289f ) );
		paintCompositeGraphicsNode_0_0_0_0_0_22_1_1( g );
		g.setTransform( trans_0_0_0_0_0_22_1_1 );
	}

	private void paintCompositeGraphicsNode_0_0_0_0_0_22( Graphics2D g ) {
		// _0_0_0_0_0_22_0
		AffineTransform trans_0_0_0_0_0_22_0 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_22_0( g );
		g.setTransform( trans_0_0_0_0_0_22_0 );
		// _0_0_0_0_0_22_1
		AffineTransform trans_0_0_0_0_0_22_1 = g.getTransform();
		g.transform( new AffineTransform( 0.9844217896461487f, 0.0f, 0.0f, 0.9599136114120483f, 11.221179962158203f, 21.665260314941406f ) );
		paintCompositeGraphicsNode_0_0_0_0_0_22_1( g );
		g.setTransform( trans_0_0_0_0_0_22_1 );
	}

	private void paintShapeNode_0_0_0_0_0_23( Graphics2D g ) {
		GeneralPath shape435 = new GeneralPath();
		shape435.moveTo( 753.8125, 70.625 );
		shape435.lineTo( 755.46875, 86.4375 );
		shape435.curveTo( 755.4697, 86.45831, 755.4697, 86.47919, 755.46875, 86.5 );
		shape435.curveTo( 755.46875, 87.1672, 755.804, 87.841965, 756.5, 88.5 );
		shape435.curveTo( 757.196, 89.158035, 758.244, 89.785385, 759.5625, 90.3125 );
		shape435.curveTo( 762.1994, 91.36673, 765.9118, 92.03125, 770.0, 92.03125 );
		shape435.curveTo( 774.0882, 92.03125, 777.8006, 91.36673, 780.4375, 90.3125 );
		shape435.curveTo( 781.756, 89.785385, 782.804, 89.158035, 783.5, 88.5 );
		shape435.curveTo( 784.196, 87.841965, 784.53125, 87.1672, 784.53125, 86.5 );
		shape435.curveTo( 784.5303, 86.47919, 784.5303, 86.45831, 784.53125, 86.4375 );
		shape435.lineTo( 786.1875, 70.625 );
		shape435.lineTo( 753.8125, 70.625 );
		shape435.closePath();
		g.setPaint( new LinearGradientPaint( new Point2D.Double( 688.0, 92.03125 ), new Point2D.Double( 688.0, 73.0 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 238, 238, 236, 255 ), new Color( 186, 189, 182, 0 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 82.0f, 0.0f ) ) );
		g.fill( shape435 );
	}

	private void paintShapeNode_0_0_0_0_0_24_0( Graphics2D g ) {
		GeneralPath shape436 = new GeneralPath();
		shape436.moveTo( 762.5, 59.1875 );
		shape436.lineTo( 762.5, 61.5 );
		shape436.lineTo( 765.0, 85.5 );
		shape436.curveTo( 765.0, 88.81647, 771.72, 91.5, 780.0, 91.5 );
		shape436.curveTo( 788.28, 91.5, 795.0, 88.81647, 795.0, 85.5 );
		shape436.lineTo( 797.5, 61.5 );
		shape436.lineTo( 797.5, 59.1875 );
		shape436.curveTo( 797.1822, 62.150127, 789.4729, 64.53125, 780.0, 64.53125 );
		shape436.curveTo( 770.5271, 64.53125, 762.81775, 62.150127, 762.5, 59.1875 );
		shape436.closePath();
		g.setPaint( new RadialGradientPaint( new Point2D.Double( 767.9999389648438, 78.99209594726562 ), 17.5f, new Point2D.Double( 767.9999389648438, 78.99209594726562 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 255, 255, 255, 255 ), new Color( 255, 255, 255, 76 ) }, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform( 0.9001134037971497f, 0.0f, 0.0f, 3.4375f, 88.71292877197266f, -185.869140625f ) ) );
		g.fill( shape436 );
	}

	private void paintShapeNode_0_0_0_0_0_24_1( Graphics2D g ) {
		GeneralPath shape437 = new GeneralPath();
		shape437.moveTo( 751.25, 65.8125 );
		shape437.lineTo( 753.5, 86.5 );
		shape437.curveTo( 753.5, 89.81647, 759.72, 92.0, 768.0, 92.0 );
		shape437.curveTo( 776.28, 92.0, 782.625, 89.81647, 782.625, 86.5 );
		shape437.lineTo( 784.625, 65.8125 );
		shape437.curveTo( 776.68756, 70.67296, 759.64923, 70.800766, 751.25, 65.8125 );
		shape437.closePath();
	}

	private void paintCompositeGraphicsNode_0_0_0_0_0_24( Graphics2D g ) {
		// _0_0_0_0_0_24_0
		g.setComposite( AlphaComposite.getInstance( 3, 1.0f * origAlpha ) );
		AffineTransform trans_0_0_0_0_0_24_0 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_24_0( g );
		g.setTransform( trans_0_0_0_0_0_24_0 );
		// _0_0_0_0_0_24_1
		AffineTransform trans_0_0_0_0_0_24_1 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 12.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_24_1( g );
		g.setTransform( trans_0_0_0_0_0_24_1 );
	}

	private void paintCompositeGraphicsNode_0_0_0_0_0( Graphics2D g ) {
		// _0_0_0_0_0_0
		g.setComposite( AlphaComposite.getInstance( 3, 0.4f * origAlpha ) );
		AffineTransform trans_0_0_0_0_0_0 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, -8.0f, -1.0f ) );
		paintShapeNode_0_0_0_0_0_0( g );
		g.setTransform( trans_0_0_0_0_0_0 );
		// _0_0_0_0_0_1
		g.setComposite( AlphaComposite.getInstance( 3, 1.0f * origAlpha ) );
		AffineTransform trans_0_0_0_0_0_1 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 52.0f, 0.0f ) );
		paintCompositeGraphicsNode_0_0_0_0_0_1( g );
		g.setTransform( trans_0_0_0_0_0_1 );
		// _0_0_0_0_0_2
		g.setComposite( AlphaComposite.getInstance( 3, 1.0f * origAlpha ) );
		AffineTransform trans_0_0_0_0_0_2 = g.getTransform();
		g.transform( new AffineTransform( 1.3276629447937012f, 0.0f, 0.0f, 1.2711591720581055f, -179.52792358398438f, 13.388467788696289f ) );
		paintShapeNode_0_0_0_0_0_2( g );
		g.setTransform( trans_0_0_0_0_0_2 );
		// _0_0_0_0_0_3
		g.setComposite( AlphaComposite.getInstance( 3, 0.3f * origAlpha ) );
		AffineTransform trans_0_0_0_0_0_3 = g.getTransform();
		g.transform( new AffineTransform( 1.1362465620040894f, 0.0f, 0.0f, 1.0947370529174805f, -42.62933349609375f, 24.13157081604004f ) );
		paintShapeNode_0_0_0_0_0_3( g );
		g.setTransform( trans_0_0_0_0_0_3 );
		// _0_0_0_0_0_4
		g.setComposite( AlphaComposite.getInstance( 3, 0.8f * origAlpha ) );
		AffineTransform trans_0_0_0_0_0_4 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, -8.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_4( g );
		g.setTransform( trans_0_0_0_0_0_4 );
		// _0_0_0_0_0_5
		g.setComposite( AlphaComposite.getInstance( 3, 1.0f * origAlpha ) );
		AffineTransform trans_0_0_0_0_0_5 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 52.0f, -20.0f ) );
		paintCompositeGraphicsNode_0_0_0_0_0_5( g );
		g.setTransform( trans_0_0_0_0_0_5 );
		// _0_0_0_0_0_6
		AffineTransform trans_0_0_0_0_0_6 = g.getTransform();
		g.transform( new AffineTransform( 1.0212769508361816f, 0.0f, 0.0f, 0.7578948736190796f, 39.595458984375f, 41.5526237487793f ) );
		paintShapeNode_0_0_0_0_0_6( g );
		g.setTransform( trans_0_0_0_0_0_6 );
		// _0_0_0_0_0_7
		AffineTransform trans_0_0_0_0_0_7 = g.getTransform();
		g.transform( new AffineTransform( 0.20933790504932404f, 0.0f, 0.0f, 0.20933790504932404f, 737.9274291992188f, 39.00255584716797f ) );
		paintCompositeGraphicsNode_0_0_0_0_0_7( g );
		g.setTransform( trans_0_0_0_0_0_7 );
		// _0_0_0_0_0_8
		g.setComposite( AlphaComposite.getInstance( 3, 0.3f * origAlpha ) );
		AffineTransform trans_0_0_0_0_0_8 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 450.0f, 0.0f ) );
		paintCompositeGraphicsNode_0_0_0_0_0_8( g );
		g.setTransform( trans_0_0_0_0_0_8 );
		// _0_0_0_0_0_9
		AffineTransform trans_0_0_0_0_0_9 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, -10.0f, 0.0f ) );
		paintCompositeGraphicsNode_0_0_0_0_0_9( g );
		g.setTransform( trans_0_0_0_0_0_9 );
		// _0_0_0_0_0_10
		AffineTransform trans_0_0_0_0_0_10 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 82.0f, 0.0f ) );
		paintCompositeGraphicsNode_0_0_0_0_0_10( g );
		g.setTransform( trans_0_0_0_0_0_10 );
		// _0_0_0_0_0_11
		g.setComposite( AlphaComposite.getInstance( 3, 0.2f * origAlpha ) );
		AffineTransform trans_0_0_0_0_0_11 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_11( g );
		g.setTransform( trans_0_0_0_0_0_11 );
		// _0_0_0_0_0_12
		g.setComposite( AlphaComposite.getInstance( 3, 0.1f * origAlpha ) );
		AffineTransform trans_0_0_0_0_0_12 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_12( g );
		g.setTransform( trans_0_0_0_0_0_12 );
		// _0_0_0_0_0_13
		g.setComposite( AlphaComposite.getInstance( 3, 1.0f * origAlpha ) );
		AffineTransform trans_0_0_0_0_0_13 = g.getTransform();
		g.transform( new AffineTransform( 1.2255326509475708f, 0.0f, 0.0f, 1.0526312589645386f, -106.48562622070312f, -1.565775990486145f ) );
		paintShapeNode_0_0_0_0_0_13( g );
		g.setTransform( trans_0_0_0_0_0_13 );
		// _0_0_0_0_0_14
		AffineTransform trans_0_0_0_0_0_14 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_14( g );
		g.setTransform( trans_0_0_0_0_0_14 );
		// _0_0_0_0_0_15
		g.setComposite( AlphaComposite.getInstance( 3, 0.4f * origAlpha ) );
		AffineTransform trans_0_0_0_0_0_15 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_15( g );
		g.setTransform( trans_0_0_0_0_0_15 );
		// _0_0_0_0_0_16
		g.setComposite( AlphaComposite.getInstance( 3, 1.0f * origAlpha ) );
		AffineTransform trans_0_0_0_0_0_16 = g.getTransform();
		g.transform( new AffineTransform( 1.1932722330093384f, 0.0f, 0.0f, 0.9307202100753784f, -83.41336822509766f, 3.575089931488037f ) );
		paintShapeNode_0_0_0_0_0_16( g );
		g.setTransform( trans_0_0_0_0_0_16 );
		// _0_0_0_0_0_17
		AffineTransform trans_0_0_0_0_0_17 = g.getTransform();
		g.transform( new AffineTransform( 0.8536584973335266f, 0.0f, 0.0f, 0.8536584973335266f, 158.9737548828125f, 12.123395919799805f ) );
		paintShapeNode_0_0_0_0_0_17( g );
		g.setTransform( trans_0_0_0_0_0_17 );
		// _0_0_0_0_0_18
		AffineTransform trans_0_0_0_0_0_18 = g.getTransform();
		g.transform( new AffineTransform( 0.41044938564300537f, 0.0f, 0.0f, 0.41044938564300537f, 471.7131652832031f, 39.519256591796875f ) );
		paintShapeNode_0_0_0_0_0_18( g );
		g.setTransform( trans_0_0_0_0_0_18 );
		// _0_0_0_0_0_19
		AffineTransform trans_0_0_0_0_0_19 = g.getTransform();
		g.transform( new AffineTransform( 0.8536584973335266f, 0.0f, 0.0f, 0.8536584973335266f, 159.15052795410156f, 38.7005615234375f ) );
		paintShapeNode_0_0_0_0_0_19( g );
		g.setTransform( trans_0_0_0_0_0_19 );
		// _0_0_0_0_0_20
		AffineTransform trans_0_0_0_0_0_20 = g.getTransform();
		g.transform( new AffineTransform( 0.41044938564300537f, 0.0f, 0.0f, 0.41044938564300537f, 471.88995361328125f, 66.09642028808594f ) );
		paintShapeNode_0_0_0_0_0_20( g );
		g.setTransform( trans_0_0_0_0_0_20 );
		// _0_0_0_0_0_21
		AffineTransform trans_0_0_0_0_0_21 = g.getTransform();
		g.transform( new AffineTransform( 0.31288841366767883f, 0.0f, 0.0f, 0.31288841366767883f, 558.5305786132812f, 36.097084045410156f ) );
		paintShapeNode_0_0_0_0_0_21( g );
		g.setTransform( trans_0_0_0_0_0_21 );
		// _0_0_0_0_0_22
		AffineTransform trans_0_0_0_0_0_22 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 50.0f, 0.0f ) );
		paintCompositeGraphicsNode_0_0_0_0_0_22( g );
		g.setTransform( trans_0_0_0_0_0_22 );
		// _0_0_0_0_0_23
		g.setComposite( AlphaComposite.getInstance( 3, 0.7f * origAlpha ) );
		AffineTransform trans_0_0_0_0_0_23 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
		paintShapeNode_0_0_0_0_0_23( g );
		g.setTransform( trans_0_0_0_0_0_23 );
		// _0_0_0_0_0_24
		g.setComposite( AlphaComposite.getInstance( 3, 0.19902912f * origAlpha ) );
		AffineTransform trans_0_0_0_0_0_24 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, -9.9375f, 0.09375f ) );
		paintCompositeGraphicsNode_0_0_0_0_0_24( g );
		g.setTransform( trans_0_0_0_0_0_24 );
	}

	private void paintCompositeGraphicsNode_0_0_0_0( Graphics2D g ) {
		// _0_0_0_0_0
		AffineTransform trans_0_0_0_0_0 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, -450.0f, 0.0f ) );
		paintCompositeGraphicsNode_0_0_0_0_0( g );
		g.setTransform( trans_0_0_0_0_0 );
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

	private void paintCompositeGraphicsNode_0_0_1_1( Graphics2D g ) {
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
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, -299.0625f, -49.96875f ) );
		paintCompositeGraphicsNode_0_0_0( g );
		g.setTransform( trans_0_0_0 );
		// _0_0_1
		AffineTransform trans_0_0_1 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, -299.0625f, -49.96875f ) );
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
		return 42;
	}

	/**
	 * Returns the height of the bounding box of the original SVG image.
	 * 
	 * @return The height of the bounding box of the original SVG image.
	 */
	public int getOrigHeight() {
		return 49;
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
	public TrashFullNoPattern() {
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
