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
package org.alice.stageide.personresource.views;

/**
 * @author Dennis Cosgrove
 */
public abstract class JColorSlider extends javax.swing.JComponent {
	private static int HALF_ARROW_WIDTH = 3;
	private static int ARROW_HEIGHT = 4;

	private final java.awt.Color[] colors;
	private float portion = 0.5f;

	private final float[][] hsbBuffers = new float[ 6 ][ 3 ];
	private final float minHue;
	private final float maxHue;

	public JColorSlider( java.awt.Color[] colors ) {
		this.colors = colors;
		int i = 0;
		float min = Float.MAX_VALUE;
		float max = -Float.MAX_VALUE;
		for( java.awt.Color color : colors ) {
			java.awt.Color.RGBtoHSB( color.getRed(), color.getGreen(), color.getBlue(), hsbBuffers[ i ] );
			min = Math.min( min, hsbBuffers[ i ][ 0 ] );
			max = Math.max( max, hsbBuffers[ i ][ 0 ] );
			i++;
		}
		minHue = min;
		maxHue = max;
	}

	private final java.awt.event.MouseListener mouseListener = new java.awt.event.MouseListener() {
		@Override
		public void mousePressed( java.awt.event.MouseEvent e ) {
			setPortion( e );
		}

		@Override
		public void mouseReleased( java.awt.event.MouseEvent e ) {
			setPortion( e );
		}

		@Override
		public void mouseEntered( java.awt.event.MouseEvent e ) {
		}

		@Override
		public void mouseExited( java.awt.event.MouseEvent e ) {
		}

		@Override
		public void mouseClicked( java.awt.event.MouseEvent e ) {
		}
	};

	private final java.awt.event.MouseMotionListener mouseMotionListener = new java.awt.event.MouseMotionListener() {
		@Override
		public void mouseMoved( java.awt.event.MouseEvent e ) {
		}

		@Override
		public void mouseDragged( java.awt.event.MouseEvent e ) {
			setPortion( e );
		}
	};

	private void setPortion( java.awt.event.MouseEvent e ) {
		int xA = HALF_ARROW_WIDTH;
		int xF = this.getWidth() - HALF_ARROW_WIDTH;
		int w = xF - xA;
		this.portion = ( e.getX() - xA ) / (float)w;
		this.portion = Math.min( this.portion, 1.0f );
		this.portion = Math.max( this.portion, 0.0f );
		this.repaint();

		int index = (int)( this.portion / 0.2 );
		java.awt.Color nextColor;
		if( index < 0 ) {
			nextColor = colors[ 0 ];
		} else if( index >= ( colors.length - 1 ) ) {
			nextColor = colors[ colors.length - 1 ];
		} else {
			float interp = this.portion % 0.2f;
			interp *= 5.0;

			float[] hsbBuffer0 = hsbBuffers[ index ];
			float[] hsbBuffer1 = hsbBuffers[ index + 1 ];
			float[] hsbBuffer = new float[ 3 ];
			for( int i = 0; i < 3; i++ ) {
				hsbBuffer[ i ] = ( hsbBuffer0[ i ] * ( 1 - interp ) ) + ( hsbBuffer1[ i ] * interp );
			}
			nextColor = java.awt.Color.getHSBColor( hsbBuffer[ 0 ], hsbBuffer[ 1 ], hsbBuffer[ 2 ] );
		}
		this.handleNextColor( nextColor );
	}

	protected abstract void handleNextColor( java.awt.Color nextColor );

	@Override
	public void addNotify() {
		super.addNotify();
		this.addMouseListener( this.mouseListener );
		this.addMouseMotionListener( this.mouseMotionListener );
	}

	@Override
	public void removeNotify() {
		this.removeMouseMotionListener( this.mouseMotionListener );
		this.removeMouseListener( this.mouseListener );
		super.removeNotify();
	}

	@Override
	public java.awt.Dimension getMinimumSize() {
		java.awt.Insets insets = this.getInsets();
		return new java.awt.Dimension( insets.left + insets.right + HALF_ARROW_WIDTH + HALF_ARROW_WIDTH + 32, insets.top + insets.bottom + ARROW_HEIGHT + ARROW_HEIGHT + 16 );
	}

	@Override
	protected void paintComponent( java.awt.Graphics g ) {
		super.paintComponent( g );

		java.awt.Insets insets = this.getInsets();
		g.translate( insets.left, insets.top );
		int width = this.getWidth() - insets.left - insets.right;

		int xA = HALF_ARROW_WIDTH;
		int xF = width - HALF_ARROW_WIDTH;

		int w = xF - xA;

		int xB = (int)( xA + ( w * 0.2 ) );
		int xC = (int)( xA + ( w * 0.4 ) );
		int xD = (int)( xA + ( w * 0.6 ) );
		int xE = (int)( xA + ( w * 0.8 ) );

		int height = this.getHeight() - insets.top - insets.bottom;
		int y0 = ARROW_HEIGHT;
		int y1 = height - ARROW_HEIGHT;

		int h = y1 - y0;

		java.awt.GradientPaint abPaint = new java.awt.GradientPaint( xA, y0, colors[ 0 ], xB, y0, colors[ 1 ] );
		java.awt.GradientPaint bcPaint = new java.awt.GradientPaint( xB, y0, colors[ 1 ], xC, y0, colors[ 2 ] );
		java.awt.GradientPaint cdPaint = new java.awt.GradientPaint( xC, y0, colors[ 2 ], xD, y0, colors[ 3 ] );
		java.awt.GradientPaint dePaint = new java.awt.GradientPaint( xD, y0, colors[ 3 ], xE, y0, colors[ 4 ] );
		java.awt.GradientPaint efPaint = new java.awt.GradientPaint( xE, y0, colors[ 4 ], xF, y0, colors[ 5 ] );

		java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;

		g2.setPaint( abPaint );
		g2.fillRect( xA, y0, xB - xA, h );
		g2.setPaint( bcPaint );
		g2.fillRect( xB, y0, xC - xB, h );
		g2.setPaint( cdPaint );
		g2.fillRect( xC, y0, xD - xC, h );
		g2.setPaint( dePaint );
		g2.fillRect( xD, y0, xE - xD, h );
		g2.setPaint( efPaint );
		g2.fillRect( xE, y0, xF - xE, h );

		g2.setColor( java.awt.Color.BLACK );

		double x = portion * w;
		edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.drawTriangle( g2, edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.Heading.SOUTH, (int)x, 0, HALF_ARROW_WIDTH * 2, ARROW_HEIGHT );
		edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.drawTriangle( g2, edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.Heading.NORTH, (int)x, y1, HALF_ARROW_WIDTH * 2, ARROW_HEIGHT );

		g.translate( -insets.left, -insets.top );
	}
}
