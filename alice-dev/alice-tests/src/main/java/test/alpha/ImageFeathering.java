/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
 */
package test.alpha;

import edu.cmu.cs.dennisc.image.ImageUtilities;

/**
 * @author Dennis Cosgrove
 */
public class ImageFeathering {
	private static final java.awt.Stroke[] HIGHLIGHT_STROKES;
	static {
		final int N = 16;
		HIGHLIGHT_STROKES = new java.awt.Stroke[ N ];
		for( int i = 0; i < N; i++ ) {
			HIGHLIGHT_STROKES[ i ] = new java.awt.BasicStroke( ( i + 1 ) * 4.0f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND );
		}
	};

	public static void main( String[] args ) throws Exception {
		javax.swing.JFrame frame = new javax.swing.JFrame();
		java.awt.Image image = ImageUtilities.read( new java.net.URL( "http://www.alice.org/banner/frontpage_noflash.jpg" ) );
		java.awt.MediaTracker mediaTracker = new java.awt.MediaTracker( frame );
		mediaTracker.addImage( image, 0 );
		mediaTracker.waitForAll();
		final java.awt.image.BufferedImage alphaMaskedImage = ImageUtilities.createAlphaMaskedImage( image, new edu.cmu.cs.dennisc.java.awt.Painter<Void>() {
			@Override
			public void paint( java.awt.Graphics2D g2, Void value, int width, int height ) {
				g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
				final boolean IS_GRADIENT_DESIRED = true;
				if( IS_GRADIENT_DESIRED ) {
					final boolean IS_RADIAL_GRADIENT_DESIRED = true;
					float fractions[] = { 0.0f, 1.0f };
					java.awt.Color[] colors = { java.awt.Color.WHITE, new java.awt.Color( 255, 255, 255, 0 ) };
					java.awt.Paint paint;
					if( IS_RADIAL_GRADIENT_DESIRED ) {
						java.awt.geom.Point2D center = new java.awt.geom.Point2D.Float( width * 0.5f, height * 0.5f );
						float radius = height * 0.75f;
						final boolean IS_SCALE_DESIRED = true;
						if( IS_SCALE_DESIRED ) {
							java.awt.geom.Point2D origin = new java.awt.geom.Point2D.Float( 0, 0 );
							java.awt.geom.AffineTransform m = new java.awt.geom.AffineTransform();
							m.translate( center.getX(), center.getY() );
							m.scale( width / (double)height, 1 );
							paint = new java.awt.RadialGradientPaint( origin, radius, origin, fractions, colors, java.awt.MultipleGradientPaint.CycleMethod.NO_CYCLE, java.awt.MultipleGradientPaint.ColorSpaceType.SRGB, m );
						} else {
							paint = new java.awt.RadialGradientPaint( center, radius, fractions, colors, java.awt.MultipleGradientPaint.CycleMethod.NO_CYCLE );
						}
					} else {
						java.awt.geom.Point2D p0 = new java.awt.geom.Point2D.Float( width * 0.5f, height );
						java.awt.geom.Point2D p1 = new java.awt.geom.Point2D.Float( width, height );
						paint = new java.awt.LinearGradientPaint( p0, p1, fractions, colors, java.awt.MultipleGradientPaint.CycleMethod.NO_CYCLE );
					}
					g2.setPaint( paint );
					g2.fillRect( 0, 0, width, height );
				} else {
					final int INDENT = 32;
					final int ARC = 32;
					java.awt.Shape shape = new java.awt.geom.RoundRectangle2D.Float( INDENT, INDENT, width - INDENT - INDENT, height - INDENT - INDENT, ARC, ARC );
					g2.setPaint( java.awt.Color.WHITE );
					g2.fill( shape );

					int alpha = 255;
					for( java.awt.Stroke stroke : HIGHLIGHT_STROKES ) {
						g2.setPaint( new java.awt.Color( 255, 255, 255, alpha ) );
						alpha -= 15;
						g2.setStroke( stroke );
						g2.draw( shape );
					}
				}
			}
		} );
		frame.getContentPane().add( new javax.swing.JComponent() {
			@Override
			protected void paintComponent( java.awt.Graphics g ) {
				//g.setColor( java.awt.Color.RED );
				//g.fillRect( 0, 0, this.getWidth(), this.getHeight() );
				g.drawImage( alphaMaskedImage, 0, 0, this );
			}

			@Override
			public java.awt.Dimension getPreferredSize() {
				return new java.awt.Dimension( alphaMaskedImage.getWidth(), alphaMaskedImage.getHeight() );
			}
		} );
		frame.pack();
		frame.setVisible( true );
		frame.setDefaultCloseOperation( javax.swing.JFrame.EXIT_ON_CLOSE );
	}
}
