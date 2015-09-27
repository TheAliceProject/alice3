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
package org.alice.ide.clipboard.icons;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;

/**
 * This class has been automatically generated using svg2java
 * 
 */
public class ClipboardIcon implements javax.swing.Icon {

	private float origAlpha = 1.0f;

	//	private static java.awt.Paint new_LinearGradientPaint( Point2D start, Point2D end, float[] fractions, Color[] colors, CycleMethod cycleMethod, ColorSpaceType colorSpace, AffineTransform gradientTransform ) {
	//		assert cycleMethod == java.awt.MultipleGradientPaint.CycleMethod.NO_CYCLE : cycleMethod;
	//		assert colorSpace == java.awt.MultipleGradientPaint.ColorSpaceType.SRGB : colorSpace;
	//		return new java.awt.LinearGradientPaint( start, end, fractions, colors, cycleMethod, colorSpace, gradientTransform );
	//	}
	//
	//	private static java.awt.Paint new_RadialGradientPaint( Point2D center, float radius, Point2D focus, float[] fractions, Color[] colors, CycleMethod cycleMethod, ColorSpaceType colorSpace, AffineTransform gradientTransform ) {
	//		assert cycleMethod == java.awt.MultipleGradientPaint.CycleMethod.NO_CYCLE : cycleMethod;
	//		assert colorSpace == java.awt.MultipleGradientPaint.ColorSpaceType.SRGB : colorSpace;
	//		return new java.awt.RadialGradientPaint( center, radius, focus, fractions, colors, cycleMethod, colorSpace, gradientTransform );
	//	}

	private static java.awt.Paint new_LinearGradientPaint( Point2D start, Point2D end, float[] fractions, Color[] colors, AffineTransform gradientTransform ) {
		try {
			Class<?> cls = Class.forName( "java.awt.LinearGradientPaint" );
			Class<?> cycleMethodCls = Class.forName( "java.awt.MultipleGradientPaint$CycleMethod" );
			Class<?> colorSpaceTypeCls = Class.forName( "java.awt.MultipleGradientPaint$ColorSpaceType" );
			final Object NO_CYCLE = cycleMethodCls.getField( "NO_CYCLE" ).get( null );
			final Object SRGB = colorSpaceTypeCls.getField( "SRGB" ).get( null );
			java.lang.reflect.Constructor<?> cnstrctr = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.getConstructor( cls, Point2D.class, Point2D.class, float[].class, Color[].class, cycleMethodCls, colorSpaceTypeCls, AffineTransform.class );
			return (java.awt.Paint)cnstrctr.newInstance( start, end, fractions, colors, NO_CYCLE, SRGB, gradientTransform );
		} catch( Throwable t ) {
			//t.printStackTrace();
			return colors[ 0 ];
			//		return new java.awt.LinearGradientPaint( start, end, fractions, colors, java.awt.MultipleGradientPaint.CycleMethod.NO_CYCLE, java.awt.MultipleGradientPaint.ColorSpaceType.SRGB, gradientTransform );
		}
	}

	private static java.awt.Paint new_RadialGradientPaint( Point2D center, float radius, Point2D focus, float[] fractions, Color[] colors, AffineTransform gradientTransform ) {
		try {
			Class<?> cls = Class.forName( "java.awt.RadialGradientPaint" );
			Class<?> cycleMethodCls = Class.forName( "java.awt.MultipleGradientPaint$CycleMethod" );
			Class<?> colorSpaceTypeCls = Class.forName( "java.awt.MultipleGradientPaint$ColorSpaceType" );
			final Object NO_CYCLE = cycleMethodCls.getField( "NO_CYCLE" ).get( null );
			final Object SRGB = colorSpaceTypeCls.getField( "SRGB" ).get( null );
			java.lang.reflect.Constructor<?> cnstrctr = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.getConstructor( cls, Point2D.class, Float.TYPE, Point2D.class, float[].class, Color[].class, cycleMethodCls, colorSpaceTypeCls, AffineTransform.class );
			return (java.awt.Paint)cnstrctr.newInstance( center, radius, focus, fractions, colors, NO_CYCLE, SRGB, gradientTransform );
		} catch( Throwable t ) {
			//t.printStackTrace();
			return colors[ 0 ];
			//return new java.awt.RadialGradientPaint( center, radius, focus, fractions, colors, java.awt.MultipleGradientPaint.CycleMethod.NO_CYCLE, java.awt.MultipleGradientPaint.ColorSpaceType.SRGB, gradientTransform );
		}
	}

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

		Color boardColor = this.dragReceptorState.getBoardColor();
		g.setPaint( new_LinearGradientPaint( new Point2D.Double( 25.5, -13.625 ), new Point2D.Double( 26.0, -39.125 ), new float[] { 0.0f, 1.0f }, new Color[] { boardColor, new Color( 199, 155, 85, 255 ) },
				//MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, 
				new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 296.0f, -52.0f ) ) );

		g.fill( shape2 );
		g.setPaint( new_LinearGradientPaint( new Point2D.Double( 18.39735221862793, -37.160858154296875 ), new Point2D.Double( 10.831841468811035, 4.028111457824707 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 143, 89, 2, 255 ), new Color( 233, 185, 110, 255 ) },
				//MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, 
				new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 296.0f, -50.0f ) ) );
		g.setStroke( new BasicStroke( 1.0f, 0, 0, 4.0f, null, 0.0f ) );
		g.draw( shape2 );
	}

	private void paintShapeNode_0_0_2_0_0_3( Graphics2D g ) {
		RoundRectangle2D.Double shape3 = new RoundRectangle2D.Double( 306.5, -91.5, 28.00001335144043, 30.000003814697266, 3.0, 3.0 );
		g.setPaint( new_LinearGradientPaint( new Point2D.Double( 14.787761688232422, -9.017683982849121 ), new Point2D.Double( 14.787761688232422, -69.46895599365234 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 255, 255, 255, 255 ), new Color( 255, 255, 255, 0 ) },
				//MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, 
				new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 296.0f, -52.0f ) ) );
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
		shape4.moveTo( 142.93753f, 60.5f );
		shape4.lineTo( 164.068f, 60.5f );
		shape4.curveTo( 164.86101f, 60.5f, 165.49942f, 61.138416f, 165.49942f, 61.931427f );
		shape4.lineTo( 165.49973f, 82.5f );
		shape4.curveTo( 165.49973f, 82.5f, 160.49973f, 87.5f, 160.49973f, 87.5f );
		shape4.lineTo( 142.93753f, 87.5057f );
		shape4.curveTo( 142.14452f, 87.5057f, 141.5061f, 86.86729f, 141.5061f, 86.07427f );
		shape4.lineTo( 141.5061f, 61.931465f );
		shape4.curveTo( 141.5061f, 61.138454f, 142.14452f, 60.50004f, 142.93753f, 60.50004f );
		shape4.closePath();
		g.setPaint( new Color( 0, 0, 0, 255 ) );
		g.fill( shape4 );
		g.setStroke( new BasicStroke( 1.0000001f, 0, 0, 4.0f, null, 0.0f ) );
		g.draw( shape4 );
	}

	private void paintShapeNode_0_0_2_0_4_0( Graphics2D g ) {
		GeneralPath shape5 = new GeneralPath();
		shape5.moveTo( -34.29474f, 4.03866f );
		shape5.lineTo( -0.3885193f, 4.03866f );
		shape5.curveTo( 0.88395476f, 4.03866f, 1.9083652f, 4.9814334f, 1.9083652f, 6.1525016f );
		shape5.lineTo( 1.9088448f, 36.526886f );
		shape5.curveTo( 1.9088448f, 36.526886f, -6.114217f, 43.910576f, -6.114217f, 43.910576f );
		shape5.lineTo( -34.29474f, 43.918976f );
		shape5.curveTo( -35.56721f, 43.918976f, -36.59162f, 42.976204f, -36.59162f, 41.805134f );
		shape5.lineTo( -36.59162f, 6.152546f );
		shape5.curveTo( -36.59162f, 4.9814777f, -35.56721f, 4.0387044f, -34.29474f, 4.0387044f );
		shape5.closePath();
		Color paperColor = this.dragReceptorState.getPaperColor();
		g.setPaint( new_RadialGradientPaint( new Point2D.Double( -117.93485260009766, 5.198304176330566 ), 18.000002f, new Point2D.Double( -117.93485260009766, 5.198304176330566 ), new float[] { 0.0f, 1.0f }, new Color[] { paperColor, new Color( 211, 215, 207, 255 ) },
				//MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, 
				new AffineTransform( 3.19461989402771f, 0.0f, 0.0f, 1.5470696687698364f, 365.3152770996094f, 23.795835494995117f ) ) );
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
		shape6.moveTo( -34.29474f, 4.03866f );
		shape6.lineTo( -0.3885193f, 4.03866f );
		shape6.curveTo( 0.88395476f, 4.03866f, 1.9083652f, 4.9814334f, 1.9083652f, 6.1525016f );
		shape6.lineTo( 1.9088448f, 36.526886f );
		shape6.curveTo( 1.9088448f, 36.526886f, -6.114217f, 43.910576f, -6.114217f, 43.910576f );
		shape6.lineTo( -34.29474f, 43.918976f );
		shape6.curveTo( -35.56721f, 43.918976f, -36.59162f, 42.976204f, -36.59162f, 41.805134f );
		shape6.lineTo( -36.59162f, 6.152546f );
		shape6.curveTo( -36.59162f, 4.9814777f, -35.56721f, 4.0387044f, -34.29474f, 4.0387044f );
		shape6.closePath();
		g.setPaint( new_LinearGradientPaint( new Point2D.Double( -367.9520568847656, 16.063671112060547 ), new Point2D.Double( -393.3939208984375, -46.69970703125 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 85, 87, 83, 255 ), new Color( 186, 189, 182, 255 ) },
				//MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, 
				new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 356.0f, 50.0f ) ) );
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
		shape7.moveTo( 331.5f, 84.5f );
		shape7.lineTo( 326.5f, 84.5f );
		shape7.lineTo( 326.5f, 89.5f );
		shape7.lineTo( 331.5f, 84.5f );
		shape7.closePath();
		g.setPaint( new Color( 0, 0, 0, 255 ) );
		g.fill( shape7 );
	}

	private void paintShapeNode_0_0_2_0_7( Graphics2D g ) {
		GeneralPath shape8 = new GeneralPath();
		shape8.moveTo( 165.49973f, 81.5f );
		shape8.lineTo( 160.49973f, 81.5f );
		shape8.lineTo( 160.49973f, 86.5f );
		shape8.lineTo( 165.49973f, 81.5f );
		shape8.closePath();
		g.setPaint( new_RadialGradientPaint( new Point2D.Double( 328.5484619140625, 85.5484619140625 ), 3.0f, new Point2D.Double( 328.5484619140625, 85.5484619140625 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 255, 255, 255, 255 ), new Color( 136, 138, 133, 255 ) },
				//MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, 
				new AffineTransform( 0.6394925117492676f, 0.0f, 0.0f, 0.6394924521446228f, -48.60456085205078f, 27.792404174804688f ) ) );
		g.fill( shape8 );
		g.setPaint( new_RadialGradientPaint( new Point2D.Double( 327.53125, 84.5 ), 3.0f, new Point2D.Double( 327.53125, 84.5 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 211, 215, 207, 255 ), new Color( 85, 87, 83, 255 ) },
				//MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, 
				new AffineTransform( 1.9562286138534546f, 0.0f, 0.0f, 1.9562286138534546f, -480.1950378417969f, -83.80131530761719f ) ) );
		g.setStroke( new BasicStroke( 1.0f, 0, 1, 4.0f, null, 0.0f ) );
		g.draw( shape8 );
	}

	private void paintShapeNode_0_0_2_0_8( Graphics2D g ) {
		GeneralPath shape9 = new GeneralPath();
		shape9.moveTo( 143.07256f, 60.5f );
		shape9.lineTo( 163.92691f, 60.5f );
		shape9.curveTo( 164.24425f, 60.5f, 164.49973f, 60.755478f, 164.49973f, 61.07282f );
		shape9.lineTo( 164.49973f, 81.0f );
		shape9.curveTo( 164.49973f, 81.0f, 159.99973f, 85.5f, 159.99973f, 85.5f );
		shape9.lineTo( 143.07254f, 85.5f );
		shape9.curveTo( 142.7552f, 85.5f, 142.49973f, 85.24452f, 142.49973f, 84.927185f );
		shape9.lineTo( 142.49973f, 61.07282f );
		shape9.curveTo( 142.49973f, 60.755478f, 142.7552f, 60.5f, 143.07254f, 60.5f );
		shape9.closePath();
		g.setPaint( new_LinearGradientPaint( new Point2D.Double( 325.5882263183594, 82.02571105957031 ), new Point2D.Double( 333.8441162109375, 90.2815933227539 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 255, 255, 255, 255 ), new Color( 255, 255, 255, 0 ) },
				//MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, 
				new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, -167.00027465820312f, -3.0f ) ) );
		g.setStroke( new BasicStroke( 0.99999946f, 0, 0, 4.0f, null, 0.0f ) );
		g.draw( shape9 );
	}

	private void paintShapeNode_0_0_2_0_9_0( Graphics2D g ) {
		GeneralPath shape10 = new GeneralPath();
		shape10.moveTo( 16.722015f, 14.506832f );
		shape10.lineTo( 32.354477f, 14.506832f );
		shape10.curveTo( 33.326748f, 14.551023f, 33.381283f, 12.617748f, 33.381283f, 12.617748f );
		shape10.lineTo( 30.497072f, 10.307271f );
		shape10.lineTo( 30.506172f, 9.552748f );
		shape10.curveTo( 30.506172f, 9.552748f, 18.491043f, 9.532198f, 18.491043f, 9.532198f );
		shape10.lineTo( 18.491043f, 10.4223995f );
		shape10.lineTo( 15.890176f, 12.665036f );
		shape10.curveTo( 15.890176f, 12.665036f, 15.838146f, 14.462638f, 16.722025f, 14.506832f );
		shape10.closePath();
		g.setPaint( Color.BLACK );
		g.fill( shape10 );
	}

	private void paintShapeNode_0_0_2_0_9_1( Graphics2D g ) {
		GeneralPath shape11 = new GeneralPath();
		shape11.moveTo( 312.72202f, 64.50683f );
		shape11.lineTo( 328.3545f, 64.50683f );
		shape11.curveTo( 329.32675f, 64.55102f, 329.3813f, 62.617744f, 329.3813f, 62.617744f );
		shape11.lineTo( 326.49707f, 60.307266f );
		shape11.lineTo( 326.50607f, 56.552742f );
		shape11.curveTo( 326.50607f, 55.73013f, 325.81427f, 54.619465f, 324.80728f, 54.619465f );
		shape11.lineTo( 316.28723f, 54.531075f );
		shape11.curveTo( 315.0748f, 54.531075f, 314.49094f, 55.719227f, 314.49094f, 56.532192f );
		shape11.lineTo( 314.49094f, 60.422394f );
		shape11.lineTo( 311.89008f, 62.66503f );
		shape11.curveTo( 311.89008f, 62.66503f, 311.83807f, 64.46263f, 312.72192f, 64.50683f );
		shape11.closePath();
		g.setPaint( new_LinearGradientPaint( new Point2D.Double( 24.635435104370117, 3.519411563873291 ), new Point2D.Double( 24.635435104370117, 11.540999412536621 ), new float[] { 0.0f, 0.13349205f, 0.53102833f, 0.78739f, 1.0f }, new Color[] { new Color( 186, 189, 182, 255 ), new Color( 238, 238, 236, 255 ), new Color( 186, 189, 182, 255 ), new Color( 255, 255, 255, 255 ), new Color( 156, 152, 138, 255 ) },
				//MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, 
				new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 296.0f, 52.0f ) ) );
		g.fill( shape11 );
		g.setPaint( new_LinearGradientPaint( new Point2D.Double( 32.91161346435547, 16.214149475097656 ), new Point2D.Double( 31.417892456054688, 4.031081199645996 ), new float[] { 0.0f, 1.0f }, new Color[] { new Color( 85, 87, 83, 255 ), new Color( 186, 189, 182, 255 ) },
				//MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, 
				new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 296.0f, 50.0f ) ) );
		g.setStroke( new BasicStroke( 1.0f, 0, 0, 4.0f, null, 0.0f ) );
		g.draw( shape11 );
	}

	private void paintShapeNode_0_0_2_0_9_2( Graphics2D g ) {
		RoundRectangle2D.Double shape12 = new RoundRectangle2D.Double( 313.0, 62.0, 15.0, 1.0416321754455566, 1.0416321754455566, 1.0416321754455566 );
		g.setPaint( Color.WHITE );
		g.fill( shape12 );
	}

	private void paintShapeNode_0_0_2_0_9_3( Graphics2D g ) {
		GeneralPath shape13 = new GeneralPath();
		shape13.moveTo( 316.0f, 60.0f );
		shape13.lineTo( 316.0f, 57.0f );
		shape13.curveTo( 315.9606f, 56.368443f, 316.20798f, 55.966385f, 317.0f, 56.0f );
		shape13.lineTo( 324.0f, 56.0f );
		shape13.curveTo( 324.46307f, 56.07386f, 324.94202f, 56.11598f, 325.0f, 57.0f );
		shape13.lineTo( 325.0f, 60.0f );
		shape13.lineTo( 324.0f, 57.0f );
		shape13.lineTo( 317.0f, 57.0f );
		shape13.lineTo( 316.0f, 60.0f );
		shape13.closePath();
		g.setPaint( new_LinearGradientPaint( new Point2D.Double( 24.49800682067871, 3.9980428218841553 ), new Point2D.Double( 24.49800682067871, 8.0 ), new float[] { 0.0f, 1.0f }, new Color[] { Color.WHITE, new Color( 255, 255, 255, 0 ) },
				//MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, 
				new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 296.0f, 52.0f ) ) );
		g.fill( shape13 );
	}

	private void paintShapeNode_0_0_2_0_9_4( Graphics2D g ) {
		GeneralPath shape14 = new GeneralPath();
		shape14.moveTo( 316.28125f, 55.40625f );
		shape14.curveTo( 315.96063f, 55.40625f, 315.79126f, 55.524544f, 315.625f, 55.75f );
		shape14.curveTo( 315.45874f, 55.975456f, 315.375f, 56.32886f, 315.375f, 56.53125f );
		shape14.lineTo( 315.375f, 60.4375f );
		shape14.curveTo( 315.37088f, 60.691208f, 315.25687f, 60.930645f, 315.0625f, 61.09375f );
		shape14.lineTo( 312.78125f, 63.03125f );
		shape14.curveTo( 312.79025f, 63.164944f, 312.77625f, 63.212463f, 312.81244f, 63.375f );
		shape14.curveTo( 312.84085f, 63.503906f, 312.88025f, 63.571346f, 312.90613f, 63.625f );
		shape14.lineTo( 328.24988f, 63.625f );
		shape14.curveTo( 328.2776f, 63.58374f, 328.3303f, 63.499012f, 328.37488f, 63.34375f );
		shape14.curveTo( 328.41977f, 63.18762f, 328.41977f, 63.136784f, 328.43738f, 63.0f );
		shape14.lineTo( 325.93738f, 61.0f );
		shape14.curveTo( 325.73462f, 60.82988f, 325.61972f, 60.57713f, 325.62488f, 60.3125f );
		shape14.lineTo( 325.62488f, 56.5625f );
		shape14.curveTo( 325.62488f, 56.392494f, 325.51932f, 56.03895f, 325.34363f, 55.8125f );
		shape14.curveTo( 325.16806f, 55.58605f, 324.99304f, 55.5f, 324.8125f, 55.5f );
		shape14.lineTo( 316.28125f, 55.40625f );
		shape14.closePath();
		g.setPaint( Color.WHITE );
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
		if( this.isFull || ( this.dragReceptorState == org.alice.ide.clipboard.DragReceptorState.ENTERED ) ) {
			// _0_0_2_0_1
			g.setComposite( AlphaComposite.getInstance( 3, 0.2f * origAlpha ) );
			AffineTransform trans_0_0_2_0_1 = g.getTransform();
			g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f ) );
			paintShapeNode_0_0_2_0_1( g );
			g.setTransform( trans_0_0_2_0_1 );
			// _0_0_2_0_2
			g.setComposite( AlphaComposite.getInstance( 3, 1.0f * origAlpha ) );
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
		}
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

	private void paintCanvasGraphicsNode_0_0( Graphics2D g ) {
		// _0_0_2
		AffineTransform trans_0_0_2 = g.getTransform();
		g.transform( new AffineTransform( 1.0f, 0.0f, 0.0f, 1.0f, -128.79701232910156f, -51.03125f ) );
		paintCompositeGraphicsNode_0_0_2( g );
		g.setTransform( trans_0_0_2 );
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

	private boolean isFull;

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
	public ClipboardIcon() {
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

	public org.alice.ide.clipboard.DragReceptorState getDragReceptorState() {
		return this.dragReceptorState;
	}

	public void setDragReceptorState( org.alice.ide.clipboard.DragReceptorState dragReceptorState ) {
		this.dragReceptorState = dragReceptorState;
	}

	public boolean isFull() {
		return this.isFull;
	}

	public void setFull( boolean isFull ) {
		this.isFull = isFull;
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
