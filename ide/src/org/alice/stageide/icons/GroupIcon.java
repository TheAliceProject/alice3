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
package org.alice.stageide.icons;

/**
 * @author Dennis Cosgrove
 */
public class GroupIcon extends ShapeIcon {
	private static final int N = 5;

	private static final double[] xs;
	private static final double[] ys;

	static {
		xs = new double[ N ];
		double xDelta = 0.2;
		double x = 0.0;
		for( int i = 0; i < N; i++ ) {
			xs[ i ] = x;
			x += xDelta;
		}
		double y0 = 0.0;
		double yDelta = 0.1;
		ys = new double[] {
				y0 + ( yDelta * 2 ),
				y0 + ( yDelta * 1 ),
				y0 + ( yDelta * 0 ),
				y0 + ( yDelta * 1 ),
				y0 + ( yDelta * 2 ),
		};
	}

	private final javax.swing.Icon[] icons = { null, null, null, null, null };
	private final int[] drawOrder;

	public GroupIcon( java.awt.Dimension size, java.util.List<? extends org.lgna.croquet.icon.AbstractImageIconFactory> iconFactories ) {
		super( size );
		int subWidth = ( 2 * size.width ) / 3;
		int subHeight = ( 2 * size.height ) / 3;
		java.awt.Dimension subSize = new java.awt.Dimension( subWidth, subHeight );
		for( int i = 0; i < iconFactories.size(); i++ ) {
			this.icons[ i ] = iconFactories.get( i ).getSourceImageIcon();
		}
		switch( iconFactories.size() ) {
		case 0:
			this.drawOrder = new int[] {};
			break;
		case 1:
			this.drawOrder = new int[] { 2 };
			this.icons[ 2 ] = this.icons[ 0 ];
			break;
		case 2:
			this.drawOrder = new int[] { 1, 3 };
			this.icons[ 3 ] = this.icons[ 1 ];
			this.icons[ 1 ] = this.icons[ 0 ];
			break;
		case 3:
			this.drawOrder = new int[] { 0, 4, 2 };
			this.icons[ 4 ] = this.icons[ 2 ];
			this.icons[ 2 ] = this.icons[ 1 ];
			break;
		case 4:
			this.drawOrder = new int[] { 0, 4, 1, 3 };
			this.icons[ 4 ] = this.icons[ 3 ];
			this.icons[ 3 ] = this.icons[ 2 ];
			break;
		default:
			this.drawOrder = new int[] { 0, 4, 1, 3, 2 };
		}
	}

	private static final double ROUND = 10;

	private static java.awt.Shape createBackShape( double width, double height ) {
		java.awt.geom.RoundRectangle2D a = new java.awt.geom.RoundRectangle2D.Double( 0, 0, width * 0.4, height, ROUND, ROUND );
		java.awt.geom.RoundRectangle2D b = new java.awt.geom.RoundRectangle2D.Double( 0, height * 0.1, width, height * 0.9, ROUND, ROUND );
		return edu.cmu.cs.dennisc.java.awt.geom.AreaUtilities.createUnion( a, b );
	}

	private static java.awt.Shape createFrontShape( double width, double height ) {
		return new java.awt.geom.RoundRectangle2D.Double( 0, height * 0.5, width, height * 0.5, ROUND, ROUND );
	}

	@Override
	protected void paintIcon( java.awt.Component c, java.awt.Graphics2D g2, int width, int height, java.awt.Paint fillPaint, java.awt.Paint drawPaint ) {
		//g2.setPaint( java.awt.Color.red );
		//g2.fillRect( 0, 0, width, height );
		java.awt.Stroke prevStroke = g2.getStroke();
		double dx = width * 0.15;
		double dy = height * 0.05;
		java.awt.geom.AffineTransform t = g2.getTransform();
		double w = width - ( dx * 2 );
		double h = height - ( dy * 2 );

		java.awt.Paint backFillPaint = new java.awt.GradientPaint( 0, 0, java.awt.Color.WHITE, width, height, new java.awt.Color( 191, 191, 127 ) );
		java.awt.Paint frontFillPaint = new java.awt.GradientPaint( 0, height / 2, new java.awt.Color( 255, 255, 255, 255 ), width, height, new java.awt.Color( 221, 221, 127, 127 ) );
		try {
			g2.translate( dx, dy );
			g2.translate( 0, h );
			g2.shear( 0.1, 0.0 );
			g2.translate( 0, -h );
			java.awt.Shape backShape = createBackShape( w, h );
			java.awt.Shape frontShape = createFrontShape( w, h );
			g2.setPaint( backFillPaint );
			g2.fill( backShape );
			g2.setPaint( drawPaint );
			g2.draw( backShape );

			g2.setTransform( t );

			for( int i : this.drawOrder ) {
				javax.swing.Icon icon = this.icons[ i ];
				int x = (int)( xs[ i ] * width );
				int y = (int)( ys[ i ] * height );

				if( icon instanceof javax.swing.ImageIcon ) {
					javax.swing.ImageIcon imageIcon = (javax.swing.ImageIcon)icon;
					int imageWidth = imageIcon.getIconWidth();
					int imageHeight = imageIcon.getIconHeight();

					double scale = ( width * 0.3 ) / imageWidth;
					int dstWidth = (int)( imageWidth * scale );
					int dstHeight = (int)( imageHeight * scale );

					g2.drawImage( imageIcon.getImage(), x, y, ( x + dstWidth ) - 1, ( y + (int)dstHeight ) - 1, 0, 0, imageWidth - 1, imageHeight - 1, c );
				} else {
					icon.paintIcon( c, g2, x, y );
				}
			}

			g2.translate( dx, dy );
			g2.translate( 0, h );
			g2.shear( -0.4, 0.0 );
			g2.translate( 0, -h );
			g2.setPaint( frontFillPaint );
			g2.fill( frontShape );
			g2.setPaint( drawPaint );
			g2.draw( frontShape );
		} finally {
			g2.setTransform( t );
			g2.setStroke( prevStroke );
		}
	}
}
