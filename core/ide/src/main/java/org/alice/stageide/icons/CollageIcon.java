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
package org.alice.stageide.icons;

/**
 * @author Dennis Cosgrove
 */
public abstract class CollageIcon extends ShapeIcon {
	private static final int N = 5;

	private static final double[] xs;
	private static final double[] ys;

	static {
		xs = new double[ N ];
		double xDelta = 0.15;
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

	public CollageIcon( java.awt.Dimension size, java.util.List<? extends org.lgna.croquet.icon.AbstractSingleSourceImageIconFactory> iconFactories ) {
		super( size );
		if( size.width > 64 ) {
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
		} else {
			this.drawOrder = new int[] {};
		}
	}

	private static final double ROUND = 10;

	protected abstract java.awt.Shape createBackShape( double width, double height );

	protected abstract java.awt.Shape createFrontShape( double width, double height );

	protected double getX( int i ) {
		return xs[ i ];
	}

	protected double getY( int i ) {
		return ys[ i ];
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

		try {
			g2.translate( dx, dy );
			g2.translate( 0, h );
			g2.shear( 0.1, 0.0 );
			g2.translate( 0, -h );
			java.awt.Shape backShape = this.createBackShape( w, h );
			java.awt.Shape frontShape = this.createFrontShape( w, h );
			if( backShape != null ) {
				java.awt.Paint backFillPaint = new java.awt.GradientPaint( 0, 0, java.awt.Color.WHITE, width, height, new java.awt.Color( 191, 191, 127 ) );
				g2.setPaint( backFillPaint );
				g2.fill( backShape );
				g2.setPaint( drawPaint );
				g2.draw( backShape );
			}

			g2.setTransform( t );

			if( this.drawOrder.length > 0 ) {
				int totalAvailableArea = width * height;
				int totalIconArea = 0;
				for( int i : this.drawOrder ) {
					javax.swing.Icon icon = this.icons[ i ];
					int iconIArea = icon.getIconWidth() * icon.getIconHeight();
					totalIconArea += iconIArea;
				}
				//edu.cmu.cs.dennisc.java.util.logging.Logger.outln( totalAvailableArea, totalIconArea );
				double scale = 0.5;
				for( int i : this.drawOrder ) {
					javax.swing.Icon icon = this.icons[ i ];
					int x = (int)( this.getX( i ) * width );
					int y = (int)( this.getY( i ) * height );

					if( icon instanceof javax.swing.ImageIcon ) {
						javax.swing.ImageIcon imageIcon = (javax.swing.ImageIcon)icon;
						int imageWidth = imageIcon.getIconWidth();
						int imageHeight = imageIcon.getIconHeight();

						int dstWidth = (int)( imageWidth * scale );
						int dstHeight = (int)( imageHeight * scale );

						g2.drawImage( imageIcon.getImage(), x, y, ( x + dstWidth ) - 1, ( y + (int)dstHeight ) - 1, 0, 0, imageWidth - 1, imageHeight - 1, c );
					} else {
						icon.paintIcon( c, g2, x, y );
					}
				}
			}

			if( frontShape != null ) {
				g2.translate( dx, dy );
				g2.translate( 0, h );
				g2.shear( -0.4, 0.0 );
				g2.translate( 0, -h );
				java.awt.Paint frontFillPaint = new java.awt.GradientPaint( 0, height / 2, new java.awt.Color( 255, 255, 255, 255 ), width, height, new java.awt.Color( 221, 221, 127, 127 ) );
				g2.setPaint( frontFillPaint );
				g2.fill( frontShape );
				g2.setPaint( drawPaint );
				g2.draw( frontShape );
			}
		} finally {
			g2.setTransform( t );
			g2.setStroke( prevStroke );
		}
	}

	private final javax.swing.Icon[] icons = { null, null, null, null, null };
	private final int[] drawOrder;
}
